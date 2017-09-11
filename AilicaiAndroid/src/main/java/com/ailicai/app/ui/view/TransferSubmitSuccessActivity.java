package com.ailicai.app.ui.view;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.SpannableUtil;
import com.ailicai.app.model.response.ApplyAssignmentResponse;
import com.ailicai.app.ui.base.BaseBindActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 房产宝转让提交成功页面
 * Created by liyanan on 16/7/29.
 */
public class TransferSubmitSuccessActivity extends BaseBindActivity {
    public static String KEY = "response";
    @Bind(R.id.tv_transfer_product)
    TextView tvProduct;
    @Bind(R.id.tv_transfer_price)
    TextView tvPrice;
    @Bind(R.id.tv_regular_status_label)
    TextView tvStatusLabel;

    ApplyAssignmentResponse response;

    @Override
    public int getLayout() {
        return R.layout.activity_transfer_submit_success;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        response = (ApplyAssignmentResponse) getIntent().getExtras().getSerializable(KEY);
        initData();
    }

    private void initData() {
        if (null != response) {
            tvProduct.setText("转让产品 " + response.getProductName() + "号");
            SpannableUtil spanUtil = new SpannableUtil(this);
            String transfetPrice = CommonUtil.amountWithTwoAfterPoint(Double.valueOf(TextUtils.isEmpty(response.getProductPrice()) ? "0.00":response.getProductPrice()));
            SpannableStringBuilder builder1 = spanUtil.getSpannableString("转让价格 ", transfetPrice, "元",
                    R.style.text_13_757575, R.style.text_13_e84a01, R.style.text_13_757575);
            tvPrice.setText(builder1);
            SpannableStringBuilder builder2 = spanUtil.getSpannableString("转让申请提交后，如在", response.getEndDate(), "前未能转让成功，您将继续持有剩余的本金。",
                    R.style.text_13_757575, R.style.text_13_e84a01, R.style.text_13_757575);
            tvStatusLabel.setText(builder2);
        }
    }

    @Override
    public void onBackPressed() {
        finishAndBackToDetail();
    }

    @OnClick(R.id.btn_finish)
    void clickFinish() {
        finishAndBackToDetail();
    }

    private void finishAndBackToDetail() {
        setResult(RESULT_OK);
        finish();
    }
}
