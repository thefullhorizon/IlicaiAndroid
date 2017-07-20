package com.ailicai.app.ui.mine;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.SpannableUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.bean.InviteRecord;
import com.ailicai.app.model.bean.RewardRecord;
import com.ailicai.app.model.request.InviteRecordRequest;
import com.ailicai.app.model.request.InviteRewardRequest;
import com.ailicai.app.model.request.RewardRecordRequest;
import com.ailicai.app.model.response.InviteRecordResponse;
import com.ailicai.app.model.response.InviteRewardResponse;
import com.ailicai.app.model.response.RewardRecordResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.widget.bottomrefreshlistview.BottomRefreshListView;
import com.ailicai.app.widget.bottomrefreshlistview.BottomRefreshListViewCallbacks;
import com.ailicai.app.widget.bottomrefreshlistview.BottomRefreshListViewScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Gerry on 2017/7/18.
 */

public class InviteRewardsActivity extends BaseBindActivity implements BottomRefreshListViewCallbacks, BottomRefreshListView.OnLoadMoreListener {
    public SpannableUtil spanUtil;
    @Bind(R.id.content_view)
    FrameLayout mAllView;
    @Bind(R.id.reward_top_head)
    LinearLayout rewardTopHead;
    @Bind(R.id.tabs_head)
    LinearLayout tabsHead;
    @Bind(R.id.rewards_list)
    BottomRefreshListView mSwipeListView;
    @Bind(R.id.alreadyAward)
    TextView alreadyAward;
    @Bind(R.id.waittingAward)
    TextView waittingAward;
    @Bind(R.id.inviteNumber)
    TextView inviteNumber;
    @Bind(R.id.investAmount)
    TextView investAmount;
    @Bind(R.id.plot_house_list_group)
    RadioGroup aroundHouseGroup;
    @Bind(R.id.house_thumb_view)
    View thumb;
    @Bind(R.id.tt1)
    TextView tt1;
    @Bind(R.id.tt2)
    TextView tt2;
    @Bind(R.id.tt3)
    TextView tt3;

    List<InviteRecord> inviteRecordList = ObjectUtil.newArrayList();
    List<InviteRecord> inviteRecordListCallBack = ObjectUtil.newArrayList();
    List<RewardRecord> rewardRecordList = ObjectUtil.newArrayList();
    List<RewardRecord> rewardRecordListCallBack = ObjectUtil.newArrayList();
    private int offSet = 0;//数据偏移量(请求记录数之和)
    private int pageSize = 10;//每页记录数

