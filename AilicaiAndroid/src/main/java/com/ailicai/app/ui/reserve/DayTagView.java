package com.ailicai.app.ui.reserve;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.model.bean.CustomSingleChoiceBean;


/**
 * Created by zhujiang on 2017/3/29.
 */

public class DayTagView extends FrameLayout implements TagLayout.IitemView<CustomSingleChoiceBean>{

    public DayTagView(Context context) {
        this(context,null);
    }

    public DayTagView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    private TextView tvReserveTerm;

    public DayTagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_day_tag_view,this,true);
        tvReserveTerm = (TextView) findViewById(R.id.tv_reserve_term);
    }

    @Override
    public void render(CustomSingleChoiceBean bean) {
        tvReserveTerm.setText(bean.getName());
    }
}
