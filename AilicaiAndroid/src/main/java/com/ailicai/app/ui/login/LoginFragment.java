package com.ailicai.app.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.utils.NetCheckUtil;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.StringUtil;
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
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.widget.DialogBuilder;
import com.ailicai.app.widget.ManyiEditText;
import com.ailicai.app.widget.TextViewTF;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Configuration;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.util.ManyiUtils;
import com.huoqiu.framework.util.NetworkUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 用户登录
 */
public class LoginFragment extends BaseBindFragment {
    private int fromPage = LoginManager.LOGIN_FROM_MINE;
    private final int TIMER_STARTING = 0x0001;
    private final int TIMER_CANCELED = 0x0002;
    @Bind(R.id.close_button)
    TextViewTF mCloseBtn;
    @Bind(R.id.phone_edit)
    ManyiEditText mPhoneEditText;
    @Bind(R.id.auth_edit)
    ManyiEditText mAuthEditeText;
    @Bind(R.id.get_auth_code)
    Button mGetAuthCodeBtn;
    @Bind(R.id.get_voice_auth_code)
    Button mGetVoiceAuthCodeBtn;
    @Bind(R.id.permit_agreement)
    TextView mPermitTextView;
    @Bind(R.id.login_btn)
    Button mLoginBtn;

    private boolean showVoiceBtn = true;
    private long appointmentId;
    private int appointBizType;

    //private RecommenderDialog dialog;

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

