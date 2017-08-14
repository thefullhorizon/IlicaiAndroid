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
import com.ailicai.app.model.bean.BuyHuoqibaoResponse;
import com.ailicai.app.model.request.BuyHuoqibaoRequest;
import com.ailicai.app.model.request.BuyHuoqibaoResendRequest;
import com.ailicai.app.model.request.BuyHuoqibaoStateRequest;
import com.ailicai.app.model.request.HuoqibaoPayAdvanceRequest;
import com.ailicai.app.model.response.BuyHuoqibaoCheckResponse;
import com.ailicai.app.model.response.BuyHuoqibaoResendResponse;
import com.ailicai.app.model.response.account.AccountResponse;
import com.ailicai.app.ui.dialog.BuyDialogShowInfo;
import com.ailicai.app.ui.login.AccountInfo;
import com.ailicai.app.ui.view.CurrentRollInActivity;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Response;

/**
 * Created by Jer on 2016/1/7.
 * 安全卡购买钱包
 */
public class BuyCurrentPay extends BaseBuyFinancePay {

    private static int reqMaxTimes = 5;
    CurrentPayInfo currentPayInfo;

    Handler mHandler;
    //上一次支付结果
    BuyHuoqibaoResponse mBuyHuoqibaoResponse;
    BuyHuoqibaoRequest buyHuoqibaoRequest;
    String currentOutTradeNo;
    String currentAdvanceVoucherNo;
    boolean isCurrentQuery = false;
    //请求间隔时间
    private int currentReqTime = 1;
    //请求次数1 2 4 8 16
    private int addTimes = 1;
    // 五次轮询用的 在提交验证码时候返回时记录
    private long currentActivityDealId = 0;
    IwjwRespListener<BuyHuoqibaoResponse> buyHuoqibaoResponseIwjwRespListener = new IwjwRespListener<BuyHuoqibaoResponse>() {

        @Override
        public void onStart() {
            super.onStart();
            if (addTimes == 1) {
                loadMsgCodeProgress();
                // PayPresenter.showProgressTextHorDialog(mActivity, "加载中...");
            }
        }

        @Override
        public void onJsonSuccess(final BuyHuoqibaoResponse buyHuoqibaoResponse) {
            toDoBuyResult(buyHuoqibaoResponse);
        }

        @Override
        public void onFailInfo(String errorInfo) {
            super.onFailInfo(errorInfo);
            ToastUtil.showInCenter(errorInfo);
            LogUtil.d("debuglog", "第" + addTimes + "次请求后接口返回不对，停止请求");
            // PayPresenter.dismissProgressDialog();
            onDialogDismiss();
            if (addTimes > 1) {
                if (mBuyHuoqibaoResponse != null) {
                    iwPayResultListener.onPayStateDelay("处理中...", mBuyHuoqibaoResponse);
                }
            }
        }

        @Override
        public void onFailInfo(Response response, String errorInfo) {
            //2102表示活动已过期或失效,转入失败时刷新当前页面
            if (response == null) {
                onFailInfo(errorInfo);
                return;
            }
            if (response.getErrorCode() == 2102) {
                onDialogDismiss();
                ToastUtil.showInCenter(errorInfo);
                CurrentRollInActivity rollInActivity = (CurrentRollInActivity) mActivity;
                rollInActivity.initBaseInfo();
            } else {
                onFailInfo(errorInfo);
            }
        }
    };

    public BuyCurrentPay(FragmentActivity mActivity, CurrentPayInfo currentPayInfo, IwPwdPayResultListener iwPayResultListener) {
        super(mActivity, iwPayResultListener);
        this.currentPayInfo = currentPayInfo;
        mHandler = new Handler();
    }

