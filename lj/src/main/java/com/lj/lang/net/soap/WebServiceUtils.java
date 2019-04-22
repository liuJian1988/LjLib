package com.lj.lang.net.soap;

/**
 * 作者：liujian on 2018/6/27 17:55
 * 邮箱：15313727484@163.com
 */

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;


/**
 * Created by Administrator on 2015/11/5.
 */
public class WebServiceUtils {


    /**
     * @param url                WebService服务器地址
     * @param methodName         WebService的调用方法名
     * @param properties         WebService的参数
     * @param webServiceCallBack 回调接口
     */
    public static void callWebService(String url, String nameSpace, final String methodName, String soapAction,
                                      Map<String, String> properties,
                                      final WebServiceCallBack webServiceCallBack, Executor executor, boolean file) {
        soapAction = soapAction + methodName;
        //创建HttpTransportSE对象，传递WebService服务器地址
        final HttpTransportSE httpTransportSE = new HttpTransportSE(url);
        //创建SoapObject对象
        SoapObject soapObject = new SoapObject(nameSpace, methodName);
        //SoapObject添加参数
        if (properties != null) {
            for (Iterator<Map.Entry<String, String>> it = properties.entrySet()
                    .iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                soapObject.addProperty(entry.getKey(), entry.getValue());
            }
        }
        //实例化SoapSerializationEnvelope,传入WebService的SOAP协议的版本号
        final SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        (new MarshalBase64()).register(soapSerializationEnvelope);
        //设置是否调用的是.net开发的WebService
        soapSerializationEnvelope.setOutputSoapObject(soapObject);
        soapSerializationEnvelope.dotNet = true;
        httpTransportSE.debug = true;

        //开启线程去访问WebService
        String finalSoapAction = soapAction;
        executor.execute(new Runnable() {
            SoapObject resultSoapObject = null;

            @Override
            public void run() {

                try {
                    httpTransportSE.call(finalSoapAction, soapSerializationEnvelope);
                    if (soapSerializationEnvelope.getResponse() != null) {
                        //获取服务器响应返回的SoapObject
                        resultSoapObject = (SoapObject) soapSerializationEnvelope.bodyIn;
                    }
                } catch (HttpResponseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } finally {
                    //将获取的消息利用Handler发送的主线程
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
                    if (!file) {
                        webServiceCallBack.callBack(resultSoapObject);
                    } else {
                        webServiceCallBack.downLoadFileCallBack(resultSoapObject);
                    }
//                        }
//                    });
                }
            }
        });
    }

    public static void callWebService(String url, String nameSpace, String m, String action, Map<String, String> properties, WebServiceCallBack webServiceCallBack, Executor executor) {
        callWebService(url, nameSpace, m, action, properties, webServiceCallBack, executor, false);
    }

//    public byte[] downloadFile(String strUrl, String strFileID)
//            throws IOException, XmlPullParserException {
//        byte[] bResponse = null;
//        HttpTransportSE ht = null;
//
//        try {
//            System.out.println("----------start---------");
//            String serviceNamespace = strUrl;
//            String methodName = "downloadFile";
//            String serviceURL = strUrl;
//
//            SoapObject request = new SoapObject(serviceNamespace, methodName);
//
//            request.addProperty("FileID", strFileID);
//
//            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//
//            envelope.bodyOut = request;
//            (new MarshalBase64()).register(envelope);
//
//            ht = new HttpTransportSE(serviceURL);
//
//            ht.debug = true;
//            ht.call(null, envelope);
//
//            bResponse = (byte[]) envelope.getResponse(); //这里总是报ClassCastException</span>
//            Object Response = envelope.getResult();
//            byte[] by = (byte[]) Response;
//        } finally {
//            if (ht != null) {
//                System.out.println("request dump>>" + ht.requestDump);
//                System.out.println("response dump>>" + ht.responseDump);
//            }
//        }
//
//        return bResponse;
//    }

    public interface WebServiceCallBack {
        void callBack(SoapObject result);

        void downLoadFileCallBack(SoapObject data);
    }
}
