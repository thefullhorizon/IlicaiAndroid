package com.ailicai.app.common.utils;

import android.text.TextUtils;

/**
 * Created by Ted on 2014/9/25.
 */
public class LastUpdateUtil {
    /**
     * 一分钟的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_MINUTE = 60 * 1000;

    /**
     * 一小时的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_HOUR = 60 * ONE_MINUTE;

    /**
     * 一天的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_DAY = 24 * ONE_HOUR;

    /**
     * ONE_WEEK:一周的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_WEEK = 7 * ONE_DAY;
    /**
     * 一月的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_MONTH = 30 * ONE_DAY;

    /**
     * 一年的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_YEAR = 12 * ONE_MONTH;

    /**
     * 刷新下拉头中上次更新时间的文字描述。
     */
    public static String getLastUpdateTime(long lastUpdateTime) {
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - lastUpdateTime;
        long timeIntoFormat;
        String updateStr = "前更新过";
        String updateAtValue;
        if (lastUpdateTime == -1) {
            updateAtValue = "";
        } else if (timePassed < 0) {
            updateAtValue = "";
        } else if (timePassed < ONE_MINUTE) {
            timeIntoFormat = timePassed / 1000;
            String value = timeIntoFormat + "秒";
            updateAtValue = value + updateStr;
        } else if (timePassed < ONE_HOUR) {
            timeIntoFormat = timePassed / ONE_MINUTE;
            String value = timeIntoFormat + "分钟";
            updateAtValue = value + updateStr;
        } else if (timePassed < ONE_DAY) {
            timeIntoFormat = timePassed / ONE_HOUR;
            String value = timeIntoFormat + "小时";
            updateAtValue = value + updateStr;
        } else if (timePassed < ONE_WEEK) {
            timeIntoFormat = timePassed / ONE_DAY;
            String value = timeIntoFormat + "天";
            updateAtValue = value + updateStr;
        } else if (timePassed < ONE_MONTH) {
            timeIntoFormat = timePassed / ONE_WEEK;
            String value = timeIntoFormat + "周";
            updateAtValue = value + updateStr;
        } else {
            timeIntoFormat = timePassed / ONE_MONTH;
            String value = timeIntoFormat + "月";
            updateAtValue = value + updateStr;
        }
        //		else if (timePassed < ONE_YEAR) {
        //			timeIntoFormat = timePassed / ONE_MONTH;
        //			String value = timeIntoFormat + "月";
        //			updateAtValue = value + updateStr;
        //		} else {
        //			timeIntoFormat = timePassed / ONE_YEAR;
        //			String value = timeIntoFormat + "年";
        //			updateAtValue = value + updateStr;
        //		}
        return TextUtils.isEmpty(updateAtValue) ? "" : "您在" + updateAtValue;
    }
}
