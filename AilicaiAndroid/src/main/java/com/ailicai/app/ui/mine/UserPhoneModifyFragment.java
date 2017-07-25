package com.ailicai.app.ui.mine;

import android.app.Activity;
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
import com.ailicai.app.eventbus.EditUserInfoEvent;
import com.ailicai.app.eventbus.SmsCodeEvent;
import com.ailicai.app.model.request.GetVerifyCodeRequest;
import com.ailicai.app.model.response.UserInfoResponse;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.ManyiEditText;
import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DeviceUtil;
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
public class UserPhoneModifyFragment extends BaseBindFragment {
    private final int TIMER_STARTING = 0x0001;
    private final int TIMER_CANCELED = 0x0002;
    @Bind(R.id.user_edit_top_title)
    IWTopTitleView mTopTitleView;
    @Bind(R.id.new_phone_edit_text)
    ManyiEditText mNewPhoneEditText;
    @Bind(R.id.auth_code_edit_text)
    ManyiEditText mAuthCodeEditText;
    @Bind(R.id.edit_modify_submit)
    Button mEditSubmitBtn;
    @Bind(R.id.get_auth_code)
    Button mGetAuthCodeBtn;
    String phoneStr = "";
    boolean canDel = false;
    Handler timerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMER_STARTING:
                    mGetAuthCodeBtn.setEnabled(false);
                    String timeNum = MyApplication.getInstance().getString(R.string.auth_code_time_num, msg.arg1);
                    mGetAuthCodeBtn.setText(timeNum);
                    break;
                case TIMER_CANCELED:
                    mGetAuthCodeBtn.setEnabled(true);
                    mGetAuthCodeBtn.setText(R.string.validate_phone_auth_code);
                    break;
            }
        }
    };
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
    IWTopTitleView.TopTitleOnClickListener topTitleOnClickListener = new IWTopTitleView.TopTitleOnClickListener() {
        @Override
        public boolean onBackClick() {
            if (getActivity() != null) {
                getActivity().finish();
            }
            return true;
        }
    };
    private Map<String, Object> dataMap;
    private Context mContext;
    private EditText mCurrentEdit;
    private TimerUtil mTimerUtil = null;
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
                if (v.getId() == R.id.auth_code_edit_text) {
                    mCurrentEdit = mAuthCodeEditText.getmEditText();
                    mCurrentEdit.setSelection(mCurrentEdit.getText().length());
                    mCurrentEdit.setSelected(hasFocus);
                    //检测手机格式
                    String phoneNum = StringUtil.allSpaceFormat(mNewPhoneEditText.getEditorText().toString());
                    String phoneStr = StringUtil.formatCheckMobileNumber(phoneNum, " ");
                    mNewPhoneEditText.setEditorText(phoneStr);
                } else {
                    mCurrentEdit = mNewPhoneEditText.getmEditText();
                    mCurrentEdit.setSelection(mCurrentEdit.getText().length());
                    mCurrentEdit.setSelected(hasFocus);
                }
            }
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
                    if (before > 0 && _buffer.charAt(start) == ' ') {
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
            if (_formatable) {
                _formatable = false;
                String formatMobile = formatMobile(_buffer.toString());
                //以防意外.
                if (_selection > formatMobile.length()) {
                    _selection = formatMobile.length();
                }
                mNewPhoneEditText.setEditorText(formatMobile);
            } else {
                _formatable = true;
            }
            mNewPhoneEditText.getmEditText().setSelection(_selection);
            setActionViewState();
        }
    };

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onPause() {
        if (getActivity() != null) {
            ManyiUtils.closeKeyBoard(getActivity(), getView());
        }
        //倒计时继续该页面不需要
        /*if (mTimerUtil.isTimerStarting()) {
            mTimerUtil.pauseTimer();
        }*/

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

    @Override
    public int getLayout() {
        return R.layout.userphone_modify_fragment_layout;
    }

//    private TextWatcher mPhoneTextWatch = new TextWatcher() {
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            if (s.toString().trim().length() == 3) {
//                canDel = true;
//            } else {
//                canDel = false;
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            //Temp::136 6409 0019中间加入2个空格
//            /**
//             if (s.length() == 3 && !canDel) {
//             phoneStr = s.toString() + " ";
//             mNewPhoneEditText.setEditorText(phoneStr);
//             mNewPhoneEditText.getmEditText().setSelection(phoneStr.length());
//             } else if (s.length() == phoneStr.length() + 4 && s.length() != 11 + 2) {
//             phoneStr = s.toString() + " ";
//             mNewPhoneEditText.setEditorText(phoneStr);
//             mNewPhoneEditText.getmEditText().setSelection(phoneStr.length());
//             }
//             */
//
//            //Temp::136 6409 0019中间加入2个空格
//            String afterText = s.toString().trim();
//            if (afterText.length() == 3 && !canDel) {
//                phoneStr = afterText + " ";
//                mNewPhoneEditText.setEditorText(phoneStr);
//                mNewPhoneEditText.getmEditText().setSelection(phoneStr.length());
//            } else if (afterText.length() == 4) {
//                phoneStr = afterText.substring(0, 3) + " " + afterText.substring(3, afterText.length());
//                mNewPhoneEditText.setEditorText(phoneStr);
//                mNewPhoneEditText.getmEditText().setSelection(phoneStr.length());
//            } else if (afterText.length() == 9) {
//                phoneStr = afterText.substring(0, 8) + " " + afterText.substring(8, afterText.length());
//                mNewPhoneEditText.setEditorText(phoneStr);
//                mNewPhoneEditText.getmEditText().setSelection(phoneStr.length());
//            } else if (StringUtil.allSpaceFormat(afterText).length() == 12) {//删除一个空格输入一个数字
//                phoneStr = StringUtil.formatMobileNumber(StringUtil.allSpaceFormat(afterText), " ");
//                mNewPhoneEditText.setEditorText(phoneStr);
//                mNewPhoneEditText.getmEditText().setSelection(phoneStr.length());
//            } else if (StringUtil.allSpaceFormat(afterText).length() == 13) {//删除两个空格输入两个数字
//                phoneStr = StringUtil.formatMobileNumber(StringUtil.allSpaceFormat(afterText), " ");
//                mNewPhoneEditText.setEditorText(phoneStr);
//                mNewPhoneEditText.getmEditText().setSelection(phoneStr.length());
//            }
//            setActionViewState();
//        }
//    };

    @Override
    public void init(Bundle savedInstanceState) {
        initView();
    }

    //@AfterViews
    public void initView() {
        //设置返回箭头为取消
        //mTopTitleView.setBackBtnText(R.string.user_edit_cancel_back);
        mTopTitleView.setTitleOnClickListener(topTitleOnClickListener);

        //手机号
        mNewPhoneEditText.setFocusChangeInterface(mOnFocusChangeInterface);
        mNewPhoneEditText.setEditorWatchListener(mPhoneTextWatch);
        mNewPhoneEditText.setFocusable(true);
        mNewPhoneEditText.setEditorHintColor(getResources().getColor(R.color.edit_text_hint_color));
        //验证码
        mAuthCodeEditText.setFocusChangeInterface(mOnFocusChangeInterface);
        mAuthCodeEditText.setEditorWatchListener(mAuthTextWatch);
        mAuthCodeEditText.setEditorHintColor(getResources().getColor(R.color.edit_text_hint_color));
        setActionViewState();
        //倒计时继续该页面不需要
        //mTimerUtil.restartTimer();
    }

    private void setActionViewState() {
        String phoneNum = mNewPhoneEditText.getEditorText().toString();
        String authCode = mAuthCodeEditText.getEditorText().toString();
        //发送验证码按钮状态
        if (mTimerUtil.isTimerStarting()) {
            mGetAuthCodeBtn.setEnabled(false);
        } else {
            if (!ValidateUtil.isValidMobile(StringUtil.allSpaceFormat(phoneNum))) {
                mGetAuthCodeBtn.setEnabled(false);
            } else {
                mGetAuthCodeBtn.setEnabled(true);
            }
        }
        //完成按钮状态
        if (!ValidateUtil.isValidAuthCode(StringUtil.allSpaceFormat(authCode))
                || !ValidateUtil.isValidMobile(StringUtil.allSpaceFormat(phoneNum))) {
            mEditSubmitBtn.setEnabled(false);
        } else {
            mEditSubmitBtn.setEnabled(true);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            dataMap = MyIntent.getData(getArguments());
        }
        showInputMethod(mNewPhoneEditText.getmEditText());
    }

    /**
     * 自动填充短信验证码
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleSmsCodeEvent(SmsCodeEvent event) {
        mAuthCodeEditText.setEditorText(event.getCode());
        mAuthCodeEditText.getmEditText().setFocusable(true);
        mAuthCodeEditText.getmEditText().requestFocus();
        mAuthCodeEditText.getmEditText().setSelection(event.getCode().length());
        //验证码自动填充后关闭一下键盘
        if (getActivity() != null) {
            ManyiUtils.closeKeyBoard(getActivity(), getView());
        }
        mTimerUtil.stopTimer();
        setSubmitBtnStateOnSmsChanged();
    }

    private void setSubmitBtnStateOnSmsChanged() {
        String authCode = mAuthCodeEditText.getEditorText().toString();
        String phoneNum = mNewPhoneEditText.getEditorText().toString();
        if (!ValidateUtil.isValidAuthCode(StringUtil.allSpaceFormat(authCode))
                || !ValidateUtil.isValidMobile(StringUtil.allSpaceFormat(phoneNum))) {
            mEditSubmitBtn.setEnabled(false);
        } else {
            mEditSubmitBtn.setEnabled(true);
        }
    }

    public void sendToGetAuthCode() {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        String phoneNum = mNewPhoneEditText.getEditorText().toString();
        GetVerifyCodeRequest getVerifyCodeRequest = new GetVerifyCodeRequest();
        getVerifyCodeRequest.setType(2);
        getVerifyCodeRequest.setMobile(StringUtil.allSpaceFormat(phoneNum));
        ServiceSender.exec(this, getVerifyCodeRequest, new IwjwRespListener<Response>(this) {
            @Override
            public void onJsonSuccess(Response jsonObject) {
                int code = jsonObject.getErrorCode();
                if (code == 0) {
                    showContentView();
                    showMessage(jsonObject.getMessage());
                } else if (code == RestException.GET_VERIFY_CODE_ERROR || code == RestException.GET_VERIFY_CODE_ERROR_TEN_TIMES) {
                    onFailInfo(jsonObject.getMessage());
                } else {
                    onFailInfo(jsonObject.getMessage());
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showContentView();
                showMessage(errorInfo);
                mTimerUtil.stopTimer();
            }
        });
    }

    public void sendToSubmit() {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        String phoneNum = mNewPhoneEditText.getEditorText().toString();
        String authCode = mAuthCodeEditText.getEditorText().toString().trim();
        UserMobileChangeRequest userMobileChangeRequest = new UserMobileChangeRequest();
        userMobileChangeRequest.setUserId(MapUtil.getLong(dataMap, CommonTag.PERSONAL_USER_ID));
        userMobileChangeRequest.setMobile(StringUtil.allSpaceFormat(phoneNum));
        userMobileChangeRequest.setVerifyCode(StringUtil.allSpaceFormat(authCode));
        userMobileChangeRequest.setMobileSn(DeviceUtil.getIMEI(MyApplication.getInstance()));
        ServiceSender.exec(this, userMobileChangeRequest, new IwjwRespListener<UserInfoResponse>(this) {
            @Override
            public void onJsonSuccess(UserInfoResponse jsonObject) {
                setSubmitEnabled(true);
                String msg = jsonObject.getMessage();
                int code = jsonObject.getErrorCode();
                if (!"".equals(msg)) {
                    showMessage(jsonObject.getMessage());
                }
                if (code == 0) {
                    showMessage(R.string.personal_phone_modify_success);
                    onSubmitSuccess(jsonObject);
                } else {
                    onFailInfo(jsonObject.getMessage());
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showMessage(errorInfo);
            }
        });
    }

    /**
     * 变更手机号码成功
     */
    public void onSubmitSuccess(UserInfoResponse response) {
        EditUserInfoEvent event = new EditUserInfoEvent();
        event.setUserId(response.getUserId());
        event.setRealName(response.getRealName());
        event.setGender(response.getGender());
        event.setMobile(response.getMobile());
        EventBus.getDefault().post(event);
        LoginManager.updateUserInfoData();
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @OnClick(R.id.get_auth_code)
    public void getAuthCode() {
        if (!NetCheckUtil.hasActiveNetwork()) {
            ToastUtil.showInCenter("网络未连接，请检查网络后重试！");
            return;
        }

        if (checkPhone()) {
            mAuthCodeEditText.getmEditText().setFocusable(true);
            mAuthCodeEditText.getmEditText().requestFocus();
            sendToGetAuthCode();
            mGetAuthCodeBtn.setEnabled(false);
            mTimerUtil.startTimer();
        }
    }

    @OnClick(R.id.edit_modify_submit)
    public void submit() {
        if (!NetCheckUtil.hasActiveNetwork()) {
            ToastUtil.showInCenter("网络未连接，请检查网络后重试！");
            return;
        }

        if (checkAuthCode()) {
            ManyiAnalysis.getInstance().onEvent(EventStr.MINE_CHANGE_MOBILE_SUCCESS_CONFIRM);
            mEditSubmitBtn.setEnabled(false);
            sendToSubmit();
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
        if (!ValidateUtil.isValidAuthCode(StringUtil.allSpaceFormat(authCode))) {
            showMessage("验证码有误");
            return false;
        }
        return true;
    }

    public void setSubmitEnabled(boolean enabled) {
        mEditSubmitBtn.setEnabled(enabled);
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

