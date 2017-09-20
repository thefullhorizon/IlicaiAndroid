package com.ailicai.app.common.version;

import android.app.Activity;

import com.ailicai.app.MyApplication;
import com.ailicai.app.common.utils.ToastUtil;
import com.huoqiu.framework.rest.UpdateInfo;

/**
 * Created by David on 15/8/10.
 */
public class VersionUtil {

    private static VersionInterface versionInterface = new VersionInterface() {
        @Override
        public void remindPoint() {

        }

        @Override
        public void checkStart() {

        }

        @Override
        public void checkSuccess() {

        }

        @Override
        public void checkFailed(String message) {

        }

        @Override
        public void checkLatest(String version) {
            ToastUtil.show(MyApplication.getInstance(), "已是最新版本");
        }

        @Override
        public boolean ignorePop() {
            return true;
        }

        @Override
        public void popFailed(){}
    };


    public static void check(VersionInterface _interface, Activity activity) {
        Version version = Version.instance();
        version.setVersionInterface(_interface);
        version.checkUpdate(activity);
    }


    public static void check(VersionInterface _interface, Activity activity, UpdateInfo updateInfo) {
        Version version = Version.instance();
        version.setVersionInterface(_interface);
        version.checkUpdateStrong(activity, updateInfo);
    }


    public static void destroy() {
        Version version = Version.instance();
        version.setVersionInterface(null);
    }

    public static void check(Activity activity) {
        check(versionInterface, activity);
    }
}
