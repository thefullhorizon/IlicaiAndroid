package com.ailicai.app.ui.reserve;


import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.model.request.ReserveListRequest;
import com.ailicai.app.model.response.ReserveListResponse;

/**
 * Created by Owen on 2016/5/25
 */
public class ReserveListPresenter {
    private final int PAGE_SIZE = 10;
    private ReserveListActivity activity;
    private ReserveListInterface mInterface;

    public ReserveListPresenter(ReserveListActivity activity, ReserveListInterface mInterface) {
        this.activity = activity;
        this.mInterface = mInterface;
    }

    public void requestForReserveListData() {
        ReserveListRequest request = new ReserveListRequest();
        request.setProductId(activity.productId);
        request.setHorizon(activity.horizon);
        request.setOffset(mInterface.getPage());
        request.setPageSize(PAGE_SIZE);
        ServiceSender.exec(activity, request, new IwjwRespListener<ReserveListResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                if (activity != null) {
                    activity.showLoadView();
                }
            }

            @Override
            public void onJsonSuccess(ReserveListResponse jsonObject) {
                if (activity != null) {
                    activity.showContentView();
                    activity.onRefreshFinished();
                    activity.setTime(jsonObject.getInterestTimeStr());
                }
                mInterface.setListData(jsonObject.getRows());
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                if (activity != null) {
                    activity.onRefreshFinished();
                    activity.showErrorView(errorInfo);
                }
            }
        });
    }

}
