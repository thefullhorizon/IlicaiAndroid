package com.ailicai.app.model.response;

/**
 * Created by David on 15/9/14.
 */
public class Order {
    private long orderId;//订单ID
    private String orderCode; //订单编号（二手房）
    private String zoneName = "";//小区名称
    private String building = "";
    // 楼栋号
    private String room = ""; // 室号
    private String contractPeriodFrom = ""; // 合约周期开始  格式 YYYY-MM-DD
    private String contractPeriodTo = ""; // 合约周期结束  格式 YYYY-MM-DD
    private int contractState; // 合同状态 1：待支付合同 2：有效合同 3：无效合同
    private double monthlyRent;// 每月租金
    private String payPeriod = ""; // 付款周期 包括月付、季付、半年付、年付四个选项
    /**
     * 业务类型 1：出租，2：房管房，3：二手房
     */
    private int type;
    private int billCnt; // 账单数
    private double billAmount; // 账单金额

    private int advanceStatus; //交易进度 0-未签居间 1-完成签居间 2-完成网签 3-完成过户准备 4-已过户

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getContractPeriodFrom() {
        return contractPeriodFrom;
    }

    public void setContractPeriodFrom(String contractPeriodFrom) {
        this.contractPeriodFrom = contractPeriodFrom;
    }

    public String getContractPeriodTo() {
        return contractPeriodTo;
    }

    public void setContractPeriodTo(String contractPeriodTo) {
        this.contractPeriodTo = contractPeriodTo;
    }

    public int getContractState() {
        return contractState;
    }

    public void setContractState(int contractState) {
        this.contractState = contractState;
    }

    public double getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(double monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public String getPayPeriod() {
        return payPeriod;
    }

    public void setPayPeriod(String payPeriod) {
        this.payPeriod = payPeriod;
    }

    /**
     * @see #type
     */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBillCnt() {
        return billCnt;
    }

    public void setBillCnt(int billCnt) {
        this.billCnt = billCnt;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    public int getAdvanceStatus() {
        return advanceStatus;
    }

    public void setAdvanceStatus(int advanceStatus) {
        this.advanceStatus = advanceStatus;
    }
}
