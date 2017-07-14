package com.ailicai.app.common.utils;

import android.os.Handler;
import android.os.Message;

/**
 * 延时操作类
 *
 * @author manyi
 */
public abstract class DelayHandler extends Handler {

    @Override
    public void handleMessage(Message msg) {
        onDelayFinished(msg.what);
    }

    /**
     * 启动延时操作，启动之前会自动取消上次未完成的延时
     *
     * @param what        标识
     * @param delayMillis 延迟毫秒数
     */
    public void startDelay(int what, long delayMillis) {
        if (hasMessages(what)) {
            removeMessages(what);
        }
        sendEmptyMessageDelayed(what, delayMillis);
    }

    /**
     * 取消指定的延时操作
     *
     * @param what 标识
     */
    public void cancelDelay(int what) {
        if (hasMessages(what)) {
            removeMessages(what);
        }
    }

    public abstract void onDelayFinished(int what);
}
