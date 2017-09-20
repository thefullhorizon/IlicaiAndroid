package com.ailicai.app.ui.login;

import android.content.Context;

import com.ailicai.app.MyApplication;
import com.ailicai.app.db.UserInfoDBUtil;
import com.ailicai.app.db.UserInfoTableModel;

/**
 * Created by Gerry on 2015/7/3.
 * 为了结构更加清晰，用户模块将逐步梳理
 * 新增加的类
 */
public class UserManager {

    public Context mContext;
    public static UserManager userManager = null;
    public UserInfoDBUtil userInfoDBUtil;

    private UserManager() {

    }

    private UserManager(Context context) {
        this.mContext = context;
        this.userInfoDBUtil = new UserInfoDBUtil(context);
    }

    public static UserManager getInstance(Context context) {
        if (userManager == null) {
            userManager = new UserManager(context);
        }
        return userManager;
    }

    public UserInfoBase getUserByUserId(long userId) {
        return getUserInfoFormDB(userId);
    }

    public void saveUser(UserInfoBase userInfoBase) {
        saveUserInfoToDB(userInfoBase);
    }

    /**
     * 保存用户信息到数据表
     *
     * @param userInfoBase
     */
    private void saveUserInfoToDB(UserInfoBase userInfoBase) {
        if (userInfoBase != null) {
            UserInfoTableModel userInfoModel = new UserInfoTableModel();
            userInfoModel.setUserId(userInfoBase.getUserId());
            userInfoModel.setRealName(userInfoBase.getRealName());
            userInfoModel.setGender(userInfoBase.getGender());
            userInfoModel.setMobile(userInfoBase.getMobile());
            userInfoModel.setIsSetPayPwd(userInfoBase.getIsSetPayPwd());
            userInfoModel.setIsOpenAccount(userInfoBase.getIsOpenAccount());
            userInfoModel.setIsAilicaiAllowUser(userInfoBase.getIsAilicaiAllowUser());
            userInfoModel.setKey1(userInfoBase.getKey1());
            userInfoModel.setKey2(userInfoBase.getKey2());
            userInfoModel.setRsaClose(userInfoBase.getRsaClose());
            userInfoModel.setHasSafeCard(userInfoBase.getHasSafeCard());
            userInfoModel.setBankName(userInfoBase.getBankName());
            userInfoModel.setBankcardTailNo(userInfoBase.getBankcardTailNo());
            userInfoModel.setIsRealNameVerify(userInfoBase.getIsRealNameVerify());
            userInfoModel.setIsBinDebitCard(userInfoBase.getIsBinDebitCard());
            userInfoModel.setImUserId(userInfoBase.getImUserId());
            userInfoModel.setImPasswd(userInfoBase.getImPasswd());
            userInfoModel.setIdCardNo(userInfoBase.getIdCardNo());
            userInfoModel.setCollectionNum(userInfoBase.getCollectionNum());
          //  userInfoModel.isTestUser=userInfoBase.isTestUser;不存数据库
            userInfoModel.setMemberLevel(userInfoBase.getMemberLevel());
            userInfoModel.setIsAutoBid(userInfoBase.getIsAutoBid());
            userInfoDBUtil.insert(userInfoModel);
        }
    }

