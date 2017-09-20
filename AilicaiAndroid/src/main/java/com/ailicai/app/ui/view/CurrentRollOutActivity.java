package com.ailicai.app.ui.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.SpannableUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.bean.Protocol;
import com.ailicai.app.model.request.AilicaiNoticeListOnRollRequest;
import com.ailicai.app.model.request.CurrentRollOutBaseInfoRequest;
import com.ailicai.app.model.request.UserTipsWhenTransactionOutRequest;
import com.ailicai.app.model.response.AilicaiNoticeListOnRollReponse;
import com.ailicai.app.model.response.CurrentRollOutBaseInfoResponse;
import com.ailicai.app.model.response.SaleHuoqibaoResponse;
import com.ailicai.app.model.response.UserTipsWhenTransactionOutResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.buy.IwPwdPayResultListener;
import com.ailicai.app.ui.buy.OutCurrentPay;
import com.ailicai.app.widget.DialogBuilder;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.RollHotTopicView;
import com.huoqiu.framework.util.ManyiUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 钱包转出
 * Created by Gerry on 2015/12/29.
 */
public class CurrentRollOutActivity extends BaseBindActivity implements View.OnClickListener {
    @Bind(R.id.confirm_btn)
    Button mConfirmBtn;
    @Bind(R.id.top_title_view)
    IWTopTitleView mTopTitleView;
    @Bind(R.id.index_hot_topic_view)
    RollHotTopicView mIndexHotTopicView;
    @Bind(R.id.current_balance)
    TextView mCurrentBalance;
    @Bind(R.id.current_balance_all_of_out)
    TextView mCurrentBalanceAllOfOut;
    @Bind(R.id.roll_out_time)
    TextView mRollOutTime;
    //@Bind(R.id.bank_card_name)
    //TextView mBankCardNamee;
    @Bind(R.id.input_price_edit)
    EditText mInputPriceEdit;
    @Bind(R.id.input_price_edit_lable)
    TextView mInputPriceEditLable;
    @Bind(R.id.price_del)
    TextView priceDel;
    @Bind(R.id.agreement_layout)
    LinearLayout mAgreementLayout;
    @Bind(R.id.agreement_checkbox)
    CheckBox mAgreementCheckbox;

    @Bind(R.id.bank_name_text)
    TextView bankNameText;
    @Bind(R.id.account_name_text)
    TextView accountNameText;
    @Bind(R.id.account_desc_text)
    TextView accountDescText;
    @Bind(R.id.account_check_box)
    CheckBox accountCheckBox;
    @Bind(R.id.bank_check_box)
    CheckBox bankCheckBox;
    @Bind(R.id.bank_layout)
    RelativeLayout bankLayout;
    @Bind(R.id.account_layout)
    RelativeLayout accountLayout;

    private CurrentRollOutBaseInfoResponse infoResponse;
    private ProtocolHelper protocolHelper;
    private int toType;
    private static final String ACCOUNT = "2";//存管账户（这里的1，2由服务器定义）
    private static final String SECURITYCARD = "1";//银行卡
    private String mTransactionOutType = "1";

    private boolean isBeforeFifteen ;

    @Override
    public void onResume() {
        super.onResume();
        if (infoResponse != null) {
            ManyiUtils.showKeyBoard(this, mInputPriceEdit);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_current_roll_out;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mTopTitleView.setTitleOnClickListener(new IWTopTitleView.TopTitleOnClickListener() {
            @Override
            public boolean onBackClick() {
                //SystemUtil.HideSoftInput(CurrentRollOutActivity.this);
                SystemUtil.hideKeyboard(mInputPriceEdit);
                finish();
                return true;
            }
        });
        mConfirmBtn.setEnabled(false);
        accountLayout.setOnClickListener(this);
        bankLayout.setOnClickListener(this);
        accountCheckBox.setClickable(false);
        bankCheckBox.setClickable(false);
        //请求数据
//        initAilicaiNoticeList();//7.2去掉公告轮播展示
        initBaseInfo();
        initProtocol();
    }

    private void initAilicaiNoticeList() {
        showLoadTranstView();
        AilicaiNoticeListOnRollRequest request = new AilicaiNoticeListOnRollRequest();
        ServiceSender.exec(this, request, new IwjwRespListener<AilicaiNoticeListOnRollReponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onJsonSuccess(AilicaiNoticeListOnRollReponse jsonObject) {
                showContentView();
                bindAlicaiNoticeData(jsonObject);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showContentView();
                ToastUtil.showInCenter(errorInfo);
            }
        });
    }

