package com.ailicai.app.common.push.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ailicai.app.common.push.PushBootService;


public class ConnectivityReceiver extends BroadcastReceiver {

    private PushBootService mqttService;

    public ConnectivityReceiver(PushBootService mqttService) {
        this.mqttService = mqttService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                if (!mqttService.isStarted()) {
                    mqttService.start();
                }
            }
        }
    }

}
