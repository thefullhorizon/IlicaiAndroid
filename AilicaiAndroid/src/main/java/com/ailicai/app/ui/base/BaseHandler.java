package com.ailicai.app.ui.base;

import android.os.Handler;
import android.os.Message;

import com.huoqiu.framework.util.LogUtil;

import java.lang.ref.WeakReference;

/**
 * Created by Jer on 2016/8/24.
 */

public abstract class BaseHandler<T> extends Handler {

    private WeakReference<T> mWeakReference;

    public BaseHandler(T t) {
        super();
        mWeakReference = new WeakReference<>(t);
    }

    @Override
    public void handleMessage(Message msg) {
        //doSomething();
        T t = mWeakReference.get();
        if (t != null) {
            LogUtil.d("debuglog","t不是空的");
            handleMessage(msg, t);
        }else{
            LogUtil.d("debuglog","t空的");
        }
    }

    public abstract void handleMessage(Message msg, T t);
}
