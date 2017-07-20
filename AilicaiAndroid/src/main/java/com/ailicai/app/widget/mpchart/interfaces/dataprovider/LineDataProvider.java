package com.ailicai.app.widget.mpchart.interfaces.dataprovider;


import com.ailicai.app.widget.mpchart.components.YAxis;
import com.ailicai.app.widget.mpchart.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
