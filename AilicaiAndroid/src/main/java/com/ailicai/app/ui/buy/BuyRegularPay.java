package com.ailicai.app.ui.buy;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.BuyDingqibaoRequest;
import com.ailicai.app.model.request.QueryBuyTransferRequest;
import com.ailicai.app.model.response.BuyDingqibaoResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.dialog.BuyDialogShowInfo;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Response;

/**
 * Created by Jer on 2016/1/7.
 */
public class BuyRegularPay extends BaseBuyFinancePay {
    public static class RegularPayInfo {
        private double amount;
        private String productId;
        private String productName; //产品名称
        private int voucherId; // 卡券id 5.2增加
        private long activityId; //活动id
        private long configId; //业务id
        private int relationId; //活动关联表id
        private long ruleId; //规则id
        private double realAmount; // 实际支付(购买转让用与购买的本金相等)

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

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
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
    }

    BuyDingqibaoResponse mBuyDingqibaoResponse;

    RegularPayInfo regularPayInfo;

    Handler mHandler;

    private static int reqMaxTimes = 5;

    //请求间隔时间
    private int currentReqTime = 1;

    //请求次数1 2 4 8 16
    private int addTimes = 1;

    public BuyRegularPay(FragmentActivity mActivity, RegularPayInfo regularPayInfo, IwPwdPayResultListener iwPayResultListener) {
        super(mActivity, iwPayResultListener);
        this.regularPayInfo = regularPayInfo;
        mHandler = new Handler();
    }

    @Override
    void toPayForInfo(String s) {
        if (TextUtils.isEmpty(s)) {
            throw new IllegalArgumentException("密码加密有误!检查一下");
        }
        BuyDingqibaoRequest buyHuoqibaoRequest = new BuyDingqibaoRequest();
        buyHuoqibaoRequest.setAmount(regularPayInfo.getAmount()+"");
        buyHuoqibaoRequest.setProductId(regularPayInfo.getProductId());
        buyHuoqibaoRequest.setPaypwd(s);
        if (regularPayInfo.getVoucherId() > 0) {
            buyHuoqibaoRequest.setVoucherId(regularPayInfo.getVoucherId());
        }
        if (regularPayInfo.getRuleId() > 0) {
            //表示参加了活动
            buyHuoqibaoRequest.setRuleId(regularPayInfo.getRuleId());
            buyHuoqibaoRequest.setActivityId(regularPayInfo.getActivityId());
            buyHuoqibaoRequest.setConfigId(regularPayInfo.getConfigId());
            buyHuoqibaoRequest.setRelationId(regularPayInfo.getRelationId());
        }
        ServiceSender.exec(mActivity, buyHuoqibaoRequest, buyDingqibaoIwjwRespListener);
    }

    IwjwRespListener<BuyDingqibaoResponse> buyDingqibaoIwjwRespListener = new IwjwRespListener<BuyDingqibaoResponse>() {

        @Override
        public void onStart() {
            super.onStart();
            loadProgress();
        }

        @Override
        public void onJsonSuccess(BuyDingqibaoResponse response) {
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

            /**
             V5.9新加
             只有购买转让房产宝才走轮询,response.getBizStatus()只有转让购买才会有值
             */
            if (response.getIsTransfer() > 0
                    && "P".equalsIgnoreCase(response.getBizStatus())
                    && response.getUsePoll() == 1) {
                //进入5次轮询环节
                toDoBuyResult(response);
            } else {
                /**
                 * 老代码
                 */
                onDialogDismiss();
                // 如果出现“P”状态，直接进入购买中页面 针对 转让标购买
                if ("P".equalsIgnoreCase(response.getBizStatus())) {
                    iwPayResultListener.onPayStateDelay(response.getMessage(), response);
                } else if ("F".equalsIgnoreCase(response.getBizStatus())) {
                    iwPayResultListener.onPayFailInfo(response.getMessage(), "", response);
                } else {
                    iwPayResultListener.onPayComplete(response);
                }
            }
        }

        @Override
        public void onFailInfo(Response response, String errorInfo) {
            onDialogDismiss();
                /*if (response != null && response.getBizCode() != 0) {
                    //限额错误，弹窗提示
                    DialogBuilder.showSimpleDialog(mActivity, "购买失败", errorInfo, null, null, "我知道了", null);
                    return;
                }*/
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
                iwPayResultListener.onPayFailInfo(errorInfo, "", response);
            }

        }

        @Override
        public void onFailInfo(String errorInfo) {
            super.onFailInfo(errorInfo);
            ToastUtil.showInCenterLong(mActivity, errorInfo);
            onDialogDismiss();
        }
    };

    /**
     * 5次轮询接口调用
     *
     * @param buyRes
     */
    private void retryGetPayState(BuyDingqibaoResponse buyRes) {
        mBuyDingqibaoResponse = buyRes;
        addTimes++;                             //    2    3   4   5
        currentReqTime = currentReqTime * 2;         //     2   4  8  16
        QueryBuyTransferRequest queryBuyTransferRequest = new QueryBuyTransferRequest();
        queryBuyTransferRequest.setBidOrderNo(buyRes.getBidOrderNo());
        ServiceSender.exec(mActivity, queryBuyTransferRequest, buyDingqibaoIwjwRespListener);
    }

    private void toDoBuyResult(final BuyDingqibaoResponse applyResponse) {
        if (applyResponse != null) {
            String bizStatus = applyResponse.getBizStatus();
            switch (bizStatus) {
                case "S":
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
                        onDialogDismiss();
                        iwPayResultListener.onPayStateDelay("处理中", applyResponse);
                    }
                    break;
                case "F":
                    /*
                    int errorcode = applyResponse.getErrorCode();
                    if (errorcode == RestException.PAY_MSGCODE_ERROR) {
                        //显示输入验证码
                        disLoadProgress();
                        ToastUtil.showInCenter(applyResponse.getMessage());
                    } else {
                    */
                    onDialogDismiss();
                    iwPayResultListener.onPayFailInfo("支付失败", "", applyResponse);
                    /*}*/
                    break;
                default:
                    LogUtil.d("debuglog", "支付状态未知" + applyResponse.toString());
                    ToastUtil.showInCenter(applyResponse.getMessage());
                    onDialogDismiss();
                    if (addTimes > 1) {
                        iwPayResultListener.onPayStateDelay("处理中", applyResponse);
                    }
                    break;
            }
        } else {
            onDialogDismiss();
            if (addTimes > 1) {
                iwPayResultListener.onPayStateDelay("处理中", applyResponse);
            }
        }
    }

    @Override
    BuyDialogShowInfo getDispalyInfo() {
        return new BuyDialogShowInfo.BankPayBuild()
                .setAmount(regularPayInfo.amount)
                //.setMoneyOutStr("购买房产宝" + regularPayInfo.getProductId() + "号")
                .setMoneyOutStr(regularPayInfo.getProductName())
                .setPayTypFrom("使用账户余额支付")
                .setPayTypFromIco(mActivity.getString(R.string.current_pay))
                .create();
    }

    @Override
    int getBizType() {
        return -1;
    }

    @Override
    double getAmount() {
        return regularPayInfo.getAmount();
    }
}
