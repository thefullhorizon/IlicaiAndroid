package com.ailicai.app;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.ailicai.app.common.constants.AILICAIBuildConfig;
import com.ailicai.app.common.constants.CommonTag;
import com.ailicai.app.common.push.PushUtil;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.share.ShareUtil;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.HtmlUrlRequest;
import com.ailicai.app.model.request.ServerTimeRequest;
import com.ailicai.app.model.response.Iwjwh5UrlResponse;
import com.ailicai.app.model.response.TimeResponse;
import com.ailicai.app.setting.DeBugLogActivity;
import com.ailicai.app.setting.ServerIPManger;
import com.ailicai.app.setting.ServerIPModel;
import com.ailicai.app.ui.html5.SupportUrl;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.android.volley.VolleyError;
import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.AppConfig;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Configuration;
import com.huoqiu.framework.util.ConvertUtil;
import com.huoqiu.framework.util.TimeUtil;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.common.SocializeConstants;

/**
 * Created by Jer on 2016/8/2.
 */

public class ApplicationPresenter {

    //热晕TrackIO
    //安卓：APPKEY：90263f2940828d77ed70a034d17110ba、TOKEN：F6CDC6C0DE57692937BCC02080CBF3A5
    private static final String REYUN_IW_USER_APPKEY = "90263f2940828d77ed70a034d17110ba";
    private static final String REYUN_IW_USER_TOKEN = "F6CDC6C0DE57692937BCC02080CBF3A5";
    //Talking Data
    //App ID：E273855C7A2647F2873F0EBB1E376406
    private static final String TALKING_DATA_IW_USER_APPKEY = "E273855C7A2647F2873F0EBB1E376406";
    public static boolean isAgentMobile = false;
    private static ApplicationPresenter appPresenter;
    MyApplication myApplication;
    /*********************************************************************/
    boolean isUpDialog = false;//升级弹窗
    boolean isIndexAdDialog = false;//广告弹窗
    boolean isChangeCityDialog = false;//选择城市弹窗
    boolean appraiseDialogCanDialog = false;//经纪人评价弹窗

    private ApplicationPresenter(MyApplication myApplication) {
        this.myApplication = myApplication;
        initModule();
    }

    public static ApplicationPresenter getInstance(MyApplication myApplication) {
        return appPresenter = appPresenter == null ? new ApplicationPresenter(myApplication) : appPresenter;
    }

