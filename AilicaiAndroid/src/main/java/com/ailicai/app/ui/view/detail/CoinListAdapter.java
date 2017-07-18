package com.ailicai.app.ui.view.detail;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.model.bean.Coin;
import com.ailicai.app.ui.asset.treasure.ProductCategory;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.view.RegularFinanceDetailH5Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nanshan on 2017/5/8.
 */

public class CoinListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Coin> products = new ArrayList<>();
    private ProductCategory category;

    public CoinListAdapter(Context mContext, ProductCategory category) {
        this.mContext = mContext;
        this.category = category;
    }

    @Override
    public int getCount() {
        return products.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_coin_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (category == ProductCategory.Expired) {
            viewHolder.mRealProfitTitleRight.setText("实际收益");
            viewHolder.mLoanContractLayout.setVisibility(View.GONE);
        }else{
            viewHolder.mRealProfitTitleRight.setText("预计收益");
        }

        viewHolder.mCoinNumberValueLeft.setText(products.get(position).getPennyCode());
        viewHolder.mHoldCapitalValueMiddle.setText(products.get(position).getBidAmount());
        viewHolder.mRealProfitValueRight.setText(products.get(position).getUserBidInterest());

        viewHolder.mPreviousCoinInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RegularFinanceDetailH5Activity.class);
                intent.putExtra(RegularFinanceDetailH5Activity.EXTRA_URL, products.get(position).getInfoUrl());
                mContext.startActivity(intent);
            }
        });
        viewHolder.mLoanContractLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Map<String, String> dataMap = ObjectUtil.newHashMap();
                dataMap.put(WebViewActivity.TITLE, "借款协议");
                dataMap.put(WebViewActivity.URL, products.get(position).getProtocolUrl());
                dataMap.put(WebViewActivity.NEED_REFRESH, 0 + "");
                dataMap.put(WebViewActivity.TOPVIEWTHEME, "true");
                MyIntent.startActivity(mContext, WebViewActivity.class, dataMap);
            }

        });

        return convertView;
    }

    void goToWebView(String url, String title){
        Map<String, String> dataMap = ObjectUtil.newHashMap();
        dataMap.put(WebViewActivity.TITLE, title);
        dataMap.put(WebViewActivity.URL, url);
        dataMap.put(WebViewActivity.NEED_REFRESH, 0 + "");
        dataMap.put(WebViewActivity.TOPVIEWTHEME, "true");
        MyIntent.startActivity(mContext, RegularFinanceDetailH5Activity.class, dataMap);

    }

    static class ViewHolder {

        @Bind(R.id.coin_number_value_left)
        TextView mCoinNumberValueLeft;
        @Bind(R.id.hold_capital_value_middle)
        TextView mHoldCapitalValueMiddle;
        @Bind(R.id.real_profit_title_right)
        TextView mRealProfitTitleRight;
        @Bind(R.id.real_profit_value_right)
        TextView mRealProfitValueRight;

        @Bind(R.id.previous_coin_info_layout)
        View mPreviousCoinInfoLayout;
        @Bind(R.id.loan_contract_layout)
        View mLoanContractLayout;


        public ViewHolder() {

        }
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void updateData(List<Coin> products, boolean needClear){
        if (needClear) {
            this.products.clear();
        }
        this.products.addAll(products);
    }
    public int getHasLoadedSmallCoinData(){
        return this.products.size();
    }


}
