package com.ailicai.app.common.utils;

import android.net.UrlQuerySanitizer;

/**
 * Created by duo.chen on 2016/7/19.19:12
 */

public class UrlDecoder {

    public final static String TAG = "UrlDecoder";
    /**
     * @param url 需要处理的url
     * @param key key
     */
    public static String parseValue(String url, String key){
        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer();
        sanitizer.setAllowUnregisteredParamaters(true);
        sanitizer.setUnregisteredParameterValueSanitizer(UrlQuerySanitizer.getAllButNulLegal());
        try {
            sanitizer.parseUrl(url);
        } catch (Exception e) {
            LogUtil.i(TAG,e.toString());
        }

        return getValue(sanitizer,key);
    }

    public static String getValue(UrlQuerySanitizer sanitizer,String key){
        String value = "";
        try {
            if (sanitizer.getValue(key) != null) {
                value = new String(sanitizer.getValue(key).getBytes("iso8859-1"),"UTF-8");
                LogUtil.i(TAG,"getValue key "+ key + " value " + value);
            }
        } catch (Exception e) {
            LogUtil.i(TAG,e.toString());
        }
        return value;
    }

}
