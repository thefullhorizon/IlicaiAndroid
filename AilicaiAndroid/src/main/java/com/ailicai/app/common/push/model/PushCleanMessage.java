package com.ailicai.app.common.push.model;

import java.io.Serializable;

/**
 * Created by duo.chen on 2016/5/23.
 */

public class PushCleanMessage implements Serializable {
    private String topic;
    private String clientId;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
