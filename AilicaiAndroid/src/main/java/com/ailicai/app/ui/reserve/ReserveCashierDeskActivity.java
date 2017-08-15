package com.ailicai.app.ui.reserve;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.MathUtil;
import com.ailicai.app.eventbus.ReservePayEvent;
import com.ailicai.app.model.bean.Product;
import com.ailicai.app.model.bean.Protocol;
import com.ailicai.app.model.response.reserve.AdvanceDepositAndApplyAppResponse;
import com.ailicai.app.model.response.ApplyReserveAppResponse;
import com.ailicai.app.model.response.ReserveDetailResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.buy.IwPwdPayResultListener;
import com.ailicai.app.ui.buy.ReservePay;
import com.ailicai.app.ui.view.ProtocolHelper;
import com.ailicai.app.widget.DialogBuilder;
import com.ailicai.app.widget.IWTopTitleView;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.util.CheckDoubleClick;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.List;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 预约收银台页面
 * Created by Owen on 16/7/1
 */
public class ReserveCashierDeskActivity extends BaseBindActivity implements IWTopTitleView.TopTitleOnClickListener {

    @Bind(R.id.tvKl)
    TextView tvKl;
    @Bind(R.id.tvParamsHint)
    TextView tvParamsHint;

    @Bind(R.id.tvMoneyLable)
    TextView tvMoneyLable;
    @Bind(R.id.etMoney)
    EditText etMoney;
    @Bind(R.id.tv_clear)
    TextView tvClear;

    @Bind(R.id.tvBalance)
    TextView tvBalance;
    @Bind(R.id.tvInputErrorHint)
    TextView tvInputErrorHint;

    @Bind(R.id.agreement_layout)
    LinearLayout mAgreementLayout;
    @Bind(R.id.cbAgreement)
    CheckBox cbAgreement;

    @Bind(R.id.btConfirm)
    Button btConfirm;

    private ReserveDetailResponse reserveResponse;
    private Product product;
    private int term;
    private String reservePwd;
    private String yearRateZone;//年化收益率区间

    private boolean isEnough;
    private double rechargeAmountSum;

