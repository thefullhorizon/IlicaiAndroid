package com.ailicai.app.ui.buy;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.AdvanceDepositAndApplyRequest;
import com.ailicai.app.model.request.ApplyReserveRequest;
import com.ailicai.app.model.request.DepositAndApplyInvestReSendRequest;
import com.ailicai.app.model.request.DepositAndApplyReserveRequest;
import com.ailicai.app.model.request.QueryDepositAndApplyRequest;
import com.ailicai.app.model.response.reserve.AdvanceDepositAndApplyAppResponse;
import com.ailicai.app.model.response.ApplyReserveAppResponse;
import com.ailicai.app.model.response.DepositAndApplyInvestAppResponse;
import com.ailicai.app.ui.dialog.BuyDialogShowInfo;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Response;

import java.math.BigDecimal;

/**
 * 预约支付.
 * Created by David on 16/3/14.
 */
public class ReservePay extends BaseBuyFinancePay {

    private static int reqMaxTimes = 5;
    private ReservePayInfo payInfo;
    private Handler mHandler;
    //上一次支付结果
    private AdvanceDepositAndApplyAppResponse aDAAResponse;
    private String currentOutTradeNo;
    private String currentAdvanceVoucherNo;
    //请求间隔时间
    private int currentReqTime = 1;
    //请求次数1 2 4 8 16
    private int addTimes = 1;
    IwjwRespListener<AdvanceDepositAndApplyAppResponse> aDAAResponseIwjwRespListener = new IwjwRespListener<AdvanceDepositAndApplyAppResponse>() {

        @Override
        public void onStart() {
            super.onStart();
            if (addTimes == 1) {
                loadMsgCodeProgress();
            }
        }

        @Override
        public void onJsonSuccess(final AdvanceDepositAndApplyAppResponse response) {
            toDoBuyResult(response);
        }

        @Override
        public void onFailInfo(Response response, String errorInfo) {
            if (response.getErrorCode() == 2) {
                clearPassword();
                disLoadProgress();
                onDialogDismiss();
                ToastUtil.showInCenter(errorInfo);
            } else {
                onFailInfo(errorInfo);
            }
        }

        @Override
        public void onFailInfo(String errorInfo) {
            super.onFailInfo(errorInfo);
            clearPassword();
            disLoadProgress();
            onDialogDismiss();
            iwPayResultListener.onPayFailInfo(errorInfo, "", null);
        }
    };
    private String payPassword = "";

    public ReservePay(FragmentActivity mActivity, ReservePayInfo payInfo, IwPwdPayResultListener iwPayResultListener) {
        super(mActivity, iwPayResultListener);
        this.payInfo = payInfo;
        mHandler = new Handler();
    }

    @Override
    void toPayForInfo(String s) {
        if (TextUtils.isEmpty(s)) {
            throw new IllegalArgumentException("密码加密有误!检查一下");
        }
        payPassword = s;
        if (payInfo.isEnough()) {
            requestForReserve();
        } else {
            requestForBuy();
        }
    }

