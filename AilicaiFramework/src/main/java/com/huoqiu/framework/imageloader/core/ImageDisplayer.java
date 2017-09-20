package com.huoqiu.framework.imageloader.core;

import android.content.Context;
import android.widget.ImageView;

import com.huoqiu.framework.imageloader.core.listener.CustomImageLoadingListener;
import com.huoqiu.framework.imageloader.core.listener.ImageLoadingListener;


/**
 * class name:ImageDisplayer <BR>
 * class description: 加载图片的业务接口 <BR>
 * Remark: <BR>
 *
 * @author IWJW)Zhou Xuan
 * @version 1.00 2015-7-13
 */
public interface ImageDisplayer {
    /**
     * 显示图片到imageView上
     */
    void display(Context context, ImageView targetImageView,LoadParam loadParam, ImageLoadingListener listener);
    /**
     * true :蜂窝数据下设置无图模式
     */
    void setNoImage(boolean isNoImage);

    /**
     * 根据图片URL查找是否本地存储中有缓存
     */
    boolean imgHasDiskCached(Context context,String imgUrl);

    /**
     * 根据图片URL加载url
     */
    void loadImage(String url, CustomImageLoadingListener listener);
}
