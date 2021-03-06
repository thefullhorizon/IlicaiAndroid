package com.ailicai.app.common.push.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.push.constant.CommonTags;
import com.ailicai.app.common.push.model.PushMessage;
import com.ailicai.app.common.push.utils.DeathChecker;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.index.IndexActivity;
import com.ailicai.app.ui.message.BaseMessageListActivity;
import com.ailicai.app.ui.welcome.WelcomeActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by duo.chen on 2016/4/13
 */
public class PushUiDispatcherActivity extends BaseBindActivity {

    public final static String TAG = "PushUiDispatcherActivity";
    private PushMessage pushMessage;

    @Override
    public int getLayout() {
        return R.layout.push_ui_dispatcher_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Intent intent = getIntent();

        if (null != intent) {
            boolean isAppAlive = intent.getBooleanExtra(DeathChecker.ISALIVE, false);
            String from = intent.getStringExtra(CommonTags.FROM);
            if (TextUtils.isEmpty(from)) {
                //默认来源为推送
                from = CommonTags.PUSH;
            }
            getPushMessage(intent);
            switch (from) {
                case CommonTags.PUSH:
                    if (isAppAlive) {
                        upEventLog(pushMessage);
                        goPushDirect(pushMessage);
                    } else {
                        goPushWelcome(pushMessage);
                    }
                    break;
            }
        }
    }

    private void getPushMessage(Intent intent){
        try {
            MiPushMessage miPushMessage = (MiPushMessage) intent.getSerializableExtra
                    (PushMessageHelper.KEY_MESSAGE);

            if (null != miPushMessage) {
                byte[] payload = miPushMessage.getContent().getBytes();
                pushMessage = JSON.parseObject(payload, PushMessage.class, Feature
                        .IgnoreNotMatch, Feature.AllowISO8601DateFormat);
                pushMessage.setPayload(new String(payload));
            } else if(!TextUtils.isEmpty(intent.getStringExtra("content"))){//华为推送和极光，后端将自定义参数放在content字段内
//                            Log.d(TAG,"61__" + intent.getStringExtra("content"));
                pushMessage = JSON.parseObject(intent.getStringExtra("content"), PushMessage.class);
                pushMessage.setPayload(intent.getStringExtra("content"));
            }else {
                pushMessage = (PushMessage) intent.getSerializableExtra(PushMessage
                        .PUSHMESSAGE);
            }

        } catch (Exception e) {
            LogUtil.i(TAG, e.toString());
        }finally {
            //防止由于出现解析或其他，不上报事件，默认发送空json字符串
            if(pushMessage == null){
                pushMessage = new PushMessage();
                pushMessage.setPayload("");
            }
        }
    }

    private void upEventLog(PushMessage pushMessage){
        //TODO 统计
        EventLog.upEventLog("500", pushMessage.getPayload());

    }

    //应用第一次启动时，会发送此事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handlePushMessage(PushMessage event) {
        upEventLog(pushMessage);
    }

    private void goPushDirect(PushMessage pushMessage) {

        LogUtil.i(TAG,"goPushDirect" + pushMessage.toString());

        switch (pushMessage.getMsgType()) {
            case PushMessage.REMINDTYPE:
                goMessageList(pushMessage);
                break;
            case PushMessage.INFOTYPE:
            case PushMessage.ACTIVITYTYPE:
                if (pushMessage.getOptional().getType() == PushMessage
                        .NOTICETYPETOFINANCE) {
                    goPushIndexHome(pushMessage);
                } else {
                    goMessageList(pushMessage);
                }
                break;
        }
    }

    private void goMessageList(PushMessage pushMessage){
        BaseMessageListActivity.goActivity(this,pushMessage);
        finishDelay();
    }

    private void goPushWelcome(PushMessage pushMessage) {
        LogUtil.i(TAG,"goWelcome" + pushMessage.toString());
        final Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(PushMessage.PUSHMESSAGE, pushMessage);
        intent.putExtra(CommonTags.FROM,CommonTags.PUSH);
        intent.putExtra("settabIndex", 0);

        String manufacturer = android.os.Build.MANUFACTURER;
        if ("Xiaomi".equals(manufacturer)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    finishDelay();
                }
            },2000);
        } else {
            startActivity(intent);
            finishDelay();
        }
    }

    private void goPushIndexHome(PushMessage pushMessage){
        LogUtil.i(TAG,"goIndexHome" + pushMessage.toString());
        Intent intent=new Intent(this,IndexActivity.class);
        intent.putExtra(PushMessage.PUSHMESSAGE, pushMessage);
        intent.putExtra(CommonTags.FROM,CommonTags.PUSH);
        intent.putExtra("settabIndex", 0);
        startActivity(intent);
        finishDelay();
    }

    public void finishDelay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },1000);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }
}
