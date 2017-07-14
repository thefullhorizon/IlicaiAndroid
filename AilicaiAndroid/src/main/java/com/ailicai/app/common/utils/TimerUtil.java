package com.ailicai.app.common.utils;

import com.ailicai.app.receiver.SmsObserver;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gerry on 2015/7/14.
 * 验证码倒计时帮助类，结合发送短信使用
 */
public class TimerUtil {
    private final int INTERVALS = 60;
    private int timesNum = INTERVALS;
    private Timer sendTimer;
    private String PERIOD_NUM = TimerUtil.class.getCanonicalName();
    private String CURRENT_TIME = TimerUtil.class.getCanonicalName();
    private boolean isTimerStarting = false;

    private TimerListener mTimerListener = new TimerListener() {

        @Override
        public void onTimerStarting(int periodNum) {

        }

        @Override
        public void onTimerCancel() {

        }

    };

    public TimerUtil(TimerListener l, String tag) {
        mTimerListener = l;
        this.PERIOD_NUM = "periodNum_" + tag;
        this.CURRENT_TIME = "currentTime_" + tag;
    }

    /**
     * 开始倒计时
     */
    public void startTimer() {
        initTimerCommon();
        timesNum = INTERVALS;
        if (null != sendTimer) {
            sendTimer.cancel();
            setIsTimerStarting(false);
        }
        sendTimer = new Timer();
        sendTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerStarted();
            }
        }, 0, 1000);
    }

    /**
     * 倒计时继续
     */
    public void restartTimer() {
        if (!isTimerResume()) {
            return;
        }
        if (null != sendTimer) {
            sendTimer.cancel();
            setIsTimerStarting(false);
        }
        sendTimer = new Timer();
        sendTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerStarted();
            }
        }, 0, 1000);
    }

    /**
     * 停止倒计时
     */
    public void stopTimer() {
        timesNum = INTERVALS;
        if (null != sendTimer) {
            sendTimer.cancel();
            setIsTimerStarting(false);
            initTimerCommon();
            mTimerListener.onTimerCancel();
        }
    }

    /**
     * 页面返回时调用
     */
    public void pauseTimer() {
        if (timesNum != INTERVALS) {
            sendTimer.cancel();
            setIsTimerStarting(false);
            initTimerCommon();
            saveTimerCommon();
        }
    }

    /**
     * 初始化需要保存的值
     */
    public void initTimerCommon() {
        MyPreference.getInstance().write(SmsObserver.MESSAGE_COMEBACK, false);
        MyPreference.getInstance().write(PERIOD_NUM, 0);
        MyPreference.getInstance().write(CURRENT_TIME, new Long(0));
    }

    /**
     * 倒计时开始后，返回页面时保存当时的时间，及倒计时数值
     */
    public void saveTimerCommon() {
        MyPreference.getInstance().write(PERIOD_NUM, timesNum);
        MyPreference.getInstance().write(CURRENT_TIME, System.currentTimeMillis());
    }

    /**
     * 倒计时接口回调
     */
    private void timerStarted() {
        timesNum--;
        if (timesNum <= 0) {
            sendTimer.cancel();
            setIsTimerStarting(false);
            initTimerCommon();
            timesNum = INTERVALS;
            mTimerListener.onTimerCancel();
        } else {
            setIsTimerStarting(true);
            mTimerListener.onTimerStarting(timesNum);
        }
    }

    /**
     * 倒计时是否继续，何时继续
     *
     * @return
     */
    private boolean isTimerResume() {
        boolean isMsgOk = MyPreference.getInstance().read(SmsObserver.MESSAGE_COMEBACK, false);
        int periodNum = MyPreference.getInstance().read(PERIOD_NUM, 0);
        long currentTime = MyPreference.getInstance().read(CURRENT_TIME, new Long(0));
        if (periodNum <= 0 || currentTime <= 0 || isMsgOk) {
            return false;
        }
        long cTimeMillis = System.currentTimeMillis();
        long timeSub = cTimeMillis - currentTime;
        if (timeSub > 0) {
            long sum = timeSub / 1000;
            int temp = periodNum - new Long(sum).intValue();
            if (temp > 0) {
                //当前的倒计时数值
                timesNum = temp;
                return true;
            }
        }
        return false;
    }

    public boolean isTimerStarting() {
        return isTimerStarting;
    }

    public void setIsTimerStarting(boolean isTimerStarting) {
        this.isTimerStarting = isTimerStarting;
    }

    /**
     * ===================================
     */
    public interface TimerListener {
        void onTimerStarting(int periodNum);

        void onTimerCancel();

    }

    public static String convertMillis2Time(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MICROSECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.HOURS.toSeconds(TimeUnit.MICROSECONDS.toHours(millis)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }
}
