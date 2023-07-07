package com.example.androidautoclick.ui.script

import com.auto.assist.accessibility.api.AcessibilityApi
import com.auto.assist.accessibility.api.UiApi
import com.auto.assist.accessibility.util.LogUtil
import com.auto.assist.accessibility.util.Screen
import com.example.androidautoclick.ui.MainAccessService
import com.example.androidautoclick.ui.UiApplication
import com.example.androidautoclick.ui.uitils.Utils

/**
 * <pre>
 * author: Logan
 * time  : 2023/07/06
 * desc  : 直接打开抖音，连续点赞
</pre> *
 */
object LiveRoomAutomaticLikesScript {

    val defaultHostName: String = "'安欣✨音乐电台'"
    var hostName: String = defaultHostName

    val defaultMustConditions: String = "'安欣✨音乐电台'"
    var mustConditions: String = defaultMustConditions

    val defaultOptionConditions: String = ""
    var optionConditions: String = defaultOptionConditions

    fun doWrok() {
        /*首先让手机回退到桌面,注意:由于每个手机的桌面包名不一致,
        请添加你的测试机桌面包名至com.auto.assist.accessibility.util.Config中,
        否者无法成功回退*/
//        UiApi.backToDesk();

        //启动抖音
        startTiktok()

        if (goFollowList()) {
            if (goHostNameHomePage()) {
                LogUtil.D("前往主页成功")
                if (goLiveRoom()) {
                    LogUtil.D("前往直播间成功")
                    doAction()
                } else {
                    LogUtil.E("前往直播间异常,停止")
                }
            } else {
                LogUtil.E("前往主页异常,停止")
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
        val context = UiApplication.context
        val screenWidth = Screen.getScreenWidth(context)
        val screenHeight = Screen.getScreenHeight(context)
        //点击头像
        AcessibilityApi.click(screenWidth / 5, (screenHeight / 5F).toInt())
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return true
    }

    private fun doAction() {
        //已进入界面
        LogUtil.D("已在抖音指定页面")
        val context = UiApplication.context
        val screenWidth = Screen.getScreenWidth(context)
        val screenHeight = Screen.getScreenHeight(context)
        val tempWidth = screenWidth / 2
        val tempHeight = screenHeight / 2
        while (true) {
            if (isDouyinLiveRoomPage) {
                // 生成1位随机数字
                val random1 = (Math.random() * 9f + 1).toInt()
                // 生成2位随机数字
                val random2 = ((Math.random() * 9f + 1) * 10).toInt()
                AcessibilityApi.click(tempWidth + random2, tempHeight - random1)
                try {
                    Thread.sleep((15 * random1).toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            } else {
                LogUtil.E("当前不在抖音指定页面,暂停操作 sleep 5s")
                try {
                    Thread.sleep(3000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
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

    private fun startTiktok() {
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