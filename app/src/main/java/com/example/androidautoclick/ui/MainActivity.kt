package com.example.androidautoclick.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.auto.assist.accessibility.util.ApiUtil
import com.example.androidautoclick.R
import com.example.androidautoclick.ui.uitils.Utils
import com.example.androidautoclick.ui.script.LiveRoomAutomaticLikesScript
import com.example.androidautoclick.ui.uitils.CommonPreferencesUtil
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_main.tvCurrentDesc
import kotlinx.android.synthetic.main.activity_main.accessibilityServiceStatus
import kotlinx.android.synthetic.main.activity_main.actionDouyin
import kotlinx.android.synthetic.main.activity_main.ivBackground
import kotlinx.android.synthetic.main.activity_main.llAccessibilityServiceStatus
import kotlinx.android.synthetic.main.activity_main.titleBar
import kotlinx.android.synthetic.main.activity_main.tvAccessibilityServiceTitle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Utils.getAllApps(this)
        initView()
        setListener()
        LiveRoomAutomaticLikesScript.hostName =
            CommonPreferencesUtil.getString(
                EditSettingsActivity.HOST_NAME_KEY,
                LiveRoomAutomaticLikesScript.defaultHostName
            )
        LiveRoomAutomaticLikesScript.mustConditions =
            CommonPreferencesUtil.getString(
                EditSettingsActivity.MUST_KEY,
                LiveRoomAutomaticLikesScript.defaultMustConditions
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
            }
        })

        llAccessibilityServiceStatus.setOnClickListener {
            gotoAccessibilitySettings(this@MainActivity)
        }
        actionDouyin.setOnClickListener {
            if (ApiUtil.isAccessibilityServiceOn(
                    UiApplication.context,
                    MainAccessService::class.java
                )
            ) {
                Thread { LiveRoomAutomaticLikesScript.doWrok() }.start()
            } else {
                Toast.makeText(this@MainActivity, "请开启辅助功能", Toast.LENGTH_SHORT).show()
                gotoAccessibilitySettings(this@MainActivity)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val accessibilityServiceOn = ApiUtil.isAccessibilityServiceOn(
            UiApplication.context,
            MainAccessService::class.java
        )
        accessibilityServiceStatus.isChecked = accessibilityServiceOn
        if (accessibilityServiceOn) {
            tvAccessibilityServiceTitle.setTextColor(getColor(R.color.color_F8F0D6))
        } else {
            tvAccessibilityServiceTitle.setTextColor(getColor(R.color.color_7B7C7D))
        }
        updateCurrentDesc()
    }


    private fun updateCurrentDesc() {
        val currentHostName =
            CommonPreferencesUtil.getString(EditSettingsActivity.HOST_NAME_KEY, LiveRoomAutomaticLikesScript.hostName)
        val currentMmustConditions =
            CommonPreferencesUtil.getString(EditSettingsActivity.MUST_KEY, LiveRoomAutomaticLikesScript.mustConditions)
        tvCurrentDesc.text =
            "当前设置如下：\n抖音昵称：${currentHostName}\n判断直播间条件：${currentMmustConditions}\n"
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