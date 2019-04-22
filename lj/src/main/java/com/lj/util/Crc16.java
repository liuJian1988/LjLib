package com.lj.util;

/**
 * @author LiuJian:
 * @version 创建时间：2017-5-23 上午9:41:30
 * 类说明
 */
public class Crc16 {

    /**
     * @category 高8位
     */
    private static final byte[] auchCRCHi = {0, -63, -127, 64, 1, -64, -128,
            65, 1, -64, -128, 65, 0, -63, -127, 64, 1, -64, -128, 65, 0, -63,
            -127, 64, 0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65, 0,
            -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127,
            64, 1, -64, -128, 65, 1, -64, -128, 65, 0, -63, -127, 64, 1, -64,
            -128, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65, 0,
            -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65, 0, -63, -127,
            64, 0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128, 65, 0, -63,
            -127, 64, 1, -64, -128, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1,
            -64, -128, 65, 1, -64, -128, 65, 0, -63, -127, 64, 0, -63, -127,
            64, 1, -64, -128, 65, 0, -63, -127, 64, 1, -64, -128, 65, 1, -64,
            -128, 65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65, 1,
            -64, -128, 65, 0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127,
            64, 0, -63, -127, 64, 1, -64, -128, 65, 0, -63, -127, 64, 1, -64,
            -128, 65, 1, -64, -128, 65, 0, -63, -127, 64, 1, -64, -128, 65, 0,
            -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65, 1, -64, -128,
            65, 0, -63, -127, 64, 0, -63, -127, 64, 1, -64, -128, 65, 0, -63,
            -127, 64, 1, -64, -128, 65, 1, -64, -128, 65, 0, -63, -127, 64};

    /**
     * @category 低8位
     */
    private static final byte[] auchCRCLo = {0, -64, -63, 1, -61, 3, 2, -62,
            -58, 6, 7, -57, 5, -59, -60, 4, -52, 12, 13, -51, 15, -49, -50, 14,
            10, -54, -53, 11, -55, 9, 8, -56, -40, 24, 25, -39, 27, -37, -38,
            26, 30, -34, -33, 31, -35, 29, 28, -36, 20, -44, -43, 21, -41, 23,
            22, -42, -46, 18, 19, -45, 17, -47, -48, 16, -16, 48, 49, -15, 51,
            -13, -14, 50, 54, -10, -9, 55, -11, 53, 52, -12, 60, -4, -3, 61,
            -1, 63, 62, -2, -6, 58, 59, -5, 57, -7, -8, 56, 40, -24, -23, 41,
            -21, 43, 42, -22, -18, 46, 47, -17, 45, -19, -20, 44, -28, 36, 37,
            -27, 39, -25, -26, 38, 34, -30, -29, 35, -31, 33, 32, -32, -96, 96,
            97, -95, 99, -93, -94, 98, 102, -90, -89, 103, -91, 101, 100, -92,
            108, -84, -83, 109, -81, 111, 110, -82, -86, 106, 107, -85, 105,
            -87, -88, 104, 120, -72, -71, 121, -69, 123, 122, -70, -66, 126,
            127, -65, 125, -67, -68, 124, -76, 116, 117, -75, 119, -73, -74,
            118, 114, -78, -77, 115, -79, 113, 112, -80, 80, -112, -111, 81,
            -109, 83, 82, -110, -106, 86, 87, -105, 85, -107, -108, 84, -100,
            92, 93, -99, 95, -97, -98, 94, 90, -102, -101, 91, -103, 89, 88,
            -104, -120, 72, 73, -119, 75, -117, -118, 74, 78, -114, -113, 79,
            -115, 77, 76, -116, 68, -124, -123, 69, -121, 71, 70, -122, -126,
            66, 67, -125, 65, -127, -128, 64};

    public static void CheckCRC16(byte[] value, int len, byte[] result) {
        byte tCRCHi = -1;
        byte tCRCLo = -1;

        for (int i = 0; i < len; ++i) {
            int uIndex = (tCRCHi ^ value[i]) & 0xFF;
            tCRCHi = (byte) (tCRCLo ^ auchCRCHi[uIndex]);
            tCRCLo = auchCRCLo[uIndex];
        }

        result[0] = tCRCHi;
        result[1] = tCRCLo;
    }

    public static String CheckCRC16(byte[] value, int len) {
        byte[] re = new byte[2];
        CheckCRC16(value, len, re);
        String ss = String.format("%1$02x",
                new Object[]{Integer.valueOf(BTI(re[0]))});
        ss = ss
                + String.format("%1$02x",
                new Object[]{Integer.valueOf(BTI(re[1]))});
        return ss;
    }

    private static int BTI(byte val) {
        return (val + 256) % 256;
    }

    public static int[] CheckCRC16int(byte[] value, int len) {
        byte tCRCHi = -1;
        byte tCRCLo = -1;

        for (int i = 0; i < len; ++i) {
            int uIndex = (tCRCHi ^ value[i]) & 0xFF;
            tCRCHi = (byte) (tCRCLo ^ auchCRCHi[uIndex]);
            tCRCLo = auchCRCLo[uIndex];
        }

        int[] tcrc = new int[]{Integer.valueOf(BTI(tCRCLo)),
                Integer.valueOf(BTI(tCRCHi))};
        return tcrc;
    }

    public static byte[] CheckCRC16byte(byte[] value, int offset, int len) {
        byte tCRCHi = -1;
        byte tCRCLo = -1;

        for (int i = 0; i < len; ++i) {
            int uIndex = (tCRCHi ^ value[i]) & 0xFF;
            tCRCHi = (byte) (tCRCLo ^ auchCRCHi[uIndex]);
            tCRCLo = auchCRCLo[uIndex];
        }

        byte[] tcrc = new byte[]{tCRCHi, tCRCLo};
        return tcrc;
    }

}

