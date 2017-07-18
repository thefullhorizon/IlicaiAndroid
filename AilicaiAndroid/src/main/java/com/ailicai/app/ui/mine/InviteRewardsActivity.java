package com.ailicai.app.ui.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.model.bean.InviteRewards;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.widget.bottomrefreshlistview.BottomRefreshListView;
import com.ailicai.app.widget.bottomrefreshlistview.BottomRefreshListViewCallbacks;
import com.ailicai.app.widget.bottomrefreshlistview.BottomRefreshListViewScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Gerry on 2017/7/18.
 */

public class InviteRewardsActivity extends BaseBindActivity implements BottomRefreshListViewCallbacks {
    @Bind(R.id.content_view)
    FrameLayout mAllView;
    @Bind(R.id.reward_top_head)
    LinearLayout rewardTopHead;
    @Bind(R.id.tabs_head)
    LinearLayout tabsHead;
    @Bind(R.id.rewards_list)
    BottomRefreshListView mRewardsList;

    @Override
    public int getLayout() {
        return R.layout.invite_rewards_activity;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        CommonUtil.addAnimForView(mAllView);
        mRewardsList.setScrollViewCallbacks(this);
        rewardTopHead.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayout listHead = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.rewards_list_head, null);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, rewardTopHead.getMeasuredHeight());
                listHead.setLayoutParams(lp);
                mRewardsList.addHeaderView(listHead);
            }
        }, 100);

        InviteRewardsListAdapter listAdapter = new InviteRewardsListAdapter(this, getInviteRewards());
        mRewardsList.setAdapter(listAdapter);

    }

    public List<InviteRewards> getInviteRewards() {
        List<InviteRewards> inviteRewards = ObjectUtil.newArrayList();
        for (int i = 0; i < 10; i++) {
            InviteRewards mInviteRewards = new InviteRewards();
            mInviteRewards.setMobile("138******79");
            mInviteRewards.setInviteTime("2017.07.06");
            mInviteRewards.setStatus(0);
            inviteRewards.add(mInviteRewards);
        }
        return inviteRewards;
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
}
