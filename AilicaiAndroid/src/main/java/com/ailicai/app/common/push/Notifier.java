package com.ailicai.app.common.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.push.constant.CommonTags;
import com.ailicai.app.common.push.model.PushMessage;
import com.ailicai.app.common.push.ui.PushUiDispatcherActivity;
import com.ailicai.app.common.push.utils.DeathChecker;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.eventbus.RefreshPushEvent;
import com.ailicai.app.ui.message.MsgLiteView;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

/**
 * Created by duo.chen on 2015/4/9.
 */
public class Notifier {

    private static final Random random = new Random(System.currentTimeMillis());

    private Context context;
    private NotificationManager notificationManager;

    private static Notifier instance;

    public static Notifier getInstance(Context context) {
        if (instance == null) {
            instance = new Notifier(context);
        }
        return instance;
    }

    private Notifier(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context
                .NOTIFICATION_SERVICE);
    }

    /**
     * @param pushMessage pushMessage Body
     */
    public void notify(PushMessage pushMessage) {

        if (!SystemUtil.isApplicationForeground(context)) {
            // Notification
            NotificationCompat.Builder b = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setLargeIcon(BitmapFactory.decodeResource(MyApplication.getAppResources(), R
                            .mipmap.ic_launcher))
                    .setColor(MyApplication.getAppResources().getColor(R.color.main_red_color_dark))
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND |
                            Notification.DEFAULT_VIBRATE)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setTicker(pushMessage.getMessage())
                    .setContentTitle(pushMessage.getTitle())
                    .setContentText(pushMessage.getMessage());

            Intent intent = new Intent(context, PushUiDispatcherActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(DeathChecker.ISALIVE, DeathChecker.isAlive());
            bundle.putSerializable(PushMessage.PUSHMESSAGE, pushMessage);
            bundle.putString(CommonTags.FROM,CommonTags.PUSH);
            intent.putExtras(bundle);

            //消息提醒
            PendingIntent contentIntent = PendingIntent.getActivity(context, random.nextInt(),
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //--------------------------
            b.setContentIntent(contentIntent);
            Notification notification;

            //minSdkVersion 15
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                notification = b.getNotification();
            } else {
                notification = b.build();
            }
            if (pushMessage.getNoticeId() > 0) {
                notificationManager.notify(pushMessage.getNoticeId(), notification);
            } else {
                notificationManager.notify(random.nextInt(), notification);
            }
            //--------------------------
        }
        RefreshPushEvent refreshPushEvent = new RefreshPushEvent();
        refreshPushEvent.setMsgType(pushMessage.getMsgType());
        if (null != pushMessage.getOptional()) {
            refreshPushEvent.setType(pushMessage.getOptional().getType());
        }
        EventBus.getDefault().post(refreshPushEvent);

        MsgLiteView.refreshNoticeNums(null);

        //TODO 统计EventLog
        EventLog.upEventLog("501", pushMessage.getPayload());

    }
}
