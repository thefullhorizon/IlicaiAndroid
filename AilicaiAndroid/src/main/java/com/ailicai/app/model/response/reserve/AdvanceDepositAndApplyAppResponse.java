package com.ailicai.app.model.response.reserve;

import com.huoqiu.framework.rest.Response;

/**
 * Created by Owen on 2016/7/12
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

    private int remainingCnt = 0; // 密码错误剩余次数
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

    public int getRemainingCnt() {
        return remainingCnt;
    }

    public void setRemainingCnt(int remainingCnt) {
        this.remainingCnt = remainingCnt;
    }

    public String getBidOrderNo() {
        return bidOrderNo;
    }

    public void setBidOrderNo(String bidOrderNo) {
        this.bidOrderNo = bidOrderNo;
    }
}
