package com.ailicai.app.common.push.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ailicai.app.common.push.PushUtil;


/**
 * Created by duo.chen on 2015/4/24.
 */
public class PersistReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PushUtil.startMqttService(context);
    }
}
