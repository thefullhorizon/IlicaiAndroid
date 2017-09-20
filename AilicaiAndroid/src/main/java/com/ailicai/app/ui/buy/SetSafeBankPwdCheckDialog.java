package com.ailicai.app.ui.buy;

import android.support.v4.app.FragmentActivity;

import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.PayPwdCheckRequest;
import com.ailicai.app.model.response.PayPwdCheckResponse;
import com.ailicai.app.ui.dialog.BuyDialogShowInfo;
import com.ailicai.app.ui.login.UserInfo;
import com.huoqiu.framework.exception.RestException;

/**
 * Created by jrvair on 16/2/17.
 */
public class SetSafeBankPwdCheckDialog extends BaseBuyFinancePay {


    public SetSafeBankPwdCheckDialog(FragmentActivity mActivity, IwPwdPayResultListener iwPayResultListener) {
        super(mActivity, iwPayResultListener);
    }

    @Override
    void toPayForInfo(String pwd) {
        //验证...
        SystemUtil.HideSoftInput(mActivity);
        requestForCheck(pwd);
    }

    public void show() {
        pay();
    }

    private void requestForCheck(String pwd) {
        PayPwdCheckRequest params = new PayPwdCheckRequest();
        params.setUserId(UserInfo.getInstance().getUserId());
        params.setPaypwd(pwd);
        ServiceSender.exec(mActivity, params, new IwjwRespListener<PayPwdCheckResponse>() {
            @Override
            public void onStart() {
                super.onStart();
                loadProgress();
            }

            @Override
            public void onJsonSuccess(PayPwdCheckResponse response) {
                if (response.getErrorCode() == RestException.PAY_PWD_ERROR) {
                    clearPassword();
                    disLoadProgress();
                    onDialogDismiss();
                    int remainingCnt = response.getRemainingCnt();
                    if (remainingCnt == 0) {
                        showPwdLockedErrorDialog(mActivity, response.getMessage());
                    } else {
                        showPwdErrorResetDialog(mActivity, response.getMessage());
                    }
                    return;
                }

                if (response.getCheckFlag() == 1) {
                    iwPayResultListener.onPayComplete(response);
                    onDialogDismiss();
                } else {
                    disLoadProgress();
                    clearPassword();
                    ToastUtil.showInCenter(response.getMessage());
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                disLoadProgress();
                clearPassword();
                ToastUtil.showInCenter(errorInfo);
            }
        });
    }

    @Override
    BuyDialogShowInfo getDispalyInfo() {
        return new BuyDialogShowInfo.BankPayBuild()
                .setMoneyOutStr("以设置银行卡")
                .create();
    }

    @Override
    int getBizType() {
        return -1;
    }

    @Override
    double getAmount() {
        return 0;
    }
}
