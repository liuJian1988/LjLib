package com.lj.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：liujian on 2018/12/5 16:17
 * 邮箱：15313727484@163.com
 */
public class LjStringUtil {
    /**
     * @param src   字符串数据源
     * @param dsc   查找的字符串
     * @param index dsc出现的位置
     * @author 刘建
     * @time 2018/12/5  4:18 PM
     * @describe
     */
    public static int getCharacterPosition(String src, String dsc, int index) {
        //这里是获取dsc符号的位置
        Matcher slashMatcher = Pattern.compile(dsc).matcher(src);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            //当"/"符号第三次出现的位置
            if (mIdx == index) {
                break;
            }
        }
        return slashMatcher.start();
    }

    /**
     * @param num 需要规范的字符串
     *            把数字字符串中的空格、回车、换行符等替换空字符
     * @author 刘建
     * @time 2019/3/4  3:59 PM
     * @describe
     */
    public static String formatNumString(String num) {
        String ret = num;
        if(TextUtils.isEmpty(ret)){
            return "";
        }
        if (ret.contains(" ")) {
            ret = ret.replace(" ", "");
        }
        if (ret.contains("\r")) {
            ret = ret.replace("\r", "");
        }
        if (ret.contains("\n")) {
            ret = ret.replace("\n", "");
        }
        return ret;
    }
}
