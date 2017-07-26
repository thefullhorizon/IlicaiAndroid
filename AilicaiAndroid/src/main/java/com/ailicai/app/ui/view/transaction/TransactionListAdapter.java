package com.ailicai.app.ui.view.transaction;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.widget.TextViewTF;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wulianghuan on 2016/1/4.
 */
public class TransactionListAdapter extends BaseAdapter {
    Context context;
    List<TransactionListModel> dataList = new ArrayList<>();

    public TransactionListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<TransactionListModel> data) {
        this.dataList = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public TransactionListModel getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final TransactionListModel model = dataList.get(position);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.transaction_list_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTextTransactionName.setText(model.getTradeContent());
        viewHolder.mTextTransactionAmount.setText(String.valueOf(model.getTradeAmtStr()));
        viewHolder.mTextTransactionTime.setText(model.getTradeTimeYmdHm());
        viewHolder.mTextReasonDesc.setText(model.getErrorMsg());
        viewHolder.mTextTransactionResult.setText(model.getStatusStr());
        if (1 == model.getStatus()) {
            // 成功
            viewHolder.mLayoutTransactionResult.setVisibility(View.GONE);
        } else if (2 == model.getStatus()) {
            // 失败
            viewHolder.mLayoutTransactionResult.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(model.getErrorMsg())) {
                // 禁止点击
                viewHolder.mLayoutTransactionResult.setEnabled(false);
                viewHolder.mTextResultIcon.setVisibility(View.GONE);
            } else {
                viewHolder.mTextResultIcon.setVisibility(View.VISIBLE);
                viewHolder.mLayoutTransactionResult.setEnabled(true);
            }
        } else if (3 == model.getStatus()) {
            // 进行中
            viewHolder.mLayoutTransactionResult.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(model.getErrorMsg())) {
                // 禁止点击
                viewHolder.mLayoutTransactionResult.setEnabled(false);
                viewHolder.mTextResultIcon.setVisibility(View.GONE);
            } else {
                viewHolder.mTextResultIcon.setVisibility(View.VISIBLE);
                viewHolder.mLayoutTransactionResult.setEnabled(true);
            }
        } else if (4 == model.getStatus()) {
            // 已退款
            viewHolder.mLayoutTransactionResult.setVisibility(View.GONE);
        }

        if (model.isExpand() == true) {
            viewHolder.mTextResultIcon.setText(R.string.area);
            viewHolder.mLayoutResonDesc.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mTextResultIcon.setText(R.string.homepage_arrow_left);
            viewHolder.mLayoutResonDesc.setVisibility(View.GONE);
        }

        viewHolder.mLayoutTransactionResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.isExpand() == false) {
                    viewHolder.mTextResultIcon.setText(R.string.chevous_up);
                    viewHolder.mLayoutResonDesc.setVisibility(View.VISIBLE);
                    model.setIsExpand(true);
                } else {
                    viewHolder.mTextResultIcon.setText(R.string.homepage_arrow_left);
                    viewHolder.mLayoutResonDesc.setVisibility(View.GONE);
                    model.setIsExpand(false);
                }
            }
        });

        viewHolder.mLayoutRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TransactionDetailActivity.class);
                intent.putExtra(TransactionDetailActivity.PARAM_TRANSACTION_NO, model.getTradeNo());
                intent.putExtra(TransactionDetailActivity.PARAM_TRANSACTION_TYPE, model.getTradeType());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.layout_root)
        LinearLayout mLayoutRoot;
        @Bind(R.id.text_transaction_name)
        TextView mTextTransactionName;
        @Bind(R.id.text_transaction_amount)
        TextView mTextTransactionAmount;
        @Bind(R.id.text_transaction_time)
        TextView mTextTransactionTime;
        @Bind(R.id.layout_transaction_result)
        LinearLayout mLayoutTransactionResult;
        @Bind(R.id.text_transaction_result)
        TextView mTextTransactionResult;
        @Bind(R.id.text_result_icon)
        TextViewTF mTextResultIcon;
        @Bind(R.id.layout_reason_desc)
        LinearLayout mLayoutResonDesc;
        @Bind(R.id.text_reason_desc)
        TextView mTextReasonDesc;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}