package com.ailicai.app.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.ailicai.app.model.bean.AssetPieBean;

import java.util.List;

/**
 * Created by nanshan on 7/17/2017.
 */

public class AssetPie extends View{

    private List<AssetPieBean> data;
    private String defaultColor = "#e6e6e6";

    private Paint mPaint;//饼状画笔
    private Paint mTextPaint; // 文字画笔
    private static final int DEFAULT_RADIUS = 200;
    private int mRadius = DEFAULT_RADIUS;
    private float animatedValue;
    private RectF oval;
    private String totalAsset;

    private int number = 0;
    private int index = 0;

    public List<AssetPieBean> getData() {
        return data;
    }

    public void setData(List<AssetPieBean> data) {
        this.data = data;
        for (int i = 0 ;i < data.size(); i++){
            if(data.get(i).getAmount() != 0.0){
                number ++;
                index = i;
            }
        }
    }

    public int getmRadius() {
        return mRadius;
    }

    public void setmRadius(int mRadius) {
        this.mRadius = mRadius;
    }

    public String getTotalAsset() {
        return totalAsset;
    }

    public void setTotalAsset(String totalAsset) {
        this.totalAsset = totalAsset;
    }

    private float getAnimatedValue() {
        return animatedValue;
    }

    private void setAnimatedValue(float animatedValue) {
        this.animatedValue = animatedValue;
    }

    public AssetPie(Context context) {
        this(context, null, 0);

    }

    public AssetPie(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AssetPie(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wideSize = MeasureSpec.getSize(widthMeasureSpec);
        int wideMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width, height;
        if (wideMode == MeasureSpec.EXACTLY) { //精确值 或matchParent
            width = wideSize;
        } else {
            width = mRadius * 2 + getPaddingLeft() + getPaddingRight();
            if (wideMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, wideSize);
            }

        }

        if (heightMode == MeasureSpec.EXACTLY) { //精确值 或matchParent
            height = heightSize;
        } else {
            height = mRadius * 2 + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }

        }
        setMeasuredDimension(width, height);
        mRadius = (int) (Math.min(width - getPaddingLeft() - getPaddingRight(),
                height - getPaddingTop() - getPaddingBottom()) * 1.0f / 2);
        oval = new RectF(-mRadius, -mRadius, mRadius, mRadius);
    }

    @Override
    protected void onDraw(Canvas mCanvas) {
        super.onDraw(mCanvas);

        mCanvas.translate((getWidth() + getPaddingLeft() - getPaddingRight()) / 2, (getHeight() + getPaddingTop() - getPaddingBottom()) / 2);
        drawTorus(mCanvas);
        if (totalAsset !=null ){
            drawText(mCanvas, totalAsset, 0, 0, mTextPaint);
        }else{
            drawText(mCanvas,"0.00" , 0, 0, mTextPaint);
        }

    }


    private void drawTorus(final Canvas mCanvas) {

        //画背景环
        if (!"0.00".equals(totalAsset) && data != null && data.size() > 0){
            float currentAngle = 0.0f;
            if (number == 1) {
                int num = (int)data.get(index).getAmount();
                float needDrawAngle = num * 1.0f / (int)Double.parseDouble(totalAsset.replace(",","")) * 360;
                if (Math.min(needDrawAngle, animatedValue - currentAngle) >= 0) {
                    mPaint.setColor(Color.parseColor(data.get(index).getColor()));
                    mCanvas.drawArc(oval, currentAngle, Math.min(needDrawAngle, animatedValue - currentAngle), true, mPaint);
                }
                currentAngle = currentAngle + needDrawAngle;
            }else {
                for (int i = 0 ; i < data.size(); i++){
                    int num = (int)data.get(i).getAmount();
                    if(num != 0){
                        float needDrawAngle = num * 1.0f / (int)Double.parseDouble(totalAsset.replace(",","")) * 360;
                        if (Math.min(needDrawAngle, animatedValue - currentAngle) >= 0) {
                            mPaint.setColor(Color.parseColor(data.get(i).getColor()));
                            mCanvas.drawArc(oval, currentAngle, Math.min(needDrawAngle - dp2px(1), animatedValue - currentAngle), true, mPaint);
                        }
                        currentAngle = currentAngle + needDrawAngle;
                    }
                }
            }

        }else {
            mPaint.setColor(Color.parseColor(defaultColor));
            mCanvas.drawArc(oval, 0, 360, true, mPaint);
        }
        //画前景圆
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(255);
        mCanvas.drawCircle(0, 0, mRadius  - dp2px(30), mPaint);
    }

    //画中间文字
    private void drawText(Canvas mCanvas, String text, float x, float y, Paint mPaint) {
        Rect rect = new Rect();
        //up
        String textTemp = "总资产（元）";
        mTextPaint.setTextSize(sp2px(12));
        mTextPaint.setColor(Color.parseColor("#757575"));
        mTextPaint.getTextBounds(textTemp, 0, textTemp.length(), rect);
        mCanvas.drawText(textTemp, x, y  -  rect.height(), mTextPaint);

        //down
        mTextPaint.setTextSize(sp2px(24));
        mTextPaint.setColor(Color.parseColor("#212121"));
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        mCanvas.drawText(text, x, y + rect.height(), mTextPaint);

    }

    public void startDraw() {
        if (data != null && totalAsset != null) {
            initAnimator();
        }
    }

    private void initAnimator() {
        ValueAnimator anim = ValueAnimator.ofFloat(0, 360);
        anim.setDuration(1000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatedValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        anim.start();
    }

    /**
     * dp 2 px
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }

}
