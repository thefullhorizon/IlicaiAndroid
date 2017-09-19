package com.ailicai.app.ui.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.model.request.IncomeDetailRequest;
import com.ailicai.app.model.response.IncomeDetailResponse;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.ui.view.income.IncomeAdapter;
import com.ailicai.app.widget.bottomrefreshlistview.BottomRefreshListView;

import butterknife.Bind;

/**
 * Created by David on 16/1/4
 * Modified by Owen on 16/8/10
 * 钱包收益
 */
public class IncomeDetailWalletFragment extends BaseBindFragment implements SwipeRefreshLayout.OnRefreshListener, BottomRefreshListView.OnLoadMoreListener{

    @Bind(R.id.income_list)
    BottomRefreshListView mListView;

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;


    View mHeader;
    TextView mTotalIncome;

    IncomeAdapter mAdapter;

    public static final String INCOME_TYPE = "income_type";
    public static final int INCOME_ALL = 0;
    public static final int INCOME_CURRENT_TREASURE = 2;

    // 这里的数字是相对于网络请求接口，不要跟UI的上的位置搞混
    private int position = 0;

    /**
     * 请求偏移量.
     */
    int offset;
    /**
     * 服务端返回.
     */
    boolean next;
    /**
     * 第一次加载.
     */
    boolean firstLoad = true;
    /**
     * 请求标识，以最新请求为准，用于拦截不希望的请求返回.
     */
    long session;

    @Override
    public int getLayout() {
        return R.layout.fragment_income;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        mSwipeLayout.setOnRefreshListener(this);
        mListView.setOnLoadMoreListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.main_red_color);

        mHeader = LayoutInflater.from(getActivity()).inflate(R.layout.income_header, null);
        mListView.addHeaderView(mHeader);

        mHeader.setVisibility(View.GONE);

        mTotalIncome = (TextView) mListView.findViewById(R.id.income_header_total);

        mAdapter = new IncomeAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        //0：全部(网贷+活期宝) 1：活期宝 2：定期宝 3体验宝
        if (getArguments().getInt(INCOME_TYPE) == INCOME_ALL) {
            position = 0;
        } else if (getArguments().getInt(INCOME_TYPE) == INCOME_CURRENT_TREASURE) {
            position = 1;
        }
        onRefresh();
    }


    @Override
    public void onLoadMore() {
        if (mSwipeLayout.isRefreshing()) return;

        if (next) {
            mListView.setLoadingText("数据正在加载中");

            offset = mAdapter.getCount();
            loadData(false);
        } else {
            mListView.onAllLoaded();
            mListView.setPromptText("数据已全部加载");
        }
    }

    @Override
    public void onRefresh() {
        mSwipeLayout.setRefreshing(true);
        offset = 0;
        next = false;
        mListView.resetAll();
        loadData(true);
    }

    @Override
    public void reloadData() {
        onRefresh();
    }

    void loadData(boolean refresh) {
        session = System.currentTimeMillis();
        final long time = session;

        IncomeDetailRequest request = new IncomeDetailRequest();
        request.setDepositType(position);
        request.setOffSet(offset);
        request.setPageSize(20);
        ServiceSender.exec(this, request, new IncomeCallback(firstLoad, refresh, time));
    }


    void handleResponse(IncomeDetailResponse response, boolean refresh, long session) {
        if (refresh) mSwipeLayout.setRefreshing(false);
        else mListView.onLoadMoreComplete();

        if (response == null) return;
        if (this.session != session) return;

        mAdapter.addData(response.getIncomeDetailList(), refresh);
        mAdapter.notifyDataSetChanged();

        firstLoad = false;
        next = mAdapter.getCount() < response.getTotal();

        if (response.getTotal() > 0) {
            mHeader.setVisibility(View.VISIBLE);
            if (refresh) mTotalIncome.setText("+" + CommonUtil.amountWithTwoAfterPoint(response.getTotalIncome()));
        } else {
            mHeader.setVisibility(View.GONE);
            View noDataView = View.inflate(getActivity(), R.layout.list_nodataview_layout, null);
            TextView noDataText = (TextView) noDataView.findViewById(R.id.nodata_text);
            noDataText.setText("当前没有收益");
            showNoDataView(noDataView);
        }


    }

    static class IncomeCallback extends IwjwRespListener<IncomeDetailResponse> {

        boolean _first;
        boolean _refresh;
        long _session;


        public IncomeCallback(boolean first, boolean refresh, long session) {
            super();
            this._first = first;
            this._refresh = refresh;
            this._session = session;
        }


        @Override
        public void onStart() {
            IncomeDetailWalletFragment fragment = getWRFragment();
            if (fragment == null) return;

            if (_first) fragment.showLoadView();
        }

        @Override
        public void onJsonSuccess(IncomeDetailResponse response) {
            IncomeDetailWalletFragment fragment = getWRFragment();
            if (fragment == null) return;

            fragment.showContentView();
            fragment.handleResponse(response, _refresh, _session);
        }

        @Override
        public void onFailInfo(String error) {
            IncomeDetailWalletFragment fragment = getWRFragment();
            if (fragment == null) return;

            fragment.handleResponse(null, _refresh, _session);
            fragment.showErrorView(error);
        }

    }
}
