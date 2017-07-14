package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;


/**
 * 获取最新全局数据 Function: TODO ADD FUNCTION
 *
 * @author hubin
 * @Date 2014年6月16日 下午6:29:43
 * @see
 */
public class RreshDataResponse extends Response {

    private int reserveNum;//预约看房数

    private int seekHouseNum; // 看房单数量

    private int appointNum; // 行程数量

    private boolean myupdate; // 我的是否有更新

    private int recentEnd; // 最新完成 0：无 1：

    private int hasNewNotify; //是否有提醒或通知或动态 大于零:true,0:false

    private int remindNum; //提醒数

    private int noticeNum; // 通知数

    private String lastNoticeTime; // 最后的消息时间

    private int hasNewDynamic; // 动态是否有最新消息 0：无，1：有

    private int notReadCollectionNum; // 未读的关注数（关注列表红点展示用）

    private int hasPopActive;

    private int activityNum; // 活动未读数

    private int isRentRedPoint; //关注是否有出租动态小红点：1有，0没有
    private int isSaleRedPoint; //关注是否有出售动态小红点：1有，0没有

    // -------------------------v6.4增加---关注列表小红点
    private int rentFeedHouseUnreadCount;//出租房源未读数 0:无；大于0：有
    private int saleFeedHouseUnreadCount;//出售房源未读数 0:无；大于0：有
    private int estateUnreadCount;//小区未读数 0:无；大于0：有


    public int getReserveNum() {
        return reserveNum;
    }

    public void setReserveNum(int reserveNum) {
        this.reserveNum = reserveNum;
    }

    public int getActivityNum() {
        return activityNum;
    }

    public void setActivityNum(int activityNum) {
        this.activityNum = activityNum;
    }

    public int getHasPopActive() {//是否有弹窗活动 1有 0没有
        return hasPopActive;
    }

    public void setHasPopActive(int hasPopActive) {
        this.hasPopActive = hasPopActive;
    }

    public int getNotReadCollectionNum() {
        return notReadCollectionNum;
    }

    public void setNotReadCollectionNum(int notReadCollectionNum) {
        this.notReadCollectionNum = notReadCollectionNum;
    }

    public int getSeekHouseNum() {
        return seekHouseNum;
    }

    public void setSeekHouseNum(int seekHouseNum) {
        this.seekHouseNum = seekHouseNum;
    }

    public int getAppointNum() {
        return appointNum;
    }

    public void setAppointNum(int appointNum) {
        this.appointNum = appointNum;
    }

    public boolean isMyupdate() {
        return myupdate;
    }

    public void setMyupdate(boolean myupdate) {
        this.myupdate = myupdate;
    }

    public int getRecentEnd() {
        return recentEnd;
    }

    public void setRecentEnd(int recentEnd) {
        this.recentEnd = recentEnd;
    }

    public boolean getHasNewNotify() {
        return hasNewNotify == 1;
    }

    public void setHasNewNotify(int hasNewNotify) {
        this.hasNewNotify = hasNewNotify;
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

    public int getIsRentRedPoint() {
        return isRentRedPoint;
    }

    public void setIsRentRedPoint(int isRentRedPoint) {
        this.isRentRedPoint = isRentRedPoint;
    }

    public int getIsSaleRedPoint() {
        return isSaleRedPoint;
    }

    public void setIsSaleRedPoint(int isSaleRedPoint) {
        this.isSaleRedPoint = isSaleRedPoint;
    }

    public int getRentFeedHouseUnreadCount() {
        return rentFeedHouseUnreadCount;
    }

    public void setRentFeedHouseUnreadCount(int rentFeedHouseUnreadCount) {
        this.rentFeedHouseUnreadCount = rentFeedHouseUnreadCount;
    }

    public int getSaleFeedHouseUnreadCount() {
        return saleFeedHouseUnreadCount;
    }

    public void setSaleFeedHouseUnreadCount(int saleFeedHouseUnreadCount) {
        this.saleFeedHouseUnreadCount = saleFeedHouseUnreadCount;
    }

    public int getEstateUnreadCount() {
        return estateUnreadCount;
    }

    public void setEstateUnreadCount(int estateUnreadCount) {
        this.estateUnreadCount = estateUnreadCount;
    }

    @Override
    public String toString() {
        return "RreshDataResponse{" +
                "seekHouseNum=" + seekHouseNum +
                ", appointNum=" + appointNum +
                ", myupdate=" + myupdate +
                ", recentEnd=" + recentEnd +
                ", hasNewNotify=" + hasNewNotify +
                ", remindNum=" + remindNum +
                ", noticeNum=" + noticeNum +
                ", lastNoticeTime='" + lastNoticeTime + '\'' +
                ", hasNewDynamic=" + hasNewDynamic +
                ", notReadCollectionNum=" + notReadCollectionNum +
                ", hasPopActive=" + hasPopActive +
                ", activityNum=" + activityNum +
                ", isRentRedPoint=" + isRentRedPoint +
                ", isSaleRedPoint=" + isSaleRedPoint +
                '}';
    }
}
