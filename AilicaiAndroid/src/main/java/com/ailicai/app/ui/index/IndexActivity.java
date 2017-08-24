package com.ailicai.app.ui.index;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.constants.CommonTag;
import com.ailicai.app.common.download.DownloadNotificationManager;
import com.ailicai.app.common.download.DownloadProgressDialogManger;
import com.ailicai.app.common.reqaction.IwjwHttp;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.common.utils.UIUtils;
import com.ailicai.app.common.version.VersionInterface;
import com.ailicai.app.common.version.VersionUtil;
import com.ailicai.app.eventbus.LoginEvent;
import com.ailicai.app.eventbus.MineShowRedPointEvent;
import com.ailicai.app.model.request.HtmlUrlRequest;
import com.ailicai.app.model.response.Iwjwh5UrlResponse;
import com.ailicai.app.receiver.ScreenStatusReceiver;
import com.ailicai.app.ui.asset.FinanceUpgradePresenter;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.html5.SupportUrl;
import com.ailicai.app.ui.index.adapter.NavigationPagerAdapter;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.message.MessageTypeProcessUtils;
import com.ailicai.app.ui.mine.MineFragment;
import com.ailicai.app.widget.NoScrollViewPager;
import com.ailicai.app.widget.ahbottomnavigation.AHBottomNavigation;
import com.ailicai.app.widget.ahbottomnavigation.AHBottomNavigationItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;


/**
 * name: IndexActivity <BR>
 * description: 首页容器<BR>
 * create date: 2017/7/12
 *
 * @author: IWJW Zhou Xuan
 */
public class IndexActivity extends BaseBindActivity implements VersionInterface {

    private final static int[][] tabiconss = new int[][]{
            {R.drawable.tabbar_homepage_inactive, R.drawable.tabbar_homepage_active},
            {R.drawable.tabbar_investment_inactive, R.drawable.tabbar_investment_active},
            {R.drawable.tabbar_mine_inactive, R.drawable.tabbar_mine_active}};


