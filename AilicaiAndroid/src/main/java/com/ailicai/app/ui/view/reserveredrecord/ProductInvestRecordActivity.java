package com.ailicai.app.ui.view.reserveredrecord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ailicai.app.R;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.huoqiu.framework.backstack.Op;

/**
 * Created by zhujiang on 2017/3/30.
 */

public class ProductInvestRecordActivity extends BaseBindActivity {

    public static final String PROD_ID = "prod_id";

    ProductInvestRecordFragment fragment;
    private boolean inited;
    @Override
    public void init(Bundle savedInstanceState) {
        if (null == savedInstanceState) {
            //投资记录
            fragment = new ProductInvestRecordFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commit();
        }
    }

    @Override
    public Op pop() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!inited) {
            inited = true;
            fragment.onRefresh();
        }
    }

    public static void startActivity(Activity activity, String id) {
        Intent intent = new Intent(activity,ProductInvestRecordActivity.class);
        intent.putExtra(PROD_ID, id);
        intent.putExtra("isReserve", true);
        activity.startActivity(intent);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_product_invest_record;
    }
}
