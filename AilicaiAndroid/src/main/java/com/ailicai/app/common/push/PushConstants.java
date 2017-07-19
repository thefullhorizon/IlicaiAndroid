package com.ailicai.app.common.push;

/**
 * Created by duo.chen on 2015/4/10.
 */
public class PushConstants {

    /**
     * For test env
     */
    public static final String TEST_FETCH_CONFIG_URL = "https://192.168.1.39:8443/mqtt/config";

    /**
     * For official
     */
    public static final String OFF_FETCH_CONFIG_URL = "https://121.43.69.152/mqtt/config";

    /**
     * For beta
     */
    public static final String BETA_FETCH_CONFIG_URL = "https://121.40.129.114/mqtt/config";

    public static final String ACTION_MQTT_SHOW_NOTIFICATION =
            "ACTION_ALICAI_MQTT_SHOW_NOTIFICATION";

    public static final String ACTION_MQTT_NOTIFICATION_CLICKED =
            "ACTION_ALICAI_MQTT_NOTIFICATION_CLICKED";

    public static final String ACTION_MQTT_NOTIFICATION_CLEARED =
            "ACTION_ALICAI_MQTT_NOTIFICATION_CLEARED";

    public static MqttManager mqttManager = null;

    /**
     * MiPush Id & key
     */
    public static final String MIPUSH_APP_ID = "2882303761517597107";
    public static final String MIPUSH_APP_KEY = "5481759754107";
    public static final String MIPUSH_APP_NAME = "alicai/push/android/xiaoMI";
    public static final String HWPUSH_APP_NAME = "alicai/push/android/huaWei";
    public static final String JPUSH_APP_NAME = "alicai/push/android/jpush";

    /**
     * clientId prefix
     * username and password for fetching config from url
     */
    public static final String clientId = "Android_MQTT_alicai";
    public static String username = "username";
    public static final char[] password = new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};
    public static boolean usingTLS = true;
    public static final String KEYSTORE_FILE = "inthingsq_trust.bks";
    public static final char[] KEYSTORE_PWD = new char[]{'p', 'a', 's', 's', 'w', '0', 'r', 'd'};
    /**
     * for collectting log topic
     */
    public static final String LOG_TOPIC = "superjia/push/track/user/android";
    /**
     * for push broadcast PREFIX
     */
    public static final String PUSH_USER_BASE_TOPIC_PREFLX = "superjia/push/alicai/android/";
    public static final String PUSH_USER_TOPIC_PREFIX = PUSH_USER_BASE_TOPIC_PREFLX;
    public static final String PUSH_MI_USER_TOPIC_PREFIX = PUSH_USER_BASE_TOPIC_PREFLX + "xiaoMI/";
    /**
     * for clean logout
     */
    public static final String PUSH_USER_CLEAN = "super/push/clean";
}
