package com.ailicai.app.ui.buy;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.model.request.AutoBidRequest;
import com.ailicai.app.model.request.AutoBidSwitchRequest;
import com.ailicai.app.model.response.AutoBidResponse;
import com.ailicai.app.model.response.AutoBidSwitchResponse;
import com.ailicai.app.ui.base.BasePresenter;
import com.ailicai.app.ui.base.BaseView;
import com.huoqiu.framework.exception.RestException;

/**
 * Created by jeme on 2017/8/18.
 */

public class AutomaticTenderPresenter extends BasePresenter<AutomaticTenderPresenter.AutomaticTenderView> {

    private AutomaticTenderPay mPay;

    public interface AutomaticTenderView extends BaseView{

        void processSuccess(@NonNull AutoBidResponse response);

        void processAfterSubmit(boolean forOpen, boolean isSuccess, String message);

        void pwdDialogClose(boolean forOpen);

    }

    /***
     * 加载自动投标页面的数据内容
     */
    public void loadData(){
        getMvpView().showLoading();
        AutoBidRequest request = new AutoBidRequest();
        ServiceSender.exec(getContext(),request,new IwjwRespListener<AutoBidResponse>(){

            @Override
            public void onJsonSuccess(AutoBidResponse jsonObject) {
                getMvpView().hideLoading();
                if(jsonObject != null){
                    getMvpView().processSuccess(jsonObject);
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                getMvpView().hideLoading();
            }
        });
    }

    /***
     * 提交修改的自动投标的信息
     */
    public void submit(final boolean isOpenAuto,int strategyType,Double reserveBalance,String payPwd){
        AutoBidSwitchRequest request = new AutoBidSwitchRequest();
        request.setAutoBidCommand(isOpenAuto ? 1 : 0);
        request.setPayPwd(payPwd);
        if(isOpenAuto) {
            request.setStrategyType(strategyType);
            request.setReserveBalance(reserveBalance);
        }
        ServiceSender.exec(getContext(),request,new IwjwRespListener<AutoBidSwitchResponse>(){

            @Override
            public void onStart() {
                super.onStart();
                getMvpView().showLoading();
            }

            @Override
            public void onJsonSuccess(AutoBidSwitchResponse jsonObject) {
                getMvpView().hideLoading();
                if(jsonObject != null){
                    if (jsonObject.getErrorCode() == RestException.PAY_PWD_ERROR) {
                        mPay.clearPassword();
                        mPay.disLoadProgress();
                        mPay.onDialogDismiss();
                        int remainingCnt = jsonObject.getRemainingCnt();
                        if (remainingCnt == 0) {
                            BaseBuyFinancePay.showPwdLockedErrorDialog((Activity)getContext(), jsonObject.getMessage());
                        } else {
                            mPay.showPwdErrorResetDialog((Activity)getContext(), jsonObject.getMessage());
                        }
                        getMvpView().processAfterSubmit(isOpenAuto,false,"");
                    }else if(jsonObject.getErrorCode() == 0){//0表示修改成功
                        getMvpView().processAfterSubmit(isOpenAuto,true,jsonObject.getMessage());
                        mPay.onDialogDismiss();
                    }else{
                        getMvpView().processAfterSubmit(isOpenAuto,false,jsonObject.getMessage());
                    }
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                getMvpView().hideLoading();
                getMvpView().processAfterSubmit(isOpenAuto,false,errorInfo);
            }
        });
    }

    /***
     * 关闭或者点击地步确认按钮需要交易密码验证
     */
    public void showPwdDialogForOpen(boolean forOpen,int strategyType,Double reserveMoney){

      /*  if(mPay != null){
            return;
        }*/
        final AutomaticTenderPay.AutomaticTenderInfo info = new AutomaticTenderPay.AutomaticTenderInfo();
        info.forOpen = forOpen;
        info.strategyType = strategyType;
        info.reserveMoney = reserveMoney;
        mPay = new AutomaticTenderPay((FragmentActivity)getContext(), info,new IwPwdPayResultListener<AutoBidSwitchResponse>() {
            @Override
            public void onPayPwdTryAgain() {
                showPwdDialogForOpen(info.forOpen, info.strategyType, info.reserveMoney);
            }

            @Override
            public void onPayComplete(AutoBidSwitchResponse object) {
                if(object != null) {
                    getMvpView().processAfterSubmit(object.isForOpen(), true, object.getMessage());
                }
            }

            @Override
            public void onPayStateDelay(String msgInfo, AutoBidSwitchResponse object) {
            }

            @Override
            public void onPayFailInfo(String msgInfo, String errorCode, AutoBidSwitchResponse object) {
                if(object != null) {
                    getMvpView().processAfterSubmit(object.isForOpen(), false, msgInfo);
                }
            }
        });
        mPay.setAutomaticPresenter(this);
        mPay.pay();
    }

    /***
     * 关闭密码框
     */
    public void pwdDialogClose(boolean needRefresh){
        getMvpView().pwdDialogClose(needRefresh);
    }

    public Double getReserveMoney(String reserveMoney){
        Double reserve = 0d;
        if(!TextUtils.isEmpty(reserveMoney)){

            try {
                reserve = Double.valueOf(reserveMoney);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                reserve = -1d;
            }
        }
        return reserve;
    }
}
