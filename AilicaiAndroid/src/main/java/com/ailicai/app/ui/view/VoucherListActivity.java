package com.ailicai.app.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.ailicai.app.R;
import com.ailicai.app.model.bean.Voucher;
import com.ailicai.app.model.response.VoucherListResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.view.vocher.VoucherListAdapterNew;
import com.ailicai.app.ui.view.vocher.VoucherListPresenter;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.bottomrefreshlistview.BottomRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 加息券列表Activity
 * Created by liyanan on 16/3/4.
 */
public class VoucherListActivity extends BaseBindActivity implements SwipeRefreshLayout.OnRefreshListener, BottomRefreshListView.OnLoadMoreListener, VoucherListAdapterNew.UseClickListener {
    public static final String EXTRA_PRODUCT_ID = "product_id";
    public static final String EXTRA_APPROPRIATE_VOUCHER_ID = "appropriate_voucher_id";
    public static final String EXTRA_AMOUNT = "amount";
    @Bind(R.id.srl_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.lv_red_envelope)
    BottomRefreshListView lvRedEnvelope;
    @Bind(R.id.title_view)
    IWTopTitleView titleView;

    private View vHead;
    private int amount;
    private int appropriateOrSelectedVoucherId = -1;

    @OnClick(R.id.top_title_back_textview)
    public void onTopTitleBackTextview() {
//        setResult(1000);
//        finish();
    }

    private List<Voucher> values = new ArrayList<>();
    private VoucherListAdapterNew adapter;
    private VoucherListPresenter presenter;
    /**
     * 总数量
     */
    private int totalSize = 0;
    private String productId;

    @Override
    public int getLayout() {
        return R.layout.activity_voucher_list;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        appropriateOrSelectedVoucherId = getIntent().getIntExtra(EXTRA_APPROPRIATE_VOUCHER_ID,-1);
        amount = getIntent().getIntExtra(EXTRA_AMOUNT,0);
        swipeRefreshLayout.setColorSchemeResources(R.color.main_red_color);
        swipeRefreshLayout.setOnRefreshListener(this);
        lvRedEnvelope.setOnLoadMoreListener(this);
        presenter = new VoucherListPresenter(this);
        presenter.refresh(true, productId,amount);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        //刷新红包列表
        presenter.refresh(false, productId,amount);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMore() {
        if (loadMore()) {
            lvRedEnvelope.setLoadingText("加载中");
        } else {
            lvRedEnvelope.onAllLoaded();
            lvRedEnvelope.setPromptText("卡券已全部加载");
        }
    }

    private boolean loadMore() {
        int size = values.size();
        if (size > 0 && totalSize > size) {
            //加载更多
            presenter.loadMore(size, productId,amount);
            return true;
        }
        return false;
    }

    /**
     * 上次加载失败，重新加载数据
     */
    @Override
    public void reloadData() {
        super.reloadData();
        presenter.refresh(true, productId,amount);
    }

    /**
     * 刷新出错
     *
     * @param errorInfo
     */
    public void refreshFail(String errorInfo) {
        swipeRefreshLayout.setRefreshing(false);
        showErrorView(errorInfo);
    }

    /**
     * 刷新成功
     *
     * @param jsonObject
     */
    public void refreshSuccess(VoucherListResponse jsonObject) {
        swipeRefreshLayout.setRefreshing(false);
        if (jsonObject != null && jsonObject.getVoucherList() != null && jsonObject.getVoucherList().size() > 0) {
            //有数据
            showContentView();
            totalSize = jsonObject.getTotal();
            if (adapter == null) {
                adapter = new VoucherListAdapterNew(this, values, appropriateOrSelectedVoucherId, this);
                lvRedEnvelope.setAdapter(adapter);
                vHead= View.inflate(this, R.layout.voucher_no_use, null);
                vHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(1000);
                        finish();
                    }
                });
                lvRedEnvelope.addHeaderView(vHead);
            }
            values.clear();
            values.addAll(jsonObject.getVoucherList());
            adapter.notifyDataSetChanged();
            lvRedEnvelope.smoothScrollToPosition(0);
            if (values.size() == totalSize) {
                lvRedEnvelope.onAllLoaded();
                lvRedEnvelope.setPromptText("卡券已全部加载");
            } else {
                lvRedEnvelope.resetAll();
            }
        } else {
            //无数据
            View emptyView = getLayoutInflater().inflate(R.layout.voucher_list_empty, null);
            showNoDataView(emptyView);
        }

    }

    /**
     * 加载更多失败
     *
     * @param errorInfo
     */
    public void loadMoreFail(String errorInfo) {
        lvRedEnvelope.onLoadMoreComplete();
        showErrorView(errorInfo);
    }

    /**
     * 加载更多成功
     *
     * @param jsonObject
     */
    public void loadMoreSuccess(VoucherListResponse jsonObject) {
        if (jsonObject != null && jsonObject.getVoucherList() != null && jsonObject.getVoucherList().size() > 0) {
            //有数据
            lvRedEnvelope.onLoadMoreComplete();
            values.addAll(jsonObject.getVoucherList());
            adapter.notifyDataSetChanged();
        } else {
            //无数据
            lvRedEnvelope.onAllLoaded();
            lvRedEnvelope.setPromptText("卡券已全部加载");
        }

    }

    /**
     * 点击选择卡券
     *
     * @param position
     */
    @Override
    public void useClick(int position) {
        Intent intent = new Intent();
        intent.putExtra("doesUse", 1);
        intent.putExtra("voucherId", values.get(position).getVoucherId());
        intent.putExtra("voucherType", values.get(position).getVoucherType());
        intent.putExtra("minAmountCent", values.get(position).getMinAmountCent());
        switch (values.get(position).getVoucherType()){
            case 73://加息券
                intent.putExtra("addRate", values.get(position).getAddRate());
                intent.putExtra("addRateDay", values.get(position).getAddRateDay());
            break;
            case 74://返金券
                intent.putExtra("voucherValue", values.get(position).getAmountCentString());

                break;
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.removeActivity();
    }
}
