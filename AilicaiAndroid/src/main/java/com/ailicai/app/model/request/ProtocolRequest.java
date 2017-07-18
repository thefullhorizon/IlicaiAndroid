package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * 请求协议名称和url
 * Created by liyanan on 16/10/8.
 */
@RequestPath("/ailicai/getProtocolInfo.rest")
public class ProtocolRequest extends Request {
    private int bizType;//业务类型  1填写银行卡信息  2钱包转入收银台  3普通房产宝收银台  4转让房产宝收银台 5预约房产宝收银台  6小绑卡

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }
}
