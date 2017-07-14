package com.ailicai.app.common.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author xinyh
 * @version1.0
 * @creat date 2012-3-27
 */
public class DateUtil {
    public static final String TIMEZONE_CN = "Asia/Shanghai";

    /**
     * Calendar 中对应的年
     */
    public final static int YEAR = Calendar.YEAR;
    /**
     * Calendar 中对应的月
     */
    public final static int MONTH = Calendar.MONTH;
    /**
     * Calendar 中对应的日
     */
    public final static int DAY = Calendar.DAY_OF_MONTH;
    /**
     * Calendar 中对应的时
     */
    public final static int HOUR = Calendar.HOUR_OF_DAY;
    /**
     * Calendar 中对应的分
     */
    public final static int MINUTE = Calendar.MINUTE;
    /**
     * Calendar 中对应的秒
     */
    public final static int SECOND = Calendar.SECOND;

    public final static int DATE = Calendar.DATE;

    /**
     * 比较到年级别
     */
    public final static int COMPARELEVEL_YEAR = 0;
    /**
     * 比较到月钟级别
     */
    public final static int COMPARELEVEL_MONTH = 1;
    /**
     * 比较到天级别
     */
    public final static int COMPARELEVEL_DAY = 2;
    /**
     * 比较到小时级别
     */
    public final static int COMPARELEVEL_HOUR = 3;
    /**
     * 比较到分钟级别
     */
    public final static int COMPARELEVEL_MINUTE = 4;
    /**
     * 比较到秒级别
     */
    public final static int COMPARELEVEL_SECOND = 5;

    /**
     * 星期数组
     */
    private final static String[] WEEKNAME_CHINESE = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    private final static String[] WEEKNAME_CHINESE2 = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

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
     * @see #SIMPLEFORMATTYPESTRING18
     */
    public final static int SIMPLEFORMATTYPE18 = 0x11;

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
     * SIMPLEFORMATTYPE18对应的类型：yyyy-MM
     *
     * @see #SIMPLEFORMATTYPE18
     */
    public final static String SIMPLEFORMATTYPESTRING18 = "yyyy-MM";

    // =====================================End===================================
    /**
     * 农历
     */
    final static long[] lunarInfo = new long[]{0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
            0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50,
            0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0,
            0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550,
            0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260,
            0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
            0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3,
            0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0,
            0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0,
            0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0,
            0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0};

    /**
     * 今天 ，明天，后天，数组
     */
    private final static String[] THREEDAYARR = new String[]{"今天", "明天", "后天"};//
    /**
     * 今天 ，明天，后天，数组
     */
    private final static String[] FIVEDAYARR = new String[]{"前天", "昨天", "今天", "明天", "后天"};//

    /**
     * 获取当前日期 yyyyMMddHHmmss 14位
     *
     * @return String
     * @see #getCalendarStrBySimpleDateFormat
     * @see #SIMPLEFORMATTYPESTRING1
     */
    public static String getCurrentTime() {
        Calendar currentCalendar = Calendar.getInstance();
        return getCalendarStrBySimpleDateFormat(currentCalendar, SIMPLEFORMATTYPE1);
    }

    /**
     * 获取当前日期 8位
     *
     * @return String
     * @see #getCalendarStrBySimpleDateFormat
     * @see #SIMPLEFORMATTYPESTRING6
     */
    public static String getCurrentDate() {
        Calendar currentCalendar = Calendar.getInstance();
        return getCalendarStrBySimpleDateFormat(currentCalendar, SIMPLEFORMATTYPE6);
    }

    /**
     * 获取当前日期
     *
     * @return Calendar
     */
    public static Calendar getCurrentCalendar() {
        Calendar currentCalendar = Calendar.getInstance();
        return currentCalendar;
    }

