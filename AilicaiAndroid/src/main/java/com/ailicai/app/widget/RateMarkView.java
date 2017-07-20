package com.ailicai.app.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.widget.mpchart.components.MarkerView;
import com.ailicai.app.widget.mpchart.data.Entry;
import com.ailicai.app.widget.mpchart.highlight.Highlight;


/**
 * Created by duo.chen on 2017/1/4
 */

public class RateMarkView extends MarkerView {

    TextView textContentRate;
    View arrow;
    float[] yList;
    updateContentListener listener;

    public RateMarkView(Context context, int wallet_rate_mark_view_layout, float[] yList) {
        super(context, wallet_rate_mark_view_layout);
        textContentRate = (TextView) findViewById(R.id.tvcontent_rate);
        arrow = findViewById(R.id.arrow_image);
        this.yList = yList;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e != null) {
            updateContent(e.getXIndex());
        }
    }

    public void setListener(updateContentListener listener) {
        this.listener = listener;
    }

    private void updateContent(int xIndex) {
        if (null != yList && yList.length >= xIndex) {

            textContentRate.setText(String.format("%.3f%%",yList[xIndex]));
            if (xIndex == 0) {
                textContentRate.setBackground(ContextCompat.getDrawable(getContext(), R.drawable
                        .rate_mark_left_bg));
                arrow.setBackground(ContextCompat.getDrawable(getContext(), R.drawable
                        .mark_rate_left));
            } else if ((yList.length - 1) == xIndex) {
                textContentRate.setBackground(ContextCompat.getDrawable(getContext(), R.drawable
                        .rate_mark_right_bg));
                arrow.setBackground(ContextCompat.getDrawable(getContext(), R.drawable
                        .mark_rate_right));
            } else {
                textContentRate.setBackground(ContextCompat.getDrawable(getContext(), R.drawable
                        .rate_mark_middle_bg));
                arrow.setBackground(ContextCompat.getDrawable(getContext(), R.drawable
                        .mark_rate_mid));
            }

            if(null != listener) {
                listener.updateContent(xIndex);
            }
        }
    }

    @Override
    public int getXOffset(float xpos) {
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        return -getHeight() - 16;
    }

    @Override
    public boolean getMarkViewAlongWithHighlight() {
        return false;
    }

    public interface updateContentListener {
        void updateContent(int index);
    }
}
