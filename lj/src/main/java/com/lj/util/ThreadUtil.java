package com.lj.util;

/**
 * 作者：liujian on 2018/9/13 10:30
 * 邮箱：15313727484@163.com
 */
public class ThreadUtil {
    public static int ThreadSleep(int m) {
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