    /**
     * 获取手机当前日期
     *
     * @return Calendar
     */
    public static Calendar getLocalCalendar() {
        Calendar localCalendar = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE_CN));
        return localCalendar;
    }

    /**
     * 获取 + n个月 最后一天
     *
     * @param n 相隔n个月
     * @return Calendar
     */
    public static Calendar getMonthEndCalendar(Calendar startCalendar, int n) {
        if (startCalendar == null) {
            startCalendar = getCurrentCalendar();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startCalendar.getTimeInMillis());
        calendar.add(Calendar.MONTH, n + 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar;
    }

    /**
     * 将日期字串转为 Calendar ,dateStr需超过8位且不能为空,否则返回null
     *
     * @param dateStr
     * @return Calendar
     */
    public static Calendar getCalendarByDateStr(String dateStr) {
        if (TextUtils.isEmpty(dateStr) || dateStr.length() < 8)
            return null;
        Calendar calendar = getLocalCalendar();
        int year = Integer.valueOf(dateStr.substring(0, 4));
        int month = Integer.valueOf(dateStr.substring(4, 6));
        int day = Integer.valueOf(dateStr.substring(6, 8));
        calendar.set(year, month - 1, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 将时间字串转为 Calendar dateStr小于8时返回null，不足14位补0
     *
     * @param dateStr
     * @return
     */
    public static Calendar getCalendarByDateTimeStr(String dateStr) {
        if (TextUtils.isEmpty(dateStr) || dateStr.length() < 8)
            return null;
        while (dateStr.length() < 14) {
            dateStr += "0";
        }
        Calendar calendar = getLocalCalendar();
        // LogUtil.e("dateStr" + dateStr);
        int year = StringUtil.toInt(dateStr.substring(0, 4));
        int month = StringUtil.toInt(dateStr.substring(4, 6));
        int day = StringUtil.toInt(dateStr.substring(6, 8));
        int hour = StringUtil.toInt(dateStr.substring(8, 10));
        int min = StringUtil.toInt(dateStr.substring(10, 12));
        int second = 0;
        if (dateStr.length() >= 14)
            second = StringUtil.toInt(dateStr.substring(12, 14));
        calendar.set(year, month - 1, day, hour, min, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 当前日期前一天 yyyyMMdd
     *
     * @return
     * @see #getCalendarStrBySimpleDateFormat
     * @see #SIMPLEFORMATTYPE6
     * @see #SIMPLEFORMATTYPESTRING6
     */
    public static String getLastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return getCalendarStrBySimpleDateFormat(calendar, SIMPLEFORMATTYPE6);
    }

    /**
     * 当前日期后一天 yyyyMMdd
     *
     * @return
     * @see #getCalendarStrBySimpleDateFormat
     * @see #SIMPLEFORMATTYPE6
     * @see #SIMPLEFORMATTYPESTRING6
     */
    public static String getNextDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        return getCalendarStrBySimpleDateFormat(calendar, SIMPLEFORMATTYPE6);
    }

    public static String getNextDate(Calendar calendar) {
        calendar.add(Calendar.DATE, 1);
        return getCalendarStrBySimpleDateFormat(calendar, SIMPLEFORMATTYPE6);
    }

    /**
     * 根据日期返回对应的星期
     *
     * @param calendar if calendar 为null则返回空字串
     * @return "星期*"
     * @see #WEEKNAME_CHINESE
     * @see #getWeek
     */
    public static String getShowWeekByCalendar(Calendar calendar) {
        String weekString = "";
        if (calendar == null) {
            return weekString;
        }
        if (getWeek(calendar) != -1) {
            weekString = WEEKNAME_CHINESE[getWeek(calendar)];
        }
        return weekString;
    }

    /**
     * 根据日期返回对应的周几
     *
     * @param calendar if calendar 为null则返回空字串
     * @return "星期*"
     * @see #WEEKNAME_CHINESE
     * @see #getWeek
     */
    public static String getShowWeekByCalendar2(Calendar calendar) {
        String weekString = "";
        if (calendar == null) {
            return weekString;
        }
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
     * @see #THREEDAYARR
     */
    public static String getThreeDayDes(int count) {
        if (count >= 0 && count <= 2) {
            return THREEDAYARR[count];
        }
        return "";
    }

    /**
     * 返回，前天/昨天/今天/明天/后天
     *
     * @param count 0，1，2,3,4 其他则返回空字串
     * @return
     */
    public static String getFiveDayDes(int count) {
        if (count >= 0 && count <= 4) {
            return FIVEDAYARR[count];
        }
        return "";
    }

    /**
     * 获取calendar对应的年
     *
     * @param calendar
     * @return 年字段值
     * @see #YEAR
     */
    public static int getYear(Calendar calendar) {
        return calendar.get(YEAR);
    }

    /**
     * 获取calendar对应的月份
     *
     * @param calendar
     * @return 月
     * @see #MONTH
     */
    public static int getMonth(Calendar calendar) {
        return calendar.get(MONTH) + 1;
    }

    /**
     * 获取calendar对应的DAY
     *
     * @param calendar
     * @return DAY
     * @see #DAY
     */
    public static int getDay(Calendar calendar) {
        return calendar.get(DAY);
    }

    /**
     * 获取calendar对应的小时
     *
     * @param calendar
     * @return HOUR_OF_DAY
     * @see #HOUR
     */
    public static int getHourOfDay(Calendar calendar) {
        return calendar.get(HOUR);
    }

    /**
     * 获取calendar对应的分钟
     *
     * @param calendar
     * @return MINUTE
     * @see #MINUTE
     */
    public static int getMinute(Calendar calendar) {
        return calendar.get(MINUTE);
    }

    /**
     * 获取calendar对应的秒
     *
     * @param calendar
     * @return SECOND
     * @see #SECOND
     */
    public static int getSecond(Calendar calendar) {
        return calendar.get(SECOND);
    }

    /**
     * 计算日期字串 DAY字段
     *
     * @param date 日期字串
     * @param step 日期需要加减的值
     * @return 如果date为空或者小于8位则返回""字串,如果step等于0则直接返回date
     * @see #getCalendarByDateStr
     * @see #calculateCalendar
     * @see #SIMPLEFORMATTYPE6
     * @see #SIMPLEFORMATTYPESTRING6
     */
    public static String getDateByStep(String date, int step) {
        if (TextUtils.isEmpty(date) || date.length() < 8)
            return "";
        else if (step == 0)
            return date;
        else {
            return getCalendarStrBySimpleDateFormat(calculateCalendar(getCalendarByDateStr(date), DAY, step), SIMPLEFORMATTYPE6);
        }
    }

    /**
     * 对14位的日期对应的字段进行加减计算 并返回yyyyMMddHHmmss
     *
     * @param date  14位日期字串
     * @param field Calendar对应的年、月、日 属性
     * @param step
     * @return 计算过后的日期字串 yyyyMMddHHmmss
     * @see #SIMPLEFORMATTYPE1
     * @see #getCalendarByDateTimeStr
     * @see #calculateCalendar
     */
    public static String getTimeByStep(String date, int field, int step) {
        if (TextUtils.isEmpty(date) || date.length() < 14)
            return "";
        else if (step == 0)
            return date;
        else {
            return getCalendarStrBySimpleDateFormat(calculateCalendar(getCalendarByDateTimeStr(date), field, step), SIMPLEFORMATTYPE1);
        }
    }

    /**
     * 日期加减
     *
     * @param newcalendar 日历
     * @param field       calendar对应的属性
     * @param amount      需要加减的值
     * @return Calendar
     */
    public static Calendar calculateCalendar(Calendar newcalendar, int field, int amount) {
        Calendar calendar = (Calendar) newcalendar.clone();
        if (calendar == null) {
            return null;
        }
        switch (field) {
            case YEAR:
                calendar.add(YEAR, amount);
                break;
            case MONTH:
                calendar.add(MONTH, amount);
                break;
            case DAY:
                calendar.add(DAY, amount);
                break;
            case HOUR:
                calendar.add(HOUR, amount);
                break;
            case MINUTE:
                calendar.add(MINUTE, amount);
                break;
            case SECOND:
                calendar.add(SECOND, amount);
                break;
        }
        return calendar;
    }

    /**
     * dateStr小于8时返回null，不足14位补0 将dateStr 转换成 SimpleDateFormatType对应格式的字串
     *
     * @param dateStr
     * @param SimpleDateFormatType
     * @return
     * @see #CalendarStrBySimpleDateFormat
     */
    public static String CalendarStrBySimpleDateFormat(String dateStr, int SimpleDateFormatType) {
        return getCalendarStrBySimpleDateFormat(getCalendarByDateTimeStr(dateStr), SimpleDateFormatType);
    }

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
            case SIMPLEFORMATTYPE18:
                type = SIMPLEFORMATTYPESTRING18;
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
     * 将yyyyMMdd字符串转成yyyy年M月d日 星期x格式
     *
     * @param dateStr
     * @return 如果 dateStr为空则返回“”
     * @see #getCalendarByDateStr
     * @see #getCalendarStrBySimpleDateFormat
     * @see #getShowWeekByCalendar
     */
    public static String getShowWeekByDate(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return "";
        }
        Calendar currentDate = getCalendarByDateStr(dateStr);
        String date = getCalendarStrBySimpleDateFormat(currentDate, SIMPLEFORMATTYPE10);
        StringBuffer showStringBuffer = new StringBuffer(18);
        showStringBuffer.append(date);
        String weekString = getShowWeekByCalendar(currentDate);
        showStringBuffer.append("  " + weekString);
        return showStringBuffer.toString().trim();
    }

    /**
     * 若计算datestr与当前日期相差在3天内，将yyyyMMdd字符串转成"今天/明天/后天/" 否者，转换成"星期*"
     *
     * @param dateStr YYMMDD
     * @return 今天/明天/后天 or 星期*
     * @see #getCalendarByDateStr
     * @see #getShowWeekByCalendar
     * @see #getCurrentTime
     */
    public static String getShowWeek(String dateStr) {
        String weekday = getShowWeekByCalendar(getCalendarByDateStr(dateStr));
        String today = "";
        String ctripDateStr = getCurrentTime(); // 获取当前日期字串
        // 计算datestr与当前日期相差几天
        long ctripDateM = getCalendarByDateStr(ctripDateStr).getTimeInMillis();
        long dateM = getCalendarByDateStr(dateStr).getTimeInMillis();
        long dValue = dateM - ctripDateM;
        int dDay = (int) (dValue / (24 * 60 * 60 * 1000));

        // dDay在3天内则返回相应的今天/明天/后天
        if (dDay >= 0 && dDay <= 2) {
            today += getThreeDayDes(dDay);
        }

        if (!TextUtils.isEmpty(today)) {
            return today;
        } else {
            return weekday;
        }
    }

    /**
     * 日期转换为星期 ：显示 今天、明天、后天 > 节日 > 星期X
     *
     * @param dateStr YYMMDD
     * @return 今天、明天、后天 > 节日 > 星期X
     */
    public static String getShowWeekOrHoliday(String dateStr) {
        String today = "";
        String ctripDateStr = getCurrentTime(); // 获取当前日期字串
        // 计算datestr与当前日期相差几天
        long ctripDateM = getCalendarByDateStr(ctripDateStr).getTimeInMillis();
        long dateM = getCalendarByDateStr(dateStr).getTimeInMillis();
        long dValue = dateM - ctripDateM;
        int dDay = (int) (dValue / (24 * 60 * 60 * 1000));

        // dDay在3天内则返回相应的今天/明天/后天
        if (dDay >= 0 && dDay <= 2) {
            today += getThreeDayDes(dDay);
        }
        if (!TextUtils.isEmpty(today)) {
            return today;
        }

        // 获取返回节日
        String holiday = getHolidayString(dateStr);
        if (!TextUtils.isEmpty(holiday)) {
            return holiday;
        }
        // 返回星期X
        String weekday = getShowWeekByCalendar(getCalendarByDateStr(dateStr));
        return weekday;
    }

    /**
     * 日期转换为星期 ：显示 今天、明天、后天 > 节日 > 周X
     *
     * @param dateStr YYMMDD
     * @return 今天、明天、后天 > 节日 > 周X
     */
    public static String getShowWeekOrHoliday2(String dateStr) {
        String today = "";
        String ctripDateStr = getCurrentTime(); // 获取当前日期字串
        // 计算datestr与当前日期相差几天
        long ctripDateM = getCalendarByDateStr(ctripDateStr).getTimeInMillis();
        long dateM = getCalendarByDateStr(dateStr).getTimeInMillis();
        long dValue = dateM - ctripDateM;
        int dDay = (int) (dValue / (24 * 60 * 60 * 1000));

        // dDay在3天内则返回相应的今天/明天/后天
        if (dDay >= 0 && dDay <= 2) {
            today += getThreeDayDes(dDay);
        }
        if (!TextUtils.isEmpty(today)) {
            return today;
        }

        // 获取返回节日
        String holiday = getHolidayString(dateStr);
        if (!TextUtils.isEmpty(holiday)) {
            return holiday;
        }
        // 返回周X
        Calendar calendar = getCalendarByDateStr(dateStr);
        String weekString = "";
        if (calendar == null) {
            return weekString;
        }
        if (getWeek(calendar) != -1) {
            weekString = WEEKNAME_CHINESE2[getWeek(calendar)];
        }
        return weekString;
    }

    /**
     * 日期转换为星期 ：显示 今天、明天、后天 > 节日 > 星期X
     *
     * @param calendar 日期对象
     * @return 今天、明天、后天 > 节日 > 星期X
     */
    public static String getShowWeekOrHoliday(Calendar calendar) {
        String dateStr = getCalendarStrBySimpleDateFormat(calendar, DateUtil.SIMPLEFORMATTYPE6);
        return getShowWeekOrHoliday(dateStr);
    }

    /**
     * 日期转换为星期 ：显示 今天、明天、后天 > 节日 > 周X
     *
     * @param calendar 日期对象
     * @return 今天、明天、后天 > 节日 > 周X
     */
    public static String getShowWeekOrHoliday2(Calendar calendar) {
        String dateStr = getCalendarStrBySimpleDateFormat(calendar, DateUtil.SIMPLEFORMATTYPE6);
        return getShowWeekOrHoliday2(dateStr);
    }

    /**
     * 若计算datestr与当前日期相差在3天内，将yyyyMMdd字符串转成"今天/明天/后天/" 否则返回""
     *
     * @param dateStr
     * @return 今天/明天/后天*
     * @see #getCalendarByDateStr
     * @see #getShowWeekByCalendar
     * @see #getCurrentTime
     */
    public static String getDateStrCompareToDay(String dateStr) {
        String today = "";
        String ctripDateStr = getCurrentTime(); // 获取当前日期字串
        // 计算datestr与当前日期相差几天
        long ctripDateM = getCalendarByDateStr(ctripDateStr).getTimeInMillis();
        Calendar dateMCalendar = getCalendarByDateStr(dateStr);
        if (dateMCalendar == null) {
            return "";
        }
        long dateM = dateMCalendar.getTimeInMillis();
        long dValue = dateM - ctripDateM;
        int dDay = (int) (dValue / (24 * 60 * 60 * 1000));
        // dDay在3天内则返回相应的今天/明天/后天
        if (dDay >= 0 && dDay <= 2) {
            today += getThreeDayDes(dDay);
        }
        return today;
    }

    /**
     * 若计算datestr与当前日期相差在3天内，将yyyyMMdd字符串转成"前天/昨天/今天/明天/后天/" 否则返回""
     *
     * @param dateStr
     * @return 前天/昨天/今天/明天/后天*
     * @see #getCalendarByDateStr
     * @see #getShowWeekByCalendar
     * @see #getCurrentTime
     */
    public static String getFiveDateStrCompareToDay(String dateStr) {
        String today = "";
        String ctripDateStr = getCurrentTime(); // 获取当前日期字串
        // 计算datestr与当前日期相差几天
        long ctripDateM = getCalendarByDateStr(ctripDateStr).getTimeInMillis();
        Calendar dateMCalendar = getCalendarByDateStr(dateStr);
        if (dateMCalendar == null) {
            return "";
        }
        long dateM = dateMCalendar.getTimeInMillis();
        long dValue = dateM - ctripDateM;
        int dDay = (int) (dValue / (24 * 60 * 60 * 1000));
        dDay = dDay + 2;
        if (dDay >= 0 && dDay <= 4) {
            today += getFiveDayDes(dDay);
        }
        return today;
    }

    /**
     * 将yyyyMMdd字符串转成yyyy-MM-dd 星期x格式
     *
     * @param dateStr
     * @return
     * @see #getCalendarByDateStr
     * @see #SIMPLEFORMATTYPESTRING7
     * @see #getCalendarStrBySimpleDateFormat
     */
    public static String getShowWeekByDate2(String dateStr) {
        String str = getCalendarStrBySimpleDateFormat(getCalendarByDateStr(dateStr), SIMPLEFORMATTYPE7) + getShowWeek(dateStr);
        return str.toString().trim();
    }

    /**
     * 将yyyyMMdd字符串转成yyyy-MM-dd 星期x格式,在星期前面加上了空格
     *
     * @param dateStr
     * @return
     * @see #getCalendarByDateStr
     * @see #SIMPLEFORMATTYPESTRING7
     * @see #getCalendarStrBySimpleDateFormat
     */
    public static String getShowWeekByDate2WithBlank(String dateStr) {
        String str = getCalendarStrBySimpleDateFormat(getCalendarByDateStr(dateStr), SIMPLEFORMATTYPE7) + " " + getShowWeek(dateStr);
        return str.toString().trim();
    }

    /**
     * 将yyyyMMdd字符串转成yyyy-MM-dd
     *
     * @param dateStr
     * @return
     * @see #getCalendarByDateStr
     * @see #SIMPLEFORMATTYPESTRING7
     * @see #getCalendarStrBySimpleDateFormat
     */
    public static String getShowWeekByDate5(String dateStr) {
        String str = getCalendarStrBySimpleDateFormat(getCalendarByDateStr(dateStr), SIMPLEFORMATTYPE7);
        return str.toString().trim();
    }

    /**
     * 将yyyyMMdd字符串转成yyyy-MM-dd 今天/明天/后天/星期x格式
     *
     * @param dateStr
     * @return
     * @see #getCalendarByDateStr
     * @see #SIMPLEFORMATTYPESTRING7
     * @see #getCalendarStrBySimpleDateFormat
     * @see #getCurrentTime
     */
    public static String getShowWeekByDate3(String dateStr) {
        String str4Retrun = getCalendarStrBySimpleDateFormat(getCalendarByDateStr(dateStr), SIMPLEFORMATTYPE7);
        String ctripDateStr = getCurrentTime();
        long ctripDateM = getCalendarByDateStr(ctripDateStr).getTimeInMillis();
        long dateM = getCalendarByDateStr(dateStr).getTimeInMillis();
        long dValue = dateM - ctripDateM;
        int dDay = (int) (dValue / (24 * 60 * 60 * 1000));
        if (dDay >= 0 && dDay <= 2) {
            str4Retrun += " 　" + getThreeDayDes(dDay);
            return str4Retrun;
        } else {
            return getShowWeekByDate2(dateStr);
        }
    }

    /**
     * 将yyyyMMdd字符串转成yyyy-MM-dd 前天/昨天/今天/明天/后天/星期x格式
     *
     * @param dateStr
     * @return
     * @see #getCalendarByDateStr
     * @see #SIMPLEFORMATTYPESTRING7
     * @see #getCalendarStrBySimpleDateFormat
     * @see #getCurrentTime
     */
    public static String getShowWeekByDate6(String dateStr) {
        String str4Retrun = getCalendarStrBySimpleDateFormat(getCalendarByDateStr(dateStr), SIMPLEFORMATTYPE7);
        String ctripDateStr = getCurrentTime();
        long ctripDateM = getCalendarByDateStr(ctripDateStr).getTimeInMillis();
        long dateM = getCalendarByDateStr(dateStr).getTimeInMillis();
        long dValue = dateM - ctripDateM;
        int dDay = (int) (dValue / (24 * 60 * 60 * 1000));
        dDay = dDay + 2;
        if (dDay >= 0 && dDay <= 4) {
            str4Retrun += " " + getFiveDayDes(dDay);
            return str4Retrun;
        } else {
            return getShowWeekByDate2WithBlank(dateStr);
        }
    }

    /**
     * 将yyyyMMdd字符串转成"yyyy-MM-dd [今天/明天/后天]"+"-"+"星期*"+"-"+"节日"
     *
     * @param dateStr
     * @return
     * @see #getHolidayString
     * @see #getCalendarByDateStr
     * @see #getShowWeekByCalendar
     * @see #getCurrentTime
     */
    public static String getShowWeekByDate4(String dateStr) {
        String holiday = getHolidayString(dateStr);
        String weekday = getShowWeekByCalendar(getCalendarByDateStr(dateStr));
        String today = CalendarStrBySimpleDateFormat(dateStr, SIMPLEFORMATTYPE7);
        String ctripDateStr = getCurrentTime();
        long ctripDateM = getCalendarByDateStr(ctripDateStr).getTimeInMillis();
        long dateM = getCalendarByDateStr(dateStr).getTimeInMillis();
        long dValue = dateM - ctripDateM;
        int dDay = (int) (dValue / (24 * 60 * 60 * 1000));
        if (dDay >= 0 && dDay <= 2) {
            today += getThreeDayDes(dDay);
        }
        if (!TextUtils.isEmpty(holiday) && !TextUtils.isEmpty(weekday) && !TextUtils.isEmpty(today)) {
            return today + "-" + "" + "-" + holiday;
        } else {
            return today + "-" + weekday + "-" + holiday;
        }
    }

    /**
     * 将两个Calendar进行比较，返回两个long型 的差 ，如果两个Calendar为null则返回-1
     *
     * @param firstCalendar
     * @param secondCalendar
     * @param level
     * @return 返回两个long型 的差
     */

    public static long compareCalendarByLevel(Calendar firstCalendar, Calendar secondCalendar, int level) {
        if (firstCalendar == null || secondCalendar == null) {
            return -1;
        }
        int firstYear = getYear(firstCalendar);
        int secondYear = getYear(secondCalendar);
        int firstMonth = getMonth(firstCalendar);
        int secondMonth = getMonth(secondCalendar);
        int firstDay = getDay(firstCalendar);
        int secondDay = getDay(secondCalendar);
        int firstHour = getHourOfDay(firstCalendar);
        int secondHour = getHourOfDay(secondCalendar);
        int firstMinute = getMinute(firstCalendar);
        int secondMinute = getMinute(secondCalendar);
        int firstSec = getSecond(firstCalendar);
        int secondSec = getSecond(secondCalendar);
        switch (level) {
            case COMPARELEVEL_YEAR:
                firstMonth = 0;
                secondMonth = 0;
                firstDay = 0;
                secondDay = 0;
                firstHour = 0;
                secondHour = 0;
                firstMinute = 0;
                secondMinute = 0;
                firstSec = 0;
                secondSec = 0;
                break;
            case COMPARELEVEL_MONTH:
                firstDay = 0;
                secondDay = 0;
                firstHour = 0;
                secondHour = 0;
                firstMinute = 0;
                secondMinute = 0;
                firstSec = 0;
                secondSec = 0;
                break;
            case COMPARELEVEL_DAY:
                firstHour = 0;
                secondHour = 0;
                firstMinute = 0;
                secondMinute = 0;
                firstSec = 0;
                secondSec = 0;
                break;
            case COMPARELEVEL_HOUR:
                firstMinute = 0;
                secondMinute = 0;
                firstSec = 0;
                secondSec = 0;
                break;
            case COMPARELEVEL_MINUTE:
                // firstMinute = 0;
                // secondMinute = 0;
                firstSec = 0;
                secondSec = 0;
                break;
            case COMPARELEVEL_SECOND:
                break;
            default:
                firstMonth = 0;
                secondMonth = 0;
                firstDay = 0;
                secondDay = 0;
                firstHour = 0;
                secondHour = 0;
                firstMinute = 0;
                secondMinute = 0;
                firstSec = 0;
                secondSec = 0;
                break;
        }
        firstCalendar.set(firstYear, firstMonth - 1, firstDay, firstHour, firstMinute, firstSec);
        secondCalendar.set(secondYear, secondMonth - 1, secondDay, secondHour, secondMinute, secondSec);
        firstCalendar.set(Calendar.MILLISECOND, 0);
        secondCalendar.set(Calendar.MILLISECOND, 0);
        long firstM = firstCalendar.getTimeInMillis();
        long secondM = secondCalendar.getTimeInMillis();
        return (firstM - secondM);
    }

    /**
     * 两个日期字串进行比较 字串为空则返回-1,将日期转成long型比较差
     *
     * @param firstDate
     * @param secondDate
     * @param level      比较等级
     * @return 两个long型的差
     * @see #getCalendarByDateTimeStr
     * @see #COMPARELEVEL_YEAR
     * @see StringUtil#
     */
    public static long compareDateStringByLevel(String firstDate, String secondDate, int level) {
        String fristDateStr = firstDate;
        String secondDateStr = secondDate;

        if (TextUtils.isEmpty(fristDateStr) || TextUtils.isEmpty(secondDateStr)) {
            return -1;
        }
        return compareCalendarByLevel(getCalendarByDateTimeStr(fristDateStr), getCalendarByDateTimeStr(secondDateStr), level);
    }

    /**
     * 两个日期字串是否相等
     *
     * @param firstDate
     * @param secondDate
     * @param level      等级，年，月，日，时，分秒 COMPARELEVEL_*
     * @return true or false
     * @see #compareDateStringByLevel
     */
    public static boolean dateStringEquls(String firstDate, String secondDate, int level) {
        return compareDateStringByLevel(firstDate, secondDate, level) == 0;
    }

    /**
     * firstDate晚于secondDate
     *
     * @param firstDate
     * @param secondDate
     * @param level      等级，年，月，日，时，分秒 COMPARELEVEL_*
     * @return true or false
     * @see #compareDateStringByLevel
     */
    public static boolean firstDateStrAfterSecondDateStr(String firstDate, String secondDate, int level) {

        return compareDateStringByLevel(firstDate, secondDate, level) > 0;
    }

    /**
     * firstDate<secondDate
     *
     * @param firstDate
     * @param secondDate
     * @param level      等级，年，月，日，时，分秒 COMPARELEVEL_*
     * @return true or false
     * @see #compareDateStringByLevel
     */
    public static boolean firstDateStrBeforeSecondDateStr(String firstDate, String secondDate, int level) {

        return compareDateStringByLevel(firstDate, secondDate, level) < 0;
    }

    /**
     * 两个日期 是否相等
     *
     * @param firstCalendar
     * @param seCalendar
     * @param level         等级，年，月，日，时，分秒 COMPARELEVEL_*
     * @return true or false
     * @see #compareCalendarByLevel
     */
    public static boolean firstDateStrEquleSecondDateStr(Calendar firstCalendar, Calendar seCalendar, int level) {

        return compareCalendarByLevel(firstCalendar, seCalendar, level) == 0;
    }

    /**
     * 两个日期 是否相等
     *
     * @param firstDate
     * @param secondDate
     * @param level      等级，年，月，日，时，分秒 COMPARELEVEL_*
     * @return true or false
     * @see #compareCalendarByLevel
     */
    public static boolean firstDateStrEquleSecondDateStr(String firstDate, String secondDate, int level) {

        return compareDateStringByLevel(firstDate, secondDate, level) == 0;
    }

    /**
     * firstCalendar是否晚于seCalendar
     *
     * @param firstCalendar
     * @param seCalendar
     * @param level         等级，年，月，日，时，分秒 COMPARELEVEL_*
     * @return true or false
     */
    public static boolean firstCalendarAfterSecondCalendar(Calendar firstCalendar, Calendar seCalendar, int level) {

        return compareCalendarByLevel(firstCalendar, seCalendar, level) > 0;
    }

    /**
     * firstCalendar是否早于seCalendar
     *
     * @param firstCalendar
     * @param seCalendar
     * @param level         等级，年，月，日，时，分秒 COMPARELEVEL_*
     * @return true or false
     * @see #compareCalendarByLevel
     */
    public static boolean firstCalendarBeforeSecondCalendar(Calendar firstCalendar, Calendar seCalendar, int level) {


        return compareCalendarByLevel(firstCalendar, seCalendar, level) < 0;
    }

    /**
     * 两个日期 是否相等
     *
     * @param firstCalendar
     * @param seCalendar
     * @param level         等级，年，月，日，时，分秒 COMPARELEVEL_*
     * @return true or false
     * @see #compareCalendarByLevel
     */
    public static boolean firstCalendarEquleSecondCalendar(Calendar firstCalendar, Calendar seCalendar, int level) {

        return compareCalendarByLevel(firstCalendar, seCalendar, level) == 0;
    }

    /**
     * firstDate是否晚于当前日期
     *
     * @param firstCalendar
     * @param level
     * @return true or false
     * @see #getCurrentCalendar
     */
    public static boolean dateCalendarAfterToday(Calendar firstCalendar, int level) {
        Calendar secondCalendar = getCurrentCalendar();
        return firstCalendarAfterSecondCalendar(firstCalendar, secondCalendar, level);
    }

    /**
     * firstDate是否 等于当前日期
     *
     * @param firstCalendar
     * @param level
     * @return true or false
     */
    public static boolean dateCalendarEqulsToday(Calendar firstCalendar, int level) {
        Calendar secondCalendar = getCurrentCalendar();
        return firstDateStrEquleSecondDateStr(firstCalendar, secondCalendar, level);
    }

    /**
     * firstDate是否 早于当前日期
     *
     * @param firstCalendar
     * @param level
     * @return true or false
     */
    public static boolean dateCalendarBeforeToday(Calendar firstCalendar, int level) {
        Calendar secondCalendar = getCurrentCalendar();
        return firstCalendarBeforeSecondCalendar(firstCalendar, secondCalendar, level);
    }

    /**
     * firstDate是否晚于当前日期
     *
     * @param firstDate
     * @param level
     * @return true or false
     * @see #getCurrentTime
     */
    public static boolean dateStringAfterToday(String firstDate, int level) {
        String secondDate = getCurrentTime();
        return firstDateStrAfterSecondDateStr(firstDate, secondDate, level);
    }

    /**
     * firstDate是否早于当前日期
     *
     * @param firstDate
     * @param level
     * @return true or false
     * @see #firstDateStrBeforeSecondDateStr
     * @see #getCurrentTime
     */
    public static boolean dateStringBeforeToday(String firstDate, int level) {
        String secondDate = getCurrentTime();
        return firstDateStrBeforeSecondDateStr(firstDate, secondDate, level);
    }

    /**
     * firstDate是否等于当前日期
     *
     * @param firstDate
     * @param level     比较级别
     * @return true or false
     * @see #dateStringEquls
     * @see #getCurrentTime
     */
    public static boolean dateStringEqulsToday(String firstDate, int level) {
        String secondDate = getCurrentTime();
        return dateStringEquls(firstDate, secondDate, level);
    }

    /**
     * 农历一年的总天数
     *
     * @param year
     * @return
     * @see #lunarInfo
     */
    public static int yearDays(int year) {
        int i, sum = 348;
        for (i = 0x8000; i > 0x8; i >>= 1) {
            if ((lunarInfo[year - 1900] & i) != 0)
                sum += 1;
        }
        return (sum + leapDays(year));
    }

    /**
     * 农历 y年闰月的天数
     *
     * @param year
     * @return
     * @see #lunarInfo
     */
    public static int leapDays(int year) {
        if (leapMonth(year) != 0) {
            if ((lunarInfo[year - 1900] & 0x10000) != 0)
                return 30;
            else
                return 29;
        } else
            return 0;
    }

    /**
     * 农历 y年闰哪个月 1-12 , 没闰返回 0
     *
     * @param year
     * @return
     * @see #lunarInfo
     */
    public static int leapMonth(int year) {
        return (int) (lunarInfo[year - 1900] & 0xf);
    }

    /**
     * 农历 y年m月的总天数
     *
     * @param year
     * @param month
     * @return
     * @see #lunarInfo
     */
    public static int monthDays(int year, int month) {
        if ((lunarInfo[year - 1900] & (0x10000 >> month)) == 0)
            return 29;
        else
            return 30;
    }

    /**
     * 将时间字串进行转换 取掉:号，并将每段补齐成两位显示
     *
     * @param d 00:00格式
     * @return 000000
     */
    public static String parseTude(String d) {
        String r = "";
        if (d != null && !"".equals(d)) {
            int i = d.indexOf(":");
            r += d.substring(0, i);
            if (r.length() == 1) {
                r = "0" + r;
            }
            String ss = d.substring(i + 1, d.length());
            if (ss.length() == 1) {
                ss = "0" + ss;
            }
            r = r + ss + "00";
        }
        return r;
    }

    /**
     * 1.航班筛选 daparDate出发Hout 是否在 fCTimeHorizon开始结束Hour内,不在则返回false 2.如果出发hour等于开始Hour或结束Hour时如果departDate中Minute等于0时返回true
     *
     * @param departDate    航班起飞时间
     * @param fCTimeHorizon 筛选区间
     * @return true or false
     */
    public static boolean isInTimeHorizon(String departDate, String fCTimeHorizon) {
        if (departDate.length() < 14 || fCTimeHorizon.length() < 11) {
            return false;
        }
        int departTime = Integer.parseInt(departDate.substring(8, 12));
        int startTime = Integer.parseInt(fCTimeHorizon.substring(0, 2) + fCTimeHorizon.substring(3, 5));
        int endTime = Integer.parseInt(fCTimeHorizon.substring(6, 8) + fCTimeHorizon.substring(9, 11));

        return departTime >= startTime && departTime <= endTime;
        // int departHour = Integer.parseInt(departDate.substring(8, 10));
        // int startHour = Integer.parseInt(fCTimeHorizon.substring(0, 2));
        // int endHour = Integer.parseInt(fCTimeHorizon.substring(6, 8));
        // if (departHour < startHour || departHour > endHour) {
        // return false;
        // }
        // if (departHour == startHour || departHour == endHour) {
        // int departMinute = Integer.parseInt(departDate.substring(10, 12));
        // if (departMinute == 0) {
        // return false ;
        // } else {
        // return true;
        // }
        // }
        // return true;
    }

    /**
     * 2012-2022 国定节假日
     *
     * @param dateStr yyyyMMdd格式
     * @return 节假日名称，""为非节假日
     */
    public static String getHolidayString(String dateStr) {
        if (dateStr == null || dateStr.length() < 8)
            return "";
        int inYear = StringUtil.toInt(dateStr.substring(0, 4));
        int inMonth = StringUtil.toInt(dateStr.substring(4, 6));
        int inDate = StringUtil.toInt(dateStr.substring(6, 8));
        switch (inYear) {
            case 2012:
                switch (inMonth) {
                    case 1:
                        switch (inDate) {
                            case 1:
                                return "元旦";
                            // model 22:
                            // return "除夕";
                            case 23:
                                return "春节";
                        }
                        break;
                    case 2:
                        switch (inDate) {
                            case 14:
                                return "情人节";
                        }
                        break;
                    case 4:
                        switch (inDate) {
                            case 4:
                                return "清明节";
                        }
                        break;
                    case 5:
                        switch (inDate) {
                            case 1:
                                return "劳动节";
                        }
                        break;
                    case 6:
                        switch (inDate) {
                            case 23:
                                return "端午节";
                        }
                        break;
                    case 9:
                        switch (inDate) {
                            case 30:
                                return "中秋节";
                        }
                        break;
                    case 10:
                        switch (inDate) {
                            case 1:
                                return "国庆节";
                        }
                        break;
                    case 12:
                        switch (inDate) {
                            case 25:
                                return "圣诞节";
                        }
                }
                break;
            case 2013:
                switch (inMonth) {
                    case 1:
                        switch (inDate) {
                            case 1:
                                return "元旦";
                        }
                        break;
                    case 2:
                        switch (inDate) {
                            // model 9:
                            // return "除夕";
                            case 10:
                                return "春节";
                            case 14:
                                return "情人节";
                        }
                        break;
                    case 4:
                        switch (inDate) {
                            case 4:
                                return "清明节";
                        }
                        break;
                    case 5:
                        switch (inDate) {
                            case 1:
                                return "劳动节";
                        }
                        break;
                    case 6:
                        switch (inDate) {
                            case 12:
                                return "端午节";
                        }
                        break;
                    case 9:
                        switch (inDate) {
                            case 19:
                                return "中秋节";
                        }
                        break;
                    case 10:
                        switch (inDate) {
                            case 1:
                                return "国庆节";
                        }
                        break;
                    case 12:
                        switch (inDate) {
                            case 25:
                                return "圣诞节";
                        }
                }
                break;
            case 2014:
                switch (inMonth) {
                    case 1:
                        switch (inDate) {
                            case 1:
                                return "元旦";
                    /*
                     * modified by junyingding for v5.3changelist at 2013.12.24 新增需求： 2014年日历日期下面增加小字“除夕”“初一”，取消原“春节”的文案
					 */
                            case 30:
                                return "除夕";
                            case 31:
                                return "初一";
                        }
                        break;
                    case 2:
                        switch (inDate) {

                            case 14:
                                return "情人节";
                        }
                        break;
                    case 4:
                        switch (inDate) {
                            case 5:
                                return "清明节";
                        }
                        break;
                    case 5:
                        switch (inDate) {
                            case 1:
                                return "劳动节";
                        }
                        break;
                    case 6:
                        switch (inDate) {
                            case 2:
                                return "端午节";
                        }
                        break;
                    case 9:
                        switch (inDate) {
                            case 8:
                                return "中秋节";
                        }
                        break;
                    case 10:
                        switch (inDate) {
                            case 1:
                                return "国庆节";
                        }
                        break;
                    case 12:
                        switch (inDate) {
                            case 25:
                                return "圣诞节";
                        }
                }
                break;
            case 2015:
                switch (inMonth) {
                    case 1:
                        switch (inDate) {
                            case 1:
                                return "元旦";
                        }
                        break;
                    case 2:
                        switch (inDate) {
                            // model 18:
                            // return "除夕";
                            case 19:
                                return "春节";
                            case 14:
                                return "情人节";
                        }
                        break;
                    case 4:
                        switch (inDate) {
                            case 5:
                                return "清明节";
                        }
                        break;
                    case 5:
                        switch (inDate) {
                            case 1:
                                return "劳动节";
                        }
                        break;
                    case 6:
                        switch (inDate) {
                            case 20:
                                return "端午节";
                        }
                        break;
                    case 9:
                        switch (inDate) {
                            case 27:
                                return "中秋节";
                        }
                        break;
                    case 10:
                        switch (inDate) {
                            case 1:
                                return "国庆节";
                        }
                        break;
                    case 12:
                        switch (inDate) {
                            case 25:
                                return "圣诞节";
                        }
                }
                break;
            case 2016:
                switch (inMonth) {
                    case 1:
                        switch (inDate) {
                            case 1:
                                return "元旦";
                        }
                        break;
                    case 2:
                        switch (inDate) {
                            // model 7:
                            // return "除夕";
                            case 8:
                                return "春节";
                            case 14:
                                return "情人节";
                        }
                        break;
                    case 4:
                        switch (inDate) {
                            case 4:
                                return "清明节";
                        }
                        break;
                    case 5:
                        switch (inDate) {
                            case 1:
                                return "劳动节";
                        }
                        break;
                    case 6:
                        switch (inDate) {
                            case 9:
                                return "端午节";
                        }
                        break;
                    case 9:
                        switch (inDate) {
                            case 15:
                                return "中秋节";
                        }
                        break;
                    case 10:
                        switch (inDate) {
                            case 1:
                                return "国庆节";
                        }
                        break;
                    case 12:
                        switch (inDate) {
                            case 25:
                                return "圣诞节";
                        }
                }
                break;
            case 2017:
                switch (inMonth) {
                    case 1:
                        switch (inDate) {
                            case 1:
                                return "元旦";
                            // model 27:
                            // return "除夕";
                            case 28:
                                return "春节";
                        }
                        break;
                    case 2:
                        switch (inDate) {
                            case 14:
                                return "情人节";
                        }
                        break;
                    case 4:
                        switch (inDate) {
                            case 4:
                                return "清明节";
                        }
                        break;
                    case 5:
                        switch (inDate) {
                            case 1:
                                return "劳动节";
                            case 30:
                                return "端午节";
                        }
                        break;
                    case 10:
                        switch (inDate) {
                            case 1:
                                return "国庆节";
                            case 4:
                                return "中秋节";
                        }
                        break;
                    case 12:
                        switch (inDate) {
                            case 25:
                                return "圣诞节";
                        }
                }
                break;
            case 2018:
                switch (inMonth) {
                    case 1:
                        switch (inDate) {
                            case 1:
                                return "元旦";
                        }
                        break;
                    case 2:
                        switch (inDate) {
                            case 14:
                                return "情人节";
                            // model 15:
                            // return "除夕";
                            case 16:
                                return "春节";
                        }
                        break;
                    case 4:
                        switch (inDate) {
                            case 5:
                                return "清明节";
                        }
                        break;
                    case 5:
                        switch (inDate) {
                            case 1:
                                return "劳动节";
                        }
                        break;
                    case 6:
                        switch (inDate) {
                            case 18:
                                return "端午节";
                        }
                        break;
                    case 9:
                        switch (inDate) {
                            case 24:
                                return "中秋节";
                        }
                        break;
                    case 10:
                        switch (inDate) {
                            case 1:
                                return "国庆节";
                        }
                        break;
                    case 12:
                        switch (inDate) {
                            case 25:
                                return "圣诞节";
                        }
                }
                break;
            case 2019:
                switch (inMonth) {
                    case 1:
                        switch (inDate) {
                            case 1:
                                return "元旦";
                        }
                        break;
                    case 2:
                        switch (inDate) {
                            // model 4:
                            // return "除夕";
                            case 5:
                                return "春节";
                            case 14:
                                return "情人节";
                        }
                        break;
                    case 4:
                        switch (inDate) {
                            case 5:
                                return "清明节";
                        }
                        break;
                    case 5:
                        switch (inDate) {
                            case 1:
                                return "劳动节";
                        }
                        break;
                    case 6:
                        switch (inDate) {
                            case 7:
                                return "端午节";
                        }
                        break;
                    case 9:
                        switch (inDate) {
                            case 13:
                                return "中秋节";
                        }
                        break;
                    case 10:
                        switch (inDate) {
                            case 1:
                                return "国庆节";
                        }
                        break;
                    case 12:
                        switch (inDate) {
                            case 25:
                                return "圣诞节";
                        }
                }
                break;
            case 2020:
                switch (inMonth) {
                    case 1:
                        switch (inDate) {
                            case 1:
                                return "元旦";
                            // model 24:
                            // return "除夕";
                            case 25:
                                return "春节";
                        }
                        break;
                    case 2:
                        switch (inDate) {
                            case 14:
                                return "情人节";
                        }
                        break;
                    case 4:
                        switch (inDate) {
                            case 4:
                                return "清明节";
                        }
                        break;
                    case 5:
                        switch (inDate) {
                            case 1:
                                return "劳动节";
                        }
                        break;
                    case 6:
                        switch (inDate) {
                            case 25:
                                return "端午节";
                        }
                        break;
                    case 10:
                        switch (inDate) {
                            case 1:
                                return "国庆节";
                        }
                        break;
                    case 12:
                        switch (inDate) {
                            case 25:
                                return "圣诞节";
                        }
                }
                break;
            case 2021:
                switch (inMonth) {
                    case 1:
                        switch (inDate) {
                            case 1:
                                return "元旦";
                        }
                        break;
                    case 2:
                        switch (inDate) {
                            // model 11:
                            // return "除夕";
                            case 12:
                                return "春节";
                            case 14:
                                return "情人节";
                        }
                        break;
                    case 4:
                        switch (inDate) {
                            case 4:
                                return "清明节";
                        }
                        break;
                    case 5:
                        switch (inDate) {
                            case 1:
                                return "劳动节";
                        }
                        break;
                    case 6:
                        switch (inDate) {
                            case 14:
                                return "端午节";
                        }
                        break;
                    case 9:
                        switch (inDate) {
                            case 21:
                                return "中秋节";
                        }
                        break;
                    case 10:
                        switch (inDate) {
                            case 1:
                                return "国庆节";
                        }
                        break;
                    case 12:
                        switch (inDate) {
                            case 25:
                                return "圣诞节";
                        }
                }
                break;
            case 2022:
                switch (inMonth) {
                    case 1:
                        switch (inDate) {
                            case 1:
                                return "元旦";
                            // model 31:
                            // return "除夕";
                        }
                        break;
                    case 2:
                        switch (inDate) {
                            case 1:
                                return "春节";
                            case 14:
                                return "情人节";
                        }
                        break;
                    case 4:
                        switch (inDate) {
                            case 5:
                                return "清明节";
                        }
                        break;
                    case 5:
                        switch (inDate) {
                            case 1:
                                return "劳动节";
                        }
                        break;
                    case 6:
                        switch (inDate) {
                            case 3:
                                return "端午节";
                        }
                        break;
                    case 9:
                        switch (inDate) {
                            case 10:
                                return "中秋节";
                        }
                        break;
                    case 10:
                        switch (inDate) {
                            case 1:
                                return "国庆节";
                        }
                        break;
                    case 12:
                        switch (inDate) {
                            case 25:
                                return "圣诞节";
                        }
                }
                break;
            default:
                break;
        }
        return "";
    }

    /**
     * 给日期 字符串中的（今天、明天、后天）上色
     *
     * @param dateStr
     * @return
     */
    public static SpannableStringBuilder colorDate(String dateStr) {
        if (dateStr.indexOf("今天") > 0 || dateStr.indexOf("明天") > 0 || dateStr.indexOf("后天") > 0) {
            SpannableStringBuilder styleStr = new SpannableStringBuilder(dateStr);
            styleStr.setSpan(new ForegroundColorSpan(Color.rgb(209, 0, 8)), 12, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return styleStr;
        }
        return null;
    }

    /**
     * 功能描述:判断日期是否为今天且当前时间在0:00-06:00
     *
     * @param calendar
     * @return 若日期为今天且时间在0:00-06:00，返回ture；否则返回false
     */
    public static boolean isCurrentDateMidnight(Calendar calendar) {
        // if (calendar == null) {
        // // 空保护
        // return false;
        // }
        Calendar currentDay = getCurrentCalendar();
        if (currentDay != null && calendar != null && currentDay.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && currentDay.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && currentDay.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
            // 当天
            Calendar time1 = Calendar.getInstance();
            time1.set(time1.get(Calendar.YEAR), time1.get(Calendar.MONTH), time1.get(Calendar.DAY_OF_MONTH), 6, 0);
            long result = compareCalendarByLevel(currentDay, time1, COMPARELEVEL_MINUTE);
            return result <= 0;
        } else {
            // 非当天
            return false;
        }
    }

    /**
     * 将yyyyMMdd字符串转成yyyy年MM月dd日 格式
     *
     * @param dateStr
     * @return
     */
    public static String getShowStrByDateStr(String dateStr) {
        Calendar currentDate = getCalendarByDateStr(dateStr);
        if (currentDate == null) {
            return "";
        }
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH) + 1;
        int day = currentDate.get(Calendar.DAY_OF_MONTH);
        StringBuffer showStringBuffer = new StringBuffer(18);
        showStringBuffer.append(year);
        showStringBuffer.append("年");
        showStringBuffer.append(month);
        showStringBuffer.append("月");
        showStringBuffer.append(day);
        showStringBuffer.append("日");
        return showStringBuffer.toString().trim();
    }

    /**
     * 功能描述: 返回航班看板刷新时间
     * <p>
     * <pre>
     *     xinyh:   2013-5-8      新建
     * </pre>
     *
     * @param calendar
     * @return
     */
/*    public static int getInRefreshTimeRangeType(Calendar calendar) {
        int type = -1;
        if (calendar != null) {
            Calendar temp = calculateCalendar(getCurrentCalendar(), DateUtil.HOUR, -4);
            if (firstCalendarAfterSecondCalendar(temp, calendar, COMPARELEVEL_DAY)) {
                type = 1;
            } else if (firstCalendarBeforeSecondCalendar(calendar, temp, COMPARELEVEL_DAY)) {
                type = -1;
            } else {
                type = 0;
            }
        }
        return type;
    }*/

    /**
     * 根据14位日期字符串返回00:00这样的时间字符串,空则返回""
     *
     * @param date
     * @return
     */
/*    public static String getTimeStrFromDateString(String date) {
        Calendar temp = getCalendarByDateTimeStr(date);
        if (temp == null) {
            return "";
        }
        return getCalendarStrBySimpleDateFormat(temp, SIMPLEFORMATTYPE13);

    }*/

    /**
     * 根据8位日期字符串返回x月x日这样的日期字符串,空则返回""
     *
     * @param date
     * @return
     */
    public static String getSimpleDateStrFromDateString(String date) {
        Calendar temp = getCalendarByDateStr(date);
        if (temp == null) {
            return "";
        }
        return getCalendarStrBySimpleDateFormat(temp, SIMPLEFORMATTYPE11);

    }

  /*  *//**
     * @param dateStr
     * @return
     *//*
    public static String getAgendaItemDate(String dateStr) {
        String result = "";
        if (TextUtils.isEmpty(dateStr)) return result;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = sdf.parse(dateStr);
            calendar.setTime(date);
        } catch (ParseException e) {
            LogUtil.e("日期格式转换错误");
            return result;
        }
        String[] dateTime = dateStr.split(" ");
        String date = dateTime[0];
        //String time = dateTime[1].substring(0, index);
        String week = getShowWeekByCalendar(calendar);
        int hour = calendar.get(Calendar.HOUR);
        if (hour == 0) {
            hour = 12;
        }
        String time = String.format("%02d", hour) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE));
        String am_pm = calendar.get(Calendar.AM_PM) == 0 ? "上午" : "下午";
        result = date + "(" + week + ")" + " " + am_pm + time;
        return result;
    }*/
/*
    public static String getAgendaListItemDate(String dateStr) {
        String result = "";
        if (TextUtils.isEmpty(dateStr)) return result;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = sdf.parse(dateStr);
            calendar.setTime(date);
        } catch (ParseException e) {
            LogUtil.e("日期格式转换错误");
        }
        *//**几月几日*//*
        String month = getCalendarStrBySimpleDateFormat(calendar, SIMPLEFORMATTYPE11);
        String week = getShowWeekByCalendar(calendar);
        String am_pm = calendar.get(Calendar.AM_PM) == 0 ? "上午" : "下午";
        int hour = calendar.get(Calendar.HOUR);
        if (hour == 0) {
            hour = 12;
        }
        String time = String.format("%02d", hour) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE));
        result = month + "(" + week + ")" + " " + am_pm + time;
        return result;
    }*/

    /**
     * 发布时间格式： 1小时内:XX分钟前 24小时内：XX小时前 7天内：X天前 超过7天：显示日期
     *
     * @param date
     * @return
     */
    public static String fattrDate(Date date) {
        long time = System.currentTimeMillis() - date.getTime();// 得到 当前时间的毫秒数
        double hours = time / 1000 / 60 / 60d;// 小时
        String fattrStr = "";
        if ((hours / 24) >= 7) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            fattrStr = sdf.format(date);
        } else if (hours < 1) {
            double t = (time / 1000 / 60d);
            fattrStr = (t <= 1 ? "1" : Math.round(t)) + "分钟前发布";
        } else if (1 <= hours && hours < 24) {
            fattrStr = Math.round(hours) + "小时前发布";
        } else if ((hours / 24) >= 1 && (hours / 24) < 7) {
            double t = (hours / 24d);
            fattrStr = (t <= 1 ? "1" : Math.round(t)) + "天前发布";
        }
        return fattrStr;
    }

    public static String formatDate2(Object obj) {
        if (obj == null) {
            return "";
        }
        Date date = new Date();
        try {
            date = (Date) obj;
        } catch (Exception e) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(date);
    }

    /**
     * 发布时间描述 当日：今日发布 ； 其他： XX天前发布
     *
     * @param smdate
     * @return
     */
    public static String pubDateFormat(Date smdate) {
        Date bdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        String formatStr = "";

        if (between_days == 0) {
            formatStr = "今日发布";
        } else {
            formatStr = between_days + "天前发布";
        }

        return formatStr;
    }

    /**
     * 功能描述:将制定格式的字符串转为Date类型
     * <p>
     * <pre>
     * Modify Reason:(修改原因,不需覆盖，直接追加.)
     *     hubin:   2014年6月17日      新建
     * </pre>
     *
     * @param dateString
     * @param formatStr
     * @return
     */
    public static Date string2date(String dateString, String formatStr) {
        Date formateDate = null;
        DateFormat format = new SimpleDateFormat(formatStr);
        try {
            formateDate = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return formateDate;
    }

    public static String date2string(Date date, String formatStr) {
        String strDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        strDate = sdf.format(date);
        return strDate;
    }

    public static String todayString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");// 设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    /**
     * 功能描述:格式化日期字符串显示格式
     * <p>
     * <pre>
     * Modify Reason:(修改原因,不需覆盖，直接追加.)
     *     hubin:   2014年6月17日      新建
     * </pre>
     *
     * @param sdate  原始日期格式 s - 表示 "yyyy-mm-dd" 形式的日期的 String 对象
     * @param format 格式化后日期格式
     * @return 格式化后的日期显示
     */
    public static String dateFormat(String sdate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        java.sql.Date date = java.sql.Date.valueOf(sdate);
        String dateString = formatter.format(date);

        return dateString;
    }

    /**
     * 根据指定的年、月、日返回当前是星期几。1表示星期天、2表示星期一、7表示星期六。
     *
     * @param year
     * @param month month是从1开始的12结束
     * @param day
     * @return 返回一个代表当期日期是星期几的数字。1表示星期天、2表示星期一、7表示星期六。
     */
    public static int getDayOfWeek(String year, String month, String day) {
        Calendar cal = new GregorianCalendar(Integer.valueOf(year), Integer.valueOf(month).intValue() - 1, Integer.valueOf(day).intValue());
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 根据指定的年、月、日返回当前是星期几。1表示星期天、2表示星期一、7表示星期六。
     *
     * @param date "yyyy/MM/dd",或者"yyyy-MM-dd"
     * @return 返回一个代表当期日期是星期几的数字。1表示星期天、2表示星期一、7表示星期六。
     */
    public static int getDayOfWeek(String date) {
        String[] temp = null;
        if (date.indexOf("/") > 0) {
            temp = date.split("/");
        }
        if (date.indexOf("-") > 0) {
            temp = date.split("-");
        }
        return getDayOfWeek(temp[0], temp[1], temp[2]);
    }

    /**
     * 根据指定的年、月、日返回当前是星期几。1表示星期天、2表示星期一、7表示星期六。
     *
     * @param date
     * @return 返回一个代表当期日期是星期几的数字。1表示星期天、2表示星期一、7表示星期六。
     */
    public static int getDayOfWeek(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 返回当前日期是星期几。例如：星期日、星期一、星期六等等。
     *
     * @param date 格式为 yyyy/MM/dd 或者 yyyy-MM-dd
     * @return 返回当前日期是星期几
     */
    public static String getChinaDayOfWeek(Date date) {
        String[] weeks = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int week = getDayOfWeek(date);
        return weeks[week - 1];
    }

    /**
     * 返回当前日期是星期几。例如：星期日、星期一、星期六等等。
     *
     * @param date 格式为 yyyy/MM/dd 或者 yyyy-MM-dd
     * @return 返回当前日期是星期几
     */
    public static String getChinaDayOfWeek(String date) {
        String[] weeks = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int week = getDayOfWeek(date);
        return weeks[week - 1];
    }

    /**
     * 今天0点时间搓
     *
     * @return
     */
    public static long getTodayTimeInMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 功能描述:将US格式的日期格式化为指定日期格式
     * <p>
     * <pre>
     *      eg.from "12/Aug/2014:23:57:47" to "2014-08-12 23:57:47"
     * Modify Reason:(修改原因,不需覆盖，直接追加.)
     *     hu-bin:   2014年8月14日      新建
     * </pre>
     *
     * @param datestr   日期字符串
     * @param oraFormat 原来格式
     * @param desFormat 转化后的格式
     * @return
     */
    public static String USdatestrFormat(String datestr, String oraFormat, String desFormat) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat(oraFormat, Locale.US);
        Date d = new Date();
        try {
            d = bartDateFormat.parse(datestr);
        } catch (ParseException e) {// TODO 自动生成 catch 块                e.printStackTrace();
        }
        String dstr = DateUtil.date2string(d, desFormat);
        return dstr;
    }


    /**
     * 取得给定格式的昨天的日期输出
     *
     * @param format 日期输出的格式
     * @return String 给定格式的日期字符串.
     */
    public static String getFormatYestoday(String format) {
        return getFormatCurrentAdd(-1, format);
    }

    /**
     * 获得当前日期固定间隔天数的日期，如前60天dateAdd(-60)
     *
     * @param amount 距今天的间隔日期长度，向前为负，向后为正
     * @param format 输出日期的格式.
     * @return java.lang.String 按照格式输出的间隔的日期字符串.
     */
    public static String getFormatCurrentAdd(int amount, String format) {

        Date d = getDateAdd(new Date(), amount);

        return getFormatDateTime(d, format);
    }

    /**
     * 根据给定的格式与时间(Date类型的)，返回时间字符串。最为通用。
     *
     * @param date   指定的日期
     * @param format 日期格式字符串
     * @return String 指定格式的日期字符串.
     */
    public static String getFormatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 功能描述:取得给定日期加上一定天数后的日期对象.
     * <p>
     * <pre>
     * Modify Reason:(修改原因,不需覆盖，直接追加.)
     *     hu-bin:   2014年8月25日      新建
     * </pre>
     *
     * @param date   给定的日期对象
     * @param amount 需要添加的天数，如果是向前的天数，使用负数就可以.
     * @return Date 加上一定天数以后的Date对象.
     */
    public static Date getDateAdd(Date date, int amount) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(GregorianCalendar.DATE, amount);
        return cal.getTime();
    }

    /**
     * 是否是今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE));
        Date today = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(today).equals(format.format(date));
    }

    /**
     * 是否是昨天
     *
     * @param date
     * @return
     */
    public static boolean isYesterday(Date date) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
        Date today = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(today).equals(format.format(date));
    }

    /**
     * 获取时间展示
     *
     * @param time
     * @return
     */
    public static String getMessageTime(long time) {
        Date date = new Date(time);
        if (isToday(date)) {
            //今天
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            return formatter.format(date);
        } else if (isYesterday(date)) {
            //昨天
            return "昨天";
        } else {
            //其他
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
            return formatter.format(date);
        }
    }

}
