package com.ailicai.app.common.push.bridge;

import com.ailicai.app.common.push.MqttManager;
import com.ailicai.app.common.push.PushConstants;
import com.huawei.android.pushagent.api.PushManager;
import com.manyi.inthingsq.android.CloudBridgeService;


/**
 * Created by jeme on 2017/5/8.
 */

public class HuaweiPushBridgeService extends PushBrigdgeService {

    public static final String PUSH_USER_TOPIC_PREFIX = PushConstants.PUSH_USER_BASE_TOPIC_PREFLX + "huaWei/";

    public HuaweiPushBridgeService(MqttManager mqttManager, String applicationName) {
        super(mqttManager, applicationName,PUSH_USER_TOPIC_PREFIX);
    }

    @Override
    protected void unsubscribeOldTopic() {
        mqttManager.unregisterUserTopic(JPushBridgeService.PUSH_USER_TOPIC_PREFIX);
    }

    @Override
    public CloudBridgeService start() throws Exception {
        super.start();
        PushManager.requestToken(context);
        return this;
    }

    @Override
    public void close() throws Exception {
        super.close();
        PushManager.deregisterToken(context,regID);
    }

    @Override
    public CloudBridgeService clearAllArrivedMessages() {
        //huawei 未提供清除通知栏的api
        return super.clearAllArrivedMessages();
    }
}
