package com.ailicai.app.ui.mine;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.model.bean.IntegralModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 积分明细记录
 */
public class IntegralDetailListAdapter extends BaseAdapter {

    List<IntegralModel> integralRecordList;
    private Context mContext;

    public IntegralDetailListAdapter(Context context, List<IntegralModel> rewardRecordList) {
        this.mContext = context;
        this.integralRecordList = rewardRecordList;
    }

    @Override
    public int getCount() {
        return (integralRecordList.size() == 0) ? 1 : integralRecordList.size();
    }

    @Override
    public Object getItem(int position) {
        return integralRecordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (integralRecordList.size() == 0) {
            View emptyView = LayoutInflater.from(mContext).inflate(R.layout.integral_record_adapter_empty, null);
            TextView noData = (TextView) emptyView.findViewById(R.id.no_record);
            noData.setText("暂无积分记录");
            return emptyView;
        } else {
            ItemView itemView;
            //if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.integral_record_adapter, null);
            itemView = new ItemView(convertView);
            convertView.setTag(itemView);
            //} else {
            //    itemView = (ItemView) convertView.getTag();
            //}
            itemView.bindData((IntegralModel) getItem(position), position);
            return convertView;
        }

    }

    class ItemView {
        @Bind(R.id.integral_title)
        TextView integralTitle;
        @Bind(R.id.integral_time)
        TextView integralTime;
        @Bind(R.id.integral_text)
        TextView integralText;

        public ItemView(View view) {
            ButterKnife.bind(this, view);
        }

        public void bindData(IntegralModel model, int position) {
            if (position % 2 == 0) {
                integralText.setTextColor(ContextCompat.getColor(mContext, R.color.color_212121));
            } else {
                integralText.setTextColor(ContextCompat.getColor(mContext, R.color.main_red_color));
            }

            integralText.setText("+"+model.getIntegralNum());
            integralTitle.setText(model.getIntegralTitle());
            integralTime.setText(model.getIntegralTime());

        }


    }

}
