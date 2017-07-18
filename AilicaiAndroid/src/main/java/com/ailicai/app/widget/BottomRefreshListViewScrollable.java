
package com.ailicai.app.widget;

import android.view.ViewGroup;

/**
 * Created by Gerry on 2016/10/27.
 */

public interface BottomRefreshListViewScrollable {
    @Deprecated
    void setScrollViewCallbacks(BottomRefreshListViewCallbacks listener);

    void addScrollViewCallbacks(BottomRefreshListViewCallbacks listener);

    void removeScrollViewCallbacks(BottomRefreshListViewCallbacks listener);

    void clearScrollViewCallbacks();

    void scrollVerticallyTo(int y);

    int getCurrentScrollY();

    void setTouchInterceptionViewGroup(ViewGroup viewGroup);
}
