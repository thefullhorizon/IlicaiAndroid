package com.ailicai.app.ui.buy;

import android.support.v4.app.FragmentActivity;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.model.request.AutoBidSwitchRequest;
import com.ailicai.app.model.response.AutoBidSwitchResponse;
import com.ailicai.app.ui.dialog.BaseBuyFinanceDialog;
import com.ailicai.app.ui.dialog.BuyDialogShowInfo;
import com.huoqiu.framework.exception.RestException;

/**
 * Created by jeme on 2017/8/21.
 */

public class AutomaticTenderPay extends BaseBuyFinancePay {
    private AutomaticTenderPresenter mPresenter;
    private AutomaticTenderInfo mInfo;
    private boolean mIsSuccess = false;//操作是否成功，防止用户中途点击关闭按钮
    private boolean mIsFromClick = true;//是否用户手动点击关闭按钮

    public static class AutomaticTenderInfo {
        public boolean forOpen;
        public int strategyType;
        public Double reserveMoney;

    }
    public AutomaticTenderPay(FragmentActivity mActivity,AutomaticTenderInfo info, IwPwdPayResultListener iwPayResultListener) {
        super(mActivity, iwPayResultListener);
        this.mInfo = info;
        setDialogDismissListener(new BaseBuyFinanceDialog.DialogDismissListener() {
            @Override
            public void onDismiss() {
                if(mPresenter != null && mInfo != null){
                    mPresenter.pwdDialogClose(mInfo.forOpen,mIsSuccess,mIsFromClick);
                    mIsFromClick = true;
                }
            }
        });
    }
    public void setAutomaticPresenter(AutomaticTenderPresenter presenter){
        mPresenter = presenter;
    }

    @Override
    void toPayForInfo(String jsonObject) {
        SystemUtil.HideSoftInput(mActivity);
        submit(mInfo.forOpen,mInfo.strategyType,mInfo.reserveMoney,jsonObject);
       /* if(mPresenter != null && mInfo != null){
            mPresenter.submit(mInfo.forOpen,mInfo.strategyType,mInfo.reserveMoney,jsonObject);
        }*/
    }

    /***
     * 提交修改的自动投标的信息
     */
    public void submit(final boolean isOpenAuto,int strategyType,Double reserveBalance,String payPwd){
        final AutoBidSwitchRequest request = new AutoBidSwitchRequest();
        request.setAutoBidCommand(isOpenAuto ? 1 : 0);
        request.setPayPwd(payPwd);
        if(isOpenAuto) {
            request.setStrategyType(strategyType);
            request.setReserveBalance(reserveBalance);
        }
        ServiceSender.exec(mActivity,request,new IwjwRespListener<AutoBidSwitchResponse>(){

            @Override
            public void onStart() {
                super.onStart();
                loadProgress();
            }

            @Override
            public void onJsonSuccess(AutoBidSwitchResponse jsonObject) {
                disLoadProgress();
                if(jsonObject != null){
                    jsonObject.setForOpen(isOpenAuto);
                    if (jsonObject.getErrorCode() == RestException.PAY_PWD_ERROR) {
                        mIsFromClick = false;
                        clearPassword();
                        disLoadProgress();
                        onDialogDismiss();
                        int remainingCnt = jsonObject.getRemainingCnt();
                        if (remainingCnt == 0) {
                            BaseBuyFinancePay.showPwdLockedErrorDialog(mActivity, jsonObject.getMessage());
                        } else {
                            showPwdErrorResetDialog(mActivity, jsonObject.getMessage());
                        }
                        if(iwPayResultListener != null){
                            iwPayResultListener.onPayFailInfo("",jsonObject.getErrorCode()+"",jsonObject);
                        }

                    }else if(jsonObject.getErrorCode() == 0){//0表示修改成功
                        mIsSuccess = true;//只有这一种情况表示操作成功
                        if(iwPayResultListener != null){
                            iwPayResultListener.onPayComplete(jsonObject);
                        }
                        onDialogDismiss();
                    }else{
                        if(iwPayResultListener != null){
                            iwPayResultListener.onPayFailInfo(jsonObject.getMessage(),jsonObject.getErrorCode()+"",jsonObject);
                        }
                    }
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                disLoadProgress();
                if(iwPayResultListener != null){
                    AutoBidSwitchResponse response = new AutoBidSwitchResponse();
                    response.setForOpen(isOpenAuto);
                    iwPayResultListener.onPayFailInfo(errorInfo,"-1",response);
                }
            }
        });
    }

    @Override
    BuyDialogShowInfo getDispalyInfo() {
        return new BuyDialogShowInfo.BankPayBuild()
                .setAmount(0)
                //.setMoneyOutStr("购买房产宝" + regularPayInfo.getProductId() + "号")
                .setMoneyOutStr("")
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
        return 0;
    }
}
