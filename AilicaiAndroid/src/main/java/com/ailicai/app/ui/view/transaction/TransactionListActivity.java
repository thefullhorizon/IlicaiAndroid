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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.model.bean.TransactionListModel;
import com.ailicai.app.model.bean.TransactionTypeModel;
import com.ailicai.app.model.bean.TransactionTypeModelByTime;
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
    private static final int QUICK_TYPE_LATEST_A_WEEK = 1; // 近一周查询
    private static final int QUICK_TYPE_LATEST_A_MONTH = 2; // 近一月查询
    private static final int QUICK_TYPE_LATEST_THREE_MONTH = 3; // 近三月查询
    private static final int QUICK_TYPE_LATEST_HALF_YEAR = 4; // 近半年
    private static final int QUICK_TYPE_LATEST_A_YEAR = 5; // 近一年

    //*/
    private static final int REQUEST_TYPE_QUERY = 1001;
    private static final int REQUEST_TYPE_REFRESH = 1002;
    private static final int REQUEST_TYPE_LOADMORE = 1003;
    //*/
    @Bind(R.id.layout_split)
    LinearLayout mLayoutSplit;

    TextView mTextStartDate;
    TextView mTextEndDate;

//    @Bind(R.id.check_filter_last_week)
//    CheckBox mCheckLastWeek;
//    @Bind(R.id.check_filter_last_month)
//    CheckBox mCheckLastMonth;
//    @Bind(R.id.check_filter_three_month)
//    CheckBox mCheckThreeMonth;

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.list_view)
    BottomRefreshListView mListView;
    @Bind(R.id.layout_no_data)
    LinearLayout mLayoutNoData;

    @Bind(R.id.ll_split_item_time)
    View mFiltItemTime;
    @Bind(R.id.ll_split_item_time_show)
    TextView mSplitItemTimeShow;
    @Bind(R.id.ll_split_item_time_arrow)
    TextViewTF mSplitItemTimeArrow;

    @Bind(R.id.ll_split_item_transaction_type)
    View mFiltItemTransactionCatagory;
    @Bind(R.id.transaction_catagory_show)
    TextView mTransactionCatagoryShow;
    @Bind(R.id.transaction_catagory_arrow)
    TextViewTF mTextFilterIconByCatagory;

    @Bind(R.id.filter_shade_view)
    View mFilterShadeView;

    private LayoutInflater mLayoutInflater = null;
    private Date mChoosedStartDate = null;
    private Date mChoosedEndDate = null;
    private SimpleDateFormat mSimpleDateFormat;

    private PopupWindow mPopupWindowByTime = null;
    private List<TransactionTypeModelByTime> mFilterDataByTimeList = new ArrayList<>();
    private FilterByTimeAdapter mFilterAdapterByTime = null;
    private TransactionEnumByTime mChoosedEnumByTime = TransactionEnumByTime.LATEST_A_WEEK;//默认设置选中近一周
