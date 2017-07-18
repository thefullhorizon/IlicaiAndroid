package com.ailicai.app.widget.bottomrefreshlistview;


/**
 * Created by Gerry on 2016/10/27.
 */

public interface BottomRefreshListViewCallbacks {
    void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging);

    void onDownMotionEvent();

    void onUpOrCancelMotionEvent(BottomRefreshListViewScrollState scrollState);
}
