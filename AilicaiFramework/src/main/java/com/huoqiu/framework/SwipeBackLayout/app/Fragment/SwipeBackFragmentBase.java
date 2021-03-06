package com.huoqiu.framework.SwipeBackLayout.app.Fragment;

import com.huoqiu.framework.SwipeBackLayout.TSwipeBackLayout;

/**
 * @author XiongWei
 */
public interface SwipeBackFragmentBase {

    TSwipeBackLayout getSwipeBackLayout();

    void setSwipeBackEnable(boolean enable);

    /**
     * Scroll out contentView and remove the fragment
     */
    void scrollToRemoveFragment();

}
