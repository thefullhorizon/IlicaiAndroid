package com.ailicai.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailicai.app.R;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

/**
 * 底部加载更多、可设置回调坚听滚动距离
 */
public class BottomRefreshListView extends ListView implements BottomRefreshListViewScrollable {

    private static final String TAG = "BottomRefreshListView";
    private OnScrollListener mOnScrollListener;
    private View mFooterDividerView;
    private RelativeLayout mFooterView;
    private LinearLayout mProgressLayout;
    private View mProgressBarLoadMore;
    private TextView mLoadComplete;
    private TextView mLoadingText;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean bIsLoadingMore = false;
    private boolean bIsLoaded = false;
    private int nCurrentScrollState;
    private int oldFirstVisibleItem = 0;
    private OnItemClickListener itemClickListener;
    private TypedArray a;
    private boolean showLine;

    // Fields that should be saved onSaveInstanceState
    private int mPrevFirstVisiblePosition;
    private int mPrevFirstVisibleChildHeight = -1;
    private int mPrevScrolledChildrenHeight;
    private int mPrevScrollY;
    private int mScrollY;
    private SparseIntArray mChildrenHeights;

    // Fields that don't need to be saved onSaveInstanceState
    private BottomRefreshListViewCallbacks mCallbacks;
    private List<BottomRefreshListViewCallbacks> mCallbackCollection;
    private BottomRefreshListViewScrollState mScrollState;
    private boolean mFirstScroll;
    private boolean mDragging;
    private boolean mIntercepted;
    private MotionEvent mPrevMoveEvent;
    private ViewGroup mTouchInterceptionViewGroup;

    private OnScrollListener mOriginalScrollListener;

