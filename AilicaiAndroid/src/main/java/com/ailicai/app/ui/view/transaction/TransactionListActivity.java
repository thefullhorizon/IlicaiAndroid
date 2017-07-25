package com.ailicai.app.ui.view.transaction;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.model.request.TransactionListRequest;
import com.ailicai.app.model.response.TransactionListResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.widget.BottomRefreshListView;
import com.ailicai.app.widget.TextViewTF;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by wulianghuan on 2015/12/30.
 */
public class TransactionListActivity extends BaseBindActivity implements View.OnClickListener {
    private static final int QUICK_TYPE_NORMAL = 0; // 普通查询
    private static final int QUICK_TYPE_LAST_WEEK = 1; // 近一周查询
    private static final int QUICK_TYPE_LAST_MONTH = 2; // 近一月查询
    private static final int QUICK_TYPE_THREE_MONTH = 3; // 近三月查询

    //*/
    private static final int REQUEST_TYPE_QUERY = 1001;
    private static final int REQUEST_TYPE_REFRESH = 1002;
    private static final int REQUEST_TYPE_LOADMORE = 1003;
    //*/
    @Bind(R.id.layout_title)
    FrameLayout mLayoutTitle;
    @Bind(R.id.transaction_filter_layout)
    LinearLayout mFilterLayout;
    @Bind(R.id.transaction_type_title)
    TextView mTextTypeTitle;
    @Bind(R.id.text_filter_icon)
    TextViewTF mTextFilterIcon;
    @Bind(R.id.text_filter_start_date)
    TextView mTextStartDate;
    @Bind(R.id.text_filter_end_date)
    TextView mTextEndDate;
    @Bind(R.id.check_filter_last_week)
    CheckBox mCheckLastWeek;
    @Bind(R.id.check_filter_last_month)
    CheckBox mCheckLastMonth;
    @Bind(R.id.check_filter_three_month)
    CheckBox mCheckThreeMonth;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.list_view)
    BottomRefreshListView mListView;
    @Bind(R.id.layout_no_data)
    LinearLayout mLayoutNoData;
    @Bind(R.id.filter_shade_view)
    View mFilterShadeView;

    @Bind(R.id.layout_top_info)
    View layoutTopInfo;

    private LayoutInflater mLayoutInflater = null;
    private PopupWindow mPopupWindow = null;
    private List<TransactionTypeModel> mFilterDataList = new ArrayList<>();
    private FilterAdapter mFilterAdapter = null;
    private List<TransactionListModel> mDataList = new ArrayList<TransactionListModel>();
    private TransactionListAdapter mListAdapter = null;
    private TransactionEnum mChoosedEnum = null;
    private int mChoosedQuickType = QUICK_TYPE_NORMAL; // 默认为普通查询
    private Date mChoosedStartDate = null;
    private Date mChoosedEndDate = null;
    private SimpleDateFormat mSimpleDateFormat;

    /**
     * 已获取多少条
     */
    private int mOffset = 0;
    /**
     * 一页下发多少条
     */
    private int mPageSize = 20;
    /**
     * 一共有多少条数据
     */
    private int mTotalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutInflater = LayoutInflater.from(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_transaction_list;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_red_color);
        initData();
        addListener();
    }

    private void initData() {
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 默认为全部交易
        mChoosedEnum = TransactionEnum.ALL;
        mFilterDataList.add(new TransactionTypeModel(TransactionEnum.ALL, true));
        mFilterDataList.add(new TransactionTypeModel(TransactionEnum.TRANSFER_IN, false));
        mFilterDataList.add(new TransactionTypeModel(TransactionEnum.TTRANSFER_OUT, false));
        mFilterDataList.add(new TransactionTypeModel(TransactionEnum.BUY, false));
        mFilterDataList.add(new TransactionTypeModel(TransactionEnum.BACK_FUND, false));
        mFilterDataList.add(new TransactionTypeModel(TransactionEnum.BACK_ALL, false));
//        mFilterDataList.add(new TransactionTypeModel(TransactionEnum.PAY, false));
        mFilterDataList.add(new TransactionTypeModel(TransactionEnum.TRANSFER, false));
        mFilterAdapter = new FilterAdapter();

        mListAdapter = new TransactionListAdapter(TransactionListActivity.this);
        View view = new View(this);
        view.setBackgroundColor(Color.TRANSPARENT);
        view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 32));
        mListView.addHeaderView(view);
        mListView.setAdapter(mListAdapter);
        mListView.setEmptyView(mLayoutNoData);
        mListAdapter.setData(mDataList);
        // 默认设置选中近一周
        onclickLastWeek();
    }

    private void addListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(REQUEST_TYPE_REFRESH);
            }
        });
        mTextStartDate.setOnClickListener(this);
        mTextEndDate.setOnClickListener(this);
        mCheckLastWeek.setOnClickListener(this);
        mCheckLastMonth.setOnClickListener(this);
        mCheckThreeMonth.setOnClickListener(this);
        mListView.setOnLoadMoreListener(onLoadMoreListener);
    }

    private BottomRefreshListView.OnLoadMoreListener onLoadMoreListener = new BottomRefreshListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            if (loadMore()) {
                mListView.setLoadingText("加载中");
            } else {
                mListView.onAllLoaded();
                mListView.setPromptText("");
            }
        }
    };

    private boolean loadMore() {
        int size = mDataList.size();
        if (size > 0 && mTotalCount > size) {
            //加载更多
            loadData(REQUEST_TYPE_LOADMORE);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_filter_start_date:
                showDatePikcer(true, getStartDate());
                break;
            case R.id.text_filter_end_date:
                showDatePikcer(false, getEndDate());
                break;
            case R.id.check_filter_last_week:
                onclickLastWeek();
                break;
            case R.id.check_filter_last_month:
                onclickLastMonth();
                break;
            case R.id.check_filter_three_month:
                onclickThreeMonth();
                break;
        }
    }

    private Date getStartDate() {
        if (mChoosedStartDate == null) {
            // 默认起始时间：最近7天
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.get(Calendar.DAY_OF_MONTH) - 6);
            mChoosedStartDate = startCalendar.getTime();
        }
        return mChoosedStartDate;
    }


    private Date getEndDate() {
        if (mChoosedEndDate == null) {
            // 默认起始时间：当前日期
            mChoosedEndDate = new Date();
        }
        return mChoosedEndDate;
    }

    private void onclickLastWeek() {
        // 设置选择状态
        mChoosedQuickType = QUICK_TYPE_LAST_WEEK;
        mCheckLastWeek.setChecked(true);
        mCheckLastMonth.setChecked(false);
        mCheckThreeMonth.setChecked(false);
        // 设置日期
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.get(Calendar.DAY_OF_MONTH) - 6);
        mChoosedStartDate = startCalendar.getTime();
        mTextStartDate.setText(mSimpleDateFormat.format(mChoosedStartDate));

        Date date = new Date();
        mChoosedEndDate = date;
        mTextEndDate.setText(mSimpleDateFormat.format(date));
        loadData(REQUEST_TYPE_QUERY);
    }

    private void onclickLastMonth() {
        // 设置选择状态
        mChoosedQuickType = QUICK_TYPE_LAST_MONTH;
        mCheckLastWeek.setChecked(false);
        mCheckLastMonth.setChecked(true);
        mCheckThreeMonth.setChecked(false);
        // 设置日期
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH) - 1);
        mChoosedStartDate = startCalendar.getTime();
        mTextStartDate.setText(mSimpleDateFormat.format(mChoosedStartDate));

        Date date = new Date();
        mChoosedEndDate = date;
        mTextEndDate.setText(mSimpleDateFormat.format(date));
        loadData(REQUEST_TYPE_QUERY);
    }

    private void onclickThreeMonth() {
        // 设置选择状态
        mChoosedQuickType = QUICK_TYPE_THREE_MONTH;
        mCheckLastWeek.setChecked(false);
        mCheckLastMonth.setChecked(false);
        mCheckThreeMonth.setChecked(true);
        // 设置日期
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH) - 3);
        mChoosedStartDate = startCalendar.getTime();
        mTextStartDate.setText(mSimpleDateFormat.format(mChoosedStartDate));

        Date date = new Date();
        mChoosedEndDate = date;
        mTextEndDate.setText(mSimpleDateFormat.format(date));
        loadData(REQUEST_TYPE_QUERY);
    }

    @Override
    protected void setupInjectComponent() {

    }

    @OnClick(R.id.text_query)
    void doQuery() {
        if (CheckDoubleClick.isFastDoubleClick(1000)) {
            return;
        }
        loadData(REQUEST_TYPE_QUERY);
    }

    @OnClick(R.id.transaction_filter_layout)
    void showFilter() {
        if (CheckDoubleClick.isFastDoubleClick(1000)) {
            return;
        }
        showFiterTypeDialog();
    }

    @Override
    public void onPause() {
        SystemUtil.HideSoftInput(this);
        super.onPause();
    }

    private void showFiterTypeDialog() {
        mPopupWindow = new PopupWindow(mLayoutInflater.inflate(R.layout.transaction_list_filter_dialog, null),
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ListView listView = (ListView) mPopupWindow.getContentView().findViewById(R.id.transaction_filter_type_list);
        listView.setAdapter(mFilterAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 点击切换item
                for (int i = 0; i < mFilterDataList.size(); i++) {
                    if (i == position) {
                        mFilterDataList.get(i).setIsCurrent(true);
                        // 设置选中的枚举类型
                        mChoosedEnum = mFilterDataList.get(i).getTransactionEnum();
                    } else {
                        mFilterDataList.get(i).setIsCurrent(false);
                    }
                    mFilterAdapter.notifyDataSetChanged();
                    dismissPopUpWindow();
                }
                mTextTypeTitle.setText(mFilterDataList.get(position).getTransactionEnum().getTitle());
                loadData(REQUEST_TYPE_QUERY);
            }
        });
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mFilterShadeView.setVisibility(View.GONE);
                mTextFilterIcon.setText(getResources().getString(R.string.chevous_down));
            }
        });
