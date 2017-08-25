package com.ailicai.app.ui.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.MathUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.SpannableUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.eventbus.FinancePayEvent;
import com.ailicai.app.eventbus.MoneyChangeEvent;
import com.ailicai.app.model.bean.Protocol;
import com.ailicai.app.model.request.CalActualPayRequest;
import com.ailicai.app.model.request.RegularPayBaseInfoRequest;
import com.ailicai.app.model.response.AdvanceDepositAndApplyAppResponse;
import com.ailicai.app.model.response.BuyDingqibaoResponse;
import com.ailicai.app.model.response.CalActualPayResponse;
import com.ailicai.app.model.response.RegularPayBaseInfoResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.buy.BuyRegularPay;
import com.ailicai.app.ui.buy.IwPwdPayResultListener;
import com.ailicai.app.ui.buy.RegularReChangePay;
import com.ailicai.app.ui.html5.SupportUrl;
import com.ailicai.app.widget.DialogBuilder;
import com.ailicai.app.widget.IWTopTitleView;
import com.huoqiu.framework.util.ManyiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 购买转让房产宝收银台
 * Created by ZhouXuan on 2016/08/01.
 */
public class BuyTransferPayActivity extends BaseBindActivity {

    public static final int REQUEST_CODE_SELECT_VOUCHER = 1000;
    @Bind(R.id.regular_balance)
    TextView mRegularBalance;
    @Bind(R.id.input_error_tips)
    TextView mErrorTips;
    @Bind(R.id.confirm_btn)
    Button mConfirmBtn;
    @Bind(R.id.top_title_view)
    IWTopTitleView mTopTitleView;
    @Bind(R.id.input_price_edit)
    EditText mInputPriceEdit;
    @Bind(R.id.input_price_edit_lable)
    TextView mInputPriceEditLable;
    @Bind(R.id.agreement_layout)
    RelativeLayout mAgreementLayout;
    @Bind(R.id.agreement_checkbox)
    CheckBox mAgreementCheckbox;
    @Bind(R.id.tv_profit_text)
    TextView tvProfitText;
    @Bind(R.id.tv_clear)
    TextView tvClear;
    @Bind(R.id.tv_leave_account)
    TextView tvLeaveAccount;
    @Bind(R.id.rl_my_account)
    RelativeLayout rlMyAccount;
    @Bind(R.id.tv_last_money_text)
    TextView tvLastMoneyText;
    @Bind(R.id.tv_real_pay_count)
    TextView tv_real_pay_count;
    @Bind(R.id.ll_real_pay)
    LinearLayout llRealPay;
    @Bind(R.id.line_account)
    View lineAccount;
    @Bind(R.id.tv_all_buy)
    TextView tvAllBuy;

    @Bind(R.id.rl_max_value)
    RelativeLayout mMaxValueLayout;
    @Bind(R.id.tv_max_value_per_time)
    TextView mMaxValue;

    private RegularPayBaseInfoResponse infoResponse;
    private boolean isLast;//是否是最后一笔

    @Override
    public int getLayout() {
        return R.layout.activity_buy_transfer_pay;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        EventBus.getDefault().register(this);
        mTopTitleView.setTitleOnClickListener(new IWTopTitleView.TopTitleOnClickListener() {
            @Override
            public boolean onBackClick() {
                //SystemUtil.HideSoftInput(RegularPayActivity.this);
                SystemUtil.hideKeyboard(mInputPriceEdit);
                finish();
                return true;
            }
        });
        mConfirmBtn.setEnabled(false);
        initBaseInfo();
        addEditorActionListener();
        SpannableUtil spannableUtil = new SpannableUtil(this);
        SpannableStringBuilder builder = spannableUtil.getSpannableString("预计收益 ", "0.00", " 元", R.style.text_12_757575, R.style.text_12_757575, R.style.text_13_757575);
        tvProfitText.setText(builder);
        initProtocol();
    }

    private ProtocolHelper protocolHelper;

