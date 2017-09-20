package com.ailicai.app.ui.bankcard;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;


import com.ailicai.app.R;
import com.ailicai.app.eventbus.FinancePayEvent;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.view.AssetInViewOfBirdActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * description: 银行卡可用并且资产余额大于服务器下发金额<BR>
 * create date: 16/2/17
 *
 * @author: iwjw zhouxuan
 */
public class ExSafeCardValidBiggerThanChangeLimitActivity extends BaseBindActivity {

    @Bind(R.id.textViewProperty)
    TextView textViewProperty;
    @Bind(R.id.tvDescTitle)
    TextView tvDescTitle;
    @Bind(R.id.tvDesc03)
    TextView tvDesc03;

    private double property = 0d;
    private int changeLimit = 0;

    @Override
    public int getLayout() {
        return R.layout.activity_bankcard_ex_valid_bigger_than_changelimit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        getDataFromIntentOrSavedInstanceState(savedInstanceState);
        setData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("property",property);
        outState.putInt("changeLimit",changeLimit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getDataFromIntentOrSavedInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            property = savedInstanceState.getDouble("property");
            changeLimit = savedInstanceState.getInt("changeLimit");
        } else {
            property = getIntent().getDoubleExtra("property",0d);
            changeLimit = getIntent().getIntExtra("changeLimit",0);
        }
    }

    private void setData() {
        textViewProperty.setText(getPropertyShowTxt());
        tvDescTitle.setText("您的资产余额超过"+changeLimit+"元，为保证您的账户安全，请完成以下操作后再次更换银行卡：");
        tvDesc03.setText("3.将账户余额提现至低于"+changeLimit+"元");
    }

    private String getPropertyShowTxt() {
        return formatTosepara(property);
    }

    public String formatTosepara(double data) {
        DecimalFormat df = new DecimalFormat("#,###.##");
        String formatStr = df.format(data);
        if(!TextUtils.isEmpty(formatStr) && !formatStr.contains(".")) {
            formatStr = formatStr + ".00";
        }
        return formatStr;
    }

    @OnClick(R.id.relativeLayoutProperty)
    void goToPropertyActivity() {
        Intent intent = new Intent(this, AssetInViewOfBirdActivity.class);
        startActivity(intent);
    }

    // 有转入转出动作的时候，关闭当前页
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleFinancePayEvent(FinancePayEvent event) {
        this.finish();
    }


}