//      contentView.setFocusableInTouchMode(true);
        // 实例化一个ColorDrawable颜色为透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.showAsDropDown(mLayoutTitle);
        mPopupWindow.update();
        mTextFilterIcon.setText(getResources().getString(R.string.chevous_up));
        mFilterShadeView.setVisibility(View.VISIBLE);
    }

    private void dismissPopUpWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    private class FilterAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mFilterDataList.size();
        }

        @Override
        public TransactionTypeModel getItem(int position) {
            return mFilterDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view;
            ViewHolder viewHolder;
            if (null != convertView) {
                view = convertView;
                viewHolder = (ViewHolder) convertView.getTag();
            } else {
                view = mLayoutInflater.inflate(R.layout.item_transaction_filter_type, null);
                viewHolder = new ViewHolder();
                viewHolder.textTypeName = (TextView) view.findViewById(R.id.text_type_name);
                viewHolder.textTypeSelected = (TextViewTF) view.findViewById(R.id.text_type_selected);
                view.setTag(viewHolder);
            }
            TransactionTypeModel model = getItem(position);
            if (model.isCurrent() == true) {
                // 当前项选中设置红色
                viewHolder.textTypeName.setTextColor(getResources().getColor(R.color.main_red_color));
            } else {
                viewHolder.textTypeName.setTextColor(getResources().getColor(R.color.radiobutton_text_color_normal));
            }
            viewHolder.textTypeName.setText(model.getTransactionEnum().getItem());
            viewHolder.textTypeSelected.setVisibility(model.isCurrent() ? View.VISIBLE : View.GONE);
            return view;
        }

        private class ViewHolder {
            public TextView textTypeName;
            public TextViewTF textTypeSelected;
        }
    }

    /**
     * 请求数据
     */
    void loadData(final int requestType) {
        if (REQUEST_TYPE_REFRESH == requestType || REQUEST_TYPE_QUERY == requestType) {
            mListView.resetAll();
            mListView.smoothScrollToPosition(0);
        }
        ServiceSender.exec(this, getRequestParams(requestType), new IwjwRespListener<TransactionListResponse>() {
            @Override
            public void onStart() {
                super.onStart();
                if (REQUEST_TYPE_QUERY == requestType) {
                    // 点击查询才需要显示
                    showLoadTranstView();
                }
            }

            @Override
            public void onJsonSuccess(TransactionListResponse response) {
                mSwipeRefreshLayout.setRefreshing(false);
                showContentView();
                if (response != null && response.getTotal() > 0) {
                    mTotalCount = response.getTotal();
                    showData(response.getRows(), requestType);
                } else {
                    mDataList.clear();
                    mListAdapter.notifyDataSetChanged();
                    showNoData(true);

                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                mSwipeRefreshLayout.setRefreshing(false);
                showErrorView(errorInfo);
            }
        });
    }

    /**
     * 重新加载界面数据
     */
    public void reloadData() {
        loadData(REQUEST_TYPE_QUERY);
    }

    private TransactionListRequest getRequestParams(int requestType) {
        if (REQUEST_TYPE_LOADMORE == requestType) {
            if (mDataList.size() > 0 && mTotalCount > mDataList.size()) {
                mOffset = mDataList.size();
            }
        } else {
            mOffset = 0;
        }
        TransactionListRequest request = new TransactionListRequest();
        request.setTradeType(mChoosedEnum.getCode());
        request.setQuickType(mChoosedQuickType);
        request.setStartDate(mSimpleDateFormat.format(getStartDate()));
        request.setEndDate(mSimpleDateFormat.format(getEndDate()));
        request.setPageSize(mPageSize);
        request.setOffSet(mOffset);
        return request;
    }

    private void showData(List<TransactionListModel> dataList, int requestType) {
        showNoData(false);
        if (REQUEST_TYPE_LOADMORE != requestType) {
            // 下拉刷新或点击查询
            mDataList.clear();
        }
        mDataList.addAll(dataList);
        mListAdapter.setData(mDataList);
        mListView.onLoadMoreComplete();
    }

    void showNoData(boolean showEmpty) {
    /*    if (showEmpty == true) {
            mLayoutNoData.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            mLayoutNoData.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }*/
    }

    private void showDatePikcer(final boolean isForStart, Date showdate) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(showdate);
        //设置日历的时间，把一个新建Date实例myDate传入
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获得日历中的 year month day
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance(Locale.CHINA);
                calendar.set(year, monthOfYear, dayOfMonth);
                if (isForStart) {
                    // 选择起始时间
                    mChoosedStartDate = calendar.getTime();
                    mTextStartDate.setText(mSimpleDateFormat.format(mChoosedStartDate));
                } else {
                    // 选择结束时间
                    mChoosedEndDate = calendar.getTime();
                    mTextEndDate.setText(mSimpleDateFormat.format(mChoosedEndDate));
                }
                //*/ 改变时间段选择框的状态
                mChoosedQuickType = QUICK_TYPE_NORMAL;
                mCheckLastWeek.setChecked(false);
                mCheckLastMonth.setChecked(false);
                mCheckThreeMonth.setChecked(false);
                //*/
            }
        }, year, month, day);
        datePickerDialog.setAccentColor(getResources().getColor(R.color.dialog_blue));
        datePickerDialog.setYearRange(2015, 2037);
        datePickerDialog.show(getFragmentManager(), "filterDatePicker");
    }
}