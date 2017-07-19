package com.ailicai.app.common.push.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ailicai.app.common.push.MqttManager;
import com.ailicai.app.common.push.Notifier;
import com.ailicai.app.common.push.PushConstants;
import com.ailicai.app.common.push.model.PushCleanMessage;
import com.ailicai.app.common.push.model.PushMessage;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.huoqiu.framework.util.LogUtil;

public final class NotificationReceiver extends BroadcastReceiver {

    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (PushConstants.ACTION_MQTT_SHOW_NOTIFICATION.equals(action)) {
            Notifier notifier = Notifier.getInstance(context);
            byte[] payload = intent.getByteArrayExtra("message");
            String topic = intent.getStringExtra("topic");
            if (topic.equals(PushConstants.PUSH_USER_CLEAN)) {

                PushCleanMessage pushCleanMessage = JSON.parseObject(payload, PushCleanMessage
                        .class, Feature.IgnoreNotMatch, Feature.AllowISO8601DateFormat);
                //getClientId中保存的是最后一次登录的clientId，如果跟本机不相同则退出
                if (!pushCleanMessage.getClientId().equals(MqttManager.getClientIdByMobile
                        (MqttManager.getMobile()))) {

                    if (UserInfo.isLogin()){
                        LoginManager.loginOut(context);
                    }
                }

            } else {
                try {
                    PushMessage pushMessage = JSON.parseObject(payload, PushMessage.class,
                            Feature.IgnoreNotMatch, Feature.AllowISO8601DateFormat);

                    pushMessage.setPayload(new String(payload));
                    LogUtil.i(MqttManager.TAG, "receive push message " + pushMessage);
                    notifier.notify(pushMessage);
                } catch (Exception e) {
                    LogUtil.i(MqttManager.TAG, "fail to parseObject " + new String(payload), e);
                }
            }
        }
    }

}
