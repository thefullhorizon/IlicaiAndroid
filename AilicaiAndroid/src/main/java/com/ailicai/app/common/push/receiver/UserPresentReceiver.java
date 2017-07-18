package com.ailicai.app.common.push.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ailicai.app.common.push.PushBootService;


public class UserPresentReceiver extends BroadcastReceiver {

    private PushBootService mqttService;

    public UserPresentReceiver(PushBootService mqttService) {
        this.mqttService = mqttService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mqttService.connect();
    }

}