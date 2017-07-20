package com.ailicai.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;


/**
 * 更多TextView(默认固定行数,点击展开所有行)
 * created by liyanan on 16/04/12
 */
public class MoreTextView extends LinearLayout {
    protected TextView contentView;
    protected TextView expandView;

    protected int textColor;
    protected float textSize;
    private int defaultTextLine;//默认展示行数
    private int defaultTextColor = Color.BLACK;
    private int defaultTextSize = 12;
    private int defaultLine = 3;//未设置默认展示行数为3
    private String text;
    boolean isExpand;//是否展开

    public MoreTextView(Context context) {
        this(context, null);
    }

    public MoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithAttrs(context, attrs);
        initView();
        addListener();
    }

    protected void initWithAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MoreTextView);
        textColor = a.getColor(R.styleable.MoreTextView_textColor,
                defaultTextColor);
        textSize = a.getDimensionPixelSize(R.styleable.MoreTextView_textSize, defaultTextSize);
        text = a.getString(R.styleable.MoreTextView_text);
        defaultTextLine = a.getInt(R.styleable.MoreTextView_defaultTextLine, defaultLine);
        a.recycle();
    }

    protected void initView() {
        setOrientation(HORIZONTAL);
        contentView = new TextView(getContext());
        contentView.setTextColor(textColor);
        contentView.setLineSpacing(0, 1.5f);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        contentView.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
        contentView.setText(text);
        addView(contentView);
        expandView = new TextViewTF(getContext());
        expandView.setText(R.string.chevous_down);
        expandView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        expandView.setTextColor(Color.parseColor("#b0b0b0"));
        LayoutParams llp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        expandView.setLayoutParams(llp);
        addView(expandView);
        setContentHeight();
    }

    protected void setContentHeight() {
        if (isExpand) {
            //展示全部高度
            contentView.setHeight(contentView.getLineHeight() * contentView.getLineCount());
            expandView.setVisibility(View.INVISIBLE);
        } else {
            //展示默认行的高度
            contentView.setHeight(contentView.getLineHeight() * defaultTextLine);
            expandView.setVisibility(View.VISIBLE);
        }
    }

    protected void addListener() {
        setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isExpand) {
                    return;
                }
                isExpand = !isExpand;
                contentView.clearAnimation();
                final int deltaValue;
                final int startValue = contentView.getHeight();
                int durationMillis = 360;
                if (isExpand) {
                    deltaValue = contentView.getLineHeight() * contentView.getLineCount() - startValue;
                } else {
                    deltaValue = contentView.getLineHeight() * defaultTextLine - startValue;
                }
                Animation animation = new Animation() {
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        contentView.setHeight((int) (startValue + deltaValue * interpolatedTime));
                    }
                };
                animation.setDuration(durationMillis);
                contentView.startAnimation(animation);
                expandView.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 设置文字
     *
     * @param text
     */
    public void setText(String text) {
        contentView.setText(text);
        setContentHeight();
    }

}
