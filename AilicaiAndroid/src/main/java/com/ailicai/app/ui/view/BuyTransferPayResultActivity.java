package com.ailicai.app.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.share.ShareUtil;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.MathUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.UIUtils;
import com.ailicai.app.eventbus.RegularPayEvent;
import com.ailicai.app.eventbus.RegularPayH5ActivityFinishEvent;
import com.ailicai.app.model.request.BannerListRequest;
import com.ailicai.app.model.response.BannerListResponse;
import com.ailicai.app.model.response.BuyDingqibaoResponse;
import com.ailicai.app.ui.asset.CapitalListProductDetailActivity;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.webview.TransparentWebViewActivity;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.dialog.ShareFinanceDialog;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.view.banner.IndexBannerScroller;
import com.ailicai.app.ui.view.banner.PayResultBannerPagerAdapter;
import com.ailicai.app.ui.view.transaction.TransactionListActivity;
import com.alibaba.fastjson.JSON;
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
 * 转让房产宝购买结果
 * Created by ZhouXuan on 2016/8/2.
 */
public class BuyTransferPayResultActivity extends BaseBindActivity {

    public int LOTTERY_REQUEST = 1;

    @Bind(R.id.ll_result_success)
    View ll_result_success;
    @Bind(R.id.fl_result_not_success)
    View fl_result_not_success;
    @Bind(R.id.fl_result_buying)
    View fl_result_buying;
    @Bind(R.id.tvTotalMoneyDesc)
    TextView tvTotalMoneyDesc;
    @Bind(R.id.tvFailReason)
    TextView tvFailReason;
    @Bind(R.id.tvProductName)
    TextView tvProductName;
    @Bind(R.id.tvMoneyCount)
    TextView tvMoneyCount;
    @Bind(R.id.tvPrice)
    TextView tvPrice;
    @Bind(R.id.tvRestPeriod)
    TextView tvRestPeriod;
    @Bind(R.id.tvInterestAmount)
    TextView tvInterestAmount;
    @Bind(R.id.confirm_repay)
    Button confirm_repay;

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


    private BuyDingqibaoResponse response;
    public static String KEY = "response";

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
        return R.layout.activity_transfer_pay_result;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mView = BuyTransferPayResultActivity.this;
        response = (BuyDingqibaoResponse) getIntent().getExtras().getSerializable(KEY);
        //处理结果，S成功;P处理中;F失败
        String bizStatus = response.getBizStatus();
        //post 事件刷新相关页面
        postEventBus(bizStatus);
        switch (bizStatus) {
            case "S":
                //购买成功
                initPaySuccess();
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
        fl_result_not_success.setVisibility(View.VISIBLE);
        ll_result_success.setVisibility(View.GONE);
        fl_result_buying.setVisibility(View.GONE);
        tvFailReason.setText(response.getMessage());
        confirm_repay.setText("重新购买");
        EventLog.upEventLog("201610282", "show", "zrfcb_fail");
    }

