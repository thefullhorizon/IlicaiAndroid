package com.ailicai.app.widget.pop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ailicai.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owen on 2016/5/26
 */
public class CustomPopWindowStyleAdapter extends BaseAdapter {

    int currentListType = -1;
    Context mContext;
    CustomPopWindowInterface mInterface;
    int selectPosition = -1;
    private ArrayList<CustomPopWindowBean> dataStrs = new ArrayList<>();

    public CustomPopWindowStyleAdapter(Context mContext, CustomPopWindowInterface mInterface) {
        this.mContext = mContext;
        this.mInterface = mInterface;
    }

    public int getCurrentListType() {
        return currentListType;
    }

    public void setCurrentListType(int currentListType) {
        this.currentListType = currentListType;
    }

    public void addAllData(List<CustomPopWindowBean> datas) {
        if (datas == null) return;
        dataStrs.clear();
        dataStrs.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataStrs.size();
    }

    @Override
    public CustomPopWindowBean getItem(int position) {
        return dataStrs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomPopWindowBean dataStr = getItem(position);

        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pop_check, null);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvCheck = (TextView) convertView.findViewById(R.id.tvCheck);

        tvName.setText(dataStr.getTitleName());
        boolean isSelect = isSelect(dataStr);
        if (isSelect) {
            selectPosition = position;
        }
        tvCheck.setVisibility(isSelect ? View.VISIBLE : View.GONE);
        return convertView;
    }

    boolean isSelect(CustomPopWindowBean bean) {
        if (mInterface != null) {
            return mInterface.checkIsSelect(bean);
        } else {
            return false;
        }
    }

    public int getSelectPosition() {
        return selectPosition;
    }


}
