package com.ailicai.app.common.reqaction.reporthttp;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.huoqiu.framework.commhttp.BaseHttpResponseListener;
import com.huoqiu.framework.commhttp.BaseRequest;
import com.huoqiu.framework.util.LogUtil;

import java.util.List;
import java.util.Map;

/**
 * 用于错误数据反馈的Requset
 * Created by jrvair on 16/1/13.
 */
public class IwjwReportHttpReq extends BaseRequest {

    Map paramsObject;//

    List<Map> listParams;

    /**
     * @param method
     * @param url
     * @param paramsObject
     * @param httpResponseListener
     */
    public IwjwReportHttpReq(int method, String url, Map paramsObject, BaseHttpResponseListener httpResponseListener) {
        super(method, url, httpResponseListener);
        this.paramsObject = paramsObject;
        // 非正式版本，打印服务Log
        LogUtil.i("ReportHttpClient", "-------------------  resquest  Url  --------------");
        LogUtil.i("ReportHttpClient", url);
        LogUtil.i("ReportHttpClient", "------------------- resquest params --------------");
    }

    public IwjwReportHttpReq(int method, String url, List<Map> listParams, BaseHttpResponseListener httpResponseListener) {
        super(method, url, httpResponseListener);
        this.listParams = listParams;
        // 非正式版本，打印服务Log
        LogUtil.i("ReportHttpClient", "-------------------  resquest  Url  --------------");
        LogUtil.i("ReportHttpClient", url);
        LogUtil.i("ReportHttpClient", "------------------- resquest params --------------");
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        String jsonStr = JSON.toJSONString(paramsObject == null ? listParams : paramsObject);
        LogUtil.d("IwErrorLogSender", "收集的错误数据是：" + jsonStr);
        return jsonStr.getBytes();
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=" + getParamsEncoding();
    }
}
