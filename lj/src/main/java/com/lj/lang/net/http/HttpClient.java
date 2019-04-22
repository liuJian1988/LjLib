package com.lj.lang.net.http;


import com.lj.lang.develop.log.LjLogger;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author 刘建
 * @date 2017-8-21
 * @copyright lj
 * @email 1187502892@qq.com
 */
public class HttpClient {
    private String TAG = "HttpClient";
    byte[] buffer = new byte[1024];
    private String mUrl;

    public HttpClient(String url) {
        this.mUrl = url;
    }

//    public String downloadStringData2() {
//        return MyOkHttpClient.getInstance().getDataFromNet(mUrl);
//    }

    /**
     * 获取字符串
     *
     * @return
     */
    public String downloadStringData() {
        StringBuffer strBuffer = new StringBuffer();
        InputStream in = null;
        HttpURLConnection connection = null;
        URL url = null;
        try {
            url = new URL(mUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.connect();
            in = connection.getInputStream();
            int count = 0;
            while ((count = in.read(buffer)) != -1) {
                String strTmp = new String(buffer, 0, count);
                strBuffer.append(strTmp);
                LjLogger.instance.log(TAG, strTmp);
            }

        } catch (Exception e) {
            // TODO Auto-geated catch block
            e.printStackTrace();
            LjLogger.instance.log(TAG, e.getMessage());

        } finally {
            try {
                in.close();
                connection.disconnect();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        LjLogger.instance.log(TAG, "http result " + strBuffer.toString());
//        Log.e(TAG, "a");

        return strBuffer.toString();
    }

}
