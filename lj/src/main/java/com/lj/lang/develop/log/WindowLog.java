package com.lj.lang.develop.log;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.lj.app.LjEnvironment;
import com.lj.util.LjStringUtil;


public class WindowLog extends AbsLog {
    private static final String TAG = WindowLog.class.getSimpleName();
    private TextView tv_show;
    private int maxLine;
    private int lineNum = 1;
    private StringBuilder sb = new StringBuilder();
    private WindowManager wm;

    @Override
    public void init(Application context) {
        super.init(context);
        wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        makeNewSystemWindow();
    }

    /**
     * @author 刘建
     * @time 2018/12/5  4:24 PM
     * @describe 创建View并且初始化window
     */
    public void makeNewSystemWindow() {
        tv_show = new TextView(context);
        tv_show.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tv_show.setTextColor(Color.parseColor("#ffff4444"));
        tv_show.setBackgroundColor(Color.BLACK);
        tv_show.setMovementMethod(ScrollingMovementMethod.getInstance());
        LayoutParams lp = new LayoutParams();
        lp.type = LayoutParams.TYPE_APPLICATION_OVERLAY;
        lp.width = 600;
        lp.height = 600;
        lp.gravity = Gravity.RIGHT | Gravity.TOP;
        wm.addView(tv_show, lp);
    }

    /**
     * @author 刘建
     * @time 2018/12/5  4:24 PM
     * @describe 更新View
     */
    public void upDateView(String msg) {
        sb.append(" NO" + lineNum + ": " + msg + "\n");
        tv_show.setText(sb.toString());

        //移动textView
        if (maxLine == 0) {
            maxLine = tv_show.getHeight()
                    / tv_show.getLineHeight();
        }
        int defLines = tv_show.getLineCount() - maxLine;
        int y = tv_show.getLineHeight()
                * defLines;
        Log.e(TAG, "scrollTo " + y);
        if (y < 0)
            y = 0;
        tv_show.scrollTo(0, y);

        //截取字符串,防止内存溢出导致崩溃，我们只缓存2行的数据，否则无限制保存下去，内存会崩溃的
        if (defLines > 2) {
            int position = LjStringUtil.getCharacterPosition(sb.toString(), "NO", 2);
            sb.delete(0, position);
        }
        if (lineNum == Integer.MAX_VALUE) {
            lineNum = 0;
        }
        //增长行号
        lineNum++;

    }

    @Override
    public void printLog(String tag, String logStr) {
        super.printLog(tag, logStr);
        LjEnvironment.platform.getAppExecutors().mainThread().execute(() -> {
            upDateView(logStr);
        });
    }

    @Override
    public void close() {
        super.close();
        wm.removeView(tv_show);
    }
}
