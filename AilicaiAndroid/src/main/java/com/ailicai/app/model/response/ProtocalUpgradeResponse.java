package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.Protocol;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by Zhou Xuan on 2016/11/24
 *
 * 协议升级返回值
 */
public class ProtocalUpgradeResponse extends Response {

    private int showDialog; //是否提示升级dialog   0否  1是
    private List<Protocol> protocols;//协议集合
    private int errorCode = 0; //返回代码 0-正常 其他参考对应的errorCode定义
    private String message = "";//返回消息
    private String subTitle = "";
    private String content = "";

    public boolean isShowDialog() {
        return showDialog == 1;
    }

    public int getShowDialog() {
        return showDialog;
    }

    public void setShowDialog(int showDialog) {
        this.showDialog = showDialog;
    }

    public List<Protocol> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<Protocol> protocols) {
        this.protocols = protocols;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
