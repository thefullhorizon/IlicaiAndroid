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
import android.widget.EditText;
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
import com.ailicai.app.model.request.TransferPayBaseInfoRequest;
import com.ailicai.app.model.response.ApplyAssignmentResponse;
import com.ailicai.app.model.response.TransferPayBaseInfoResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.buy.ApplyCreditAssignmentPay;
import com.ailicai.app.ui.buy.IwPwdPayResultListener;
import com.ailicai.app.widget.IWTopTitleView;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.ManyiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.RoundingMode;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 提交转让
 * Created by Gong.
 */
public class TransferPayActivity extends BaseBindActivity {
    public static final String INTENT_EXTRA_ORDER_ID = "order_id";
    public static int INTENT_TRANSFER_OK = 1000;
    @Bind(R.id.regular_balance)
    TextView mRegularBalance;
    @Bind(R.id.tv_all_buy)
    TextView mTvAllBuy;
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
    @Bind(R.id.tv_ticket_text)
    TextView tvTicketText;
    @Bind(R.id.tv_fee_text)
    TextView tvFeeText;
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

    private TransferPayBaseInfoResponse infoResponse;
    private boolean isLast;//是否是最后一笔

    @Override
    public int getLayout() {
        return R.layout.activity_transfer_pay;
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
        SpannableStringBuilder builder = spannableUtil.getSpannableString("预计到账金额 ", "0.00", " 元", R.style.text_12_757575, R.style.text_12_757575, R.style.text_12_757575);
        tvProfitText.setText(builder);
        setTransferRule();
    }