    @Override
    void toPayForInfo(String s) {
        buyHuoqibaoRequest = new BuyHuoqibaoRequest();
        buyHuoqibaoRequest.setAmount(currentPayInfo.getAmount());
        buyHuoqibaoRequest.setPaypwd(s);
        buyHuoqibaoRequest.setOutTradeNo(getOutTradeNoResponse().getOutTradeNo());
        buyHuoqibaoRequest.setRequestNo(getOutTradeNoResponse().getRequestNo());
        buyHuoqibaoRequest.setAccountType(currentPayInfo.getAccountType());
        buyHuoqibaoRequest.setPayMethod(currentPayInfo.getPayMethod());
        ServiceSender.exec(mActivity, buyHuoqibaoRequest, new IwjwRespListener<BuyHuoqibaoCheckResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                loadProgress();
            }

            @Override
            public void onJsonSuccess(final BuyHuoqibaoCheckResponse response) {
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
                //账户余额支付、无需获取验证码直接进入轮询接口
                if ("2".equals(currentPayInfo.getPayMethod())) {
                    retryGetPayState();
                } else {
                    showMsgCodeLayout(mobileStr);
                }
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
        HuoqibaoPayAdvanceRequest huoqibaoPayAdvanceRequest = new HuoqibaoPayAdvanceRequest();
        huoqibaoPayAdvanceRequest.setOutTradeNo(currentOutTradeNo);
        huoqibaoPayAdvanceRequest.setAdvanceVoucherNo(currentAdvanceVoucherNo);
        huoqibaoPayAdvanceRequest.setVerifyCode(verifyCode.trim().replace(" ", ""));
        huoqibaoPayAdvanceRequest.setAmount(currentPayInfo.getAmount());
        //活动相关ID
        huoqibaoPayAdvanceRequest.setRuleId(currentPayInfo.getRuleId());
        huoqibaoPayAdvanceRequest.setActivityId(currentPayInfo.getActivityId());
        huoqibaoPayAdvanceRequest.setRelationId(currentPayInfo.getRelationId());
        huoqibaoPayAdvanceRequest.setConfigId(currentPayInfo.getConfigId());
        ServiceSender.exec(mActivity, huoqibaoPayAdvanceRequest, buyHuoqibaoResponseIwjwRespListener);
        isCurrentQuery = false;
    }

    /**
     * 重新获取短信验证码
     */
    private void reqRetrySendMsgCode() {
        getReSentBtn().setEnabled(false);
        BuyHuoqibaoResendRequest buyHuoqibaoResendRequest = new BuyHuoqibaoResendRequest();
        buyHuoqibaoResendRequest.setAmount(currentPayInfo.getAmount());
        buyHuoqibaoResendRequest.setAccountType(currentPayInfo.getAccountType());
        buyHuoqibaoResendRequest.setPayMethod(currentPayInfo.getPayMethod());
        buyHuoqibaoResendRequest.setPaypwd(buyHuoqibaoRequest.getPaypwd());
        ServiceSender.exec(mActivity, buyHuoqibaoResendRequest, new IwjwRespListener<BuyHuoqibaoResendResponse>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onJsonSuccess(BuyHuoqibaoResendResponse jsonObject) {
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

    private void retryGetPayState(BuyHuoqibaoResponse buyRes) {

        if (!isCurrentQuery) {
            currentActivityDealId = buyRes.getActivityDealId();
        }

        mBuyHuoqibaoResponse = buyRes;
        addTimes++;                             //    2    3   4   5
        currentReqTime = currentReqTime * 2;         //     2   4  8  16
        BuyHuoqibaoStateRequest buyHuoqibaoStateRequest = new BuyHuoqibaoStateRequest();
        buyHuoqibaoStateRequest.setAccountType(currentPayInfo.getAccountType());
        buyHuoqibaoStateRequest.setPayMethod(currentPayInfo.getPayMethod());
        buyHuoqibaoStateRequest.setOutTradeNo(buyRes.getOutTradeNo());
        buyHuoqibaoStateRequest.setActivityDealId(currentActivityDealId);
        ServiceSender.exec(mActivity, buyHuoqibaoStateRequest, buyHuoqibaoResponseIwjwRespListener);
        isCurrentQuery = true;
    }


    /**
     * 选择账户余额时不需要短信验证码直接走轮询接口
     */
    private void retryGetPayState() {
        BuyHuoqibaoStateRequest buyHuoqibaoStateRequest = new BuyHuoqibaoStateRequest();
        buyHuoqibaoStateRequest.setAccountType(currentPayInfo.getAccountType());
        buyHuoqibaoStateRequest.setPayMethod(currentPayInfo.getPayMethod());
        buyHuoqibaoStateRequest.setOutTradeNo(currentOutTradeNo);
        buyHuoqibaoStateRequest.setActivityDealId(currentActivityDealId);
        ServiceSender.exec(mActivity, buyHuoqibaoStateRequest, buyHuoqibaoResponseIwjwRespListener);
    }

    private void toDoBuyResult(final BuyHuoqibaoResponse buyHuoqibaoResponse) {
        if (buyHuoqibaoResponse != null) {
            String bizStatus = buyHuoqibaoResponse.getBizStatus();
            switch (bizStatus) {
                case "S":
                    //      PayPresenter.dismissProgressDialog();
                    onDialogDismiss();
                    iwPayResultListener.onPayComplete(buyHuoqibaoResponse);
                    break;
                case "P":
                    //    2    3   4   5
                    if (addTimes <= reqMaxTimes) {
                        LogUtil.d("debuglog", "第" + addTimes + "次请求，等待");
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                retryGetPayState(buyHuoqibaoResponse);
                            }
                        }, 1000 * currentReqTime);  //2   4   8  16
                    } else {
                        LogUtil.d("debuglog", "第" + addTimes + "次请求,全部结束");
                        currentReqTime = 1;
                        addTimes = 1;
                        //   PayPresenter.dismissProgressDialog();
                        onDialogDismiss();
                        iwPayResultListener.onPayStateDelay("处理中", buyHuoqibaoResponse);
                    }
                    break;
                case "F":
                    int errorcode = buyHuoqibaoResponse.getErrorCode();
                    if (errorcode == RestException.PAY_MSGCODE_ERROR) {
                        //显示输入验证码
                        disMsgCodeProgress();
                        ToastUtil.showInCenter(buyHuoqibaoResponse.getMessage());
                    } else {
                        onDialogDismiss();
                        //PayPresenter.dismissProgressDialog();
                        iwPayResultListener.onPayFailInfo("支付失败", "", buyHuoqibaoResponse);
                    }

                    break;
                default:
                    LogUtil.d("debuglog", "支付状态未知" + buyHuoqibaoResponse.toString());
                    ToastUtil.showInCenter(buyHuoqibaoResponse.getMessage());
                    //    PayPresenter.dismissProgressDialog();
                    onDialogDismiss();
                    if (addTimes > 1) {
                        iwPayResultListener.onPayStateDelay("处理中", buyHuoqibaoResponse);
                    }
                    break;
            }
        } else {
            //  PayPresenter.dismissProgressDialog();
            onDialogDismiss();
            if (addTimes > 1) {
                iwPayResultListener.onPayStateDelay("处理中", buyHuoqibaoResponse);
            }
        }
    }

    @Override
    BuyDialogShowInfo getDispalyInfo() {
        AccountResponse accountResponse = AccountInfo.getInstance().getAccountResponse();
        String title = null;
        String tips = null;
        String iconString = mActivity.getString(R.string.account_bankcard);
        if ("106".equals(currentPayInfo.getAccountType())) {
            title = "充值账户余额";
            tips = "使用" + accountResponse.getBankName() + "(" + accountResponse.getBankcardTailNo() + ")支付";
            iconString = mActivity.getString(R.string.account_bankcard);
        } else if ("101".equals(currentPayInfo.getAccountType())) {
            title = "转入活期宝";
            //支付到的账户类型 1-安全卡；2-账户余额
            if ("1".equals(currentPayInfo.getPayMethod())) {
                tips = "使用" + accountResponse.getBankName() + "(" + accountResponse.getBankcardTailNo() + ")支付";
                iconString = mActivity.getString(R.string.account_bankcard);
            } else if ("2".equals(currentPayInfo.getPayMethod())) {
                tips = "使用账户余额支付";
                iconString = mActivity.getString(R.string.icon_rmb_process);
            }
        }
        return new BuyDialogShowInfo.BankPayBuild()
                .setAmount(currentPayInfo.amount)
                .setMoneyOutStr(title)
                .setPayTypFrom(tips)
                .setPayTypFromIco(iconString)
                .setPayMethod(currentPayInfo.getPayMethod())
                .create();
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
        //钱包转入活动相关字段
        long ruleId;
        double amount;
        private long activityId; //活动id
        private long configId; //业务id
        private int relationId; //活动关联表id
        private String accountType; //转入类型：101-存钱罐；106-用户账户
        private String payMethod; //支付方式 1-安全卡；2-账户余额

        public long getRuleId() {
            return ruleId;
        }

        public void setRuleId(long ruleId) {
            this.ruleId = ruleId;
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

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getPayMethod() {
            return payMethod;
        }

        public void setPayMethod(String payMethod) {
            this.payMethod = payMethod;
        }
    }
}
