package com.ailicai.app.ui.base;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.ailicai.app.ApplicationPresenter;
import com.ailicai.app.common.constants.AILICAIBuildConfig;
import com.ailicai.app.common.reqaction.IwjwHttp;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.bean.Protocol;
import com.ailicai.app.widget.DialogBuilder;
import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.backstack.BackOpFragmentActivity;

import java.util.List;


/**
 * 大部分Activity请继承本类，如果同时希望支持手势滑动返回，请继承BaseSwipeBackActivity
 */
public class BaseActivity extends BackOpFragmentActivity {
    public Context mContext;
    public static final String TAG = "BaseActivity";
    protected FragmentHelper mFragmentHelper;
    BaseActivityPresenter baseActivityPresenter;
    /**
     * 标示当前Activity是否包含Fragments,如果包含则onResume()方法中不需要上传埋点
     */
    public boolean doesContainFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mFragmentHelper = new FragmentHelper(getSupportFragmentManager());
        baseActivityPresenter = new BaseActivityPresenter(this);
        CommonUtil.initSystemBar(this);
        LogUtil.d("nanshan","："+this.getClass().getSimpleName());
    }

    public int titleViewHeight = -1;

    @Override
    protected void onStart() {
        super.onStart();
        if (showSystemBarTint) {
            titleViewHeight = CommonUtil.uiSystemBarTint(this, getWindow().getDecorView());
        }
        if (!blackForMIUI) {
            //CommonUtil.miDarkSystemBar(this);
        }
    }

    boolean blackForMIUI = false;


    public void setBlackForMIUI(boolean blackForMIUI) {
        this.blackForMIUI = blackForMIUI;
    }

    /**
     * 默认表示显示系统通知栏
     */
    boolean showSystemBarTint = true;

    public void setShowSystemBarTint(boolean showSystemBarTint) {
        this.showSystemBarTint = showSystemBarTint;
    }

    public void setNavigationbarHide() {
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ApplicationPresenter.isAgentMobile) {
            ManyiAnalysis.onPageStart(this.getClass().getSimpleName());
            ManyiAnalysis.getInstance().onResume();
        }
        if (!this.doesContainFragment){
//            PVIDHandler.uploadPVIDLogical(this.getClass().getSimpleName());
        }
//        EventLog.upEventLctLog(this.getClass().getSimpleName());
        cancelAllNotification();
    }

    private void cancelAllNotification() {
        //App打开时清除通知
        NotificationManager notificationManager = (NotificationManager) getSystemService
                (Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        //小米推送清除通知
//        MiPushClient.clearNotification(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!ApplicationPresenter.isAgentMobile) {
            ManyiAnalysis.onPageEnd(this.getClass().getSimpleName());
            ManyiAnalysis.getInstance().onPause();
        }
    }

    @Override
    public void logout(Activity activity, String msg) {
        ApplicationPresenter.appLogout(activity, msg);
    }

    public void showCallPhoneDialog(final String phone) {
        DialogBuilder.showSimpleDialog(this, phone, null, "取消", null, "拨打电话", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SystemUtil.callPhone(mContext, phone);
            }
        });
    }

    public void showMyToast(String msg) {
        ToastUtil.showInCenter(msg);
    }

    /**
     * 加载界面
     */
    public void showLoadView() {
        baseActivityPresenter.showLoadView();
    }

    /**
     * 透明底部的加载界面
     */
    public void showLoadTranstView() {
        baseActivityPresenter.showLoadTranstView();
    }

    /**
     * 显示登录界面
     *
     * @param customeLoginView
     */
    public void showLoginView(final View customeLoginView) {
        baseActivityPresenter.showLoginView(customeLoginView);
    }

    /**
     * 显示业务主界面
     */
    public void showContentView() {
        baseActivityPresenter.showContentView();
    }

    /**
     * 请求失败，显示请求错误界面
     *
     * @param errorInfo errorLog
     */
    public void showErrorView(final String errorInfo) {
        baseActivityPresenter.showErrorView(errorInfo);
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
        baseActivityPresenter.showNoDataView(customeNoDataView);
    }


    @Override
    public void finish() {
        super.finish();
    }

    public FragmentHelper getmFragmentHelper() {
        return mFragmentHelper;
    }

    private Object pageRequestTag;

    public Object getPageRequestTag() {
        if (pageRequestTag == null) {
            pageRequestTag = "IWJW_REQUEST_TAG#" + getClass().getSimpleName() + hashCode();
        }
        return pageRequestTag;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IwjwHttp.cancel(getPageRequestTag());
    }

    @Override
    protected void onStop() {
        super.onStop();
        boolean isCurrentRunningForeground = SystemUtil.isApplicationForeground(this);
        if (!AILICAIBuildConfig.isDebug() && !isCurrentRunningForeground) {
            LogUtil.d(TAG, "退回到后台");
        }
    }

    public void verifyProtocolListLogical(List<Protocol> list){

    }
}

