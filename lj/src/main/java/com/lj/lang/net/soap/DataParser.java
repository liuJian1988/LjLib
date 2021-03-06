package com.lj.lang.net.soap;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.HashSet;
import java.util.Iterator;

/**
 * 作者：liujian on 2018/6/27 19:38
 * 邮箱：15313727484@163.com
 */
public class DataParser {

    private final String tag = DataParser.class.getSimpleName();
    /**
     * SoapObject解析对象
     **/
    private static DataParser instance;
    private HashSet<String> hashset;

    /**
     * 得到单例模式SoapManager对象
     *
     * @return
     */
    public static DataParser getInstance() {
        if (instance == null) {
            synchronized (DataParser.class) {
                if (instance == null) {
                    instance = new DataParser();
                }
            }
        }
        return instance;
    }

    /**
     * the method is used to convert the soapObject to json.
     *
     * @param clazz
     * @param soapObject
     * @return
     */
    public <T> T soapToJson(SoapObject soapObject, Class<T> clazz) {
        long start = System.currentTimeMillis();
        JSONObject jsonResult = new JSONObject();
        HashSet<String> hashset = new HashSet<String>();

        //将SoapObject转json字符串
        getSoapresult(soapObject, jsonResult, hashset);

        String json = getSoapJsonResult(jsonResult, hashset);
        if (json == null || json.replace("{", "").replace("}", "").trim().length() <= 2) {
            //某些情况下getSoapresult数据解析不出来，需要用到它
            jsonResult = new JSONObject();
            getSoapresult(soapObject, jsonResult, hashset);
            json = jsonResult.toString();
        }
        Log.e(tag, "soapToJson: " + json.toString());

        long end = System.currentTimeMillis();
        Log.e(tag, "soapToJson take time : " + (end - start));
        T re = null;
        try {
            re = JSON.parseObject(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }

    /**
     * the method is used to convert the soapObject to json.
     *
     * @param soapObject
     * @return
     */
    public JSONObject soapToJson(SoapObject soapObject) {
        long start = System.currentTimeMillis();
        JSONObject jsonResult = new JSONObject();
        HashSet<String> hashset = new HashSet<String>();

        //将SoapObject转json字符串
        getSoapresult(soapObject, jsonResult, hashset);


        long end = System.currentTimeMillis();
        Log.e(tag, "soapToJson take time : " + (end - start));
        return jsonResult;
    }

    /**
     * 将SoapObject转json字符串
     *
     * @param soapresult SoapObject
     * @param jsonResult JSONObject
     */
    private void getSoapresult(SoapObject soapresult, JSONObject jsonResult, HashSet<String> hashset) {
        try {
            if (soapresult != null) {
                PropertyInfo proinfo = null;
                SoapObject childsoap = null;
                JSONObject childJson = null;
                String curName = null;

                int count = soapresult.getPropertyCount();
                for (int i = 0; i < count; i++) {
                    //获取当前节点的信息
                    proinfo = new PropertyInfo();
                    soapresult.getPropertyInfo(i, proinfo);

                    curName = proinfo.getName();

                    //判断当前节点的类型，如果是SoapPrimitive则可以直接取值，如果是SoapObject则继续解析
                    if (SoapPrimitive.class == proinfo.getType()) {
                        jsonResult.put(curName, proinfo.getValue());
                    } else if (SoapObject.class == proinfo.getType()) {

                        childJson = new JSONObject();
                        childsoap = (SoapObject) soapresult.getProperty(i);

                        //判断该节点下面有相同节点，那么他就是一个数组
                        if (isArrayNode(childsoap)) {
                            hashset.add(curName);
                            Log.e(tag, "curName : " + curName);
                        }

                        //处理相同名字的节点，到时候好替换成JSONArray
                        if (jsonResult.has(curName)) {
                            curName = curName + i;
                        }

                        //递归调用自己，获取子节点
                        getSoapresult(childsoap, childJson, hashset);

                        //将子节点添加到根节点下面
                        jsonResult.put(curName, childJson);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断该节点下面是否有相同的节点，如果说明是数组
     *
     * @param childsoap
     * @return
     */
    public boolean isArrayNode(SoapObject childsoap) {
        PropertyInfo proinfo = null;
        int count = childsoap.getPropertyCount();

        for (int i = 0; i < count; i++) {
            proinfo = new PropertyInfo();
            childsoap.getPropertyInfo(i, proinfo);
            if (SoapObject.class == proinfo.getType()) {
                if (childsoap.hasProperty(proinfo.getName())) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 获取SoapObject中数组转化成JSONArray
     */
    private String getSoapJsonResult(JSONObject jsonResult, HashSet<String> hashset) {
        this.hashset = hashset;
        try {
            String key = "";
            String jsonkey = "";
            Iterator<?> it = null;
            JSONObject tempJson = null;
            JSONArray jsonArray = new JSONArray();

            Iterator<String> setIt = hashset.iterator();
            while (setIt.hasNext()) {
                key = setIt.next().toString();
                tempJson = (JSONObject) jsonResult.remove(key);
                if (tempJson != null) {
                    it = tempJson.keys();
                    while (it.hasNext()) {
                        jsonkey = it.next().toString();
                        jsonArray.put(tempJson.getJSONObject(jsonkey));
                    }
                    jsonResult.put(key, jsonArray);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonResult.toString();
    }

    public String soapToJsonString(SoapObject sb) {
        HashSet<String> hashset = new HashSet<String>();
        JSONObject jsonResult = new JSONObject();

        //将SoapObject转json字符串
        getSoapresult(sb, jsonResult, hashset);

        String json = getSoapJsonResult(jsonResult, hashset);
        return json;
    }
}
