package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.Protocol;
import com.huoqiu.framework.rest.Response;

/**
 * 自动投标的响应报文
 * Created by jeme on 2017/8/21.
 */

public class AutoBidResponse extends Response {

    private String isAutoBid; // "Y":表示自动投标, "N":表示未设置自动投标
    private int strategyType; // 投标策略 1:期限短 2: 利率高
    private double reserveBalance; // 保留金额

    private Protocol autoBidProtocol;//自动投标授权协议

    public String getIsAutoBid() {
        return isAutoBid;
    }

    public void setIsAutoBid(String isAutoBid) {
        this.isAutoBid = isAutoBid;
    }

    public int getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(int strategyType) {
        this.strategyType = strategyType;
    }

    public double getReserveBalance() {
        return reserveBalance;
    }

    public void setReserveBalance(double reserveBalance) {
        this.reserveBalance = reserveBalance;
    }

    public Protocol getAutoBidProtocol() {
        return autoBidProtocol;
    }

    public void setAutoBidProtocol(Protocol autoBidProtocol) {
        this.autoBidProtocol = autoBidProtocol;
    }
}
