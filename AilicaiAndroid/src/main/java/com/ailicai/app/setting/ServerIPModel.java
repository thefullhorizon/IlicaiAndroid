package com.ailicai.app.setting;

import java.io.Serializable;

/**
 * Created by Ted on 14-7-25.
 */
public class ServerIPModel implements Serializable {
    private static final long serialVersionUID = -5476051481692117803L;
    public int mIPType = 0;
    /**
     * 服务器地址前缀
     */
    public String mServerProtocol = "";//http
    /**
     * 服务器地址主机地址
     */
    public String mServerHostname = "";//"192.168.1.123"
    /**
     * 服务器地址端口号
     */
    public int mServerPort = 80;//
    /**
     * 服务器地址后缀
     */
    public String mServerPath = "";//"/rest"
    /**
     * 推送服务器地址
     */
    public String mPushServerIP = "";
    /**
     * 推送服务器端口号
     */
    public int mPushServerPort = 0;
}
