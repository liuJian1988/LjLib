package com.lj.lang.net.soap;

import com.lj.app.IExecutors;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：liujian on 2018/6/28 13:20
 * 邮箱：15313727484@163.com
 */
public class SoapClient {
    private SoapParam param;
    private IExecutors executors;
    private HashMap<String, String> properties = new HashMap();


    public SoapClient(SoapParam param, IExecutors executors) {
        this.param = param;
        this.executors = executors;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public <T> SoapClient executeRemoteMethod(String m, Class<T> c, Map<String, String> properties, SoapCallBack callBack) {
        WebServiceUtils.callWebService(param.getUrl(), param.getNameSpace(), m, param.getAction(), properties, new WebServiceCallBackImpl(callBack, c), executors.networkIO());
        return this;
    }

    public SoapClient executeDownLoadFile(String m, Map map, SoapCallBack callBack) {
        WebServiceUtils.callWebService(param.getUrl(), param.getNameSpace(), m, param.getAction(), properties, new WebServiceCallBackImpl(callBack, byte[].class), executors.networkIO(), true);
        return this;
    }

    private class WebServiceCallBackImpl<T> implements WebServiceUtils.WebServiceCallBack

    {
        private SoapCallBack callBack;
        private Class<T> c;

        public WebServiceCallBackImpl(SoapCallBack callBack, Class<T> c) {
            this.callBack = callBack;
            this.c = c;
        }

        @Override
        public void callBack(SoapObject result) {
            T r = null;
            String json = null;
            if (result != null) {
                r = DataParser.getInstance().soapToJson(result, c);
                json = DataParser.getInstance().soapToJsonString(result);
            }
            T finalR = r;
            callBack.callBack(finalR, json);
        }


        @Override
        public void downLoadFileCallBack(SoapObject result) {
//            JSONObject jsonObject = DataParser.getInstance().soapToJson(result);
            byte[] bytes = null;
            try {
                String str = result.getProperty(0).toString();
                bytes = android.util.Base64.decode(str, android.util.Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                byte[] finalBytes = bytes;
                executors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callBack.callBack(finalBytes, null);
                    }
                });
            }
        }


    }


    public interface SoapCallBack<T> {
        void callBack(T t, String json);
    }

}
