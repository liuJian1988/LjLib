//package com.lj.lang.net.http;
//
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.Headers;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//
///**
// * Created by Liujian on 2017/9/7 0007.
// *
// * @link http://blog.csdn.net/liujian8654562
// */
//
//public class MyOkHttpClient {
//    private OkHttpClient okHttpClient;
//    private static MyOkHttpClient myOkHttpClient;
//    private String session = null;
//
//    public void pickSession(Response response) {
//        //获取session的操作，session放在cookie头，且取出后含有“；”，取出后为下面的 s （也就是jsesseionid）
//        Headers headers = response.headers();
//        List<String> cookies = headers.values("Set-Cookie");
//        if (cookies != null && cookies.size() > 0) this.session = cookies.get(0);
//    }
//
//    public static MyOkHttpClient getInstance() {
//        if (myOkHttpClient == null) {
//            myOkHttpClient = new MyOkHttpClient();
//        }
//        return myOkHttpClient;
//    }
//
//    private MyOkHttpClient() {
//        okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(5, TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS)
//                .build();
//    }
//
//    public void cleanCache() {
//        this.session = null;
//    }
//
//    public void getDataFromNet(String url, Callback callback) {
//
//        Request.Builder builder = new Request.Builder()
//                .url(url);
//        if (session != null) {
//            builder.addHeader("cookie", session);
//        }
//        Request request = builder.build();
//        //new call
//        Call call = okHttpClient.newCall(request);
//        //请求加入调度
//        call.enqueue(callback);
//    }
//
//    /**
//     * 同步请求，通过get方式获取网络字符数据
//     *
//     * @param url
//     */
//    public String getDataFromNet(String url) {
//        try {
//            Request.Builder builder = new Request.Builder()
//                    .url(url);
//            Request request = builder.build();
//            //new call
//            Call call = okHttpClient.newCall(request);
//            Response execute = null;
//            execute = call.execute();
//            if (execute.isSuccessful()) {
//                //获取到响应体
//                ResponseBody body = execute.body();
//                //根据相应体获取到相应内容
//                String netData = body.string();
//                return netData;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void postData(String url, Callback callback, RequestBody body) {
//        Request.Builder builder = new Request.Builder().url(url).post(body);
//        if (session != null) {
//            builder.addHeader("cookie", session);
//        }
//        Request request = builder.build();
//        Call call = okHttpClient.newCall(request);
//        //请求加入调度
//        call.enqueue(callback);
//    }
//
//    /**
//     * 上传单个文件
//     */
//    public void post_UpLoadIMG(String url, String imgPath, Callback callback) {
//        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
//        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        File f = new File(imgPath);
//        if (f != null) {
//            builder.addFormDataPart("file", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
//        }
//        MultipartBody requestBody = builder.build();
//        postData(url, callback, requestBody);
//
//    }
//
//    /**
//     * 上传多张图片及参数
//     *
//     * @param reqUrl  URL地址
//     * @param params  参数
//     * @param pic_key 上传图片的关键字
//     */
//    private void upFile(String reqUrl, Map<String, String> params, List<File> files, String pic_key, Callback callback) {
//////        /* 第一个要上传的file */
//////        File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/a.jpg");
//////        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), file1);
//////        String file1Name = "testFile1.txt";
//////
//////        /* 第二个要上传的文件,这里偷懒了,和file1用的一个图片 */
//////        File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/a.jpg");
//////        RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), file2);
//////        String file2Name = "testFile2.txt";
////
////
////        /* form的分割线,自己定义 */
////        String boundary = "xx--------------------------------------------------------------xx";
////
////        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
////        multipartBodyBuilder.setType(MultipartBody.FORM);
////
//////        RequestBody[] requestBodies = new RequestBody[files.size()];
//////        String[] fileName = new String[files.size()];
////        int count = 0;
////        for (String key : files.keySet()) {
////            requestBodies[count] = RequestBody.create(MediaType.parse("application/octet-stream"), files.get(key));
////            fileName[count] = key;
////            count++;
////        }
////
//////        MultipartBody mBody = new MultipartBody.Builder(boundary).setType(MultipartBody.FORM)
//////                /* 上传一个普通的String参数 , key 叫 "p" */
//////                .addFormDataPart("p", "你大爷666")
//////                /* 底下是上传了两个文件 */
//////                .addFormDataPart("file", file1Name, fileBody1)
//////                .addFormDataPart("file", file2Name, fileBody2)
//////                .build();
//
//
//        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
//        multipartBodyBuilder.setType(MultipartBody.FORM);
//        //遍历map中所有参数到builder
//        if (params != null) {
//            for (String key : params.keySet()) {
//                multipartBodyBuilder.addFormDataPart(key, params.get(key));
//            }
//        }
//        //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
//        if (files != null) {
//            for (File file : files) {
//                multipartBodyBuilder.addFormDataPart(pic_key, file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
//            }
//        }
//        //构建请求体
//        RequestBody requestBody = multipartBodyBuilder.build();
////        Request.Builder RequestBuilder = new Request.Builder();
////        RequestBuilder.url(reqUrl);// 添加URL地址
////        RequestBuilder.post(requestBody);
////        Request request = RequestBuilder.build();
////        okHttpClient.newCall(request).enqueue(new Callback() {
////            @Override
////            public void onFailure(Call call, IOException e) {
////                subscriber.onError(e);
////                subscriber.onCompleted();
////                call.cancel();
////            }
////
////            @Override
////            public void onResponse(Call call, Response response) throws IOException {
////                String str = response.body().string();
////                subscriber.onNext(str);
////                subscriber.onCompleted();
////                call.cancel();
////            }
////        });
//
//        /* 下边的就和post一样了 */
////        Request request = new Request.Builder().url("http://192.168.10.117:8080/test").post(mBody).build();
////        okHttpClient.newCall(request).enqueue(callback);
//        postData(reqUrl, callback, requestBody);
//
//    }
//}
