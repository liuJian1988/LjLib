package com.lj.lang.develop.log;

import com.lj.app.LjEnvironment;
import com.lj.util.AppUtil;

/**
 * 作者：liujian on 2018/12/5 15:22
 * 邮箱：15313727484@163.com
 */
public class Config {
    //
    public static String LOG_DIR = LjEnvironment.sdcardRootDir
            + AppUtil.getAppPkgName(LjEnvironment.platform.getApplication()) + "/log";
}
