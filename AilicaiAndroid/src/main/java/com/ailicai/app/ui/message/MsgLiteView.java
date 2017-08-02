package com.ailicai.app.ui.message;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.eventbus.LoginEvent;
import com.ailicai.app.eventbus.NewNotifMsgEvent;
import com.ailicai.app.message.NoticeChangeRequest;
import com.ailicai.app.message.NoticeRequest;
import com.ailicai.app.message.NoticeResponse;
import com.ailicai.app.ui.index.IndexActivity;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.widget.TextViewTF;
import com.huoqiu.framework.rest.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Jer on 2016/4/20.
 */
public class MsgLiteView extends FrameLayout implements View.OnClickListener {
    public interface INoticeListener {
        void onNoticeNums(int msgSum);
    }

    public MsgLiteView(Context context) {
        super(context);
        initView();
    }

    public MsgLiteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MsgLiteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MsgLiteView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    public void setMsgWhite(boolean isWhite) {
        TextViewTF msgBtn = ((TextViewTF) findViewById(R.id.msg_btn));
        if (isWhite) {
            msgBtn.setTextColor(getResources().getColor(R.color.white));
        } else {
            msgBtn.setTextColor(getResources().getColor(R.color.color_616161));
        }
    }

    public void setNumBg(int resId) {
        TextView tvNum = ((TextView) findViewById(R.id.msg_sum_txt));
        tvNum.setBackgroundResource(resId);
    }

    public void setNumTextColor(int color) {
        TextView tvNum = ((TextView) findViewById(R.id.msg_sum_txt));
        tvNum.setTextColor(color);
    }

    TextView msgSumTxt;

    void initView() {
        EventBus.getDefault().register(this);
        inflate(getContext(), R.layout.msg_lite_layout, this);
        findViewById(R.id.msg_view).setOnClickListener(this);
        msgSumTxt = (TextView) findViewById(R.id.msg_sum_txt);
        initData();
    }

    public TextViewTF getMsgView() {
        return (TextViewTF) findViewById(R.id.msg_btn);
    }

    public void initData() {
        if (getContext() instanceof IndexActivity) {
            //控制首页获取消息只请求一次
            if (((IndexActivity) getContext()).getNotifLoadCount() == 0) {
                ((IndexActivity) getContext()).initOneceNum();
                refreshNoticeNums(iNoticeListener);
                //      LogUtil.d("debuglog", "首页数据第一次获取消息，设置监听");
            } else {
                //        LogUtil.d("debuglog", "首页数据并且非第一次获取消息，去本地保存的消息" + MyApplication.getInstance().getCurrentNoticeNums());
                showMsgSumTxt(MyApplication.getInstance().getCurrentNoticeNums());
            }
        } else {
            refreshNoticeNums(iNoticeListener);
        }
    }


    /**
     * 刷新消息数
     *
     * @param iNoticeListener
     */
    public static void refreshNoticeNums(final INoticeListener iNoticeListener) {
        if (UserInfo.getInstance().getLoginState() != UserInfo.LOGIN) {
            if (iNoticeListener != null) {
                iNoticeListener.onNoticeNums(0);
            }
            return;
        }
        NoticeRequest noticeRequest = new NoticeRequest();
        ServiceSender.exec(MyApplication.getInstance(), noticeRequest, new IwjwRespListener<NoticeResponse>() {
            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onJsonSuccess(NoticeResponse n) {
                UserInfo.getInstance().updatePushNum(n);
                int sum = n.getNoticeNum() + n.getRemindNum() + n.getActivityNum();
                MyApplication.getInstance().setCurrentNoticeNums(sum);
                if (iNoticeListener != null) {
                    iNoticeListener.onNoticeNums(sum);
                }
                NewNotifMsgEvent newNotifMsgEvent = new NewNotifMsgEvent();
                //   newNotifMsgEvent.from = "MsgLiteView142";
                newNotifMsgEvent.notifNum = sum;
                newNotifMsgEvent.remindNum = n.getRemindNum(); // MessageFragment中提醒单独用
                newNotifMsgEvent.hasPopActive = n.getHasPopActive();
                //         LogUtil.d("debuglog", "post 消息");
                EventBus.getDefault().post(newNotifMsgEvent);
            }
        });
    }

