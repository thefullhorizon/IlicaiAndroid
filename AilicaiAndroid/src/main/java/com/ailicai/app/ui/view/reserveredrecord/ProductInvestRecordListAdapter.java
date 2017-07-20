package com.ailicai.app.ui.view.reserveredrecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.model.bean.ProductInvestRecord;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 投资记录ListAdapter
 * Created by liyanan on 16/4/8.
 */
public class ProductInvestRecordListAdapter extends BaseAdapter {
    private Context context;
    private List<ProductInvestRecord> records;

    public ProductInvestRecordListAdapter(Context context, List<ProductInvestRecord> records) {
        this.context = context;
        this.records = records;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_deposit_history, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ProductInvestRecord record = records.get(position);
        holder.tvPhone.setText(record.getMoblie());
        holder.tvMoneyCount.setText(String.valueOf(record.getAmtStr()));
        holder.tvTime.setText(record.getTimeStr());
        if (position == getCount() - 1) {
            holder.line.setVisibility(View.INVISIBLE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.tv_phone)
        TextView tvPhone;
        @Bind(R.id.tv_money_count)
        TextView tvMoneyCount;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.line)
        View line;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
