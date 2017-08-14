package com.ailicai.app.widget.psd;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ailicai.app.R;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by Allen on 2017/5/7.
 * 自定义支付密码输入框
 */

public class PayPsdInputView extends EditText {

    private static final int GRID_SIZE = 48;
    private final static int psdType_weChat = 0;
    private final static int psdType_bottomLine = 1;
    private final static int psdType_grid = 2;
    private static final int DEFAULT_TEXTSIZE = 20;
    /**
     * 格子的颜色
     */
    private static final int DEFAULT_BORDERGRI_DCOLOR = 0xFFbdbdbd;
    private static final int DEFAULT_BORDERGRI_SELECTED_DCOLOR = 0xFF212121;
    private static final int DEFAULT_GRID_TEXT_DCOLOR = 0xFF212121;
    private int mTextSize = DEFAULT_TEXTSIZE;
    private Context mContext;
    /**
     * 第一个圆开始绘制的圆心坐标
     */
    private float startX;
    private float startY;
    private float cX;
    /**
     * 实心圆的半径
     */
    private int radius = 10;
    /**
     * view的高度
     */
    private int height;
    private int width;
    /**
     * 当前输入密码位数
     */
    private int textLength = 0;
    private int bottomLineLength;
    /**
     * 最大输入位数
     */
    private int maxCount = 6;
    /**
     * 圆的颜色   默认BLACK
     */
    private int circleColor = Color.BLACK;

    private int gridTextColor = DEFAULT_GRID_TEXT_DCOLOR;
    /**
     * 底部线的颜色   默认GRAY
     */
    private int bottomLineColor = Color.GRAY;
    /**
     * 分割线的颜色
     */
    private int borderColor = Color.GRAY;
    /**
     * 分割线的画笔
     */
    private Paint borderPaint;
    private int borderGridColor = DEFAULT_BORDERGRI_DCOLOR;
    private int borderGridSelectedColor = DEFAULT_BORDERGRI_SELECTED_DCOLOR;
    /**
     * 格子画笔
     */
    private Paint gridPaint;
    private Paint gridSelectedPaint;
    /**
     * 分割线开始的坐标x
     */
    private int divideLineWStartX;
    /**
     * 分割线的宽度  默认2
     */
    private int divideLineWidth = 2;
    /**
     * 竖直分割线的颜色
     */
    private int divideLineColor = Color.GRAY;
    private RectF rectF = new RectF();
    /**
     * 格子
     */
    private RectF gridRectF = new RectF();
    private int psdType = 0;
    /**
     * 矩形边框的圆角
     */
    private int rectAngle = 0;
    private int rectGridAngle = 0;
    /**
     * 竖直分割线的画笔
     */
    private Paint divideLinePaint;
    /**
     * 圆的画笔
     */
    private Paint circlePaint;
    private Paint gridTextPaint;
    /**
     * 底部线的画笔
     */
    private Paint bottomLinePaint;

    /**
     * 需要对比的密码  一般为上次输入的
     */
    private String mComparePassword = null;

    /**
     * 光标View
     */
    private View cursorV;

    private OnPasswordChangedListener mListener;

    public PayPsdInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        getAtt(attrs);
        initPaint();