    public static void changeNoticeStatus(long id,int msgType,int status){
        NoticeChangeRequest noticeSaveRequest = new NoticeChangeRequest();
        long userid = UserInfo.getInstance().getUserId();
        if (userid > 0) {
            noticeSaveRequest.setUserId(userid);
            noticeSaveRequest.setId(id);
            noticeSaveRequest.setType(status);
            noticeSaveRequest.setNoticeType(msgType);
            ServiceSender.exec(MyApplication.getInstance(), noticeSaveRequest, new IwjwRespListener<Response>() {
                @Override
                public void onJsonSuccess(Response jsonObject) {
                    refreshNoticeNums(null);
                }

                @Override
                public void onFailInfo(String errorInfo) {
                }
            });
        }
    }

    /**
     * 跳转消息界面
     *
     * @param activity
     * @return 是否成功跳转
     */
    public static boolean gotoMsgActivity(Activity activity, int fromCode) {
        if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
            activity.startActivity(new Intent(activity, MessageActivity.class));
            return true;
        } else {
            LoginManager.goLogin(activity, fromCode);
            return false;
        }
    }


    private void showMsgSumTxt(int sum) {
        //  LogUtil.d("debuglog", sum + ":NoticeResponse");
        msgSumTxt.setText("" + sum);
        msgSumTxt.setVisibility(View.VISIBLE);
        if (sum == 0) {
            msgSumTxt.setVisibility(View.GONE);
        } else if (sum > 99) {
            msgSumTxt.setText("99+");
        }
    }


    Activity getActivity() {
        return mActivity == null ? (Activity) getContext() : mActivity;
    }

    Activity mActivity;

    public void setActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

   /*  public Context getContext(){

         return
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.msg_view:
                //TODO 统计
                EventLog.upEventLog("160", "show");
                callBackToMsgActivity = !gotoMsgActivity(getActivity(), -1);
                break;
            default:
                break;
        }
    }

    boolean callBackToMsgActivity = false;

    /**
     * 登录成功了
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLoginEvent(LoginEvent event) {
        if (!event.isLoginSuccess()) {
            showMsgSumTxt(0);
        }

        if (event.isCancelLogin()) {
            callBackToMsgActivity = false;
            return;
        } else {
            if (callBackToMsgActivity && event.isLoginSuccess()) {
                callBackToMsgActivity = false;
                if(event.isContinueNext()){
                    getActivity().startActivity(new Intent(getActivity(), MessageActivity.class));
                }
            }
        }
    }

    //EventBUS  聊天有新消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleNewNotifMsgEvent(NewNotifMsgEvent event) {
        //   LogUtil.d("debuglog", "收到消息并设置：" + event.from + "/" + event.notifNum);
        showMsgSumTxt(event.notifNum);
        Activity mA = getActivity();
        if (getActivity() != null) {
         /*   if (event.hasPopActive == 1 && mA != null && !mA.isFinishing()) {
                FinanceAdActivity.showAdFullDialog(mA);
            }*/
        }
    }


    INoticeListener iNoticeListener = new INoticeListener() {
        @Override
        public void onNoticeNums(int msgSum) {
            //  LogUtil.d("debuglog", "监听到的消息并设置：" + msgSum);
            showMsgSumTxt(msgSum);
        }
    };

    public void onFinish() {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!EventBus.getDefault().isRegistered(this)) {
            //  LogUtil.d("debuglog", "onAttachedToWindow:" + "重新设置数字");
            EventBus.getDefault().register(this);
            showMsgSumTxt(MyApplication.getInstance().getCurrentNoticeNums());
        }
    }
}
