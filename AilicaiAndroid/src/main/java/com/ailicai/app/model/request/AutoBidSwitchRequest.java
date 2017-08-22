package com.ailicai.app.model.request;

import com.ailicai.app.common.reqaction.RequestPath;

/**
 * 为当前用户开启或者关闭自动投标
 * Created by jeme on 2017/8/21.
 */

@RequestPath("/user/autoBidSwitch.rest")
public class AutoBidSwitchRequest extends Request {

    private int autoBidCommand; // 自动投资开关 0:close 1:open
    private int strategyType; // 投标策略 1:期限短 2: 利率高
    private double reserveBalance; // 保留金额
    private String payPwd; // 交易密码

    public int getAutoBidCommand() {
        return autoBidCommand;
    }

    public void setAutoBidCommand(int autoBidCommand) {
        this.autoBidCommand = autoBidCommand;
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

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }
}
