package com.ailicai.app.widget.drag;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * 用于解决DragDownScrollView的下拉刷新
 * Created by liyanan on 16/4/7.
 */
public class DragSwipeRefreshLayout extends SwipeRefreshLayout {
    private DragLayout dragLayout;
    private int scrollViewId;

    public DragSwipeRefreshLayout(Context context) {
        super(context);
    }

    public DragSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDragView(DragLayout dragLayout, int scrollViewId) {
        this.dragLayout = dragLayout;
        this.scrollViewId = scrollViewId;
    }

    @Override
    public boolean canChildScrollUp() {
        if (dragLayout.getCurrentPage() == 1) {
            //处于第二页
            return true;
        } else {
            //处于第一页
            DragDownScrollView scrollView = (DragDownScrollView) dragLayout.findViewById(scrollViewId);
            if (scrollView.getScrollY() > 0) {
                return true;
            }
        }
        return super.canChildScrollUp();
    }
}
