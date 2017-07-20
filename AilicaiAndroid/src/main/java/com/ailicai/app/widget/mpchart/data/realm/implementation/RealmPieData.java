package com.ailicai.app.widget.mpchart.data.realm.implementation;


import com.ailicai.app.widget.mpchart.data.PieData;
import com.ailicai.app.widget.mpchart.data.realm.base.RealmUtils;
import com.ailicai.app.widget.mpchart.interfaces.datasets.IPieDataSet;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Philipp Jahoda on 19/12/15.
 */
public class RealmPieData extends PieData {

    public RealmPieData(RealmResults<? extends RealmObject> result, String xValuesField, IPieDataSet dataSet) {
        super(RealmUtils.toXVals(result, xValuesField), dataSet);
    }
}
