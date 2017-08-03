package com.ailicai.app.ui.base;

import android.os.Bundle;

import com.ailicai.app.MyApplication;
import com.ailicai.app.common.constants.GlobleConstants;
import com.ailicai.app.common.utils.AppUtils;
import com.ailicai.app.common.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Created by duo.chen on 2015/12/28
 */
public abstract class BaseBindActivity extends BaseActivity {

    // 当前Activity是否可见
    private boolean isActivityVisible = false;
    // 页面是否允许唤起手势密码
    protected boolean enableLock = true;

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
    protected void onPostResume() {
        super.onPostResume();
        if (enableLock && !MyApplication.getAppPresenter().isInFront()) {
            MyApplication.getAppPresenter().setAppFront(true);
            // 减得当前APP在后台滞留的时间 durTime
            long durTime = System.currentTimeMillis() - GlobleConstants.mLockAppTime;
            if (durTime > Constants.LOCK_TIME) {
                // 显示手势密码页面
                AppUtils.goActivity(this);
            }
        }
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

    /**
     * 部分页面禁用手势密码需要调用该方法，例如启动页、注册登录页、解锁页等
     */
    protected void disablePatternLock() {
        enableLock = false;
    }
}
