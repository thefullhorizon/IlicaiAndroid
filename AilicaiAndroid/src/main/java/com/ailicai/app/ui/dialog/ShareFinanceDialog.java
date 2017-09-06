package com.ailicai.app.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.UIUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by Gerry on 2015/6/16.
 */
public class ShareFinanceDialog extends MyBaseDialog implements View.OnClickListener {
    private Context mContext;
    private String imageUrl = "";

    public DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ailicai_promo_default)// 设置图片在下载期间显示的图片
            .showImageForEmptyUri(R.drawable.ailicai_promo_default)// 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.img_failed)// 设置图片加载/解码过程中错误时候显示的图片
            .cacheInMemory(true)// 是否缓存都內存中
            .cacheOnDisk(true)// 是否缓存到sd卡上
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new SimpleBitmapDisplayer()).build();

    private OnShareButtonClickListener mOnShareButtonClickListenerSummy = new OnShareButtonClickListener() {
        @Override
        public void shareByWeChat() {

        }

        @Override
        public void shareByWeChatCircle() {

        }

        @Override
        public void closeShare() {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public boolean cancelable() {
        return false;
    }

    @Override
    public void setupView(View rootView, Bundle bundle) {
        int width = DeviceUtil.getScreenSize()[0] - DeviceUtil.getPixelFromDip(mContext, 60);
        setDialogSize(rootView, width, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout shareWechatCircle = UIUtils.findView(rootView, R.id.share_to_wechat_circle);
        LinearLayout shareWechat = UIUtils.findView(rootView, R.id.share_to_wechat);
        TextView closeButton = UIUtils.findView(rootView, R.id.close_button);
        shareWechatCircle.setOnClickListener(this);
        shareWechat.setOnClickListener(this);
        closeButton.setOnClickListener(this);
        setPageData(rootView);
    }

    public void setPageData(View rootView) {
        ImageView bannerImage = UIUtils.findView(rootView, R.id.banner_img);
        ImageLoader.getInstance().displayImage(imageUrl, bannerImage, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    private void setDialogSize(View rootView, int width, int height) {
        //顶部图片宽高比：5:3
        FrameLayout bannerImgLayout = UIUtils.findView(rootView, R.id.banner_img_layout);
        LinearLayout.LayoutParams ps = new LinearLayout.LayoutParams(width, width * 3 / 5);
        bannerImgLayout.setLayoutParams(ps);

        LinearLayout rootLayout = (LinearLayout) rootView
                .findViewById(R.id.dialog_root);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(width,
                height);
        rootLayout.setLayoutParams(lp1);
    }

    @Override
    public void setupData(Bundle bundle) {

    }

    @Override
    public int getLayout() {
        return R.layout.regular_pay_share_dialog;
    }

    @Override
    public int getTheme() {
        return MYTHEME4;
    }

    @Override
    public int displayWindowLocation() {
        return Gravity.CENTER;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_to_wechat:
                mOnShareButtonClickListenerSummy.shareByWeChat();
                break;
            case R.id.share_to_wechat_circle:
                mOnShareButtonClickListenerSummy.shareByWeChatCircle();
                break;
            case R.id.close_button:
                mOnShareButtonClickListenerSummy.closeShare();
                break;
        }
    }

    public void setActionButtonClickListener(OnShareButtonClickListener listener) {
        mOnShareButtonClickListenerSummy = listener;
    }

    public void setShareImageUrl(String url) {
        this.imageUrl = url;
    }

    public interface OnShareButtonClickListener {
        void shareByWeChat();

        void shareByWeChatCircle();

        void closeShare();
    }

}
