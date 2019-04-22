package com.lj.lang.develop.log;

import android.app.Application;
import android.content.Context;

import java.util.Calendar;

/**
 * 作者：liujian on 2018/12/5 14:41
 * 邮箱：15313727484@163.com
 * 1、将我们常用的工具放到这里面
 * 2、我们把可以有些不常用的方法在这里实现了
 */
public abstract class AbsLog implements ILog {
    protected Calendar calendar = Calendar.getInstance();
    protected boolean isLog;
    protected Context context;


    @Override
    public void init(Application context) {
        isLog = true;
        this.context = context;
    }

    @Override
    public void close() {
        isLog = false;
    }

    @Override
    public void printLog(String tag, String msg) {
        if (!isLog) {
            return;
        }
    }
}
