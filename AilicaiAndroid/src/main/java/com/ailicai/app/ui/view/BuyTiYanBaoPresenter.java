package com.ailicai.app.ui.view;


import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.BuyTiyanbaoRequest;
import com.ailicai.app.model.response.BuyTiyanbaoResponse;
import com.ailicai.app.ui.buy.IwPwdPayResultListener;
import com.ailicai.app.ui.buy.TiYanBaoPay;

/**
 * Created by liyanan on 16/8/15.
 */
public class BuyTiYanBaoPresenter {
    private BuyTiYanBaoActivity activity;

    public BuyTiYanBaoPresenter(BuyTiYanBaoActivity activity) {
        this.activity = activity;
    }

    public void buyWithPwd(final int currentCouponId, final double couponAmount, final long activityId) {
        TiYanBaoPay.TiYanBaoPayInfo payInfo = new TiYanBaoPay.TiYanBaoPayInfo();
        payInfo.setCouponId(currentCouponId);
        payInfo.setCouponAmount(couponAmount);
        payInfo.setActivityId(activityId);
        payInfo.setCheckPwd(1);
        TiYanBaoPay tiYanBaoPay = new TiYanBaoPay(activity, payInfo, new IwPwdPayResultListener<BuyTiyanbaoResponse>() {
            @Override
            public void onPayPwdTryAgain() {
                buyWithPwd(currentCouponId, couponAmount, activityId);
            }

            @Override
            public void onPayComplete(BuyTiyanbaoResponse object) {
                //购买成功
                if (activity != null) {
                    activity.goToPayResultActivity(object);
                }
            }

            @Override
            public void onPayStateDelay(String msgInfo, BuyTiyanbaoResponse object) {

            }

            @Override
            public void onPayFailInfo(String msgInfo, String errorCode, BuyTiyanbaoResponse object) {

            }
        });
        tiYanBaoPay.pay();
    }

    public void buyWithNoPwd(final long activityId, int couponId) {
        BuyTiyanbaoRequest request = new BuyTiyanbaoRequest();
        request.setActivityId(activityId);
        request.setCouponId(couponId);
        request.setCheckPwd(0);
        ServiceSender.exec(activity, request, new IwjwRespListener<BuyTiyanbaoResponse>() {
            @Override
            public void onStart() {
                super.onStart();
                if (activity != null) {
                    activity.showLoadTranstView();
                }
            }

            @Override
            public void onJsonSuccess(BuyTiyanbaoResponse response) {
                if (activity != null) {
                    activity.showContentView();
                    activity.goToPayResultActivity(response);
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                if (activity != null) {
                    activity.showContentView();
                }
                ToastUtil.showInCenter(errorInfo);
            }
        });

    }

    public void removeActivity() {
        this.activity = null;
    }

}
