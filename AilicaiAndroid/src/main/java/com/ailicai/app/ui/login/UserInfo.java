package com.ailicai.app.ui.login;

import android.text.TextUtils;

import com.ailicai.app.common.utils.Constants;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.eventbus.RefreshDataUpgradeEvent;
import com.ailicai.app.message.NoticeResponse;
import com.ailicai.app.model.bean.AgentBean;
import com.ailicai.app.model.response.RreshDataResponse;
import com.ailicai.app.model.response.UserInfoResponse;
import com.ailicai.app.model.response.UserLoginResponse;
import com.ailicai.app.model.response.UserMyResponse;
import com.ailicai.app.setting.AppInfo;
import com.huoqiu.framework.app.AppConfig;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xubin on 14-6-9.
 */
public class UserInfo {
    public static final String IM_USERID = "im_userid";
    public static final String IM_PASSWD = "im_passwd";
    public static final String ID_CARDNO = "idCardNo";
    public static final String COLLECTION_NUM = "collectionNum";
    public static final String IS_TESTUSER = "isTestuser";
    public static final String IS_ALL_DONT_DISTURB = "isAllDontDisturb";

    public static final String USER_APP_PUBLIC_KEY = "user_app_public_key";
    public static final String USER_APP_RSA_CLOSED = "user_app_rsa_closed";

    public static final String USERINFO_SP_NEME = "local_userinfo";
    public static final String USERINFO_KEY_USER_ID = "userinfo_key_user_id";
    public static final String USERINFO_KEY_USER_NAME = "userinfo_key_user_name";
    public static final String USERINFO_KEY_USER_GENDER = "userinfo_key_user_gender";
    public static final String USERINFO_KEY_MOBILE = "userinfo_key_mobile";
    public static final String USERINFO_KEY_MOBILEID = "userinfo_key_mobileID";
    public static final String USERINOF_KEY_UTICKET = "userinof_key_uticket";
    public static final String USERINFO_KEY_SERVER_NUMBER = "userinfo_key_server_number";
    public static final String USERINFO_AGENT_ID = "userinfo_agent_id";
    public static final String USERINFO_AGENT_NAME = "userinfo_agent_name";
    public static final String USERINFO_AGENT_PHONE = "userinfo_agent_phone";
    public static final String USERINFO_AGENT_IMAG = "userinfo_agent_imag";
    public static final String USERINFO_AGENT_SCORE = "userinfo_agent_score";
    public static final String USERINFO_AGENT_TYPE = "userinfo_agent_type";
    public static final String USERINFO_UTICKET = "userinfo_uticket";
    public static final String USERINO_NOTIFY_LASTNOTICETIME = "userino_notify_lastnoticetime";
    public static final String USERINO_NOTIFY_OLDNOTICETIME = "userino_notify_oldnoticetime";
    public static final int NOT_LOGIN = 0;
    public static final int LOGIN = 1;
    /**
     * loginState:
     */
    private static int loginState = NOT_LOGIN;
    private static UserInfo sUserInfo = null;
    public int isTestuser = 0;////是否为新浪渠道内测用户 0-否 1-是
    private String SERVICE_PHONE_NUMBER = Constants.DEFAULT_SERVER_NUMBER;
    private long userId;
    private String mName; // 用户姓名
    private int mGender;//性别 1：男，2：女， 3:保密
    private String userMobile = "";
    private String uticket = "";
    private String userMobileID = "";
    private boolean isAutoBid;//是否开始自动投标
    /**
     * mAgent:用户对应的经纪人
     */
    private AgentBean agent = new AgentBean();
    /**
     * mMyOrderNum:
     */
    private int mMyOrderNum = 0;
    /**
     * seekHouseNum:看房单数量
     */
    private int seekHouseNum = 0;
    private int reserveNum;//预约看房数
    /**
     * appointNum:行程数量
     */
    private int appointNum = 0;
    private int recentEnd = 0; // 最新完成 0：无 1：有
    /**
     * 3.0新增消息相关
     */
    private boolean hasNewNotify;//是否有新的通知
    private int remindNum; //提醒数
    private int noticeNum; // 通知数
    private String lastNoticeTime;
    private int hasNewDynamic;//动态是否有最新消息 0：无，1：有
    private int notReadCollectionNum;
    private int dynamicEstateNum; // 小区动态件数
    private int dynamicHouseNum; // 房源动态件数
    private boolean myUpdate = false;
    /**
     * 是否有新的经纪人信息
     */
    private boolean hasNewAgenda = false;

