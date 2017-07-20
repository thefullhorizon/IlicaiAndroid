package com.ailicai.app.ui.base;

import android.os.Bundle;

/**
 * Created by jeme on 2017/7/10.
 */

public abstract class BaseMvpActivity<V extends BaseView,T extends BasePresenter> extends BaseBindActivity{

    protected T mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPresenter = initPresenter();

        if(!(this instanceof BaseView)){
            throw new RuntimeException("activity must be BaseView");
        }else {
            mPresenter.attach(this, (V) this);
        }

        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mPresenter.dettach();
        super.onDestroy();
    }

    // 实例化presenter
    public abstract T initPresenter();
}
