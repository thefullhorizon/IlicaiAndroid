package com.ailicai.app.ui.view.reserveredrecord;


import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.model.bean.ProductInvestRecord;
import com.ailicai.app.model.request.ProductInvestRecordRequest;
import com.ailicai.app.model.response.ProductInvestRecordListResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 投资记录presenter
 * Created by liyanan on 16/4/8.
 */
public class ProductInvestRecordPresenter {
    private ProductInvestRecordFragment fragment;

    public ProductInvestRecordPresenter(ProductInvestRecordFragment fragment) {
        this.fragment = fragment;
    }

    public void requestData(String id, final int offset, final boolean showLoadView) {
        ProductInvestRecordRequest request = new ProductInvestRecordRequest();
        request.setPageSize(10);
        request.setOffset(offset);
        request.setProductId(id);
        ServiceSender.exec(fragment, request, new IwjwRespListener<ProductInvestRecordListResponse>() {
            @Override
            public void onStart() {
                super.onStart();
                if (showLoadView && fragment != null) {
                    fragment.showLoadView();
                }
            }

            @Override
            public void onJsonSuccess(ProductInvestRecordListResponse jsonObject) {
                if (fragment != null) {
                    if (showLoadView) {
                        //第一次
                        fragment.requestSuccess(jsonObject);
                    } else {
                        //加载更多
                        fragment.loadMoreSuccess(jsonObject);
                    }
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                if (fragment != null) {
                    if (showLoadView) {
                        fragment.requestFail(errorInfo);
                    } else {
                        fragment.loadMoreFail(errorInfo);
                    }
//                    //这是测试代码
//                    if (showLoadView) {
//                        fragment.requestSuccess(buildTestResponse(offset));
//                    } else {
//                        fragment.loadMoreSuccess(buildTestResponse(offset));
//                    }
                }
            }
        });

    }

    private ProductInvestRecordListResponse buildTestResponse(int offset) {
        ProductInvestRecordListResponse response = new ProductInvestRecordListResponse();
        response.setTotal(100);
        List<ProductInvestRecord> list = new ArrayList<>();
        for (int i = offset; i < offset + 10; i++) {
            ProductInvestRecord history = new ProductInvestRecord();
            history.setAmtStr(String.valueOf(i));
            history.setMoblie("12345678900");
            history.setTimeStr("2016-03-06:18:00");
            list.add(history);
        }
        response.setRows(list);
        return response;
    }

    public void removeFragment() {
        fragment = null;
    }

}
