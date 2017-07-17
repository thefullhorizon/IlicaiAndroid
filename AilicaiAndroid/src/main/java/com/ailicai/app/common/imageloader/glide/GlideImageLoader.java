package com.ailicai.app.common.imageloader.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.huoqiu.framework.imageloader.core.ImageDisplayer;
import com.huoqiu.framework.imageloader.core.LoadParam;
import com.huoqiu.framework.imageloader.core.listener.ImageLoadingListener;

/**
 * name: GlideImageLoader <BR>
 * description:  加载图片 Glide框架<BR>
 * create date: 2016-7-13
 *
 * @author: IWJW Zhou Xuan
 */
public class GlideImageLoader implements ImageDisplayer {

    @Override
    public void display(final Context context, ImageView targetImageView, LoadParam loadParam, final ImageLoadingListener listener) {
        Glide.with(context).load(loadParam.getImgUri()).skipMemoryCache(true).into(new GlideDrawableImageViewTarget(targetImageView) {

                                                                                       @Override
                                                                                       public
                                                                                       void
                                                                                       onLoadStarted(Drawable placeholder) {
                                                                                           super.onLoadStarted(placeholder);
                                                                                           if (listener != null) {
                                                                                               listener.onLoadingStarted();
                                                                                           }
                                                                                       }

                                                                                       @Override
                                                                                       public
                                                                                       void
                                                                                       onLoadFailed(Exception e, Drawable errorDrawable) {
                                                                                           super.onLoadFailed(e, errorDrawable);
                                                                                           if (listener != null) {
                                                                                               listener.onLoadingFailed();
                                                                                           }
                                                                                       }

                                                                                       @Override
                                                                                       public
                                                                                       void
                                                                                       onLoadCleared(Drawable placeholder) {
                                                                                           super.onLoadCleared(placeholder);
                                                                                           if (listener != null) {
                                                                                               listener.onLoadingStarted();
                                                                                           }
                                                                                       }

                                                                                       @Override
                                                                                       public
                                                                                       void
                                                                                       onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                                                                                           super.onResourceReady(drawable, anim);
                                                                                           //在这里添加一些图片加载完成的操作
                                                                                           if (listener != null) {
                                                                                               listener.onLoadingSuccess();
                                                                                           }
                                                                                       }
                                                                                   }
        );
    }

    @Override
    public void setNoImage(boolean isNoImage) {
        // TODO 未完善 如果有蜂窝数据不显示图片需求需要添加相关代码
    }
}
