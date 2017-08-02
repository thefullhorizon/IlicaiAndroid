package com.ailicai.app.common.utils;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Gerry on 2017/8/2.
 */

public class ScreenUtils {
    private static Display defaultDisplay;
    private static float mDensity = 0.0F;
    private static float mScaledDensity = 0.0F;
    private static int mDensityDpi;


    public static float getDensity(Context context) {
        if (mDensity == 0.0F) {
            mDensity = context.getResources().getDisplayMetrics().density;
        }

        return mDensity;
    }

    public static int getDensityDpi(Context context) {
        if (mDensityDpi == 0) {
            mDensityDpi = context.getResources().getDisplayMetrics().densityDpi;
        }

        return mDensityDpi;
    }

    public static float getScaledDensity(Context context) {
        if (mScaledDensity == 0.0F) {
            mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        }

        return mScaledDensity;
    }

    public static int dip2px(int dip, Context context) {
        return (int) (0.5F + getDensity(context) * (float) dip);
    }

    public static int px2dip(int px, Context context) {
        return (int) (0.5F + (float) px / getDensity(context));
    }

    public static int px2sp(float pxValue, Context context) {
        return (int) (pxValue / getScaledDensity(context) + 0.5F);
    }

    public static int sp2px(float spValue, Context context) {
        return (int) (spValue * getScaledDensity(context) + 0.5F);
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getSreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static Display getDefaultDisplay(Context context) {
        if (defaultDisplay == null) {
            defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        }

        return defaultDisplay;
    }

    public static int getHeight(Context context) {
        return getDefaultDisplay(context).getHeight();
    }

    public static int getWidth(Context context) {
        return getDefaultDisplay(context).getWidth();
    }

    public static int percentHeight(float percent, Context context) {
        return (int) (percent * (float) getHeight(context));
    }

    public static int percentWidth(float percent, Context context) {
        return (int) (percent * (float) getWidth(context));
    }
}

