package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

@RequestPath("/ailicai/applyCreditAssignment.rest")
public class ApplyCreditAssignmentRequest extends Request {
    private String creditAssignmentId; // 债权转让ID
    private String assignmentAmount;//转让金额
    private String paypwd; //交易密码

    public String getCreditAssignmentId() {
        return creditAssignmentId;
    }

    public void setCreditAssignmentId(String creditAssignmentId) {
        this.creditAssignmentId = creditAssignmentId;
    }

    public String getAssignmentAmount() {
        return assignmentAmount;
    }

    public void setAssignmentAmount(String assignmentAmount) {
        this.assignmentAmount = assignmentAmount;
    }

    public String getPaypwd() {
        return paypwd;
    }

    public void setPaypwd(String paypwd) {
        this.paypwd = paypwd;
    }
}
