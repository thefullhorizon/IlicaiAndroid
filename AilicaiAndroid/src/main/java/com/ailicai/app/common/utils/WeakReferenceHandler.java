package com.ailicai.app.common.utils;

import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by yanxin on 2015/4/24.
 */
public abstract class WeakReferenceHandler<T> extends android.os.Handler {

    private WeakReference<T> mReference;

    public WeakReferenceHandler(T reference) {
        mReference = new WeakReference<T>(reference);
    }

    public WeakReferenceHandler(T reference, Looper looper) {
        super(looper);
        mReference = new WeakReference<T>(reference);
    }

    @Override
    public void handleMessage(Message msg) {
        if (mReference.get() == null) return;
        handleMessage(mReference.get(), msg);
    }

    protected abstract void handleMessage(T reference, Message msg);

}
