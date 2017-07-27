package com.ailicai.app.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.eventbus.CapitalApplyRefreshEvent;
import com.ailicai.app.eventbus.CapitalProductTypeChangeEvent;
import com.ailicai.app.eventbus.LoginEvent;
import com.ailicai.app.eventbus.RegularPayEvent;
import com.ailicai.app.eventbus.ReservePayEvent;
import com.ailicai.app.model.response.AssetInfoResponse;
import com.ailicai.app.model.response.HasBuyProductListResponse;
import com.ailicai.app.ui.asset.CapitalListProductDetailActivity;
import com.ailicai.app.ui.asset.FinanceUpgradePresenter;
import com.ailicai.app.ui.asset.HouseTreasureAdapter;
import com.ailicai.app.ui.asset.treasure.ProductCategory;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.index.IndexActivity;
import com.ailicai.app.ui.login.AccountInfo;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.view.detail.ExpiredTiyanbaoListActivity;
import com.ailicai.app.ui.view.detail.IncomeDetailActivity;
import com.ailicai.app.ui.view.detail.SmallCoinSackActivity;
import com.ailicai.app.widget.BottomRefreshListView;
import com.ailicai.app.widget.DialogBuilder;
import com.ailicai.app.widget.TextViewDinFont;
import com.huoqiu.framework.util.CheckDoubleClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created on 2016/5/30.
 */

