package com.ailicai.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ailicai.app.R;

import butterknife.ButterKnife;

/**
 * 自动投标页的投资选项
 * Created by jeme on 2017/8/18.
 */

public class AutomaticTenderTypeView extends FrameLayout implements CompoundButton.OnCheckedChangeListener,
View.OnClickListener{

    private TextView mTvTypeName;
    private TextView mTvTypeTag;
    private TextView mTvTypeDesc;
    private RadioButton mCbCheck;
    private String mTypeName;
    private String mTypeTag;
    private String mTypeDesc;
    private boolean mChecked;
    private AutomaticTenderCheckListener mListener;


    public interface AutomaticTenderCheckListener{
        void onCheckedChanged(AutomaticTenderTypeView view,boolean isChecked);
    }
    public AutomaticTenderTypeView(@NonNull Context context) {
        this(context,null);
    }

    public AutomaticTenderTypeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AutomaticTenderTypeView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.widget_automatic_tender_type,this);
        initAttrs(context,attrs);
        initView();
    }

    private void initAttrs(Context context,AttributeSet attrs){
        if(attrs == null){
            return;
        }
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.AutomaticTenderTypeView);
        mTypeName = array.getString(R.styleable.AutomaticTenderTypeView_tenderTypeName);
        mTypeTag = array.getString(R.styleable.AutomaticTenderTypeView_tenderTypeTag);
        mTypeDesc = array.getString(R.styleable.AutomaticTenderTypeView_tenderTypeDesc);
        mChecked = array.getBoolean(R.styleable.AutomaticTenderTypeView_checked,false);
        array.recycle();
    }

    private void initView(){
        mTvTypeName = ButterKnife.findById(this,R.id.tv_type_name);
        mTvTypeTag = ButterKnife.findById(this,R.id.tv_type_tag);
        mTvTypeDesc = ButterKnife.findById(this,R.id.tv_type_desc);
        mCbCheck = ButterKnife.findById(this,R.id.cb_check);

        mTvTypeName.setText(TextUtils.isEmpty(mTypeName) ? "" : mTypeName);
        mTvTypeTag.setText(TextUtils.isEmpty(mTypeTag) ? "" : mTypeTag);
        mTvTypeDesc.setText(TextUtils.isEmpty(mTypeDesc) ? "" : mTypeDesc);
        mCbCheck.setChecked(mChecked);

        mCbCheck.setOnCheckedChangeListener(this);
    }

    public void setSelect(boolean isSelected){
        mCbCheck.setChecked(isSelected);
    }

    public boolean getChecked(){
        return mCbCheck.isChecked();
    }

    public void setOnCheckChangeListener(AutomaticTenderCheckListener listener){
        mListener = listener;
    }

    @Override
    public void onClick(View v) {


    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(mListener != null){
            mListener.onCheckedChanged(this,isChecked);
        }
    }
}
