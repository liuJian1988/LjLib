package com.lj.util;

/**
 * 作者：liujian on 2019/3/4 13:48
 * 邮箱：15313727484@163.com
 */
public class TypeUtil {
    public static boolean checkIsInt(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
