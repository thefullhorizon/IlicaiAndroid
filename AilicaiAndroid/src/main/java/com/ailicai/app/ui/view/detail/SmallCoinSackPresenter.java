package com.ailicai.app.ui.view.detail;

import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.model.request.ProductSimpleInfoRequest;
import com.ailicai.app.model.response.ProductSimpleInfoResponse;

/**
 * Created by nanshan on 2017/5/4.
 */
public class SmallCoinSackPresenter {

    private SmallCoinSackActivity mView;

    public void setView(SmallCoinSackActivity view){
        this.mView = view;
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
