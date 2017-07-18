package com.ailicai.app.ui.view.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.ExpiredTiyanbaoListRequest;
import com.ailicai.app.model.response.ExpiredTiyanbaoListResponse;
import com.ailicai.app.ui.asset.CapitalListProductDetailActivity;
import com.ailicai.app.ui.asset.ExpiredTiyanbaoListAdapter;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.login.AccountInfo;
import com.ailicai.app.widget.BottomRefreshListView;
import com.alibaba.fastjson.JSON;
import com.huoqiu.framework.util.CheckDoubleClick;

import java.util.HashMap;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/8/15.
 */
public class ExpiredTiyanbaoListActivity extends BaseBindActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        ExpiredTiyanbaoListAdapter.Event,
        View.OnClickListener,
        BottomRefreshListView.OnLoadMoreListener{

    @Bind(R.id.tiyanbao_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.tiyanbao_list)
    BottomRefreshListView mListView;

    TextView tiyanAmount;//体验金
    TextView mIncome;//累计收益
    View mIncomeLayout;
    ExpiredTiyanbaoListAdapter mAdapter;
    private int offSet;
    private int total;
    @Override
    public int getLayout() {
        return R.layout.expired_tiyanbao_list;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_red_color);
        initListHeader();
        mAdapter = new ExpiredTiyanbaoListAdapter(this);
        mAdapter.setEvent(this);
        mListView.setAdapter(mAdapter);
        mListView.onLoadMoreComplete();
        mListView.setOnLoadMoreListener(this);
        getServerData(true,true,offSet);

    }

    private void getServerData(final boolean showLoading, final boolean refresh,int offSet){
        ExpiredTiyanbaoListRequest request = new ExpiredTiyanbaoListRequest();
        request.setPageSize(10);
        request.setOffset(offSet);
        ServiceSender.exec(this, request, new IwjwRespListener<ExpiredTiyanbaoListResponse>() {
            @Override
            public void onStart() {
                if (showLoading){
                    showLoadView();
                }
            }

            @Override
            public void onJsonSuccess(ExpiredTiyanbaoListResponse response) {
                if (response.getErrorCode() == 0){
                    handleServerData(response,refresh);
                }else {
                    onFailInfo(response.getMessage());
                }


            }

            @Override
            public void onFailInfo(String errorInfo) {
                ToastUtil.showInCenter(errorInfo);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (showLoading){
                    showContentView();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private void handleServerData(ExpiredTiyanbaoListResponse response,boolean refresh){
        if (response == null)return;

        tiyanAmount.setText(response.getAmount());
        mIncome.setText(response.getIncome());
        total = response.getTotal();
        mAdapter.addDataSource(response.getTiyanbaoList(),refresh);
        if (total == mAdapter.getProductOffset()){
            mListView.onAllLoaded();
            mListView.setPromptText("数据已全部加载");
        }
        mAdapter.notifyDataSetChanged();



    }

    private void initListHeader(){
        if (mListView.getHeaderViewsCount() > 0) return;
        View headerView = getLayoutInflater().inflate(R.layout.expired_tiyanbao_list_header,null);
        tiyanAmount =(TextView) headerView.findViewById(R.id.tiyan_amount);
        mIncome =(TextView) headerView.findViewById(R.id.tiyan_income);
        mIncomeLayout = headerView.findViewById(R.id.tiyanbao_income_layout);
        mIncomeLayout.setOnClickListener(this);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        mListView.addHeaderView(headerView,null,false);
    }

    @Override
    public void onRefresh() {
        offSet = 0;
        getServerData(false,true,offSet);

    }

    @Override
    public void toTiyanbaoProductDetail(long couponId) {
        Intent intent = new Intent(this, CapitalListProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(CapitalListProductDetailActivity.TIYANBAO_ID,couponId);
        bundle.putBoolean(CapitalListProductDetailActivity.IS_TIYANBAO, true);
        bundle.putInt(CapitalListProductDetailActivity.TYPE, 3);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        switch (v.getId()){
            case R.id.tiyanbao_income_layout:
                toRegularIncome();
                break;
        }
    }

    private void toRegularIncome() {
        if (AccountInfo.isOpenAccount()) {
            HashMap map = new HashMap();
            map.put("name","ljsy");
            map.put("action","click");
            EventLog.upEventLog("806", JSON.toJSONString(map));
            Intent intent = new Intent(this, IncomeDetailActivity.class);
            intent.putExtra(IncomeDetailActivity.TYPE, IncomeDetailActivity.TRY);
            startActivity(intent);
        }

    }

    @Override
    public void onLoadMore() {
        int productOffset = mAdapter.getProductOffset();
        if (total > productOffset){
            mListView.setLoadingText("更多数据加载中...");
            getServerData(false,false,productOffset);
        }else {
            mListView.onAllLoaded();
            mListView.setPromptText("数据已全部加载");
        }

    }
}
