
package com.ailicai.app.widget.mpchart.jobs;

import android.view.View;

import com.ailicai.app.widget.mpchart.utils.Transformer;
import com.ailicai.app.widget.mpchart.utils.ViewPortHandler;


/**
 * Created by Philipp Jahoda on 19/02/16.
 */
public class MoveViewJob extends ViewPortJob {

    public MoveViewJob(ViewPortHandler viewPortHandler, float xValue, float yValue, Transformer trans, View v) {
        super(viewPortHandler, xValue, yValue, trans, v);
    }

    @Override
    public void run() {

        pts[0] = xValue;
        pts[1] = yValue;

        mTrans.pointValuesToPixel(pts);
        mViewPortHandler.centerViewPort(pts, view);
    }
}
