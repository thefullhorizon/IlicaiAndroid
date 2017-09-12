package com.ailicai.app.ui.reserve;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.eventbus.ReserveAvtivityFinishEvent;
import com.ailicai.app.model.bean.Product;
import com.ailicai.app.model.response.ReserveDetailResponse;
import com.ailicai.app.ui.asset.CapitalListProductDetailActivity;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.view.transaction.TransactionListActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by David on 16/3/7
 */
public class ReserveResultActivity extends BaseBindActivity {

    @Bind(R.id.llSuccess)
    LinearLayout llSuccess;
    @Bind(R.id.llProcessing)
    LinearLayout llProcessing;
    @Bind(R.id.tvReserveProduct)
    TextView tvReserveProduct;
    @Bind(R.id.tvReservePrice)
    TextView tvReservePrice;
    @Bind(R.id.tvLcqx)
    TextView tvLcqx;
    @Bind(R.id.tvNhll)
    TextView tvNhll;
    @Bind(R.id.btLeft)
    Button btLeft;
    @Bind(R.id.btRight)
    Button btRight;

    private String amount;
    private ReserveDetailResponse reserveResponse;
    private Product product;
    private int term;

    @Override
    public int getLayout() {
        return R.layout.activity_finance_reserve_result;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initIntentValue();
        setValueView();
    }

    private void initIntentValue() {
        amount = getIntent().getStringExtra("amount");
        term = getIntent().getIntExtra("term", 0);
        reserveResponse = (ReserveDetailResponse) getIntent().getSerializableExtra("ReserveDetailResponse");
        if (reserveResponse == null) reserveResponse = new ReserveDetailResponse();
        product = reserveResponse.getProduct();
        if (product == null) product = new Product();
    }

    private void setValueView() {
        if (!TextUtils.isEmpty(amount)) {
            //预约进行
            llSuccess.setVisibility(View.GONE);
            llProcessing.setVisibility(View.VISIBLE);
            tvReservePrice.setText(CommonUtil.amountWithTwoAfterPoint(Double.parseDouble(amount)) + "元");
            tvReserveProduct.setText(product.getProductName());
            tvLcqx.setText(term + "天内");
            tvNhll.setText(product.getYearInterestRateStr());
            btLeft.setText("完成");
            btRight.setText("交易记录");
        }
    }

    /**
     * 完成
     */
    @OnClick(R.id.btLeft)
    void onAppointmentGoOn() {
        finish();
    }

    /**
     * 查看详情,交易记录
     */
    @OnClick(R.id.btRight)
    void onAppointmentLookup() {
        if (!TextUtils.isEmpty(amount)) {
            //交易记录
            Intent intent = new Intent(this, TransactionListActivity.class);
            startActivity(intent);
            finish();
        } else {
            //查看详情
            Intent intent = new Intent(this, CapitalListProductDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(CapitalListProductDetailActivity.PRODUCT_ID, getIntent().getStringExtra("bidOrderNo"));
            bundle.putBoolean(CapitalListProductDetailActivity.IS_RESERVE, true);
            bundle.putBoolean(CapitalListProductDetailActivity.START_CAPIYAL, true);
            intent.putExtras(bundle);
            startActivity(intent);
            EventBus.getDefault().post(new ReserveAvtivityFinishEvent());
            finish();
        }
    }
}
