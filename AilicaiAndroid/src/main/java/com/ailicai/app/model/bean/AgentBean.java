package com.ailicai.app.model.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * @author shengjunkai 经纪人实体
 */
public class AgentBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String phone;

    private String img;

    private float rating;

    private int bizType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    /**
     * 获取评分
     */
    public float getRating() {
        return rating;
    }

    /**
     * 设置评分
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    @Override
    public String toString() {
        return "[id=" + id + ";name=" + name + ";phone=" + phone + ";img=" + img + ";rating=" + rating + "]";
    }


    public AgentBean(String id, String name, String img, String phone, float score, int bizType) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.phone = phone;
        this.rating = score;
        this.bizType = bizType;
    }

    public AgentBean() {
        this.id = "";
        this.name = "";
        this.img = "";
        this.phone = "";
        this.rating = 0;
        this.bizType = 0;
    }

    /**
     * @param @return
     * @return boolean
     * @Title: isLegalAgent
     * @Description:经纪人是否可用
     */
    public boolean isLegalAgent() {
        String agent = id + name + phone + img;
        return !TextUtils.isEmpty(agent);
    }

    public String AgentToString() {
        String result = "Agent = " + id + name + phone + img + bizType;
        return result;
    }

}
