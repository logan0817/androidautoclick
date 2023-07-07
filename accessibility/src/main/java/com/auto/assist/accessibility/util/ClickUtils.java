package com.auto.assist.accessibility.util;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.util.Log;

public class ClickUtils {
    public static void click(AccessibilityService accessibilityService, Float x, Float y) {
        Log.d("ClickUtils", "click: (" + x + " " + y + ")");
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x, y);
        builder.addStroke(new GestureDescription.StrokeDescription(path, 0, 25));
        GestureDescription gesture = builder.build();
        accessibilityService.dispatchGesture(
                gesture,
                new AccessibilityService.GestureResultCallback() {
                    @Override
                    public void onCompleted(GestureDescription gestureDescription) {
                        super.onCompleted(gestureDescription);
                    }

                    @Override
                    public void onCancelled(GestureDescription gestureDescription) {
                        super.onCancelled(gestureDescription);
                    }
                },
                null
        );
    }

    /**
     * 模拟点击事件
     *
     * @param x
     * @param y
     */
    public static void Tap(AccessibilityService accessibilityService, int x, int y) {
        Log.e("ClickUtils","模拟点击事件");

        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path p = new Path();
        p.moveTo(x , y);
        builder.addStroke(new GestureDescription.StrokeDescription(p, 0L, 10L));
        GestureDescription gesture = builder.build();
        accessibilityService.dispatchGesture(gesture, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.e("ClickUtils", "onCompleted: 完成..........");
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.e("ClickUtils", "onCompleted: 取消..........");
            }
        }, null);

    }
    /**
     * 模拟滑动事件
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param startTime 0即可执行
     * @param duration  滑动时长
     * @return 执行是否成功
     */
    public static void Swipe(AccessibilityService accessibilityService, int x1, int y1,int x2 , int y2 ,final int startTime , final int duration) {
        Log.e("ClickUtils","模拟滑动事件");

        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path p = new Path();
        p.moveTo(x1 , y1);
        p.lineTo(x2 , y2);
        builder.addStroke(new GestureDescription.StrokeDescription(p, 0L, 500L));
        GestureDescription gesture = builder.build();
        accessibilityService.dispatchGesture(gesture, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.e("ClickUtils", "onCompleted: 完成..........");
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.e("ClickUtils", "onCompleted: 取消..........");
            }
        }, null);

    }

}
