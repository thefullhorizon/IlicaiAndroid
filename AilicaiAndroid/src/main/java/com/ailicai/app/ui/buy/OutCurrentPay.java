package com.ailicai.app.ui.buy;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.SaleHuoqibaoRequest;
import com.ailicai.app.model.response.SaleHuoqibaoResponse;
import com.ailicai.app.model.response.SaleStatusQueryRequest;
import com.ailicai.app.model.response.account.AccountResponse;
import com.ailicai.app.ui.dialog.BuyDialogShowInfo;
import com.ailicai.app.ui.login.AccountInfo;
import com.ailicai.app.widget.DialogBuilder;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Response;

/**
 * Created by Jer on 2016/1/7.
 * 钱包转出到银行卡
 */
public class OutCurrentPay extends BaseBuyFinancePay {

    private static int reqMaxTimes = 5;
    CurrentPayInfo currentPayInfo;

    Handler mHandler;
    //上一次支付结果
    SaleHuoqibaoResponse msaleHuoqibaoResponse;
    //请求间隔时间
    private int currentReqTime = 1;
    //请求次数1 2 4 8 16
    private int addTimes = 1;
    IwjwRespListener<SaleHuoqibaoResponse> saleHuoqibaoResponseIwjwRespListener = new IwjwRespListener<SaleHuoqibaoResponse>() {

        @Override
        public void onStart() {
            super.onStart();
            if (addTimes == 1) {
                loadProgress();
                //  PayPresenter.showProgressTextHorDialog(mActivity, "加载中...");
            }
        }


        @Override
        public void onJsonSuccess(final SaleHuoqibaoResponse response) {
            if (response.getErrorCode() == RestException.PAY_PWD_ERROR) {
                //    ToastUtil.showInCenter(mActivity, saleHuoqibaoResponse.getMessage());
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
            toDoBuyResult(response);
        }

        @Override
        public void onFailInfo(Response response, String errorInfo) {
            if (response != null && response.getErrorCode() == 10) {
                onDialogDismiss();
                //弹层报错
                DialogBuilder.showSimpleDialog(mActivity, "提示", errorInfo, null, null, "我知道了", null).setCanceledOnTouchOutside(false);
            } else {
                super.onFailInfo(response, errorInfo);
            }
        }

        @Override
        public void onFailInfo(String errorInfo) {
            super.onFailInfo(errorInfo);
            ToastUtil.showInCenter(errorInfo);
            LogUtil.d("debuglog", "第" + addTimes + "次请求后接口返回不对，停止请求");
            onDialogDismiss();
            //   PayPresenter.dismissProgressDialog();
            if (addTimes > 1) {
                iwPayResultListener.onPayStateDelay("处理中", msaleHuoqibaoResponse);
            }
        }
    };

    public OutCurrentPay(FragmentActivity mActivity, CurrentPayInfo currentPayInfo, IwPwdPayResultListener iwPayResultListener) {
        super(mActivity, iwPayResultListener);
        this.currentPayInfo = currentPayInfo;
        mHandler = new Handler();
    }

    @Override
    void toPayForInfo(String s) {
        //  onDialogDismiss();
        SaleHuoqibaoRequest saleHuoqibaoRequest = new SaleHuoqibaoRequest();
        saleHuoqibaoRequest.setAmount(currentPayInfo.getAmount()+"");
        saleHuoqibaoRequest.setAccountType(currentPayInfo.getAccountType());
        saleHuoqibaoRequest.setPayMethod(currentPayInfo.getPayMethod());
        saleHuoqibaoRequest.setPaypwd(s);
        saleHuoqibaoRequest.setOutTradeNo(getOutTradeNoResponse().getOutTradeNo());
        ServiceSender.exec(mActivity, saleHuoqibaoRequest, saleHuoqibaoResponseIwjwRespListener);
    }

    private void retryGetPayState(SaleHuoqibaoResponse saleResp) {
        msaleHuoqibaoResponse = saleResp;
        addTimes++;                             //    2    3   4   5
        currentReqTime = currentReqTime * 2;         //     2   4  8  16
        SaleStatusQueryRequest saleStatusQueryRequest = new SaleStatusQueryRequest();
        saleStatusQueryRequest.setAccountType(currentPayInfo.getAccountType());
        saleStatusQueryRequest.setPayMethod(currentPayInfo.getPayMethod());
        saleStatusQueryRequest.setOutTradeNo(saleResp.getOutTradeNo());
        LogUtil.d("debuglog", "等待轮询" + addTimes);
        ServiceSender.exec(mActivity, saleStatusQueryRequest, saleHuoqibaoResponseIwjwRespListener);
    }

    void toDoBuyResult(final SaleHuoqibaoResponse saleHuoqibaoResponse) {
        if (saleHuoqibaoResponse != null) {
            String bizStatus = saleHuoqibaoResponse.getBizStatus();
            switch (bizStatus) {
                case "S":
                    //      PayPresenter.dismissProgressDialog();
                    onDialogDismiss();
                    iwPayResultListener.onPayComplete(saleHuoqibaoResponse);
                    break;
                case "P":
                    //    2    3   4   5
                    if (addTimes <= reqMaxTimes) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mActivity.isFinishing()) {
                                    return;
                                }
                                retryGetPayState(saleHuoqibaoResponse);
                            }
                        }, 1000 * currentReqTime);  //2   4   8  16
                    } else {
                        currentReqTime = 1;
                        addTimes = 1;
                        //   PayPresenter.dismissProgressDialog();
                        onDialogDismiss();
                        iwPayResultListener.onPayStateDelay("处理中", saleHuoqibaoResponse);
                    }
                    break;
                case "F":
                    //     PayPresenter.dismissProgressDialog();
                    onDialogDismiss();
                    iwPayResultListener.onPayFailInfo("支付失败", "", saleHuoqibaoResponse);
                    break;
                default:
                    ToastUtil.showInCenter(saleHuoqibaoResponse.getMessage() + "-bizStatus:" + bizStatus);
                    //   PayPresenter.dismissProgressDialog();
                    onDialogDismiss();
                    if (addTimes > 1) {
                        iwPayResultListener.onPayStateDelay("处理中", saleHuoqibaoResponse);
                    }
                    break;
            }
        } else {
            //PayPresenter.dismissProgressDialog();
            onDialogDismiss();
            if (addTimes > 1) {
                iwPayResultListener.onPayStateDelay("处理中", saleHuoqibaoResponse);
            }
        }
    }

    @Override
    BuyDialogShowInfo getDispalyInfo() {
        AccountResponse accountResponse = AccountInfo.getInstance().getAccountResponse();
        String copywriter1 = null, copywriter2 = null;
        if ("106".equals(currentPayInfo.getAccountType())) {
            copywriter1 = "提现至" + accountResponse.getBankName() + "(" + accountResponse.getBankcardTailNo() + ")";
            copywriter2 = "使用账户余额支付";
        } else if ("101".equals(currentPayInfo.getAccountType())) {
            copywriter2 = "使用活期宝支付";
            //支付到的账户类型 1-银行卡；2-账户余额
            if ("1".equals(currentPayInfo.getPayMethod())) {
                copywriter1 = "转出至"+accountResponse.getBankName() + "(" + accountResponse.getBankcardTailNo() + ")";
            } else if ("2".equals(currentPayInfo.getPayMethod())) {
                copywriter1 = "转出至账户余额";
            }
        }
        return new BuyDialogShowInfo.BankPayBuild()
                .setAmount(currentPayInfo.amount)
                .setMoneyOutStr(copywriter1)
                .setPayTypFrom(copywriter2).setPayTypFromIco(mActivity.getString(R.string.current_pay))
                .create();
    }

    @Override
    int getBizType() {
        return 2;
    }

    @Override
    double getAmount() {
        return currentPayInfo.getAmount();
    }

    public static class CurrentPayInfo {
        private double amount;
        private String accountType; //收银台类型：101-活期宝；106-用户账户
        private String payMethod; //支付到的账户类型 1-银行卡；2-账户余额 说明：活期宝收银台需指定

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
