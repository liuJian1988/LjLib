package com.lj.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

/**
 * @author LiuJian:
 * @version 创建时间：2017-5-23 上午10:14:41
 * 类说明
 */
public class DataConverter {

    /**
     * @return intValue转换成4个字节的数组, 高位在前
     * @category int类型转换成4个字节的数组
     */
    public static final byte[] integerToByteArry(int intValue) {
        byte[] retBytes = new byte[4];
        int temp = intValue & 0xFF000000;
        retBytes[0] = (byte) (temp >>> 24);

        temp = intValue & 0x00FF0000;
        retBytes[1] = (byte) (temp >> 16);

        temp = intValue & 0x0000FF00;
        retBytes[2] = (byte) (temp >> 8);

        temp = intValue & 0x000000FF;
        retBytes[3] = (byte) temp;
        return retBytes;
    }

    public static final byte[] intToByteArray(int i) {
        byte[] result = new byte[2];
        result[1] = (byte) ((i >> 8) & 0xFF);
        result[0] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * @param bytes 要转换成整形（Integer）类型的字节数组,(要求高位在前)
     * @return bytes转换成整形的值
     * @category byte数组转化int
     */
    public static final int getIntFromBytes(byte[] bytes) {
        int iRetValue = 0;
        if (bytes != null && bytes.length == 4) {
            int h0 = 0;
            h0 = bytes[0] << 24 & 0xFF000000;
            int h1 = 0;
            h1 = (bytes[1] << 16) & 0x00FF0000;
            int l0 = 0;
            l0 = (bytes[2] << 8) & 0x0000FF00;
            int l1 = 0;
            l1 = bytes[3] & 0x000000FF;
            iRetValue = h0 + h1 + l0 + l1;
        } else if (bytes != null && bytes.length == 2) {
            int l0 = 0;
            l0 = (bytes[0] << 8) & 0x0000FF00;
            int l1 = 0;
            l1 = bytes[1] & 0x000000FF;
            iRetValue = l0 + l1;
        }
        return iRetValue;
    }

    public static final int[] getIntArrayFromBytes(byte[] bytes) {
        int bytelen = bytes.length;
        int intLenth = 1;
        if (bytelen != 0) {
            if (bytelen % 4 == 0) {
                intLenth = bytelen / 4;
            } else {
                intLenth = bytelen / 4 + 1;
            }
        }
        int[] retInts = new int[intLenth];
        int h1 = 0;
        int h0 = 0;
        int l1 = 0;
        int l0 = 0;

        for (int i = 0; i < (bytelen - bytelen % 4); ) {
            l0 = bytes[i];
            l1 = (bytes[i + 1] << 8) & 0x0000FF00;
            h0 = (bytes[i + 2] << 16) & 0x00FF0000;
            h1 = (bytes[i + 3] << 24) & 0xFF000000;
            retInts[i / 4] = h0 + h1 + l0 + l1;
            i += 4;
        }

        for (int i = 0; i < bytelen % 4; i++) {
            retInts[intLenth - 1] += bytes[bytelen - i - 1]
                    & 0xff << ((bytelen % 4 - i - 1) * 8);
        }

        return retInts;
    }

    /**
     * @return bytes转换成整形的值
     * @category byte转化为16进制数显示格式的字符串
     * 要转换成整形（Integer）类型的字节数组,(要求高位在前)
     */
    public static final String byteToHexString(byte b) {
        String hex = new String();

        hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }

        return hex;
    }

    /**
     * @category 保留小数点后2位
     */
    public static String getDecimal(double d) {
        DecimalFormat df2 = new DecimalFormat("0.00");
        return df2.format(d);
    }

    /**
     * @param databytes
     * @param spliter
     * @return
     * @category 将字节数组转换为gbk编码的字符串，并将字符串按照指定分割符分开，返回子字符串
     */
    public static String[] get_Substr_from_bytes(byte[] databytes,
                                                 String spliter) {
        String str = null;
        String[] substr = null;

        if (databytes != null && databytes.length > 0) {
            try {
                str = new String(databytes, "gbk");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            int lenth = 0;
            try {
                lenth = str.split(spliter).length;
                substr = new String[lenth];
                substr = str.split(spliter);
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
        return substr;
    }

    public static String[] get_substr_from_str(String str, String spliter) {
        if (str != null) {

            return str.split(spliter);
        }
        return null;
    }

    /**
     * @param data
     * @param filepath
     * @category 将字节数组存为文件内容
     */
    public static void creat_file_from_bytes(byte[] data, String filepath) {
        File receiveFile = new File(filepath);
        if (receiveFile.exists()) { // 若对应文件名的文件已存在，则删除原来的文件
            receiveFile.delete();
        }
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(filepath);
            fos.write(data);
        } catch (Exception e) {
            // TODO: handle exception

        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {

                }

            }
        }
    }

    /**
     * @param filename
     * @return
     * @category 将文件内容读到字节数组返回
     */
    public static byte[] get_bytes_from_file(String filename) {

        FileInputStream fis = null;
        byte[] buffer = new byte[1024];
        int rlen = 0, lenth = 0;
        try {
            fis = new FileInputStream(filename);
            while ((rlen = fis.read(buffer)) != -1) {
                lenth += rlen;
            }
            byte[] data_bytes = new byte[lenth];
            fis.read(data_bytes);
            fis.close();
            return data_bytes;
        } catch (Exception e) {
            // handle exception
            Log.i("DataConverter", "get_bytes_from_file failed");
        }

        return null;
    }

    /**
     * @param str
     * @category 判断str 是不是纯数字字符串（在字符串转int型前，判断一下，可以减少出错）
     * @author hxy
     */
    public static boolean string_isDigital(String str) {
        boolean isDigital = true;
        int num_size = str.length();
        if (num_size > 0) {
            for (int j = 0; j < num_size; j++) {
                if (!Character.isDigit(str.charAt(j))) {
                    isDigital = false;
                    break;
                }
            }
        } else {
            isDigital = false;
        }

        return isDigital;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param scale   （DisplayMetrics类中属性density）
     * @return
     */

    public static int px2dip(float pxValue, float scale) {
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * @param scale （DisplayMetrics类中属性density）
     * @return
     * @category 将dip或dp值转换为px值，保证尺寸大小不变
     */
    public static int dip2px(float dipValue, float scale) {
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * @param i
     * @return
     * @category int型转换为ip地址字符串
     */
    public static String intToIp(int i) {

        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }

    public static int getIntFromByte1(byte[] a) {
        int mask = 0xff;
        int temp = 0;
        int n = 0;
        for (int i = 0; i < 4; i++) {
            n <<= 8;
            temp = a[i] & mask;
            n |= temp;
        }
        return n;
    }

    public static void main(String[] args) {
        Integer b = 10;
        byte a[] = intToByteArray(b);
        Log.i("DataConverter", "------" + a.length);
        System.out.println(getIntFromByte1(a));
    }
}
