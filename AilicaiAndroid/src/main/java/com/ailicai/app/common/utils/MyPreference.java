package com.ailicai.app.common.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;


import com.ailicai.app.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Set;

/**
 * Preference包装类，可将数据存储于SharedPreferences中
 */
public class MyPreference {
    private static MyPreference myPreference = null;
    private String packName = "IWJW";
    private Context mContext;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    /**
     * 是否是事务模式，在事务模式下，提交的修改都不会写入文件
     */
    private volatile boolean isTransactionMode = false;

    private MyPreference(Context context) {
        mContext = context;
        packName = context.getPackageName();
        settings = mContext.getSharedPreferences(packName, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public static MyPreference getInstance() {
        if (myPreference == null) {
            myPreference = new MyPreference(MyApplication.getInstance());
        }
        return myPreference;
    }

    /**
     * 开启一个存储事务，开启后修改操作都不会写入文件，必须通过{@link #commitTransaction()}提交修改
     */
    public void beginTransaction() {
        synchronized (this) {
            isTransactionMode = true;
        }
    }

    /**
     * 提交存储事务
     */
    public void commitTransaction() {
        synchronized (this) {
            isTransactionMode = false;
        }
        editor.commit();
    }

    private boolean commit() {
        synchronized (this) {
            if (!isTransactionMode) {
                return  editor.commit();
            }
        }
        return false;
    }

    /**
     * 读取数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String read(String key, String defaultValue) {
        return settings.getString(key, defaultValue);
    }

    /**
     * 读取数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean read(String key, boolean defaultValue) {
        return settings.getBoolean(key, defaultValue);
    }

    /**
     * 读取数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public float read(String key, float defaultValue) {
        return settings.getFloat(key, defaultValue);
    }

    /**
     * 读取数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public int read(String key, int defaultValue) {
        return settings.getInt(key, defaultValue);
    }

    /**
     * 读取数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public long read(String key, long defaultValue) {
        return settings.getLong(key, defaultValue);
    }


    public Set<String> read(String key, Set<String> defaultValue) {
        if (!EnvironmentInfo.SdkUtil.hasHoneycomb()) {
            return ObjectUtil.newHashSet();
        }
        return settings.getStringSet(key, defaultValue);
    }

    public <T extends Serializable> T read(Class<T> clazz) {
        // 以类名作为键名
        String keyName = clazz.getSimpleName();
        return read(keyName, clazz);
    }

    /**
     * 写入数据
     *
     * @param key
     * @param value
     */
    public void write(String key, String value) {
        if (key == null) return;

        editor.putString(key, value);
        commit();
    }

    /**
     * 写入数据
     *
     * @param key
     * @param value
     */
    public void write(String key, boolean value) {
        if (key == null) return;

        editor.putBoolean(key, value);
        commit();
    }

    /**
     * 写入数据
     *
     * @param key
     * @param value
     */
    public void write(String key, float value) {
        if (key == null) return;

        editor.putFloat(key, value);
        commit();
    }

    /**
     * 写入数据
     *
     * @param key
     * @param value
     */
    public void write(String key, int value) {
        if (key == null) return;

        editor.putInt(key, value);
        commit();
    }

    /**
     * 写入数据
     *
     * @param key
     * @param value
     */
    public void write(String key, long value) {
        if (key == null) return;

        editor.putLong(key, value);
        commit();
    }

    /**
     * 写入数据
     *
     * @param key
     * @param value
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void write(String key, Set<String> value) {
        if (key == null) return;

        if (!EnvironmentInfo.SdkUtil.hasHoneycomb()) {
            commit();
            return;
        }
        editor.putStringSet(key, value);
        commit();
    }

    //根据对象名保存对象数据
    public <T extends Serializable>  boolean write(T t) {
        String keyName = t.getClass().getSimpleName();
        return writeObjectByName(keyName, t);
    }

    //根据指定的键名保存对象数据
    public <T extends Serializable> boolean writeObjectByName(String key, T t) {
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(t);
            // 将字节流编码成base64的字符窜
            String base64FromObject = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            // 以类名作为键名
            editor.putString(key, base64FromObject);
            return commit();
        } catch (IOException e) {
            return false;
        }
    }

    public <T extends Serializable> T read(String key, Class<T> clazz) {
        T t = null;
        String productBase64 = settings.getString(key, "");

        if (TextUtils.isEmpty(productBase64)) {
            return null;
        }

        //读取字节
        byte[] base64 = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);
        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                t = (T) bis.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    public void remove(String key) {
        if (key == null) return;

        editor.remove(key);
        commit();
    }

}