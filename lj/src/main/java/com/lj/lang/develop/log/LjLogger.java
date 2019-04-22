package com.lj.lang.develop.log;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Liujian on 2018/3/6 0006.
 *
 * @link http://blog.csdn.net/liujian8654562
 * 默认添加文件和屏幕log输出
 */

public class LjLogger {
    // true: 输出log到屏幕，false:不输出
    public static boolean isLog = false;
    public static LjLogger instance = new LjLogger();
    private List<ILog> logs;

    private LjLogger() {

        logs = new ArrayList<>();
        if (isLog) {
            //添加文件打印
            logs.add(new FileLog(Config.LOG_DIR));
//            //添加屏幕打印
//            logs.add(new WindowLog());
        }
    }

    public void installLog(ILog log) {
        if (!logs.contains(log)) {
            logs.add(log);
        }
    }

    /**
     * @author 刘建
     * @time 2018/12/5  4:56 PM
     * @describe 初始化所有的log对象
     */
    public LjLogger init(Application context) {
        for (ILog log : logs) {
            log.init(context);
        }
        return this;
    }

    /**
     * @author 刘建
     * @time 2018/12/5  4:12 PM
     * @describe 输出日志
     */
    public void log(String tag, String msg) {
        Log.e(tag, msg);
        if (isLog) {
            for (ILog log : logs) {
                log.printLog(tag, msg);
            }
        }
    }

    /**
     * @author 刘建
     * @time 2018/12/5  4:58 PM
     * @describe 关闭所有的log，一般用在关闭整个app的时候使用
     */
    public void close() {
        for (ILog l : logs) {
            l.close();
        }
    }
}
