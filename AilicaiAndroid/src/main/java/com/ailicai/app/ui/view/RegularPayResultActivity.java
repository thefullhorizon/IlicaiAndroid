package com.ailicai.app.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.share.ShareUtil;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.MathUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.SpannableUtil;
import com.ailicai.app.common.utils.UIUtils;
import com.ailicai.app.eventbus.RegularPayEvent;
import com.ailicai.app.eventbus.RegularPayH5ActivityFinishEvent;
import com.ailicai.app.model.request.OptionReportRequest;
import com.ailicai.app.model.response.BannerListResponse;
import com.ailicai.app.model.response.BuyDingqibaoResponse;
import com.ailicai.app.ui.asset.CapitalListProductDetailActivity;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.webview.TransparentWebViewActivity;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.dialog.ShareFinanceDialog;
import com.ailicai.app.ui.view.banner.IndexBannerScroller;
import com.ailicai.app.ui.view.banner.PayResultBannerPagerAdapter;
import com.ailicai.app.ui.view.detail.SmallCoinSackActivity;
import com.ailicai.app.ui.view.transaction.TransactionListActivity;
import com.ailicai.app.widget.TextViewTF;
import com.alibaba.fastjson.JSON;
import com.huoqiu.framework.rest.Response;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 房产宝购买结果
 * Created by Gerry on 2015/12/29.
 */
public class RegularPayResultActivity extends BaseBindActivity {

    public int LOTTERY_REQUEST = 1;

    @Bind(R.id.product_txt)
    TextView mProductTxt;
    @Bind(R.id.money_txt)
    TextView mMoneyTxt;
    @Bind(R.id.period_txt)
    TextView mPeriodTxt;
    @Bind(R.id.rate_txt)
    TextView mRateTxt;
    @Bind(R.id.pay_icon)
    TextViewTF mPayIcon;
    @Bind(R.id.pay_msg)
    TextView mPayMsg;
    @Bind(R.id.pay_detail_layout)
    LinearLayout mPayDetailLayout;
    @Bind(R.id.confirm_repay)
    Button mConfirmRepay;
    @Bind(R.id.pay_tips_text)
    TextView payTipsText;
    @Bind(R.id.ll_activity_msg)
    LinearLayout llActivityMsg;
    @Bind(R.id.tv_activity_msg)
    TextView tvActivityMsg;
    @Bind(R.id.ll_result_success)
    LinearLayout llResultSuccess;
    @Bind(R.id.fl_result_not_success)
    FrameLayout flResultNotSuccess;
    @Bind(R.id.tv_regular_label)
    TextView tvRegularLabel;
    @Bind(R.id.tv_profit_label)
    TextView tvProfitLabel;
    @Bind(R.id.tv_regular_process)
    TextView tvRegularProcess;
    @Bind(R.id.tv_regular_process_label)
    TextView tvRegularProcessLabel;
    @Bind(R.id.confirm_success)
    Button mConfirmSuccess;

    //Banner
    @Bind(R.id.layout_banner)
    LinearLayout mLayoutBanner;
    @Bind(R.id.viewpager_banner)
    ViewPager mViewPagerBanner;
    @Bind(R.id.layout_indicators_banner)
    LinearLayout mIndicatorsBanner;

    // 首投抽奖
    @Bind(R.id.llFirstInvestLottery)
    LinearLayout llFirstInvestLottery;
    @Bind(R.id.tvLotteryDesc)
    TextView tvLotteryDesc;
    @Bind(R.id.llFirstInvestLotteryImage)
    LinearLayout llFirstInvestLotteryImage;

    private RegularPayResultPresenter regularPayResultPresenter;

    private BuyDingqibaoResponse response;
    public static String KEY = "response";

    private boolean isFromSmallCoin = false;

    private ShareFinanceDialog mShareFinanceDialog;

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

