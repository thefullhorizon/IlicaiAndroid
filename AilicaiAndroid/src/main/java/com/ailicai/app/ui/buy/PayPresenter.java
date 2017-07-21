package com.ailicai.app.ui.buy;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import com.ailicai.app.widget.DialogBuilder;

import java.io.Serializable;

/**
 * Created by Jer on 2015/9/17.
 * 把支付做成静态的支付工具
 */
public class PayPresenter implements Serializable {

    public interface ICompletePayListener {
        void paySuccess();

        void payFail();
    }

    private static AlertDialog progressDialog;

//    public static void showResultDialog(final Activity mActivity, String msgInfo) {
//        final AlertDialog dialog = DialogBuilder.showSimpleDialog(mActivity, msgInfo,null, null, null, "确定", null);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(true);
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                if (mActivity instanceof WXPayEntryActivity) {
//                    mActivity.finish();
//                }
//            }
//        });
//        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                if (mActivity instanceof WXPayEntryActivity) {
//                    mActivity.finish();
//                }
//            }
//        });
//    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static AlertDialog showProgressDialog(Activity mActivity, String msg) {
        iniProgressDialog2(mActivity, msg);
        progressDialog.show();
        return progressDialog;
    }

/*    private static ProgressDialog iniProgressDialog(Activity mActivity, String msg) {
        dismissProgressDialog();
        progressDialog = progressDialog == null ? new ProgressDialog(mActivity, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT) : progressDialog;//,"获取支付信息中稍等...."
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(msg);
        return progressDialog;
    }*/

    private static AlertDialog iniProgressDialog2(Activity mActivity, String msg) {
        dismissProgressDialog();
        progressDialog = DialogBuilder.showProgressTextHorDialog(mActivity, msg);
        //   progressDialog = progressDialog == null ? new ProgressDialog(mActivity, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT) : progressDialog;//,"获取支付信息中稍等...."
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }
}
