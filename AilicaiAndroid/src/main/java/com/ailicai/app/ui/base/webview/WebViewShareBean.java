package com.ailicai.app.ui.base.webview;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.share.MessageDetailShareDialog;
import com.ailicai.app.common.share.ShareUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.ui.base.FragmentHelper;
import com.alibaba.fastjson.JSON;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by duo.chen on 2016/4/8.
 */
public class WebViewShareBean implements MessageDetailShareDialog.ShareToWhereListener {

    protected FragmentHelper mFragmentHelper;
    private String shareUrl;
    UMShareListener defUmShareListener = new UMShareListener() {

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            ToastUtil.showInCenter("分享成功");
            upEventLog("success");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            ToastUtil.showInCenter("分享失败");
            upFailEventLog("fail", -3);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            ToastUtil.showInCenter("分享失败");
            upFailEventLog("fail", -2);
        }
    };
    private String shareImageUrl;
    private int shareIcon;
    private String shareTitle;
    private String shareContent;
    private int defaultIconShare = R.drawable.app_icon_for_share;
    private MessageDetailShareDialog mShareDialog;
    private Activity context;

    public WebViewShareBean(Activity context) {
        this.context = context;
        init();
    }

    private String getShareUrl() {
        if (null != shareUrl) {
            if (shareUrl.startsWith("http")) {
                return shareUrl;
            } else {
                return "http://" + shareUrl;
            }
        }
        return null;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    @Override
    public void shareToWechat() {
    /*    WeiXinShareContent content = new WeiXinShareContent();
        UMImage shareImage;
        content.setShareContent(getShareContent());
        content.setTitle(getShareTitle());
        content.setTargetUrl(getShareUrl());
        if (!TextUtils.isEmpty(getShareImageUrl())) {
            shareImage = new UMImage(context, getShareImageUrl());
        } else {
            if (shareIcon == 0) {
                shareImage = new UMImage(context, defaultIconShare);
            } else {
                shareImage = new UMImage(context, shareIcon);
            }
        }
        content.setShareMedia(shareImage);
        ShareUtil.getInstance(context).shareToWeiXin(content, mShareCallBackListener);
     */
        ShareUtil.shareToWXByUm(context, getShareInfo(context), defUmShareListener);
        upEventLog("share");
        upEventLog("weixin");

    }

    @Override
    public void shareToWechatCircle() {
/*        CircleShareContent content = new CircleShareContent();
        UMImage shareImage;
        content.setShareContent(getShareContent());
        content.setTitle(getShareTitle());
        content.setTargetUrl(getShareUrl());
        if (!TextUtils.isEmpty(getShareImageUrl())) {
            shareImage = new UMImage(context, getShareImageUrl());
        } else {
            if (shareIcon == 0) {
                shareImage = new UMImage(context, defaultIconShare);
            } else {
                shareImage = new UMImage(context, shareIcon);
            }
        }
        content.setShareMedia(shareImage);
        ShareUtil.getInstance(context).shareToWeiXinCircle(content, mShareCallBackListener);*/
        ShareUtil.shareToWXCircleByUm(context, getShareInfo(context), defUmShareListener);
        upEventLog("share");
        upEventLog("friend");
    }

    private ShareUtil.ShareBeanByUm getShareInfo(Activity context) {
        ShareUtil.ShareBeanByUm shareBeanByUm = new ShareUtil.ShareBeanByUm();
        shareBeanByUm.titleStr = getShareTitle();
        shareBeanByUm.textStr = getShareContent();
        shareBeanByUm.targetUrl = getShareUrl();
        UMImage shareImage;
        if (!TextUtils.isEmpty(getShareImageUrl())) {
            shareImage = new UMImage(context, getShareImageUrl());
        } else {
            if (shareIcon == 0) {
                shareImage = new UMImage(context, defaultIconShare);
            } else {
                shareImage = new UMImage(context, shareIcon);
            }
        }
        shareBeanByUm.mediaImg = shareImage;
        return shareBeanByUm;
    }

/*    private SocializeListeners.SnsPostListener mShareCallBackListener = new SocializeListeners
            .SnsPostListener() {
        @Override
        public void onStart() {
            showToastMesg(context.getResources().getString(R.string.house_share_start));
            upEventLog("share");
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int eCode, SocializeEntity
                socializeEntity) {
            if (eCode == StatusCode.ST_CODE_SUCCESSED) {
                showToastMesg(context.getResources().getString(R.string.house_share_sucess));
                upEventLog("success");
            } else {
                showToastMesg(context.getResources().getString(R.string.house_share_fail));
                upFailEventLog("fail", eCode);
            }
        }
    };*/

    @Override
    public void cancel() {
        upEventLog("close");
    }

    protected void setShareListener(UMShareListener listener) {
        this.defUmShareListener = listener;
    }

    public void showShareDialog(FragmentManager fragmentManager) {
        if (mFragmentHelper == null) {
            mFragmentHelper = new FragmentHelper(fragmentManager);
        }
        if (mShareDialog == null) {
            mShareDialog = new MessageDetailShareDialog();
        }
        mShareDialog.setOnClickListener(this);
        mFragmentHelper.showDialog(null, mShareDialog);

        upEventLog("share");

    }

    private void upEventLog(String action) {
        Map map = new HashMap();
        map.put("url", URLEncoder.encode(getShareUrl()));
        map.put("action", action);
//        EventLog.upEventLog(String.valueOf(626), JSON.toJSONString(map));
    }

    private void upFailEventLog(String action, int errorCode) {
        String errorMessage = "";
        switch (errorCode) {
            case -1:
                errorMessage = "网络连接失败";
                break;
            //   case '鱀':
            case -2:
                errorMessage = "用户取消";
                break;
            case -3:
                errorMessage = "发送失败";
                break;
            case -4:
                errorMessage = "认证被否决";
                break;
        }
        Map map = new HashMap();
        map.put("url", URLEncoder.encode(getShareUrl()));
        map.put("action", action);
        map.put("message", errorMessage);
//        EventLog.upEventLog(String.valueOf(626), JSON.toJSONString(map));
    }

    public void init() {
        shareUrl = "";
        shareImageUrl = "";
        shareIcon = 0;
        shareTitle = "";
        shareContent = "";
    }

    public void showToastMesg(String msg) {
        ToastUtil.show(context, msg);
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareImageUrl() {
        return shareImageUrl;
    }

    public void setShareImageUrl(String shareImageUrl) {
        this.shareImageUrl = shareImageUrl;
    }

    public int getShareIcon() {
        return shareIcon;
    }

    public void setShareIcon(int shareIcon) {
        this.shareIcon = shareIcon;
    }

}

