//package com.ailicai.app.ui.mine;
//
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.text.Html;
//import android.text.SpannableStringBuilder;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.ailicai.app.R;
//import com.ailicai.app.common.utils.CommonUtil;
//import com.ailicai.app.common.utils.MyIntent;
//import com.ailicai.app.eventbus.LoginEvent;
//import com.ailicai.app.ui.base.BaseBindFragment;
//import com.ailicai.app.ui.login.LoginManager;
//import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
//import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
//import com.huoqiu.framework.analysis.ManyiAnalysis;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.lang.ref.SoftReference;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//import butterknife.Bind;
//import butterknife.OnClick;
//import butterknife.OnLongClick;
//
//public class PersonalCenterFragment extends BaseBindFragment implements ObservableScrollViewCallbacks {
//
//    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
//    /**
//     * 点击头像区域跳转至个人信息编辑页面
//     */
//    public OnClickListener userLayoutOnClickListener = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.ivUserPhoto:
//                case R.id.rlUserInfo:
//                    //MyIntent.startActivity(getActivity(), UserInfoEditActivity.class, getDataMap());
//                    break;
//            }
//        }
//    };
//    @Bind(R.id.statusBarBg)
//    View statusBarBg;
//    @Bind(R.id.my_scroll_view)
//    ObservableScrollView mScrollView;
//    @Bind(R.id.complaint_state_text)
//    TextView mComplaintStateText;
//    //我的订单项自定义控件
//    @Bind(R.id.order_state_text)
//    TextView mMyOrderInfo;
//    @Bind(R.id.mine_service_tel)
//    TextView serviceTel;
//    //购房须知选项
//    @Bind(R.id.purchase_notice_container)
//    LinearLayout mPurchaseNotice;
//
//    //    @Bind(R.id.mine_bank_card_container)
////    LinearLayout mBankCardManage;
//    //交易密码
//    @Bind(R.id.mine_password_manage_container)
//    LinearLayout mPasswordManage;
//    @Bind(R.id.mine_finance_container)
//    LinearLayout myfinanceContainer;
//    @Bind(R.id.tvCardCouponsCount)
//    TextView tvCardCouponsCount;
//    @Bind(R.id.ticket_red_dot)
//    ImageView ticket_red_dot;
//    @Bind(R.id.tvOwnerCount)
//    TextView tvOwnerCount;
//    @Bind(R.id.tvWalletSum)
//    TextView tvWalletSum;
//    //可滚动区域容器
//    @Bind(R.id.personal_root_view)
//    LinearLayout mAllView;
//
//    //    @Bind(R.id.mine_top_bar)
////    FrameLayout mineTopBar;
////
////    @Bind(R.id.overlay_layout)
////    LinearLayout overlayLayout;
////
////    @Bind(R.id.personal_layout_login_info_name)
////    FrameLayout personalLayoutLoginInfoName;
////
////    @Bind(R.id.personal_layout_login_name)
////    FrameLayout personalLayoutLoginName;
////
////    @Bind(R.id.personal_layout_login_info_phone)
////    FrameLayout personalLayoutLoginInfoPhone;
////
////    @Bind(R.id.personal_layout_login_phone)
////    FrameLayout personalLayoutLoginPhone;
////
////    @Bind(R.id.personal_layout_no_login_layout)
////    FrameLayout personalNoLoginLayout;
////
////    @Bind(R.id.personal_layout_no_login)
////    FrameLayout personalLayoutNoLogin;
////
////    @Bind(R.id.mine_login_btn_alpha)
////    TextView mineLoginBtnAlpha;
////
////    @Bind(R.id.rlHead)
////    FrameLayout mineAgentPhotoLayout;
//    @Bind(R.id.tvRedPoint)
//    TextView tvRedPoint;
//    @Bind(R.id.llNotLogin)
//    LinearLayout llNotLogin;
//    @Bind(R.id.tvLogin)
//    TextView tvLogin;
//    @Bind(R.id.rlUserInfo)
//    RelativeLayout rlUserInfo;
//    @Bind(R.id.ivUserPhoto)
//    ImageView userPhoto;
//    @Bind(R.id.tvUserName)
//    TextView userName;
//    @Bind(R.id.ivAuthIcon)
//    ImageView ivAuthIcon;
//    @Bind(R.id.user_name_arrow)
//    TextView userNameArrow;
//    @Bind(R.id.tvBankCardCount)
//    TextView tvBankCardCount;
//    @Bind(R.id.tv_kan_fang_num)
//    TextView tvKanFangNum;
//    @Bind(R.id.tv_yue_kan_count)
//    TextView tvYueKanCount;
//    @Bind(R.id.tv_total_balance)
//    TextView tvTotalBalance;
//    @Bind(R.id.tv_eyes_status)
//    TextView tvEyesStatus;
//    @Bind(R.id.tv_wallet_balance)
//    TextView tvWalletBalance;
//    @Bind(R.id.tv_deposit_balance)
//    TextView tvDepositBalance;
//    @Bind(R.id.rl_close_wallet)
//    RelativeLayout rlCloseWallet;
//    @Bind(R.id.rl_new_balance)
//    RelativeLayout rlNewBalance;
//    private AtomicBoolean mIsFirstLoad;
//    //MVP Presenter对象
//    private MinePresenter mPresenter;
//    //登录成功后头像来个动画效果，其他情况不执行动画
//    private boolean isLoginBack = false;
//    private float maxOffset;
//    private boolean eyeOpen = false;
//    private AilicaiPartResponse ailicaiPartResponse;
//
//    //登录成功 埋点需求 -----start
//    private static final String CLICK_LOCATION_CLICK_PAGE = "mine";
//    private static final String CLICK_LOCATION_CLICK_IMMEDIATE = "a";
//
//    private static final String CLICK_LOCATION_CLICK_WALLET = "b";
//    private static final String CLICK_LOCATION_CLICK_CARD_TICKET = "c";
//    private static final String CLICK_LOCATION_CLICK_BANK_CARD = "d";
//
//    private static final String CLICK_LOCATION_CLICK_YUE_KAN = "f";
//    private static final String CLICK_LOCATION_CLICK_KAN_FANG_SHEDULE= "g";
//    private static final String CLICK_LOCATION_CLICK_AGENT = "h";
//    private static final String CLICK_LOCATION_CLICK_CONTRACT_ORDER = "i";
//
//    private static final String CLICK_LOCATION_CLICK_MY_ENTRUST = "j";
//    private static final String CLICK_LOCATION_CLICK_RESERVE_BRAND_APARTMENT = "k";
//    private static final String CLICK_LOCATION_CLICK_MYCOMPLAINT = "l";
//    private String whereClick = "";
//    //登录成功 埋点需求 -----end
//
//    //  private MineBannerDialog mMineBannerDialog;
//    private HousePartResponse housePartResponse;
//    private LoginManager.LoginAction loginAction = LoginManager.LoginAction.ACTION_INDEX_NORMAL;
//    private OnClickListener mOnClickListener = new OnClickListener() {
//
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.tvLogin:
//                    whereClick = CLICK_LOCATION_CLICK_IMMEDIATE;
//                    LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
//                    //EventLog.upEventLog("200", "1");
//                    ManyiAnalysis.getInstance().onEvent("mine_mine_login");
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
//
//    @Override
//    public int getLayout() {
//        return R.layout.personal_layout;
//    }
//
//    @Override
//    public void init(Bundle savedInstanceState) {
//        super.init(savedInstanceState);
//        if (mPresenter == null) {
//            mPresenter = new MinePresenter();
//        }
//        EventBus.getDefault().register(this);
//        mIsFirstLoad = new AtomicBoolean();
//        mIsFirstLoad.set(true);
//
//        //添加顶部用户信息区域布局文件(动画效果)
//        CommonUtil.addAnimForView(mAllView);
//        mScrollView.setScrollViewCallbacks(this);
//        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
//            @Override
//            public void run() {
//                //mScrollView.scrollTo(0, mFlexibleSpaceImageHeight - mActionBarSize);
//                // If you'd like to start from scrollY == 0, don't write like this:
//                //mScrollView.scrollTo(0, 0);
//                // The initial scrollY is 0, so it won't invoke onScrollChanged().
//                // To do this, use the following:
//                //onScrollChanged(0, false, false);
//            }
//        });
//        eyeOpen = MyPreference.getInstance().read("eyeOpen", false);
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        CommonUtil.uiSystemBarTintNoTitle(getActivity(), view.findViewById(R.id.rlHead));
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        refreshMyDataFromServer();
//    }
//
//    @Override
//    public void onDestroy() {
//        EventBus.getDefault().unregister(this);
//        MyPreference.getInstance().write("eyeOpen", eyeOpen);
//        super.onDestroy();
//    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            refreshMyDataFromServer();
//        }
//    }
//
//    /**
//     * 接收登陆成功或者退出登录之后的事件
//     *
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void handleLoginEvent(LoginEvent event) {
//        if (event.isLoginSuccess()) {
//            if(LoginManager.loginPageLocation.isLastLoginInThisPage(LoginManager.LoginLocation.MY) && event.getFromPageCode() == LoginManager.LOGIN_FROM_MINE ){
//                //123 EventLog.upEventLog("2017050801", whereClick,"my");
//            }
//            isLoginBack = true;
//            refreshMyDataFromServer();
//            // 未登录，点击订单登录，如果一个账单跳转账单列表 ①
//            if (loginAction != LoginManager.LoginAction.ACTION_INDEX_ORDER) {
//                jumpToMenuTarget(loginAction);
//            }
//        } else {
//            loginAction = LoginManager.LoginAction.ACTION_INDEX_NORMAL;
//            isLoginBack = false;
//            housePartResponse = null;
//            ailicaiPartResponse = null;
//            setUIData();
//        }
//        mScrollView.smoothScrollTo(0, 0);
//    }
//
//    /*
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void handleEntrustSwitchEvent(EntrustSwitchEvent event) {
//        //点击委托完成后，跳转到我的-委托管理页
//        mPresenter.gotoEntrustManageList(getActivity(), event.getSubmitHouseInfo());
//    }
//    */
//
//    /**
//     * 登陆后跳转至目标页面
//     *
//     * @param action
//     */
//    public void jumpToMenuTarget(LoginManager.LoginAction action) {
//        if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_ORDER.getActionIndex())) {
//            //我的订单,这里把原来直接跳转到订单列表的Fragment改成跳转至Activity。
//            //解决我的页面首次登陆显示引导页出错的问题。
//            //startDefaultTransition(OrderListFragment.class, null);
//            if (housePartResponse != null && housePartResponse.getOrderId() > 0) {
//                gotoBillDetail();
//            } else {
//                Intent intent = new Intent(getActivity(), H5OrderListActivity.class);
//                intent.putExtra(H5OrderListActivity.URL_TYPE, H5OrderListActivity.TYPE_ORDER);
//                startActivity(intent);
//                // startActivity(new Intent(getActivity(), OrderListActivity.class));
//            }
//        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_COMPLAIN.getActionIndex())) {
//            //我的投诉
//            mPresenter.gotoMyComplain(getActivity());
//        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_BANK_CARD.getActionIndex())) {
//            //银行卡
//            mPresenter.gotoMyBankCrad(getActivity());
//        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_PASSWORD.getActionIndex())) {
//            //交易密码
//            mPresenter.gotoPassWordManage(getActivity());
//        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_AGENT.getActionIndex())) {
//            //我的经纪人
//            mPresenter.gotoMyConsultantList(getActivity());
//        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_OWNER.getActionIndex())) {
//            //委托的房源
//            mPresenter.gotoEntrustManageList(getActivity());
//        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_CARD_COUPONST.getActionIndex())) {
//            //卡券
//            mPresenter.gotoCardCoupon(getActivity());
//        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_YUE_KAN_LIST.getActionIndex())) {
//            //约看清单
//            mPresenter.gotoCartList(getActivity());
//        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_KAN_FANG_LIST.getActionIndex())) {
//            //看房日程
//            mPresenter.gotoAgendaList(getActivity());
//        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_MY_REGULAR.getActionIndex())) {
//            //我的房产宝
//            mPresenter.gotoMyRegular(getActivity());
//        } else if(action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_SEE_RECORD.getActionIndex())) {
//            //我的品牌公寓约看记录
//            mPresenter.gotoSeeRecord(getActivity());
//        } else if (action.isActionIndex(LoginManager.LoginAction.ACTION_INDEX_MY_ASSETS.getActionIndex())) {
//            //我的资产
//            mPresenter.gotoMyAssets(getActivity());
//        }
//        loginAction = LoginManager.LoginAction.ACTION_INDEX_NORMAL;
//    }
//
//    /**
//     * 订单个数为1，直接跳转订单详情页
//     */
//    private void gotoBillDetail() {
//        OrderListRequest request = new OrderListRequest();
//        request.setOffSet(0);
//        request.setPageSize(20);
//        ServiceSender.exec(this, request, new OrderListResponseIwjwRespListener(this));
//    }
//
//    /**
//     * 修改用户信息后调用
//     *
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void handleEditUserInfoEvent(EditUserInfoEvent event) {
//        long userId = UserInfo.getInstance().getUserId();
//        UserInfoBase infoBase = UserManager.getInstance(MyApplication.getInstance()).getUserByUserId(userId);
//        infoBase.setUserId(event.getUserId());
//        infoBase.setRealName(event.getRealName());
//        infoBase.setGender(event.getGender());
//        infoBase.setMobile(event.getMobile());
//        UserManager.getInstance(MyApplication.getInstance()).saveUser(infoBase);
//        setUIData();
//    }
//
//    @Override
//    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
//        int mFlexibleSpaceImageHeight = DeviceUtil.getPixelFromDip(getActivity(), 130);
//        int mStatusBarHeight = DeviceUtil.getPixelFromDip(getActivity(), 24);
//        int scrollViewMeasuredHeight = mScrollView.getChildAt(0).getMeasuredHeight();
//        int height = mScrollView.getHeight();
//        if ((scrollViewMeasuredHeight - height) >= mFlexibleSpaceImageHeight) {
//            onScrollDisplay(scrollY);
//            if (scrollY >= (mFlexibleSpaceImageHeight - mStatusBarHeight)) {
//                statusBarBg.setVisibility(View.VISIBLE);
//            } else {
//                statusBarBg.setVisibility(View.GONE);
//            }
//        } else {
//            statusBarBg.setVisibility(View.GONE);
//            //顶部不滚动
//        }
//
//    }
//
//    public void onScrollDisplay(int scrollY) {
//        //各个元素Y坐标微调值
////        int mFlexibleSpaceImageHeight = DeviceUtil.getPixelFromDip(getActivity(), 130);
////        int mActionBarSize = mineTopBar.getHeight() + DeviceUtil.getPixelFromDip(getActivity(), 2);
////        int loginBtnOffsetY = DeviceUtil.getPixelFromDip(getActivity(), 22);
////        int userPhotoOffsetY = DeviceUtil.getPixelFromDip(getActivity(), 31);
////        int userNameOffsetY = DeviceUtil.getPixelFromDip(getActivity(), 35);
////        int userPhoneOffsetY = DeviceUtil.getPixelFromDip(getActivity(), 8);
////
////        int minOverlayTransitionY = mActionBarSize - mFlexibleSpaceImageHeight;
////        int minPhotoTransitionY = mActionBarSize + userPhotoOffsetY - mFlexibleSpaceImageHeight;
////        int minLoginBtnTransitionY = mActionBarSize + loginBtnOffsetY - mFlexibleSpaceImageHeight;
////        int minNameTransitionY = mActionBarSize + userNameOffsetY - mFlexibleSpaceImageHeight;
////        int minPhoneTransitionY = mActionBarSize + userPhoneOffsetY - mFlexibleSpaceImageHeight;
////
////        overlayLayout.setTranslationY(ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));
////        mineAgentPhotoLayout.setTranslationY(ScrollUtils.getFloat(-scrollY / 2, minPhotoTransitionY, 0));
////        personalNoLoginLayout.setTranslationY(ScrollUtils.getFloat(-scrollY / 2, minLoginBtnTransitionY, 0));
////        personalLayoutLoginInfoName.setTranslationY(ScrollUtils.getFloat(-scrollY / 2, minNameTransitionY, 0));
////        personalLayoutLoginInfoPhone.setTranslationY(ScrollUtils.getFloat(-scrollY / 2, minPhoneTransitionY, 0));
////
////        //头像缩小至0.7f
////        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
////        float scale = 0.7f + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
////        userPhoto.setPivotX(0);
////        userPhoto.setPivotY(0);
////        userPhoto.setScaleX(scale);
////        userPhoto.setScaleY(scale);
////
////        //各个元素X坐标微调值
////        int loginInfoOffsetX = (int) (userPhoto.getWidth() * scale) + DeviceUtil.getPixelFromDip(getActivity(), 2);
////        int loginBtnOffsetX = (int) (userPhoto.getWidth() * scale) + DeviceUtil.getPixelFromDip(getActivity(), 2);
////        int userPhotoOffsetX = DeviceUtil.getPixelFromDip(getActivity(), 13);
////
////        mineAgentPhotoLayout.setTranslationX(ScrollUtils.getFloat(-scrollY / 2, -userPhotoOffsetX, 0));
////        personalNoLoginLayout.setTranslationX(ScrollUtils.getFloat(-scrollY / 2, -loginBtnOffsetX, 0));
////        personalLayoutLoginInfoName.setTranslationX(ScrollUtils.getFloat(-scrollY / 2, -loginInfoOffsetX, 0));
////        personalLayoutLoginInfoPhone.setTranslationX(ScrollUtils.getFloat(-scrollY / 2, -loginInfoOffsetX, 0));
////
////        //渐变相关元素
////        if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
////            // Change alpha of overlay
////            userPhone.setAlpha(1 - ScrollUtils.getFloat((float) scrollY / 2 / flexibleRange, 0, 1));
////        } else {
////            mineLoginBtnAlpha.setAlpha(1 - ScrollUtils.getFloat((float) scrollY / 2 / flexibleRange, 0, 1));
////        }
////
////        //用户名放大缩小
////        float offset = ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0);
////        maxOffset = -minOverlayTransitionY;
////        float expandedPercentage = 1 - (-offset / maxOffset);
////        float userNameCollapsedTextSize = getResources().getDimensionPixelSize(R.dimen.default_username_collapsed_text_size);
////        float userNameExpandedTextSize = getResources().getDimensionPixelSize(R.dimen.default_username_expanded_text_size);
////        float arrowCollapsedTextSize = getResources().getDimensionPixelSize(R.dimen.default_arrow_collapsed_text_size);
////        float arrowExpandedTextSize = getResources().getDimensionPixelSize(R.dimen.default_arrow_expanded_text_size);
////        float phoneCollapsedTextSize = getResources().getDimensionPixelSize(R.dimen.default_phone_collapsed_text_size);
////        float phoneExpandedTextSize = getResources().getDimensionPixelSize(R.dimen.default_phone_expanded_text_size);
////
////        float userNameTextSize = userNameCollapsedTextSize + (userNameExpandedTextSize - userNameCollapsedTextSize) * expandedPercentage;
////        float arrowTextSize = arrowCollapsedTextSize + (arrowExpandedTextSize - arrowCollapsedTextSize) * expandedPercentage;
////        float phoneTextSize = phoneCollapsedTextSize + (phoneExpandedTextSize - phoneCollapsedTextSize) * expandedPercentage;
////        userName.setTextSize(TypedValue.COMPLEX_UNIT_PX, userNameTextSize);
////        userNameArrow.setTextSize(TypedValue.COMPLEX_UNIT_PX, arrowTextSize);
////        userPhone.setTextSize(TypedValue.COMPLEX_UNIT_PX, phoneTextSize);
//    }
//
//    @Override
//    public void onDownMotionEvent() {
//
//    }
//
// /*   *//**
//     * 退出对话框
//     *//*
//    private void showMineBannerDialog() {
//        dismissMineBannerDialog();
//        if (mFragmentHelper != null) {
//            mMineBannerDialog = new MineBannerDialog();
//            mFragmentHelper.showDialog(null, mMineBannerDialog);
//        }
//    }*/
//
//  /*  */
//
//    /**
//     * dismiss 退出对话框
//     *//*
//    private void dismissMineBannerDialog() {
//        if (mMineBannerDialog != null) {
//            mMineBannerDialog.dismiss();
//            mMineBannerDialog = null;
//        }
//    }*/
//    @Override
//    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
//
//    }
//
//    /**
//     * 设置我的投诉
//     */
//    private void setMyComplaint() {
//        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
//            mComplaintStateText.setVisibility(View.GONE);
//        } else if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
//            if (housePartResponse != null) {
//                mComplaintStateText.setVisibility(View.VISIBLE);
//                int mComplaintNum = housePartResponse.getComplaintNum();
//                if (mComplaintNum > 0) {
//                    mComplaintStateText.setText("(" + mComplaintNum + ")");
//                } else {
//                    mComplaintStateText.setVisibility(View.GONE);
//                }
//            } else {
//                mComplaintStateText.setVisibility(View.GONE);
//            }
//        }
//    }
//
//    /**
//     * 设置我的银行卡张数
//     */
//    private void setMyBankCardNum() {
//        tvBankCardCount.setText("0张");
//        if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
//            if (housePartResponse != null) {
//                int mComplaintNum = housePartResponse.getBackCardNum();
//                if (mComplaintNum > 0) {
//                    tvBankCardCount.setText(String.valueOf(mComplaintNum) + "张");
//                }
//            }
//        }
//    }
//
//    /**
//     * 设置交易密码
//     * 1. 交易密码，只有登陆用户且设置过交易密码，才会在银行卡下方展示出来
//     * 2. 银行卡，所有用户可见，功能访问需登陆
//     *
//     * @param rentOrSale
//     */
//    private void setPassWordManage(boolean rentOrSale) {
//        if (rentOrSale) {
//            myfinanceContainer.setVisibility(View.GONE);
//            if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
//                //未登录不显示设置密码选项
//                mPasswordManage.setVisibility(View.GONE);
//            } else if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
//                //boolean isAilicaiAllowUser = UserManager.getInstance(MyApplication.getInstance()).isAilicaiAllowUser();
//                //if (isAilicaiAllowUser) {
//                // 判断是否设置支付密码 0:否，1:是
//                boolean isSetPayPwd = UserManager.getInstance(MyApplication.getInstance()).isSetPayPwd();
//                if (isSetPayPwd) {
//                    mPasswordManage.setVisibility(View.VISIBLE);
//                } else {
//                    mPasswordManage.setVisibility(View.GONE);
//                }
//                //} else {
//                //不是白名单用户不显示设置密码选项
//                //mPasswordManage.setVisibility(View.GONE);
//                //}
//            }
//        } else {
//            myfinanceContainer.setVisibility(View.GONE);
//        }
//    }
//
//    /**
//     * 卡券张数
//     */
//    private void setCardCouponsNum() {
//        tvCardCouponsCount.setText("0张");
//        if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
//            if (housePartResponse != null) {
//                int mCardCouponsNum = housePartResponse.getVoucherNum();
//                if (mCardCouponsNum > 0) {
//                    tvCardCouponsCount.setText(String.valueOf(mCardCouponsNum)+"张");
//                }
//            }
//        }
//    }
//
//    /**
//     * 我委托的房源数
//     */
//    private void setOwnerNum() {
//        CityDBItem cityDBItem = CityManager.getInstance().getCurrentCity();
//        tvOwnerCount.setText(cityDBItem.getCommissionTips());
//        if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
//            if (housePartResponse != null) {
//                tvOwnerCount.setText(housePartResponse.getCommissionTips());
//            }
//        }
//    }
//
//    /**
//     * 钱包余额(爱理财被关闭)
//     */
//    private void setCloseWalletData() {
//        String walletSum = "0.00";
//        if (ailicaiPartResponse != null) {
//            walletSum = ailicaiPartResponse.getCurrentDepositBalance();
//        }
//        SpannableUtil mSpannableUtil = new SpannableUtil(getActivity());
//        SpannableStringBuilder sb = mSpannableUtil.getSpannableString(walletSum, "元", R.style
//                .text_22_212121, R.style.text_14_212121);
//        tvWalletSum.setText(sb);
//    }
//
//    private void setWalletData() {
//        tvTotalBalance.setTypeface(MyApplication.getInstance().getDinMediumFont());
//        if (ailicaiPartResponse != null) {
//            tvTotalBalance.setText(ailicaiPartResponse.getTotalBalance());
//            tvWalletBalance.setText(ailicaiPartResponse.getCurrentDepositBalanceDesc());
//            tvDepositBalance.setText(ailicaiPartResponse.getTimeDepositBalanceDesc());
//        } else {
//            tvTotalBalance.setText("0.00");
//            tvWalletBalance.setText("0.00元");
//            tvDepositBalance.setText("预计年化超7%-9%");
//        }
//    }
//
//    /**
//     * 只有上海二手房业务模式下才显示“购房须知”菜单项
//     *
//     * @param rentOrSale
//     */
//    private void setPurchaseNoticeContainer(boolean rentOrSale) {
//        //从当前城市获取购房须知相关字段
//        CityDBItem cityDBItem = CityManager.getInstance().getCurrentCity();
//        if (cityDBItem != null) {
//            //是否有购房须知 0 无 1 购房须知  2 购房百科
//            int hasBuyNotice = cityDBItem.getHasBuyNotice();
//            if (rentOrSale) {
//                if (hasBuyNotice == 0) {
//                    mPurchaseNotice.setVisibility(View.GONE);
////                    mEncyclopedias.setVisibility(View.GONE);
//                } else if (hasBuyNotice == 1) {
//                    mPurchaseNotice.setVisibility(View.VISIBLE);
////                    mEncyclopedias.setVisibility(View.GONE);
//                } else if (hasBuyNotice == 2) {
//                    mPurchaseNotice.setVisibility(View.GONE);
////                    mEncyclopedias.setVisibility(View.GONE);
//                }
//            } else {//业主委托或租房模式不显示购房须知
//                mPurchaseNotice.setVisibility(View.GONE);
//                if (hasBuyNotice == 2) {
////                    mEncyclopedias.setVisibility(View.GONE);
//                } else {
////                    mEncyclopedias.setVisibility(View.GONE);
//                }
//            }
//        } else {
//            mPurchaseNotice.setVisibility(View.GONE);
////            mEncyclopedias.setVisibility(View.GONE);
//        }
//
//    }
//
//    /**
//     * 租房或二手房模式下显示看房顾问显示逻辑
//     *
//     * @param rentOrSale
//     */
//    private void setConsultant(boolean rentOrSale) {
////        if (rentOrSale) {
////            //未登录不显示看房顾问选项
////            if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
////                mConsultant.setVisibility(View.GONE);
////            } else if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
////                if (housePartResponse != null) {
////                    //是否有经纪人 0:无  1：有
////                    int hasAgent = housePartResponse.getHasAgent();
////                    if (hasAgent == 0) {
////                        mConsultant.setVisibility(View.GONE);
////                    } else if (hasAgent == 1) {
////                        mConsultant.setVisibility(View.VISIBLE);
////                    }
////                } else {
////                    mConsultant.setVisibility(View.GONE);
////                }
////            }
////        } else {//业主委托模式不显示看房顾问
////            mConsultant.setVisibility(View.GONE);
////        }
//    }
//
//    public void setMyOrderAction() {
//        if (housePartResponse != null) {
//            int newOrderCnt = housePartResponse.getNewOrderCnt();
//            if (newOrderCnt > 0) {
//                mMyOrderInfo.setVisibility(View.VISIBLE);
//            } else {
//                mMyOrderInfo.setVisibility(View.GONE);
//            }
//        } else {
//            mMyOrderInfo.setVisibility(View.GONE);
//        }
//
//    }
//
//    /**
//     * 设置房源部分数据
//     */
//    public void setHousePartData() {
//        setConsultant(true);//我的看房顾问
//        setPurchaseNoticeContainer(true);//购房百科、购房须知
//        setMyOrderAction();//我的订单
//        setMyComplaint();//我的投诉
//        setPassWordManage(true);//交易密码
//        setMyBankCardNum();//银行卡设置
//        setCardCouponsNum();//卡券设置
//        setOwnerNum();//我委托的房源设置
//        setYueKanData();//设置约看数量
//        setKanFangData();//设置看房日程
//        setAuthIcon();//设置业主认证图标
//    }
//
//    /**
//     * 设置爱理财部分数据
//     */
//    public void setAilicaiPartData() {
//        if ("0".equals(SupportUrl.getAlicaiType())) {
//            //爱理财入口被关闭
//            rlCloseWallet.setVisibility(View.VISIBLE);
//            rlNewBalance.setVisibility(View.GONE);
//            setCloseWalletData();
//        } else {
//            rlCloseWallet.setVisibility(View.GONE);
//            rlNewBalance.setVisibility(View.VISIBLE);
//            handlerEyesStatus();
//        }
//
//    }
//
//    /**
//     * 设置看房日程红点
//     */
//    private void setKanFangData() {
//        if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
//            //设置看房日程
//            if (housePartResponse != null) {
//                if (housePartResponse.getAppointNum() > 0) {
//                    tvKanFangNum.setVisibility(View.VISIBLE);
//                } else {
//                    tvKanFangNum.setVisibility(View.GONE);
//                }
//            } else {
//                tvKanFangNum.setVisibility(View.GONE);
//            }
//        } else {
//            tvKanFangNum.setVisibility(View.GONE);
//        }
//    }
//
//    /**
//     * 设置约看数据
//     */
//    private void setYueKanData() {
//        if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
//            if (housePartResponse != null) {
//                if (housePartResponse.getSeekhouseNum() > 0) {
//                    tvYueKanCount.setText(housePartResponse.getSeekhouseNumStr());
//                    tvYueKanCount.setBackgroundResource(housePartResponse.getSeekhouseNumBg());
//                    tvYueKanCount.setVisibility(View.VISIBLE);
//                } else {
//                    tvYueKanCount.setVisibility(View.GONE);
//                }
//            } else {
//                tvYueKanCount.setVisibility(View.GONE);
//            }
//        } else {
//            tvYueKanCount.setVisibility(View.GONE);
//        }
//    }
//
//    /**
//     * 设置约看数据
//     */
//    private void setAuthIcon() {
//        if (housePartResponse != null) {
//            if (housePartResponse.getHasCommission() > 0) {
//                ivAuthIcon.setVisibility(View.VISIBLE);
//            } else {
//                ivAuthIcon.setVisibility(View.GONE);
//            }
//        }
//    }
//
//    public void setCompoundDrawable(TextView msgText, Drawable drawable, int width, int height) {
//        if (drawable != null) {
//            drawable.setBounds(0, 0, width == 0 ? drawable.getIntrinsicWidth() : width, height == 0 ? drawable.getIntrinsicHeight() : height);
//        }
//        msgText.setCompoundDrawables(null, null, drawable, null);
//    }
//
//    /**
//     * 获取版本号
//     *
//     * @return 当前应用的版本号
//     */
//    public String getVersion() {
//        String version = "";
//        try {
//            PackageManager manager = MyApplication.getInstance().getPackageManager();
//            PackageInfo info = manager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
//            version = info.versionName;
//            return version;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return version;
//    }
//
//    /**
//     * 有新版本更新时设置按钮上面显示红点引导提示
//     */
//    public void refreshUpdateTab() {
//        if (tvRedPoint == null) return;
//        if (AppInfo.getInstance().isHaveUpdate()) {
//            tvRedPoint.setVisibility(View.VISIBLE);
//        } else {
//            tvRedPoint.setVisibility(View.GONE);
//        }
//    }
//
//    public void setUIData() {
//        if (!PersonalCenterFragment.this.isVisible() || !PersonalCenterFragment.this.isAdded()) {
//            return;
//        }
//        setHousePartData();
//        setAilicaiPartData();
//        refreshUpdateTab();
//        //客服电话设置
//        String serverNumber = UserInfo.getInstance().getServicePhoneNumber();
//        serviceTel.setText(Html.fromHtml(getActivity().getResources().getString(R.string.mine_service_tell_txt, serverNumber)));
//
//        //根据登录或未登录显示用户头像等相关信息
//        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
//            llNotLogin.setVisibility(View.VISIBLE);
//            //登录按钮点击
//            tvLogin.setOnClickListener(mOnClickListener);
//            userPhoto.setClickable(false);
//            userPhoto.setOnClickListener(null);
//            rlUserInfo.setVisibility(View.GONE);
//            rlUserInfo.setClickable(false);
//            ticket_red_dot.setVisibility(View.INVISIBLE);
//            rlUserInfo.setOnClickListener(null);
//        } else if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
//            llNotLogin.setVisibility(View.GONE);
//            userPhoto.setClickable(true);
//            userPhoto.setOnClickListener(userLayoutOnClickListener);
//            rlUserInfo.setVisibility(View.VISIBLE);
//            rlUserInfo.setClickable(true);
//            rlUserInfo.setOnClickListener(userLayoutOnClickListener);
//            //根据登录的手机号获取已保存的用户信息
//            long userId = MyPreference.getInstance().read(UserInfo.USERINFO_KEY_USER_ID, new Long(0));
//            UserInfoBase infoBase = UserManager.getInstance(MyApplication.getInstance()).getUserByUserId(userId);
//
//            int gender = 0;
//            int resID = R.drawable.avatar_default;
//            if (infoBase != null) {
//                gender = infoBase.getGender();
//                String call = "";
//                /*
//                if (gender == 1) {//男士
//                    call = MyApplication.getInstance().getString(R.string.personal_gender_boy_call);
//                } else if (gender == 2) {//女士
//                    call = MyApplication.getInstance().getString(R.string.personal_gender_girl_call);
//                }
//                 */
//                String realname = TextUtils.isEmpty(infoBase.getRealName()) ? StringUtil.formatMobileSub(infoBase.getMobile()) : infoBase.getRealName();
//                userName.setText(realname);
//                if (!"".equals(realname)) {
//                    userNameArrow.setVisibility(View.VISIBLE);
//                } else {
//                    userNameArrow.setVisibility(View.GONE);
//                }
//
//
//            }
//            //不男不女
//            if (gender != 0) {
//                resID = gender == 1 ? R.drawable.avatar_default : R.drawable.avatar_default;
//            }
//            DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
//            builder.cacheInMemory(true)// 是否缓存都內存中
//                    .cacheOnDisk(true)// 是否缓存到sd卡上
//                    .considerExifParams(true)
//                    .imageScaleType(ImageScaleType.EXACTLY)
//                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .showImageForEmptyUri(resID)
//                    .showImageOnFail(resID)
//                    .showImageOnLoading(resID)
//                    .displayer(new SimpleBitmapDisplayer());
//        }
//
//        // 卡券小红点
//        if(isShowVoucherRedDotState()) {
//            ticket_red_dot.setVisibility(View.VISIBLE);
//        } else {
//            ticket_red_dot.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    /**
//     * 获取页面传参的数据Map
//     *
//     * @return
//     */
//    public Map<String, Object> getDataMap() {
//        if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
//            long userId = MyPreference.getInstance().read(UserInfo.USERINFO_KEY_USER_ID, new Long(0));
//            UserInfoBase infoBase = UserManager.getInstance(MyApplication.getInstance()).getUserByUserId(userId);
//            Map<String, Object> dataMap = ObjectUtil.newHashMap();
//            dataMap.put(CommonTag.PERSONAL_USER_ID, infoBase.getUserId());
//            dataMap.put(CommonTag.PERSONAL_USER_NAME, infoBase.getRealName());
//            dataMap.put(CommonTag.PERSONAL_USER_R_NAME, infoBase.getrName());
//            dataMap.put(CommonTag.PERSONAL_USER_SEX, infoBase.getGender());
//            dataMap.put(CommonTag.PERSONAL_USER_PHONE, infoBase.getMobile());
//            dataMap.put(CommonTag.PERSONAL_USER_ISREALNAMEVERIFY, infoBase.getIsRealNameVerify());
//            dataMap.put(CommonTag.PERSONAL_USER_IDCARDNUMBER, infoBase.getIdCardNo());
//            return dataMap;
//        }
//        return ObjectUtil.newHashMap();
//    }
//
//    @OnClick(R.id.tv_eyes_status)
//    void clickEyesStatus() {
//        eyeOpen = !eyeOpen;
//        handlerEyesStatus();
//    }
//
//    private void handlerEyesStatus() {
//        if (!eyeOpen) {
//            //显示明文
//            setWalletData();
//            tvEyesStatus.setText(R.string.eyes_opening);
//        } else {
//            //显示密文
//            tvEyesStatus.setText(R.string.eyes_closed);
//            tvTotalBalance.setText("****");
//            tvWalletBalance.setText("****");
//            tvDepositBalance.setText("****");
//        }
//
//    }
//
//    /**
//     * 我的订单点击
//     */
//    @OnClick(R.id.mine_my_order)
//    void onClickMyOrder() {
//        whereClick = CLICK_LOCATION_CLICK_CONTRACT_ORDER;
//        EventLog.upEventLog("220", "myorder");
//        ManyiAnalysis.getInstance().onEvent(EventStr.MINE_MINE_ORDER);
//        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
//            loginAction = LoginManager.LoginAction.ACTION_INDEX_ORDER;
//            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
//        } else {
//            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_ORDER);
//        }
//    }
//
//    /**
//     * 浏览历史
//     */
//    @OnClick(R.id.house_brower)
//    void onClickBrowerHis() {
//        Intent intent = new Intent(getActivity(), HouseBrowerHistoryActivity.class);
//        startActivity(intent);
//    }
//
//    /**
//     * 点击我的钱包(爱理财关闭时显示)
//     */
//    @OnClick(R.id.rl_close_wallet)
//    void onClickDemandTreasure() {
//        if (null != getActivity()) {
//            MyWalletActivity.goMywallet(getActivity());
//        }
//        EventLog.upEventLog("220", "my_wallet");
//    }
//
//    /**
//     * 点击我的钱包(爱理财开启时显示)
//     */
//    @OnClick(R.id.rl_wallet)
//    void onClickWallet() {
//        whereClick = CLICK_LOCATION_CLICK_WALLET;
//        if (null != getActivity()) {
//            MyWalletActivity.goMywalletFromMyFragment(getActivity());
//        }
//        EventLog.upEventLog("220", "my_wallet");
//    }
//
//    /**
//     * 点击爱理财(since 5.6)
//     */
//    @OnClick(R.id.rl_deposit)
//    void onClickDeposit() {
//        if ("1".equals(SupportUrl.getAlicaiType())) {
//            //进入我的爱理财首页
//            mPresenter.gotoFinanceHome(getActivity());
//        } else if ("2".equals(SupportUrl.getAlicaiType())) {
//            //进入我的房产宝
//            if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
//                loginAction = LoginManager.LoginAction.ACTION_INDEX_MY_ASSETS;
//                LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
//            } else {
//                jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_MY_ASSETS);
//            }
//        }
//        EventLog.upEventLog("220", "alc");
//    }
//
//    /**
//     * 我的经纪人
//     */
//    @OnClick(R.id.rlAgent)
//    void onClickConsultantList() {
//        whereClick = CLICK_LOCATION_CLICK_AGENT;
//        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
//            loginAction = LoginManager.LoginAction.ACTION_INDEX_AGENT;
//            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
//        } else {
//            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_AGENT);
//            ManyiAnalysis.getInstance().onEvent(EventStr.MINE_AGENT_MENU_ITEM_CLICK);
//            EventLog.upEventLog("220", "mycounselor");
//        }
//    }
//
//    /**
//     * 我委托的房源
//     */
//    @OnClick(R.id.mine_entrust_manage_list)
//    void onClickOwner() {
////        EventLog.upEventLog("254", "my_complaint");
//        whereClick = CLICK_LOCATION_CLICK_MY_ENTRUST;
//        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
//            loginAction = LoginManager.LoginAction.ACTION_INDEX_OWNER;
//            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
//        } else {
//            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_OWNER);
//        }
//    }
//
//    /**
//     * 品牌公寓约看记录
//     */
//    @OnClick(R.id.rl_brand_department_appointment_record)
//    void onClickBrandDepartmentAppointmentRecord() {
//
//        whereClick = CLICK_LOCATION_CLICK_RESERVE_BRAND_APARTMENT;
//        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
//            loginAction = LoginManager.LoginAction.ACTION_INDEX_SEE_RECORD;
//            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
//        } else {
//            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_SEE_RECORD);
//        }
//
//    }
//
//    /**
//     * 我的投诉
//     */
//    @OnClick(R.id.mine_complaint)
//    void onClickComplaint() {
//        EventLog.upEventLog("220", "complain");
//        whereClick = CLICK_LOCATION_CLICK_MYCOMPLAINT;
//        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
//            loginAction = LoginManager.LoginAction.ACTION_INDEX_COMPLAIN;
//            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
//        } else {
//            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_COMPLAIN);
//        }
//    }
//
//    /**
//     * 卡券
//     */
//    @OnClick(R.id.mineCardCouponst)
//    void onClickRedEnvelopes() {
//        whereClick = CLICK_LOCATION_CLICK_CARD_TICKET;
//        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
//            loginAction = LoginManager.LoginAction.ACTION_INDEX_CARD_COUPONST;
//            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
//        } else {
//            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_CARD_COUPONST);
//            // 点击后卡券后，本地小红点状态改为false
//            setVoucherRedDotState(false);
//        }
//        EventLog.upEventLog("220", "kg");
//    }
//
//    /**
//     * 银行卡
//     */
//    @OnClick(R.id.rl_bank_card)
//    void onClickBankCard() {
//        whereClick = CLICK_LOCATION_CLICK_BANK_CARD;
//        EventLog.upEventLog("220", "bankCard");
//        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
//            loginAction = LoginManager.LoginAction.ACTION_INDEX_BANK_CARD;
//            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
//        } else {
//            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_BANK_CARD);
//        }
//    }
//
//    /**
//     * 交易密码
//     */
//    @OnClick(R.id.mine_password_manage)
//    void onClickPassword() {
//        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
//            loginAction = LoginManager.LoginAction.ACTION_INDEX_PASSWORD;
//            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
//        } else {
//            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_PASSWORD);
//        }
//    }
//
//    /*
//    购房须知
//     */
//    @OnClick(R.id.mine_purchase_notice)
//    void onClickPurchaseNotice() {
//        //从当前城市获取购房须知相关字段
//        CityDBItem cityDBItem = CityManager.getInstance().getCurrentCity();
//        Log.d("BuyNoticeUrl::", cityDBItem.getBuyNoticeUrl());
//        ManyiAnalysis.getInstance().onEvent(EventStr.MINE_NOTICE_MENU_ITEM_CLICK);
//        mPresenter.goPurchaseNotice(getActivity(), cityDBItem.getBuyNoticeUrl(), cityDBItem.getNeedRefresh());
//    }
//
//    /*
//    购房百科
//     */
//    @OnClick(R.id.mine_encyclopedias)
//    void onClickEncyclopedias() {
//        //从当前城市获取购房须知相关字段
//        CityDBItem cityDBItem = CityManager.getInstance().getCurrentCity();
//        Log.d("BuyNoticeUrl::", cityDBItem.getBuyNoticeUrl());
//        ManyiAnalysis.getInstance().onEvent(EventStr.MINE_NOTICE_MENU_ITEM_CLICK);
//        MinePresenter.goEncyclopedias(getActivity(), cityDBItem.getBuyNoticeUrl(), cityDBItem.getNeedRefresh());
//    }
//
//    /**
//     * 帮助中心
//     */
//    @OnClick(R.id.mine_help)
//    void onClickHelpCenter() {
//        EventLog.upEventLog("220", "helpAndTickling");
//        CityDBItem cityDBItem = CityManager.getInstance().getCurrentCity();
//        String url = SupportUrl.getHelpCenterUrl();
//        mPresenter.gotoHelpCenter(getActivity(), url, cityDBItem.getNeedRefresh());
//
////        Intent intent = new Intent(getActivity(), RegularFinancingDetailActivity.class);
////        intent.putExtra(RegularFinancingDetailActivity.PROD_ID, "20160601161423S60004");
////        startActivity(intent);
//    }
//
//    @OnClick(R.id.mine_setting_btn)
//    void onClickSettings() {
//        mPresenter.gotoSettings(getActivity());
//        EventLog.upEventLog("220", "set");
//    }
//
//    @OnLongClick(R.id.mine_setting_btn)
//    boolean onLongClickSettings() {
//        if (!DeBugLogActivity.isRelease()) {
//            Intent m = new Intent(getActivity(), AgentDetailActivity.class);
//            getActivity().startActivity(m);
//        }
//        return true;
//    }
//
//    @OnClick(R.id.mine_servicetel)
//    void onClickServicetel() {
//        //mPresenter.callPhone(getActivity());
//        String serverNumber = UserInfo.getInstance().getServicePhoneNumber();
//        showConfirmCallDialog(serverNumber);
//    }
//
//
//    /**
//     * 点击约看清单
//     */
//    @OnClick(R.id.rl_yue_kan_list)
//    void onClickYueKanList() {
//        whereClick = CLICK_LOCATION_CLICK_YUE_KAN;
//        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
//            loginAction = LoginManager.LoginAction.ACTION_INDEX_YUE_KAN_LIST;
//            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
//        } else {
//            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_YUE_KAN_LIST);
//        }
//    }
//
//    /**
//     * 点击看房日程
//     */
//    @OnClick(R.id.rl_kan_fang)
//    void onClickKanFang() {
//        whereClick = CLICK_LOCATION_CLICK_KAN_FANG_SHEDULE;
//        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
//            loginAction = LoginManager.LoginAction.ACTION_INDEX_KAN_FANG_LIST;
//            LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_MINE);
//        } else {
//            jumpToMenuTarget(LoginManager.LoginAction.ACTION_INDEX_KAN_FANG_LIST);
//        }
//    }
//
//
//    /**
//     * 点击版本更新时，强制更新退出客户端
//     *
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void handleExitEvent(ExitEvent event) {
//        if (event.isForceExit()) {
//            //更新版本时强制退出
//            SystemUtil.exitApplication(getActivity());
//        } else {
//            //退出登录
//            LoginManager.loginOut(getActivity());
//        }
//    }
//
//
//    /**
//     * 获取我的页面相关信息
//     */
//    void refreshMyDataFromServer() {
//        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
//            housePartResponse = null;
//            ailicaiPartResponse = null;
//            setUIData();
//            return;
//        }
//        getUserInfo();
//        getHousePartData();
//        getAilicaiPartData();
//    }
//
//    /**
//     * 获取用户信息
//     */
//    public void getUserInfo() {
//        UserInfoRequest request = new UserInfoRequest();
//        request.setUserId((int) UserInfo.getInstance().getUserId());
//        ServiceSender.exec(this, request, new UserInfoResponseIwjwRespListener(this));
//    }
//
//    /**
//     * 获取房源部分数据
//     */
//    public void getHousePartData() {
//        HousePartRequest request = new HousePartRequest();
//        ServiceSender.exec(this, request, new HousePartResponseIwjwRespListener(this));
//    }
//
//    /**
//     * 获取爱理财部分数据
//     */
//    public void getAilicaiPartData() {
//        AilicaiPartRequest request = new AilicaiPartRequest();
//        ServiceSender.exec(this, request, new AilicaiPartResponseIwjwRespListener(this));
//    }
//
//    @Override
//    public void reloadData() {
//        LogUtil.d("---mine-----reloadData-------");
//        if (!PersonalCenterFragment.this.isVisible() || !PersonalCenterFragment.this.isAdded()) {
//            return;
//        }
//        refreshMyDataFromServer();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void handleShowRefreshNotifEvent(ShowRefreshNotifEvent event) {
//        setKanFangData();
//    }
//
//    private static class OrderListResponseIwjwRespListener extends IwjwRespListener<OrderListResponse> {
//
//        private SoftReference<PersonalCenterFragment> personalCenterFragmentSoftReference;
//
//        OrderListResponseIwjwRespListener(PersonalCenterFragment personalCenterFragment) {
//            this.personalCenterFragmentSoftReference = new SoftReference<>(personalCenterFragment);
//        }
//
//        @Override
//        public void onStart() {
//            if (null != personalCenterFragmentSoftReference.get()) {
//                personalCenterFragmentSoftReference.get().showLoadTranstView();
//            }
//        }
//
//        @Override
//        public void onFinish() {
//            super.onFinish();
//            if (null != personalCenterFragmentSoftReference.get()) {
//                PersonalCenterFragment personalCenterFragment = personalCenterFragmentSoftReference.get();
//                personalCenterFragment.showContentView();
//            }
//        }
//
//        @Override
//        public void onJsonSuccess(OrderListResponse response) {
//            if (null != personalCenterFragmentSoftReference.get()) {
//                PersonalCenterFragment personalCenterFragment = personalCenterFragmentSoftReference.get();
//                if (response != null && response.getOrderList().size() > 0) {
//                    Order order = response.getOrderList().get(0);
//                    if (order != null) {
//                        Intent intent;
//                        switch (order.getType()) {
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
//                        }
//                        personalCenterFragment.getActivity().startActivity(intent);
//                    }
//                }
//            }
//        }
//    }
//
//    private static class UserInfoResponseIwjwRespListener extends IwjwRespListener<UserInfoResponse> {
//
//        private SoftReference<PersonalCenterFragment> personalCenterFragmentSoftReference;
//
//        UserInfoResponseIwjwRespListener(PersonalCenterFragment personalCenterFragment) {
//            super(personalCenterFragment);
//            this.personalCenterFragmentSoftReference = new SoftReference<>(personalCenterFragment);
//        }
//
//        @Override
//        public void onJsonSuccess(UserInfoResponse jsonObject) {
//            LoginManager.saveUserInfo(jsonObject);
//            if (null != personalCenterFragmentSoftReference.get()) {
//                personalCenterFragmentSoftReference.get().setUIData();
//            }
//        }
//
//        @Override
//        public void onFailInfo(String errorInfo) {
//            if (null != personalCenterFragmentSoftReference.get()) {
//                personalCenterFragmentSoftReference.get().setUIData();
//            }
//            ToastUtil.showInCenter(errorInfo);
//        }
//    }
//
//    private static class HousePartResponseIwjwRespListener extends IwjwRespListener<HousePartResponse> {
//
//        private SoftReference<PersonalCenterFragment> personalCenterFragmentSoftReference;
//
//        HousePartResponseIwjwRespListener(PersonalCenterFragment personalCenterFragment) {
//            super(personalCenterFragment);
//            this.personalCenterFragmentSoftReference = new SoftReference<>(personalCenterFragment);
//        }
//
//        @Override
//        public void onJsonSuccess(HousePartResponse jsonObject) {
//            if (null != personalCenterFragmentSoftReference.get()) {
//                PersonalCenterFragment personalCenterFragment = personalCenterFragmentSoftReference.get();
//                int code = jsonObject.getErrorCode();
//                if (code == 0) {
//                    personalCenterFragment.housePartResponse = jsonObject;
//                    personalCenterFragment.setHousePartData();
//                }
//                // 未登录，点击订单登录，如果一个账单跳转账单列表 ①
//                if (personalCenterFragment.loginAction == LoginManager.LoginAction.ACTION_INDEX_ORDER) {
//                    personalCenterFragment.jumpToMenuTarget(personalCenterFragment.loginAction);
//                }
//            }
//        }
//
//        @Override
//        public void onFailInfo(String errorInfo) {
//            if (null != personalCenterFragmentSoftReference.get()) {
//                PersonalCenterFragment personalCenterFragment = personalCenterFragmentSoftReference.get();
//                personalCenterFragment.housePartResponse = null;
//                personalCenterFragment.setHousePartData();
//                ToastUtil.showInCenter(errorInfo);
//            }
//        }
//    }
//
//    private static class AilicaiPartResponseIwjwRespListener extends IwjwRespListener<AilicaiPartResponse> {
//
//        private SoftReference<PersonalCenterFragment> personalCenterFragmentSoftReference;
//
//        AilicaiPartResponseIwjwRespListener(PersonalCenterFragment personalCenterFragment) {
//            super(personalCenterFragment);
//            this.personalCenterFragmentSoftReference = new SoftReference<>(personalCenterFragment);
//        }
//
//        @Override
//        public void onJsonSuccess(AilicaiPartResponse jsonObject) {
//            if (null != personalCenterFragmentSoftReference.get()) {
//                PersonalCenterFragment personalCenterFragment = personalCenterFragmentSoftReference.get();
//                int code = jsonObject.getErrorCode();
//                if (code == 0) {
//                    personalCenterFragment.ailicaiPartResponse = jsonObject;
//                    personalCenterFragment.setAilicaiPartData();
//                }
//            }
//        }
//
//        @Override
//        public void onFailInfo(String errorInfo) {
//            if (null != personalCenterFragmentSoftReference.get()) {
//                PersonalCenterFragment personalCenterFragment = personalCenterFragmentSoftReference.get();
//                personalCenterFragment.ailicaiPartResponse = null;
//                personalCenterFragment.setAilicaiPartData();
//                ToastUtil.showInCenter(errorInfo);
//            }
//        }
//    }
//
//    /**
//     * 卡券上小红点
//     *
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void handleRefreshPushEvent(RefreshPushEvent event) {
//        if (event.getMsgType() == PushMessage.REMINDTYPENEWVOUCHER || event.getMsgType() ==
//                PushMessage.REMINDTYPECOUPONBANNER || event.getMsgType() == PushMessage.REMINDTYPETIYANJI || event.getMsgType() == PushMessage.REMINDTYPEFANLISHENHE) {
//            setVoucherRedDotState(true);
//        }
//    }
//
//    private void setVoucherRedDotState(boolean isShow) {
//        if(isShow) {
//            ticket_red_dot.setVisibility(View.VISIBLE);
//        } else {
//            ticket_red_dot.setVisibility(View.INVISIBLE);
//        }
//        saveVoucherRedDotStateByUser(isShow);
//    }
//
//    private void saveVoucherRedDotStateByUser(boolean isShow) {
//        long userId = UserManager.getInstance(getWRActivity()).getUserInfoBase().getUserId();
//        MyPreference.getInstance().write(userId+"VoucherRedDotIsShow",isShow);
//    }
//
//    public boolean isShowVoucherRedDotState() {
//        long userId = UserManager.getInstance(getWRActivity()).getUserInfoBase().getUserId();
//        return  MyPreference.getInstance().read(userId+"VoucherRedDotIsShow",false);
//    }
//}
