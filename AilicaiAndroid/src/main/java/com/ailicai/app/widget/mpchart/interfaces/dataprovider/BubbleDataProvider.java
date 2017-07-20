package com.ailicai.app.widget.mpchart.interfaces.dataprovider;


import com.ailicai.app.widget.mpchart.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
