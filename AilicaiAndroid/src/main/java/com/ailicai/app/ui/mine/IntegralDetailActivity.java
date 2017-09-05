package com.ailicai.app.ui.mine;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.model.bean.IntegralModel;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.widget.bottomrefreshlistview.BottomRefreshListView;
import com.ailicai.app.widget.bottomrefreshlistview.BottomRefreshListViewCallbacks;
import com.ailicai.app.widget.bottomrefreshlistview.BottomRefreshListViewScrollState;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Gerry on 2017/7/18.
 */

public class IntegralDetailActivity extends BaseBindActivity implements SwipeRefreshLayout.OnRefreshListener,
        BottomRefreshListViewCallbacks, BottomRefreshListView.OnLoadMoreListener {

    public List<IntegralModel> integralRecordList = ObjectUtil.newArrayList();
    public IntegralDetailListAdapter adapter;
    @Bind(R.id.reward_record_list)
    BottomRefreshListView listView;
    @Bind(R.id.swipe)
    SwipeRefreshLayout mSwipeLayout;

    @Override
    public int getLayout() {
        return R.layout.integral_detail_activity;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.main_red_color);

        listView.setScrollViewCallbacks(this);
        listView.setOnLoadMoreListener(this);

        adapter = new IntegralDetailListAdapter(this, integralRecordList);
        listView.setAdapter(adapter);
        //test
        setIntegralData();
    }

    public void setIntegralData() {
        for (int i = 0; i < 10; i++) {
            IntegralModel model = new IntegralModel();
            model.setIntegralTitle("装备消耗");
            model.setIntegralTime("2017-08-28");
            model.setIntegralNum("100");
            integralRecordList.add(model);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(BottomRefreshListViewScrollState scrollState) {

    }

    @Override
    public void onLoadMore() {

    }
}
