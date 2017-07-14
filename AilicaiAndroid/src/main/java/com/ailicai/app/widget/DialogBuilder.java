package com.ailicai.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ailicai.app.R;


public abstract class DialogBuilder {

    public static AlertDialog.Builder getAlertDialog(Context mContext) {
        return new AlertDialog.Builder(mContext);
    }

    public static AlertDialog.Builder getAlertDialog(Context mContext, int theme) {
        return new AlertDialog.Builder(mContext, theme);
    }


    /**
     * “我知道了” 并不能被取消
     *
     * @param message
     * @param context
     */
    public static void showSimpleDialog(String message, Context context) {
        showSimpleDialog(message, context, null);

    }

    /**
     * “我知道了” 并不能被取消
     *
     * @param message
     * @param context
     */
    public static void showSimpleDialogCenter(String message, Context context) {
        showSimpleDialogCenter(message, context, null);

    }

    /**
     * 没有title，一个 “我知道了”按钮
     *
     * @param message
     * @param context
     * @param listener
     */
    public static void showSimpleDialog(String message, Context context, OnClickListener listener) {
        AlertDialog alertDialog = showSimpleDialog(context, null, message, null, null, "我知道了", listener);
        alertDialog.setCancelable(false);
    }

    /**
     * 没有title，一个 “我知道了”按钮 message居中
     *
     * @param message
     * @param context
     * @param listener
     */
    public static void showSimpleDialogCenter(String message, Context context, OnClickListener listener) {
        AlertDialog alertDialog = showSimpleDialogCenter(context, null, message, null, null, "我知道了", listener);
        alertDialog.setCancelable(false);
    }

    /**
     * 没有title,没有取消按钮，不能被返回取消
     *
     * @param message
     * @param context
     * @param listener
     * @param positive
     */
    public static void customSimpleDialog(String message, Context context, OnClickListener listener, String positive) {
        getAlertDialog(context).setPositiveButton(positive, listener).setView(getSingleMsgView(context, message)).setCancelable(false).show();
    }


    /**
     * 没有title，只有确定按钮
     *
     * @param context
     * @param message
     * @param posMessage
     * @param listener
     */
    public static void showSimpleDialog(Context context, String message, String posMessage, OnClickListener listener) {
        showSimpleDialog(context, null, message, null, null, posMessage, listener);
    }

    /**
     * 只有独立Msg的情况
     *
     * @param context
     * @param msg
     * @return
     */
    private static View getSingleMsgView(Context context, CharSequence msg) {
        View loadView = View.inflate(context, R.layout.ued_dialog_msg, null);
        ((TextView) loadView.findViewById(R.id.message)).setText(msg);
        return loadView;
    }

    /**
     * 只有独立Title的情况
     *
     * @param context
     * @param title
     * @return
     */
    private static View getSingleTitleView(Context context, CharSequence title) {
        View loadView = View.inflate(context, R.layout.ued_dialog_msg, null);
        ((TextView) loadView.findViewById(R.id.message)).setGravity(Gravity.CENTER);
        ((TextView) loadView.findViewById(R.id.message)).setText(title);
        return loadView;
    }


    public static AlertDialog showSimpleListDialog(Context context, String dialog_title, String[] itemStrings, String positiveStr, OnClickListener itemListener) {
        AlertDialog.Builder b = getAlertDialog(context);
        b.setTitle(dialog_title);
        b.setItems(itemStrings, itemListener);
        b.setPositiveButton(positiveStr, null);
        return b.show();
    }


    /**
     * 没有取消事件
     *
     * @param context
     * @param title
     * @param message
     * @param posMessage
     * @param negMessage
     * @param mPositiveButtonListener
     */
 /*   public static void showSimpleDialog(Context context, String title, String message, String posMessage, String negMessage, OnClickListener mPositiveButtonListener) {
        showSimpleDialog(context, title, message, negMessage, null, posMessage, mPositiveButtonListener);
    }*/

    /**
     * 有title 有内容 有取消按钮 有确定按钮
     *
     * @param context
     * @param title
     * @param message
     * @param negMessage
     * @param mNegativeButtonListener
     * @param posMessage
     * @param mPositiveButtonListener
     * @return
     */
    public static AlertDialog showSimpleDialog(Context context, String title, CharSequence message, String negMessage, OnClickListener mNegativeButtonListener, String posMessage, OnClickListener mPositiveButtonListener) {
        AlertDialog.Builder alertDialogBuild = getAlertDialog(context);
        if (TextUtils.isEmpty(title)) {
            alertDialogBuild.setView(getSingleMsgView(context, message));
        } else if (TextUtils.isEmpty(message)) {
            alertDialogBuild.setView(getSingleTitleView(context, title));
        } else {
            alertDialogBuild.setTitle(title).setMessage(message);
        }
        alertDialogBuild.setNegativeButton(negMessage, mNegativeButtonListener).setPositiveButton(posMessage, mPositiveButtonListener);
        return alertDialogBuild.show();
/*        if (TextUtils.isEmpty(negMessage)) {
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            // alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }*/
    }

    public static AlertDialog showSimpleDialogCenter(Context context, String title, CharSequence message, String negMessage, OnClickListener mNegativeButtonListener, String posMessage, OnClickListener mPositiveButtonListener) {
        AlertDialog.Builder alertDialogBuild = getAlertDialog(context);
        if (TextUtils.isEmpty(title)) {
            alertDialogBuild.setView(getSingleTitleView(context, message));
        } else if (TextUtils.isEmpty(message)) {
            alertDialogBuild.setView(getSingleTitleView(context, title));
        } else {
            alertDialogBuild.setTitle(title).setMessage(message);
        }
        alertDialogBuild.setNegativeButton(negMessage, mNegativeButtonListener).setPositiveButton(posMessage, mPositiveButtonListener);
        return alertDialogBuild.show();
    }

    //文字和进度圈左右
    public static AlertDialog showProgressTextHorDialog(Context context, String msg) {
        View loadView = View.inflate(context, R.layout.progress_l_hor_dialog, null);
        ((TextView) loadView.findViewById(R.id.message)).setText(msg);
        return getAlertDialog(context).setView(loadView).show();
    }

    private static Toast toast = null;

    /**
     * 统一弹Toast
     *
     * @param context
     * @param message
     */
    public static void showSimpleToast(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showCancelableToast(Context context, String message) {
        // TextView tv = new TextView(context);
        // tv.setText(message);
        // mCancelableDialog = new AlertDialog.Builder(context).setView(tv).setCancelable(true).create();
        mCancelableDialog = getAlertDialog(context).setMessage(message).setCancelable(true).create();
        mCancelableDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mTimer != null) {
                    mTimer.cancel();
                }
            }
        });
        mCancelableDialog.show();

        if (mTimer != null) {
            mTimer.start();
        }

    }

    private static Dialog mCancelableDialog = null;
    private static CountDownTimer mTimer = new CountDownTimer(5000, 1000) {
        @Override
        public void onTick(long arg0) {
        }

        @Override
        public void onFinish() {
            if (mCancelableDialog != null && mCancelableDialog.isShowing()) {
                mCancelableDialog.dismiss();
                mCancelableDialog = null;
            }
        }
    };
}
