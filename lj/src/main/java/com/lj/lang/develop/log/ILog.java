package com.lj.lang.develop.log;

import android.app.Application;

/**
 * Created by Liujian on 2018/3/6 0006.
 *
 * @link http://blog.csdn.net/liujian8654562
 */

public interface ILog {
     void init(Application context);
     void printLog(String tag, String msg);
     void close();

}
