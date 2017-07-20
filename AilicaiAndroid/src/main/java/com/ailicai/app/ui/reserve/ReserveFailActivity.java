package com.ailicai.app.ui.reserve;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;


import com.ailicai.app.R;
import com.ailicai.app.ui.base.BaseBindActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhujiang on 2017/3/29.
 */

public class ReserveFailActivity extends BaseBindActivity {

    @Bind(R.id.tv_message)
    TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras().containsKey("msgInfo")) {
            String msgInfo = getIntent().getExtras().getString("msgInfo");
            if (!TextUtils.isEmpty(msgInfo)) {
                tvMessage.setText(msgInfo);
            }
        }
    }

    @OnClick(R.id.btn_complete)
    void onCompleteClicked() {
        Intent intent = new Intent(this, ReserveActivity.class);
        startActivity(intent);
    }


    @Override
    public int getLayout() {
        return R.layout.activity_finance_reserve_failed;
    }
}
