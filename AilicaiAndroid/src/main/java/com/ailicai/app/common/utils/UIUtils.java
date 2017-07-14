package com.ailicai.app.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


import com.ailicai.app.MyApplication;
import com.ailicai.app.R;

import java.lang.reflect.Method;

/**
 * UI Util类
 */
public class UIUtils {

    private static Toast mToast = null;

    public static void cancel() {
        if (null != mToast) {
            mToast.cancel();
        }
    }

    public static void setBoldText(TextView textView) {
        TextPaint tpaint = textView.getPaint();
        tpaint.setFakeBoldText(true);
    }

    public static void showToast(Context context, int resId) {
        mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void showToast(Context context, CharSequence msg) {
        mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 自适应高度
     */
    public static int getHeightSpec(View view, int heightMeasureSpec) {
        int heightSpec = 0;
        if (view.getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            // The great Android "hackatlon", the love, the magic.
            // The two leftmost bits in the height measure spec have
            // a special meaning, hence we can't use them to describe height.
            heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST);
        } else {
            // Any other height should be respected as is.
            heightSpec = heightMeasureSpec;
        }
        return heightSpec;
    }

    public static final <E extends View> E findView(Activity activity, int id) {
        return findView(activity.getWindow().getDecorView(), id);
    }

    @SuppressWarnings("unchecked")
    public static final <E extends View> E findView(View view, int id) {
        try {
            return (E) view.findViewById(id);
        } catch (ClassCastException e) {
            throw e;
        }
    }

    /**
     * 获取Activity中的rootView
     */
    public static View getContentView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content))
                .getChildAt(0);
    }

    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Activity ctx) {
        int width;
        Display display = ctx.getWindowManager().getDefaultDisplay();
        try {
            Method mGetRawW = Display.class.getMethod("getRawWidth");
            width = (Integer) mGetRawW.invoke(display);
        } catch (Exception e) {
            width = display.getWidth();
        }
        return width;
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        return dm.widthPixels;
    }

    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Activity ctx) {
        int height;
        Display display = ctx.getWindowManager().getDefaultDisplay();
        try {
            Method mGetRawH = Display.class.getMethod("getRawHeight");
            height = (Integer) mGetRawH.invoke(display);
        } catch (Exception e) {
            height = display.getHeight();
        }
        return height;
    }

    /**
     * 精确获取屏幕尺寸（例如：3.5、4.0、5.0寸屏幕）
     */
    public static double getScreenPhysicalSize(Activity ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2)
                + Math.pow(dm.heightPixels, 2));
        return diagonalPixels / (160 * dm.density);
    }

    public static int getScreenWidthPixels(Activity ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeightPixels(Activity ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * dip转px
     */
    public static int dipToPx(final Context ctx, float dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip, ctx.getResources().getDisplayMetrics());
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static String shortenMessage(String message) {
        String shortenedMessage;
        if (message == null) {
            shortenedMessage = "";
            //} else if (message.length() < 35) {
            //    shortenedMessage = message.replace("\n", " ");
        } else {
            //shortenedMessage = message.substring(0, 35).replace("\n", " ") + "...";
            shortenedMessage = message.replace("\n", " ");
        }
        return shortenedMessage;
    }

    public static int getDisplayHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int displayHeight = wm.getDefaultDisplay().getHeight();
        return displayHeight;
    }

    public static int getDisplayWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int displayHeight = wm.getDefaultDisplay().getWidth();
        return displayHeight;
    }

  /*  public static Drawable[] getWelcomeDrawableArray(Context context) {
        Drawable[] myColors;
        TypedArray ta = context.getResources().obtainTypedArray(R.array.welcome_switch_imgs);
        myColors = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            myColors[i] = ta.getDrawable(i);
        }
        ta.recycle();
        return myColors;
    }*/

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        String version = "";
        try {
            PackageManager manager = MyApplication.getInstance().getPackageManager();
            PackageInfo info = manager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
            version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    public static void showTips(Context mContext,String msg) {
        AlertDialog.Builder b = new AlertDialog.Builder(mContext, R.style.AppCompatDialog);
        b.setMessage(msg);
        b.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.show();
    }



}
