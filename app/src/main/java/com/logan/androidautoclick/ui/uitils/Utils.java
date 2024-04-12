package com.logan.androidautoclick.ui.uitils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.logan.androidautoclick.ui.UiApplication;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static void RunApp(String packageName) {
        PackageInfo pi;
        try {
            Context context = UiApplication.context;
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.setPackage(pi.packageName);
            PackageManager pManager = context.getPackageManager();
            List<ResolveInfo> apps = pManager.queryIntentActivities(
                    resolveIntent, 0);
            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cn = new ComponentName(packageName, className);
                intent.setComponent(cn);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static boolean checkAppInstalled(Context context, String pName) {
        if (pName == null || pName.isEmpty()) {
            return false;
        }
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> info = packageManager.getInstalledPackages(0);
        if (info == null || info.isEmpty()) {
            return false;
        }
        for (int i = 0; i < info.size(); i++) {
            if (pName.equals(info.get(i).packageName)) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public static List<String> getAllApps(Context context) {
        List<String> packageList = new ArrayList<>();

        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> resolveInfoList = packageManager.getInstalledPackages(0);
        Log.d("caowj", "全部的应用数量：" + resolveInfoList.size());
        for (PackageInfo info : resolveInfoList) {
            packageList.add(info.packageName);
        }
        Log.d("caowj", "非系统应用数量：" + packageList.size());
        return packageList;
    }


}