//    private int mChoosedQuickType = QUICK_TYPE_NORMAL; // 默认为普通查询

    private PopupWindow mPopupWindow = null;
    private List<TransactionTypeModel>    mFilterDataList = new ArrayList<>();
    private FilterAdapter mFilterAdapter = null;
    private TransactionEnum mChoosedEnum = null;

    private List<TransactionListModel> mDataList = new ArrayList<>();
    private TransactionListAdapter mListAdapter = null;

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
        mLayoutInflater = LayoutInflater.from(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_red_color);
        initData();
        addListener();
    }

    private void initData() {

        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //Split by a certain period
        mChoosedEnumByTime = TransactionEnumByTime.LATEST_A_WEEK;// A week as default
        mFilterDataByTimeList.add(new TransactionTypeModelByTime(TransactionEnumByTime.LATEST_A_WEEK, true));
        mFilterDataByTimeList.add(new TransactionTypeModelByTime(TransactionEnumByTime.LATEST_A_MONTH, false));
        mFilterDataByTimeList.add(new TransactionTypeModelByTime(TransactionEnumByTime.LATEST_THREE_MONTH, false));
        mFilterDataByTimeList.add(new TransactionTypeModelByTime(TransactionEnumByTime.LATEST_HALF_YEAR, false));
        mFilterDataByTimeList.add(new TransactionTypeModelByTime(TransactionEnumByTime.LATEST_A_YEAR, false));
        mFilterAdapter = new FilterAdapter();

        //Split by transaction category
        mChoosedEnum = TransactionEnum.ALL;// All as default
        mFilterDataList.add(new TransactionTypeModel(TransactionEnum.ALL, true));
        mFilterDataList.add(new TransactionTypeModel(TransactionEnum.TRANSFER_IN, false));
        mFilterDataList.add(new TransactionTypeModel(TransactionEnum.TTRANSFER_OUT, false));
        mFilterDataList.add(new TransactionTypeModel(TransactionEnum.BUY, false));
        mFilterDataList.add(new TransactionTypeModel(TransactionEnum.BACK_FUND, false));
        mFilterDataList.add(new TransactionTypeModel(TransactionEnum.BACK_ALL, false));
        mFilterDataList.add(new TransactionTypeModel(TransactionEnum.TRANSFER, false));
        mFilterAdapterByTime = new FilterByTimeAdapter();

        mListAdapter = new TransactionListAdapter(TransactionListActivity.this);
        View view = new View(this);
        view.setBackgroundColor(Color.TRANSPARENT);
        view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 32));
        mListView.addHeaderView(view);
        mListView.setAdapter(mListAdapter);
        mListView.setEmptyView(mLayoutNoData);
        mListAdapter.setData(mDataList);

        loadData(REQUEST_TYPE_QUERY);
    }

    private void addListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(REQUEST_TYPE_REFRESH);
            }
        });
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
            case R.id.text_query:
                mChoosedEnumByTime = TransactionEnumByTime.LATEST_COMMON;
                for (int i = 0; i < mFilterDataByTimeList.size(); i++) {
                    mFilterDataByTimeList.get(i).setIsCurrent(false);
                }
                mFilterAdapterByTime.notifyDataSetChanged();
                if (mPopupWindowByTime != null && mPopupWindowByTime.isShowing()) {
                    mPopupWindowByTime.dismiss();
                }
                mSplitItemTimeShow.setText(mSimpleDateFormat.format(mChoosedStartDate) + " - " + mSimpleDateFormat.format(new Date()));
                loadData(REQUEST_TYPE_QUERY);

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

    @Override
    protected void setupInjectComponent() {

    }

    @OnClick(R.id.ll_split_item_time)
    void clickSplitItemTime() {
        if (CheckDoubleClick.isFastDoubleClick(1000)) {
            return;
        }
        showFilterDialogByTime();
    }

    @OnClick(R.id.ll_split_item_transaction_type)
    void clickSplitItemTransactionType() {
        if (CheckDoubleClick.isFastDoubleClick(1000)) {
            return;
        }
        showFilterDialogByTransactionCategory();
    }


    @Override
    public void onPause() {
        SystemUtil.HideSoftInput(this);
        super.onPause();
    }

    private void showFilterDialogByTime(){

        mPopupWindowByTime = new PopupWindow(mLayoutInflater.inflate(R.layout.transaction_list_filter_dialog_by_time, null),
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ListView listView = (ListView) mPopupWindowByTime.getContentView().findViewById(R.id.transaction_filter_type_list);
        mTextStartDate = (TextView) mPopupWindowByTime.getContentView().findViewById(R.id.text_filter_start_date);
        mTextEndDate = (TextView) mPopupWindowByTime.getContentView().findViewById(R.id.text_filter_end_date);
        TextView queryByAssign = (TextView) mPopupWindowByTime.getContentView().findViewById(R.id.text_query);
        mTextStartDate.setOnClickListener(this);
        mTextEndDate.setOnClickListener(this);
        queryByAssign.setOnClickListener(this);

        Calendar startCalendar = Calendar.getInstance();
        switch (mChoosedEnumByTime){

            case LATEST_COMMON:
                mTextStartDate.setText(mSimpleDateFormat.format(mChoosedStartDate));
                break;

            case LATEST_A_WEEK:
                startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.get(Calendar.DAY_OF_MONTH) - 6);
                mChoosedStartDate = startCalendar.getTime();
                mTextStartDate.setText(mSimpleDateFormat.format(mChoosedStartDate));
                break;

            case LATEST_A_MONTH:
                startCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH) - 1);
                mChoosedStartDate = startCalendar.getTime();
                mTextStartDate.setText(mSimpleDateFormat.format(mChoosedStartDate));
                break;

            case LATEST_THREE_MONTH:
                startCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH) - 3);
                mChoosedStartDate = startCalendar.getTime();
                mTextStartDate.setText(mSimpleDateFormat.format(mChoosedStartDate));
                break;

            case LATEST_HALF_YEAR:
                startCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH) - 6);
                mChoosedStartDate = startCalendar.getTime();
                mTextStartDate.setText(mSimpleDateFormat.format(mChoosedStartDate));
                break;

            case LATEST_A_YEAR:
                startCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH) - 12);
                mChoosedStartDate = startCalendar.getTime();
                mTextStartDate.setText(mSimpleDateFormat.format(mChoosedStartDate));
                break;

        }
        Date date = new Date();
        mChoosedEndDate = new Date();
        mTextEndDate.setText(mSimpleDateFormat.format(date));

        listView.setAdapter(mFilterAdapterByTime);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < mFilterDataByTimeList.size(); i++) {
                    if (i == position) {
                        mFilterDataByTimeList.get(i).setIsCurrent(true);
                        mChoosedEnumByTime = mFilterDataByTimeList.get(i).getTransactionEnumByTime();
                    } else {
                        mFilterDataByTimeList.get(i).setIsCurrent(false);
                    }
                }
                mFilterAdapterByTime.notifyDataSetChanged();
                if (mPopupWindowByTime != null && mPopupWindowByTime.isShowing()) {
                    mPopupWindowByTime.dismiss();
                }
                mSplitItemTimeShow.setText(mFilterDataByTimeList.get(position).getTransactionEnumByTime().getTitle());
                loadData(REQUEST_TYPE_QUERY);
            }
        });
        mPopupWindowByTime.setFocusable(true);
        mPopupWindowByTime.setOutsideTouchable(true);
        mPopupWindowByTime.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mFilterShadeView.setVisibility(View.GONE);
                mSplitItemTimeArrow.setText(getResources().getString(R.string.chevous_down));
                mSplitItemTimeArrow.setTextColor(Color.parseColor("#212121"));
                mSplitItemTimeShow.setTextColor(Color.parseColor("#212121"));

            }
        });
