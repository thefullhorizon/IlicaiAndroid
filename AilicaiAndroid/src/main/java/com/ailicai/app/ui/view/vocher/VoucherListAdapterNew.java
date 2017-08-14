package com.ailicai.app.ui.view.vocher;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.SpannableUtil;
import com.ailicai.app.model.bean.Voucher;
import com.huoqiu.framework.util.CheckDoubleClick;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 现金券列表adapter
 * Created by liyanan on 16/3/8.
 */
public class VoucherListAdapterNew extends BaseAdapter {

    private List<Voucher> values;
    private Context context;
    private UseClickListener listener;
    private int appropriateVoucherId ;

    public VoucherListAdapterNew(Context context, List<Voucher> values,int appropriateVoucherId, UseClickListener listener) {
        this.context = context;
        this.values = values;
        this.listener = listener;
        this.appropriateVoucherId = appropriateVoucherId;
    }

    @Override
    public int getCount() {
        return values == null ? 0 : values.size();
    }

    @Override
    public Voucher getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_voucher_new, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Voucher voucher = getItem(position);

        //top
        switch (voucher.getVoucherType()) {
            case 73://加息券
                viewHolder.mIndicatorVoucherType.setText(" + ");
                viewHolder.mVoucherValue.setText(String.valueOf(voucher.getAddRate() + "%"));
                viewHolder.mVoucherType.setText("加息券");
                break;
            case 74://返金券
                viewHolder.mIndicatorVoucherType.setText(R.string.help_payquestion);
                viewHolder.mVoucherValue.setText(voucher.getAmountCentString()+"");
                viewHolder.mVoucherType.setText("返金券");
                break;
        }
        switch (voucher.getStatus()) {
            case 1:
                viewHolder.mItemLayout.setClickable(true);
                viewHolder.mItemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!CheckDoubleClick.isFastDoubleClick()) {
                            listener.useClick(position);
                        }
                    }
                });
                viewHolder.mItemUp.setBackgroundResource(R.drawable.bg_voucher_up);
                viewHolder.mBgVoucherGear.setBackgroundResource(R.drawable.bg_voucher_up);
                viewHolder.mIndicatorBest.setVisibility(View.VISIBLE);

                viewHolder.mIndicatorBest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!CheckDoubleClick.isFastDoubleClick()) {
                            listener.useClick(position);
                        }
                    }
                });
                if (appropriateVoucherId != -1 && appropriateVoucherId == voucher.getVoucherId() ){
                    viewHolder.mIndicatorBest.setChecked(true);
                }else{
                    viewHolder.mIndicatorBest.setChecked(false);
                }

                break;
            default:
                viewHolder.mItemLayout.setClickable(false);
                viewHolder.mItemUp.setBackgroundColor(Color.parseColor("#dddddd"));
                viewHolder.mBgVoucherGear.setBackgroundColor(Color.parseColor("#dddddd"));//BackgroundResource R.color.color_dddddd
                viewHolder.mIndicatorBest.setVisibility(View.GONE);
                break;
        }
        viewHolder.mVoucherName.setText(voucher.getVoucherName());

        //middle
        viewHolder.mVoucherDescription.setText(voucher.getBottomDesc());
        String limit = "";
        if (!TextUtils.isEmpty(voucher.getSimpleDesc())){
            limit += voucher.getSimpleDesc();
        }
        if (!TextUtils.isEmpty(voucher.getMinAmountCentString())){
            limit += voucher.getMinAmountCentString();
        }
        if (!TextUtils.isEmpty(voucher.getUseRange())){
            limit += voucher.getUseRange();
        }
        viewHolder.mVoucherLimit.setText(limit);

        //bottom
        SpannableUtil spannableUtil = new SpannableUtil(context);
        String leftDay = voucher.getLeftValidDayString();
        SpannableStringBuilder builder ;
        if (!TextUtils.isEmpty(leftDay)){
            builder = spannableUtil.getSpannableString("有效期: "+ voucher.getUserTimeFrom()+" 至 "+voucher.getUserTimeTo()," （仅剩"+voucher.getLeftValidDays()+"天)", R.style.text_12_757575, R.style.text_12_d0011b);
        }else{
            builder = spannableUtil.getSpannableString("有效期: "+ voucher.getUserTimeFrom()+" 至 "+voucher.getUserTimeTo(), R.style.text_12_757575);
        }
        viewHolder.mVoucherAvailableTime.setText(builder);

        return convertView;
    }

    /**
     * 日期转换
     *
     * @param time
     * @return
     */
    private String formatDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(time);
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    class ViewHolder {

        @Bind(R.id.item_layout)
        LinearLayout mItemLayout;
        @Bind(R.id.rl_bg_voucher_gear)
        RelativeLayout mBgVoucherGear;
        @Bind(R.id.item_up)
        LinearLayout mItemUp;
        @Bind(R.id.indicator_voucher_type)
        TextView mIndicatorVoucherType;
        @Bind(R.id.voucher_value)
        TextView mVoucherValue;
        @Bind(R.id.voucher_type)
        TextView mVoucherType;
        @Bind(R.id.voucher_name)
        TextView mVoucherName;
        @Bind(R.id.indicator_best)
        CheckBox mIndicatorBest;

        @Bind(R.id.voucher_description)
        TextView mVoucherDescription;
        @Bind(R.id.voucher_limit)
        TextView mVoucherLimit;

        @Bind(R.id.voucher_available_time)
        TextView mVoucherAvailableTime;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public interface UseClickListener {
        void useClick(int position);
    }
}
