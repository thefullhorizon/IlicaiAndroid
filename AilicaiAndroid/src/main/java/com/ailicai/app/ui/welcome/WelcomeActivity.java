package com.ailicai.app.ui.welcome;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.ApplicationPresenter;
import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.constants.CommonTag;
import com.ailicai.app.common.constants.GlobleConstants;
import com.ailicai.app.common.imageloader.ImageLoaderClient;
import com.ailicai.app.common.push.constant.CommonTags;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.response.SplashScreenResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.guide.GuideActivity;
import com.ailicai.app.ui.index.IndexActivity;
import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.imageloader.core.LoadParam;
import com.huoqiu.framework.imageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class WelcomeActivity extends BaseBindActivity {
    private static final int SHOW_GUIDE_OR_HOME = 0x001;
    private static final int SHOW_GUIDE_VIEW = 0x002;
    private static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    private static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";

    @Bind(R.id.llJumpOver)
    LinearLayout llJumpOver;
    @Bind(R.id.tvRestSeconds)
    TextView tvRestSeconds;
    @Bind(R.id.ivAdvertisement)
    ImageView ivAdvertisement;

    @Override
    public int getLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        ApplicationPresenter.syncTime(null);
        setNavigationbarHide();
        disablePatternLock();
        GlobleConstants.mLockAppTime = 0;
        setWelcomeAdvertiseData();
    }

    private void setWelcomeAdvertiseData() {
        final SplashScreenResponse response = MyPreference.getInstance().read(SplashScreenResponse.class);
        if(response != null) {
            if(isShouldShowAdvertise(response)) {
                LoadParam param = new LoadParam();
                param.setImgUri(response.getImgUrl());
                ImageLoaderClient.display(this, ivAdvertisement, param, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted() {

                    }

                    @Override
                    public void onLoadingFailed() {

                    }

                    @Override
                    public void onLoadingSuccess() {
                        setJumpOverLogic(response);
                    }

                    @Override
                    public void onLoadingCancelled() {

                    }

                    @Override
                    public void onLoadingFinish() {

                    }
                });
            }
        }
    }

    // 是否应该显示广告位
    private boolean isShouldShowAdvertise(SplashScreenResponse response) {
        boolean isAdvertiseInValidDate = isAdvertiseInValidDate(response);
        boolean isImageUrlValid = !TextUtils.isEmpty(response.getImgUrl());
        boolean isImageHasCached = ImageLoaderClient.imgHasDiskCached(this,response.getImgUrl());
        LogUtil.e("================>",isAdvertiseInValidDate+" " +isImageUrlValid + " "+isImageHasCached);
        return isAdvertiseInValidDate && isImageUrlValid && isImageHasCached;
    }

    private boolean isAdvertiseInValidDate(SplashScreenResponse response) {
        long currentMillis = System.currentTimeMillis();
        long validMillis = response.getValidTill();
        // 0代表永久有效
        if(currentMillis < validMillis || validMillis == 0) {
            return true;
        }
        return false;
    }

    // 设置跳过按钮的相关逻辑
    CountDownTimer countDownTimer;
    private void setJumpOverLogic(SplashScreenResponse response) {
        int showTime = response.getShowTime();
        if(showTime > 0) {
            llJumpOver.setVisibility(View.VISIBLE);
            tvRestSeconds.setText(showTime+"");
            mHandler.removeCallbacksAndMessages(null);
            countDownTimer = new CountDownTimer(1000 * (showTime+2), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int showTime = ((int)millisUntilFinished) / 1000-1;
                    if(showTime >= 0) {
                        tvRestSeconds.setText(showTime+"");
                    }
                    if(showTime == 0) {
                        choosePageToGo();
                    }
                }

                @Override
                public void onFinish() {
                    // finish会比tick慢，不知道为啥
//                    tvRestSeconds.setText(0+"");
                }
            }.start();
        }
    }

    @OnClick(R.id.llJumpOver)
    void onJumpOverClick() {
        removePageAllCallbacks();
        choosePageToGo();
        finish();
    }

    @OnClick(R.id.ivAdvertisement)
    void onAdvertiseClick() {
        LogUtil.e("==========>","preOnClick");
        goToAdvertiseDetailAndRemoveCallBackAndFinishThis();
    }


    private void goToAdvertiseDetailAndRemoveCallBackAndFinishThis() {
        LogUtil.e("==========>","onClick");
        SplashScreenResponse response = MyPreference.getInstance().read(SplashScreenResponse.class);
        if(response !=null && isShouldShowAdvertise(response)) {
            LogUtil.e("==========>","onClick JumpUrl not empty");
            removePageAllCallbacks();

            Map<String, String> dataMap = ObjectUtil.newHashMap();
            dataMap.put(WebViewActivity.URL,response.getJumpUrl());
            dataMap.put(WebViewActivity.NEED_REFRESH, "0");
            dataMap.put(WebViewActivity.TOPVIEWTHEME, "false");
            MyIntent.startActivity(this, WelcomeAdvertiseWebViewActivity.class, dataMap);

            finish();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHandler.sendEmptyMessageDelayed(SHOW_GUIDE_OR_HOME, 3000);
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_GUIDE_OR_HOME:
                    choosePageToGo();
                    break;
                case SHOW_GUIDE_VIEW:
            }
            return false;
        }
    });

    private void choosePageToGo() {
        if(MyPreference.getInstance().read(CommonTag.IS_FIREST_START,true)){
            goGuidePage();
        }else{
            toHomePage();
        }
    }

    private void goGuidePage(){
        startActivity(getDescIntent(GuideActivity.class));
        finish();
    }

    public void toHomePage() {
        startActivity(getDescIntent(IndexActivity.class));
        finish();
    }

    private Intent getDescIntent(Class<?> cls){
        Intent intent = new Intent(this, cls);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        Uri data = getIntent().getData();
        if (null != data) {
            //短信会有此值
            intent.putExtra(CommonTags.URIDATA, data);
        }
        return intent;
    }


    @Override
    public void onResume() {
        super.onResume();
        ManyiAnalysis.onPageStart(this.getClass().getSimpleName()); //统计页面
        ManyiAnalysis.getInstance().onResume();//统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        ManyiAnalysis.onPageEnd(this.getClass().getSimpleName());// 保证onPageEnd在onPause之前调用,
        // 因为onPause中会保存信息
        ManyiAnalysis.getInstance().onPause();
    }

    public static int REQUEST_READ_CONTACTS_LOCATION = 101;
    public OnPermissionCallbackListener onPermissionCallbackListener;

    @TargetApi(Build.VERSION_CODES.M)
    public void requestRuntimePermission(List<String> permissionsList, int requestCode, OnPermissionCallbackListener onPermissionCallbackListener) {
        this.onPermissionCallbackListener = onPermissionCallbackListener;
        ArrayList<String> noPermissionsList = new ArrayList();
        for (int i = 0; i < permissionsList.size(); i++) {
            switch (checkSelfPermission(permissionsList.get(i))) {
                case PackageManager.PERMISSION_GRANTED://已经通过
                    break;
                case PackageManager.PERMISSION_DENIED://没获得权限
                    noPermissionsList.add(permissionsList.get(i));
                    break;
            }
        }

        if (noPermissionsList.size() == 0) {
            onPermissionCallbackListener.onGranted();
        } else {
            requestPermissions(noPermissionsList.toArray(new String[noPermissionsList.size()]), requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS_LOCATION) {
            Map<String, Integer> perms = new HashMap<>();
            perms.put(ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            perms.put(READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
            for (int i = 0; i < permissions.length; i++) {
                perms.put(permissions[i], grantResults[i]);
                if (perms.get(READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("wxl", "授权请求被通过");
                    onPermissionCallbackListener.onGranted();
                    if (perms.get(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                        ToastUtil.show("不能获取定位信息");
                    }
                } else {
                    onPermissionCallbackListener.onDenied();
                    Log.i("wxl", "授权请求不被通过");
                }
            }
        }
    }

    /**
     * 同时请求通讯录定位权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void requestContactsLocation() {
        List<String> permissionsList = new ArrayList<>();
        permissionsList.add(READ_PHONE_STATE);
        permissionsList.add(ACCESS_FINE_LOCATION);
        requestRuntimePermission(permissionsList, REQUEST_READ_CONTACTS_LOCATION, onPermissionListener);
    }

    public void onPermission(View v) {
        //  requestRuntimePermission("android.permission.CAMERA", onPermissionListener);
    /*    switch (v.getId()) {
            case R.id.calendar:
                //日历
                requestRuntimePermission(READ_CALENDAR, this);
                break;
            case R.id.camera:
                //照相机
                requestRuntimePermission(CAMERA, this);
                break;
            case R.id.contacts:
                //通讯录
                requestRuntimePermission(READ_CONTACTS, this);
                break;
            case R.id.location:
                //定位
                requestRuntimePermission(ACCESS_FINE_LOCATION, this);
                break;
            case R.id.microPhone:
                //录音
                requestRuntimePermission(RECORD_AUDIO, this);
                break;
            case R.id.readPhone:
                //读取手机状态
                requestRuntimePermission(READ_PHONE_STATE, this);
                break;
            case R.id.sensors:
                //传感器
                requestRuntimePermission(BODY_SENSORS, this);
                break;
            case R.id.sms:
                //短信
                requestRuntimePermission(SEND_SMS, this);
                break;
            case R.id.storage:
                //文件管理
                requestRuntimePermission(READ_EXTERNAL_STORAGE, this);
                break;
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removePageAllCallbacks();
    }

    private void removePageAllCallbacks() {
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    OnPermissionCallbackListener onPermissionListener = new OnPermissionCallbackListener() {
        @Override
        public void onGranted() {
            MyApplication.getAppPresenter().initTime();
            toHomePage();
        }

        @Override
        public void onDenied() {
            ToastUtil.show("没有拿到权限");
        }
    };

    public interface OnPermissionCallbackListener {
        void onGranted();

        void onDenied();
    }

}
