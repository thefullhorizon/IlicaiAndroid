package com.ailicai.app.common.push.bridge;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.ailicai.app.common.constants.AILICAIBuildConfig;
import com.ailicai.app.common.push.MqttManager;
import com.ailicai.app.common.push.PushConstants;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.MyPreference;
import com.manyi.inthingsq.android.CloudBridgeService;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by jeme on 2017/5/8.
 */

public class JPushBridgeService extends PushBrigdgeService {

    public static final String PUSH_USER_TOPIC_PREFIX = PushConstants.PUSH_USER_BASE_TOPIC_PREFLX + "jpush/";
    public static final String JPUSH_REGID_KEY= "jpush_redId_key";

    public JPushBridgeService(MqttManager mqttManager, String applicationName) {
        super(mqttManager, applicationName,PUSH_USER_TOPIC_PREFIX);
    }

    @Override
    protected void unsubscribeOldTopic() {
        mqttManager.unregisterUserTopic(HuaweiPushBridgeService.PUSH_USER_TOPIC_PREFIX);
    }

    @Override
    public CloudBridgeService start() throws Exception {
        super.start();
        JPushInterface.setDebugMode(!AILICAIBuildConfig.isProduction());
//        if(JPushInterface.isPushStopped(context)) {
            JPushInterface.init(context);
//        }else{
        if(JPushInterface.isPushStopped(context)) {
            JPushInterface.resumePush(context);
        }
//        }

        //极光推送只有在第一次连接服务器时才发送获取id的广播，其他情况不再广播
        //所以需要手动获取id,但此处可能因为推送未初始化完成而导致未收到id
        //所以发现为空时，再从本地获取一次，本地数据在第一次广播时进行保存
        String regId = JPushInterface.getRegistrationID(context);
        LogUtil.d("JPushInterface","regId=" + regId);

        if(TextUtils.isEmpty(regId)){
            regId = MyPreference.getInstance().read(JPUSH_REGID_KEY,"");
        }
        if(!TextUtils.isEmpty(regId)) {
            Intent intent0 = new Intent();
            intent0.setAction(PushBrigdgeService.INTERNAL_ACTION_BRIDGED_REGISTER_TOKEN);
            intent0.putExtra(PushBrigdgeService.INTERNAL_KEY_BRIDGED_REGISTER_TOKEN, regId);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent0);
        }
        return this;
    }

    @Override
    public void close() throws Exception {
        super.close();
        JPushInterface.stopPush(context);
    }




}
