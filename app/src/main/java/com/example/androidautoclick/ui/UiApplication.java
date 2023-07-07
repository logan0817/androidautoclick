package com.example.androidautoclick.ui;

import android.app.Application;
import android.content.Context;

import com.example.androidautoclick.ui.uitils.CommonPreferencesUtil;
import com.hjq.bar.ITitleBarStyle;
import com.hjq.bar.TitleBar;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/23
 *     desc  : new class
 * </pre>
 */
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
