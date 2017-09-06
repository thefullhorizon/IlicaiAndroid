package com.ailicai.app.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ailicai.app.MyApplication;


public class TextViewVipFont extends TextView {

    public TextViewVipFont(Context context) {
        super(context);
    }

    public TextViewVipFont(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewVipFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTypeface(Typeface tf) {
       // setTextSize();
        if (isInEditMode()) {
            super.setTypeface(tf);
            return;
        } else {
            super.setTypeface(MyApplication.getInstance().getVipNumberFont());
        }
    }
}
