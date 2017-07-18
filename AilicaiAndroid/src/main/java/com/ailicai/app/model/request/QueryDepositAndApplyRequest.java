package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by liyanan on 16/7/12.
 */
@RequestPath("/ailicai/queryDepositAndApply.rest")
public class QueryDepositAndApplyRequest extends Request {
    private String advanceVoucherNo; //交易推进号
    private long activityDealId; //活动推进id
    private String productId; //房产宝id

    public String getAdvanceVoucherNo() {
        return advanceVoucherNo;
    }

    public void setAdvanceVoucherNo(String advanceVoucherNo) {
        this.advanceVoucherNo = advanceVoucherNo;
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
}
