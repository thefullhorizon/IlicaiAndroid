package com.ailicai.app.ui.message;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.push.model.PushMessage;
import com.ailicai.app.eventbus.LoginEvent;
import com.ailicai.app.eventbus.NewNotifMsgEvent;
import com.ailicai.app.eventbus.RefreshPushEvent;
import com.ailicai.app.message.NoticeResponse;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.message.presenter.MessagePresenter;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.MessageTypeItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liyanan on 16/7/5.
 * fixed by jeme on 17/7/18
 */
public class MessageFragment extends BaseBindFragment{

//    public static boolean indexSwitchNeedUpdateMessage = false;

    @Bind(R.id.message_top_title)
    IWTopTitleView topView;
    @Bind(R.id.mtiv_activity)
    MessageTypeItemView mMtivActivity;
    @Bind(R.id.mtiv_information)
    MessageTypeItemView mMtivInfomation;
    @Bind(R.id.mtiv_remind)
    MessageTypeItemView mMtivRemind;

    private MessagePresenter presenter;

    @Override
    public int getLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        EventBus.getDefault().register(this);
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
//            presenter.updateConversationWhenNeed(forceUpdate);
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
            refreshMsgView(response.getRemindTitle(), response.getRemindNum(), response.getRemindTime(),PushMessage.REMINDTYPE);
        }
    }

    /**
     * 请求失败
     */
    public void requestFail(String errorInfo) {
        showMyToast(errorInfo);
    }


    @OnClick(R.id.mtiv_activity)
    void onActivityClick(){
        //进入活动页面
        BaseMessageListActivity.goActivity(getActivity(),PushMessage.ACTIVITYTYPE);
    }

    @OnClick(R.id.mtiv_information)
    void onInformationClick(){
        //进入活动页面
        BaseMessageListActivity.goActivity(getActivity(),PushMessage.INFOTYPE);
        //TODO 统计
        EventLog.upEventLog("160", "notice");
    }

    @OnClick(R.id.mtiv_remind)
    void onRemindClick(){
        //进入活动页面
        BaseMessageListActivity.goActivity(getActivity(),PushMessage.REMINDTYPE);
        //todo 统计
        EventLog.upEventLog("160", "hint");
    }


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
//            presenter.setStartLoad(true);
//            presenter.updateConversationWhenNeed(false);

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

}
