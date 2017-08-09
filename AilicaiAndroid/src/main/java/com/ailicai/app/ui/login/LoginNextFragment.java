package com.ailicai.app.ui.login;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.push.MqttManager;
import com.ailicai.app.common.push.PushUtil;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.NetCheckUtil;
import com.ailicai.app.common.utils.StringUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.TimerUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.common.utils.ValidateUtil;
import com.ailicai.app.eventbus.SmsCodeEvent;
import com.ailicai.app.model.request.GetVerifyCodeRequest;
import com.ailicai.app.model.request.SendVoiceVerifyCodeRequest;
import com.ailicai.app.model.request.UserLoginRequest;
import com.ailicai.app.model.request.VoiceVerifyCodeInitRequest;
import com.ailicai.app.model.response.UserLoginResponse;
import com.ailicai.app.model.response.VoiceVerifyCodeInitResponse;
import com.ailicai.app.receiver.SmsObserver;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.widget.DialogBuilder;
import com.ailicai.app.widget.psd.PayPsdInputView;
import com.github.florent37.viewanimator.ViewAnimator;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.util.NetworkUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 登录下一步页面
 */
public class LoginNextFragment extends BaseBindFragment implements PayPsdInputView.OnPasswordChangedListener {
    public static final String PHONE_NUM = "phone_num_text";
    private static final int CURSORTIMEROPENED = 0;
    private final int TIMER_STARTING = 0x0001;
    private final int TIMER_CANCELED = 0x0002;
    public LoginSuccessCardDialog cardDialog;
    public Handler smsHandler = new Handler() {
    };
    @Bind(R.id.show_phone_text)
    TextView mShowPhoneText;
    //@Bind(R.id.gpv_auth_edit)
    //GridPasswordView mGpvAuthCode;
    @Bind(R.id.psd_edit)
    PayPsdInputView mPayPsdInputView;
    @Bind(R.id.cursor_view)
    View mCursorView;
    @Bind(R.id.auto_code_layout)
    RelativeLayout mAutoCodeLayout;
    @Bind(R.id.get_voice_auth_code)
    TextView mGetVoiceAuthCodeBtn;
    @Bind(R.id.get_auth_code)
    TextView mGetAuthCodeBtn;
    @Bind(R.id.logining_layout)
    LinearLayout loginingLayout;
    Handler timerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMER_STARTING:
                    mGetAuthCodeBtn.setEnabled(false);
                    String timeNum = MyApplication.getInstance().getString(R.string.auth_code_time_num, msg.arg1);
                    mGetAuthCodeBtn.setText(timeNum);

