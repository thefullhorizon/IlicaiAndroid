package com.ailicai.app.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.SpannableUtil;
import com.ailicai.app.eventbus.FinancePayEvent;
import com.ailicai.app.model.response.WalletInfoResponse;
import com.ailicai.app.ui.asset.FinanceUpgradePresenter;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.buy.NoSetSafeCardHint;
import com.ailicai.app.ui.buy.ProcessActivity;
import com.ailicai.app.ui.login.AccountInfo;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.paypassword.PayPwdManageActivity;
import com.ailicai.app.ui.view.detail.FinanceHomeActivity;
import com.ailicai.app.ui.view.detail.IncomeDetailActivity;
import com.ailicai.app.ui.view.reserveredrecord.ReserveRecordListActivity;
import com.ailicai.app.ui.view.transaction.TransactionListActivity;
import com.ailicai.app.widget.CustomScrollView;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.mpchart.charts.LineChart;
import com.huoqiu.framework.util.CheckDoubleClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by duo.chen on 2016/7/1.10:50
 * <p>
 * 钱包首页
 */

public class MyWalletActivity extends BaseBindActivity implements SwipeRefreshLayout
        .OnRefreshListener, CustomScrollView.ScrollViewListener {

    private static final int REQUEST_FOR_PROCESS_OUT = 10001;
    private static final int REQUEST_FOR_PROCESS_IN = 10002;
    private static final int REQUEST_FOR_PROCESS_INCOME_YESTER = 10004;
    private static final int REQUEST_FOR_PROCESS_RESERVE = 10005;

    @Bind(R.id.mywallet_title)
    IWTopTitleView topTitleView;
    @Bind(R.id.wallet_yesterday_income)
    TextView walletYesIncome;
    @Bind(R.id.wallet_frozen)
    TextView walletFrozen;
    @Bind(R.id.wallet_transacton_text)
    TextView walletTransactonText;
    @Bind(R.id.wallet_available)
    TextView walletAvailable;
    @Bind(R.id.wallet_password_manager)
    View walletPasswordManager;
    @Bind(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.wallet_scrollview)
    CustomScrollView scrollView;
    @Bind(R.id.mywallet_line_chart)
    LineChart lineChart;
    @Bind(R.id.wallet_unitIncome)
    TextView unitIncome;
    @Bind(R.id.mywallet_yesterday_rate)
    TextView yesterdayRate;
    SpannableUtil spannableUtil;
    private MyWalletPresenter presenter;
    private String productInfoUrl;

    //区别于goMywallet()，跟之前的业务隔离，此方法只在“我的”页面里面的“钱包”点击事件调用
    public static void goMywalletFromMyFragment(Activity activity) {
        if (UserInfo.isLogin()) {
            Intent intent = new Intent(activity, MyWalletActivity.class);
            activity.startActivity(intent);
        } else {
            LoginManager.goLogin(activity, LoginManager.LOGIN_FROM_MINE);
        }
    }

    public static void goMywallet(Activity activity) {
        if (UserInfo.isLogin()) {
            Intent intent = new Intent(activity, MyWalletActivity.class);
            activity.startActivity(intent);
        } else {
            LoginManager.goLogin(activity, LoginManager.LOGIN_FROM_DEMANDTREASURE_DIALOG);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.mywallet_layout;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        EventBus.getDefault().register(this);
        presenter = new MyWalletPresenter(this);
        spannableUtil = new SpannableUtil(this);
        /*
        topTitleView.addRightText(R.string.help_ic, R.style.text_18_ffffff, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMoreDetail();
            }
        });
        topTitleView.getRightText().setAlpha(1.0f);
        */
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.main_red_color);
        scrollView.setOnScrollListener(this);
        showLoadView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.requestMyWalletInfo();
        refresh();

        // 请求是否 是否维护中
        FinanceUpgradePresenter presenter = new FinanceUpgradePresenter();
        presenter.httpForSystemIsFix(this);
        // 请求是否弹框提醒升级协议
        presenter.httpForProtocalUpgradeState(this);
    }

    public void refresh() {
        if (UserInfo.isLogin() && AccountInfo.isSetPayPwd()) {
            walletPasswordManager.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.wallet_transaction_layout)
    void clickTransaction() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        Intent intent = new Intent(this, TransactionListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.wallet_password_manager)
    void clickPasswordManage() {
        if (CheckDoubleClick.isFastDoubleClick()) return;

        Intent intent = new Intent(this, PayPwdManageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.wallet_roll_out)
    void clickRollout() {
        if (CheckDoubleClick.isFastDoubleClick()) return;

        if (!NoSetSafeCardHint.isShowHintDialog(this)) {
//            EventLog.upEventLog("350", "out", 4);
            Intent intent = new Intent(this, ProcessActivity.class);
            startActivityForResult(intent, REQUEST_FOR_PROCESS_OUT);
        }
    }

    @OnClick(R.id.wallet_roll_in)
    void clickRollin() {
        if (CheckDoubleClick.isFastDoubleClick()) return;

        if (!NoSetSafeCardHint.isShowHintDialog(this)) {
//            EventLog.upEventLog("350", "into", 4);
            Intent intent = new Intent(this, ProcessActivity.class);
            startActivityForResult(intent, REQUEST_FOR_PROCESS_IN);
        }
    }

    @OnClick(R.id.wallet_yesterday_income_layout)
    void toDemandIncomeDetail() {
        if (CheckDoubleClick.isFastDoubleClick()) return;

//        EventLog.upEventLog("755", "zrsy", 4);

        Intent intent = new Intent(this, ProcessActivity.class);
        startActivityForResult(intent, REQUEST_FOR_PROCESS_INCOME_YESTER);
    }

    @OnClick(R.id.wallet_reserve_layout)
    void clickReserveLayout() {
        if (CheckDoubleClick.isFastDoubleClick()) return;

        Intent intent = new Intent(this, ProcessActivity.class);
        startActivityForResult(intent, REQUEST_FOR_PROCESS_RESERVE);
    }

    public void toMoreDetail() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        if (!TextUtils.isEmpty(productInfoUrl)) {
//            EventLog.upEventLog("354", "huoqibao_detial", 4);
            Map<String, String> dataMap = ObjectUtil.newHashMap();
            dataMap.put(WebViewActivity.TITLE, "更多详情");
            dataMap.put(WebViewActivity.URL, productInfoUrl);
            dataMap.put(WebViewActivity.NEED_REFRESH, 0 + "");
            dataMap.put(WebViewActivity.TOPVIEWTHEME, "true");
            MyIntent.startActivity(MyWalletActivity.this, WebViewActivity.class, dataMap);
        }
    }

    @OnClick(R.id.mywallet_more_detail)
    void clickMoreDetail() {
        toMoreDetail();
    }

    @OnClick(R.id.mywallet_raise_income)
    void clickRaiseIncome() {
        Intent intent = new Intent(this, FinanceHomeActivity.class);
        startActivity(intent);
//        EventLog.upEventLog("354", "more_money_product", 4);
    }

    @OnClick(R.id.wallet_frozen_layout)
    void walletFrozenRecoard() {
        Intent intent = new Intent(this, IncomeDetailActivity.class);
        intent.putExtra(IncomeDetailActivity.TYPE, IncomeDetailActivity.REGULAR);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_FOR_PROCESS_IN: {
                    Intent intent = new Intent(this, CurrentRollInActivity.class);
                    startActivity(intent);
                    break;
                }
                case REQUEST_FOR_PROCESS_OUT: {
                    Intent intent = new Intent(this, CurrentRollOutActivity.class);
                    startActivity(intent);
                    break;
                }
                case REQUEST_FOR_PROCESS_INCOME_YESTER: {
                    Intent intent = new Intent(this, IncomeDetailActivity.class);
                    intent.putExtra(IncomeDetailActivity.TYPE, IncomeDetailActivity.WALLET);
                    startActivity(intent);
                    break;
                }
                case REQUEST_FOR_PROCESS_RESERVE:
                    Intent intent = new Intent(this, ReserveRecordListActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    public void setMywalletInfo(WalletInfoResponse walletInfoResponse) {
        if (!TextUtils.isEmpty(walletInfoResponse.getYestodayIncomeStr())) {

            if (walletInfoResponse.getYestodayIncomeStr().contains("计算中")) {
                walletYesIncome.setTextSize(24);
            } else {
                walletYesIncome.setTextSize(40);
            }
            walletYesIncome.setText(walletInfoResponse.getYestodayIncomeStr());
        } else {
            walletYesIncome.setText(CommonUtil.formatMoneyForFinance(walletInfoResponse.getYestodayIncome()));
        }
        walletAvailable.setText(CommonUtil.formatMoneyForFinance(walletInfoResponse.getAvailableBalance()));
        walletFrozen.setText(CommonUtil.formatMoneyForFinance(walletInfoResponse.getTotalIncome()));
        yesterdayRate.setText(walletInfoResponse.getYearInterestRateDesc());
        if (walletInfoResponse.getRecentNum() > 0) {
            walletTransactonText.setText(String.format("最近7日有%s笔记录", walletInfoResponse.getRecentNum()));
        }

        unitIncome.setText(spannableUtil.getSpannableString(
                "投资万元每日可得实际收益",
                walletInfoResponse.getUnitIncome(),
                "元",
                R.style.text_12_757575,
                R.style.text_12_f23829,
                R.style.text_12_757575));

        productInfoUrl = walletInfoResponse.getProductInfoUrl();

        if (walletInfoResponse.getFundYeildRateList() != null && walletInfoResponse
                .getFundYeildRateList().size() > 0) {
            presenter.initChart(lineChart, walletInfoResponse.getFundYeildRateList());
        }
    }

    @Override
    public void reloadData() {
        super.reloadData();
        if (null != presenter) {
            presenter.requestMyWalletInfo();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleFinancePayEvent(FinancePayEvent event) {
        if (!event.getPayState().equals("F")) {
            presenter.requestMyWalletInfo();
        }
    }

    @Override
    public void logout(Activity activity, String msg) {
        super.logout(activity, msg);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        reloadData();
    }

    public void onLoadFinish() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onScrollChanged(CustomScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (x == 0) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }
    }

    @Override
    public void scrollOritention(int oritention) {

    }

    public void updateContent(String money) {
        unitIncome.setText(spannableUtil.getSpannableString(
                "投资万元每日可得实际收益",
                money,
                "元",
                R.style.text_12_757575,
                R.style.text_12_f23829,
                R.style.text_12_757575));
    }
}
