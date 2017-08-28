package com.ailicai.app.ui.buy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.bean.Protocol;
import com.ailicai.app.model.response.AutoBidResponse;
import com.ailicai.app.ui.base.BaseMvpActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.html5.SupportUrl;
import com.ailicai.app.widget.AutomaticTenderTypeView;
import com.ailicai.app.widget.DialogBuilder;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.ToggleButton;
import com.ailicai.app.widget.XEditText;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;

/**
 * 自动投标页面
 * Created by jeme on 2017/8/18.
 */
public class AutomaticTenderActivity extends BaseMvpActivity<AutomaticTenderPresenter>
        implements AutomaticTenderPresenter.AutomaticTenderView,
            ToggleButton.OnToggleChanged,AutomaticTenderTypeView.AutomaticTenderCheckListener,
                View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    public static final String NETBALANCE_KEY = "netBalance";
    public static final String ACCOUNTBALANCE_KEY = "accountBalance";

    @Bind(R.id.iwttv_top)
    IWTopTitleView mIwttvTop;
    @Bind(R.id.tv_assert_money_tip)
    TextView mTvAssertMoneyTip;
    @Bind(R.id.v_line)
    View mVLine;
    @Bind(R.id.tv_balance_tip)
    TextView mTvBalanceTip;
    @Bind(R.id.tb_automatic_tender)
    ToggleButton mTbAutomaticTender;
    @Bind(R.id.ll_toggle_on_container)
    View mVToggleContainer;
    @Bind(R.id.xet_reserve_money)
    XEditText mXetReserveMoney;
    @Bind(R.id.attv_year_max)
    AutomaticTenderTypeView mAttvYearMax;
    @Bind(R.id.attv_time_shortest)
    AutomaticTenderTypeView mAttvTimeShortest;
    @Bind(R.id.rl_agreement_container)
    View mVAgreementContainer;
    @Bind(R.id.cb_agreement_checkbox)
    CheckBox mCbAgreement;
    @Bind(R.id.tv_user_agreement_link)
    TextView mTvUserAgreementLink;
    @Bind(R.id.tv_ok)
    TextView mTvOk;

    private int mStrategyType;
    private String mReserveMoney = "0.00";//用户预留的金额

    public static void open(Context context, String netBalance, String accountBalance){
        Intent intent = new Intent(context,AutomaticTenderActivity.class);
        intent.putExtra(AutomaticTenderActivity.NETBALANCE_KEY,netBalance);
        intent.putExtra(AutomaticTenderActivity.ACCOUNTBALANCE_KEY,accountBalance);
        context.startActivity(intent);
    }
    @Override
    public AutomaticTenderPresenter initPresenter() {
        return new AutomaticTenderPresenter();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_automatic_tender;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        String netBalance = getIntent().getStringExtra(NETBALANCE_KEY);
        String accountBalance = getIntent().getStringExtra(ACCOUNTBALANCE_KEY);

        if(TextUtils.isEmpty(netBalance) || TextUtils.isEmpty(accountBalance)){
            mTvAssertMoneyTip.setVisibility(View.GONE);
            mTvBalanceTip.setVisibility(View.GONE);
            mVLine.setVisibility(View.GONE);
        }else{
            mTvAssertMoneyTip.setText(String.format(Locale.CHINA,
                    getResources().getString(R.string.automatic_net_balance), netBalance));
            mTvBalanceTip.setText(String.format(Locale.CHINA,
                    getResources().getString(R.string.automatic_account_balance),accountBalance));
            mTvAssertMoneyTip.setVisibility(View.VISIBLE);
            mTvBalanceTip.setVisibility(View.VISIBLE);
            mVLine.setVisibility(View.VISIBLE);
        }

        mTbAutomaticTender.setOnToggleChanged(this);
        mAttvYearMax.setOnCheckChangeListener(this);
        mAttvTimeShortest.setOnCheckChangeListener(this);
        mTvOk.setOnClickListener(this);
        mTbAutomaticTender.toggleOff();
        mCbAgreement.setOnCheckedChangeListener(this);
        mTvUserAgreementLink.setOnClickListener(this);
        mTvUserAgreementLink.setOnClickListener(this);

        mXetReserveMoney.setMaxmumFilter(1000000000,2);

        mPresenter.loadData();

        mIwttvTop.addRightText(R.string.information, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = SupportUrl.getSupportUrlsResponse().getAutoBidNoteH5Url();
                if(TextUtils.isEmpty(url)){
                    return;
                }
                Map<String,String> dataMap = new HashMap<>();
                dataMap.put(BaseWebViewActivity.URL,url );
                dataMap.put(BaseWebViewActivity.TITLE, "");
                dataMap.put(BaseWebViewActivity.USEWEBTITLE, "true");
                dataMap.put(BaseWebViewActivity.TOPVIEWTHEME, "false");
                MyIntent.startActivity(mContext, WebViewActivity.class, dataMap);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SystemUtil.hideKeyboard(mXetReserveMoney);
    }

    @Override
    public void showLoading() {
        showLoadView();
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void reloadData() {
        super.reloadData();
        mPresenter.loadData();
    }

    @Override
    public void processAutomaticOpen(@NonNull AutoBidResponse response) {
        showContentView();
        mStrategyType = response.getStrategyType();
        DecimalFormat format = new DecimalFormat("#0.00");
        mReserveMoney = format.format(response.getReserveBalance());
        mXetReserveMoney.setText(mReserveMoney);
        if(mStrategyType == 1){//期限短
            mAttvTimeShortest.setSelect(true);
        }else{//利率高
            mAttvYearMax.setSelect(true);
        }
        mTbAutomaticTender.toggleOn();
    }

    @Override
    public void processAutomaticClose(@NonNull AutoBidResponse response) {
        showContentView();
        mTbAutomaticTender.toggleOff();
    }

    @Override
    public void processFail(String message) {
        showErrorView(message);
    }

    @Override
    public void processAfterSubmit(boolean forOpen, boolean isSuccess, String message) {
        if(forOpen){
            if(isSuccess){
                finish();
            }
        }else{//关闭自动投标
            if(!isSuccess) {
                mTbAutomaticTender.toggleOn();
            }else{
                //如果关闭成功就清空
                mXetReserveMoney.setText("");
                mAttvTimeShortest.setSelect(false);
                mAttvYearMax.setSelect(false);
            }
        }
        if(!TextUtils.isEmpty(message)) {
            ToastUtil.showInCenter(message);
        }
    }

    @Override
    public void pwdDialogClose(boolean forOpen,boolean isSuccess) {
        if(!forOpen && !isSuccess) {
            mTbAutomaticTender.toggleOn();
        }
    }

    @Override
    public void onToggle(boolean formClick, boolean on) {
        if(on){
            mXetReserveMoney.clearFocus();
            mVToggleContainer.setVisibility(View.VISIBLE);
            mVAgreementContainer.setVisibility(View.VISIBLE);
            mTvOk.setVisibility(View.VISIBLE);
            mCbAgreement.setChecked(true);//重新打开都勾选
            mTvOk.setEnabled(mCbAgreement.isChecked());
        }else{
            mTvOk.requestFocus();
            SystemUtil.HideSoftInput(this);
            mVToggleContainer.setVisibility(View.GONE);
            mVAgreementContainer.setVisibility(View.GONE);
            mTvOk.setVisibility(View.GONE);

            if(formClick && mPresenter.getServerIsOpen()) {
                DialogBuilder.showSimpleDialog(this, getString(R.string.automatic_exit_tip), null,
                        "再想想", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mTbAutomaticTender.toggleOn();
                            }
                        },
                        "立即关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.showPwdDialogForOpen(false, 0, 0d);
                            }
                        });

            }
        }
    }

    @Override
    public void onCheckedChanged(AutomaticTenderTypeView view,boolean fromClick, boolean isChecked) {
        mXetReserveMoney.clearFocus();
        SystemUtil.hideKeyboard(mXetReserveMoney);
        int vId = view.getId();
        switch (vId){
            case R.id.attv_year_max:
                if(isChecked){
                    mAttvTimeShortest.setSelect(false);
                }
                break;
            case R.id.attv_time_shortest:
                if(isChecked){
                    mAttvYearMax.setSelect(false);
                }
                break;
        }
    }


    private void processOkBtn(){
        if(!mCbAgreement.isChecked()){
            return;
        }
        if(!mAttvYearMax.getChecked() && !mAttvTimeShortest.getChecked()) {
            ToastUtil.showInCenter("请选择投资策略");
            return;
        }
        int strategyType = getCurrentStrategy(0);
        Double reserveMoney = mPresenter.getReserveMoney(mXetReserveMoney.getText().toString());
        if(reserveMoney == -1){
            ToastUtil.showInCenter("请输入正确的账号预留金额");
            return;
        }
        mPresenter.showPwdDialogForOpen(true,strategyType,reserveMoney);
    }

    @Override
    public void onClick(View v) {
        mXetReserveMoney.clearFocus();
        SystemUtil.hideKeyboard(mXetReserveMoney);
        int vId = v.getId();
        switch (vId){
            case R.id.tv_ok:
                processOkBtn();
                break;
            case R.id.tv_user_agreement_link:
                Protocol protocol = mPresenter.getProtocol();
                if(protocol == null){
                    return;
                }
                Map<String,String> dataMap = new HashMap<>();
                dataMap.put(BaseWebViewActivity.URL, protocol.getUrl());
                dataMap.put(BaseWebViewActivity.TITLE, protocol.getName());
                dataMap.put(BaseWebViewActivity.USEWEBTITLE, "true");
                dataMap.put(BaseWebViewActivity.TOPVIEWTHEME, "false");
                MyIntent.startActivity(mContext, WebViewActivity.class, dataMap);
                break;
        }

    }

    /***
     * 底部协议是否选中
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mTvOk.setEnabled(mCbAgreement.isChecked());
    }

    /***
     * 获取页面当前策略
     */
    private int getCurrentStrategy(int defaultStrategy){
        int strategyType = defaultStrategy;
        if(mAttvTimeShortest.getChecked()){
            strategyType = 1;//期限短
        }else if(mAttvYearMax.getChecked()){
            strategyType = 2;//利率高
        }
        return strategyType;
    }

    @Override
    public void onBackPressed() {
        mXetReserveMoney.clearFocus();
        SystemUtil.hideKeyboard(mXetReserveMoney);
        if(mTbAutomaticTender.isToggleOn() ) {
            if(!mPresenter.getServerIsOpen() || mStrategyType != getCurrentStrategy(0) || !TextUtils.equals(mReserveMoney,mXetReserveMoney.getText().toString())) {
                DialogBuilder.showSimpleDialog(this, getString(R.string.automatic_close_tip), null, "取消", null, "退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                return;
            }
        }
        super.onBackPressed();
    }


}
