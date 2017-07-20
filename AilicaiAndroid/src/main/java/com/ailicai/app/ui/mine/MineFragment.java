package com.ailicai.app.ui.mine;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.constants.CommonTag;
import com.ailicai.app.common.push.model.PushMessage;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.eventbus.ExitEvent;
import com.ailicai.app.eventbus.LoginEvent;
import com.ailicai.app.eventbus.MineShowRedPointEvent;
import com.ailicai.app.eventbus.NewNotifMsgEvent;
import com.ailicai.app.eventbus.RefreshPushEvent;
import com.ailicai.app.model.request.AssetInfoNewRequest;
import com.ailicai.app.model.request.UserInfoRequest;
import com.ailicai.app.model.response.AssetInfoNewResponse;
import com.ailicai.app.model.response.UserInfoResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.ui.buy.NoSetSafeCardHint;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.login.UserInfoBase;
import com.ailicai.app.ui.login.UserManager;
import com.ailicai.app.ui.message.MessageActivity;
import com.ailicai.app.ui.message.MsgLiteView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.SoftReference;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

import static com.ailicai.app.ui.mine.JumpProcessActivity.ACTION_KEY;
import static com.ailicai.app.ui.mine.JumpProcessActivity.ACTION_VAL_CHARGE;
import static com.ailicai.app.ui.mine.JumpProcessActivity.ACTION_VAL_GET_CASH;

public class MineFragment extends BaseBindFragment implements ObservableScrollViewCallbacks {