    @Override
    public int getLayout() {
        return R.layout.activity_regular_pay_result;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        regularPayResultPresenter = new RegularPayResultPresenter(this);
        isFromSmallCoin = getIntent().getBooleanExtra(RegularPayActivity.IS_FROM_SMALL_COIN,false);
        response = (BuyDingqibaoResponse) getIntent().getExtras().getSerializable(KEY);
        //确定当前成功页面的类型（房产宝，转让房产宝，小钱袋）
        if(isFromSmallCoin){
            BANNER_LOCATION = BANNER_XQD_BAG;
        }else{
            if(response.getIsTransfer() == 0){
                BANNER_LOCATION = BANNER_FCB_BUY;
            }else{
                BANNER_LOCATION = BANNER_FCB_TRANS;
            }
        }
        //处理结果，S成功;P处理中;F失败
        String bizStatus = response.getBizStatus();
        //post 事件刷新相关页面
        postEventBus(bizStatus);
        if (response.getHasBuyPrecent() == 1) {
            response.setLast(true);
        }
        switch (bizStatus) {
            case "S":
                //购买成功
                initPaySuccess(response.isLast());
                break;
            case "F":
                //购买失败
                initPayFail();
                break;
            case "P":
                //购买进行中
                initPayHandling();
                break;
        }
        //弹出分享
        if (response.getIsPopShare()) {
            showShareDialog();
            //type;//业务类型：0-一般业务（app大首页banner，爱理财banner，开屏弹窗） 1-分享弹窗业务 （微信分享）
            //status;//0-banner点击统计pv/uv     2-弹出弹窗的次数 3-点击分享分享朋友圈 4-点击分享给好友
            //requestOptionReport(response.getBannerId(), 1, 2);
        }
    }

    /**
     * 购买失败
     */
    private void initPayFail() {
        llResultSuccess.setVisibility(View.GONE);
        flResultNotSuccess.setVisibility(View.VISIBLE);
        mPayIcon.setText(R.string.failured);
        mPayIcon.setTextColor(Color.parseColor("#9d9d9d"));
        mPayMsg.setText("购买失败");
        mConfirmRepay.setText("重新购买");
        payTipsText.setText(response.getMessage());
        mPayDetailLayout.setVisibility(View.GONE);
        llActivityMsg.setVisibility(View.GONE);
        EventLog.upEventLog("201610282", "show", "ptfcb_fail");
    }

    /**
     * 购买成功
     */
    private void initPaySuccess(boolean isLast) {

        setInvestActivity();

        llResultSuccess.setVisibility(View.VISIBLE);
        flResultNotSuccess.setVisibility(View.GONE);
        if (isLast) {
            //最后一笔
            SpannableUtil spanUtil = new SpannableUtil(this);
            SpannableStringBuilder builder = spanUtil.getSpannableString("账户余额中", MathUtil.subZeroAndDot(CommonUtil.formatMoneyForFinance(response.getAmount())), "元已变成申购款",
                    R.style.text_13_757575, R.style.text_13_e84a01, R.style.text_13_757575);
            tvRegularLabel.setText(builder);
            tvProfitLabel.setVisibility(View.INVISIBLE);
            tvRegularProcess.setText("募集完成，等待审核");
            tvRegularProcessLabel.setText("募集成功后，将于" + response.getInterestDateStr() + "起息");
        } else {
            //非最后一笔
            SpannableUtil spanUtil = new SpannableUtil(this);
            SpannableStringBuilder builder1 = spanUtil.getSpannableString("账户余额中", MathUtil.subZeroAndDot(CommonUtil.formatMoneyForFinance(response.getAmount())), "元已变成申购款",
                    R.style.text_13_757575, R.style.text_13_e84a01, R.style.text_13_757575);
            tvRegularLabel.setText(builder1);
            //tvProfitLabel.setVisibility(View.VISIBLE);
            tvProfitLabel.setVisibility(View.INVISIBLE);
            SpannableStringBuilder builder2 = spanUtil.getSpannableString("申购期间仍享预计年化", response.getHuoqibaoRate(), "的钱包收益",
                    R.style.text_13_757575, R.style.text_13_e84a01, R.style.text_13_757575);
            tvProfitLabel.setText(builder2);
            SpannableStringBuilder builder3 = spanUtil.getSpannableString("募集中", "(申购进度" + response.getHasBuyPrecentStr() + ")",
                    R.style.text_18_212121_b, R.style.text_18_212121);
            tvRegularProcess.setText(builder3);
            tvRegularProcessLabel.setText("申购将于" + response.getEndBuyTimeStr() + "结束\n" +
                    "若募集完成, 将于" + response.getInterestDateStr() + "(含)前起息\n" +
                    "若募集不满, 申购款将自动返回可用余额");
        }
        if (!TextUtils.isEmpty(response.getActivityMsg())) {
            llActivityMsg.setVisibility(View.VISIBLE);
            tvActivityMsg.setText(response.getActivityMsg());
        } else {
            llActivityMsg.setVisibility(View.GONE);
        }
        EventLog.upEventLog("201610282", "show", "ptfcb_success");
    }