    public static String getChannelNo() {
        ApplicationInfo appInfo;
        try {
            appInfo = MyApplication.getInstance().getPackageManager().getApplicationInfo(MyApplication.getInstance().getPackageName(), PackageManager.GET_META_DATA);
            String channelNo = appInfo.metaData.getString("UMENG_CHANNEL");
            if (TextUtils.isEmpty(channelNo)) {
                channelNo = "unknown";
            }
            return channelNo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    private static void initMIUIInfo() {
        String vString = android.os.Build.MANUFACTURER;
        if ("Xiaomi".equals(vString)) {// 是小米设备
            AppConfig.miuiV = CommonUtil.getSystemProperty("ro.miui.ui.version.name");
            AppConfig.CAN_SET_MIUIBAR = CommonUtil.canSetMIUIbar();
        }
    }

    /**
     * 初始化图片加载器
     *
     * @param context
     */
    private static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)// 设置线程的优先级
                .denyCacheImageMultipleSizesInMemory()// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())// 设置缓存文件的名字
                .diskCacheFileCount(30)// 缓存文件的最大个数
                .tasksProcessingOrder(QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序
                .build();
        ImageLoader.getInstance().init(config);
        /**图片加载器的log*/
        if (!AILICAIBuildConfig.isProduction()) {
            L.writeDebugLogs(false);
            L.writeLogs(false);
        } else {
            L.writeDebugLogs(true);
            L.writeLogs(true);
        }
    }

    public static void clearUserInfo() {
        if (TextUtils.isEmpty(UserInfo.getInstance().getUticket())) {
            UserInfo.getInstance().loginOut();
        }
    }

    public static void updatePhoneInfo(Context mActivity) {
//        MobilePropertyRequest request = new MobilePropertyRequest();
//        request.setOsType("android");// 系统
//        request.setUserId(UserInfo.getInstance().getUserId());
//        request.setOsVersion(String.valueOf(Build.VERSION.SDK_INT));// 系统版本号
//        request.setNetType(NetCheckUtil.getNetWorkType());
//        TelephonyManager telephonyManager = (TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE);
//        request.setImei(telephonyManager.getDeviceId());
//        //  request.setPhoneNumber(telephonyManager.getLine1Number());
//        request.setBrand(Build.MANUFACTURER);
//        request.setUuid(DeviceUtil.getIWJWUUID());
//        request.setModel(Build.MODEL);
//        String s = telephonyManager.getSimOperator();
//        if ("46000".equals(s) || "46002".equals(s) || "46007".equals(s)) {
//            request.setSupport("移动");
//        } else if ("46001".equals(s)) {
//            request.setSupport("联通");
//        } else if ("46003".equals(s)) {
//            request.setSupport("电信");
//        } else {
//            request.setSupport("");
//        }
//        LogUtil.d("debuglog", "updatePhoneInfo");
//        ServiceSender.exec(mActivity, request, new IwjwRespListener<Response>() {
//            @Override
//            public void onJsonSuccess(Response response) {
//            }
//
//            @Override
//            public void onFailInfo(String errorInfo) {
//            }
//        });
    }

    /**
     * 同步服务器时间
     *
     * @param
     */
    public static void syncTime(final IwjwRespListener iwjwRespListener) {
        ServerTimeRequest request = new ServerTimeRequest();
        ServiceSender.exec(MyApplication.getInstance(), request, new IwjwRespListener<TimeResponse>() {

            @Override
            public void onSuccess(byte[] response) {
                String resultString = ConvertUtil.byte2Str(response, "UTF-8");
                try {
                    TimeResponse resultObject = ConvertUtil.json2Obj(resultString, TimeResponse.class);
                    int errorCode = resultObject.getErrorCode();
                    if (errorCode == RestException.NO_ERROR) {
                        if (resultObject.getSysDate() != null) {
                            TimeUtil.calTime(MyApplication.getInstance(), resultObject.getSysDate());
                            DeBugLogActivity.saveDiffTime(TimeUtil.getDisTime(MyApplication.getInstance()));
                            DeBugLogActivity.saveOtheLog("同步到时间差:" + TimeUtil.getDisTime(MyApplication.getInstance()));
                            if (null != iwjwRespListener) {
                                iwjwRespListener.onJsonSuccess(response);
                                return;
                            }
                        } else {
                            DeBugLogActivity.saveOtheLog("同步失败:" + resultString);
                        }
                    } else {
                        LogUtil.e("iwjw", "同步失败");
                        DeBugLogActivity.saveOtheLog("同步失败:" + resultString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onJsonSuccess(TimeResponse jsonObject) {
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                volleyError.printStackTrace();
                DeBugLogActivity.saveOtheLog("同步请求失败");
                if (null != iwjwRespListener) {
                    iwjwRespListener.onFailInfo("");
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {

            }


            @Override
            public void onFinish() {
                super.onFinish();
                if (null != iwjwRespListener) {
                    iwjwRespListener.onFinish();
                }
            }
        });
    }

    /**
     * 登出操作
     *
     * @param activity
     * @param msg
     */
    public static void appLogout(Activity activity, String msg) {
        if (UserInfo.isLogin()) {
            //重复登录等相关错误信息提示语
            ToastUtil.showInCenter(msg);
        }
        if (UserInfo.isLogin()) {
            LoginManager.loginOut(activity);
            LoginManager.goLogin(activity, LoginManager.LOGIN_FROM_MINE);
        }
    }

//    /**
//     * 根据adPopId标记弹窗已经打开过了
//     *
//     * @param adPopId
//     */
//    public static void doneOpenScreenRequest(long adPopId) {
//        CountOpenScreenRequest request = new CountOpenScreenRequest();
//        request.setActicityId(adPopId);
//        ServiceSender.exec(MyApplication.getInstance(), request, new IwjwRespListener<Response>() {
//            @Override
//            public void onJsonSuccess(Response jsonObject) {
//                LogUtil.d("debuglog", "doneOpenScreenRequest:" + jsonObject.toString());
//            }
//
//            @Override
//            public void onFailInfo(String errorInfo) {
//            }
//        });
//    }

    public void initModule() {
        long aaa = System.currentTimeMillis();
        /**初始化AppStatus参数*/
        initAppStatus();
        /**初始化屏幕参数*/
        DeviceUtil.initScreenParams(myApplication.getResources());
        initServerIP();
        initImageLoader(myApplication);
        initMqtt();
        initTime();
        htmlUrlUpdate();
//        initBaiduMap();
//        initEngineManager(myApplication);
//        initIM();
        initUmeng();
        initBugly();
        //Register GrowingIO
//        registerGrowingIO();
        //热晕SDK
        initReYunTrackingIOSDK();
        //Talking Data
        initTalkingDataSDK();
        //initOneAPM();
        long bb = System.currentTimeMillis() - aaa;
        LogUtil.d("debuglog", "initModule:" + bb);

    }

    public void onExitApp() {
        //TrackingIO.exitSdk();
    }

//    public void registerGrowingIO() {
//        String channelName = "iwjw";
//        try {
//            ApplicationInfo appInfo = myApplication.getPackageManager().getApplicationInfo(myApplication.getPackageName(), PackageManager.GET_META_DATA);
//            channelName = appInfo.metaData.getString("UMENG_CHANNEL");
//            LogUtil.d("=======channelName==========" + channelName);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        GrowingIO configuration = GrowingIO.startWithConfiguration(myApplication, new com.growingio.android.sdk.collection.Configuration()
//                .useID()
//                .trackAllFragments()
//                .setChannel(channelName));
//        if (UserInfo.getInstance().isLogin()) {
//            configuration.setCS1("userid", UserInfo.getInstance().getUserId() + "").setCS2("mobile", UserInfo.getInstance().getUserMobile());
//        }
//        configuration.setCS3("city", CityManager.getInstance().getCurrentCity().getCityName());
//    }

    /**
     * 安卓：APPKEY：90263f2940828d77ed70a034d17110ba、TOKEN：F6CDC6C0DE57692937BCC02080CBF3A5
     */
    public void initReYunTrackingIOSDK() {
//        if (IWBuildConfig.isProduction()) {
//            String channelName = "iwjw";
//            try {
//                ApplicationInfo appInfo = myApplication.getPackageManager().getApplicationInfo(myApplication.getPackageName(), PackageManager.GET_META_DATA);
//                channelName = appInfo.metaData.getString("UMENG_CHANNEL");
//                LogUtil.d("=======channelName=====ReYun=====" + channelName);
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//            TrackingIO.initWithKeyAndChannelId(myApplication, REYUN_IW_USER_APPKEY, channelName);
//            TrackingIO.setRegisterWithAccountID(channelName);
//        }
    }

    public void initTalkingDataSDK() {

//        if (AILICAIBuildConfig.isProduction()) {
//            String channelName = "iwjw";
//            try {
//                ApplicationInfo appInfo = myApplication.getPackageManager().getApplicationInfo(myApplication.getPackageName(), PackageManager.GET_META_DATA);
//                channelName = appInfo.metaData.getString("UMENG_CHANNEL");
//                LogUtil.d("=======channelName===TalkingData=======" + channelName);
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//            TalkingDataAppCpa.init(myApplication, TALKING_DATA_IW_USER_APPKEY, channelName);
//            //关闭TalkingData的相关日志输出
//            //TalkingDataAppCpa.setVerboseLogDisable();
//        }
    }

    void initBugly() {
//        if (IWBuildConfig.isProduction()) {
//            // 获取当前包名
//            String packageName = myApplication.getPackageName();
//            // 获取当前进程名
//            String processName = SystemUtil.getProcessName(android.os.Process.myPid());
//            // 设置是否为上报进程
//            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(myApplication);
//            strategy.setUploadProcess(processName == null || processName.equals(packageName));
//
//            /**
//             * debug is true
//             * release is false
//             */
//            CrashReport.initCrashReport(myApplication.getApplicationContext(), "e401948380", true, strategy);
//        }
    }

    /**
     * oneAPM集成
     */
    /*
    private void initOneAPM() {
        if (!IWBuildConfig.isDebug()) {
            String oneAPMkey;
            if (IWBuildConfig.isProduction()) {
                oneAPMkey = "A5796EAF4D6FB040A44F52EE8F5E5F6638";
            } else {
                oneAPMkey = "25C0CBA27E37E2972E433072ED47106135";
            }

            if (!TextUtils.isEmpty(UserInfo.getInstance().getUserMobile())) {
                HashMap<String, String> extraData = new HashMap<>();
                String userTel = UserInfo.getInstance().getUserMobile();
                extraData.put("tel", userTel);
                ContextConfig config = new ContextConfig();
                String searchValue = userTel;
                config.setSearchValue(searchValue); // 设置一个搜索值
                config.setExtra(extraData);
                OneApmAgent.init(myApplication).setContextConfig(config).setToken(oneAPMkey).start();

            } else {
                OneApmAgent.init(myApplication).setToken(oneAPMkey).start();
            }
            PerformanceConfiguration.getInstance().setEnableFps(true);
        }
    }
    */
    private void initUmeng() {
        ShareUtil.initWXConfig();//初始化微信相关ID
        /**初始化友盟账号，BETA环境与生产环境分开统计，测试账号jervisshe@superjia.com*/
        SocializeConstants.APPKEY = AILICAIBuildConfig.isProduction() ? "5968a0394544cb6d3b001d73" : "5968a0394544cb6d3b001d73";
        ManyiAnalysis.getInstance().initSelf(myApplication, SocializeConstants.APPKEY, getChannelNo());
        MobclickAgent.setDebugMode(AILICAIBuildConfig.isDebug());
    }


    private void initMqtt() {
        try {
            PushUtil.startMqttService(myApplication);
        } catch (Exception e) {
            LogUtil.i(e.toString());
        }
    }

    public void initTime() {
        syncTime(new IwjwRespListener() {
            @Override
            public void onJsonSuccess(Object jsonObject) {
            }

            @Override
            public void onFinish() {
                super.onFinish();
                myApplication.runOnNotUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updatePhoneInfo(myApplication);
                    }
                });
                if (UserInfo.isLogin()) {
                    LoginManager.updateUserInfoData();
                }
            }
        });
    }

    private void htmlUrlUpdate() {
        HtmlUrlRequest request = new HtmlUrlRequest();
        ServiceSender.exec(MyApplication.getInstance(), request, new IwjwRespListener<Iwjwh5UrlResponse>() {
            @Override
            public void onJsonSuccess(Iwjwh5UrlResponse jsonObject) {
                SupportUrl.saveUrls(jsonObject);
            }
        });
    }

    /**
     * 初始化AppStatus参数
     */
    private void initAppStatus() {
        MyApplication.getInstance().runOnNotUiThread(new Runnable() {
            @Override
            public void run() {
                AppConfig.platform = "IWJW";
                /**初始化城市ID信息到Header*/
//                AppConfig.cityId = CityManager.getInstance().getCurrentCity().getProvinceId() + "";
                AppConfig.uticket = UserInfo.getInstance().getUticket();
                /** 初始化版本信息*/
                try {
                    PackageInfo pi = myApplication.getPackageManager().getPackageInfo(myApplication.getPackageName(), 0);
                    AppConfig.versionCode = pi.versionCode;
                    AppConfig.versionName = pi.versionName;
                    AppConfig.channelNo = getChannelNo();
                    //ToastUtil.show(this,"渠道号为"+channelNo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                initMIUIInfo();
            }
        });
        clearUserInfo();
    }

    /**
     * 初始化服务器地址
     */
    private void initServerIP() {
        /**根据版本不同，设置不同的服务器地址**/
        if (AILICAIBuildConfig.isProduction()) {
            /**生产版本*/
            ServerIPModel serverIPModel = ServerIPManger.GetServerIP();
            if (null != serverIPModel) {
                Configuration.IWJW_RELEASE.protocol = serverIPModel.mServerProtocol;
                Configuration.IWJW_RELEASE.hostname = serverIPModel.mServerHostname;
                Configuration.IWJW_RELEASE.port = serverIPModel.mServerPort;
                Configuration.IWJW_RELEASE.path = serverIPModel.mServerPath;
                Configuration.DEFAULT = Configuration.IWJW_RELEASE;
                Configuration.DEFAULTIMG = Configuration.IWJW_RELEASE_IMG;
            } else {
                ToastUtil.show(myApplication, "程序初始化服务器地址时失败，请重启应用！");
            }
        } else if (AILICAIBuildConfig.isBeta()) {
            /**Beta版本*/
            String ip = MyPreference.getInstance().read(CommonTag.SERVER_IP, "");
            if (!TextUtils.isEmpty(ip)) {
                Configuration.IWJW_BETA.hostname = ip;
            }
            int port = MyPreference.getInstance().read(CommonTag.SERVER_PORT, 0);

            if (port != 0) {
                Configuration.IWJW_BETA.port = port;
            }
            Configuration.IWJW_BETA.path = "";
            Configuration.DEFAULT = Configuration.IWJW_BETA;
            Configuration.DEFAULTIMG = Configuration.IWJW_BETA_IMG;

        } else {
            /**不是生产版本 也不是beta版本，就是测试版本(包括开发和调试版本)*/
            String ip = MyPreference.getInstance().read(CommonTag.SERVER_IP, "");
            if (!TextUtils.isEmpty(ip)) {
                Configuration.IWJW_TEST.hostname = ip;
            }
            int port = MyPreference.getInstance().read(CommonTag.SERVER_PORT, 0);

            if (port != 0) {
                Configuration.IWJW_TEST.port = port;
            }
            Configuration.IWJW_TEST.path = "";
            Configuration.DEFAULT = Configuration.IWJW_TEST;
            Configuration.DEFAULTIMG = Configuration.IWJW_TEST_IMG;
        }
    }

    public void setUpDialog(boolean upDialog) {
        isUpDialog = upDialog;
    }

    public void setIndexAdDialog(boolean indexAdDialog) {
        isIndexAdDialog = indexAdDialog;
    }

    public void setAppraiseDialogCanDialog(boolean appraiseDialogCanDialog) {
        this.appraiseDialogCanDialog = appraiseDialogCanDialog;
    }

    public void inibussDialogState() {
        isUpDialog = false;
        isIndexAdDialog = false;
        isChangeCityDialog = false;
    }


    /*********************************************************************/

    public boolean weUpDialogCanShow() {
        isUpDialog = true;
        return isUpDialog;
    }

    public boolean weIndexAdCanShow() {
        if (isUpDialog) {
            return false;
        }
        if (isChangeCityDialog) {
            return false;
        }
        isIndexAdDialog = true;
        return isIndexAdDialog;
    }

    public boolean weChangeCityCanShow() {
        if (isUpDialog) {
            return false;
        }
        isChangeCityDialog = true;
        return isChangeCityDialog;
    }

    public boolean appraiseDialogCanShow() {
        if (isUpDialog) {
            return false;
        }
        if (isChangeCityDialog) {
            return false;
        }
        if (isIndexAdDialog) {
            return false;
        }
        appraiseDialogCanDialog = true;
        return appraiseDialogCanDialog;
    }
}