    private void setTransferRule() {
        mTopTitleView.addRightText("转让规则", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> dataMap = ObjectUtil.newHashMap();
                dataMap.put(WebViewActivity.USEWEBTITLE, "true");
                dataMap.put(WebViewActivity.TOPVIEWTHEME, "false");
                dataMap.put(WebViewActivity.NEED_REFRESH, "0");
                if (null != infoResponse) {
                    dataMap.put(WebViewActivity.URL, infoResponse.getAssignRule());
                }
                MyIntent.startActivity(TransferPayActivity.this, WebViewActivity.class, dataMap);
                EventLog.upEventLog("803", "zrgz");
            }
        });
        mTopTitleView.getRightText().setAlpha(1);
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
        if (infoResponse == null) {
            return;
        }
        //可转额度
        double availableBalance = infoResponse.getApplyAmount();
        mInputPriceEdit.setText(MathUtil.subZeroAndDot(String.valueOf(availableBalance)));
        EventLog.upEventLog("803", "qbzr");
    }

    @OnTextChanged(value = R.id.input_price_edit, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged(Editable s) {
//        if (!"".equals(s.toString()) && mAgreementCheckbox.isChecked()) {
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
        double applyAmount = infoResponse.getApplyAmount();
        if (applyAmount > 0) {
            if (checkInputMoney()) {
                mConfirmBtn.setEnabled(true);
            } else {
                mConfirmBtn.setEnabled(false);
            }
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

    }

    /**
     * 计算收益
     */
    private void calculateProfit() {
        String moneyCountString = mInputPriceEdit.getText().toString();
        if (TextUtils.isEmpty(moneyCountString)) {
            SpannableUtil spannableUtil = new SpannableUtil(this);
            SpannableStringBuilder builder = spannableUtil.getSpannableString("预计到账金额: ", "0.00", " 元", R.style.text_12_757575, R.style.text_12_757575, R.style.text_12_757575);
            tvProfitText.setText(builder);
            tvTicketText.setText("0.00元");
            tvFeeText.setText("0.00元");
        } else {
            double moneyCount = Double.parseDouble(moneyCountString);
            if (moneyCount > 0) {
                double transferPrice = moneyCount + moneyCount * infoResponse.getYearInterestRate() * infoResponse.getHoldDays() / 360;
                //金额*天数*年利率/年的天数
                double transferFee;
                int limitDays = infoResponse.getLimitDays();//30
                int holdDays = infoResponse.getHoldDays();
                if (holdDays > limitDays) {
                    transferFee = 0;
                } else {
                    transferFee = Double.valueOf(MathUtil.saveTwoDecimal(transferPrice)) * infoResponse.getGt30CostRate();
                }
                String transferPriceStr = MathUtil.saveTwoDecimal(transferPrice);
                String transferFeeStr = MathUtil.saveTwoDecimal(transferFee);
                tvTicketText.setText(CommonUtil.amountWithTwoAfterPoint(Double.valueOf(transferPrice), RoundingMode.FLOOR) + "元");
                tvFeeText.setText(CommonUtil.amountWithTwoAfterPoint(Double.valueOf(transferFee),RoundingMode.FLOOR) + "元");
                SpannableUtil spannableUtil = new SpannableUtil(this);
                String inAccount = MathUtil.saveTwoDecimal(Double.valueOf(transferPriceStr) - Double.valueOf(transferFeeStr));
                SpannableStringBuilder builder = spannableUtil.getSpannableString("预计到账金额: ", CommonUtil.amountWithTwoAfterPoint(Double.parseDouble(inAccount)), " 元", R.style.text_12_757575, R.style.text_12_e84a01, R.style.text_12_757575);
                tvProfitText.setText(builder);
            }
        }
    }

    @OnClick(R.id.input_price_edit)
    public void OnEditTextClick(View v) {
        if (!"".equals(mInputPriceEdit.getText().toString())) {
            mInputPriceEdit.setSelection(mInputPriceEdit.getText().length());
        }
    }


    @OnClick(R.id.rl_activity_click)
    public void clickHelp() {
        if (null != infoResponse) {
            String content = infoResponse.getChargeTips();
//        String content = "如房产宝持有时间小于等于" + infoResponse.getLimitDays() + "天，手续费为转让价格的" + infoResponse.getGt30CostRateStr();
            showMessageTipsDialog("手续费", content);
            EventLog.upEventLog("803", "sxf");
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleFinancePayEvent(FinancePayEvent event) {
        if (!event.getPayState().equals("F")) {
            initBaseInfo();
        }
    }

    /**
     * 设置银行卡刷新余额
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
        if (CheckDoubleClick.isFastDoubleClick()) return;
        EventLog.upEventLog("803", "qrzr");
        //购买金额客户端校验
        if (!checkInputMoneyOnConfirm()) {
            return;
        }
        ManyiUtils.closeKeyBoard(this, mInputPriceEdit);
        double money = Double.parseDouble(mInputPriceEdit.getText().toString());
        //直接购买
        transferChanBaoWithInputPwd(money);
    }


    /**
     * 账户余额充足,直接购买房产宝
     *
     * @param money
     */
    private void transferChanBaoWithInputPwd(double money) {
        ApplyCreditAssignmentPay.Transfer regularPayInfo = new ApplyCreditAssignmentPay.Transfer();
        regularPayInfo.setAssignmentAmount(money);
        regularPayInfo.setProductId(infoResponse.getProductId());
        regularPayInfo.setCreditAssignmentId(infoResponse.getCreditId());
        regularPayInfo.setCreditAssignmentName(infoResponse.getProductName());
        ApplyCreditAssignmentPay applyCreditAssignmentPay = new ApplyCreditAssignmentPay(this, regularPayInfo, new IwPwdPayResultListener<ApplyAssignmentResponse>() {
            @Override
            public void onPayComplete(ApplyAssignmentResponse object) {
                //购买成功，跳转到成功页面
                goToPayResultActivity(object);
            }

            @Override
            public void onPayFailInfo(String msgInfo, String errorCode, ApplyAssignmentResponse object) {
                //购买失败
                showMessageTipsDialog("不可转让", msgInfo);
            }

            @Override
            public void onPayStateDelay(String msgInfo, ApplyAssignmentResponse object) {
            }

            @Override
            public void onPayPwdTryAgain() {
                onConfirmClick();
            }
        });
        applyCreditAssignmentPay.pay();
    }

    /**
     * 进入购买结果页面
     *
     * @param object
     */
    public void goToPayResultActivity(ApplyAssignmentResponse object) {
        Intent mIntent = new Intent(mContext, TransferSubmitSuccessActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(TransferSubmitSuccessActivity.KEY, object);
        mIntent.putExtras(mBundle);
        startActivityForResult(mIntent, INTENT_TRANSFER_OK);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == INTENT_TRANSFER_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    public boolean checkInputMoneyOnConfirm() {
        String money = mInputPriceEdit.getText().toString();
        double applyAmount = infoResponse.getApplyAmount();
        double minAmount = infoResponse.getMinAmount();
        if (!CommonUtil.isMoneyNumber(money)) {
            showMessageTipsDialog("不可转让", "请输入正确的购买金额");
            return false;
        } else if (applyAmount >= minAmount && minAmount > 0 && Double.parseDouble(money) < minAmount) {
            //剩余额度大于最小购买金额且购买金额小于起购金额
            showMessageTipsDialog("不可转让", "购买金额不能低于起购金额");
            return false;
        } else if (applyAmount >= 0 && Double.parseDouble(money) > applyAmount) {
            String msg = "转让金额超过了可转额度，请重新输入";
            showMessageTipsDialog("不可转让", msg);
            return false;
        }
        return true;
    }

    /**
     * 信息提示框
     */
    private void showMessageTipsDialog(String title, String msg) {
        AlertDialog.Builder b = new AlertDialog.Builder(mContext, R.style.AppCompatDialog);
        b.setTitle(title);
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
        mConfirmBtn.setText("确认转让");
        if (infoResponse == null) {
            return false;
        }
        double applyAmount = infoResponse.getApplyAmount();
        double minAmount = infoResponse.getMinAmount();
        double unit = infoResponse.getUnit();
        String money = mInputPriceEdit.getText().toString();
        if ("".equals(money)) {
            mErrorTips.setText("");
            mInputPriceEditLable.setVisibility(View.VISIBLE);
            mRegularBalance.setText("可转本金 " + CommonUtil.formatMoney(applyAmount) + " 元");
            mRegularBalance.setVisibility(View.VISIBLE);
            mTvAllBuy.setVisibility(View.VISIBLE);
            return false;
        } else if (Double.parseDouble(money) > 0) {

            if (!CommonUtil.isMoneyNumber(money)) {
                //ToastUtil.show("请输入正确的金额");
                mErrorTips.setText("请输入正确的转让金额");
                mErrorTips.setVisibility(View.VISIBLE);
                rlMyAccount.setVisibility(View.GONE);
                return false;
            } else if (applyAmount >= 0 && Double.parseDouble(money) > applyAmount) {
                String msg = "转让金额超过了可转额度，请重新输入";
                mErrorTips.setText(msg);
                mErrorTips.setVisibility(View.VISIBLE);
                rlMyAccount.setVisibility(View.GONE);
                return false;
            } else if (applyAmount > 0 && applyAmount < minAmount) {
                setTvProfitText(applyAmount);
                ManyiUtils.closeKeyBoard(this, mInputPriceEdit);
                rlMyAccount.setVisibility(View.GONE);
                return true;
            } else if (Double.parseDouble(money) < unit) {
                mErrorTips.setText("请输入" + CommonUtil.numberFormat(unit) + "的整数倍");
                mErrorTips.setVisibility(View.VISIBLE);
                rlMyAccount.setVisibility(View.GONE);
                return false;
            } else if (Double.parseDouble(money) % unit != 0) {
                mErrorTips.setText("请输入" + CommonUtil.numberFormat(unit) + "的整数倍");
                mErrorTips.setVisibility(View.VISIBLE);
                rlMyAccount.setVisibility(View.GONE);
                return false;
            } else if (Double.parseDouble(money) >= minAmount) {
                setTvProfitText(Double.parseDouble(money));
            }
        } else if ((Double.parseDouble(money) == 0)) {
            return false;
        }
        return true;
    }

    private void setTvProfitText(double moneyCount) {
        mErrorTips.setText("");
        mErrorTips.setVisibility(View.GONE);
        rlMyAccount.setVisibility(View.VISIBLE);
        if (null != infoResponse) {
            double profit = moneyCount * infoResponse.getYearInterestRate() * infoResponse.getHoldDays() / 360;
            mRegularBalance.setText("已产生利息 " + CommonUtil.amountWithTwoAfterPoint(profit,RoundingMode.FLOOR) + " 元");
        }
    }

    public void bindViewData(TransferPayBaseInfoResponse jsonObject) {
        this.infoResponse = jsonObject;
        mInputPriceEditLable.setText(infoResponse.getHint());
        tvLeaveAccount.setText("剩余期限 " + jsonObject.getRemainDays());
        mRegularBalance.setText("可转本金 " + CommonUtil.amountWithTwoAfterPoint(jsonObject.getApplyAmount()) + " 元");
        double minAmount = infoResponse.getMinAmount();
        double applyAmount = jsonObject.getApplyAmount();
        if (applyAmount > 0 && applyAmount <= minAmount) {
            //剩余额度小于起购金额,直接填入剩余额度且不可修改
            mInputPriceEdit.setText(MathUtil.subZeroAndDot(jsonObject.getApplyAmount()));
            mInputPriceEdit.setEnabled(false);
            mInputPriceEdit.setTextColor(Color.parseColor("#9b9b9b"));
            if (!TextUtils.isEmpty(jsonObject.getTips())) {
                tvLastMoneyText.setVisibility(View.VISIBLE);
                tvLastMoneyText.setText(jsonObject.getTips());
            }
            tvClear.setEnabled(false);
            tvClear.setVisibility(View.GONE);
            mInputPriceEditLable.setVisibility(View.GONE);
            isLast = true;
            mTvAllBuy.setVisibility(View.GONE);
            setTvProfitText(applyAmount);
            //计算收益
            calculateProfit();
        } else {
            ManyiUtils.showKeyBoard(this, mInputPriceEdit);
            mTvAllBuy.setVisibility(View.VISIBLE);
            EventLog.upEventLog("201610284", "qezr_show");
        }
        mConfirmBtn.setEnabled(isLast);
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
//        TransferPayBaseInfoResponse jsonObject = new TransferPayBaseInfoResponse();
//        jsonObject.setApplyAmount(2000);
//        jsonObject.setMinAmount(1000);
//        jsonObject.setUnit(1000);
//        jsonObject.setHint("请输入1000的整数倍");
//        jsonObject.setYearInterestRate(0.05);
//        bindViewData(jsonObject);
        showLoadTranstView();
        String orderId = "";
        try {
            orderId = getIntent().getExtras().getString(INTENT_EXTRA_ORDER_ID);
        } catch (Exception e) {
        }
        TransferPayBaseInfoRequest request = new TransferPayBaseInfoRequest();
        request.setOrderNo(orderId);
        ServiceSender.exec(this, request, new IwjwRespListener<TransferPayBaseInfoResponse>(this) {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onJsonSuccess(TransferPayBaseInfoResponse jsonObject) {
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

}
