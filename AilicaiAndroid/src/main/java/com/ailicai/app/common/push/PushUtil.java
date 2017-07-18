package com.ailicai.app.common.push;

import android.content.Context;
import android.content.Intent;

/**
 * Created by duo.chen on 2015/4/28.
 */
public class PushUtil {
    public static void stopMqttService(Context context) {
        Intent intent = PushBootService.getIntent();
        intent.setPackage(context.getPackageName());
        context.stopService(intent);
    }

    public static void startMqttService(Context context) {
        Intent intent = PushBootService.getIntent();
        intent.setPackage(context.getPackageName());
        context.startService(intent);
    }

    public static void resetMqttService(Context context){
        stopMqttService(context);
        startMqttService(context);
    }
}
