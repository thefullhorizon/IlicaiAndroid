package com.ailicai.app.common.push.bridge;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ailicai.app.common.push.MqttManager;
import com.ailicai.app.common.push.PushConstants;
import com.alibaba.fastjson.JSON;
import com.manyi.inthingsq.AsyncMqttClient;
import com.manyi.inthingsq.android.CloudBridgeService;
import com.manyi.inthingsq.android.service.MqttAndroidClient;
import com.xiaomi.mipush.sdk.Logger;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by jeme on 2017/5/8.
 */

public abstract class PushBrigdgeService implements CloudBridgeService {

    private final String TAG = "PushBrigdgeService";
    private static final String SERVICE_NAME = "com.manyi.lovehouse.common.push.bridge.PushBrigdgeService";/*PushBrigdgeService.class.getPackage().getName() + "."+ PushBrigdgeService.class.getSimpleName();*/
    public static final String INTERNAL_ACTION_BRIDGED_MESSAGE_ARRIVED = SERVICE_NAME+".INTERNAL_ACTION_BRIDGED_MESSAGE_ARRIVED";
    public static final String INTERNAL_KEY_BRIDGED_MESSAGE_ARRIVED = SERVICE_NAME+".INTERNAL_KEY_BRIDGED_MESSAGE_ARRIVED";
    public static final String INTERNAL_ACTION_BRIDGED_REGISTER_TOKEN = SERVICE_NAME+".INTERNAL_ACTION_BRIDGED_REGISTER_TOKEN";
    public static final String INTERNAL_KEY_BRIDGED_REGISTER_TOKEN = SERVICE_NAME+".INTERNAL_KEY_BRIDGED_REGISTER_TOKEN";

    public static final String SCHEDULER_ACTION = "com.manyi.lovehouce.common.push.PushBridgeService.ACTION_SCHEDULER";

    protected final Context context;
    protected final String clientID;
    protected MqttManager mqttManager = null;
    protected final MqttAndroidClient mqttClient;
    protected final String tokenSubscribeTopic;
    protected final String tokenUnsubscribeTopic;

    protected volatile boolean started = false;
    private volatile PendingIntent scheduleOperation;
    protected String regID;

    private static final String PUSH_SHARED_PREFERENCE = SERVICE_NAME+".SHARED_PREFERENCE";
    private static final String PREFERENCE_KEY_PUSH_LAST_SESSIONS = SERVICE_NAME+".PUSH_LAST_SESSIONS";

    /*private static final String EXTRA_KEY_MESSAGE_RETAINED = "MSG_retained";
    private static final String EXTRA_KEY_MESSAGE_QOS = "MSG_QOS";

    public static final String EXTRA_KEY_MESSAGE_TOPIC = "MSG_TOPIC";
    public static final String DEFAULT_TOPIC_NAME = "UNKNOWN";
    public static final String ACTION_BRIDGED_MESSAGE = SERVICE_NAME+".ACTION_BRIDGED_MESSAGE";
    public static final String KEY_BRIDGED_MESSAGE = SERVICE_NAME+".KEY_BRIDGED_MESSAGE";
    public static final String KEY_CLICKED_MIPUSH_MESSAGE = PushMessageHelper.KEY_MESSAGE;*/


