package com.ailicai.app.ui.asset;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.model.bean.Product;
import com.ailicai.app.ui.asset.treasure.EmptyValue;
import com.ailicai.app.ui.asset.treasure.ProductCategory;
import com.ailicai.app.widget.TextViewDinFont;
import com.ailicai.app.widget.TextViewTF;
import com.alibaba.fastjson.JSON;
import com.huoqiu.framework.util.CheckDoubleClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by David on 16/3/8.
 */
public class HouseTreasureAdapter extends BaseAdapter implements View.OnClickListener {
    Context mContext;
    DisplayMetrics mDisplayMetrics;
    Event event;

    List<Product> products = new ArrayList<>();
    List<Product> reserves = new ArrayList<>(); //申购列表中的预约
    List<Product> tiyanbaos = new ArrayList<>();//持有列表中的体验宝
    ProductCategory category;
    boolean validNetwork = true;
    String errorInfo = "";
    int expiredTiyanbaoCount;

    EmptyValue empty = new EmptyValue();


    public HouseTreasureAdapter(Context context) {
        this.mContext = context;
        this.mDisplayMetrics = mContext.getResources().getDisplayMetrics();
    }


    public void updateEmpty(ProductCategory category, boolean showLoading, int height) {
        this.products.clear();
        this.reserves.clear();
        this.tiyanbaos.clear();
        this.empty.setCategory(category);
        this.empty.setShowLoading(showLoading);
        this.empty.setHeight(height);
    }

    public void updateNetworkState(boolean validNetwork,String errorInfo){
        this.validNetwork = validNetwork;
        this.errorInfo = errorInfo;
    }

    public void updateExpiredTiyanbaoCount(int count){
        this.expiredTiyanbaoCount = count;
    }
    public void setEvent(Event event) {
        this.event = event;
    }


    public void addDataSource(List<Product> products, List<Product> reserves, List<Product> tiyanbaos,ProductCategory category, boolean clear) {
        if (clear) {
            this.products.clear();
            this.reserves.clear();
            this.tiyanbaos.clear();
        }

        this.products.addAll(products);
        this.reserves.addAll(reserves);
        if (tiyanbaos != null){
            this.tiyanbaos.addAll(tiyanbaos);
        }
        this.category = category;
    }

    public int getProductOffset() {
        return products.size();
    }

    public int getReserveOffset() {
        return reserves.size();
    }

    public int getTiyanbaoOffset(){
        return tiyanbaos.size();
    }

    @Override
    public int getCount() {
        return products.size() + reserves.size() +tiyanbaos.size() == 0 ? 1 : products.size() + reserves.size() +tiyanbaos.size();
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (products.size() + reserves.size() +tiyanbaos.size() == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (getItemViewType(position) == 0) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.house_tresure_empty_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.loading = convertView.findViewById(R.id.treasure_empty_loading);
                viewHolder.content = (TextView) convertView.findViewById(R.id.treasure_empty_content);
                viewHolder.action = (TextView) convertView.findViewById(R.id.treasure_empty_action);
                viewHolder.aLayout = convertView.findViewById(R.id.treasure_empty_action_layout);

                viewHolder.eLayout = convertView.findViewById(R.id.network_error_layout);
                viewHolder.eTips = (TextView) convertView.findViewById(R.id.error_text);
                viewHolder.eReload = (Button) convertView.findViewById(R.id.error_btn);
                viewHolder.tLayout = convertView.findViewById(R.id.treasure_empty_tiyanbao_layout);
                viewHolder.tNumber = (TextView) convertView.findViewById(R.id.treasure_empty_tiyanbao_number);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (empty == null) return convertView;
            viewHolder.aLayout.setVisibility(!empty.isShowLoading() &&validNetwork ? View.VISIBLE : View.GONE);
            viewHolder.eLayout.setVisibility(!empty.isShowLoading() && !validNetwork ? View.VISIBLE :View.GONE);
            viewHolder.loading.setVisibility(empty.isShowLoading() ? View.VISIBLE : View.GONE);
            viewHolder.eTips.setText(errorInfo);
            viewHolder.content.setText(empty.getCategory().getContent());
            viewHolder.action.setText(empty.getCategory().getAction());
            viewHolder.eReload.setOnClickListener(this);
            viewHolder.action.setOnClickListener(this);
            if (category == ProductCategory.Expired && expiredTiyanbaoCount > 0){
                HashMap map = new HashMap();
                map.put("name","dqtyj");
                map.put("action","show");
                EventLog.upEventLog("806", JSON.toJSONString(map));
                viewHolder.tLayout.setVisibility(View.VISIBLE);
                viewHolder.tLayout.setOnClickListener(this);
                viewHolder.tNumber.setText(expiredTiyanbaoCount+"笔");
            }else {
                viewHolder.tLayout.setVisibility(View.GONE);
            }
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, empty.getHeight() < 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : empty.getHeight()));

