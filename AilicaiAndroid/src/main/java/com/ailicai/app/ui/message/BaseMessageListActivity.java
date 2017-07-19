package com.ailicai.app.ui.message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.message.adapter.BaseMessageListAdapter;
import com.ailicai.app.ui.message.presenter.BaseMessageListActivityPresenter;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.bottomrefreshlistview.BottomRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by duo.chen on 2015/8/7.
 */
public class BaseMessageListActivity extends BaseBindActivity implements BaseMessageListAdapter.OndeleteListener, OnRefreshListener {

    public final static String MESSAGELISTTYPE = "messagelisttype";

    public final static int INITMESSAGE = 1000;
    public final static int MESSAGE_DELSUCCESS = INITMESSAGE + 1;
    public final static int MESSAGE_LOADDATA = INITMESSAGE + 2;
    public final static int REQUEST_ERRORORFAILED = INITMESSAGE + 3;

    @Bind(R.id.message_top_title)
    IWTopTitleView topTitleView;
    @Bind(R.id.message_base_fragment_list)
    BottomRefreshListView listView;

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private BaseMessageListAdapter baseMessageListAdapter;
    private BaseMessageListActivityPresenter baseMessageListActivityPresenter;

    private List<Notice> data;

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
        baseMessageListActivityPresenter = new BaseMessageListActivityPresenter(this, handler,
                messageListtype);
        data = new ArrayList<>();
        baseMessageListAdapter.setListData(data);
        baseMessageListAdapter.setOndeleteListener(this);
        listView.setAdapter(baseMessageListAdapter);
        listView.setOnScrollListener(onScrollListener);
        listView.setOnLoadMoreListener(onLoadMoreListener);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_red_color);
        loadData(true);
        if (data.size() == 0) {
            showLoadView();
        }
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

        int mOffset = 0;
        if (reload) {
            listView.resetAll();
            mOffset = 0;
            listView.smoothScrollToPosition(0);
        } else {
            int size = baseMessageListAdapter.getCount();
            if (size > 0 && mTotal > size) {
                mOffset = size;
            }
        }
        baseMessageListActivityPresenter.loadData(mOffset, reload);
    }

    @Override
    public void onDeleteItem(int position) {
        delMessage(position);
        data.remove(position);
        baseMessageListAdapter.setListData(data);
        baseMessageListAdapter.notifyDataSetChanged();
    }

    public void delMessage(int position) {
        if (null != data && null != data.get(position)) {
            baseMessageListActivityPresenter.delMessage(data.get(position).getId());
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
        int size = data.size();
        if (size > 0 && mTotal > size) {
            loadData(false);//加载更多
            return true;
        }
        return false;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_DELSUCCESS:
                    //del message success
                    break;
                case MESSAGE_LOADDATA:
                    //load message data
                    setSwipeRefresh(false);
                    NoticeListResponse noticeListResponse = (NoticeListResponse) msg.obj;
                    mTotal = noticeListResponse.getTotalNum();
                    if (mTotal == 0) {
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
                    } else {
                        if (msg.arg1 == 1) {
                            data.clear();
                            MsgLiteView.refreshNoticeNums(null);
                        }
                        data.addAll(noticeListResponse.getNoticeList());
                        baseMessageListAdapter.notifyDataSetChanged();
                        listView.onLoadMoreComplete();
                        showContentView();
                    }
                    break;
                case REQUEST_ERRORORFAILED:
                    //exception or error
                    String error = (String) msg.obj;
                    showErrorView(error);
                    break;
            }
        }
    };

    @Override
    public void reloadData() {
        if (null != listView) {
            if (data.size() == 0) {
                showLoadView();
            } else {
                showLoadTranstView();
            }
            data.clear();
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
        if (null != listView) {
            data.clear();
            loadData(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleRefreshPushEvent(RefreshPushEvent event) {
        if (event.getMsgType() == messageListtype) {
            reloadData();
        }
    }
}