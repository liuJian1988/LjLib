package com.lj.util;


import com.lj.app.AppExecutors;
import com.lj.device.IP;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Liujian on 2017/8/21 0021.
 */

public class NetUtil {


    /**
     * 获取本机ip地址
     */
    public static String getLocalIpAddress(AppExecutors AppExecutors) {
//        try {
//            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
//            while (en.hasMoreElements()) {
//                NetworkInterface nif = en.nextElement();
//                Enumeration<InetAddress> enumIpAddr = nif.getInetAddresses();
//                while (enumIpAddr.hasMoreElements()) {
//                    InetAddress mInetAddress = enumIpAddr.nextElement();
//                    if (!mInetAddress.isLoopbackAddress()
//                            && isIPv4Address(mInetAddress.getHostAddress())) {
//                        return mInetAddress.getHostAddress().toString();
//                    }
//                }
//            }
//        }toString catch (SocketException ex) {
//            Log.e("IpAndPort", "获取本地IP地址失败");
//        }
//        {//获取 以太网ip 的方法, 需要源码环境编译, 文章后面有下载地址;
//            ConnectivityManager connectivityManager = (ConnectivityManager) AndroidApplication.getMyApplicationContext()
//                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            LinkProperties properties = connectivityManager
//                    .getLinkProperties(ConnectivityManager.TYPE_ETHERNET);
//
//            if (properties != null) {
//                String ipString = properties.getLinkAddresses().toString();
//
//                Pattern pattern = Pattern.compile("\\d+.\\d+.\\d+.\\d+");
//                Matcher matcher = pattern.matcher(ipString);
//                if (matcher.find()) {
//                    return matcher.group();
//                }
//            }
//        }
//        ConnectivityManager cm = (ConnectivityManager)
//                AndroidApplication.getMyApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfonetworkInfo = cm.getActiveNetworkInfo();
//        if (activeNetworkInfonetworkInfo.isConnected() && activeNetworkInfonetworkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
////            AndroidApplication.getMyApplicationContext().
//        }
//        Runnable r = new Runnable() {
//
//            @Override
//            public void run() {
//                byte[] buffer = new byte[1024];
//                Process p = null;
//                InputStream ins = null;
//                try {
//                    p = Runtime.getRuntime().exec("ip -f inet addr");
//                    p.waitFor();
//                    ins = p.getInputStream();
//                    int count = ins.read(buffer);
//                    String result = new String(buffer, 0, count);
//                    parseAddress(result);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        ins.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
        Runnable r = new MyRunnable("ip -f inet addr", new IExecuteCMDCallBack() {
            @Override
            public void callback(String r) {
                parseAddress(r);
            }
        });
        AppExecutors.diskIO().execute(r);
        return "192.168.8.222";
    }

    private static void parseAddress(String result) {
//                    1: lo: <LOOPBACK,UP,LOWER_UP> mtu 16436 qdisc noqueue state UNKNOWN
//                    inet 127.0.0.1/8 scope host lo
//                    3: eth0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast state UP qlen 1000
//                    inet 192.168.8.222/24 brd 192.168.8.255 scope global eth0l

//        1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN
//        inet 127.0.0.1/8 scope host lo
//        valid_lft forever preferred_lft forever
//        3: wlan0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1400 qdisc pfifo_fast state UP qlen 1000
//        inet 192.168.11.15/24 brd 192.168.11.255 scope global wlan0
//        valid_lft forever preferred_lft forever

        String[] results = result.split("\n");
        String eth0 = "";
        for (String e : results) {
            if (e.contains("192.168.")) {
                eth0 = e.trim();
            }
        }
        int beginIndex = eth0.indexOf(" ");
        int endIndex = eth0.indexOf("/");
        if (beginIndex < 0 || endIndex < 0) {
            return;
        }
        eth0 = eth0.substring(beginIndex, endIndex).trim();
        IP.getDeviceIP().setIp(eth0);
    }

    public static boolean isIPv4Address(String hostAddress) {
        if (hostAddress.equals("127.0.0.1")) {
            return false;
        }
        String[] data = hostAddress.split(".");
        if (data.length == 4) {
            for (int i = 0; i < data.length; i++)
                try {
                    int num = Integer.parseInt(data[i]);
                    if (num < 0 || num > 255) {
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            return true;
        }
        return false;
    }

    public static boolean checkPortIsClose(String port) {
        final boolean[] f = {false};
        Runnable r = new MyRunnable("netstat", new IExecuteCMDCallBack() {
            @Override
            public void callback(String r) {

//                String[] results = r.split("\n");
//                for (String res : results) {
                if (r.contains(port)) {
                    f[0] = false;
                    return;
                }
//                }
                f[0] = true;
            }
        });
        r.run();
        return f[0];
    }

    public static class MyRunnable implements Runnable {
        private String cmd = "";
        private IExecuteCMDCallBack cmdCallBack;

        public MyRunnable(String cmd, IExecuteCMDCallBack cmdCallBack) {
            this.cmd = cmd;
            this.cmdCallBack = cmdCallBack;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            Process p = null;
            InputStream ins = null;
            try {
                p = Runtime.getRuntime().exec(cmd);
                p.waitFor();
                ins = p.getInputStream();
                int count = ins.read(buffer);
                String result = new String(buffer, 0, count);
//                parseAddress(result);
                cmdCallBack.callback(result);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface IExecuteCMDCallBack {
        void callback(String r);
    }
}
