package com.example.androidautoclick.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.auto.assist.accessibility.util.ApiUtil
import com.example.androidautoclick.R
import com.example.androidautoclick.ui.ScreenListener.ScreenStateListener
import com.example.androidautoclick.ui.script.AnXinLiveRoomAutomaticLikesScript
import com.example.androidautoclick.ui.uitils.CommonPreferencesUtil
import com.example.androidautoclick.ui.uitils.DensityUtils
import com.example.androidautoclick.ui.uitils.Utils
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.interfaces.OnTouchRangeListener
import com.lzf.easyfloat.permission.PermissionUtils
import com.lzf.easyfloat.utils.DragUtils
import com.lzf.easyfloat.widget.BaseSwitchView
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_main.accessibilityServiceStatus
import kotlinx.android.synthetic.main.activity_main.actionDouyin
import kotlinx.android.synthetic.main.activity_main.ivBackground
import kotlinx.android.synthetic.main.activity_main.llAccessibilityServiceStatus
import kotlinx.android.synthetic.main.activity_main.titleBar
import kotlinx.android.synthetic.main.activity_main.tvCurrentDesc


class MainActivity : AppCompatActivity() {
    private lateinit var vibrator: Vibrator
    private var vibrating = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        Utils.getAllApps(this)
        initView()
        setListener()
        AnXinLiveRoomAutomaticLikesScript.hostName =
            CommonPreferencesUtil.getString(
                EditSettingsActivity.HOST_NAME_KEY,
                AnXinLiveRoomAutomaticLikesScript.defaultHostName
            )
        AnXinLiveRoomAutomaticLikesScript.mustConditions =
            CommonPreferencesUtil.getString(
                EditSettingsActivity.MUST_KEY,
                AnXinLiveRoomAutomaticLikesScript.defaultMustConditions
            )

    }


    private fun initView() {
        Blurry.with(ivBackground.context)
            .sampling(3)
            .animate(500)
            .from(BitmapFactory.decodeResource(resources, R.drawable.img_anxin_shu))
            .into(ivBackground)
    }

    private fun setListener() {
        titleBar.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onRightClick(titleBar: TitleBar?) {
                startActivity(Intent(this@MainActivity, EditSettingsActivity::class.java))
//                startActivity(Intent(this@MainActivity, PinLockActivity::class.java))
            }
        })

        llAccessibilityServiceStatus.setOnClickListener {
            gotoAccessibilitySettings(this@MainActivity)
        }
        actionDouyin.setOnClickListener {
            checkPermission()
        }
        val l = ScreenListener(this)

        l.begin(object : ScreenStateListener {
            override fun onUserPresent() {
                Log.e("ScreenStateListener", "onUserPresent")
                AnXinLiveRoomAutomaticLikesScript.runStatus = false
            }

            override fun onScreenOn() {
                Log.e("ScreenStateListener", "onScreenOn")
                AnXinLiveRoomAutomaticLikesScript.runStatus = false
            }

            override fun onScreenOff() {
                Log.e("ScreenStateListener", "onScreenOff")
                AnXinLiveRoomAutomaticLikesScript.runStatus = false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val accessibilityServiceOn = ApiUtil.isAccessibilityServiceOn(
            UiApplication.context,
            MainAccessService::class.java
        )
        accessibilityServiceStatus.isChecked = accessibilityServiceOn
        updateCurrentDesc()
    }


    private fun updateCurrentDesc() {
        val currentHostName =
            CommonPreferencesUtil.getString(
                EditSettingsActivity.HOST_NAME_KEY,
                AnXinLiveRoomAutomaticLikesScript.hostName
            )
        val currentMmustConditions =
            CommonPreferencesUtil.getString(
                EditSettingsActivity.MUST_KEY,
                AnXinLiveRoomAutomaticLikesScript.mustConditions
            )
        val currentCount =
            CommonPreferencesUtil.getString(
                EditSettingsActivity.CLICK_COUNT_KEY,
                "3100"
            )
//        tvCurrentDesc.text = "当前设置如下\n点击次数：${currentCount}\n抖音昵称：${currentHostName}\n判断直播间条件：${currentMmustConditions}\n"
        tvCurrentDesc.text = "当前设置如下\n点击次数：${currentCount}\n"
    }


    /**
     * 检测浮窗权限是否开启，若没有给与申请提示框（非必须，申请依旧是EasyFloat内部进行）
     */
    private fun checkPermission() {
        if (PermissionUtils.checkPermission(this)) {
            checkAccessibilityServiceOn() {
                showAppFloat()
            }
        } else {
            AlertDialog.Builder(this)
                .setMessage("使用浮窗功能，需要您授权悬浮窗权限。")
                .setPositiveButton("去开启") { _, _ ->
                    showAppFloat()
                }
                .setNegativeButton("取消") { _, _ -> }
                .show()
        }
    }

    private fun showAppFloat() {
        EasyFloat.with(this.applicationContext)
            .setShowPattern(ShowPattern.ALL_TIME)
            .setSidePattern(SidePattern.RESULT_SIDE)
            .setImmersionStatusBar(true)
            .setGravity(Gravity.END, -20, DensityUtils.dp2px(600))
            .setLayout(R.layout.float_app) {
                val tvCount = it.findViewById<TextView>(R.id.tvCount)
                tvCount.text =
                    CommonPreferencesUtil.getString(EditSettingsActivity.CLICK_COUNT_KEY, "3100")
                it.findViewById<ImageView>(R.id.ivClose).setOnClickListener {
                    AnXinLiveRoomAutomaticLikesScript.runStatus = false
                    EasyFloat.dismiss()
                }
                it.findViewById<TextView>(R.id.openDouYin).setOnClickListener {
                    checkAccessibilityServiceOn() {
                        try {
                            Thread { AnXinLiveRoomAutomaticLikesScript.openTiktok() }.start()
                        } catch (e: Exception) {
                            Thread { AnXinLiveRoomAutomaticLikesScript.openTiktok() }.start()
                        }

                    }
                }
                it.findViewById<TextView>(R.id.startClick).setOnClickListener {
                    checkAccessibilityServiceOn() {
                        try {
                            val count = tvCount.text.toString().toInt()
                            Thread { AnXinLiveRoomAutomaticLikesScript.doAction(count) }.start()
                        } catch (e: Exception) {
                            Thread { AnXinLiveRoomAutomaticLikesScript.doAction(3100) }.start()
                        }
                    }
                }
                it.findViewById<TextView>(R.id.stopClick).setOnClickListener {
                    AnXinLiveRoomAutomaticLikesScript.runStatus = false
                }
            }
            .registerCallback {
                drag { _, motionEvent ->
                    DragUtils.registerDragClose(motionEvent, object : OnTouchRangeListener {
                        override fun touchInRange(inRange: Boolean, view: BaseSwitchView) {
                            setVibrator(inRange)
                            view.findViewById<TextView>(com.lzf.easyfloat.R.id.tv_delete).text =
                                if (inRange) "松手删除" else "删除浮窗"

                            view.findViewById<ImageView>(com.lzf.easyfloat.R.id.iv_delete)
                                .setImageResource(
                                    if (inRange) com.lzf.easyfloat.R.drawable.icon_delete_selected
                                    else com.lzf.easyfloat.R.drawable.icon_delete_normal
                                )
                        }

                        override fun touchUpInRange() {
                            AnXinLiveRoomAutomaticLikesScript.runStatus = false
                            EasyFloat.dismiss()
                        }
                    }, showPattern = ShowPattern.ALL_TIME)
                }
            }
            .show()
    }

    fun checkAccessibilityServiceOn(callback: () -> Unit) {
        if (ApiUtil.isAccessibilityServiceOn(
                UiApplication.context,
                MainAccessService::class.java
            )
        ) {
            callback.invoke()
        } else {
            Toast.makeText(this@MainActivity, "请开启辅助功能", Toast.LENGTH_SHORT)
                .show()
            gotoAccessibilitySettings(this@MainActivity)
        }
    }

    fun setVibrator(inRange: Boolean) {
        if (!vibrator.hasVibrator() || (inRange && vibrating)) return
        vibrating = inRange
        if (inRange) if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else vibrator.vibrate(100)
        else vibrator.cancel()
    }

    companion object {
        fun gotoAccessibilitySettings(context: Context): Boolean {
            val settingsIntent = Intent(
                Settings.ACTION_ACCESSIBILITY_SETTINGS
            )
            if (context !is Activity) {
                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            var isOk = true
            try {
                context.startActivity(settingsIntent)
            } catch (e: ActivityNotFoundException) {
                isOk = false
            }
            return isOk
        }
    }
}