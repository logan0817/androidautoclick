package com.example.androidautoclick.ui.script

import com.auto.assist.accessibility.api.AcessibilityApi
import com.auto.assist.accessibility.api.UiApi
import com.auto.assist.accessibility.util.LogUtil
import com.auto.assist.accessibility.util.Screen
import com.example.androidautoclick.ui.CoordinateActivity
import com.example.androidautoclick.ui.UiApplication
import com.example.androidautoclick.ui.uitils.CommonPreferencesUtil
import com.example.androidautoclick.ui.uitils.Utils

/**
 * author: Logan
 * time  : 2023/07/06
 * desc  : 直接打开抖音->我->关注->主播个人信息页面->点击头像->连续点赞3000次
 */
object AnXinLiveRoomAutomaticLikesScript {

    val defaultHostName: String = "'安欣✨电台'"
    var hostName: String = defaultHostName

    //    val defaultMustConditions: String = "'安欣✨电台'"
    val defaultMustConditions: String = ""
    var mustConditions: String = defaultMustConditions

    val defaultOptionConditions: String = ""
    var optionConditions: String = defaultOptionConditions


    var runStatus: Boolean = false

    fun doWrok(count: Int) {
        //启动抖音
        openTiktok()
        if (hostName.isNullOrBlank()) {
            LogUtil.D("没有设置hostName，打开抖音成功，结束")
        } else if (goFollowList()) {
            if (goHostNameHomePage()) {
                LogUtil.D("前往个人主页成功")
                if (goLiveRoom()) {
                    LogUtil.D("前往直播间成功，开始点击")
                    doAction(count)
                } else {
                    LogUtil.E("前往直播间异常,停止")
                }
            } else {
                LogUtil.E("前往个人主页异常,停止")
            }
        } else {
            LogUtil.E("前往关注列表异常,停止")
        }

    }

    private fun goFollowList(): Boolean {
        val step0 = ("{"
                + "'maxWClickMSec':1000,"
                + "'click':{'text':'我'},"
                + "'page':"
                + "{"
                + "'maxMustMills':5000,"
                + "'maxOptionMills':5000,"
                + "'must':{'text':['我','首页'],'id':[],'desc':[]},"
                + "'option':{'text':[],'id':[],'desc':[]}"
                + "}"
                + "}")
        val step1 = ("{"
                + "'maxWClickMSec':1000,"
                + "'click':{'text':'关注'},"
                + "'page':"
                + "{"
                + "'maxMustMills':5000,"
                + "'maxOptionMills':5000,"
                + "'must':{'text':['我','关注'],'id':[],'desc':[]},"
                + "'option':{'text':[],'id':[],'desc':[]}"
                + "}"
                + "}")
        return UiApi.jumpToNeedPage(arrayOf(step0, step1))
    }

    private fun goHostNameHomePage(): Boolean {
        val step = ("{"
                + "'maxWClickMSec':1000,"
                + "'click':{'text':${hostName}},"
                + "'page':"
                + "{"
                + "'maxMustMills':5000,"
                + "'maxOptionMills':5000,"
                + "'must':{'text':['关注'],'id':[],'desc':[]},"
                + "'option':{'text':[],'id':[],'desc':[]}"
                + "}"
                + "}")
        return UiApi.jumpToNeedPage(arrayOf(step))
    }

    private fun goLiveRoom(): Boolean {
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val userX = CommonPreferencesUtil.getFloat(CoordinateActivity.USER_IMG_X_KEY, 0F)
        val userY = CommonPreferencesUtil.getFloat(CoordinateActivity.USER_IMG_X_KEY, 0F)
        if (userX == 0F || userY == 0F) {
            //点击头像设置坐标点
            AcessibilityApi.click(userX.toInt(), userY.toInt())
        } else {
            val context = UiApplication.context
            val screenWidth = Screen.getScreenWidth(context)
            val screenHeight = Screen.getScreenHeight(context)
            //点击头像
            AcessibilityApi.click((screenWidth * 0.18F).toInt(), (screenHeight * 0.245).toInt())
        }
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return true
    }

    fun doAction(count: Int) {
        runStatus = true
        LogUtil.D("已到达抖音指定页面")
        val context = UiApplication.context
        val screenWidth = Screen.getScreenWidth(context)
        val screenHeight = Screen.getScreenHeight(context)
        val tempWidth = (screenWidth * 0.5F).toInt()
        val tempHeight = (screenHeight * 0.38F).toInt()
        val setingsLikeX = CommonPreferencesUtil.getFloat(CoordinateActivity.CLICK_LIKE_X_KEY, 0F)
        val setingsLikeY = CommonPreferencesUtil.getFloat(CoordinateActivity.CLICK_LIKE_Y_KEY, 0F)
        var tempCount = 0;
        while (true) {
            if (mustConditions.isNullOrBlank() || isDouyinLiveRoomPage) {
                // 生成1位随机数字
                val random1 = (Math.random() * 9f + 1).toInt()
                // 生成2位随机数字
                val random2 = ((Math.random() * 9f + 1) * 10).toInt()
                if (setingsLikeX==0F||setingsLikeY==0F){
                    AcessibilityApi.click(tempWidth + random2, tempHeight - random1)
                }else{
                    AcessibilityApi.click((setingsLikeX + random2).toInt(), (setingsLikeY - random1).toInt())
                }
                try {
                    Thread.sleep((15 * random1).toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    tempCount++
                }
            } else {
                LogUtil.E("当前不在抖音指定页面,停止 break")
                break
            }

            if (tempCount > count) {
                LogUtil.D("到达指定数量$count，停止 break")
                break
            }
            if (!runStatus) {
                LogUtil.D("运行状态为停止$runStatus，停止 break")
                break
            }
        }
    }

    private val isDouyinLiveRoomPage: Boolean
        private get() {
            val pageStr = ("{"
                    + "'maxMustMills':5000,"
                    + "'maxOptionMills':5000,"
                    + "'must':{'text':[${mustConditions}],'id':[],'desc':[]},"
                    + "'option':{'text':[${optionConditions}],'id':[],'desc':[]}"
                    + "}")
            return UiApi.isMyNeedPage(pageStr)
        }

    private fun openTiktok() {
        //抖音
        val packageName = "com.ss.android.ugc.aweme"
        val exist = Utils.checkAppInstalled(UiApplication.context, packageName)
        if (exist) {
            LogUtil.E("startTiktok正在打开抖音")
            Utils.RunApp(packageName)
        } else {
            LogUtil.E("没用安装抖音，告辞。")
        }
    }
}