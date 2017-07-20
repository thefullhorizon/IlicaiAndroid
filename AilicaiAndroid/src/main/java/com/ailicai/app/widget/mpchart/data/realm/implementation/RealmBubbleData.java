package com.ailicai.app.widget.mpchart.data.realm.implementation;

import com.ailicai.app.widget.mpchart.data.BubbleData;
import com.ailicai.app.widget.mpchart.data.realm.base.RealmUtils;
import com.ailicai.app.widget.mpchart.interfaces.datasets.IBubbleDataSet;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Philipp Jahoda on 19/12/15.
 */
public class RealmBubbleData extends BubbleData {

    public RealmBubbleData(RealmResults<? extends RealmObject> result, String xValuesField, List<IBubbleDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
