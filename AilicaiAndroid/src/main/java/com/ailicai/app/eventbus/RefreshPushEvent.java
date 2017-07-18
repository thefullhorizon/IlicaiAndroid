package com.ailicai.app.eventbus;

/**
 * Created by duo.chen on 2015/8/25.
 */
public class RefreshPushEvent {

    private int msgType;

    private int type;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
