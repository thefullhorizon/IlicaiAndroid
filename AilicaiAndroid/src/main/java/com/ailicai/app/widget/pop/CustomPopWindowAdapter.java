package com.ailicai.app.widget.pop;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jer on 2015/6/4
 */
public class CustomPopWindowAdapter extends BaseAdapter {

    int currentListType = -1;
    Context mContext;
    boolean isLeftAlign;
    CustomPopWindowInterface mInterface;
    int selectPosition = -1;
    private ArrayList<CustomPopWindowBean> dataStrs = new ArrayList<>();

    public CustomPopWindowAdapter(Context mContext, CustomPopWindowInterface mInterface, boolean isLeftAlign) {
        this.mContext = mContext;
        this.mInterface = mInterface;
        this.isLeftAlign = isLeftAlign;
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
        TextView textView;
        if (null == convertView) {
            int dip46 = DeviceUtil.getPixelFromDip(mContext, 46);

            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, dip46);
            textView = new TextView(mContext);
            textView.setLayoutParams(layoutParams);
            if(isLeftAlign) {
                textView.setGravity(Gravity.CENTER_VERTICAL| Gravity.LEFT);
            } else {
                textView.setGravity(Gravity.CENTER);
            }
            textView.setTextAppearance(mContext, R.style.text_14_333333);
        } else {
            textView = (TextView) convertView;
        }
        textView.setText(dataStr.getTitleName());
        boolean isSelect = isSelect(dataStr);
        if (isSelect) {
            selectPosition = Integer.valueOf(position);
        }
        textView.setSelected(isSelect);
        textView.setTextAppearance(mContext, isSelect ? R.style.text_14_e84a01 : R.style.text_14_333333);
        return textView;
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
