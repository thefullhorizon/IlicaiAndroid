package com.ailicai.app.ui.paypassword;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.rsa.RSAEncrypt;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.common.utils.UIUtils;
import com.ailicai.app.model.request.PayPwdCheckRequest;
import com.ailicai.app.model.response.PayPwdCheckResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.login.AccountInfo;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.TextViewTF;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.jungly.gridpasswordview.GridPasswordView;
import com.jungly.gridpasswordview.GridPasswordView.OnPasswordChangedListener;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author owen
 *         2016/1/6
 */
public class PayPwdCheckActivity extends BaseBindActivity {

    // 区分是否是开户过来的校验密码
    private boolean isOpenAccount = false;

    /** 开户流程步数**/
    @Bind(R.id.openaccountStepContainer)
    View openaccountStepContainer;
    @Bind(R.id.tfStep01)
    TextViewTF tfStep01;
    @Bind(R.id.tfStep02)
    TextViewTF tfStep02;
    @Bind(R.id.tfStep03)
    TextViewTF tfStep03;
    @Bind(R.id.textViewStep01)
    TextView textViewStep01;
    @Bind(R.id.textViewStep02)
    TextView textViewStep02;
    @Bind(R.id.lineStep01Whith02)
    View lineStep01Whith02;
    @Bind(R.id.lineStep02Whith03)
    View lineStep02Whith03;
    @Bind(R.id.tvOpenAccountBonus)
    TextView tvOpenAccountBonus;
    @Bind(R.id.tvLastProcessName)
    TextView tvLastProcessName;
    /** 开户流程步数**/

    @Bind(R.id.gpv_custom)
    GridPasswordView gpv;

    @Bind(R.id.tvPayPwdHint)
    TextView tvPayPwdHint;

    @Bind(R.id.tvCancel)
    TextView tvCancel;

    @Bind(R.id.order_detail_top_title)
    IWTopTitleView topTitle;

    private int type;//1：修改，2：解绑银行卡，0：其它
    private String hint;
    private boolean isDark;

    @Override
    public int getLayout() {
        return R.layout.activity_pay_pwd_check;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initGpv();
        getIntentValue();
        setDifferHint();
        setTitleStyle();
        setOpenAccountStep();
    }

    private void initGpv() {
        gpv.requestFocus();
        gpv.setOnPasswordChangedListener(onPCListener);
    }

    private void getIntentValue() {
        type = getIntent().getIntExtra("TYPE", 0);
        hint = getIntent().getStringExtra("HINT");
        isDark = getIntent().getBooleanExtra("ISDARK", false);
        isOpenAccount = getIntent().getBooleanExtra("ISOPENACCOUNT",false);
    }

    private void setTitleStyle() {
        topTitle.setDark(isDark);
        topTitle.setIsShowLeftBtn(false);
        tvCancel.setVisibility(View.VISIBLE);
        if (isDark) {
            tvCancel.setTextColor(getResources().getColor(R.color.white));
        }
//         else {
//            topTitle.setIsShowLeftBtn(true);
//            tvCancel.setVisibility(View.GONE);
//        }
    }

    private void  setOpenAccountStep() {
        if(isOpenAccount) {
            openaccountStepContainer.setVisibility(View.VISIBLE);
            setOpenAccountStepTo01();
        }else {
            openaccountStepContainer.setVisibility(View.GONE);
        }
    }

