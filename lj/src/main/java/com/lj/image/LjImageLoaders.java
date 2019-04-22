package com.lj.image;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;


/**
 * 作者：liujian on 2019/3/11 10:33
 * 邮箱：15313727484@163.com
 * 使用者使用的类，用来用户加载图片
 */
public class LjImageLoaders {
    private static final LjImageLoaders ljImageLoaders = new LjImageLoaders();
    private ImageService imageService;
    private String url;
    private Handler mHandler;
    private Context mContext;

    public LjImageLoaders() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static LjImageLoaders with(Context context) {
        ljImageLoaders.mContext = context;
        return ljImageLoaders;
    }

    public LjImageLoaders setImageService(ImageService imageService) {
        this.imageService = imageService;
        return this;
    }

    public LjImageLoaders setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * @author 刘建
     * @time 2019/3/20  6:56 PM
     * @describe 只下载图片
     */
    public void downLoad(ImageService.CallBack callBack) {

        imageService.getImage(url, callBack);
    }

    public void show(final ImageView imgView) {

        imageService.getImage(url, new ImageService.CallBack() {
            private ImageView imageView = imgView;

            @Override
            public void onSuccess(final String filePath) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (imgView != null)
                            Glide.with(mContext).load(new File(filePath)).into(imgView);
                    }
                });
            }

            @Override
            public void onFailed(String ex) {

            }
        });
    }


}
