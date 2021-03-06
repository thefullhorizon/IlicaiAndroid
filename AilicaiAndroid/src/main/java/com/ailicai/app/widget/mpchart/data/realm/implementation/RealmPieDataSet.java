package com.ailicai.app.widget.mpchart.data.realm.implementation;

import com.ailicai.app.widget.mpchart.data.Entry;
import com.ailicai.app.widget.mpchart.data.PieDataSet;
import com.ailicai.app.widget.mpchart.data.realm.base.RealmBaseDataSet;
import com.ailicai.app.widget.mpchart.interfaces.datasets.IPieDataSet;
import com.ailicai.app.widget.mpchart.utils.Utils;

import io.realm.DynamicRealmObject;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Philipp Jahoda on 07/11/15.
 */
public class RealmPieDataSet<T extends RealmObject> extends RealmBaseDataSet<T, Entry> implements IPieDataSet {

    /**
     * the space in pixels between the chart-slices, default 0f
     */
    private float mSliceSpace = 0f;

    /**
     * indicates the selection distance of a pie slice
     */
    private float mShift = 18f;

    private PieDataSet.ValuePosition mXValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE;
    private PieDataSet.ValuePosition mYValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE;
    private int mValueLineColor = 0xff000000;
    private float mValueLineWidth = 1.0f;
    private float mValueLinePart1OffsetPercentage = 75.f;
    private float mValueLinePart1Length = 0.3f;
    private float mValueLinePart2Length = 0.4f;
    private boolean mValueLineVariableLength = true;

    /**
     * Constructor for creating a PieDataSet with realm data.
     *
     * @param result       the queried results from the realm database
     * @param yValuesField the name of the field in your data object that represents the y-value
     */
    public RealmPieDataSet(RealmResults<T> result, String yValuesField) {
        super(result, yValuesField);

        build(this.results);
        calcMinMax(0, results.size());
    }

    /**
     * Constructor for creating a PieDataSet with realm data.
     *
     * @param result       the queried results from the realm database
     * @param yValuesField the name of the field in your data object that represents the y-value
     * @param xIndexField  the name of the field in your data object that represents the x-index
     */
    public RealmPieDataSet(RealmResults<T> result, String yValuesField, String xIndexField) {
        super(result, yValuesField, xIndexField);

        build(this.results);
        calcMinMax(0, results.size());
    }

    @Override
    public void build(RealmResults<T> results) {

        if (mIndexField == null) { // x-index not available

            int xIndex = 0;

            for (T object : results) {

                DynamicRealmObject dynamicObject = new DynamicRealmObject(object);
                mValues.add(new Entry(dynamicObject.getFloat(mValuesField), xIndex));
                xIndex++;
            }

        } else {

            for (T object : results) {

                DynamicRealmObject dynamicObject = new DynamicRealmObject(object);
                mValues.add(new Entry(dynamicObject.getFloat(mValuesField), dynamicObject.getInt(mIndexField)));
            }
        }
    }

    /**
     * Sets the space that is left out between the piechart-slices in dp.
     * Default: 0 --> no space, maximum 20f
     *
     * @param spaceDp
     */
    public void setSliceSpace(float spaceDp) {

        if (spaceDp > 20)
            spaceDp = 20f;
        if (spaceDp < 0)
            spaceDp = 0f;

        mSliceSpace = Utils.convertDpToPixel(spaceDp);
    }

    @Override
    public float getSliceSpace() {
        return mSliceSpace;
    }

    /**
     * sets the distance the highlighted piechart-slice of this DataSet is
     * "shifted" away from the center of the chart, default 12f
     *
     * @param shift
     */
    public void setSelectionShift(float shift) {
        mShift = Utils.convertDpToPixel(shift);
    }

    @Override
    public float getSelectionShift() {
        return mShift;
    }

    @Override
    public PieDataSet.ValuePosition getXValuePosition()
    {
        return mXValuePosition;
    }

    public void setXValuePosition(PieDataSet.ValuePosition xValuePosition)
    {
        this.mXValuePosition = xValuePosition;
    }

    @Override
    public PieDataSet.ValuePosition getYValuePosition()
    {
        return mYValuePosition;
    }

    public void setYValuePosition(PieDataSet.ValuePosition yValuePosition)
    {
        this.mYValuePosition = yValuePosition;
    }

    /** When valuePosition is OutsideSlice, indicates line color */
    @Override
    public int getValueLineColor()
    {
        return mValueLineColor;
    }

    public void setValueLineColor(int valueLineColor)
    {
        this.mValueLineColor = valueLineColor;
    }

    /** When valuePosition is OutsideSlice, indicates line width */
    @Override
    public float getValueLineWidth()
    {
        return mValueLineWidth;
    }

    public void setValueLineWidth(float valueLineWidth)
    {
        this.mValueLineWidth = valueLineWidth;
    }

    /** When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size */
    @Override
    public float getValueLinePart1OffsetPercentage()
    {
        return mValueLinePart1OffsetPercentage;
    }

    public void setValueLinePart1OffsetPercentage(float valueLinePart1OffsetPercentage)
    {
        this.mValueLinePart1OffsetPercentage = valueLinePart1OffsetPercentage;
    }

    /** When valuePosition is OutsideSlice, indicates length of first half of the line */
    @Override
    public float getValueLinePart1Length()
    {
        return mValueLinePart1Length;
    }

    public void setValueLinePart1Length(float valueLinePart1Length)
    {
        this.mValueLinePart1Length = valueLinePart1Length;
    }

    /** When valuePosition is OutsideSlice, indicates length of second half of the line */
    @Override
    public float getValueLinePart2Length()
    {
        return mValueLinePart2Length;
    }

    public void setValueLinePart2Length(float valueLinePart2Length)
    {
        this.mValueLinePart2Length = valueLinePart2Length;
    }

    /** When valuePosition is OutsideSlice, this allows variable line length */
    @Override
    public boolean isValueLineVariableLength()
    {
        return mValueLineVariableLength;
    }

    public void setValueLineVariableLength(boolean valueLineVariableLength)
    {
        this.mValueLineVariableLength = valueLineVariableLength;
    }
}
