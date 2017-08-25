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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.HandlerUtil;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.MathUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.SpannableUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.eventbus.FinancePayEvent;
import com.ailicai.app.eventbus.MoneyChangeEvent;
import com.ailicai.app.model.bean.ActivityRuleModel;
import com.ailicai.app.model.bean.Protocol;
import com.ailicai.app.model.request.GetAppropriateCouponRequest;
import com.ailicai.app.model.request.RegularPayBaseInfoRequest;
import com.ailicai.app.model.response.AdvanceDepositAndApplyAppResponse;
import com.ailicai.app.model.response.BuyDingqibaoResponse;
import com.ailicai.app.model.response.GetAppropriateCouponResponse;
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
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 房产宝购买
 * Created by Gerry on 2015/12/29.
 */
public class RegularPayActivity extends BaseBindActivity {

    public static final String PRODUCT_ID_KEY = "PRODUCT_ID";
    public static final String IS_FROM_SMALL_COIN = "FROM_SMALL_COIN";
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
    @Bind(R.id.tv_ticket_text)
    TextView tvTicketText;
    @Bind(R.id.tv_profit_text)
    TextView tvProfitText;
    @Bind(R.id.tv_clear)
    TextView tvClear;
    @Bind(R.id.rl_my_account)
    RelativeLayout rlMyAccount;
    @Bind(R.id.tv_last_money_text)
    TextView tvLastMoneyText;
    @Bind(R.id.rl_activity)
    RelativeLayout rlActivity;
    @Bind(R.id.tv_activity_text)
    TextView tvActivityText;
    @Bind(R.id.sv_regular)
    ScrollView svRegular;
    @Bind(R.id.tv_all_buy)
    TextView tvAllBuy;

    @Bind(R.id.rl_max_value)
    RelativeLayout mMaxValueLayout;
    @Bind(R.id.tv_max_value_per_time)
    TextView mMaxValue;

    private RegularPayBaseInfoResponse infoResponse;
    //卡券利率
    private double voucherRate;
    //返金金额
    private String voucherValue;
    private int use;
    //卡券id
    private int voucherId;
    private int voucherType;
    //卡券加息天数
    private int addRateDay;
    private ActivityRuleModel ruleModel;
    private boolean isLast;//是否是最后一笔
    private ProtocolHelper protocolHelper;

    private String productId = "";
    private boolean isFromSmallCoin = false;
    private int availableVoucherNumber ;
    private String input = "";
    private String initialInputValue ="0";

    private static final String MP_AUTO_INVEST = "auto_invest";

    @Override
    public int getLayout() {
        return R.layout.activity_regular_pay;
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
        getDataFromIntent();
        mTopTitleView.setTitleOnClickListener(new IWTopTitleView.TopTitleOnClickListener() {
            @Override
            public boolean onBackClick() {
                //SystemUtil.HideSoftInput(RegularPayActivity.this);
                SystemUtil.hideKeyboard(mInputPriceEdit);
                finish();
                return true;
            }
        });
//        setShowSystemBarTint(false);
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(R.color.main_top_deep_color);
        mConfirmBtn.setEnabled(false);
        initBaseInfo();
        addListener();
        SpannableUtil spannableUtil = new SpannableUtil(this);
        SpannableStringBuilder builder = spannableUtil.getSpannableString("预计收益 ", "0.00", " 元", R.style.text_12_757575, R.style.text_12_757575, R.style.text_12_757575);
        tvProfitText.setText(builder);
        initProtocol();

    }

    private void getDataFromIntent() {
        productId = getIntent().getStringExtra(PRODUCT_ID_KEY);
        isFromSmallCoin = getIntent().getBooleanExtra(IS_FROM_SMALL_COIN,false);
        if(TextUtils.isEmpty(productId)) {
            productId = MyIntent.getData(getIntent());
        }
    }

    private void initProtocol() {
        protocolHelper = new ProtocolHelper(this, ProtocolHelper.TYPE_NORMAL_PAY);
    }

