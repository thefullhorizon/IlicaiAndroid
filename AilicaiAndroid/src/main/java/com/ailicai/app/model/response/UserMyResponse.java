package com.ailicai.app.model.response;

import android.text.TextUtils;

import com.huoqiu.framework.rest.Response;


/**
 * 进入“我的”页面时获取的数据Response模型
 *
 * @author hubin
 */
public class UserMyResponse extends Response {
    private int orderNum; //订单数量
    private int collectionNum; //收藏数量
    private String serviceTel = ""; //客服电话
    private String assigneeName = ""; //经纪人姓名
    private String assigneeTel = ""; //经纪人电话
    private String assigneePhotoUrl = ""; //经纪人照片URL
    private float score; //经纪人评分
    private int bizType; // 1 出租经纪人 ， 2出售经纪人
    private int needRefresh; //是否需要刷新购房须知页面 0 无需刷新 1 需要刷新
    private int hasBuyNotice; //是否有购房须知 0 无 1 有
    private String buyNoticeUrl; //购房须知路径
    private int hasAgent; //是否有经纪人 0:无  1：有

    private int orderCnt; // 账单数（合同订单）
    private int newOrderCnt; // 新账单数（待付款账单）

    private long orderId; // 唯一订单ID
    private int orderType;//唯一订单业务类型 1：出租，2：房管房，3：二手房

    private int collectionHouseNum; //关注房源数
    private int collectionEstateNum; //关注小区数

    private int complaintNum; // 投诉件数

    private int backCardNum; //银行卡数量

    private String currentDepositBalance = ""; // 钱包余额，余额没有则下发空串
    private String timeDepositBalance = ""; // 定期宝余额，余额没有则下发空串

    private int voucherNum;//卡券数量
    private int commissionNum;//委托数量
    private int seekhouseNum=0;//约看清单数量

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(int collectionNum) {
        this.collectionNum = collectionNum;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getAssigneeTel() {
        return assigneeTel;
    }

    public void setAssigneeTel(String assigneeTel) {
        this.assigneeTel = assigneeTel;
    }

    public String getAssigneePhotoUrl() {
        return assigneePhotoUrl;
    }

    public void setAssigneePhotoUrl(String assigneePhotoUrl) {
        this.assigneePhotoUrl = assigneePhotoUrl;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public int getHasAgent() {
        return hasAgent;
    }

    public void setHasAgent(int hasAgent) {
        this.hasAgent = hasAgent;
    }

    public int getOrderCnt() {
        return orderCnt;
    }

    public void setOrderCnt(int orderCnt) {
        this.orderCnt = orderCnt;
    }

    public int getNewOrderCnt() {
        return newOrderCnt;
    }

    public void setNewOrderCnt(int newOrderCnt) {
        this.newOrderCnt = newOrderCnt;
    }

    public int getComplaintNum() {
        return complaintNum;
    }

    public void setComplaintNum(int complaintNum) {
        this.complaintNum = complaintNum;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getBackCardNum() {
        return backCardNum;
    }

    public void setBackCardNum(int backCardNum) {
        this.backCardNum = backCardNum;
    }

    public String getCurrentDepositBalance() {
        return TextUtils.isEmpty(currentDepositBalance) ? "0.00" : currentDepositBalance;
    }

    public void setCurrentDepositBalance(String currentDepositBalance) {
        this.currentDepositBalance = currentDepositBalance;
    }

    public String getTimeDepositBalance() {
        return timeDepositBalance;
    }

    public void setTimeDepositBalance(String timeDepositBalance) {
        this.timeDepositBalance = timeDepositBalance;
    }

    public int getVoucherNum() {
        return voucherNum;
    }

    public void setVoucherNum(int voucherNum) {
        this.voucherNum = voucherNum;
    }

    public int getCommissionNum() {
        return commissionNum;
    }

    public void setCommissionNum(int commissionNum) {
        this.commissionNum = commissionNum;
    }

    public int getSeekhouseNum() {
        return seekhouseNum;
    }

    public void setSeekhouseNum(int seekhouseNum) {
        this.seekhouseNum = seekhouseNum;
    }
}
