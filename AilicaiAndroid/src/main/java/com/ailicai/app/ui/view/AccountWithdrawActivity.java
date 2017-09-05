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
import android.widget.EditText;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.SpannableUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
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

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 账户提现
 * Created by nanshan on 2017/7/10.
 */
public class AccountWithdrawActivity extends BaseBindActivity {
    @Bind(R.id.confirm_btn)
    Button mConfirmBtn;
    @Bind(R.id.top_title_view)
    IWTopTitleView mTopTitleView;
    @Bind(R.id.index_hot_topic_view)
    RollHotTopicView mIndexHotTopicView;
    @Bind(R.id.current_balance)
    TextView mCurrentBalance;
    @Bind(R.id.roll_out_time)
    TextView mRollOutTime;
    @Bind(R.id.bank_card_name)
    TextView mBankCardNamee;
    @Bind(R.id.input_price_edit)
    EditText mInputPriceEdit;
    @Bind(R.id.input_price_edit_lable)
    TextView mInputPriceEditLable;
    @Bind(R.id.input_error_tips)
    TextView mErrorTips;
    @Bind(R.id.price_del)
    TextView priceDel;

    private CurrentRollOutBaseInfoResponse infoResponse;

    private static final String CASHIERDESKTYPE = "106";

    @Override
    public void onResume() {
        super.onResume();
        if (infoResponse != null){
            ManyiUtils.showKeyBoard(this, mInputPriceEdit);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_account_withdraw;
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

        //请求数据
        initAilicaiNoticeList();
        initBaseInfo();
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
            mIndexHotTopicView.updateView4AlicaiData(AccountWithdrawActivity.this, jsonObject.getBankNotices());
        } else {
            mIndexHotTopicView.setVisibility(View.GONE);
        }
    }

    @OnTextChanged(value = R.id.input_price_edit, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable s) {
        if (checkInputMoney()) {
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
        request.setAccountType("106");
        ServiceSender.exec(this, request, new IwjwRespListener<UserTipsWhenTransactionOutResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onJsonSuccess(UserTipsWhenTransactionOutResponse jsonObject) {
                showContentView();

                DialogBuilder.showSimpleDialog(AccountWithdrawActivity.this,"",jsonObject.getMessageLine1()+"\n"+jsonObject.getMessageLine2(),
                        "取消",null,
                        "确认转出",new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toTransaction();
                            }
                        });
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
        Intent mIntent = new Intent(mContext, AccountTransactionResultActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(CurrentRollOutResultActivity.KEY, object);
        mBundle.putSerializable(AccountTransactionResultActivity.TRANSACTIONTYPE, AccountTransactionResultActivity.WITHDRAW);
        mIntent.putExtras(mBundle);
        mContext.startActivity(mIntent);
    }

    /**
     * check 输入的金额
     *
     * @return
     */
    private boolean checkInputMoney() {
        mErrorTips.setVisibility(View.GONE);
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
            showError("您最多可转 " + CommonUtil.numberFormatWithTwoDigital(infoResponse.getWithdrawBalance()) + " 元");
            return false;
        } else if (Double.parseDouble(money) > infoResponse.getLimit()) {
            if (infoResponse.getLimit() % 10000 == 0) {
                showError("每笔最多可转出 " + CommonUtil.numberFormatWithTwoDigital(infoResponse.getLimit() / 10000) + " 万");
            } else {
                showError("每笔最多可转出" + CommonUtil.numberFormatWithTwoDigital(infoResponse.getLimit()));
            }
            return false;
        }
        return true;
    }

    private void showError(String errorMessage) {
        SpannableUtil spanUtil = new SpannableUtil(this);
        SpannableStringBuilder builder = spanUtil.getSpannableString(errorMessage, R.style.text_14_e84a01);
        mCurrentBalance.setText(builder);
    }

    private void showBalance() {
        if(infoResponse != null){
            SpannableUtil spanUtil = new SpannableUtil(this);
            SpannableStringBuilder builder = spanUtil.getSpannableString("账户余额可用 ", CommonUtil.amountWithTwoAfterPoint(infoResponse.getWithdrawBalance()), " 元",
                    R.style.text_14_8a000000, R.style.text_14_8a000000, R.style.text_14_8a000000);
            mCurrentBalance.setText(builder);
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
        } else if (infoResponse.getWithdrawBalance() > 0 && Double.parseDouble(money) > infoResponse.getWithdrawBalance()) {
            ToastUtil.show("您最多可转出账户可用" + CommonUtil.formatMoneyForFinance(infoResponse.getWithdrawBalance()) + "元");
            return false;
        }
        return true;
    }

    public void bindViewData(CurrentRollOutBaseInfoResponse jsonObject) {
        this.infoResponse = jsonObject;
        mRollOutTime.setText(Html.fromHtml(getString(R.string.roll_out_time_text,jsonObject.getGiveDate())));
        SpannableUtil spanUtil = new SpannableUtil(this);
        SpannableStringBuilder builder = null;

        mBankCardNamee.setText(Html.fromHtml(getString(R.string.account_withdraw_bank_card_text, jsonObject.getBankName(), jsonObject.getCardNo())));
        builder = spanUtil.getSpannableString("可用余额 ", CommonUtil.amountWithTwoAfterPoint(jsonObject.getWithdrawBalance()), " 元",
                R.style.text_14_757575, R.style.text_14_757575, R.style.text_14_757575);
        mCurrentBalance.setText(builder);
        mInputPriceEditLable.setText(jsonObject.getHint());
    }

    /**
     * 页面数据获取请求
     */
    public void initBaseInfo() {
        showLoadTranstView();
        CurrentRollOutBaseInfoRequest request = new CurrentRollOutBaseInfoRequest();
        request.setAccountType(CASHIERDESKTYPE);
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

    @Override
    protected void onPause() {
        super.onPause();
        SystemUtil.hideKeyboard(mInputPriceEdit);
    }

    private void toTransaction(){
        OutCurrentPay.CurrentPayInfo currentPayInfo = new OutCurrentPay.CurrentPayInfo();
        currentPayInfo.setAmount(Double.valueOf(mInputPriceEdit.getText().toString()));
        currentPayInfo.setAccountType(CASHIERDESKTYPE);
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
