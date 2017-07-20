package com.ailicai.app.ui.reserve;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.huoqiu.framework.util.TimeUtil;

import butterknife.ButterKnife;

/**
 * Created by duo.chen on 2016/1/8.
 */
public abstract class BaseTimeTextCounter extends LinearLayout {

    CountDownTimer countDownTimer;
    CounterFinishListener counterFinishListener;
    CalculatorCalender calendar;

    public BaseTimeTextCounter(Context context) {
        super(context);
        initView(context,null);
    }

    public BaseTimeTextCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public BaseTimeTextCounter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View.inflate(context, getLayout(), this);
        ButterKnife.bind(this);
        init(context,attrs);
    }

    public void init(Context context, AttributeSet attrs){

    }

    public abstract int getLayout();

    public long currentTimeMillis() {
        return TimeUtil.getCurrentTimeExact().getTimeInMillis();
    }

    public void setTimeMills(long timeMills) {
        long time = timeMills - currentTimeMillis();
        time = time > 0 ? time : 0L;

        if (null == countDownTimer) {
            initCounter(time);
        } else {
            countDownTimer.cancel();
            initCounter(time);
        }
    }

    public void initCounter(long timeMills) {
        if (timeMills >= 0) {
            setHMS(timeMills);
            countDownTimer = new CountDownTimer(timeMills, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setHMS(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    if (null != counterFinishListener) {
                        counterFinishListener.onFinish();
                    }
                }
            }.start();
        } else {
            if (null != counterFinishListener) {
                counterFinishListener.onFinish();
            }
        }

    }

    protected abstract void setHMS(long timeMills);

    public String addZeroPrefix(int num) {
        StringBuilder stringBuilder = new StringBuilder();
        if (num >= 10) {
            stringBuilder.append(num);
        } else {
            stringBuilder.append(0).append(num);
        }
        return stringBuilder.toString();
    }

    public void destory() {
        if (null != countDownTimer) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destory();
    }

    public void setCounterFinishListener(CounterFinishListener counterFinishListener) {
        this.counterFinishListener = counterFinishListener;
    }
}
