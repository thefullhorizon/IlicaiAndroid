package com.ailicai.app.ui.view.reserveredrecord;

import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.bean.ReserveRecordBuyListBean;
import com.ailicai.app.model.request.ReserveRecordBuyListRequest;
import com.ailicai.app.model.request.ReserveRecordListRequest;
import com.ailicai.app.model.response.ReserveRecordBuyListResponse;
import com.ailicai.app.model.response.ReserveRecordListResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.buy.ReserveCancelInterface;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.widget.DialogBuilder;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by Owen on 2016/3/10
 */
public class ReserveRecordListPresenter {
    private final int PAGE_SIZE = 10;
    private ReserveRecordListActivity activity;
    private ReserveRecordInterface mInterface;

    public ReserveRecordListPresenter(ReserveRecordListActivity activity, ReserveRecordInterface mInterface) {
        this.activity = activity;
        this.mInterface = mInterface;
    }

    public void requestForBookingRecordData() {
        ReserveRecordListRequest request = new ReserveRecordListRequest();
        request.setUserId(UserInfo.getInstance().getUserId());
        request.setOffset(mInterface.getPage());
        request.setPageSize(PAGE_SIZE);
        ServiceSender.exec(activity, request, new IwjwRespListener<ReserveRecordListResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                if (activity != null) {
                    activity.showLoadView();
                }
            }

            @Override
            public void onJsonSuccess(ReserveRecordListResponse jsonObject) {
                if (activity != null) {
                    activity.showContentView();
                    activity.swipeRefreshLayout.setRefreshing(false);
                }
                mInterface.setListData(jsonObject.getReserveList());
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                if (activity != null) {
                    activity.swipeRefreshLayout.setRefreshing(false);
                    activity.showErrorView(errorInfo);
                }
            }
        });
    }

    public static void requestForBookingCancel(final ReserveCancelInterface reserveCancelInterface) {
        final BaseBindActivity mActivity = reserveCancelInterface.getBaseActivity();
        ServiceSender.exec(mActivity, reserveCancelInterface.getBookingCancelRequest(), new IwjwRespListener<ReserveRecordListResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                if (mActivity != null) {
                    mActivity.showLoadTranstView();
                }
            }

            @Override
            public void onJsonSuccess(ReserveRecordListResponse jsonObject) {
                reserveCancelInterface.cancelSuccess();
            }

            @Override
            public void onFailInfo(Response response, String errorInfo) {
//                if (response == null) {
//                    ToastUtil.showInCenter(errorInfo);
//                } else {
//                    DialogBuilder.showSimpleDialog(mActivity, "提示", errorInfo, null, null, "我知道了", null);
//                }
                reserveCancelInterface.cancelFailed(errorInfo);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (mActivity != null) {
                    mActivity.showContentView();
                }
            }
        });
    }

    public void requestForGetBuyListData(String bidOrderNo, final ReserveRecordListAdapter.ViewHodle hodle) {
        ReserveRecordBuyListRequest request = new ReserveRecordBuyListRequest();
        request.setBidOrderNo(bidOrderNo);
        ServiceSender.exec(activity, request, new IwjwRespListener<ReserveRecordBuyListResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                if (activity != null) {
                    activity.showLoadView();
                }
            }

            @Override
            public void onJsonSuccess(ReserveRecordBuyListResponse jsonObject) {
                if (activity != null) {
                    activity.showContentView();
                    if (jsonObject != null) {
                        List<ReserveRecordBuyListBean> rowList = jsonObject.getRows();
                        activity.adapter.setBuyList(rowList, hodle);
                    }
                }
            }

            @Override
            public void onFailInfo(Response response, String errorInfo) {
                if (response == null) {
                    ToastUtil.showInCenter(errorInfo);
                } else {
                    DialogBuilder.showSimpleDialog(activity, "提示", errorInfo, null, null, "我知道了", null);
                }
                activity.showContentView();
            }
        });
    }

}
