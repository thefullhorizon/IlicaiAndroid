package com.ailicai.app.ui.message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.push.model.PushMessage;
import com.ailicai.app.common.utils.HandlerUtil;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.eventbus.LoginEvent;
import com.ailicai.app.eventbus.NewNotifMsgEvent;
import com.ailicai.app.eventbus.RefreshPushEvent;
import com.ailicai.app.message.NoticeResponse;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.message.presenter.MessagePresenter;
import com.ailicai.app.widget.CircleImageView;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.MessageTypeItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liyanan on 16/7/5.
 */
public class MessageFragment extends BaseBindFragment{

    public static boolean indexSwitchNeedUpdateMessage = false;

    @Bind(R.id.lv_message_list)
    ListView lvMessageList;
    @Bind(R.id.message_top_title)
    IWTopTitleView topView;
    @Bind(R.id.mtiv_activity)
    MessageTypeItemView mMtivActivity;
    @Bind(R.id.mtiv_information)
    MessageTypeItemView mMtivInfomation;
    @Bind(R.id.mtiv_remind)
    MessageTypeItemView mMtivRemind;

    boolean isConsumingMsg = false;
    private TextView tvActivityContent;//最新一条活动内容
    private TextView tvActivityNum;//未读活动数字
    private TextView tvActivityTime;//最后一条活动时间
    private TextView tvNoticeContent;//最新一条通知内容
    private TextView tvNoticeNum;//未读通知数字
    private TextView tvNoticeTime;//最后一条通知时间
    private TextView tvReminderContent;//最新一条提醒内容
    private TextView tvReminderNum;//未读提醒数字
    private TextView tvRemindTime;//最后一条提醒时间
    private MessagePresenter presenter;

