package com.ailicai.app.model.bean;

/**
 * 投资记录
 * Created by liyanan on 16/4/8.
 */
public class ProductInvestRecord {
    private String moblie; // 手机号
    private String amtStr; // 投资金额
    private String timeStr; // 投资时间

    public String getMoblie() {
        return moblie;
    }

    public void setMoblie(String moblie) {
        this.moblie = moblie;
    }

    public String getAmtStr() {
        return amtStr;
    }

    public void setAmtStr(String amtStr) {
        this.amtStr = amtStr;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }
}
