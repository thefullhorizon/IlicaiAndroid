package com.ailicai.app.common.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;


import com.ailicai.app.R;
import com.ailicai.app.setting.AppInfo;

import java.io.File;

/**
 * Created by Ted on 14-7-31.
 */
public class DownloadNotificationManager {
    private Context mContext;
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private static DownloadNotificationManager mDownloadNotificationManager = null;

    public DownloadNotificationManager(Context context) {
        this.mContext = context;
        PendingIntent pi = PendingIntent.getActivity(mContext, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        mNotification = new Notification();
        mNotification.icon = R.mipmap.ic_launcher;
        mNotification.contentView = new RemoteViews(mContext.getPackageName(), R.layout.download_notice_notification);
        mNotification.contentIntent = pi;
    }


    public static DownloadNotificationManager getInstance(Context context) {
        if (null == mDownloadNotificationManager) {
            mDownloadNotificationManager = new DownloadNotificationManager(context);
        }
        return mDownloadNotificationManager;
    }

    public void updateDownloadSize(int size, int totalSize) {
        long sizeLong = size * 100L;
        long temp = sizeLong / totalSize;
//        LogUtil.e("size======>",size+"");
//        LogUtil.e("totalSize======>",totalSize+"");
        mNotification.defaults = 0;
        mNotification.contentView.setTextViewText(R.id.tvSize, temp + "%");
        mNotification.contentView.setTextViewText(R.id.tvTitle,
                mContext.getResources().getString(R.string.download_percent_notifacation, AppInfo.getInstance().getmServerVersion()));
        mNotification.contentView.setProgressBar(R.id.pbDownLoad, 100, (int)temp, false);
        mNotificationManager.notify(0, mNotification);
    }

    public void setNotificationClickToInstallApk(String filePath) {

        Intent intent = intstallApk(filePath);

        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.contentIntent = contentIntent;
        mNotification.contentView.setTextViewText(R.id.tvSize, 100 + "%");
        mNotification.contentView.setTextViewText(R.id.tvTitle,
                mContext.getResources().getString(R.string.download_finished, AppInfo.getInstance().getmServerVersion()));
        mNotification.contentView.setProgressBar(R.id.pbDownLoad, 100, 100, false);
        mNotification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
        mNotification.tickerText = mContext.getResources().getString(R.string.download_finished, AppInfo.getInstance().getmServerVersion());
        mNotificationManager.notify(0, mNotification);
    }

    private Intent intstallApk(String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }


    public void onMergingPatch() {
        mNotification.defaults = 0;
        mNotification.contentView.setTextViewText(R.id.tvSize, 100 + "%");
        mNotification.contentView.setTextViewText(R.id.tvTitle,"正在升级");
        mNotification.contentView.setProgressBar(R.id.pbDownLoad, 100, 100, false);
        mNotificationManager.notify(0, mNotification);
    }


    public void destroy() {
        if (null != mNotificationManager) {
            //mNotificationManager.cancelAll();
        }
    }
}