//      contentView.setFocusableInTouchMode(true);
        // 实例化一个ColorDrawable颜色为透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        mPopupWindowByTime.setBackgroundDrawable(dw);
        mPopupWindowByTime.showAsDropDown(mLayoutSplit);
        mPopupWindowByTime.update();
        mSplitItemTimeArrow.setText(getResources().getString(R.string.chevous_up));
        mSplitItemTimeArrow.setTextColor(Color.parseColor("#e84a01"));
        mSplitItemTimeShow.setTextColor(Color.parseColor("#e84a01"));
        mFilterShadeView.setVisibility(View.VISIBLE);

    }

    private void showFilterDialogByTransactionCategory() {
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
                mTransactionCatagoryShow.setText(mFilterDataList.get(position).getTransactionEnum().getTitle());
                loadData(REQUEST_TYPE_QUERY);
            }
        });
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mFilterShadeView.setVisibility(View.GONE);
                mTextFilterIconByCatagory.setText(getResources().getString(R.string.chevous_down));
                mTextFilterIconByCatagory.setTextColor(Color.parseColor("#212121"));
                mTransactionCatagoryShow.setTextColor(Color.parseColor("#212121"));
            }
        });
//      contentView.setFocusableInTouchMode(true);
        // 实例化一个ColorDrawable颜色为透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.showAsDropDown(mLayoutSplit);
        mPopupWindow.update();
        mTextFilterIconByCatagory.setText(getResources().getString(R.string.chevous_up));
        mTextFilterIconByCatagory.setTextColor(Color.parseColor("#e84a01"));
        mTransactionCatagoryShow.setTextColor(Color.parseColor("#e84a01"));
        mFilterShadeView.setVisibility(View.VISIBLE);
    }

    private void dismissPopUpWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    private class FilterByTimeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mFilterDataByTimeList.size();
        }

        @Override
        public TransactionTypeModelByTime getItem(int position) {
            return mFilterDataByTimeList.get(position);
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
            TransactionTypeModelByTime model = getItem(position);
            if (model.isCurrent() == true) {
                // 当前项选中设置红色
                viewHolder.textTypeName.setTextColor(getResources().getColor(R.color.main_red_color));
            } else {
                viewHolder.textTypeName.setTextColor(getResources().getColor(R.color.radiobutton_text_color_normal));
            }
            viewHolder.textTypeName.setText(model.getTransactionEnumByTime().getItem());
            viewHolder.textTypeSelected.setVisibility(model.isCurrent() ? View.VISIBLE : View.GONE);
            return view;
        }

        private class ViewHolder {
            public TextView textTypeName;
            public TextViewTF textTypeSelected;
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
        request.setQuickType(mChoosedEnumByTime.getCode());
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
                mChoosedEnumByTime = TransactionEnumByTime.LATEST_COMMON;
                //*/
            }
        }, year, month, day);
        datePickerDialog.setAccentColor(getResources().getColor(R.color.dialog_blue));
        datePickerDialog.setYearRange(2015, 2037);
        datePickerDialog.show(getFragmentManager(), "filterDatePicker");
    }

}