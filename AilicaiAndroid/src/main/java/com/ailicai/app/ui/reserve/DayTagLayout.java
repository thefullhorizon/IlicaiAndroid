package com.ailicai.app.ui.reserve;

import android.content.Context;
import android.util.AttributeSet;


import com.ailicai.app.model.bean.CustomSingleChoiceBean;

import java.util.List;



/**
 * Created by zhujiang on 2017/3/29.
 */

public class DayTagLayout extends TagLayout<CustomSingleChoiceBean> {

    public DayTagLayout(Context context) {
        super(context);
    }

    public DayTagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DayTagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init(List<CustomSingleChoiceBean> listBean) {
        init(listBean,90);
    }

    public void init(List<CustomSingleChoiceBean> listBean, int selectTerm) {
        removeAllViews();
        for (CustomSingleChoiceBean t:listBean) {
            DayTagView dayTagView = new DayTagView(getContext());
            if (t.getName().contains(selectTerm+"")) {
                dayTagView.setSelected(true);
            }
            dayTagView.setTag(t);
            dayTagView.render(t);
            dayTagView.setOnClickListener(this);
            addView(dayTagView);
            views.add(dayTagView);
        }
    }
}
