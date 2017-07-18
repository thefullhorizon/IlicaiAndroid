package com.ailicai.app.ui.view;

import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.model.request.BannerListRequest;
import com.ailicai.app.model.response.BannerListResponse;
import com.ailicai.app.ui.login.UserInfo;

/**
 * Created by nanshan on 2017/5/22.
 */
public class RegularPayResultPresenter {

    private RegularPayResultActivity mView;

    public RegularPayResultPresenter(RegularPayResultActivity view){
        this.mView = view;
    }

    public void getBannerList(int bType){
        if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
            BannerListRequest request = new BannerListRequest();
            request.setUserId(UserInfo.getInstance().getUserId());
            request.setBtype(bType);
            ServiceSender.exec(mView, request, new BannerListCallback());
        }
    }

    class BannerListCallback extends IwjwRespListener<BannerListResponse> {

        @Override
        public void onStart() {
            mView.showLoadView();
        }

        @Override
        public void onJsonSuccess(BannerListResponse response) {
            mView.showContentView();
            if (response.getErrorCode() == 0) {
                mView.disposeBannerListInfo(response);
            } else {
                onFailInfo(response.getMessage());
            }
        }

        @Override
        public void onFailInfo(String errorInfo) {
            mView.showErrorView(errorInfo);
        }

    }




}
