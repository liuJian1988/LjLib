package com.lj.lang.net.tcp;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.lj.app.BaseLjApp;
import com.lj.lang.develop.log.LjLogger;
import com.lj.util.ThreadUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 作者：liujian on 2018/6/28 17:22
 * 邮箱：15313727484@163.com
 */
public class LongConnectTcpClient implements ITcpClient {

    private final String TAG = LongConnectTcpClient.class.getSimpleName();
    private final int CONNECT_TIME_OUT = 15 * 1000;
    private final int SO_TIME_OUT = 10 * 1000;
    private final int WAITE_TIME = 10 * 1000;
    private String ip = "";
    private int port;
    private boolean flag = true;

    private byte[] buffer;
    private Socket s;
    private volatile OutputStream os = null;
    private volatile InputStream in = null;
    private OnDataReceiveListener listener;
    private OnDataSendResultListener sendResultListener;

    private HandlerThread hThread;
    private Handler mHandler;
//    private NetGuarder netGuarder;

    private Thread thread;


    public String getIp() {
        return ip;
    }


    public int getPort() {
        return port;
    }


    public LongConnectTcpClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        buffer = new byte[7];
    }

    @Override
    public int initial() throws Exception {

        Log.e(TAG, "初始化 initial");
        flag = true;

        //1、建立服务器连接
        connect();
        //2、开启非主线称loop
        hThread = new HandlerThread(LongConnectTcpClient.class.getSimpleName());
        hThread.start();
        mHandler = new H(hThread.getLooper(), this);

//        netGuarder = new NetGuarder(mHandler);
//        //维护网络连接我们使用单独的线程。
//        mHandler.post(netGuarder);

        //3、开启数据接收线程，接收数据
        thread = new Thread(new NetReceiver());
        thread.start();

        Log.e(TAG, "初始化 完成");
        return 0;
    }

    /**
     * 连接网络
     *
     * @return
     */
    public int connect() {
        if (!flag) return -2;
        try {
            InetSocketAddress iad = new InetSocketAddress(
                    ip, port);
            s = new Socket();
            s.setSoLinger(true, 0);
            s.setKeepAlive(true);
            s.connect(iad, CONNECT_TIME_OUT);
//            s.setSoTimeout(SO_TIME_OUT);
            os = s.getOutputStream();
            in = s.getInputStream();
            Log.e(TAG, "建立tcp长连接 ip：" + ip);
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "建立tcp长连接失败：" + e.getMessage());
            return -1;
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(TAG, "建立tcp长连接失败：" + e2.getMessage());
            return -1;
        }

    }

    public int receive() {
        while (flag) {

            try {
                Log.e(TAG, "等待接收数据 receive");
                int count = in.read(buffer);
                byte[] data = new byte[count];
                System.arraycopy(buffer, 0, data, 0, count);
                if (count > 0) {
                    if (listener != null) {
//                        //使用单独线程处理收到的数据
//                        BasicApp.getApp().getAppExecutors().networkIO().execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                listener.dataReceive(data, count);
//                            }
//                        });
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                LjLogger.instance.log(TAG, "开始分发数据");
                                listener.dataReceive(data, count);
                                LjLogger.instance.log(TAG, "数据分发成功");
                            }

                        });
                    }
                } else {
                    Log.e(TAG, "接收到数据 -1");
                }
                Log.e(TAG, "接收到数据 receive");
            } catch (Exception e) {
                e.printStackTrace();
                //等待重连，不做无用的循环
//                ThreadUtil.ThreadSleep(WAITE_TIME * 2);
                Log.e(TAG, "接收到数据 报错 : " + e.getLocalizedMessage());
                //检查网络情况
                if (flag) {
                    check();
                } else {
                    break;
                }
            }
        }
        return 0;
    }

    public int send(byte[] data, OnDataSendResultListener l) {//异步线程发送防止出现线程阻塞问题
        this.sendResultListener = l;
        if (mHandler == null) {
            return -1;
        }
        BaseLjApp.getAppContext().getAppExecutors().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                int ret = send(data, false);
                if (ret < 0) {
                    ret = send(data, false);
                }
                if (sendResultListener != null) {
                    sendResultListener.result(ret);
                }
            }
        });
        return 0;
    }

    public int send(byte[] data, boolean must) {
        if (s == null || os == null) {
            return -1;
        }
        try {
            os.write(data);
            os.flush();
            LjLogger.instance.log(TAG, "发送数据：ok");
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            LjLogger.instance.log(TAG, "发送数据：" + e.getMessage());
            check();
        }

        if (must) {//会一直不停的连接服务器，直到连接成功并发送出去

            //因为是同步方法所以为了防止发送阻塞，我们使用其他线程处理
            Message m = mHandler.obtainMessage();
            m.what = 0;
            m.obj = data;
            mHandler.sendMessage(m);//继续连接
        }
        return -1;
    }


    private synchronized int check() {
        if (!isConnect()) {
            //1、重新tcp连接
            return connect();
        }
        return 0;
    }

    /**
     * 三次验证：
     * 第一次：看看数据流和socket是不是空的。
     * 第二次：看看socket是否还在连接中
     * 第三次：使用os写数据，看看通道是否真的可以发送数据
     *
     * @return
     */
    @Override
    public boolean isConnect() {

        if (os == null || s == null) {
            return false;
        }

        if (!s.isConnected()) {
            return false;
        }

        try {
            os.write(0);
            os.flush();
            ThreadUtil.ThreadSleep(500);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int close() {

        //1、先关闭当前持有的线程，并等待它的结束
        closeThread();
        return closeSocket();
    }

    private void closeThread() {
        //1、关闭接收线程
        flag = false;
        if (thread != null) {
            thread.interrupt();
        }
        //2、关闭网络维护线程
        if (hThread != null) {
            hThread.quit();
        }
    }

    private int closeSocket() {
        try {
            if (s != null) {
                os.close();
                in.close();
                s.close();

                Log.e(TAG, "关闭socket 成功");
                os = null;
                in = null;
                s = null;
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "关闭socket 失败");
            return -1;
        }
    }


    public void setOnDataReceiveListener(OnDataReceiveListener l) {
        this.listener = l;
    }

    @Override
    public void setOnDataSendResultListener(OnDataSendResultListener l) {
        this.sendResultListener = l;
    }

    private static class H extends Handler {
        private WeakReference<LongConnectTcpClient> client;

        H(Looper l, LongConnectTcpClient client) {
            super(l);
            this.client = new WeakReference(client);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://处理发送数据失败问题

                    break;
            }
        }
    }


//    private class NetGuarder implements Runnable {
//        private Handler mH;
//
//        public NetGuarder(Handler h) {
//            mH = h;
//        }
//
//        @Override
//        public void run() {
//            check();//检测tcp状态
//            mH.postDelayed(this, WAITE_TIME);
//        }
//    }

    private class NetReceiver implements Runnable {
        @Override
        public void run() {
            //接收数据
            receive();
        }
    }
}
