package com.ailicai.app.common.reqaction;

import com.alibaba.fastjson.JSON;
import com.huoqiu.framework.commhttp.BaseHttpResponseListener;
import com.huoqiu.framework.commhttp.BaseRequest;
import com.huoqiu.framework.util.LogUtil;

import java.util.Map;

/**
 * http通过getParams做常规请求，支持对象和map
 * Created by Jer on 2016/10/18.
 */
public class HttpCommonReq extends BaseRequest {

    Object paramsObject;
    Map<String, String> paramsMap;

    /**
     * @param method
     * @param url
     * @param paramsObject
     * @param httpResponseListener
     */
    public HttpCommonReq(int method, String url, Object paramsObject, BaseHttpResponseListener httpResponseListener) {
        super(method, url, httpResponseListener);
        this.paramsObject = paramsObject;
        if (httpResponseListener != null) {
            httpResponseListener.onGetReqUrl(url);
        }
        LogUtil.i("HttpClient", "-------------------  resquest  Url  --------------");
        LogUtil.i("HttpClient", url);
        LogUtil.i("HttpClient", "------------------- resquest params --------------");
    }

    public HttpCommonReq(int method, String url, Map<String, String> paramsMap, BaseHttpResponseListener httpResponseListener) {
        super(method, url, httpResponseListener);
        this.paramsMap = paramsMap;
        if (httpResponseListener != null) {
            httpResponseListener.onGetReqUrl(url);
        }
        LogUtil.i("HttpClient", "-------------------  resquest  Url  --------------");
        LogUtil.i("HttpClient", url);
    }

    @Override
    protected Map<String, String> getParams() {
        if (paramsMap == null) {
            LogUtil.i("HttpClient", "------------------- resquest params --------------");
            String string = JSON.toJSONString(paramsObject);
            LogUtil.i("HttpClient", "paramsObject:" + string);
            LogUtil.i("HttpClient", "------------------- resquest params> --------------");

            return (Map<String, String>) JSON.parse(string);
        } else {
            LogUtil.i("HttpClient", "------------------- resquest params --------------");
            String string = paramsMap.keySet().size()+"";
            LogUtil.i("HttpClient", "paramsObject:" + string);
            LogUtil.i("HttpClient", "------------------- resquest params> --------------");

            return paramsMap;
        }
    }

  /*  @Override
    public String getBodyContentType() {
        return "application/json; charset=" + getParamsEncoding();
    }
 */

}