    @Bind(R.id.fragment_pager)
    NoScrollViewPager mViewPager;
    @Bind(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;
    @Bind(R.id.bottom_line_view)
    View bottomLineView;
    NavigationPagerAdapter nvgPagerAdapter;
    /**
     * 控制首页初始化只用加载一次通知数据
     */
    int notifLoadCount = 0;
    Toast toast;
    private boolean hasCheckNewVersion = false;
    private long LastBackTime;
    private ScreenStatusReceiver mScreenStatusReceiver;

    public static void startIndexActivityToTab(Activity activity, int settabIndex) {
        Intent mIntent = new Intent(activity, IndexActivity.class);
        mIntent.putExtra("settabIndex", settabIndex);
        activity.startActivity(mIntent);
    }

    public static void goToInvestTab(Activity activity, int indexWantedInInvest) {
        Intent mIntent = new Intent(activity, IndexActivity.class);
        mIntent.putExtra("settabIndex", 1);
        mIntent.putExtra("investTabIndex", indexWantedInInvest);
        activity.startActivity(mIntent);
    }

    /**
     * 销毁下载弹窗
     */
    private static void downloadProgressDestroy(Context mC) {
        DownloadProgressDialogManger.getInstance().destory();
        DownloadNotificationManager.getInstance(mC).destroy();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_index_main;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        EventBus.getDefault().register(this);
        setViewPageData();
        htmlUrlUpdate();
        MyPreference.getInstance().write(CommonTag.IS_FIREST_START, false);
        //注册息屏广播
        if (mScreenStatusReceiver == null) {
            mScreenStatusReceiver = new ScreenStatusReceiver();
            IntentFilter screenStatusIF = new IntentFilter();
//            screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
            screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(mScreenStatusReceiver, screenStatusIF);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //if (!MyPreference.getInstance().read(HASCHECKNEWVERSION, false)) {
        if (!hasCheckNewVersion) {
            VersionUtil.check(this, this);
        }
        //}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //MyPreference.getInstance().write(HASCHECKNEWVERSION, false);
        downloadProgressDestroy(MyApplication.getInstance());
        IwjwHttp.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mScreenStatusReceiver != null) {
            unregisterReceiver(mScreenStatusReceiver);
            mScreenStatusReceiver = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    private void setViewPageData() {
        mViewPager.setCanScroll(false);
        mViewPager.setOffscreenPageLimit(3);
        nvgPagerAdapter = new NavigationPagerAdapter(this, bottomNavigation, mViewPager, getSupportFragmentManager());
        Bundle indexBundle = new Bundle();
        nvgPagerAdapter.addNvgItem(new AHBottomNavigationItem(R.string.tab_nvg_index, tabiconss[0][1], tabiconss[0][0], android.R.color.white), IndexFragment.class, indexBundle);
        Bundle followsBundle = getIntent().getExtras();
        nvgPagerAdapter.addNvgItem(new AHBottomNavigationItem(R.string.tab_nvg_invest, tabiconss[1][1], tabiconss[1][0], android.R.color.white), InvestmentMainFragment.class, followsBundle);
        Bundle msgBundle = new Bundle();
        nvgPagerAdapter.addNvgItem(new AHBottomNavigationItem(R.string.tab_nvg_me, tabiconss[2][1], tabiconss[2][0], android.R.color.white), MineFragment.class, msgBundle);
        bottomNavigation.addItems(nvgPagerAdapter.getBottomNavigationItems());
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setAccentColor(getResources().getColor(R.color.main_red_color));
        bottomNavigation.setInactiveColor(Color.parseColor("#616161"));
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#ff001f"));
        bottomNavigation.setNotificationTextColor(Color.WHITE);
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.color_f7f7f7));
        bottomNavigation.setUseElevation(true);
        bottomNavigation.setTitleTextSize(UIUtils.dipToPx(this, 11f), UIUtils.dipToPx(this, 10f));
        MessageTypeProcessUtils.parseIntent(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                setChildMiMiSystemBarColor(position);
                setChildFragmentAutoRefreshState(position);
                notifyInvestLoad();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent != null) {
            if (intent.hasExtra("settabIndex")) {
                int settabitem = intent.getIntExtra("settabIndex", -1);
                setCurrentItem(settabitem);
            }

            if (intent.hasExtra("investTabIndex")) {
                int investTabIndex = intent.getIntExtra("investTabIndex", -1);
                InvestmentMainFragment fragment = (InvestmentMainFragment) nvgPagerAdapter.getItem(1);
                if (fragment != null) {
                    fragment.setCurrentItem(investTabIndex);
                }
            }
        }
        MessageTypeProcessUtils.parseIntent(this);
    }

    private void htmlUrlUpdate() {
        HtmlUrlRequest request = new HtmlUrlRequest();
        ServiceSender.exec(this, request, new IwjwRespListener<Iwjwh5UrlResponse>() {
            @Override
            public void onJsonSuccess(Iwjwh5UrlResponse jsonObject) {
                SupportUrl.saveUrls(jsonObject);

                // 请求是否弹框提醒升级协议(爱理财接入协议)
                FinanceUpgradePresenter presenter = new FinanceUpgradePresenter();
                presenter.httpForProtocalUpgradeState(IndexActivity.this);
            }
        });
    }

    @Override
    public void remindPoint() {
        // TODO 等有更新时候设置里面需要红点的时候再打开
//        setRedNotification(2);
    }

    @Override
    public void checkStart() {

    }

    @Override
    public void checkSuccess() {
        //检查通过
        this.hasCheckNewVersion = true;
    }

    @Override
    public void checkFailed(String message) {
        //检查没通过
    }

    @Override
    public void checkLatest(String version) {
        //检查通过并没有更新
    }

    @Override
    public boolean ignorePop() {
        return false;
    }

    public int getCurrentItem() {
        return bottomNavigation.getCurrentItem();
    }

    public void setCurrentItem(int index) {
        //   mViewPager.setCurrentItem(index);
        bottomNavigation.setCurrentItem(index);
    }

    /**
     * Set the notification number
     *
     * @param nbNotification
     * @param itemPosition
     */
    public void refreshNotification(int nbNotification, int itemPosition) {
        if (null != bottomNavigation) {
            bottomNavigation.setNotification(nbNotification, itemPosition);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleShowRefreshNotifEvent(MineShowRedPointEvent showPoint) {
        if (UserInfo.isLogin()) {
            if (showPoint.isShowRedPoint()) {
                //我的红点
                setRedNotification(2);
            } else {
                //去掉我的红点
                refreshNotification(0, 2);
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleShowRefreshNotifEvent(LoginEvent event) {
        if (!event.isLoginSuccess()) {
            //去掉我的红点
            refreshNotification(0, 2);
        }
    }

    /**
     * 设置红点提醒
     * 设置红点提醒
     */
    public void setRedNotification(int tabIndex) {
        if (bottomNavigation == null) {
            return;
        }

        for (int t = 0; t < bottomNavigation.getChildCount(); t++) {
            android.view.View tabView = bottomNavigation.getChildAt(t);
            if (tabView instanceof ViewGroup) {
                ViewGroup tabViewGroup = (ViewGroup) tabView;
                if (tabViewGroup.getChildCount() == getBottomNavigationNbItems()) {
                    TextView textViewNotification = (TextView) tabViewGroup.getChildAt(tabIndex)
                            .findViewById(R.id.bottom_navigation_notification);
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) textViewNotification
                            .getLayoutParams();
                    lp.height = getResources().getDimensionPixelOffset(R.dimen._8);
                    lp.width = getResources().getDimensionPixelOffset(R.dimen._8);
                    textViewNotification.setLayoutParams(lp);
                    textViewNotification.setTextColor(Color.TRANSPARENT);
                    bottomNavigation.setNotification(1, tabIndex);
                }
            }
        }
    }

    /**
     * Return the number of items in the bottom navigation
     */
    public int getBottomNavigationNbItems() {
        return bottomNavigation.getItemsCount();
    }

    public int getNotifLoadCount() {
        return notifLoadCount;
    }

    public void initOneceNum() {
        notifLoadCount = 1;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLoginEvent(LoginEvent event) {
        //TODO 只添加了推送或短信消息类型处理
//        refreshGlobalData(this);
        if (event.isLoginSuccess()) {
            MessageTypeProcessUtils.parseIntent(this);
        } else {
            setIntent(new Intent());
        }

        if (event.isCancelLogin()) {
            setIntent(new Intent());
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - LastBackTime > 2500) {
            LastBackTime = System.currentTimeMillis();
            toast = ToastUtil.showInBottom(getApplication(), "再按一次返回退出爱理财");
        } else {
            if (toast != null) {
                toast.cancel();
                toast = null;
            }
            MyApplication.getAppPresenter().onExitApp();
            SystemUtil.exitApplication(this);
        }
    }

    public void setChildMiMiSystemBarColor(int position) {
        if (position == 0) {
            IndexFragment indexFragment = (IndexFragment) nvgPagerAdapter.getItem(0);
            indexFragment.setMiSystemBarColor();
        } else if (position == 1) {
            CommonUtil.miDarkSystemBar(IndexActivity.this);
        } else if (position == 2) {
            CommonUtil.miWhiteSystemBar(IndexActivity.this);
        }
    }

    public void setChildFragmentAutoRefreshState(int position) {
        IndexFragment indexFragment = (IndexFragment) nvgPagerAdapter.getItem(0);
        InvestmentMainFragment investmentMainFragment = (InvestmentMainFragment) nvgPagerAdapter.getItem(1);

        if (position == 0) {
            indexFragment.startOrStopAutoRefresh(true);
            investmentMainFragment.setAllRefreshStateStop();
        } else if (position == 1) {
            indexFragment.startOrStopAutoRefresh(false);
            investmentMainFragment.setAutoRefreshStateStart();
        } else {
            indexFragment.startOrStopAutoRefresh(false);
            investmentMainFragment.setAllRefreshStateStop();
        }
    }

    public void notifyInvestLoad() {
        InvestmentMainFragment investmentMainFragment = (InvestmentMainFragment) nvgPagerAdapter.getItem(1);
        investmentMainFragment.notifyLoadUrl(0);
    }
}
