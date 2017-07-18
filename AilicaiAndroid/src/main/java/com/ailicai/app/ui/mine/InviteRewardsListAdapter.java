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
import com.ailicai.app.model.bean.InviteRewards;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 邀请记录
 */
public class InviteRewardsListAdapter extends BaseAdapter {

    List<InviteRewards> inviteRewards;
    private Context mContext;

    public InviteRewardsListAdapter(Context context, List<InviteRewards> inviteRewards) {
        this.mContext = context;
        this.inviteRewards = inviteRewards;
    }

    @Override
    public int getCount() {
        return inviteRewards.size();
    }

    @Override
    public Object getItem(int position) {
        return inviteRewards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemView itemView;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.invite_rewards_adapter, null);
            itemView = new ItemView(convertView);
            convertView.setTag(itemView);
        } else {
            itemView = (ItemView) convertView.getTag();
        }
        itemView.bindData((InviteRewards) getItem(position), position);
        return convertView;
    }

    class ItemView {
        @Bind(R.id.mobile_text)
        TextView mobileText;
        @Bind(R.id.invite_time_text)
        TextView inviteTimeText;
        @Bind(R.id.status_text)
        TextView statusText;
        @Bind(R.id.item_bg)
        LinearLayout itemBg;

        public ItemView(View view) {
            ButterKnife.bind(this, view);
        }

        public void bindData(InviteRewards model, int position) {
            if (position % 2 == 0) {
                itemBg.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            } else {
                itemBg.setBackgroundColor(ContextCompat.getColor(mContext, R.color.rewards_item_bg));
            }
            mobileText.setText(model.getMobile());
            inviteTimeText.setText(model.getInviteTime());

            //0:未投资，1：投资中 ， 2：失效
            switch (model.getStatus()) {
                case 0:
                    statusText.setText("未投资");
                    break;
                case 1:
                    statusText.setText("投资中");
                    break;
                case 2:
                    statusText.setText("失效");
                    break;
            }
        }


    }

}
