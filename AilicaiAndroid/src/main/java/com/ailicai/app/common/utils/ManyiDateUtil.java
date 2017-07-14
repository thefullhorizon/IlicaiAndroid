package com.ailicai.app.common.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by xubin on 14-5-21.
 */
public class ManyiDateUtil {

    public static final String TIMEZONE_CN = "Asia/Shanghai";

    // ================================SIMPLEFORMAT定义================
    /**
     ************** SIMPLEFORMATTYPE对就的类型定义****************** ＜p＞相应格式类型＜br＞
     */
    /**
     * @see #SIMPLEFORMATTYPESTRING1
     */
    public final static int SIMPLEFORMATTYPE1 = 0x01;
    /**
     * @see #SIMPLEFORMATTYPESTRING2
     */
    public final static int SIMPLEFORMATTYPE2 = 0x02;
    /**
     * @see #SIMPLEFORMATTYPESTRING3
     */
    public final static int SIMPLEFORMATTYPE3 = 0x03;
    /**
     * @see #SIMPLEFORMATTYPESTRING4
     */
    public final static int SIMPLEFORMATTYPE4 = 0x04;
    /**
     * @see #SIMPLEFORMATTYPESTRING5
     */
    public final static int SIMPLEFORMATTYPE5 = 0x05;
    /**
     * @see #SIMPLEFORMATTYPESTRING6
     */
    public final static int SIMPLEFORMATTYPE6 = 0x06;
    /**
     * @see #SIMPLEFORMATTYPESTRING7
     */
    public final static int SIMPLEFORMATTYPE7 = 0x07;
    /**
     * @see #SIMPLEFORMATTYPESTRING8
     */
    public final static int SIMPLEFORMATTYPE8 = 0x08;
    /**
     * @see #SIMPLEFORMATTYPESTRING9
     */
    public final static int SIMPLEFORMATTYPE9 = 0x09;
    /**
     * @see #SIMPLEFORMATTYPESTRING10
     */
    public final static int SIMPLEFORMATTYPE10 = 0x0a;
    /**
     * @see #SIMPLEFORMATTYPESTRING11
     */
    public final static int SIMPLEFORMATTYPE11 = 0x0b;
    /**
     * @see #SIMPLEFORMATTYPESTRING12
     */
    public final static int SIMPLEFORMATTYPE12 = 0x0c;
    /**
     * @see #SIMPLEFORMATTYPESTRING13
     */
    public final static int SIMPLEFORMATTYPE13 = 0x0d;
    /**
     * @see #SIMPLEFORMATTYPESTRING14
     */
    public final static int SIMPLEFORMATTYPE14 = 0x0e;
    /**
     * @see #SIMPLEFORMATTYPESTRING15
     */
    public final static int SIMPLEFORMATTYPE15 = 0x0f;

    /**
     * @see #SIMPLEFORMATTYPESTRING16
     */
    public final static int SIMPLEFORMATTYPE16 = 0x10;

    /**
     * ********************SIMPLEFORMATTYPE对应的字串*********************
     */
    /**
     * SIMPLEFORMATTYPE1 对应类型：yyyyMMddHHmmss
     *
     * @see #SIMPLEFORMATTYPE1
     */
    public final static String SIMPLEFORMATTYPESTRING1 = "yyyyMMddHHmmss";

    /**
     * SIMPLEFORMATTYPE2 对应的类型：yyyy-MM-dd HH:mm:ss
     *
     * @see #SIMPLEFORMATTYPE2
     */
    public final static String SIMPLEFORMATTYPESTRING2 = "yyyy-MM-dd HH:mm:ss";

    /**
     * SIMPLEFORMATTYPE3 对应的类型：yyyy-M-d HH:mm:ss
     *
     * @see #SIMPLEFORMATTYPE3
     */
    public final static String SIMPLEFORMATTYPESTRING3 = "yyyy-M-d HH:mm:ss";

    /**
     * SIMPLEFORMATTYPE4对应的类型：yyyy-MM-dd HH:mm
     *
     * @see #SIMPLEFORMATTYPE4
     */
    public final static String SIMPLEFORMATTYPESTRING4 = "yyyy-MM-dd HH:mm";

