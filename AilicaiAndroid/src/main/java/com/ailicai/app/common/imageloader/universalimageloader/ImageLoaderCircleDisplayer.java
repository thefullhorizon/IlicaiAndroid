package com.ailicai.app.common.imageloader.universalimageloader;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.huoqiu.framework.imageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * ImageLoaderDisplayListener 将imageloader加载好的bitmap展示成圆形的图片
 *
 * @author Zhou Xuan
 */
public class ImageLoaderCircleDisplayer extends SimpleImageLoadingListener {

    private int cornerRadius;
    static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

    ImageLoadingListener listener;

    public ImageLoaderCircleDisplayer(int cornerRadiusPixels,ImageLoadingListener listener) {
        this.listener = listener;
        this.cornerRadius = cornerRadiusPixels;
    }

    public ImageLoaderCircleDisplayer(int cornerRadiusPixels) {
        this.cornerRadius = cornerRadiusPixels;
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {
        super.onLoadingStarted(imageUri, view);

        if(listener != null) {
            listener.onLoadingStarted();
        }
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
        super.onLoadingCancelled(imageUri, view);

        if(listener != null) {
            listener.onLoadingCancelled();
            listener.onLoadingFinish();
        }
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        super.onLoadingFailed(imageUri, view, failReason);

        if(listener != null) {
            listener.onLoadingFailed();
            listener.onLoadingFinish();
        }
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        if (loadedImage != null) {

            if(listener != null) {
                listener.onLoadingSuccess();
            }

            ImageView imageView = (ImageView) view;
            imageView.setImageDrawable(new RoundedBitmapDisplayer.RoundedDrawable(loadedImage, this.cornerRadius, 0));

            // animate
            boolean firstDisplay = !displayedImages.contains(imageUri);
            if (firstDisplay) {
                FadeInBitmapDisplayer.animate(imageView, 500);
                displayedImages.add(imageUri);
            }

        }else {
            if(listener != null) {
                listener.onLoadingFailed();
            }
        }
        if(listener != null) {
            listener.onLoadingFinish();
        }
    }
}
