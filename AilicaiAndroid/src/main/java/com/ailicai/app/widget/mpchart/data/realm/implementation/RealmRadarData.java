package com.ailicai.app.widget.mpchart.data.realm.implementation;


import com.ailicai.app.widget.mpchart.data.RadarData;
import com.ailicai.app.widget.mpchart.data.realm.base.RealmUtils;
import com.ailicai.app.widget.mpchart.interfaces.datasets.IRadarDataSet;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Philipp Jahoda on 19/12/15.
 */
public class RealmRadarData extends RadarData {

    public RealmRadarData(RealmResults<? extends RealmObject> result, String xValuesField, List<IRadarDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
