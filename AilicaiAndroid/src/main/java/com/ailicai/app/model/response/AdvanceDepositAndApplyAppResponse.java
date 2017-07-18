package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * 充值并投标交易推进
 * Created by liyanan on 16/7/12.
 */
public class AdvanceDepositAndApplyAppResponse extends Response {
    private String advanceVoucherNo; //推进号
    private String bizStatus = "P";// S成功;P处理中;F失败
    private String activityMsg = "";//活动发券结果文案
    private int activityState; //参加活动状态 0:未参加活动 1:成功 2：参加活动失败
    private long activityDealId; //活动推进id
    //若推进成功会返回一下数据
    private String productId = ""; // 产品Id
    private double amount;//申购金额
    private String horizon = ""; // 投资期限
    private String yearInterestRate = ""; // 预计年化利率
    private String productName = ""; // 产品名称
    private int isPopShare = 0; //是否弹出分享 0-不弹 1-弹
    private String imgUrl = "";//图片地址
    private String title = "";//分享标题
    private String description = ""; // 描述
    private String webpageUrl = ""; // 页面跳转地址
    private String shareIcon = ""; // 分享图标
    private long bannerId; //cms弹窗id

    private int isTransfer;//是否是转让房产宝 1是 0否
    private String transferPrice = "";//转让价格
    private String amountStr="";//格式化的申购金额

    private String huoqibaoRate; //活期宝年化收益率
    private String endBuyTimeStr = ""; //购买截止时间 格式：yyyy-MM-dd
    private String interestDateStr = ""; //起息日期 格式：yyyy-MM-dd
    private double hasBuyPrecent;//融资进度，小数表示，比如0.15表示15%
    private String hasBuyPrecentStr="";//融资进度，比如15%

    private String backAmount=""; //预计回款收益
    private String bidOrderNo="";//订单编号  成功购买返回 用于跳转到详情


    public String getAdvanceVoucherNo() {
        return advanceVoucherNo;
    }

    public void setAdvanceVoucherNo(String advanceVoucherNo) {
        this.advanceVoucherNo = advanceVoucherNo;
    }

    public String getBizStatus() {
        return bizStatus;
    }

    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }

    public String getActivityMsg() {
        return activityMsg;
    }

    public void setActivityMsg(String activityMsg) {
        this.activityMsg = activityMsg;
    }

    public int getActivityState() {
        return activityState;
    }

    public void setActivityState(int activityState) {
        this.activityState = activityState;
    }

    public long getActivityDealId() {
        return activityDealId;
    }

    public void setActivityDealId(long activityDealId) {
        this.activityDealId = activityDealId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getHorizon() {
        return horizon;
    }

    public void setHorizon(String horizon) {
        this.horizon = horizon;
    }

    public String getYearInterestRate() {
        return yearInterestRate;
    }

    public void setYearInterestRate(String yearInterestRate) {
        this.yearInterestRate = yearInterestRate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getIsPopShare() {
        return isPopShare;
    }

    public void setIsPopShare(int isPopShare) {
        this.isPopShare = isPopShare;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebpageUrl() {
        return webpageUrl;
    }

    public void setWebpageUrl(String webpageUrl) {
        this.webpageUrl = webpageUrl;
    }

    public String getShareIcon() {
        return shareIcon;
    }

    public void setShareIcon(String shareIcon) {
        this.shareIcon = shareIcon;
    }

    public long getBannerId() {
        return bannerId;
    }

    public void setBannerId(long bannerId) {
        this.bannerId = bannerId;
    }

    public int getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(int isTransfer) {
        this.isTransfer = isTransfer;
    }

    public String getTransferPrice() {
        return transferPrice;
    }

    public void setTransferPrice(String transferPrice) {
        this.transferPrice = transferPrice;
    }

    public String getHuoqibaoRate() {
        return huoqibaoRate;
    }

    public void setHuoqibaoRate(String huoqibaoRate) {
        this.huoqibaoRate = huoqibaoRate;
    }

    public String getEndBuyTimeStr() {
        return endBuyTimeStr;
    }

    public void setEndBuyTimeStr(String endBuyTimeStr) {
        this.endBuyTimeStr = endBuyTimeStr;
    }

    public String getInterestDateStr() {
        return interestDateStr;
    }

    public void setInterestDateStr(String interestDateStr) {
        this.interestDateStr = interestDateStr;
    }

    public double getHasBuyPrecent() {
        return hasBuyPrecent;
    }

    public void setHasBuyPrecent(double hasBuyPrecent) {
        this.hasBuyPrecent = hasBuyPrecent;
    }

    public String getBackAmount() {
        return backAmount;
    }

    public void setBackAmount(String backAmount) {
        this.backAmount = backAmount;
    }

    public String getHasBuyPrecentStr() {
        return hasBuyPrecentStr;
    }

    public void setHasBuyPrecentStr(String hasBuyPrecentStr) {
        this.hasBuyPrecentStr = hasBuyPrecentStr;
    }

    public String getAmountStr() {
        return amountStr;
    }

    public void setAmountStr(String amountStr) {
        this.amountStr = amountStr;
    }

    public String getBidOrderNo() {
        return bidOrderNo;
    }

    public void setBidOrderNo(String bidOrderNo) {
        this.bidOrderNo = bidOrderNo;
    }
}
