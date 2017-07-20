package com.ailicai.app.ui.reserve;

import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.CheckReservePasswdRequest;
import com.ailicai.app.model.request.ReserveDetailRequest;
import com.ailicai.app.model.response.ReserveDetailResponse;
import com.ailicai.app.ui.login.UserInfo;
import com.huoqiu.framework.rest.Response;

/**
 * Created by David on 16/3/11.
 */
public class ReservePresenter {

    ReserveActivity activity;

    public ReservePresenter(ReserveActivity activity) {
        this.activity = activity;
    }

    public void sendReserveService() {
        ReserveDetailRequest request = new ReserveDetailRequest();
        request.setUserId(UserInfo.getInstance().getUserId());
        ServiceSender.exec(activity, request, new ReserveCallback());
    }

    private void handleReserveResponse(ReserveDetailResponse response) {
        activity.deployPageElements(response);
        activity.onRefreshFinished();
    }

    class ReserveCallback extends IwjwRespListener<ReserveDetailResponse> {

        @Override
        public void onStart() {
            if (activity == null) return;
            activity.showLoadView();
        }

        @Override
        public void onJsonSuccess(ReserveDetailResponse response) {
            if (activity == null) return;
            activity.showContentView();
            activity.presenter.handleReserveResponse(response);
        }

        @Override
        public void onFailInfo(String error) {
            if (activity == null) return;
            ToastUtil.showInBottom(activity, error);
            activity.onRefreshFinished();
        }
    }

    /**
     * 校验红包
     *
     * @param reserveCommand
     */
    public void checkReserveCommand(final String reserveCommand) {
        CheckReservePasswdRequest request = new CheckReservePasswdRequest();
        request.setReservePwd(reserveCommand);
        ServiceSender.exec(activity, request, new IwjwRespListener<Response>() {
            @Override
            public void onJsonSuccess(Response jsonObject) {
                if (jsonObject.getBizCode() == 0) {
                    activity.checkCommandSuccess(reserveCommand);
                } else {
                    activity.checkCommandError(jsonObject.getMessage());
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                activity.checkCommandError(errorInfo);
            }
        });
    }

    public void removeActivity() {
        activity = null;
    }

}
