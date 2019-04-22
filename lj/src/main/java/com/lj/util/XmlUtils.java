package com.lj.util;

/**
 * 作者：liujian on 2019/3/12 18:29
 * 邮箱：15313727484@163.com
 */
public class XmlUtils {
    public static String scale(String html,double scale){
        String[] strArray = html.split(";");
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < strArray.length; i++) {
            if (strArray[i].contains("font-size:")) {
                String[] strArray_ = strArray[i].split(":");
                double fontSize = Double.parseDouble(strArray_[1]);
                fontSize = fontSize * scale;
                strArray[i] = "font-size:" + (int) fontSize;
            }
        }
        for (int i = 0; i < strArray.length; i++) {
            if (i != strArray.length - 1) {
                strBuilder.append(strArray[i] + ";");
            } else {
                strBuilder.append(strArray[i]);
            }
        }
        return strBuilder.toString();
    }
}
