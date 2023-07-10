package com.example.androidautoclick.ui

import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_edittext_settings.btnCoordinate
import kotlinx.android.synthetic.main.activity_edittext_settings.clickCountEditText
import kotlinx.android.synthetic.main.activity_edittext_settings.hostNameEditText
import kotlinx.android.synthetic.main.activity_edittext_settings.ivBackground
import kotlinx.android.synthetic.main.activity_edittext_settings.mustEditText
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
        btnCoordinate.setOnClickListener {
            startActivity(Intent(this, CoordinateActivity::class.java))
        }
        hostNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable) {
                var value = p0.toString()
                if (value.isNullOrBlank() || "null" == value) {
                    value = ""
                }
                CommonPreferencesUtil.saveValue(HOST_NAME_KEY, value)
                AnXinLiveRoomAutomaticLikesScript.hostName = "'${value}'"
            }
        })
        mustEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable) {
                var value = p0.toString()
                if (value.isNullOrBlank() || "null" == value) {
                    value = ""
                }
                CommonPreferencesUtil.saveValue(MUST_KEY, value)
                AnXinLiveRoomAutomaticLikesScript.mustConditions = "'${value}'"
            }
        })
        clickCountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable) {
                var value = p0.toString()
                if (value.isNullOrBlank() || "null" == value) {
                    value = ""
                }
                CommonPreferencesUtil.saveValue(CLICK_COUNT_KEY, value)
            }
        })
    }


    private fun initView() {
        Blurry.with(ivBackground.context)
            .sampling(3)
            .animate(500)
            .from(BitmapFactory.decodeResource(resources, R.drawable.img_anxin_shu))
            .into(ivBackground)

        val currentHostName =
            CommonPreferencesUtil.getString(
                EditSettingsActivity.HOST_NAME_KEY,
                ""
            )
        val currentMmustConditions =
            CommonPreferencesUtil.getString(
                EditSettingsActivity.MUST_KEY,
                ""
            )
        val currentCount =
            CommonPreferencesUtil.getString(
                EditSettingsActivity.CLICK_COUNT_KEY,
                ""
            )
        hostNameEditText.setText(currentHostName)
        mustEditText.setText(currentMmustConditions)
        clickCountEditText.setText(currentCount)

        AnXinLiveRoomAutomaticLikesScript.hostName = currentHostName
        AnXinLiveRoomAutomaticLikesScript.mustConditions = currentMmustConditions
    }

    companion object {
        val HOST_NAME_KEY = "host_name_key"
        val MUST_KEY = "must_key"
        val CLICK_COUNT_KEY = "click_count_key"
        val OPTION_KEY = "option_key"
    }

}