        this.setBackgroundColor(Color.TRANSPARENT);
        this.setCursorVisible(false);
        this.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxCount)});

    }

    private void getAtt(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.PayPsdInputView);
        maxCount = typedArray.getInt(R.styleable.PayPsdInputView_maxCount, maxCount);
        circleColor = typedArray.getColor(R.styleable.PayPsdInputView_circleColor, circleColor);
        bottomLineColor = typedArray.getColor(R.styleable.PayPsdInputView_bottomLineColor, bottomLineColor);
        radius = typedArray.getDimensionPixelOffset(R.styleable.PayPsdInputView_radius, radius);

        divideLineWidth = typedArray.getDimensionPixelSize(R.styleable.PayPsdInputView_divideLineWidth, divideLineWidth);
        divideLineColor = typedArray.getColor(R.styleable.PayPsdInputView_divideLineColor, divideLineColor);
        psdType = typedArray.getInt(R.styleable.PayPsdInputView_psdType, psdType);
        rectAngle = typedArray.getDimensionPixelOffset(R.styleable.PayPsdInputView_rectAngle, rectAngle);
        rectGridAngle = typedArray.getDimensionPixelOffset(R.styleable.PayPsdInputView_rectGridAngle, rectGridAngle);

        typedArray.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //initViews();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        cursorV = inflater.inflate(R.layout.cursor, (ViewGroup) this.getParent(), false);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {

        gridTextPaint = getPaint(3, Paint.Style.FILL, gridTextColor);

        circlePaint = getPaint(5, Paint.Style.FILL, circleColor);

        bottomLinePaint = getPaint(2, Paint.Style.FILL, bottomLineColor);

        borderPaint = getPaint(3, Paint.Style.STROKE, borderColor);

        divideLinePaint = getPaint(divideLineWidth, Paint.Style.FILL, borderColor);

        gridPaint = getPaint(3, Paint.Style.STROKE, borderGridColor);
        gridSelectedPaint = getPaint(3, Paint.Style.STROKE, borderGridSelectedColor);

    }

    /**
     * 设置画笔
     *
     * @param strokeWidth 画笔宽度
     * @param style       画笔风格
     * @param color       画笔颜色
     * @return
     */
    private Paint getPaint(int strokeWidth, Paint.Style style, int color) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(style);
        paint.setColor(color);
        paint.setAntiAlias(true);

        return paint;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;

        divideLineWStartX = w / maxCount;

        startX = w / maxCount / 2;
        startY = h / 2;

        bottomLineLength = w / (maxCount + 2);

        rectF.set(0, 0, width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //不删除的画会默认绘制输入的文字
        //super.onDraw(canvas);

        switch (psdType) {
            case psdType_weChat:
                drawWeChatBorder(canvas);
                drawPsdCircle(canvas);
                break;
            case psdType_bottomLine:
                drawBottomBorder(canvas);
                drawPsdCircle(canvas);
                break;
            case psdType_grid:
                drawGridBorder(canvas);
                drawPsdText(canvas);
                break;
        }


    }


    /**
     * 画微信支付密码的样式
     *
     * @param canvas
     */
    private void drawWeChatBorder(Canvas canvas) {

        canvas.drawRoundRect(rectF, rectAngle, rectAngle, borderPaint);

        for (int i = 0; i < maxCount - 1; i++) {
            canvas.drawLine((i + 1) * divideLineWStartX,
                    0,
                    (i + 1) * divideLineWStartX,
                    height,
                    divideLinePaint);
        }

    }


    /**
     * 画格子
     *
     * @param canvas
     */
    private void drawGridBorder(Canvas canvas) {
        for (int i = 0; i < maxCount; i++) {
            cX = startX + i * 2 * startX;
            Paint pt = gridPaint;
            if (i == textLength) {
                pt = gridSelectedPaint;
                mListener.showCursor(cX, height / 2);
            }
            gridRectF.set(cX - DisplayUtils.dip2px(mContext, GRID_SIZE) / 2,
                    (height - DisplayUtils.dip2px(mContext, GRID_SIZE)) / 2, cX + DisplayUtils.dip2px(mContext, GRID_SIZE) / 2,
                    DisplayUtils.dip2px(mContext, GRID_SIZE));
            canvas.drawRoundRect(gridRectF, rectGridAngle, rectGridAngle, pt);
        }
    }

    /**
     * 画底部显示的分割线
     *
     * @param canvas
     */
    private void drawBottomBorder(Canvas canvas) {

        for (int i = 0; i < maxCount; i++) {
            cX = startX + i * 2 * startX;
            canvas.drawLine(cX - bottomLineLength / 2,
                    height,
                    cX + bottomLineLength / 2,
                    height, bottomLinePaint);
        }
    }

    /**
     * 画密码实心圆
     *
     * @param canvas
     */
    private void drawPsdCircle(Canvas canvas) {
        for (int i = 0; i < textLength; i++) {
            canvas.drawCircle(startX + i * 2 * startX,
                    startY,
                    radius,
                    circlePaint);
        }
    }

    /**
     * 画输入的数值
     *
     * @param canvas
     */
    private void drawPsdText(Canvas canvas) {
        for (int i = 0; i < textLength; i++) {
            int size = DisplayUtils.dip2px(mContext, mTextSize);
            gridTextPaint.setTextSize(size);
            gridTextPaint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = gridTextPaint.getFontMetricsInt();
            float baseline = (DisplayUtils.dip2px(mContext, GRID_SIZE) + (height - DisplayUtils.dip2px(mContext, GRID_SIZE)) / 2 - fontMetrics.bottom - fontMetrics.top) / 2;
            canvas.drawText(getPasswordString().charAt(i) + "", startX + i * 2 * startX, baseline, gridTextPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        textLength = text.toString().length();
        notifyTextChanged();
        invalidate();
    }

    private void notifyTextChanged() {
        if (mListener == null)
            return;

        String currentPsw = getPasswordString();
        mListener.onTextChanged(currentPsw);

        if (mComparePassword != null && textLength == maxCount) {
            //密码比较
            if (TextUtils.equals(mComparePassword, getPasswordString())) {
                mListener.onEqual(getPasswordString());
            } else {
                mListener.onDifference();
            }
        } else {
            if (textLength == maxCount)
                mListener.onInputFinish(currentPsw);
        }
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);

        //保证光标始终在最后
        if (selStart == selEnd) {
            setSelection(getText().length());
        }
    }

    /**
     * 获取输入的密码
     *
     * @return
     */
    public String getPasswordString() {
        return getText().toString().trim();
    }

    public void setPasswordString(String password) {
        clearPassword();
        if (TextUtils.isEmpty(password))
            return;
        setText(password);
    }

    public void clearPassword() {
        setText("");
    }

    public void setComparePassword(String comparePassword, OnPasswordChangedListener listener) {
        mComparePassword = comparePassword;
        mListener = listener;
    }

    /**
     * 密码比较、输入完成监听
     */
    public interface OnPasswordChangedListener {
        void onDifference();

        void onEqual(String psd);

        void onTextChanged(String psw);

        void onInputFinish(String psw);

        void showCursor(float x, float y);
    }
}
