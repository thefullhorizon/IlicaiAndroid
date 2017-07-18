package com.ailicai.app.ui.buy;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.BuyTiyanbaoRequest;
import com.ailicai.app.model.response.BuyTiyanbaoResponse;
import com.ailicai.app.ui.dialog.BuyDialogShowInfo;
import com.huoqiu.framework.exception.RestException;

/**
 * Created by liyanan on 16/8/15.
 */
public class TiYanBaoPay extends BaseBuyFinancePay {

    public static class TiYanBaoPayInfo {
        private long activityId;//活动id
        private int couponId;//卡券id
        private int checkPwd;//是否验证密码 0否 1是
        private double couponAmount;

        public long getActivityId() {
            return activityId;
        }

        public void setActivityId(long activityId) {
            this.activityId = activityId;
        }

        public int getCouponId() {
            return couponId;
        }

        public void setCouponId(int couponId) {
            this.couponId = couponId;
        }

        public int getCheckPwd() {
            return checkPwd;
        }

        public void setCheckPwd(int checkPwd) {
            this.checkPwd = checkPwd;
        }

        public double getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(double couponAmount) {
            this.couponAmount = couponAmount;
        }
    }

    TiYanBaoPayInfo tiYanBaoPayInfo;

    public TiYanBaoPay(FragmentActivity mActivity, TiYanBaoPayInfo payInfo, IwPwdPayResultListener iwPayResultListener) {
        super(mActivity, iwPayResultListener);
        this.tiYanBaoPayInfo = payInfo;
    }

    @Override
    void toPayForInfo(String s) {
        if (TextUtils.isEmpty(s)) {
            throw new IllegalArgumentException("密码加密有误!检查一下");
        }
        final BuyTiyanbaoRequest request = new BuyTiyanbaoRequest();
        request.setPaypwd(s);
        request.setActivityId(tiYanBaoPayInfo.getActivityId());
        request.setCouponId(tiYanBaoPayInfo.getCouponId());
        request.setCheckPwd(tiYanBaoPayInfo.getCheckPwd());
        ServiceSender.exec(mActivity, request, new IwjwRespListener<BuyTiyanbaoResponse>() {
            @Override
            public void onStart() {
                super.onStart();
                loadProgress();
            }

            @Override
            public void onJsonSuccess(BuyTiyanbaoResponse response) {
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
                ToastUtil.showInCenterLong(mActivity, errorInfo);
                onDialogDismiss();
            }
        });

    }

    @Override
    BuyDialogShowInfo getDispalyInfo() {
        return new BuyDialogShowInfo.BankPayBuild()
                .setAmount(tiYanBaoPayInfo.couponAmount)
                //.setMoneyOutStr("购买房产宝" + regularPayInfo.getProductId() + "号")
                .setMoneyOutStr("购买体验宝")
                .setPayTypFrom("使用体验金支付")
                .setPayTypFromIco(mActivity.getString(R.string.current_pay))
                .create();
    }

    @Override
    int getBizType() {
        return -1;
    }

    @Override
    double getAmount() {
        return tiYanBaoPayInfo.couponAmount;
    }
}
