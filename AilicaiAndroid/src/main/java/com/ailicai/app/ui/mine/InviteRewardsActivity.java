package com.ailicai.app.ui.mine;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
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
import com.ailicai.app.model.bean.InviteRewards;
import com.ailicai.app.model.request.InviteRewardRequest;
import com.ailicai.app.model.response.InviteRewardResponse;
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

public class InviteRewardsActivity extends BaseBindActivity implements BottomRefreshListViewCallbacks {
    public SpannableUtil spanUtil;
    @Bind(R.id.content_view)
    FrameLayout mAllView;
    @Bind(R.id.reward_top_head)
    LinearLayout rewardTopHead;
    @Bind(R.id.tabs_head)
    LinearLayout tabsHead;
    @Bind(R.id.rewards_list)
    BottomRefreshListView mRewardsList;
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

    @Override
    public int getLayout() {
        return R.layout.invite_rewards_activity;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        spanUtil = new SpannableUtil(mContext);
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

        setTabsCheckAction();

        InviteRewardsListAdapter listAdapter = new InviteRewardsListAdapter(this, getInviteRewards());
        mRewardsList.setAdapter(listAdapter);

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

    public void setTabsCheckAction() {
        aroundHouseGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.plot_sale_house_rb:
                        setThumbAnimation(thumb, 0, -DeviceUtil.getScreenWidth() / 2);
                        //setHouseListData(true, response, onClickListener);
                        break;
                    case R.id.plot_rent_house_rb:
                        setThumbAnimation(thumb, -DeviceUtil.getScreenWidth() / 2, 0);
                        //setHouseListData(false, response, onClickListener);
                        break;
                }
            }
        });
        aroundHouseGroup.check(aroundHouseGroup.getChildAt(0).getId());
    }

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

    public void setTopDataInfo(InviteRewardResponse jsonObject) {
        alreadyAward.setText(jsonObject.getAlreadyAward());
        SpannableStringBuilder builderWaittingAward = spanUtil.getSpannableString(jsonObject.getWaittingAward(), " 元", R.style.text_18_f75a14, R.style.text_12_f75a14);
        waittingAward.setText(builderWaittingAward);
        SpannableStringBuilder builderInviteNumber = spanUtil.getSpannableString(jsonObject.getInviteNumber() + "", " 人", R.style.text_18_212121, R.style.text_12_212121);
        inviteNumber.setText(builderInviteNumber);
        SpannableStringBuilder builderInvestAmount = spanUtil.getSpannableString(jsonObject.getInvestAmount() + "", " 元", R.style.text_18_212121, R.style.text_12_212121);
        investAmount.setText(builderInvestAmount);
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
