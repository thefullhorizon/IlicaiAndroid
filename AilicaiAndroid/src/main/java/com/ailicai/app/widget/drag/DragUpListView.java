package com.ailicai.app.widget.drag;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailicai.app.R;


/**
 * 向上拖动ListView与DragLayout配合使用
 * Created by liyanan on 16/4/6.
 */
public class DragUpListView extends ListView implements AbsListView.OnScrollListener {
    boolean allowDragTop = true; // 如果是true，则允许拖动至顶部的上一页
    float downY = 0;
    boolean needConsumeTouch = true; // 是否需要承包touch事件，needConsumeTouch一旦被定性，则不会更改

    public DragUpListView(Context arg0) {
        this(arg0, null);
    }

    public DragUpListView(Context arg0, AttributeSet attrs) {
        this(arg0, attrs, 0);
    }

    public DragUpListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downY = ev.getRawY();
            needConsumeTouch = true; // 默认情况下，listView内部的滚动优先，默认情况下由该listView去消费touch事件
            allowDragTop = isAtTop();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (!needConsumeTouch) {
                // 在最顶端且向上拉了，则这个touch事件交给父类去处理
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            } else if (allowDragTop) {
                // needConsumeTouch尚未被定性，此处给其定性
                // 允许拖动到底部的下一页，而且又向上拖动了，就将touch事件交给父view
                if (ev.getRawY() - downY > 2) {
                    // flag设置，由父类去消费
                    needConsumeTouch = false;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
            }
        }

        // 通知父view是否要处理touch事件
        getParent().requestDisallowInterceptTouchEvent(needConsumeTouch);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断listView是否在顶部
     *
     * @return 是否在顶部
     */
    private boolean isAtTop() {
        boolean resultValue = false;
        int childNum = getChildCount();
        if (childNum == 0) {
            // 没有child，肯定在顶部
            resultValue = true;
        } else {
            if (getFirstVisiblePosition() == 0) {
                // 根据第一个childView来判定是否在顶部
                View firstView = getChildAt(0);
                if (Math.abs(firstView.getTop() - getTop()) < 2) {
                    resultValue = true;
                }
            }
        }

        return resultValue;
    }

    /**
     * 以下代码为添加加载更多功能
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    private static final String TAG = "BottomFreashListView";
    /**
     * mOnScrollListener:滚动监听
     */
    private OnScrollListener mOnScrollListener;

    /**
     * mFooterDividerView:底部分割线
     */
    private View mFooterDividerView;
    /**
     * mFooterView:底部view
     */
    private RelativeLayout mFooterView;
    /**
     * mProgressLayout:加载布局
     */
    private LinearLayout mProgressLayout;
    /**
     * mProgressBarLoadMore:加载框
     */
    private View mProgressBarLoadMore;
    /**
     * mLoadComplete:结束文字
     */
    private TextView mLoadComplete;
    /**
     * mLoadingText:加载中文字
     */
    private TextView mLoadingText;
    /**
     * mOnLoadMoreListener:加载监听
     */
    private OnLoadMoreListener mOnLoadMoreListener;
    /**
     * bIsLoadingMore:是否正在加载下一页
     */
    private boolean bIsLoadingMore = false;
    /**
     * bIsLoaded:全部加载完毕
     */
    private boolean bIsLoaded = false;
    /**
     * nCurrentScrollState:滚动状态
     */
    private int nCurrentScrollState;
    /**
     * oldFirstVisibleItem:
     */
    private int oldFirstVisibleItem = 0;
    /**
     * itemClickListener:item点击监听
     */
    private OnItemClickListener itemClickListener;
    private boolean showLine;

