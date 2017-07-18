package com.ailicai.app.ui.buy;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.model.request.ApplyCreditAssignmentRequest;
import com.ailicai.app.model.response.ApplyAssignmentResponse;
import com.ailicai.app.ui.dialog.BuyDialogShowInfo;
import com.huoqiu.framework.exception.RestException;

/**
 * 转让房产宝
 * Created by Gong on 16/8/1.
 */
public class ApplyCreditAssignmentPay extends BaseBuyFinancePay {
    public static class Transfer {
        private String creditAssignmentId; // 债权转让ID
        private String creditAssignmentName; // 债权转让Name
        private double assignmentAmount;//转让金额
        private String productId="";//房产宝id
        private String paypwd; //交易密码

        public String getCreditAssignmentId() {
            return creditAssignmentId;
        }

        public void setCreditAssignmentId(String creditAssignmentId) {
            this.creditAssignmentId = creditAssignmentId;
        }

        public String getCreditAssignmentName() {
            return creditAssignmentName;
        }

        public void setCreditAssignmentName(String creditAssignmentName) {
            this.creditAssignmentName = creditAssignmentName;
        }

        public double getAssignmentAmount() {
            return assignmentAmount;
        }

        public void setAssignmentAmount(double assignmentAmount) {
            this.assignmentAmount = assignmentAmount;
        }

        public String getPaypwd() {
            return paypwd;
        }

        public void setPaypwd(String paypwd) {
            this.paypwd = paypwd;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }
    }

    private Transfer transfer;

    public ApplyCreditAssignmentPay(FragmentActivity mActivity, Transfer transfer, IwPwdPayResultListener iwPayResultListener) {
        super(mActivity, iwPayResultListener);
        this.transfer = transfer;
    }

    @Override
    void toPayForInfo(String s) {
        if (TextUtils.isEmpty(s)) {
            throw new IllegalArgumentException("密码加密有误!检查一下");
        }
        ApplyCreditAssignmentRequest request = new ApplyCreditAssignmentRequest();
        request.setCreditAssignmentId(String.valueOf(transfer.getCreditAssignmentId()));
        request.setAssignmentAmount(transfer.getAssignmentAmount());
        request.setPaypwd(s);
        ServiceSender.exec(mActivity, request, new IwjwRespListener<ApplyAssignmentResponse>() {
            @Override
            public void onJsonSuccess(ApplyAssignmentResponse jsonObject) {
                if (jsonObject.getErrorCode() == RestException.PAY_PWD_ERROR) {
                    clearPassword();
                    disLoadProgress();
                    onDialogDismiss();
                    int remainingCnt = jsonObject.getRemainingCnt();
                    if (remainingCnt == 0) {
                        showPwdLockedErrorDialog(mActivity, jsonObject.getMessage());
                    } else {
                        showPwdErrorResetDialog(mActivity, jsonObject.getMessage());
                    }
                    return;
                }
                onDialogDismiss();
                iwPayResultListener.onPayComplete(jsonObject);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                onDialogDismiss();
                iwPayResultListener.onPayFailInfo(errorInfo, null, null);
            }
        });

    }

    @Override
    BuyDialogShowInfo getDispalyInfo() {
        return new BuyDialogShowInfo.BankPayBuild()
                .setAmount(transfer.getAssignmentAmount())
                .setMoneyOutStr("转让房产宝" + transfer.getCreditAssignmentName() + "号")
                .setPayTypFrom("转让成功后，金额自动转入账户余额")
                .create();
    }

    @Override
    int getBizType() {
        return -1;
    }

    @Override
    double getAmount() {
        return transfer.getAssignmentAmount();
    }

}
