/**
 * File: TimeUtil
 */
package com.huoqiu.framework.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import java.util.Calendar;

public class TimeUtil {
    private static long defaultInterval = 60 * 1000;

    private static long disElapsedTime;

    private static boolean synced = false;


    // 确定服务器时间和本地时间是否一致，60秒内认为一致
    public static void calTime(Application context, long inputTime) {
        synced = true;
        long localTime; // 本地时间
        long elapsedRealTime; //开机后流逝时间
        long serviceTime;// 服务器时间

        serviceTime = inputTime;
        localTime = System.currentTimeMillis();
        elapsedRealTime = SystemClock.elapsedRealtime();
        mdisTime = serviceTime - localTime;
        saveDiffTime(context, mdisTime);
        disElapsedTime = serviceTime - elapsedRealTime;
    }

    public static void saveDiffTime(Context mC, long difftime) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mC);
        sharedPreferences.edit().putLong("disTime", difftime).commit();
    }

    public static long getDiffTime(Context mC) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mC);
        return sharedPreferences.getLong("disTime", 0l);
    }

    public static Calendar getCurrentTimeExact() {
        Calendar currentCalendar = DateUtil.getCalendarTime();
        if (synced) {
            currentCalendar.setTimeInMillis(disElapsedTime + SystemClock.elapsedRealtime());
        } else {
            currentCalendar.setTimeInMillis(System.currentTimeMillis());
        }
        return currentCalendar;
    }

    /**
     * 服务器时间获取是否成功，获取校正后的本地时间 否则获取手机本地时间
     *
     * @return Calendar
     * @see DateUtil#
     */
    public static Calendar getCurrentTime(Context mC) {
        Calendar currentCalendar = DateUtil.getCalendarTime();
        if (mdisTime == 0l && mC != null) {
            mdisTime = getDiffTime(mC);
        }
        if (Math.abs(mdisTime) > defaultInterval) {
            currentCalendar.setTimeInMillis(mdisTime + System.currentTimeMillis());
        }
        return currentCalendar;
    }

    public static Calendar getCurrentTime() {
        return getCurrentTime(null);
    }

    public static long getDisTime(Context mC) {
        if (mdisTime == 0l) {
            mdisTime = getDiffTime(mC);
        }
        return mdisTime;
    }

    private static long mdisTime = 0l;
}
