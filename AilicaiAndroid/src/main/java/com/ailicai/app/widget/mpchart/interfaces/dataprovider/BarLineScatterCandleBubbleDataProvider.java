package com.ailicai.app.widget.mpchart.interfaces.dataprovider;


import com.ailicai.app.widget.mpchart.components.YAxis.AxisDependency;
import com.ailicai.app.widget.mpchart.data.BarLineScatterCandleBubbleData;
import com.ailicai.app.widget.mpchart.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    int getMaxVisibleCount();
    boolean isInverted(AxisDependency axis);
    
    int getLowestVisibleXIndex();
    int getHighestVisibleXIndex();

    BarLineScatterCandleBubbleData getData();
}
