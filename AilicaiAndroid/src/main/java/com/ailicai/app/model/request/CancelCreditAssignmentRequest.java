package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * 取消债权转让Request
 * Created by liyanan on 16/8/1.
 */
@RequestPath("/ailicai/cancelCreditAssignment.rest")
public class CancelCreditAssignmentRequest extends Request {
    private String creditAssignmentIds; // 债权转让ID
    private String payPwd;//支付密码，RSA加密

    public String getCreditAssignmentIds() {
        return creditAssignmentIds;
    }

    public void setCreditAssignmentIds(String creditAssignmentIds) {
        this.creditAssignmentIds = creditAssignmentIds;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }
}
