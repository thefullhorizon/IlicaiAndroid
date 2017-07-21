package com.ailicai.app.ui.buy;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.ailicai.app.MyApplication;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.BankCardUnbindRequest;
import com.ailicai.app.model.request.BankcardUnbindVerifyBySinaRequest;
import com.ailicai.app.model.response.BankcardUnbindResponse;
import com.ailicai.app.ui.dialog.BuyDialogShowInfo;
import com.ailicai.app.ui.login.UserInfo;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Response;

/**
 * Created by jrvair on 16/2/17.
 */
public class UnbindPwdCheckDialog extends BaseBuyFinancePay {


    String bankCardId;
    String currentPwd;
    //推进号
    private String advanceVoucherNo;

    public UnbindPwdCheckDialog(FragmentActivity mActivity, String bankCardId, IwPwdPayResultListener iwPayResultListener) {
        super(mActivity, iwPayResultListener);
        this.bankCardId = bankCardId;
    }

    @Override
    void toPayForInfo(String pwd) {
        currentPwd = pwd;
        //验证...
        httpForDeleteBankCard(pwd);
    }

    /**
     * 新浪支付：银行卡解绑
     *
     * @param verifyCode 验证码
     */
    private void reqBKCardUnbindVerifyBySina(String verifyCode) {
        BankcardUnbindVerifyBySinaRequest bkcardUnbBySinaRequest = new BankcardUnbindVerifyBySinaRequest();
        bkcardUnbBySinaRequest.advanceVoucherNo = advanceVoucherNo;
        bkcardUnbBySinaRequest.verifyCode = verifyCode;
        ServiceSender.exec(mActivity, bkcardUnbBySinaRequest, new IwjwRespListener<Response>() {
            @Override
            public void onStart() {
                super.onStart();
                loadMsgCodeProgress();
            }

            @Override
            public void onJsonSuccess(Response response) {
                int errorcode = response.getErrorCode();
                if (errorcode == RestException.PAY_MSGCODE_ERROR) {
                    //显示输入验证码
                    disMsgCodeProgress();
                    ToastUtil.showInCenter(response.getMessage());
                } else {
                    onDialogDismiss();
                    iwPayResultListener.onPayComplete(response);
                    ToastUtil.showInCenterLong(MyApplication.getInstance(), response.getMessage());
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                ToastUtil.showInCenterLong(MyApplication.getInstance(), errorInfo);
                disMsgCodeProgress();
            }
        });
    }


    public void show() {
        pay();
    }

    public void httpForDeleteBankCard(String password) {
        BankCardUnbindRequest request = new BankCardUnbindRequest();
        request.setBankAccountId(bankCardId);
        request.setPaypwd(password);
        ServiceSender.exec(mActivity, request, new IwjwRespListener<BankcardUnbindResponse>() {
            @Override
            public void onStart() {
                loadProgress();
            }

            @Override
            public void onJsonSuccess(BankcardUnbindResponse response) {
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
                    if (UserInfo.getInstance().isTestuser()) {
                        //内测用户走新浪支付流程
                        if (TextUtils.isEmpty(response.mobile)) {
                            disLoadProgress();
                            clearPassword();
                            ToastUtil.showInCenter("获取不到手机号");
                        } else if (TextUtils.isEmpty(response.advanceVoucherNo)) {
                            //如果没有推进号，就走老流程
                            onDialogDismiss();
                            iwPayResultListener.onPayComplete(response);
                  /*          disLoadProgress();
                            clearPassword();
                            ToastUtil.showInCenter("获取支付数据有误");*/
                        } else {
                            advanceVoucherNo = response.advanceVoucherNo;
                            showMsgCodeLayout(response.mobile);
                        }
                    } else {
                        onDialogDismiss();
                        iwPayResultListener.onPayComplete(response);
                    }
                } else {
                    disLoadProgress();
                    clearPassword();
                    ToastUtil.showInCenter(response.getMessage());
                }
            }

            @Override
            public void onFailInfo(String error) {
                disLoadProgress();
                ToastUtil.showInCenter(error);
                clearPassword();
            }
        });
    }

    /**
     * 新浪支付：显示输入短信验证码的视图
     */
    private void showMsgCodeLayout(String mobileStr) {
        changeViewToMsgView(mobileStr, "输入短信验证", "短信码已发送至" + mobileStr + "，验证后" + "\n将解绑此卡并前去设置新安全卡", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                String verifyCode = (String) v.getTag();
                if (!TextUtils.isEmpty(verifyCode)
                        && !TextUtils.isEmpty(verifyCode.replace(" ", ""))
                        && verifyCode.replace(" ", "").length() >= 4) {
                    //提交解绑数据
                    reqBKCardUnbindVerifyBySina(verifyCode);
                } else {
                    ToastUtil.showInCenter("请输入有效的短信验证码");
                }
                */
                String verifyCode = (String) v.getTag();
                //提交解绑数据
                reqBKCardUnbindVerifyBySina(verifyCode);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重发短信
                reqRetrySendMsgCode();
            }
        });
    }

    /**
     * 新浪支付：重新验证验证码，短信重发，
     */
    private void reqRetrySendMsgCode() {
        getReSentBtn().setEnabled(false);
        BankCardUnbindRequest request = new BankCardUnbindRequest();
        request.setBankAccountId(bankCardId);
        request.setPaypwd(currentPwd);
        ServiceSender.exec(mActivity, request, new IwjwRespListener<BankcardUnbindResponse>() {

            @Override
            public void onJsonSuccess(BankcardUnbindResponse jsonObject) {
                ToastUtil.showInCenter("发送成功");
                advanceVoucherNo = jsonObject.advanceVoucherNo;
                show60msTimer();
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                ToastUtil.showInCenter(errorInfo);
                getReSentBtn().setEnabled(true);
            }
        });
    }

    @Override
    BuyDialogShowInfo getDispalyInfo() {
        return new BuyDialogShowInfo.BankPayBuild()
                .setMoneyOutStr(UserInfo.getInstance().isTestuser() ? "输入交易密码，以验证身份" : "验证交易密码后将解绑此卡并前去\n设置新安全卡")
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
