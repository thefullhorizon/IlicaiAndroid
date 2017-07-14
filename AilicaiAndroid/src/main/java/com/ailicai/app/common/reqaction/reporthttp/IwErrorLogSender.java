package com.ailicai.app.common.reqaction.reporthttp;

import android.os.Build;

import com.ailicai.app.common.constants.AILICAIBuildConfig;
import com.ailicai.app.common.reqaction.HttpCommonReq;
import com.ailicai.app.common.reqaction.IwjwHttp;
import com.ailicai.app.common.utils.NetCheckUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.huoqiu.framework.app.AppConfig;
import com.huoqiu.framework.commhttp.BaseHttpResponseListener;
import com.huoqiu.framework.commhttp.BaseRequest;
import com.huoqiu.framework.commhttp.JsonHttpResponseListener;
import com.huoqiu.framework.rest.ReportRequest;
import com.huoqiu.framework.util.DateUtil;
import com.huoqiu.framework.util.DeviceUtil;
import com.huoqiu.framework.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 错误数据反馈专用
 * Created by jrvair on 16/1/13.
 */
public class IwErrorLogSender {

    /**
     * 此请求用于服务端对所有接口请求的统计，1/10的概率发送数据
     *
     * @param reportRequest
     */
  /*  public static void sendReport(ReportRequest reportRequest) {
        int randomNum = new Random().nextInt(10);
        //   LogUtil.d(LOGTAG, "统计 reportRequest:" + reportRequest.toString());
        if (randomNum == 1) {
            //  LogUtil.d(LOGTAG, "满足1/10的概率发送统计");
            //     LogUtil.d(LOGTAG, "统计地址是" + ReportRequest.getReportUrl());
            IwjwHttpReq iwjwHttpReq = new IwjwHttpReq(Request.Method.POST, getReportUrl(), reportRequest, new BaseHttpResponseListener() {

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    //        LogUtil.d(LOGTAG, "发送统计失败了");
                }

                @Override
                public void onStart() {
                }

                @Override
                public void onSuccess(byte[] response) {
                    //      LogUtil.d(LOGTAG, "发送统计成功了");
                }

                @Override
                public void onFinish() {
                }

                @Override
                public void onGetReqUrl(String requrl) {

                }
            });
            IwjwHttp.getQueue().add(iwjwHttpReq);
        }
    }*/
    public static void sendReport2(ReportRequest reportRequest) {
        int randomNum = new Random().nextInt(10);
        //   LogUtil.d(LOGTAG, "统计 reportRequest:" + reportRequest.toString());
        if (randomNum == 1) {
            //  LogUtil.d(LOGTAG, "满足1/10的概率发送统计");
            //     LogUtil.d(LOGTAG, "统计地址是" + ReportRequest.getReportUrl());
        }
        HttpCommonReq httpCommonReq = new HttpCommonReq(Request.Method.POST, getReportUrl(), reportRequest, new JsonHttpResponseListener() {
            @Override
            public void onJsonSuccess(Object jsonObject) {

            }
        });
        IwjwHttp.getQueue().add(httpCommonReq);
    }


