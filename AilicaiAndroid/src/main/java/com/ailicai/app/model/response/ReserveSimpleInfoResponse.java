package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by Administrator on 2016/5/25.
 */
public class ReserveSimpleInfoResponse extends Response {

    private String productName = "";//产品标题
    private String reserveTime; //预约时间
    private String reserveAmt = "";// 预约金额
    private String preBuyTime = "";// 预计申购时间
    private String horizon="";// 预约期限
    private String yearInterestRateStr; // 预计年化利率字符串

    private int canbecancel; //是否可取消 1-可取消
    private String bidOrderNo; // 预约订单编号

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }

    public String getReserveAmt() {
        return reserveAmt;
    }

    public void setReserveAmt(String reserveAmt) {
        this.reserveAmt = reserveAmt;
    }

    public String getPreBuyTime() {
        return preBuyTime;
    }

    public void setPreBuyTime(String preBuyTime) {
        this.preBuyTime = preBuyTime;
    }

    public String getHorizon() {
        return horizon;
    }

    public void setHorizon(String horizon) {
        this.horizon = horizon;
    }

    public String getYearInterestRateStr() {
        return yearInterestRateStr;
    }

    public void setYearInterestRateStr(String yearInterestRateStr) {
        this.yearInterestRateStr = yearInterestRateStr;
    }

    public int getCanbecancel() {
        return canbecancel;
    }

    public boolean isCancel() {
        return canbecancel == 1;
    }

    public void setCanbecancel(int canbecancel) {
        this.canbecancel = canbecancel;
    }

    public String getBidOrderNo() {
        return bidOrderNo;
    }

    public void setBidOrderNo(String bidOrderNo) {
        this.bidOrderNo = bidOrderNo;
    }
}