    // 顶部设置开户流程到第一步
    private void setOpenAccountStepTo01() {
        tfStep01.setTextAppearance(this,R.style.textview_openaccount_current_step_left);
        tfStep01.setPadding(UIUtils.dipToPx(this,15),0,0,0);
        tfStep01.setText(getResources().getString(R.string.open_account_step01));

        lineStep01Whith02.setBackgroundColor(Color.parseColor("#e6e6e6"));

        tfStep02.setTextAppearance(this,R.style.textview_openaccount_not_reach_step_center);
        tfStep03.setTextAppearance(this,R.style.textview_openaccount_not_reach_step_right);
        int processNameLength = AccountInfo.getOpenAccountLastProcessName().length();
        tfStep03.setPadding(0,0,UIUtils.dipToPx(this,(processNameLength*14/2)-4),0);

        lineStep02Whith03.setBackgroundColor(Color.parseColor("#e6e6e6"));
        if(!TextUtils.isEmpty(AccountInfo.getOpenAccountActivityMemo())) {
            tvOpenAccountBonus.setText("获得"+AccountInfo.getOpenAccountActivityMemo());
        } else {
            tvOpenAccountBonus.setText(AccountInfo.getOpenAccountActivityMemo());
        }
        textViewStep01.setTextColor(Color.parseColor("#f75a14"));
        tvLastProcessName.setText(AccountInfo.getOpenAccountLastProcessName());
    }

    @OnClick(R.id.tvCancel)
    void cancel() {
        if (CheckDoubleClick.isFastDoubleClick(1000)) {
            return;
        }
        SystemUtil.HideSoftInput(this);
        finish();
    }

    private void setDifferHint() {
        if (hint != null) tvPayPwdHint.setText(hint);
    }

    OnPasswordChangedListener onPCListener = new OnPasswordChangedListener() {
        @Override
        public void onTextChanged(String s) {
        }

        @Override
        public void onInputFinish(String s) {
            requestForCheck();
        }
    };

    private void requestForCheck() {
        SystemUtil.HideSoftInput(this);
        ServiceSender.exec(this, getParams(), new IwjwRespListener<PayPwdCheckResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadTranstView();
            }

            @Override
            public void onJsonSuccess(PayPwdCheckResponse jsonObject) {
                showContentView();
                if (jsonObject.getErrorCode() == RestException.PAY_PWD_ERROR || jsonObject.getCheckFlag() == 0) {
                    showErrorDialog(jsonObject.getRemainingCnt(), jsonObject.getMessage());
                } else {
                    if (type == 1) {
                        Intent intent = new Intent(PayPwdCheckActivity.this, PayPwdCheckPhoneActivity.class);
                        intent.putExtra("TYPE", type);
                        startActivity(intent);
                        gpv.clearPassword();
                    } else {
                        setResult(RESULT_OK, new Intent().putExtra("password", RSAEncrypt.encrypt(gpv.getPassWord())));
                    }
                    finish();
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                ToastUtil.show(errorInfo);
                showContentView();
                showSoftInputAndClearPwd();
            }

        });
    }

    private void showErrorDialog(int count, String messge) {
        if (count == 0) {
            showDialog2(messge, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        } /*else if (type == 0) {
            showDialog2(messge, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showSoftInputAndClearPwd();
                }
            });
        } */ else {
            showDialog(messge, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case -1:
                            showSoftInputAndClearPwd();
                            break;
                        case -2:
                            if(AccountInfo.isRealNameVerify()){
                                Intent intent = new Intent(PayPwdCheckActivity.this, PayPwdResetActivity.class);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(PayPwdCheckActivity.this, PayPwdCheckPhoneActivity.class);
                                intent.putExtra("TYPE", 2);
                                startActivity(intent);
                            }
                            gpv.clearPassword();
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    private void showSoftInputAndClearPwd() {
        gpv.clearPassword();
        SystemUtil.showOrHideSoftInput(PayPwdCheckActivity.this);
    }

    private PayPwdCheckRequest getParams() {
        PayPwdCheckRequest params = new PayPwdCheckRequest();
        params.setUserId(UserInfo.getInstance().getUserId());
        params.setPaypwd(RSAEncrypt.encrypt(gpv.getPassWord()));
        return params;
    }

    public void showDialog(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(this, R.style.AppCompatDialog).setPositiveButton("重新输入", listener).setNegativeButton("忘记密码", listener).setMessage(message).setCancelable(false).show();
    }

    public void showDialog2(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(this, R.style.AppCompatDialog).setPositiveButton("我知道了", listener).setMessage(message).setCancelable(false).show();
    }

}
