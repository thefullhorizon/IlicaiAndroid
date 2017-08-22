package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * 购买房产宝Response
 * Created by Jer on 2016/1/7.
 */
public class BuyDingqibaoResponse extends Response {
    private int isTransfer = 1;//是否是转让房产宝 1 是 0否
    private String amountStr = "";//格式化的申购金额

    private long bannerId; //cms弹窗id
    private String productId = ""; // 产品Id
    private double amount;//申购金额
    private String horizon = ""; // 投资期限
    private String yearInterestRate = ""; // 预计年化利率
    private String productName = ""; // 产品名称
    private String bizStatus;//S成功;P处理中;F失败
    private int isPopShare = 0; //是否弹出分享 0-不弹 1-弹
    private String imgUrl = "";//图片地址
    private String title = "";//分享标题
    private String description = ""; //
    private String webpageUrl = ""; //
    private String shareIcon = ""; // 分享图标
    private int remainingCnt = 0; // 密码错误剩余次数
    private String activityMsg = "";//活动发券结果文案
    private boolean isLast;//是否是最后一笔
    private String huoqibaoRate = ""; //活期宝年化收益率
    private String endBuyTimeStr = ""; //购买截止时间 格式：yyyy-MM-dd
    private String interestDateStr = ""; //起息日期 格式：yyyy-MM-dd
    private double hasBuyPrecent;//融资进度，小数表示，比如0.15表示15%
    private String transferPrice = "";//转让价格
    private String backAmount = "";//转房产宝预计回款
    private String hasBuyPrecentStr = "";//融资进度，比如15%
    private String bidOrderNo = "";//订单编号  成功购买返回 用于跳转到详情
    private int usePoll; //   1轮询  0 不轮询   余额购买  仅新浪渠道购买转让房产宝需要轮询 是否轮询根据 Status=P&&userPoll =1

    // V1.2新增
    private String firstInvestLotteryURL="";//首投抽奖的url
    private int isLottery; //是否可抽奖 1:可以抽奖

    public int getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(int isTransfer) {
        this.isTransfer = isTransfer;
    }

    public String getAmountStr() {
        return amountStr;
    }

    public void setAmountStr(String amountStr) {
        this.amountStr = amountStr;
    }

    public long getBannerId() {
        return bannerId;
    }

    public void setBannerId(long bannerId) {
        this.bannerId = bannerId;
    }

    public int getRemainingCnt() {
        return remainingCnt;
    }

    public void setRemainingCnt(int remainingCnt) {
        this.remainingCnt = remainingCnt;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getYearInterestRate() {
        return yearInterestRate;
    }

    public void setYearInterestRate(String yearInterestRate) {
        this.yearInterestRate = yearInterestRate;
    }

    public String getBizStatus() {
        return bizStatus;
    }

    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }

    public boolean getIsPopShare() {
        return (isPopShare == 1);
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

    public String getActivityMsg() {
        return activityMsg;
    }

    public void setActivityMsg(String activityMsg) {
        this.activityMsg = activityMsg;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public boolean isLast() {
        return isLast;
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

    public String getTransferPrice() {
        return transferPrice;
    }

    public void setTransferPrice(String transferPrice) {
        this.transferPrice = transferPrice;
    }

    public String getBackAmount() {
        return backAmount;
    }

    public void setBackAmount(String backAmount) {
        this.backAmount = backAmount;
    }

    public void setHasBuyPrecentStr(String hasBuyPrecentStr) {
        this.hasBuyPrecentStr = hasBuyPrecentStr;
    }

    public String getHasBuyPrecentStr() {
        return hasBuyPrecentStr;
    }

    public String getBidOrderNo() {
        return bidOrderNo;
    }

    public void setBidOrderNo(String bidOrderNo) {
        this.bidOrderNo = bidOrderNo;
    }

    public int getUsePoll() {
        return usePoll;
    }

    public void setUsePoll(int usePoll) {
        this.usePoll = usePoll;
    }

    public String getFirstInvestLotteryURL() {
        return firstInvestLotteryURL;
    }

    public void setFirstInvestLotteryURL(String firstInvestLotteryURL) {
        this.firstInvestLotteryURL = firstInvestLotteryURL;
    }

    public int getIsLottery() {
        return isLottery;
    }

    public void setIsLottery(int isLottery) {
        this.isLottery = isLottery;
    }

    @Override
    public String toString() {
        return "BuyDingqibaoResponse{" +
                "bannerId=" + bannerId +
                ", productId='" + productId + '\'' +
                ", amount=" + amount +
                ", horizon='" + horizon + '\'' +
                ", yearInterestRate='" + yearInterestRate + '\'' +
                ", productName='" + productName + '\'' +
                ", bizStatus='" + bizStatus + '\'' +
                ", isPopShare=" + isPopShare +
                ", imgUrl='" + imgUrl + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", webpageUrl='" + webpageUrl + '\'' +
                ", shareIcon='" + shareIcon + '\'' +
                ", remainingCnt=" + remainingCnt +
                ", activityMsg='" + activityMsg + '\'' +
                ", isLast=" + isLast +
                ", huoqibaoRate='" + huoqibaoRate + '\'' +
                ", endBuyTimeStr='" + endBuyTimeStr + '\'' +
                ", interestDateStr='" + interestDateStr + '\'' +
                ", hasBuyPrecent=" + hasBuyPrecent +
                ", transferPrice='" + transferPrice + '\'' +
                ", backAmount='" + backAmount + '\'' +
                ", hasBuyPrecentStr='" + hasBuyPrecentStr + '\'' +
                '}';
    }
}
