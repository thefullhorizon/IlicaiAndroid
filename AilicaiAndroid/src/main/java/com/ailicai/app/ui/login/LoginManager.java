package com.ailicai.app.ui.login;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.push.PushUtil;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.GestureLockTools;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.eventbus.LoginEvent;
import com.ailicai.app.eventbus.ShowRefreshNotifEvent;
import com.ailicai.app.eventbus.UserInfoUpdateEvent;
import com.ailicai.app.model.request.FreshMsgDataRequest;
import com.ailicai.app.model.request.UserInfoRequest;
import com.ailicai.app.model.request.account.AccountRequest;
import com.ailicai.app.model.response.RreshDataResponse;
import com.ailicai.app.model.response.UserInfoResponse;
import com.ailicai.app.model.response.UserLoginResponse;
import com.ailicai.app.model.response.account.AccountResponse;
import com.ailicai.app.ui.gesture.GestureLockActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class LoginManager {
    public static final int LOGIN_TAG = 0x4001;

    /**
     * 登录请求发起的来源页面标记位
     */
    public static final String LOGIN_FROM = "login_from";
    /**
     * 登录请求发起的来源页面标记值
     */
    public static final int LOGIN_FROM_MINE = 0x01;
    /**
     * 我的页面
     */
    public static final int LOGIN_FROM_RENT_DETAIL = 0x02;
    /**
     * (租房)房源详情页面
     */
    public static final int LOGIN_FROM_SELL_DETAIL = 0x03;
    /**
     * (二手房)房源详情页面
     */
    public static final int LOGIN_FROM_CHECK = 0x04;
    /**
     * 约看页面
     */
    public static final int LOGIN_FROM_AGENDA = 0x05;/**日程页面*/

    /**
     * 从业主委托登陆
     */
    public static final int LOGIN_FROM_ENTRUST = 0x06;

    /**
     * 房屋管理页
     */
    public static final int LOGIN_FROM_ENTRUST_MANAGER = 0x07;
    /**
     * (租房)房源详情页面 立即看房
     */
    public static final int LOGIN_FROM_RENT_DETAIL_SEE_HOUSE = 0x08;
    /**
     * (二手房)房源详情页面 立即看房
     */
    public static final int LOGIN_FROM_SELL_DETAIL_SEE_HOUSE = 0x09;

    /**
     * 短信跳转到订单详情验证登陆
     */
    public static final int LOGIN_FROM_SMS_GOORDER = 0x10;

    /**
     * 短信跳转房产宝
     */
    public static final int LOGIN_FROM_SMS_FCB = 0x11;

    //二手房地图浮层列表
    public static final int LOGIN_FROM_SALE_OVER_LIST = 0x11;
    //租房地图浮层列表
    public static final int LOGIN_FROM_RENT_OVER_LIST = 0x12;
    /**
     * 短信跳转到红包列表验证登录
     */
    public static final int LOGIN_FROM_SMS_VOUCHER = 0x13;

    /**
     * 房源、小区纠错登录
     */
    public static final int LOGIN_FROM_HOUSE_JC = 0x14;
    public static final int LOGIN_FROM_ESTATE_JC = 0x15;

    // 从爱理财详情立即购买登录
    public static final int LOGIN_FROM_AILICAI = 0x16;

    /**
     * 钱包（查询交易历史和银行卡管理）
     */
    public static final int LOGIN_FROM_DEMANDTREASURE_DIALOG = 0x16;

    /**
     * 扫码登录
     */
    public static final int LOGIN_FROM_QR_SCAN = 0x17;

    /**
     * 短信跳转H5订单详情
     */
    public static final int LOGIN_FROM_SMS_H5ORDERDETAIL = 0x18;

    /**
     * H5 登录
     */
    public static final int LOGIN_FROM_H5_WEB = 0x19;

    /**
     * H5 登录
     */
    public static final int LOGIN_FROM_APPRAISEAGENT = 0x20;

    /**
     * 扫码绑经纪人
     */
    public static final int LOGIN_FROM_SCAN_BIND = 0x21;

    //房源详情关注房源时登录ID
    public static final int LOGIN_FROM_SELL_DETAIL_ATTENTION_HOUSE = 1000;
    public static final int LOGIN_FROM_SELL_DETAIL_ATTENTION_ESTATE = LOGIN_FROM_SELL_DETAIL_ATTENTION_HOUSE + 1;
    //房源详情关小区时登录ID
    public static final int LOGIN_FROM_RENT_DETAIL_ATTENTION_HOUSE = LOGIN_FROM_SELL_DETAIL_ATTENTION_ESTATE + 1;
    public static final int LOGIN_FROM_RENT_DETAIL_ATTENTION_ESTATE = LOGIN_FROM_RENT_DETAIL_ATTENTION_HOUSE + 1;

    //二手房地图浮层 关注小区时登录ID
    public static final int LOGIN_FROM_SELL_MAP_ATTENTION_ESTATE = LOGIN_FROM_RENT_DETAIL_ATTENTION_ESTATE + 1;
    //租房地图浮层 关注小区时登录ID
    public static final int LOGIN_FROM_RENT_MAP_ATTENTION_ESTATE = LOGIN_FROM_SELL_MAP_ATTENTION_ESTATE + 1;

    //房源列表关键词搜索某一具体小区后 ，零少界面中的关注
    public static final int LOGIN_FROM_LIST_ZERO_ATTENTION_ESTATE = LOGIN_FROM_RENT_MAP_ATTENTION_ESTATE + 1;

    //房源详情页咨询弹框中的online
    public static final int LOGIN_FROM_HOUSE_DETAIL_CONSULT_ONLINE = LOGIN_FROM_LIST_ZERO_ATTENTION_ESTATE + 1;
    //房源详情页 未登录 点击视频限制
    public static final int LOGIN_FROM_HOUSE_DETAIL_VIDEO_RESTRICTION = LOGIN_FROM_HOUSE_DETAIL_CONSULT_ONLINE + 1;
    //新房关注登陆
    public static final int LOGIN_FROM_NEW_HOUSE_DETAIL_ATTENTION = LOGIN_FROM_HOUSE_DETAIL_VIDEO_RESTRICTION + 1;
    public static final int LOGIN_FROM_NEW_HOUSE_CALL_ADVISER = LOGIN_FROM_NEW_HOUSE_DETAIL_ATTENTION + 1;
    // 屏蔽骚扰登陆
    public static final int LOGIN_FROM_FORBIDDEN_DISTURB = LOGIN_FROM_NEW_HOUSE_CALL_ADVISER + 1;
    //视频播放器中 未登录 点击视频限制
    public static final int LOGIN_FROM_HOUSE_MEDIA_VIDEO_RESTRICTION = LOGIN_FROM_FORBIDDEN_DISTURB + 1;
    //房源详情页面购物车点击登录
    public static final int LOGIN_FROM_HOUSE_GO_CART_LIST = LOGIN_FROM_HOUSE_MEDIA_VIDEO_RESTRICTION + 1;
    //房源详情房源评价列表IM点击
    public static final int LOGIN_FROM_HOUSE_DETAIL_REVIEW_IM = LOGIN_FROM_HOUSE_GO_CART_LIST + 1;
    //房源评价列表页面IM点击
    public static final int LOGIN_FROM_HOUSE_REVIEW_LIST_IM = LOGIN_FROM_HOUSE_DETAIL_REVIEW_IM + 1;
    // 首页跳转交房租登陆
    public static final int LOGIN_FROM_H5PAY_RENT = LOGIN_FROM_HOUSE_REVIEW_LIST_IM + 1;
    // 视频播放完成点击咨询
    public static final int LOGIN_FROM_HOUSE_VIDEO_CONSULT = LOGIN_FROM_H5PAY_RENT + 1;

    // 视频播放完成点击我要看房
    public static final int LOGIN_FROM_HOUSE_VIDEO_SEE_HOUSE = LOGIN_FROM_HOUSE_VIDEO_CONSULT + 1;

    //新房详情页中 未登录 点击视频限制
    public static final int LOGIN_FROM_NEW_HOUSE_MEDIA_VIDEO_RESTRICTION = LOGIN_FROM_HOUSE_VIDEO_SEE_HOUSE + 1;
    //新房户型详情登陆
    public static final int LOGIN_FROM_NEW_HOUSE_TYPE_DETAIL_CALL_ADVISER = LOGIN_FROM_NEW_HOUSE_MEDIA_VIDEO_RESTRICTION + 1;
    //房源详情房源评价进入经纪人详情时登录
    public static final int LOGIN_FROM_HOUSE_DETAIL_REVIEW_PHOTO = LOGIN_FROM_NEW_HOUSE_TYPE_DETAIL_CALL_ADVISER + 1;
    //房源评价列表
    public static final int LOGIN_FROM_HOUSE_REVIEW_LIST_PHOTO = LOGIN_FROM_HOUSE_DETAIL_REVIEW_PHOTO + 1;
    //带看记录进入经纪人详情
    public static final int LOGIN_FROM_HOUSE_RECORD_LIST_PHOTO = LOGIN_FROM_HOUSE_REVIEW_LIST_PHOTO + 1;

    //热销二手房
    public static final int LOGIN_FROM_HOTSECD_HOUSE = LOGIN_FROM_HOUSE_RECORD_LIST_PHOTO + 1;

    //新房主页
    public static final int LOGIN_FROM_NEW_HOSUE = LOGIN_FROM_HOTSECD_HOUSE + 1;

    //新房户型关注登陆
    public static final int LOGIN_FROM_NEW_HOUSE_TYPE_DETAIL_ATTENTION = LOGIN_FROM_NEW_HOSUE + 1;
    //户型视频次数限制
    public static final int LOGIN_FROM_NEW_HOUSE_TYPE_VIDEO_RESTRICTION = LOGIN_FROM_NEW_HOUSE_TYPE_DETAIL_ATTENTION + 1;

    public static final int LOGIN_FROM_HOUSE_ESTATE_DETAIL_CONSULT_ONLINE = LOGIN_FROM_NEW_HOUSE_TYPE_VIDEO_RESTRICTION + 1;

    public static final int LOGIN_FROM_HOUSE_DETAIL_OPEN_COUPON_DETAIL = LOGIN_FROM_HOUSE_ESTATE_DETAIL_CONSULT_ONLINE + 1;

    public static final String SUCCESS = "success";

    public static LoginLocation loginPageLocation;

    /**
     * @param activity      发起Activity，一般都是mainActivity
     * @param loginFromCode 发起登录请求的页面code，请参照LoginManager中的code定义
     */
    public static void goLogin(Activity activity, int loginFromCode) {

        Map<String, Object> dataMap = ObjectUtil.newHashMap();
        dataMap.put(LoginManager.LOGIN_FROM, loginFromCode);
        goLogin(activity, dataMap);
    }

    public static void goLogin(final Activity activity, final Map<String, Object> dataMap) {
        if (activity != null) {
            try {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyIntent.startActivityForResult(activity, LoginActivity.class, dataMap, LOGIN_TAG);
                        //int enterAnim, int exitAnim
                        activity.overridePendingTransition(R.anim.activity_lollipop_open_enter, R.anim.activity_lollipop_open_exit);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 执行退出登录，仅供loginfragment调用
     *
     * @param context
     */
    public static void loginOut(Context context) {
        UserInfo.getInstance().loginOut();
        //退出登录清除用户的通知。
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
//        EaseUI.getInstance().getNotifier().reset();
        PushUtil.clearAllNofity(context);
        PushUtil.resetMqttService(context);

        LoginEvent loginEvent = new LoginEvent();
        loginEvent.setLoginSuccess(false);
        loginEvent.setFromPageCode(LoginManager.LOGIN_FROM_MINE);
        EventBus.getDefault().post(loginEvent);
        //123 IMHelper.getInstance().logout(false, null);
        MyPreference.getInstance().write("FinanceAdActivity", false);
        MobclickAgent.onProfileSignOff();
        //清除跳过的标志位
        MyPreference.getInstance().remove(GestureLockTools.getJumpLockViewKey());
        MyPreference.getInstance().remove(GestureLockTools.getLockTryTimesKey());
    }

    public static void loginSuccess(Context context, int fromPage, UserLoginResponse jsonObject, boolean showPackage) {
        //登录成功获取用户信息接口
        LoginManager.updateUserInfoData();

        //登陆成功后日志收集
        /*TODO:NEW APP
        EventLog.getInstance().init();
        EventLog.getInstance().upSearchInfo();
        ApplicationPresenter.checkAgentMobile();
         */

        LoginEvent loginEvent = new LoginEvent();
        loginEvent.setContinueNext(!showPackage);
        loginEvent.setLoginSuccess(true);
        loginEvent.setFromPageCode(fromPage);
        loginEvent.setJsonObject(jsonObject);

        MobclickAgent.onProfileSignIn(UserInfo.getInstance().getUserMobile());

        Intent intent = GestureLockTools.getGestureLockIntent(context, GestureLockActivity.TYPE_SETTING);
        if (intent != null) {
            //如果没有设置手势密码，需要拦截
            intent.putExtra("loginEvent", loginEvent);
            context.startActivity(intent);
        } else {
            EventBus.getDefault().post(loginEvent);

            //新用户登录弹出大礼包
            if (showPackage) {
                Intent cardIntent = new Intent(context, LoginSuccessCardDialog.class);
                cardIntent.putExtra(LoginSuccessCardDialog.CARD_DATA, jsonObject);
                context.startActivity(cardIntent);
            }
        }

        //房源详情关注、咨询、我要看房
        if (fromPage != LoginManager.LOGIN_FROM_SELL_DETAIL_ATTENTION_HOUSE
                && fromPage != LoginManager.LOGIN_FROM_RENT_DETAIL_ATTENTION_HOUSE
                && fromPage != LoginManager.LOGIN_FROM_HOUSE_ESTATE_DETAIL_CONSULT_ONLINE
                && fromPage != LoginManager.LOGIN_FROM_NEW_HOUSE_DETAIL_ATTENTION
                && fromPage != LoginManager.LOGIN_FROM_NEW_HOUSE_CALL_ADVISER
                && fromPage != LoginManager.LOGIN_FROM_SELL_DETAIL
                && fromPage != LoginManager.LOGIN_FROM_RENT_DETAIL
                && fromPage != LoginManager.LOGIN_FROM_HOUSE_DETAIL_CONSULT_ONLINE
                && fromPage != LOGIN_FROM_AILICAI
                && fromPage != LoginManager.LOGIN_FROM_H5PAY_RENT
                && fromPage != LoginManager.LOGIN_FROM_NEW_HOSUE
                && fromPage != LoginManager.LOGIN_FROM_HOTSECD_HOUSE
                && fromPage != -1
                && LoginManager.loginPageLocation != null
                && !LoginManager.loginPageLocation.isLastLoginInThisPage(LoginManager.LoginLocation.MESSAGE)
                && !LoginManager.loginPageLocation.isLastLoginInThisPage(LoginManager.LoginLocation.INDEXATTENTION)
                && !LoginManager.loginPageLocation.isLastLoginInThisPage(LoginLocation.MY)
                && fromPage != LoginManager.LOGIN_FROM_MINE) {
            //123 EventLog.upEventLog("2017050801", "z", "");
        }
    }

    public static void loginCancel() {
        //处理登陆过程中返回或者关闭
        LoginEvent loginEvent = new LoginEvent();
        loginEvent.setCancelLogin(true);
        EventBus.getDefault().post(loginEvent);
    }

    /**
     * 请求47、根据用户ID获取用户基本信息
     */
    public static void updateUserInfoData() {
        final Context mContext = MyApplication.getInstance().getApplicationContext();
        UserInfoRequest request = new UserInfoRequest();
        request.setUserId((int) UserInfo.getInstance().getUserId());
        ServiceSender.exec(mContext, request, new IwjwRespListener<UserInfoResponse>() {

            @Override
            public void onJsonSuccess(UserInfoResponse jsonObject) {
                saveUserInfo(jsonObject);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                ToastUtil.showInCenter(errorInfo);
            }
        });

        ServiceSender.exec(mContext, new AccountRequest(), new IwjwRespListener<AccountResponse>() {
            @Override
            public void onJsonSuccess(AccountResponse response) {
                AccountInfo.getInstance().saveAccountInfo(response);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                ToastUtil.showInCenter(errorInfo);
            }
        });
        //      refreshGlobalData();
    }

    public static void refreshGlobalData() {
        final Context mContext = MyApplication.getInstance().getApplicationContext();
        FreshMsgDataRequest request = new FreshMsgDataRequest();
        request.setUserId(UserInfo.getInstance().getUserId());
        ServiceSender.exec(mContext, request, new IwjwRespListener<RreshDataResponse>() {
            @Override
            public void onJsonSuccess(RreshDataResponse jsonObject) {
                UserInfo.getInstance().setUserInfoData(jsonObject);
                ShowRefreshNotifEvent showRefreshNotifEvent = new ShowRefreshNotifEvent();
                showRefreshNotifEvent.setRreshDataResponse(jsonObject);
                EventBus.getDefault().post(showRefreshNotifEvent);
            }
        });

    }

    /**
     * 获取用户信息接口返回后保存用户信息
     *
     * @param userInfo
     */
    public static void saveUserInfo(UserInfoResponse userInfo) {
        UserInfoBase infoBase = new UserInfoBase();
        infoBase.setUserId(userInfo.getUserId());
        infoBase.setGender(userInfo.getGender());
        infoBase.setRealName(userInfo.getRealName());
        infoBase.setMobile(userInfo.getMobile());
        infoBase.setIsOpenAccount(userInfo.getIsOpenAccount());
        infoBase.setIsSetPayPwd(userInfo.getIsSetPayPwd());
        infoBase.setIsAilicaiAllowUser(userInfo.getIsAilicaiAllowUser());
        infoBase.setKey1(userInfo.getKey1());
        infoBase.setKey2(userInfo.getKey2());
        infoBase.setRsaClose(userInfo.getRsaClose());
        infoBase.setHasSafeCard(userInfo.getHasSafeCard());
        infoBase.setBankName(userInfo.getBankName());
        infoBase.setBankcardTailNo(userInfo.getBankcardTailNo());
        infoBase.setIsRealNameVerify(userInfo.getIsRealNameVerify());
        infoBase.setIsBinDebitCard(userInfo.getIsBinDebitCard());
        infoBase.setIdCardNo(userInfo.getIdCardNo());
        infoBase.setCollectionNum(userInfo.getCollectionNum());
        infoBase.isTestUser = userInfo.isTestUser;
        String imName = userInfo.getImUserId();
        String pwd = userInfo.getImPasswd();
        infoBase.setImUserId(imName);
        infoBase.setImPasswd(pwd);
        //loginIM(imName, pwd);
        UserManager.getInstance(MyApplication.getInstance()).saveUser(infoBase);
        UserInfo.getInstance().updateUserInfo(userInfo);

        UserInfoUpdateEvent event = new UserInfoUpdateEvent();
        EventBus.getDefault().post(event);
    }

    /*
    public static void loginIM(final String username, final String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            LogUtil.d("LoginIM username or password is null!");
            return;
        }
        LogUtil.d("LoginIM username: " + username + "password: " + password);
        if (IMHelper.getInstance().isLoggedIn()) {
            //App异常退出时候，有可能环信并未登出。切换appid再次登录，可能导致app用户变了，环信用户还是旧的
            String currentLoginedUser = IMHelper.getInstance().getCurrentUsernName();
            if (!currentLoginedUser.equalsIgnoreCase(username)) {
                EMChatManager.getInstance().logout(false, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        doIMLogin(username, password);
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String error) {
                    }
                });
            } else {
                return;
            }
        }
        doIMLogin(username, password);
    }
    */

    /*
    private static void doIMLogin(final String username, String password) {
        final Context mContext = MyApplication.getInstance().getApplicationContext();
        EMChatManager.getInstance().login(username, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMChat.getInstance().setAutoLogin(true);
                // 登陆成功，保存用户名
                IMHelper.getInstance().setCurrentUserName(username);
                // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                // ** manually load all local groups and
                EMChatManager.getInstance().loadAllConversations();
                EventBus.getDefault().post(new IMLoginEvent());
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                LogUtil.e(mContext.getString(R.string.Login_failed) + message);
                //上报埋点
                EventLog.upEventLog("701", message);
                ReportRequest mReportRequest = new ReportRequest();
                mReportRequest.setErrorMessage("code :" + code + " " + message);
                IwErrorLogSender.sendReqErrorlogObj(mReportRequest);
            }
        });
    }
    */

    public enum LoginLocation {
        //        我的  my
//        消息   message
//        首页  homepage
//        房源详情页  house_detial
//        小区详情页 estate_detail
//        爱理财详情页 alc_detail
//        地图页  map
//        列表页  list

        // 只要记住MY  MESSAGE  HOMEPAGE INDEXATTENTION  这四个是首页tab切换的时候去记录覆盖，加一个HOUSEDETAIL就行，因为关注里面没登录的时候可以直接进入房源详情，不然在房源详情里面登录还记着INDEXATTENTION，然后INDEXATTENTION就会上报
        MY("my"),
        MESSAGE("message"),
        HOMEPAGE("homepage"),
        INDEXATTENTION("index_attention"),
        HOUSEDETAIL("house_detial"),
        ESTATESDETAIL("estate_detail"),
        ALCDETAIL("alc_detail"),
        MAP("map"),
        LIST("list");

        String locationStr;

        LoginLocation(String locationStr) {
            this.locationStr = locationStr;
        }

        public boolean isLastLoginInThisPage(LoginLocation loginLocation) {
            if (loginLocation != null) {
                return this.locationStr.equals(loginLocation.locationStr);
            }
            return false;
        }
    }

    public enum LoginAction {
        /**
         * 卡券
         */
        ACTION_INDEX_CARD_COUPONST(0),

        /**
         * 交易记录
         */
        ACTION_INDEX_TRANSACTION_LIST(1),

        /**
         * 预约记录
         */
        ACTION_INDEX_RESERVERECORD_LIST(2),

        /**
         * 邀请奖励
         */
        ACTION_INDEX_REWARDS_LIST(3),

        /**
         * 网贷类
         */
        ACTION_INDEX_WANG_DAI_CLICK(4),

        /**
         * 活期宝
         */
        ACTION_INDEX_HUOQIBAO_CLICK(5),


        ACTION_INDEX_NORMAL(-1);

        private int actionIndex;

        LoginAction(int actionIndex) {
            this.actionIndex = actionIndex;
        }

        public int getActionIndex() {
            return actionIndex;
        }

        public boolean isActionIndex(int actionIndex) {
            return this.actionIndex == actionIndex;
        }
    }

}
