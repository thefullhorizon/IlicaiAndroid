package com.ailicai.app.ui.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.widget.AssetPie;

import butterknife.Bind;
import butterknife.OnClick;

public class AssetInViewOfBirdActivity extends BaseBindActivity {


    @Bind(R.id.asset_pie)
    AssetPie mAssetPie;
    @Bind(R.id.available_balance_value)
    AssetPie mAvailableBalanceValue;

    @Bind(R.id.net_loan_value)
    TextView mNetLoanValue;
    @Bind(R.id.apply_value)
    TextView mApplyValue;
    @Bind(R.id.money_fund_value)
    TextView mMoneyFundValue;

    @Bind(R.id.earnings_accumulated)
    TextView mEarningsAccumulated;

    @Bind(R.id.earnings_net_loan)
    TextView mEarningsNetLoan;
    @Bind(R.id.earnings_money_fund)
    TextView mEarningsMoneyFund;
    @Bind(R.id.earnings_tiyan)
    TextView mEarningsTiyan;

    @Override
    public int getLayout() {
        return R.layout.activity_asset_in_view_of_bird;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);


    }

    @OnClick(R.id.net_loan_layout)
    public void netLoanClick(View v) {
        //TODO nanshan

    }
    @OnClick(R.id.apply_layout)
    public void applyClick(View v) {
        //TODO nanshan

    }
    @OnClick(R.id.money_fund_layout)
    public void moneyFundClick(View v) {
        //TODO nanshan

    }


}
