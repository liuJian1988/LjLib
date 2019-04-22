package com.lj.util.wps;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import java.io.File;

/**
 * 作者：liujian on 2018/12/17 15:06
 * 邮箱：15313727484@163.com
 */
public class WPSUtils {

    public static final WPSUtils instance = new WPSUtils();


    public boolean openFile(Activity c, String path) {

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.NORMAL);
        //打开模式
        bundle.putBoolean(WpsModel.SEND_SAVE_BROAD, true);//关闭时是否发送广播
        bundle.putString(WpsModel.THIRD_PACKAGE, c.getPackageName());
        //第三方应用的包名，用于对改应用合法性的验证
        bundle.putBoolean(WpsModel.CLEAR_TRACE, true);//清除打开记录
        bundle.putBoolean(WpsModel.SEND_CLOSE_BROAD, true);// 关闭文件时是否发送广播
        bundle.putBoolean(WpsModel.HOMEKEY_DOWN, true);// 按下Home键
        bundle.putBoolean(WpsModel.BACKKEY_DOWN, true);// 按下Back键
        bundle.putBoolean(WpsModel.AUTO_JUMP, true);// //第三方打开文件时是否自动跳转
        bundle.putBoolean(WpsModel.CACHE_FILE_INVISIBLE, false);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setClassName(WpsModel.PackageName.NORMAL, WpsModel.ClassName.NORMAL);
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        intent.putExtras(bundle);
        try {
            c.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
