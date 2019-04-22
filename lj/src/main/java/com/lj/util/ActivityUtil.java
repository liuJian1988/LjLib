package com.lj.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * 作者：liujian on 2018/9/19 11:15
 * 邮箱：15313727484@163.com
 */

public class ActivityUtil {
    public static String getTopActivity(Context c) {
        ActivityManager am = (ActivityManager) c.getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cn.getShortClassName();
    }
}
