package com.ailicai.app.widget.mpchart.data.realm.implementation;


import com.ailicai.app.widget.mpchart.data.BarData;
import com.ailicai.app.widget.mpchart.data.realm.base.RealmUtils;
import com.ailicai.app.widget.mpchart.interfaces.datasets.IBarDataSet;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Philipp Jahoda on 19/12/15.
 */
public class RealmBarData extends BarData {

    public RealmBarData(RealmResults<? extends RealmObject> result, String xValuesField, List<IBarDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