    protected final BroadcastReceiver bridgedMessageReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle extras = intent.getExtras();
            if (INTERNAL_ACTION_BRIDGED_REGISTER_TOKEN.equals(action)) {
                regID = extras.getString(INTERNAL_KEY_BRIDGED_REGISTER_TOKEN);
                if(regID == null || regID.trim().equals("")){
                    Log.e(TAG, "Fail to register client[" + clientID + "], Invalid regID:" + regID);
                    return;
                }
                onReceiveRegisterResult(regID);
            }else if (INTERNAL_ACTION_BRIDGED_MESSAGE_ARRIVED.equals(action)) {
                //没有使用到透传信息
               /* Bundle bundle = extras.getBundle(INTERNAL_KEY_BRIDGED_MESSAGE_ARRIVED);
                if(bundle == null){
                    return;
                }
                onNotificationMessageArrived(MiPushMessage.fromBundle(bundle));*/
            }
            else {
                // unrecognized action
                Log.w(TAG, "unrecognized message action:["+action+"]. associated intent:"+intent);
            }
        }
    };

    protected final BroadcastReceiver schedulerReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!started){
                return;
            }

            String regID = intent.getStringExtra("regID");
            if (regID == null) {
                return;
            }

            if(scheduleOperation == null){
                return;
            }

            if(isDirtySession()){
                cleanDirtySession();
            }
            onReceiveRegisterResult(regID);
        }
    };

    /***
     * 取消之前的订阅
     * 已经统一取消了小米和老版本的topic
     */
    protected abstract void unsubscribeOldTopic();

    public PushBrigdgeService(MqttManager mqttManager,
                              String applicationName, String userTopic) {
        this(mqttManager, new String[]{"token/subscribe/"+applicationName, "token/unsubscribe/"+applicationName});
        registerUserTopic(userTopic);
    }

    public PushBrigdgeService(MqttManager mqttManager,
                              String[] subscribeTipic) {
        this.mqttManager = mqttManager;
        this.mqttClient = mqttManager.getAsyncMqttClient();
        this.context = this.mqttClient.getContext();
        this.clientID = mqttClient.getClientId();

        this.tokenSubscribeTopic = subscribeTipic[0];
        this.tokenUnsubscribeTopic = subscribeTipic[1];
    }

    private void registerUserTopic(String userTopic){
        if(mqttManager.registerUserTopics(userTopic)){
            //此topic为老版本的，为了兼容新版直接删除
            mqttManager.unregisterUserTopic(PushConstants.PUSH_USER_TOPIC_PREFIX);
            //因为小米推送不继承此类，此处可直接取消
            mqttManager.unregisterUserTopic(PushConstants.PUSH_MI_USER_TOPIC_PREFIX);

            unsubscribeOldTopic();
        }
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public CloudBridgeService start() throws Exception {
        if(started){
            throw new IllegalStateException("Current service already started before!");
        }

        //如果有脏数据，则清除
        if(isDirtySession()){
            this.cleanDirtySession(); // started before? close it first
        }

        IntentFilter bridgedMsgFilter = new IntentFilter();
        bridgedMsgFilter.addAction(INTERNAL_ACTION_BRIDGED_REGISTER_TOKEN);
        bridgedMsgFilter.addAction(INTERNAL_ACTION_BRIDGED_MESSAGE_ARRIVED);
        LocalBroadcastManager.getInstance(context).registerReceiver(bridgedMessageReceiver, bridgedMsgFilter);
        this.context.registerReceiver(this.schedulerReceiver, new IntentFilter(SCHEDULER_ACTION));

        if(!mqttClient.isTraceEnabled()){
            Logger.disablePushFileLog(this.context);
        }
        addPersistedSession(clientID);
        started = true;
        return this;
    }

    @Override
    public void close() throws Exception {
        this.cancelScheduler();
        if(started && isConnected(mqttClient)){
            // current connection already started
            Log.i(TAG, "Prepare to un-bridge device token for current client:" + clientID);
            unsubscribeToken(clientID);
            clearPersistedSession(clientID);
        }
        this.context.unregisterReceiver(this.schedulerReceiver);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(bridgedMessageReceiver);
    }

    @Override
    public CloudBridgeService clearArrivedMessage(int messageID) {
        return this;
    }

    @Override
    public CloudBridgeService clearAllArrivedMessages() {
        return this;
    }

    public synchronized void onReceiveRegisterResult(String regID) {
        if(!started){
            return;
        }
        if(isConnected(mqttClient)){
            subscribeToken(clientID, regID);
        }else{
            // try-later
            scheduleSubscribeToken(regID);
        }
    }

    /***
     * 如果mqtt未连接，则稍后上传token
     * @param regID token
     */
    private void scheduleSubscribeToken(String regID) {
        this.cancelScheduler();
        Intent intent = new Intent(SCHEDULER_ACTION);
        intent.putExtra("regID", regID);
        scheduleOperation = PendingIntent.getBroadcast(this.context, clientID.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long reconnectTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15);
        AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(Service.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, reconnectTime, scheduleOperation);
    }

    protected void cancelScheduler() {
        if(scheduleOperation != null) {
            AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(Service.ALARM_SERVICE);
            alarmManager.cancel(scheduleOperation);
            scheduleOperation = null;
        }
    }

    /***
     * 在mqtt服务上订阅topic
     */
    protected void subscribeToken(String clientID, String token) {
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("clientID", clientID);
        tokenMap.put("token", token);
        try {
            this.mqttClient.publish(this.tokenSubscribeTopic, JSON.toJSONString(tokenMap).getBytes(), AsyncMqttClient.QOS_MOST_ONCE, false);
            Log.i(TAG, "successfully bridge device token for client: " + tokenMap);
        }
        catch (MqttException ignored) {
            Log.e(TAG, "Fail to bridge device token for client[" + clientID + "]. target topic:" + this.tokenSubscribeTopic, ignored);
        }
    }

    /***
     * 在mqtt服务上取消订阅topic
     */
    protected void unsubscribeToken(String clientID){
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("clientID", clientID);
        try {
            this.mqttClient.publish(this.tokenUnsubscribeTopic, JSON.toJSONString(tokenMap).getBytes(), AsyncMqttClient.QOS_MOST_ONCE, false);
            Log.i(TAG, "successfully un-bridge device token for client: "+tokenMap);
        }
        catch (MqttException ignored) {
            Log.e(TAG, "Fail to un-bridge device token for client[" + clientID + "]. target topic:"+this.tokenUnsubscribeTopic, ignored);
        }
    }

    /*public void onNotificationMessageArrived(PushMessage message) {
        AndroidMqttMessage mqttMessage;
        try {
            mqttMessage = toMqttMessage(message);
        }
        catch (Exception ignored) {
            Log.d(TAG, "Fail to parse MQTTMessage from MiPush context.", ignored);
            mqttMessage = toPassThroughMqttMessage(message.getContent().getBytes());
        }

        MqttCallback callback = mqttClient.getCallback();
        if(callback == null){
            Intent data = new Intent(ACTION_BRIDGED_MESSAGE);
            data.putExtra(KEY_BRIDGED_MESSAGE, mqttMessage);
            context.sendBroadcast(data);
            return;
        }

        Map<String, String> extras = message.getExtra();
        String topic = DEFAULT_TOPIC_NAME;
        if (extras.containsKey(EXTRA_KEY_MESSAGE_TOPIC)) {
            topic = extras.get(EXTRA_KEY_MESSAGE_TOPIC);
        }

        try {
            callback.messageArrived(topic, mqttMessage);
        }
        catch (Exception ignored) {
            Log.e(TAG, "Fail to invoke message arrived callback from MiPush Context:" + callback, ignored);
        }
    }*/

    protected boolean isDirtySession(){
        return !getPersistedSessions().isEmpty();
    }
    protected void cleanDirtySession() {
        if(isConnected(mqttClient)){
            for(String previousSession : getPersistedSessions()){
                Log.i(TAG,"Prepare to un-bridge device token for persisted client:"+previousSession);
                unsubscribeToken(previousSession);
                clearPersistedSession(previousSession);
            }
        }
    }
    protected boolean isConnected(MqttAndroidClient mqttClient){
        try {
            return mqttClient.isConnected();
        }
        catch (Exception e) {
            return false;
        }
    }
    /* private static int parseQos(String qosStr) {
        if (qosStr == null) {
            return AsyncMqttClient.QOS_MOST_ONCE;
        }
        String trimQosStr = qosStr.trim();
        if (trimQosStr.equals("") || trimQosStr.toCharArray().length != 1) {
            return AsyncMqttClient.QOS_MOST_ONCE;
        }
        char qosFlag = trimQosStr.toCharArray()[0];
        switch (qosFlag) {
            case '0': return AsyncMqttClient.QOS_MOST_ONCE;
            case '1': return AsyncMqttClient.QOS_LEAST_ONCE;
            case '2': return AsyncMqttClient.QOS_EXACTLY_ONCE;
            default: return AsyncMqttClient.QOS_MOST_ONCE;
        }
    }

    private static boolean parseRetained(String retainStr) {
        try {
            return Boolean.parseBoolean(retainStr);
        }
        catch (Exception e) {
            return false;
        }
    }

   public static AndroidMqttMessage toMqttMessage(PushMessage message) {
        Map<String, String> extras = message.getExtra();
        AndroidMqttMessage bridgedMessage = new AndroidMqttMessage();
        bridgedMessage.setBridged(true);
        bridgedMessage.setQos(parseQos(extras.get(EXTRA_KEY_MESSAGE_QOS)));
        bridgedMessage.setMessageId(message.getNotifyId());
        bridgedMessage.setDuplicate(message.isNotified());
        bridgedMessage.setRetained(parseRetained(extras.get(EXTRA_KEY_MESSAGE_RETAINED)));
        bridgedMessage.setPayload(message.getContent().getBytes());
        return bridgedMessage;
    }

    public static AndroidMqttMessage toPassThroughMqttMessage(byte[] payload) {
        AndroidMqttMessage bridgedMessage = new AndroidMqttMessage();
        bridgedMessage.setBridged(true);
        bridgedMessage.setQos(AsyncMqttClient.QOS_MOST_ONCE);
        bridgedMessage.setMessageId(0);
        bridgedMessage.setDuplicate(false);
        bridgedMessage.setRetained(false);
        bridgedMessage.setPayload(payload);
        return bridgedMessage;
    }*/

    // ------------------ session persist --------------------
    protected synchronized Set<String> getPersistedSessions() {
        SharedPreferences preferences = context.getSharedPreferences(PUSH_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return preferences.getStringSet(PREFERENCE_KEY_PUSH_LAST_SESSIONS, Collections.<String>emptySet());
    }

    protected synchronized void addPersistedSession(String clientID){
        SharedPreferences preferences = context.getSharedPreferences(PUSH_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        Set<String> persistedSessions = new HashSet<String>(preferences.getStringSet(PREFERENCE_KEY_PUSH_LAST_SESSIONS, Collections.<String>emptySet()));
        persistedSessions.add(clientID);
        updateSharedPreferences(preferences, persistedSessions);
    }

    protected synchronized void clearPersistedSession(String clientID){
        SharedPreferences preferences = context.getSharedPreferences(PUSH_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        Set<String> persistedSessions = new HashSet<String>(preferences.getStringSet(PREFERENCE_KEY_PUSH_LAST_SESSIONS, Collections.<String>emptySet()));
        if(persistedSessions.remove(clientID)){
            updateSharedPreferences(preferences, persistedSessions);
        }
    }

    protected synchronized void updateSharedPreferences(SharedPreferences preferences, Set<String> updatedSessions) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(PREFERENCE_KEY_PUSH_LAST_SESSIONS, updatedSessions);
        if(!editor.commit()){
            editor.apply();
        }
    }
}