    /**
     * 购买进行中
     */
    private void initPayHandling() {
        llResultSuccess.setVisibility(View.GONE);
        flResultNotSuccess.setVisibility(View.VISIBLE);
        mPayIcon.setText(R.string.waiting);
        mPayIcon.setTextColor(Color.parseColor("#e84a01"));
        mPayMsg.setText("购买进行中");
        mConfirmRepay.setText("交易记录");
        payTipsText.setText("份额确认中，稍后请在交易记录中查询");
        mPayDetailLayout.setVisibility(View.VISIBLE);
        llActivityMsg.setVisibility(View.GONE);
        mPayDetailLayout.setVisibility(View.VISIBLE);
        mProductTxt.setText(response.getProductName());
        mMoneyTxt.setText(CommonUtil.formatMoneyDou(response.getAmount()) + "元");
        mPeriodTxt.setText(response.getHorizon());
        mRateTxt.setText(response.getYearInterestRate());
        EventLog.upEventLog("201610282", "show", "ptfcb_underway");
    }

    // 投资活动相关  banner 和首投奖励
    private void setInvestActivity() {
        if(response.getIsLottery() == 1) {
            mLayoutBanner.setVisibility(View.GONE);
            llFirstInvestLottery.setVisibility(View.VISIBLE);
            setLotteryWidthAndHeight();
            tvLotteryDesc.setText("恭喜您获得抽奖机会，立刻去抽奖");
            llFirstInvestLottery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> dataMap = ObjectUtil.newHashMap();
                    dataMap.put(WebViewActivity.URL, response.getFirstInvestLotteryURL());
                    MyIntent.startActivityForResult(RegularPayResultActivity.this, TransparentWebViewActivity.class, dataMap,LOTTERY_REQUEST);
                }
            });
        } else {
            llFirstInvestLottery.setVisibility(View.GONE);
            regularPayResultPresenter.getBannerList(10);
        }
    }

    private void setLotteryWidthAndHeight() {
        int width = DeviceUtil.getScreenSize()[0] - UIUtils.dipToPx(this,24);
        int height = 276*width/888;
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(width, height);
        lp1.setMargins(UIUtils.dipToPx(this,12),0,UIUtils.dipToPx(this,12),UIUtils.dipToPx(this,12));
        llFirstInvestLotteryImage.setLayoutParams(lp1);
    }



    /**
     * post 事件刷新相关页面
     *
     * @param bizStatus
     */
    public void postEventBus(String bizStatus) {
        RegularPayEvent regularPayEvent = new RegularPayEvent();
        EventBus.getDefault().post(regularPayEvent);
    }

    /**
     * 统计事件
     *
     * @param bannerId
     * @param type
     * @param status
     */
    public void requestOptionReport(long bannerId, int type, int status) {
        final OptionReportRequest request = new OptionReportRequest();
        request.setBannerId(bannerId);
        request.setType(type);
        request.setStatus(status);
        ServiceSender.exec(this, request, new IwjwRespListener<Response>() {
            @Override
            public void onJsonSuccess(Response response) {
                if (response != null && response.getErrorCode() == 0) {
                    Log.i("IndexPresenter", "rquestOptionReport() success");
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                Log.i("IndexPresenter", "rquestOptionReport() onFailInfo");
            }
        });
    }

    private void showShareDialog() {
        if (mFragmentHelper != null) {
            if (mShareFinanceDialog == null) {
                mShareFinanceDialog = new ShareFinanceDialog();
            }
            Map params = new HashMap();
            params.put("id", response.getBannerId());
            params.put("title", response.getTitle());
            params.put("action", "show");
            EventLog.upEventLog("686", JSON.toJSONString(params));
            mShareFinanceDialog.setShareImageUrl(response.getImgUrl());
            mShareFinanceDialog.setActionButtonClickListener(new ShareFinanceDialog.OnShareButtonClickListener() {

                @Override
                public void closeShare() {
                    Map params = new HashMap();
                    params.put("id", response.getBannerId());
                    params.put("title", response.getTitle());
                    params.put("action", "cancel");
                    EventLog.upEventLog("686", JSON.toJSONString(params));
                    dismissTipsDialog();
                }

                @Override
                public void shareByWeChat() {
                    Map params = new HashMap();
                    params.put("id", response.getBannerId());
                    params.put("title", response.getTitle());
                    params.put("action", "weixin");
                    EventLog.upEventLog("686", JSON.toJSONString(params));
                    dismissTipsDialog();
                    shareToByWeChat(RegularPayResultActivity.this, response);
                    //type;//业务类型：0-一般业务（app大首页banner，爱理财banner，开屏弹窗） 1-分享弹窗业务 （微信分享）
                    //status;//0-banner点击统计pv/uv     2-弹出弹窗的次数 3-点击分享分享朋友圈 4-点击分享给好友
                    //requestOptionReport(response.getBannerId(), 1, 4);
                }

                @Override
                public void shareByWeChatCircle() {
                    Map params = new HashMap();
                    params.put("id", response.getBannerId());
                    params.put("title", response.getTitle());
                    params.put("action", "friend");
                    EventLog.upEventLog("686", JSON.toJSONString(params));
                    dismissTipsDialog();
                    shareToByWeChatCircle(RegularPayResultActivity.this, response);
                    //type;//业务类型：0-一般业务（app大首页banner，爱理财banner，开屏弹窗） 1-分享弹窗业务 （微信分享）
                    //status;//0-banner点击统计pv/uv     2-弹出弹窗的次数 3-点击分享分享朋友圈 4-点击分享给好友
                    //requestOptionReport(response.getBannerId(), 1, 3);
                }
            });
            mFragmentHelper.showDialog(null, mShareFinanceDialog);
        }
    }

    private void dismissTipsDialog() {
        if (mShareFinanceDialog != null) {
            mShareFinanceDialog.dismiss();
            mShareFinanceDialog = null;
        }
    }

    /**
     * 分享结果回调接口
     */
/*    private SocializeListeners.SnsPostListener mShareCallBackListener = new SocializeListeners.SnsPostListener() {
        @Override
        public void onStart() {
            ToastUtil.showInCenter(getResources().getString(R.string.house_share_start));
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int eCode, SocializeEntity socializeEntity) {
            if (eCode == StatusCode.ST_CODE_SUCCESSED) {
                ToastUtil.showInCenter(getResources().getString(R.string.house_share_sucess));
            } else {
                ToastUtil.showInCenter(getResources().getString(R.string.house_share_fail));
            }
        }
    };*/
    private static ShareUtil.ShareBeanByUm getShareInfo(Activity context, BuyDingqibaoResponse response) {
        if (response == null) {
            return null;
        }
        UMImage shareImage;
        if ("".equals(response.getShareIcon())) {
            shareImage = new UMImage(context, R.drawable.wechat_share_to_other);
        } else {
            shareImage = new UMImage(context, response.getShareIcon());
        }
        ShareUtil.ShareBeanByUm shareBeanByUm = new ShareUtil.ShareBeanByUm();
        shareBeanByUm.titleStr = response.getTitle();
        shareBeanByUm.textStr = response.getDescription();
        shareBeanByUm.targetUrl = response.getWebpageUrl();
        shareBeanByUm.mediaImg = shareImage;
        return shareBeanByUm;
    }

    /**
     * 分享至微信
     */
    public static void shareToByWeChat(Activity context, BuyDingqibaoResponse response) {
        ShareUtil.ShareBeanByUm shareBeanByUm = getShareInfo(context, response);
        ShareUtil.shareToWXByUm(context, shareBeanByUm);
/*        UMImage shareImage;
        if ("".equals(response.getShareIcon())) {
            shareImage = new UMImage(context, R.drawable.wechat_share_to_other);
        } else {
            shareImage = new UMImage(context, response.getShareIcon());
        }
        WeiXinShareContent content = new WeiXinShareContent();
        content.setShareContent(response.getDescription());
        content.setTitle(response.getTitle());
        content.setTargetUrl(response.getWebpageUrl());
        content.setShareMedia(shareImage);*/
        // ShareUtil.getInstance(context).shareToWeiXin(content, shareCallBackListener);
    }

    /**
     * 分享至朋友圈
     */
    public static void shareToByWeChatCircle(Activity context, BuyDingqibaoResponse response) {
        ShareUtil.ShareBeanByUm shareBeanByUm = getShareInfo(context, response);
        ShareUtil.shareToWXCircleByUm(context, shareBeanByUm);

        /*     UMImage shareImage;
        if ("".equals(response.getShareIcon())) {
            shareImage = new UMImage(context, R.drawable.wechat_share_to_other);
        } else {
            shareImage = new UMImage(context, response.getShareIcon());
        }
        CircleShareContent content = new CircleShareContent();
        content.setShareContent(response.getDescription());
        content.setTitle(response.getTitle());
        content.setTargetUrl(response.getWebpageUrl());
        content.setShareMedia(shareImage);*/
        //  ShareUtil.getInstance(context).shareToWeiXinCircle(content, shareCallBackListener);
    }


    @Nullable
    @OnClick(R.id.confirm_success)
    public void confirmSuccess(Button success) {
        finish();
        String bizStatus = response.getBizStatus();
        switch (bizStatus) {
            case "S":
                //成功
                EventLog.upEventLog("201610282", "click_finish", "ptfcb_success");
                break;
            case "F":
                //失败
                EventLog.upEventLog("201610282", "click_finish", "ptfcb_fail");
                break;
            case "P":
                //进行中
                EventLog.upEventLog("201610282", "click_finish", "ptfcb_underway");
                break;
        }
    }

    /**
     * 查看详情,重新购买,交易记录
     *
     * @param repay
     */
    @Nullable
    @OnClick(R.id.confirm_repay)
    public void confirmRepay(Button repay) {
        //处理结果，S成功;P处理中;F失败
        String bizStatus = response.getBizStatus();
        switch (bizStatus) {
            case "S":
                //查看详情
                if(!isFromSmallCoin) {
                    // 普通标
                    Intent intent = new Intent(this, CapitalListProductDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(CapitalListProductDetailActivity.PRODUCT_ID, response.getBidOrderNo());
                    bundle.putBoolean(CapitalListProductDetailActivity.IS_RESERVE, false);
                    bundle.putString(CapitalListProductDetailActivity.HAS_TRANSFER_AMOUNT, "");
                    bundle.putString(CapitalListProductDetailActivity.TRANSFERING_AMOUNT, "");
                    bundle.putBoolean(CapitalListProductDetailActivity.START_CAPIYAL, true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    // 小钱袋的
                    Intent intent = new Intent(this, SmallCoinSackActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(SmallCoinSackActivity.PRODUCT_ID, response.getBidOrderNo());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                EventBus.getDefault().post(new RegularPayH5ActivityFinishEvent());
                finish();
                EventLog.upEventLog("201610282", "click_detial", "ptfcb_success");
                break;
            case "F":
                //重新购买
                Intent intent = new Intent(this, RegularPayActivity.class);
                intent.putExtra(RegularPayActivity.PRODUCT_ID_KEY,response.getProductId());
                intent.putExtra(RegularPayActivity.IS_FROM_SMALL_COIN,true);
                startActivity(intent);
                finish();
                EventLog.upEventLog("201610282", "click_afresh", "ptfcb_fail");
                break;
            case "P":
                //交易记录
                MyIntent.startActivity(this, TransactionListActivity.class, "");
                finish();
                EventLog.upEventLog("201610282", "click_history", "ptfcb_underway");
                break;
        }
    }

    public void disposeBannerListInfo(BannerListResponse response){

        //*/ 设置轮播广告数据
        if (response.getBannerList() == null || response.getBannerList().isEmpty()) {
            mLayoutBanner.setVisibility(View.GONE);
        } else {
            //上报展示量埋点
            Map logMap = new HashMap();
            logMap.put("banner_id", 0);
            logMap.put("banner_l",BANNER_LOCATION);
            EventLog.upEventLog("20170519001", JSON.toJSONString(logMap));

            mLayoutBanner.setVisibility(View.VISIBLE);
            PayResultBannerPagerAdapter pagerAdapter = new PayResultBannerPagerAdapter(this, response.getBannerList(),BANNER_LOCATION);
            try {
                Field mField = ViewPager.class.getDeclaredField("mScroller");
                mField.setAccessible(true);
                IndexBannerScroller mScroller = new IndexBannerScroller(this, new AccelerateInterpolator());
                mField.set(mViewPagerBanner, mScroller);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mViewPagerBanner.setAdapter(pagerAdapter);
            mViewPagerBanner.setOnTouchListener(mBannerTouchListner);
            mViewPagerBanner.setOnPageChangeListener(mOnPageChangeListener);
            if (response.getBannerList().size() > 1) {
                mIndicatorsBanner.setVisibility(View.VISIBLE);
                // 设置indicators
                initPageIndicator(response.getBannerList().size());
                // 设置此值实现往左滑动
                mCurrentPagerIndex = response.getBannerList().size() * 100;
                mViewPagerBanner.setCurrentItem(mCurrentPagerIndex);
                // 少于一张不用自动轮播, 2张及以上开始倒计时轮播
                initCountDownTimer();
            } else {
                mIndicatorsBanner.setVisibility(View.GONE);
                // 如果之前有轮播，则停止
                if (mAutoRefreshCountDownTimer != null) {
                    mAutoRefreshCountDownTimer.cancel();
                }
            }
        }
    }

    private String BANNER_LOCATION = "";
    private static final String BANNER_FCB_BUY = "fcb_buy";
    private static final String BANNER_FCB_TRANS = "fcb_trans";
    private static final String BANNER_XQD_BAG = "xqd_bag";
    AutoRefreshCountDownTimer mAutoRefreshCountDownTimer = null;
    private static final int UPTATE_CURRENT_ITEM = 0;
    private int mCurrentPagerIndex = 0;
    private List<View> mIndicatorViewList = null;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPTATE_CURRENT_ITEM:
                    if (msg.arg1 != 0) {
                        mViewPagerBanner.setCurrentItem(msg.arg1);
                    } else {
                        // false 当从末页调到首页时，不显示翻页动画效果，
                        mViewPagerBanner.setCurrentItem(msg.arg1, false);
                    }
                    break;
            }
        }
    };
    private void initPageIndicator(int totalCount) {
        if (mIndicatorViewList == null) {
            mIndicatorViewList = new ArrayList<>();
        } else {
            mIndicatorViewList.clear();
        }
        mIndicatorsBanner.removeAllViews();
        for (int i = 0; i < totalCount; i++) {
            View view = new View(this);
            view.setBackgroundResource(R.drawable.banner_indicator_normal);
            mIndicatorsBanner.addView(view);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            layoutParams.width = getResources().getDimensionPixelOffset(R.dimen._6);
            layoutParams.height = getResources().getDimensionPixelOffset(R.dimen._6);
            layoutParams.setMargins(0, 0, getResources().getDimensionPixelOffset(R.dimen._6), 0);
            mIndicatorViewList.add(view);
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        //图片左右滑动时候，将当前页的圆点图片设为选中状态
        @Override
        public void onPageSelected(int position) {
            if (mIndicatorViewList == null || mIndicatorViewList.isEmpty()) {
                return;
            }

            mCurrentPagerIndex = position;
            int currentIndex = position % mIndicatorViewList.size();
            if (currentIndex < 0) {
                currentIndex = mIndicatorViewList.size() + currentIndex;
            }
            updateCurrentIndicator(currentIndex);
        }

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private View.OnTouchListener mBannerTouchListner = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    // 暂停轮播计时
                    if (mAutoRefreshCountDownTimer != null) {
                        mAutoRefreshCountDownTimer.pause();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    // 恢复轮播计时
                    if (mAutoRefreshCountDownTimer != null) {
                        mAutoRefreshCountDownTimer.resume();
                    }
                    break;
            }
            return false;
        }
    };
    private void initCountDownTimer() {
        if (mAutoRefreshCountDownTimer != null) {
            mAutoRefreshCountDownTimer.cancel();
        }
        mAutoRefreshCountDownTimer = new AutoRefreshCountDownTimer(4000) {
            @Override
            public void onTick() {
                Message message = new Message();
                message.what = UPTATE_CURRENT_ITEM;
                if (mCurrentPagerIndex < Integer.MAX_VALUE) {
                    message.arg1 = mCurrentPagerIndex + 1;
                } else {
                    message.arg1 = 0;
                }
                mHandler.sendMessage(message);
            }
        };
        mAutoRefreshCountDownTimer.start();
    }

    /**
     * 设置当前选中的indicator
     *
     * @param position 选中的position
     */
    private void updateCurrentIndicator(int position) {
        if (mIndicatorViewList == null || mIndicatorViewList.isEmpty()) {
            return;
        }
        if (position < mIndicatorViewList.size()) {
            for (int i = 0; i < mIndicatorViewList.size(); i++) {
                if (position == i) {
                    mIndicatorViewList.get(i).setBackgroundResource(R.drawable.banner_indicator_current);
                } else {
                    mIndicatorViewList.get(i).setBackgroundResource(R.drawable.banner_indicator_normal);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAutoRefreshCountDownTimer != null) {
            mAutoRefreshCountDownTimer.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAutoRefreshCountDownTimer != null) {
            mAutoRefreshCountDownTimer.pause();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) {
            return;
        }

        if(requestCode == LOTTERY_REQUEST) {
            tvLotteryDesc.setText("您已抽过奖啦");
            llFirstInvestLottery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
