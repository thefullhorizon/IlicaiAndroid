package com.ailicai.app.ui.reserve;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;


import com.ailicai.app.R;

import butterknife.Bind;

/**
 * Created by duo.chen on 2016/1/5.
 */
public class TimeZHTextCounter extends BaseTimeTextCounter {

    @Bind(R.id.time_zh_text_counter_pretag)
    TextView mTimeZhTextCounterPreTag;
    @Bind(R.id.time_zh_counter_h)
    TextView mTimeZhCounterH;
    @Bind(R.id.time_zh_counter_m)
    TextView mTimeZhCounterM;
    @Bind(R.id.time_zh_counter_s)
    TextView mTimeZhCounterS;
    @Bind(R.id.tv_hour_label)
    TextView tvHourLabel;
    @Bind(R.id.tv_minute_label)
    TextView tvMinuteLable;
    @Bind(R.id.tv_second_label)
    TextView tvSecondLabel;

    public TimeZHTextCounter(Context context) {
        super(context);
    }

    public TimeZHTextCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeZHTextCounter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TimeZHTextCounter);
        int textColor = attributes.getColor(R.styleable.TimeZHTextCounter_time_text_color, ContextCompat.getColor(context, R.color.white));
        mTimeZhTextCounterPreTag.setTextColor(textColor);
        mTimeZhCounterH.setTextColor(textColor);
        mTimeZhCounterM.setTextColor(textColor);
        mTimeZhCounterS.setTextColor(textColor);
        tvHourLabel.setTextColor(textColor);
        tvMinuteLable.setTextColor(textColor);
        tvSecondLabel.setTextColor(textColor);
        attributes.recycle();
        setPreTag("距开始:");
    }

    @Override
    public int getLayout() {
        return R.layout.time_zh_text_counter_layout;
    }

    public void setPreTag(String tag) {
        mTimeZhTextCounterPreTag.setText(tag);
    }

    protected void setHMS(long time) {
        if (null == calendar) {
            calendar = new CalculatorCalender(time);
        } else {
            calendar.setTimeInMillis(time);
        }

        int hour = calendar.getHour();
        int minute = calendar.getMinute();
        int second = calendar.getSecond();
        mTimeZhCounterH.setText(addZeroPrefix(hour));
        mTimeZhCounterM.setText(addZeroPrefix(minute));
        mTimeZhCounterS.setText(addZeroPrefix(second));
    }
}
