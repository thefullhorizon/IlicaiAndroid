package com.ailicai.app.ui.view.detail;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.ailicai.app.R;
import com.ailicai.app.model.response.CoinListResponse;
import com.ailicai.app.ui.asset.treasure.ProductCategory;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.widget.BottomRefreshListView;
import com.ailicai.app.widget.IWTopTitleView;

import butterknife.Bind;

/**
 * Created by nanshan on 2017/5/4.
 */

public class SmallCoinListActivity extends BaseBindActivity implements SwipeRefreshLayout.OnRefreshListener, BottomRefreshListView.OnLoadMoreListener, AbsListView.OnScrollListener {

    @Bind(R.id.iw_title_back)
    IWTopTitleView iwTitleBack;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.ll_content)
    LinearLayout mLLContent;
    @Bind(R.id.coin_list_view)
    BottomRefreshListView coinListView;

    boolean next;
    private CoinListAdapter mAdapter;
    private SmallCoinListPresenter coinListPresenter;

    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_CATEGORY = "product_category";
    public static final String PRODUCT_STATE = "product_state";
    public static final String BID_ORDER_NO = "bid_order_no";

    private String productId;
    private String productName;
    private String smallCoinSackStatus;
    private int productCategory;//产品类型
    private String bidOrderNo = "";

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        productId = bundle.getString(PRODUCT_ID, "");
        productName = bundle.getString(PRODUCT_NAME, "");
        productCategory = bundle.getInt(PRODUCT_CATEGORY, 0);
        smallCoinSackStatus = bundle.getString(PRODUCT_STATE, "N");
        bidOrderNo = bundle.getString(BID_ORDER_NO, "");

        iwTitleBack.setTitleText(productName);

        if(smallCoinSackStatus.equals("Y")){//根据小钱袋的申购进度状态初始化界面

            mSwipeLayout.setOnRefreshListener(this);
            mSwipeLayout.setColorSchemeResources(R.color.main_red_color);
            coinListView.onLoadMoreComplete();
            coinListView.setOnLoadMoreListener(this);

            mAdapter = new CoinListAdapter(this,getProductCategory());
            coinListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            coinListView.setOnScrollListener(this);

            coinListPresenter = new SmallCoinListPresenter(this);
            coinListPresenter.updateCoinListData(productId,bidOrderNo,0,true);

        }else{
            mLLContent.setVisibility(View.GONE);
        }

    }

    ProductCategory getProductCategory(){
        if(productCategory == 1){
            return ProductCategory.Apply;
        }else if(productCategory == 2){
            return ProductCategory.Holder;
        }else if(productCategory == 3){
            return ProductCategory.Expired;
        }
        return null;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_coin_list;
    }


    public void disposeCoinListInfo(CoinListResponse response, boolean needClear){

        onRefreshFinished(true);
        next = mAdapter.getHasLoadedSmallCoinData() <  response.getTotal();
        mAdapter.updateData(response.getProductList(),needClear);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRefresh() {
        coinListPresenter.updateCoinListData(productId,bidOrderNo,0,true);
    }


    @Override
    public void onLoadMore() {
        if (mSwipeLayout.isRefreshing()) {
            return;
        }
        if (next) {
            coinListView.setLoadingText("更多小钱加载中...");
            coinListPresenter.updateCoinListData(productId,bidOrderNo,mAdapter.getHasLoadedSmallCoinData(),false);

        } else {
            coinListView.onAllLoaded();
            coinListView.setPromptText("小钱已全部加载");
        }

    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    public void onRefreshFinished(boolean refresh) {
        if (refresh) {
            mSwipeLayout.setRefreshing(false);
        } else {
            coinListView.onLoadMoreComplete();
        }
    }


}
