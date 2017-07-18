package com.ailicai.app.ui.view.reserveredrecord;


import com.ailicai.app.model.request.ReserveCancelRequest;
import com.ailicai.app.ui.base.BaseBindActivity;

/**
 * Created by Owen on 2016/7/5
 */
public interface ReserveCancelInterface {

    BaseBindActivity getBaseActivity();

    ReserveCancelRequest getBookingCancelRequest();

    void cancelSuccess();

    void cancelFailed(String failInfo);
}
