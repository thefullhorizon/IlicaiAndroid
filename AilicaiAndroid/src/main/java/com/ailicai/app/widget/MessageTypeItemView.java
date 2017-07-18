package com.ailicai.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ailicai.app.R;

import butterknife.Bind;

/**
 * Created by jeme on 2017/7/18.
 */

public class MessageTypeItemView extends FrameLayout {

    private CircleImageView mCivHead;
    private TextView mTvName;
//    private TextView mTvTitle;
    private TextView mTvNum;
    private TextView mTvTime;
    private TextView mTvContent;

    public MessageTypeItemView(@NonNull Context context) {
        this(context,null);
    }

    public MessageTypeItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MessageTypeItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(context,attrs);
    }

    private void initView(@NonNull Context context){
        LayoutInflater.from(context).inflate(R.layout.list_item_message_list,this);
        mCivHead = (CircleImageView)findViewById(R.id.civ_head);
        mTvName = (TextView)findViewById(R.id.tv_name);
//        mTvTitle = (TextView)findViewById(R.id.tv_title);
        mTvNum = (TextView)findViewById(R.id.tv_num);
        mTvTime = (TextView)findViewById(R.id.tv_time);
        mTvContent = (TextView)findViewById(R.id.tv_content);
    }
    private void initAttrs(@NonNull Context context,@Nullable AttributeSet attrs){
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MessageTypeItemView);
            mCivHead.setImageResource(array.getResourceId(R.styleable.MessageTypeItemView_head,-1));
            mTvName.setText(array.getString(R.styleable.MessageTypeItemView_name));
            array.recycle();
        }
    }

    public void setContent(String content){
        mTvContent.setText(content);
    }
    public void setTime(String time){
        mTvTime.setText(time);
    }
    public void setNum(int num){

       if (num > 0 && num < 10) {
            //1-9
           mTvNum.setText(String.valueOf(num));
           mTvNum.setBackgroundResource(R.drawable.msg_new_ones);
        } else if (num > 9 && num < 100) {
            //10-99
           mTvNum.setText(String.valueOf(num));
           mTvNum.setBackgroundResource(R.drawable.msg_new_tens);
        } else {
            //大于99
           mTvNum.setText("99+");
           mTvNum.setBackgroundResource(R.drawable.msg_new_more);
        }
        mTvNum.setVisibility(num <= 0 ? GONE : VISIBLE);
    }
}
