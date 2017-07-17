package com.ailicai.app.common.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.ailicai.app.common.imageloader.universalimageloader.UniversalImageLoader;
import com.huoqiu.framework.imageloader.core.ImageDisplayer;
import com.huoqiu.framework.imageloader.core.LoadParam;
import com.huoqiu.framework.imageloader.core.LoaderFactory;
import com.huoqiu.framework.imageloader.core.listener.ImageLoadingListener;


/**
 * Created by Zhou Xuan on 2015/7/13.
 */
public class ImageLoaderClient {

    private static ImageDisplayer imageDisplayer;

    /**
     * 加载并显示图片
     **/
    public static void display(Context context, ImageView targetImageView, LoadParam loadParam) {
        display(context, targetImageView, loadParam, null);
    }

    /**
     * 加载并显示图片
     **/
    public static void display(Context context, ImageView targetImageView, String url, LoadParam loadParam) {
        loadParam.setImgUri(url);
        display(context, targetImageView, loadParam, null);
    }

    /**
     * 加载并显示图片
     **/
    public static void display(Context context, ImageView targetImageView, String url, LoadParam loadParam, ImageLoadingListener imageLoadingListener) {
        loadParam.setImgUri(url);
        display(context, targetImageView, loadParam, imageLoadingListener);
    }

    /**
     * 加载并显示图片
     **/
    public static void display(Context context, ImageView targetImageView, LoadParam loadParam, ImageLoadingListener imageLoadingListener) {
        getImageLoaderInstance();
        imageDisplayer.display(context, targetImageView, loadParam, imageLoadingListener);
    }

    public static void setNoImage(boolean isNoImage) {
        getImageLoaderInstance();
        imageDisplayer.setNoImage(isNoImage);
    }

    public static ImageDisplayer getImageLoaderInstance() {
        try {
            imageDisplayer = LoaderFactory.getLoader(UniversalImageLoader.class);
        } catch (InstantiationException e) {
            e.printStackTrace();
            imageDisplayer = new UniversalImageLoader();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            imageDisplayer = new UniversalImageLoader();
        }
        return imageDisplayer;
    }
}
