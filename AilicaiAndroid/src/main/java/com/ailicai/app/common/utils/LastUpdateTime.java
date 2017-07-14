package com.ailicai.app.common.utils;

import android.content.Context;

public class LastUpdateTime {
    private static final String KEY_LAST_UPDATE_TIME = "lastUpdateTime";

    /**
     * 保存最后更新时间
     */
    public static void saveLastUpdateTime(Context context) {
        MyPreference.getInstance().write(KEY_LAST_UPDATE_TIME, System.currentTimeMillis());
    }

    public static long getLastUpdateTime(Context context) {
        return MyPreference.getInstance().read(KEY_LAST_UPDATE_TIME, 0l);
    }
}
