package com.ailicai.app.ui.login;


import android.os.Bundle;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.ui.base.BaseBindActivity;

import java.util.Map;

public class LoginActivity extends BaseBindActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        LoginFragment mLoginFragment = new LoginFragment();
        Map<String, Object> dataMap = MyIntent.getData(getIntent());
        Bundle bundle = MyIntent.setData(new Bundle(), dataMap);
        mLoginFragment.setArguments(bundle);
        FragmentExchangeController.initFragment(getSupportFragmentManager(), mLoginFragment, LoginFragment.class.getCanonicalName());
        */
    }

    @Override
    public void init(Bundle savedInstanceState) {
        CommonUtil.miDarkSystemBar(this);
        initLoginFragment();
    }

    public void initLoginFragment() {
        LoginFragment mLoginFragment = new LoginFragment();
        Map<String, Object> dataMap = MyIntent.getData(getIntent());
        Bundle bundle = MyIntent.setData(new Bundle(), dataMap);
        mLoginFragment.setArguments(bundle);

        mLoginFragment.setContainerId(R.id.login_fragment_container);
        mLoginFragment.setTag(LoginFragment.class.getCanonicalName());
        mLoginFragment.setManager(getSupportFragmentManager());
        //mLoginFragment.setDefaultAnimations();
        /*
        mLoginFragment.setCustomAnimations(R.anim.none, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
                */
        mLoginFragment.setBackOp(null);
        mLoginFragment.show(mLoginFragment.SHOW_ADD);
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
        LoginManager.loginCancel();
        super.onBackPressed();
    }
}
