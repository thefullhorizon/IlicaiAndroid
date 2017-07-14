package com.ailicai.app.common.utils;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * 校验字符串数组，是否EmptyOrNull
     *
     * @param valueAndNameArr 二维数组
     * @param errorInfo
     * @return
     */
    public static boolean checkEmptyOrNull(String[][] valueAndNameArr, StringBuilder errorInfo) {
        boolean flag = true;
        for (String[] valueAndName : valueAndNameArr) {
            if (TextUtils.isEmpty(valueAndName[0])) {
                errorInfo.append(valueAndName[1] + ",");
                flag = false;
            }
        }
        if (!flag) {
            errorInfo.append(" can't be emptyOrNull!");
        }
        return flag;
    }

    /**
     * 号码11位,为返回xxx-xxxx-xxxx
     *
     * @param number
     * @return
     */
    public static String formatMobileNumber(String number) {
        /**
         if (TextUtils.isEmpty(number) || number.length() != 11)
         return number;
         String num1 = number.substring(0, 3);
         String num2 = number.substring(3, 7);
         String num3 = number.substring(7, 11);
         return num1 + "-" + num2 + "-" + num3;
         */

        if (!ValidateUtil.isValidMobile(number))
            return number;
        String num1 = number.substring(0, 3);
        String num2 = number.substring(number.length() - 4, number.length());
        String formatStr = num1 + "-****-" + num2;
        if (number.length() == 15) {
            formatStr = num1 + "-****-****-" + num2;
        }
        return formatStr;
    }

    /**
     * 号码11位,为返回xxx-xxxx-xxxx
     *
     * @param number
     * @return
     */
    public static String formatMobileNumber(String number, String formatSuffix) {
        //if (TextUtils.isEmpty(number) || number.length() != 11)
        //特殊需要
        if (TextUtils.isEmpty(number) || number.length() < 11)
            return number;
        StringBuilder sb = new StringBuilder();
        String num1 = number.substring(0, 3);
        String num2 = number.substring(3, 7);
        String num3 = number.substring(7, 11);
        sb.append(num1);
        sb.append(formatSuffix);
        sb.append(num2);
        sb.append(formatSuffix);
        sb.append(num3);
        if (number.length() > 11) {
            String num4 = number.substring(11, number.length());
            sb.append(formatSuffix);
            sb.append(num4);
        }
        return sb.toString();
    }

    /**
     * 检测手机号码的位数，并插入空格：135 6409 0019
     *
     * @param number
     * @param formatSuffix
     * @return
     */
    public static String formatCheckMobileNumber(String number, String formatSuffix) {
        if (number.length() > 3 && number.length() <= 7) {
            String num1 = number.substring(0, 3);
            String num2 = number.substring(3, number.length());
            return num1 + formatSuffix + num2;
        } else if (number.length() > 7 && number.length() <= 11) {
            String num1 = number.substring(0, 3);
            String num2 = number.substring(3, 7);
            String num3 = number.substring(7, number.length());
            return num1 + formatSuffix + num2 + formatSuffix + num3;
        }
        return number;
    }

    /**
     * 去除所有空格
     *
     * @param str
     * @return
     */
    public static String allSpaceFormat(String str) {
        if (str != null) {
            return str.replaceAll(" ", "");
        }
        return "";
    }

    /**
     * 只显示前三位+****+后四位
     *
     * @param number
     * @return
     */
    public static String formatMobileSub(String number) {
        if (!ValidateUtil.isValidMobile(number))
            return number;
        String num1 = number.substring(0, 3);
        String num2 = number.substring(number.length() - 4, number.length());
        String formatStr = num1 + getStarSub(number) + num2;
        return formatStr;
    }


    private static String getStarSub(String number) {
        String formatStr = "";
        if (number.length() == 11) {
            formatStr = " **** ";
        } else if (number.length() == 12) {
            formatStr = " ***** ";
        } else if (number.length() == 13) {
            formatStr = " ****** ";
        } else if (number.length() == 14) {
            formatStr = " ******* ";
        } else if (number.length() == 15) {
            formatStr = " **** **** ";
        }
        return formatStr;
    }

    /**
     * 将inputStr 根据 encode 编码，补空格
     * </pre>
     *
     * @param inputStr
     * @param fillLength
     * @param encode     编码
     * @return
     */
    public static String FillStr(String inputStr, int fillLength, String encode) {
        int inputStrLength;
        try {
            inputStrLength = inputStr.getBytes(encode).length;
            if (inputStrLength == fillLength) {
                return inputStr;
            } else if (inputStrLength > fillLength) {
                byte[] inputByte = inputStr.getBytes(encode);
                byte[] outputByte = new byte[fillLength];
                System.arraycopy(inputByte, 0, outputByte, 0, fillLength);
                return new String(outputByte, encode);
            } else {
                StringBuilder ctripStringBuilder = new StringBuilder();
                for (int i = 0; i < (fillLength - inputStrLength); i++)
                    ctripStringBuilder.append(" ");
                ctripStringBuilder.append(inputStr);
                return ctripStringBuilder.toString();
            }
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }

    /**
     * MD5
     *
     * @param source
     * @return
     */
    public static String getMD5(byte[] source) {
        StringBuilder sb = new StringBuilder();
        java.security.MessageDigest md5 = null;
        try {
            md5 = java.security.MessageDigest.getInstance("MD5");
            md5.update(source);
        } catch (NoSuchAlgorithmException e) {
            LogUtil.d("Exception", e.toString());
        }
        if (md5 != null) {
            for (byte b : md5.digest()) {
                sb.append(String.format("%02X", b));
            }
        }
        return sb.toString();
    }


    /**
     * 比较两个字符串是否相等（两个同为NULL不相等，两个同为""相等）
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean compareString(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        } else {
            return str1.equals(str2);
        }
    }

    /**
     * 无效数据返回-1
     *
     * @param intValue
     * @return
     */
    public static int toInt(String intValue) {
        try {
            return Integer.parseInt(intValue);
        } catch (Exception e) {
            return -1;
        }
    }

    private static DecimalFormat mformat = new DecimalFormat("########.## ");

    /**
     * 格式化输出 "########.## "
     *
     * @param f
     * @return
     */
    public static String fromatString(double f) {
        try {
            return mformat.format(f);
        } catch (Exception e) {
            return "-1";
        }
    }

    /**
     * @param builder
     * @param bold    是否加粗
     * @param size    字体大小
     * @param color   字体颜色
     * @param start   起始位置
     * @param end     结束位置
     * @return
     */
    public static SpannableStringBuilder format(SpannableStringBuilder builder, boolean bold, float size, int color, int start, int end) {
        if (bold) {
            builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        builder.setSpan(new RelativeSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (color != 0) {
            builder.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return builder;
    }

    /**
     * 将byte数组，转化成字符串，用服务端下发的统一编码
     *
     * @param byteArr
     * @return
     */
    public static String getStrFromByteArr(byte[] byteArr, String charsetName) {
        String retStr = "";
        if (byteArr != null) {
            try {
                retStr = new String(byteArr, charsetName);
            } catch (UnsupportedEncodingException e) {
            }
        }
        return retStr;
    }

    /**
     * 将两个string转化成int并比较
     *
     * @param s1
     * @param s2
     * @return
     */
    public static int compareStrToInt(String s1, String s2) {
        int i = 0;
        try {
            i = Integer.parseInt(s1) - Integer.parseInt(s2);
        } catch (Exception e) {
        }
        return i;
    }

    /**
     * 将string 转为long,异常时返回-1
     *
     * @param s
     * @return
     */
    public static long toLong(String s) {
        long i = 0L;
        try {
            i = Long.parseLong(s);
        } catch (Exception e) {
            i = -1L;
        }
        return i;
    }

    /**
     * 将string 转为float,异常时返回-1
     *
     * @param s
     * @return
     */
    public static float toFloat(String s) {
        float i = 0f;
        try {
            i = Float.parseFloat(s);
        } catch (Exception e) {
            i = -1f;
        }
        return i;
    }


    /**
     * 取字串小数点前整数
     *
     * @param s
     * @return
     */
    public static String toIntString(String s) {
        String intString = "";
        try {
            if (s.contains("."))
                intString = s.substring(0, s.indexOf("."));
            else
                intString = s;
        } catch (Exception e) {

        }
        return intString;
    }


    /**
     * 是否为中国籍，包含港澳台
     *
     * @param nationality
     * @return
     */
    public static boolean isValidChineseNationality(String nationality) {
        if ("".equals(nationality))
            return true;
        return "CNTWHKMO".indexOf(nationality.toUpperCase()) > -1;
    }

    /**
     * 航班号是否正确
     *
     * @param flightid
     * @return true or false
     */
    public static boolean isFlightID(String flightid) {
        Pattern p = Pattern.compile("^(CA|CZ|FM|MU|BK|JD|EU|CN|NS|HU|VD|G5|HO|KY|3U|SC|ZH|GS|PN|JR|MF|8L|KN|QF|OS)+[A-Z0-9]{3,4}");
        Matcher m = p.matcher(flightid);
        boolean valid = m.matches();
        return valid;
    }

    /**
     * 计算EditText中输入字串的长度(按字节计算)
     *
     * @param temp
     * @return
     */
    public static int strlen(CharSequence temp) {
        if (temp == null || temp.length() <= 0) {
            return 0;
        }

        int len = 0;
        char c;
        for (int i = temp.length() - 1; i >= 0; i--) {
            c = temp.charAt(i);
            if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                // 字母, 数字
                len++;
            } else {
                if (Character.isLetter(c)) { // 中文
                    len += 2;
                } else { // 符号或控制字符
                    len++;
                }
            }
        }

        return len;
    }

    /**
     * 获取时间字串并显示成HH:MM
     *
     * @param date 不能为空需大于12
     * @return
     */
    public static String getTime(String date) {
        if (!TextUtils.isEmpty(date) && date.length() >= 12) {
            return DateUtil.getCalendarStrBySimpleDateFormat(DateUtil.getCalendarByDateTimeStr(date), DateUtil.SIMPLEFORMATTYPE13);
        }
        return "";
    }

    /**
     * 将日期字串转为 2012-3-28
     *
     * @param date
     * @return
     */
    public static String parseDate(String date) {
        String str = "";
        if (date != null && date.length() >= 8) {
            str = DateUtil.getCalendarStrBySimpleDateFormat(DateUtil.getCalendarByDateStr(date), DateUtil.SIMPLEFORMATTYPE8);

        }
        return str;
    }

    /**
     * 将年，月，日，时，分，拼成14位长度的字串
     *
     * @param _year   年
     * @param _month  月
     * @param _day    日
     * @param _hour   时
     * @param _minute 分
     * @return 14位长度的字串
     */
    public static String formatDateString(int _year, int _month, int _day, int _hour, int _minute) {
        String value = String.valueOf(_year);
        if (_month < 10)
            value += "0" + _month;
        else
            value += _month;

        if (_day < 10)
            value += "0" + _day;
        else
            value += _day;

        if (_hour < 10)
            value += "0" + _hour;
        else
            value += _hour;

        if (_minute < 10)
            value += "0" + _minute;
        else
            value += _minute;

        value += "00";
        return value;
    }

    /**
     * 将年，月，日拼成一个8位长的字串
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 8位长的字串
     */
    public static String formatDateString(int year, int month, int day) {
        String value = String.valueOf(year);
        while (value.length() < 4) {
            value = "0" + value;
        }
        if (month < 10)
            value += "0" + month;
        else
            value += month;

        if (day < 10)
            value += "0" + day;
        else
            value += day;

        return value;
    }

    /**
     * 时间字符串格式转换 20101001082000 -> 2010-10-01 08:20
     *
     * @param timeStr
     * @return
     */
    public static String formatDateTimeString(String timeStr) {
        if (timeStr == null)
            return null;
        if (timeStr.length() < 14)
            return timeStr;
        String str = "";
        str = DateUtil.getCalendarStrBySimpleDateFormat(DateUtil.getCalendarByDateTimeStr(timeStr), DateUtil.SIMPLEFORMATTYPE4);
        return str;
    }

    /**
     * 时间格式转换并截取日期 20101001082000 -> 2010-10-01
     *
     * @param timeStr
     * @return
     */
    public static String formatDateString(String timeStr) {
        if (timeStr == null)
            return null;
        else if (timeStr.length() < 14)
            return timeStr;
        else {
            String dateStr = formatDateTimeString(timeStr).substring(0, 10);
            return dateStr;
        }
    }


    public static int getSeekBarProgress(int value, int minValue, int midValue, int maxValue) {
        if (value < 0)
            return 0;
        if (value <= midValue) {
            return 50 * (value - minValue) / (midValue - minValue);
        } else {
            return 50 + 50 * (value - midValue) / (maxValue - midValue);
        }
    }

    public static int getSeekBarValue(int progress, int minValue, int midValue, int maxValue) {
        if (progress < 0)
            return 0;

        if (progress <= 50) {
            return (int) ((midValue - minValue) * (progress / 50.00)) + minValue;
        } else {
            int overValue = (int) ((progress - 50) * (maxValue - midValue) / 50.00);
            return midValue + overValue;
        }
    }

    /**
     * string是否有效
     *
     * @param inputStr
     * @return
     */
    public static boolean isValidStr(String inputStr) {
        char[] charArray = inputStr.toCharArray();
        int length = charArray.length;
        for (int i = 0; i < length; i++) {
            if (!(charArray[i] >= '0' && charArray[i] <= '9') && !(charArray[i] >= 'A' && charArray[i] <= 'z') && charArray[i] != '_')
                return false;
        }

        return true;
    }

    /**
     * 返回一个中间有线的SpannableString
     *
     * @param s
     * @return
     */
    public static SpannableString getMidLineStr(String s) {
        SpannableString ss = new SpannableString(s);
        ss.setSpan(new StrikethroughSpan(), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * @param firstStr
     * @param secondStr
     * @return
     */
    public static boolean stringCompare(String firstStr, String secondStr) {
        if (TextUtils.isEmpty(firstStr))
            return false;
        return firstStr.compareTo(secondStr) > 0;
    }

    /**
     * 根据机型标志字段获取机型
     *
     * @param craftKind
     * @return
     */
    public static String getCraftKindStr(String craftKind) {
        String craftKindStr = "";
        if (craftKind.equalsIgnoreCase("M")) {
            craftKindStr = "中机型";
        } else if (craftKind.equalsIgnoreCase("S")) {
            craftKindStr = "小机型";
        } else if (craftKind.equalsIgnoreCase("L")) {
            craftKindStr = "大机型";
        }
        return craftKindStr;
    }

    public static String getDateMessage(String[] sa) {
        String s = "";
        if (sa != null && sa.length >= 4) {
            s += sa[0];
            if (toInt(sa[1]) < 10) {
                s += "0" + sa[1];
            } else {
                s += sa[1];
            }
            if (toInt(sa[2]) < 10) {
                s += "0" + sa[2];
            } else {
                s += sa[2];
            }
        }
        return s;
    }

    /**
     * 超过四位的字串前两位后加上:
     *
     * @param str
     * @return
     */
    public static String processTimeStr(String str) {
        String s = "";
        if (str != null && str.length() >= 4) {
            s = str.substring(0, 2) + ":" + str.substring(2, str.length());
        }
        return s;
    }

    /**
     * 将日期拆分并将年月日存入数组中
     *
     * @param date
     * @return
     */
    public static int[] getDateField(String date) {
        if (date != null && date.length() >= 8) {
            int[] fields = new int[3];
            fields[0] = toInt(date.substring(0, 4));
            String month = date.substring(4, 6);
            fields[1] = toInt(month) - 1;
            String day = date.substring(6, 8);
            fields[2] = toInt(day);
            return fields;
        }
        return null;
    }

    /**
     * 返回 日期格式 yyyyMMdd
     *
     * @param c
     * @return
     */
    public static String getDateString(Calendar c) {
        String s = "";
        if (c != null) {
            s = DateUtil.getCalendarStrBySimpleDateFormat(c, DateUtil.SIMPLEFORMATTYPE6);
        }
        return s;
    }

    /**
     * 将8位的日期字串与4位的时间字串组成14位长的字串
     *
     * @param dateStr
     * @param timeStr
     * @return
     */
    public static String formatDateTimeString(String dateStr, String timeStr) {
        if (dateStr == null || timeStr == null)
            return "";
        if (dateStr.length() != 8 || timeStr.length() != 4)
            return "";
        String preStr = dateStr + timeStr + "00";
        return formatDateTimeString(preStr);
    }

    /**
     * 将年，月，日 ，三个值拼成 YYYY-MM-DD
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String dateToString(int year, int month, int day) {
        if (year < 0 || month < 0 || day < 0)
            return "";
        else
            return year + "-" + month + "-" + day;
    }

    /**
     * 将字串中的两个:替换成.
     *
     * @param d
     * @return
     */
    public static String parseTude(String d) {
        String r = "";
        if (d != null && !"".equals(d)) {
            int i = d.indexOf(":");
            r += d.substring(0, i) + ".";
            String ss = d.substring(i + 1, d.length());
            int si = ss.indexOf(":");
            r += ss.substring(0, si) + ".";
            r += ss.substring(si + 1);
        }
        return r;
    }

    /**
     * 12-03修改：大舱位(ClassGrade);0:Y(经济舱);2:C(公务舱);3:F(头等舱)
     */
    public static String getFlightClassName(String code) {
        if ("Y".equalsIgnoreCase(code)) {
            return "经济舱";
        } else if ("F".equalsIgnoreCase(code)) {
            return "头等舱";
        } else if ("A".equalsIgnoreCase(code)) {
            return "不限";
        } else if ("C".equalsIgnoreCase(code)) {
            return "公务舱";
        }
        return "";
    }

    /**
     * 获取航班类型 C/F合并
     */
    public static String getFlightClassName2(String code) {
        if ("Y".equalsIgnoreCase(code)) {
            return "经济舱";
        } else if ("F".equalsIgnoreCase(code)) {
            return "公务舱/头等舱";
        } else if ("A".equalsIgnoreCase(code)) {
            return "不限";
        } else if ("C".equalsIgnoreCase(code)) {
            return "公务舱/头等舱";
        }
        return "";
    }

    /**
     * 12-17修改：（商务舱改为 公务舱） 大舱位(ClassGrade);0:Y(经济舱);2:C(公务舱);3:F(头等舱)
     */
    public static String getFlightClassNameByFlag(int flag) {
        String subClassName = "";
        if (flag == 0) {
            subClassName = "经济舱";

        } else if (flag == 2) {
            subClassName = "公务舱";
            // subClassName = "商务舱";
        } else if (flag == 3) {
            subClassName = "头等舱";
        }
        return subClassName;
    }

    /**
     * 12-02新增(ClassForDisplay)：舱位标签（显示用），1: 高端; 2: 超值; 3: 豪华;
     */
    public static String getFlightSubClassLabelByFlag(int flag) {
        String subClassName = "";
        switch (flag) {
            case 1:
                subClassName = "高端";
                break;
            case 2:
                subClassName = "超值";

                break;
            case 3:

                subClassName = "豪华";
                break;
            default:
                break;
        }
        return subClassName;
    }

    /** 大舱位中文（显示用--ClassForDisplay），1: 高端经济舱; 2: 超值头等舱; 3: 超级经济舱; 4:商务舱; 5:头等舱; 6:经济舱 */
    /**
     * 11-18:修改：大舱位中文（显示用），1: 高端经济舱; 2: 超值头等舱; 3: 豪华; 4:公务舱; 5:头等舱; 6:经济舱
     */
    public static String getFlightSubClassNameByFlag(int flag) {
        String subClassName = "";
        switch (flag) {
            case 1:
                subClassName = "高端经济舱";
                break;
            case 2:
                subClassName = "超值头等舱";

                break;
            case 3:

                subClassName = "豪华经济舱";
                break;
            case 4:
                subClassName = "公务舱";
                // subClassName = "商务舱";

                break;
            case 5:
                subClassName = "头等舱";

                break;
            case 6:
                subClassName = "经济舱";

                break;

            default:
                break;
        }

        return subClassName;
    }

    /**
     * 在srcStr的position位置，插入insertStr
     *
     * @param srcStr
     * @param insertStr
     * @param position
     * @return
     * @author liuwj 2012-6-5
     */
    public static String insertSymbolInStrPotion(String srcStr, String insertStr, int position) {
        if (TextUtils.isEmpty(srcStr)) {
            return "";
        }
        String str = "";
        if (position > srcStr.length()) {
            return str;
        }
        StringBuffer showStringBuffer = new StringBuffer(srcStr.length() + insertStr.length());
        String tmep = srcStr.substring(position, srcStr.length());
        showStringBuffer.append(srcStr.substring(0, position));
        showStringBuffer.append(insertStr);
        showStringBuffer.append(tmep);
        str = showStringBuffer.toString();
        return str;
    }

    /**
     * 校验yyyyMMdd日期是否合法
     *
     * @param date
     * @return 八位的年月日
     * @author liuwj 2012-7-19下午3:21:59
     */
    public static boolean isDateRight(String date) {
        if (date.length() == 8) {
            int year = -1;
            int month = -1;
            int day = -1;
            boolean isLeapYear = false;// 闰年
            year = toInt(date.substring(0, 4));
            month = toInt(date.substring(4, 6));
            day = toInt(date.substring(6, 8));
            if (year / 4 == 0 && year / 100 != 0) {
                isLeapYear = true;// 闰年
            }
            if (year / 400 == 0) {
                isLeapYear = true;// 闰年
            }
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    if (day <= 31 && day >= 1) {
                        return true;
                    }
                    break;
                case 2:
                    if (isLeapYear) {
                        if (day <= 29 && day >= 1) {
                            return true;
                        }
                    } else {
                        if (day <= 28 && day >= 1) {
                            return true;
                        }
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    if (day <= 30 && day >= 1) {
                        return true;
                    }
                    break;
                default:
                    return false;
            }
            return false;
        }
        return false;
    }

    /**
     * 计算yyyyMMdd与yyyyMMdd之间差多少天
     *
     * @param dateStr1
     * @param dateStr2
     * @return
     * @throws ParseException
     * @author liuwj 2012-5-31
     */
    public static int calcTwoDate(String dateStr1, String dateStr2) {
        // SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long diff;

        // 获得两个时间的毫秒时间差异
        try {
            // diff = sd.parse(dateStr2).getTime() - sd.parse(dateStr1).getTime();
            diff = DateUtil.compareDateStringByLevel(dateStr2, dateStr1, DateUtil.COMPARELEVEL_SECOND);
            long day = diff / nd;// 计算差多少天
            return (int) (day);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 计算yyyyMMddHHmm与yyyyMMddHHmm之间差多少分钟
     *
     * @param dateStr1
     * @param dateStr2
     * @return
     * @throws ParseException
     * @author liuwj 2012-5-31
     */
    public static int calcTwoDateTime(String dateStr1, String dateStr2) {
        // SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault());
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long diff;

        // 获得两个时间的毫秒时间差异
        try {
            // diff = sd.parse(dateStr2).getTime() - sd.parse(dateStr1).getTime();
            diff = DateUtil.compareDateStringByLevel(dateStr1, dateStr2, DateUtil.COMPARELEVEL_SECOND);
            long day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            return Math.abs((int) (min + hour * 60 + day * 1440));
        } catch (Exception e) {

            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 计算两个String类型的时间之和
     *
     * @param time1
     * @param time2
     * @return
     * @author liuwj 2012-7-17下午6:09:44
     */
    public static int plusGlobalTwoTime(String time1, String time2) {
        int timeA = 0, timeB = 0;
        if (!TextUtils.isEmpty(time1)) {
            timeA = toInt(time1);
        }
        if (!TextUtils.isEmpty(time2)) {
            timeB = toInt(time2);
        }
        return timeA + timeB;
    }


    /**
     * 公里转化米 支持小数
     *
     * @return
     */
    public static String kilometreToMetre(String kilometre) {
        String retStr = "";
        if (TextUtils.isEmpty(kilometre)) {
            return retStr;
        }
        float floatData = StringUtil.toFloat(kilometre) * 1000;
        if (floatData == -1000f) {
            return retStr;
        }
        retStr = StringUtil.toIntString(floatData + "");
        return retStr;
    }

    /**
     * 米 转化 公里 支持小数
     *
     * @return
     */
    public static String metreToKilometre(String metre) {
        String retStr = "";
        if (TextUtils.isEmpty(metre)) {
            return retStr;
        }
        if (StringUtil.toInt(metre) == -1) {
            return retStr;
        }
        float floatData = StringUtil.toInt(metre) / (1000f);
        retStr = floatData + "";
        return retStr;
    }

    /**
     * 截取字符串前几位，如果不满的话，直接return原字符串
     *
     * @param content
     * @param length
     * @return
     * @author liuwj 2012-11-9下午3:35:54
     */
    public static String subString(String content, int length) {
        if (content.length() < length) {
            return content;
        } else {
            return content.substring(0, length);
        }
    }

    /**
     * 以GBK编码格式计算字符串长度, 半角字符=1个长度, 全角字符=2个长度
     *
     * @param text 字符串
     * @return 字符串长度
     */
    public static int getSBCCaseLength(String text) {
        if (text == null || text.length() == 0) {
            return 0;
        }
        try {
            return text.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 功能描述:判断字符串是否含中文
     * <pre>
     *     youj:   2013-5-8      新建
     * </pre>
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {

        if (TextUtils.isEmpty(str)) {
            return false;
        }

        char[] charArr = str.toCharArray();

        for (char c : charArr) {

            if (isContainChinese(c)) {
                return true;
            }

        }

        return false;
    }

    private static boolean isContainChinese(char c) {

        Character.UnicodeBlock cu = Character.UnicodeBlock.of(c);

        return cu == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || cu == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || cu == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || cu == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || cu == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || cu == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;

    }

    /**
     * 功能描述:传入秒数，如果超过一小时返回 hh:mm:ss否则 返回mm:ss格式字串
     * <pre>
     *     xinyh:   2013-5-10      新建
     * </pre>
     *
     * @param time
     * @return
     */
    public static String getTalkTime(int time) {
        int hour = time / 3600;
        int minu = (time % 3600) / 60;
        int sec = time % 60;
        String str = "";
        if (hour == 0) {
            str = String.format("%02d:%02d", minu, sec);
        } else {
            str = String.format("%02d:%02d:%02d", hour, minu, sec);
        }
        return str;
    }

    /**
     * 功能描述,按num位数截取字符串并以endString结尾.如果不够num位的,直接返回.
     * 默认endString为"..."
     * <pre>
     *     xubin:   2013-6-14      新建
     * </pre>
     *
     * @return 转换后的字符串
     */
    public static String cutStringByNum(String content, int num, String endString) {
        String end = endString;
        if (end == null) {
            end = "...";
        }
        String str = "";
        if (content.length() > num) {
            str = content.substring(0, num) + end;
        } else {
            str = content;
        }
        return str;
    }

    public static String convertDispatchFee(int value) {

        if (value == 0) {

            return "0";

        }

        if (value % 100 == 0) {

            return String.valueOf(value / 100);

        } else {

            return Float.toString((value / 100.0f));

        }

    }

    public static int convertStringToIntOnlyForFocusFlight(String string) {
        byte[] b = string.getBytes();
        int value = 0;
        for (int i = 0; i < b.length; i++) {
            int n = (b[i] < 0 ? (int) b[i] + 256 : (int) b[i]) << (8 * i);
            value += n;
        }
        return Math.abs(value);
    }


    /**
     * @param hotelStar 需要显示的星级/评级数组
     * @param level     等级
     * @return 显示的星级
     */
    public static String getShowStar(String[] hotelStar, int level) {
        if (hotelStar == null || hotelStar.length < level) {
            return "";
        }
        String str = "";
        if (level > 0 && level < 6) {
            str = hotelStar[level - 1];
        }
        return str;
    }

    /**
     * 小数字符串（"0.05"）转换为百分比字符串（"5%"）
     * <p/>
     * jbai:   2013-8-14      新建
     *
     * @param decimal
     * @return
     */
    public static String decimalToPercent(String decimal) {
        float value = -1;
        if (!TextUtils.isEmpty(decimal)) {
            try {
                value = Float.parseFloat(decimal);
            } catch (NumberFormatException e) {
            }
        }
        if (value == -1) {
            return "";
        } else {
            DecimalFormat format = new DecimalFormat("0.#%");
            return format.format(value);
        }
    }

    /**
     * 传入以分为单位的价格数字,转换成元以后,若有小数,去掉小数部分,整数部分+1.负数返回0
     * 例如110返回1.1元
     *
     * @param price 以分为单位的价格数字
     * @return
     */
    public static String getCeilPriceString(int price) {
        String priceText = "";
        if (price <= 0) {
            return "0";
        }
        if (price % 100 != 0) {
            priceText = String.valueOf(price / 100 + 1);
        } else {
            priceText = String.valueOf(price / 100);
        }
        return priceText;
    }


    /**
     * 保留一位小数，第二位小数四舍五入.
     * 如果value为非数字，返回0.0
     *
     * @param value
     * @return
     */
    public static String toOneDecimal(String value) {
        Double huashi = 0d;
        try {
            huashi = Double.parseDouble(value);
        } catch (Exception e) {
        }

        DecimalFormat format = new DecimalFormat("#0.0");
        String buf = format.format(huashi).toString();
        return buf;
    }

    /**
     * DateTime是值类型， 故目前无法给出一个空的数据. 约定如下：
     * DateTime 0001-01-01 00:00:00表示为空日期;00010101000000
     * Date 0001-01-01表示为空日期;00010101
     * <p/>
     * create by DingJunYing for v5.0
     * 约定来自高文厂
     *
     * @param dateTime
     * @return 日期为空返回true
     */
    public static boolean isDateTimeEmpty(String dateTime) {
        final String emptyDate = "00010101";// 8位空日期
        final String emptyDateTime = "00010101000000";// 14位空日期
        if (TextUtils.isEmpty(dateTime)) {
            return true;
        }
        // 14位时间
        if (dateTime.equals(emptyDateTime)) {
            return true;
        }
        // 8位日期
        return dateTime.equals(emptyDate);
    }

    /**
     * 根据指定的长度,自动加上换行符
     *
     * @param s
     * @param num 多少字符换行
     * @return
     */
    public static String autoLineFeed(String s, int num) {
        StringBuilder sb = new StringBuilder();
        float size = 0f;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (size >= num) {
                sb.append("\n");
                size = 0f;
            }
            Character.UnicodeBlock cUB = Character.UnicodeBlock.of(c);
            if (cUB != Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
                sb.append(c);
                size += 0.5;
            } else {
                sb.append(c);
                size += 1;
            }
        }
        return sb.toString();
    }

    /**
     * 格式化距离:大于1000米，转为：X.0千米，小于1000米，转为：x米
     */
    public static String formatDistance(double distance) {
        if (distance == 0f) {
            return "0m";
        }

        if (distance >= 1000d) {
            DecimalFormat deFor = new DecimalFormat("#.0");
            distance = distance / 1000d;
            return deFor.format(distance) + "km";
        }

        DecimalFormat deFor = new DecimalFormat("#");
        return deFor.format(distance) + "m";
    }

    /**
     * 对比两个版本号是否相当 版本号必须以.作为分隔
     *
     * @param source
     * @param compare
     * @return
     */
    public static boolean isBigThan(String source, String compare) {
        boolean result = false;
        boolean hasCompare = false;
        if (TextUtils.isEmpty(source) || TextUtils.isEmpty(compare)) return result;
        String[] sourceS = source.split("[.]");
        String[] compareS = compare.split("[.]");
        int sCount = sourceS.length;
        int cCount = compareS.length;
        int minCount = Math.min(sCount, cCount);
        if (minCount < 1) return result;

        for (int i = 0; i < minCount; i++) {
            String sStr = sourceS[i];
            String cStr = compareS[i];
            if (TextUtils.isEmpty(sStr)) sStr = "0";
            if (TextUtils.isEmpty(cStr)) cStr = "0";
            int s = Integer.parseInt(sStr);
            int c = Integer.parseInt(cStr);
            if (s != c) {
                result = s > c;
                hasCompare = true;
                break;
            }
        }
        if (!hasCompare) {
            result = sCount > cCount;
        }
        return result;
    }

    /**
     * 判断一个字符串是否含有数字
     *
     * @param content
     * @return
     */
    public static boolean hasDigit(String content) {

        boolean flag = false;

        Pattern p = Pattern.compile(".*\\d+.*");

        Matcher m = p.matcher(content);

        if (m.matches())

            flag = true;

        return flag;

    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }


    /**
     * 真实姓名中是否包含数字
     *
     * @param name
     * @return
     */
    public static boolean checkNameEx(String name) {
        //String regEx = " [a-zA-Z0-9]{10}";
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(name);
        String num = m.replaceAll("").trim();
        return "".equals(num);
    }
}
