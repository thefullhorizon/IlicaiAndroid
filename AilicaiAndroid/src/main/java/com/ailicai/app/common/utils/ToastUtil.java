/**
 *
 */
package com.ailicai.app.common.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ailicai.app.R;


public class ToastUtil {

    public static void show(Context context, String info) {
        if (TextUtils.isEmpty(info)) {
            return;
        }
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }


    public static void show(String info) {
        if (TextUtils.isEmpty(info)) {
            return;
        }
        Toast.makeText(ContextUtil.getApplicationContext(), info, Toast.LENGTH_LONG).show();
    }


    public static void show(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    public static void showInCenter(String message) {
        ToastOneUtil.showToastShort(message);
    }

    public static void showInRectangleCenter(Context context, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.common_radius_toast, null);
        TextView tv = (TextView) view.findViewById(R.id.toast_message);
        tv.setText(message);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static Toast showInBottom(Context context, String message) {
        if (TextUtils.isEmpty(message)) {
            return null;
        }
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.common_radius_toast, null);
        TextView tv = (TextView) view.findViewById(R.id.toast_message);
        tv.setText(message);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 120);
        toast.show();
        return toast;
    }

    public static void showInBottomLong(Context context, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.common_radius_toast, null);
        TextView tv = (TextView) view.findViewById(R.id.toast_message);
        tv.setText(message);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 120);
        toast.show();
    }


    public static void showInCenterLong(Context context, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.common_radius_toast, null);
        TextView tv = (TextView) view.findViewById(R.id.toast_message);
        tv.setText(message);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showInCenterLong(Context context, View view) {
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
