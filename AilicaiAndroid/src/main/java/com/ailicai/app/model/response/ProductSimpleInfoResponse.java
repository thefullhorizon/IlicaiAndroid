package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by Administrator on 2016/5/25.
 */
public class ProductSimpleInfoResponse extends Response {

    private String productId; //产品编号
    private String productName = "";//产品标题
    private String bidAmount; // 投资金额
    private String hasBuyPrecentStr;//融资进度
    private String bidTimeStr;//申购时间
    private String horizonStr; // 投资期限，单位是天
    private String yearInterestRateStr; // 预计年化利率字符串
    private String profitStr = "";//收益金额
    private String protocolUrl = ""; //协议地址（居间、借款、转让）

    private String backTime;//（预计）回款时间
    private String backAmount;//（预计）回款金额
    private int isPaid; //是否赔付 0-正常回款 1-风险保障金赔付
    private String paidMemo;//赔付说明

    private String yearInterestRateAddStr=""; // 加息券加息字符串
    private String profitAddStr = "";//加息收益金额
    private String paidAmount="";//赔付金额
    private String addRateStr="";//加息说明 eg.享前30天加息1.5%
    private String productDetailUrl="";//房产宝详情页h5url

    private String bidOrderNo; //交易单号
    private int isTransfer;//是否是转让房产宝 1 是 0否
    private String hasTransferAmount = ""; //已转让金额（包含文案）
    private String transferingAmount = ""; //转让中的金额（包含文案）
    private int canBeTransfer; //是否可被转让，0-不可 1-可转让 改为调用接口判断
    private String transferAmount ="";//转让价格 包含单位元
    private String transferRecordsUrl=""; //转让记录url


    private int type; //1-申购;2-持有;3-到期
    private String subDateMMDD="";//申购日
    private String interestDateMMDD="";//起息日
    private String backDateMMDD="";//预计到期日
    private String beTransferMMdd="";//受让日 转让房产宝有

    private int totalDay; // 投资期限
    private int passDay;//距申购日天数

    private int fullDay = 1;//申购到满标天数
    private int interestTotal =1;//申购到起息日总天数

    private String matchStatus ;//小钱申购进度状态 “Y”匹配完成 “N”匹配未开始 “P”匹配进行中
    private int  pennyNumber; // 小钱数量

    private String subjectNo   = ""; //产品编号

    //6.9新增
    private String status; // 状态：15待收益发放
    private String notTransferTitle=""; // 无法转让标题
    private String notTransferReason="";//无法转让原因


    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public int getPennyNumber() {
        return pennyNumber;
    }

    public void setPennyNumber(int pennyNumber) {
        this.pennyNumber = pennyNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(String bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getHasBuyPrecentStr() {
        return hasBuyPrecentStr;
    }

    public void setHasBuyPrecentStr(String hasBuyPrecentStr) {
        this.hasBuyPrecentStr = hasBuyPrecentStr;
    }

    public String getBidTimeStr() {
        return bidTimeStr;
    }

    public void setBidTimeStr(String bidTimeStr) {
        this.bidTimeStr = bidTimeStr;
    }

    public String getHorizonStr() {
        return horizonStr;
    }

    public void setHorizonStr(String horizonStr) {
        this.horizonStr = horizonStr;
    }

    public String getYearInterestRateStr() {
        return yearInterestRateStr;
    }

    public void setYearInterestRateStr(String yearInterestRateStr) {
        this.yearInterestRateStr = yearInterestRateStr;
    }

    public String getProfitStr() {
        return profitStr;
    }

    public void setProfitStr(String profitStr) {
        this.profitStr = profitStr;
    }

    public String getProtocolUrl() {
        return protocolUrl;
    }

    public void setProtocolUrl(String protocolUrl) {
        this.protocolUrl = protocolUrl;
    }

    public String getBackTime() {
        return backTime;
    }

    public void setBackTime(String backTime) {
        this.backTime = backTime;
    }

    public String getBackAmount() {
        return backAmount;
    }

    public void setBackAmount(String backAmount) {
        this.backAmount = backAmount;
    }

    public int getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(int isPaid) {
        this.isPaid = isPaid;
    }

    public String getPaidMemo() {
        return paidMemo;
    }

    public void setPaidMemo(String paidMemo) {
        this.paidMemo = paidMemo;
    }

    public String getYearInterestRateAddStr() {
        return yearInterestRateAddStr;
    }

    public void setYearInterestRateAddStr(String yearInterestRateAddStr) {
        this.yearInterestRateAddStr = yearInterestRateAddStr;
    }

    public String getProfitAddStr() {
        return profitAddStr;
    }

    public void setProfitAddStr(String profitAddStr) {
        this.profitAddStr = profitAddStr;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAddRateStr() {
        return addRateStr;
    }

    public void setAddRateStr(String addRateStr) {
        this.addRateStr = addRateStr;
    }

    public String getProductDetailUrl() {
        return productDetailUrl;
    }

    public void setProductDetailUrl(String productDetailUrl) {
        this.productDetailUrl = productDetailUrl;
    }

    public String getBidOrderNo() {
        return bidOrderNo;
    }

    public void setBidOrderNo(String bidOrderNo) {
        this.bidOrderNo = bidOrderNo;
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

    public int getCanBeTransfer() {
        return canBeTransfer;
    }

    public void setCanBeTransfer(int canBeTransfer) {
        this.canBeTransfer = canBeTransfer;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferRecordsUrl() {
        return transferRecordsUrl;
    }

    public void setTransferRecordsUrl(String transferRecordsUrl) {
        this.transferRecordsUrl = transferRecordsUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSubDateMMDD() {
        return subDateMMDD;
    }

    public void setSubDateMMDD(String subDateMMDD) {
        this.subDateMMDD = subDateMMDD;
    }

    public String getInterestDateMMDD() {
        return interestDateMMDD;
    }

    public void setInterestDateMMDD(String interestDateMMDD) {
        this.interestDateMMDD = interestDateMMDD;
    }

    public String getBackDateMMDD() {
        return backDateMMDD;
    }

    public void setBackDateMMDD(String backDateMMDD) {
        this.backDateMMDD = backDateMMDD;
    }

    public String getBeTransferMMdd() {
        return beTransferMMdd;
    }

    public void setBeTransferMMdd(String beTransferMMdd) {
        this.beTransferMMdd = beTransferMMdd;
    }

    public int getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(int totalDay) {
        this.totalDay = totalDay;
    }

    public int getPassDay() {
        return passDay;
    }

    public void setPassDay(int passDay) {
        this.passDay = passDay;
    }

    public int getFullDay() {
        return fullDay;
    }

    public void setFullDay(int fullDay) {
        this.fullDay = fullDay;
    }

    public int getInterestTotal() {
        return interestTotal;
    }

    public void setInterestTotal(int interestTotal) {
        this.interestTotal = interestTotal;
    }

    public String getSubjectNo() {
        return subjectNo;
    }

    public void setSubjectNo(String subjectNo) {
        this.subjectNo = subjectNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotTransferTitle() {
        return notTransferTitle;
    }

    public void setNotTransferTitle(String notTransferTitle) {
        this.notTransferTitle = notTransferTitle;
    }

    public String getNotTransferReason() {
        return notTransferReason;
    }

    public void setNotTransferReason(String notTransferReason) {
        this.notTransferReason = notTransferReason;
    }
}
