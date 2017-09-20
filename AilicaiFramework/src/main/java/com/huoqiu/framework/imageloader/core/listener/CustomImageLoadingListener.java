package com.huoqiu.framework.imageloader.core.listener;

import android.graphics.Bitmap;
import android.view.View;

/**
 * name: CustomImageLoadingListener <BR>
 * description: 可返回bitmap的图片加载监听<BR>
 * create date: 2017/9/19
 *
 * @author: IWJW Zhou Xuan
 */
public interface CustomImageLoadingListener {

    void onLoadingStarted(String uri, View view);

    void onLoadingFailed(String uri, View view);

    void onLoadingComplete(String uri, View view, Bitmap bitmap);

    void onLoadingCancelled(String uri, View view);
}
