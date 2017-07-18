package com.ailicai.app.common.push;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ailicai.app.common.constants.AILICAIBuildConfig;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.ui.login.UserInfo;
import com.huoqiu.framework.util.LogUtil;
import com.manyi.inthingsq.AsyncMqttClient;
import com.manyi.inthingsq.ClientProperties;
import com.manyi.inthingsq.android.AndroidAsyncMqttClient;
import com.manyi.inthingsq.android.AndroidClientBuilder;
import com.manyi.inthingsq.android.AndroidMqttCallback;
import com.manyi.inthingsq.android.AndroidMqttMessage;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by duo.chen on 2015/4/8.
 */
public class MqttManager {
    public static final String TAG = "MqttManager";
    public static final Charset DEFAULT_MESSAGE_ENCODING = Charset.forName("UTF-8");
    private volatile boolean topicRegistered;
    private Map<String, Integer> topics = new ConcurrentHashMap<>();

    private volatile ConnectionStatus connectionStatus = ConnectionStatus.READY;
    private Context mContext;
    private AndroidAsyncMqttClient asyncMqttClient;
    private boolean sessionPresentIsOnServer;

    public MqttManager(Context context) {
        mContext = context;
//        registerUserTopics(PushConstants.PUSH_USER_TOPIC_PREFIX);
    }

    /**
     * Handles call backs from the MQTT Client
     */
    private AndroidMqttCallback callbackHandler = new AndroidMqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            AndroidAsyncMqttClient asyncMqttClient = getAsyncMqttClient();
            if (asyncMqttClient == null || asyncMqttClient.isClosed()) {
                return;
            }
            if (isConnectedOrConnecting()) {
                setConnectionStatus(MqttManager.ConnectionStatus.ERROR);
            }

