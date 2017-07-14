package com.ailicai.app.ui.welcome;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.ailicai.app.ApplicationPresenter;
import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.ui.index.IndexActivity;
import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.backstack.BackOpFragmentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WelcomeActivity extends BackOpFragmentActivity {
    private static final int SHOW_GUIDE_OR_HOME = 0x001;
    private static final int SHOW_GUIDE_VIEW = 0x002;
    private static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    private static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ApplicationPresenter.syncTime(null);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHandler.sendEmptyMessageDelayed(SHOW_GUIDE_OR_HOME, 300);
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_GUIDE_OR_HOME:
                    toHomePage();
                    break;
                case SHOW_GUIDE_VIEW:
            }
            return false;
        }
    });

    public void toHomePage() {
        Intent mIntent = new Intent(this, IndexActivity.class);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mIntent.putExtras(bundle);
        }
        Uri data = getIntent().getData();
        if (null != data) {
            // TODO PUSH跳转
//            mIntent.putExtra(CommonTags.URIDATA, data);
        }
        startActivity(mIntent);
        finish();
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
