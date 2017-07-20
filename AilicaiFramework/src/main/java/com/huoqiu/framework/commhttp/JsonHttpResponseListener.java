package com.huoqiu.framework.commhttp;


import com.android.volley.VolleyError;
import com.huoqiu.framework.rest.ReportRequest;
import com.huoqiu.framework.util.ConvertUtil;
import com.huoqiu.framework.util.LogUtil;
import com.huoqiu.framework.util.ReflectUtil;

public abstract class JsonHttpResponseListener<T> implements BaseHttpResponseListener {

    private Class<T> resultType;
    private JsonResponsePreProcessor<T> responsePreProcessor;
    public ReportRequest reportRequest = new ReportRequest();     //成功后向服务器提供每个请求的状况

    public JsonHttpResponseListener(Class<T> resultType) {
        this.resultType = resultType;
    }

    private long startTime = 0;

    public long getStartTime() {
        return startTime;
    }


    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public ReportRequest getReportRequest() {
        return reportRequest;
    }

    public JsonHttpResponseListener() {
        this.resultType = (Class) ReflectUtil.getSuperClassGenricType(this.getClass());
    }

    public JsonHttpResponseListener(Class<T> resultType, JsonResponsePreProcessor<T> responsePreProcessor) {
        this.resultType = resultType;
        this.responsePreProcessor = responsePreProcessor;
    }

    public void setResponsePreProcessor(JsonResponsePreProcessor<T> responsePreProcessor) {
        this.responsePreProcessor = responsePreProcessor;
    }

    /**
     * 未经预判的原始String数据
     *
     * @param response
     */
    public void onSuccess(String response) {
        //仅仅用于特殊情况下只需要String的情况
    }

    @Override
    public void onSuccess(byte[] response) {
        if (response == null) {
            LogUtil.i("HttpClient", "Responses String is null");
            return;
        }

        String resultString = ConvertUtil.byte2Str(response, "UTF-8");
        onSuccess(resultString);
        LogUtil.d("HttpClient", resultString);
        try {
            T resultObject = ConvertUtil.json2Obj(resultString, resultType);
            if (responsePreProcessor != null) {
                if (responsePreProcessor.preProcess(resultObject)) {
                    onJsonSuccess(resultObject);
                }
            } else {
                onJsonSuccess(resultObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("HttpClient", "onSuccess(), catch Exception: ");
        }

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        if (responsePreProcessor != null) {
            responsePreProcessor.errorPreprocess(volleyError);
        }
    }

    public abstract void onJsonSuccess(T jsonObject);

    String mTag;

    public Object getTag() {
        return mTag;
    }

    public void setTag(String mTag) {
        this.mTag = mTag;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onFinish() {
    }

    String requrl = "";

    public String getRequrl() {
        return requrl;
    }

    @Override
    public void onGetReqUrl(String requrl) {
        this.requrl = requrl;
    }

    @Override
    public void parseNetworkResponse(BaseRequest request) {

    }
}
