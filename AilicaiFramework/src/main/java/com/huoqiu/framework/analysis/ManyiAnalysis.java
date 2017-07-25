package com.huoqiu.framework.analysis;

import android.content.Context;

import com.huoqiu.framework.rest.Configuration;
import com.huoqiu.framework.util.LogUtil;
import com.umeng.analytics.MobclickAgent;

public class ManyiAnalysis {
    private Context mContext;

    private static ManyiAnalysis self = null;

    public static ManyiAnalysis getInstance() {
        if (null == self) {
            synchronized (ManyiAnalysis.class) {
                if (null == self) {
                    self = new ManyiAnalysis();
                }
            }

        }
        return self;
    }

  /*  public static void init(Context context) {
        MobclickAgent.setDebugMode(false);
        //SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        //然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setCatchUncaughtExceptions(false);
        MobclickAgent.updateOnlineConfig(context);
    }

    public static void init(Context context, String key) {
        init(context);
        AnalyticsConfig.setAppkey(key);
    }*/

    public static void onEvent(Context context, String eventId) {
        if (Configuration.DEFAULT == Configuration.IWJW_BETA) {
            return;
        }
        MobclickAgent.onEvent(context, eventId);
    }

    public static void onPageStart(String page) {
        if (Configuration.DEFAULT == Configuration.IWJW_BETA) {
            return;
        }
        MobclickAgent.onPageStart(page);
    }

    public static void onPageEnd(String page) {
        if (Configuration.DEFAULT == Configuration.IWJW_BETA) {
            return;
        }
        MobclickAgent.onPageEnd(page);
    }

    public static void onResume(Context context) {
        if (Configuration.DEFAULT == Configuration.IWJW_BETA) {
            return;
        }
        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context) {
        if (Configuration.DEFAULT == Configuration.IWJW_BETA) {
            return;
        }
        MobclickAgent.onPause(context);
    }

    public static void reportError(Context context, Throwable e) {
        if (Configuration.DEFAULT == Configuration.IWJW_BETA) {
            return;
        }
        MobclickAgent.reportError(context, e);
        MobclickAgent.onKillProcess(context);
    }


    public void initSelf(Context context, String key, String channelNo) {
        this.mContext = context;
        MobclickAgent.UMAnalyticsConfig umAnalyticsConfig = new MobclickAgent.UMAnalyticsConfig(context, key, channelNo, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.enableEncrypt(true);//6.0.0版本及以后
        MobclickAgent.setCatchUncaughtExceptions(false);
        MobclickAgent.startWithConfigure(umAnalyticsConfig);
  /*      //SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        //然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setCatchUncaughtExceptions(false);
        MobclickAgent.updateOnlineConfig(context);
        AnalyticsConfig.setAppkey(key);*/
        LogUtil.e("==============>",key);
        LogUtil.e("==============>",channelNo);
    }

    /**
     * 不做服务ip判断，所有的事件都要统计 by Ted
     */
    public void onEvent(String eventId) {
        if (null == mContext) return;
        MobclickAgent.onEvent(mContext, eventId);
    }

    public void onResume() {
        if (null == mContext) return;
        MobclickAgent.onResume(mContext);
    }

    public void onPause() {
        if (null == mContext) return;
        MobclickAgent.onPause(mContext);
    }

    public void reportError(Throwable e) {
        if (null == mContext) return;
        MobclickAgent.reportError(mContext, e);
        MobclickAgent.onKillProcess(mContext);
    }
}