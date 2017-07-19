package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

@RequestPath("/ailicai/inviteReward.rest")
public class InviteRewardRequest extends Request {
    public long userid;

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }
}
