package com.ailicai.app.ui.login;


import android.os.Bundle;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.FragmentExchangeController;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.ui.base.BaseBindActivity;

import java.util.Map;

/**
 * @author xiongwei
 * @ClassName: LoginActivity
 * @date 2014年6月04日 上午11:41:03
 */
public class LoginActivity extends BaseBindActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtil.miDarkSystemBar(this);
        //注册短信监听，用于登录时截获短信验证码（有的手机用BroadcastReceiver的方式无法截获短信验证码，所以这里再加一种方式）

        LoginFragment mLoginFragment = new LoginFragment();
        Map<String, Object> dataMap = MyIntent.getData(getIntent());
        Bundle bundle = MyIntent.setData(new Bundle(), dataMap);
        mLoginFragment.setArguments(bundle);
        FragmentExchangeController.initFragment(getSupportFragmentManager(), mLoginFragment, LoginFragment.class.getCanonicalName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        CommonUtil.uiSystemBarTintNoTitle(this, findViewById(R.id.fragment_container));
    }

    @Override
    protected void onDestroy() {
        //反注册短信监听
        super.onDestroy();
    }

    @Override
    public int getLayout() {
        return R.layout.blank_activity_layout;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_lollipop_close_enter, R.anim.activity_lollipop_close_exit);
    }

    @Override
    public void onBackPressed() {
        finish();
        LoginManager.loginCancel();
        super.onBackPressed();
    }
}
