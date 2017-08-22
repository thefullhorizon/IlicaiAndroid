package com.ailicai.app.ui.mine;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
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
import com.ailicai.app.common.utils.DeviceUtil;
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
import com.ailicai.app.model.response.AssetInfoNewResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.ui.buy.AutomaticTenderActivity;
import com.ailicai.app.ui.buy.NoSetSafeCardHint;
import com.ailicai.app.ui.buy.ProcessActivity;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.login.UserInfoBase;
import com.ailicai.app.ui.login.UserManager;
import com.ailicai.app.ui.message.MessageActivity;
import com.ailicai.app.ui.message.MsgLiteView;
import com.ailicai.app.ui.view.AccountTopupActivity;
import com.ailicai.app.ui.view.AccountWithdrawActivity;
import com.ailicai.app.ui.view.AssetInViewOfBirdActivity;
import com.ailicai.app.ui.view.CapitalActivity;
import com.ailicai.app.ui.view.detail.IncomeDetailActivity;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class MineFragment extends BaseBindFragment implements ObservableScrollViewCallbacks, SwipeRefreshLayout.OnRefreshListener {

    /**
     * 点击头像区域跳转至个人信息编辑页面
     */
    public OnClickListener userLayoutOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fl_msg_container:
                    //消息点击
                    Intent intent = new Intent(getWRActivity(), MessageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.userPhoto:
                    MyIntent.startActivity(getActivity(), SettingsActivity.class, getDataMap());
                    break;
            }
        }
    };
    @Bind(R.id.title_root)
    RelativeLayout titleRoot;
    @Bind(R.id.tv_new_msg_point)
    TextView mTvNewMsgPoint;
    @Bind(R.id.top_bg)
    FrameLayout topBg;
    @Bind(R.id.my_scroll_view)
    ObservableScrollView mScrollView;
    @Bind(R.id.ticket_red_dot)
    TextView ticket_red_dot;
    @Bind(R.id.personal_root_view)
    LinearLayout mAllView;
    @Bind(R.id.mine_top_head)
    LinearLayout mineTopHead;
    @Bind(R.id.mine_not_login)
    LinearLayout mineNotLogin;
    @Bind(R.id.mine_login)
    LinearLayout mineLogin;
    @Bind(R.id.mine_top_margin)
    LinearLayout mineTopMargin;
    @Bind(R.id.title_root_layout)
    RelativeLayout titleBg;
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
    @Bind(R.id.top_line)
    View topLine;
    @Bind(R.id.purchaseAmount)
    TextView purchaseAmount;
    @Bind(R.id.swipe)
    SwipeRefreshLayout mSwipeLayout;
    private MinePresenter mPresenter;
    private boolean eyeOpen = false;
    private AssetInfoNewResponse assetInfoNewResponse;
    private int mFlexibleSpaceImageHeight;

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
            dataMap.put(CommonTag.PERSONAL_BANK_NAME, infoBase.getBankName());
            dataMap.put(CommonTag.PERSONAL_BANKCARDTAILNO, infoBase.getBankcardTailNo());
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

        mFlexibleSpaceImageHeight = DeviceUtil.getScreenSize()[0] * 34 / 75;

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.main_red_color);

        //topBg.getLayoutParams().height = mFlexibleSpaceImageHeight + DeviceUtil.getPixelFromDip(getWRActivity(), 20);
        mineTopMargin.getLayoutParams().height = CommonUtil.getTitleHeight(getWRActivity());
        ((FrameLayout.LayoutParams) titleRoot.getLayoutParams()).setMargins(0, CommonUtil.getStatusBarHeight(getWRActivity()), 0, 0);

        titleBg.setAlpha(0);

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

    }

    @Override
    public void onRefresh() {
        refreshMyDataFromServer();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshMyDataFromServer();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
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
            //弹出大礼包后不自动跳转页面
            if (!event.isContinueNext()) {
                loginAction = LoginManager.LoginAction.ACTION_INDEX_NORMAL;
            }
            assetInfoNewResponse = new AssetInfoNewResponse();
            setUIData();
            refreshMyDataFromServer();
        } else {
            loginAction = LoginManager.LoginAction.ACTION_INDEX_NORMAL;
            assetInfoNewResponse = new AssetInfoNewResponse();
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
        if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_CARD_COUPONST.getActionIndex())) {
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
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_WANG_DAI_CLICK.getActionIndex())) {
            //网贷类
            mPresenter.goWangDai(getWRActivity());
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_HUOQIBAO_CLICK.getActionIndex())) {
            //活期宝
            mPresenter.goHQB(getWRActivity());
        }
        loginAction = LoginManager.LoginAction.ACTION_INDEX_NORMAL;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        onScrollDisplay(scrollY);
    }

    public void onScrollDisplay(int scrollY) {
        int mFlexibleSpaceImageHeight = mineTopHead.getMeasuredHeight();
        int scrollViewMeasuredHeight = mScrollView.getChildAt(0).getMeasuredHeight();
        int height = mScrollView.getHeight();

        float flexibleRange = mineTopHead.getMeasuredHeight() - CommonUtil.getTitleHeight(getWRActivity()) - CommonUtil.getStatusBarHeight(getWRActivity());
        float alpha = ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1);

        if ((scrollViewMeasuredHeight - height) >= mFlexibleSpaceImageHeight / 2) {
            if (UserInfo.isLogin()) {
                titleBg.setAlpha(alpha);
            } else {
                titleBg.setAlpha(0);
            }
        } else {
            titleBg.setAlpha(0);
        }

        mineNotLogin.setAlpha(1 - alpha);
        mineLogin.setAlpha(1 - alpha);
        //int minOverlayTransitionY = CommonUtil.getTitleHeight(getWRActivity()) - mineTopHead.getMeasuredHeight();
        //mineTopHeadBg.setTranslationY(ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        //mineTopHead.setTranslationY(-scrollY);
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
            mTvNewMsgPoint.setVisibility(View.GONE);
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
            topLine.setVisibility(View.GONE);
            purchaseView.setVisibility(View.GONE);
            rewardsMoney.setVisibility(View.GONE);
            long userId = MyPreference.getInstance().read(UserInfo.USERINFO_KEY_USER_ID, new Long(0));
            MyPreference.getInstance().write("eyeOpen_" + userId, eyeOpen);
        } else if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
            ticket_red_dot.setVisibility(View.GONE);
            //mTvNewMsgPoint.setVisibility(View.GONE);
            mineNotLogin.setVisibility(View.GONE);
            mineLogin.setVisibility(View.VISIBLE);

            flMsgContainer.setVisibility(View.VISIBLE);
            flMsgContainer.setClickable(true);
            flMsgContainer.setOnClickListener(userLayoutOnClickListener);

            userPhoto.setVisibility(View.VISIBLE);
            userPhoto.setClickable(true);
            userPhoto.setOnClickListener(userLayoutOnClickListener);
            accountbalanceLayout.setVisibility(View.VISIBLE);
            topLine.setVisibility(View.VISIBLE);
            purchaseView.setVisibility(View.GONE);
            rewardsMoney.setVisibility(View.GONE);

            //根据登录的手机号获取已保存的用户信息
            long userId = MyPreference.getInstance().read(UserInfo.USERINFO_KEY_USER_ID, new Long(0));
            UserInfoBase infoBase = UserManager.getInstance(MyApplication.getInstance()).getUserByUserId(userId);
            int resID = R.drawable.head_portrait;
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
                eyeOpen = MyPreference.getInstance().read("eyeOpen_" + userId, false);
                handlerEyesStatus();
                //卡券红点
                setVoucherRedDotState(assetInfoNewResponse.getVoucherRedPoint() > 0);
            }

            MsgLiteView.refreshNoticeNums(null);

            //自动跳转至目标页面
            jumpToMenuTarget(loginAction);
        }
    }

    public void setDataInfo() {
        totalAsset.setText(assetInfoNewResponse.getTotalAsset());
        yestodayIncome.setText(assetInfoNewResponse.getYestodayIncome());
        totalIncome.setText(assetInfoNewResponse.getTotalIncome());
        accountBalance.setText(assetInfoNewResponse.getAccountBalance());
        rewardsMoney.setText("待发收益" + assetInfoNewResponse.getInviteReward() + "元");
        if (TextUtils.isEmpty(assetInfoNewResponse.getInviteReward()) ||
                "0.00".equals(assetInfoNewResponse.getInviteReward()) ||
                "0".equals(assetInfoNewResponse.getInviteReward()) ||
                "0.0".equals(assetInfoNewResponse.getInviteReward())) {
            rewardsMoney.setVisibility(View.GONE);
        } else {
            rewardsMoney.setVisibility(View.GONE);
        }
        if (assetInfoNewResponse.getPurchaseCount() > 0) {
            purchaseView.setVisibility(View.VISIBLE);
            purchaseAmount.setText(assetInfoNewResponse.getPurchaseCount() + "笔共" + assetInfoNewResponse.getPurchaseAmount() + "元");
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
        if (TextUtils.isEmpty(assetInfoNewResponse.getInviteReward()) ||
                "0.00".equals(assetInfoNewResponse.getInviteReward()) ||
                "0".equals(assetInfoNewResponse.getInviteReward()) ||
                "0.0".equals(assetInfoNewResponse.getInviteReward())) {
            rewardsMoney.setVisibility(View.GONE);
        } else {
            rewardsMoney.setVisibility(View.GONE);
        }
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

    @OnClick(R.id.rl_auto_invest)
    void autoInvest() {
        if (!NoSetSafeCardHint.isOpenAccount()) {
            Intent intent = new Intent(getWRActivity(), ProcessActivity.class);
            startActivity(intent);
        } else {
            AutomaticTenderActivity.open(getWRActivity(), assetInfoNewResponse.getNetLoanBalance(), assetInfoNewResponse.getAccountBalance());
        }

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
            showEvent.setShowRedPoint(true);
            EventBus.getDefault().post(showEvent);
        } else {
            if (ticket_red_dot.getVisibility() == View.GONE) {
                MineShowRedPointEvent showEvent = new MineShowRedPointEvent();
                showEvent.setShowRedPoint(false);
                EventBus.getDefault().post(showEvent);
            }
        }
    }


    /**
     * 获取我的页面相关信息
     */
    void refreshMyDataFromServer() {
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            mSwipeLayout.setRefreshing(false);
            assetInfoNewResponse = new AssetInfoNewResponse();
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
                mSwipeLayout.setRefreshing(false);
                setUIData();
            }

            @Override
            public void onFailInfo(String errorInfo) {
                //showContentView();
                mSwipeLayout.setRefreshing(false);
                assetInfoNewResponse = new AssetInfoNewResponse();
                setUIData();
                ToastUtil.showInCenter(errorInfo);
            }
        });
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        //UserInfoRequest request = new UserInfoRequest();
        //request.setUserId((int) UserInfo.getInstance().getUserId());
        //ServiceSender.exec(this, request, new UserInfoResponseIwjwRespListener(this));
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

    @OnClick(R.id.purchase_view_click)
    void goPurchase() {
        MyIntent.startActivity(getWRActivity(), CapitalActivity.class, null);
    }

    //提现
    @OnClick(R.id.tv_account_balance_get_cash)
    void accountBalanceGetCashClick() {
        if (!NoSetSafeCardHint.isOpenAccount()) {
            Intent intent = new Intent(getWRActivity(), ProcessActivity.class);
            startActivity(intent);
        } else if (!NoSetSafeCardHint.isHasSafeCard((BaseBindActivity) getWRActivity())) {
            //设置安全卡弹窗
        } else {
            Intent intent1 = new Intent(getWRActivity(), AccountWithdrawActivity.class);
            startActivity(intent1);
        }

    }

    //充值
    @OnClick(R.id.tv_account_balance_charge)
    void accountBalanceChargeClick() {
        if (!NoSetSafeCardHint.isOpenAccount()) {
            Intent intent = new Intent(getWRActivity(), ProcessActivity.class);
            startActivity(intent);
        } else if (!NoSetSafeCardHint.isHasSafeCard((BaseBindActivity) getWRActivity())) {
            //设置安全卡弹窗
        } else {
            Intent intent1 = new Intent(getWRActivity(), AccountTopupActivity.class);
            startActivity(intent1);
        }

    }


    //资产预览
    @OnClick(R.id.totalAsset_click)
    void goAssetsTotal() {
        if (!NoSetSafeCardHint.isOpenAccount()) {
            Intent intent = new Intent(getWRActivity(), ProcessActivity.class);
            startActivity(intent);
        } else {
            MyIntent.startActivity(getWRActivity(), AssetInViewOfBirdActivity.class, null);
        }
    }

    //累计收益
    @OnClick(R.id.total_money)
    void goIncomeDetailByTotal() {
        if (!NoSetSafeCardHint.isOpenAccount()) {
            Intent intent = new Intent(getWRActivity(), ProcessActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getWRActivity(), IncomeDetailActivity.class);
            intent.putExtra(IncomeDetailActivity.TYPE, IncomeDetailActivity.REGULAR);
            startActivity(intent);
        }
    }

    //昨日收益
    @OnClick(R.id.yestoday_money)
    void goIncomeDetailByYestoday() {
        if (!NoSetSafeCardHint.isOpenAccount()) {
            Intent intent = new Intent(getWRActivity(), ProcessActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getWRActivity(), IncomeDetailActivity.class);
            intent.putExtra(IncomeDetailActivity.TYPE, IncomeDetailActivity.REGULAR);
            startActivity(intent);
        }
    }

    @OnClick(R.id.net_loan_layout)
    public void netLoanClick() {
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            loginAction = LoginManager.LoginAction.ACTION_INDEX_WANG_DAI_CLICK;
            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
        } else {
            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_WANG_DAI_CLICK);
        }
    }

    @OnClick(R.id.to_huo_qi_bao)
    public void goHuoQiBao() {
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            loginAction = LoginManager.LoginAction.ACTION_INDEX_HUOQIBAO_CLICK;
            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
        } else {
            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_HUOQIBAO_CLICK);
        }
    }


    /**
     * 卡券上小红点
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleRefreshPushEvent(RefreshPushEvent event) {
        if (event.getType() == PushMessage.REMINDTYPENEWVOUCHER || event.getType() == PushMessage.REMINDTYPETIYANJI) {
            setVoucherRedDotState(true);
        }
    }

    private void setVoucherRedDotState(boolean isShow) {
        if (isShow) {
            MineShowRedPointEvent showEvent = new MineShowRedPointEvent();
            showEvent.setShowRedPoint(true);
            EventBus.getDefault().post(showEvent);
            ticket_red_dot.setVisibility(View.VISIBLE);
        } else {
            ticket_red_dot.setVisibility(View.INVISIBLE);
            if (mTvNewMsgPoint.getVisibility() == View.GONE) {
                MineShowRedPointEvent showEvent = new MineShowRedPointEvent();
                showEvent.setShowRedPoint(false);
                EventBus.getDefault().post(showEvent);
            }
        }
//        saveVoucherRedDotStateByUser(isShow);
    }

//    private void saveVoucherRedDotStateByUser(boolean isShow) {
//        long userId = UserManager.getInstance(getWRActivity()).getUserInfoBase().getUserId();
//        MyPreference.getInstance().write(userId + "VoucherRedDotIsShow", isShow);
//    }
//
//    public boolean isShowVoucherRedDotState() {
//        long userId = UserManager.getInstance(getWRActivity()).getUserInfoBase().getUserId();
//        return MyPreference.getInstance().read(userId + "VoucherRedDotIsShow", false);
//    }

    /*
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
    */


}
