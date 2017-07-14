package com.ailicai.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.DeviceUtil;

/**
 * 封装系统{@link android.widget.EditText}功能，并实现能够删除全部输入文字的快捷功能
 * <p>
 * 当有输入内容时，删除全部的按钮会显示；否则不会显示。
 * </p>
 *
 * @author guzy
 */
public class ManyiEditText extends RelativeLayout {

    protected static final int ID_CLEAR_BUTTON = 0x101;

    private static final int EDITTEXT_DEFAULT_STYLE = R.style.text_24_000000;
    private static final int DEFAULT_INPUT_MAX_LENGTH = 300;
    private static final int EDITTEXT_HINT_COLOR = 0x88333333;

    private EditText mEditText; // Value输入框
    private String mHintValue = ""; // hint值
    private int nEditStyle = EDITTEXT_DEFAULT_STYLE; // 输入框样式
    private int nEditBackground = 0;
    private int nEditInputType = InputType.TYPE_CLASS_TEXT; // 输入类型
    private TextWatcher mExternTextWatch = null;
    private int nEditPaddingLeft = 0;

    private int nMaxLength = 300;
    private boolean isShowEditBackGround = true;
    private ImageView mCleanImg; // 清除图标
    private OnFocusChangeInterface mFocusChangeInterface = null;

    public interface OnFocusChangeInterface {
        void onViewChange(View v, boolean hasFocus);

        void hasClearText(View v);
    }

