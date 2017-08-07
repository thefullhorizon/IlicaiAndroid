package com.ailicai.app.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yeyong.zhang on 16/1/5.
 */
public class Product implements Serializable {
    private boolean isShow;//预约记录专用,记录是否显示
    private String productId = ""; // 产品Id
    private String productName = ""; // 产品名称
    private double yearInterestRate; // 预计年化利率 小数表示，比如0.15表示15%
    private String yearInterestRateStr; // 预计年化利率字符串 带文案
    private String yearInterestRateNoMemo;  // 预计年化利率字符串 不带文案
    private int horizon; // 投资期限，单位是天
    private String horizonStr; // 投资期限，单位是天
    private int status; // 理财状态 1-正在募集；2-即将开售；3-已售罄
    private double bidAmount; // 投资金额
    private String bidAmountStr; // 投资金额字符串
    private double applyAmount; // 标的金额
    private String applyAmountStr = ""; // 标的金额带单位
    private double biddableAmount; // 可投标金额
    private String biddableAmountStr; // 可投标金额字符串
    private double bidUnit; // 投标单位
    private String bidUnitStr; // 投标单位带单位
    private double minAmount; // 起购金额
    private String minAmountStr = ""; // 起购金额带单位
    private String validDateStr = ""; // 标的有效期限
    private long startBuyTime; //开始购买时间单位毫秒;开始预约时间（预约时）
    private double hasBuyPrecent;//融资进度，小数表示，比如0.15表示15%
    private String hasBuyPrecentStr;//融资进度，带百分比，eg.15%
    private int assetStatus; // 资产状态 1-已到期已回款；2-未到期；3-募集中
    private long dueTime; // 理财到期时间
    private int remainingDays; // 剩余天数
    private String subjectStatus; //标的状态 01:未投标，03:审批通过,04:审批失败,05:招标中,08:满标处理中,10:已满标,13:满标后流标,15:还款中,20:已完结,99:流标,50:已取消


    private String bidTimeStr;//标的申购时间
    private String bidOrderNo = "";//购买预约订单编号
    private int orderStatus; //预约状态  1-即将开始预约 2-预约中 3-额度已满  4-预约已过期 5-已取消 6-已完成
    private int canbecancel; //是否可取消 1-可取消
    private String orderDealAmount = ""; //实际预约金额
    private String orderTimeStr = ""; //预约时间
    private String preBuyTimeStr = ""; //预计购买时间
    private String profitStr = "";//收益金额
    private String backAmount = "";//（预计）回款金额
    private String interestDateStr = ""; //起息日期
    private String backDateStr = ""; //（预计）回款日期
    private String protocolUrl = ""; //居间协议地址
    private int isAddRate; //是否使用加息券 0-没使用，1-使用
    private String addRateInfo = ""; //加息券信息 eg.加息券：享前{N}天加息{加息值}

    private int           isCashBackVoucher;//是否是返金券：0：不是，1：是
    private String        cashBackVoucherCopywriter;   // 返金券文案

    private List<String> tags; //运营活动标签
    private int isTransfer;//是否是转让房产宝  1 是   0否
    private String oriProductId ="";//原房产宝编号
    private String transferAmount ="";//转让价格
    private int isOwnTransfer;//是否是自己的转让 1是 0 否

    private String hasTransferAmount = ""; //已转让金额
    private String transferingAmount = ""; //转让中金额
    private String transferRecordsUrl=""; //转让记录url
    private long couponId;//卡券id

    //V6.7新增
    private String type;// V6.7增加小钱袋混排，加字段区分产品类型  FIXED_REDEEM  房产宝 赎楼, FIXED_TAIL  房产宝 尾款 ,FIXED_DISPLACE  房产宝 置换, NEW_OTHER  房产宝 其他, CONSUME_PURSE 小钱袋

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getYearInterestRate() {
        return yearInterestRate;
    }

    public void setYearInterestRate(double yearInterestRate) {
        this.yearInterestRate = yearInterestRate;
    }

    public String getYearInterestRateStr() {
        return yearInterestRateStr;
    }

    public void setYearInterestRateStr(String yearInterestRateStr) {
        this.yearInterestRateStr = yearInterestRateStr;
    }
    public String getYearInterestRateNoMemo() {
        return yearInterestRateNoMemo;
    }

    public void setYearInterestRateNoMemo(String yearInterestRateNoMemo) {
        this.yearInterestRateNoMemo = yearInterestRateNoMemo;
    }

    public int getHorizon() {
        return horizon;
    }

    public void setHorizon(int horizon) {
        this.horizon = horizon;
    }

    public String getHorizonStr() {
        return horizonStr;
    }

