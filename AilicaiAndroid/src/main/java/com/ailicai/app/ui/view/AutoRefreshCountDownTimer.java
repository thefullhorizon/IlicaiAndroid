package com.ailicai.app.ui.view;

import android.os.Handler;
import android.os.Message;

/**
 * Created by duo.chen on 2016/4/11.
 */
public abstract class AutoRefreshCountDownTimer {

    public final static String TAG = "AutoRefreshCountDownTimer";

    private static final int MSG = 1;
    private boolean mCancelled = false;
    private boolean mPause = false;
    private long timeInterval;

    public AutoRefreshCountDownTimer(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    public abstract void onTick();

    public synchronized final void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
     //   LogUtil.i(TAG,"cancel");
    }

    public synchronized final void pause() {
        mPause = true;
     //   LogUtil.i(TAG,"pause");
    }

    public synchronized final void resume() {
        mPause = false;
  //      LogUtil.i(TAG,"resume");
    }

    /**
     * Start the countdown.
     */
    public synchronized final void start() {
        mCancelled = false;
        mPause = false;
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), timeInterval);
     //   LogUtil.i(TAG,"start");
    }


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (AutoRefreshCountDownTimer.this) {
                if (mCancelled) {
                    return;
                }
                if (!mPause) {
                    onTick();
             //       LogUtil.i(TAG,"onTick");
                }
                sendMessageDelayed(obtainMessage(MSG), timeInterval);
            }
        }
    };

}
