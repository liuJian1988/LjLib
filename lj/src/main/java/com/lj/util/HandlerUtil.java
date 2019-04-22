package com.lj.util;

import android.os.Handler;
import android.os.Message;

/**
 * 作者：liujian on 2019/3/14 10:54
 * 邮箱：15313727484@163.com
 */
public class HandlerUtil {
    /**
     *  @author 刘建
     *  @time 2019/3/14  10:58 AM
     *  @describe
     *  封装handler传递消息
     */
    public static void handlerEvent(Handler h, int w, Object obj) {
        if (h == null) return;
        Message m = h.obtainMessage();
        if (obj != null) m.obj = obj;
        m.what = w;
        h.sendMessage(m);
    }

    /**
     *  @author 刘建
     *  @time 2019/3/14  10:59 AM
     *  @describe
     *  封装handler传递消息
     */
    public static void handlerEvent(Handler h, int w) {
        handlerEvent(h, w, null);
    }
}
