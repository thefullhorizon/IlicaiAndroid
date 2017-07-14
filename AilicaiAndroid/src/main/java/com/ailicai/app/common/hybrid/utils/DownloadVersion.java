package com.ailicai.app.common.hybrid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import java.io.Serializable;

/**
 * Created by duo.chen on 2016/5/18.
 */
public class DownloadVersion {

    public final static String NAME = "hybrid";

    private final static String DOWNINFO = "downinfo";

    private final static String VERSION = "version";

    public static void saveDownloadInfo(Context context, DownInfo downInfo) {
        if (null != context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context
                    .MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(DOWNINFO, JSON.toJSONString(downInfo));
            editor.apply();
        }
    }

    public static DownInfo getSaveDownloadInfo(Context context) {
        DownInfo downInfo = null;
        if (null != context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context
                    .MODE_PRIVATE);
            if (!TextUtils.isEmpty(sharedPreferences.getString(DOWNINFO, ""))) {
                downInfo = JSON.parseObject(sharedPreferences.getString(DOWNINFO, ""),
                        DownInfo.class, Feature.IgnoreNotMatch, Feature.AllowISO8601DateFormat);
            }
        }
        return downInfo;
    }

    public static void deleteSavedDownloadInfo(Context context) {
        if (null != context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context
                    .MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }

    public static void saveOrUpdateDownloadVersion(Context context, String version) {
        if (null != context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context
                    .MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(VERSION, version);
            editor.apply();
        }
    }

    public static String getDownloadVersion(Context context){
        String version = "";
        if (null != context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context
                    .MODE_PRIVATE);
            version = sharedPreferences.getString(VERSION,"");
        }
        return version;
    }

    public static class DownInfo implements Serializable {
        String downloadUrl; //下载地址
        long id;            //下载id
        String md5;         //文件的md5值。下发
        String version;     //版本号
        int mergeOption;    //更新类型

        public DownInfo() {
        }

        public DownInfo(String downloadUrl, String md5, String version, int mergeOption) {
            this.downloadUrl = downloadUrl;
            this.md5 = md5;
            this.version = version;
            this.mergeOption = mergeOption;
        }

        public DownInfo(String downloadUrl, long id, String md5, String version, int mergeOption) {
            this.downloadUrl = downloadUrl;
            this.md5 = md5;
            this.mergeOption = mergeOption;
            this.version = version;
            this.id = id;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public void setMergeOption(int mergeOption) {
            this.mergeOption = mergeOption;
        }

        public long getId() {
            return id;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getMd5() {
            return md5;
        }

        public int getMergeOption() {
            return mergeOption;
        }
    }

}
