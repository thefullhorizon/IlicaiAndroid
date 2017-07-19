package com.ailicai.app.ui.mine;


import android.os.Bundle;
import android.os.Handler;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.receiver.SmsObserver;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.FragmentHelper;

import java.util.Map;

public class UserPhoneModifyActivity extends BaseBindActivity{

    private SmsObserver smsObserver;
    public Handler smsHandler = new Handler() {
    };

    @Override
    public int getLayout() {
        return R.layout.userphone_modify_activity_layout;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        //注册短信监听，用于登录时截获短信验证码（有的手机用BroadcastReceiver的方式无法截获短信验证码，所以这里再加一种方式）
        smsObserver = new SmsObserver(this, smsHandler);
        getContentResolver().registerContentObserver(SmsObserver.SMS_INBOX, true,
                smsObserver);
        setContentView(R.layout.userphone_modify_activity_layout);
        FragmentHelper mFragmentHelper = new FragmentHelper(getSupportFragmentManager());
        Map<String, Object> dataMap = MyIntent.getData(getIntent());
        mFragmentHelper.replace(MyIntent.setData(new Bundle(), dataMap), R.id.user_phone_modify_main, new UserPhoneModifyFragment());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        //反注册短信监听
        getContentResolver().unregisterContentObserver(smsObserver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
