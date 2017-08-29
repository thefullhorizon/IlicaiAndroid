package com.ailicai.app.ui.buy;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.CancelCreditAssignmentRequest;
import com.ailicai.app.model.response.CancelCreditAssignmentResponse;
import com.ailicai.app.ui.dialog.BuyDialogShowInfo;
import com.huoqiu.framework.exception.RestException;

/**
 * 取消转让房产宝
 * Created by liyanan on 16/7/29.
 */
public class CancelTransferPay extends BaseBuyFinancePay {
    public static class CancelTransfer {
        private String id;
        private String txt;
        private String money;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }

    private CancelTransfer transfer;

    public CancelTransferPay(FragmentActivity mActivity, CancelTransfer transfer, IwPwdPayResultListener iwPayResultListener) {
        super(mActivity, iwPayResultListener);
        this.transfer = transfer;
    }

    @Override
    void toPayForInfo(String s) {
        if (TextUtils.isEmpty(s)) {
            throw new IllegalArgumentException("密码加密有误!检查一下");
        }
        final CancelCreditAssignmentRequest request = new CancelCreditAssignmentRequest();
        request.setCreditAssignmentIds(transfer.getId());
        request.setPayPwd(s);
        ServiceSender.exec(mActivity, request, new IwjwRespListener<CancelCreditAssignmentResponse>() {
            @Override
            public void onJsonSuccess(CancelCreditAssignmentResponse response) {
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
                onDialogDismiss();
//                ToastUtil.showInCenter("取消转让成功");
                iwPayResultListener.onPayComplete(response);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                onDialogDismiss();
                ToastUtil.showInCenter(errorInfo);
                iwPayResultListener.onPayFailInfo(errorInfo, null, null);
            }
        });

    }

    @Override
    BuyDialogShowInfo getDispalyInfo() {
        return new BuyDialogShowInfo.BankPayBuild()
                .setAmount(Double.parseDouble(transfer.getMoney().replace(",","")))
                .setMoneyOutStr(transfer.getTxt())
                .setPayTypFrom("取消后，您将继续持有本金")
                .create();
    }

    @Override
    int getBizType() {
        return -1;
    }

    @Override
    double getAmount() {
        return Double.parseDouble(transfer.getMoney().replace(",",""));
    }

}