            return convertView;
        }


        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.house_tresure_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Product product;

        viewHolder.productIcon.setText(mContext.getResources().getString(R.string.fang_soild_border));
        viewHolder.productIcon.setTextColor(mContext.getResources().getColor(R.color.main_red_color));
        viewHolder.productIcon.setTextSize(16);
        viewHolder.reservingTagApplying.setVisibility(View.GONE);
        if (category == ProductCategory.Apply) {
//            viewHolder.applyProgress.setVisibility(View.VISIBLE);
            if (position <= reserves.size() - 1) {
                viewHolder.mTVYearInterestDivider.setVisibility(View.GONE);
                viewHolder.mTVYearInterest.setVisibility(View.GONE);
                product = reserves.get(position);
                viewHolder.productIcon.setText(mContext.getResources().getString(R.string.reservation_fill));
                viewHolder.productIcon.setTextColor(mContext.getResources().getColor(R.color.color_005ebd));
                viewHolder.productIcon.setTextSize(16);
                viewHolder.labelLeft.setText("预约金额(元)");
                viewHolder.valueLeft.setText(product.getBidAmountStr());
                viewHolder.labelRight.setText("预计年化");
                viewHolder.valueRight.setText(product.getYearInterestRateNoMemo());
//                viewHolder.applyProgress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.capital_reserve_progressbar_drawable));
//                viewHolder.applyProgress.setProgress((int)Math.round(100*product.getHasBuyPrecent()));
                viewHolder.mTVDate.setText(product.getOrderTimeStr() +" 预约");
                viewHolder.mTVLimit.setText("期限 "+product.getHorizonStr());

                viewHolder.reservingTagApplying.setVisibility(View.VISIBLE);
                viewHolder.reservingTagApplying.setText("预约进度"+product.getHasBuyPrecentStr());
                viewHolder.reservingTagApplying.setBackgroundResource(R.drawable.treasure_purchasing_tag);

            } else {
                product = products.get(position - reserves.size());
                viewHolder.labelLeft.setText("申购金额(元)");
                viewHolder.valueLeft.setText(product.getBidAmountStr());
                viewHolder.labelRight.setText("预计收益(元)");
                viewHolder.valueRight.setText(product.getProfitStr());
//                viewHolder.applyProgress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.financing_progressbar_drawable));
//                viewHolder.applyProgress.setProgress((int)Math.round(100*product.getHasBuyPrecent()));
                viewHolder.mTVDate.setText(product.getOrderTimeStr() +" 申购");
                viewHolder.mTVYearInterest.setText(product.getYearInterestRateStr());
                viewHolder.mTVLimit.setText("期限 "+product.getHorizonStr());

                viewHolder.reservingTagApplying.setVisibility(View.VISIBLE);
                viewHolder.reservingTagApplying.setText("申购进度"+product.getHasBuyPrecentStr());
                viewHolder.reservingTagApplying.setBackgroundResource(R.drawable.treasure_purchasing_normal_tag);
            }

        } else if (category == ProductCategory.Holder) {
            viewHolder.applyProgress.setVisibility(View.GONE);
            viewHolder.mTVYearInterestDivider.setVisibility(View.VISIBLE);
            viewHolder.mTVYearInterest.setVisibility(View.VISIBLE);
            if (position <= tiyanbaos.size() -1){
                product = tiyanbaos.get(position);
            }else {
                product = products.get(position - tiyanbaos.size());
            }
            if (product.getIsTransfer() == 1){
                viewHolder.productIcon.setText(mContext.getResources().getString(R.string.zhuan_soild_border));
                viewHolder.productIcon.setTextColor(mContext.getResources().getColor(R.color.color_26b095));
            }

            viewHolder.labelLeft.setText("持有金额(元)");
            viewHolder.valueLeft.setText(product.getBidAmountStr());
            viewHolder.labelRight.setText("预计收益(元)");
            viewHolder.mTVDate.setText(product.getBackDateStr() +" 回款");
            if (position <= tiyanbaos.size() -1){
                viewHolder.valueRight.setText(product.getBackAmount());
            }else {
                viewHolder.valueRight.setText(product.getProfitStr());
            }
            viewHolder.mTVYearInterest.setText(product.getYearInterestRateStr());
            viewHolder.mTVLimit.setText("期限 "+product.getHorizonStr());
        } else {
            viewHolder.applyProgress.setVisibility(View.GONE);
            product = products.get(position);
            if (product.getIsTransfer() == 1){
                viewHolder.productIcon.setText(mContext.getResources().getString(R.string.zhuan_soild_border));
                viewHolder.productIcon.setTextColor(mContext.getResources().getColor(R.color.color_26b095));
            }
            viewHolder.labelLeft.setText("投资金额（元）");
            viewHolder.valueLeft.setText(product.getBidAmountStr());
            viewHolder.labelRight.setText("实际收益（元）");
            viewHolder.valueRight.setText(product.getProfitStr());
            viewHolder.mTVDate.setText(product.getBackDateStr() +" 回款");
            viewHolder.mTVYearInterest.setText(product.getYearInterestRateStr());
            viewHolder.mTVLimit.setText("期限 "+product.getHorizonStr());
        }

        if ("CONSUME_PURSE".equals(product.getType())){
            viewHolder.productIcon.setText(mContext.getResources().getString(R.string.coin_sack_border));
            viewHolder.productIcon.setTextColor(mContext.getResources().getColor(R.color.color_e84a01));
        }
        //加息券逻辑+返金券显示
        String text = "";
        if (product.getIsAddRate() > 0 || product.getIsCashBackVoucher() > 0){
            viewHolder.addRateLayout.setVisibility(View.VISIBLE);
            String textAddRate = TextUtils.isEmpty(product.getAddRateInfo()) ? "" : product.getAddRateInfo();
            String textCoucher = TextUtils.isEmpty(product.getCashBackVoucherCopywriter()) ? "" : product.getCashBackVoucherCopywriter();
            viewHolder.addRateContent.setText(textAddRate + textCoucher);
        }else {
            viewHolder.addRateLayout.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(product.getHasTransferAmount())&& TextUtils.isEmpty(product.getTransferingAmount())){
            viewHolder.transferLayout.setVisibility(View.GONE);
        }else {
            viewHolder.transferLayout.setVisibility(View.VISIBLE);
            viewHolder.capitalTransferred.setText(product.getHasTransferAmount());
            viewHolder.capitalTransfering.setText(product.getTransferingAmount());
        }
        viewHolder.productName.setText(product.getProductName());


        viewHolder.detail.setOnClickListener(this);
        viewHolder.detail.setTag(position);
        if (position == 0 && category == ProductCategory.Expired && expiredTiyanbaoCount > 0){
            HashMap map = new HashMap();
            map.put("name","dqtyj");
            map.put("action","show");
            EventLog.upEventLog("806", JSON.toJSONString(map));
            viewHolder.tiyanbaoLayout.setVisibility(View.VISIBLE);
            viewHolder.tiyanbaoLayout.setOnClickListener(this);
            viewHolder.tiyanbaoNumber.setText(expiredTiyanbaoCount+"笔");
        }else {
            viewHolder.tiyanbaoLayout.setVisibility(View.GONE);
        }


        return convertView;
    }

    static class ViewHolder {
        View eLayout;//错误的总体布局
        TextView eTips;//错误说明
        Button eReload;//重新加载

        View aLayout;
        View loading;
        TextView content;
        TextView action;
        View tLayout;//到期列表，空数据是的体验宝笔数布局
        TextView tNumber;//到期列表，空数据是的体验宝笔数


        @Bind(R.id.treasure_detail)
        View detail;
        @Bind(R.id.treasure_name)
        TextView productName;


//        @Bind(R.id.treasure_yearinterestrate)
//        TextView yearInterestrate;
        @Bind(R.id.tv_date)
        TextView mTVDate;
        @Bind(R.id.tv_year_interest_divider)
        TextView mTVYearInterestDivider;
        @Bind(R.id.tv_year_interest)
        TextView mTVYearInterest;
        @Bind(R.id.tv_limit)
        TextView mTVLimit;



        @Bind(R.id.treasure_value_left)
        TextViewDinFont valueLeft;
        @Bind(R.id.treasure_value_right)
        TextViewDinFont valueRight;
        @Bind(R.id.treasure_label_left)
        TextView labelLeft;
        @Bind(R.id.treasure_label_right)
        TextView labelRight;

        @Bind(R.id.add_rate_layout)
        View addRateLayout;
        @Bind(R.id.add_rate_content)
        TextView addRateContent;
        @Bind(R.id.product_left_icon)
        TextViewTF productIcon;
        @Bind(R.id.apply_progress)
        ProgressBar applyProgress;
        @Bind(R.id.capital_transfer_layout)
        View transferLayout;
        @Bind(R.id.capital_transferred)
        TextView capitalTransferred;
        @Bind(R.id.capital_transfering)
        TextView capitalTransfering;
        @Bind(R.id.tiyanbao_number_layout)
        View tiyanbaoLayout;//到期体验宝
        @Bind(R.id.tiyanbao_number)
        TextView tiyanbaoNumber;
        @Bind(R.id.reserving_tag_applying)
        TextView reservingTagApplying;

        public ViewHolder() {

        }
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    @Override
    public void onClick(View v) {
        if (CheckDoubleClick.isFastDoubleClick()) return;


        switch (v.getId()){
            case R.id.treasure_empty_action:
                EventLog.upEventLog("353","shengou");
                event.toFinanceRegular();
                break;
            case R.id.treasure_detail:
                int position = (Integer) v.getTag();
                if (category == ProductCategory.Apply){
                    if (position <= reserves.size() -1){
                        Product product =reserves.get(position);
                        event.toReserveProductDetail(product.getProductId());

                    }else {
                        Product product = products.get(position-reserves.size());
                        if ("CONSUME_PURSE".equals(product.getType())){
                            event.toCoinSack(product.getProductId(),category.getType());
                        }else{
                            event.toProductDetail(product.getProductId(),category.getType(),product.getHasTransferAmount(),product.getTransferingAmount());
                        }
                    }

                }else if (category == ProductCategory.Holder){
                    if (position <= tiyanbaos.size() -1){
                        Product product = tiyanbaos.get(position);
                        event.toTiyanbaoProductDetail(product.getCouponId(),category.getType());
                    }else {
                        Product product = products.get(position - tiyanbaos.size());
                        if ("CONSUME_PURSE".equals(product.getType())){
                            event.toCoinSack(product.getProductId(),category.getType());
                        }else{
                            event.toProductDetail(product.getProductId(),category.getType(),product.getHasTransferAmount(),product.getTransferingAmount());
                        }
                    }
                }else {
                    Product product = products.get(position);
                    if ("CONSUME_PURSE".equals(product.getType())){
                        event.toCoinSack(product.getProductId(),category.getType());
                    }else{
                        event.toProductDetail(product.getProductId(),category.getType(),product.getHasTransferAmount(),product.getTransferingAmount());
                    }
                }
                break;
            case R.id.error_btn:
                event.errorReload();
                break;
            case R.id.tiyanbao_number_layout:
            case R.id.treasure_empty_tiyanbao_layout:
                HashMap map = new HashMap();
                map.put("name","dqtyj");
                map.put("action","click");
                EventLog.upEventLog("806", JSON.toJSONString(map));
                event.toExpiredTiyanbaoList();
                break;

        }
    }

    public interface Event {
        void toFinanceRegular();
        void toProductDetail(String productId, int type, String hasTransferAmount, String transferingAmount);
        void toReserveProductDetail(String productId);
        void errorReload();
        void toExpiredTiyanbaoList();
        void toTiyanbaoProductDetail(long couponId, int type);
        void toCoinSack(String productId, int type);
    }
}
