package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.InviteRecord;
import com.huoqiu.framework.rest.Response;

import java.util.List;

public class InviteRecordResponse extends Response {

    List<InviteRecord> InviteRecordList;//最多十条邀请记录

    public List<InviteRecord> getInviteRecordList() {
        return InviteRecordList;
    }

    public void setInviteRecordList(List<InviteRecord> inviteRecordList) {
        InviteRecordList = inviteRecordList;
    }
}
