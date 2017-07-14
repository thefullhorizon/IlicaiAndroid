package com.ailicai.app.common.reqaction;



import com.ailicai.app.common.constants.AILICAIBuildConfig;
import com.ailicai.app.common.reqaction.ssl.RequestQueueFactory;
import com.ailicai.app.common.utils.MD5;
import com.ailicai.app.model.request.Request;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.huoqiu.framework.commhttp.JsonHttpResponseListener;
import com.huoqiu.framework.commhttp.MultipartRequest;
import com.huoqiu.framework.rest.Configuration;
import com.huoqiu.framework.rest.ImgUploadResponse2;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.util.TimeUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by David on 15/7/22.
 */
public class IwjwHttp {

    public final static String CENTER_URL = "/ihouse";
    public static String ROOT_URL = Configuration.DEFAULT.protocol + "://" + Configuration.DEFAULT.hostname + ":" + Configuration.DEFAULT.port;// 生产环境
    private static RequestQueue mQueue;
    private static IwjwHttp instance;
    private boolean shouldCache;
    private String path;
    private int method = com.android.volley.Request.Method.POST;

    public static synchronized IwjwHttp instance(String path) {
        if (instance == null) {
            instance = new IwjwHttp();
        }
        instance.setPath(path);
        return instance;
    }

    public static synchronized RequestQueue getQueue() {
        mQueue = mQueue == null ? RequestQueueFactory.newRequestQueue() : mQueue;
        return mQueue;
    }

    public static void onDestroy() {
        mQueue.stop();
        mQueue = null;
    }

    private static String reqUrlForRest(String path) {
        if (path.contains("http://")) {
            return path;
        }
        return ROOT_URL + CENTER_URL + path;
    }

    public static void cancel(Object tag) {
        getQueue().cancelAll(tag);
    }

    public void configRequest(HttpConfig config) {

    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setShouldCache(boolean shouldCache) {
        this.shouldCache = shouldCache;
    }

    public void exec(Request param, JsonHttpResponseListener<? extends Response> listener) {
        String url = reqUrlForRest(path);
        IwjwHttpReq fastRequest = new IwjwHttpReq(method, url, param, listener);
        fastRequest.setShouldCache(shouldCache);
        fastRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //  fastRequest.setRetryPolicy(new DefaultRetryPolicy());
        if (listener.getTag() != null) fastRequest.setTag(listener.getTag());
        listener.getReportRequest().setRestUrl(url.substring(Configuration.DEFAULT.getDomain().length()));
        listener.onStart();
        listener.setStartTime(System.currentTimeMillis());
        getQueue().add(fastRequest);
    }

    public void exec(Map param, JsonHttpResponseListener listener) {
        String url = reqUrlForRest(path);
        IwjwHttpReq fastRequest = new IwjwHttpReq(method, url, param, listener);
        fastRequest.setShouldCache(shouldCache);
        fastRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //  fastRequest.setRetryPolicy(new DefaultRetryPolicy());
        if (listener.getTag() != null) fastRequest.setTag(listener.getTag());
        listener.getReportRequest().setRestUrl(url.substring(Configuration.DEFAULT.getDomain().length()));
        listener.onStart();
        listener.setStartTime(System.currentTimeMillis());
        getQueue().add(fastRequest);
    }

    /**
     * 同步请求方法
     * @param param
     * @return
     */
    public JSONObject execSync(Map param) {
        String url = reqUrlForRest(path);
        final IwjwHttpReq fastRequest = new IwjwHttpReq(method, url, param, null);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(param), future, future){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return fastRequest.getHeaders();
            }
        };
        getQueue().add(request);
        JSONObject response = null;

        try {
            response = future.get(); // this will block
        } catch (InterruptedException e) {
            // exception handling
        } catch (ExecutionException e) {
            // exception handling
        }
        return response;
    }


    public static void uploadImg(File imgFile, JsonHttpResponseListener<ImgUploadResponse2> listener) {
        String url;
        if (AILICAIBuildConfig.isProduction()) {
            url = "http://img.superjia.com/uploadSingleFileEncryOriginal.action";
        } else if (AILICAIBuildConfig.isBeta()) {
            url = "http://beta.imgsoa.com:8133/uploadSingleFileEncryOriginal.action";
        } else {
            url = "http://192.168.1.44:1097/uploadSingleFileEncryOriginal.action";
        }
/*
        String downloadPath = Environment.getExternalStorageDirectory() + "/Pictures";
        File imgFile = new File(downloadPath, "ttt.jpg");*/
        byte[] fileBytes = getBytesFromFile(imgFile.getAbsolutePath());
        StringBuilder length = new StringBuilder(String.valueOf(fileBytes.length));
        final long time = TimeUtil.getCurrentTimeExact().getTimeInMillis();
        StringBuilder secret = length.append("&").append(String.valueOf(time / 100000));
        String appSecret = MD5.getMessageDigest(secret.toString().getBytes());
        final String bizType = "BRADN_APARTMENT_IMAGE_CONTRACT";
        final String CONTENT_TYPE = "JPEG";
        HashMap<String, String> requestParam = new HashMap<>();
        requestParam.put("App-Secret", appSecret + "");
        requestParam.put("App-Time", time + "");
        requestParam.put("bizType", bizType + "");
        requestParam.put("contentType", CONTENT_TYPE + "");
        requestParam.put("length", fileBytes.length + "");
        MultipartRequest request = new MultipartRequest(url, listener, "file", imgFile, requestParam);
        listener.onStart();
        getQueue().add(request);
    }

    /**
     * 读取文件的字节流
     *
     * @param filePath String
     * @return byte[]
     */
    public static byte[] getBytesFromFile(String filePath) {
        File file = new File(filePath);
        byte[] ret = null;
        try {
            if (file == null) {
                return null;
            }
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
            ret = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

}