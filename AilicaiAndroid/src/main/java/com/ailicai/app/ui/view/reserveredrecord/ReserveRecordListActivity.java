package com.ailicai.app.ui.view.reserveredrecord;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.eventbus.ReservePayEvent;
import com.ailicai.app.model.bean.Product;
import com.ailicai.app.model.request.ReserveCancelRequest;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.FragmentHelper;
import com.ailicai.app.ui.buy.IwPwdPayResultListener;
import com.ailicai.app.ui.buy.ReserveCancelPwdCheckDialog;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.widget.BottomRefreshListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Owen on 2016/3/10
 * 预约记录列表
 */
public class ReserveRecordListActivity extends BaseBindActivity implements SwipeRefreshLayout.OnRefreshListener, BottomRefreshListView.OnLoadMoreListener, ReserveRecordInterface, ReserveCancelInterface {

    @Bind(R.id.srl)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.listView)
    BottomRefreshListView listView;

    private List<Product> listData = new ArrayList<>();
    public ReserveRecordListAdapter adapter;
    private ReserveRecordListPresenter presenter;

    private int page = 0;

    private Product productBean;

    public Product getProductBean() {
        return productBean == null ? new Product() : productBean;
    }

    public void setProductBean(Product productBean) {
        this.productBean = productBean;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_booking_record;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initListView();
        initPresenter();
    }

    private void initListView() {
        swipeRefreshLayout.setColorSchemeResources(R.color.main_red_color);
        swipeRefreshLayout.setOnRefreshListener(this);
        listView.setOnLoadMoreListener(this);
        adapter = new ReserveRecordListAdapter(this, listData);
        listView.setAdapter(adapter);
    }

    private void initPresenter() {
        presenter = new ReserveRecordListPresenter(this, this);
        presenter.requestForBookingRecordData();
    }

    @Override
    public void onRefresh() {
        page = 0;
        presenter.requestForBookingRecordData();
    }

    @Override
    public void reloadData() {
        super.reloadData();
        presenter.requestForBookingRecordData();
    }

    @Override
    public void onLoadMore() {
        listView.setLoadingText("加载中");
        presenter.requestForBookingRecordData();
    }


    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void setListData(List<Product> list) {
        if (page == 0) {
            listData.clear();
            adapter.mapList.clear();
            adapter.positionList.clear();
        }
        listData.addAll(list);
        if (list.size() > 0) {
            // 是否小于一页的最大条数（是：加载完数据；否：继续请求下一页++）
            if (list.size() < 10) {
                loadedAll();
            } else {
                page = listData.size();
                listView.resetAll();
            }
        } else {
            // 是否是第一页（是：暂无数据；否加载完所有数据）
            if (page == 0) {
                noData();
            } else {
                loadedAll();
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 无数据
     */
    public void noData() {
        swipeRefreshLayout.setRefreshing(false);
        View emptyView = getLayoutInflater().inflate(R.layout.list_nodataview_layout, null);
        ((TextView) emptyView.findViewById(R.id.nodata_text)).setText("当前没有任何预约");
        showNoDataView(emptyView);
    }

    public void loadedAll() {
        listView.onAllLoaded();
        listView.setPromptText("没有更多记录");
    }

    private ReserveCancelPwdCheckDialog pwdDialog;

    public void showCancelDialog() {
        FragmentHelper fragmentHelper = new FragmentHelper(getSupportFragmentManager());
        ReserveCancelDialog dialog = new ReserveCancelDialog();
        dialog.setReserveCancleEvent(event);
        fragmentHelper.showDialog(null, dialog);
    }

    private ReserveCancelDialog.ReserveCancelEvent event = new ReserveCancelDialog.ReserveCancelEvent() {
        @Override
        public void cancelBooking() {

            pwdDialog = new ReserveCancelPwdCheckDialog(ReserveRecordListActivity.this, new IwPwdPayResultListener() {
                @Override
                public void onPayPwdTryAgain() {
                    pwdDialog.show();
                }

                @Override
                public void onPayComplete(Object object) {
                    ReserveRecordListPresenter.requestForBookingCancel(ReserveRecordListActivity.this);
                }

                @Override
                public void onPayStateDelay(String msgInfo, Object object) {
                }

                @Override
                public void onPayFailInfo(String msgInfo, String errorCode, Object object) {
                }
            }, new ReserveCancelPwdCheckDialog.ShowInfo() {
                @Override
                public double showAmount() {
                    return productBean.getBidAmount();
                }

                @Override
                public String showMoneyOutStr() {
                    return "取消" + productBean.getProductName();
                }
            });
            pwdDialog.show();
        }
    };

    @Override
    public BaseBindActivity getBaseActivity() {
        return this;
    }

    @Override
    public ReserveCancelRequest getBookingCancelRequest() {
        ReserveCancelRequest request = new ReserveCancelRequest();
        request.setUserId(UserInfo.getInstance().getUserId());
        request.setPaypwd(pwdDialog.getPayPwd());
        request.setBidOrderNo(getProductBean().getBidOrderNo());
        return request;
    }

    @Override
    public void cancelSuccess() {
        changerAfterCancelState();
    }

    @Override
    public void cancelFailed(String failInfo) {
        ToastUtil.showInCenter(failInfo);
    }

    public void changerAfterCancelState() {
        //post reserve pay cancel event.
        EventBus.getDefault().post(new ReservePayEvent());

        productBean.setOrderStatus(5);
        productBean.setCanbecancel(0);
        adapter.notifyDataSetChanged();
        ToastUtil.showInCenter("取消预约成功");
    }

    public void requestForGetBuyListData(String bidOrderNo, ReserveRecordListAdapter.ViewHodle hodle) {
        presenter.requestForGetBuyListData(bidOrderNo, hodle);
    }

    public void smoothScrollToPosition(int position) {

        final int firstListItemPosition = listView.getFirstVisiblePosition() - 1;
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position >= firstListItemPosition && position <= lastListItemPosition) {
            int bottom = listView.getChildAt(position - firstListItemPosition).getBottom();
            /**View childView = mAdapter.getView(position, null, mHomePage);
             *int UNBOUNDED = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
             *childView.measure(UNBOUNDED, UNBOUNDED);*/

            final int offset = bottom - listView.getHeight();
            if (offset > 0) listView.smoothScrollBy(offset, 200);

        }
    }

}
