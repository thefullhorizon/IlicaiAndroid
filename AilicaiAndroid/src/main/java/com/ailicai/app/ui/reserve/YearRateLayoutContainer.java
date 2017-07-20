package com.ailicai.app.ui.reserve;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.ailicai.app.R;
import com.ailicai.app.model.response.ReserveDetailResponse;
import com.ailicai.app.ui.view.RegularFinanceDetailH5Activity;

import java.util.List;
import java.util.Locale;

/**
 * Created by zhujiang on 2017/3/29.
 */

public class YearRateLayoutContainer extends FrameLayout implements TagLayout.OnItemClickListener<ReserveDetailResponse.ProductRate>{

    private View nodata;
    private View container;

    private YearRateLayout yearRateLayout;
    private TextView tvCount;
    private TextView tvPreserveBuyTimeTip;
    private List<ReserveDetailResponse.ProductRate> productRates;

    public YearRateLayoutContainer(@NonNull Context context) {
        this(context,null);
    }

    public YearRateLayoutContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public YearRateLayoutContainer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_year_rate,this,true);
        nodata = findViewById(R.id.tv_no_data);
        container = findViewById(R.id.ll_container);
        yearRateLayout = (YearRateLayout) findViewById(R.id.year_rate_layout);
        tvCount = (TextView) findViewById(R.id.tv_count);
        tvPreserveBuyTimeTip = (TextView)findViewById(R.id.tv_preserve_buy_time);
    }

    public void init(String preBuyTime, List<ReserveDetailResponse.ProductRate> yearRates) {
        productRates = yearRates;
        if (productRates!=null && productRates.size() > 0) {
            tvPreserveBuyTimeTip.setText(String.format(Locale.CHINA,getContext().getResources().getString(R.string.reserve_pre_buy_time),preBuyTime));
            nodata.setVisibility(GONE);
            container.setVisibility(VISIBLE);
            yearRateLayout.init(productRates);
            yearRateLayout.setOnItemClickListener(this);
            tvCount.setText(String.format("将为您投资到以下%d个房产宝",productRates.size()));
        } else {
            nodata.setVisibility(VISIBLE);
            container.setVisibility(GONE);
        }

    }
    public List<ReserveDetailResponse.ProductRate> getProductRates(){
        return productRates;
    }

    @Override
    public void onClicked(View itemView, ReserveDetailResponse.ProductRate bean) {
        if(bean != null) {
            Intent intent = new Intent(getContext(), RegularFinanceDetailH5Activity.class);
            intent.putExtra(RegularFinanceDetailH5Activity.EXTRA_URL, bean.getProductDetailUrl());
            getContext().startActivity(intent);
        }
    }
}
