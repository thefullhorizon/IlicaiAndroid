package com.ailicai.app.ui.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.ailicai.app.ApplicationPresenter;
import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwHttp;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.widget.DialogBuilder;
import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.SuperFragment;

import java.lang.ref.WeakReference;

/**
 * 大部分Fragment请继承本类，如果同时希望支持手势滑动返回，请继承BaseSwipeBackFragment
 */
public class BaseFragment<T> extends SuperFragment<T> {
    private final static String STATE_REQUEST_CODE = "STATE_REQUEST_CODE";
    protected FragmentHelper mFragmentHelper;
    private BaseFragmentPresenter baseFragmentPresenter;
    private Object pageRequestTag;
    private int titleViewHight = 0;
    private boolean showSystemBarTint = true;
    private WeakReference<Activity> activityWeakReference;
    /**
     * 标示当前Activity是否包含Fragments,如果包含则onResume()方法中不需要上传埋点
     */
    public boolean doesContainFragment = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_REQUEST_CODE)) {
            setTargetFragment(null, savedInstanceState.getInt(STATE_REQUEST_CODE));
        }
        baseFragmentPresenter = new BaseFragmentPresenter(this);
        mFragmentHelper = new FragmentHelper(getActivity().getSupportFragmentManager());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_REQUEST_CODE, getTargetRequestCode());
        setTargetFragment(null, -1);
        super.onSaveInstanceState(outState);
    }

    public int getTitleViewHight() {
        return titleViewHight;
    }

    public Object getPageRequestTag() {
        if (pageRequestTag == null) {
            pageRequestTag = "IWJW_REQUEST_TAG#" + getClass().getSimpleName() + hashCode();
        }
        return pageRequestTag;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (showSystemBarTint) {
            titleViewHight = CommonUtil.uiSystemBarTint(getActivity(), view);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setClickable(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!ApplicationPresenter.isAgentMobile) {
            ManyiAnalysis.onPageStart(this.getClass().getSimpleName());
        }
        if (!this.doesContainFragment){
//            PVIDHandler.uploadPVIDLogical(this.getClass().getSimpleName());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!ApplicationPresenter.isAgentMobile) {
            ManyiAnalysis.onPageEnd(this.getClass().getSimpleName());
        }
        SystemUtil.HideSoftInput(getActivity());
    }

    /**
     * 设置默认的动画
     */
    public void setDefaultAnimations() {
        this.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
    }

    /**
     * 设置默认的ContainerId
     */
    public void setDefaultContainerId() {
        this.setContainerId(R.id.main_container);
    }

    /**
     * 默认转场.
     * SHOW_ADD_HIDE.
     */
    protected void startDefaultTransition(BaseFragment baseFragment, Bundle bundle) {
        baseFragment.setArguments(bundle);
        baseFragment.setDefaultContainerId();
        baseFragment.setDefaultAnimations();
        baseFragment.setTag(baseFragment.getClass().getCanonicalName() + baseFragment.hashCode());
        baseFragment.setManager(getActivity().getSupportFragmentManager());
        baseFragment.show(SHOW_ADD_HIDE);
    }

    public void setShowSystemBarTint(boolean showSystemBarTint) {
        this.showSystemBarTint = showSystemBarTint;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("debuglog", "取消请求" + getPageRequestTag());
        IwjwHttp.cancel(getPageRequestTag());
    }

    /**
     * 加载界面
     */
    public void showLoadView() {
        baseFragmentPresenter.showLoadView();
    }

    /**
     * 透明底部的加载界面
     */
    public void showLoadTranstView() {
        baseFragmentPresenter.showLoadTranstView();
    }

    /**
     * 显示登录界面
     *
     * @param customeLoginView
     */
    public void showLoginView(final View customeLoginView) {
        baseFragmentPresenter.showLoginView(customeLoginView);
    }

    /**
     * 显示业务主界面
     */
    public void showContentView() {
        baseFragmentPresenter.showContentView();
    }

    /**
     * 请求失败，显示请求错误界面
     *
     * @param errorInfo
     */
    public void showErrorView(final String errorInfo) {
        baseFragmentPresenter.showErrorView(errorInfo);
    }

    /**
     * 重新加载界面数据
     */
    public void reloadData() {
    }

    /**
     * 显示请求成功没有数据的业务界面
     *
     * @param customeNoDataView
     */
    public void showNoDataView(final View customeNoDataView) {
        baseFragmentPresenter.showNoDataView(customeNoDataView);
    }

    /**
     * 为动画刻意延迟100请求
     *
     * @param runnable
     */
    public void postDelayedForAnim(Runnable runnable) {
        getView().postDelayed(runnable, 100);
    }

    public void showConfirmCallDialog(final String phone) {
        if (TextUtils.isEmpty(phone)) {
            showMyToast("电话号码为空！");
            return;
        }
        DialogBuilder.showSimpleDialog(getActivity(), phone, null, "取消", null, "拨打电话", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SystemUtil.callPhone(getActivity(), phone);
            }
        });
    }

   /* public void showConfirmCallDialog(final ConfirmCallPhoneDialog.PhoneInfo info) {
        if (TextUtils.isEmpty(info.getPhone())) {
            showMyToast("电话号码为空！");
            return;
        }

        DialogBuilder.showSimpleDialog(getActivity(), info.getTitle(), info.getPhone(), "取消", null, "拨打电话", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SystemUtil.callPhone(getActivity(), info.getPhone());
            }
        });
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityWeakReference = null;
    }

    public void showMyToast(String msg) {
        if (getActivity() != null) {
            ToastUtil.showInCenter(msg);
        }
    }

    /**
     * 获取weakReference activity
     */
    public Activity getWRActivity() {
        return activityWeakReference == null ? null : activityWeakReference.get();
    }

    public void setHaveData(boolean haveData) {
        baseFragmentPresenter.haveData = haveData;
    }

    public void onBackPressed(){

    }
}