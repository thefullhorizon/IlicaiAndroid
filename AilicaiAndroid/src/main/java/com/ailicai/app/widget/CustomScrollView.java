package com.ailicai.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/*
 * ScrollView并没有实现滚动监听，所以我们必须自行实现对ScrollView的监听
 */
public class CustomScrollView extends ScrollView {
    /**
     * ScrollView正在向上滑动
     */
    public static final int SCROLL_UP = 0x01;

    /**
     * ScrollView正在向下滑动
     */
    public static final int SCROLL_DOWN = 0x10;

    /**
     * 最小的滑动距离
     */
    private static final int SCROLLLIMIT = 10;

    private ScrollViewListener onScrollListener;

    public CustomScrollView(Context context) {
        super(context, null);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置滚动接口
     *
     * @param onScrollListener
     */
    public void setOnScrollListener(ScrollViewListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.onScrollChanged(this, l, t, oldl, oldt);
        }

        if (oldt > t && oldt - t > SCROLLLIMIT) {// 向下
            if (onScrollListener != null)
                onScrollListener.scrollOritention(SCROLL_DOWN);
        } else if (oldt < t && t - oldt > SCROLLLIMIT) {// 向上
            if (onScrollListener != null)
                onScrollListener.scrollOritention(SCROLL_UP);
        }
    }

    public interface ScrollViewListener {

        void onScrollChanged(CustomScrollView scrollView, int x, int y, int oldx, int oldy);

        void scrollOritention(int oritention);

    }
}
