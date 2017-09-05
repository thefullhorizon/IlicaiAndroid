package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;

/**
 * Created by Zhou Xuan on 2016/11/24
 *
 * 吉爱财是否系统在升级返回
 */
public class FinanceSystemIsFixResponse extends Response {

    private int showDialog; //是否提示维护dialog 0否 1是
    private String time="";//恢复时间

    public boolean isShowDialog() {
        return showDialog == 1;
    }

    public int getShowDialog() {
        return showDialog;
    }

    public void setShowDialog(int showDialog) {
        this.showDialog = showDialog;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