    private void addListener() {
        mInputPriceEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    SystemUtil.hideKeyboard(mInputPriceEdit);
                }
                return false;
            }
        });
        mInputPriceEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollToBottom();
                return false;
            }
        });
    }

    @OnClick(R.id.tv_clear)
    public void clearEditText() {
        mInputPriceEdit.setText("");
        LogUtil.e("lyn", "clearEditText");
    }

    @OnClick(R.id.tv_max_value_introduction_list)
    public void maxValueIntroductionList() {

        Map<String, String> dataMap = ObjectUtil.newHashMap();
        dataMap.put(WebViewActivity.TITLE, getResources().getString(R.string.support_cards));
        dataMap.put(WebViewActivity.NEED_REFRESH, "0");
        dataMap.put(WebViewActivity.URL, SupportUrl.getSupportUrlsResponse().getSafeBankSupport());
        MyIntent.startActivity(this, WebViewActivity.class, dataMap);
    }

    /**
     * 点击全部购买
     */
    @OnClick(R.id.tv_all_buy)
    public void clickAllBuy() {
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
            int intAvailable = (int) ((availableBalance) / bidUnit);
            //剩余额度减去起购金额对投标单位的取余整数
            int intBiddable = (int) ((biddableAmount) / bidUnit);
            //二者取其最小值
            if (intAvailable >= intBiddable) {
                mInputPriceEdit.setText(MathUtil.subZeroAndDot(String.valueOf(intBiddable * bidUnit )));
            } else {
                mInputPriceEdit.setText(MathUtil.subZeroAndDot(String.valueOf(intAvailable * bidUnit )));
            }
        } else {
            //可用余额小于起购金额(since 5.8 不显示全额购买按钮)
            mInputPriceEdit.setText(MathUtil.subZeroAndDot(String.valueOf(availableBalance)));
        }
        EventLog.upEventLog("201610283", "qegm_click", "ptfcb_syt");
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
    public void OnBoxChange(CheckBox box, boolean isChecked) {
        if (isChecked && checkInputMoney()) {
            mConfirmBtn.setEnabled(true);
        } else {
            mConfirmBtn.setEnabled(false);
        }

    }


    @OnTextChanged(value = R.id.input_price_edit, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged(Editable s) {
//        if (!"".equals(s.toString()) && mAgreementCheckbox.isChecked()) {
        input = s.toString();
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
            } else {
                //剩余额度大于等于起购金额
                if (checkInputMoney() && mAgreementCheckbox.isChecked()) {
                    mConfirmBtn.setEnabled(true);
                } else {
                    mConfirmBtn.setEnabled(false);
                }
            }
            getBestVoucher(input);
        } else {
            // 剩余额度为0
            checkInputMoney();
            mConfirmBtn.setEnabled(false);
        }
        //计算收益
        calculateProfit();
        if (TextUtils.isEmpty(mInputPriceEdit.getText().toString())) {
            tvClear.setVisibility(View.GONE);
        } else {
            tvClear.setVisibility(View.VISIBLE);
            mInputPriceEdit.setSelection(mInputPriceEdit.getText().length());
        }
        //处理活动展示
        if (TextUtils.isEmpty(s.toString())) {
            tvActivityText.setText("");
        } else {
            showAutoActivity(Double.parseDouble(s.toString()));
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
            //金额*天数*年利率/年的天数
            double normalProfit = moneyCount * infoResponse.getLoanTerm() * infoResponse.getYearInterestRate() / 100 / 360;
            switch (voucherType) {
                case 73://加息券
                    if (moneyCount > 0) {
                        if (voucherRate > 0) {
                            //有加息券
                            double voucherProfit;
                            if (addRateDay == -1) {
                                //代表加息券不限天数
                                addRateDay = infoResponse.getLoanTerm();
                                voucherProfit = moneyCount * infoResponse.getLoanTerm() * voucherRate / 100 / 360;
                            } else {
                                //加息券有天数限制
                                int actualRateDay = addRateDay;
                                if (addRateDay > infoResponse.getLoanTerm()) {
                                    actualRateDay = infoResponse.getLoanTerm();
                                }
                                voucherProfit = moneyCount * actualRateDay * voucherRate / 100 / 360;
                            }
                            SpannableUtil spannableUtil = new SpannableUtil(this);
                            SpannableStringBuilder builder = spannableUtil.getSpannableString("预计收益 ",
                                    MathUtil.saveTwoDecimal(normalProfit),
                                    " 元 ", "+ 加息收益 ", MathUtil.saveTwoDecimal(voucherProfit), " 元",
                                    R.style.text_12_757575,
                                    R.style.text_12_e84a01,
                                    R.style.text_12_757575,
                                    R.style.text_12_757575,
                                    R.style.text_12_e84a01,
                                    R.style.text_12_757575);
                            tvProfitText.setText(builder);
                        } else {
                            //无加息券
                            SpannableUtil spannableUtil = new SpannableUtil(this);
                            SpannableStringBuilder builder = spannableUtil.getSpannableString("预计收益 ", MathUtil.saveTwoDecimal(normalProfit), " 元", R.style.text_12_757575, R.style.text_12_e84a01, R.style.text_12_757575);
                            tvProfitText.setText(builder);
                        }
                    }
                    break;

                case 74://返金券
                    if (moneyCount > 0) {
                        if (!TextUtils.isEmpty(voucherValue)) {
                            SpannableUtil spannableUtil = new SpannableUtil(this);
                            SpannableStringBuilder builder = spannableUtil.getSpannableString("预计收益 ",
                                    MathUtil.saveTwoDecimal(normalProfit),
                                    " 元 ", "+ 返金金额 ", voucherValue, " 元",
                                    R.style.text_12_757575,
                                    R.style.text_12_e84a01,
                                    R.style.text_12_757575,
                                    R.style.text_12_757575,
                                    R.style.text_12_e84a01,
                                    R.style.text_12_757575);
                            tvProfitText.setText(builder);
                        } else {
                            //无返金券
                            SpannableUtil spannableUtil = new SpannableUtil(this);
                            SpannableStringBuilder builder = spannableUtil.getSpannableString("预计收益 ", MathUtil.saveTwoDecimal(normalProfit), " 元", R.style.text_12_757575, R.style.text_12_e84a01, R.style.text_12_757575);
                            tvProfitText.setText(builder);
                        }
                    }
                    break;

                default://默认根据用户的输入计算收益
                    SpannableUtil spannableUtil = new SpannableUtil(this);
                    SpannableStringBuilder builder = spannableUtil.getSpannableString("预计收益 ", MathUtil.saveTwoDecimal(normalProfit), " 元", R.style.text_12_757575, R.style.text_12_e84a01, R.style.text_12_757575);
                    tvProfitText.setText(builder);
                    break;
            }
        }
    }

    /**
     * 进入选择卡券页面
     */
    @OnClick(R.id.rl_select_voucher)
    public void onSelectVoucher() {
        //选择卡券页面

        Intent intent = new Intent(this, VoucherListActivity.class);
        intent.putExtra(VoucherListActivity.EXTRA_PRODUCT_ID, productId);
        intent.putExtra(VoucherListActivity.EXTRA_APPROPRIATE_VOUCHER_ID, voucherId);
        if (input != null && input.length()>0){
            intent.putExtra(VoucherListActivity.EXTRA_AMOUNT, Integer.parseInt(input));
        }else{
            intent.putExtra(VoucherListActivity.EXTRA_AMOUNT, 0);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_VOUCHER);
        ManyiUtils.closeKeyBoard(this, mInputPriceEdit);

    }

    @OnClick(R.id.input_price_edit)
    public void OnEditTextClick(View v) {
        if (!"".equals(mInputPriceEdit.getText().toString())) {
            mInputPriceEdit.setSelection(mInputPriceEdit.getText().length());
        }
    }

    @OnClick(R.id.rl_activity_click)
    public void clickHelp() {
        showActivityDialog();
    }


    public void showAutoActivity(double inputMoney) {
        List<ActivityRuleModel> ruleList = ObjectUtil.newArrayList();
        if (infoResponse != null && infoResponse.getActivity() != null) {
            ruleList = infoResponse.getActivity().getRuleList();
        }
        boolean hasRule = false;
        if (!ruleList.isEmpty()) {
            for (int i = 0; i < ruleList.size(); i++) {
                ActivityRuleModel model = ruleList.get(i);
                if (inputMoney >= model.getReachRule()) {
                    ruleModel = model;
                    tvActivityText.setText(ruleModel.getMemo());
                    hasRule = true;
                }
            }
        }
        if (!hasRule) {
            ruleModel = null;
            tvActivityText.setText("");
        }

    }

    public void showActivityDialog() {
        View contentView = View.inflate(this, R.layout.dialog_wallet_activity_list, null);
        TextView setTimeBtn = (TextView) contentView.findViewById(R.id.i_know_btn);
        ListView myList = (ListView) contentView.findViewById(R.id.activity_list);
        LinearLayout activityView = (LinearLayout) contentView.findViewById(R.id.activity_view);
        final AlertDialog shareAlertDialog = DialogBuilder.getAlertDialog(this, R.style.AppCompatDialog2).setView(contentView).show();
        setTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAlertDialog.dismiss();
            }
        });

        List<ActivityRuleModel> ruleList = ObjectUtil.newArrayList();
        if (infoResponse != null && infoResponse.getActivity() != null) {
            ruleList = infoResponse.getActivity().getRuleList();
        }
        if (!ruleList.isEmpty()) {
            ActivityListAdapter adapter = new ActivityListAdapter(ruleList);
            myList.setAdapter(adapter);
            if (ruleList.size() > 5) {
                activityView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtil.getPixelFromDip(mContext, 180)));
            }
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
        double money = Double.parseDouble(mInputPriceEdit.getText().toString());
        if (money > infoResponse.getAvailableBalance()) {
            //购买金额大于钱包余额
            //走转入流程
            BigDecimal offset = (new BigDecimal(money)).subtract(new BigDecimal(infoResponse.getAvailableBalance()));
            double reChangeMoney = offset.doubleValue();
            if (infoResponse.getBankLimit() != 0 && reChangeMoney > infoResponse.getBankLimit()) {
                //转入金额大于安全卡限额
//                showMyToast("单笔最多可充值" + infoResponse.getBankLimit() + "元");
                DialogBuilder.showSimpleDialogCenter( "单笔最多可充值" + infoResponse.getBankLimit() + "元", RegularPayActivity.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return;
            }
            reChange(money, reChangeMoney);
            EventLog.upEventLog("201610281", "yebz", "ptfcb_syt");
        } else {
            //直接购买
            buyFangChanBaoWithInputPwd(money);
            EventLog.upEventLog("201610281", "sub", "ptfcb_syt");
        }


    }

    /**
     * 转入钱包并购买房产宝/或者小钱袋
     */
    private void reChange(double amount, double depositAmount) {
        RegularReChangePay.CurrentPayInfo currentPayInfo = new RegularReChangePay.CurrentPayInfo();
        currentPayInfo.setAmount(amount);
        currentPayInfo.setDepositAmount(depositAmount);
        if (voucherId > 0) {
            currentPayInfo.setVoucherId(voucherId);
        }
        if (ruleModel != null) {
            //表示参加了活动
            currentPayInfo.setRuleId(ruleModel.getRuleId());
            currentPayInfo.setActivityId(infoResponse.getActivity().getActivityId());
            currentPayInfo.setConfigId(infoResponse.getActivity().getConfigId());
            currentPayInfo.setRelationId(infoResponse.getActivity().getRelationId());
        }
        currentPayInfo.setProductId(infoResponse.getProductId());
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
                if (object.getBizCode() == 2) {
                    showMyToast(object.getMessage());
                    initBaseInfo();
                } else {
                    goToPayResultActivity(switchResponse(object));
                }
            }

            @Override
            public void onPayPwdTryAgain() {
                onConfirmClick();
            }
        });

        if(isFromSmallCoin) {
            buyCurrentPay.setMoneyOutStr("转入并购买小钱袋");
        }
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
        response.setProductId(object.getProductId());
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
        response.setHuoqibaoRate(object.getHuoqibaoRate());
        response.setEndBuyTimeStr(object.getEndBuyTimeStr());
        response.setInterestDateStr(object.getInterestDateStr());
        response.setHasBuyPrecent(object.getHasBuyPrecent());
        response.setHasBuyPrecentStr(object.getHasBuyPrecentStr());
        response.setBidOrderNo(object.getBidOrderNo());
        response.setFirstInvestLotteryURL(object.getFirstInvestLotteryURL());
        response.setIsLottery(object.getIsLottery());
        return response;
    }

    /**
     * 账户余额充足,直接购买房产宝
     *
     * @param money
     */
    private void buyFangChanBaoWithInputPwd(double money) {
        BuyRegularPay.RegularPayInfo regularPayInfo = new BuyRegularPay.RegularPayInfo();
        regularPayInfo.setAmount(money);
        regularPayInfo.setProductId(infoResponse.getProductId());
        regularPayInfo.setProductName(infoResponse.getProductName());
        regularPayInfo.setVoucherId(voucherId);
        if (ruleModel != null) {
            //表示参加了活动
            regularPayInfo.setRuleId(ruleModel.getRuleId());
            regularPayInfo.setActivityId(infoResponse.getActivity().getActivityId());
            regularPayInfo.setConfigId(infoResponse.getActivity().getConfigId());
            regularPayInfo.setRelationId(infoResponse.getActivity().getRelationId());
        }
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

                if (object.getBizCode() == 2) {
                    //toast相关报错
                    showMyToast(object.getMessage());
                    initBaseInfo();
                } else {
                    if(object.getErrorCode() == -1 ){
                        DialogBuilder.showSimpleDialogCenter( object.getMessage(), RegularPayActivity.this, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    }else{
                        goToPayResultActivity(object);
                    }
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
        //防止接口有时候返回的id为空
        object.setProductId(infoResponse.getProductId());
        object.setLast(isLast);
        Intent mIntent = new Intent(mContext, RegularPayResultActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(RegularPayResultActivity.KEY, object);
        mBundle.putBoolean(IS_FROM_SMALL_COIN,isFromSmallCoin);
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
            String msg = "购买金额不能超过剩余额度" + infoResponse.getBiddableAmountStr() + "元";
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
            mErrorTips.setText("请输入正确的购买金额");
            mErrorTips.setVisibility(View.VISIBLE);
            rlMyAccount.setVisibility(View.GONE);
            return false;
        } else if (Double.parseDouble(money) > 0) {
            if (!CommonUtil.isMoneyNumber(money)) {
                //ToastUtil.show("请输入正确的金额");
                mErrorTips.setText("请输入正确的购买金额");
                mErrorTips.setVisibility(View.VISIBLE);
                rlMyAccount.setVisibility(View.GONE);
                return false;
            } else if (isLast && Double.parseDouble(money) > infoResponse.getAvailableBalance()) {
                //最后一笔,且钱包余额不足
                BigDecimal offset = MathUtil.offetSetBetweenTwoBD(new BigDecimal(money) ,new BigDecimal(infoResponse.getAvailableBalance()));
                mConfirmBtn.setText("账户可用余额不足，需支付" + offset + "元");
                if((offset.compareTo(new BigDecimal(infoResponse.getBankLimit())) == 1)){
                    mMaxValueLayout.setVisibility(View.VISIBLE);
                    mMaxValue.setText(infoResponse.getBankLimitStr());
                    return false;
                }else{
                    mMaxValueLayout.setVisibility(View.GONE);
                    return true;
                }
            } else if (isLast && Double.parseDouble(money) <= infoResponse.getAvailableBalance()) {
                //最后一笔,且钱包余额充足
                return true;
            } else if (infoResponse.getBiddableAmount() >= 0 && Double.parseDouble(money) > infoResponse.getBiddableAmount()) {
                //购买金额不能超过剩余额度
                String msg = "购买金额不能超过剩余额度" + infoResponse.getBiddableAmountStr() + "元";
                mErrorTips.setText(msg);
                mErrorTips.setVisibility(View.VISIBLE);
                rlMyAccount.setVisibility(View.GONE);
                return false;
            } else if (infoResponse.getBidUnit() > 0 && Double.parseDouble(money) % infoResponse.getBidUnit() != 0) {
                //购买金额需为bidunit的倍数
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
            } else if (Double.parseDouble(money) > infoResponse.getAvailableBalance()) {
                //since 5.3变更去掉
                BigDecimal offset = MathUtil.offetSetBetweenTwoBD(new BigDecimal(money) ,new BigDecimal(infoResponse.getAvailableBalance()));
                mConfirmBtn.setText("账户可用余额不足，需支付" + offset + "元");
                if((offset.compareTo(new BigDecimal(infoResponse.getBankLimit())) == 1)){
                    mMaxValueLayout.setVisibility(View.VISIBLE);
                    mMaxValue.setText(infoResponse.getBankLimitStr());
                    return false;
                }else{
                    mMaxValueLayout.setVisibility(View.GONE);
                    return true;
                }

                /**
                 } else if (infoResponse.getBuyLimit() > 0 && Double.parseDouble(money) > infoResponse.getBuyLimit()) {
                 mErrorTips.setText(infoResponse.getBuyLimitStr());
                 mErrorTips.setVisibility(View.VISIBLE);
                 return false;
                 } else if (infoResponse.getMaxPerProductLimit() > 0 && Double.parseDouble(money) > infoResponse.getMaxPerProductLimit()) {
                 mErrorTips.setText(infoResponse.getMaxPerProductLimitStr());
                 mErrorTips.setVisibility(View.VISIBLE);
                 return false;
                 */
            }else if (Double.parseDouble(money) <= infoResponse.getAvailableBalance()) {
                //银行卡单笔限额的提醒
                BigDecimal offset = MathUtil.offetSetBetweenTwoBD(new BigDecimal(money) ,new BigDecimal(infoResponse.getAvailableBalance()));
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
        int length = mConfirmBtn.getText().length();
        if (length > 19) {
            mConfirmBtn.setTextSize(14);
        } else {
            mConfirmBtn.setTextSize(15);
        }
        return true;
    }

    public void bindViewData(RegularPayBaseInfoResponse jsonObject) {
        this.infoResponse = jsonObject;
        verifyProtocolListLogical(jsonObject.getProtocolList());
        mInputPriceEditLable.setText(jsonObject.getHint());
        mRegularBalance.setText("账户可用余额 " + CommonUtil.numberFormat(jsonObject.getAvailableBalance()) + " 元");
        voucherId = infoResponse.getVoucherId();
        addRateDay = infoResponse.getAddRateDay();
        voucherRate = infoResponse.getAddRate();

        //当前页面初始化中与卡券相关的逻辑拿掉
        /*
        if (jsonObject.getBiddableAmount()==0) {
            tvTicketText.setText("暂无可用");
            tvTicketText.setTextColor(Color.parseColor("#757575"));
        }else{
            if (voucherId > 0) {
                //选择过卡券
                if (addRateDay == -1) {
                    //加息券不限天数
                    tvTicketText.setText("享加息" + voucherRate + "%");
                } else {
                    //6.0 改
                    addRateDay = addRateDay > infoResponse.getLoanTerm() ? infoResponse.getLoanTerm() : addRateDay;
                    //加息券有天数限制
                    tvTicketText.setText("享前" + addRateDay + "天加息" + voucherRate + "%");
                }
                tvTicketText.setTextColor(Color.parseColor("#212121"));
            } else {
                //没有选过卡券
                if (jsonObject.getInterestVoucherNum() > 0) {

                    //加息券数量大于0
                    tvTicketText.setText(jsonObject.getInterestVoucherNum() + "张可用");
                    tvTicketText.setTextColor(Color.parseColor("#212121"));
                } else {
                    tvTicketText.setText("暂无可用");
                    tvTicketText.setTextColor(Color.parseColor("#757575"));
                }
            }
        }
        */

        if (infoResponse.getActivity() == null) {
            //无活动
            rlActivity.setVisibility(View.GONE);
        } else {
            //有活动
            rlActivity.setVisibility(View.VISIBLE);
        }
        if (jsonObject.getBiddableAmount()==0){
            tvLastMoneyText.setVisibility(View.VISIBLE);
            tvLastMoneyText.setText("已售罄，请选择购买其他产品");
            mInputPriceEdit.setText("0");
            mInputPriceEdit.setEnabled(false);
            mInputPriceEdit.setTextColor(Color.parseColor("#9b9b9b"));
        }else if (jsonObject.getBiddableAmount() > 0 && jsonObject.getBiddableAmount() <= jsonObject.getMinAmount()) {
            //剩余额度小于起购金额,直接填入剩余额度且不可修改
            initialInputValue = String.valueOf(jsonObject.getBiddableAmount());
            mInputPriceEdit.setText(MathUtil.subZeroAndDot(initialInputValue));
            mInputPriceEdit.setEnabled(false);
            mInputPriceEdit.setTextColor(Color.parseColor("#9b9b9b"));
            tvLastMoneyText.setVisibility(View.VISIBLE);
            tvClear.setEnabled(false);
            tvClear.setVisibility(View.GONE);
            mInputPriceEditLable.setVisibility(View.GONE);
            isLast = true;
        }
        //余额不足转入成功后刷新页面并重新校验输入的金额
        if (checkInputMoney() && mAgreementCheckbox.isChecked()) {
            mConfirmBtn.setEnabled(true);
        } else {
            mConfirmBtn.setEnabled(false);
        }
        if(jsonObject.getBiddableAmount()!=0){
            if (!isLast) {
                ManyiUtils.showKeyBoard(this, mInputPriceEdit);
                scrollToBottom();
            }
        }
        if (infoResponse.getAvailableBalance() < infoResponse.getMinAmount() | isLast) {
            //可用余额小于起购金额
            tvAllBuy.setVisibility(View.GONE);
        } else {
            tvAllBuy.setVisibility(View.VISIBLE);
            EventLog.upEventLog("201610283", "qegm_show", "ptfcb_syt");
        }

        //请求卡券相关的逻辑
//        getBestVoucher(initialInputValue);
        getBestVoucher(initialInputValue);

    }

    private void scrollToBottom() {
        HandlerUtil.postDelay(new Runnable() {
            @Override
            public void run() {
                svRegular.scrollTo(0, svRegular.getHeight());
            }
        }, 400);
    }

    @Override
    public void reloadData() {
        super.reloadData();
        initBaseInfo();
    }


    public void getBestVoucher(String amount) {

        GetAppropriateCouponRequest request = new GetAppropriateCouponRequest();
        request.setAmount(amount);
        request.setProductId(productId);
        ServiceSender.exec(this, request, new IwjwRespListener<GetAppropriateCouponResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onJsonSuccess(GetAppropriateCouponResponse jsonObject) {
                showContentView();
                bindAppropriateCouponData(jsonObject);
            }

            @Override
            public void onFailInfo(String errorInfo) {
                showContentView();
                ToastUtil.showInCenter(errorInfo);
            }
        });

    }

    private void bindAppropriateCouponData(GetAppropriateCouponResponse jsonObject) {

        availableVoucherNumber = jsonObject.getAvailableVoucherNumber();
        voucherType = jsonObject.getVoucherType();
        voucherValue = jsonObject.getAmountCentString();
        addRateDay = jsonObject.getAddRateDay();
        voucherRate = jsonObject.getAddRate();
        String text = "";
        if (availableVoucherNumber > 0){
            voucherId = jsonObject.getVoucherId();
            if (jsonObject.getVoucherType() == 73) {
                if (jsonObject.getMinAmountCent() > 0) {
                    text += "[加息券]满" + jsonObject.getMinAmountCent() + "元享加息" + jsonObject.getAddRate() + "%";
                } else {
                    text += "[加息券]享加息" + jsonObject.getAddRate() + "%";
                }
            }
            if (jsonObject.getVoucherType() == 74) {
                if (jsonObject.getMinAmountCent() > 0) {
                    text += "[返金券]满" + jsonObject.getMinAmountCent() + "元返" + jsonObject.getAmountCentString() + "元";
                } else {
                    text += "[返金券]返" + jsonObject.getAmountCentString() + "元";
                }
            }
        }else {
            text = "暂无可用";
        }
        tvTicketText.setText(text);
        calculateProfit();
    }

    /**
     * 页面数据获取请求
     */

    public void initBaseInfo() {
        voucherId = -1;
        ruleModel = null;
        showLoadTranstView();
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
                    use = data.getIntExtra("doesUse", -1);
                    if (use == 1){
                        voucherId = data.getIntExtra("voucherId", -1);
                        int minAmountCent = data.getIntExtra("minAmountCent", -1);
                        String text = "";
                        voucherType = data.getIntExtra("voucherType",0);
                        switch (voucherType){
                            case 73://加息券
                                voucherRate = data.getDoubleExtra("addRate", -1);
                                addRateDay = data.getIntExtra("addRateDay", -1);
                                if(minAmountCent > 0){
                                    text += "[加息券]满"+minAmountCent+"元享加息"+voucherRate+"%";
                                }else{
                                    text += "[加息券]享加息"+voucherRate+"%";
                                }
                                break;
                            case 74://返金券
                                voucherValue = data.getStringExtra("voucherValue");
                                if(minAmountCent > 0){
                                    text += "[返金券]满"+minAmountCent+"元返"+voucherValue+"元";
                                }else{
                                    text += "[返金券]返"+voucherValue+"元";
                                }
                                break;
                        }
                        calculateProfit();
                        tvTicketText.setText(text);
                    }

                } else if (resultCode == 1000) {//点击了不使用

                    voucherRate = -1;
                    voucherId = -1;
                    addRateDay = -1;
                    voucherValue = "";
                    if (availableVoucherNumber > 0) {
                        tvTicketText.setText(availableVoucherNumber + "张可用");
                        tvTicketText.setTextColor(Color.parseColor("#212121"));
                    } else {
                        tvTicketText.setText("暂无可用");
                        tvTicketText.setTextColor(Color.parseColor("#757575"));
                    }
                    calculateProfit();
                }
                break;
        }
    }

    class ActivityListAdapter extends BaseAdapter {

        private List<ActivityRuleModel> ruleList;

        public ActivityListAdapter(List<ActivityRuleModel> ruleList) {
            this.ruleList = ruleList;
        }

        @Override
        public int getCount() {
            return ruleList.size();
        }

        @Override
        public Object getItem(int position) {
            return ruleList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.wallet_activity_list_item, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ActivityRuleModel detail = (ActivityRuleModel) getItem(position);
            viewHolder.index.setText(position + 1 + ".");
            viewHolder.memo.setText(detail.getMemo());
            return convertView;
        }

        class ViewHolder {
            @Bind(R.id.index_num)
            TextView index;
            @Bind(R.id.memo_text)
            TextView memo;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
    @Override
    public void verifyProtocolListLogical(List<Protocol> list) {
//        if (list != null && list.size() > 0) {
//            mAgreementLayout.setVisibility(View.VISIBLE);
//        }else{
//            mAgreementLayout.setVisibility(View.GONE);
//        }

    }
}
