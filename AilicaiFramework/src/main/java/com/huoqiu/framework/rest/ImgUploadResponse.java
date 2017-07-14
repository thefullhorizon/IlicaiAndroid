package com.huoqiu.framework.rest;

import java.io.Serializable;
import java.util.Observable;

/**
 * Created by duo.chen on 2015/12/3.
 */
public class ImgUploadResponse extends Observable implements Serializable {
    int status;
    String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
