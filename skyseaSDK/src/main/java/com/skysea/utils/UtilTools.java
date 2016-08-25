package com.skysea.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

/**
 * Created by jyd-pc006 on 16/8/17.
 */
public final class UtilTools {
    public static int getScreenWidth(Context context) {
        if (context != null) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return dm.widthPixels;
        } else {
            return 0;
        }

    }

    public static int getScreenHeight(Context context) {
        if (context != null) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return dm.heightPixels;
        } else {
            return 0;
        }
    }

    //获取屏幕宽高
    public static int[] getScreenSize() {
        int[] screens;
        DisplayMetrics dm = new DisplayMetrics();
        screens = new int[]{dm.widthPixels, dm.heightPixels};
        return screens;
    }

    /**
     * 获取应用版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";

    }

}