    @Override
    public int getLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        EventBus.getDefault().register(this);
//        initActivityView();
//        initNoticeView();
//        initRemindView();
        presenter = MessagePresenter.getPresenter();
        topView.setIsShowLeftBtn(getArguments().getBoolean("showBack", false));
    }

    public void showLoginViewOrUpdateConversation(boolean forceUpdate) {
        if (getWRActivity() == null) {
            return;
        }
        boolean hasLogin = UserInfo.getInstance().getLoginState() == UserInfo.LOGIN;
        if (!hasLogin) {
            View nuLogin = View.inflate(getWRActivity(), R.layout.msg_unlogin, null);
            showLoginView(nuLogin);
        } else {
            presenter.updateConversationWhenNeed(forceUpdate);
            presenter.requestNotice();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setFragment(this);
        showLoginViewOrUpdateConversation(true);
    }


    @Override
    public void reloadData() {
        super.reloadData();
        presenter.requestNotice();
    }

    /**
     * 请求成功
     *
     * @param response
     */

    public void requestSuccess(NoticeResponse response) {
        showContentView();
        if (response != null) {
            refreshMsgView(response.getActivityTitle(), response.getActivityNum(), response.getActivityTime(),PushMessage.ACTIVITYTYPE);
            refreshMsgView(response.getNoticeTitle(), response.getNoticeNum(), response.getNoticeTime(),PushMessage.INFOTYPE);
            refreshMsgView(response.getActivityTitle(), response.getActivityNum(), response.getActivityTime(),PushMessage.REMINDTYPE);
        }
    }


    /**
     * 请求失败
     *
     * @param errorInfo
     */
    public void requestFail(String errorInfo) {
        showMyToast(errorInfo);
    }

    /**
     * 初始化活动view
     */
    /*private void initActivityView() {
        View noticeView = getActivity().getLayoutInflater().inflate(R.layout.list_item_message_list, null);
        CircleImageView civHead = (CircleImageView) noticeView.findViewById(R.id.civ_head);
        TextView tvName = (TextView) noticeView.findViewById(R.id.tv_name);
        tvActivityContent = (TextView) noticeView.findViewById(R.id.tv_content);
        tvActivityNum = (TextView) noticeView.findViewById(R.id.tv_num);
        tvActivityTime = (TextView) noticeView.findViewById(R.id.tv_time);
        civHead.setImageResource(R.drawable.message_activity);
        tvName.setText("活动");
        noticeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入活动页面
                Intent intent = new Intent();
                intent.setClass(getActivity(), BaseMessageListActivity.class);
                intent.putExtra(BaseMessageListActivity.MESSAGELISTTYPE,
                        PushMessage.ACTIVITYTYPE);
                startActivity(intent);
            }
        });
        lvMessageList.addHeaderView(noticeView, null, false);
    }*/

    @OnClick(R.id.mtiv_activity)
    void onActivityClick(){
        //进入活动页面
        BaseMessageListActivity.goActivity(getContext(),PushMessage.ACTIVITYTYPE);
    }

    /**
     * 更新活动数据
     *
     * @param content
     * @param num
     * @param time
     */
    /*private void updateActivityData(String content, int num, String time) {
        if (!TextUtils.isEmpty(content)) {
            tvActivityContent.setText(content);
        } else {
            tvActivityContent.setText("暂无活动");
        }
        if (num <= 0) {
            //小于0
            tvActivityNum.setVisibility(View.GONE);
        } else if (num > 0 && num < 10) {
            //1-9
            tvActivityNum.setVisibility(View.VISIBLE);
            tvActivityNum.setText(String.valueOf(num));
            tvActivityNum.setBackgroundResource(R.drawable.msg_new_ones);
        } else if (num > 9 && num < 100) {
            //10-99
            tvActivityNum.setVisibility(View.VISIBLE);
            tvActivityNum.setText(String.valueOf(num));
            tvActivityNum.setBackgroundResource(R.drawable.msg_new_tens);
        } else {
            //大于99
            tvActivityNum.setVisibility(View.VISIBLE);
            tvActivityNum.setText("99+");
            tvActivityNum.setBackgroundResource(R.drawable.msg_new_more);
        }
        tvActivityTime.setText(time);
    }*/

    /**
     * 初始化资讯view
     */
    /*private void initNoticeView() {
        View noticeView = getActivity().getLayoutInflater().inflate(R.layout.list_item_message_list, null);
        CircleImageView civHead = (CircleImageView) noticeView.findViewById(R.id.civ_head);
        TextView tvName = (TextView) noticeView.findViewById(R.id.tv_name);
        tvNoticeContent = (TextView) noticeView.findViewById(R.id.tv_content);
        tvNoticeNum = (TextView) noticeView.findViewById(R.id.tv_num);
        tvNoticeTime = (TextView) noticeView.findViewById(R.id.tv_time);
        civHead.setImageResource(R.drawable.message_information);
        tvName.setText("资讯");
        noticeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入通知页面
                Intent intent = new Intent();
                intent.setClass(getActivity(), BaseMessageListActivity.class);
                intent.putExtra(BaseMessageListActivity.MESSAGELISTTYPE,
                        PushMessage.INFOTYPE);
                startActivity(intent);
                //TODO 统计
                EventLog.upEventLog("160", "notice");
            }
        });
        lvMessageList.addHeaderView(noticeView, null, false);
    }*/

    @OnClick(R.id.mtiv_information)
    void onInformationClick(){
        //进入活动页面
        BaseMessageListActivity.goActivity(getContext(),PushMessage.INFOTYPE);
        //TODO 统计
        EventLog.upEventLog("160", "notice");
    }

    /**
     * 更新资讯数据
     *
     * @param content
     * @param num
     * @param time
     */
   /* private void updateNoticeData(String content, int num, String time) {
        if (!TextUtils.isEmpty(content)) {
            tvNoticeContent.setText(content);
        } else {
            tvNoticeContent.setText("暂无资讯");
        }
        if (num <= 0) {
            //小于0
            tvNoticeNum.setVisibility(View.GONE);
        } else if (num > 0 && num < 10) {
            //1-9
            tvNoticeNum.setVisibility(View.VISIBLE);
            tvNoticeNum.setText(String.valueOf(num));
            tvNoticeNum.setBackgroundResource(R.drawable.msg_new_ones);
        } else if (num > 9 && num < 100) {
            //10-99
            tvNoticeNum.setVisibility(View.VISIBLE);
            tvNoticeNum.setText(String.valueOf(num));
            tvNoticeNum.setBackgroundResource(R.drawable.msg_new_tens);
        } else {
            //大于99
            tvNoticeNum.setVisibility(View.VISIBLE);
            tvNoticeNum.setText("99+");
            tvNoticeNum.setBackgroundResource(R.drawable.msg_new_more);
        }
        tvNoticeTime.setText(time);
    }*/

    /**
     * 初始化提醒view
     */
    /*private void initRemindView() {
        View remindView = getActivity().getLayoutInflater().inflate(R.layout.list_item_message_list, null);
        CircleImageView civHead = (CircleImageView) remindView.findViewById(R.id.civ_head);
        TextView tvName = (TextView) remindView.findViewById(R.id.tv_name);
        tvReminderContent = (TextView) remindView.findViewById(R.id.tv_content);
        tvReminderNum = (TextView) remindView.findViewById(R.id.tv_num);
        civHead.setImageResource(R.drawable.message_remind);
        tvRemindTime = (TextView) remindView.findViewById(R.id.tv_time);
        tvName.setText("提醒");
        remindView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入提醒页面
                Intent intent = new Intent();
                intent.setClass(getActivity(), BaseMessageListActivity.class);
                intent.putExtra(BaseMessageListActivity.MESSAGELISTTYPE,
                        PushMessage.REMINDTYPE);
                startActivity(intent);

                //todo 统计
                EventLog.upEventLog("160", "hint");
            }
        });
        lvMessageList.addHeaderView(remindView, null, false);
    }*/

    @OnClick(R.id.mtiv_remind)
    void onRemindClick(){
        //进入活动页面
        BaseMessageListActivity.goActivity(getContext(),PushMessage.REMINDTYPE);
        //todo 统计
        EventLog.upEventLog("160", "hint");
    }

    /**
     * 更新提醒数据
     *
     * @param content
     * @param num
     * @param time
     */
    /*private void updateRemindData(String content, int num, String time) {
        if (!TextUtils.isEmpty(content)) {
            tvReminderContent.setText(content);
        } else {
            tvReminderContent.setText("当前没有消息");
        }
        refreshNums(num);
        tvRemindTime.setText(time);
    }

    private void refreshNums(int num) {
        if (num <= 0) {
            //小于0
            tvReminderNum.setVisibility(View.GONE);
        } else if (num > 0 && num < 10) {
            //1-9
            tvReminderNum.setVisibility(View.VISIBLE);
            tvReminderNum.setText(String.valueOf(num));
            tvReminderNum.setBackgroundResource(R.drawable.msg_new_ones);
        } else if (num > 9 && num < 100) {
            //10-99
            tvReminderNum.setVisibility(View.VISIBLE);
            tvReminderNum.setText(String.valueOf(num));
            tvReminderNum.setBackgroundResource(R.drawable.msg_new_tens);
        } else {
            //大于99
            tvReminderNum.setVisibility(View.VISIBLE);
            tvReminderNum.setText("99+");
            tvReminderNum.setBackgroundResource(R.drawable.msg_new_more);
        }
    }*/

    private void refreshMsgView(String content,int num,String time,int type){
        switch (type){
            case PushMessage.ACTIVITYTYPE:
                setViewValue(mMtivActivity,TextUtils.isEmpty(content) ? "暂无活动" : content,num,time);
                break;
            case PushMessage.INFOTYPE:
                setViewValue(mMtivInfomation,TextUtils.isEmpty(content) ? "暂无资讯" : content,num,time);
                break;
            case PushMessage.REMINDTYPE:
                setViewValue(mMtivRemind,TextUtils.isEmpty(content) ? "当前没有消息" : content,num,time);
                break;
        }
        /*setViewValue(mMtivActivity,TextUtils.isEmpty(content) ? "暂无活动" : content,num,time);
        setViewValue(mMtivInfomation,TextUtils.isEmpty(content) ? "暂无资讯" : content,num,time);
        setViewValue(mMtivRemind,TextUtils.isEmpty(content) ? "当前没有消息" : content,num,time);*/

    }
    private void setViewValue(MessageTypeItemView view,String content,int num,String time){
        view.setContent(content);
        view.setTime(time);
        view.setNum(num);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleRefreshPushEvent(RefreshPushEvent event) {
        reloadData();
    }

    /********  消息队列1秒一个 ************/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLoginEvent(LoginEvent event) {
        if (event.isLoginSuccess()) {
            //登入
            presenter.requestNotice();
            presenter.setStartLoad(true);
            presenter.updateConversationWhenNeed(false);

            // 在当前页面登陆成功上报的埋点
            if (LoginManager.loginPageLocation.isLastLoginInThisPage(LoginManager.LoginLocation.MESSAGE)) {
                //TODO 统计
                EventLog.upEventLog("2017050801", "m", "message");
            }

        } else {
            //登出
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handlefinal(final NewNotifMsgEvent event) {
        final Activity mA = getActivity();
        if (mA == null) {
            return;
        }
        mMtivRemind.setNum(event.remindNum > 0 ? event.remindNum : 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.removeFragment();
        EventBus.getDefault().unregister(this);
    }

    /*class Handler1 extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!msgQueue.isEmpty()) {

                *//*****消息合并逻辑********//*
                // 是否队列第一个是合并消息，否的话才进行消息合并，是的话直接跳过合并过程，直接发出这条消息，等下一次轮回再重新进行合并
                if(!isTopMergeMsg()) {
                    Set agentIdSetInQueue = getAgentIdsInQueueSet();
                    if(!SnackBarUtil.isLastMsgMerge) {
                        // 表示上一条显示的不是合并消息，即将进行第一次合并，但是必须是队列中有不同的经纪人发过来的消息才进行合并，否则跳过
                        if(agentIdSetInQueue.size() > 1) {
                            SnackBarUtil.isLastMsgMerge = true;
                            SnackBarUtil.lastMergeMsgAgentIdSet = agentIdSetInQueue;
                            SnackBarUtil.lastMergeMsgCount = msgQueue.size();
                        }
                    } else {
                        // 表示上一条显示的是合并消息，如果显示过合并消息后，后面任意消息都是在合并消息上加条数
                        SnackBarUtil.isLastMsgMerge = true;
                        SnackBarUtil.lastMergeMsgAgentIdSet.addAll(agentIdSetInQueue);
                        SnackBarUtil.lastMergeMsgCount = SnackBarUtil.lastMergeMsgCount+msgQueue.size();
                    }

                    // 如果经过以上逻辑，isLastMsgMerge=true 则发出合并消息
                    if(SnackBarUtil.isLastMsgMerge) {
                        IMMessageGlobalNotify imMessageGlobalNotify = new IMMessageGlobalNotify();
                        imMessageGlobalNotify.setMsgTitle("未读消息");
                        imMessageGlobalNotify.setMsgBody(SnackBarUtil.lastMergeMsgAgentIdSet.size()+"位经纪人给你发来"+SnackBarUtil.lastMergeMsgCount +"条消息");
                        msgQueue.clear();
                        msgQueue.add(imMessageGlobalNotify);
                    }
                }
                *//*****消息合并逻辑********//*


                *//****** 根据显示规则发出消息 *//*
                if(SystemClock.elapsedRealtime() - SnackBarUtil.lastPostElapsedRealtime > 1000+250+180) {
                    SnackBarUtil.dismissLastSnackBar();
                    IMMessageGlobalNotify imMessageGlobalNotify = msgQueue.remove(0);
                    EventBus.getDefault().post(imMessageGlobalNotify);
                    SnackBarUtil.lastPostElapsedRealtime = SystemClock.elapsedRealtime();
                    if (!msgQueue.isEmpty()) {
                        handler2.sendEmptyMessageDelayed(0, 1000+250+180);
                    } else {
                        isConsumingMsg = false;
                    }
                } else {
                    handler1.sendEmptyMessageDelayed(0, 1000+250+180-(SystemClock.elapsedRealtime() - SnackBarUtil.lastPostElapsedRealtime));
                }
                *//****** 根据显示规则发出消息 *//*

            } else {
                isConsumingMsg = false;
            }
        }
    }*/

    /*class Handler2 extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!msgQueue.isEmpty()) {

                *//*****消息合并逻辑********//*
                // 是否队列第一个是合并消息，否的话才进行消息合并，是的话直接跳过合并过程，直接发出这条消息，等下一次轮回再重新进行合并
                if(!isTopMergeMsg()) {
                    Set agentIdSetInQueue = getAgentIdsInQueueSet();
                    if(!SnackBarUtil.isLastMsgMerge) {
                        // 表示上一条显示的不是合并消息，即将进行第一次合并，但是必须是队列中有不同的经纪人发过来的消息才进行合并，否则跳过
                        if(agentIdSetInQueue.size() > 1) {
                            SnackBarUtil.isLastMsgMerge = true;
                            SnackBarUtil.lastMergeMsgAgentIdSet = agentIdSetInQueue;
                            SnackBarUtil.lastMergeMsgCount = msgQueue.size();
                        }
                    } else {
                        // 表示上一条显示的是合并消息，如果显示过合并消息后，后面任意消息都是在合并消息上加条数
                        SnackBarUtil.isLastMsgMerge = true;
                        SnackBarUtil.lastMergeMsgAgentIdSet.addAll(agentIdSetInQueue);
                        SnackBarUtil.lastMergeMsgCount = SnackBarUtil.lastMergeMsgCount+msgQueue.size();
                    }

                    // 如果经过以上逻辑，isLastMsgMerge=true 则发出合并消息
                    if(SnackBarUtil.isLastMsgMerge) {
                        IMMessageGlobalNotify imMessageGlobalNotify = new IMMessageGlobalNotify();
                        imMessageGlobalNotify.setMsgTitle("未读消息");
                        imMessageGlobalNotify.setMsgBody(SnackBarUtil.lastMergeMsgAgentIdSet.size()+"位经纪人给你发来"+SnackBarUtil.lastMergeMsgCount +"条消息");
                        msgQueue.clear();
                        msgQueue.add(imMessageGlobalNotify);
                    }
                }
                *//*****消息合并逻辑********//*


                *//****** 根据显示规则发出消息 *//*
                if(SystemClock.elapsedRealtime() - SnackBarUtil.lastPostElapsedRealtime >= 1000+250+180) {
                    SnackBarUtil.dismissLastSnackBar();
                    IMMessageGlobalNotify imMessageGlobalNotify = msgQueue.remove(0);
                    EventBus.getDefault().post(imMessageGlobalNotify);
                    SnackBarUtil.lastPostElapsedRealtime = SystemClock.elapsedRealtime();

                    if (!msgQueue.isEmpty()) {
                        handler1.sendEmptyMessageDelayed(0, 1000+250+180);
                    } else {
                        isConsumingMsg = false;
                    }
                } else {
                    handler2.sendEmptyMessageDelayed(0, 1000+250+180-(SystemClock.elapsedRealtime() - SnackBarUtil.lastPostElapsedRealtime));
                }
                *//****** 根据显示规则发出消息 *//*

            } else {
                isConsumingMsg = false;
            }
        }
    }*/

}