    @Override
    public int getLayout() {
        return R.layout.invite_rewards_activity;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        spanUtil = new SpannableUtil(mContext);
        CommonUtil.addAnimForView(mAllView);
        mSwipeListView.setScrollViewCallbacks(this);
        mSwipeListView.setOnLoadMoreListener(this);
        rewardTopHead.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayout listHead = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.rewards_list_head, null);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, rewardTopHead.getMeasuredHeight());
                listHead.setLayoutParams(lp);
                mSwipeListView.addHeaderView(listHead);
            }
        }, 100);

        setTabsCheckAction();

        getInviteReward();
    }

    public void setThumbAnimation(View tView, int fromX, int toX) {
        TranslateAnimation animation = new TranslateAnimation(fromX, toX, 0, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        tView.setAnimation(animation);
        animation.startNow();
        //tView.postInvalidate();
        tView.setVisibility(View.GONE);
        tView.setVisibility(View.VISIBLE);
    }

    public void changeFirstTabsName() {
        tt1.setText("邀请好友");
        tt2.setText("受邀时间");
        tt3.setText("投资状态");
    }

    public void changeSecondTabsName() {
        tt1.setText("奖励时间");
        tt2.setText("奖励状态");
        tt3.setText("奖励金额(元)");
    }

    public void setTabsCheckAction() {
        aroundHouseGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.invite_record_rb:
                        changeFirstTabsName();
                        setThumbAnimation(thumb, 0, -DeviceUtil.getScreenWidth() / 2);
                        loadData(false);
                        break;
                    case R.id.reward_record_rb:
                        changeSecondTabsName();
                        setThumbAnimation(thumb, -DeviceUtil.getScreenWidth() / 2, 0);
                        loadData(false);
                        break;
                }
            }
        });
        aroundHouseGroup.check(aroundHouseGroup.getChildAt(0).getId());
    }


    /**
     * 顶部数据
     */
    public void getInviteReward() {
        InviteRewardRequest request = new InviteRewardRequest();
        request.setUserid(UserInfo.getInstance().getUserId());
        ServiceSender.exec(this, request, new IwjwRespListener<InviteRewardResponse>(this) {

            @Override
            public void onStart() {
                //showLoadTranstView();
            }

            @Override
            public void onJsonSuccess(InviteRewardResponse jsonObject) {
                //showContentView();
                setTopDataInfo(jsonObject);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                //showContentView();
                ToastUtil.showInCenter(errorInfo);
            }
        });
    }

    /**
     * 邀请记录
     */
    public void getInviteRecord() {
        InviteRecordRequest request = new InviteRecordRequest();
        request.setUserid(UserInfo.getInstance().getUserId());
        request.setStartIndex(offSet);
        request.setPageSize(pageSize);
        ServiceSender.exec(this, request, new IwjwRespListener<InviteRecordResponse>(this) {

            @Override
            public void onStart() {
                //showLoadTranstView();
            }

            @Override
            public void onJsonSuccess(InviteRecordResponse jsonObject) {
                //showContentView();
                inviteRecordListCallBack = (jsonObject.getInviteRecordList() != null) ? jsonObject.getInviteRecordList() : ObjectUtil.<InviteRecord>newArrayList();
                setInviteRecordData(inviteRecordListCallBack);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                //showContentView();
                ToastUtil.showInCenter(errorInfo);
                setInviteRecordData(ObjectUtil.<InviteRecord>newArrayList());
            }
        });
    }

    /**
     * 设置邀请记录数据
     */
    public void setInviteRecordData(List<InviteRecord> InviteRecordListTemp) {
        inviteRecordList.addAll(InviteRecordListTemp);
        InviteRewardsListAdapter listAdapter = new InviteRewardsListAdapter(this, inviteRecordList);
        mSwipeListView.setAdapter(listAdapter);
    }

    /**
     * 奖励记录
     */
    public void getRewardRecord() {
        RewardRecordRequest request = new RewardRecordRequest();
        request.setUserid(UserInfo.getInstance().getUserId());
        request.setStartIndex(offSet);
        request.setPageSize(pageSize);
        ServiceSender.exec(this, request, new IwjwRespListener<RewardRecordResponse>(this) {

            @Override
            public void onStart() {
                //showLoadTranstView();
            }

            @Override
            public void onJsonSuccess(RewardRecordResponse jsonObject) {
                //showContentView();
                rewardRecordListCallBack = (jsonObject.getRewardRecordList() != null) ? jsonObject.getRewardRecordList() : ObjectUtil.<RewardRecord>newArrayList();
                setRewardRecordData(rewardRecordListCallBack);

            }

            @Override
            public void onFailInfo(String errorInfo) {
                //showContentView();
                ToastUtil.showInCenter(errorInfo);
                setRewardRecordData(ObjectUtil.<RewardRecord>newArrayList());
            }
        });
    }

    /**
     * 奖励记录数据
     */
    public void setRewardRecordData(List<RewardRecord> rewardRecordListTemp) {
        rewardRecordList.addAll(rewardRecordListTemp);
        RewardRecordListAdapter listAdapter = new RewardRecordListAdapter(this, rewardRecordList);
        mSwipeListView.setAdapter(listAdapter);
    }


    public void setTopDataInfo(InviteRewardResponse jsonObject) {
        alreadyAward.setText(jsonObject.getAlreadyAward());
        SpannableStringBuilder builderWaittingAward = spanUtil.getSpannableString(jsonObject.getWaittingAward(), " 元", R.style.text_18_f75a14, R.style.text_12_f75a14);
        waittingAward.setText(builderWaittingAward);
        SpannableStringBuilder builderInviteNumber = spanUtil.getSpannableString(jsonObject.getInviteNumber() + "", " 人", R.style.text_18_212121, R.style.text_12_212121);
        inviteNumber.setText(builderInviteNumber);
        SpannableStringBuilder builderInvestAmount = spanUtil.getSpannableString(jsonObject.getInvestAmount() + "", " 元", R.style.text_18_212121, R.style.text_12_212121);
        investAmount.setText(builderInvestAmount);
    }

    public void onScrollDisplay(int scrollY) {
        int minOverlayTransitionY = tabsHead.getMeasuredHeight() - rewardTopHead.getMeasuredHeight();
        rewardTopHead.setTranslationY(ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        onScrollDisplay(scrollY);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(BottomRefreshListViewScrollState scrollState) {

    }

    @Override
    public void onLoadMore() {
        //上拉刷新加载数据
        boolean loadMore = false;
        switch (aroundHouseGroup.getCheckedRadioButtonId()) {
            case R.id.invite_record_rb:
                if (inviteRecordListCallBack.size() < pageSize) {
                    //if (!hasNextPage || totalNum <= mDatas.size() || mDatas.size() < PAGE_SIZE) {
                    //if (!hasNextPage) {
                    loadMore = false;
                } else {
                    loadMore = true;
                }
                break;
            case R.id.reward_record_rb:
                if (rewardRecordListCallBack.size() < pageSize) {
                    //if (!hasNextPage || totalNum <= mDatas.size() || mDatas.size() < PAGE_SIZE) {
                    //if (!hasNextPage) {
                    loadMore = false;
                } else {
                    loadMore = true;
                }
                break;
        }

        if (loadMore) {
            mSwipeListView.setLoadingText("数据加载中");
            loadData(true);
        } else {
            mSwipeListView.onAllLoaded();
            mSwipeListView.setPromptText("没有更多数据");
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
            inviteRecordList.clear();
            rewardRecordList.clear();
            mSwipeListView.resetAll();
            mSwipeListView.smoothScrollToPosition(0);
        } else {
            switch (aroundHouseGroup.getCheckedRadioButtonId()) {
                case R.id.invite_record_rb:
                    offSet = inviteRecordList.size();
                    break;
                case R.id.reward_record_rb:
                    offSet = rewardRecordList.size();
                    break;
            }

        }

        switch (aroundHouseGroup.getCheckedRadioButtonId()) {
            case R.id.invite_record_rb:
                getInviteRecord();
                break;
            case R.id.reward_record_rb:
                getRewardRecord();
                break;
        }
    }

}
