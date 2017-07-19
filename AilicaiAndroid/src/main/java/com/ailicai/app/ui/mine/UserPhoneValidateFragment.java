package com.ailicai.app.ui.mine;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.constants.CommonTag;
import com.ailicai.app.common.constants.EventStr;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.MapUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.NetCheckUtil;
import com.ailicai.app.common.utils.StringUtil;
import com.ailicai.app.common.utils.TimerUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.common.utils.ValidateUtil;
import com.ailicai.app.eventbus.SmsCodeEvent;
import com.ailicai.app.model.request.GetVerifyCodeRequest;
import com.ailicai.app.model.request.UserMobileCheckRequest;
import com.ailicai.app.model.response.UserInfoResponse;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.ManyiEditText;
import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.ManyiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Gerry on 2015/7/3.
 */
public class UserPhoneValidateFragment extends BaseBindFragment {
    @Bind(R.id.user_edit_top_title)
    IWTopTitleView mTopTitleView;

    @Bind(R.id.validate_phone_tips_text)
    TextView mValidateTips;

    @Bind(R.id.auth_code_edit_text)
    ManyiEditText mAuthCodeEditText;

    @Bind(R.id.edit_next_submit)
    Button mEditSubmit;

    @Bind(R.id.get_auth_code)
    Button mGetAuthCode;

    private Map<String, Object> dataMap;

