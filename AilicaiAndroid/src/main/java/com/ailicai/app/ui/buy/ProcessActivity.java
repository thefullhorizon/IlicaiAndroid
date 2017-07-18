package com.ailicai.app.ui.buy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.eventbus.LoginEvent;
import com.ailicai.app.model.request.AccountRequest;
import com.ailicai.app.model.response.AccountResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.login.AccountInfo;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.widget.DialogBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by David on 16/1/14.
 * Modified by Tangxiaolong on 16/4/22.
 */
public class ProcessActivity extends BaseBindActivity {

    private static final int REQUEST_FOR_PWD_CHECK = 0x00;
    private static final int REQUEST_FOR_PWD_SET = 0x01;


    boolean firstLoad = false;
    boolean isFinish = true;
    State state;

    View mProcess;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mProcess = this.findViewById(R.id.processing);
        firstLoad = true;
        state = new State();
        if (UserInfo.getInstance().getLoginState() == UserInfo.NOT_LOGIN) {
            state._isLogin = false;
            LoginManager.goLogin(this, LoginManager.LOGIN_FROM_ENTRUST);
        } else {
            state._isLogin = true;
            ServiceSender.exec(this, new AccountRequest(), new AccountCallback());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_finance_empty;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int flag = intent.getIntExtra("FROM_FLAG", -1);
        if (flag <= 0) return;

        //from open account.
        if (flag == 1) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (firstLoad) {
            firstLoad = false;
            return;
        }

        if (doNothing()) finish();
    }


    private boolean doNothing() {
        boolean isLogin = UserInfo.getInstance().getLoginState() == UserInfo.LOGIN;
        boolean isAccount = AccountInfo.isOpenAccount();

        if (isLogin && isAccount) return false;

        return state._isLogin == isLogin && state._isAccount == isAccount;

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case REQUEST_FOR_PWD_CHECK:
                    case REQUEST_FOR_PWD_SET:
                        firstLoad = true;
                        //TODO nanshan 开户相关
//                        OpenAccountActivity.gotoOpenAccount(this, OpenAccountFeature.openAccount());
                        break;
                }
                break;
            case RESULT_CANCELED:
                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLoginEvent(LoginEvent event) {
        if (event.isLoginSuccess()) {
            firstLoad = true;
            state._isLogin = true;
            ServiceSender.exec(this, new AccountRequest(), new AccountCallback());
        }
    }

    void handleAccountResponse(AccountResponse account) {
        //record account info.
        AccountInfo.getInstance().saveAccountInfo(account);

        if (account.getIsOpenAccount() == 1) {
            //开户
            setResult(RESULT_OK);
            finish();
        } else if (account.getIsOpenAccount() == 2) {
            //开户中
            showOpeningAccountDialog();
        } else {
            //未开户
            //TODO nanshan 开户相关
//            if (AccountInfo.isSetPayPwd()) {
//                Intent intent = new Intent(this, PayPwdCheckActivity.class);
//                intent.putExtra("ISDARK", true);
//                intent.putExtra("ISOPENACCOUNT",true);
//                startActivityForResult(intent, REQUEST_FOR_PWD_CHECK);
//            } else {
//                Intent intent = new Intent(this, PayPwdResetAndModifyActivity.class);
//                startActivityForResult(intent, REQUEST_FOR_PWD_SET);
//            }
        }
    }

    static class AccountCallback extends IwjwRespListener<AccountResponse> {
        @Override
        public void onJsonSuccess(AccountResponse response) {
            ProcessActivity activity = getWRContext();
            if (activity == null) return;
            activity.handleAccountResponse(response);
        }

        @Override
        public void onFailInfo(String error) {
            ProcessActivity activity = getWRContext();
            if (activity == null) return;

            ToastUtil.showInBottom(activity, error);
            activity.finish();
        }
    }

    private void showOpeningAccountDialog() {
        mProcess.setVisibility(View.GONE);

        AlertDialog show = DialogBuilder.showSimpleDialog(this, "正在开户中，请稍等", null, "再等等", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, "重新开户", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //是否要关闭当前界面(因为dialog默认是点击就关闭了,导致startActivity接收不到回调)
                isFinish = false;
                // 重新校验交易密码，然后开户
                //TODO nanshan 开户相关
//                if (AccountInfo.isSetPayPwd()) {
//                    Intent intent = new Intent(ProcessActivity.this, PayPwdCheckActivity.class);
//                    intent.putExtra("ISDARK", true);
//                    intent.putExtra("ISOPENACCOUNT",true);
//                    startActivityForResult(intent, REQUEST_FOR_PWD_CHECK);
//                } else {
//                    Intent intent = new Intent(ProcessActivity.this, PayPwdResetAndModifyActivity.class);
//                    startActivityForResult(intent, REQUEST_FOR_PWD_SET);
//                }
            }
        });
        show.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (isFinish) {
                    finish();
                }
            }
        });
    }

    static class State {
        boolean _isLogin;
        boolean _isAccount;
    }
}
