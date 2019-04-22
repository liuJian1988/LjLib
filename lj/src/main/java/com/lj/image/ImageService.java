package com.lj.image;

/**
 * 作者：liujian on 2019/3/11 10:39
 * 邮箱：15313727484@163.com
 */
public interface ImageService {
    void getImage( String uri, CallBack callBack);

    public interface CallBack {
        void onSuccess(String filePath);

        void onFailed(String ex);
    }
}
