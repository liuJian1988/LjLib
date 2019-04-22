package com.lj.device;

/**
 * 作者：liujian on 2019/4/18 12:07
 * 邮箱：15313727484@163.com
 */
public class IP {
    private String ip;

    private IP() {
    }

    public static IP getDeviceIP() {
        return IPHolder.ip;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    private static class IPHolder {
        private static final IP ip = new IP();
    }
}
