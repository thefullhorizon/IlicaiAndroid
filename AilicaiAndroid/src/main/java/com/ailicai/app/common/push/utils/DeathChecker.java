package com.ailicai.app.common.push.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


/**
 * Created by duo.chen on 2016/4/14.
 * 检测App是否处于打开状态
 */
public class DeathChecker {

    public final static String TAG = "DeathChecker";

    public final static String ISALIVE = "isAlive";

    private static int activityAliveCounts = 0;

    public static void registerLife(Application app) {
        app.registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    static Application.ActivityLifecycleCallbacks lifecycleCallbacks = new Application
            .ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            activityAliveCounts++;
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
               //LogUtil.d("debuglog",activity.getClass().getSimpleName()+"的onActivityResumed");
               //AnalysisModule.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            //AnalysisModule.onPause();
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            activityAliveCounts--;
        }
    };

    private static int getActivityAliveCounts() {
        return activityAliveCounts;
    }

    public static boolean isAlive() {
        return getActivityAliveCounts() > 0;
    }

}
