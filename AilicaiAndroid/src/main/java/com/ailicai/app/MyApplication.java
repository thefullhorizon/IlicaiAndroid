package com.ailicai.app;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.multidex.MultiDexApplication;

import com.ailicai.app.common.constants.AILICAIBuildConfig;
import com.ailicai.app.common.constants.GlobleConstants;
import com.ailicai.app.common.push.utils.DeathChecker;
import com.ailicai.app.common.utils.SystemUtil;
import com.danikula.videocache.HttpProxyCacheServer;
import com.huoqiu.framework.app.AppConfig;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.socialize.utils.Log;

public class MyApplication extends MultiDexApplication {
    private final static String fontPath = "fonts/iconfont.ttf";
    private final static String NUM_FONTPATH = "fonts/din_medium.ttf";
    private final static String DIN_BOLDALTERNATE = "fonts/din_boldalternate.ttf";
    private final static String VIP_NUMBER_REGULAR = "fonts/iwlicai_vip_number_regular.ttf";
    public static ApplicationPresenter appPresenter;
    private static MyApplication application = null;
    private final Handler uiHandler = new Handler();
    /*
        public String newHouseUrl;
    */
    private Typeface iconfont;
    private Typeface dinMediumFont;
    private Typeface vipNumberFont;
    /*
        public void setNewHouseUrl(String newHouseUrl) {
            this.newHouseUrl = newHouseUrl;
        }*/
    private int currentNoticeNums;
    private boolean openWx = false;
    private HttpProxyCacheServer proxy;
    private HandlerThread notUIHandlerThread;
    private Handler notUiHandler;
    private long mLockTime;
    public boolean mInFront = false;//是否在前台

    public static MyApplication getInstance() {
        return application;
    }

    public static Resources getAppResources() {
        return application.getResources();
    }

    public static HttpProxyCacheServer getProxy() {
        MyApplication app = MyApplication.getInstance();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    public static ApplicationPresenter getAppPresenter() {
        return ApplicationPresenter.getInstance(application);
    }

    public static void inibussDialogState() {
        if(application != null && ApplicationPresenter.getInstance(application) != null) {
            ApplicationPresenter.getInstance(application).inibussDialogState();
        }
    }

    public int getCurrentNoticeNums() {
        return currentNoticeNums;
    }

    public void setCurrentNoticeNums(int currentNoticeNums) {
        this.currentNoticeNums = currentNoticeNums;
    }

    public boolean isOpenWx() {
        return openWx;
    }

    public void setOpenWx(boolean openWx) {
        this.openWx = openWx;
    }

    public Typeface getIconfont() {
        if (iconfont == null) {
            iconfont = Typeface.createFromAsset(getInstance().getAssets(), fontPath);
        }
        return iconfont;
    }

    public Typeface getDinMediumFont() {
        if (dinMediumFont == null) {
            dinMediumFont = Typeface.createFromAsset(getInstance().getAssets(), NUM_FONTPATH);
        }
        return dinMediumFont;
    }

    public Typeface getVipNumberFont() {
        if (vipNumberFont == null) {
            vipNumberFont = Typeface.createFromAsset(getInstance().getAssets(), VIP_NUMBER_REGULAR);
        }
        return vipNumberFont;
    }

    public Typeface getDinBoldAlternate() {
        if (dinMediumFont == null) {
            dinMediumFont = Typeface.createFromAsset(getInstance().getAssets(), DIN_BOLDALTERNATE);
        }
        return dinMediumFont;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(application).maxCacheFilesCount(50).build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (getPackageName().equalsIgnoreCase(SystemUtil.getCurProcessName(this))) {
            DeathChecker.registerLife(this);
            application = this;
            AppConfig.application = getInstance();
            appPresenter = ApplicationPresenter.getInstance(application);
            if (AILICAIBuildConfig.isDebug()) {
                 LeakCanary.install(this);
//                  BlockCanary.install(this, new AppBlockCanaryContext()).start();
            }
        }
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        //按返回键退出app或者按home键到桌面，或者菜单键切换app都会调用
        if(level == TRIM_MEMORY_UI_HIDDEN){
            appPresenter.setAppBackground();
        }
    }

    public Handler getUiHandler() {
        return uiHandler;
    }

    private Handler getNotUiHandler() {
        if (notUiHandler == null) {
            if (notUIHandlerThread == null) {
                notUIHandlerThread = new HandlerThread("NotMainThread");
                notUIHandlerThread.start();
            }
            notUiHandler = new Handler(notUIHandlerThread.getLooper());
        }
        return notUiHandler;
    }

    /**
     * 运行在一个非UI线程的Handler中
     *
     * @param action
     */
    public final void runOnNotUiThread(Runnable action) {
        getNotUiHandler().post(action);
    }

    /**
     * 运行在主线程
     *
     * @param action
     */
    public final void runOnUiThread(Runnable action) {
        uiHandler.post(action);
    }


}