package com.lj.lang.net.tcp;

/**
 * 作者：liujian on 2018/6/28 17:26
 * 邮箱：15313727484@163.com
 */
public interface ITcpClient {
    int initial() throws Exception;

    int connect();

    int send(byte[] data, OnDataSendResultListener l);

    int close();

    int receive();

    String getIp();

    int getPort();

    void setOnDataReceiveListener(OnDataReceiveListener l);

    void setOnDataSendResultListener(OnDataSendResultListener l);

    boolean isConnect();

    interface OnDataReceiveListener {
        void dataReceive(byte[] data, int count);
    }

    /**
     * @author 刘建
     * @time 2018/12/15  4:11 PM
     * @describe 用于tcp的发送结果回执
     */
    interface OnDataSendResultListener {
        void result(int ret);
    }

}
