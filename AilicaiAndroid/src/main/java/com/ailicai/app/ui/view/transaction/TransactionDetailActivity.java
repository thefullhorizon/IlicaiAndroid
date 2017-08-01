package com.ailicai.app.ui.view.transaction;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.model.bean.TransactionListModel;
import com.ailicai.app.model.request.TransactionDetailRequest;
import com.ailicai.app.model.response.TransactionDetailResponse;
import com.ailicai.app.ui.base.BaseBindActivity;

import butterknife.Bind;

/**
 * Created by wulianghuan on 2015/12/28.
 */
public class TransactionDetailActivity extends BaseBindActivity {
    public static final String PARAM_TRANSACTION_NO = "PARAM_TRANSACTION_NO";
    public static final String PARAM_TRANSACTION_TYPE = "PARAM_TRANSACTION_TYPE";
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.text_status_label)
    TextView mTextStatusLabel;
    @Bind(R.id.text_transaction_amount)
    TextView mTextTransactionAmount;
    @Bind(R.id.text_transaction_item)
    TextView mTextTransactionItem;
    @Bind(R.id.view_split_reason)
    View mViewSpilitReason;
    @Bind(R.id.layout_item_reason)
    LinearLayout mLayoutItemReason;
    @Bind(R.id.text_reason_desc)
    TextView mTextReasonDesc;
    @Bind(R.id.text_transaction_account_tip)
    TextView mTextTransactionAccountTip;
    @Bind(R.id.text_transaction_account)
    TextView mTextTransactionAccount;
    @Bind(R.id.text_transaction_time)
    TextView mTextTransactionTime;
    @Bind(R.id.text_transaction_no)
    TextView mTextTransactionNo;
    private String mTransactionNo = null;
    private int mTransactionType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_red_color);
        initData();
        addListener();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    private void initData() {
        mTransactionNo = getIntent().getStringExtra(PARAM_TRANSACTION_NO);
        mTransactionType = getIntent().getIntExtra(PARAM_TRANSACTION_TYPE, -1);
        if (TextUtils.isEmpty(mTransactionNo)) {
            return;
        }
        if (mTransactionType == -1) {
            return;
        }
        loadData(true);
    }

    private void loadData(final boolean isRefresh) {
        ServiceSender.exec(this, getRequestParams(), new IwjwRespListener<TransactionDetailResponse>() {
            @Override
            public void onStart() {
                super.onStart();
                if (isRefresh) {
                    showLoadView();
                }
            }

            @Override
            public void onJsonSuccess(TransactionDetailResponse response) {
                mSwipeRefreshLayout.setRefreshing(false);
                showContentView();
                if (response != null) {
                    setUpData(response.getTradeInfo());
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                mSwipeRefreshLayout.setRefreshing(false);
                showErrorView(errorInfo);
            }
        });
    }

    private TransactionDetailRequest getRequestParams() {
        TransactionDetailRequest request = new TransactionDetailRequest();
        request.setTradeNo(mTransactionNo);
        request.setTradeType(mTransactionType);
        return request;
    }

    private void setUpData(TransactionListModel transactionListModel) {
        if (transactionListModel.getStatus() == 2) {
            // 失败才需要设置红色
            mTextStatusLabel.setTextColor(Color.parseColor("#ff0021"));
        } else {
            mTextStatusLabel.setTextColor(Color.parseColor("#212121"));
        }
        mTextStatusLabel.setText(transactionListModel.getTradeTypeStr() + transactionListModel.getStatusStr());
        mTextTransactionAmount.setText(transactionListModel.getTradeAmtStr() + "元");
        mTextTransactionItem.setText(transactionListModel.getTradeContent());

        if (transactionListModel.getStatus() == 1 || transactionListModel.getStatus() == 3) {
            // 交易成功，不用显示原因
            mViewSpilitReason.setVisibility(View.GONE);
            mLayoutItemReason.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(transactionListModel.getErrorMsg())) {
            mViewSpilitReason.setVisibility(View.VISIBLE);
            mLayoutItemReason.setVisibility(View.VISIBLE);
            mTextReasonDesc.setText(transactionListModel.getErrorMsg());
        }
        switch (transactionListModel.getTradeType()) {
            case 2:            // 钱包转出
                mTextTransactionAccountTip.setText("收款账户");
                if (transactionListModel.userRole == 1) {
                    // 借款人
                    mTextTransactionAccountTip.setText("放款产品");
                }
                break;
            case 4:            // 回款
                mTextTransactionAccountTip.setText("回款产品");
                break;
            case 6:            // 转让
                mTextTransactionAccountTip.setText("转让产品");
                break;
            case 7://退款
                mTextTransactionAccountTip.setText("退款产品");
                break;
        }

        mTextTransactionAccount.setText(transactionListModel.getPayAccount());
        mTextTransactionTime.setText(transactionListModel.getTradeTimeYmdHms());
        mTextTransactionNo.setText(transactionListModel.getTradeNo());
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_transaction_detail;
    }

    @Override
    protected void setupInjectComponent() {

    }

    @Override
    public void onPause() {
        SystemUtil.HideSoftInput(this);
        super.onPause();
    }

    @Override
    public void reloadData() {
        loadData(true);
    }

    private void addListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }
        });
    }

}