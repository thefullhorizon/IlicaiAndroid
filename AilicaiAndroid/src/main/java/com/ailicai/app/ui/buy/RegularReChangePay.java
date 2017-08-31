package com.ailicai.app.ui.buy;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.AdvanceDepositAndApplyRequest;
import com.ailicai.app.model.request.DepositAndApplyInvestReSendRequest;
import com.ailicai.app.model.request.DepositAndApplyInvestRequest;
import com.ailicai.app.model.request.QueryDepositAndApplyRequest;
import com.ailicai.app.model.response.AdvanceDepositAndApplyAppResponse;
import com.ailicai.app.model.response.DepositAndApplyInvestAppResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.dialog.BuyDialogShowInfo;
import com.ailicai.app.ui.view.RegularPayActivity;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Response;

/**
 * Created by Jer on 2016/1/7.
 * 安全卡购买钱包
 */
public class RegularReChangePay extends BaseBuyFinancePay {

    private static int reqMaxTimes = 5;
    CurrentPayInfo currentPayInfo;

    Handler mHandler;
    //上一次支付结果
    AdvanceDepositAndApplyAppResponse mBuyHuoqibaoResponse;
    DepositAndApplyInvestRequest investRequest;
    String currentOutTradeNo;
    String currentAdvanceVoucherNo;
    private String pwd;
    //请求间隔时间
    private int currentReqTime = 1;
    //请求次数1 2 4 8 16
    private int addTimes = 1;
    IwjwRespListener<AdvanceDepositAndApplyAppResponse> buyHuoqibaoResponseIwjwRespListener = new IwjwRespListener<AdvanceDepositAndApplyAppResponse>() {

        @Override
        public void onStart() {
            super.onStart();
            if (addTimes == 1) {
                loadMsgCodeProgress();
                // PayPresenter.showProgressTextHorDialog(mActivity, "加载中...");
            }
        }

        @Override
        public void onJsonSuccess(final AdvanceDepositAndApplyAppResponse buyHuoqibaoResponse) {
            toDoBuyResult(buyHuoqibaoResponse);
        }

        @Override
        public void onFailInfo(Response response, String errorInfo) {
            onDialogDismiss();
            if (response == null) {
                onFailInfo(errorInfo);
                return;
            }
            if (response.getErrorCode() == 2102) {
                //2102表示活动已过期或失效
                BaseBindActivity activity = (BaseBindActivity) mActivity;
                activity.reloadData();
                ToastUtil.showInCenter(response.getMessage());
            } else {
                onFailInfo(errorInfo);
                if (response.getBizCode() == 2) {
                    //刷新页面
                    RegularPayActivity activity = (RegularPayActivity) mActivity;
                    activity.initBaseInfo();
                }
            }
        }

        @Override
        public void onFailInfo(String errorInfo) {
            super.onFailInfo(errorInfo);
            ToastUtil.showInCenter(errorInfo);
            LogUtil.d("debuglog", "第" + addTimes + "次请求后接口返回不对，停止请求");
            // PayPresenter.dismissProgressDialog();
            onDialogDismiss();
            if (addTimes > 1) {
                iwPayResultListener.onPayStateDelay("处理中...", mBuyHuoqibaoResponse);
            }
        }
    };

    public RegularReChangePay(FragmentActivity mActivity, CurrentPayInfo currentPayInfo, IwPwdPayResultListener iwPayResultListener) {
        super(mActivity, iwPayResultListener);
        this.currentPayInfo = currentPayInfo;
        mHandler = new Handler();
    }

