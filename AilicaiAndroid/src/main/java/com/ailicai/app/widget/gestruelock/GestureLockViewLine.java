package com.ailicai.app.widget.gestruelock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * 手势划线层
 * Created by jeme on 2017/8/5
 */
public class GestureLockViewLine extends View {

    private Paint mPaint;

    private Path mPath;
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

    /***
     * 是否允许划线，是否达到划线要求
     */
    private boolean mCanDrawLine = false;
    /**
     *  默认显示轨迹
     */
    private boolean mShowPath = true;

    public GestureLockViewLine(Context context) {
        this(context,null);
    }

    public GestureLockViewLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLockViewLine(Context context, AttributeSet attrs,
                               int defStyle) {
        super(context, attrs, defStyle);

        // 初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setAlpha(255);
        mPath = new Path();

    }

    public void setPathStrokeWidth(int width){
        //设置画笔的宽度为GestureLockView的内圆直径稍微小点（不喜欢的话，随便设）
        mPaint.setStrokeWidth(width * 0.02f);
    }

    /***
     * 是否显示路径
     */
    public void setShowPath(boolean showPath) {
        this.mShowPath = showPath;
    }

    /***
     * 设置手指滑动时的线条路径
     */
    public void setPath(float x,float y,boolean isLink){
        if(isLink){
            mPath.lineTo(x,y);
        }else{
            mPath.moveTo(x,y);
        }
    }

    /***
     * 清空划线选项
     */
    public void reset(){
        mCanDrawLine = false;
        mPath.reset();
        invalidate();
    }

    /***
     * 设置划线的位置参数
     * @param lastX 上一次的位置X
     * @param lastY 上一次的位置Y
     * @param currentX 当前位置X
     * @param currentY 当前位置Y
     * @param canDraw 是否达到划线要求
     */
    public void setLineLocation(float lastX,float lastY,float currentX,float currentY,boolean canDraw){
        mLastPathX = lastX;
        mLastPathY = lastY;
        mTmpTargetX = currentX;
        mTmpTargetY = currentY;
        mCanDrawLine = canDraw;
        invalidate();
    }

    /***
     * 设置线的颜色
     */
    public void setViewColor(int color) {
        mPaint.setColor(color);
        mPaint.setAlpha(255);
    }


    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!mShowPath) {
            return;
        }
        //绘制GestureLockView间的连线
        if (mPath != null) {
            canvas.drawPath(mPath, mPaint);
        }

        //绘制指引线
        if (mCanDrawLine) {
            if (mLastPathX != 0 && mLastPathY != 0) {
                canvas.drawLine(mLastPathX, mLastPathY, mTmpTargetX,
                        mTmpTargetY, mPaint);
            }
        }

    }

}