    /**
     * SIMPLEFORMATTYPE5 对应的类型：yyyy-M-d HH:mm
     *
     * @see #SIMPLEFORMATTYPE5
     */
    public final static String SIMPLEFORMATTYPESTRING5 = "yyyy-M-d HH:mm";

    /**
     * SIMPLEFORMATTYPE6对应的类型：yyyyMMdd
     *
     * @see #SIMPLEFORMATTYPE6
     */
    public final static String SIMPLEFORMATTYPESTRING6 = "yyyyMMdd";

    /**
     * SIMPLEFORMATTYPE7对应的类型：yyyy-MM-dd
     *
     * @see #SIMPLEFORMATTYPE7
     */
    public final static String SIMPLEFORMATTYPESTRING7 = "yyyy-MM-dd";

    /**
     * SIMPLEFORMATTYPE8对应的类型： yyyy-M-d
     *
     * @see #SIMPLEFORMATTYPE8
     */
    public final static String SIMPLEFORMATTYPESTRING8 = "yyyy-M-d";

    /**
     * SIMPLEFORMATTYPE9对应的类型：yyyy年MM月dd日
     *
     * @see #SIMPLEFORMATTYPE9
     */
    public final static String SIMPLEFORMATTYPESTRING9 = "yyyy年MM月dd日";

    /**
     * SIMPLEFORMATTYPE10对应的类型：yyyy年M月d日
     *
     * @see #SIMPLEFORMATTYPE10
     */
    public final static String SIMPLEFORMATTYPESTRING10 = "yyyy年M月d日";

    /**
     * SIMPLEFORMATTYPE11对应的类型：M月d日
     *
     * @see #SIMPLEFORMATTYPE11
     */
    public final static String SIMPLEFORMATTYPESTRING11 = "M月d日";

    /**
     * SIMPLEFORMATTYPE12对应的类型：HH:mm:ss
     *
     * @see #SIMPLEFORMATTYPE12
     */
    public final static String SIMPLEFORMATTYPESTRING12 = "HH:mm:ss";

    /**
     * SIMPLEFORMATTYPE13对应的类型：HH:mm
     *
     * @see #SIMPLEFORMATTYPE13
     */
    public final static String SIMPLEFORMATTYPESTRING13 = "HH:mm";
    /**
     * SIMPLEFORMATTYPE7对应的类型：yyyy-MM-dd
     *
     * @see #SIMPLEFORMATTYPE14
     */
    public final static String SIMPLEFORMATTYPESTRING14 = "yyyy/MM/dd";

    /**
     * SIMPLEFORMATTYPE15对应的类型：MM月dd日
     *
     * @see #SIMPLEFORMATTYPE15
     */
    public final static String SIMPLEFORMATTYPESTRING15 = "MM月dd日";

    /**
     * SIMPLEFORMATTYPE17对应的类型：yyyy/MM/dd HH:mm:ss
     *
     * @see #SIMPLEFORMATTYPE16
     */
    public final static String SIMPLEFORMATTYPESTRING16 = "yyyy/MM/dd HH:mm:ss";