    private OnScrollListener mScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mOriginalScrollListener != null) {
                mOriginalScrollListener.onScrollStateChanged(view, scrollState);
            }

            nCurrentScrollState = scrollState;
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrollStateChanged(view, scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (mOriginalScrollListener != null) {
                mOriginalScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }

            doOnScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            // AbsListView#invokeOnItemScrollListener calls onScrollChanged(0, 0, 0, 0)
            // on Android 4.0+, but Android 2.3 is not. (Android 3.0 is unknown)
            // So call it with onScrollListener.
            onScrollChanged();
        }
    };

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

    public BottomRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public BottomRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        a = context.obtainStyledAttributes(attrs, R.styleable.BottomRefreshListView/*, defStyle, R.style.CtripBottomRefreshListView*/);
        init(context);
    }

    public BottomRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        a = context.obtainStyledAttributes(attrs, R.styleable.BottomRefreshListView/*, defStyle, R.style.CtripBottomRefreshListView*/);
        init(context);
    }

    private void init(Context context) {
        mChildrenHeights = new SparseIntArray();
        super.setOnScrollListener(mScrollListener);
        super.setOnItemClickListener(selfItemClickListener);

        if (a != null) {
            showLine = a.getBoolean(R.styleable.BottomRefreshListView_show_bottom_line, false);
            a.recycle();
        }
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
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public void doOnScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        if (mOnLoadMoreListener != null && !bIsLoaded) {
            if (visibleItemCount == totalItemCount) {
                mProgressBarLoadMore.setVisibility(View.GONE);
                mProgressLayout.setVisibility(View.GONE);
            }
            if (firstVisibleItem >= this.oldFirstVisibleItem) {
                //boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount - 4;
                boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount / 2;
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

    public void onLoadMore() {
        Log.d(TAG, "onLoadMore");
        if (mOnLoadMoreListener != null) {
            mOnLoadMoreListener.onLoadMore();
        }
    }

    public void resetAll() {
        bIsLoadingMore = false;
        bIsLoaded = false;
        mLoadComplete.setVisibility(View.GONE);
    }

    public void onLoadMoreComplete() {
        bIsLoadingMore = false;
        bIsLoaded = false;
        mProgressBarLoadMore.setVisibility(View.GONE);
        mProgressLayout.setVisibility(View.GONE);
    }

    public void onAllLoaded() {
        onLoadMoreComplete();
        bIsLoaded = true;
        mLoadComplete.setVisibility(View.VISIBLE);
    }

    public boolean isAllLoaded() {
        return bIsLoaded;
    }

    public void setPromptText(String text) {
        if (mLoadComplete != null) {
            mLoadComplete.setText(text);
        }
    }

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

    public void hideFooterDivider() {
        if (mFooterDividerView != null && mFooterDividerView.isShown()) {
            // mFooterDividerView.setVisibility(View.GONE);
            removeFooterView(mFooterDividerView);
        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        BottomRefreshListView.SavedState ss = (BottomRefreshListView.SavedState) state;
        mPrevFirstVisiblePosition = ss.prevFirstVisiblePosition;
        mPrevFirstVisibleChildHeight = ss.prevFirstVisibleChildHeight;
        mPrevScrolledChildrenHeight = ss.prevScrolledChildrenHeight;
        mPrevScrollY = ss.prevScrollY;
        mScrollY = ss.scrollY;
        mChildrenHeights = ss.childrenHeights;
        super.onRestoreInstanceState(ss.getSuperState());
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        BottomRefreshListView.SavedState ss = new BottomRefreshListView.SavedState(superState);
        ss.prevFirstVisiblePosition = mPrevFirstVisiblePosition;
        ss.prevFirstVisibleChildHeight = mPrevFirstVisibleChildHeight;
        ss.prevScrolledChildrenHeight = mPrevScrolledChildrenHeight;
        ss.prevScrollY = mPrevScrollY;
        ss.scrollY = mScrollY;
        ss.childrenHeights = mChildrenHeights;
        return ss;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (hasNoCallbacks()) {
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // Whether or not motion events are consumed by children,
                // flag initializations which are related to ACTION_DOWN events should be executed.
                // Because if the ACTION_DOWN is consumed by children and only ACTION_MOVEs are
                // passed to parent (this view), the flags will be invalid.
                // Also, applications might implement initialization codes to onDownMotionEvent,
                // so call it here.
                mFirstScroll = mDragging = true;
                dispatchOnDownMotionEvent();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (hasNoCallbacks()) {
            return super.onTouchEvent(ev);
        }
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIntercepted = false;
                mDragging = false;
                dispatchOnUpOrCancelMotionEvent(mScrollState);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mPrevMoveEvent == null) {
                    mPrevMoveEvent = ev;
                }
                float diffY = ev.getY() - mPrevMoveEvent.getY();
                mPrevMoveEvent = MotionEvent.obtainNoHistory(ev);
                if (getCurrentScrollY() - diffY <= 0) {
                    // Can't scroll anymore.

                    if (mIntercepted) {
                        // Already dispatched ACTION_DOWN event to parents, so stop here.
                        return false;
                    }

                    // Apps can set the interception target other than the direct parent.
                    final ViewGroup parent;
                    if (mTouchInterceptionViewGroup == null) {
                        parent = (ViewGroup) getParent();
                    } else {
                        parent = mTouchInterceptionViewGroup;
                    }

                    // Get offset to parents. If the parent is not the direct parent,
                    // we should aggregate offsets from all of the parents.
                    float offsetX = 0;
                    float offsetY = 0;
                    for (View v = this; v != null && v != parent; ) {
                        offsetX += v.getLeft() - v.getScrollX();
                        offsetY += v.getTop() - v.getScrollY();
                        try {
                            v = (View) v.getParent();
                        } catch (ClassCastException ex) {
                            break;
                        }
                    }
                    final MotionEvent event = MotionEvent.obtainNoHistory(ev);
                    event.offsetLocation(offsetX, offsetY);

                    if (parent.onInterceptTouchEvent(event)) {
                        mIntercepted = true;

                        // If the parent wants to intercept ACTION_MOVE events,
                        // we pass ACTION_DOWN event to the parent
                        // as if these touch events just have began now.
                        event.setAction(MotionEvent.ACTION_DOWN);

                        // Return this onTouchEvent() first and set ACTION_DOWN event for parent
                        // to the queue, to keep events sequence.
                        post(new Runnable() {
                            @Override
                            public void run() {
                                parent.dispatchTouchEvent(event);
                            }
                        });
                        return false;
                    }
                    // Even when this can't be scrolled anymore,
                    // simply returning false here may cause subView's click,
                    // so delegate it to super.
                    return super.onTouchEvent(ev);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        // Don't set l to super.setOnScrollListener().
        // l receives all events through mScrollListener.
        mOriginalScrollListener = l;
        mOnScrollListener = l;
    }

    @Override
    public void setScrollViewCallbacks(BottomRefreshListViewCallbacks listener) {
        mCallbacks = listener;
    }

    @Override
    public void addScrollViewCallbacks(BottomRefreshListViewCallbacks listener) {
        if (mCallbackCollection == null) {
            mCallbackCollection = new ArrayList<>();
        }
        mCallbackCollection.add(listener);
    }

    @Override
    public void removeScrollViewCallbacks(BottomRefreshListViewCallbacks listener) {
        if (mCallbackCollection != null) {
            mCallbackCollection.remove(listener);
        }
    }

    @Override
    public void clearScrollViewCallbacks() {
        if (mCallbackCollection != null) {
            mCallbackCollection.clear();
        }
    }

    @Override
    public void setTouchInterceptionViewGroup(ViewGroup viewGroup) {
        mTouchInterceptionViewGroup = viewGroup;
    }

    @Override
    public void scrollVerticallyTo(int y) {
        View firstVisibleChild = getChildAt(0);
        if (firstVisibleChild != null) {
            int baseHeight = firstVisibleChild.getHeight();
            int position = y / baseHeight;
            setSelection(position);
        }
    }

    @Override
    public int getCurrentScrollY() {
        return mScrollY;
    }

    private void onScrollChanged() {
        if (hasNoCallbacks()) {
            return;
        }
        if (getChildCount() > 0) {
            int firstVisiblePosition = getFirstVisiblePosition();
            for (int i = getFirstVisiblePosition(), j = 0; i <= getLastVisiblePosition(); i++, j++) {
                if (mChildrenHeights.indexOfKey(i) < 0 || getChildAt(j).getHeight() != mChildrenHeights.get(i)) {
                    mChildrenHeights.put(i, getChildAt(j).getHeight());
                }
            }

            View firstVisibleChild = getChildAt(0);
            if (firstVisibleChild != null) {
                if (mPrevFirstVisiblePosition < firstVisiblePosition) {
                    // scroll down
                    int skippedChildrenHeight = 0;
                    if (firstVisiblePosition - mPrevFirstVisiblePosition != 1) {
                        for (int i = firstVisiblePosition - 1; i > mPrevFirstVisiblePosition; i--) {
                            if (0 < mChildrenHeights.indexOfKey(i)) {
                                skippedChildrenHeight += mChildrenHeights.get(i);
                            } else {
                                // Approximate each item's height to the first visible child.
                                // It may be incorrect, but without this, scrollY will be broken
                                // when scrolling from the bottom.
                                skippedChildrenHeight += firstVisibleChild.getHeight();
                            }
                        }
                    }
                    mPrevScrolledChildrenHeight += mPrevFirstVisibleChildHeight + skippedChildrenHeight;
                    mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
                } else if (firstVisiblePosition < mPrevFirstVisiblePosition) {
                    // scroll up
                    int skippedChildrenHeight = 0;
                    if (mPrevFirstVisiblePosition - firstVisiblePosition != 1) {
                        for (int i = mPrevFirstVisiblePosition - 1; i > firstVisiblePosition; i--) {
                            if (0 < mChildrenHeights.indexOfKey(i)) {
                                skippedChildrenHeight += mChildrenHeights.get(i);
                            } else {
                                // Approximate each item's height to the first visible child.
                                // It may be incorrect, but without this, scrollY will be broken
                                // when scrolling from the bottom.
                                skippedChildrenHeight += firstVisibleChild.getHeight();
                            }
                        }
                    }
                    mPrevScrolledChildrenHeight -= firstVisibleChild.getHeight() + skippedChildrenHeight;
                    mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
                } else if (firstVisiblePosition == 0) {
                    mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
                    mPrevScrolledChildrenHeight = 0;
                }
                if (mPrevFirstVisibleChildHeight < 0) {
                    mPrevFirstVisibleChildHeight = 0;
                }
                mScrollY = mPrevScrolledChildrenHeight - firstVisibleChild.getTop() +
                        firstVisiblePosition * getDividerHeight() + getPaddingTop();
                mPrevFirstVisiblePosition = firstVisiblePosition;

                dispatchOnScrollChanged(mScrollY, mFirstScroll, mDragging);
                if (mFirstScroll) {
                    mFirstScroll = false;
                }

                if (mPrevScrollY < mScrollY) {
                    mScrollState = BottomRefreshListViewScrollState.UP;
                } else if (mScrollY < mPrevScrollY) {
                    mScrollState = BottomRefreshListViewScrollState.DOWN;
                } else {
                    mScrollState = BottomRefreshListViewScrollState.STOP;
                }
                mPrevScrollY = mScrollY;
            }
        }
    }

    private void dispatchOnDownMotionEvent() {
        if (mCallbacks != null) {
            mCallbacks.onDownMotionEvent();
        }
        if (mCallbackCollection != null) {
            for (int i = 0; i < mCallbackCollection.size(); i++) {
                BottomRefreshListViewCallbacks callbacks = mCallbackCollection.get(i);
                callbacks.onDownMotionEvent();
            }
        }
    }

    private void dispatchOnScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(scrollY, firstScroll, dragging);
        }
        if (mCallbackCollection != null) {
            for (int i = 0; i < mCallbackCollection.size(); i++) {
                BottomRefreshListViewCallbacks callbacks = mCallbackCollection.get(i);
                callbacks.onScrollChanged(scrollY, firstScroll, dragging);
            }
        }
    }

    private void dispatchOnUpOrCancelMotionEvent(BottomRefreshListViewScrollState scrollState) {
        if (mCallbacks != null) {
            mCallbacks.onUpOrCancelMotionEvent(scrollState);
        }
        if (mCallbackCollection != null) {
            for (int i = 0; i < mCallbackCollection.size(); i++) {
                BottomRefreshListViewCallbacks callbacks = mCallbackCollection.get(i);
                callbacks.onUpOrCancelMotionEvent(scrollState);
            }
        }
    }

    private boolean hasNoCallbacks() {
        return mCallbacks == null && mCallbackCollection == null;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    static class SavedState extends BaseSavedState {
        int prevFirstVisiblePosition;
        int prevFirstVisibleChildHeight = -1;
        int prevScrolledChildrenHeight;
        int prevScrollY;
        int scrollY;
        SparseIntArray childrenHeights;

        /**
         * Called by onSaveInstanceState.
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Called by CREATOR.
         */
        private SavedState(Parcel in) {
            super(in);
            prevFirstVisiblePosition = in.readInt();
            prevFirstVisibleChildHeight = in.readInt();
            prevScrolledChildrenHeight = in.readInt();
            prevScrollY = in.readInt();
            scrollY = in.readInt();
            childrenHeights = new SparseIntArray();
            final int numOfChildren = in.readInt();
            if (0 < numOfChildren) {
                for (int i = 0; i < numOfChildren; i++) {
                    final int key = in.readInt();
                    final int value = in.readInt();
                    childrenHeights.put(key, value);
                }
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(prevFirstVisiblePosition);
            out.writeInt(prevFirstVisibleChildHeight);
            out.writeInt(prevScrolledChildrenHeight);
            out.writeInt(prevScrollY);
            out.writeInt(scrollY);
            final int numOfChildren = childrenHeights == null ? 0 : childrenHeights.size();
            out.writeInt(numOfChildren);
            if (0 < numOfChildren) {
                for (int i = 0; i < numOfChildren; i++) {
                    out.writeInt(childrenHeights.keyAt(i));
                    out.writeInt(childrenHeights.valueAt(i));
                }
            }
        }

        public static final Parcelable.Creator<BottomRefreshListView.SavedState> CREATOR
                = new Parcelable.Creator<BottomRefreshListView.SavedState>() {
            @Override
            public BottomRefreshListView.SavedState createFromParcel(Parcel in) {
                return new BottomRefreshListView.SavedState(in);
            }

            @Override
            public BottomRefreshListView.SavedState[] newArray(int size) {
                return new BottomRefreshListView.SavedState[size];
            }
        };
    }

    @Override
    public void addFooterView(View v) {
        //调整加载更多视图位置
        if (v != mFooterView) {
            removeFooterView(mFooterView);
            super.addFooterView(v);
            addFooterView(mFooterView);
            return;
        }
        super.addFooterView(v);
    }
}