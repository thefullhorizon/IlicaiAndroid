package com.ailicai.app.ui.view.reserveredrecord;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.model.bean.ProductInvestRecord;
import com.ailicai.app.model.response.ProductInvestRecordListResponse;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.ui.view.RegularFinancingDetailActivity;
import com.ailicai.app.widget.drag.DragUpListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 投资历史记录
 * Created by liyanan on 16/4/7.
 */
public class ProductInvestRecordFragment extends BaseBindFragment implements DragUpListView.OnLoadMoreListener {
    @Bind(R.id.lv_record)
    DragUpListView lvRecord;
    private TextView tvTotalCount;//总数量
    private ProductInvestRecordListAdapter adapter;
    private List<ProductInvestRecord> records = new ArrayList<>();
    private int total;
    private ProductInvestRecordPresenter presenter;
    private String id;
    private View footerView;

    private boolean isReserve = false;//是否是预约记录

    @Override
    public int getLayout() {
        return R.layout.fragment_product_invest_record;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        id = getArguments().getString(RegularFinancingDetailActivity.PROD_ID);
        isReserve = getArguments().getBoolean("isReserve");
        addHeaderView();
        initAdapter();
        presenter = new ProductInvestRecordPresenter(this);
    }

    public void onRefresh() {
        presenter.requestData(id, 0, true);
    }

    /**
     * 首次加载成功
     *
     * @param response
     */
    public void requestSuccess(ProductInvestRecordListResponse response) {
        if (response != null && response.getRows() != null && response.getRows().size() > 0) {
            //有数据
            showContentView();
            records.clear();
            records.addAll(response.getRows());
            adapter.notifyDataSetChanged();
            total = response.getTotal();
            tvTotalCount.setText("共" + total + String.valueOf(isReserve?"笔预约记录":"笔投资记录"));
            if (records.size() == total) {
                lvRecord.onAllLoaded();
                addFooterView();
            } else {
                lvRecord.resetAll();
            }
        } else {
            //无数据
            View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.product_invest_record_list_empty, null);
            showNoDataView(emptyView);
        }
    }

    /**
     * 首次加载失败
     *
     * @param errorInfo
     */
    public void requestFail(String errorInfo) {
        showErrorView(errorInfo);
    }

    /**
     * 加载更多成功
     *
     * @param response
     */
    public void loadMoreSuccess(ProductInvestRecordListResponse response) {
        if (response != null && response.getRows() != null && response.getRows().size() > 0) {
            //有数据
            lvRecord.onLoadMoreComplete();
            records.addAll(response.getRows());
            adapter.notifyDataSetChanged();
            total = response.getTotal();
            tvTotalCount.setText("共" + total + String.valueOf(isReserve?"笔预约记录":"笔投资记录"));
            if (records.size() == total) {
                lvRecord.onAllLoaded();
                addFooterView();
            }
        } else {
            //无数据
            lvRecord.onAllLoaded();
            addFooterView();
        }
    }

    /**
     * 加载更多失败
     *
     * @param errorInfo
     */
    public void loadMoreFail(String errorInfo) {
        lvRecord.onLoadMoreComplete();
        showErrorView(errorInfo);
    }

    @Override
    public void onLoadMore() {
        presenter.requestData(id, records.size(), false);
    }

    @Override
    public void reloadData() {
        super.reloadData();
        onRefresh();
    }

    /**
     * 添加headerView
     */
    private void addHeaderView() {
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.view_deposit_history_header, null);
        tvTotalCount = (TextView) headerView.findViewById(R.id.tv_total_count);
        lvRecord.addHeaderView(headerView, null, false);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        adapter = new ProductInvestRecordListAdapter(getActivity(), records);
        lvRecord.setAdapter(adapter);
        lvRecord.setOnLoadMoreListener(this);
    }

    /**
     * 添加footerView
     */
    private void addFooterView() {
        if (footerView == null) {
            footerView = LayoutInflater.from(getActivity()).inflate(R.layout.view_deposit_history_footer, null);
            lvRecord.addFooterView(footerView, null, false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.removeFragment();
    }
}
