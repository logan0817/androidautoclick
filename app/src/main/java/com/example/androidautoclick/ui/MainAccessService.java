package com.example.androidautoclick.ui;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.auto.assist.accessibility.AutoCoreService;
import com.example.androidautoclick.ui.uitils.ScreenListener;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : 继承module 中的AutoCoreService
 * </pre>
 */
public class MainAccessService extends AutoCoreService {
    private String TAG = "MainAccessService";
    public static boolean ScreenOn = true;

    @Override
    public void onAccessEvent(AccessibilityEvent event) {
        //如果需要通过监听除模拟点击之外的其他事情,再此写具体逻辑.否者无需任何操作
    }

    private ScreenListener mScreenObserver;

    @Override
    public void onCreate() {
        super.onCreate();
        mScreenObserver = new ScreenListener(this);
        mScreenObserver.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                doSomethingOnScreenOn();
            }

            @Override
            public void onScreenOff() {
                doSomethingOnScreenOff();
            }

            @Override
            public void onUserPresent() {
                doSomethingOnScreenOn();
            }
        });

    }

    private void doSomethingOnScreenOn() {
        Log.i(TAG, "Screen is on");
        ScreenOn = true;
    }

    private void doSomethingOnScreenOff() {
        Log.i(TAG, "Screen is off");
        ScreenOn = false;
    }

}