    private TimerUtil mTimerUtil = null;
    private final int TIMER_STARTING = 0x0001;
    private final int TIMER_CANCELED = 0x0002;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTimerUtil = new TimerUtil(mTimerListener, this.getClass().getCanonicalName());
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (getActivity() != null) {
            ManyiUtils.closeKeyBoard(getActivity(), getView());
        }
        if (mTimerUtil.isTimerStarting()) {
            mTimerUtil.pauseTimer();
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

    public TimerUtil.TimerListener mTimerListener = new TimerUtil.TimerListener() {
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

    Handler timerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMER_STARTING:
                    mGetAuthCode.setEnabled(false);
                    String timeNum = MyApplication.getInstance().getString(R.string.auth_code_time_num, msg.arg1);
                    mGetAuthCode.setText(timeNum);
                    break;
                case TIMER_CANCELED:
                    mGetAuthCode.setEnabled(true);
                    mGetAuthCode.setText(R.string.validate_phone_auth_code);
                    break;
            }
        }
    };

    @Override
    public int getLayout() {
        return R.layout.userphone_validate_fragment_layout;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initView();
    }

    //@AfterViews
    public void initView() {
        //设置返回箭头为取消
        //mTopTitleView.setBackBtnText(R.string.user_edit_cancel_back);
        mTopTitleView.setTitleOnClickListener(topTitleOnClickListener);
        //验证码
        mAuthCodeEditText.setFocusChangeInterface(mOnFocusChangeInterface);
        mAuthCodeEditText.setEditorWatchListener(mTextWatcher);
        mAuthCodeEditText.setEditorHintColor(getResources().getColor(R.color.edit_text_hint_color));
        setActionViewState();
        //验证码倒计时继续与否
        mTimerUtil.restartTimer();
    }

    private void setActionViewState() {
        String authCode = mAuthCodeEditText.getEditorText().toString();
        //提交按钮状态
        if (!ValidateUtil.isValidAuthCode(authCode)) {
            mEditSubmit.setEnabled(false);
        } else {
            mEditSubmit.setEnabled(true);
        }
    }

    private ManyiEditText.OnFocusChangeInterface mOnFocusChangeInterface = new ManyiEditText.OnFocusChangeInterface() {
        @Override
        public void hasClearText(View v) {
            if (v.getId() == R.id.auth_code_edit_text) {
                mEditSubmit.setEnabled(false);
            }
        }

        @Override
        public void onViewChange(View v, boolean hasFocus) {
            if (hasFocus) {
                if (v.getId() == R.id.auth_code_edit_text) {
                    EditText mCurrentEdit = mAuthCodeEditText.getmEditText();
                    mCurrentEdit.setSelection(mCurrentEdit.getText().length());
                    mCurrentEdit.setSelected(hasFocus);
                }
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            dataMap = MyIntent.getData(getArguments());
            String p = StringUtil.formatMobileSub(MapUtil.getString(dataMap, CommonTag.PERSONAL_USER_PHONE));
            mValidateTips.setText(getString(R.string.personal_phone_modify_tips_1, p));
        }
        showInputMethod(mAuthCodeEditText.getmEditText());
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setSubmitBtnStateOnSmsChanged();
        }
    };

    IWTopTitleView.TopTitleOnClickListener topTitleOnClickListener = new IWTopTitleView.TopTitleOnClickListener() {
        @Override
        public boolean onBackClick() {
            if (getActivity() != null) {
                getActivity().finish();
            }
            return true;
        }
    };

    @OnClick(R.id.get_auth_code)
    public void getAuthCode() {
        if (!NetCheckUtil.hasActiveNetwork()) {
            ToastUtil.showInCenter("网络未连接，请检查网络后重试！");
            return;
        }
        /**
         * 客户端校验手机号是否正确
         */
        if (!checkPhone()) {
            return;
        }
        EditText mCurrentEdit = mAuthCodeEditText.getmEditText();
        mCurrentEdit.setFocusable(true);
        mCurrentEdit.requestFocus();
        sendToGetAuthCode();
        mGetAuthCode.setEnabled(false);
        mTimerUtil.startTimer();
    }

    @OnClick(R.id.edit_next_submit)
    public void submit() {
        /**
         * 客户端校验验证码和手机号是否正确
         */
        if (!checkPhone() || !checkAuthCode()) {
            return;
        }
        ManyiAnalysis.getInstance().onEvent(EventStr.MINE_CHANGE_MOBILE_NEXY_STEP_CONFIRM);
        setSubmitEnabled(false);
        sendNextToSubmit();
    }

    public void sendToGetAuthCode() {
        GetVerifyCodeRequest getVerifyCodeRequest = new GetVerifyCodeRequest();
        getVerifyCodeRequest.setType(1);
        getVerifyCodeRequest.setMobile(MapUtil.getString(dataMap, CommonTag.PERSONAL_USER_PHONE));
        ServiceSender.exec(this, getVerifyCodeRequest, new IwjwRespListener<Response>() {
            @Override
            public void onJsonSuccess(Response jsonObject) {
                String msg = jsonObject.getMessage();
                if (!"".equals(msg)) {
                    showMessage(jsonObject.getMessage());
                }
                if (jsonObject.getErrorCode() == 0) {
                    //showMessage("验证码已发送到您手机，请查收");
                } else {
                    mTimerUtil.stopTimer();
                }
            }
        });
    }

    /**
     * 老手机号码验证
     */
    public void sendNextToSubmit() {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        UserMobileCheckRequest userMobileCheckRequest = new UserMobileCheckRequest();
        userMobileCheckRequest.setMobile(MapUtil.getString(dataMap, CommonTag.PERSONAL_USER_PHONE));
        userMobileCheckRequest.setVerifyCode(mAuthCodeEditText.getEditorText().toString().trim());
        ServiceSender.exec(this, userMobileCheckRequest, new IwjwRespListener<UserInfoResponse>(this) {

            @Override
            public void onFailInfo(String errorInfo) {
                setSubmitEnabled(true);
                showMessage(errorInfo);
            }

            @Override
            public void onJsonSuccess(UserInfoResponse jsonObject) {
                String msg = jsonObject.getMessage();
                int code = jsonObject.getErrorCode();
                if (!"".equals(msg)) {
                    showMessage(jsonObject.getMessage());
                }
                setSubmitEnabled(true);
                if (code == 0) {
                    showMessage(R.string.personal_phone_validate_success);
                    goNextModifyPage();
                } else {
                    showMessage(jsonObject.getMessage());
                }
            }
        });
    }

    /**
     * 跳转至新手机变更页面
     */
    public void goNextModifyPage() {
        if (getActivity() != null) {
            MyIntent.startActivity(getActivity(), UserPhoneModifyActivity.class, dataMap);
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }

    /**
     * 客户端校验手机号是否正确
     *
     * @return
     */
    public boolean checkPhone() {
        if (!ValidateUtil.isValidMobile(MapUtil.getString(dataMap, CommonTag.PERSONAL_USER_PHONE))) {
            showMessage("手机号码有误");
            return false;
        }
        return true;
    }

    /**
     * 客户端校验验证码是否正确
     *
     * @return
     */
    public boolean checkAuthCode() {
        String authCode = mAuthCodeEditText.getEditorText().toString().trim();
        if (!ValidateUtil.isValidAuthCode(authCode)) {
            showMessage("验证码有误");
            return false;
        }
        return true;
    }

    /**
     * 设置短信验证码
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleSmsCodeEvent(SmsCodeEvent event) {
        EditText mCurrentEdit = mAuthCodeEditText.getmEditText();
        mAuthCodeEditText.setEditorText(event.getCode());
        //验证码自动填充后关闭一下键盘
        if (getActivity() != null) {
            ManyiUtils.closeKeyBoard(getActivity(), getView());
        }
        mCurrentEdit.setFocusable(true);
        mCurrentEdit.setSelection(event.getCode().length());
        mTimerUtil.stopTimer();
        setSubmitBtnStateOnSmsChanged();
    }

    /**
     * 校验手机号和验证码是否正确，提交按钮是否可操作
     */
    private void setSubmitBtnStateOnSmsChanged() {
        String authCode = mAuthCodeEditText.getEditorText().toString().trim();
        String phoneNum = MapUtil.getString(dataMap, CommonTag.PERSONAL_USER_PHONE);
        if (!ValidateUtil.isValidAuthCode(authCode) || !ValidateUtil.isValidMobile(phoneNum)) {
            mEditSubmit.setEnabled(false);
        } else {
            mEditSubmit.setEnabled(true);
        }
    }

    public void setSubmitEnabled(boolean enabled) {
        mEditSubmit.setEnabled(enabled);
    }

    public void showMessage(int resId) {
        if (getActivity() != null) {
            ToastUtil.showInCenter(getString(resId));
        }
    }

    public void showMessage(String msg) {
        if (getActivity() != null) {
            ToastUtil.showInCenter(msg);
        }
    }
}

