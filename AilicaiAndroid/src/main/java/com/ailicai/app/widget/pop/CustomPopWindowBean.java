package com.ailicai.app.widget.pop;

import java.io.Serializable;

/**
 * Created by owen on 2016/1/19
 */
public class CustomPopWindowBean implements Serializable {

    private int id;
    private int key;
    private String titleName;
    private String lowValue; //下限值
    private String highValue; //上限值
    private String tag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getLowValue() {
        return lowValue;
    }

    public void setLowValue(String lowValue) {
        this.lowValue = lowValue;
    }

    public String getHighValue() {
        return highValue;
    }

    public void setHighValue(String highValue) {
        this.highValue = highValue;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "CustomPopWindowBean{" +
                "id=" + id +
                ", key=" + key +
                ", titleName='" + titleName + '\'' +
                ", lowValue='" + lowValue + '\'' +
                ", highValue='" + highValue + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
