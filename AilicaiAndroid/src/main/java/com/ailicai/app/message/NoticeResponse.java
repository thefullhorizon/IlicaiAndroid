package com.ailicai.app.message;

import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by duo.chen on 2015/8/10.
 */
public class NoticeResponse extends Response {
    private int hasNewNotify; //1:true,0:false
    private int remindNum = 0; //提醒数
    private int noticeNum = 0; // 通知数
    private String lastNoticeTime; // 最后的消息时间
    private int hasNewDynamic; // 动态是否有最新消息 0：无，1：有
    private int dynamicEstateNum = 0; // 小区动态件数
    private int dynamicHouseNum = 0; // 房源动态件数
    private int dynamicSchoolNum = 0; // 学区动态件数

    private String noticeTitle = ""; //首条通知标题
    private String noticeTime = ""; //首条通知时间
    private String remindTitle = ""; //首条提醒标题
    private String remindTime = ""; //首条通知时间
    private String activityTitle = ""; // 首条活动标题
    private String activityTime = ""; // 首条互动时间
    private int activityNum; // 活动数量

    private List<Long> agentList; // 免打扰的经纪人列表

    private int sigRentContRemindCount;    // 签署合同提醒数量

    private String payRentActivityUrl;  //交房租弹窗URL

    private int hasPopActive;

    public int getHasPopActive() {//是否有弹窗活动 1有 0没有
        return hasPopActive;
    }

    public void setHasPopActive(int hasPopActive) {
        this.hasPopActive = hasPopActive;
    }


    public boolean isHasNewNotify() {
        return hasNewNotify == 1;
    }

    public int getRemindNum() {
        return remindNum;
    }

    public void setRemindNum(int remindNum) {
        this.remindNum = remindNum;
    }

    public int getNoticeNum() {
        return noticeNum;
    }

    public void setNoticeNum(int noticeNum) {
        this.noticeNum = noticeNum;
    }

    public String getLastNoticeTime() {
        return lastNoticeTime;
    }

    public void setLastNoticeTime(String lastNoticeTime) {
        this.lastNoticeTime = lastNoticeTime;
    }

    public int getHasNewDynamic() {
        return hasNewDynamic;
    }

    public void setHasNewDynamic(int hasNewDynamic) {
        this.hasNewDynamic = hasNewDynamic;
    }

    public int getDynamicEstateNum() {
        return dynamicEstateNum;
    }

    public void setDynamicEstateNum(int dynamicEstateNum) {
        this.dynamicEstateNum = dynamicEstateNum;
    }

    public int getDynamicHouseNum() {
        return dynamicHouseNum;
    }

    public void setDynamicHouseNum(int dynamicHouseNum) {
        this.dynamicHouseNum = dynamicHouseNum;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(String noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getRemindTitle() {
        return remindTitle;
    }

    public void setRemindTitle(String remindTitle) {
        this.remindTitle = remindTitle;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public int getDynamicSchoolNum() {
        return dynamicSchoolNum;
    }

    public void setDynamicSchoolNum(int dynamicSchoolNum) {
        this.dynamicSchoolNum = dynamicSchoolNum;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public int getActivityNum() {
        return activityNum;
    }

    public void setActivityNum(int activityNum) {
        this.activityNum = activityNum;
    }

    public List<Long> getAgentList() {
        return agentList;
    }

    public void setAgentList(List<Long> agentList) {
        this.agentList = agentList;
    }

    public int getHasNewNotify() {
        return hasNewNotify;
    }

    public void setHasNewNotify(int hasNewNotify) {
        this.hasNewNotify = hasNewNotify;
    }

    public int getSigRentContRemindCount() {
        return sigRentContRemindCount;
    }

    public void setSigRentContRemindCount(int sigRentContRemindCount) {
        this.sigRentContRemindCount = sigRentContRemindCount;
    }

    public String getPayRentActivityUrl() {
        return payRentActivityUrl;
    }

    public void setPayRentActivityUrl(String payRentActivityUrl) {
        this.payRentActivityUrl = payRentActivityUrl;
    }
}
