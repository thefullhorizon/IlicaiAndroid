package com.ailicai.app.widget.mpchart.data.realm.implementation;


import com.ailicai.app.widget.mpchart.data.CandleData;
import com.ailicai.app.widget.mpchart.data.realm.base.RealmUtils;
import com.ailicai.app.widget.mpchart.interfaces.datasets.ICandleDataSet;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Philipp Jahoda on 19/12/15.
 */
public class RealmCandleData extends CandleData {

    public RealmCandleData(RealmResults<? extends RealmObject> result, String xValuesField, List<ICandleDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
