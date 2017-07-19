package com.ailicai.app.ui.mine;

import android.content.Intent;
import android.os.Bundle;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.buy.ProcessActivity;
import com.ailicai.app.ui.view.AccountTopupActivity;
import com.ailicai.app.ui.view.AccountWithdrawActivity;

import java.util.Map;

public class JumpProcessActivity extends BaseBindActivity {
    public static final String ACTION_KEY = "actionKey";
    public static final String ACTION_VAL_GET_CASH = "REQUEST_FOR_GET_CASH";
    public static final String ACTION_VAL_CHARGE = "REQUEST_FOR_CHARGE";

    private static final int REQUEST_FOR_GET_CASH = 1000;
    private static final int REQUEST_FOR_CHARGE = 1001;

    @Override
    public int getLayout() {
        return R.layout.jump_process_empty;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        Map<String, String> dataMap = MyIntent.getData(getIntent());
        if (dataMap.get(ACTION_KEY).equals(ACTION_VAL_GET_CASH)) {
            Intent intent = new Intent(this, ProcessActivity.class);
            startActivityForResult(intent, REQUEST_FOR_GET_CASH);
        } else {
            Intent intent = new Intent(this, ProcessActivity.class);
            startActivityForResult(intent, REQUEST_FOR_CHARGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_FOR_GET_CASH:
                    Intent intent = new Intent(this, AccountWithdrawActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case REQUEST_FOR_CHARGE:
                    Intent intent1 = new Intent(this, AccountTopupActivity.class);
                    startActivity(intent1);
                    finish();
                    break;
            }
        }
    }
}