            if (isConnectError() && isCleanSession()) {
                reconnect();
            }
            // for non-clean session, MQTTService will make reconnect call automatically
        }

        @Override
        public void connectionEstablished(boolean sessionPresentOnServer) {
            if (isConnectError()) {
                setConnectionStatus(MqttManager.ConnectionStatus.CONNECTED);
            }

            // 1. always subscribe topics on first connection
            // 2. always subscribe topics if current connection is clean
            // 3. for non-clean session, check whether it's status present on server(mqtt 3.1.1)

            sessionPresentIsOnServer = sessionPresentOnServer;
            //由于在注册topic时需要根据具体的push渠道来注册不同的topic，此处还未设置push渠道，所以没有topic的值，需要延后处理
            uploadRegisteredTopics();
        }

        /**
         * 收到推送消息(通过广播 发送消息)
         * @param topic 该消息所在topic
         * @param message payoad
         */
        @Override
        public void messageArrived(String topic, AndroidMqttMessage message) throws Exception {
            LogUtil.i(MqttManager.TAG, "receive new mqtt message on topic: " + topic);
            try {
                Intent intent = createIntent(topic, message);
                intent.putExtra("bridged", message.isBridged());
                mContext.sendBroadcast(intent);
            } catch (Exception ignore) {
                LogUtil.i(MqttManager.TAG, "fail to process message " + message + " on topic: " +
                        topic, ignore);
            }
        }

        @Override
        public void unknownMessageArrived(String topic, MqttMessage message) throws Exception {
            LogUtil.i(MqttManager.TAG, "receive unknown mqtt message on topic: " + topic);
            try {
                mContext.sendBroadcast(createIntent(topic, message));
            } catch (Exception ignore) {
                LogUtil.i(MqttManager.TAG, "fail to process message " + message + " on topic: " +
                        topic, ignore);
            }
        }

        private Intent createIntent(String topic, MqttMessage message) {
            Intent intent = new Intent(PushConstants.ACTION_MQTT_SHOW_NOTIFICATION);
            intent.putExtra("message", message.getPayload());
            intent.putExtra("topic", topic);
            intent.putExtra("duplicated", message.isDuplicate());
            intent.putExtra("retained", message.isRetained());
            return intent;
        }
    };
    private void uploadRegisteredTopics(){
        if(getRegisteredTopics().size() == 0){
            return;
        }
        if (!topicRegistered) {
            LogUtil.d(TAG, "prepared to subscribe fresh topics:" + getRegisteredTopics());
            subscribeRegisteredTopics();
            topicRegistered = true;
        } else if (isCleanSession() || (getMqttVersion() < MqttConnectOptions
                .MQTT_VERSION_3_1_1 || !sessionPresentIsOnServer)) {
            subscribeRegisteredTopics();
        } else {
            // non-clean session && mqttVersion >= 3.1.1 && session present on server
            LogUtil.d(TAG, "MQTT session already presented on server, no need to subscribe " +
                    "topics.");
        }
    }

    public synchronized AndroidAsyncMqttClient createAndConnect() {
        return this.createAndConnect(null, null);
    }

    public synchronized AndroidAsyncMqttClient createAndConnect(Object context,
                                                                IMqttActionListener callback) {

        LogUtil.d(TAG, "try to createAndConnect");

        if (this.asyncMqttClient != null) {
            LogUtil.d(TAG, "Current client already connected! close it before connect.");
            return getAsyncMqttClient();
        }

        final AndroidClientBuilder clientBuilder = new AndroidClientBuilder(mContext);
        try {
            String clientID = getClientIdByMobile(getMobile());

            // basic client params
            clientBuilder.setClientID(clientID);
            clientBuilder.setContext(this.mContext);
            clientBuilder.setUsername(PushConstants.username);
            clientBuilder.setPassword(PushConstants.password);

            if (PushConstants.usingTLS) {
                clientBuilder.setTrustStore(loadKeyStore(mContext.getAssets().open(PushConstants
                        .KEYSTORE_FILE), PushConstants.KEYSTORE_PWD));
            }

            boolean traceEnable = true;
            // fetch config from remote server
            String configUri = PushConstants.TEST_FETCH_CONFIG_URL;
            if (AILICAIBuildConfig.isProduction()) {
                configUri = PushConstants.OFF_FETCH_CONFIG_URL;
                traceEnable = false;
            } else if (AILICAIBuildConfig.isBeta()) {
                configUri = PushConstants.BETA_FETCH_CONFIG_URL;
                traceEnable = true;
            } else if (AILICAIBuildConfig.isDebug()) {
                configUri = PushConstants.TEST_FETCH_CONFIG_URL;
                traceEnable = true;
            }

            ClientProperties<CharSequence, CharSequence> clientProperties = clientBuilder
                    .getClientProperties();
            clientBuilder.fetchConfigFromUrl(configUri, PushConstants.username, PushConstants
                            .password, clientProperties, null, null,
                    loadKeyStore(mContext.getAssets().open(PushConstants.KEYSTORE_FILE),
                            PushConstants.KEYSTORE_PWD), null);

            asyncMqttClient = clientBuilder.build();
            asyncMqttClient.setCallback(this.callbackHandler);
            asyncMqttClient.setTraceEnabled(traceEnable);
            asyncMqttClient.setTraceCallback(new MqttTraceCallback());
            this.connect(asyncMqttClient, context, callback);
            return this.asyncMqttClient;
        } catch (Throwable e) {
            LogUtil.d(TAG, "Fail to build mqtt client!", e);
            callback.onFailure(null,new MqttStartErrorException());
            return null;
        }
    }

    private IMqttToken connect(AndroidAsyncMqttClient client) {
        return this.connect(client, null, null);
    }

    private IMqttToken connect(final AndroidAsyncMqttClient client, Object context, final
    IMqttActionListener connectCallback) {
        IMqttToken connectToken = null;
        if (null != client) {
            final String clientId = client.getClientId();
            try {
                setConnectionStatus(ConnectionStatus.CONNECTING);
                connectToken = client.connect(context, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken iMqttToken) {
                        setConnectionStatus(ConnectionStatus.CONNECTED);
                        LogUtil.i(TAG, "Successfully connect to MQTT server! client:" + clientId);
                        if (connectCallback != null) {
                            connectCallback.onSuccess(iMqttToken);
                        }
                    }

                    @Override
                    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                        setConnectionStatus(ConnectionStatus.ERROR);
                        LogUtil.e(TAG, "Fail to connecting to mqtt server! client:" + clientId,
                                throwable);
                        if (connectCallback != null) {
                            connectCallback.onFailure(iMqttToken, throwable);
                        }
                    }
                });
            } catch (MqttException e) {
                if (isConnecting()) {
                    setConnectionStatus(ConnectionStatus.ERROR);
                }
                LogUtil.e(TAG, "Fail to connecting to mqtt server! client:" + clientId, e);
                if (connectCallback != null) {
                    connectCallback.onFailure(null, e);
                }
            }
        }
        return connectToken;
    }

    public synchronized void reconnect() {
        if (asyncMqttClient == null || getAsyncMqttClient().isClosed()) {
            LogUtil.d(TAG, "Current mqtt client not exist or already closed!");
        }
        if (isConnectedOrConnecting()) {
            LogUtil.d(TAG, "Current connection already/prepared to connect mqtt server!");
            return;
        }
        if (isDisconnecting()) {
            LogUtil.d(TAG, "Current connection prepared to disconnect from mqtt server!");
            return;
        }
        if (isConnectNoError()) {
            LogUtil.d(TAG, "Current connection not in error status!");
            return;
        }
        this.connect(this.asyncMqttClient);
    }

    public synchronized void shutdown() throws MqttException {
        if (asyncMqttClient == null) {
            return;
        }
        this.unsubscribeRegisteredTopics();
        this.asyncMqttClient.unregisterResources();
        this.asyncMqttClient.setCallback(null);
        this.asyncMqttClient.setTraceCallback(null);
        this.asyncMqttClient = null;
        this.setConnectionStatus(ConnectionStatus.READY);
        this.topicRegistered = false;
    }

    private void disconnect(int timeout) {
        this.disconnect(timeout, null, null);
    }

    private void disconnect(final int timeout, final Object context, final IMqttActionListener
            disconnectCallback) {
        if (null == this.asyncMqttClient) {
            LogUtil.d(TAG, "Current connection didn't exist! =");
            return;
        }
        //if the client is not connected, process the disconnect
        if (isDisconnectingOrDisconnected()) {
            LogUtil.d(TAG, "Current connection already/prepared to disconnect from mqtt server!");
            return;
        }
        if (isConnecting() || isReady()) {
            LogUtil.d(TAG, "Current connection didn't connected with mqtt server!");
            return;
        }

        IMqttToken mqttToken = null;
        try {
            setConnectionStatus(ConnectionStatus.DISCONNECTING);
            mqttToken = asyncMqttClient.disconnect(timeout, context, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    if (isConnected() || isDisconnecting()) {
                        setConnectionStatus(MqttManager.ConnectionStatus.DISCONNECTED);
                    }
                    if (disconnectCallback != null) {
                        disconnectCallback.onSuccess(iMqttToken);
                    }
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    setConnectionStatus(MqttManager.ConnectionStatus.DISCONNECTED);
                    if (disconnectCallback != null) {
                        disconnectCallback.onFailure(iMqttToken, throwable);
                    }
                }
            });
            mqttToken.waitForCompletion((long) (timeout + (timeout * 0.25)));
        } catch (Exception e) {
            LogUtil.d(TAG, "Fail to disconnect from mqtt server, try to terminate connection!", e);
            try {
                asyncMqttClient.disconnectForcibly();
                setConnectionStatus(MqttManager.ConnectionStatus.DISCONNECTED);
                LogUtil.i(TAG, "Successfully terminate TCP connection.");
                if (disconnectCallback != null) {
                    disconnectCallback.onSuccess(mqttToken);
                }
            } catch (Exception ignored) {
                LogUtil.i(TAG, "Fail to forcibly disconnect from mqtt server", ignored);
                setConnectionStatus(MqttManager.ConnectionStatus.DISCONNECTED);
                if (disconnectCallback != null) {
                    disconnectCallback.onFailure(mqttToken, ignored);
                }
            }
        }
    }

    // --------------------------------- business delegate  ------------------------
    private void subscribeRegisteredTopics() {
        Set<Map.Entry<String, Integer>> entrySet = getRegisteredTopics().entrySet();
        int size = entrySet.size();
        if (size <= 0) {
            return;
        }

        List<String> registeredTopics = new ArrayList<String>(size);
        List<Integer> qos = new ArrayList<Integer>(size);
        for (Map.Entry<String, Integer> entry : entrySet) {
            registeredTopics.add(entry.getKey());
            qos.add(entry.getValue());
        }
        try {
            subscribe(registeredTopics, qos);
        } catch (Exception ignored) {
            LogUtil.d(TAG, "fail to subscribe registered topics:" + registeredTopics, ignored);
        }
    }

    public IMqttToken subscribe(final String topic, final int qos) {
        return this.subscribe(Collections.singletonList(topic), Collections.singletonList(qos));
    }

    public IMqttToken subscribe(final String topic, final int qos, Object context, final
    IMqttActionListener subscribeCallback) {
        return this.subscribe(Collections.singletonList(topic), Collections.singletonList(qos),
                context, subscribeCallback);
    }

    public IMqttToken subscribe(final List<String> topics, final List<Integer> qos) {
        return this.subscribe(topics, qos, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                LogUtil.d(TAG, "Successfully subscribe to topics: " + topics + ". associated qos " +
                        "level:" + qos);
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                LogUtil.d(TAG, "Fail to subscribe to topics: " + topics, throwable);
            }
        });
    }

    public IMqttToken subscribe(final List<String> topics, final List<Integer> qos, Object
            context, final IMqttActionListener subscribeCallback) {
        if (null == this.asyncMqttClient || !isConnected()) {
            return null;
        }
        int size = topics.size();
        if (size != qos.size()) {
            throw new IllegalArgumentException("unmatched qos length! topic filter's length must " +
                    "same as qos! topics=" + size + ",qos=" + qos.size());
        }
        final String[] topicArray = topics.toArray(new String[size]);
        final int[] qosArray = new int[size];
        for (int i = 0; i < qosArray.length; i++) {
            qosArray[i] = qos.get(i);
        }
        try {
            return this.asyncMqttClient.subscribe(topicArray, qosArray, context, new
                    IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken iMqttToken) {
                            if (subscribeCallback != null) {
                                try {
                                    subscribeCallback.onSuccess(iMqttToken);
                                } catch (Exception ignored) {
                                }
                            }
                            for (int i = 0; i < topicArray.length; i++) {
                                registerTopic(topicArray[i], qosArray[i]);
                            }
                        }

                        @Override
                        public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                            if (subscribeCallback != null) {
                                subscribeCallback.onFailure(iMqttToken, throwable);
                            }
                        }
                    });
        } catch (MqttException e) {
            LogUtil.d(TAG, "Fail to subscribe mqtt topics:" + Arrays.toString(topicArray), e);
            if (subscribeCallback != null) {
                subscribeCallback.onFailure(null, e);
            }
            return null;
        }
    }

    public IMqttToken unsubscribe(String topic) {
        return this.unsubscribe(Collections.singleton(topic));
    }

    public IMqttToken unsubscribe(final Set<String> topics) {
        return this.unsubscribe(topics, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                LogUtil.d(TAG, "Successfully unsubscribe topics: " + topics);
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                LogUtil.d(TAG, "Fail to unsubscribe to topics: " + topics, throwable);
            }
        });
    }

    public IMqttToken unsubscribe(final Set<String> topics, final Object context, final
    IMqttActionListener unsubscribeCallback) {
        if (null == this.asyncMqttClient || !isConnected()) {
            return null;
        }
        try {
            return this.asyncMqttClient.unsubscribe(topics.toArray(new String[topics.size()]),
                    context, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken iMqttToken) {
                            if (unsubscribeCallback != null) {
                                try {
                                    unsubscribeCallback.onSuccess(iMqttToken);
                                } catch (Exception ignored) {
                                }
                            }
                            for (String topic : topics) {
                                deregisterTopic(topic);
                            }
                        }

                        @Override
                        public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                            if (unsubscribeCallback != null) {
                                unsubscribeCallback.onFailure(iMqttToken, throwable);
                            }
                        }
                    });
        } catch (MqttException e) {
            LogUtil.d(TAG, "Fail to unsubscribe mqtt topics:" + topics, e);
            if (unsubscribeCallback != null) {
                unsubscribeCallback.onFailure(null, e);
            }
            return null;
        }
    }

    private void unsubscribeRegisteredTopics() {
        try {
            unsubscribe(getRegisteredTopics().keySet());
        } catch (Exception ignored) {
            LogUtil.d(TAG, "fail to unsubscribe registered topics:" + getRegisteredTopics()
                    .keySet(), ignored);
        }
    }

    public IMqttDeliveryToken publish(final String topic, byte[] message) {
        return this.publish(topic, message, false, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                LogUtil.d(TAG, "Successfully publish message to topic:" + topic);
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                LogUtil.d(TAG, "Fail to publish message to topic:" + topic, throwable);
            }
        });
    }

    public IMqttDeliveryToken publish(String topic, byte[] message, boolean retained, Object
            context, IMqttActionListener publishCallback) {
        if (null == this.asyncMqttClient || !isConnected()) {
            return null;
        }

        try {
            return this.asyncMqttClient.publish(topic, message, AsyncMqttClient.QOS_MOST_ONCE,
                    retained, context, publishCallback);
        } catch (MqttException e) {
            LogUtil.d(TAG, "Fail to publish message[" + Arrays.toString(message) + "] to mqtt " +
                    "topic:" + topic, e);
            publishCallback.onFailure(null, e);
        }
        return null;
    }

    public void pushLog(String message) {
        publish(PushConstants.LOG_TOPIC, message.getBytes(DEFAULT_MESSAGE_ENCODING));
    }

    public void pushMessage(String message) {
        publish(PushConstants.PUSH_USER_TOPIC_PREFIX, message.getBytes(DEFAULT_MESSAGE_ENCODING));
    }

    /**
     * Determines if the client is in a state of connecting or connected.
     *
     * @return if the client is connecting or connected
     */
    public boolean isConnectedOrConnecting() {
        return isConnected() || isConnecting();
    }

    public boolean isDisconnectingOrDisconnected() {
        return isDisconnecting() || isDisconnected();
    }

    /**
     * Client is currently not in an error state
     *
     * @return true if the client is in not an error state
     */
    public boolean isConnectNoError() {
        return connectionStatus != ConnectionStatus.ERROR;
    }

    public boolean isConnectError() {
        return connectionStatus == ConnectionStatus.ERROR;
    }

    /**
     * Determines if the client is connected
     *
     * @return is the client connected
     */
    public boolean isConnected() {
        return connectionStatus == ConnectionStatus.CONNECTED;
    }

    public boolean isConnecting() {
        return connectionStatus == ConnectionStatus.CONNECTING;
    }

    public boolean isReady() {
        return connectionStatus == ConnectionStatus.READY;
    }

    public boolean isDisconnecting() {
        return connectionStatus == ConnectionStatus.DISCONNECTING;
    }

    public boolean isDisconnected() {
        return connectionStatus == ConnectionStatus.DISCONNECTED;
    }

    void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public Context getContext() {
        return mContext;
    }

    private boolean isCleanSession() {
        return getAsyncMqttClient().getDefaultOptions().isCleanSession();
    }

    private int getMqttVersion() {
        MqttConnectOptions connectOptions = getAsyncMqttClient().getDefaultOptions();
        return connectOptions.getMqttVersion();
    }

    /**
     * @return 获取登陆用户的手机号
     */
    public static String getMobile() {
        return UserInfo.getInstance().getUserMobileID();
    }

    /**
     * @return 获取ClientId ClientID 标识模式:(平台名称)_(应用名称)_(用户名)_(设备唯一标识).
     */
    public static String getClientIdByMobile(String mobile){
        String uuid = getUUidByMobile(mobile);
        mobile = "_" + (TextUtils.isEmpty(mobile) ? "ANONYMOUS" : mobile);
        if (!TextUtils.isEmpty(uuid)) {
            uuid = "_" + uuid;
        }
        return PushConstants.clientId + mobile + uuid;
    }

    /**
     * @return 获取App的UUID 随机生成
     */
    public static String getUUidByMobile(String mobile) {
        String uuid = UUID.randomUUID().toString();

        // 这里无需判断mobile是否为""
        uuid = MyPreference.getInstance().read("MQTT_UUID" + mobile, uuid);
        MyPreference.getInstance().write("MQTT_UUID" + mobile, uuid);

        return uuid;
    }

    /**
     * @return 获取推送Topic
     */
    public String getPushUserTopic(String userTopic) {
        String mobile = getMobile();
        if (TextUtils.isEmpty(mobile)) {
            throw new NullPointerException("Mobile is null");
        } else {
            return userTopic + mobile;
        }
    }


    public boolean registerUserTopics(String userTopic) {
        try {
            registerTopic(getPushUserTopic(userTopic), AsyncMqttClient.QOS_LEAST_ONCE);
        } catch (NullPointerException e) {
            LogUtil.i(TAG, "find null mobile ignore");
            return false;
        }
        uploadRegisteredTopics();
        return true;
    }

    private void registerTopic(String topic, Integer QosLevel) {
        topics.put(topic, QosLevel);
    }

    public void unregisterUserTopic(String userTopic){
        try {
            userTopic = getPushUserTopic(userTopic);
            unsubscribe(userTopic);
        } catch (NullPointerException e) {
            LogUtil.i(TAG, "find null mobile ignore");
        }
    }

    public boolean deregisterTopic(String topic) {
        return topics.remove(topic) != null;
    }

    public Map<String, Integer> getRegisteredTopics() {
        return topics;
    }

    public AndroidAsyncMqttClient getAsyncMqttClient() {
        return asyncMqttClient;
    }

    private static KeyStore loadKeyStore(InputStream keyStoreInputStream, char[] keyStorePass) {
        try {
            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(keyStoreInputStream, keyStorePass);
            return keyStore;
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not load keyStore!", e);
        } finally {
            try {
                keyStoreInputStream.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Connections status for  a connection
     */
    enum ConnectionStatus {
        /**
         * Client is Connecting *
         */
        CONNECTING,
        /**
         * Client is Connected *
         */
        CONNECTED,
        /**
         * Client is Disconnecting *
         */
        DISCONNECTING,
        /**
         * Client is Disconnected *
         */
        DISCONNECTED,
        /**
         * Client has encountered an Error *
         */
        ERROR,
        /**
         * Client initialized & ready to connect
         */
        READY
    }
}

