package com.ailicai.app.ui.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.constants.CommonTag;
import com.ailicai.app.common.utils.MapUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.StringUtil;
import com.ailicai.app.eventbus.ExitEvent;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.paypassword.PayPwdManageActivity;
import com.ailicai.app.widget.DialogBuilder;
import com.huoqiu.framework.util.CheckDoubleClick;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Gerry on 2017/7/18.
 */

public class SettingsActivity extends BaseBindActivity {
    @Bind(R.id.user_phone_tag)
    TextView mPhoneTag;
    @Bind(R.id.real_name)
    TextView mRealName;
    @Bind(R.id.login_out)
    LinearLayout mLoginOut;
    Handler mBackHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            finish();
            return false;
        }
    });
    private Map<String, Object> dataMap;

    @Override
    public int getLayout() {
        return R.layout.settings_activity;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        if (getIntent() != null) {
            dataMap = MyIntent.getData(getIntent());
            setUserInfo(dataMap);
        } else if (savedInstanceState != null) {
            dataMap = MyIntent.getData(savedInstanceState);
            setUserInfo(dataMap);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (dataMap != null) {
            MyIntent.setData(outState, dataMap);
        }
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.check_real_name)
    void checkRealName() {
        if (MapUtil.getInt(dataMap, CommonTag.PERSONAL_USER_ISREALNAMEVERIFY) == 1) {
            MyIntent.startActivity(mContext, RealUserInfoActivity.class, dataMap);
        } else {
            //TODO:未实名
        }

    }

    @OnClick(R.id.change_transaction_password)
    void changePassword() {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            MyIntent.startActivity(mContext, PayPwdManageActivity.class, "");
        }
    }

    @OnClick(R.id.change_phone)
    void changePhone() {
        MyIntent.startActivity(mContext, UserPhoneValidateActivity.class, dataMap);
    }

    public void setUserInfo(Map<String, Object> dataMap) {
        mPhoneTag.setText(StringUtil.formatMobileSub(MapUtil.getString(dataMap, CommonTag.PERSONAL_USER_PHONE)));
        if (MapUtil.getInt(dataMap, CommonTag.PERSONAL_USER_ISREALNAMEVERIFY) == 1) {
            mRealName.setText("已实名");
        } else {
            mRealName.setText("");
        }

        setUIData();
    }

    public void setUIData() {
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            mLoginOut.setVisibility(View.GONE);
        } else if (UserInfo.getInstance().getLoginState() == UserInfo.LOGIN) {
            mLoginOut.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.login_out)
    void onClickLoginOut() {
        DialogBuilder.showSimpleDialog(this, getString(R.string.loginout_tips_text), null, "取消", null, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExitEvent exitEvent = new ExitEvent();
                exitEvent.setForceExit(false);
                EventBus.getDefault().post(exitEvent);
                setUIData();
                mBackHandler.sendEmptyMessageDelayed(0, 200);
            }
        });
    }


}
