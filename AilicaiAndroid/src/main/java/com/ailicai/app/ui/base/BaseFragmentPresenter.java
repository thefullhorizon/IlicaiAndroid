package com.ailicai.app.ui.base;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.utils.ToastUtil;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.LogUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Jer on 2015/7/23.
 */
public class BaseFragmentPresenter implements View.OnClickListener, ILayoutLoadListener {
    SuperFragment baseFragment;

    public BaseFragmentPresenter(SuperFragment baseFragment) {
        this.baseFragment = baseFragment;
    }

    public View getView() {
        return baseFragment.getView();
    }

    public Activity getActivity() {
        return baseFragment.getActivity();
    }

    ViewSwitcher loadView;
    ViewSwitcher errorSwitchLayout;

    void runOnUiThread(Runnable runnable) {
        baseFragment.getBackOpActivity().runOnUiThread(runnable);
    }

    /**
     * 加载界面
     */
    @Override
    public void showLoadView() {
        if (haveData) {
            return;
        }
        if (!initErrorLayout()) {
            return;
        }
        errorSwitchLayout.setVisibility(View.VISIBLE);
        errorSwitchLayout.setDisplayedChild(0);
        loadView.setDisplayedChild(0);
        loadView.setBackgroundResource(android.R.color.white);
    }

    @Override
    public void showLoadTranstView() {
        if (!initErrorLayout()) {
            return;
        }
        errorSwitchLayout.setVisibility(View.VISIBLE);
        errorSwitchLayout.setDisplayedChild(0);
        loadView.setDisplayedChild(0);
        loadView.setBackgroundResource(android.R.color.transparent);
    }

    /**
     * 初始化异常处理界面
     */
    public boolean initErrorLayout() {
        if (errorSwitchLayout == null) {
            View fragmentRootView = getView();
            if (fragmentRootView == null) {
                return false;
            }
            if (fragmentRootView instanceof LinearLayout) {
                LogUtil.e("error", "this root view is not LinearLayout，so ErrorView not work");
            }
            ViewGroup fragmentView = (ViewGroup) fragmentRootView;
            errorSwitchLayout = (ViewSwitcher) View.inflate(getActivity(), R.layout.loading_select, null);
            fragmentView.addView(errorSwitchLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            errorSwitchLayout.setPadding(0, baseFragment.getTitleViewHight(), 0, 0);
            loadView = (ViewSwitcher) errorSwitchLayout.findViewById(R.id.loading_viewswitch);
        }
        return true;
    }

    /**
     * 显示登录界面
     *
     * @param customeLoginView
     */
    @Override
    public void showLoginView(final View customeLoginView) {
        if (!initErrorLayout()) {
            return;
        }
        FrameLayout loginContainer = (FrameLayout) errorSwitchLayout.findViewById(R.id.login_container);
        if (loginContainer.getChildCount() == 0) {
            loginContainer.addView(customeLoginView);
            customeLoginView.findViewById(R.id.authBtn).setOnClickListener(BaseFragmentPresenter.this);
        }
        errorSwitchLayout.setVisibility(View.VISIBLE);
        errorSwitchLayout.setDisplayedChild(1);
    }

    boolean haveData = false;

    /**
     * 显示业务主界面
     */
    @Override
    public void showContentView() {
        if (errorSwitchLayout == null) {
            return;
        }
        haveData = true;
        errorSwitchLayout.setVisibility(View.GONE);
    }

    /**
     * 请求失败，显示请求错误界面
     *
     * @param errorInfo
     */
    @Override
    public void showErrorView(String errorInfo) {
        if (!initErrorLayout()) {
            return;
        }
        if (errorSwitchLayout == null) {
            return;
        }
        if (haveData) {
            ToastUtil.showInBottom(getActivity(), errorInfo);
            errorSwitchLayout.setVisibility(View.GONE);
        } else {
            errorSwitchLayout.setVisibility(View.VISIBLE);
            ViewSwitcher errorViewSwitch = (ViewSwitcher) errorSwitchLayout.findViewById(R.id.error_viewswitch);
            ImageView imageView = ((ImageView) loadView.findViewById(R.id.error_img));
            List<String> networkErrors = Arrays.asList(loadView.getResources().getStringArray(R.array.NetworkErrors));
            if (networkErrors.contains(errorInfo)) {
                imageView.setImageResource(R.drawable.no_internet);
            } else {
                imageView.setImageResource(R.drawable.server_error);
            }

            if (TextUtils.isEmpty(errorInfo)) {
                errorInfo = loadView.getResources().getString(R.string.service_exception);
            }
            ((TextView) loadView.findViewById(R.id.error_text)).setText(errorInfo);


            loadView.findViewById(R.id.error_btn).setOnClickListener(BaseFragmentPresenter.this);
            errorSwitchLayout.setDisplayedChild(0);
            loadView.setDisplayedChild(1);
            errorViewSwitch.setDisplayedChild(0);
        }
    }


    @Override
    public void onClick(View v) {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        switch (v.getId()) {
            case R.id.error_btn:
                try {
                    reloadData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.authBtn:
                goLogin();
                break;
        }
    }

    public void goLogin() {
//        LoginManager.goLogin(getActivity(), LoginManager.LOGIN_FROM_CHECK);
    }

    /**
     * 重新加载界面数据
     */
    @Override
    public void reloadData() {
        baseFragment.reloadData();
    }

    /**
     * 显示请求成功没有数据的业务界面
     *
     * @param customeNoDataView
     */
    @Override
    public void showNoDataView(final View customeNoDataView) {
        if (!initErrorLayout()) {
            return;
        }
        if (errorSwitchLayout == null) {
            return;
        }
        errorSwitchLayout.setVisibility(View.VISIBLE);
        ViewSwitcher errorViewSwitch = (ViewSwitcher) errorSwitchLayout.findViewById(R.id.error_viewswitch);
        FrameLayout nodataContainer = (FrameLayout) errorSwitchLayout.findViewById(R.id.nodata_container);
        if (nodataContainer.getChildCount() == 0) {
            nodataContainer.addView(customeNoDataView);
            FrameLayout.LayoutParams frameLayoutlp = (FrameLayout.LayoutParams) customeNoDataView.getLayoutParams();
            frameLayoutlp.topMargin = 0 - MyApplication.getInstance().getResources().getDimensionPixelOffset(R.dimen._48);
            customeNoDataView.setLayoutParams(frameLayoutlp);
        }
        errorSwitchLayout.setDisplayedChild(0);
        loadView.setDisplayedChild(1);
        errorViewSwitch.setDisplayedChild(1);
    }
}
