package com.ailicai.app.widget.mpchart.interfaces.dataprovider;


import com.ailicai.app.widget.mpchart.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
