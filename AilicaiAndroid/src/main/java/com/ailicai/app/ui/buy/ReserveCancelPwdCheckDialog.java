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
 * Created by Jer on 16/3/11
 */
public class ReserveCancelPwdCheckDialog extends BaseBuyFinancePay {

    private ShowInfo mInterface;
    private String payPwd;

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public ReserveCancelPwdCheckDialog(FragmentActivity mActivity, IwPwdPayResultListener iwPayResultListener, ShowInfo mInterface) {
        super(mActivity, iwPayResultListener);
        this.mInterface = mInterface;
    }

    @Override
    void toPayForInfo(String pwd) {
        SystemUtil.HideSoftInput(mActivity);
        requestForCheck(pwd);
        setPayPwd(pwd);
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
                .setPayTypFrom("取消成功后，预约金将在钱包解冻")
                .setAmount(mInterface.showAmount())
                .setMoneyOutStr(mInterface.showMoneyOutStr())
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

    public interface ShowInfo {
        double showAmount();

        String showMoneyOutStr();
    }

}
