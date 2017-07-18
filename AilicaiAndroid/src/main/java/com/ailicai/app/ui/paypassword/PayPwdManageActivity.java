package com.ailicai.app.ui.paypassword;

import android.content.Intent;
import com.ailicai.app.R;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.login.AccountInfo;

import butterknife.OnClick;

/**
 * @author owen
 *         2016/1/6
 */
public class PayPwdManageActivity extends BaseBindActivity {

    @Override
    public int getLayout() {
        return R.layout.activity_pay_pwd_manage;
    }

    @OnClick(R.id.rlPayPwdModify)
    void modifyPayPwd() {
        Intent intent = new Intent(this, PayPwdCheckActivity.class);
        intent.putExtra("TYPE", 1);
        startActivity(intent);
    }

    @OnClick(R.id.rlPayPwdReset)
    void resetPayPwd() {
        if(AccountInfo.isRealNameVerify()){
            Intent intent = new Intent(this, PayPwdResetActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, PayPwdCheckPhoneActivity.class);
            intent.putExtra("TYPE", 2);
            startActivity(intent);
        }
    }

}
