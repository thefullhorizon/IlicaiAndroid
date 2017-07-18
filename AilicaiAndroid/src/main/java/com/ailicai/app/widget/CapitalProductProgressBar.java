package com.ailicai.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ailicai.app.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/18.
 */
public class CapitalProductProgressBar extends LinearLayout {
    private ProgressBar rightBar;
    private ProgressBar leftBar;
    private ImageView endPoint;
    private ImageView middlePoint;
    private TextView mTitleLeft;
    private TextView mTitleMiddle;
    private TextView mTitleRight;
    private TextView mValueLeft;
    private TextView mValueMiddle;
    private TextView mValueRight;

    public CapitalProductProgressBar(Context context) {
        this(context,null);
    }

    public CapitalProductProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CapitalProductProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.capital_product_detail_time_line,this);
        rightBar = (ProgressBar) findViewById(R.id.right_progress_bar);
        leftBar = (ProgressBar) findViewById(R.id.left_progress_bar);
        middlePoint = (ImageView) findViewById(R.id.middle_point);
        endPoint = (ImageView) findViewById(R.id.end_point);
        mTitleLeft = (TextView) findViewById(R.id.left_date_title);
        mTitleMiddle = (TextView) findViewById(R.id.middle_date_title);
        mTitleRight = (TextView) findViewById(R.id.right_date_title);
        mValueLeft = (TextView) findViewById(R.id.left_date);
        mValueMiddle = (TextView) findViewById(R.id.middle_date);
        mValueRight = (TextView) findViewById(R.id.right_date);
    }
    public void updateState(ArrayList<String> titles, ArrayList<String> values, int leftMax , int leftCurrent, int rightMax, int rightCurrent){
        if ((titles == null || titles.size() < 3)||(values == null ||values.size() < 3)) return;
        mTitleLeft.setText(titles.get(0));
        mTitleMiddle.setText(titles.get(1));
        mTitleRight.setText(titles.get(2));
        mValueLeft.setText(values.get(0));
        mValueMiddle.setText(values.get(1));
        mValueRight.setText(values.get(2));
        if (leftMax == 0){
            leftBar.setMax(1);
            leftBar.setProgress(1);
        }else {
            leftBar.setMax(leftMax);
            leftBar.setProgress(leftCurrent);
        }
        rightBar.setMax(rightMax);
        rightBar.setProgress(rightCurrent);

        if (leftCurrent >= leftMax){
            middlePoint.setImageResource(R.drawable.step_point_complete);
        }else {
            middlePoint.setImageResource(R.drawable.step_point_normal);
        }

        if (rightCurrent >=rightMax){
            endPoint.setImageResource(R.drawable.step_point_complete);
        }else {
            endPoint.setImageResource(R.drawable.step_point_normal);
        }
    }
}
