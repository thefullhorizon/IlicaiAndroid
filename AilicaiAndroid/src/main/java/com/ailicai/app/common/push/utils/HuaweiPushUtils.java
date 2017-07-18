package com.ailicai.app.common.push.utils;

import android.text.TextUtils;


import com.ailicai.app.common.utils.LogUtil;

import java.lang.reflect.Method;

/**
 * Created by jeme on 2017/5/5.
 */

public class HuaweiPushUtils {

    /**
     * @return 只要返回不是""，则是EMUI版本
     */
    public static String getEmuiVersion() {
        String emuiVerion = "";
        Class<?>[] clsArray = new Class<?>[] { String.class };
        Object[] objArray = new Object[] { "ro.build.version.emui" };
        try {
            Class<?> SystemPropertiesClass = Class
                    .forName("android.os.SystemProperties");
            Method get = SystemPropertiesClass.getDeclaredMethod("get",
                    clsArray);
            String version = (String) get.invoke(SystemPropertiesClass,
                    objArray);
            LogUtil.d( "get EMUI version is:" + version);
            if (!TextUtils.isEmpty(version)) {
                return version;
            }
        } catch (ClassNotFoundException e) {
            LogUtil.e( " getEmuiVersion wrong, ClassNotFoundException");
        } catch (LinkageError e) {
            LogUtil.e( " getEmuiVersion wrong, LinkageError");
        } catch (NoSuchMethodException e) {
            LogUtil.e( " getEmuiVersion wrong, NoSuchMethodException");
        } catch (NullPointerException e) {
            LogUtil.e( " getEmuiVersion wrong, NullPointerException");
        } catch (Exception e) {
            LogUtil.e( " getEmuiVersion wrong");
        }
        return emuiVerion;
    }
}