    /**
     * 购买成功
     */
    private void initPaySuccess() {
        ll_result_success.setVisibility(View.VISIBLE);
        fl_result_not_success.setVisibility(View.GONE);
        fl_result_buying.setVisibility(View.GONE);
        tvTotalMoneyDesc.setText("到期后，您将获得预计" + response.getBackAmount() + "元回款");
        confirm_repay.setText("查看详情");
        EventLog.upEventLog("201610282", "show", "zrfcb_success");
        setInvestActivity();
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
                    MyIntent.startActivityForResult(BuyTransferPayResultActivity.this, TransparentWebViewActivity.class, dataMap,LOTTERY_REQUEST);
                    overridePendingTransition(R.anim.none,R.anim.none);
                }
            });
        } else {
            llFirstInvestLottery.setVisibility(View.GONE);
            getBannerList(10);
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
     * 购买进行中
     */
    private void initPayHandling() {
        fl_result_buying.setVisibility(View.VISIBLE);
        ll_result_success.setVisibility(View.GONE);
        fl_result_not_success.setVisibility(View.GONE);
        tvProductName.setText(response.getProductName());
        tvMoneyCount.setText(MathUtil.saveTwoDecimalHalfUp(response.getAmount()) + "元");
        tvPrice.setText(response.getTransferPrice() + "元");
        tvRestPeriod.setText(response.getHorizon());
        tvInterestAmount.setText(response.getYearInterestRate());
        confirm_repay.setText("交易记录");
        EventLog.upEventLog("201610282", "show", "zrfcb_underway");
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
                    shareToByWeChat(BuyTransferPayResultActivity.this, response);
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
                    shareToByWeChatCircle(BuyTransferPayResultActivity.this, response);
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

    /**
     * 分享至微信
     */
    private static void shareToByWeChat(Activity context, BuyDingqibaoResponse response) {
        ShareUtil.ShareBeanByUm shareBeanByUm = getShareInfo(context, response);
        ShareUtil.shareToWXByUm(context, shareBeanByUm);
    }

    /**
     * 分享至朋友圈
     */
    private static void shareToByWeChatCircle(Activity context, BuyDingqibaoResponse response) {
        ShareUtil.ShareBeanByUm shareBeanByUm = getShareInfo(context, response);
        ShareUtil.shareToWXCircleByUm(context, shareBeanByUm);
    }

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


    @Nullable
    @OnClick(R.id.confirm_success)
    public void confirmSuccess(Button success) {
        switch (response.getBizStatus()) {
            case "S":
                //购买成功
                EventLog.upEventLog("201610282", "click_finish", "zrfcb_success");
                break;
            case "F":
                //购买失败
                EventLog.upEventLog("201610282", "click_finish", "zrfcb_fail");
                break;
            case "P":
                //购买进行中
                EventLog.upEventLog("201610282", "click_finish", "zrfcb_underway");
                break;
        }
        finish();
    }

    /**
     * 查看详情，交易记录，重新购买
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
                EventLog.upEventLog("201610282", "click_detial", "zrfcb_success");
                Intent intent = new Intent(this, CapitalListProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(CapitalListProductDetailActivity.PRODUCT_ID, response.getBidOrderNo());
                bundle.putBoolean(CapitalListProductDetailActivity.IS_RESERVE, false);
                bundle.putString(CapitalListProductDetailActivity.HAS_TRANSFER_AMOUNT, "");
                bundle.putString(CapitalListProductDetailActivity.TRANSFERING_AMOUNT, "");
                bundle.putBoolean(CapitalListProductDetailActivity.START_CAPIYAL, true);
                intent.putExtras(bundle);
                startActivity(intent);
                EventBus.getDefault().post(new RegularPayH5ActivityFinishEvent());
                finish();
                break;
            case "F":
                //重新购买
                EventLog.upEventLog("201610282", "click_afresh", "zrfcb_fail");
                MyIntent.startActivity(this, BuyTransferPayActivity.class, response.getProductId());
                finish();
                break;
            case "P":
                //交易记录
                EventLog.upEventLog("201610282", "click_history", "zrfcb_underway");
                MyIntent.startActivity(this, TransactionListActivity.class, "");
                finish();
                break;
        }
    }

    private BuyTransferPayResultActivity mView;
    private BannerListResponse mBannerListResponse;

    public void getBannerList(int bType){
        if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
            BannerListRequest request = new BannerListRequest();
            request.setUserId(UserInfo.getInstance().getUserId());
            request.setBtype(bType);
            ServiceSender.exec(this, request, new BannerListCallback());
        }
    }

    class BannerListCallback extends IwjwRespListener<BannerListResponse> {

        @Override
        public void onStart() {
            mView.showLoadView();
        }

        @Override
        public void onJsonSuccess(BannerListResponse response) {
            mView.showContentView();
            if (response.getErrorCode() == 0) {
                disposeBannerListInfo(response);
            } else {
                onFailInfo(response.getMessage());
            }
        }

        @Override
        public void onFailInfo(String errorInfo) {
            mView.showErrorView(errorInfo);
        }

    }

    public void disposeBannerListInfo(BannerListResponse response){

        mBannerListResponse = response;
        //*/ 设置轮播广告数据
        if (response.getBannerList() == null || response.getBannerList().isEmpty()) {
            mLayoutBanner.setVisibility(View.GONE);
        } else {
            // 上报展示量埋点
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
    private String BANNER_LOCATION = "fcb_trans";
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
