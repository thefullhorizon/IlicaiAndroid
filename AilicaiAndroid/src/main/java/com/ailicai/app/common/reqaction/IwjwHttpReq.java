package com.ailicai.app.common.reqaction;

import android.text.TextUtils;

import com.ailicai.app.MyApplication;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.setting.DeBugLogActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.volley.AuthFailureError;
import com.huoqiu.framework.app.AppConfig;
import com.huoqiu.framework.commhttp.BaseHttpResponseListener;
import com.huoqiu.framework.commhttp.BaseRequest;
import com.huoqiu.framework.encrypt.Encrypt;
import com.huoqiu.framework.rest.Configuration;
import com.huoqiu.framework.util.LogUtil;
import com.huoqiu.framework.util.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * iwjw定制化的请求对象,仅支持普通类对象的请求参数
 * Created by Jer on 2015/7/17.
 */
public class IwjwHttpReq extends BaseRequest {

    Object paramsObject;//
    private long requestStartTime;

    /**
     * @param method
     * @param url
     * @param paramsObject
     * @param httpResponseListener
     */
    public IwjwHttpReq(int method, String url, Object paramsObject, BaseHttpResponseListener httpResponseListener) {
        super(method, url, httpResponseListener);
        this.paramsObject = paramsObject;
        if (httpResponseListener != null) {
            httpResponseListener.onGetReqUrl(url);
        }
        // 非正式版本，打印服务Log
        LogUtil.i("HttpClient", "-------------------  resquest  Url  --------------");
        LogUtil.i("HttpClient", url);
        LogUtil.i("HttpClient", "------------------- resquest params --------------");
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        String string = JSON.toJSONString(paramsObject);
        return getHeardInfo(string);
    }

/*    private static void showRequestJSON(Map<String, Object> map) {
        if (Configuration.DEFAULT == Configuration.RELEASE) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (String string : map.keySet()) {
            builder.append(string);
            builder.append("=");
            builder.append(map.get(string));
            builder.append(";");
        }
        LogUtil.i("HttpClient", "------------------- RequestJSON --------------");
        LogUtil.i("HttpClient", builder.toString());
        LogUtil.i("HttpClient", "------------------- RequestJSON --------------");
    }*/


    @Override
    public byte[] getBody() throws AuthFailureError {
        requestStartTime = System.currentTimeMillis();
        return JSON.toJSONString(paramsObject).getBytes();
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=" + getParamsEncoding();
    }


    private static HashMap<String, String> getHeardInfo(String paramsJsonStr) {
   /*
       //json转map的B方案
        objectMapper =objectMapper==null? new ObjectMapper():objectMapper;
        Map<String, Object> map;
        byte[] data= paramsJsonStr.getBytes();
        if (data.length > 0) {
            map = objectMapper.readValue(data, Map.class);
        }else{
            data = "".getBytes();
            map = new HashMap<String, Object>(0);
        }
*/
        Map<String, Object> map = (Map<String, Object>) JSON.parse(paramsJsonStr);
        List<Map.Entry<String, Object>> params = new ArrayList<>(map.entrySet());//
        Collections.sort(params, new Comparator<Map.Entry<String, Object>>() {
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                if (o1.getKey() == null || o2.getKey() == null)
                    return 0;
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        String secret = "";
        for (Map.Entry<String, Object> param : params) {
            Object value = param.getValue();
            if (value instanceof JSONArray) {
                value = Arrays.toString(((JSONArray) value).toArray());
            }
            secret = secret + param.getKey() + "=" + value + "&";
        }
        if (secret.endsWith("&")) {
            secret = secret.substring(0, secret.length() - 1);
        }
        LogUtil.i("HttpClient", "------------------- RequestJSON --------------");
        LogUtil.i("HttpClient", secret);
        LogUtil.i("HttpClient", "------------------- RequestJSON --------------");
        String timestamp = String.valueOf((TimeUtil.getCurrentTime(MyApplication.getInstance()).getTimeInMillis() / 100000));


        String md5 = Encrypt.decryptKey(secret, timestamp, MyApplication.getInstance().getPackageName());
        HashMap<String, String> headers = new HashMap<>();
        //     headers.put(appKeyLabel, AppConfig.get_appkey());
        headers.put(appKeyLabel, Configuration.DEFAULT.appKey);
        headers.put(appSecretLabel, md5);
        headers.put(appTime, TimeUtil.getCurrentTime(MyApplication.getInstance()).getTimeInMillis() + "");
        long disTime = TimeUtil.getDisTime(MyApplication.getInstance());
        headers.put("diff_time", disTime + "");
        headers.put("local_time", System.currentTimeMillis() + "");
        /***************************************DEBUG*************************************/
        if (!DeBugLogActivity.isRelease()) {
            DeBugLogActivity.saveDiffTime(disTime);
            if (DeBugLogActivity.getADiffTime() == 0L) {
                DeBugLogActivity.saveADiffTime(disTime);
                DeBugLogActivity.saveLogDiffTime(disTime);
            } else {
                LogUtil.d("HttpClient", "diff_time：" + disTime + "/     ADiffTime：" + DeBugLogActivity.getADiffTime());
                if (disTime != DeBugLogActivity.getADiffTime()) {
                    DeBugLogActivity.saveADiffTime(disTime);
                    DeBugLogActivity.saveLogDiffTime(disTime);
                }
            }
        }
        /****************************************************************************/
        headers.put(appVersion, AppConfig.versionName);
        headers.put(appOS, "android");
        headers.put(appIMEI, DeviceUtil.getIMEI());
        headers.put(appModel, DeviceUtil.getBrandModel());
        headers.put(userId, AppConfig.UID);
        headers.put(u_ticket, AppConfig.u_ticket);
        headers.put(distance, AppConfig.distance);
        headers.put(cityId, AppConfig.cityId);
        headers.put(uticket, AppConfig.uticket);
        headers.put(uuid, DeviceUtil.getIWJWUUID());
        // LogUtil.d("debuglog","UUID:"+;
        if (!TextUtils.isEmpty(AppConfig.channelNo)) {
            headers.put(channel, AppConfig.channelNo);
        }
        headers.put("Connection", "Closed");
/*        LogUtil.i("HttpClient", "-----------  headers --------------");
        LogUtil.i("HttpClient", headers.toString());
        LogUtil.i("HttpClient", "---------------------------------");*/
        return headers;
    }

    public long getRequestStartTime() {
        return requestStartTime;
    }

    /**
     * 报文头需要的字段
     */
    private final static String appTime = "App_Time";// 当前时间
    private final static String appVersion = "ver"; // 手机端版本号
    private final static String appOS = "os"; // 手机来源 (android/ios)
    private final static String appIMEI = "imei"; // 手机唯一标志
    private final static String appModel = "model"; // 手机型号
    private final static String userId = "user_id"; // 已登录用户ID
    private final static String u_ticket = "u_ticket";// 已登录用户的Cookie
    private final static String appKeyLabel = "App-Key"; // key
    private final static String appSecretLabel = "App-Secret"; // secret
    private final static String distance = "distance";
    private final static String cityId = "cityId";
    private final static String channel = "channel";//渠道
    private final static String uticket = "uticket";
    private final static String uuid = "uuid";
    private final static String mqttuuid = "mqttuuid";
}
