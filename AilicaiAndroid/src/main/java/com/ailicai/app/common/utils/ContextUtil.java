package com.ailicai.app.common.utils;

import android.content.Context;
import android.content.res.Resources;

import com.ailicai.app.MyApplication;


/**
 * Created by David on 15/8/7.
 */
public class ContextUtil {

    public static Context getApplicationContext() {
        return MyApplication.getInstance().getApplicationContext();
    }

    public static Resources getApplicationResources() {
        return MyApplication.getInstance().getResources();
    }
}
