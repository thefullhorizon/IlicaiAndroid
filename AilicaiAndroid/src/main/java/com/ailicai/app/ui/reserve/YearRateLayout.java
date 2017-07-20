package com.ailicai.app.ui.reserve;

import android.content.Context;
import android.util.AttributeSet;

import com.ailicai.app.model.response.ReserveDetailResponse;

import java.util.List;

/**
 * Created by zhujiang on 2017/3/29.
 */

public class YearRateLayout extends TagLayout<ReserveDetailResponse.ProductRate> {

    public YearRateLayout(Context context) {
        this(context,null);
    }

    public YearRateLayout(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public YearRateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init(List<ReserveDetailResponse.ProductRate> listBean) {
        views.clear();
        removeAllViews();
        for (ReserveDetailResponse.ProductRate s:listBean) {
            YearRateView yearRateView = new YearRateView(getContext());
            yearRateView.render(s.getRate());
            yearRateView.setTag(s);
            yearRateView.setOnClickListener(this);
            addView(yearRateView);
            views.add(yearRateView);
        }
    }
}
