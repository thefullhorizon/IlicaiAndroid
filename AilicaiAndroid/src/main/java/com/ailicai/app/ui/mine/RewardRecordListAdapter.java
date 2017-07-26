package com.ailicai.app.ui.mine;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.model.bean.RewardRecord;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 奖励记录
 */
public class RewardRecordListAdapter extends BaseAdapter {

    List<RewardRecord> rewardRecordList;
    private Context mContext;

    public RewardRecordListAdapter(Context context, List<RewardRecord> rewardRecordList) {
        this.mContext = context;
        this.rewardRecordList = rewardRecordList;
    }

    @Override
    public int getCount() {
        return (rewardRecordList.size() == 0) ? 1 : rewardRecordList.size();
    }

    @Override
    public Object getItem(int position) {
        return rewardRecordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (rewardRecordList.size() == 0) {
            View emptyView = LayoutInflater.from(mContext).inflate(R.layout.reward_record_adapter_empty, null);
            TextView noData = (TextView) emptyView.findViewById(R.id.no_record);
            noData.setText("暂无奖励记录");
            return emptyView;
        } else {
            ItemView itemView;
            //if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.reward_record_adapter, null);
                itemView = new ItemView(convertView);
                convertView.setTag(itemView);
            //} else {
            //    itemView = (ItemView) convertView.getTag();
            //}
            itemView.bindData((RewardRecord) getItem(position), position);
            return convertView;
        }

    }

    class ItemView {
        @Bind(R.id.rewardTime)
        TextView rewardTime;
        @Bind(R.id.status)
        TextView status;
        @Bind(R.id.reward)
        TextView reward;
        @Bind(R.id.item_bg)
        LinearLayout itemBg;

        public ItemView(View view) {
            ButterKnife.bind(this, view);
        }

        public void bindData(RewardRecord model, int position) {
            if (position % 2 == 0) {
                itemBg.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            } else {
                itemBg.setBackgroundColor(ContextCompat.getColor(mContext, R.color.rewards_item_bg));
            }
            rewardTime.setText(model.getRewardTime());
            reward.setText(model.getReward());

            //奖励状态 0：待发放，1：已发放
            switch (model.getStatus()) {
                case 0:
                    status.setText("待发放");
                    reward.setTextAppearance(mContext, R.style.text_14_212121);
                    rewardTime.setTextAppearance(mContext, R.style.text_14_212121);
                    status.setTextAppearance(mContext, R.style.text_14_212121);
                    break;
                case 1:
                    status.setText("已发放");
                    reward.setTextAppearance(mContext, R.style.text_14_757575);
                    rewardTime.setTextAppearance(mContext, R.style.text_14_757575);
                    status.setTextAppearance(mContext, R.style.text_14_757575);
                    break;
            }
        }


    }

}
