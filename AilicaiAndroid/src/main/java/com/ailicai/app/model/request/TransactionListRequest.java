package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by wulianghuan on 2016/1/6.
 */
@RequestPath("/ailicai/tradeList.rest")
public class TransactionListRequest extends Request {

    private long userId; //用户Id,通过请求Header里的uticket获取
    private int tradeType; // 交易类型 0：全部，1：转入，2：转出，3：购买，4：回款, 5:支付, 6: 转让
    private String startDate; // 开始日期 yyyy-MM-dd
    private String endDate; // 结束日期 yyyy-MM-dd
    private int quickType; // 快捷类型 0：普通查询 1：近一周查询 2：近一月查询 3：近三月查询 4：近半年 5：近一年
    private int pageSize = 20; // 每次加载20条数据
    private int offSet = 0; // 数据偏移量

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getQuickType() {
        return quickType;
    }

    public void setQuickType(int quickType) {
        this.quickType = quickType;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffSet() {
        return offSet;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }
}