    @Bind(R.id.title_root)
    RelativeLayout titleRoot;
    @Bind(R.id.tv_new_msg_point)
    TextView mTvNewMsgPoint;
    /**
     * 点击头像区域跳转至个人信息编辑页面
     */
    public OnClickListener userLayoutOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fl_msg_container:
                    //消息点击
                    mTvNewMsgPoint.setVisibility(View.GONE);
                    Intent intent = new Intent(getWRActivity(), MessageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.userPhoto:
                    MyIntent.startActivity(getActivity(), SettingsActivity.class, getDataMap());
                    break;
            }
        }
    };
    @Bind(R.id.my_scroll_view)
    ObservableScrollView mScrollView;
    @Bind(R.id.ticket_red_dot)
    TextView ticket_red_dot;
    @Bind(R.id.personal_root_view)
    LinearLayout mAllView;
    @Bind(R.id.mine_top_head)
    LinearLayout mineTopHead;
    @Bind(R.id.mine_top_head_bg)
    LinearLayout mineTopHeadBg;
    @Bind(R.id.mine_not_login)
    LinearLayout mineNotLogin;
    @Bind(R.id.mine_login)
    LinearLayout mineLogin;
    @Bind(R.id.mine_top_margin)
    LinearLayout mineTopMargin;
    @Bind(R.id.mine_top_margin_bg)
    LinearLayout mineTopMarginBg;
    @Bind(R.id.mine_top_margin_bg_scroll)
    LinearLayout mineTopMarginScroll;
    @Bind(R.id.tvLogin)
    TextView tvLogin;
    @Bind(R.id.userPhoto)
    ImageView userPhoto;
    @Bind(R.id.fl_msg_container)
    FrameLayout flMsgContainer;
    @Bind(R.id.tv_eyes_status)
    TextView tvEyesStatus;
    @Bind(R.id.totalAsset)
    TextView totalAsset;
    @Bind(R.id.yestodayIncome)
    TextView yestodayIncome;
    @Bind(R.id.totalIncome)
    TextView totalIncome;
    @Bind(R.id.accountBalance)
    TextView accountBalance;
    @Bind(R.id.rewards_money)
    TextView rewardsMoney;
    @Bind(R.id.accountbalance_layout)
    RelativeLayout accountbalanceLayout;
    @Bind(R.id.purchase_view)
    LinearLayout purchaseView;
    @Bind(R.id.purchaseAmount)
    TextView purchaseAmount;
    private MinePresenter mPresenter;
    private boolean eyeOpen = false;
    private AssetInfoNewResponse assetInfoNewResponse;

    private LoginManager.LoginAction loginAction = LoginManager.LoginAction.ACTION_INDEX_NORMAL;
    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvLogin:
                    LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 获取页面传参的数据Map
     *
     * @return
     */
    public Map<String, Object> getDataMap() {
        if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
            long userId = MyPreference.getInstance().read(UserInfo.USERINFO_KEY_USER_ID, new Long(0));
            UserInfoBase infoBase = UserManager.getInstance(MyApplication.getInstance()).getUserByUserId(userId);
            Map<String, Object> dataMap = ObjectUtil.newHashMap();
            dataMap.put(CommonTag.PERSONAL_USER_ID, infoBase.getUserId());
            dataMap.put(CommonTag.PERSONAL_USER_NAME, infoBase.getRealName());
            dataMap.put(CommonTag.PERSONAL_USER_R_NAME, infoBase.getrName());
            dataMap.put(CommonTag.PERSONAL_USER_SEX, infoBase.getGender());
            dataMap.put(CommonTag.PERSONAL_USER_PHONE, infoBase.getMobile());
            dataMap.put(CommonTag.PERSONAL_USER_ISREALNAMEVERIFY, infoBase.getIsRealNameVerify());
            dataMap.put(CommonTag.PERSONAL_USER_IDCARDNUMBER, infoBase.getIdCardNo());
            return dataMap;
        }
        return ObjectUtil.newHashMap();
    }

    @Override
    public int getLayout() {
        return R.layout.personal_layout;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (mPresenter == null) {
            mPresenter = new MinePresenter();
        }
        EventBus.getDefault().register(this);

        mineTopMargin.getLayoutParams().height = CommonUtil.getTitleHeight(getWRActivity());
        mineTopMarginScroll.getLayoutParams().height = CommonUtil.getTitleHeight(getWRActivity());
        mineTopMarginBg.getLayoutParams().height = CommonUtil.getTitleHeight(getWRActivity());
        ((RelativeLayout.LayoutParams) titleRoot.getLayoutParams()).setMargins(0, CommonUtil.getStatusBarHeight(getWRActivity()), 0, 0);

        //添加顶部用户信息区域布局文件(动画效果)
        CommonUtil.addAnimForView(mAllView);
        mScrollView.setScrollViewCallbacks(this);
        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                //mScrollView.scrollTo(0, mFlexibleSpaceImageHeight - mActionBarSize);
                // If you'd like to start from scrollY == 0, don't write like this:
                //mScrollView.scrollTo(0, 0);
                // The initial scrollY is 0, so it won't invoke onScrollChanged().
                // To do this, use the following:
                //onScrollChanged(0, false, false);
            }
        });

        eyeOpen = MyPreference.getInstance().read("eyeOpen", false);
        MsgLiteView.refreshNoticeNums(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshMyDataFromServer();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        MyPreference.getInstance().write("eyeOpen", eyeOpen);
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refreshMyDataFromServer();
        }
    }

    /**
     * 接收登陆成功或者退出登录之后的事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLoginEvent(LoginEvent event) {
        if (event.isLoginSuccess()) {
            setUIData();
            refreshMyDataFromServer();
        } else {
            loginAction = LoginManager.LoginAction.ACTION_INDEX_NORMAL;
            setUIData();
        }
        mScrollView.smoothScrollTo(0, 0);
    }

    /**
     * 登陆后跳转至目标页面
     *
     * @param action
     */
    public void jumpToMenuTarget(LoginManager.LoginAction action) {
        if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_BANK_CARD.getActionIndex())) {
            //银行卡
            mPresenter.gotoMyBankCrad(getWRActivity());
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_CARD_COUPONST.getActionIndex())) {
            //卡券
            mPresenter.gotoCardCoupon(getWRActivity());
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_TRANSACTION_LIST.getActionIndex())) {
            //交易记录
            mPresenter.gotoTransactionList(getWRActivity());
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_RESERVERECORD_LIST.getActionIndex())) {
            //预约记录
            mPresenter.gotoReserveRecordList(getWRActivity());
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_REWARDS_LIST.getActionIndex())) {
            //邀请奖励
            mPresenter.goRewards(getWRActivity());
        }
        loginAction = LoginManager.LoginAction.ACTION_INDEX_NORMAL;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int mFlexibleSpaceImageHeight = mineTopHead.getMeasuredHeight();
        int scrollViewMeasuredHeight = mScrollView.getChildAt(0).getMeasuredHeight();
        int height = mScrollView.getHeight();
        if ((scrollViewMeasuredHeight - height) >= mFlexibleSpaceImageHeight) {
            //onScrollDisplay(scrollY);
        } else {
            //顶部不滚动
        }
        onScrollDisplay(scrollY);
    }

    public void onScrollDisplay(int scrollY) {
        float flexibleRange = mineTopHead.getMeasuredHeight() - CommonUtil.getTitleHeight(getWRActivity()) - CommonUtil.getStatusBarHeight(getWRActivity());
        float alpha = ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1);
        mineNotLogin.setAlpha(1 - alpha);
        mineLogin.setAlpha(1 - alpha);
        int minOverlayTransitionY = CommonUtil.getTitleHeight(getWRActivity()) - mineTopHead.getMeasuredHeight();
        mineTopHeadBg.setTranslationY(ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        mineTopHead.setTranslationY(-scrollY);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        String version = "";
        try {
            PackageManager manager = MyApplication.getInstance().getPackageManager();
            PackageInfo info = manager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
            version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }


    public void setUIData() {
        if (!MineFragment.this.isVisible() || !MineFragment.this.isAdded()) {
            return;
        }
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            ticket_red_dot.setVisibility(View.GONE);
            mineNotLogin.setVisibility(View.VISIBLE);
            mineLogin.setVisibility(View.GONE);
            tvLogin.setOnClickListener(mOnClickListener);
            userPhoto.setVisibility(View.GONE);
            userPhoto.setClickable(false);
            userPhoto.setOnClickListener(null);

            flMsgContainer.setVisibility(View.GONE);
            flMsgContainer.setClickable(false);
            flMsgContainer.setOnClickListener(null);

            accountbalanceLayout.setVisibility(View.GONE);
            purchaseView.setVisibility(View.GONE);
        } else if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
            mineNotLogin.setVisibility(View.GONE);
            mineLogin.setVisibility(View.VISIBLE);

            flMsgContainer.setVisibility(View.VISIBLE);
            flMsgContainer.setClickable(true);
            flMsgContainer.setOnClickListener(userLayoutOnClickListener);

            userPhoto.setVisibility(View.VISIBLE);
            userPhoto.setClickable(true);
            userPhoto.setOnClickListener(userLayoutOnClickListener);
            accountbalanceLayout.setVisibility(View.VISIBLE);
            purchaseView.setVisibility(View.GONE);

            //根据登录的手机号获取已保存的用户信息
            long userId = MyPreference.getInstance().read(UserInfo.USERINFO_KEY_USER_ID, new Long(0));
            UserInfoBase infoBase = UserManager.getInstance(MyApplication.getInstance()).getUserByUserId(userId);
            int resID = R.drawable.avatar_default;
            DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
            builder.cacheInMemory(true)// 是否缓存都內存中
                    .cacheOnDisk(true)// 是否缓存到sd卡上
                    .considerExifParams(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .showImageForEmptyUri(resID)
                    .showImageOnFail(resID)
                    .showImageOnLoading(resID)
                    .displayer(new SimpleBitmapDisplayer());


            if (assetInfoNewResponse != null) {
                handlerEyesStatus();
                //卡券红点
                setVoucherRedDotState(assetInfoNewResponse.getVoucherRedPoint() > 0);
            }

            // 卡券小红点
            if (isShowVoucherRedDotState()) {
                setVoucherRedDotState(true);
            } else {
                setVoucherRedDotState(false);
            }

            //自动跳转至目标页面
            jumpToMenuTarget(loginAction);
        }
    }

    public void setDataInfo() {
        totalAsset.setText(assetInfoNewResponse.getTotalAsset());
        yestodayIncome.setText(assetInfoNewResponse.getYestodayIncome());
        totalIncome.setText(assetInfoNewResponse.getTotalIncome());
        accountBalance.setText(assetInfoNewResponse.getAccountBalance());
        rewardsMoney.setText("待发收益" + 0.00 + "元");
        if (assetInfoNewResponse.getPurchaseCount() > 0) {
            purchaseView.setVisibility(View.VISIBLE);
            purchaseAmount.setText(assetInfoNewResponse.getPurchaseCount() + "笔共" + assetInfoNewResponse.getPurchaseAmount());
        } else {
            purchaseView.setVisibility(View.GONE);
        }
    }

    public void setDataInfo(String eyes) {
        totalAsset.setText(eyes);
        yestodayIncome.setText(eyes);
        totalIncome.setText(eyes);
        accountBalance.setText(eyes);
        rewardsMoney.setText("待发收益" + eyes + "元");
        if (assetInfoNewResponse.getPurchaseCount() > 0) {
            purchaseView.setVisibility(View.VISIBLE);
            purchaseAmount.setText("*" + "笔共" + eyes + "元");
        } else {
            purchaseView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tv_eyes_status)
    void clickEyesStatus() {
        eyeOpen = !eyeOpen;
        handlerEyesStatus();
    }

    private void handlerEyesStatus() {
        if (!eyeOpen) {
            //显示明文
            tvEyesStatus.setText(R.string.eyes_opening);
            setDataInfo();
        } else {
            //显示密文
            tvEyesStatus.setText(R.string.eyes_closed);
            setDataInfo("****");
        }

    }

    @OnClick(R.id.about_us)
    void goAboutUs() {
        MyIntent.startActivity(getWRActivity(), AboutUsActivity.class, null);
    }


    /**
     * 点击版本更新时，强制更新退出客户端
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleExitEvent(ExitEvent event) {
        if (event.isForceExit()) {
            //更新版本时强制退出
            SystemUtil.exitApplication(getActivity());
        } else {
            //退出登录
            LoginManager.loginOut(getActivity());
        }
    }

    /**
     * 顶部消息红点是否展示
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handlefinal(final NewNotifMsgEvent event) {
        mTvNewMsgPoint.setVisibility(event.notifNum > 0 ? View.VISIBLE : View.GONE);
        if (event.notifNum > 0) {
            MineShowRedPointEvent showEvent = new MineShowRedPointEvent();
            EventBus.getDefault().post(showEvent);
        }
    }


    /**
     * 获取我的页面相关信息
     */
    void refreshMyDataFromServer() {
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            setUIData();
            return;
        }
        getAssetInfo();
        //getUserInfo();
    }

    public void getAssetInfo() {
        AssetInfoNewRequest request = new AssetInfoNewRequest();
        request.setUserId(UserInfo.getInstance().getUserId());
        ServiceSender.exec(this, request, new IwjwRespListener<AssetInfoNewResponse>(this) {

            @Override
            public void onStart() {
                //showLoadTranstView();
            }

            @Override
            public void onJsonSuccess(AssetInfoNewResponse jsonObject) {
                //showContentView();
                assetInfoNewResponse = jsonObject;
                setUIData();
            }

            @Override
            public void onFailInfo(String errorInfo) {
                //showContentView();
                ToastUtil.showInCenter(errorInfo);
            }
        });
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        UserInfoRequest request = new UserInfoRequest();
        request.setUserId((int) UserInfo.getInstance().getUserId());
        ServiceSender.exec(this, request, new UserInfoResponseIwjwRespListener(this));
    }

    @Override
    public void reloadData() {
        if (!MineFragment.this.isVisible() || !MineFragment.this.isAdded()) {
            return;
        }
        refreshMyDataFromServer();
    }

    /**
     * 卡券
     */
    @OnClick(R.id.mineCardCouponst)
    void onClickRedEnvelopes() {
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            loginAction = LoginManager.LoginAction.ACTION_INDEX_CARD_COUPONST;
            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
        } else {
            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_CARD_COUPONST);
            // 点击后卡券后，本地小红点状态改为false
            setVoucherRedDotState(false);
        }
    }

    /**
     * 银行卡
     */
    @OnClick(R.id.rl_bank_card)
    void onClickBankCard() {
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            loginAction = LoginManager.LoginAction.ACTION_INDEX_BANK_CARD;
            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
        } else {
            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_BANK_CARD);
        }
    }

    /**
     * 交易记录
     */
    @OnClick(R.id.gotoTransactionList)
    void gotoTransactionList() {
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            loginAction = LoginManager.LoginAction.ACTION_INDEX_TRANSACTION_LIST;
            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
        } else {
            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_TRANSACTION_LIST);
        }
    }

    /**
     * 预约记录
     */
    @OnClick(R.id.gotoReserveRecordList)
    void gotoReserveRecordList() {
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            loginAction = LoginManager.LoginAction.ACTION_INDEX_RESERVERECORD_LIST;
            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
        } else {
            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_RESERVERECORD_LIST);
        }
    }

    @OnClick(R.id.rewards)
    void goRewards() {
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            loginAction = LoginManager.LoginAction.ACTION_INDEX_REWARDS_LIST;
            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
        } else {
            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_REWARDS_LIST);
        }
    }

    //提现
    @OnClick(R.id.tv_account_balance_get_cash)
    void accountBalanceGetCashClick() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        if (!NoSetSafeCardHint.isShowHintDialog((BaseBindActivity) getWRActivity())) {
            Map<String, String> dataMap = ObjectUtil.newHashMap();
            dataMap.put(ACTION_KEY, ACTION_VAL_GET_CASH);
            MyIntent.startActivity(getWRActivity(), JumpProcessActivity.class, dataMap);
        }


    }

    //充值
    @OnClick(R.id.tv_account_balance_charge)
    void accountBalanceChargeClick() {
        if (!NoSetSafeCardHint.isShowHintDialog((BaseBindActivity) getWRActivity())) {
            Map<String, String> dataMap = ObjectUtil.newHashMap();
            dataMap.put(ACTION_KEY, ACTION_VAL_CHARGE);
            MyIntent.startActivity(getWRActivity(), JumpProcessActivity.class, dataMap);
        }

    }

    /**
     * 卡券上小红点
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleRefreshPushEvent(RefreshPushEvent event) {
        if (event.getMsgType() == PushMessage.REMINDTYPENEWVOUCHER || event.getMsgType() ==
                PushMessage.REMINDTYPECOUPONBANNER || event.getMsgType() == PushMessage.REMINDTYPETIYANJI) {
            setVoucherRedDotState(true);
        }
    }

    private void setVoucherRedDotState(boolean isShow) {
        if (isShow) {
            MineShowRedPointEvent showEvent = new MineShowRedPointEvent();
            EventBus.getDefault().post(showEvent);
            ticket_red_dot.setVisibility(View.VISIBLE);
        } else {
            ticket_red_dot.setVisibility(View.INVISIBLE);
        }
        saveVoucherRedDotStateByUser(isShow);
    }

    private void saveVoucherRedDotStateByUser(boolean isShow) {
        long userId = UserManager.getInstance(getWRActivity()).getUserInfoBase().getUserId();
        MyPreference.getInstance().write(userId + "VoucherRedDotIsShow", isShow);
    }

    public boolean isShowVoucherRedDotState() {
        long userId = UserManager.getInstance(getWRActivity()).getUserInfoBase().getUserId();
        return MyPreference.getInstance().read(userId + "VoucherRedDotIsShow", false);
    }

    private static class UserInfoResponseIwjwRespListener extends IwjwRespListener<UserInfoResponse> {

        private SoftReference<MineFragment> personalCenterFragmentSoftReference;

        UserInfoResponseIwjwRespListener(MineFragment personalCenterFragment) {
            super(personalCenterFragment);
            this.personalCenterFragmentSoftReference = new SoftReference<>(personalCenterFragment);
        }

        @Override
        public void onJsonSuccess(UserInfoResponse jsonObject) {
            LoginManager.saveUserInfo(jsonObject);
            if (null != personalCenterFragmentSoftReference.get()) {
                personalCenterFragmentSoftReference.get().setUIData();
            }
        }

        @Override
        public void onFailInfo(String errorInfo) {
            if (null != personalCenterFragmentSoftReference.get()) {
                personalCenterFragmentSoftReference.get().setUIData();
            }
            ToastUtil.showInCenter(errorInfo);
        }
    }


}
