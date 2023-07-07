package com.example.androidautoclick.ui;

import android.view.accessibility.AccessibilityEvent;

import com.auto.assist.accessibility.AutoCoreService;

public class MainAccessService extends AutoCoreService {
    private String TAG = "MainAccessService";

    @Override
    public void onAccessEvent(AccessibilityEvent event) {
        //如果需要通过监听除模拟点击之外的其他事情,再此写具体逻辑.否者无需任何操作
    }
}
