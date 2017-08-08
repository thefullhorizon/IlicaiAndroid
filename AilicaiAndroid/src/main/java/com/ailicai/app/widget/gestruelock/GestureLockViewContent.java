package com.ailicai.app.widget.gestruelock;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ailicai.app.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class GestureLockViewContent extends RelativeLayout{
    private static final String TAG = "GestureLockViewGroup";

    public static final int STATUS_SETTING = 0;//设置模式
    public static final int STATUS_VERIFY = 1;//验证模式

    @IntDef({STATUS_SETTING,STATUS_VERIFY})
    @Retention(RetentionPolicy.SOURCE)
    @interface GESTURE_STATUS{}
    /**
     * 保存所有的GestureLockView
     */
    protected GestureLockView[] mGestureLockViews;
    /**
     * 每个边上的GestureLockView的个数
     */
    private int mCount = 4;

    // 圈/间距
    protected int marginRate = 2;
    /**
     * 每个GestureLockView中间的间距 设置为：mGestureLockViewWidth * 25%
     */
    private int mMarginBetweenLockView = -1;
    /**
     * GestureLockView的边长 4 * mWidth / ( 5 * mCount + 1 )
     */
    private int mGestureLockViewWidth;

    /**
     * GestureLockView无手指触摸的状态下内圆的颜色
     */
    private int mNoFingerInnerCircleColor = 0xFF939090;
    /**
     * GestureLockView无手指触摸的状态下外圆的颜色
     */
    private int mNoFingerOuterCircleColor = 0xFFE0DBDB;
    /**
     * GestureLockView手指触摸的状态下内圆和外圆的颜色
     */
    private int mFingerOnColor = 0xFF378FC9;
    /**
     * GestureLockView手指抬起的状态下内圆和外圆的颜色
     */
    private int mFingerUpColor = 0xFFFF0000;
    private int mFingerWrongColor = 0xFFFF0000;

    /***
     * 默认手势点背景是透明色
     */
    private int mGestureLockViewBg = 0xFFFFFFFF;

    /**
     * 宽度
     */
    private int mWidth;
    /**
     * 最大尝试次数
     */
    private int mTryTimes = 4;
    /***
     * 手势密码输入正确时是否线上小三角
     */
    private boolean mIsShowArrowWhenSuccess = false;
    /***
     * 是否作为指示器使用
     */
    private boolean mIsIndicator = false;
    /***
     * 手势连线层
     */
    private GestureLockViewLine mGestureLockLineView;
    private OnGestureLockViewListener mOnGestureLockViewListener;
    // 默认false
//    private boolean isFirstSet = false;

    /**
     * 指引线的开始位置x
     */
    private float mLastPathX;
    /**
     * 指引线的开始位置y
     */
    private float mLastPathY;
    /**
     * 指引下的结束位置
     */
    private float mTmpTargetX;
    private float mTmpTargetY;
    /**
     * 保存用户选中的GestureLockView的id
     */
    private List<Integer> mChoose = new ArrayList<>();
    /**
     * 存储答案
     */
//    private List<Integer> mAnswer = new ArrayList<>();
    private String firstSetAnswerStr = "";
    private int mGestureLockViewLength;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    // 记录答案是否正确
    private boolean isAnswerRight = false;
    // 默认显示轨迹
    private boolean showPath = true;
    private @GESTURE_STATUS int mGestureStatus = STATUS_VERIFY;
    private GestureLockUtils mGestureLockUtils;

    /***
     * 手势回调
     */
    public interface OnGestureLockViewListener {
        /**
         * 是否匹配
         */
        void onGestureEvent(boolean matched);

        /**
         * 超过尝试次数
         */
        void onUnmatchedExceedBoundary();

        /**
         * 是否初始设置密码
         */
        void onFirstSetPattern(boolean patternOk);
    }


    public GestureLockViewContent(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLockViewContent(Context context, AttributeSet attrs,
                                  int defStyle) {
        this(context, attrs, defStyle,false);


    }
    public GestureLockViewContent(Context context, AttributeSet attrs,
                                  int defStyle, boolean isIndicator) {
        super(context, attrs, defStyle);
        initAttrs(context,attrs);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                reset();
                invalidate();
            }
        };
        mGestureLockViewLength = mCount * mCount;
        this.mIsIndicator = isIndicator;
        //手势指示器不需要划线层
        if(!mIsIndicator){
            mGestureLockLineView = new GestureLockViewLine(getContext());
            mGestureLockLineView.setBackgroundColor(0x00000000);//连线层设置为透明色
            addView(mGestureLockLineView,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        mGestureLockUtils = new GestureLockUtils();
    }

    private void initAttrs(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.GestureLockViewContent);
        if(a == null){
            return;
        }
        mNoFingerInnerCircleColor = a.getColor(R.styleable.GestureLockViewContent_color_no_finger_inner_circle,
                mNoFingerInnerCircleColor);
        mNoFingerOuterCircleColor = a.getColor(R.styleable.GestureLockViewContent_color_no_finger_outer_circle,
                mNoFingerOuterCircleColor);
        mFingerOnColor = a.getColor(R.styleable.GestureLockViewContent_color_finger_on, mFingerOnColor);
        mFingerUpColor = a.getColor(R.styleable.GestureLockViewContent_color_finger_right, mFingerOnColor);//默认使用按下的颜色
        mFingerWrongColor = a.getColor(R.styleable.GestureLockViewContent_color_finger_wrong, mFingerWrongColor);
        mGestureLockViewBg = a.getColor(R.styleable.GestureLockViewContent_color_background,mGestureLockViewBg);

        mIsShowArrowWhenSuccess = a.getBoolean(R.styleable.GestureLockViewContent_show_arrow_when_success,true);
        mTryTimes = a.getInt(R.styleable.GestureLockViewContent_tryTimes, 5);
        mCount = a.getInt(R.styleable.GestureLockViewContent_count, 3);

        mMarginBetweenLockView = (int)a.getDimension(R.styleable.GestureLockViewContent_margin_lock_view,-1);
        mGestureLockViewWidth = (int)a.getDimension(R.styleable.GestureLockViewContent_lock_view_size,-1);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);

        // 初始化mGestureLockViews
        if (mGestureLockViews == null) {
            mGestureLockViews = new GestureLockView[mGestureLockViewLength];
            // 计算每个GestureLockView的宽度
            mGestureLockViewWidth = mGestureLockViewWidth == -1 ? (int) (marginRate * mWidth * 1.0f / ((marginRate + 1) * mCount + 1)) : mGestureLockViewWidth;
            // 计算每个GestureLockView的间距
            mMarginBetweenLockView = mMarginBetweenLockView == -1 ? mGestureLockViewWidth / marginRate : mMarginBetweenLockView;
            if(mGestureLockLineView != null) {
                mGestureLockLineView.setPathStrokeWidth(mGestureLockViewWidth);
            }


            for (int i = 0; i < mGestureLockViewLength; i++) {
                //初始化每个GestureLockView
                mGestureLockViews[i] = new GestureLockView(getContext(),
                        mNoFingerInnerCircleColor, mNoFingerOuterCircleColor,
                        mFingerOnColor, mFingerUpColor,mFingerWrongColor,mGestureLockViewBg,mIsIndicator);
                mGestureLockViews[i].setId(i + 1);
                //设置参数，主要是定位GestureLockView间的位置
                LayoutParams lockerParams = new LayoutParams(
                        mGestureLockViewWidth, mGestureLockViewWidth);

                // 不是每行的第一个，则设置位置为前一个的右边
                if (i % mCount != 0) {
                    lockerParams.addRule(RelativeLayout.RIGHT_OF,
                            mGestureLockViews[i - 1].getId());
                }
                // 从第二行开始，设置为上一行同一位置View的下面
                if (i > mCount - 1) {
                    lockerParams.addRule(RelativeLayout.BELOW,
                            mGestureLockViews[i - mCount].getId());
                }
                //设置右下左上的边距
                int rightMargin = mMarginBetweenLockView;
                int bottomMargin = mMarginBetweenLockView;
                int leftMagin = 0;
                int topMargin = 0;

                //每个View都有右外边距和底外边距 第一行的有上外边距 第一列的有左外边距
                if (i >= 0 && i < mCount){// 第一行
                    topMargin = mMarginBetweenLockView;
                }
                if (i % mCount == 0){// 第一列
                    leftMagin = mMarginBetweenLockView;
                }

                lockerParams.setMargins(leftMagin, topMargin, rightMargin,
                        bottomMargin);
                mGestureLockViews[i].setMode(GestureLockView.STATUS_NO_FINGER, showPath);
                addView(mGestureLockViews[i], lockerParams);
            }

            mGestureLockUtils.setLockViews(mGestureLockViews);
        }
    }

    /***
     * 设置手势模式
     */
    public void setStatus(@GESTURE_STATUS int status){
        mGestureStatus = status;
        //如果是第一次设置，则必须设置显示路径
        setShowPath(mGestureStatus == STATUS_SETTING);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return !(mIsIndicator || mTryTimes == 0 || isAnswerRight) && super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 重置
                reset();
                break;
            case MotionEvent.ACTION_MOVE:
                // 初始化画笔为蓝色
                mGestureLockLineView.setViewColor(mFingerOnColor);
                GestureLockView child = mGestureLockUtils.getChildIdByPos(x, y,mGestureLockViewWidth);

                if (child != null) {
                    int cId = child.getId();
                    if (!mChoose.contains(cId)) {
                        // 循环加入中间点
                        int subId = mGestureLockUtils.checkChoose(cId,mChoose,mCount);
                        Log.e(TAG, "SubId:" + subId);
                        while (subId != -1) {
                            // 1、这部分代码和以下 2 部分基本一样，可以抽离出一个方法
                            mChoose.add(subId);
                            GestureLockView subChild = mGestureLockViews[subId - 1];
                            subChild.setMode(GestureLockView.STATUS_FINGER_ON, showPath);
                            // 设置指引线的起点
                            mLastPathX = (subChild.getLeft() + subChild.getRight())/ 2 ;
                            mLastPathY = (subChild.getTop() + subChild.getBottom()) / 2;
                            // 非第一个，将两者使用线连上
                            mGestureLockLineView.setPath(mLastPathX,mLastPathY,true);
                            // 继续循环
                            subId = mGestureLockUtils.checkChoose(cId,mChoose,mCount);
                        }

                        // 2、中间点加入完成，继续加入当前选择的点
                        mChoose.add(cId);
                        child.setMode(GestureLockView.STATUS_FINGER_ON, showPath);
                        // 设置指引线的起点
                        mLastPathX = (child.getLeft() + child.getRight())/ 2;
                        mLastPathY = (child.getTop() + child.getBottom()) / 2;

                        mGestureLockLineView.setPath(mLastPathX,mLastPathY,mChoose.size() != 1);

                    }
                }

                // 指引线的终点
                mTmpTargetX = x;
                mTmpTargetY = y;
                break;
            case MotionEvent.ACTION_UP:
                // 回调是否成功
                boolean patternOk = false;
                if (mOnGestureLockViewListener != null && mChoose.size() > 0) {
                    //如果是初次设置图案，不需要checkAnswer()，但需要setAnswer()
                    if (mGestureStatus == STATUS_SETTING) {
                        isAnswerRight = false;
                        patternOk = !(mChoose.size() < 4);
                        if (patternOk) {
                            mGestureLockUtils.setAnswer(mChoose);
//                            setAnswer(mChoose);
                            firstSetAnswerStr = mGestureLockUtils.listToStr(mChoose);
                        }
                        mGestureLockLineView.setViewColor(patternOk ? mFingerUpColor:mFingerWrongColor);
                        mOnGestureLockViewListener.onFirstSetPattern(patternOk);
                    } else {
                        isAnswerRight = mGestureLockUtils.checkAnswer(mChoose);
                        if (!isAnswerRight) {
                            this.mTryTimes--;
                        }
                        mGestureLockLineView.setViewColor(isAnswerRight?mFingerUpColor:mFingerWrongColor);
                        mOnGestureLockViewListener.onGestureEvent(isAnswerRight);
                        if (this.mTryTimes == 0) {
                            // 剩余次数为0时进行一些处理（在使用该手势密码的activity中覆写）
                            mOnGestureLockViewListener.onUnmatchedExceedBoundary();
                        }
                    }
                }
                // 将终点设置位置为起点，即取消指引线
                mTmpTargetX = mLastPathX;
                mTmpTargetY = mLastPathY;

                // 改变子元素的状态为UP
                changeItemMode(mChoose,patternOk|| isAnswerRight);

                // 计算每个元素中箭头需要旋转的角度
                for (int i = 0; i + 1 < mChoose.size(); i++) {
                    int childId = mChoose.get(i);
                    int nextChildId = mChoose.get(i + 1);

                    GestureLockView startChild = (GestureLockView) findViewById(childId);
                    GestureLockView nextChild = (GestureLockView) findViewById(nextChildId);

                    int dx = nextChild.getLeft() - startChild.getLeft();
                    int dy = nextChild.getTop() - startChild.getTop();
                    // 计算角度
                    int angle = (int) Math.toDegrees(Math.atan2(dy, dx)) + 90;
                    startChild.setArrowDegree((((mGestureStatus == STATUS_SETTING && patternOk) || isAnswerRight) && !mIsShowArrowWhenSuccess) ? -1 : angle);
                }
                if(mGestureStatus == STATUS_SETTING && patternOk){
                    mGestureStatus = STATUS_VERIFY;//如果在设置模式，答案匹配时，下一步需要修改为验证模式
                }
                mGestureLockLineView.setShowPath(needShowPath());
                delayReset();// 调用

                break;
        }
        mGestureLockLineView.setLineLocation(mLastPathX,mLastPathY,mTmpTargetX,mTmpTargetY,mChoose.size() > 0);
        invalidate();
        return true;
    }

    /**
     * 做一些必要的重置
     */
    public void reset() {
        if (mHandler != null && mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        mGestureLockLineView.reset();
        mChoose.clear();
        mLastPathX = mLastPathY = 0;

        for (GestureLockView gestureLockView : mGestureLockViews) {
            gestureLockView.setMode(GestureLockView.STATUS_NO_FINGER, showPath);
            gestureLockView.setArrowDegree(-1);//-1时不会绘制三角
        }

    }

    private void delayReset() {
        mHandler.postDelayed(mRunnable, 1000);
    }

    private boolean needShowPath(){
        return showPath || mGestureStatus == STATUS_SETTING || !isAnswerRight;
    }

    private void changeItemMode(List<Integer> chooses,boolean isPattern) {
        for (GestureLockView gestureLockView : mGestureLockViews) {
            if (chooses.contains(gestureLockView.getId())) {
                gestureLockView.setIsAnswerRight(isPattern);// 调用
                gestureLockView.setMode(GestureLockView.STATUS_FINGER_UP, showPath);
            }
        }
    }

    public void setShowPath(boolean showPath) {
        this.showPath = showPath || mGestureStatus == STATUS_SETTING;
    }

    /**
     * 设置答案
     */
    public void setAnswer(String answer) {
        mGestureLockUtils.setAnswer(answer);
    }

    public int getTryTimes() {
        return mTryTimes;
    }


    public void reInit(){
        reset();
        isAnswerRight = false;// 重置
//        mAnswer.clear();
//        firstSetAnswerStr = "";
        mGestureStatus = STATUS_SETTING;
        /*if(mGestureLockLineView != null) {
            mGestureLockLineView.reset();
        }*/
        invalidate();
    }

    /**
     * 设置回调接口
     */
    public void setOnGestureLockViewListener(OnGestureLockViewListener listener) {
        mOnGestureLockViewListener = listener;
    }

    /**
     * 设置最大实验次数
     *
     * @param boundary
     */
    public void setUnMatchExceedBoundary(int boundary) {
        mTryTimes = boundary;
    }

    public List<Integer> getChoose(){
        return mChoose;
    }

    public String getChooseStr() {
        return mGestureLockUtils.listToStr(mChoose);
    }


}
