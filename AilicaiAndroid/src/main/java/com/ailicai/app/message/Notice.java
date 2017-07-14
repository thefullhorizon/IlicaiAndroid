package com.ailicai.app.message;

import com.huoqiu.framework.rest.Response;

/**
 * Created by duo.chen on 2015/8/10.
 */
public class Notice extends Response {

    private long id; //通知Id
    private String title; //通知标题
    private String content; //通知内容
    private String url; //通知详情的地址
    private String createDate; //创建时间
    private String picUrl; //图片地址
    private int remindType; //提醒类型
    private long appointmentId; //约会ID
    private int status; // 约会状态
    private int bizType; // 经纪人业务类型 1-租房经纪人 2-二手房经纪人
    private String appointmentTime; // 约会时间
    private String appointmentPlace; // 约会地点
    private long houseId; // 房源Id
    private int hasHouseId; // 是否有真实的houseID 0-虚拟houseId，1-真实houseId（用于从没有通过审核的委托房源的区分）
    private long contractId; //合同ID
    private long billId;//账单ID
    private int readStatus; //阅读状态  1:已读  0:未读
    private long orderId;//二手房账单Id
    private int rentOrSale; //租房二手房
    private long agentId;//经纪人id
    private String appraiseTitle;//电话微聊的时间

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getRemindType() {
        return remindType;
    }

    public void setRemindType(int remindType) {
        this.remindType = remindType;
    }

    public long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentPlace() {
        return appointmentPlace;
    }

    public void setAppointmentPlace(String appointmentPlace) {
        this.appointmentPlace = appointmentPlace;
    }

    public long getHouseId() {
        return houseId;
    }

    public void setHouseId(long houseId) {
        this.houseId = houseId;
    }

    public int getHasHouseId() {
        return hasHouseId;
    }

    public void setHasHouseId(int hasHouseId) {
        this.hasHouseId = hasHouseId;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public long getBillId() {
        return billId;
    }

    public void setBillId(long billId) {
        this.billId = billId;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getRentOrSale() {
        return rentOrSale;
    }

    public void setRentOrSale(int rentOrSale) {
        this.rentOrSale = rentOrSale;
    }

    public long getAgentId() {
        return agentId;
    }

    public void setAgentId(long agentId) {
        this.agentId = agentId;
    }

    public String getAppraiseTitle() {
        return appraiseTitle;
    }

    public void setAppraiseTitle(String appraiseTitle) {
        this.appraiseTitle = appraiseTitle;
    }
}
