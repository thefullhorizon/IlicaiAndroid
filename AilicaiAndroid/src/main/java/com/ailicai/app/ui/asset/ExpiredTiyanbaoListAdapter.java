package com.ailicai.app.ui.asset;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.model.bean.TiyanbaoDetailModel;
import com.huoqiu.framework.util.CheckDoubleClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/15.
 */
public class ExpiredTiyanbaoListAdapter extends BaseAdapter implements View.OnClickListener{

    private List<TiyanbaoDetailModel> mData = new ArrayList<>();
    private Context mContext;
    private Event event;
    public ExpiredTiyanbaoListAdapter(Context context) {
        mContext = context;
    }

    public void addDataSource(List<TiyanbaoDetailModel> data, boolean clear){
        if (clear){
            mData.clear();
        }
        this.mData.addAll(data);

    }

    public int getProductOffset(){
        return mData.size();
    }
    public void setEvent(Event event){
        this.event = event;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expired_tiyanbao_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TiyanbaoDetailModel model = mData.get(position);
        viewHolder.productName.setText(model.getProductName());
        viewHolder.yearInterestrate.setText(String.format("%s | 期限 %s", model.getYearInterestRateStr(),model.getHorizonStr()));
        viewHolder.valueLeft.setText(model.getBackAmount());
        viewHolder.valueRight.setText(model.getBackDateStr());
        viewHolder.itemLayout.setOnClickListener(this);
        viewHolder.itemLayout.setTag(model.getCouponId());
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (CheckDoubleClick.isFastDoubleClick()) return;

        switch (v.getId()){
            case R.id.tiyanbao_item_layout:
                long couponId = (Long) v.getTag();
                event.toTiyanbaoProductDetail(couponId);
                break;
        }

    }

    static class ViewHolder{
        @Bind(R.id.tiyanbao_item_layout)
        View itemLayout;
        @Bind(R.id.tiyanbao_name)
        TextView productName;
        @Bind(R.id.tiyanbao_yearinterestrate)
        TextView yearInterestrate;
        @Bind(R.id.tiyanbao_value_left)
        TextView valueLeft;
        @Bind(R.id.tiyanbao_value_right)
        TextView valueRight;


        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }

    public interface Event{
        void toTiyanbaoProductDetail(long couponId);
    }
}
