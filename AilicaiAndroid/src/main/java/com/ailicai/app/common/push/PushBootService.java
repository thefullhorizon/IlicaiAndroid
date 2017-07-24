package com.ailicai.app.common.push;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.ailicai.app.common.push.bridge.HuaweiPushBridgeService;
import com.ailicai.app.common.push.bridge.JPushBridgeService;
import com.ailicai.app.common.push.model.PushMessage;
import com.ailicai.app.common.push.receiver.ConnectivityReceiver;
import com.ailicai.app.common.push.receiver.NotificationReceiver;
import com.ailicai.app.common.push.receiver.UserPresentReceiver;
import com.ailicai.app.common.push.utils.HuaweiPushUtils;
import com.ailicai.app.common.utils.LogUtil;
import com.manyi.inthingsq.android.CloudBridgeService;
import com.manyi.inthingsq.android.mipush.MiPushBridgeService;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by duo.chen on 2015/4/10.
 */
public class PushBootService extends Service {

    public static final String TAG = "com.ailicai.app.PushBootService";

    public static final String SERVICE_NAME = PushBootService.class.getName();
    public static final String CLEAR_NOTIFICATION = "clearNotification";

    private TelephonyManager telephonyManager;

    private BroadcastReceiver notificationReceiver;

    private BroadcastReceiver userPresentReceiver;

    private PhoneStateListener phoneStateListener;

    private ConnectivityReceiver connectivityReceiver;

    private ExecutorService executorService;

    private TaskSubmitter taskSubmitter;

    private TaskTracker taskTracker;

    private MqttManager mqttManager;

    private volatile boolean started;
    private volatile CloudBridgeService pushBridgeService;

