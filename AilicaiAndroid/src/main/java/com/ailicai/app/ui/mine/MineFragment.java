package com.ailicai.app.ui.mine;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.utils.StringUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.eventbus.ExitEvent;
import com.ailicai.app.eventbus.LoginEvent;
import com.ailicai.app.model.request.OrderListRequest;
import com.ailicai.app.model.request.UserInfoRequest;
import com.ailicai.app.model.response.AilicaiPartResponse;
import com.ailicai.app.model.response.HousePartResponse;
import com.ailicai.app.model.response.Order;
import com.ailicai.app.model.response.OrderListResponse;
import com.ailicai.app.model.response.UserInfoResponse;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.login.UserInfoBase;
import com.ailicai.app.ui.login.UserManager;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.SoftReference;

import butterknife.Bind;
import butterknife.OnClick;

public class MineFragment extends BaseBindFragment implements ObservableScrollViewCallbacks {

    /**
     * 点击头像区域跳转至个人信息编辑页面
     */
    public OnClickListener userLayoutOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.userPhoto:
                    //MyIntent.startActivity(getActivity(), UserInfoEditActivity.class, getDataMap());
                    break;
            }
        }
    };
    @Bind(R.id.title_root)
    RelativeLayout titleRoot;
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
    @Bind(R.id.tv_eyes_status)
    TextView tvEyesStatus;
    private MinePresenter mPresenter;
    private boolean eyeOpen = false;
    private AilicaiPartResponse ailicaiPartResponse;

    //  private MineBannerDialog mMineBannerDialog;
    private HousePartResponse housePartResponse;
    private LoginManager.LoginAction loginAction = LoginManager.LoginAction.ACTION_INDEX_NORMAL;
    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvLogin:
                    LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
                    //EventLog.upEventLog("200", "1");
                    ManyiAnalysis.getInstance().onEvent("mine_mine_login");
                    break;
                default:
                    break;
            }
        }
    };

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
            if (LoginManager.loginPageLocation.isLastLoginInThisPage(LoginManager.LoginLocation.MY) && event.getFromPageCode() == LoginManager.LOGIN_FROM_MINE) {
                //123 EventLog.upEventLog("2017050801", whereClick,"my");
            }
            refreshMyDataFromServer();
            // 未登录，点击订单登录，如果一个账单跳转账单列表 ①
            if (loginAction != LoginManager.LoginAction.ACTION_INDEX_ORDER) {
                jumpToMenuTarget(loginAction);
            }
        } else {
            loginAction = LoginManager.LoginAction.ACTION_INDEX_NORMAL;
            housePartResponse = null;
            ailicaiPartResponse = null;
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
        if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_ORDER.getActionIndex())) {
            //我的订单,这里把原来直接跳转到订单列表的Fragment改成跳转至Activity。
            //解决我的页面首次登陆显示引导页出错的问题。
            //startDefaultTransition(OrderListFragment.class, null);
            if (housePartResponse != null && housePartResponse.getOrderId() > 0) {
                gotoBillDetail();
            } else {
//                Intent intent = new Intent(getActivity(), H5OrderListActivity.class);
//                intent.putExtra(H5OrderListActivity.URL_TYPE, H5OrderListActivity.TYPE_ORDER);
//                startActivity(intent);
                // startActivity(new Intent(getActivity(), OrderListActivity.class));
            }
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_COMPLAIN.getActionIndex())) {
            //我的投诉
            mPresenter.gotoMyComplain(getActivity());
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_BANK_CARD.getActionIndex())) {
            //银行卡
            mPresenter.gotoMyBankCrad(getActivity());
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_PASSWORD.getActionIndex())) {
            //交易密码
            mPresenter.gotoPassWordManage(getActivity());
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_AGENT.getActionIndex())) {
            //我的经纪人
            mPresenter.gotoMyConsultantList(getActivity());
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_OWNER.getActionIndex())) {
            //委托的房源
            mPresenter.gotoEntrustManageList(getActivity());
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_CARD_COUPONST.getActionIndex())) {
            //卡券
            mPresenter.gotoCardCoupon(getActivity());
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_YUE_KAN_LIST.getActionIndex())) {
            //约看清单
            mPresenter.gotoCartList(getActivity());
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_KAN_FANG_LIST.getActionIndex())) {
            //看房日程
            mPresenter.gotoAgendaList(getActivity());
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_MY_REGULAR.getActionIndex())) {
            //我的房产宝
            mPresenter.gotoMyRegular(getActivity());
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_SEE_RECORD.getActionIndex())) {
            //我的品牌公寓约看记录
            mPresenter.gotoSeeRecord(getActivity());
        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_MY_ASSETS.getActionIndex())) {
            //我的资产
            mPresenter.gotoMyAssets(getActivity());
        }
        loginAction = LoginManager.LoginAction.ACTION_INDEX_NORMAL;
    }

    /**
     * 订单个数为1，直接跳转订单详情页
     */
    private void gotoBillDetail() {
        OrderListRequest request = new OrderListRequest();
        request.setOffSet(0);
        request.setPageSize(20);
        ServiceSender.exec(this, request, new OrderListResponseIwjwRespListener(this));
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

        //根据登录或未登录显示用户头像等相关信息
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            mineNotLogin.setVisibility(View.VISIBLE);
            mineLogin.setVisibility(View.GONE);
            //登录按钮点击
            tvLogin.setOnClickListener(mOnClickListener);
            userPhoto.setClickable(false);
            userPhoto.setOnClickListener(null);
            ticket_red_dot.setVisibility(View.INVISIBLE);
        } else if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
            mineNotLogin.setVisibility(View.GONE);
            mineLogin.setVisibility(View.VISIBLE);
            userPhoto.setClickable(true);
            userPhoto.setOnClickListener(userLayoutOnClickListener);
            //根据登录的手机号获取已保存的用户信息
            long userId = MyPreference.getInstance().read(UserInfo.USERINFO_KEY_USER_ID, new Long(0));
            UserInfoBase infoBase = UserManager.getInstance(MyApplication.getInstance()).getUserByUserId(userId);

            int gender = 0;
            int resID = R.drawable.avatar_default;
            if (infoBase != null) {
                gender = infoBase.getGender();
                String realname = TextUtils.isEmpty(infoBase.getRealName()) ? StringUtil.formatMobileSub(infoBase.getMobile()) : infoBase.getRealName();
                if (!"".equals(realname)) {
                    //userNameArrow.setVisibility(View.VISIBLE);
                } else {
                    //userNameArrow.setVisibility(View.GONE);
                }
            }
            //不男不女
            if (gender != 0) {
                resID = gender == 1 ? R.drawable.avatar_default : R.drawable.avatar_default;
            }
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
        }

        // 卡券小红点
        if (isShowVoucherRedDotState()) {
            ticket_red_dot.setVisibility(View.VISIBLE);
        } else {
            ticket_red_dot.setVisibility(View.INVISIBLE);
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
        } else {
            //显示密文
            tvEyesStatus.setText(R.string.eyes_closed);
        }

    }

    @OnClick(R.id.rewards)
    void goRewards() {
        MyIntent.startActivity(getWRActivity(), InviteRewardsActivity.class, null);
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
     * 获取我的页面相关信息
     */
    void refreshMyDataFromServer() {
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            housePartResponse = null;
            ailicaiPartResponse = null;
            setUIData();
            return;
        }
        getUserInfo();
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

    private void setVoucherRedDotState(boolean isShow) {
        if (isShow) {
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

    private static class OrderListResponseIwjwRespListener extends IwjwRespListener<OrderListResponse> {

        private SoftReference<MineFragment> personalCenterFragmentSoftReference;

        OrderListResponseIwjwRespListener(MineFragment personalCenterFragment) {
            this.personalCenterFragmentSoftReference = new SoftReference<>(personalCenterFragment);
        }

        @Override
        public void onStart() {
            if (null != personalCenterFragmentSoftReference.get()) {
                personalCenterFragmentSoftReference.get().showLoadTranstView();
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            if (null != personalCenterFragmentSoftReference.get()) {
                MineFragment personalCenterFragment = personalCenterFragmentSoftReference.get();
                personalCenterFragment.showContentView();
            }
        }

        @Override
        public void onJsonSuccess(OrderListResponse response) {
            if (null != personalCenterFragmentSoftReference.get()) {
                MineFragment personalCenterFragment = personalCenterFragmentSoftReference.get();
                if (response != null && response.getOrderList().size() > 0) {
                    Order order = response.getOrderList().get(0);
                    if (order != null) {
                        Intent intent;
                        switch (order.getType()) {
//                            case 1://普租
//                                intent = new Intent(personalCenterFragment.getActivity(), H5OrderListActivity.class);
//                                intent.putExtra(H5OrderListActivity.ORDER_ID, order.getOrderId());
//                                intent.putExtra(H5OrderListActivity.TYPE, order.getType());
//                                intent.putExtra(H5OrderListActivity.URL_TYPE, H5OrderListActivity.TYPE_RENT_ORDER_DETAIL);
//                                break;
//                            case 3://二手房
//                                intent = new Intent(personalCenterFragment.getActivity(), H5OrderListActivity.class);
//                                intent.putExtra(H5OrderListActivity.ORDER_ID, order.getOrderId());
//                                intent.putExtra(H5OrderListActivity.TYPE, order.getType());
//                                intent.putExtra(H5OrderListActivity.URL_TYPE, H5OrderListActivity.TYPE_DETAIL);
//                                break;
//                            case 2://极爱宅
//                            default:
//                                intent = new Intent(personalCenterFragment.getActivity(), OrderDetailActivity.class);
//                                intent.putExtra(OrderDetailActivity.ORDER_ID, order.getOrderId());
//                                intent.putExtra(OrderDetailActivity.TYPE, order.getType());
//                                break;
                        }
                        //personalCenterFragment.getActivity().startActivity(intent);
                    }
                }
            }
        }
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
