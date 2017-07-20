package com.ailicai.app.ui.reserve;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.SpannableUtil;
import com.ailicai.app.model.bean.ReserveListBean;
import com.ailicai.app.model.request.ReserveListRequest;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.widget.bottomrefreshlistview.BottomRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Owen on 2016/5/25
 * 预约购买产品列表
 */
public class ReserveListActivity extends BaseBindActivity implements SwipeRefreshLayout.OnRefreshListener, BottomRefreshListView.OnLoadMoreListener, ReserveListInterface {

    @Bind(R.id.srl)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.listView)
    BottomRefreshListView listView;
    @Bind(R.id.tvHint)
    TextView tvHint;
    @Bind(R.id.tvHint2)
    TextView tvHint2;
    @Bind(R.id.tvNoData)
    TextView tvNoData;

    private List<ReserveListBean> listData = new ArrayList<>();
    public ReserveListAdapter adapter;
    private ReserveListPresenter presenter;

    private int page = 0;
    public String productId;
    public int horizon;

    @Override
    public int getLayout() {
        return R.layout.activity_reserve_list;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initIntentValue();
        initListView();
        initPresenter();
    }

    private void initIntentValue() {
        productId = getIntent().getStringExtra("productId");
        horizon = getIntent().getIntExtra("horizon", 0);
    }

    private void initListView() {
        swipeRefreshLayout.setColorSchemeResources(R.color.main_red_color);
        swipeRefreshLayout.setOnRefreshListener(this);
        listView.setOnLoadMoreListener(this);
        adapter = new ReserveListAdapter(this, listData);
        listView.setAdapter(adapter);
        setScrollListener();
    }

    private void initPresenter() {
        presenter = new ReserveListPresenter(this, this);
        presenter.requestForReserveListData();
    }

    @Override
    public void onRefresh() {
        page = 0;
        presenter.requestForReserveListData();
    }

    @Override
    public void reloadData() {
        super.reloadData();
        presenter.requestForReserveListData();
    }

    @Override
    public void onLoadMore() {
        listView.setLoadingText("加载中");
        presenter.requestForReserveListData();
    }


    @Override
    public int getPage() {
        return page;
    }


    @Override
    public ReserveListRequest getReserveListRequest() {
        return null;
    }

    public void setTime(String interestTimeStr) {
        SpannableUtil mSpannableUtil = new SpannableUtil(this);
        SpannableStringBuilder count = mSpannableUtil.getSpannableString("申购结果将在", interestTimeStr,"通知，先到先得，卖完即止。", R.style
                .text_14_757575, R.style.text_14_price_unit, R.style.text_14_757575);
        tvHint.setText("预约成功后，爱理财将根据您选定的投资期限，按先后顺序，自动分散申购以下产品。");
        tvHint2.setText(count);
    }

    @Override
    public void setListData(List<ReserveListBean> list) {
        if (page == 0) {
            listData.clear();
        }
        listData.addAll(list);
        if (list.size() > 0) {
            // 是否小于一页的最大条数（是：加载完数据；否：继续请求下一页++）
            if (list.size() < 10) {
                loadedAll();
            } else {
                page = listData.size();
                listView.resetAll();
            }
        } else {
            // 是否是第一页（是：暂无数据；否加载完所有数据）
            if (page == 0) {
                noData();
            } else {
                loadedAll();
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 无数据
     */
    public void noData() {
        listView.setVisibility(View.GONE);
        tvNoData.setVisibility(View.VISIBLE);
        onRefreshFinished();
    }

    public void loadedAll() {
        listView.onAllLoaded();
        listView.setPromptText("");
    }

    public void onRefreshFinished() {
        swipeRefreshLayout.setRefreshing(false);
    }


    private void setScrollListener() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0 && getScrollY() == 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
    }

    public int getScrollY() {
        View c = listView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }
}
