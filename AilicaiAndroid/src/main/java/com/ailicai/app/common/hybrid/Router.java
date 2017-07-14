package com.ailicai.app.common.hybrid;

import java.util.HashMap;

/**
 * Created by duo.chen on 2016/5/17.
 */
public class Router {

    public String protocol;
    public String host;
    public String version;
    public HashMap<String,String> rules;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public HashMap<String, String> getRules() {
        return rules;
    }

    public void setRules(HashMap<String, String> rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "Router{" +
                "protocol='" + protocol + '\'' +
                ", host='" + host + '\'' +
                ", version='" + version + '\'' +
                ", rules=" + rules +
                '}';
    }
}
