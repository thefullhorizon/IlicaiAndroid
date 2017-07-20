package com.ailicai.app.ui.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.WeakReferenceHandler;
import com.ailicai.app.eventbus.FinancePayEvent;
import com.ailicai.app.eventbus.MoneyChangeEvent;
import com.ailicai.app.eventbus.RegularPayEvent;
import com.ailicai.app.model.response.RefreshProductDetailResponse;
import com.ailicai.app.model.response.RegularProductDetailResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.buy.NoSetSafeCardHint;
import com.ailicai.app.ui.buy.ProcessActivity;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.reserve.CounterFinishListener;
import com.ailicai.app.ui.reserve.TimeZHTextCounter;
import com.ailicai.app.widget.drag.DragLayout;
import com.ailicai.app.widget.drag.DragSwipeRefreshLayout;
import com.ailicai.app.widget.IWTopTitleView;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 房产宝详情页
 * Created by duo on 2016/01/04.
 */
public class RegularFinancingDetailActivity extends BaseBindActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int REQUEST_FOR_PROCESS_BUY = 10001;//立即购买
    private static final int REQUEST_FOR_PROCESS_HUO_QI_BAO = 10002;//点击钱包
    private static final int MESSAGE_CODE_REFRESH = 10003;//消息的what值
    @Bind(R.id.financing_regular_detail_title)
    IWTopTitleView mDetailTitle;
    @Bind(R.id.financing_regular_detail_timezhtextcounter)
    TimeZHTextCounter mTimeZhTextCounter;
    @Bind(R.id.financing_regular_detail_buy)
    LinearLayout mDetailBuy;
    @Bind(R.id.financing_regular_detail_buy_text)
    TextView mBuyText;
    @Bind(R.id.financing_detail_huoqibao)
    View huoQiBaoView;
    @Bind(R.id.tv_balance_label)
    TextView tvBalanceLabel;
    @Bind(R.id.drag_swipe_refresh)
    DragSwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.drag_layout)
    DragLayout dragLayout;

    public static final String PROD_ID = "prod_id";
    //负数表示没开户
    double availableBalance = -1;
    private String id;

    private RegularFinanceDetailFragment detailFragment;
    private RegularDetailTabFragment tabFragment;
    private RegularFinanceDetailPresenter detailPresenter;
    private WeakReferenceHandler handler;
    private int productStatus;
    private RegularProductDetailResponse detailResponse;

    @Override
    public int getLayout() {
        return R.layout.activity_regular_finance_detail;
    }


    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        EventBus.getDefault().register(this);
        id = getIntent().getStringExtra(PROD_ID);
        initFragment();
        initSwipeRefreshLayout();
        detailPresenter = new RegularFinanceDetailPresenter(this);
        initHandler();
     //   getServerTime();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (productStatus == 1) {
            //恢复刷新
            LogUtil.e("lyn", "onResume");
            handler.sendEmptyMessage(MESSAGE_CODE_REFRESH);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //暂停刷新
        LogUtil.e("lyn", "onPause");
        handler.removeMessages(MESSAGE_CODE_REFRESH);
    }

    /**
     * 初始化拖拽的Fragment
     */
    private void initFragment() {
        detailFragment = new RegularFinanceDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PROD_ID, getIntent().getStringExtra(PROD_ID));
        detailFragment.setArguments(bundle);
        tabFragment = new RegularDetailTabFragment();
        tabFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_top, detailFragment).commitAllowingStateLoss();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_bottom, tabFragment).commitAllowingStateLoss();
        dragLayout.setDragTextViewId(R.id.tv_drag_text);
    }

    /**
     * 初始化下拉刷新控件
     */
    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.main_red_color);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setDragView(dragLayout, R.id.drag_down_scroll);
    }

    /**
     * 初始化handler,用于自动刷新
     */
    private void initHandler() {
        handler = new WeakReferenceHandler(this) {
            @Override
            protected void handleMessage(Object reference, Message msg) {
                //刷新房产宝状态及进度值
                if (!isFinishing()) {
                    LogUtil.e("lyn", "autoRefresh start");
                    detailPresenter.autoRefresh(id);
                }
            }
        };
    }

