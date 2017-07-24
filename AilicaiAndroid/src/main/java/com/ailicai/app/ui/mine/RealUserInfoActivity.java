package com.ailicai.app.ui.mine;

import android.os.Bundle;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.constants.CommonTag;
import com.ailicai.app.common.utils.MapUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.ui.base.BaseBindActivity;

import java.util.Map;

import butterknife.Bind;

/**
 * Created by Gerry on 2017/7/18.
 */
public class RealUserInfoActivity extends BaseBindActivity {
    private Map<String, Object> dataMap;
    @Bind(R.id.tvIdName)
    TextView tvIdName;
    @Bind(R.id.tvIdCardNumber)
    TextView tvIdCardNumber;

    @Override
    public int getLayout() {
        return R.layout.real_userinfo_activity;
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

    public void setUserInfo(Map<String, Object> dataMap) {
        tvIdName.setText(MapUtil.getString(dataMap, CommonTag.PERSONAL_USER_R_NAME));
        tvIdCardNumber.setText(MapUtil.getString(dataMap, CommonTag.PERSONAL_USER_IDCARDNUMBER));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (dataMap != null) {
            MyIntent.setData(outState, dataMap);
        }
        super.onSaveInstanceState(outState);
    }


}