                    //if (msg.arg1 == 30 && showVoiceBtn && !TextUtils.isEmpty(mPhoneEditText.getEditorText())) {
                    // mGetVoiceAuthCodeBtn.setVisibility(View.VISIBLE);
                    //}
                    break;
                case TIMER_CANCELED:
                    mGetAuthCodeBtn.setEnabled(true);
                    mGetAuthCodeBtn.setText(R.string.validate_phone_auth_code_retry);
                    break;
            }
        }


    };
    private boolean cursorTimerOpened = false;
    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CURSORTIMEROPENED:
                    ViewAnimator.animate(mCursorView)
                            .alpha(mCursorView.getAlpha(), 1f - mCursorView.getAlpha())
                            .duration(200)
                            .start();

                    if (cursorTimerOpened) {
                        mHandler.sendEmptyMessageDelayed(CURSORTIMEROPENED, 800);
                    }
                    break;
            }
            return false;
        }
    });
    private WeakReference<Activity> activityWeakReference;
    private int fromPage = LoginManager.LOGIN_FROM_MINE;
    private boolean isAutoLogined = false;
    private String account;
    private String mPhoneNumber;
    private SmsObserver smsObserver;
    private TimerUtil mTimerUtil = null;
    private TimerUtil.TimerListener mTimerListener = new TimerUtil.TimerListener() {
        @Override
        public void onTimerStarting(int periodNum) {
            Message msg = Message.obtain(timerHandler, TIMER_STARTING, periodNum, 0);
            msg.sendToTarget();
        }

        @Override
        public void onTimerCancel() {
            Message msg = Message.obtain(timerHandler, TIMER_CANCELED, 0, 0);
            msg.sendToTarget();
        }

    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    public void onDetach() {
        hideCursor();
        activityWeakReference = null;
        super.onDetach();
    }

    /**
     * 获取weakReference activity
     */
    public Activity getWRActivity() {
        return activityWeakReference == null ? null : activityWeakReference.get();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        smsHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //注册短信监听，用于登录时截获短信验证码（有的手机用BroadcastReceiver的方式无法截获短信验证码，所以这里再加一种方式）
                smsObserver = new SmsObserver(getWRActivity(), smsHandler);
                getWRActivity().getContentResolver().registerContentObserver(SmsObserver.SMS_INBOX, true, smsObserver);
            }
        }, 250);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showPsdKeyBoard();
    }

    public void showPsdKeyBoard() {
        //mGpvAuthCode.forceInputViewGetFocus();
        mPayPsdInputView.setFocusable(true);
        mPayPsdInputView.setFocusableInTouchMode(true);
        mPayPsdInputView.requestFocus();
        SystemUtil.showKeyboard(mPayPsdInputView);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        Map<String, Object> dataMap = MyIntent.getData(getArguments());
        mPhoneNumber = (String) dataMap.get(PHONE_NUM);
        if (dataMap != null) {
            fromPage = (int) dataMap.get(LoginManager.LOGIN_FROM);
        }

        mTimerUtil = new TimerUtil(mTimerListener, this.getClass().getCanonicalName());

        /*
        mGpvAuthCode.clearPassword();
        mGpvAuthCode.setOnPasswordChangedListener(this);
        mGpvAuthCode.setPasswordVisibility(true);
        */

        mPayPsdInputView.clearPassword();
        mPayPsdInputView.setComparePassword(null, this);

        mShowPhoneText.setText("+86 " + StringUtil.formatCheckMobileNumber(mPhoneNumber, " "));
        mGetAuthCodeBtn.setEnabled(false);
        loginingLayout.setVisibility(View.GONE);

        mTimerUtil.startTimer();
    }

    public void closeKeyBoard() {
        //SystemUtil.hideKeyboard(mGpvAuthCode);
        SystemUtil.hideKeyboard(mPayPsdInputView);
    }

    /**
     * 隐藏光标
     */
    public void hideCursor() {
        cursorTimerOpened = false;
        mHandler.removeMessages(CURSORTIMEROPENED);
        if (mCursorView != null) {
            mCursorView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        hideCursor();
        super.onStop();
    }

    @Override
    public void onPause() {
        closeKeyBoard();
        hideCursor();
        //倒计时继续该页面不需要
        /*
        if (mTimerUtil.isTimerStarting()) {
            mTimerUtil.pauseTimer();
        }
        */
        //结束本页面停掉Timer
        if (mTimerUtil.isTimerStarting()) {
            mTimerUtil.stopTimer();
        }
        super.onPause();
    }

    @Override
    public int getLayout() {
        return R.layout.login_next_activity_layout;
    }

    @OnClick(R.id.get_voice_auth_code)
    void getVoiceAuthCode() {
        if (!NetCheckUtil.hasActiveNetwork()) {
            ToastUtil.showInCenter("网络未连接，请检查网络后重试！");
            return;
        }

        if (checkPhone()) voiceVerifyCode();
    }

    private void voiceVerifyCode() {
        VoiceVerifyCodeInitRequest request = new VoiceVerifyCodeInitRequest();
        request.setMobile(StringUtil.allSpaceFormat(mPhoneNumber));
        ServiceSender.exec(this, request, new IwjwRespListener<VoiceVerifyCodeInitResponse>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onJsonSuccess(VoiceVerifyCodeInitResponse jsonObject) {
                switch (jsonObject.getErrorCode()) {
                    case 0:
                        account = jsonObject.getAccount();
                        DialogBuilder.showSimpleDialog(getWRActivity(), "", jsonObject.getMessage(), "取消", null, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendToVoiceVerifyCode();
                            }
                        });
                        break;
                    case RestException.SEND_VOICE_VERIFY_CODE_ERROR:
                        DialogBuilder.showSimpleDialog(jsonObject.getMessage(), getWRActivity(), null);
                        break;
                    default:
                        ToastUtil.showInCenter(jsonObject.getMessage());
                        break;
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showMyToast(errorInfo);
            }
        });
    }

    private void sendToVoiceVerifyCode() {
        SendVoiceVerifyCodeRequest request = new SendVoiceVerifyCodeRequest();
        request.setMobile(StringUtil.allSpaceFormat(mPhoneNumber));
        request.setAccount(account);
        ServiceSender.exec(this, request, new IwjwRespListener<Response>() {
            @Override
            public void onJsonSuccess(Response jsonObject) {
                ToastUtil.showInCenter(jsonObject.getMessage());
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showMyToast(errorInfo);
            }
        });
    }

    private boolean checkPhone() {
        if (!ValidateUtil.isValidMobile(StringUtil.allSpaceFormat(mPhoneNumber))) {
            ToastUtil.showInCenter("手机号码输入有误");
            return false;
        }
        return true;
    }

    @OnClick(R.id.get_auth_code)
    void getAuthCode() {
        if (!NetCheckUtil.hasActiveNetwork()) {
            ToastUtil.showInCenter("网络未连接，请检查网络后重试！");
            return;
        }

        if (checkPhone()) {
            //mGpvAuthCode.clearPassword();
            mPayPsdInputView.clearPassword();
            sendToGetAuthCode();
            mGetAuthCodeBtn.setEnabled(false);
            mTimerUtil.startTimer();
        }
    }

    private void sendToGetAuthCode() {
        GetVerifyCodeRequest getVerifyCodeRequest = new GetVerifyCodeRequest();
        getVerifyCodeRequest.setMobile(StringUtil.allSpaceFormat(mPhoneNumber));
        ServiceSender.exec(this, getVerifyCodeRequest, new IwjwRespListener<Response>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onJsonSuccess(Response jsonObject) {
                int code = jsonObject.getErrorCode();
                if (code == 0) {
                    //showMyToast(jsonObject.getMessage());
                } else if (code == RestException.GET_VERIFY_CODE_ERROR || code == RestException.GET_VERIFY_CODE_ERROR_TEN_TIMES) {
                    onFailInfo(jsonObject.getMessage());
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showMyToast(errorInfo);
                mTimerUtil.stopTimer();
            }
        });
    }

    @Override
    public void onTextChanged(String s) {

    }

    @Override
    public void onInputFinish(String s) {
        //mAutoCodeLayout.setVisibility(View.GONE);
        //loginingLayout.setVisibility(View.VISIBLE);
        mTimerUtil.stopTimer();
        hideCursor();
        closeKeyBoard();
        if (!clickLogin()) {
            mPayPsdInputView.clearPassword();
            showPsdKeyBoard();
        }
    }

    @Override
    public void onDifference() {

    }

    @Override
    public void onEqual(String psd) {

    }

    @Override
    public void showCursor(float x, float y) {
        mCursorView.setVisibility(View.VISIBLE);
        mCursorView.setTranslationX(x);
        if (!cursorTimerOpened) {
            cursorTimerOpened = true;
            mHandler.sendEmptyMessage(CURSORTIMEROPENED);
        }
    }

    @Override
    public void onDestroy() {
        if (smsObserver != null) {
            getWRActivity().getContentResolver().unregisterContentObserver(smsObserver);
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 设置短信验证码
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleSmsCodeEvent(SmsCodeEvent event) {
        //mGpvAuthCode.setPassword(event.getCode());
        mPayPsdInputView.setPasswordString(event.getCode());
        mTimerUtil.stopTimer();
        //String authCode = StringUtil.allSpaceFormat(mGpvAuthCode.getPassWord());
        String authCode = StringUtil.allSpaceFormat(mPayPsdInputView.getPasswordString());
        if (ValidateUtil.isValidAuthCode(authCode)) {
            LogUtil.d("=======AUTO===LOGIN==========");
            if (!isAutoLogined) {
                isAutoLogined = true;
                closeKeyBoard();
                login();//自动登录
            }
        }
    }

    public boolean clickLogin() {
        if (!NetCheckUtil.hasActiveNetwork()) {
            ToastUtil.showInCenter("网络未连接，请检查网络后重试！");
            return false;
        }
        //if (!ValidateUtil.isValidAuthCode(StringUtil.allSpaceFormat(mGpvAuthCode.getPassWord()))) {
        if (!ValidateUtil.isValidAuthCode(StringUtil.allSpaceFormat(mPayPsdInputView.getPasswordString()))) {
            ToastUtil.showInCenter("验证码有误");
            return false;
        }

        login();
        return true;
    }

    public void login() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        List<String> list = new ArrayList<>();
        String[] houseInfos = new String[list.size()];
        houseInfos = list.toArray(houseInfos);
        loginRequest.setHouseIds(houseInfos);
        loginRequest.setMobile(mPhoneNumber);
        //loginRequest.setVerifyCode(StringUtil.allSpaceFormat(mGpvAuthCode.getPassWord()));
        loginRequest.setVerifyCode(StringUtil.allSpaceFormat(mPayPsdInputView.getPasswordString()));
        loginRequest.setMobileSn(DeviceUtil.getMobileUUID(MyApplication.getInstance()));
        loginRequest.setFromPage(fromPage);
        loginRequest.setSystemVer(DeviceUtil.getSystemVersion());
        loginRequest.setNetType(NetworkUtil.getNetWorkTypeStr(MyApplication.getInstance()));
        loginRequest.setSupport(DeviceUtil.getOperators(MyApplication.getInstance()));
        loginRequest.setClientId(MqttManager.getClientIdByMobile(mPhoneNumber));

        ServiceSender.exec(this, loginRequest, new IwjwRespListener<UserLoginResponse>() {

            @Override
            public void onStart() {
                showLoadTranstView();
            }

            @Override
            public void onJsonSuccess(UserLoginResponse jsonObject) {
                showContentView();
                int code = jsonObject.getErrorCode();
                if (code == 0) {
                    if (jsonObject.getUserId() > 0) {
                        UserInfo.getInstance().setUserInfoData(jsonObject, mPhoneNumber);

                        PushUtil.resetMqttService(getWRActivity());

                        //if (jsonObject.getFirstLogin() == 0) { //0-首次 1-否
                        //123 setInatllAgentTask();
                        //} else {
                        //appointmentId = jsonObject.getAppointmentId();
                        //appointBizType = jsonObject.getAppointBizType();
                        //if (jsonObject.getAppointmentId() > 0) {
                        //123 appraiseAgentTask();
                        //} else {
                        //   continueLogin();
                        //}
                        //}

                        continueLogin(jsonObject);
                    } else {
                        onFailInfo("认证失败，请重试！");
                    }
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showContentView();
                onLoginComplete();
                showMyToast(errorInfo);
                //mGpvAuthCode.clearPassword();
                mPayPsdInputView.clearPassword();
                showPsdKeyBoard();
            }
        });
    }

    public void onLoginComplete() {
        //mAutoCodeLayout.setVisibility(View.VISIBLE);
        //loginingLayout.setVisibility(View.GONE);
    }

    private void continueLogin(UserLoginResponse jsonObject) {
        //onLoginComplete();
        //处理登录成功相关事件
        boolean showPackage = !TextUtils.isEmpty(jsonObject.getActivityName());
        LoginManager.loginSuccess(getWRActivity(), fromPage, jsonObject, showPackage);
        getActivity().finish();
    }

}
