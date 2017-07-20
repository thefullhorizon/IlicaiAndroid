package com.ailicai.app.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.ailicai.app.common.constants.IWBuildConfig;

import java.util.List;


/**
 * Created by duo.chen on 2015/6/17.
 */
public class MapApi {

    /**
     * 底部静态地图url前缀
     */
    private final static String baiduPlaceBaseDetailUrlV2 = "http://api.map.baidu.com/place/v2/detail?";
    private final static String baiduPlaceBaseSearchUrlV2 = "http://api.map.baidu.com/place/v2/search?";
    private final static String baiduUrlV2 = "http://api.map.baidu.com/staticimage/v2?";
    private final static String baiduUrl = "http://api.map.baidu.com/staticimage?";
    private final static String baiDuPanoramaBaseUrl = "http://api.map.baidu.com/panorama/v2?";
    private final static String debugMCode = "0F:09:5F:89:DF:3E:E3:71:A4:37:3A:42:14:B2:FB:FC:8D:04:33:BA;com.manyi.lovehouse";
    private final static String releaseMCode = "2E:37:01:E8:D3:8D:87:EA:DB:7A:62:3B:74:C3:9E:EB:3D:42:CC:E1;com.manyi.lovehouse";

    /**
     * 拼接获取百度地图静态图的url
     * 以下参数取值范围根据baidu staticimage API
     * 参数名 必选 默认值   描述
     * width  否   400    图片宽度。取值范围：(0, 1024]。
     * height 否   300    图片高度。取值范围：(0, 1024]。
     * zoom   否   11     地图级别。高清图范围[3, 18]；低清图范围[3,19]
     */
    public static String getMapImgUrl(int widht, int height, int zoom, double lon, double lat) {

        /**
         * 当width超出了Api的width取值范围 并且宽大于高。将width 取值为最大值1024 并将height按比例缩放
         * 以下同理
         */

        if (widht > 1024 && widht > height) {
            widht = 1024;
            height = height * 1024 / widht;
            zoom = 17;
        } else if (height > 1024 && height > widht) {
            height = 1024;
            widht = widht * 1024 / height;
            zoom = 17;
        }
        Log.i("baiduUrl=============", baiduUrl + "center=" + lon + "," + lat + "&width=" + widht + "&height=" + height + "&zoom=" + zoom);
        return baiduUrl + "center=" + lon + "," + lat + "&width=" + widht + "&height=" + height + "&zoom=" + zoom;
    }

    /**
     * 拼接获取百度地图静态图并标注的url
     */
    public static String getMapImgUrlForMarkers(Context context, int widht, int height, int zoom, double lon, double lat, List<Double> lonArray, List<Double> latArray, List<String> markerStyles) {
        if (widht > 1024 && widht > height) {
            widht = 1024;
            height = height * 1024 / widht;
            //zoom = 17;
        } else if (height > 1024 && height > widht) {
            height = 1024;
            widht = widht * 1024 / height;
            //zoom = 17;
        }

        String mCode;
        if (IWBuildConfig.isProduction()) {
            //线上
            mCode = releaseMCode;
        } else {
            //线下
            mCode = debugMCode;
        }

        String baiduStaticImageUrl = baiduUrlV2 + "ak=" + getAK(context) + "&mcode=" + mCode + "&center=" + lon + "," + lat
                + "&width=" + widht + "&height=" + height + "&zoom=" + zoom
                + getMarkers(lonArray, latArray) + getMarkerStyles(markerStyles);
        Log.i("baiduUrl=============", baiduStaticImageUrl);
        return baiduStaticImageUrl;
    }

    /**
     * 获取百度地图的静态全景url
     */
    public static String getMapPanoramaImgUrl(Context context, int width, int height, double lon, double lat) {
        if (width > 1024) {
            width = 1024;
        }
        if (height > 1024) {
            height = 1024;
        }
        String mCode;
        if (IWBuildConfig.isProduction()) {
            //线上
            mCode = releaseMCode;
        } else {
            //线下
            mCode = debugMCode;
        }
        String baiDuPanoramaUrl = baiDuPanoramaBaseUrl + "ak=" + getAK(context) + "&width=" + width + "&height=" + height + "&location=" + lon + "," + lat + "&fov=180" + "&mcode=" + mCode;
        LogUtil.d("lyn", "baiDuPanoramaUrl=" + baiDuPanoramaUrl);
        return baiDuPanoramaUrl;
    }


    //&markers=116.483694,39.870268|116.536586,39.92518|116.529687,39.868496|116.4377,39.871154|116.37101,39.875584
    public static String getMarkers(List<Double> lonArray, List<Double> latArray) {
        StringBuilder stringBuilder = new StringBuilder("&markers=");
        for (int i = 0; i < lonArray.size(); i++) {
            if (i == lonArray.size() - 1) {
                stringBuilder = stringBuilder.append(lonArray.get(i) + "," + latArray.get(i));
            } else {
                stringBuilder = stringBuilder.append(lonArray.get(i) + "," + latArray.get(i) + "|");
            }
        }
        return stringBuilder.toString();
    }

    // &markerStyles=-1,http://files.iwjw.com/resource/userapp/icon/gongyu_location_1.png|
    // -1,http://files.iwjw.com/resource/userapp/icon/gongyu_location_2.png|
    // -1,http://files.iwjw.com/resource/userapp/icon/gongyu_location_3.png
    public static String getMarkerStyles(List<String> markerStyles) {
        StringBuilder stringBuilder = new StringBuilder("&markerStyles=");
        for (int i = 0; i < markerStyles.size(); i++) {
            if (i == markerStyles.size() - 1) {
                stringBuilder = stringBuilder.append("-1," + markerStyles.get(i));
            } else {
                stringBuilder = stringBuilder.append("-1," + markerStyles.get(i) + "|");
            }
        }
        return stringBuilder.toString();
    }

    public static String getAK(Context context) {
        PackageManager pm = context.getPackageManager();
        String ak = "";
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            ak = appInfo.metaData.getString("com.baidu.lbsapi.API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return ak;
    }

}
