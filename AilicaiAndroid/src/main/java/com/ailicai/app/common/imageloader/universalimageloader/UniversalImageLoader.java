package com.ailicai.app.common.imageloader.universalimageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.huoqiu.framework.imageloader.core.ImageDisplayer;
import com.huoqiu.framework.imageloader.core.LoadParam;
import com.huoqiu.framework.imageloader.core.listener.CustomImageLoadingListener;
import com.huoqiu.framework.imageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;

/**
 * name: UniversalImageLoader <BR>
 * description:  加载图片 UniversalImageLoader框架<BR>
 * create date: 2015-7-13
 *
 * @author: IWJW Zhou Xuan
 */
public class UniversalImageLoader implements ImageDisplayer {

    private ImageLoader imageLoader;
    private boolean isNoImage = false;


    @Override
    public void display(Context context, ImageView targetImageView, LoadParam loadParam, final ImageLoadingListener listener) {
        DisplayImageOptions options = getOptions(loadParam);
        imageLoader = getConfigedImageLoader(context);
        if(loadParam.isRound()) {
            roundDisplay(context,targetImageView,loadParam,options,listener);
        } else {
            normalDisplay(targetImageView,loadParam,options,listener);
        }
    }

    private void roundDisplay(Context context, ImageView targetImageView, final LoadParam loadParam, DisplayImageOptions options, final ImageLoadingListener listener) {
        int radiusPixel = getCornerRadiusInPixel(context,loadParam.getCornerRadiusInDp());
        imageLoader.displayImage(loadParam.getImgUri(), targetImageView, options, new ImageLoaderCircleDisplayer(radiusPixel,listener));
    }

    private void normalDisplay(ImageView targetImageView, final LoadParam loadParam, DisplayImageOptions options, final ImageLoadingListener listener) {
        imageLoader.displayImage(loadParam.getImgUri(), targetImageView, options, new ImageLoaderNormalDisplayer(listener));
    }

    @Override
    public void loadImage(String url, final CustomImageLoadingListener listener) {
        imageLoader.loadImage(url,getDefaultOptions(), new com.nostra13.universalimageloader.core.listener.ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(listener != null) {
                    listener.onLoadingStarted(imageUri,view);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(listener != null) {
                    listener.onLoadingFailed(imageUri,view);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(listener != null) {
                    listener.onLoadingComplete(imageUri,view,loadedImage);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(listener != null) {
                    listener.onLoadingCancelled(imageUri,view);
                }
            }
        });
    }

    private boolean isUploadImagePeriod() {
        double randomDouble = Math.random();
     //   LogUtil.e("randomDouble",randomDouble+"");
        if(randomDouble <= 0.3) { // TODO
            return true;
        }
        return false;
    }

    public boolean imgHasDiskCached(Context context,String imgUrl) {
        imageLoader = getConfigedImageLoader(context);
        if(!TextUtils.isEmpty(imgUrl)) {
            File cachedImageFile = imageLoader.getDiskCache().get(imgUrl);
            if(cachedImageFile != null && cachedImageFile.exists()) {
                return true;
            }
        }
        return false;
    }

    public int getCornerRadiusInPixel(Context context, float radiusDp) {
        return dipToPx(context,radiusDp);
    }

    /**
     * dip转px
     */
    public static int dipToPx(Context context, float dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip, context.getResources().getDisplayMetrics());
    }

    @Override
    public void setNoImage(boolean isNoImage) {

        // TODO 未完善 如果有蜂窝数据不显示图片需求需要添加相关代码
        if (imageLoader != null) {
            imageLoader.denyNetworkDownloads(isNoImage);
        }
        this.isNoImage = isNoImage;
    }

    public ImageLoader getConfigedImageLoader(Context context) {
        imageLoader = ImageLoader.getInstance();
        initImageLoaderConfig(context);
        return imageLoader;
    }

    private void initImageLoaderConfig(Context context) {
        imageLoader.init(getImageLoaderConfig(context));
        imageLoader.denyNetworkDownloads(isNoImage);
    }

    private ImageLoaderConfiguration getImageLoaderConfig(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context.getApplicationContext()).threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .denyCacheImageMultipleSizesInMemory().build();
        return config;
    }

    private DisplayImageOptions getOptions(LoadParam loadParam) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .showImageOnLoading(loadParam.getLoadingPicId()).showImageForEmptyUri(loadParam.getEmptyPicId())
                .showImageOnFail(loadParam.getFailPicId()).resetViewBeforeLoading(true).considerExifParams(true)
                .cacheInMemory(false).cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565);
        DisplayImageOptions options = builder.build();
        return options;
    }

    private DisplayImageOptions getDefaultOptions() {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder().considerExifParams(true)
                .cacheInMemory(false).cacheOnDisk(true);
        DisplayImageOptions options = builder.build();
        return options;
    }
}
