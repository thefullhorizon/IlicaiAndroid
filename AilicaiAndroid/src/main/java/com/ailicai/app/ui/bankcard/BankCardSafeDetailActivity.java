package com.ailicai.app.ui.bankcard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.eventbus.FinancePayEvent;
import com.ailicai.app.model.response.BankCardDetailResponse;
import com.ailicai.app.ui.account.OpenAccountWebViewActivity;
import com.ailicai.app.ui.asset.FinanceUpgradePresenter;
import com.ailicai.app.ui.bankcard.presenter.BankCardSafeDetailPresenter;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.buy.IwPwdPayResultListener;
import com.ailicai.app.ui.buy.UnbindPwdCheckDialog;
import com.ailicai.app.ui.html5.SupportUrl;
import com.ailicai.app.ui.login.AccountInfo;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.login.UserInfoBase;
import com.ailicai.app.ui.login.UserManager;
import com.huoqiu.framework.util.CheckDoubleClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * name: BankCardSafeDetailActivity <BR>
 * description: 安全卡详情 <BR>
 * create date: 2016/1/12
 *
 * @author: IWJW Zhou Xuan
 */
public class BankCardSafeDetailActivity extends BaseBindActivity {

    private BankCardSafeDetailPresenter presenter;
    private BankCardDetailResponse response;
    private String bankCardId;
    private String bankCode;

    @Bind(R.id.imageViewBankIcon)
    ImageView imageViewBankIcon;
    @Bind(R.id.textViewBankName)
    TextView textViewBankName;
    @Bind(R.id.textViewCardDesc)
    TextView textViewCardDesc;
    @Bind(R.id.textViewPayMax)
    TextView textViewPayMax;
    @Bind(R.id.textViewWhatISafeCard)
    TextView textViewWhatISafeCard;

    @Override
    public int getLayout() {
        return R.layout.activity_bankcard_safe_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntentOrSavedInstanceState(savedInstanceState);
        presenter = new BankCardSafeDetailPresenter(this);
        presenter.init();

        // 爱理财系统升级弹框
        FinanceUpgradePresenter financeUpgradePresenter = new FinanceUpgradePresenter();
        financeUpgradePresenter.httpForSystemIsFix(this);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("bankCardId", bankCardId);
        outState.putString("bankCode", bankCode);
    }

    private void getDataFromIntentOrSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            bankCardId = savedInstanceState.getString("bankCardId");
            bankCode = savedInstanceState.getString("bankCode");
        } else {
            bankCardId = getIntent().getStringExtra("bankCardId");
            bankCode = getIntent().getStringExtra("bankCode");
        }
    }

    public String getBankCardId() {
        return bankCardId;
    }

    public void setData(BankCardDetailResponse detailResponse) {
        imageViewBankIcon.setBackgroundResource(presenter.getBankIconResId(bankCode));
        textViewBankName.setText(detailResponse.getBankName());
        textViewCardDesc.setText(detailResponse.getCardTypeAndNoDesc());
        textViewPayMax.setText(detailResponse.getPayMax());
        response = detailResponse;
    }

    @Override
    public void reloadData() {
        super.reloadData();
        presenter.init();
    }

    @OnClick(R.id.tvLimitDesc)
    void onBankLimitDescClick() {
        Map<String, String> dataMap = ObjectUtil.newHashMap();
        dataMap.put(WebViewActivity.TITLE, getResources().getString(R.string.support_cards));
        dataMap.put(WebViewActivity.NEED_REFRESH, "0");
        dataMap.put(WebViewActivity.URL, SupportUrl.getSupportUrlsResponse().getSupportcardsByAllUrl() + "?channel=2");
        MyIntent.startActivity(this, WebViewActivity.class, dataMap);
    }

    @OnClick(R.id.textViewChange)
    void onChangeSafeCardClick() {
        if (CheckDoubleClick.isFastDoubleClick(1000)) {
            return;
        }
        goToExSafeCardValid();
    }


    private void goToExSafeCardValid() {
        if (isPropertyBiggerThanChangeLimit()) {
            goToPropertyBiggerThanChangeLimit();
        } else {
            goToPropertyLessThanChangeLimit();
        }
    }

    private boolean isPropertyBiggerThanChangeLimit() {
        double property = response.getTotalAsset();
        return property > response.getChangeLimit();
    }

    private void goToPropertyBiggerThanChangeLimit() {
        Intent intent = new Intent(this, ExSafeCardValidBiggerThanChangeLimitActivity.class);
        intent.putExtra("property", response.getTotalAsset());
        intent.putExtra("changeLimit",response.getChangeLimit());
        startActivity(intent);
    }

    private void goToPropertyLessThanChangeLimit() {

        UnbindPwdCheckDialog unbindPwdCheckDialog = new UnbindPwdCheckDialog(this, bankCardId, new IwPwdPayResultListener() {
            @Override
            public void onPayPwdTryAgain() {
                goToPropertyLessThanChangeLimit();
            }

            @Override
            public void onPayComplete(Object object) {

                setHasNoSafeCard();
                goToChangeSafeCard();

                BankCardListActivity.NEED_MANUAL_REFRESH_LIST = true;
                finish();
            }

            @Override
            public void onPayStateDelay(String msgInfo, Object object) {

            }

            @Override
            public void onPayFailInfo(String msgInfo, String errorCode, Object object) {

            }
        });
        unbindPwdCheckDialog.show();
    }

    private void setHasNoSafeCard() {

        // 设置UserInfo中
        long userId = UserInfo.getInstance().getUserId();
        UserInfoBase infoBase = UserManager.getInstance(MyApplication.getInstance()).getUserByUserId(userId);
        infoBase.setHasSafeCard(0);
        UserManager.getInstance(MyApplication.getInstance()).saveUser(infoBase);

        // 设置accountInfo中无安全卡
        AccountInfo.setHasSafeCard(false);
    }

    private void goToChangeSafeCard() {
        // 换安全卡
        OpenAccountWebViewActivity.goToBindNewSafeCard(this);
    }

    private void goToExSafeCardInValid() {
        Map<String, String> dataMap = ObjectUtil.newHashMap();
        dataMap.put(WebViewActivity.TITLE, "原安全卡不可用");
        dataMap.put(WebViewActivity.URL, response.getChangeSafeCardUrl());
        dataMap.put(WebViewActivity.NEED_REFRESH, String.valueOf(0));
        MyIntent.startActivity(this, WebViewActivity.class, dataMap);
    }

    @OnClick(R.id.textViewWhatISafeCard)
    void onSafeCardDescClick() {
        if (CheckDoubleClick.isFastDoubleClick(1000)) {
            return;
        }
        goToSafeCardDescWebview();
    }

    @OnClick(R.id.tvHasLost)
    void onSafeCardHasLostClick() {
        if (CheckDoubleClick.isFastDoubleClick(1000)) {
            return;
        }
        goToExSafeCardInValid();
    }

    private void goToSafeCardDescWebview() {
        Map<String, String> dataMap = ObjectUtil.newHashMap();
        dataMap.put(WebViewActivity.TITLE, "什么是安全卡？");
        dataMap.put(WebViewActivity.URL, SupportUrl.getSupportUrlsResponse().getSafecardExplainUrl());
        dataMap.put(WebViewActivity.NEED_REFRESH, String.valueOf(0));
        MyIntent.startActivity(this, WebViewActivity.class, dataMap);
    }

    // 有转入转出动作的时候，刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleFinancePayEvent(FinancePayEvent event) {
        reloadData();
    }
}
