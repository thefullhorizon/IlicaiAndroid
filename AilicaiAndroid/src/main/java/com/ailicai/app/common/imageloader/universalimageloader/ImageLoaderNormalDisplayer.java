package com.ailicai.app.common.imageloader.universalimageloader;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.huoqiu.framework.imageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * ImgLoaderNormalDisplayListener
 *
 * @author Zhou Xuan
 */
public class ImageLoaderNormalDisplayer extends SimpleImageLoadingListener {

    static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

    ImageLoadingListener listener;

    public ImageLoaderNormalDisplayer(ImageLoadingListener listener) {
        this.listener = listener;
    }

    public ImageLoaderNormalDisplayer() {

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
            imageView.setImageBitmap(loadedImage);

            // animate
            boolean firstDisplay = !displayedImages.contains(imageUri);
            if (firstDisplay) {
                FadeInBitmapDisplayer.animate(imageView, 500);
                displayedImages.add(imageUri);
            }
        } else {
            if(listener != null) {
                listener.onLoadingFailed();
            }
        }

        if(listener != null) {
            listener.onLoadingFinish();
        }
    }
}
