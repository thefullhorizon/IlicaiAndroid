package com.ailicai.app.common.download;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.LogUtil;


/**
 * Created by Ted on 14-7-31.
 */
public class DownloadProgressDialogManger {
    private Context mContext;
    private DownloadProgressDialog mProgressDialog;
    private TextView tvSizeDesc,tvTotalSizeDesc;

    public DownloadProgressDialogManger() {
        //this.mContext = context;
        //mProgressDialog = new DownloadProgressDialog(mContext, R.style.AppCompatDialog);
    }

    private static DownloadProgressDialogManger mDownloadProgressDialogManger = null;

    public static DownloadProgressDialogManger getInstance() {
        if (null == mDownloadProgressDialogManger) {
            mDownloadProgressDialogManger = new DownloadProgressDialogManger();
        }
        return mDownloadProgressDialogManger;
    }

    public void initDialog(Context mContext, boolean isCanCancle, String msg) {
        if (mContext == null || ((Activity) mContext).isFinishing()) return;
        if (mProgressDialog == null) {
            mProgressDialog = new DownloadProgressDialog(mContext, R.style.AppCompatDialog);
        }
        if (mProgressDialog.isShowing()) mProgressDialog.dismiss();
        mProgressDialog.setCancelable(!isCanCancle);
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    public void setCanDialogCancle(boolean isCanCancle) {
        if (mProgressDialog != null) {
            mProgressDialog.setCancelable(!isCanCancle);
        }
    }

    public void removeDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public void updateDialog(int size, int totalSize) {

            if (mProgressDialog != null) {

                LogUtil.e("size======>",size+"");
                LogUtil.e("totalSize======>",totalSize+"");

                mProgressDialog.setMax(totalSize);
                mProgressDialog.setProgress(size);
                mProgressDialog.setAlreadyDownloadAndTotalSize(getAlreayDownloadSizeDesc(size),getTotalSizeDesc(totalSize));
        }
    }

    private String getAlreayDownloadSizeDesc(int size) {
        String desc = humanReadableByteCount(size,false);
        return desc;
    }

    private String getTotalSizeDesc(int totalSize) {
        String desc = humanReadableByteCount(totalSize,false);
        return desc;
    }

    public void setMessage(String msg) {
        if (mProgressDialog != null) {
            mProgressDialog.setMessage(msg);
        }
    }

    public void setOriTotalSize(String oriTotalSizeStr) {
        if (mProgressDialog != null) {
            mProgressDialog.setOriTotalSize(oriTotalSizeStr);
        }
    }

    public void setOriTotalSizeInvisible() {
        if (mProgressDialog != null) {
            mProgressDialog.setOriTotalSizeInvisible();
        }
    }

    public void destory() {
        mDownloadProgressDialogManger = null;
        if(mProgressDialog !=null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "KMGTPE".charAt(exp-1) + ""; //其实正确的应该是 String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i")
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
