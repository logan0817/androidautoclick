package com.logan.androidautoclick.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.auto.assist.accessibility.util.ApiUtil
import com.auto.assist.accessibility.util.Screen
import com.logan.androidautoclick.R
import com.logan.androidautoclick.ui.ScreenListener.ScreenStateListener
import com.logan.androidautoclick.ui.script.AnXinLiveRoomAutomaticLikesScript
import com.logan.androidautoclick.ui.uitils.CommonPreferencesUtil
import com.logan.androidautoclick.ui.uitils.DensityUtils
import com.logan.androidautoclick.ui.uitils.Utils
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.interfaces.OnPermissionResult
import com.lzf.easyfloat.interfaces.OnTouchRangeListener
import com.lzf.easyfloat.permission.PermissionUtils
import com.lzf.easyfloat.utils.DragUtils
import com.lzf.easyfloat.widget.BaseSwitchView
import kotlinx.android.synthetic.main.activity_main.accessibilityServiceStatusSwitch
import kotlinx.android.synthetic.main.activity_main.actionStart
import kotlinx.android.synthetic.main.activity_main.clickAppShareBtn
import kotlinx.android.synthetic.main.activity_main.clickCoordinateBtn
import kotlinx.android.synthetic.main.activity_main.clickNumberBtn
import kotlinx.android.synthetic.main.activity_main.floatingWindowSwitch
import kotlinx.android.synthetic.main.activity_main.tvClickCoordinateBtnTitle
import kotlinx.android.synthetic.main.activity_main.tvClickNumberBtnTitle
import kotlinx.android.synthetic.main.activity_main.userGuideBtn


class MainActivity : BaseActivity() {
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
        /*        Blurry.with(ivBackground.context)
                    .sampling(3)
                    .animate(500)
                    .from(BitmapFactory.decodeResource(resources, R.drawable.img_anxin_shu))
                    .into(ivBackground)*/
    }

    private fun setListener() {
        clickNumberBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, EditSettingsActivity::class.java))
        }
        clickCoordinateBtn.setOnClickListener {
            startActivity(Intent(this, CoordinateActivity::class.java))
        }
        userGuideBtn.setOnClickListener {
            val url = "https://easylink.cc/ow94ck"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        clickAppShareBtn.setOnClickListener {
            val url = "https://fir.xcxwo.com/xjg"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        /*  llAccessibilityServiceStatus.setOnClickListener {
              gotoAccessibilitySettings(this@MainActivity)
          }*/
        actionStart.setOnClickListener {
            if (PermissionUtils.checkPermission(this)) {
                checkAccessibilityServiceOn() {
                    showAppFloat()
                }
            } else {
                AlertDialog.Builder(this)
                    .setMessage("请检查（无障碍辅助模式）和（悬浮窗权限）是否开启?")
                    .setNegativeButton("确定") { _, _ -> }
                    .show()
            }
        }
        /*  actionStart.setOnClickListener {
              if (PermissionUtils.checkPermission(this)) {
                  checkAccessibilityServiceOn() {
                      showAppFloat()
                  }
              } else {
                  AlertDialog.Builder(this)
                      .setMessage("使用浮窗功能，需要您授权悬浮窗权限。")
                      .setPositiveButton("去开启") { _, _ ->
                          PermissionUtils.requestPermission(this, object : OnPermissionResult {
                              override fun permissionResult(isOpen: Boolean) {
                                  if (isOpen) {
                                      checkAccessibilityServiceOn() {
                                          showAppFloat()
                                      }
                                  }
                              }
                          })
                      }
                      .setNegativeButton("取消") { _, _ -> }
                      .show()
              }
          }*/
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
        accessibilityServiceStatusSwitch.setOnToggledListener { toggleableView, isOn ->
            val accessibilityServiceOn = ApiUtil.isAccessibilityServiceOn(
                UiApplication.context,
                MainAccessService::class.java
            )
            if (accessibilityServiceOn) {
                accessibilityServiceStatusSwitch.isOn = true
            } else {
                if (isOn) {
                    gotoAccessibilitySettings(this@MainActivity)
                }
            }
        }
        floatingWindowSwitch.setOnToggledListener { toggleableView, isOn ->
            if (PermissionUtils.checkPermission(this)) {
                floatingWindowSwitch.isOn = true
            } else {
                if (isOn) {
                    PermissionUtils.requestPermission(this, object : OnPermissionResult {
                        override fun permissionResult(isOpen: Boolean) {
                            floatingWindowSwitch.isOn = isOpen
                        }
                    })
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val accessibilityServiceOn = ApiUtil.isAccessibilityServiceOn(
            UiApplication.context,
            MainAccessService::class.java
        )
        val hasPermission = PermissionUtils.checkPermission(this)
        accessibilityServiceStatusSwitch.isOn = accessibilityServiceOn
        floatingWindowSwitch.isOn = hasPermission
        if (accessibilityServiceOn) {
            accessibilityServiceStatusSwitch.isEnabled = false
        }
        if (hasPermission) {
            floatingWindowSwitch.isEnabled = false
        }
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
        try {
            val count = currentCount.toInt()
            tvClickNumberBtnTitle.text = "点击次数：${count}"
        } catch (e: Exception) {
            tvClickNumberBtnTitle.text = "点击次数：${3100}"
        }


        var userX = CommonPreferencesUtil.getFloat(CoordinateActivity.USER_IMG_X_KEY, 0F)
        var userY = CommonPreferencesUtil.getFloat(CoordinateActivity.USER_IMG_X_KEY, 0F)

        if (userX == 0F || userY == 0F) {
            val context = UiApplication.context
            val screenWidth = Screen.getScreenWidth(context)
            val screenHeight = Screen.getScreenHeight(context)
            //点击头像
//            AcessibilityApi.click((screenWidth * 0.18F).toInt(), (screenHeight * 0.245).toInt())
            userX = screenWidth * 0.18F
            userY = screenHeight * 0.245F
            tvClickCoordinateBtnTitle.text = "点赞坐标：(${userX.toInt()},${userY.toInt()})"
        } else {
            //点击头像设置坐标点
            tvClickCoordinateBtnTitle.text = "点赞坐标：(${userX.toInt()},${userY.toInt()})"
        }

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
        EasyFloat.dismiss()
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
        if (ApiUtil.isAccessibilityServiceOn(UiApplication.context, MainAccessService::class.java)
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