    public void setHorizonStr(String horizonStr) {
        this.horizonStr = horizonStr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getBidAmountStr() {
        return bidAmountStr;
    }

    public void setBidAmountStr(String bidAmountStr) {
        this.bidAmountStr = bidAmountStr;
    }

    public double getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(double applyAmount) {
        this.applyAmount = applyAmount;
    }

    public String getApplyAmountStr() {
        return applyAmountStr;
    }

    public void setApplyAmountStr(String applyAmountStr) {
        this.applyAmountStr = applyAmountStr;
    }

    public double getBiddableAmount() {
        return biddableAmount;
    }

    public void setBiddableAmount(double biddableAmount) {
        this.biddableAmount = biddableAmount;
    }

    public String getBiddableAmountStr() {
        return biddableAmountStr;
    }

    public void setBiddableAmountStr(String biddableAmountStr) {
        this.biddableAmountStr = biddableAmountStr;
    }

    public double getBidUnit() {
        return bidUnit;
    }

    public void setBidUnit(double bidUnit) {
        this.bidUnit = bidUnit;
    }

    public String getBidUnitStr() {
        return bidUnitStr;
    }

    public void setBidUnitStr(String bidUnitStr) {
        this.bidUnitStr = bidUnitStr;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public String getMinAmountStr() {
        return minAmountStr;
    }

    public void setMinAmountStr(String minAmountStr) {
        this.minAmountStr = minAmountStr;
    }

    public String getValidDateStr() {
        return validDateStr;
    }

    public void setValidDateStr(String validDateStr) {
        this.validDateStr = validDateStr;
    }

    public long getStartBuyTime() {
        return startBuyTime;
    }

    public void setStartBuyTime(long startBuyTime) {
        this.startBuyTime = startBuyTime;
    }

    public double getHasBuyPrecent() {
        return hasBuyPrecent;
    }

    public void setHasBuyPrecent(double hasBuyPrecent) {
        this.hasBuyPrecent = hasBuyPrecent;
    }

    public String getHasBuyPrecentStr() {
        return hasBuyPrecentStr;
    }

    public void setHasBuyPrecentStr(String hasBuyPrecentStr) {
        this.hasBuyPrecentStr = hasBuyPrecentStr;
    }

    public int getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(int assetStatus) {
        this.assetStatus = assetStatus;
    }

    public long getDueTime() {
        return dueTime;
    }

    public void setDueTime(long dueTime) {
        this.dueTime = dueTime;
    }

    public int getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(int remainingDays) {
        this.remainingDays = remainingDays;
    }

    public String getSubjectStatus() {
        return subjectStatus;
    }

    public void setSubjectStatus(String subjectStatus) {
        this.subjectStatus = subjectStatus;
    }

    public String getBidOrderNo() {
        return bidOrderNo;
    }

    public void setBidOrderNo(String bidOrderNo) {
        this.bidOrderNo = bidOrderNo;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getCanbecancel() {
        return canbecancel;
    }

    public boolean isCanbecancel() {
        return canbecancel == 1;
    }

    public void setCanbecancel(int canbecancel) {
        this.canbecancel = canbecancel;
    }

    public String getOrderDealAmount() {
        return orderDealAmount;
    }

    public void setOrderDealAmount(String orderDealAmount) {
        this.orderDealAmount = orderDealAmount;
    }

    public String getOrderTimeStr() {
        return orderTimeStr;
    }

    public void setOrderTimeStr(String orderTimeStr) {
        this.orderTimeStr = orderTimeStr;
    }

    public String getPreBuyTimeStr() {
        return preBuyTimeStr;
    }

    public void setPreBuyTimeStr(String preBuyTimeStr) {
        this.preBuyTimeStr = preBuyTimeStr;
    }

    public String getProfitStr() {
        return profitStr;
    }

    public void setProfitStr(String profitStr) {
        this.profitStr = profitStr;
    }

    public String getBackAmount() {
        return backAmount;
    }

    public void setBackAmount(String backAmount) {
        this.backAmount = backAmount;
    }

    public String getInterestDateStr() {
        return interestDateStr;
    }

    public void setInterestDateStr(String interestDateStr) {
        this.interestDateStr = interestDateStr;
    }

    public String getBackDateStr() {
        return backDateStr;
    }

    public void setBackDateStr(String backDateStr) {
        this.backDateStr = backDateStr;
    }

    public String getProtocolUrl() {
        return protocolUrl;
    }

    public void setProtocolUrl(String protocolUrl) {
        this.protocolUrl = protocolUrl;
    }

    public String getBidTimeStr() {
        return bidTimeStr;
    }

    public void setBidTimeStr(String bidTimeStr) {
        this.bidTimeStr = bidTimeStr;
    }

    public int getIsAddRate() {
        return isAddRate;
    }

    public void setIsAddRate(int isAddRate) {
        this.isAddRate = isAddRate;
    }

    public String getAddRateInfo() {
        return addRateInfo;
    }

    public void setAddRateInfo(String addRateInfo) {
        this.addRateInfo = addRateInfo;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getIsOwnTransfer() {
        return isOwnTransfer;
    }

    public void setIsOwnTransfer(int isOwnTransfer) {
        this.isOwnTransfer = isOwnTransfer;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getOriProductId() {
        return oriProductId;
    }

    public void setOriProductId(String oriProductId) {
        this.oriProductId = oriProductId;
    }

    public int getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(int isTransfer) {
        this.isTransfer = isTransfer;
    }

    public String getHasTransferAmount() {
        return hasTransferAmount;
    }

    public void setHasTransferAmount(String hasTransferAmount) {
        this.hasTransferAmount = hasTransferAmount;
    }

    public String getTransferingAmount() {
        return transferingAmount;
    }

    public void setTransferingAmount(String transferingAmount) {
        this.transferingAmount = transferingAmount;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getTransferRecordsUrl() {
        return transferRecordsUrl;
    }

    public void setTransferRecordsUrl(String transferRecordsUrl) {
        this.transferRecordsUrl = transferRecordsUrl;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }
}
