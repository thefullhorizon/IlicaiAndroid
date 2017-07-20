package com.ailicai.app.ui.reserve;

/**
 * Created by duo.chen on 2016/1/8.
 */
public class CalculatorCalender {
    int hour;
    int minute;
    int second;

    public CalculatorCalender(long timeInMillis) {
        setTimeInSecond(timeInMillis / 1000);
    }

    public void setTimeInMillis(long timeInMillis){
        setTimeInSecond(timeInMillis / 1000);
    }

    private void setTimeInSecond(long timeInSecond) {
        hour = (int) timeInSecond / 3600;
        minute = (int) ((timeInSecond % 3600) / 60);
        second = (int) (((timeInSecond % 3600) % 60) % 60);
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }
}
