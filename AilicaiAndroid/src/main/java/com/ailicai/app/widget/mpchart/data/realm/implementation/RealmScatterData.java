package com.ailicai.app.widget.mpchart.data.realm.implementation;


import com.ailicai.app.widget.mpchart.data.ScatterData;
import com.ailicai.app.widget.mpchart.data.realm.base.RealmUtils;
import com.ailicai.app.widget.mpchart.interfaces.datasets.IScatterDataSet;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Philipp Jahoda on 19/12/15.
 */
public class RealmScatterData extends ScatterData {

    public RealmScatterData(RealmResults<? extends RealmObject> result, String xValuesField, List<IScatterDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
