package com.ailicai.app.common.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * HandlerUtil工具类
 * created by liyanan on 16/4/18
 */
public class HandlerUtil {
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void post(Runnable runnable) {
        handler.post(runnable);
    }

    public static void postDelay(Runnable runnable, long delayMillis) {
        handler.postDelayed(runnable, delayMillis);
    }
}