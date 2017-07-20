package com.ailicai.app.widget.mpchart.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.widget.mpchart.charts.Chart;
import com.ailicai.app.widget.mpchart.data.Entry;
import com.ailicai.app.widget.mpchart.highlight.Highlight;

import java.lang.ref.WeakReference;

/**
 * View that can be displayed when selecting values in the chart. Extend this class to provide custom layouts for your
 * markers.
 *
 * @author Philipp Jahoda
 */
public abstract class MarkerView extends RelativeLayout {

    private WeakReference<Chart> mWeakChart;
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public MarkerView(Context context, int layoutResource) {
        super(context);
        setupLayoutResource(layoutResource);
    }

    /**
     * Sets the layout resource for a custom MarkerView.
     *
     * @param layoutResource
     */
    private void setupLayoutResource(int layoutResource) {

        View inflated = LayoutInflater.from(getContext()).inflate(layoutResource, this);

        inflated.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        inflated.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        // measure(getWidth(), getHeight());
        inflated.layout(0, 0, inflated.getMeasuredWidth(), inflated.getMeasuredHeight());
    }

    public void setChartView(Chart chart) {
        mWeakChart = new WeakReference<>(chart);
    }

    public Chart getChartView() {
        return mWeakChart == null ? null : mWeakChart.get();
    }

    /**
     * Draws the MarkerView on the given position on the screen with the given Canvas object.
     *
     * @param canvas
     * @param posx
     * @param posy
     */
    public void draw(Canvas canvas, float posx, float posy) {

        // take offsets into consideration
        PointF offset = getOffsetForDrawingAtPoint(posx,posy);

        // translate to the correct position and draw
        canvas.translate(posx + offset.x, posy + offset.y);
        draw(canvas);
        canvas.translate(-posx, -posy);
    }

    public PointF getOffsetForDrawingAtPoint(float posX, float posY) {

        PointF point = new PointF();

        point.x = getXOffset(posX);
        point.y = getYOffset(posY);
        
        Chart chart = getChartView();

        float width = getWidth();
        float height = getHeight();

        LogUtil.i("MarkView","posX " + posX
                + " point.x " + point.x
                + " getMarkViewAlongWithHighlight " + getMarkViewAlongWithHighlight()
                + " width " +width
                + " chart.getWidth() " + chart.getWidth());

        if (posX + point.x < 0) {
            point.x = - posX + 12;
        } else if (chart != null && posX + width + point.x > chart.getWidth()) {
            point.x = chart.getWidth() - posX - width - 12;
        }

        LogUtil.i("MarkView","posY " + posY
                + " point.y " + point.y
                + " getMarkViewAlongWithHighlight " + getMarkViewAlongWithHighlight()
                + " height " +height
                + " chart.getHeight() " + chart.getHeight());

        if (getMarkViewAlongWithHighlight()) {
            point.y = - getYOffset(posY);
        } else {
            if (posY + point.y < 0) {
                point.y = -posY;
            } else if (chart != null && posY + height + point.y > chart.getHeight()) {
                point.y = chart.getHeight() - posY - height;
            }
        }

        return point;
    }

    /**
     * This method enables a specified custom MarkerView to update it's content everytime the MarkerView is redrawn.
     *
     * @param e         The Entry the MarkerView belongs to. This can also be any subclass of Entry, like BarEntry or
     *                  CandleEntry, simply cast it at runtime.
     * @param highlight the highlight object contains information about the highlighted value such as it's dataset-index, the
     *                  selected range or stack-index (only stacked bar entries).
     */
    public abstract void refreshContent(Entry e, Highlight highlight);

    /**
     * Use this to return the desired offset you wish the MarkerView to have on the x-axis. By returning -(getWidth() /
     * 2) you will center the MarkerView horizontally.
     *
     * @param xpos the position on the x-axis in pixels where the marker is drawn
     * @return
     */
    public abstract int getXOffset(float xpos);

    /**
     * Use this to return the desired position offset you wish the MarkerView to have on the y-axis. By returning
     * -getHeight() you will cause the MarkerView to be above the selected value.
     *
     * @param ypos the position on the y-axis in pixels where the marker is drawn
     * @return
     */
    public abstract int getYOffset(float ypos);

    public abstract boolean getMarkViewAlongWithHighlight();
}
