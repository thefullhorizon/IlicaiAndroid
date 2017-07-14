package com.huoqiu.framework.util;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.util.AbstractMap;
import java.util.List;

/**
 * Created by Jer on 2015/7/16.
 */
public class ConvertUtil {

    public static String byte2Str(byte[] bytes, String encode) {
        try {
            String str = new String(bytes, encode);
            return str;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String byte2Str(byte[] bytes) {
        return byte2Str(bytes, "UTF-8");
    }

    public static String hashMap2JSON(AbstractMap<String,String> bytes) {
        return JSON.toJSONString(bytes);
    }


    public static <T> T json2Obj(String jsonString, Class<T> targetClass)  throws Exception{
        T obj = JSON.parseObject(jsonString, targetClass);
        return obj;
    }


    public static <T> List<T> json2Array(String jsonString, Class<T> targetClass) {
        List<T> objs = JSON.parseArray(jsonString, targetClass);
        return objs;
    }

    public static <T> String array2Json(List<T> targetList) {
        return JSON.toJSONString(targetList);
    }


    public static String obj2Json(Object object) {
        return JSON.toJSONString(object);
    }

}
