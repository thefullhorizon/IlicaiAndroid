package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

public class InviteRewardResponse extends Response {

    private String alreadyAward = "";//已发奖励

    private String waittingAward = "";//待发奖励

    private int inviteNumber = 0; //已邀请好友总数

    private String investAmount = "";//邀请好友在投金额总数

    public String getAlreadyAward() {
        return alreadyAward;
    }

    public void setAlreadyAward(String alreadyAward) {
        this.alreadyAward = alreadyAward;
    }

    public String getWaittingAward() {
        return waittingAward;
    }

    public void setWaittingAward(String waittingAward) {
        this.waittingAward = waittingAward;
    }

    public int getInviteNumber() {
        return inviteNumber;
    }

    public void setInviteNumber(int inviteNumber) {
        this.inviteNumber = inviteNumber;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }
}
