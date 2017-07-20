package com.ailicai.app.ui.view;


import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.model.request.FinanceProductDetailRequest;
import com.ailicai.app.model.response.FinanceProductDetailResponse;

/**
 * 房产宝项目详情页presenter
 * Created by liyanan on 16/4/11.
 */
public class ProjectDetailPresenter {
    private ProjectDetailFragment fragment;

    public ProjectDetailPresenter(ProjectDetailFragment fragment) {
        this.fragment = fragment;
    }

    /**
     * 请求数据
     *
     * @param id
     */
    public void requestData(String id) {
        final FinanceProductDetailRequest request = new FinanceProductDetailRequest();
        request.setProductId(id);
        ServiceSender.exec(fragment, request, new IwjwRespListener<FinanceProductDetailResponse>() {
            @Override
            public void onStart() {
                super.onStart();
                if (fragment != null) {
                    fragment.showLoadView();
                }
            }

            @Override
            public void onJsonSuccess(FinanceProductDetailResponse jsonObject) {
                if (fragment != null) {
                    fragment.initData(jsonObject);
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                if (fragment != null) {
                    fragment.showErrorView(errorInfo);
                }
            }
        });
    }

    /**
     * 移除fragment
     */
    public void removeFragment() {
        fragment = null;
    }
}