    private void bindAlicaiNoticeData(AilicaiNoticeListOnRollReponse jsonObject) {
        if (jsonObject.getBankNotices() != null && !jsonObject.getBankNotices().isEmpty()) {
            mIndexHotTopicView.setVisibility(View.VISIBLE);
            mIndexHotTopicView.updateView4AlicaiData(CurrentRollOutActivity.this, jsonObject.getBankNotices());
        } else {
            mIndexHotTopicView.setVisibility(View.GONE);
        }
    }

    @OnTextChanged(value = R.id.input_price_edit, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable s) {
        if (checkInputMoney() && mAgreementCheckbox.isChecked()) {
            mConfirmBtn.setEnabled(true);
            showBalance();
        } else {
            mConfirmBtn.setEnabled(false);
        }
    }

    @OnTextChanged(value = R.id.input_price_edit, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + 3);
                mInputPriceEdit.setText(s);
                mInputPriceEdit.setSelection(s.length());
            }
        }
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            mInputPriceEdit.setText(s);
            mInputPriceEdit.setSelection(2);
        }
        /*
        //0开始后面不能输入
        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                mInputPriceEdit.setText(s.subSequence(0, 1));
                mInputPriceEdit.setSelection(1);
                return;
            }
        }
        */

        //0789 -> 789,00->0
        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".") && !s.toString().substring(1, 2).equals("0")) {
                mInputPriceEdit.setText(s.subSequence(1, s.toString().trim().length()));
                mInputPriceEdit.setSelection(s.length() - 1);
                return;
            } else if (s.toString().substring(1, 2).equals("0")) {
                mInputPriceEdit.setText(s.subSequence(0, 1));
                mInputPriceEdit.setSelection(1);
                return;
            }
        }
    }

    @OnClick(R.id.price_del)
    public void onEditTextDelClick(View v) {
        if (!"".equals(mInputPriceEdit.getText().toString())) {
            mInputPriceEdit.setText("");
            showBalance();
        }
    }

    @OnClick(R.id.current_balance_all_of_out)
    public void allOfOut(View v) {
        mInputPriceEdit.setText(infoResponse.getWithdrawBalance()+"");
        showBalance();
    }

    @OnClick(R.id.input_price_edit)
    public void onEditTextClick(View v) {
        if (!"".equals(mInputPriceEdit.getText().toString())) {
            mInputPriceEdit.setSelection(mInputPriceEdit.getText().length());
        }
    }

    @Nullable
    @OnClick(R.id.confirm_btn)
    public void onConfirmClick() {
        if (!checkInputMoneyConfirm()) {
            return;
        }

        UserTipsWhenTransactionOutRequest request = new UserTipsWhenTransactionOutRequest();
        request.setAccountType("101");
        if (accountCheckBox.isChecked()) {
            mTransactionOutType = ACCOUNT;
        } else if (bankCheckBox.isChecked()){
            mTransactionOutType = SECURITYCARD;
        }
        request.setPayMethod(mTransactionOutType);
        ServiceSender.exec(this, request, new IwjwRespListener<UserTipsWhenTransactionOutResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onJsonSuccess(UserTipsWhenTransactionOutResponse jsonObject) {
                showContentView();

                if (accountCheckBox.isChecked()) {
                    if (jsonObject.isBeforeFifteen()){
                        isBeforeFifteen = true;
                        toTransation();
                    }else{
                        isBeforeFifteen = false;
                        showTransactionOutUserInfo(jsonObject);
                    }
                } else if (bankCheckBox.isChecked()){
                    showTransactionOutUserInfo(jsonObject);
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showContentView();
                ToastUtil.show(errorInfo);
            }
        });

    }

    public void startActivity(SaleHuoqibaoResponse object) {
        finish();
        Intent mIntent = new Intent(mContext, CurrentRollOutResultActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(CurrentRollOutResultActivity.KEY, object);
        mBundle.putBoolean(CurrentRollOutResultActivity.ISFIFTEEN, isBeforeFifteen);
        mBundle.putInt(CurrentRollOutResultActivity.TOTYPE, toType);
        mIntent.putExtras(mBundle);
        mContext.startActivity(mIntent);
    }

    /**
     * check 输入的金额
     *
     * @return
     */
    private boolean checkInputMoney() {
        mInputPriceEditLable.setVisibility(View.GONE);
        mCurrentBalance.setVisibility(View.VISIBLE);
        priceDel.setVisibility(View.VISIBLE);
        String money = mInputPriceEdit.getText().toString();
        if ("".equals(money)) {
            mInputPriceEditLable.setVisibility(View.VISIBLE);
            showBalance();
            priceDel.setVisibility(View.GONE);
            return false;
        } else if (!CommonUtil.isMoneyNumber(money)) {
            //ToastUtil.show("请输入正确的金额");
            return false;
        } else if (Double.parseDouble(money) <= 0) {
            //ToastUtil.show("请输入正确的金额");
            return false;
        } else if (infoResponse.getWithdrawBalance() >= 0 && Double.parseDouble(money) > infoResponse.getWithdrawBalance()) {
            showError("当前最多可转出 " + CommonUtil.amountWithTwoAfterPoint(infoResponse.getWithdrawBalance()) + " 元");
            return false;
        } else if (!accountCheckBox.isChecked() && Double.parseDouble(money) > infoResponse.getLimit()) {
            if (infoResponse.getLimit() % 10000 == 0) {
                showError("每笔最多可转出 " + CommonUtil.numberFormat(infoResponse.getLimit() / 10000) + " 万");
            } else {
                showError("每笔最多可转出 " + CommonUtil.amountWithTwoAfterPoint(infoResponse.getLimit()));
            }
            return false;
        }
        showBalance();
        return true;
    }

    private void showError(String errorMessage) {
        SpannableUtil spanUtil = new SpannableUtil(this);
        SpannableStringBuilder builder = spanUtil.getSpannableString(errorMessage, R.style.text_14_e84a01);
        mCurrentBalance.setText(builder);
        mCurrentBalanceAllOfOut.setVisibility(View.GONE);
        mCurrentBalance.setVisibility(View.VISIBLE);
    }

    private void showBalance() {
        if (infoResponse != null) {
            SpannableUtil spanUtil = new SpannableUtil(this);
            String balance = CommonUtil.amountWithTwoAfterPoint(infoResponse.getWithdrawBalance());
            SpannableStringBuilder builder = spanUtil.getSpannableString("活期宝余额 ", balance, " 元",
                    R.style.text_14_757575, R.style.text_14_757575, R.style.text_14_757575);
            mCurrentBalance.setText(builder);
            mCurrentBalanceAllOfOut.setVisibility(View.VISIBLE);
            mCurrentBalance.setVisibility(View.VISIBLE);
        }

    }

    /**
     * check 输入的金额
     *
     * @return
     */
    private boolean checkInputMoneyConfirm() {
        if (infoResponse == null) {
            return false;
        }
        String money = mInputPriceEdit.getText().toString();
        if (!CommonUtil.isMoneyNumber(money)) {
            ToastUtil.show("请输入正确的金额");
            return false;
        } else if (Double.parseDouble(money) <= 0) {
            ToastUtil.show("请输入正确的金额");
            return false;
        } else if (!accountCheckBox.isChecked() && infoResponse.getWithdrawBalance() > 0 && Double.parseDouble(money) > infoResponse.getWithdrawBalance()) {
            ToastUtil.show("您最多可转出可用余额 " + CommonUtil.amountWithTwoAfterPoint(infoResponse.getWithdrawBalance()) + " 元");
            return false;
        }
        return true;
    }

    public void bindViewData(CurrentRollOutBaseInfoResponse jsonObject) {
        this.infoResponse = jsonObject;
        verifyProtocolListLogical(jsonObject.getProtocolList());
        String cardNo = jsonObject.getCardNo();
        //mBankCardNamee.setText(Html.fromHtml(getString(R.string.roll_out_bank_card_text, jsonObject.getBankName(), cardNo)));
        SpannableUtil spanUtil = new SpannableUtil(this);
        String balance = CommonUtil.amountWithTwoAfterPoint(infoResponse.getWithdrawBalance());
        SpannableStringBuilder builder = spanUtil.getSpannableString("活期宝余额 ", balance, " 元",
                R.style.text_14_757575, R.style.text_14_757575, R.style.text_14_757575);
        mCurrentBalance.setText(builder);
        mInputPriceEditLable.setText(jsonObject.getHint());
        mCurrentBalanceAllOfOut.setVisibility(View.VISIBLE);
        setRollOutType(jsonObject);
    }

    public void setRollOutType(CurrentRollOutBaseInfoResponse jsonObject) {
        accountNameText.setText("账户余额");
        accountDescText.setText("当前可用余额 " + CommonUtil.amountWithTwoAfterPoint(jsonObject.getDepositoryBalance()) + " 元");
        bankNameText.setText(jsonObject.getBankName() + " (尾号" + jsonObject.getCardNo() + ")");
        accountCheckBox.setChecked(true);
        bankCheckBox.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bank_layout:
                //银行卡转出选项
                accountCheckBox.setChecked(false);
                bankCheckBox.setChecked(true);
                break;
            case R.id.account_layout:
                //账户余额转出选项
                accountCheckBox.setChecked(true);
                bankCheckBox.setChecked(false);
                break;
        }
    }

    /**
     * 页面数据获取请求
     */
    public void initBaseInfo() {
        showLoadTranstView();
        CurrentRollOutBaseInfoRequest request = new CurrentRollOutBaseInfoRequest();
        request.setAccountType("101");
        ServiceSender.exec(this, request, new IwjwRespListener<CurrentRollOutBaseInfoResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onJsonSuccess(CurrentRollOutBaseInfoResponse jsonObject) {
                showContentView();
                String msg = jsonObject.getMessage();
                int code = jsonObject.getErrorCode();
                if (!"".equals(msg)) {
                    //ToastUtil.showInCenter(mContext, msg);
                }
                bindViewData(jsonObject);
                SystemUtil.showKeyboard(mInputPriceEdit);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showContentView();
                ToastUtil.show(errorInfo);
            }
        });
    }

    private void initProtocol() {
        protocolHelper = new ProtocolHelper(this, ProtocolHelper.TYPE_WALLET_ROLL_IN);
    }

    /**
     * 用户协议点击事件
     */
    @Nullable
    @OnClick(R.id.user_agreement_link)
    public void onUserAgreementLink() {
        ManyiUtils.closeKeyBoard(this, mInputPriceEdit);
        if (infoResponse != null) {
            protocolHelper.setProtocols(infoResponse.getProtocolList());
        }
        protocolHelper.loadProtocol(true);
    }

    @OnCheckedChanged(R.id.agreement_checkbox)
    public void onBoxChange(CheckBox box, boolean isChecked) {
        if (isChecked && checkInputMoney()) {
            mConfirmBtn.setEnabled(true);
        } else {
            mConfirmBtn.setEnabled(false);
        }

    }

    @OnCheckedChanged(R.id.account_check_box)
    public void onAccountBoxChange(CheckBox box, boolean isChecked) {
        if (isChecked) {
            mInputPriceEditLable.setText("请输入金额");
            if (checkInputMoney()) {
                mConfirmBtn.setEnabled(true);
            } else {
                mConfirmBtn.setEnabled(false);
            }
            mRollOutTime.setText(Html.fromHtml(getResources().getString(R.string.roll_out_time_text,
                    infoResponse.getToDepositoryDate())));
        }

    }

    @OnCheckedChanged(R.id.bank_check_box)
    public void onBankBoxChange(CheckBox box, boolean isChecked) {
        if (isChecked) {
            mInputPriceEditLable.setText(infoResponse.getHint());
            if (checkInputMoney()) {
                mConfirmBtn.setEnabled(true);
            } else {
                mConfirmBtn.setEnabled(false);
            }
            mRollOutTime.setText(Html.fromHtml(getResources().getString(R.string.roll_out_time_text,
                    infoResponse.getGiveDate())));
        }

    }

    @Override
    public void verifyProtocolListLogical(List<Protocol> list) {
        if (list != null && list.size() > 0) {
            mAgreementLayout.setVisibility(View.VISIBLE);
        }else{
            mAgreementLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SystemUtil.hideKeyboard(mInputPriceEdit);
    }

    private void showTransactionOutUserInfo(UserTipsWhenTransactionOutResponse jsonObject){

        DialogBuilder.showSimpleDialog(CurrentRollOutActivity.this,"",jsonObject.getMessageLine1()+"\n"+jsonObject.getMessageLine2(),
                "取消",null,
                "确认转出",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toTransation();
                    }
                });

    }

    private void toTransation(){
        OutCurrentPay.CurrentPayInfo currentPayInfo = new OutCurrentPay.CurrentPayInfo();
        currentPayInfo.setAmount(Double.valueOf(mInputPriceEdit.getText().toString()));
        //收银台类型：101-活期宝；106-用户账户
        currentPayInfo.setAccountType("101");
        //支付到的账户类型 1-银行卡；2-账户余额 说明：活期宝收银台需指定
        if (accountCheckBox.isChecked()) {
            currentPayInfo.setPayMethod("2");
            toType = 2;
            mTransactionOutType = ACCOUNT;
        } else if (bankCheckBox.isChecked()) {
            currentPayInfo.setPayMethod("1");
            toType = 1;
            mTransactionOutType = SECURITYCARD;
        }

        OutCurrentPay outCurrentPay = new OutCurrentPay(this, currentPayInfo, new IwPwdPayResultListener<SaleHuoqibaoResponse>() {
            @Override
            public void onPayComplete(SaleHuoqibaoResponse object) {
                startActivity(object);
            }

            @Override
            public void onPayStateDelay(String msgInfo, SaleHuoqibaoResponse object) {
                startActivity(object);
            }

            @Override
            public void onPayFailInfo(String msgInfo, String errorCode, SaleHuoqibaoResponse object) {
                startActivity(object);
            }

            @Override
            public void onPayPwdTryAgain() {
                onConfirmClick();
            }
        });
        outCurrentPay.pay();
        EventLog.upEventLog("201610281", "sub", "out_syt");

    }


}
