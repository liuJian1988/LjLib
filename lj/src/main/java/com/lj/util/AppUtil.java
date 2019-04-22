package com.lj.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;


import com.lj.app.BaseLjApp;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Liujian on 2017/9/5 0005.
 *
 * @link http://blog.csdn.net/liujian8654562
 */

public class AppUtil {
    /**
     * 隐藏虚拟按键
     *
     * @param context
     */
    public static void hideVirtualKey(Activity context) {
        View decorView = context.getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    public static void hideBottomUIMenu(Context context) {
        operateVirtualKey("hide", context);
    }

    /**
     * 显示虚拟按键，并且全屏
     */
    public static void showBottomUIMenu(Context context) {
        operateVirtualKey("show", context);
    }

    private static void operateVirtualKey(String cmd, Context context) {
        Intent intent = new Intent();
        intent.setAction("MyRecv_action");
        intent.putExtra("cmd", cmd);
        context.sendBroadcast(intent);
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * @param filePath    apk文件保存路径
     * @param installPath apk安装路径
     * @param filename    apk文件名
     * @category 安装apk，同时杀死自己的进程
     */
    public static void installApk(String filePath, String installPath,
                                  String filename) {
        Log.i("installAPP", "---------我开始安装APP----------");
        File deleteFile = new File(installPath + filename);
        if (deleteFile.exists()) {// 判断该文件是否存在，存在的话删除
            deleteFile.delete();
        }
        File file = new File(filePath + filename);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(Uri.fromFile(file), type);
        Log.i("installAPP", "--------跳转到安装界面----------");
        BaseLjApp.getAppContext().startActivity(intent);// 跳转安装

        android.os.Process.killProcess(android.os.Process.myPid());
    }


    public static boolean isServiceWorked(String name, Context context) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 作者：刘建
     * 时间：2018年12月15日
     * 获取项目的包名称
     */
    public static String getAppPkgName(Context context) {
        return context.getPackageName();
    }
}
