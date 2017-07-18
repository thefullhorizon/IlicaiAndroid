package com.ailicai.app.common.push;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneStateChangeListener extends PhoneStateListener {

    private final PushBootService mqttService;

    public PhoneStateChangeListener(PushBootService mqttService) {
        this.mqttService = mqttService;
    }

    @Override
    public void onDataConnectionStateChanged(int state) {
        super.onDataConnectionStateChanged(state);

        if (state == TelephonyManager.DATA_CONNECTED) {
            mqttService.connect();
        }
    }
}
