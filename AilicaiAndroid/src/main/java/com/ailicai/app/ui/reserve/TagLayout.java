package com.ailicai.app.ui.reserve;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiang on 2017/3/29.
 */

public abstract class TagLayout<T> extends FlexboxLayout implements View.OnClickListener{

    public OnItemCheckedListener<T> onItemCheckedListener;
    public OnItemClickListener<T> onItemClickListener;
    protected List<View> views = new ArrayList<>();

    public TagLayout(Context context) {
        this(context,null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFlexWrap(FLEX_WRAP_WRAP);
        setFlexDirection(FLEX_DIRECTION_ROW);
        setAlignItems(ALIGN_ITEMS_STRETCH);
        setAlignContent(ALIGN_CONTENT_STRETCH);
        setJustifyContent(ALIGN_CONTENT_FLEX_START);
    }

    public void setOnItemCheckedListener(OnItemCheckedListener<T> onItemCheckedListener) {
        this.onItemCheckedListener = onItemCheckedListener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public abstract void init(List<T> listBean);

    @Override
    public void onClick(View v) {

        if (null != onItemCheckedListener) {
            for (View tv:views) {
                tv.setSelected(false);
            }
            v.setSelected(true);
            onItemCheckedListener.onChecked(v, (T) v.getTag());
        }

        if (null != onItemClickListener) {
            onItemClickListener.onClicked(v,(T)v.getTag());
        }

    }

    public interface OnItemCheckedListener<T> {
        void onChecked(View itemView, T bean);
    }

    public interface OnItemClickListener<T> {
        void onClicked(View itemView, T bean);
    }

    public interface IitemView<T> {
        void render(T bean);
    }

}
