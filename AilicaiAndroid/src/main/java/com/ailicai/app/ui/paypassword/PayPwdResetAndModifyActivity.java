package com.ailicai.app.ui.paypassword;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.rsa.RSAEncrypt;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.common.utils.UIUtils;
import com.ailicai.app.model.request.PayPwdResetAndModifyRequest;
import com.ailicai.app.model.request.PayPwdSetRequest;
import com.ailicai.app.model.request.Request;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.login.AccountInfo;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.login.UserManager;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.TextViewTF;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.jungly.gridpasswordview.GridPasswordView;
import com.jungly.gridpasswordview.GridPasswordView.OnPasswordChangedListener;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author owen
 *         2016/1/6
 *         重置，修改，设置共用（重置和修改是同一个接口，设置另外）
 */
public class PayPwdResetAndModifyActivity extends BaseBindActivity {


    private static final int REQUESR_FOR_SELF = 0x00;

//    @Bind(R.id.btPayPwdNext)
//    Button btNext;

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

    @Bind(R.id.tvShowOrHide)
    TextView tvShowOrHide;

    @Bind(R.id.tvPayPwdHint)
    TextView tvPayPwdHint;

    @Bind(R.id.tvPasswordDiffer)
    TextView tvPasswordDiffer;

    @Bind(R.id.tvCancel)
    TextView tvCancel;

    @Bind(R.id.order_detail_top_title)
    IWTopTitleView topTitle;

    private int type;
    private String requestNo;
    private String firstPassword;

    private boolean isCancel;
    private boolean isPwdError;

    @Override
    public int getLayout() {
        return R.layout.activity_pay_pwd_reset_and_modify;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initView();
        getIntentValue();
        setView();
        setTitleStyle();
        setOpenAccountStepTo01();
    }

    private void initView() {
        gpv.requestFocus();
        gpv.setOnPasswordChangedListener(onPCListener);
    }

    private void getIntentValue() {
        type = getIntent().getIntExtra("TYPE", 0);
        requestNo = getIntent().getStringExtra("REQUESTNO");
        firstPassword = getIntent().getStringExtra("FIRSTPASSWORD");
    }

    private void setView() {
        openaccountStepContainer.setVisibility(View.GONE);
        if (firstPassword == null) {

            switch (type) {
                case 0://开户设置
                case 3://添加卡设置
                case 4://解绑卡设置
                    tvPayPwdHint.setText("为了您的账户安全，请设置交易密码");
                    break;
                case 1://修改
                case 2://重置
                    tvPayPwdHint.setText("请输入新的交易密码");
                    break;
                default:
                    break;
            }
        } else {
//            btNext.setVisibility(View.VISIBLE);
            tvPayPwdHint.setText("请再次输入交易密码");
        }

        //类型是开户，设置开户流程可见
        if(type == 0) {
            openaccountStepContainer.setVisibility(View.VISIBLE);
        }
    }

    private void setTitleStyle() {
        topTitle.setIsShowLeftBtn(false);
        tvCancel.setVisibility(View.VISIBLE);
        topTitle.setTitleText("设置交易密码");
        if (type == 0) {
            topTitle.setDark(true);
            tvCancel.setTextColor(getResources().getColor(R.color.white));
        }
//        else if (type == 3) {
//            topTitle.setIsShowLeftBtn(false);
//            topTitle.setTitleText("设置交易密码");
//        }else if (type == 4) {
//            topTitle.setTitleText("设置交易密码");
//        }
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
        if (type == 0) {//0是黑色标题,开户流程中点击取消添加埋点
            EventLog.upEventLog("682", "cancel", "jypass");
        }
        SystemUtil.HideSoftInput(this);
        if (type == 1) {
            goToPayPwdManage();
            return;
        } else if (type == 2) {
            setResult(RESULT_OK);
            finish();
            return;
        }
        if (firstPassword != null) setResult(RESULT_OK, new Intent().putExtra("isCancel", true));
        finish();
    }

