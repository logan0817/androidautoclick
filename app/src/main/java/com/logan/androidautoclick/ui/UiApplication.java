package com.logan.androidautoclick.ui;

import android.app.Application;
import android.content.Context;

import com.logan.androidautoclick.ui.uitils.CommonPreferencesUtil;

public class UiApplication extends Application
{
    public static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        if(context==null){
            context=this;
        }
        CommonPreferencesUtil.INSTANCE.init(this);
        // 初始化 TitleBar 默认样式
//        TitleBar.setDefaultStyle(new ITitleBarStyle());
    }
}
