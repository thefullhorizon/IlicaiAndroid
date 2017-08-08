package com.ailicai.app.widget.gestruelock;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

/**
 * 手势指示器
 * Created by jeme on 2016/10/28 0028.
 */
public class GestureLockIndicator extends GestureLockViewContent {
    private static final String TAG = "GestureLockIndicator";

    public GestureLockIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLockIndicator(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle,true);
    }

    /***
     * 设置指示器的值
     */
    public void setIndictorAnswer(List<Integer> choose){
        if(mGestureLockViews == null){
            return;
        }
        for(GestureLockView lockView : mGestureLockViews){
            lockView.setMode(GestureLockView.STATUS_NO_FINGER,true);
            if(choose == null){
                //如果被选中的为空，则循环将所有的圆点置为无触摸的状态
                continue;
            }
            for(int id : choose){
                if(id == lockView.getId()){
                    lockView.setMode(GestureLockView.STATUS_FINGER_ON,true);
                }
            }
        }
    }
}