    /**
     * 发送图片数据
     */
    public static void sendImgDataReport(long distanceTime,String imgUrl) {

        BaseRequest request = new BaseRequest(Request.Method.GET, getImgReportUrl() + "&resptime=" + distanceTime + "&url="+imgUrl, new BaseHttpResponseListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(byte[] response) {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onGetReqUrl(String requrl) {

            }

            @Override
            public void parseNetworkResponse(BaseRequest request) {

            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        IwjwHttp.getQueue().add(request);
    }


    /**
     * 收集请求的错误日志
     * error : 运行期错误；
     * warn : 警告信息；
     * info: 有意义的事件信息，如程序启动，关闭事件，收到请求事件等；
     * debug: 调试信息，可记录详细的业务处理到哪一步了，以及当前的变量状态；
     * trace: 更详细的跟踪信息；
     * errorBaseMap.put("logLevel", "error");
     *
     * @param reportRequest
     */
    public static void sendReqErrorlogObj(Object reportRequest) {
        HashMap hashMap = new HashMap();
        Map<String, Object> reportRequestMap = (Map<String, Object>) JSON.parse(JSON.toJSONString(reportRequest));
        hashMap.putAll(reportRequestMap);
        LogUtil.d(LOGTAG, "收集到请求的错误日志:" + reportRequest.toString());
        sendReqErrorlogMap(hashMap);
    }

    public static void sendReqErrorlogObj(List<Object> reportRequest) {
        List<Map> arrayList = new ArrayList<>();
        for (int mi = 0; mi < reportRequest.size(); mi++) {
            Object reqDataObj = reportRequest.get(mi);
            Map<String, Object> reportRequestMap = (Map<String, Object>) JSON.parse(JSON.toJSONString(reqDataObj));
            initErrorBaseMapData(reportRequestMap);
            arrayList.add(reportRequestMap);
        }
        reqUpErrorArrayData(arrayList);
    }

    /**
     * 收集请求的错误日志
     * error : 运行期错误；
     * warn : 警告信息；
     * info: 有意义的事件信息，如程序启动，关闭事件，收到请求事件等；
     * debug: 调试信息，可记录详细的业务处理到哪一步了，以及当前的变量状态；
     * trace: 更详细的跟踪信息；
     * errorBaseMap.put("logLevel", "error");
     *
     * @param mMapData
     */
    public static void sendReqErrorlogMap(Map<String, Object> mMapData) {
        if (mMapData == null) {
            return;
        }
        initErrorBaseMapData(mMapData);
        reqUpErrorData(mMapData);
    }

    public static void sendReqErrorlogMap(List<Map> mMapDatas) {
        if (mMapDatas == null | mMapDatas.isEmpty()) {
            return;
        }
        ArrayList<Map> arrayList = new ArrayList<>();
        for (int mi = 0; mi < mMapDatas.size(); mi++) {
            Map<String, Object> map = mMapDatas.get(mi);
            initErrorBaseMapData(map);
            arrayList.add(map);
        }
        reqUpErrorArrayData(arrayList);
    }

    private static Map<String, Object> initErrorBaseMapData(Map<String, Object> errorBaseMap) {
        if (errorBaseMap == null) {
            return errorBaseMap;
        }
        errorBaseMap.put("logTime", DateUtil.getCalendarStrBySimpleDateFormat(System.currentTimeMillis(), "yyyy-MM-dd'T'HH:mm:ss"));
        errorBaseMap.put("Time4SevSync", DateUtil.getCalendarStrBySimpleDateFormat(DateUtil.getCurrentTime().getTimeInMillis(), "yyyy-MM-dd'T'HH:mm:ss"));
        if (errorBaseMap.get("logLevel") == null) {
            errorBaseMap.put("logLevel", "error");
        }
        errorBaseMap.put("IMEI", DeviceUtil.getIMEI(AppConfig.application) == null ? "-" : DeviceUtil.getIMEI(AppConfig.application));
        errorBaseMap.put("OS", "android");
        errorBaseMap.put("DeviceModel", DeviceUtil.getDeviceModel());
        errorBaseMap.put("projectName", "IwjwAndroidApp");
        errorBaseMap.put("AppVersion", SystemUtil.getVersion());
        errorBaseMap.put("DevVer", SystemUtil.getAppBuildVersion());
        errorBaseMap.put("NetWorkType", NetCheckUtil.getNetWorkType());
        errorBaseMap.put("AndroidBuildSDK", String.valueOf(Build.VERSION.SDK_INT));// 系统版本号
        return errorBaseMap;
    }

    private static void reqUpErrorData(Map hashMap) {
        if (hashMap == null) {
            return;
        }
        BaseHttpResponseListener baseHttpResponseListener = new BaseHttpResponseListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.d(LOGTAG, "错误Map统计发送失败了");
            }

            @Override
            public void onStart() {
                LogUtil.d(LOGTAG, "开始Map数据发送");
            }

            @Override
            public void onSuccess(byte[] response) {
                LogUtil.d(LOGTAG, "错误Map统计发送成功了");
            }

            @Override
            public void onFinish() {
            }

            @Override
            public void onGetReqUrl(String requrl) {

            }

            @Override
            public void parseNetworkResponse(BaseRequest request) {

            }
        };
        IwjwReportHttpReq iwjwHttpReq = new IwjwReportHttpReq(Request.Method.POST, getReportErrorUrl(), hashMap, baseHttpResponseListener);
        IwjwHttp.getQueue().add(iwjwHttpReq);
    }

    private static void reqUpErrorArrayData(List<Map> mapList) {
        if (mapList == null) {
            return;
        }
        BaseHttpResponseListener baseHttpResponseListener = new BaseHttpResponseListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.d(LOGTAG, "错误List统计发送失败了");
            }

            @Override
            public void onStart() {
                LogUtil.d(LOGTAG, "开始List数据发送");
            }

            @Override
            public void onSuccess(byte[] response) {
                LogUtil.d(LOGTAG, "错误List统计发送成功了");
            }

            @Override
            public void onFinish() {
            }

            @Override
            public void onGetReqUrl(String requrl) {

            }

            @Override
            public void parseNetworkResponse(BaseRequest request) {

            }
        };
        IwjwReportHttpReq iwjwHttpReq = new IwjwReportHttpReq(Request.Method.POST, getReportErrorUrl(), mapList, baseHttpResponseListener);
        IwjwHttp.getQueue().add(iwjwHttpReq);
    }


    /**
     * 请求接口的错误收集地址
     *
     * @return
     */
    private static String getReportErrorUrl() {
        String endUrl = "/dataCollect/errorlog.do";
        if (AILICAIBuildConfig.isProduction()) {
            return "http://plog.iwjw.com" + endUrl;
        } else {
            return "http://collect.iwjwtest.com" + endUrl;
        }
    }

    /**
     * 根据胡宾的说明，修改报告服务器的地址
     * http://120.26.56.158:8080/confluence/pages/viewpage.action?pageId=6488094
     *
     * @return
     */
    private static String getReportUrl() {
        // String endUrl = "/dataCollect/report/appReport.do";
/*        if (Configuration.DEFAULT == Configuration.IWJW_TEST) {
            return Configuration.DEFAULT.protocol + "://192.168.1.14:8112" + endUrl;
        } else if (Configuration.DEFAULT == Configuration.IWJW_BETA) {
            return Configuration.DEFAULT.protocol + "://192.168.1.14:8112" + endUrl;
        } else {
            return releasePreUrl + endUrl;
        }*/

        if (AILICAIBuildConfig.isProduction()) {
            return "http://plog.iwjw.com/dataCollect/servicePush.do?pf=android_reqbase";
        } else {
            //  return "http://192.168.1.86:8112/dataCollect/servicePush.do?pf=android_reqbase";
            return "http://collect.iwjwtest.com/dataCollect/servicePush.do?pf=android_reqbase";
            //return "http://collect.iwjwtest.com" + endUrl;
        }
    }

    private static String getImgReportUrl() {
//        test:http://collect.iwjwtest.com/dataCollect/servicePush.do
//        beta:http://userappbeta.iwjw.com:8112/dataCollect/servicePush.do
//        prod:https://plog.iwjw.com/dataCollect/servicePush.do
        if (AILICAIBuildConfig.isProduction()) {
            return "http://plog.iwjw.com/dataCollect/servicePush.do?pf=android-picfilt";
        } else if(AILICAIBuildConfig.isBeta()){
            return "http://userappbeta.iwjw.com:8112/dataCollect/servicePush.do?pf=android-picfilt";
        } else {
            return "http://collect.iwjwtest.com/dataCollect/servicePush.do?pf=android-picfilt";
        }
    }


    private static String LOGTAG = "IwErrorLogSender";
}
