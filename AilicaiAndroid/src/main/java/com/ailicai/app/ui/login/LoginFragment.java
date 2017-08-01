package com.ailicai.app.ui.login;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.utils.NetCheckUtil;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.StringUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.common.utils.ValidateUtil;
import com.ailicai.app.model.request.GetVerifyCodeRequest;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.widget.ManyiEditText;
import com.ailicai.app.widget.TextViewTF;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Configuration;
import com.huoqiu.framework.rest.Response;
import com.wang.avi.AVLoadingIndicatorView;

import java.lang.ref.WeakReference;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 用户登录
 */
public class LoginFragment extends BaseBindFragment {
    @Bind(R.id.close_button)
    TextViewTF mCloseBtn;
    @Bind(R.id.login_next_btn)
    TextView mNextBtn;
    @Bind(R.id.next_loading)
    AVLoadingIndicatorView mNextLoading;
    @Bind(R.id.phone_edit)
    ManyiEditText mPhoneEditText;
    @Bind(R.id.permit_agreement)
    TextView mPermitTextView;
    /**
     * 自动获取到的电话号码
     */
    String autoMobileNum;
    private WeakReference<Activity> activityWeakReference;
    private boolean isNextBtnEnable;
    private String mPhoneNumber;
    private View.OnClickListener mCloseBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null != getWRActivity()) {
                closeKeyBoard();
                LoginManager.loginCancel();
                getWRActivity().finish();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        SystemUtil.hideKeyboard(mPhoneEditText);
    }

    @Override
    public int getLayout() {
        return R.layout.login_layout;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        //关闭按钮点击事件
        mCloseBtn.setOnClickListener(mCloseBtnClick);
        //爱理财平台服务协议
        String permitStr = getString(R.string.permit_agreement_text);

        if (Build.VERSION.SDK_INT >= 24) {
            mPermitTextView.setText(Html.fromHtml(permitStr, Html.FROM_HTML_OPTION_USE_CSS_COLORS));
        } else {
            mPermitTextView.setText(Html.fromHtml(permitStr));
        }
        //手机号
        String savePhone = MyPreference.getInstance().read(UserInfo.USERINFO_KEY_MOBILE, "");
        mPhoneEditText.setEditorText(StringUtil.formatCheckMobileNumber(savePhone, " "));
        mPhoneEditText.setFocusChangeInterface(mOnFocusChangeInterface);
        mPhoneEditText.setEditorWatchListener(mPhoneTextWatch);
        mPhoneEditText.setFocusable(true);
        mPhoneEditText.setEditorHintColor(getResources().getColor(R.color.login_edit_text_hint_color));

        mNextLoading.hide();

        setActionViewState();
    }

    @OnClick(R.id.permit_agreement)
    void clickReadText() {
        String url = Configuration.DEFAULT.getDomain() + "/ihouse/policy.html";
        Map<String, String> dataMap = ObjectUtil.newHashMap();
        dataMap.put(WebViewActivity.TITLE, getString(R.string.permit_agreement_title_text));
        dataMap.put(WebViewActivity.URL, url);
        MyIntent.startActivity(getWRActivity(), WebViewActivity.class, dataMap);
    }

    public void showNextView() {
        closeKeyBoard();
        mPhoneEditText.showClearButton(true);
        mPhoneEditText.getmEditText().setEnabled(true);
        toLoginNextFragment();
    }

    public void toLoginNextFragment() {
        LoginNextFragment mLoginNextFragment = new LoginNextFragment();
        Map<String, Object> dataMap = MyIntent.getData(getArguments());
        dataMap.put(LoginNextFragment.PHONE_NUM, mPhoneNumber);
        Bundle bundle = MyIntent.setData(new Bundle(), dataMap);
        mLoginNextFragment.setArguments(bundle);
        mLoginNextFragment.setContainerId(R.id.login_fragment_container);
        mLoginNextFragment.setTag(LoginNextFragment.class.getSimpleName());
        mLoginNextFragment.setManager(getFragmentManager());
        mLoginNextFragment.setDefaultAnimations();
        /*
        mLoginNextFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.activity_lollipop_close_exit, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        mLoginNextFragment.setCustomAnimations(R.anim.none, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
                */

        //mLoginNextFragment.setBackOp(null);
        mLoginNextFragment.show(mLoginNextFragment.SHOW_ADD_HIDE);
    }

    @Override
    public void onPause() {
        closeKeyBoard();
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
        SystemUtil.showKeyboard(editText);
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

    private boolean checkPhone() {
        if (!ValidateUtil.isValidMobile(StringUtil.allSpaceFormat(mPhoneEditText.getEditorText()))) {
            ToastUtil.showInCenter("手机号码输入有误");
            return false;
        }
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showInputMethod(mPhoneEditText.getmEditText());
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

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showPhoneNum();
    }

    void showPhoneNum() {
        if (TextUtils.isEmpty(mPhoneEditText.getEditorText())) {
            autoMobileNum = CommonUtil.getPhone11Num(getWRActivity());
            if (!TextUtils.isEmpty(autoMobileNum)) {
                mPhoneEditText.setEditorText(StringUtil.formatCheckMobileNumber(autoMobileNum, " "));
            }
        }
    }

    public void showMyToast(String msg) {
        if (getActivity() != null) {
            ToastUtil.showInCenter(msg);
        }
    }


}