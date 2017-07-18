package com.ailicai.app.common.push.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.ailicai.app.common.push.bridge.JPushBridgeService;
import com.ailicai.app.common.push.bridge.PushBrigdgeService;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.MyPreference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.net.URISyntaxException;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by jeme on 2017/5/27.
 */

public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = "JIGUANG";
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(action)) {
            //只有第一次启动时才会手动此广播，所以需要做本地缓存
            String regID = intent.getExtras().getString(JPushInterface.EXTRA_REGISTRATION_ID);
            LogUtil.d(TAG, "[MyReceiver] 接收Registration Id : " + regID);

            if(!TextUtils.isEmpty(regID)) {
                MyPreference.getInstance().write(JPushBridgeService.JPUSH_REGID_KEY,regID);
                Intent intent0 = new Intent();
                intent0.setAction(PushBrigdgeService.INTERNAL_ACTION_BRIDGED_REGISTER_TOKEN);
                intent0.putExtra(PushBrigdgeService.INTERNAL_KEY_BRIDGED_REGISTER_TOKEN, regID);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent0);
            }
        }else if(JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)){
            String extra = intent.getStringExtra(JPushInterface.EXTRA_EXTRA);
            Map<String,String> map = JSON.parseObject(extra,new TypeReference<Map<String, String>>(){});
            LogUtil.d(TAG,"intent="+map.get("intent"));
            LogUtil.d(TAG,"content="+map.get("content"));
            try {
                Intent openIntent = Intent.parseUri(map.get("intent"),0);
                openIntent.putExtra("content",map.get("content"));
                openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(openIntent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
