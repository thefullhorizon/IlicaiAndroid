package com.ailicai.app.ui.reserve;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.SpannableUtil;


/**
 * Created by zhujiang on 2017/3/29.
 */

public class YearRateView extends FrameLayout implements TagLayout.IitemView<String>{

    private TextView tvYearRate;

    public YearRateView(Context context) {
        this(context,null);
    }

    public YearRateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public YearRateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_year_rate,this,true);
        tvYearRate = (TextView) findViewById(R.id.year_rate);
    }

    @Override
    public void render(String bean) {
        SpannableUtil spannableUtil = new SpannableUtil(getContext());
        SpannableStringBuilder builder;
        builder = spannableUtil.getSpannableString(bean.replace("%",""),"%",R.style.text_24_2962ff, R.style.text_16_2962ff);
        tvYearRate.setText(builder);
    }
}
