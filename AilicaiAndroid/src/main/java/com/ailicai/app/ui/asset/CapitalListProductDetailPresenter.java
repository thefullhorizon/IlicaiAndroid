package com.ailicai.app.ui.asset;


import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.model.request.ProductSimpleInfoRequest;
import com.ailicai.app.model.request.ReserveSimpleInfoRequest;
import com.ailicai.app.model.request.TiyanbaoSimpleInfoRequest;
import com.ailicai.app.model.response.ProductSimpleInfoResponse;
import com.ailicai.app.model.response.ReserveSimpleInfoResponse;
import com.ailicai.app.model.response.TiyanbaoSimpleInfoResponse;

/**
 * Created by Administrator on 2016/8/19.
 */
public class CapitalListProductDetailPresenter {


    private CapitalListProductDetailActivity mView;

    public void setView(CapitalListProductDetailActivity view){
        this.mView = view;
    }
    public void getReserveProductDetailData(String productId) {
        final ReserveSimpleInfoRequest request = new ReserveSimpleInfoRequest();
        request.setProductId(productId);
        ServiceSender.exec(mView, request, new IwjwRespListener<ReserveSimpleInfoResponse>() {
            @Override
            public void onStart() {
                mView.showLoadView();
            }

            @Override
            public void onJsonSuccess(ReserveSimpleInfoResponse response) {
                mView.showContentView();
                if (response.getErrorCode() == 0) {
                    mView.disposeReserveProductInfo(response);
                } else {
                    onFailInfo(response.getMessage());
                }

            }

            @Override
            public void onFailInfo(String errorInfo) {
                mView.showErrorView(errorInfo);
            }
        });
    }

    public void getTiyanbaoDetailData(long tiyanbaoId){
        TiyanbaoSimpleInfoRequest request = new TiyanbaoSimpleInfoRequest();
        request.setCouponId(tiyanbaoId);
        ServiceSender.exec(mView, request, new IwjwRespListener<TiyanbaoSimpleInfoResponse>() {
            @Override
            public void onStart() {
                mView.showLoadView();
            }

            @Override
            public void onJsonSuccess(TiyanbaoSimpleInfoResponse response) {
                mView.showContentView();
                if (response.getErrorCode() == 0){
                    mView.disposeTiyanbaoInfo(response);
                }else {
                    onFailInfo(response.getMessage());
                }

            }

            @Override
            public void onFailInfo(String errorInfo) {
                mView.showErrorView(errorInfo);
            }
        });
    }


    public void getProductDetailData(String productId) {
        ProductSimpleInfoRequest request = new ProductSimpleInfoRequest();
        request.setProductId(productId);
        ServiceSender.exec(mView, request, new IwjwRespListener<ProductSimpleInfoResponse>() {
            @Override
            public void onStart() {
                mView.showLoadView();
            }

            @Override
            public void onJsonSuccess(ProductSimpleInfoResponse response) {
                mView.showContentView();
                if (response.getErrorCode() == 0) {
                    mView.disposeProductInfo(response);
                } else {
                    onFailInfo(response.getMessage());
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                mView.showErrorView(errorInfo);
            }
        });
    }

}
