package com.auto.assist.accessibility.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


public class Screen {

    /**
     * 屏幕宽高PX
     */
    public static Pair<Integer, Integer> getScreenPX(Context context) {
        DisplayMetrics dm = getScreen(context);
        return new Pair<>(dm.widthPixels, dm.heightPixels);
    }

    /**
     * 屏幕宽高DP
     */
    public static Pair<Float, Float> getScreenDP(Context context) {
        Pair<Integer, Integer> screenPX = getScreenPX(context);
        return new Pair<>(px2dip(context, screenPX.first), px2dip(context, screenPX.second));
    }

    /**
     * 获取屏幕Metrics
     */
    public static DisplayMetrics getScreen(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        return outMetrics;
    }

    /**
     * 计算百分比
     */
    public static String getPercent(int n, float total) {
        float rs = (n / total) * 100;
        // 判断是否是正整数
        if (String.valueOf(rs).indexOf(".0") != -1) {
            return String.valueOf((int) rs);
        } else {
            return String.format("%.1f", rs);
        }
    }

    // 根据手机的分辨率从 dp 的单位 转成为 px(像素)
    public static float dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static float px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxValue / scale + 0.5f);
    }

    /***
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 隐藏状态栏
     *
     * @param activity activity
     */
    public static void hideStatusBar(Activity activity) {
        //最终方案
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 全透明实现
            //getWindow.setStatusBarColor(Color.TRANSPARENT)
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4 全透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 获取viewgroup的快照
     *
     * @param viewGroup
     * @return
     */
    public static Bitmap getViewGroupBitmap(ViewGroup viewGroup) {
        viewGroup.setDrawingCacheEnabled(true);
        Bitmap screenShotAsBitmap = Bitmap.createBitmap(viewGroup.getDrawingCache());
        viewGroup.setDrawingCacheEnabled(false);
        return screenShotAsBitmap;
    }

    /*获取屏幕宽度*/
    public static int getScreenWidth(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    /*获取屏幕高度*/
    public static int getScreenHeight(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }

}
