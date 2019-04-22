package com.lj.lang.develop.log;

import android.app.Application;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

import static com.lj.util.FileUtil.openFileBufferedWriter;

/**
 * 作者：liujian on 2018/12/5 14:37
 * 邮箱：15313727484@163.com
 */
public class FileLog extends AbsLog {

    private String dir = "";
    private String fileName = "";
    private BufferedWriter bufferedWriter;


    public FileLog(String logDir) {
        Date date = calendar.getTime();
//        dir = Environment.getExternalStorageDirectory() + "/flx/log";
        dir = logDir;
        fileName = date.getMonth() + "/" + date.getDate();
    }

    @Override
    public void init(Application context) {
        bufferedWriter = openFileBufferedWriter(dir, fileName);
    }

    @Override
    public void printLog(String tag, String msg) {
        super.printLog(tag, msg);

        try {
            //将log写入文件中
            if (bufferedWriter != null) {
                String log_str = calendar.getTime().toGMTString()
                        + " : " + tag
                        + " : " + msg
                        + "\n";
                bufferedWriter.write(log_str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