    private OnItemClickListener selfItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position != parent.getCount() - 1) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(parent, view, position, id);
                }
            }
        }
    };

    /**
     * 功能描述:初始化
     */
    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFooterView = (RelativeLayout) inflater.inflate(R.layout.load_more_footer, this, false);
        mProgressLayout = (LinearLayout) mFooterView.findViewById(R.id.loading_more_layout);
        mProgressBarLoadMore = mFooterView.findViewById(R.id.load_more_progressBar);
        mLoadComplete = (TextView) mFooterView.findViewById(R.id.load_complete);
        mLoadingText = (TextView) mFooterView.findViewById(R.id.load_text);
        mFooterView.setClickable(false);
        if (showLine) {
            View view = new View(getContext());
            view.setBackgroundColor(getResources().getColor(R.color.color_black));
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
            mFooterDividerView = view;
            addFooterView(view, null, false);
        }
        addFooterView(mFooterView, null, false);
        setFooterDividersEnabled(false);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            setSelector(context.getResources().getDrawable(R.drawable.bg_black_a10_selector));
        }
        setCacheColorHint(Color.TRANSPARENT);
        super.setOnScrollListener(this);
        super.setOnItemClickListener(selfItemClickListener);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        mOnScrollListener = listener;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        if (mOnLoadMoreListener != null && !bIsLoaded) {
            if (visibleItemCount == totalItemCount) {
                mProgressBarLoadMore.setVisibility(View.GONE);
                mProgressLayout.setVisibility(View.GONE);
                return;
            }
            if (firstVisibleItem > this.oldFirstVisibleItem) {
                boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount - 4;
                if (!bIsLoadingMore && loadMore && nCurrentScrollState != SCROLL_STATE_IDLE) {
                    mProgressBarLoadMore.setVisibility(View.VISIBLE);
                    mProgressLayout.setVisibility(View.VISIBLE);
                    bIsLoadingMore = true;
                    onLoadMore();
                }
            }
            oldFirstVisibleItem = firstVisibleItem;
        } else {
            if (mProgressBarLoadMore != null) {
                mProgressBarLoadMore.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        nCurrentScrollState = scrollState;
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    /**
     * 功能描述:加载更多
     * <p/>
     * <pre>
     *     yrguo:   2012-11-22      新建
     * </pre>
     */
    public void onLoadMore() {
        Log.d(TAG, "onLoadMore");
        if (mOnLoadMoreListener != null) {
            mOnLoadMoreListener.onLoadMore();
        }
    }

    /**
     * 功能描述:状态回至
     * <p/>
     * <pre>
     *     yrguo:   2012-11-22      新建
     * </pre>
     */
    public void resetAll() {
        bIsLoadingMore = false;
        bIsLoaded = false;
        mLoadComplete.setVisibility(View.GONE);
    }


    /**
     * 功能描述:单次查询结束
     * <p/>
     * <pre>
     *     yrguo:   2012-11-22      新建
     * </pre>
     */
    public void onLoadMoreComplete() {
        bIsLoadingMore = false;
        mProgressBarLoadMore.setVisibility(View.GONE);
        mProgressLayout.setVisibility(View.GONE);
    }

    /**
     * 功能描述:已到最后一页
     * <p/>
     * <pre>
     *     yrguo:   2012-11-22      新建
     * </pre>
     */
    public void onAllLoaded() {
        onLoadMoreComplete();
        bIsLoaded = true;
        mLoadComplete.setVisibility(View.GONE);
    }

    /**
     * 功能描述:设置结束文字
     * <p/>
     * <pre>
     *     yrguo:   2012-11-22      新建
     * </pre>
     *
     * @param text
     */
    public void setPromptText(String text) {
        if (mLoadComplete != null) {
            mLoadComplete.setText(text);
        }
    }

    /**
     * 功能描述:设置加载中文字
     * <p/>
     * <pre>
     *     yrguo:   2012-11-22      新建
     * </pre>
     *
     * @param text
     */
    public void setLoadingText(String text) {
        if (mLoadingText != null) {
            mLoadingText.setText(text);
        }
    }

    public void addLoadMoreFootView() {
        addFooterView(mFooterView, null, false);
    }


    public void setDividerHeight() {
        if (mFooterDividerView != null) {
            LayoutParams lp = (LayoutParams) mFooterDividerView.getLayoutParams();
            if (lp != null) {
                lp.height = 4;
                mFooterDividerView.setLayoutParams(lp);
            }
        }
    }

    /**
     * 功能描述:隐藏分割线
     * <p/>
     * <pre>
     *     yrguo:   2012-11-22      新建
     * </pre>
     */
    public void hideFooterDivider() {
        if (mFooterDividerView != null && mFooterDividerView.isShown()) {
            // mFooterDividerView.setVisibility(View.GONE);
            removeFooterView(mFooterDividerView);
        }
    }
}
