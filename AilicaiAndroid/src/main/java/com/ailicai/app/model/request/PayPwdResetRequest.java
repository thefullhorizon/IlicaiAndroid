package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * @author owen
 *         2016/1/7
 */
@RequestPath("/ailicai/resetPayVerifyIdCard.rest")
public class PayPwdResetRequest extends Request {
    private String userName; // 持卡人姓名，RSA加密
    private String idCardNo; // 身份证号，RSA加密

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
