package com.ailicai.app.ui.paypassword;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.PayPwdCheckVerifyCodeRequest;
import com.ailicai.app.model.request.PayPwdGetVerifyCodeRequest;
import com.ailicai.app.model.response.PayPwdCheckVerifyCodeResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.widget.IWTopTitleView;
import com.huoqiu.framework.util.CheckDoubleClick;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author owen
 *         2016/1/6
 */
public class PayPwdCheckPhoneActivity extends BaseBindActivity {

    @Bind(R.id.btPayPwdNext)
    Button btNext;

    @Bind(R.id.tvPhone)
    TextView tvPhone;

    Button btAgain;

    @Bind(R.id.etVerifyCode)
    EditText etVerifyCode;

    @Bind(R.id.tvCancel)
    TextView tvCancel;

    @Bind(R.id.order_detail_top_title)
    IWTopTitleView topTitle;
    TextWatcher textWahcherListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() >= 4)
                btNext.setEnabled(true);
            else
                btNext.setEnabled(false);
        }
    };
    private int type;
    private CountDownTimer mTimer;
    private int time = 60;

    @Override
    public int getLayout() {
        return R.layout.activity_pay_pwd_check_phone;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initView();
        getIntentValue();
        setTitleStyle();
        requestForGetVerifyCode();
    }

    private void initView() {
        etVerifyCode.requestFocus();
        etVerifyCode.addTextChangedListener(textWahcherListener);
        tvPhone.setText(subStringMobileNumber(UserInfo.getInstance().getUserMobile()));
        btAgain = (Button) findViewById(R.id.btnPayPwdGetAgain);
    }

    private void getIntentValue() {
        type = getIntent().getIntExtra("TYPE", 0);
    }

    private void setTitleStyle() {
        topTitle.setIsShowLeftBtn(false);
        tvCancel.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tvCancel)
    void cancel() {
        if (CheckDoubleClick.isFastDoubleClick(1000)) {
            return;
        }
        if (type == 2) {
            setResult(RESULT_OK);
            finish();
        } else {
            goToPayPwdManage();
        }
        SystemUtil.HideSoftInput(this);
    }

    @OnClick(R.id.btnPayPwdGetAgain)
    void modifyPayPwd() {
        if (CheckDoubleClick.isFastDoubleClick(1000)) {
            return;
        }
        requestForGetVerifyCode();
    }


    @OnClick(R.id.btPayPwdNext)
    void resetAndModifyPayPwd() {
        if (CheckDoubleClick.isFastDoubleClick(1000)) {
            return;
        }
        requestForCheckVerifyCode();
    }

    private void requestForGetVerifyCode() {


        ServiceSender.exec(this, getVerifyCodeParams(), new IwjwRespListener<PayPwdCheckVerifyCodeResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadTranstView();
            }

            @Override
            public void onJsonSuccess(PayPwdCheckVerifyCodeResponse jsonObject) {
                startTimer();
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                ToastUtil.show(errorInfo);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                showContentView();
            }
        });
    }

    private void requestForCheckVerifyCode() {
        ServiceSender.exec(this, getCheckVerifyCodeParams(), new IwjwRespListener<PayPwdCheckVerifyCodeResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadTranstView();
            }

            @Override
            public void onJsonSuccess(PayPwdCheckVerifyCodeResponse jsonObject) {
                showContentView();
                Intent intent = new Intent(PayPwdCheckPhoneActivity.this, PayPwdResetAndModifyActivity.class);
                intent.putExtra("TYPE", type);
                intent.putExtra("REQUESTNO", jsonObject.getRequestNo());
                startActivityForResult(intent, 200);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                ToastUtil.show(errorInfo);
                showContentView();
            }
        });
    }

    private PayPwdGetVerifyCodeRequest getVerifyCodeParams() {
        PayPwdGetVerifyCodeRequest params = new PayPwdGetVerifyCodeRequest();
        params.setUserId(UserInfo.getInstance().getUserId());
        return params;
    }

    private PayPwdCheckVerifyCodeRequest getCheckVerifyCodeParams() {
        PayPwdCheckVerifyCodeRequest params = new PayPwdCheckVerifyCodeRequest();
        params.setUserId(UserInfo.getInstance().getUserId());
        params.setVerifyCode(etVerifyCode.getText().toString().trim());
        return params;
    }

    public void startTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        mTimer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long arg0) {
                if (time > 0) {
                    int valueTime = --time;
                    setVerifyCodeButton(valueTime);
                } else {
                    mTimer.cancel();
                }
            }

            @Override
            public void onFinish() {
                cancelTimer();
            }
        };
        mTimer.start();
    }

    public void cancelTimer() {
        time = 60;
        setVerifyCodeButton();
    }

    public void setVerifyCodeButton(int milliSecond) {
        btAgain.setText("重新获取(" + milliSecond + "s)");
        btAgain.setEnabled(false);
    }

    public void setVerifyCodeButton() {
        btAgain.setText("重新获取");
        btAgain.setEnabled(true);
    }

    private String subStringMobileNumber(String value) {
        return value == null || value.length() < 11 ? "" : value.substring(0, 3) + "****" + value.substring(value.length() - 4, value.length());
    }

    private void goToPayPwdManage() {
        Intent intent = new Intent(this, PayPwdManageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

}