/*    *//**
     * 获取服务端时间
     *//*
    private void getServerTime() {
        detailPresenter.getServerTime();
    }*/

    /**
     * 请求ServerTimer成功后
     */
    public void handleServiceTimeResponse() {
        loadDetailData();
    }

    /**
     * 加载房产宝详情页数据
     */
    public void loadDetailData() {
        if (!TextUtils.isEmpty(id)) {
            detailPresenter.requestDetail(id);
        }
    }

    /**
     * 重新加载
     */
    @Override
    public void reloadData() {
        super.reloadData();
 //       getServerTime();
        handler.removeMessages(MESSAGE_CODE_REFRESH);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        reloadData();
    }

    /**
     * 第一次或下拉处理请求成功
     *
     * @param response
     */
    public void handleResponse(final RegularProductDetailResponse response) {
        detailResponse = response;
        productStatus = response.getProduct().getStatus();
        availableBalance = response.getAvailableBalance();
        mDetailTitle.setTitleText(response.getProduct().getProductName());
        handleBuyByStatus(productStatus);
        mTimeZhTextCounter.setCounterFinishListener(new CounterFinishListener() {

            @Override
            public void onFinish() {
                if (productStatus == 2) {
                    productStatus = 1;
                    handleBuyByStatus(1);
                    handler.sendEmptyMessage(MESSAGE_CODE_REFRESH);
                }
            }
        });
        mTimeZhTextCounter.setTimeMills(response.getProduct().getStartBuyTime());
        swipeRefreshLayout.setRefreshing(false);
        detailFragment.handleResponse(response);
        tabFragment.onRefresh();
        if (productStatus == 1) {
            handler.sendEmptyMessageDelayed(MESSAGE_CODE_REFRESH, 3000);
        }
    }


    /**
     * 处理底部立即购买的状态
     *
     * @param status
     */
    public void handleBuyByStatus(int status) {
        int dp_14 = DeviceUtil.getPixelFromDip(this, 14);
        int dp_8 = DeviceUtil.getPixelFromDip(this, 8);
        LinearLayout.LayoutParams params;
        switch (status) {
            case 1:
                //募集中
                mTimeZhTextCounter.setVisibility(View.GONE);
                params = (LinearLayout.LayoutParams) mBuyText.getLayoutParams();
                params.setMargins(0, dp_14, 0, dp_14);
                mBuyText.setText("立即购买");
                mBuyText.setTextColor(getResources().getColor(R.color.color_ffffff));
                mDetailBuy.setBackgroundResource(R.drawable.btn_red_selector);
                mDetailBuy.setEnabled(true);
                break;
            case 2:
                //即将开售
                params = (LinearLayout.LayoutParams) mBuyText.getLayoutParams();
                params.setMargins(0, dp_8, 0, 0);
                mTimeZhTextCounter.setVisibility(View.VISIBLE);
                mDetailBuy.setBackgroundColor(getResources().getColor(R.color.white));
                mDetailBuy.setEnabled(false);
                mBuyText.setText("即将开售");
                mBuyText.setTextColor(Color.parseColor("#757575"));
                break;
            case 3:
                //已售罄
                params = (LinearLayout.LayoutParams) mBuyText.getLayoutParams();
                params.setMargins(0, dp_14, 0, dp_14);
                mTimeZhTextCounter.setVisibility(View.GONE);
                mDetailBuy.setBackgroundColor(Color.parseColor("#d2d5db"));
                mDetailBuy.setEnabled(false);
                mBuyText.setText("已售罄");
                mBuyText.setTextColor(getResources().getColor(R.color.color_BDBDBD));
                break;
            default:
                break;
        }
        if (UserInfo.isLogin()) {
            //已登录
            if (detailResponse.getIsOpen() == 1) {
                //已开户
                tvBalanceLabel.setText("账户余额不足1000元 马上转入");
                if (availableBalance < 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            huoQiBaoView.setVisibility(View.GONE);
                        }
                    });
                } else if (availableBalance < 1000) {
                    huoQiBaoView.setVisibility(View.VISIBLE);
                    mDetailBuy.setEnabled(false);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            huoQiBaoView.setVisibility(View.GONE);
                        }
                    });
                }
            } else {
                //未开户
                changeBalanceLabel(status);
            }
        } else {
            //未登录
            changeBalanceLabel(status);
        }
    }

    /**
     * 改变余额的文案
     *
     * @param status
     */
    private void changeBalanceLabel(int status) {
        tvBalanceLabel.setText("房产宝产品必须用账户余额购买 马上转入");
        huoQiBaoView.setVisibility(View.VISIBLE);
        if (status != 2) {
            mDetailBuy.setBackgroundColor(Color.parseColor("#d2d5db"));
            mDetailBuy.setEnabled(false);
            mBuyText.setTextColor(getResources().getColor(R.color.color_BDBDBD));
        }
    }


    /**
     * 自动刷新成功
     */
    public void autoRefreshSuccess(RefreshProductDetailResponse response) {
        productStatus = response.getStatus();
        LogUtil.e("lyn", "autoRefreshSuccess");
        //更新底部状态
        handleBuyByStatus(productStatus);
        //更新进度和额度
        detailFragment.autoRefresh(response.getHasBuyPrecent(), response.getBiddableAmountStr(), productStatus);
        if (productStatus == 1) {
            handler.sendEmptyMessageDelayed(MESSAGE_CODE_REFRESH, 3000);
        }
    }

    /**
     * 自动刷新失败
     */
    public void autoRefreshFail() {
        LogUtil.e("lyn", "autoRefreshFail");
        if (productStatus == 1) {
            handler.sendEmptyMessageDelayed(MESSAGE_CODE_REFRESH, 3000);
        }
    }

    /**
     * 立即购买
     */
    @OnClick(R.id.financing_regular_detail_buy)
    public void clickBuy() {
        EventLog.upEventLog("252", "ccb_buy");
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (UserInfo.isLogin() && availableBalance >= 1000) {
                //已登录且可用余额大于1000
                MyIntent.startActivity(this, RegularPayActivity.class, id);
            } else {
                //进入统一处理页面
                Intent intent = new Intent(this, ProcessActivity.class);
                startActivityForResult(intent, REQUEST_FOR_PROCESS_BUY);
            }
        }
    }

    /**
     * 点击余额不足
     */
    @OnClick(R.id.financing_detail_huoqibao)
    public void clickHuoQiBao() {
        Intent intent = new Intent(this, ProcessActivity.class);
        startActivityForResult(intent, REQUEST_FOR_PROCESS_HUO_QI_BAO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_FOR_PROCESS_HUO_QI_BAO:
                    //点击钱包
                    reloadData();
                    if (!NoSetSafeCardHint.isShowHintDialog(this)) {
                        Intent intent = new Intent(this, CurrentRollInActivity.class);
                        startActivity(intent);
                    }
                    break;
                case REQUEST_FOR_PROCESS_BUY:
                    //立即购买
                    reloadData();
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleRegularPayEvent(RegularPayEvent regularPayEvent) {
        reloadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleFinancePayEvent(FinancePayEvent payEvent) {
        reloadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMoneyChangeEvent(MoneyChangeEvent payEvent) {
        reloadData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(MESSAGE_CODE_REFRESH);
        }
        detailPresenter.removeActivity();
        EventBus.getDefault().unregister(this);
    }


}