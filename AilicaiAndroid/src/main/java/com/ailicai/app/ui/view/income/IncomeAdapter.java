package com.ailicai.app.ui.view.income;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.model.bean.IncomeDetail;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by David on 16/1/4.
 */
public class IncomeAdapter extends BaseAdapter {

    Context mContext;

    List<IncomeDetail> data = new ArrayList<>();

    public IncomeAdapter(Context context) {
        mContext = context;
    }


    public void addData(List<IncomeDetail> data, boolean clear) {
        if (clear) this.data.clear();
        if (data != null) this.data.addAll(data);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.income_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        IncomeDetail detail = (IncomeDetail) getItem(position);

        viewHolder.date.setText(detail.getTime());
        viewHolder.money.setText("+" + CommonUtil.formatMoneyForFinance(detail.getIncome()));
        viewHolder.category.setText(detail.getIncomeTypeMemo());

        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.income_date)
        TextView date;
        @Bind(R.id.income_money)
        TextView money;
        @Bind(R.id.income_category)
        TextView category;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
