package com.ailicai.app.ui.reserve;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.ailicai.app.R;
import com.ailicai.app.model.bean.ReserveListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owen on 2016/3/10
 */
public class ReserveListAdapter extends BaseAdapter {

    private ReserveListActivity activity;
    private List<ReserveListBean> listData = new ArrayList<>();

    public ReserveListAdapter(ReserveListActivity activity, List<ReserveListBean> listData) {
        this.activity = activity;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public ReserveListBean getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHodle hodle;
        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.item_reserve_list, null);
            hodle = new ViewHodle(view);
            view.setTag(hodle);
        } else {
            hodle = (ViewHodle) view.getTag();
        }

        final ReserveListBean bean = getItem(position);
        hodle.tvName.setText(bean.getCustomerName());
        hodle.tvPrice.setText(bean.getBidAmountStr());
        hodle.tvDays.setText(bean.getHorizonStr());
        hodle.tvNhl.setText(bean.getYearInterestRateStr());

        return view;
    }

    ViewHodle hodle;


    class ViewHodle {

        TextView tvName;
        TextView tvPrice;
        TextView tvDays;
        TextView tvNhl;

        ViewHodle(View v) {
            tvName = (TextView) v.findViewById(R.id.tvName);
            tvPrice = (TextView) v.findViewById(R.id.tvPrice);
            tvDays = (TextView) v.findViewById(R.id.tvDays);
            tvNhl = (TextView) v.findViewById(R.id.tvNhl);
        }
    }
}
