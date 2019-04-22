package com.lj.lang.net.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Liujian on 2018/2/7 0007.
 *
 * @link http://blog.csdn.net/liujian8654562
 */

public class TcpClient implements ITcpClient {
    private String ip = "";
    private int port;
    private Socket s;

    public String getIp() {
        return ip;
    }


    public int getPort() {
        return port;
    }


    public TcpClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public int initial() {
        return 0;
    }

    public int connect() {
        try {

            InetSocketAddress iad = new InetSocketAddress(
                    ip, port);
            s = new Socket();
            s.connect(iad, 5000);
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public int send(byte[] data, OnDataSendResultListener l) {
        OutputStream os = null;
        try {
            os = s.getOutputStream();
            os.write(data);
            os.flush();
            os.close();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int close() {
        try {
            s.close();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int receive() {
        return 0;
    }

    @Override
    public void setOnDataReceiveListener(OnDataReceiveListener l) {

    }

    @Override
    public void setOnDataSendResultListener(OnDataSendResultListener l) {

    }

    @Override
    public boolean isConnect() {
        return s.isConnected();
    }
}
