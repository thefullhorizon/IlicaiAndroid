package com.ailicai.app.model.response;

import com.huoqiu.framework.rest.Response;


/**
 * 登陆验证Response模型
 *
 * @author hubin
 */
public class UserLoginResponse extends Response {

    private long userId; // 用户ID

    private String name = ""; // 用户姓名

    private int gender;//性别 1：男，2：女， 3:保密

    private int seekHouseNum; // 看房单数量

    private int appointNum; //行程数量（待确认）

    private int recentEnd; //最新完成 0：无 1：有

    private boolean myupdate; //我的是否有更新

    private String assigneeName = ""; //经纪人姓名

    private String assigneeTel = ""; //经纪人电话

    private String assigneePhotoUrl = ""; //经纪人照片URL

    private float score; //经纪人评分

    private int bizType;//经纪人类型 0:租房经纪人  1：二手房经纪人

    private String uticket; // userId加密字符串(存储到本地和header中)

    private int isOpenAccount = 0; // 判断爱理财是否是否开户 0:否，1:是
    private int hasSafeCard = 0; // 是否已绑定安全卡  0:否，1:是
    private int isSetPayPwd = 0; // 判断是否设置支付密码 0:否，1:是
    private int isRealNameVerify = 0; // 爱理财是否通过实名认证 0:否，1:是
    private int isBinDebitCard = 0; // 爱理财是否已绑定借记卡 0:否，1:是
    private int isAilicaiAllowUser = 0; // 判断是否爱理财白名单用户 0:否，1:是
    private String key1=""; // app端加密用公钥
    private String key2=""; // server端加密的对应解密私钥
    private int rsaClose = 0; // RSA加密是否关闭，1：关闭，0：打开

    private String imUserId = ""; // 环信用户Id
    private String imPasswd = ""; // 环信用户密码
    private String rName = ""; // 用户真实姓名（5.2更新）
    private String idCardNo = ""; // 身份证号（5.2更新）
    private int collectionNum; //关注总数量
    private int firstLogin; //0-首次 1-否
    private int isAllDontDisturb; // 是否设置了全量免打扰 0：否，1：是

    // 日程评价弹窗 appointmentId > 0则表示有评价弹窗
    private long appointmentId; //约会ID
    private int appointBizType; // 业务类型 1：租房，2：二手房

    private String activityName;//大礼包数据

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getRecentEnd() {
        return recentEnd;
    }

    public void setRecentEnd(int recentEnd) {
        this.recentEnd = recentEnd;
    }

    public boolean isMyupdate() {
        return myupdate;
    }

    public void setMyupdate(boolean myupdate) {
        this.myupdate = myupdate;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getAssigneeTel() {
        return assigneeTel;
    }

    public void setAssigneeTel(String assigneeTel) {
        this.assigneeTel = assigneeTel;
    }

    public void setAssigneePhotoUrl(String assigneePhotoUrl) {
        this.assigneePhotoUrl = assigneePhotoUrl;
    }

    public String getAssigneePhotoUrl() {
        return assigneePhotoUrl;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public String getUticket() {
        return uticket;
    }

    public void setUticket(String uticket) {
        this.uticket = uticket;
    }


    public int getIsOpenAccount() {
        return isOpenAccount;
    }

    public void setIsOpenAccount(int isOpenAccount) {
        this.isOpenAccount = isOpenAccount;
    }

    public int getHasSafeCard() {
        return hasSafeCard;
    }

    public void setHasSafeCard(int hasSafeCard) {
        this.hasSafeCard = hasSafeCard;
    }

    public int getIsSetPayPwd() {
        return isSetPayPwd;
    }

    public void setIsSetPayPwd(int isSetPayPwd) {
        this.isSetPayPwd = isSetPayPwd;
    }

    public int getIsRealNameVerify() {
        return isRealNameVerify;
    }

    public void setIsRealNameVerify(int isRealNameVerify) {
        this.isRealNameVerify = isRealNameVerify;
    }

    public int getIsBinDebitCard() {
        return isBinDebitCard;
    }

    public void setIsBinDebitCard(int isBinDebitCard) {
        this.isBinDebitCard = isBinDebitCard;
    }

    public int getIsAilicaiAllowUser() {
        return isAilicaiAllowUser;
    }

    public void setIsAilicaiAllowUser(int isAilicaiAllowUser) {
        this.isAilicaiAllowUser = isAilicaiAllowUser;
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public int getRsaClose() {
        return rsaClose;
    }

    public void setRsaClose(int rsaClose) {
        this.rsaClose = rsaClose;
    }

    public String getImUserId() {
        return imUserId;
    }

    public void setImUserId(String imUserId) {
        this.imUserId = imUserId;
    }

    public String getImPasswd() {
        return imPasswd;
    }

    public void setImPasswd(String imPasswd) {
        this.imPasswd = imPasswd;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public int getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(int collectionNum) {
        this.collectionNum = collectionNum;
    }

    public int getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(int firstLogin) {
        this.firstLogin = firstLogin;
    }

    public int getIsAllDontDisturb() {
        return isAllDontDisturb;
    }

    public void setIsAllDontDisturb(int isAllDontDisturb) {
        this.isAllDontDisturb = isAllDontDisturb;
    }

    public long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getAppointBizType() {
        return appointBizType;
    }

    public void setAppointBizType(int appointBizType) {
        this.appointBizType = appointBizType;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