    public PushBootService() {
        notificationReceiver = new NotificationReceiver();
        userPresentReceiver = new UserPresentReceiver(this);
        phoneStateListener = new PhoneStateChangeListener(this);
        connectivityReceiver = new ConnectivityReceiver(this);
        executorService = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new
                LinkedBlockingQueue<Runnable>());
        taskSubmitter = new TaskSubmitter(this);
        taskTracker = new TaskTracker(this);
    }

    public static Intent getIntent() {
        return new Intent(SERVICE_NAME);
    }

    @Override
    public void onCreate() {
        LogUtil.d(TAG, "PushBootService onCreate()");
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        PushConstants.mqttManager = this.mqttManager = new MqttManager(getApplicationContext());
        start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.getIntExtra(CLEAR_NOTIFICATION,0) == 1){
            if(pushBridgeService != null){
                //应用小米的实现类在jar包中，无法修改，需要单独修改
                if(pushBridgeService instanceof MiPushBridgeService){
                    MiPushClient.clearNotification(getApplicationContext());
                }else{
                    pushBridgeService.clearAllArrivedMessages();
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stop();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public TaskSubmitter getTaskSubmitter() {
        return taskSubmitter;
    }

    public TaskTracker getTaskTracker() {
        return taskTracker;
    }

    public MqttManager getMqttManager() {
        return mqttManager;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void connect() {
        taskSubmitter.submit(new Runnable() {
            public void run() {
                PushBootService.this.getMqttManager().reconnect();
            }
        });
    }

    private void registerNotificationReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(PushConstants.ACTION_MQTT_SHOW_NOTIFICATION);
        filter.addAction(PushConstants.ACTION_MQTT_NOTIFICATION_CLICKED);
        filter.addAction(PushConstants.ACTION_MQTT_NOTIFICATION_CLEARED);
        registerReceiver(notificationReceiver, filter);
    }

    private void unregisterNotificationReceiver() {
        unregisterReceiver(notificationReceiver);
    }

    private void registerConnectivityReceiver() {
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver,filter);
    }

    private void unregisterConnectivityReceiver() {
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(connectivityReceiver);
    }

    private void registerUserPresentReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(userPresentReceiver, filter);
    }

    private void unregisterUserPresentReceiver() {
        unregisterReceiver(userPresentReceiver);
    }

    public void start() {
        if (started) {
            return;
        }
        registerNotificationReceiver();
        registerConnectivityReceiver();
        registerUserPresentReceiver();
        taskSubmitter.submit(new Runnable() {
            public void run() {
                mqttManager.createAndConnect(null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken iMqttToken) {
                        //在推送的PushUiDispatcherActivity页面接收，因为此页面需要上报的数据可能因为mqtt未连接而发送失败
                        EventBus.getDefault().post(new PushMessage());
                        enablePush();
                    }

                    @Override
                    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                        LogUtil.e(TAG, "Fail to connecting to mqtt server! " + (throwable == null
                                ? "" : throwable.toString()));
                        if (throwable instanceof MqttStartErrorException) {
                            started = false;
                        }

                    }
                });
            }
        });
        started = true;
    }

    private void enablePush() {
        if (!started ) {
            return;
        }
        taskSubmitter.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!started) {
                        return;
                    }
                    //判断是否为小米手机
                    if(MiPushClient.shouldUseMIUIPush(getApplicationContext())){
                        pushBridgeService = new MiPushBridgeService(mqttManager.getAsyncMqttClient
                                (), PushConstants.MIPUSH_APP_NAME,
                                PushConstants.MIPUSH_APP_ID, PushConstants.MIPUSH_APP_KEY);
                        if(mqttManager.registerUserTopics(PushConstants.PUSH_MI_USER_TOPIC_PREFIX)){
                            //为了与旧版本兼容，订阅新主题前取消其余订阅
                            mqttManager.unregisterUserTopic(PushConstants.PUSH_USER_TOPIC_PREFIX);
                            mqttManager.unregisterUserTopic(HuaweiPushBridgeService.PUSH_USER_TOPIC_PREFIX);
                            mqttManager.unregisterUserTopic(JPushBridgeService.PUSH_USER_TOPIC_PREFIX);
                        }
                    }else if(!TextUtils.isEmpty(HuaweiPushUtils.getEmuiVersion())){//判断是否为华为手机
                        pushBridgeService = new HuaweiPushBridgeService(mqttManager,
                                PushConstants.HWPUSH_APP_NAME);
                    }else{
                        pushBridgeService = new JPushBridgeService(mqttManager,
                                PushConstants.JPUSH_APP_NAME);
                    }
                   if(pushBridgeService != null) {
                       pushBridgeService.start();
                   }
                } catch (Exception e) {
                    LogUtil.e(TAG, "Fail to start Push Brige Service!" + e.toString());
                }
            }
        });
    }

    protected void stop() {
        if (!started) {
            return;
        }
        started = false;
        unregisterNotificationReceiver();
        unregisterConnectivityReceiver();
        unregisterUserPresentReceiver();
        taskSubmitter.submit(new Runnable() {
            public void run() {

                try {
                    mqttManager.shutdown();
                } catch (MqttException ignored) {
                    LogUtil.e(TAG, "Fail to close mqtt manager! " + ignored.toString());
                }

                try {
                    if (pushBridgeService != null) {
                        pushBridgeService.close();
                    }
                } catch (Exception ignored) {
                    LogUtil.e(TAG, "Fail to stop MiPush Service! " + ignored.toString());
                }

            }
        });
        taskSubmitter.close();
    }

    public static final class TaskSubmitter {

        final PushBootService pushBootService;

        public TaskSubmitter(PushBootService pushBootService) {
            this.pushBootService = pushBootService;
        }

        @SuppressWarnings("rawtypes")
        public Future submit(Runnable task) {
            Future result = null;
            if (!pushBootService.getExecutorService().isTerminated()
                    && !pushBootService.getExecutorService().isShutdown()
                    && task != null) {
                result = pushBootService.getExecutorService().submit(task);
            }
            return result;
        }

        public void close() {
            pushBootService.getExecutorService().shutdown();
        }
    }

    public static final class TaskTracker {

        final PushBootService pushBootService;

        public int count;

        public TaskTracker(PushBootService pushBootService) {
            this.pushBootService = pushBootService;
            this.count = 0;
        }

        public void increase() {
            synchronized (pushBootService.getTaskTracker()) {
                pushBootService.getTaskTracker().count++;
            }
        }

        public void decrease() {
            synchronized (pushBootService.getTaskTracker()) {
                pushBootService.getTaskTracker().count--;
            }
        }
    }
}