    /**
     * 根据 SimpleDateFormatType类型将calendar转成对应的格式 如果calendar 为null则返回“”
     *
     * @param calendar             日历
     * @param SimpleDateFormatType 需要转换的格式类型
     * @return SIMPLEFORMATTYPESTRING1对应的格式
     * @see #SIMPLEFORMATTYPESTRING1
     * @see #SIMPLEFORMATTYPE1
     */
    public static String getCalendarStrBySimpleDateFormat(Calendar calendar, int SimpleDateFormatType) {
        String str = "";
        String type = "";
        switch (SimpleDateFormatType) {
            case SIMPLEFORMATTYPE1:
                type = SIMPLEFORMATTYPESTRING1;
                break;
            case SIMPLEFORMATTYPE2:
                type = SIMPLEFORMATTYPESTRING2;
                break;
            case SIMPLEFORMATTYPE3:
                type = SIMPLEFORMATTYPESTRING3;
                break;
            case SIMPLEFORMATTYPE4:
                type = SIMPLEFORMATTYPESTRING4;
                break;
            case SIMPLEFORMATTYPE5:
                type = SIMPLEFORMATTYPESTRING5;
                break;
            case SIMPLEFORMATTYPE6:
                type = SIMPLEFORMATTYPESTRING6;
                break;
            case SIMPLEFORMATTYPE7:
                type = SIMPLEFORMATTYPESTRING7;
                break;
            case SIMPLEFORMATTYPE8:
                type = SIMPLEFORMATTYPESTRING8;
                break;
            case SIMPLEFORMATTYPE9:
                type = SIMPLEFORMATTYPESTRING9;
                break;
            case SIMPLEFORMATTYPE10:
                type = SIMPLEFORMATTYPESTRING10;
                break;
            case SIMPLEFORMATTYPE11:
                type = SIMPLEFORMATTYPESTRING11;
                break;
            case SIMPLEFORMATTYPE12:
                type = SIMPLEFORMATTYPESTRING12;
                break;
            case SIMPLEFORMATTYPE13:
                type = SIMPLEFORMATTYPESTRING13;
                break;
            case SIMPLEFORMATTYPE14:
                type = SIMPLEFORMATTYPESTRING14;
                break;
            case SIMPLEFORMATTYPE15:
                type = SIMPLEFORMATTYPESTRING15;
                break;
            case SIMPLEFORMATTYPE16:
                type = SIMPLEFORMATTYPESTRING16;
                break;
            default:
                type = SIMPLEFORMATTYPESTRING1;
                break;
        }
        if (!TextUtils.isEmpty(type) && calendar != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(type);
            dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE_CN));
            str = (dateFormat).format(calendar.getTime());
        }
        return str;
    }

    /**
     * 今天 ，明天，后天，数组
     */
    private final static String[] THREEDAYARR = new String[]{"今天", "明天", "后天"};//

    private final static String[] WEEKNAME_CHINESE2 = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    // /**
    // * 日期转换为星期 ：显示 今天、明天、后天 > 节日 > 周X
    // *
    // * @param calendar
    // * 日期对象
    // * @return 今天、明天、后天 > 节日 > 周X
    // */
    // public static String getShowWeekOrHoliday2(Calendar calendar) {
    // String dateStr = getCalendarStrBySimpleDateFormat(calendar, DateUtil.SIMPLEFORMATTYPE6);
    // return getShowWeekOrHoliday2(dateStr);
    // }

    /**
     * 日期转换为星期 ：显示 今天、明天、后天 > 节日 > 周X
     *
     * @return 今天、明天、后天 > 节日 > 周X
     */
    public static String getShowWeekOrToday(Calendar calendar) {
        if (calendar == null) return "";
        String today = "";
        // 计算datestr与当前日期相差几天

        int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int calendatDay = calendar.get(Calendar.DAY_OF_YEAR);

        int dDay = calendatDay - nowDay;

        if (dDay < 3) {
            today = getThreeDayDes(dDay);
        }
        if (!TextUtils.isEmpty(today)) {
            return today;
        }

        // 返回周X
        String weekString = "";
        if (getWeek(calendar) != -1) {
            weekString = WEEKNAME_CHINESE2[getWeek(calendar)];
        }
        return weekString;
    }

    /**
     * 星期几, 数字表述. 从周日开始
     *
     * @param calendar if calendar 为null则返回-1
     * @return 索引
     * @see Calendar#DAY_OF_WEEK
     */
    public static int getWeek(Calendar calendar) {
        int theResult = -1;
        if (calendar != null) {
            theResult = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return theResult;
    }

    /**
     * 返回，今天/明天/后天
     *
     * @param count 0，1，2 其他则返回空字串
     * @return
     */
    public static String getThreeDayDes(int count) {
        if (count >= 0 && count <= 2) {
            return THREEDAYARR[count];
        }
        return "";
    }

    /**
     * 根据long型毫秒数返回2012年1月1日
     *
     * @param time
     * @return
     */
    public static String getDateStrFromLong(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return getCalendarStrBySimpleDateFormat(cal, SIMPLEFORMATTYPE9);
    }
}