    private OnClickListener mCleanListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            mEditText.setText("");
            showClearButton(false);
            if (null != mFocusChangeInterface) {
                mFocusChangeInterface.hasClearText(ManyiEditText.this);
            }
        }
    };

    private OnFocusChangeListener mEditorFocusChangedListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            setSelected(hasFocus);
            showClearButton(hasFocus && !TextUtils.isEmpty(mEditText.getText().toString()));
            if (null != mFocusChangeInterface) {
                mFocusChangeInterface.onViewChange(ManyiEditText.this, hasFocus);
            }
        }
    };

    private TextWatcher mTextWatch = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mExternTextWatch != null) {
                mExternTextWatch.onTextChanged(s, start, before, count);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (mExternTextWatch != null) {
                mExternTextWatch.beforeTextChanged(s, start, count, after);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            showClearButton((s.length() > 0));
            if (mExternTextWatch != null) {
                mExternTextWatch.afterTextChanged(s);
            }
        }
    };

    public ManyiEditText(Context context) {
        this(context, null);
    }

    public ManyiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFromAttributes(context, attrs);

        // 设置默认背景
        if (nEditBackground > 0) {
            // setBackgroundDrawable(getResources().getDrawable(nEditBackground));
            setBackgroundResource(nEditBackground);
        }

        // 清除按钮
        mCleanImg = new ImageView(context);
        mCleanImg.setId(ID_CLEAR_BUTTON);
        mCleanImg.setImageResource(R.drawable.input_delete);
        mCleanImg.setVisibility(View.GONE);
        mCleanImg.setOnClickListener(mCleanListener);

        LayoutParams rlp = new LayoutParams(DeviceUtil.getPixelFromDip(getResources().getDisplayMetrics(), 16), DeviceUtil.getPixelFromDip(getResources().getDisplayMetrics(), 16));
        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rlp.addRule(RelativeLayout.CENTER_VERTICAL);
        rlp.leftMargin = 2;
        rlp.rightMargin = 8;
        addView(mCleanImg, rlp);


        // 输入框
        mEditText = (EditText) LayoutInflater.from(context).inflate(R.layout.item_edittext, null);

        // 输入框不设置固定ID，防止同页面多个控件的情况下，onSaveInstanceState方法赋值覆盖
        // mEditText.setId(ID_EDITTEXT);
        if (isShowEditBackGround) {
            mEditText.setBackgroundResource(0);
        }
        mEditText.setPadding(DeviceUtil.getPixelFromDip(getContext(), nEditPaddingLeft), 0, 0, 0);
        mEditText.setGravity(Gravity.CENTER_VERTICAL);
        mEditText.setTextAppearance(getContext(), nEditStyle);
        mEditText.setHint(mHintValue);
        mEditText.setInputType(nEditInputType);
        setEditorHintColor(EDITTEXT_HINT_COLOR);
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(nMaxLength)});
        rlp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        rlp.addRule(RelativeLayout.LEFT_OF, ID_CLEAR_BUTTON);
        //rlp.addRule(RelativeLayout.CENTER_VERTICAL);

        addView(mEditText, rlp);

        mEditText.addTextChangedListener(mTextWatch);
        mEditText.setOnFocusChangeListener(mEditorFocusChangedListener);
    }

    private void initFromAttributes(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ManyiEditText);
            nEditStyle = ta.getResourceId(R.styleable.ManyiEditText_edit_appearance, EDITTEXT_DEFAULT_STYLE);
            nEditBackground = ta.getResourceId(R.styleable.ManyiEditText_edit_background, 0);
            mHintValue = ta.getString(R.styleable.ManyiEditText_edit_hint_value);
            nEditInputType = ta.getInt(R.styleable.ManyiEditText_edit_inputType, InputType.TYPE_CLASS_TEXT);
            nMaxLength = ta.getInt(R.styleable.ManyiEditText_edit_maxLength, 300);
            isShowEditBackGround = ta.getBoolean(R.styleable.ManyiEditText_edit_ground, true);
            nEditPaddingLeft = ta.getInt(R.styleable.ManyiEditText_editText_paddingLeft, 0);
            ta.recycle();
        } else {
            nEditStyle = EDITTEXT_DEFAULT_STYLE;
            mHintValue = null;
            nEditInputType = InputType.TYPE_CLASS_TEXT;
            nMaxLength = 300;
        }
    }

    public void setFocusChangeInterface(OnFocusChangeInterface mFocusChangeInterface) {
        this.mFocusChangeInterface = mFocusChangeInterface;
    }

    /**
     * @return 获取当前editor的输入text
     */
    public String getEditorText() {
        return mEditText.getText().toString();
    }

    /**
     * 设置背景图片资源
     *
     * @param res 需要显示的背景图片
     */
    public void setBackground(int res) {
        // setBackgroundDrawable(getResources().getDrawable(res));
        setBackgroundResource(res);
    }

    /**
     * 设置editor的左右padding值
     *
     * @param left  左padding值(in dimension format)
     * @param right 右padding值(in dimension format)
     */
    public void setPadding(int left, int right) {
        mEditText.setPadding(DeviceUtil.getPixelFromDip(getResources().getDisplayMetrics(), left), 0, DeviceUtil.getPixelFromDip(getResources().getDisplayMetrics(), right), 0);
    }

    /**
     * 设置editor显示的hint文本
     *
     * @param text hint文本
     */
    public void setEditorHint(CharSequence text) {
        mEditText.setHint(text);
    }

    /**
     * 设置editor初始显示的文本
     *
     * @param text 初始显示的文本
     */
    public void setEditorText(CharSequence text) {
        mEditText.setText(text);
        if (null != text) {
            showClearButton(mEditText.hasFocus() && !TextUtils.isEmpty(text.toString()));
        }
    }

    /**
     * 设置editor的输入类型
     *
     * @param inputType 输入类型
     */
    public void setInputType(int inputType) {
        mEditText.setInputType(inputType);
    }

    /**
     * 设置editor的最大输入长度
     *
     * @param maxLength 最大长度
     */
    public void setInputMaxLength(int maxLength) {
        if (maxLength >= 0 && maxLength <= DEFAULT_INPUT_MAX_LENGTH) {
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
    }

    /**
     * 添加editor的文本改变监听Listener
     *
     * @param textWatcher 文本改变的监听对象
     */
    public void setEditorWatchListener(TextWatcher textWatcher) {
        mExternTextWatch = textWatcher;
    }

    /**
     * @return 获取控件内部实际的EditText控件
     */
    public EditText getmEditText() {
        return mEditText;
    }

    /**
     * 移除editor的文本改变监听事件
     *
     * @param textWatcher 外边改变的监听对象
     */
    public void removeEditorWatchListener(TextWatcher textWatcher) {
        if (textWatcher != null) {
            mEditText.removeTextChangedListener(textWatcher);
        }
    }

    /**
     * 设置editor的hint文本
     *
     * @param text hint文本
     */
    public void setEditorHint(String text) {
        mEditText.setHint(text);
    }

    /**
     * 设置editor的hint颜色
     *
     * @param color hint颜色值
     */
    public void setEditorHintColor(int color) {
        mEditText.setHintTextColor(color);

    }

    /**
     * 设置editor的文本输入过滤器
     *
     * @param filters 过滤器列表
     */
    public void setEditorFilters(InputFilter[] filters) {
        if (filters != null) {
            mEditText.setFilters(filters);
        }
    }

    /**
     * 设置editor显示文本的式样
     *
     * @param style 文本式样
     */
    public void setEditTextStyle(int style) {
        mEditText.setTextAppearance(getContext(), style);
    }

    /**
     * 设置是否显示清除按钮
     *
     * @param isShow true显示, false不显示
     */
    public void showClearButton(boolean isShow) {
        mCleanImg.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
