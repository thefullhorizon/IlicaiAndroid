package com.huoqiu.framework.rest;

public enum Configuration {

    /**
     * ************IW Configuration*********************
     */
    IWJW_RELEASE("https", "payapp.iwjw.com", 443, ""),
//    IWJW_BETA("http", "poros.iwlicaibeta.com", 80, ""),
    IWJW_BETA("http", "118.178.242.96", 8737, ""),
    IWJW_TEST("http", "192.168.1.44", 1319, ""),
    IWJW_TEST2("http", "poros.iwlicaitest.com", 1319, ""),
    IWJW_TEST3("http", "192.168.1.183", 8080, ""),

    /**
     * 图片上传服务器
     */
    IWJW_RELEASE_IMG("http","img.superjia.com",80,""),
    IWJW_BETA_IMG("http","beta.imgsoa.com",8133,""),
    IWJW_TEST_IMG("http","20.0.0.7",8133,"");

    Configuration(String protocol, String hostname, int port, String path) {
        this.protocol = protocol;
        this.hostname = hostname;
        this.port = port;
        this.path = path;
    }

    public static Configuration DEFAULT = IWJW_TEST;
    public static Configuration DEFAULTIMG = IWJW_TEST_IMG;
    public String appKey = "iwjw.superjia.com";
    // 报文头需要的字段
    public String appKeyLabel = "App-Key"; // key
    public String appSecretLabel = "App-Secret"; // secret
    public String appTime = "App_Time";// 当前时间
    public String appVersion = "ver"; // 手机端版本号
    public String appOS = "os"; // 手机来源 (android/ios)
    public String appIMEI = "imei"; // 手机唯一标志
    public String appModel = "model"; // 手机型号

    // Configuration
    public String protocol = "http";
    public String hostname = "192.168.1.123";// 生产环境
    public int port = 80;
    public String path = "/rest";

    public String getRootUrl() {
        return protocol + "://" + hostname + ":" + port + path;
    }

    public String getDomain() {
        return protocol + "://" + hostname + ":" + port;
    }

}
