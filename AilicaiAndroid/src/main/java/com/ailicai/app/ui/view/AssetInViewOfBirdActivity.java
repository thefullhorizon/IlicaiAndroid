package com.ailicai.app.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.bean.AssetPieBean;
import com.ailicai.app.model.request.AssetInfoNewRequest;
import com.ailicai.app.model.response.AssetInfoNewResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.widget.AssetPie;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 资产总览
 */
public class AssetInViewOfBirdActivity extends BaseBindActivity {

    @Bind(R.id.asset_pie)
    AssetPie mAssetPie;

    @Bind(R.id.available_balance_value)
    TextView mAvailableBalanceValue;
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
        requestData();
    }

    private void requestData() {

        AssetInfoNewRequest request = new AssetInfoNewRequest();
        request.setUserId(UserInfo.getInstance().getUserId());
        ServiceSender.exec(this, request, new IwjwRespListener<AssetInfoNewResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onJsonSuccess(AssetInfoNewResponse jsonObject) {
                showContentView();
                bindViewData(jsonObject);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showContentView();
                ToastUtil.showInCenter(errorInfo);
            }
        });

    }

    private void bindViewData(AssetInfoNewResponse jsonObject){

        List<AssetPieBean> data = new ArrayList<>();
        data.add(new AssetPieBean("账户可用余额","#40c8f7",Double.parseDouble(jsonObject.getAccountBalance().replace(",",""))));
        data.add(new AssetPieBean("网贷资产","#ff6b5d",Double.parseDouble(jsonObject.getNetLoanBalance().replace(",",""))));
        data.add(new AssetPieBean("申购款","#29d96c",Double.parseDouble(jsonObject.getPurchaseAmount().replace(",",""))));
        data.add(new AssetPieBean("货币基金","#007afa",Double.parseDouble(jsonObject.getTimeDepositBalance().replace(",",""))));
        mAssetPie.setData(data);
        mAssetPie.setTotalAsset(jsonObject.getTotalAsset());
        mAssetPie.startDraw();

        mAvailableBalanceValue.setText(jsonObject.getAccountBalance()+"(元)");
        mNetLoanValue.setText(jsonObject.getNetLoanBalance()+"(元)");
        mApplyValue.setText(jsonObject.getPurchaseAmount()+"(元)");
        mMoneyFundValue.setText(jsonObject.getTimeDepositBalance()+"(元)");

        mEarningsAccumulated.setText(jsonObject.getTotalIncome());
        mEarningsNetLoan.setText(jsonObject.getNetLoanIncome());
        mEarningsMoneyFund.setText(jsonObject.getDepositIncome());
        mEarningsTiyan.setText(jsonObject.getExperienceIncome());

    }

    @OnClick(R.id.net_loan_layout)
    public void netLoanClick(View v) {
        Intent intent = new Intent(this, CapitalActivity.class);
        intent.putExtra(CapitalActivity.TAB, CapitalActivity.HOLD);
        startActivity(intent);

    }
    @OnClick(R.id.apply_layout)
    public void applyClick(View v) {
        Intent intent = new Intent(this, CapitalActivity.class);
        startActivity(intent);

    }
    @OnClick(R.id.money_fund_layout)
    public void moneyFundClick(View v) {

        Intent intent = new Intent(this, MyWalletActivity.class);
        startActivity(intent);

    }


}
