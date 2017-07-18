package com.ailicai.app.common.logCollect;

import com.ailicai.app.common.push.PushConstants;
import com.ailicai.app.common.utils.LogUtil;
import com.huoqiu.framework.util.StringUtil;

/**
 * Created by duo.chen on 2015/4/10.
 */
public class  LogCollectUtil {
    public static void pushLog(String log) {
        if (PushConstants.mqttManager == null || PushConstants.mqttManager.getAsyncMqttClient() == null || StringUtil.isEmptyNull(log)) {
            return;
        }
        try {
            PushConstants.mqttManager.pushLog(log);
        }catch (Exception e) {
            LogUtil.i(e.toString());
        }

    }
}
