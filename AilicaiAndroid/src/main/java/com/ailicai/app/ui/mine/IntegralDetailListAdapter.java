package com.ailicai.app.ui.mine;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.model.response.ScoreDetailResponse;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 积分明细记录
 */
public class IntegralDetailListAdapter extends BaseAdapter {

    List<ScoreDetailResponse> integralRecordList;
    private Context mContext;

    public IntegralDetailListAdapter(Context context, List<ScoreDetailResponse> rewardRecordList) {
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
            noData.setText("");
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
            itemView.bindData((ScoreDetailResponse) getItem(position), position);
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

        public void bindData(ScoreDetailResponse model, int position) {
            integralTime.setText(model.getChangeDate());
            // 积分变动缘由(1:续期消耗, 2:升级消耗,3:投资获得)
            switch (model.getReason()) {
                case 1:
                    integralText.setTextColor(ContextCompat.getColor(mContext, R.color.color_212121));
                    integralTitle.setText("续期消耗");
                    integralText.setText("-" + model.getIncrement());
                    break;
                case 2:
                    integralText.setTextColor(ContextCompat.getColor(mContext, R.color.color_212121));
                    integralTitle.setText("升级消耗");
                    integralText.setText("-" + model.getIncrement());
                    break;
                case 3:
                    integralText.setTextColor(ContextCompat.getColor(mContext, R.color.main_red_color));
                    integralTitle.setText("投资获得");
                    integralText.setText("+" + model.getIncrement());
                    break;
            }

        }


    }

}
