package com.ailicai.app.ui.base;

import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Created by duo.chen on 2015/12/28
 */
public abstract class BaseBindActivity extends BaseActivity {

    // 当前Activity是否可见
    private boolean isActivityVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupInjectComponent();
        if (getLayout() != 0) {
            setContentView(getLayout());
        }
        ButterKnife.bind(this);
        init(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 注册公共全局事件
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityVisible = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityVisible = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void init(Bundle savedInstanceState) {

    }

    public abstract int getLayout();

    /**
     * Dagger2 框架依赖注入专用方法
     * //TODO:TEST
     */
    protected void setupInjectComponent() {
    }

    public boolean isActivityVisible() {
        return isActivityVisible;
    }
}
