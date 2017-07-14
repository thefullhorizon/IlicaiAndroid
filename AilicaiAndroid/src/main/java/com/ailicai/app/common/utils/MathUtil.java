package com.ailicai.app.common.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by liyanan on 16/3/21.
 */
public class MathUtil {
    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    public static String subZeroAndDot(double number) {
        String s = String.valueOf(number);
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 保存小数点后两位,且不四舍五入
     *
     * @param count
     * @return
     */
    public static String saveTwoDecimal(double count) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingSize(0);
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        double doubleCount = Double.parseDouble(decimalFormat.format(count));
        DecimalFormat format = new DecimalFormat("##0.00");
        return format.format(doubleCount);
    }

    /**
     * 保存小数点后两位,四舍五入,且固定保留两位小数，例5显示为5.00
     *
     * @param count
     * @return
     */
    public static String saveTwoDecimalHalfUp(double count) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setGroupingSize(0);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        double doubleCount = Double.parseDouble(decimalFormat.format(count));
        DecimalFormat format = new DecimalFormat("##0.00");
        return format.format(doubleCount);
    }

    /**
     * double计算保留两位精度
     *
     * @param count
     * @return
     */
    public static double doubleFormatTwoDecimal(double count) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setGroupingSize(0);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(decimalFormat.format(count));
    }
}
