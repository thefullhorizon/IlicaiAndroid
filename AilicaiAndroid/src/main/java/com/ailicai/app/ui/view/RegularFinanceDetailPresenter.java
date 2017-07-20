package com.ailicai.app.ui.view;


import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.model.request.RefreshProductDetailRequest;
import com.ailicai.app.model.request.RegularProductDetailRequest;
import com.ailicai.app.model.response.RefreshProductDetailResponse;
import com.ailicai.app.model.response.RegularProductDetailResponse;

/**
 * 房产宝详情页presenter
 * Created by duo.chen on 2016/1/8.
 */
public class RegularFinanceDetailPresenter {

    private RegularFinancingDetailActivity detailActivity;

    public RegularFinanceDetailPresenter(RegularFinancingDetailActivity
                                                 detailActivity) {
        this.detailActivity = detailActivity;
    }

    public void requestDetail(String id) {
        RegularProductDetailRequest request = new RegularProductDetailRequest();
        request.setProductId(id);
        ServiceSender.exec(detailActivity, request, new
                IwjwRespListener<RegularProductDetailResponse>() {


                    @Override
                    public void onStart() {
                        super.onStart();
                        if (null != detailActivity) {
                            detailActivity.showLoadView();
                        }
                    }

                    @Override
                    public void onJsonSuccess(RegularProductDetailResponse jsonObject) {
                        if (null != detailActivity) {
                            detailActivity.handleResponse(jsonObject);
                            detailActivity.showContentView();
                        }

                    }

                    @Override
                    public void onFailInfo(String errorInfo) {
                        super.onFailInfo(errorInfo);
                        if (null != detailActivity) {
                            detailActivity.showErrorView(errorInfo);
                        }
                    }
                });
    }

   /* public void getServerTime() {
        ServerTimeRequest request = new ServerTimeRequest();
        ServiceSender.exec(detailActivity, request, new
                IwjwRespListener<TimeResponse>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        if (null != detailActivity) {
                            detailActivity.showLoadView();
                        }
                    }

                    @Override
                    public void onJsonSuccess(TimeResponse response) {
                        if (response != null) {
                            if (response.getSysDate() != null) {
                                TimeUtil.calTime(response.getSysDate());
                            }
                            if (null != detailActivity) {
                                detailActivity.handleServiceTimeResponse();
                            }
                        }
                    }

                    @Override
                    public void onFailInfo(String errorInfo) {
                        if (null != detailActivity) {
                            detailActivity.showErrorView(errorInfo);
                        }
                    }
                });
    }*/

    public void autoRefresh(String id) {
        RefreshProductDetailRequest request = new RefreshProductDetailRequest();
        request.setProductId(String.valueOf(id));
        ServiceSender.exec(detailActivity, request, new
                IwjwRespListener<RefreshProductDetailResponse>() {

                    @Override
                    public void onJsonSuccess(RefreshProductDetailResponse jsonObject) {
                        if (null != detailActivity) {
                            detailActivity.autoRefreshSuccess(jsonObject);
                        }
                    }

                    @Override
                    public void onFailInfo(String errorInfo) {
                        super.onFailInfo(errorInfo);
                        if (detailActivity != null) {
                            detailActivity.autoRefreshFail();
                        }
                    }
                });
    }

    public void removeActivity() {
        detailActivity = null;
    }

}
