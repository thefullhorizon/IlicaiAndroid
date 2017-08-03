package com.ailicai.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ailicai.app.MyApplication;

/**
 * 侦听息屏广播
 * Created by jeme on 2017/8/2.
 */

public class ScreenStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(TextUtils.equals(intent.getAction(),Intent.ACTION_SCREEN_OFF)){
            MyApplication.getAppPresenter().setAppBackground();
        }
    }
}
