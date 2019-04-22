package com.lj.lang.net.soap;

/**
 * 作者：liujian on 2018/6/28 13:23
 * 邮箱：15313727484@163.com
 */
public class SoapParam {

    private String url = "";
    private String nameSpace = "";
    private String action = "";

    public SoapParam(String url, String nameSpace, String action) {
        this.url = url;
        this.nameSpace = nameSpace;
        this.action = action;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

