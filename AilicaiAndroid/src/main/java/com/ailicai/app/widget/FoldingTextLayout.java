package com.ailicai.app.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.ui.login.UserInfo;


/**
 * Created by liyanan on 16/4/7.
 */
public class FoldingTextLayout extends RelativeLayout {
    private TextView tvTitle;
    private TextViewTF tvAction;
    private TextView tvContent;
    private OnContentShowListener onContentShowListener;
    private boolean isExpand;//当前展开的状态

    public FoldingTextLayout(Context context) {
        super(context);
        init(context);
    }

    public FoldingTextLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public FoldingTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_folding_text, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvAction = (TextViewTF) findViewById(R.id.tv_action);
        tvContent = (TextView) findViewById(R.id.tv_content);
        addListener();
    }

    public void initData(String title, String content, ServiceCallClickListener callClickListener) {
        tvTitle.setText(title);
        if (callClickListener != null) {
            String line = "\n";
            String call = "客服电话: ";
            String phone = UserInfo.getInstance().getServicePhoneNumber();
            String total = content + line + call + phone;
            SpannableString spanString = new SpannableString(total);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#4a90e2"));
            spanString.setSpan(colorSpan, (content + line + call).length(), total.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            NoLineClickSpan clickSpan = new NoLineClickSpan(callClickListener);
            spanString.setSpan(clickSpan, (content + line).length(), total.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvContent.setText(spanString);
            tvContent.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接为可点击状态
        } else {
            tvContent.setText(content);
        }
        tvContent.setHeight(0);
    }


    public void setOnContentShowListener(OnContentShowListener onContentShowListener) {
        this.onContentShowListener = onContentShowListener;
    }

    private void addListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpand) {
                    hideContent();
                } else {
                    showContent();
                    if (onContentShowListener != null) {
                        onContentShowListener.onContentShow(FoldingTextLayout.this);
                    }
                }
            }
        });
    }

    public void hideContent() {
        isExpand = false;
        tvAction.clearAnimation();
        tvContent.clearAnimation();
        final int deltaValue;
        final int startValue = tvContent.getHeight();
        int durationMillis = 360;
        deltaValue = tvContent.getLineHeight() * 0 - startValue;
        Animation contentAnim = new Animation() {
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                tvContent.setHeight((int) (startValue + deltaValue * interpolatedTime));
            }
        };
        tvContent.startAnimation(contentAnim);
        contentAnim.setDuration(durationMillis);
        RotateAnimation rotateAnimation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(durationMillis);
        rotateAnimation.setFillAfter(true);
        tvAction.startAnimation(rotateAnimation);
    }

    public void showContent() {
        isExpand = true;
        tvAction.clearAnimation();
        tvContent.clearAnimation();
        final int deltaValue;
        final int startValue = tvContent.getHeight();
        int durationMillis = 360;
        deltaValue = tvContent.getLineHeight() * tvContent.getLineCount() - startValue;
        Animation contentAnim = new Animation() {
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                tvContent.setHeight((int) (startValue + deltaValue * interpolatedTime));
            }
        };
        contentAnim.setDuration(durationMillis);
        tvContent.startAnimation(contentAnim);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(durationMillis);
        rotateAnimation.setFillAfter(true);
        tvAction.startAnimation(rotateAnimation);
    }

    public boolean isExpand() {
        return isExpand;
    }

    public interface ServiceCallClickListener {
        void serviceCallClick();
    }

    public interface OnContentShowListener {
        void onContentShow(FoldingTextLayout v);
    }

    class NoLineClickSpan extends ClickableSpan {
        private ServiceCallClickListener listener;

        private NoLineClickSpan(ServiceCallClickListener listener) {
            super();
            this.listener = listener;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false); //去掉下划线
        }

        @Override
        public void onClick(View widget) {
            listener.serviceCallClick(); //点击超链接时调用
        }

    }
}
