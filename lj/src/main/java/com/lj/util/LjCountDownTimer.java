package com.lj.util;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * 作者：liujian on 2019/3/14 13:28
 * 邮箱：15313727484@163.com
 * 倒计时器，使用毫秒作为单位
 * 默认使用非UI线程，防止长时间占用ui，影响UI渲染，事件响应
 */
public class LjCountDownTimer implements Runnable {
    private static Handler defaultHandler;
    private boolean isRunning = false;//是否正在工作中
    private Handler counter;
    private CallBack callBack;
    private long delay = 0;
    private long period = 0;//总时长
    private long localTime = 0;//收到服务器发来时间段的本地时间
    private long showTime = 0;//显示时间
    private HandlerThread handlerThread;//计时器线程


    private LjCountDownTimer(CallBack callBack) {
        this.callBack = callBack;
        handlerThread = new HandlerThread(this.getClass().getName());
        handlerThread.start();
        defaultHandler = new Handler(handlerThread.getLooper());
        this.counter = defaultHandler;
    }

    public void setCounter(Handler counter) {
        if (counter == null) return;//过滤掉空的handler
        this.counter = counter;
    }

    public static LjCountDownTimer newTimer(CallBack callBack) {
        return new LjCountDownTimer(callBack);
    }

    /**
     * @author 刘建
     * @time 2019/3/14  1:56 PM
     * @describe 开始计时
     */
    public LjCountDownTimer start(long totalTime) {
        this.period = totalTime;
        this.showTime = totalTime;
        this.localTime = System.currentTimeMillis();
        startCountDown();

        isRunning = true;

        return this;
    }

    private void startCountDown() {
        counter.post(this);
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void run() {

        long startTime = System.currentTimeMillis();

//        //换算成时间的字符串
//        long totalSecond = (showTime / 1000);
//        //小时
//        int hour = (int) totalSecond / (60 * 60);
//        //分钟
//        int min = (int) totalSecond % (60 * 60) / 60;
//        //秒
//        int second = (int) totalSecond % 60;
//
//        //拼装成时间字符串
//        String strSthowTime = (hour == 0 ? "" : ("" + hour + sp)) //小时
//                + (min == 0 ? "" : ("" + min + sp)) //分钟
//                + (second < 10 ? ("0" + second) : ("" + second));//秒
//
//        fayan_time.setText(strSthowTime);
        if (callBack != null) callBack.onSecond(showTime);

        showTime -= 1000;//倒计时的时间
        delay = 1000 - (System.currentTimeMillis() - startTime);//花费的时间

        //如果超过了服务器设置的总时间，我们就要退出来了
        if ((System.currentTimeMillis() - localTime) >= period) {
//            Message msg = mTimerHandler.obtainMessage();
//            //发言时间到了
//            mHandler.sendEmptyMessage(0);
            callBack.onComplete();
            cancel();
            return;
        }
        //去掉执行计算界面刷新的时间
        counter.postDelayed(this, delay);

    }

    /**
     * @author 刘建
     * @time 2019/3/14  1:53 PM
     * @describe 取消并重置计时器
     * 可使用@start 方法继续使用
     */
    public void cancel() {

        counter.removeCallbacks(this);
        delay = 0;
        period = 0;
        localTime = 0;
        showTime = 0;

        isRunning = false;
    }

    /**
     * @author 刘建
     * @time 2019/3/14  1:55 PM
     * @describe 关闭掉计时器后就不能够再次使用了，必须重新new一个
     */
    public void close() {
        cancel();
        handlerThread.quit();
    }


    public interface CallBack {
        void onSecond(long restTime);

        void onComplete();
    }
}
