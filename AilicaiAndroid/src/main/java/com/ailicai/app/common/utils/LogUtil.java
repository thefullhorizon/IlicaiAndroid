package com.ailicai.app.common.utils;

import android.util.Log;

import com.ailicai.app.common.constants.AILICAIBuildConfig;

public class LogUtil {

    private static final String DEFAULT_TAG = "com.ailicai.app";

    public static void v(String msg) {
        v(DEFAULT_TAG, msg);
    }

    public static void v(String TAG, String msg) {
        if (!isRelease()) {
            Log.v(TAG, msg);
        }
    }

    public static void i(String msg) {
        i(DEFAULT_TAG, msg);
    }

    public static void i(String TAG, String msg) {
        if (!isRelease()) {
            Log.i(TAG, msg);
        }
    }

    public static void d(String msg) {
        d(DEFAULT_TAG, msg);
    }

    public static void d(String TAG, String msg) {
        if (!isRelease()) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        e(DEFAULT_TAG, msg);
    }

    public static void e(String TAG, String msg) {
        if (!isRelease()) {
            Log.e(TAG, msg);
        }
    }

    private static boolean isRelease() {
        return AILICAIBuildConfig.isProduction();
    }
}
