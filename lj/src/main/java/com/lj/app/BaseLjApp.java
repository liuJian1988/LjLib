package com.lj.app;

import android.app.Application;
import android.content.Context;

import com.lj.lang.develop.log.LjLogger;

/**
 * 作者：liujian on 2018/12/5 14:23
 * 邮箱：15313727484@163.com
 * 。
 * 1、包含 我们为app定义了executor类
 */
public class BaseLjApp extends Application {
    private static final String TAG = BaseLjApp.class.getSimpleName();
    private static BaseLjApp context;
    protected AppExecutors mAppExecutors;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mAppExecutors = new AppExecutors();
        //初始化lj工具
        LjEnvironment.platform.initLjUtil(this, mAppExecutors);
        //初始化Log
        LjLogger.instance.init(this);
        LjLogger.instance.log(TAG, "app start");
    }

    public static BaseLjApp getAppContext() {
        return context;
    }

    public AppExecutors getAppExecutors() {
        return mAppExecutors;
    }

}
