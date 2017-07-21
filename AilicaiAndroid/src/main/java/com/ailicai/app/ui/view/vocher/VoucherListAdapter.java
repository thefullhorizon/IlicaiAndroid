package com.ailicai.app.ui.view.vocher;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ailicai.app.R;
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
public class VoucherListAdapter extends BaseAdapter {

    private List<Voucher> values;
    private Context context;
    private UseClickListener listener;

    public VoucherListAdapter(Context context, List<Voucher> values, UseClickListener listener) {
        this.context = context;
        this.values = values;
        this.listener = listener;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_voucher, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Voucher voucher = getItem(position);
        //利率
        viewHolder.tvVoucherRate.setText(String.valueOf(voucher.getAddRate() + "%"));
        viewHolder.tvVoucherName.setText(voucher.getVoucherName());
        //卡券类型
        viewHolder.tvVoucherType.setText(voucher.getVoucherTypeStr());
        //卡券类型下的描述
        viewHolder.tvVoucherTerm.setText(voucher.getSimpleDesc());
        //卡券描述
        viewHolder.tvVoucherDesc.setText(voucher.getBottomDesc());
        //卡券有效期
        viewHolder.tvVoucherTime.setText("有效期: " + voucher.getUserTimeFrom() + " 至 " + voucher.getUserTimeTo());
        switch (voucher.getStatus()) {
            case 1:
                //可用
                viewHolder.tvMoneyUnit.setTextColor(ContextCompat.getColor(context, R.color.color_e84a01));
                viewHolder.tvVoucherRate.setTextColor(ContextCompat.getColor(context, R.color.color_e84a01));
                viewHolder.tvVoucherType.setTextColor(ContextCompat.getColor(context, R.color.color_757575));
                viewHolder.tvVoucherTerm.setTextColor(ContextCompat.getColor(context, R.color.color_757575));
                viewHolder.tvVoucherDesc.setTextColor(ContextCompat.getColor(context, R.color.color_757575));
                viewHolder.tvVoucherTime.setTextColor(ContextCompat.getColor(context, R.color.color_757575));
                viewHolder.tvVoucherName.setTextColor(ContextCompat.getColor(context, R.color.color_757575));
                viewHolder.tvUse.setClickable(true);
                viewHolder.tvUse.setBackgroundResource(R.drawable.select_voucher_btn_selector);
                viewHolder.tvUse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击立即使用
                        if (!CheckDoubleClick.isFastDoubleClick()) {
                            listener.useClick(position);
                        }
                    }
                });
                break;
            default:
                //不可用
                viewHolder.tvMoneyUnit.setTextColor(ContextCompat.getColor(context, R.color.color_BDBDBD));
                viewHolder.tvVoucherRate.setTextColor(ContextCompat.getColor(context, R.color.color_BDBDBD));
                viewHolder.tvVoucherType.setTextColor(ContextCompat.getColor(context, R.color.color_BDBDBD));
                viewHolder.tvVoucherTerm.setTextColor(ContextCompat.getColor(context, R.color.color_BDBDBD));
                viewHolder.tvVoucherDesc.setTextColor(ContextCompat.getColor(context, R.color.color_BDBDBD));
                viewHolder.tvVoucherTime.setTextColor(ContextCompat.getColor(context, R.color.color_BDBDBD));
                viewHolder.tvVoucherName.setTextColor(ContextCompat.getColor(context, R.color.color_BDBDBD));
                viewHolder.tvUse.setEnabled(false);
                viewHolder.tvUse.setBackgroundResource(R.drawable.ticket_unavailable);
                break;
        }
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
        @Bind(R.id.tv_money_unit)
        TextView tvMoneyUnit;//+号
        @Bind(R.id.tv_voucher_rate)
        TextView tvVoucherRate;//加息全利率
        @Bind(R.id.tv_voucher_type)
        TextView tvVoucherType;//卡券类型
        @Bind(R.id.tv_voucher_term)
        TextView tvVoucherTerm;//多少天以上投资可用
        @Bind(R.id.tv_voucher_desc)
        TextView tvVoucherDesc;//卡券描述
        @Bind(R.id.tv_voucher_time)
        TextView tvVoucherTime;//有效期
        @Bind(R.id.tv_use)
        TextView tvUse;//选择按钮
        @Bind(R.id.tv_voucher_name)
        TextView tvVoucherName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public interface UseClickListener {
        void useClick(int position);
    }
}