                    if (msg.arg1 == 30 && showVoiceBtn && !TextUtils.isEmpty(mPhoneEditText.getEditorText())) {
                        mGetVoiceAuthCodeBtn.setVisibility(View.VISIBLE);
                    }
                    break;
                case TIMER_CANCELED:
                    mGetAuthCodeBtn.setEnabled(true);
                    mGetAuthCodeBtn.setText(R.string.validate_phone_auth_code);
                    break;
            }
        }


    };
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
    private EditText mCurrentEdit;
    private String mPhoneNumber;
    private boolean isAutoLogined = false;
    private View.OnClickListener mCloseBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null != getWRActivity()) {
                ManyiUtils.closeKeyBoard(getWRActivity(), mCurrentEdit);
                LoginManager.loginCancel();
                getWRActivity().finish();
            }
        }
    };
    private ManyiEditText.OnFocusChangeInterface mOnFocusChangeInterface = new ManyiEditText.OnFocusChangeInterface() {
        @Override
        public void hasClearText(View v) {
            if (v.getId() == R.id.phone_edit) {
                mGetAuthCodeBtn.setEnabled(false);
            }
        }

        @Override
        public void onViewChange(View v, boolean hasFocus) {
            if (hasFocus) {
                if (v.getId() == R.id.auth_edit) {
                    mCurrentEdit = mAuthEditeText.getmEditText();
                    mCurrentEdit.setSelection(mCurrentEdit.getText().length());
                    mCurrentEdit.setSelected(true);
                } else {
                    mCurrentEdit = mPhoneEditText.getmEditText();
                    mCurrentEdit.setSelection(mCurrentEdit.getText().length());
                    mCurrentEdit.setSelected(true);
                }
            }
            setEditTagOnFocus(v, hasFocus);
        }
    };
    private TextWatcher mAuthTextWatch = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            setActionViewState();
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
                showVoiceBtn = false;
                mGetVoiceAuthCodeBtn.setVisibility(View.GONE);
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

    @Override
    public int getLayout() {
        return R.layout.login_layout;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mTimerUtil = new TimerUtil(mTimerListener, this.getClass().getCanonicalName());
        EventBus.getDefault().register(this);

        //关闭按钮点击事件
        mCloseBtn.setOnClickListener(mCloseBtnClick);
        //爱屋吉屋软件许可及服务协议
        String permitStr = getString(R.string.permit_agreement_text);
        mPermitTextView.setText(Html.fromHtml(permitStr));
        //手机号
        String savePhone = MyPreference.getInstance().read(UserInfo.USERINFO_KEY_MOBILE, "");
        mPhoneEditText.setEditorText(savePhone);
        mPhoneEditText.setFocusChangeInterface(mOnFocusChangeInterface);
        mPhoneEditText.setEditorWatchListener(mPhoneTextWatch);
        mPhoneEditText.setFocusable(true);
        mPhoneEditText.setEditorHintColor(getResources().getColor(R.color.login_edit_text_hint_color));
        //验证码
        mAuthEditeText.setFocusChangeInterface(mOnFocusChangeInterface);
        mAuthEditeText.setEditorWatchListener(mAuthTextWatch);
        mAuthEditeText.setEditorHintColor(getResources().getColor(R.color.login_edit_text_hint_color));
        setActionViewState();
    }

    void showPhoneNum() {
        if (TextUtils.isEmpty(mPhoneEditText.getEditorText())) {
            autoMobileNum = CommonUtil.getPhone11Num(getWRActivity());
            if (!TextUtils.isEmpty(autoMobileNum)) {
                mPhoneEditText.setEditorText(autoMobileNum);
            }
        }
    }

    private SmsObserver smsObserver;

    public Handler smsHandler = new Handler() {
    };

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public void onDestroy() {
        if (smsObserver != null && getWRActivity() != null && getWRActivity().isFinishing()) {
            getWRActivity().getContentResolver().unregisterContentObserver(smsObserver);
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //反注册短信监听

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
        mAuthEditeText.setEditorText(event.getCode());
        mAuthEditeText.getmEditText().setFocusable(true);
        mAuthEditeText.getmEditText().requestFocus();
        mAuthEditeText.getmEditText().setSelection(event.getCode().length());
        mTimerUtil.stopTimer();
        setLoginBtnStateOnSmsChanged();
    }

    @OnClick(R.id.get_auth_code)
    void getAuthCode() {
        if (!NetCheckUtil.hasActiveNetwork()) {
            ToastUtil.showInCenter("网络未连接，请检查网络后重试！");
            return;
        }

        if (checkPhone()) {
            mPhoneNumber = StringUtil.allSpaceFormat(mPhoneEditText.getEditorText());
            mAuthEditeText.getmEditText().setFocusable(true);
            mAuthEditeText.getmEditText().requestFocus();
            sendToGetAuthCode();
            mGetAuthCodeBtn.setEnabled(false);
            mGetVoiceAuthCodeBtn.setVisibility(View.GONE);
            showVoiceBtn = true;
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

    @OnClick(R.id.login_btn)
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
        if (!ValidateUtil.isValidAuthCode(StringUtil.allSpaceFormat(mAuthEditeText.getEditorText()))) {
            ToastUtil.showInCenter("验证码有误");
            return false;
        }
        return true;
    }

    @OnClick(R.id.permit_agreement)
    void clickReadText() {
        String url = Configuration.DEFAULT.getDomain() + "/ihouse/policy.html";
        Map<String, String> dataMap = ObjectUtil.newHashMap();
        dataMap.put(WebViewActivity.TITLE, getString(R.string.permit_agreement_title_text));
        dataMap.put(WebViewActivity.URL, url);
        MyIntent.startActivity(getWRActivity(), WebViewActivity.class, dataMap);
    }

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
                            EventLog.upEventLog("869", agentId + "");
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

    public void login() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        mPhoneNumber = StringUtil.allSpaceFormat(mPhoneEditText.getEditorText());
        List<String> list = new ArrayList<>();
        String[] houseInfos = new String[list.size()];
        houseInfos = list.toArray(houseInfos);
        loginRequest.setHouseIds(houseInfos);
        loginRequest.setMobile(mPhoneNumber);
        loginRequest.setVerifyCode(StringUtil.allSpaceFormat(mAuthEditeText.getEditorText()));
        loginRequest.setMobileSn(DeviceUtil.getMobileUUID(MyApplication.getInstance()));
        loginRequest.setFromPage(fromPage);
        loginRequest.setSystemVer(DeviceUtil.getSystemVersion());
        loginRequest.setNetType(NetworkUtil.getNetWorkTypeStr(MyApplication.getInstance()));
        loginRequest.setSupport(DeviceUtil.getOperators(MyApplication.getInstance()));
        //TODO:NEW APP
        // loginRequest.setClientId(MqttManager.getClientIdByMobile(mPhoneNumber));

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
                    if (null != getWRActivity()) {
                        if (jsonObject.getUserId() > 0) {
                            if (getWRActivity() instanceof LoginActivity) {

                                UserInfo.getInstance().setUserInfoData(jsonObject, mPhoneNumber);
                                //统计登录
                                loginOKEventForNum();
                                ManyiUtils.closeKeyBoard(getWRActivity(), mCurrentEdit);

                                //TODO:NEW APP
                                // PushUtil.resetMqttService(getWRActivity());
                            }

                            if (jsonObject.getFirstLogin() == 0) { //0-首次 1-否
                                //TODO:NEW APP
                                // setInatllAgentTask();
                            } else {
                                appointmentId = jsonObject.getAppointmentId();
                                appointBizType = jsonObject.getAppointBizType();
                                if (jsonObject.getAppointmentId() > 0) {
                                    //TODO:NEW APP
                                    // appraiseAgentTask();
                                } else {
                                    continueLogin();
                                }
                            }
                        } else {
                            ManyiUtils.closeKeyBoard(getWRActivity(), mCurrentEdit);
                            showMyToast("认证失败，请重试！");
                        }
                    }
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showContentView();
                showErrorDialog(errorInfo);
            }
        });
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

    AppraiseAgentDialogUpdate.AppraiseAgentInterface appraiseAgentInterface = new AppraiseAgentDialogUpdate.AppraiseAgentInterface() {
        @Override
        public void appraiseAgentOver() {
            continueLogin();
        }
    };
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

    public void dismissRecommenderDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
    */

    private void continueLogin() {
        getWRActivity().finish();
        //处理登录成功相关事件
        LoginManager.loginSuccess(fromPage);
    }

    /**
     * 统计自动登录的情况
     */
    private void loginOKEventForNum() {
        if (TextUtils.isEmpty(autoMobileNum)) {
            return;
        }
        //123 EventLog.getInstance().init();
        //123 EventLog.getInstance().setActionType(String.valueOf(44));
        //123 EventLog.getInstance().setActionValue(autoMobileNum);
        //123 EventLog.getInstance().upSearchInfo();
    }


    private boolean checkLogin() {
        mAuthEditeText.clearFocus();
        mPhoneEditText.getmEditText().clearFocus();
        if (!NetCheckUtil.hasActiveNetwork()) {
            showErrorDialog("请检查网络设置并重试");
            return false;
        }
        return true;
    }

    public void showErrorDialog(String str) {
        mLoginBtn.setEnabled(true);
        mLoginBtn.setClickable(true);
        mAuthEditeText.getmEditText().setFocusable(true);
        mAuthEditeText.getmEditText().requestFocus();
        mAuthEditeText.getmEditText().setSelection(mAuthEditeText.getmEditText().getText().length());
        if (isAdded() && null != getWRActivity()) {
            mLoginBtn.setText(getWRActivity().getString(R.string.to_login_text));
        }
        showMyToast(str);
    }

    /**
     * 自动获取到的电话号码
     */
    String autoMobileNum;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showInputMethod(mPhoneEditText.getmEditText());
        Map<String, Object> dataMap = MyIntent.getData(getArguments());
        if (dataMap != null) {
            fromPage = (int) dataMap.get(LoginManager.LOGIN_FROM);
        }
    }

    public void closeKeyBoard(Context context, View view) {
        if (getWRActivity().getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            //getWRActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setActionViewState() {
        String authCode = StringUtil.allSpaceFormat(mAuthEditeText.getEditorText());
        String phoneNum = StringUtil.allSpaceFormat(mPhoneEditText.getEditorText());
        //发送验证码按钮状态
        if (mTimerUtil.isTimerStarting()) {
            mGetAuthCodeBtn.setEnabled(false);
        } else {
            if (!ValidateUtil.isValidMobile(phoneNum)) {
                mGetAuthCodeBtn.setEnabled(false);
            } else {
                mGetAuthCodeBtn.setEnabled(true);
            }
        }
        //登录按钮状态
        if (!ValidateUtil.isValidAuthCode(authCode)
                || !ValidateUtil.isValidMobile(phoneNum)) {
            mLoginBtn.setEnabled(false);
        } else {
            mLoginBtn.setEnabled(true);
        }
    }

    /**
     * 处理获取焦点情况下的iconfont设置
     *
     * @param v
     * @param hasFocus
     */
    private void setEditTagOnFocus(View v, boolean hasFocus) {
        if (hasFocus) {
            String phoneNum = StringUtil.allSpaceFormat(mPhoneEditText.getEditorText());
            switch (v.getId()) {
                case R.id.auth_edit:
                    //检测手机格式
                    String phoneStr = StringUtil.formatCheckMobileNumber(phoneNum, " ");
                    mPhoneEditText.setEditorText(phoneStr);
                    break;
                default:
                    break;
            }
        }
    }

    private void setLoginBtnStateOnSmsChanged() {
        String authCode = StringUtil.allSpaceFormat(mAuthEditeText.getEditorText());
        String phoneNum = StringUtil.allSpaceFormat(mPhoneEditText.getEditorText());
        if (!ValidateUtil.isValidAuthCode(authCode) || !ValidateUtil.isValidMobile(phoneNum)) {
            mLoginBtn.setEnabled(false);
        } else {
            mLoginBtn.setEnabled(true);
            mLoginBtn.setClickable(false);
            mLoginBtn.setText(getString(R.string.login_auto_text));
            //5.2再测试是否有重复登录
            LogUtil.d("=======AUTO===LOGIN==========");
            if (!isAutoLogined) {
                isAutoLogined = true;
                login();//自动登录
            }
        }
    }

    private boolean isEnable;

    private void sendToGetAuthCode() {
        GetVerifyCodeRequest getVerifyCodeRequest = new GetVerifyCodeRequest();
        getVerifyCodeRequest.setMobile(StringUtil.allSpaceFormat(mPhoneEditText.getEditorText()));
        ServiceSender.exec(this, getVerifyCodeRequest, new IwjwRespListener<Response>() {
            @Override
            public void onStart() {
                showLoadTranstView();
                isEnable = mLoginBtn.isEnabled();
                if (isEnable) {
                    mLoginBtn.setEnabled(false);
                }
            }

            @Override
            public void onJsonSuccess(Response jsonObject) {
                mLoginBtn.setEnabled(isEnable);
                int code = jsonObject.getErrorCode();
                if (code == 0) {
                    showContentView();
                    showMyToast(jsonObject.getMessage());
                } else if (code == RestException.GET_VERIFY_CODE_ERROR || code == RestException.GET_VERIFY_CODE_ERROR_TEN_TIMES) {
                    onFailInfo(jsonObject.getMessage());
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                mLoginBtn.setEnabled(isEnable);
                showContentView();
                showErrorDialog(errorInfo);
                mTimerUtil.stopTimer();
            }
        });
    }

    private void sendToVoiceVerifyCode() {
        SendVoiceVerifyCodeRequest request = new SendVoiceVerifyCodeRequest();
        request.setMobile(StringUtil.allSpaceFormat(mPhoneEditText.getEditorText()));
        request.setAccount(account);
        ServiceSender.exec(this, request, new IwjwRespListener<Response>() {
            @Override
            public void onJsonSuccess(Response jsonObject) {
                ToastUtil.showInCenter(jsonObject.getMessage());
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showErrorDialog(errorInfo);
            }
        });
    }

    private String account;

    private void voiceVerifyCode() {
        //123   EventLog.upEventLog("622", "noReceive");

        VoiceVerifyCodeInitRequest request = new VoiceVerifyCodeInitRequest();
        request.setMobile(StringUtil.allSpaceFormat(mPhoneEditText.getEditorText()));
        ServiceSender.exec(this, request, new IwjwRespListener<VoiceVerifyCodeInitResponse>() {
            @Override
            public void onStart() {
                showLoadTranstView();
            }

            @Override
            public void onJsonSuccess(VoiceVerifyCodeInitResponse jsonObject) {
                showContentView();
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
                showContentView();
                showErrorDialog(errorInfo);
            }
        });
    }
}