package com.example.androidautoclick.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.auto.assist.accessibility.api.AcessibilityApi
import com.auto.assist.accessibility.util.Screen
import com.example.androidautoclick.R
import com.example.androidautoclick.ui.uitils.CommonPreferencesUtil
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_coordinate_settings.btnSave1
import kotlinx.android.synthetic.main.activity_coordinate_settings.btnSave2
import kotlinx.android.synthetic.main.activity_coordinate_settings.ivBackground
import kotlinx.android.synthetic.main.activity_coordinate_settings.myDrawLineView

class CoordinateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinate_settings)
        initView()
        initListener()
    }

    private fun initListener() {
        btnSave1.setOnClickListener {
            myDrawLineView.x1 = myDrawLineView.x0
            myDrawLineView.y1 = myDrawLineView.y0
            myDrawLineView.invalidate()

            CommonPreferencesUtil.saveValue(USER_IMG_X_KEY, myDrawLineView.x1)
            CommonPreferencesUtil.saveValue(USER_IMG_Y_KEY, myDrawLineView.y1)
        }
        btnSave2.setOnClickListener {
            myDrawLineView.x2 = myDrawLineView.x0
            myDrawLineView.y2 = myDrawLineView.y0
            myDrawLineView.invalidate()

            CommonPreferencesUtil.saveValue(CLICK_LIKE_X_KEY, myDrawLineView.x2)
            CommonPreferencesUtil.saveValue(CLICK_LIKE_Y_KEY, myDrawLineView.y2)
        }
    }

    private fun initView() {
        Blurry.with(ivBackground.context)
            .sampling(3)
            .animate(500)
            .from(BitmapFactory.decodeResource(resources, R.drawable.img_anxin_shu))
            .into(ivBackground)

    /*    val userX = CommonPreferencesUtil.getFloat(CoordinateActivity.USER_IMG_X_KEY, 0F)
        val userY = CommonPreferencesUtil.getFloat(CoordinateActivity.USER_IMG_X_KEY, 0F)
        if (userX == 0F || userY == 0F) {
            val context = UiApplication.context
            val screenWidth = Screen.getScreenWidth(context)
            val screenHeight = Screen.getScreenHeight(context)
            //点击头像
//            AcessibilityApi.click((screenWidth * 0.18F).toInt(), (screenHeight * 0.245).toInt())
            myDrawLineView.x1 = (screenWidth * 0.18F)
            myDrawLineView.y1 = (screenHeight * 0.245F)
            myDrawLineView.invalidate()
        }else{
            myDrawLineView.x1 = userX
            myDrawLineView.y1 = userY
            myDrawLineView.invalidate()
        }*/
        val setingsLikeX = CommonPreferencesUtil.getFloat(CoordinateActivity.CLICK_LIKE_X_KEY, 0F)
        val setingsLikeY = CommonPreferencesUtil.getFloat(CoordinateActivity.CLICK_LIKE_Y_KEY, 0F)
        if (setingsLikeX == 0F || setingsLikeY == 0F) {
            val context = UiApplication.context
            val screenWidth = Screen.getScreenWidth(context)
            val screenHeight = Screen.getScreenHeight(context)
            val tempWidth = (screenWidth * 0.5F)
            val tempHeight = (screenHeight * 0.38F)
            myDrawLineView.x2 = tempWidth
            myDrawLineView.y2 = tempHeight
            myDrawLineView.invalidate()
        }else{
            myDrawLineView.x2 = setingsLikeX
            myDrawLineView.y2 = setingsLikeY
            myDrawLineView.invalidate()
        }
    }

    companion object {
        val USER_IMG_X_KEY = "user_img_x_key"
        val USER_IMG_Y_KEY = "user_img_y_key"

        val CLICK_LIKE_X_KEY = "click_like_x_key"
        val CLICK_LIKE_Y_KEY = "click_like_y_key"
    }
}