    @Override
    public int getLayout() {
        return R.layout.activity_cashier_desk;
    }


    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initIntentValue();
        initView();
        initProtocol();
    }

    private ProtocolHelper protocolHelper;

    private void initProtocol() {
        protocolHelper = new ProtocolHelper(this, ProtocolHelper.TYPE_RESERVE_PAY);
        protocolHelper.loadProtocol(false);
    }

    private void initIntentValue() {
        reserveResponse = (ReserveDetailResponse) getIntent().getSerializableExtra("ReserveDetailResponse");
        if (reserveResponse == null) reserveResponse = new ReserveDetailResponse();
        product = reserveResponse.getProduct();
        if (product == null) product = new Product();
        term = getIntent().getIntExtra("term", 0);
        reservePwd = getIntent().getStringExtra("reservePwd");
        yearRateZone = getIntent().getStringExtra("yearRateZone");
        //如果为空，则取后端返回的所有收益范围
        yearRateZone = TextUtils.isEmpty(yearRateZone) ? product.getYearInterestRateStr():yearRateZone;
    }

    private void initView() {
        if (!TextUtils.isEmpty(reservePwd)) tvKl.setVisibility(View.VISIBLE);
        tvParamsHint.setText(product.getProductName() + "  " + yearRateZone + "  " + term + "天内");
        tvBalance.setText("账户可用余额 " + CommonUtil.formatMoneyForFinance(reserveResponse.getAvailableBalance()) + " 元");//账户可用余额
        tvMoneyLable.setText(reserveResponse.getReserveAmtMemo());
        etMoney.setInputType(InputType.TYPE_CLASS_NUMBER);
        etMoney.addTextChangedListener(mTextWatcher);
    }

    /**
     * 预约金额清空按钮
     */
    @OnClick(R.id.tv_clear)
    public void deleteEditText() {
        etMoney.setText("");
        checkReserveBuy();
    }


    @OnCheckedChanged(R.id.cbAgreement)
    public void onCbClick() {
        checkReserveBuy();
    }

    /**
     * 协议
     */
    @OnClick(R.id.reserve_delegate)
    public void onDelegateClick() {
        protocolHelper.loadProtocol(true);
    }


    /**
     * 确认预约
     */
    @OnClick(R.id.btConfirm)
    public void onConfirmClick() {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        if (reserveResponse.getBankLimit() != 0 && rechargeAmountSum > reserveResponse.getBankLimit()) {
            showMyToast("单笔最多可转入" + reserveResponse.getBankLimit() + "元");
            return;
        }
        final Product product = reserveResponse.getProduct();
        final ReservePay.ReservePayInfo payInfo = new ReservePay.ReservePayInfo();
        payInfo.setProductId(product.getProductId());
        payInfo.setAmount(etMoney.getText().toString().length() == 0 ? 0 : Integer.valueOf(etMoney.getText().toString()));
        payInfo.setRechargeAmount(rechargeAmountSum);
        payInfo.setProductName(product.getProductName());
        payInfo.setReservePwd(reservePwd);
        payInfo.setTerm(term);
        payInfo.setEnough(isEnough);
        payInfo.setYearInterestRateStr(yearRateZone);
        new ReservePay(this, payInfo, new IwPwdPayResultListener() {
            @Override
            public void onPayPwdTryAgain() {
                onConfirmClick();
            }

            @Override
            public void onPayComplete(Object object) {
                EventBus.getDefault().post(new ReservePayEvent());
                double reserveAmout = Double.parseDouble(etMoney.getText().toString());
                product.setBiddableAmount(product.getBiddableAmount() - reserveAmout);
                reserveResponse.setAvailableBalance(reserveAmout >= reserveResponse.getAvailableBalance() ? 0 : reserveResponse.getAvailableBalance() - reserveAmout);
                Intent intent = new Intent(ReserveCashierDeskActivity.this, ReserveResultActivity.class);
                intent.putExtra("term", term);
                intent.putExtra("ReserveDetailResponse", reserveResponse);
                if (payInfo.isEnough()) {
                    //直接预约
                    ApplyReserveAppResponse response = (ApplyReserveAppResponse) object;
                    intent.putExtra("bidOrderNo", response.getBidOrderNo());
                } else {
                    //充值并预约
                    AdvanceDepositAndApplyAppResponse response = (AdvanceDepositAndApplyAppResponse) object;
                    intent.putExtra("bidOrderNo", response.getBidOrderNo());
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onPayStateDelay(String msgInfo, Object object) {
                EventBus.getDefault().post(new ReservePayEvent());
                Intent intent = new Intent(ReserveCashierDeskActivity.this, ReserveResultActivity.class);
                intent.putExtra("amount", etMoney.getText().toString());
                intent.putExtra("term", term);
                intent.putExtra("ReserveDetailResponse", reserveResponse);
                startActivity(intent);
                finish();
            }

            @Override
            public void onPayFailInfo(String msgInfo, String errorCode, Object object) {
                Intent intent = new Intent(ReserveCashierDeskActivity.this, ReserveFailActivity.class);

                if (object instanceof Response) {
                    msgInfo = ((Response) object).getMessage();
                }

                intent.putExtra("msgInfo",msgInfo);
                startActivity(intent);
                finish();
            }
        }).pay();
    }

    private void checkReserveBuy() {
        if (TextUtils.isEmpty(etMoney.getText().toString())) {
            tvInputErrorHint.setVisibility(View.GONE);
            tvBalance.setVisibility(View.VISIBLE);
            btConfirm.setText("确认");
            btConfirm.setEnabled(false);
            return;
        }
        tvInputErrorHint.setVisibility(View.VISIBLE);
        tvBalance.setVisibility(View.GONE);
        btConfirm.setText("确认");
        btConfirm.setEnabled(false);
        isEnough = true;
        double amount = Double.parseDouble(etMoney.getText().toString());
//        if (reserveResponse.getAvailableBalance() < product.getMinAmount()) {
//            tvInputErrorHint.setText(String.format("账户可用余额不足%s元，请先转入钱包", String.valueOf((int) product.getMinAmount())));
//        } else
        if (product.getBidUnit() > 0 && amount % product.getBidUnit() != 0) {
            tvInputErrorHint.setText(String.format("请输入%s的整数倍", (int)product.getBidUnit() + "元"));
        } else if (amount < product.getMinAmount()) {
            tvInputErrorHint.setText(String.format("预约金额必须大于等于%s元", product.getMinAmountStr()));
        } else if (amount > product.getBiddableAmount() && TextUtils.isEmpty(reservePwd)) {
            tvInputErrorHint.setText("预约金额超过当前剩余额度");
        } else {
            if (amount > reserveResponse.getAvailableBalance()) {
                isEnough = false;
                BigDecimal offset = (new BigDecimal(amount)).subtract(new BigDecimal(reserveResponse.getAvailableBalance()));
                rechargeAmountSum = offset.doubleValue();
                btConfirm.setText("账户可用余额不足，需充值" + MathUtil.saveTwoDecimal(rechargeAmountSum) + "元");
            }
            tvInputErrorHint.setVisibility(View.GONE);
            tvBalance.setVisibility(View.VISIBLE);
            tvInputErrorHint.setText("");
            if (cbAgreement.isChecked()) {
                btConfirm.setEnabled(true);
            }
        }
    }


    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String input = s.toString();
            if (input.startsWith("0") && input.length() > 1 && !input.contains(".")) {
                input = input.substring(1, input.length());
                etMoney.setText(input);
                etMoney.setSelection(input.length());
                return;
            }
            checkReserveBuy();
            if (s.length() > 0) {
                tvMoneyLable.setVisibility(View.GONE);
                tvClear.setVisibility(View.VISIBLE);
            } else {
                tvMoneyLable.setVisibility(View.VISIBLE);
                tvClear.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public boolean onBackClick() {
        if (TextUtils.isEmpty(reservePwd)) {
            return false;
        } else {
            onBackWarning();
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(reservePwd)) {
            super.onBackPressed();
        } else {
            onBackWarning();
        }
    }

    public void onBackWarning() {
        DialogBuilder.showSimpleDialog(this, "", "您是否放弃使用口令预约", "再想想", null, "放弃预约", new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
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
