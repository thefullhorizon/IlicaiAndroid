package com.ailicai.app.ui.view.vocher;


import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.model.request.VoucherListByProductRequest;
import com.ailicai.app.model.response.VoucherListResponse;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.view.VoucherListActivity;

/**
 * 现金券列表presenter
 * Created by liyanan on 16/3/4.
 */
public class VoucherListPresenter {
    private final int PAGE_SIZE = 10;
    private VoucherListActivity activity;

    public VoucherListPresenter(VoucherListActivity activity) {
        this.activity = activity;
    }

    /**
     * 刷新红包列表
     *
     * @param reLoad true 页面重新加载,显示loadingView
     *               false 下拉刷新
     */
    public void refresh(final boolean reLoad, String productId) {
        VoucherListByProductRequest request = new VoucherListByProductRequest();
        request.setUserId(String.valueOf(UserInfo.getInstance().getUserId()));
        request.setOffset(0);
        request.setPageSize(PAGE_SIZE);
        request.setProductId(productId);
        ServiceSender.exec(activity, request, new IwjwRespListener<VoucherListResponse>() {
            @Override
            public void onStart() {
                super.onStart();
                if (reLoad && activity != null) {
                    activity.showLoadView();
                }
            }

            @Override
            public void onJsonSuccess(VoucherListResponse jsonObject) {
                //有红包
                if (activity != null) {
                    activity.refreshSuccess(jsonObject);
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                if (activity != null) {
                    activity.refreshFail(errorInfo);
                }
            }
        });
    }

    /**
     * 加载更多
     *
     * @param offset 已看过多少条
     */

    public void loadMore(int offset, String productId) {
        VoucherListByProductRequest request = new VoucherListByProductRequest();
        request.setOffset(offset);
        request.setPageSize(PAGE_SIZE);
        request.setProductId(productId);
        ServiceSender.exec(activity, request, new IwjwRespListener<VoucherListResponse>() {
            @Override
            public void onJsonSuccess(VoucherListResponse jsonObject) {
                if (activity != null) {
                    activity.loadMoreSuccess(jsonObject);
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                if (activity != null) {
                    activity.loadMoreFail(errorInfo);
                }
            }
        });
    }


    /**
     * 移除Activity的引用
     */
    public void removeActivity() {
        activity = null;
    }

}