    /**
     * 从数据表取出用户信息
     *
     * @return
     */
    private UserInfoBase getUserInfoFormDB(long userId) {
        UserInfoBase userInfoBase = new UserInfoBase();
        UserInfoTableModel userModel = userInfoDBUtil.queryUserByMobile(userId);
        if (userModel != null) {
            userInfoBase.setUserId(userModel.getUserId());
            userInfoBase.setRealName(userModel.getRealName());
            userInfoBase.setGender(userModel.getGender());
            userInfoBase.setMobile(userModel.getMobile());
            userInfoBase.setIsSetPayPwd(userModel.getIsSetPayPwd());
            userInfoBase.setIsOpenAccount(userModel.getIsOpenAccount());
            userInfoBase.setIsAilicaiAllowUser(userModel.getIsAilicaiAllowUser());
            userInfoBase.setKey1(userModel.getKey1());
            userInfoBase.setKey2(userModel.getKey2());
            userInfoBase.setRsaClose(userModel.getRsaClose());
            userInfoBase.setHasSafeCard(userModel.getHasSafeCard());
            userInfoBase.setBankName(userModel.getBankName());
            userInfoBase.setBankcardTailNo(userModel.getBankcardTailNo());
            userInfoBase.setIsRealNameVerify(userModel.getIsRealNameVerify());
            userInfoBase.setIsBinDebitCard(userModel.getIsBinDebitCard());
            userInfoBase.setImUserId(userModel.getImUserId());
            userInfoBase.setImPasswd(userModel.getImPasswd());
            userInfoBase.setIdCardNo(userModel.getIdCardNo());
            userInfoBase.setCollectionNum(userModel.getCollectionNum());
            userInfoBase.setMemberLevel(userModel.getMemberLevel());
            userInfoBase.setIsAutoBid(userModel.getIsAutoBid());
        }
        return userInfoBase;
    }

    /**
     * 判断是否设置支付密码 0:否，1:是
     *
     * @return
     */
    public boolean isSetPayPwd() {
        long userId = UserInfo.getInstance().getUserId();
        UserInfoBase infoBase = getUserByUserId(userId);
        int isSetPayPwd = infoBase.getIsSetPayPwd();
        return isSetPayPwd != 0;
    }

    /**
     * 设置支付密码
     */
    public static void setIsSetPayPwd(boolean isSet) {
        long userId = UserInfo.getInstance().getUserId();
        UserInfoBase infoBase = UserManager.getInstance(MyApplication.getInstance()).getUserByUserId(userId);
        infoBase.setHasSafeCard(isSet ? 1 : 0);
        UserManager.getInstance(MyApplication.getInstance()).saveUser(infoBase);
    }

    /**
     * 判断是否吉爱财白名单用户 0:否，1:是
     *
     * @return
     */
    public boolean isAilicaiAllowUser() {
        long userId = UserInfo.getInstance().getUserId();
        UserInfoBase infoBase = getUserByUserId(userId);
        int isAilicaiAllowUser = infoBase.getIsAilicaiAllowUser();
        return isAilicaiAllowUser != 0;
    }

    /**
     * 是否已绑定银行卡  0:否，1:是
     *
     * @return
     */
    public boolean isHasSafeCard() {
        long userId = UserInfo.getInstance().getUserId();
        UserInfoBase infoBase = getUserByUserId(userId);
        int isHasSafeCard = infoBase.getHasSafeCard();
        return isHasSafeCard != 0;
    }

    /**
     * 判断吉爱财是否是否开户 0:否，1:是
     *
     * @return
     */
    public boolean isOpenAccount() {
        long userId = UserInfo.getInstance().getUserId();
        UserInfoBase infoBase = getUserByUserId(userId);
        int isOpenAccount = infoBase.getIsOpenAccount();
        return isOpenAccount != 0;
    }

    public boolean isRealNameVerify() {
        long userId = UserInfo.getInstance().getUserId();
        UserInfoBase infoBase = getUserByUserId(userId);
        int isRealNameVerify = infoBase.getIsRealNameVerify();
        return isRealNameVerify != 0;
    }

    public String getKey1() {
        long userId = UserInfo.getInstance().getUserId();
        UserInfoBase infoBase = getUserByUserId(userId);
        return infoBase.getKey1();
    }

    public String getKey2() {
        long userId = UserInfo.getInstance().getUserId();
        UserInfoBase infoBase = getUserByUserId(userId);
        return infoBase.getKey2();
    }

    public int getRsaClose() {
        long userId = UserInfo.getInstance().getUserId();
        UserInfoBase infoBase = getUserByUserId(userId);
        return infoBase.getRsaClose();
    }

    public UserInfoBase getUserInfoBase() {
        long userId = UserInfo.getInstance().getUserId();
        UserInfoBase infoBase = getUserByUserId(userId);
        return infoBase;
    }
}