    OnPasswordChangedListener onPCListener = new OnPasswordChangedListener() {
        @Override
        public void onTextChanged(String s) {
//            if (s.length() == 6)
//                btNext.setEnabled(true);
//            else
//                btNext.setEnabled(false);
        }

        @Override
        public void onInputFinish(String s) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (firstPassword == null) {
                        Intent intent = new Intent(PayPwdResetAndModifyActivity.this, PayPwdResetAndModifyActivity.class);
                        intent.putExtra("REQUESTNO", requestNo);
                        intent.putExtra("FIRSTPASSWORD", gpv.getPassWord());
                        intent.putExtra("TYPE", type);
                        startActivityForResult(intent, REQUESR_FOR_SELF);
                        gpv.clearPassword();
                    } else {
                        if (firstPassword.equals(gpv.getPassWord())) {
                            requestForModifyOrResetOrSetPwd();
                        } else {
//            gpv.clearPassword();
//            tvPasswordDiffer.setVisibility(View.VISIBLE);
                            ToastUtil.showInCenter("两次密码不一致,请重新输入");
                            setResult(RESULT_OK, new Intent().putExtra("isPwdError", true));
                            finish();
                        }
                    }
                }
            }, 100);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (data != null) {
            isCancel = data.getBooleanExtra("isCancel", false);
            isPwdError = data.getBooleanExtra("isPwdError", false);
        }
        if (requestCode == REQUESR_FOR_SELF) {
            if (isPwdError) {
                gpv.setPasswordVisibility(false);
                tvShowOrHide.setText("显示密码");
                return;
            }
            if (!isCancel) {
                setResult(RESULT_OK, new Intent().putExtra("password", RSAEncrypt.encrypt(gpv.getPassWord())));
            }
            finish();
        }
    }

    @OnClick(R.id.tvShowOrHide)
    void showAndHideNumber() {
        if ("显示密码".equals(tvShowOrHide.getText().toString())) {
            gpv.setPasswordVisibility(true);
            tvShowOrHide.setText("隐藏密码");
        } else {
            gpv.setPasswordVisibility(false);
            tvShowOrHide.setText("显示密码");
        }

    }

    @OnClick(R.id.btPayPwdNext)
    void next() {
        if (CheckDoubleClick.isFastDoubleClick(1000)) {
            return;
        }
    }

    private void requestForModifyOrResetOrSetPwd() {
        SystemUtil.HideSoftInput(this);
        ServiceSender.exec(this, getParams(), new IwjwRespListener<Response>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadTranstView();
            }

            @Override
            public void onJsonSuccess(Response jsonObject) {
                toastDiffer();
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                ToastUtil.show(errorInfo);
                gpv.clearPassword();
                SystemUtil.showOrHideSoftInput(PayPwdResetAndModifyActivity.this);
                showContentView();
            }
        });
    }

    private void toastDiffer() {
        switch (type) {
            case 0://设置
            case 3://设置
            case 4://设置
                ToastUtil.showInCenter("设置交易密码成功");
                AccountInfo.setIsSetPayPwd(true);
                UserManager.setIsSetPayPwd(true);
                setResult(RESULT_OK, new Intent().putExtra("isPwdError", false));
                finish();
                break;
            case 1://修改
                ToastUtil.showInCenter("修改交易密码成功");
                goToPayPwdManage();
                break;
            case 2://重置
                ToastUtil.showInCenter("重置交易密码成功");
                setResult(RESULT_OK, new Intent().putExtra("isPwdError", false));
                finish();
                break;
            default:
                break;
        }
    }

    private void goToPayPwdManage() {
        Intent intent = new Intent(this, PayPwdManageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private Request getParams() {
        return type == 1 || type == 2 ? getPayPwdResetOrModifyRequest() : getPayPwdSetRequest();
    }

    private PayPwdResetAndModifyRequest getPayPwdResetOrModifyRequest() {
        PayPwdResetAndModifyRequest request = new PayPwdResetAndModifyRequest();
        request.setUserId(UserInfo.getInstance().getUserId());
        request.setRequestNo(requestNo);
        request.setPaypwd(RSAEncrypt.encrypt(gpv.getPassWord()));
        return request;
    }

    private PayPwdSetRequest getPayPwdSetRequest() {
        PayPwdSetRequest request = new PayPwdSetRequest();
        request.setUserId(UserInfo.getInstance().getUserId());
        request.setPaypwd(RSAEncrypt.encrypt(gpv.getPassWord()));
        return request;
    }

    /**private String getEncryptPassword() {
     try {
     return FinanceUtil.encrypt(gpv.getPassWord(), null);
     } catch (Exception e) {
     e.printStackTrace();
     }
     return "";
     }*/

}