    /***
     * 校验交易密码
     *
     * @param s
     */
    @Override
    void toPayForInfo(String s) {
        pwd = s;
        investRequest = new DepositAndApplyInvestRequest();
        investRequest.setPaypwd(s);
        investRequest.setAmount(currentPayInfo.getAmount()+"");
        investRequest.setDepositAmount(currentPayInfo.getDepositAmount()+"");
        investRequest.setOutTradeNo(getOutTradeNoResponse().getOutTradeNo());
        investRequest.setRequestNo(getOutTradeNoResponse().getRequestNo());
        investRequest.setProductId(currentPayInfo.getProductId());
        investRequest.setIsTransfer(currentPayInfo.getIsTransfer());
        investRequest.setCreditId(currentPayInfo.getCreditId());

        if (currentPayInfo.getVoucherId() > 0) {
            investRequest.setVoucherId(currentPayInfo.getVoucherId());
        }
        ServiceSender.exec(mActivity, investRequest, new IwjwRespListener<DepositAndApplyInvestAppResponse>() {

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
                    //提交购买房产宝
                    reqBuyHuoqibao(verifyCode);
                } else {
                    ToastUtil.showInCenter("请输入有效的短信验证码");
                }
                */
                String verifyCode = (String) v.getTag();
                //提交购买房产宝
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
        AdvanceDepositAndApplyRequest applyRequest = new AdvanceDepositAndApplyRequest();
        applyRequest.setOutTradeNo(currentOutTradeNo);
        applyRequest.setAdvanceVoucherNo(currentAdvanceVoucherNo);
        applyRequest.setVerifyCode(verifyCode.trim().replace(" ", ""));
        applyRequest.setAmount(currentPayInfo.getAmount()+"");
        applyRequest.setProductId(currentPayInfo.getProductId());
        if (currentPayInfo.getRuleId() > 0) {
            applyRequest.setRuleId(currentPayInfo.getRuleId());
            applyRequest.setActivityId(currentPayInfo.getActivityId());
            applyRequest.setConfigId(currentPayInfo.getConfigId());
            applyRequest.setRelationId(currentPayInfo.getRelationId());
        }
        ServiceSender.exec(mActivity, applyRequest, buyHuoqibaoResponseIwjwRespListener);
    }

    /**
     * 重新获取短信验证码
     */
    private void reqRetrySendMsgCode() {
        getReSentBtn().setEnabled(false);
        DepositAndApplyInvestReSendRequest reSendRequest = new DepositAndApplyInvestReSendRequest();
        reSendRequest.setPaypwd(pwd);
        reSendRequest.setAmount(currentPayInfo.getAmount()+"");
        reSendRequest.setDepositAmount(currentPayInfo.getDepositAmount()+"");
        reSendRequest.setOutTradeNo(currentOutTradeNo);
        reSendRequest.setRequestNo(currentAdvanceVoucherNo);
        reSendRequest.setProductId(currentPayInfo.getProductId());
        reSendRequest.isTransfer = currentPayInfo.getIsTransfer();
        if (currentPayInfo.getVoucherId() > 0) {
            reSendRequest.setVoucherId(currentPayInfo.getVoucherId());
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

    private void retryGetPayState(AdvanceDepositAndApplyAppResponse buyRes) {
        mBuyHuoqibaoResponse = buyRes;
        addTimes++;                             //    2    3   4   5
        currentReqTime = currentReqTime * 2;         //     2   4  8  16
        QueryDepositAndApplyRequest andApplyRequest = new QueryDepositAndApplyRequest();
        andApplyRequest.setProductId(currentPayInfo.getProductId());
        andApplyRequest.setActivityDealId(mBuyHuoqibaoResponse.getActivityDealId());
        andApplyRequest.setAdvanceVoucherNo(mBuyHuoqibaoResponse.getAdvanceVoucherNo());
        ServiceSender.exec(mActivity, andApplyRequest, buyHuoqibaoResponseIwjwRespListener);
    }

    private void toDoBuyResult(final AdvanceDepositAndApplyAppResponse applyResponse) {
        if (applyResponse != null) {
            String bizStatus = applyResponse.getBizStatus();
            switch (bizStatus) {
                case "S":
                    //      PayPresenter.dismissProgressDialog();
                    onDialogDismiss();
                    iwPayResultListener.onPayComplete(applyResponse);
                    break;
                case "P":
                    //    2    3   4   5
                    if (addTimes <= reqMaxTimes) {
                        LogUtil.d("debuglog", "第" + addTimes + "次请求，等待");
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                retryGetPayState(applyResponse);
                            }
                        }, 1000 * currentReqTime);  //2   4   8  16
                    } else {
                        LogUtil.d("debuglog", "第" + addTimes + "次请求,全部结束");
                        currentReqTime = 1;
                        addTimes = 1;
                        //   PayPresenter.dismissProgressDialog();
                        onDialogDismiss();
                        iwPayResultListener.onPayStateDelay("处理中", applyResponse);
                    }
                    break;
                case "F":
                    int errorcode = applyResponse.getErrorCode();
                    if (errorcode == RestException.PAY_MSGCODE_ERROR) {
                        //显示输入验证码
                        disMsgCodeProgress();
                        ToastUtil.showInCenter(applyResponse.getMessage());
                    } else {
                        onDialogDismiss();
                        //PayPresenter.dismissProgressDialog();
                        iwPayResultListener.onPayFailInfo("支付失败", "", applyResponse);
                    }

                    break;
                default:
                    LogUtil.d("debuglog", "支付状态未知" + applyResponse.toString());
                    ToastUtil.showInCenter(applyResponse.getMessage());
                    //    PayPresenter.dismissProgressDialog();
                    onDialogDismiss();
                    if (addTimes > 1) {
                        iwPayResultListener.onPayStateDelay("处理中", applyResponse);
                    }
                    break;
            }
        } else {
            //  PayPresenter.dismissProgressDialog();
            onDialogDismiss();
            if (addTimes > 1) {
                iwPayResultListener.onPayStateDelay("处理中", applyResponse);
            }
        }
    }

