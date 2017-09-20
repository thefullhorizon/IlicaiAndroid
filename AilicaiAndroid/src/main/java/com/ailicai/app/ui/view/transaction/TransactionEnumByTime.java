package com.ailicai.app.ui.view.transaction;

/**
 * Created by wulianghuan on 2016/1/7.
 */

public enum TransactionEnumByTime {

    // 交易时间类型 0：普通查询，1：近一周，:2：近一个月，3：近三个月，4：近半年，5：近一年
    LATEST_COMMON(0, "普通查询", "普通查询"),//有开始时间和结束时间的查询
    LATEST_A_WEEK(1, "近一周", "近一周"),
    LATEST_A_MONTH(2, "近一个月", "近一个月"),
    LATEST_THREE_MONTH(3, "近三个月", "近三个月"),
    LATEST_HALF_YEAR(4, "近半年", "近半年"),
    LATEST_A_YEAR(5, "近一年", "近一年");

    private int code;
    private String item;
    private String title;

    TransactionEnumByTime(int code, String item, String title) {
        this.code = code;
        this.item = item;
        this.title = title;
    }

    public int getCode() {
        return this.code;
    }

    public String getItem() {
        return item;
    }

    public String getTitle() {
        return title;
    }

    public static TransactionEnumByTime find(int code) {
        if (LATEST_A_WEEK.getCode() == code) return LATEST_A_WEEK;
        else if (LATEST_A_MONTH.getCode() == code) return LATEST_A_MONTH;
        else if (LATEST_THREE_MONTH.getCode() == code) return LATEST_THREE_MONTH;
        else if (LATEST_HALF_YEAR.getCode() == code) return LATEST_HALF_YEAR;
        else if (LATEST_A_YEAR.getCode() == code) return LATEST_A_YEAR;
        return null;
    }

}