    private UserInfo() {
        long userID = MyPreference.getInstance().read(UserInfo.USERINFO_KEY_USER_ID, new Long(0));
        String userMobile = MyPreference.getInstance().read(USERINFO_KEY_MOBILE, "");
        String userMobileID = MyPreference.getInstance().read(USERINFO_KEY_MOBILEID, "");
        String serverPhone = MyPreference.getInstance().read(USERINFO_KEY_SERVER_NUMBER, Constants.DEFAULT_SERVER_NUMBER);
        String suticket = MyPreference.getInstance().read(USERINOF_KEY_UTICKET, "");
        String lasttime = MyPreference.getInstance().read(USERINO_NOTIFY_LASTNOTICETIME, "");
        String userName = MyPreference.getInstance().read(USERINFO_KEY_USER_NAME, "");
        int userGender = MyPreference.getInstance().read(USERINFO_KEY_USER_GENDER, 0);
        isTestuser = MyPreference.getInstance().read(IS_TESTUSER, 0);
        /** 如果记住了合法的用户ID，就为登录状态 */
        setLoginState(userID > 0 ? LOGIN : NOT_LOGIN);
        setUserId(userID);
        setUserMobile(userMobile);
        setUticket(suticket);
        setUserMobileID(userMobileID);
        setServicePhoneNumber(serverPhone);
        setLastNoticeTime(lasttime);
        setmName(userName);
        setmGender(userGender);
        String agentId = MyPreference.getInstance().read(USERINFO_AGENT_ID, "");
        String agentName = MyPreference.getInstance().read(USERINFO_AGENT_NAME, "");
        String agentPhone = MyPreference.getInstance().read(USERINFO_AGENT_PHONE, "");
        String agentImage = MyPreference.getInstance().read(USERINFO_AGENT_IMAG, "");
        float agentScore = MyPreference.getInstance().read(USERINFO_AGENT_SCORE, 0f);
        int agentType = MyPreference.getInstance().read(USERINFO_AGENT_TYPE, 0);
        AgentBean agentBean = new AgentBean(agentId, agentName, agentImage, agentPhone, agentScore, agentType);
        this.agent = agentBean;
    }

    public static UserInfo getInstance() {
        if (null == sUserInfo) {
            sUserInfo = new UserInfo();
        }
        return sUserInfo;
    }

    public static boolean isLogin() {
        return UserInfo.getInstance().getLoginState() == UserInfo.LOGIN;
    }

    public static String getPublicKey() {
        return MyPreference.getInstance().read(USER_APP_PUBLIC_KEY, "");
    }

    public static int getRSAClosed() {
        return MyPreference.getInstance().read(USER_APP_RSA_CLOSED, 0);
    }

    public static String getImUserid() {
        return MyPreference.getInstance().read(IM_USERID, "");
    }

    public static String getImPasswd() {
        return MyPreference.getInstance().read(IM_PASSWD, "");
    }

    public static int getIsAllDontDisturb() {
        return MyPreference.getInstance().read(UserInfo.IS_ALL_DONT_DISTURB, 0);
    }

    public boolean isTestuser() {
        return isTestuser == 1;
    }

    public int getIsTestuser() {
        return isTestuser;
    }

