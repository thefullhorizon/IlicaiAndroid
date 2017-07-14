package com.ailicai.app.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;

public class ToastOneUtil {

    public static Toast toast = null;

    public static void showToastShort(String infoString) {
        showToastText(infoString, Toast.LENGTH_SHORT);
    }

    public static void showToastShort(int infoStringResId) {
        showToastText(MyApplication.getInstance().getString(infoStringResId), Toast.LENGTH_SHORT);
    }

    public static void showToastLong(Context context, String infoString) {
        if (context == null) {
            return;
        }
        showToastText(infoString, Toast.LENGTH_LONG);
    }

    public static void showToastLong(Context context, int infoStringResId) {
        if (context == null) {
            return;
        }
        showToastText(context.getString(infoStringResId), Toast.LENGTH_LONG);
    }

    public static void cancel() {
        if (toast == null) {
            return;
        }
        toast.cancel();
        toast = null;
    }

    private static void setToast(Toast toast2Set) {
        toast = toast2Set;
    }

    private static Toast getToast() {
        if (toast == null) {
            toast = new Toast(MyApplication.getInstance());
            LayoutInflater inflate = (LayoutInflater) MyApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflate.inflate(R.layout.common_radius_toast, null);
            toast.setView(v);
            setToast(toast);
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    private static void showToastText(String infoStr, int TOAST_LENGTH) {
        if (TextUtils.isEmpty(infoStr)) {
            return;
        }
        toast = getToast();
        TextView textView = (TextView) toast.getView().findViewById(R.id.toast_message);
        textView.setText(infoStr);
        toast.setDuration(TOAST_LENGTH);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