    private void initProtocol() {
        protocolHelper = new ProtocolHelper(this, ProtocolHelper.TYPE_TRANSFER_PAY);
    }

    private void addEditorActionListener() {
        mInputPriceEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    SystemUtil.hideKeyboard(mInputPriceEdit);
                }
                return false;
            }
        });
    }

    @OnClick(R.id.tv_clear)
    public void clearEditText() {
        mInputPriceEdit.setText("");
        LogUtil.e("lyn", "clearEditText");
    }

    /**
     * 点击全部购买
     */
    @OnClick(R.id.tv_all_buy)
    public void clickAllBuy() {

//        全部购买：
//        act_k=804
//        act_v=”qbgm”

        // 上报打点
        EventLog.upEventLog("804", "qbgm");

        if (infoResponse == null) {
            return;
        }
        //可用余额
        double availableBalance = infoResponse.getAvailableBalance();
        //起购金额
        double minAmount = infoResponse.getMinAmount();
        //投标单位
        double bidUnit = infoResponse.getBidUnit();
        //剩余额度
        double biddableAmount = infoResponse.getBiddableAmount();
        if (biddableAmount < minAmount) {
            //剩余额度小于起购金额
            return;
        }
        if (availableBalance > minAmount) {
            //可用余额大于起购金额
            //可用余额减去起购金额对投标单位的取余整数
            int intAvailable = (int) ((availableBalance - minAmount) / bidUnit);
            //剩余额度减去起购金额对投标单位的取余整数
            int intBiddable = (int) ((biddableAmount - minAmount) / bidUnit);
            //二者取其最小值
            if (intAvailable >= intBiddable) {
                mInputPriceEdit.setText(MathUtil.subZeroAndDot(String.valueOf(intBiddable * bidUnit + minAmount)));
            } else {
                mInputPriceEdit.setText(MathUtil.subZeroAndDot(String.valueOf(intAvailable * bidUnit + minAmount)));
            }
        } else {
            //可用余额小于起购金额(since 5.8 不显示全额购买按钮)
            mInputPriceEdit.setText(MathUtil.subZeroAndDot(String.valueOf(availableBalance)));
        }

    }

    /**
     * 用户协议点击事件
     */
    @Nullable
    @OnClick(R.id.user_agreement_link)
    public void onUserAgreementLink() {
        //用户协议：
        //act_k=804
        //act_v=”yhxy”
        // 上报打点
        EventLog.upEventLog("804", "yhxy");
        // 关闭键盘
        ManyiUtils.closeKeyBoard(this, mInputPriceEdit);
        if (infoResponse != null) {
            protocolHelper.setProtocols(infoResponse.getProtocolList());
        }
        protocolHelper.loadProtocol(true);
    }

    @OnCheckedChanged(R.id.agreement_checkbox)
    public void OnBoxChange(CheckBox box, boolean isChecked) {

        if (infoResponse.getBiddableAmount() < infoResponse.getMinAmount()) {
            //剩余额度小于起购金额 最后一笔
            if (isChecked) {
                mConfirmBtn.setEnabled(true);
            } else {
                mConfirmBtn.setEnabled(false);
            }
        } else {
            // 非最后一笔
            if (isChecked && checkInputMoney()) {
                mConfirmBtn.setEnabled(true);
            } else {
                mConfirmBtn.setEnabled(false);
            }
        }
    }


    @OnTextChanged(value = R.id.input_price_edit, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged(Editable s) {
        String input = s.toString();
        if (input.startsWith("0") && input.length() > 1 && !input.contains(".")) {
            input = input.substring(1, input.length());
            mInputPriceEdit.setText(input);
            mInputPriceEdit.setSelection(input.length());
            return;
        }
        if (infoResponse == null) {
            return;
        }
        if (infoResponse.getBiddableAmount() > 0) {
            //剩余额度大于0
            if (infoResponse.getBiddableAmount() < infoResponse.getMinAmount()) {
                //剩余额度小于起购金额
                if (mAgreementCheckbox.isChecked()) {
                    mConfirmBtn.setEnabled(true);
                } else {
                    mConfirmBtn.setEnabled(false);
                }

                // 如果是输入0。按钮仍然不可点
                if (!TextUtils.isEmpty(s.toString()) && Double.parseDouble(s.toString()) <= 0) {
                    mConfirmBtn.setEnabled(false);
                }

            } else {
                //剩余额度大于等于起购金额
                if (checkInputMoney() && mAgreementCheckbox.isChecked()) {
                    mConfirmBtn.setEnabled(true);
                } else {
                    mConfirmBtn.setEnabled(false);
                }
            }
        } else {
            // 剩余额度为0
            checkInputMoney();
            mConfirmBtn.setEnabled(false);
        }
        // 计算收益
        calculateProfit();
        // 计算实际需要支付
        calculateRealPay();
        if (TextUtils.isEmpty(mInputPriceEdit.getText().toString())) {
            tvClear.setVisibility(View.GONE);
        } else {
            tvClear.setVisibility(View.VISIBLE);
            mInputPriceEdit.setSelection(mInputPriceEdit.getText().length());
        }
    }

    /**
     * 计算收益
     */
    private void calculateProfit() {
        String moneyCountString = mInputPriceEdit.getText().toString();
        if (TextUtils.isEmpty(moneyCountString)) {
            SpannableUtil spannableUtil = new SpannableUtil(this);
            SpannableStringBuilder builder = spannableUtil.getSpannableString("预计收益 ", "0.00", " 元", R.style.text_12_757575, R.style.text_12_757575, R.style.text_12_757575);
            tvProfitText.setText(builder);
        } else {
            double moneyCount = Double.parseDouble(moneyCountString);
            if (moneyCount > 0) {
                //金额*天数*年利率/年的天数
                double normalProfit = moneyCount * infoResponse.getOriLoanTerm() * infoResponse.getYearInterestRate() / 100 / 360;
                //无加息券
                SpannableUtil spannableUtil = new SpannableUtil(this);
                SpannableStringBuilder builder = spannableUtil.getSpannableString("预计收益 ", MathUtil.saveTwoDecimal(normalProfit), " 元", R.style.text_12_757575, R.style.text_12_e84a01, R.style.text_12_757575);
                tvProfitText.setText(builder);
            }
        }
    }

    /**
     * 计算实际支付
     **/
    private void calculateRealPay() {
//        实际支付：即转让价格
//        当未输入时，显示0.00元
//        当输入金额后，本地计算公式如下：
//        债权转让价格=转让金额+转让金额*（转让日-起息日）*利率/360

        double realNeedPay = getRealPayDouble();
        if (realNeedPay == 0) {
            llRealPay.setVisibility(View.GONE);
            lineAccount.setBackgroundColor(Color.parseColor("#dddddd"));
            tv_real_pay_count.setText("0.00");
        } else {
            llRealPay.setVisibility(View.VISIBLE);
            lineAccount.setBackgroundColor(Color.parseColor("#ffddca"));
            tv_real_pay_count.setText(MathUtil.saveTwoDecimal(realNeedPay) + "");
        }
    }

    private double getRealPayDouble() {

        double payCount = 0d;

        String moneyCountString = mInputPriceEdit.getText().toString();
        if (TextUtils.isEmpty(moneyCountString)) {
            payCount = 0d;
        } else {
            double moneyCount = Double.parseDouble(moneyCountString);
            if (moneyCount > 0) {
                double realPay = moneyCount + moneyCount * infoResponse.getTimeDiff() * infoResponse.getYearInterestRate() / 100 / 360;
                payCount = Double.parseDouble(MathUtil.saveTwoDecimal(realPay));
            }
        }

        return payCount;
    }

    /**
     * 实际支付说明点击事件
     */
    @OnClick(R.id.ll_real_pay)
    public void onRealPayDescClick() {
        if (infoResponse == null) {
            return;
        }
//        实际支付：
//        act_k=804
//        act_v=”sjzf”
        // 上报打点
        EventLog.upEventLog("804", "sjzf");
        View view = getLayoutInflater().inflate(R.layout.view_pay_desc, null);
        String input = mInputPriceEdit.getText().toString();
        TextView tvRealPay = (TextView) view.findViewById(R.id.tv_real_pay);
        TextView tvProfit = (TextView) view.findViewById(R.id.tv_profit);
        if (TextUtils.isEmpty(input) || Double.parseDouble(input) == 0) {
            tvRealPay.setVisibility(View.GONE);
            tvProfit.setVisibility(View.GONE);
        } else {
            tvRealPay.setVisibility(View.VISIBLE);
            //1000.20＝19000+19000x9%x30/360
            String rateString = MathUtil.subZeroAndDot(String.valueOf(infoResponse.getYearInterestRate()));
            tvRealPay.setText(MathUtil.saveTwoDecimal(getRealPayDouble()) + "=" + input + "+" + input + "x" + rateString + "%" + "x" + infoResponse.getTimeDiff() + "/360");
            tvProfit.setVisibility(View.VISIBLE);
            // 251.89＝19000x9%x120/360
            double normalProfit = Double.parseDouble(input) * infoResponse.getOriLoanTerm() * infoResponse.getYearInterestRate() / 100 / 360;
            tvProfit.setText(MathUtil.saveTwoDecimal(normalProfit) + "=" + input + "x" + rateString + "%" + "x" + infoResponse.getOriLoanTerm() + "/360");
        }
        DialogBuilder.getAlertDialog(this).setView(view).setTitle("说明").setPositiveButton("我知道了", null).show();
//        DialogBuilder.showSimpleDialog(this, "说明", infoResponse.getActualPayTipsString(), null, null, "我知道了", null);
    }

    @OnClick(R.id.input_price_edit)
    public void OnEditTextClick(View v) {
        if (!"".equals(mInputPriceEdit.getText().toString())) {
            mInputPriceEdit.setSelection(mInputPriceEdit.getText().length());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleFinancePayEvent(FinancePayEvent event) {
        if (!event.getPayState().equals("F")) {
            initBaseInfo();
        }
    }

    /**
     * 设置安全卡刷新余额
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMoneyChangeEvent(MoneyChangeEvent event) {
        initBaseInfo();
    }

    /**
     * 确认购买
     */
    @Nullable
    @OnClick(R.id.confirm_btn)
    public void onConfirmClick() {
        //购买金额客户端校验
        if (!checkInputMoneyOnConfirm()) {
            return;
        }
        ManyiUtils.closeKeyBoard(this, mInputPriceEdit);

        getRealPayFromServer();
    }

    /**
     * 从服务器获取实际需要支付
     **/
    public void getRealPayFromServer() {
        CalActualPayRequest request = new CalActualPayRequest();
        String moneyCountString = mInputPriceEdit.getText().toString();
        request.setAmount(Double.parseDouble(moneyCountString));
        request.setRateStr(infoResponse.getYearInterestRate() + "");
        request.setTerm(infoResponse.getTimeDiff() + "");
        request.setBeginTime(infoResponse.getInterestDate());

        ServiceSender.exec(this, request, new IwjwRespListener<CalActualPayResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
                showLoadTranstView();
            }

            @Override
            public void onJsonSuccess(CalActualPayResponse jsonObject) {

                // 拿到实际需要支付的数额，处理支付流程
                BigDecimal actualPay = jsonObject.getActualPay();
                double buyAmount = Double.parseDouble(mInputPriceEdit.getText().toString());

                handlePayProcess(actualPay.doubleValue(), buyAmount);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showContentView();
                ToastUtil.showInCenter(errorInfo);



            }

            @Override
            public void onFinish() {
                super.onFinish();
                showContentView();
            }
        });
    }

    public void handlePayProcess(double actualPay, double buyAmount) {
        // 如果账户余额不足，走推进流程
        if (actualPay > infoResponse.getAvailableBalance()) {
            //购买金额大于钱包余额
            //走转入流程
            if (actualPay - infoResponse.getAvailableBalance() > infoResponse.getBankLimit()) {
                //转入金额大于安全卡限额
                showMyToast("单笔最多可转入" + infoResponse.getBankLimit() + "元");
                return;
            }
            reChange(actualPay, buyAmount, actualPay - infoResponse.getAvailableBalance());

//            钱包余额不足：
//            act_k=804
//            act_v=”yebz”
            // 上报打点
            EventLog.upEventLog("804", "yebz");


        } else {
            //直接购买
            buyFangChanBaoWithInputPwd(actualPay, buyAmount);

//            确认：
//            act_k=804
//            act_v=”qr”
            // 上报打点
            EventLog.upEventLog("804", "qr");
        }
    }

    /**
     * 转入钱包并购买房产宝
     */
    private void reChange(double showMoney, double buyAmount, double depositAmount) {
        RegularReChangePay.CurrentPayInfo currentPayInfo = new RegularReChangePay.CurrentPayInfo();
        currentPayInfo.setAmount(showMoney);
        currentPayInfo.setRealAmount(buyAmount);
        currentPayInfo.setDepositAmount(Double.parseDouble(MathUtil.saveTwoDecimalHalfUp(depositAmount)));
        currentPayInfo.setProductId(infoResponse.getProductId());
        currentPayInfo.setIsTransfer(1);// 是否是转让标 0-普通投标；1-债权购买
        currentPayInfo.setCreditId(infoResponse.getCreditId());

        RegularReChangePay buyCurrentPay = new RegularReChangePay(this, currentPayInfo, new IwPwdPayResultListener<AdvanceDepositAndApplyAppResponse>() {
            @Override
            public void onPayComplete(AdvanceDepositAndApplyAppResponse object) {
                LogUtil.e("lyn", "onPayComplete");
                //购买成功
                goToPayResultActivity(switchResponse(object));
            }

            @Override
            public void onPayStateDelay(String msgInfo, AdvanceDepositAndApplyAppResponse object) {
                LogUtil.e("lyn", "onPayStateDelay");
                //购买进行中
                goToPayResultActivity(switchResponse(object));
            }

            @Override
            public void onPayFailInfo(String msgInfo, String errorCode, AdvanceDepositAndApplyAppResponse object) {
                LogUtil.e("lyn", "onPayFailInfo");
                //购买失败
                goToPayResultActivity(switchResponse(object));
            }

            @Override
            public void onPayPwdTryAgain() {
                onConfirmClick();
            }
        });
        buyCurrentPay.pay();
    }

    /**
     * 转换response
     *
     * @param object
     * @return
     */
    private BuyDingqibaoResponse switchResponse(AdvanceDepositAndApplyAppResponse object) {
        BuyDingqibaoResponse response = new BuyDingqibaoResponse();
        response.setBizStatus(object.getBizStatus());
        String productId = MyIntent.getData(getIntent());
        response.setProductId(productId);
        double money = Double.parseDouble(mInputPriceEdit.getText().toString());
        response.setAmount(money);
        response.setBannerId(object.getBannerId());
        response.setDescription(object.getDescription());
        response.setHorizon(object.getHorizon());
        response.setImgUrl(object.getImgUrl());
        response.setIsPopShare(object.getIsPopShare());
        response.setProductName(object.getProductName());
        response.setShareIcon(object.getShareIcon());
        response.setTitle(object.getTitle());
        response.setWebpageUrl(object.getWebpageUrl());
        response.setYearInterestRate(object.getYearInterestRate());
        response.setActivityMsg(object.getActivityMsg());
        response.setMessage(object.getMessage());
        response.setErrorCode(object.getErrorCode());
        response.setBizCode(object.getBizCode());
        response.setBackAmount(object.getBackAmount());
        response.setTransferPrice(object.getTransferPrice());
        response.setBidOrderNo(object.getBidOrderNo());
        response.setFirstInvestLotteryURL(object.getFirstInvestLotteryURL());
        response.setIsLottery(object.getIsLottery());
        return response;
    }

    /**
     * 钱包余额充足,直接购买房产宝
     *
     * @param showMoney
     */
    private void buyFangChanBaoWithInputPwd(double showMoney, double buyAmount) {
        BuyRegularPay.RegularPayInfo regularPayInfo = new BuyRegularPay.RegularPayInfo();
        regularPayInfo.setRealAmount(buyAmount);
        regularPayInfo.setAmount(showMoney);// 显示作用
        regularPayInfo.setProductId(infoResponse.getProductId());
        regularPayInfo.setProductName(infoResponse.getProductName());
        BuyRegularPay buyRegularPay = new BuyRegularPay(this, regularPayInfo, new IwPwdPayResultListener<BuyDingqibaoResponse>() {
            @Override
            public void onPayComplete(BuyDingqibaoResponse object) {
                //购买成功
                object.setBizStatus("S");
                goToPayResultActivity(object);
            }

            @Override
            public void onPayFailInfo(String msgInfo, String errorCode, BuyDingqibaoResponse object) {
                //购买失败
                object.setBizStatus("F");
                if(object.getErrorCode() == 0 ){
                    goToPayResultActivity(object);
                }else{
                    DialogBuilder.showSimpleDialogCenter( object.getMessage(), BuyTransferPayActivity.this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }

            }

            @Override
            public void onPayStateDelay(String msgInfo, BuyDingqibaoResponse object) {
                //购买进行中
                object.setBizStatus("P");
                goToPayResultActivity(object);
            }

            @Override
            public void onPayPwdTryAgain() {
                onConfirmClick();
            }
        });
        buyRegularPay.pay();
    }

    /**
     * 进入购买结果页面
     *
     * @param object
     */
    public void goToPayResultActivity(BuyDingqibaoResponse object) {
        object.setLast(isLast);
        Intent mIntent = new Intent(mContext, BuyTransferPayResultActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(RegularPayResultActivity.KEY, object);
        mIntent.putExtras(mBundle);
        mContext.startActivity(mIntent);
        finish();
    }

    public boolean checkInputMoneyOnConfirm() {
        String money = mInputPriceEdit.getText().toString();
        if (!CommonUtil.isMoneyNumber(money)) {
            showMessageTipsDialog("请输入正确的购买金额");
            return false;
        } else if (infoResponse.getBiddableAmount() >= infoResponse.getMinAmount() && infoResponse.getMinAmount() > 0 && Double.parseDouble(money) < infoResponse.getMinAmount()) {
            //剩余额度大于最小购买金额且购买金额小于起购金额
            showMessageTipsDialog("购买金额不能低于起购金额");
            return false;
        } else if (infoResponse.getBiddableAmount() >= 0 && Double.parseDouble(money) > infoResponse.getBiddableAmount()) {
            String msg = "购买金额不能超过剩余额度" + MathUtil.saveTwoDecimalHalfUp(infoResponse.getBiddableAmount()) + "元";
            showMessageTipsDialog(msg);
            return false;
        }
        return true;
    }

    /**
     * 信息提示框
     */
    private void showMessageTipsDialog(String msg) {
        AlertDialog.Builder b = new AlertDialog.Builder(mContext, R.style.AppCompatDialog);
        b.setTitle("购买失败");
        b.setMessage(msg);
        b.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.show();
    }

    /**
     * check 输入的金额
     *
     * @return
     */
    private boolean checkInputMoney() {
        mErrorTips.setVisibility(View.GONE);
        mInputPriceEditLable.setVisibility(View.GONE);
        rlMyAccount.setVisibility(View.VISIBLE);
        mConfirmBtn.setEnabled(false);
        mConfirmBtn.setText("确认");
        if (infoResponse == null) {
            return false;
        }
        String money = mInputPriceEdit.getText().toString();
        if ("".equals(money)) {
            mErrorTips.setText("");
            mInputPriceEditLable.setVisibility(View.VISIBLE);
            return false;
        } else if (Double.parseDouble(money) == 0) {
            mErrorTips.setText("");
            mInputPriceEditLable.setVisibility(View.GONE);
            return false;
        } else if (Double.parseDouble(money) > 0) {
            if (!CommonUtil.isMoneyNumber(money)) {
                //ToastUtil.show("请输入正确的金额");
                mErrorTips.setText("请输入正确的购买金额");
                mErrorTips.setVisibility(View.VISIBLE);
                rlMyAccount.setVisibility(View.GONE);
                return false;
            } else if (infoResponse.getBiddableAmount() >= 0 && Double.parseDouble(money) > infoResponse.getBiddableAmount()) {
                String msg = "购买金额不能超过剩余额度" + MathUtil.saveTwoDecimalHalfUp(infoResponse.getBiddableAmount()) + "元";
                mErrorTips.setText(msg);
                mErrorTips.setVisibility(View.VISIBLE);
                rlMyAccount.setVisibility(View.GONE);
                return false;
            } else if (infoResponse.getBidUnit() > 0 && Double.parseDouble(money) % infoResponse.getBidUnit() != 0) {
                //购买金额必须是bidUnit的整数倍
                mErrorTips.setText("购买金额须为" + MathUtil.subZeroAndDot(String.valueOf(infoResponse.getBidUnit())) + "的整数倍");
                mErrorTips.setVisibility(View.VISIBLE);
                rlMyAccount.setVisibility(View.GONE);
                return false;
            } else if (infoResponse.getMinAmount() > 0 && Double.parseDouble(money) < infoResponse.getMinAmount()) {
                //购买金额小于起购金额
                mErrorTips.setText(getResources().getString(R.string.reaular_pay_small_amount_text));
                mErrorTips.setVisibility(View.VISIBLE);
                rlMyAccount.setVisibility(View.GONE);
                return false;
            }else if (getRealPayDouble() > infoResponse.getAvailableBalance()) {// 本地计算的实际需要支付的值大于可用余额

                BigDecimal offset = MathUtil.offetSetBetweenTwoBD(new BigDecimal(getRealPayDouble()) ,new BigDecimal(infoResponse.getAvailableBalance()));
                mConfirmBtn.setText("账户余额不足，需支付" + offset + "元");
                if((offset.compareTo(new BigDecimal(infoResponse.getBankLimit())) == 1)){
                    mMaxValueLayout.setVisibility(View.VISIBLE);
                    mMaxValue.setText(infoResponse.getBankLimitStr());
                    return false;
                }else{
                    mMaxValueLayout.setVisibility(View.GONE);
                    return true;
                }
            }else if (getRealPayDouble() <= infoResponse.getAvailableBalance()) {
                BigDecimal offset = MathUtil.offetSetBetweenTwoBD(new BigDecimal(getRealPayDouble()) ,new BigDecimal(infoResponse.getAvailableBalance()));
                if((offset.compareTo(new BigDecimal(infoResponse.getBankLimit())) == 1)){
                    mMaxValueLayout.setVisibility(View.VISIBLE);
                    mMaxValue.setText(infoResponse.getBankLimitStr());
                    return false;
                }else{
                    mMaxValueLayout.setVisibility(View.GONE);
                    return true;
                }
            }

        }
        return true;
    }

    public void bindViewData(RegularPayBaseInfoResponse jsonObject) {
        this.infoResponse = jsonObject;
        verifyProtocolListLogical(jsonObject.getProtocolList());
        mInputPriceEditLable.setText(jsonObject.getHint());
        tvLeaveAccount.setText("剩余本金 " + jsonObject.getBiddableAmountStr() + "元");
        mRegularBalance.setText("账户可用余额 " + CommonUtil.numberFormat(jsonObject.getAvailableBalance()) + " 元");
        //余额不足转入成功后刷新页面并重新校验输入的金额
        if (checkInputMoney() && mAgreementCheckbox.isChecked()) {
            mConfirmBtn.setEnabled(true);
        } else {
            mConfirmBtn.setEnabled(false);
        }
        if (jsonObject.getBiddableAmount() == 0){
            tvLeaveAccount.setVisibility(View.GONE);
            tvLastMoneyText.setVisibility(View.VISIBLE);
            tvLastMoneyText.setText("已售罄，请选择购买其他产品");

        }else if (jsonObject.getBiddableAmount() > 0 && jsonObject.getBiddableAmount() <= jsonObject.getMinAmount()) {
            tvLeaveAccount.setVisibility(View.GONE);
            //剩余额度小于起购金额,直接填入剩余额度且不可修改
            mInputPriceEdit.setText(MathUtil.subZeroAndDot(String.valueOf(jsonObject.getBiddableAmount())));
            mInputPriceEdit.setEnabled(false);
            mInputPriceEdit.setFocusable(false);
            mInputPriceEdit.setTextColor(Color.parseColor("#9b9b9b"));
            tvLastMoneyText.setVisibility(View.VISIBLE);
            tvClear.setEnabled(false);
            tvClear.setVisibility(View.GONE);
            mInputPriceEditLable.setVisibility(View.GONE);
            isLast = true;
            // 最后一笔余额不足直接显示需要转入
            if (getRealPayDouble() > infoResponse.getAvailableBalance()) {
                double offset = getRealPayDouble() - infoResponse.getAvailableBalance();
                mConfirmBtn.setText("账户余额不足，需充值" + MathUtil.saveTwoDecimalHalfUp(offset) + "元");
            }
        } else {
            ManyiUtils.showKeyBoard(this, mInputPriceEdit);
        }
        if (infoResponse.getAvailableBalance() < infoResponse.getMinAmount() | isLast) {
            //可用余额小于起购金额
            tvAllBuy.setVisibility(View.GONE);
        } else {
            tvAllBuy.setVisibility(View.VISIBLE);
            EventLog.upEventLog("201610283", "qegm_show", "zrfcb_syt");
        }
    }

    @OnClick(R.id.tv_max_value_introduction_list)
    public void maxValueIntroductionList() {

        Map<String, String> dataMap = ObjectUtil.newHashMap();
        dataMap.put(WebViewActivity.TITLE, getResources().getString(R.string.support_cards));
        dataMap.put(WebViewActivity.NEED_REFRESH, "0");
        dataMap.put(WebViewActivity.URL, SupportUrl.getSupportUrlsResponse().getSafeBankSupport());
        MyIntent.startActivity(this, WebViewActivity.class, dataMap);
    }

    @Override
    public void reloadData() {
        super.reloadData();
        initBaseInfo();
    }

    /**
     * 页面数据获取请求
     */

    public void initBaseInfo() {
        showLoadTranstView();
        String productId = MyIntent.getData(getIntent());
        RegularPayBaseInfoRequest request = new RegularPayBaseInfoRequest();
        request.setProductId(productId);
        ServiceSender.exec(this, request, new IwjwRespListener<RegularPayBaseInfoResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onJsonSuccess(RegularPayBaseInfoResponse jsonObject) {
                showContentView();
                String msg = jsonObject.getMessage();
                int code = jsonObject.getErrorCode();
                if (!"".equals(msg)) {
                    //ToastUtil.showInCenter(mContext, msg);
                }
                bindViewData(jsonObject);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showContentView();
                ToastUtil.showInCenter(errorInfo);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECT_VOUCHER:
                if (resultCode == RESULT_OK && data != null) {
                    if (resultCode == 1000) {
                        calculateProfit();
                    }
                }
                break;
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

}
