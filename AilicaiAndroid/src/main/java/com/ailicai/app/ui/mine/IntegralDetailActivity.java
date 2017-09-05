package com.ailicai.app.ui.mine;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.IntegralRecordRequest;
import com.ailicai.app.model.response.IntegralRecordResponse;
import com.ailicai.app.model.response.ScoreDetailResponse;
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

    public List<ScoreDetailResponse> integralRecordList = ObjectUtil.newArrayList();
    public List<ScoreDetailResponse> integralRecordListCallBack = ObjectUtil.newArrayList();
    public IntegralDetailListAdapter adapter;
    @Bind(R.id.reward_record_list)
    BottomRefreshListView listView;
    @Bind(R.id.swipe)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.score)
    TextView score;

    private int offSet = 0;//数据偏移量(请求记录数之和)
    private int pageSize = 10;//每页记录数
    private int total;// 数据总笔数

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

        //会员当前积分
        score.setText("600");

        getIntegralRecord();
    }


    @Override
    public void onRefresh() {
        loadData(false);
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
        if (integralRecordListCallBack.size() < pageSize || integralRecordList.size() >= total) {
            listView.onAllLoaded();
            listView.setPromptText("没有更多记录");
            if (listView.getCount() == 1) {
                listView.setPromptText("");
            }
        } else {
            listView.setLoadingText("数据加载中");
            loadData(true);
        }
    }

    /**
     * 数据请求入口
     *
     * @param loadMore
     */
    public void loadData(boolean loadMore) {
        if (!loadMore) {
            offSet = 0;
            integralRecordList.clear();
            integralRecordListCallBack.clear();
            listView.resetAll();
            //listView.smoothScrollToPosition(0);
        } else {
            offSet = integralRecordList.size();
        }
        getIntegralRecord();
    }

    /**
     * 请求积分明细记录
     */
    public void getIntegralRecord() {
        IntegralRecordRequest request = new IntegralRecordRequest();
        request.setPageSize(pageSize);
        request.setOffset(offSet);
        ServiceSender.exec(this, request, new IwjwRespListener<IntegralRecordResponse>(this) {

            @Override
            public void onStart() {
            }

            @Override
            public void onJsonSuccess(IntegralRecordResponse jsonObject) {
                total = jsonObject.getTotal();
                integralRecordListCallBack = (jsonObject.getRows() != null) ? jsonObject.getRows() : ObjectUtil.<ScoreDetailResponse>newArrayList();
                setIntegralRecordData(integralRecordListCallBack);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                ToastUtil.showInCenter(errorInfo);
                setIntegralRecordData(ObjectUtil.<ScoreDetailResponse>newArrayList());
            }
        });
    }

    public void setIntegralRecordData(List<ScoreDetailResponse> integralRecordListTemp) {
        listView.onLoadMoreComplete();
        mSwipeLayout.setRefreshing(false);
        if (integralRecordListTemp != null) {
            integralRecordList.addAll(integralRecordListTemp);
        }
        adapter.notifyDataSetChanged();
        if ((integralRecordListTemp.size() < pageSize || integralRecordList.size() >= total) && adapter.getCount() != 0) {
            listView.onAllLoaded();
            listView.setPromptText("没有更多记录");
            if (adapter.getCount() == 1) {
                listView.setPromptText("");
            }
        }
    }
}
