package com.ailicai.app.model.response;

import com.ailicai.app.R;
import com.huoqiu.framework.rest.Response;

/**
 * Created by liyanan on 16/9/7.
 */
public class HousePartResponse extends Response {
    private String serviceTel; // 客服电话
    private int orderCnt; // 账单数（合同订单）
    private int newOrderCnt; // 新账单数（待付款账单）
    private int complaintNum; // 投诉件数
    private int backCardNum; // 银行卡数量
    private int seekhouseNum;// 约看清单数量
    private int hasAgent;//是否有看房顾问
    private long orderId; // 唯一订单ID
    private int orderType;//唯一订单业务类型 1：出租，2：房管房，3：二手房
    private int advanceStatus; //唯一订单交易进度 0-未签居间 1-完成签居间 2-完成网签 3-完成过户准备 4-已过户
    private int appointNum; //待评价数量
    private int voucherNum;//卡券数量
    private String commissionTips = "";//我的委托文案提示
    private int hasCommission;//是否有出租、出售历史委托成功的房源(>0代表业主认证)

    public String getServiceTel() {
        return serviceTel;
    }

    public void setServiceTel(String serviceTel) {
        this.serviceTel = serviceTel;
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

    public int getBackCardNum() {
        return backCardNum;
    }

    public void setBackCardNum(int backCardNum) {
        this.backCardNum = backCardNum;
    }

    public int getSeekhouseNum() {
        return seekhouseNum;
    }

    public String getSeekhouseNumStr() {
        return String.valueOf(seekhouseNum).length() == 3 ? "99+" : String.valueOf(seekhouseNum);
    }

    public int getSeekhouseNumBg() {
        int totalBg = R.drawable.msg_new_ones;
        if (String.valueOf(seekhouseNum).length() == 3) {
            totalBg = R.drawable.msg_new_tens;
        }
        return totalBg;
    }

    public void setSeekhouseNum(int seekhouseNum) {
        this.seekhouseNum = seekhouseNum;
    }

    public int getHasAgent() {
        return hasAgent;
    }

    public void setHasAgent(int hasAgent) {
        this.hasAgent = hasAgent;
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

    public int getAdvanceStatus() {
        return advanceStatus;
    }

    public void setAdvanceStatus(int advanceStatus) {
        this.advanceStatus = advanceStatus;
    }

    public int getAppointNum() {
        return appointNum;
    }

    public void setAppointNum(int appointNum) {
        this.appointNum = appointNum;
    }

    public int getVoucherNum() {
        return voucherNum;
    }

    public void setVoucherNum(int voucherNum) {
        this.voucherNum = voucherNum;
    }

    public String getCommissionTips() {
        return commissionTips;
    }

    public void setCommissionTips(String commissionTips) {
        this.commissionTips = commissionTips;
    }

    public int getHasCommission() {
        return hasCommission;
    }

    public void setHasCommission(int hasCommission) {
        this.hasCommission = hasCommission;
    }
}
