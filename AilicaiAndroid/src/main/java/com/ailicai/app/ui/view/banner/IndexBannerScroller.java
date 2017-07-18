package com.ailicai.app.ui.view.banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by wulianghuan on 2016/7/20.
 */
public class IndexBannerScroller extends Scroller {
    private int animTime = 300;

    public IndexBannerScroller(Context context) {
        super(context);
    }

    public IndexBannerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, animTime);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, animTime);
    }

    public void setmDuration(int animTime) {
        this.animTime = animTime;
    }
}
