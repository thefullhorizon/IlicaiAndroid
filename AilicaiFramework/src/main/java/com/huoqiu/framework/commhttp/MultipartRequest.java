package com.huoqiu.framework.commhttp;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhujiang on 2016/3/22.
 */
public class MultipartRequest extends BaseRequest {

    private static final String TAG = "MultipartRequest";

    HttpEntity httpEntity;
    private List<File> mFileParts;
    private String mFilePartName;

    private BaseHttpResponseListener mListener;

    private HashMap<String, String> mParams;
    private HashMap<String, String> headerInfo;


    public void setHeaderInfo(HashMap<String, String> headerInfo) {
        this.headerInfo = headerInfo;
    }

    /**
     * 单个文件
     *
     * @param url
     * @param filePartName
     * @param file
     * @param params
     */
    public MultipartRequest(String url, BaseHttpResponseListener listenerObj, String filePartName, File file,
                            HashMap<String, String> params) {
        super(Method.POST, url, listenerObj);
        this.setRetryPolicy(new DefaultRetryPolicy(20000, 0, 1f));
        mFileParts = new ArrayList<>();
        if (file != null) {
            mFileParts.add(file);
        }
        mFilePartName = filePartName;
        mListener = listenerObj;
        mParams = params;
        buildMultipartEntity();
    }

    /**
     * 多个文件，对应一个key
     *
     * @param url
     * @param filePartName
     * @param files
     * @param params
     */
    public MultipartRequest(String url, BaseHttpResponseListener listenerObj, String filePartName,
                            List<File> files, HashMap<String, String> params) {
        super(Method.POST, url, listenerObj);
        this.setRetryPolicy(new DefaultRetryPolicy(20000, 0, 1f));
        mFilePartName = filePartName;
        mListener = listenerObj;
        mFileParts = files;
        mParams = params;
        buildMultipartEntity();
    }

    private void buildMultipartEntity() {
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();

        // add params
        if (mParams != null && mParams.size() > 0) {
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                entity.addPart(
                        entry.getKey(),
                        new StringBody(entry.getValue(), ContentType.TEXT_PLAIN /*Charset.forName("UTF-8")*/));
            }
        }

        if (mFileParts != null && mFileParts.size() > 0) {
            for (File file : mFileParts) {
                entity.addPart(mFilePartName, new FileBody(file));
            }
            httpEntity = entity.build();
            long l = httpEntity.getContentLength();
            Log.d(TAG, mFileParts.size() + "个，长度：" + l);
        }
    }


    @Override
    public String getBodyContentType() {
        return httpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            httpEntity.writeTo(bos);
        } catch (IOException e) {
            Log.e(TAG, "IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (headerInfo == null || headerInfo.equals(Collections.emptyMap())) {
            headerInfo = new HashMap<>();
        }
        return headerInfo;
    }

}
