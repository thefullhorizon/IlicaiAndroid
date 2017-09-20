package com.ailicai.app.ui.mine;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.login.UserInfo;

import butterknife.Bind;

/**
 * Created by Gerry on 2017/7/18.
 */

public class ContactUsActivity extends BaseBindActivity {

    @Bind(R.id.server_tel)
    TextView serverTel;

    @Override
    public int getLayout() {
        return R.layout.contact_us_activity;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        String serverNumber = UserInfo.getInstance().getServicePhoneNumber();
        serverTel.setText(Html.fromHtml(getResources().getString(R.string.mine_service_tell_txt, serverNumber)));
    }


}