    public void setIsTestuser(int isTestuser) {
        this.isTestuser = isTestuser;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserMobileID() {
        return userMobileID;
    }

    public void setUserMobileID(String userMobileID) {
        this.userMobileID = userMobileID;
    }

    public String getUticket() {
        return uticket;
    }

    public void setUticket(String uticket) {
        this.uticket = uticket;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmGender() {
        return mGender;
    }

    public void setmGender(int mGender) {
        this.mGender = mGender;
    }

    public boolean isHasNewAgenda() {
        return hasNewAgenda;
    }

    public void setHasNewAgenda(boolean hasNewAgenda) {
        this.hasNewAgenda = hasNewAgenda;
    }

    public String getServicePhoneNumber() {
        return SERVICE_PHONE_NUMBER;
    }

    public void setServicePhoneNumber(String sERVICE_PHONE_NUMBER) {
        if (!TextUtils.isEmpty(sERVICE_PHONE_NUMBER)) {
            SERVICE_PHONE_NUMBER = sERVICE_PHONE_NUMBER;
        }
    }

    public int getLoginState() {
        return loginState;
    }

    public void setLoginState(int mloginState) {
        loginState = mloginState;
    }

    public AgentBean getAgent() {
        return agent;
    }

    public void setAgent(AgentBean agent) {
        /**在设置经纪人信息之前，先确认一下是否是新的经纪人*/
        AgentBean oldAgentBean = getAgent();
        if (!TextUtils.isEmpty(agent.getImg() + agent.getName() + agent.getPhone())) {
            if (oldAgentBean.getId() != agent.getId()
                    || !oldAgentBean.getImg().equalsIgnoreCase(agent.getImg())
                    || !oldAgentBean.getPhone().equalsIgnoreCase(agent.getPhone())
                    || !oldAgentBean.getName().equalsIgnoreCase(agent.getName())
                    || oldAgentBean.getRating() != agent.getRating()) {

                setHasNewAgenda(true);
            }
        } else {
            setHasNewAgenda(false);
        }

        this.agent = agent;
    }

    public void setMyOrderNum(int mMyOrderNum) {
        this.mMyOrderNum = mMyOrderNum;
    }

    public boolean isMyUpdate() {
        return myUpdate || AppInfo.getInstance().isHaveUpdate();
    }

    public void setMyUpdate(boolean myUpdate) {
        this.myUpdate = myUpdate;
    }

    public int getSeekHouseNum() {
        return seekHouseNum;
    }

    public void setSeekHouseNum(int seekHouseNum) {
        this.seekHouseNum = seekHouseNum;
    }

    public int getReserveNum() {
        return reserveNum;
    }

    public void setReserveNum(int reserveNum) {
        this.reserveNum = reserveNum;
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

    public String getLastNoticeTime() {
        return lastNoticeTime;
    }

    public void setLastNoticeTime(String lastNoticeTime) {
        this.lastNoticeTime = lastNoticeTime;
    }

    public String getOldLastNoticeTime() {
        return MyPreference.getInstance().read(USERINO_NOTIFY_OLDNOTICETIME, "");
    }

    public void setOldLastNoticeTime(String time) {
        MyPreference.getInstance().write(USERINO_NOTIFY_OLDNOTICETIME, time);
    }

    public int getNoticeNum() {
        return noticeNum;
    }

    public void setNoticeNum(int noticeNum) {
        this.noticeNum = noticeNum;
    }

    public int getRemindNum() {
        return remindNum;
    }

    public void setRemindNum(int remindNum) {
        this.remindNum = remindNum;
    }

    public boolean isHasNewNotify() {
        return hasNewNotify;
    }

    public void setHasNewNotify(boolean hasNewNotify) {
        this.hasNewNotify = hasNewNotify;
    }

    public int getHasNewDynamic() {
        return hasNewDynamic;
    }

    public void setHasNewDynamic(int hasNewDynamic) {
        this.hasNewDynamic = hasNewDynamic;
    }

    public int getNotReadCollectionNum() {
        return notReadCollectionNum;
    }

    public void setNotReadCollectionNum(int notReadCollectionNum) {
        this.notReadCollectionNum = notReadCollectionNum;
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

    public boolean isAutoBid() {
        return isAutoBid;
    }

    public void setAutoBid(boolean autoBid) {
        isAutoBid = autoBid;
    }

    public void setUserInfoData(final UserLoginResponse loginResponse, String mPhoneNumber) {
        if (null != loginResponse && loginResponse.getUserId() > 0) {
            setLoginState(LOGIN);
            setUserMobile(mPhoneNumber);
            setUserMobileID(mPhoneNumber);
            setUserId(loginResponse.getUserId());
            setAutoBid(TextUtils.equals("Y",loginResponse.getIsAutoBid()));
            saveLoginData(loginResponse, mPhoneNumber);
            if (!TextUtils.isEmpty(loginResponse.getAssigneeName())) {
                AgentBean agentBean = new AgentBean();
                /**服务下发的经纪人没有经纪人编号，这里默认为1*/
                agentBean.setId("1");
                agentBean.setImg(loginResponse.getAssigneePhotoUrl());
                agentBean.setName(loginResponse.getAssigneeName());
                agentBean.setPhone(loginResponse.getAssigneeTel());
                agentBean.setRating(loginResponse.getScore());
                agentBean.setBizType(loginResponse.getBizType());
                setAgent(agentBean);
                setmName(loginResponse.getName());
                setmGender(loginResponse.getGender());
            } else {
                setAgent(new AgentBean());
            }
            setMyUpdate(loginResponse.isMyupdate());
            setAppointNum(loginResponse.getAppointNum());
            setSeekHouseNum(loginResponse.getSeekHouseNum());
        }
    }

    /**
     * 更换手机号后，更新一下登陆时保存的信息
     */
    public void updateUserInfo(UserInfoResponse userInfo) {
        setUserMobile(userInfo.getMobile());
        setUserMobileID(userInfo.getMobile());
        setUserId(userInfo.getUserId());
        setIsTestuser(userInfo.isTestUser);
        MyPreference.getInstance().write(UserInfo.USERINFO_KEY_USER_ID, userInfo.getUserId());
        MyPreference.getInstance().write(UserInfo.USERINFO_KEY_USER_NAME, userInfo.getRealName());
        MyPreference.getInstance().write(UserInfo.USERINFO_KEY_MOBILE, userInfo.getMobile());
        MyPreference.getInstance().write(UserInfo.USERINFO_KEY_MOBILEID, userInfo.getMobile());

        MyPreference.getInstance().write(UserInfo.USER_APP_PUBLIC_KEY, userInfo.getKey1());
        MyPreference.getInstance().write(UserInfo.USER_APP_RSA_CLOSED, userInfo.getRsaClose());

        MyPreference.getInstance().write(UserInfo.IM_USERID, userInfo.getImUserId());
        MyPreference.getInstance().write(UserInfo.IM_PASSWD, userInfo.getImPasswd());

        MyPreference.getInstance().write(UserInfo.ID_CARDNO, userInfo.getIdCardNo());
        MyPreference.getInstance().write(UserInfo.COLLECTION_NUM, userInfo.getCollectionNum());
        MyPreference.getInstance().write(UserInfo.IS_TESTUSER, userInfo.isTestUser);
    }

    private void saveLoginData(UserLoginResponse loginResponse, String mPhoneNumber) {
        MyPreference.getInstance().write(UserInfo.USERINFO_UTICKET, loginResponse.getUticket());
        MyPreference.getInstance().write(UserInfo.USERINFO_KEY_USER_ID, loginResponse.getUserId());
        MyPreference.getInstance().write(UserInfo.USERINFO_KEY_USER_NAME, loginResponse.getName());
        MyPreference.getInstance().write(UserInfo.USERINOF_KEY_UTICKET, loginResponse.getUticket());
        MyPreference.getInstance().write(UserInfo.USERINFO_KEY_MOBILE, mPhoneNumber);
        MyPreference.getInstance().write(UserInfo.USERINFO_KEY_MOBILEID, mPhoneNumber);
        MyPreference.getInstance().write(UserInfo.USERINFO_KEY_USER_GENDER, loginResponse.getGender());
        sUserInfo = new UserInfo();
        AppConfig.uticket = loginResponse.getUticket();

        MyPreference.getInstance().write(UserInfo.USER_APP_PUBLIC_KEY, loginResponse.getKey1());
        MyPreference.getInstance().write(UserInfo.USER_APP_RSA_CLOSED, loginResponse.getRsaClose());
        MyPreference.getInstance().write(AccountInfo.ACCOUNT_OPENED, loginResponse.getIsOpenAccount());

        MyPreference.getInstance().write(UserInfo.IM_USERID, loginResponse.getImUserId());
        MyPreference.getInstance().write(UserInfo.IM_PASSWD, loginResponse.getImPasswd());

        MyPreference.getInstance().write(UserInfo.ID_CARDNO, loginResponse.getIdCardNo());
        MyPreference.getInstance().write(UserInfo.COLLECTION_NUM, loginResponse.getCollectionNum());
        MyPreference.getInstance().write(UserInfo.IS_ALL_DONT_DISTURB, loginResponse.getIsAllDontDisturb());
    }

    public void setUserInfoData(final RreshDataResponse refreDataResponse) {
        setMyUpdate(refreDataResponse.isMyupdate());
        setAppointNum(refreDataResponse.getAppointNum());
        setSeekHouseNum(refreDataResponse.getSeekHouseNum());
        setReserveNum(refreDataResponse.getReserveNum());
        setRecentEnd(refreDataResponse.getRecentEnd());
        setHasNewNotify(refreDataResponse.getHasNewNotify());
        setRemindNum(refreDataResponse.getRemindNum());
        setNoticeNum(refreDataResponse.getNoticeNum());
        setLastNoticeTime(refreDataResponse.getLastNoticeTime());
        setHasNewDynamic(refreDataResponse.getHasNewDynamic());
        setNotReadCollectionNum(refreDataResponse.getNotReadCollectionNum());
        MyPreference.getInstance().write(USERINO_NOTIFY_LASTNOTICETIME, refreDataResponse.getLastNoticeTime());
        EventBus.getDefault().post(new RefreshDataUpgradeEvent());
    }

    public void updatePushNum(NoticeResponse noticeResponse) {
        setHasNewNotify(noticeResponse.isHasNewNotify());
        setNoticeNum(noticeResponse.getNoticeNum());
        setRemindNum(noticeResponse.getRemindNum());
        setHasNewDynamic(noticeResponse.getHasNewDynamic());
        setLastNoticeTime(noticeResponse.getLastNoticeTime());
        setDynamicEstateNum(noticeResponse.getDynamicEstateNum());
        setDynamicHouseNum(noticeResponse.getDynamicHouseNum());
        MyPreference.getInstance().write(USERINO_NOTIFY_LASTNOTICETIME, noticeResponse.getLastNoticeTime());
    }

    public void setUserInfoData(final UserMyResponse userMyResponse) {
        if (null != userMyResponse && userMyResponse.getErrorCode() == 0) {
            if (getLoginState() == LOGIN) {
                if (!TextUtils.isEmpty(userMyResponse.getAssigneeName())) {
                    AgentBean agentBean = new AgentBean();
                    agentBean.setId("");
                    agentBean.setImg(userMyResponse.getAssigneePhotoUrl());
                    agentBean.setName(userMyResponse.getAssigneeName());
                    agentBean.setPhone(userMyResponse.getAssigneeTel());
                    agentBean.setRating(userMyResponse.getScore());
                    agentBean.setBizType(userMyResponse.getBizType());
                    setAgent(agentBean);
                }
                setMyOrderNum(userMyResponse.getOrderNum());
//                setServicePhoneNumber(userMyResponse.getServiceTel());
                rememberData();
            }
        }
    }

    public void loginOut() {
        setLoginState(NOT_LOGIN);
        setAgent(new AgentBean());
        setAppointNum(0);
        setSeekHouseNum(0);
        setRecentEnd(0);
        MyPreference.getInstance().write(IS_TESTUSER, 0);
        MyPreference.getInstance().write(USERINFO_AGENT_ID, "");
        MyPreference.getInstance().write(USERINFO_AGENT_NAME, "");
        MyPreference.getInstance().write(USERINFO_AGENT_PHONE, "");
        MyPreference.getInstance().write(USERINFO_AGENT_IMAG, "");
        MyPreference.getInstance().write(USERINFO_AGENT_SCORE, 0f);
        MyPreference.getInstance().write(USERINFO_AGENT_TYPE, 0);
        MyPreference.getInstance().write(USERINFO_KEY_SERVER_NUMBER, "");

        MyPreference.getInstance().write(UserInfo.USERINFO_KEY_USER_ID, new Long(0));
        MyPreference.getInstance().write(UserInfo.USERINOF_KEY_UTICKET, "");
        MyPreference.getInstance().write(UserInfo.USERINFO_KEY_USER_NAME, "");
        MyPreference.getInstance().write(UserInfo.USERINFO_KEY_USER_GENDER, 0);
        //MyPreference.getInstance().write(UserInfo.USERINFO_KEY_MOBILE, "");
        MyPreference.getInstance().write(UserInfo.USERINFO_KEY_MOBILEID, "");
        AppConfig.uticket = "";
        sUserInfo = new UserInfo();

        MyPreference.getInstance().write(UserInfo.USER_APP_PUBLIC_KEY, "");
        MyPreference.getInstance().write(UserInfo.USER_APP_RSA_CLOSED, 0);

        MyPreference.getInstance().write(UserInfo.IM_USERID, "");
        MyPreference.getInstance().write(UserInfo.IM_PASSWD, "");

        AccountInfo.getInstance().clearAccountInfo();
        /*
        EntrustDraft draft = new EntrustDraft();
        draft.clearAll(true);

        // 清除预约看房里面填写的值
        MyPreference.getInstance().write(CommonTag.NAME_KEY, "");
        MyPreference.getInstance().write(CommonTag.SEX_KEY, -1);
        MyPreference.getInstance().write(CommonTag.APPOINT_DATE_KEY, 0);
        MyPreference.getInstance().write(CommonTag.APPOINT_TIME_KEY, 0);
        MyPreference.getInstance().write(CommonTag.APPOINT_DATE_TIME_STR_KEY, "");
        MyPreference.getInstance().write(FlatAppointSeeHouseActivity.BRAND_APPOINT_PHONE_KEY, "");
        */
    }

    private void rememberData() {
        MyPreference.getInstance().write(USERINFO_AGENT_ID, agent.getId());
        MyPreference.getInstance().write(USERINFO_AGENT_NAME, agent.getName());
        MyPreference.getInstance().write(USERINFO_AGENT_PHONE, agent.getPhone());
        MyPreference.getInstance().write(USERINFO_AGENT_IMAG, agent.getImg());
        MyPreference.getInstance().write(USERINFO_AGENT_SCORE, agent.getRating());
        MyPreference.getInstance().write(USERINFO_AGENT_TYPE, agent.getBizType());
        MyPreference.getInstance().write(USERINFO_KEY_SERVER_NUMBER, SERVICE_PHONE_NUMBER);
    }
}
