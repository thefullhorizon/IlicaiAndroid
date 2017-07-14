package com.huoqiu.framework.rest;

import java.io.Serializable;
import java.util.Observable;

/**
 * Created by duo.chen on 2015/12/3.
 */
public class ImgUploadResponse2 extends Observable implements Serializable {
    public int status;
    public UploadResponseData data;

    //   147 {"status":1,"data":
    // {"key":"bradnapartmentimagecontract/2017/2/13/7ed9451791e84b598c6808f1182e9c6d",
    // "original_big":"http://house-test.oss-cn-hangzhou.aliyuncs.com/bradnapartmentimagecontract/2017/2/13/7ed9451791e84b598c6808f1182e9c6d?Expires=1486985770&OSSAccessKeyId=2e8LhKbYeIse3Cw8&Signature=XXgjGKdPdktMsku8/n30EDKxl7E%3D"}}
    // 0
    public class UploadResponseData {

        public String key;
        public String original_big;

    }
}