    @Override
    BuyDialogShowInfo getDispalyInfo() {
        return new BuyDialogShowInfo.BankPayBuild()
                .setAmount(currentPayInfo.amount)
                .setMoneyOutStr(TextUtils.isEmpty(moneyOutStr)?"支付并购买房产宝":moneyOutStr)
                .setPayTypFrom("使用账户余额+安全卡支付")
                .setPayTypFromIco(mActivity.getString(R.string.current_pay))
                .create();
    }

    // 外部设置 转入时候de描述
    private String moneyOutStr = "";
    public void setMoneyOutStr(String moneyOutStr) {
        this.moneyOutStr = moneyOutStr;
    }

    @Override
    int getBizType() {
        return 1;
    }

    @Override
    double getAmount() {
        return currentPayInfo.getAmount();
    }

    public static class CurrentPayInfo {
        private double amount;//房产宝申购金额
        private String productId = ""; // 标的编号(预约编号)
        private double depositAmount;//钱包充值金额
        private int voucherId; // 卡券id
        private long activityId; //活动id
        private long configId; //业务id
        private int relationId; //活动关联表id
        private long ruleId; //规则id
        private double realAmount; // 实际支付(购买转让用与购买的本金相等)
        private int isTransfer; //是否是转让标 0-普通投标；1-债权购买
        private String creditId; //债权转让ID
        private String yearInterestRateStr;//预计年化收益率

        public double getAmount() {
            return realAmount <= 0 ? amount : realAmount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public double getDepositAmount() {
            return depositAmount;
        }

        public void setDepositAmount(double depositAmount) {
            this.depositAmount = depositAmount;
        }

        public int getVoucherId() {
            return voucherId;
        }

        public void setVoucherId(int voucherId) {
            this.voucherId = voucherId;
        }

        public long getActivityId() {
            return activityId;
        }

        public void setActivityId(long activityId) {
            this.activityId = activityId;
        }

        public long getConfigId() {
            return configId;
        }

        public void setConfigId(long configId) {
            this.configId = configId;
        }

        public int getRelationId() {
            return relationId;
        }

        public void setRelationId(int relationId) {
            this.relationId = relationId;
        }

        public long getRuleId() {
            return ruleId;
        }

        public void setRuleId(long ruleId) {
            this.ruleId = ruleId;
        }

        public double getRealAmount() {
            return realAmount;
        }

        public void setRealAmount(double realAmount) {
            this.realAmount = realAmount;
        }

        public int getIsTransfer() {
            return isTransfer;
        }

        public void setIsTransfer(int isTransfer) {
            this.isTransfer = isTransfer;
        }

        public String getCreditId() {
            return creditId;
        }

        public void setCreditId(String creditId) {
            this.creditId = creditId;
        }

        public String getYearInterestRateStr() {
            return yearInterestRateStr;
        }

        public void setYearInterestRateStr(String yearInterestRateStr) {
            this.yearInterestRateStr = yearInterestRateStr;
        }
    }

}
