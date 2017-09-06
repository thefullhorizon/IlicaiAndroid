package com.ailicai.app.ui.mine.presenter;

import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.model.request.MemberInfoRequest;
import com.ailicai.app.model.response.BankcardListResponse;
import com.ailicai.app.model.response.MemberInfoResponse;
import com.ailicai.app.ui.mine.MemberShipCenterActivity;

/**
 * name: MemberShipCenterPersenter <BR>
 * description: 会员中心persenter <BR>
 * create date: 2017/9/6
 *
 * @author: IWJW Zhou Xuan
 */
public class MemberShipCenterPersenter {

    MemberShipCenterActivity activity;

    public MemberShipCenterPersenter(MemberShipCenterActivity activity) {
        this.activity = activity;
    }

    public void httpForMemberShipInfo() {
        MemberInfoRequest request = new MemberInfoRequest();
        ServiceSender.exec(activity, request, new IwjwRespListener<MemberInfoResponse>() {
            @Override
            public void onStart() {
                activity.showLoadView();
            }

            @Override
            public void onJsonSuccess(MemberInfoResponse response) {
                activity.showContentView();
                activity.bindData(response);
            }

            @Override
            public void onFailInfo(String error) {
                activity.showErrorView(error);
            }
        });
    }
}
