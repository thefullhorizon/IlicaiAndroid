package com.ailicai.app.widget.mpchart.data.realm.implementation;

import com.ailicai.app.widget.mpchart.data.LineData;
import com.ailicai.app.widget.mpchart.data.realm.base.RealmUtils;
import com.ailicai.app.widget.mpchart.interfaces.datasets.ILineDataSet;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Philipp Jahoda on 19/12/15.
 */
public class RealmLineData extends LineData {

    public RealmLineData(RealmResults<? extends RealmObject> result, String xValuesField, List<ILineDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
