package com.ailicai.app.ui.message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.PVIDHandler;
import com.ailicai.app.common.push.model.PushMessage;
import com.ailicai.app.eventbus.RefreshPushEvent;
import com.ailicai.app.message.Notice;
import com.ailicai.app.message.NoticeListResponse;
import com.ailicai.app.ui.base.BaseMvpActivity;
import com.ailicai.app.ui.message.adapter.BaseMessageListAdapter;
import com.ailicai.app.ui.message.presenter.BaseMessageActivityPresenter;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.bottomrefreshlistview.BottomRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

/**
 * Created by duo.chen on 2015/8/7.
 */
public class BaseMessageListActivity extends BaseMvpActivity<BaseMessageActivityPresenter>
        implements BaseMessageListAdapter.OndeleteListener, OnRefreshListener,BaseMessageActivityPresenter.BaseMessageView{

    public final static String MESSAGELISTTYPE = "messagelisttype";

    @Bind(R.id.message_top_title)
    IWTopTitleView topTitleView;
    @Bind(R.id.message_base_fragment_list)
    BottomRefreshListView listView;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private BaseMessageListAdapter baseMessageListAdapter;

    private int messageListtype = 0;
    private int mTotal;

    public static void goActivity(Activity context, PushMessage message){
        Intent intent = new Intent();
        intent.setClass(context, BaseMessageListActivity.class);
        intent.putExtra(PushMessage.PUSHMESSAGE,message);
        context.startActivity(intent);
    }
    public static void goActivity(Activity context, int type){
        Intent intent = new Intent();
        intent.setClass(context, BaseMessageListActivity.class);
        intent.putExtra(BaseMessageListActivity.MESSAGELISTTYPE,
                type);
        context.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.message_base_activity_layout;
    }

    @Override
    public BaseMessageActivityPresenter initPresenter() {
        return new BaseMessageActivityPresenter();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        EventBus.getDefault().register(this);
        doesContainFragment = true;
        if (null != getIntent()) {
            if (getIntent().hasExtra(PushMessage.PUSHMESSAGE)){
                PushMessage pushMessage = (PushMessage) getIntent().getSerializableExtra
                        (PushMessage.PUSHMESSAGE);
                messageListtype = pushMessage.getMsgType();

            } else {
                messageListtype = getIntent().getIntExtra(MESSAGELISTTYPE, messageListtype);
            }
        }
        mPresenter.setMsgType(messageListtype);


        switch (messageListtype) {
            case PushMessage.INFOTYPE:
                topTitleView.setTitleText("资讯");
                break;
            case PushMessage.REMINDTYPE:
                topTitleView.setTitleText("提醒");
                break;
            case PushMessage.ACTIVITYTYPE:
                topTitleView.setTitleText("活动");
                break;
        }

        baseMessageListAdapter = new BaseMessageListAdapter(this, messageListtype);
        baseMessageListAdapter.setOndeleteListener(this);
        listView.setAdapter(baseMessageListAdapter);
        listView.setOnScrollListener(onScrollListener);
        listView.setOnLoadMoreListener(onLoadMoreListener);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_red_color);

        loadData(true);
    }

    @Override
    public void onResume() {
        //TODO 统计
        PVIDHandler.uploadPVIDLogical(this.getClass().getSimpleName() + messageListtype);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void loadData(boolean reload) {

        int offset = 0;
        if (reload) {
            listView.resetAll();
            listView.smoothScrollToPosition(0);
        } else {
            int size = baseMessageListAdapter.getCount();
            if (size > 0 && mTotal > size) {
                offset = size;
            }
        }
        mPresenter.loadData(messageListtype,offset,reload);
    }

    @Override
    public void onDeleteItem(int position) {
        Notice notice = baseMessageListAdapter.getItem(position);
        if (notice != null) {
            mPresenter.deleteMsgById(messageListtype,position,notice.getId());
        }
    }

    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            ImageLoader loader = ImageLoader.getInstance();
            loader.pause();
            switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    loader.resume();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int
                totalItemCount) {
        }
    };

    private BottomRefreshListView.OnLoadMoreListener onLoadMoreListener = new
            BottomRefreshListView.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if (loadMore()) {
                        listView.setLoadingText("加载中");
                    } else {
                        listView.onAllLoaded();
                        listView.setPromptText("");
                    }
                }
            };

    private boolean loadMore() {
        int size = baseMessageListAdapter.getCount();
        if (size > 0 && mTotal > size) {
            loadData(false);//加载更多
            return true;
        }
        return false;
    }


   @Override
    public void reloadData() {
        if (null != listView) {
            loadData(true);
        }
    }

    public void setSwipeRefresh(boolean refresh) {
        if (null != mSwipeRefreshLayout) {
            mSwipeRefreshLayout.setRefreshing(refresh);
        }
    }

    @Override
    public void onRefresh() {
        reloadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleRefreshPushEvent(RefreshPushEvent event) {
        if (event.getMsgType() == messageListtype) {
            reloadData();
        }
    }

    @Override
    public void showLoading() {
        showLoadView();
    }

    @Override
    public void hideLoading() {
        setSwipeRefresh(false);
        listView.onLoadMoreComplete();
    }

    @Override
    public void processSuccessView(NoticeListResponse response,boolean reload) {
        showLoadTranstView();
        mTotal = response.getTotalNum();
        if (reload) {
            MsgLiteView.refreshNoticeNums(null);
        }
        baseMessageListAdapter.setListData(response.getNoticeList(),reload);

        showContentView();
    }

    @Override
    public void showNoData() {
        View nodataView = View.inflate(BaseMessageListActivity.this, R.layout
                .list_nodataview_layout, null);
        TextView textView = (TextView) nodataView.findViewById(R.id.nodata_text);
        switch (messageListtype) {
            case PushMessage.REMINDTYPE:
                textView.setText("暂无提醒");
                break;
            case PushMessage.INFOTYPE:
                textView.setText("暂无资讯");
                break;
            case PushMessage.ACTIVITYTYPE:
                textView.setText("暂无活动");
                break;
        }
        showNoDataView(nodataView);
    }

    @Override
    public void onFail(String msg) {
        showErrorView(msg);
    }

    @Override
    public void deleteMsgSuccess(int pos) {
        baseMessageListAdapter.deleteListData(pos);
    }
}