public class CapitalActivity extends BaseBindActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener,
        AbsListView.OnScrollListener,
        HouseTreasureAdapter.Event,
        BottomRefreshListView.OnLoadMoreListener {


    private static final int REQUEST_FOR_PROCESS = 0x00;
    public static final String TAB = "TAB";
    public static final String APPLY = "apply";
    public static final String EXPIRED = "Expired";
    public static final String HOLD = "hold";

    private CapitalPresenter presenter;

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.capital_home_page)
    BottomRefreshListView mHomePage;
    @Bind(R.id.capital_regular_float)
    View mFloatView;

    TextView mRegularApply;
    TextView mRegularHold;
    TextView mRegularExpired;

    @Bind(R.id.capital_regular_apply_float)
    TextView mRegularApplyF;
    @Bind(R.id.capital_regular_hold_float)
    TextView mRegularHoldF;
    @Bind(R.id.capital_regular_expired_float)
    TextView mRegularExpiredF;

    List<TextView> regularList = new ArrayList<>();

    View mRegularLayout;

    TextViewDinFont mCapitalTotal;
    TextViewDinFont mIncome;//累计收益
    TextViewDinFont preProfit;//待收收益

    View mIncomeLayout,preProfitLayout;
    HouseTreasureAdapter mAdapter;

    int statusBarHeight = 0;
    boolean next;
    private String principalTitle = "";
    private String principalDes = "";

    boolean tabSpecified = false;
    boolean first = true;

    @OnClick(R.id.capital_regular_apply_float)
    void onApplyClick() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        regularApply();
    }

    @OnClick(R.id.capital_regular_hold_float)
    void onHoldClick() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        regularHold();
    }

    @OnClick(R.id.capital_regular_expired_float)
    void onExpiredClick() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        regularExpired();
    }

    void regularApply() {
        loadProductList(mRegularApply, mRegularApplyF, ProductCategory.Apply);
    }

    void regularHold() {
        loadProductList(mRegularHold, mRegularHoldF, ProductCategory.Holder);
    }

    void regularExpired() {
        loadProductList(mRegularExpired, mRegularExpiredF, ProductCategory.Expired);
    }

    void loadProductList(View regularView, View floatView, ProductCategory category) {
        resetHomePage();
        boolean lastSelected = regularView.isSelected();
        setRegularSelected(regularView, floatView);
        if (!lastSelected) {
            presenter.updateContent(true);
        }
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_capital;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        EventBus.getDefault().register(this);

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.main_red_color);

        View headerView = getLayoutInflater().inflate(R.layout.capital_home_page_header, null);
        mHomePage.addHeaderView(headerView, null, false);
        mHomePage.onLoadMoreComplete();
        mHomePage.setOnLoadMoreListener(this);
        findHeaderView(headerView);
        layoutHeaderView();

        mAdapter = new HouseTreasureAdapter(this);
        mHomePage.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mHomePage.setOnScrollListener(this);
        mAdapter.setEvent(this);

        // 爱理财系统升级弹框
        FinanceUpgradePresenter financeUpgradePresenter = new FinanceUpgradePresenter();
        financeUpgradePresenter.httpForSystemIsFix(this);

        presenter = new CapitalPresenter(this);

        presenter.updateHeader();
        presenter.updateContent(true);

        if (null != getIntent() && !TextUtils.isEmpty(getIntent().getStringExtra(TAB))) {
            switch (getIntent().getStringExtra(TAB)) {
                case APPLY:
                    tabSpecified = true;
                    regularApply();
                    break;
                case EXPIRED:
                    tabSpecified = true;
                    regularExpired();
                    break;
                case HOLD:
                    tabSpecified = true;
                    regularHold();
                    break;
            }
        }

        // 请求是否弹框提醒升级协议
        FinanceUpgradePresenter presenter = new FinanceUpgradePresenter();
        presenter.httpForProtocalUpgradeState(this);
    }

    int calFitEmptyHeight() {

        int fitHeight;
        int scrollY = getScrollY(mHomePage);

        int header = statusBarHeight + mRegularLayout.getHeight();

        int diff = mRegularLayout.getBottom() - scrollY;

        if (diff <= header) fitHeight = mHomePage.getHeight() - header;
        else fitHeight = mHomePage.getHeight() - diff;

        return fitHeight;
    }

    void findHeaderView(View root) {
        mRegularLayout = root.findViewById(R.id.capital_regular_layout);
        mRegularApply = (TextView) root.findViewById(R.id.capital_regular_apply);
        mRegularHold = (TextView) root.findViewById(R.id.capital_regular_hold);
        mRegularExpired = (TextView) root.findViewById(R.id.capital_regular_expired);
        mCapitalTotal = (TextViewDinFont) root.findViewById(R.id.capital_total);
        mIncome = (TextViewDinFont) root.findViewById(R.id.accumulated_income);
        preProfit = (TextViewDinFont) root.findViewById(R.id.pre_profit);

        mIncomeLayout = root.findViewById(R.id.accumulated_income_layout);
        preProfitLayout = root.findViewById(R.id.preProfit_layout);

        root.findViewById(R.id.capital_help).setOnClickListener(this);
        mIncomeLayout.setOnClickListener(this);
        preProfitLayout.setOnClickListener(this);
        mRegularApply.setOnClickListener(this);
        mRegularHold.setOnClickListener(this);
        mRegularExpired.setOnClickListener(this);

        regularList.add(mRegularApply);
        regularList.add(mRegularApplyF);
        regularList.add(mRegularHold);
        regularList.add(mRegularHoldF);
        regularList.add(mRegularExpired);
        regularList.add(mRegularExpiredF);

        setRegularSelected(mRegularApply, mRegularApplyF);

    }


    void layoutHeaderView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            statusBarHeight = CommonUtil.getStatusBarHeight(this);
        }
    }

    void setRegularSelected(View regularView, View floatView) {
        for (View view : regularList)
            view.setSelected(view.equals(regularView) || view.equals(floatView));

//        if (mFloatView.getVisibility() == View.VISIBLE)
//            mHomePage.setSelectionFromTop(1, mRegularLayout.getHeight() + statusBarHeight);

    }

    @Override
    public void toFinanceRegular() {
        IndexActivity.startIndexActivityToTab(this,0);
    }

    private void toRegularIncome() {
        if (AccountInfo.isOpenAccount()) {
            EventLog.upEventLog("800","ljsy");
            Intent intent = new Intent(this, IncomeDetailActivity.class);
            intent.putExtra(IncomeDetailActivity.TYPE, IncomeDetailActivity.REGULAR);
            startActivity(intent);
        }

    }

    @Override
    public void toProductDetail(String productId, int type, String hasTransferAmount, String transferingAmount) {
        Intent intent = new Intent(this, CapitalListProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CapitalListProductDetailActivity.PRODUCT_ID, productId);
        bundle.putBoolean(CapitalListProductDetailActivity.IS_RESERVE, false);
        bundle.putInt(CapitalListProductDetailActivity.TYPE, type);
        bundle.putString(CapitalListProductDetailActivity.HAS_TRANSFER_AMOUNT,hasTransferAmount);
        bundle.putString(CapitalListProductDetailActivity.TRANSFERING_AMOUNT,transferingAmount);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void toReserveProductDetail(String productId) {
        Intent intent = new Intent(this, CapitalListProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CapitalListProductDetailActivity.PRODUCT_ID, productId);
        bundle.putBoolean(CapitalListProductDetailActivity.IS_RESERVE, true);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void toCoinSack(String productId , int type) {
        Intent intent = new Intent(this, SmallCoinSackActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SmallCoinSackActivity.PRODUCT_ID, productId);
        bundle.putInt(SmallCoinSackActivity.CATEGORY, type);
        intent.putExtras(bundle);
        startActivity(intent);

    }


    @Override
    public void errorReload() {
        resetHomePage();
        presenter.updateContent(true);
    }

    @Override
    public void toExpiredTiyanbaoList() {
        Intent intent = new Intent(this, ExpiredTiyanbaoListActivity.class);
        startActivity(intent);
    }

    @Override
    public void toTiyanbaoProductDetail(long couponId,int type) {
        Intent intent = new Intent(this, CapitalListProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(CapitalListProductDetailActivity.TIYANBAO_ID,couponId);
        bundle.putBoolean(CapitalListProductDetailActivity.IS_TIYANBAO, true);
        bundle.putInt(CapitalListProductDetailActivity.TYPE, type);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        if (CheckDoubleClick.isFastDoubleClick()) return;
        switch (v.getId()) {

            case R.id.accumulated_income_layout:
                toRegularIncome();
                break;
            case R.id.capital_regular_apply:
                regularApply();
                break;
            case R.id.capital_regular_hold:
                regularHold();
                break;
            case R.id.capital_regular_expired:
                regularExpired();
                break;
            case R.id.capital_help:
                showCapitalHelpDialog();
                break;
        }

    }

    private void showCapitalHelpDialog() {
        EventLog.upEventLog("800","cybj");
        DialogBuilder.showSimpleDialog(this, principalTitle, principalDes, null, null, "我知道了", null).setCancelable(false);
/*        DialogBuilder.getAlertDialog(this)
                .setTitle(principalTitle)
                .setMessage(principalDes)
                .setPositiveButton("我知道了", null)
                .setCancelable(false)
                .show();*/
    }

    @Override
    public void onLoadMore() {
        if (mSwipeLayout.isRefreshing()) {
            return;
        }

        if (next) {
            mHomePage.setLoadingText("更多房产宝加载中...");

            int productOffset = mAdapter.getProductOffset();
            int reserveOffset = mAdapter.getReserveOffset();

            presenter.sendProductService(false, productOffset, reserveOffset);
        } else {
            mHomePage.onAllLoaded();
            mHomePage.setPromptText("已全部加载");
        }

    }

    public void onRefreshFinished(boolean refresh) {
        if (refresh) {
            mSwipeLayout.setRefreshing(false);
        } else {
            mHomePage.onLoadMoreComplete();
        }
    }

    public void showNetworkErrorView(boolean validNetwork,String error){
        mAdapter.updateEmpty(getCategory(),false,-1);
        mAdapter.updateNetworkState(validNetwork,error);
        mAdapter.notifyDataSetChanged();

    }

    void resetHomePage() {
        next = false;
        mHomePage.resetAll();
    }

    @Override
    public void onRefresh() {
        resetHomePage();
        presenter.updateHeader();
        presenter.updateContent(true);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mRegularLayout.getTop() <= 0) return;
        if (getScrollY(view) >= mRegularLayout.getTop()) {
            mFloatView.setVisibility(View.VISIBLE);
        } else {
            mFloatView.setVisibility(View.GONE);
        }
    }

    int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();
        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = view.getHeight();
        }
        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    public void deployPageElements(AssetInfoResponse assetInfo) {
        principalTitle = assetInfo.getPrincipalTitle();
        principalDes = assetInfo.getPrincipalDes();
        String total = "0.00";
        if (!TextUtils.isEmpty(assetInfo.getTimeDepositBalance())) {
            total = assetInfo.getTimeDepositBalance();
        }
        mCapitalTotal.setText(total);
        mIncome.setText(CommonUtil.formatMoneyForFinance(assetInfo.getTotalIncome()));
        preProfit.setText(assetInfo.getPreProfit());
        updateRegularSection(assetInfo.getApplyNum(), assetInfo.getHoldNum(), assetInfo.getOverdueNum());
    }

    public void deployProductList(HasBuyProductListResponse data, boolean clear) {
        ProductCategory category = getCategory();

        mAdapter.addDataSource(data.getProductList(), data.getReserveList(), data.getTiyanbaoList(),category, clear);


        int productOffset = mAdapter.getProductOffset();
        int reserveOffset = mAdapter.getReserveOffset();
        int tiyanbaoOffset = mAdapter.getTiyanbaoOffset();

        if (category == ProductCategory.Apply) {
            next = data.getTotal() + data.getReserveTotal() > (productOffset + reserveOffset);
            if (productOffset + reserveOffset == 0) mAdapter.updateEmpty(category, false, -1);
        } else if (category == ProductCategory.Holder){
            next = data.getTotal() > (productOffset);
            if (productOffset + tiyanbaoOffset == 0) mAdapter.updateEmpty(category,false, -1);
        }else {
            next = data.getTotal() > productOffset;
            if (productOffset == 0) {
                mAdapter.updateEmpty(category, false, -1);
            }
            if (clear){
                mAdapter.updateExpiredTiyanbaoCount(data.getTiyanbaoTotal());

            }
        }

        if (clear){
            if (category == ProductCategory.Apply) {
                updateRegularSection(data.getTotal() + data.getReserveTotal(), -1, -1);
            } else if (category == ProductCategory.Holder) {
                updateRegularSection(-1, data.getTotal()+data.getTiyanbaoTotal(), -1);
            } else if (category == ProductCategory.Expired) {
                updateRegularSection(-1, -1, data.getTotal()+data.getTiyanbaoTotal());
            }
        }

        mAdapter.updateNetworkState(true,"");
        mAdapter.notifyDataSetChanged();
    }

    public void loadingState(boolean loading, boolean calHeight) {
        mAdapter.updateExpiredTiyanbaoCount(0);
        mAdapter.updateEmpty(getCategory(), loading, calHeight ? calFitEmptyHeight() : -1);
        mAdapter.notifyDataSetChanged();
    }

    public void showNoDataView() {
        loadingState(false, false);

        /** reset float view. **/
        updateRegularSection(0, 0, 0);
    }


    private void updateRegularSection(int apply, int hold, int expired) {
        if (apply == 0) {
            mRegularApply.setText("申购");
            mRegularApplyF.setText("申购");
        } else if (apply > 0) {
            mRegularApply.setText(String.format("申购(%s)", apply > 99 ? "99+" : apply));
            mRegularApplyF.setText(String.format("申购(%s)", apply > 99 ? "99+" : apply));
        }

        if (hold == 0) {
            mRegularHold.setText("持有");
            mRegularHoldF.setText("持有");
        } else if (hold > 0) {
            mRegularHold.setText(String.format("持有(%s)", hold > 99 ? "99+" : hold));
            mRegularHoldF.setText(String.format("持有(%s)", hold > 99 ? "99+" : hold));
        }

        if (expired == 0) {
            mRegularExpired.setText("回款");
            mRegularExpiredF.setText("回款");
        } else if (expired > 0) {
            mRegularExpired.setText(String.format("回款(%s)", expired > 99 ? "99+" : expired));
            mRegularExpiredF.setText(String.format("回款(%s)", expired > 99 ? "99+" : expired));
        }

        if (!tabSpecified && first) {
            if (apply > 0) {
                regularApply();
                first = false;
            } else if (hold > 0) {
                regularHold();
                first = false;
            } else if (expired > 0) {
                regularExpired();
                first = false;
            }
        }

    }

    public ProductCategory getCategory() {
        if (mRegularApply.isSelected()) return ProductCategory.Apply;
        if (mRegularHold.isSelected()) return ProductCategory.Holder;
        if (mRegularExpired.isSelected()) return ProductCategory.Expired;
        return null;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLoginEvent(LoginEvent event) {
        resetHomePage();
        //presenter.updateHeader();
        presenter.updateContent(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleRegularPayEvent(RegularPayEvent regularPayEvent) {
        resetHomePage();
        presenter.updateContent(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleCapitalProductTypeChangeEvent(CapitalProductTypeChangeEvent event) {
        presenter.updateContent(true);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleReservePayEvent(ReservePayEvent reservePayEvent) {
        resetHomePage();
        presenter.updateContent(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_FOR_PROCESS: {
                resetHomePage();
                presenter.updateHeader();
                presenter.updateContent(true);

                break;
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.updateHeader();
        presenter.updateContent(false);
    }

    @Override
    public void onDestroy() {

        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void reloadData() {
        super.reloadData();
        presenter.updateHeader();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleCapitalApplyRefreshEvent(CapitalApplyRefreshEvent event) {
        presenter.updateContent(true);
    }

    public static boolean goCapital(Activity context, Bundle bundle) {
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            LoginManager.goLogin(context, LoginManager.LOGIN_FROM_SMS_FCB);
            return false;
        } else {
            Intent capital = new Intent(context, CapitalActivity.class);
            capital.putExtras(bundle);
            context.startActivity(capital);
            return true;
        }
    }

    @Override
    public void logout(Activity activity, String msg) {
        super.logout(activity, msg);
        finish();
    }
}
