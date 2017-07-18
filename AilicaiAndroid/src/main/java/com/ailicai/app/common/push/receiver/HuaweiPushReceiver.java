package com.ailicai.app.common.push.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.ailicai.app.common.push.bridge.PushBrigdgeService;
import com.huawei.android.pushagent.api.PushEventReceiver;

/**
 * 接收Huawei Push所有消息的广播接收器
 * Created by jeme on 2017/5/5.
 */

public class HuaweiPushReceiver extends PushEventReceiver {

    @Override
    public void onToken(Context context, String token, Bundle bundle) {
        if(!TextUtils.isEmpty(token)){
            callbackMessageRegister(context, token);
        }
    }

    @Override
    public boolean onPushMsg(Context context, byte[] bytes, Bundle bundle) {
        //透传消息，不做处理
        /*if(false){
            LogUtil.d("HuaweiPushReceiver", "Successfully get HWPush Message from server:");
            Intent intent = new Intent();
            intent.setAction(PushBrigdgeService.INTERNAL_ACTION_BRIDGED_MESSAGE_ARRIVED);
            intent.putExtra(PushBrigdgeService.INTERNAL_KEY_BRIDGED_MESSAGE_ARRIVED, bundle);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }*/
        return false;
    }

    private void callbackMessageRegister(Context context, String regID) {
        Intent intent = new Intent();
        intent.setAction(PushBrigdgeService.INTERNAL_ACTION_BRIDGED_REGISTER_TOKEN);
        intent.putExtra(PushBrigdgeService.INTERNAL_KEY_BRIDGED_REGISTER_TOKEN, regID);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
