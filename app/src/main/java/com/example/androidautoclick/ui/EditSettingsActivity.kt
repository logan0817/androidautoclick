package com.example.androidautoclick.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.androidautoclick.R
import com.example.androidautoclick.ui.script.AnXinLiveRoomAutomaticLikesScript
import com.example.androidautoclick.ui.uitils.CommonPreferencesUtil
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_edittext_settings.hostNameEditText
import kotlinx.android.synthetic.main.activity_edittext_settings.ivBackground
import kotlinx.android.synthetic.main.activity_edittext_settings.layout_edit
import kotlinx.android.synthetic.main.activity_edittext_settings.mustEditText
import kotlinx.android.synthetic.main.activity_edittext_settings.tvCurrentDesc
import kotlinx.android.synthetic.main.activity_main.titleBar

class EditSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edittext_settings)
        initView()
        initListener()
    }

    private fun initListener() {
        titleBar.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(titleBar: TitleBar?) {
                finish()
            }
        })
        hostNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                var value = p0.toString()
                if (value.isNullOrBlank() || "null" == value) {
                    value = AnXinLiveRoomAutomaticLikesScript.defaultHostName
                }
                CommonPreferencesUtil.saveValue(HOST_NAME_KEY, value)
                AnXinLiveRoomAutomaticLikesScript.hostName = "'${value}'"
                updateCurrentDesc()
            }
        })
        mustEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                var value = p0.toString()
                if (value.isNullOrBlank() || "null" == value) {
                    value = AnXinLiveRoomAutomaticLikesScript.defaultMustConditions
                }
                CommonPreferencesUtil.saveValue(MUST_KEY, value)
                AnXinLiveRoomAutomaticLikesScript.mustConditions = "'${value}'"
                updateCurrentDesc()
            }
        })
    }


    private fun initView() {
        Blurry.with(ivBackground.context)
            .sampling(3)
            .animate(500)
            .from(BitmapFactory.decodeResource(resources, R.drawable.img_anxin_shu))
            .into(ivBackground)
        layout_edit.setLineColor(getColor(R.color.white))
        updateCurrentDesc()
    }

    private fun updateCurrentDesc() {
        val currentHostName =
            CommonPreferencesUtil.getString(HOST_NAME_KEY, AnXinLiveRoomAutomaticLikesScript.hostName)
        val currentMmustConditions =
            CommonPreferencesUtil.getString(MUST_KEY, AnXinLiveRoomAutomaticLikesScript.mustConditions)
        tvCurrentDesc.text =
            "当前设置如下：\n抖音昵称：${currentHostName}\n判断直播间条件：${currentMmustConditions}\n"
    }

    companion object {
        val HOST_NAME_KEY = "host_name_key"
        val MUST_KEY = "must_key"
        val OPTION_KEY = "option_key"
    }

}