    /**
     * 直接预约
     */
    private void requestForReserve() {
        ApplyReserveRequest request = new ApplyReserveRequest();
        request.setProductId(payInfo.getProductId());
        request.setAmount(payInfo.getAmount());
        request.setTerm(payInfo.getTerm());
        request.setPaypwd(payPassword);
        if (!TextUtils.isEmpty(payInfo.getReservePwd())) {
            request.setReservePwd(payInfo.getReservePwd());
        }
        request.setYearInterestRateStr(payInfo.getYearInterestRateStr());
        ServiceSender.exec(mActivity, request, new IwjwRespListener<ApplyReserveAppResponse>() {
            @Override
            public void onStart() {
                super.onStart();
                loadProgress();
            }


            @Override
            public void onJsonSuccess(ApplyReserveAppResponse response) {
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
                iwPayResultListener.onPayComplete(response);

            }


            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                clearPassword();
                disLoadProgress();
                onDialogDismiss();
                iwPayResultListener.onPayFailInfo(errorInfo, "", null);
                //ToastUtil.showInCenter(mActivity, errorInfo);
            }
        });
    }

    /**
     * 充值并预约
     */
    private void requestForBuy() {
        DepositAndApplyReserveRequest dAARequest = new DepositAndApplyReserveRequest();
        dAARequest.setAmount(payInfo.getAmount());
        dAARequest.setPaypwd(payPassword);
        dAARequest.setOutTradeNo(getOutTradeNoResponse().getOutTradeNo());
        dAARequest.setRequestNo(getOutTradeNoResponse().getRequestNo());
        dAARequest.setProductId(payInfo.getProductId());
        dAARequest.setDepositAmount(payInfo.getRechargeAmount());
        dAARequest.setTerm(payInfo.getTerm());
        if (!TextUtils.isEmpty(payInfo.getReservePwd())) {
            dAARequest.setReservePwd(payInfo.getReservePwd());
        }
        dAARequest.setYearInterestRateStr(payInfo.getYearInterestRateStr());
        ServiceSender.exec(mActivity, dAARequest, new IwjwRespListener<DepositAndApplyInvestAppResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                loadProgress();
            }

            @Override
            public void onJsonSuccess(final DepositAndApplyInvestAppResponse response) {
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
                String mobileStr = response.getMobile();
                if (TextUtils.isEmpty(mobileStr)) {
                    ToastUtil.showInCenter("获取不到手机号");
                }
                currentOutTradeNo = response.getOutTradeNo();
                currentAdvanceVoucherNo = response.getAdvanceVoucherNo();
                showMsgCodeLayout(mobileStr);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                onDialogDismiss();
                disLoadProgress();
                ToastUtil.showInCenter(errorInfo);
                clearPassword();
            }
        });
    }

    /**
     * 显示输入短信验证码的视图
     */
    private void showMsgCodeLayout(String mobileStr) {
        changeViewToMsgView(mobileStr, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                String verifyCode = (String) v.getTag();
                if (!TextUtils.isEmpty(verifyCode)
                        && !TextUtils.isEmpty(verifyCode.replace(" ", ""))
                        && verifyCode.replace(" ", "").length() >= 4) {
                    //提交购买钱包
                    reqBuyHuoqibao(verifyCode);
                } else {
                    ToastUtil.showInCenter("请输入有效的短信验证码");
                }
                */

                String verifyCode = (String) v.getTag();
                //提交购买钱包
                reqBuyHuoqibao(verifyCode);
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
     * 验证码提交购买
     *
     * @param verifyCode
     */
    private void reqBuyHuoqibao(String verifyCode) {
        AdvanceDepositAndApplyRequest aDAARequest = new AdvanceDepositAndApplyRequest();
        aDAARequest.setOutTradeNo(currentOutTradeNo);
        aDAARequest.setAdvanceVoucherNo(currentAdvanceVoucherNo);
        aDAARequest.setVerifyCode(verifyCode.trim().replace(" ", ""));
        aDAARequest.setAmount(payInfo.getRechargeAmount());
        aDAARequest.setProductId(payInfo.getProductId());
        ServiceSender.exec(mActivity, aDAARequest, aDAAResponseIwjwRespListener);
    }

    /**
     * 购买结果
     * S成功;P处理中;F失败
     */
    private void toDoBuyResult(final AdvanceDepositAndApplyAppResponse response) {
        if (response != null) {
            String bizStatus = response.getBizStatus();
            switch (bizStatus) {
                case "S":
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
                    iwPayResultListener.onPayComplete(response);
                    break;
                case "P":
                    //    2    3   4   5
                    if (addTimes <= reqMaxTimes) {
                        LogUtil.d("debuglog", "第" + addTimes + "次请求，等待");
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                retryGetPayState(response);
                            }
                        }, 1000 * currentReqTime);  //2   4   8  16
                    } else {
                        LogUtil.d("debuglog", "第" + addTimes + "次请求,全部结束");
                        currentReqTime = 1;
                        addTimes = 1;
                        //   PayPresenter.dismissProgressDialog();
                        onDialogDismiss();
                        iwPayResultListener.onPayStateDelay("处理中", response);
                    }
                    break;
                case "F":
                    int errorcode = response.getErrorCode();
                    if (errorcode == RestException.PAY_MSGCODE_ERROR) {
                        //显示输入验证码
                        disMsgCodeProgress();
                        ToastUtil.showInCenter(response.getMessage());
                    } else {
                        onDialogDismiss();
                        //PayPresenter.dismissProgressDialog();
                        iwPayResultListener.onPayFailInfo("支付失败", "", response);
                    }

                    break;
                default:
                    break;
            }
        } else {
            //  PayPresenter.dismissProgressDialog();
            onDialogDismiss();
            ToastUtil.showInCenter("服务器异常");
        }
    }

    /**
     * 循环请求5次
     */
    private void retryGetPayState(AdvanceDepositAndApplyAppResponse aDAAResponse) {
        this.aDAAResponse = aDAAResponse;
        addTimes++;                             //    2    3   4   5
        currentReqTime = currentReqTime * 2;         //     2   4  8  16
        QueryDepositAndApplyRequest qDAARequest = new QueryDepositAndApplyRequest();
        qDAARequest.setProductId(payInfo.getProductId());
        qDAARequest.setActivityDealId(aDAAResponse.getActivityDealId());
        qDAARequest.setAdvanceVoucherNo(aDAAResponse.getAdvanceVoucherNo());
        ServiceSender.exec(mActivity, qDAARequest, aDAAResponseIwjwRespListener);
    }

    /**
     * 重新获取短信验证码
     */
    private void reqRetrySendMsgCode() {
        getReSentBtn().setEnabled(false);
        DepositAndApplyInvestReSendRequest reSendRequest = new DepositAndApplyInvestReSendRequest();
        reSendRequest.setOutTradeNo(currentOutTradeNo);
        reSendRequest.setPaypwd(payPassword);
        reSendRequest.setAmount(payInfo.getAmount());
        reSendRequest.setDepositAmount(payInfo.getRechargeAmount());
        reSendRequest.setProductId(payInfo.getProductId());
        reSendRequest.setRequestNo(getOutTradeNoResponse().getRequestNo());
        reSendRequest.setTerm(payInfo.getTerm());
        reSendRequest.setYearInterestRateStr(payInfo.getYearInterestRateStr());
        try {
            reSendRequest.setVoucherId(Integer.parseInt(currentAdvanceVoucherNo));
        }catch (Exception e) {
            e.printStackTrace();
        }
        ServiceSender.exec(mActivity, reSendRequest, new IwjwRespListener<DepositAndApplyInvestAppResponse>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onJsonSuccess(DepositAndApplyInvestAppResponse jsonObject) {
                ToastUtil.showInCenter("发送成功");
                currentOutTradeNo = jsonObject.getOutTradeNo();
                currentAdvanceVoucherNo = jsonObject.getAdvanceVoucherNo();
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
                .setAmount(getAmountType())
                .setMoneyOutStr(getProductName())
                .setPayTypFrom(getShowText())
                .setPayTypFromIco(mActivity.getString(R.string.play))
                .create();
    }

    @NonNull
    private double getAmountType() {
        return payInfo.getAmount();
    }

    @NonNull
    private String getProductName() {
        return payInfo.isEnough() ? payInfo.getProductName() : "充值并" + payInfo.getProductName();
    }

    @NonNull
    private String getShowText() {
        return payInfo.isEnough() ? "使用账户余额支付" : "使用账户余额+安全卡支付";
    }

    @Override
    int getBizType() {
        return payInfo.isEnough() ? -1 : 1;
    }

    @Override
    double getAmount() {
        return payInfo.getRechargeAmount();
    }

    public static class ReservePayInfo {
        private double amount;//预约金额
        private double rechargeAmount;//充值金额
        private String productId;
        private String productName; //产品名称
        private int term;
        private String reservePwd; // 预约口令
        private boolean isEnough;//是预约还是充值
        private String yearInterestRateStr;//年化收益率或收益区间

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getRechargeAmount() {
            BigDecimal bg = new BigDecimal(rechargeAmount);
            return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        public void setRechargeAmount(double rechargeAmount) {
            this.rechargeAmount = rechargeAmount;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getTerm() {
            return term;
        }

        public void setTerm(int term) {
            this.term = term;
        }

        public String getReservePwd() {
            return reservePwd;
        }

        public void setReservePwd(String reservePwd) {
            this.reservePwd = reservePwd;
        }

        public boolean isEnough() {
            return isEnough;
        }

        public void setEnough(boolean enough) {
            isEnough = enough;
        }

        public String getYearInterestRateStr() {
            return yearInterestRateStr;
        }

        public void setYearInterestRateStr(String yearInterestRateStr) {
            this.yearInterestRateStr = yearInterestRateStr;
        }
    }
}
