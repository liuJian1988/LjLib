package com.lj.app;

import android.app.Application;
import android.os.Environment;

/**
 * 作者：liujian on 2018/12/5 13:33
 * 邮箱：15313727484@163.com
 */
public class LjEnvironment {
    public static LjEnvironment platform = new LjEnvironment();
    private Application application;
    protected AppExecutors mAppExecutors;
    public static String sdcardRootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

    private LjEnvironment() {
    }

    public void initLjUtil(Application application, AppExecutors mAppExecutors) {
        this.application = application;
        this.mAppExecutors = mAppExecutors;
    }

    public Application getApplication() {
        return application;
    }

    public AppExecutors getAppExecutors() {
        return mAppExecutors;
    }

}
