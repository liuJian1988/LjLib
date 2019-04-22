package com.lj.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtil {

    public static int[] getWH(Context context) {
        int[] wh = new int[2];
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        wh[0] = outMetrics.widthPixels;
        wh[1] = outMetrics.heightPixels;
        return wh;
    }

    /**
     * @author 刘建
     * @time 2019/3/19  4:35 PM
     * @describe dp转换为像素
     */
    public static int dpToPx(Context context, int dp) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        return (int) (outMetrics.density * dp);
    }
}
