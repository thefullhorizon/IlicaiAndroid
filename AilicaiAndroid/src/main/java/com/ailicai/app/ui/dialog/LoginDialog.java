package com.ailicai.app.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.push.MqttManager;
import com.ailicai.app.common.push.PushUtil;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.MyPreference;
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
import com.ailicai.app.ui.base.FragmentHelper;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.widget.DialogBuilder;
import com.ailicai.app.widget.ManyiEditText;
import com.ailicai.app.widget.TextViewTF;
import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.util.ManyiUtils;
import com.huoqiu.framework.util.NetworkUtil;
import com.jungly.gridpasswordviewext.GridPasswordView;
import com.wang.avi.AVLoadingIndicatorView;

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
 * Created by Gerry on 2016/7/29.
 */

public class LoginDialog extends MyBaseDialog implements GridPasswordView.OnPasswordChangedListener {

    private final int TIMER_STARTING = 0x0001;
    private final int TIMER_CANCELED = 0x0002;
    private final BaseSpringSystem mSpringSystem = SpringSystem.create();
    private final ExampleSpringListener mSpringListener = new ExampleSpringListener();
    public Handler smsHandler = new Handler() {
    };
    @Bind(R.id.root_view)
    LinearLayout mRootView;
    @Bind(R.id.login_back)
    TextView loginBackView;
    @Bind(R.id.dialog_title)
    TextView dTitle;
    @Bind(R.id.login_first_view)
    LinearLayout loginFirstView;
    @Bind(R.id.login_next_view)
    LinearLayout loginNextView;
    @Bind(R.id.close_button)
    TextViewTF mCloseBtn;
    @Bind(R.id.login_next_btn)
    TextView mNextBtn;
    @Bind(R.id.next_loading)
    AVLoadingIndicatorView mNextLoading;
    @Bind(R.id.phone_edit)
    ManyiEditText mPhoneEditText;
    @Bind(R.id.auto_code_layout)
    RelativeLayout mAutoCodeLayout;
    @Bind(R.id.get_auth_code)
    Button mGetAuthCodeBtn;
    @Bind(R.id.show_phone_text)
    TextView mShowPhoneText;
    @Bind(R.id.get_voice_auth_code)
    Button mGetVoiceAuthCodeBtn;
    @Bind(R.id.logining_layout)
    LinearLayout loginingLayout;
    //@Bind(R.id.permit_agreement)
    //TextView mPermitTextView;
    @Bind(R.id.gpv_auth_edit)
    GridPasswordView mGpvAuthCode;
    String autoMobileNum;
    private FragmentHelper mFragmentHelper;
    private Context mContext;
    private WeakReference<Activity> activityWeakReference;
    Handler timerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (getWRActivity() == null) {
                return;
            }
            if (mGetAuthCodeBtn == null) {
                return;
            }
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
    private int fromPage = LoginManager.LOGIN_FROM_MINE;
    private UserLoginResponse mUserLoginResponse = null;
    /*
    AppraiseAgentDialogUpdate.AppraiseAgentInterface appraiseAgentInterface = new AppraiseAgentDialogUpdate.AppraiseAgentInterface() {
        @Override
        public void appraiseAgentOver() {
            continueLogin();
        }
    };
    */
    private boolean isNextBtnEnable;
    private String account;
    private boolean showVoiceBtn = true;
    private long appointmentId;
    private int appointBizType;
    //private RecommenderDialog dialog;
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
    private TimerUtil mTimerUtil = null;
    private String mPhoneNumber;
    private boolean isAutoLogined = false;
    private View.OnClickListener mCloseBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null != getWRActivity()) {
                closeKeyBoard();
                LoginManager.loginCancel();
                dismiss();
            }
        }
    };
    private ManyiEditText.OnFocusChangeInterface mOnFocusChangeInterface = new ManyiEditText.OnFocusChangeInterface() {
        @Override
        public void hasClearText(View v) {
        }

        @Override
        public void onViewChange(View v, boolean hasFocus) {
        }
    };
    private TextWatcher mPhoneTextWatch = new TextWatcher() {

        /**
         * 以防在afterTextChanged设值后重复调用.
         */
        private boolean _formatable = true;
        private int _selection = 0;
        private StringBuffer _buffer = new StringBuffer();

        private String formatMobile(String mobile) {
            mobile = mobile.replace(" ", "");
            StringBuffer buffer = new StringBuffer(mobile);

            if (mobile.length() >= 11) {
                buffer.insert(3, " ");
                buffer.insert(8, " ");
                buffer.insert(13, " ");
            } else if (mobile.length() >= 7) {
                buffer.insert(3, " ");
                buffer.insert(8, " ");
            } else if (mobile.length() >= 3) {
                buffer.insert(3, " ");
            }

            return buffer.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (_formatable) {

                //手动删(剪切一个字符视为手动删除).
                if (before == 1) {
                    _selection = start - before + 1;
                    if (_buffer.charAt(start) == ' ') {
                        _buffer.setLength(0);
                        _buffer.append(s);
                        _buffer.deleteCharAt(start - 1);
                        _selection = _selection - 1;
                    } else {
                        _buffer.setLength(0);
                        _buffer.append(s);
                    }

                } else if (before > 1) {//剪切或者全部删.
                    _buffer.setLength(0);
                    _buffer.append(s);
                    _selection = _buffer.length();

                } else {
                    //增加 after > 0.
                    _buffer.setLength(0);
                    _buffer.append(s);
                }

                String sub = _buffer.toString().substring(0, _selection);

                _selection += formatMobile(sub).length() - sub.length();

            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (_formatable) {
                _buffer.setLength(0);
                _buffer.append(s);
                if (after > 0) {
                    _selection = start + after;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(mPhoneNumber) && !mPhoneNumber.equals(_buffer.toString().replace(" ", ""))) {
                //showVoiceBtn = false;
                //mGetVoiceAuthCodeBtn.setVisibility(View.GONE);
            }
            if (_formatable) {
                _formatable = false;
                String formatMobile = formatMobile(_buffer.toString());
                //以防意外.
                if (_selection > formatMobile.length()) {
                    _selection = formatMobile.length();
                }
                mPhoneEditText.setEditorText(formatMobile);
            } else {
                _formatable = true;
            }
            mPhoneEditText.getmEditText().setSelection(_selection);
            setActionViewState();
        }
    };
    private SmsObserver smsObserver;
    /*
    private RecommenderDialog.OnActionClickListener listener = new RecommenderDialog.OnActionClickListener() {
        @Override
        public void onNextBtnClick(final long agentId, String mobile) {
            ConfirmInstallRequest request = new ConfirmInstallRequest();
            request.setAgentId(agentId);
            request.setLoginTime(new Date().getTime());
            request.setMobile(mobile);
            ServiceSender.exec(getContext(), request, new IwjwRespListener<Response>(getContext()) {
                @Override
                public void onJsonSuccess(Response jsonObject) {
                    int code = jsonObject.getErrorCode();
                    switch (code) {
                        case RestException.NO_ERROR:
                            dismissRecommenderDialog();
                            ToastUtil.showInCenter("提交成功");
                            //123 EventLog.upEventLog("869", agentId + "");
                            break;
                        case RestException.LOGIN_RECOMMEND_AGENT_YOURS:
                            ToastUtil.showInCenter("不能填写自己的手机号码");
                            break;
                        case RestException.LOGIN_RECOMMEND_AGENT_NOREGISTER:
                            ToastUtil.showInCenter("您填写的手机号尚未注册");
                            break;
                        case RestException.LOGIN_RECOMMEND_AGENT_SINGLE:
                            ToastUtil.showInCenter("您只能选择一位推荐人");
                            break;
                    }
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    dismissRecommenderDialog();
                    continueLogin();
                }
            });
        }
    };
    */
    private Spring mScaleSpring;

    @Override
    public int getTheme() {
        return MYTHEME2;
    }

    @Override
    public int displayWindowLocation() {
        return Gravity.CENTER;
    }

    @Override
    public boolean cancelable() {
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityWeakReference = null;
    }

    /**
     * 获取weakReference activity
     */
    public Activity getWRActivity() {
        return activityWeakReference == null ? null : activityWeakReference.get();
    }

    public void closeKeyBoard() {
        //ManyiUtils.closeKeyBoard(getWRActivity(), mGpvAuthCode);
        //ManyiUtils.closeKeyBoard(getWRActivity(), mPhoneEditText);
        //closeKeyBoard(getWRActivity(), mGpvAuthCode);
        //closeKeyBoard(getWRActivity(), mPhoneEditText);
        SystemUtil.hideKeyboard(mGpvAuthCode);
        SystemUtil.hideKeyboard(mPhoneEditText);
    }

    @Override
    public void setupView(View rootView, Bundle savedInstanceState) {
        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DeviceUtil.getScreenSize()[0] - DeviceUtil.getPixelFromDip(mContext, 60),

        //slideInFirstView();
        loginFirstView.setVisibility(View.VISIBLE);
        loginNextView.setVisibility(View.GONE);
        //slideOutNextView();

        mTimerUtil = new TimerUtil(mTimerListener, this.getClass().getCanonicalName());
        EventBus.getDefault().register(this);

        //关闭按钮点击事件
        mCloseBtn.setOnClickListener(mCloseBtnClick);
        //爱屋吉屋软件许可及服务协议
        String permitStr = getString(R.string.permit_agreement_text);
        //mPermitTextView.setText(Html.fromHtml(permitStr));
        //手机号
        String savePhone = MyPreference.getInstance().read(UserInfo.USERINFO_KEY_MOBILE, "");
        mPhoneEditText.setEditorText(StringUtil.formatCheckMobileNumber(savePhone, " "));
        mPhoneEditText.setFocusChangeInterface(mOnFocusChangeInterface);
        mPhoneEditText.setEditorWatchListener(mPhoneTextWatch);
        mPhoneEditText.setFocusable(true);
        mPhoneEditText.setEditorHintColor(getResources().getColor(R.color.login_edit_text_hint_color));

        mNextLoading.hide();

        mGpvAuthCode.clearPassword();
        mGpvAuthCode.setOnPasswordChangedListener(this);
        mGpvAuthCode.setPasswordVisibility(true);

        setActionViewState();
    }

    public void showFirstView() {
        closeKeyBoard();
        loginBackView.setVisibility(View.GONE);
        slideInFirstView();
        slideOutNextView();
        dTitle.setText("登录");
        mTimerUtil.stopTimer();
    }

    public void showNextView() {
        closeKeyBoard();
        loginBackView.setVisibility(View.VISIBLE);
        slideInNexyView();
        slideOutFirstView();
        dTitle.setText("输入验证码");
        mShowPhoneText.setText(StringUtil.formatCheckMobileNumber(mPhoneEditText.getEditorText(), " "));

        mGetAuthCodeBtn.setEnabled(false);
        //mGetVoiceAuthCodeBtn.setVisibility(View.GONE);
        showVoiceBtn = true;
        if (showVoiceBtn && !TextUtils.isEmpty(mPhoneNumber)) {
            mGetVoiceAuthCodeBtn.setVisibility(View.VISIBLE);
        }
        mTimerUtil.startTimer();
    }

    public void slideInFirstView() {
        loginFirstView.setVisibility(View.VISIBLE);
        mPhoneEditText.showClearButton(true);
        mPhoneEditText.getmEditText().setEnabled(true);
        //Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.login_view_left_in);
        //anim.setFillAfter(true);
        //loginFirstView.startAnimation(anim);
    }

    public void slideOutFirstView() {
        loginFirstView.setVisibility(View.GONE);
        //Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.login_view_left_out);
        //anim.setFillAfter(true);
        //loginFirstView.startAnimation(anim);
    }

    public void slideInNexyView() {
        loginNextView.setVisibility(View.VISIBLE);
        //Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.login_view_right_in);
        //anim.setFillAfter(true);
        //loginNextView.startAnimation(anim);
    }

    public void slideOutNextView() {
        loginNextView.setVisibility(View.GONE);
        //Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.login_view_right_out);
        //anim.setFillAfter(true);
        //loginNextView.startAnimation(anim);
    }

    @Override
    public void onTextChanged(String psw) {

    }

    @Override
    public void onInputFinish(String psw) {
        loginBackView.setVisibility(View.GONE);
        mAutoCodeLayout.setVisibility(View.GONE);
        mGetVoiceAuthCodeBtn.setVisibility(View.GONE);
        loginingLayout.setVisibility(View.VISIBLE);

        closeKeyBoard();
        clickLogin();
    }

    void showPhoneNum() {
        if (TextUtils.isEmpty(mPhoneEditText.getEditorText())) {
            autoMobileNum = CommonUtil.getPhone11Num(getWRActivity());
            if (!TextUtils.isEmpty(autoMobileNum)) {
                mPhoneEditText.setEditorText(StringUtil.formatCheckMobileNumber(autoMobileNum, " "));
            }
        }
    }

    @Override
    public void onDestroy() {
        if (smsObserver != null && getWRActivity() != null && getWRActivity().isFinishing()) {
            getWRActivity().getContentResolver().unregisterContentObserver(smsObserver);
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (getWRActivity() != null) {
            ManyiUtils.closeKeyBoard(getWRActivity(), getView());
        }
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

        // Remove the listener to the spring when the Activity pauses.
        mScaleSpring.removeListener(mSpringListener);
        super.onPause();
    }

    /**
     * 弹出软件盘
     *
     * @param editText
     */
    private void showInputMethod(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    /**
     * 设置短信验证码
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleSmsCodeEvent(SmsCodeEvent event) {
        mGpvAuthCode.setPassword(event.getCode());
        mTimerUtil.stopTimer();
        String authCode = StringUtil.allSpaceFormat(mGpvAuthCode.getPassWord());
        if (ValidateUtil.isValidAuthCode(authCode)) {
            LogUtil.d("=======AUTO===LOGIN==========");
            if (!isAutoLogined) {
                isAutoLogined = true;
                closeKeyBoard();
                login();//自动登录
            }
        }
    }

    @OnClick(R.id.login_next_btn)
    void loginNext() {
        closeKeyBoard();
        if (!NetCheckUtil.hasActiveNetwork()) {
            ToastUtil.showInCenter("网络未连接，请检查网络后重试！");
            return;
        }
        if (checkPhone()) {
            mNextLoading.smoothToShow();
            mNextBtn.setText("");
            mPhoneEditText.showClearButton(false);
            mPhoneEditText.getmEditText().setEnabled(false);

            mPhoneNumber = StringUtil.allSpaceFormat(mPhoneEditText.getEditorText());
            sendToGetAuthCodeFirst();
        }
    }

    @OnClick(R.id.login_back)
    void loginBackClick() {
        showFirstView();
    }

    @OnClick(R.id.get_auth_code)
    void getAuthCode() {
        if (!NetCheckUtil.hasActiveNetwork()) {
            ToastUtil.showInCenter("网络未连接，请检查网络后重试！");
            return;
        }

        if (checkPhone()) {
            mPhoneNumber = StringUtil.allSpaceFormat(mPhoneEditText.getEditorText());
            mGpvAuthCode.clearPassword();
            sendToGetAuthCode();
            mGetAuthCodeBtn.setEnabled(false);
            //mGetVoiceAuthCodeBtn.setVisibility(View.GONE);
            showVoiceBtn = true;
            if (showVoiceBtn && !TextUtils.isEmpty(mPhoneNumber)) {
                mGetVoiceAuthCodeBtn.setVisibility(View.VISIBLE);
            }
            mTimerUtil.startTimer();
        }
    }

    @OnClick(R.id.get_voice_auth_code)
    void getVoiceAuthCode() {
        if (!NetCheckUtil.hasActiveNetwork()) {
            ToastUtil.showInCenter("网络未连接，请检查网络后重试！");
            return;
        }

        if (checkPhone()) voiceVerifyCode();
    }

    /*
    @OnClick(R.id.permit_agreement)
    void clickReadText() {
        String url = Configuration.DEFAULT.getDomain() + "/ihouse/policy.html";
        Map<String, String> dataMap = ObjectUtil.newHashMap();
        dataMap.put(WebViewActivity.TITLE, getString(R.string.permit_agreement_title_text));
        dataMap.put(WebViewActivity.URL, url);
        MyIntent.startActivity(getWRActivity(), WebViewActivity.class, dataMap);
    }
    */

    void clickLogin() {
        if (!NetCheckUtil.hasActiveNetwork()) {
            ToastUtil.showInCenter("网络未连接，请检查网络后重试！");
            return;
        }
        //123 EventLog.upEventLog("200", "2");
        if (checkLogin()) {
            if (checkAuthCode()) {
                login();
            }
        }
    }

    private boolean checkPhone() {
        if (!ValidateUtil.isValidMobile(StringUtil.allSpaceFormat(mPhoneEditText.getEditorText()))) {
            ToastUtil.showInCenter("手机号码输入有误");
            return false;
        }
        return true;
    }

    private boolean checkAuthCode() {
        if (!ValidateUtil.isValidAuthCode(StringUtil.allSpaceFormat(mGpvAuthCode.getPassWord()))) {
            ToastUtil.showInCenter("验证码有误");
            return false;
        }
        return true;
    }

    public void login() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        mPhoneNumber = StringUtil.allSpaceFormat(mPhoneEditText.getEditorText());
        List<String> list = new ArrayList<>();
        String[] houseInfos = new String[list.size()];
        houseInfos = list.toArray(houseInfos);
        loginRequest.setHouseIds(houseInfos);
        loginRequest.setMobile(mPhoneNumber);
        loginRequest.setVerifyCode(StringUtil.allSpaceFormat(mGpvAuthCode.getPassWord()));
        loginRequest.setMobileSn(DeviceUtil.getMobileUUID(MyApplication.getInstance()));
        loginRequest.setFromPage(fromPage);
        loginRequest.setSystemVer(DeviceUtil.getSystemVersion());
        loginRequest.setNetType(NetworkUtil.getNetWorkTypeStr(MyApplication.getInstance()));
        loginRequest.setSupport(DeviceUtil.getOperators(MyApplication.getInstance()));
        loginRequest.setClientId(MqttManager.getClientIdByMobile(mPhoneNumber));

        ServiceSender.exec(this, loginRequest, new IwjwRespListener<UserLoginResponse>() {

            @Override
            public void onStart() {
            }

            @Override
            public void onJsonSuccess(UserLoginResponse jsonObject) {
                mUserLoginResponse = jsonObject;
                int code = jsonObject.getErrorCode();
                if (code == 0) {
                    if (null != getWRActivity()) {
                        if (jsonObject.getUserId() > 0) {
                            UserInfo.getInstance().setUserInfoData(jsonObject, mPhoneNumber);
                            //统计登录
                            loginOKEventForNum();

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

                            continueLogin();
                        } else {
                            showMyToast("认证失败，请重试！");
                            onLoginFail();
                        }
                    }
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                onLoginFail();
                showMyToast(errorInfo);
                mGpvAuthCode.clearPassword();
            }
        });
    }

    public void onLoginFail() {
        loginBackView.setVisibility(View.VISIBLE);
        mAutoCodeLayout.setVisibility(View.VISIBLE);
        mGetVoiceAuthCodeBtn.setVisibility(View.VISIBLE);
        loginingLayout.setVisibility(View.GONE);
    }


    /**
     * 获得评价经纪人弹窗初始化信息
     */
    /*
    private void appraiseAgentTask() {
        EvaluationPopScreenRequest request = new EvaluationPopScreenRequest();
        request.setAppointmentId(appointmentId);
        request.setBizType(appointBizType);
        ServiceSender.exec(getContext(), request, new IwjwRespListener<EvaluationPopScreenResponse>(getContext()) {
            @Override
            public void onJsonSuccess(EvaluationPopScreenResponse response) {
                AppraiseAgentDialogUpdate appraiseAgentDialog = new AppraiseAgentDialogUpdate();
                Bundle bundle = new Bundle();
                bundle.putLong("appointmentId", appointmentId);
                bundle.putSerializable("EvaluationPopScreenResponse", response);
                appraiseAgentDialog.setupData(bundle);
                appraiseAgentDialog.setAppraiseAgentInterface(appraiseAgentInterface);
                mFragmentHelper.showDialog(null, appraiseAgentDialog);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                ToastUtil.showInCenter(errorInfo);
                continueLogin();
            }
        });
    }
    */

    /**
     * 获取推荐安装App经纪人列表,并选择推荐经纪人
     */
    /*
    private void setInatllAgentTask() {
        GetInstallAgentRequest request = new GetInstallAgentRequest();
        ServiceSender.exec(getContext(), request, new IwjwRespListener<GetInstallAgentResponse>(getContext()) {
            @Override
            public void onJsonSuccess(GetInstallAgentResponse response) {
                showRecommenderDialog(response);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                continueLogin();
            }
        });
    }
    */

    /*
    public void showRecommenderDialog(GetInstallAgentResponse response) {
        List<AgentInfo> agentModels = response.getAgentModels();
        if ((agentModels != null && agentModels.size() > 0) || response.isFirstLoginShowMobile()) {
            dialog = new RecommenderDialog();
            dialog.setActionClickListener(listener);
            dialog.setListData(agentModels, response.isFirstLoginShowMobile());
            mFragmentHelper.showDialog(getArguments(), dialog);
        } else {
            continueLogin();
        }
    }
    */
    /*
    public void dismissRecommenderDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
    */
    private void continueLogin() {
        //处理登录成功相关事件
        LoginManager.loginSuccess(fromPage, mUserLoginResponse, false);
        dismiss();
    }

    /**
     * 统计自动登录的情况
     */
    private void loginOKEventForNum() {
        if (TextUtils.isEmpty(autoMobileNum)) {
            return;
        }
        /*123
        EventLog.getInstance().init();
        EventLog.getInstance().setActionType(String.valueOf(44));
        EventLog.getInstance().setActionValue(autoMobileNum);
        EventLog.getInstance().upSearchInfo();
        */
    }

    private boolean checkLogin() {
        mPhoneEditText.getmEditText().clearFocus();
        if (!NetCheckUtil.hasActiveNetwork()) {
            showMyToast("请检查网络设置并重试");
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add a listener to the spring when the Activity resumes.
        mScaleSpring.addListener(mSpringListener);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showInputMethod(mPhoneEditText.getmEditText());
        Map<String, Object> dataMap = MyIntent.getData(getArguments());
        if (dataMap != null) {
            fromPage = (int) dataMap.get(LoginManager.LOGIN_FROM);
        }

        mScaleSpring = mSpringSystem.createSpring();
        mScaleSpring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(40, 3));
        mScaleSpring.setEndValue(1);
    }

    public void closeKeyBoard(Context context, View view) {
        if (getWRActivity().getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            //getWRActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setActionViewState() {
        String phoneNum = StringUtil.allSpaceFormat(mPhoneEditText.getEditorText());
        if (!ValidateUtil.isValidMobile(phoneNum)) {
            mNextBtn.setEnabled(false);
        } else {
            mNextBtn.setEnabled(true);
        }
    }

    public void setNextBtnStyle(boolean isEnable) {
        if (isEnable) {
            mNextBtn.setEnabled(isEnable);
            mNextBtn.setClickable(isEnable);
            mNextBtn.setText("下一步");
        } else {
            mNextBtn.setEnabled(isEnable);
            mNextBtn.setClickable(isEnable);
            mNextBtn.setText("下一步");
        }

    }

    private void sendToGetAuthCodeFirst() {
        GetVerifyCodeRequest getVerifyCodeRequest = new GetVerifyCodeRequest();
        getVerifyCodeRequest.setMobile(StringUtil.allSpaceFormat(mPhoneEditText.getEditorText()));
        ServiceSender.exec(this, getVerifyCodeRequest, new IwjwRespListener<Response>() {
            @Override
            public void onStart() {
                isNextBtnEnable = mNextBtn.isEnabled();
                if (isNextBtnEnable) {
                    setNextBtnStyle(false);
                }
                mNextLoading.smoothToShow();
                mNextBtn.setText("");
            }

            @Override
            public void onJsonSuccess(Response jsonObject) {
                setNextBtnStyle(isNextBtnEnable);
                mNextLoading.smoothToHide();
                int code = jsonObject.getErrorCode();
                if (code == 0) {
                    //showMyToast(jsonObject.getMessage());
                    showNextView();
                } else if (code == RestException.GET_VERIFY_CODE_ERROR || code == RestException.GET_VERIFY_CODE_ERROR_TEN_TIMES) {
                    onFailInfo(jsonObject.getMessage());
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                setNextBtnStyle(isNextBtnEnable);
                mNextLoading.smoothToHide();
                mPhoneEditText.showClearButton(true);
                mPhoneEditText.getmEditText().setEnabled(true);
                showMyToast(errorInfo);
            }
        });
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

    private void voiceVerifyCode() {
        //123 EventLog.upEventLog("622", "noReceive");

        VoiceVerifyCodeInitRequest request = new VoiceVerifyCodeInitRequest();
        request.setMobile(StringUtil.allSpaceFormat(mPhoneNumber));
        ServiceSender.exec(this, request, new IwjwRespListener<VoiceVerifyCodeInitResponse>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onJsonSuccess(VoiceVerifyCodeInitResponse jsonObject) {
                if (null != getWRActivity()) {
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
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showMyToast(errorInfo);
            }
        });
    }

    @Override
    public void setupData(Bundle savedInstanceState) {
        mFragmentHelper = new FragmentHelper(((FragmentActivity) getWRActivity()).getSupportFragmentManager());
        smsHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //注册短信监听，用于登录时截获短信验证码（有的手机用BroadcastReceiver的方式无法截获短信验证码，所以这里再加一种方式）
                if (getWRActivity() != null && !getWRActivity().isFinishing()) {
                    smsObserver = new SmsObserver(getWRActivity(), smsHandler);
                    getWRActivity().getContentResolver().registerContentObserver(SmsObserver.SMS_INBOX, true, smsObserver);
                    showPhoneNum();
                }
            }
        }, 250);
    }

    @Override
    public int getLayout() {
        return R.layout.login_dialog_layout;
    }

    public void showMyToast(String msg) {
        if (getActivity() != null) {
            ToastUtil.showInCenter(msg);
        }
    }

    private class ExampleSpringListener extends SimpleSpringListener {
        @Override
        public void onSpringUpdate(Spring spring) {
            // On each update of the spring value, we adjust the scale of the image view to match the
            // springs new value. We use the SpringUtil linear interpolation function mapValueFromRangeToRange
            // to translate the spring's 0 to 1 scale to a 100% to 50% scale range and apply that to the View
            // with setScaleX/Y. Note that rendering is an implementation detail of the application and not
            // Rebound itself. If you need Gingerbread compatibility consider using NineOldAndroids to update
            // your view properties in a backwards compatible manner.
            float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.9);
            mRootView.setScaleX(mappedValue);
            mRootView.setScaleY(mappedValue);
        }
    }

}
