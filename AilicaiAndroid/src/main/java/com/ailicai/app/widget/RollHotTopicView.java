package com.ailicai.app.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ailicai.app.R;
import com.ailicai.app.model.bean.AilicaiNotice;
import com.ailicai.app.ui.view.RegularFinanceDetailH5Activity;

import java.util.List;


/**
 * 转入转出热点
 * Created by nanshan on 2017/6/20.
 */
public class RollHotTopicView extends LinearLayout implements View.OnClickListener {
    private FragmentActivity mActivity;
    private ViewFlipper viewFlipper;

    public RollHotTopicView(Context context) {
        super(context);
        initView();
    }

    public RollHotTopicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RollHotTopicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RollHotTopicView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.index_hot_topic_view_roll, this);
        viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
    }

    public void updateView4AlicaiData(FragmentActivity activity, List<AilicaiNotice> topicDataList) {
        this.mActivity = activity;
        viewFlipper.removeAllViews();
        for (final AilicaiNotice banner : topicDataList) {
            View topicViewItem = View.inflate(getContext(), R.layout.index_hot_topic_item_roll, null);
            TextView textTopicTitle = (TextView) topicViewItem.findViewById(R.id.text_topic_title);
            textTopicTitle.setPadding(0,2,2,0);
            textTopicTitle.setText(banner.getNoticeTitle());
            RelativeLayout topicViewItemRoot = (RelativeLayout) topicViewItem.findViewById(R.id.hot_topic_item_root);
            topicViewItemRoot.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, RegularFinanceDetailH5Activity.class);
                    intent.putExtra(RegularFinanceDetailH5Activity.EXTRA_URL, banner.getNoticeUrl());
                    mActivity.startActivity(intent);
//                    Map logMap = new HashMap();
//                    logMap.put("id", banner.getNoticeId());
//                    logMap.put("ct", CityManager.getInstance().getCurrentCity().getCityId());
//                    EventLog.upEventLog("661", JSON.toJSONString(logMap));
                }
            });
            viewFlipper.addView(topicViewItem);
        }
        if (topicDataList != null && topicDataList.size() > 1) {
            // 至少大于1个才做动画
            viewFlipper.setInAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.topic_push_up_in));
            viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.topic_push_up_out));
            viewFlipper.startFlipping();
        } else {
            viewFlipper.stopFlipping();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.news_item_root:
//                break;
        }
    }
}
