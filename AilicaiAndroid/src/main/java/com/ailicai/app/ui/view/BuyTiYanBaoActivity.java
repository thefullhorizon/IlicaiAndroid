package com.ailicai.app.ui.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.MathUtil;
import com.ailicai.app.common.utils.SpannableUtil;
import com.ailicai.app.model.request.TiyanbaoDetailRequest;
import com.ailicai.app.model.response.BuyTiyanbaoInitResponse;
import com.ailicai.app.model.response.BuyTiyanbaoResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.login.AccountInfo;
import com.huoqiu.framework.util.CheckDoubleClick;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 体验宝购买页
 * Created by liyanan on 16/8/15.
 */
public class BuyTiYanBaoActivity extends BaseBindActivity {
    public static String EXTRA_KEY = "key";
    public final static int REQUEST_CODE_COUPON = 1000;
    @Bind(R.id.tv_ti_yan_title)
    TextView tvTiYanTitle;
    @Bind(R.id.tv_money_count)
    TextView tvMoneyCount;
    @Bind(R.id.tv_ti_yan_money)
    TextView tvTiYanMoney;
    @Bind(R.id.tv_profit_text)
    TextView tvProfitText;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    @Bind(R.id.icon_arrow)
    View iconArrow;
    private BuyTiYanBaoPresenter presenter;

    BuyTiyanbaoInitResponse response;

    private int currentCouponId;//当前卡券id
    private double couponAmount; //金额

    @Override
    public int getLayout() {
        return R.layout.activity_buy_tiyan_bao;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        presenter = new BuyTiYanBaoPresenter(this);
        response = (BuyTiyanbaoInitResponse) getIntent().getSerializableExtra(EXTRA_KEY);
        initData();
    }

    /**
     * 初始化数据
     */
    public void initData() {
        tvTiYanTitle.setText("体验宝 (年化" + response.getYearInterestRateStr() + " " + response.getHorizonStr() + ")");
        if (response.getHasCoupon() == 1) {
            currentCouponId = response.getCouponId();
            couponAmount = response.getCouponAmount();
            //有体验金
            initHasCoupon(response.getCouponTitle());
        } else {
            //无体验金
            tvMoneyCount.setText("0");
            iconArrow.setVisibility(View.GONE);
            tvTiYanMoney.setText("暂无体验金");
            tvTiYanMoney.setTextColor(Color.parseColor("#757575"));
            tvProfitText.setVisibility(View.GONE);
            btnConfirm.setEnabled(false);
        }
    }

    private void initHasCoupon(String couponTitle) {
        tvMoneyCount.setText(MathUtil.subZeroAndDot(String.valueOf(couponAmount)));
        iconArrow.setVisibility(View.VISIBLE);
        tvTiYanMoney.setText(couponTitle);
        //体验金*期限*年化利率/360保留2位小数
        double profit = couponAmount * response.getHorizon() * response.getYearInterestRate() / 360;
        String profitText = MathUtil.saveTwoDecimal(profit);
        SpannableUtil spanUtil = new SpannableUtil(this);
        SpannableStringBuilder builder;
        if (response.getIsOpen() == 0 && response.getExpiredDays() > 0) {
            //如果用户未开户且有收益有效期
            builder = spanUtil.getSpannableString("预计收益 ", profitText, " 元，收益回款后请于" + response.getDueDateStr() + "前提现转出，逾期将清零。",
                    R.style.text_12_757575, R.style.text_12_e84a01, R.style.text_12_757575);
        } else {
            //
            builder = spanUtil.getSpannableString("预计收益 ", profitText, " 元",
                    R.style.text_12_757575, R.style.text_12_e84a01, R.style.text_12_757575);
        }
        tvProfitText.setText(builder);
        tvProfitText.setVisibility(View.VISIBLE);
        btnConfirm.setEnabled(true);
    }

    /**
     * 点击确认,提交体验金
     */
    @OnClick(R.id.btn_confirm)
    void clickConfirm() {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        //判断是否有交易密码
        if (AccountInfo.isSetPayPwd()) {
            //有交易密码
            presenter.buyWithPwd(currentCouponId, couponAmount, response.getActivityId());
        } else {
            //直接购买体验宝
            presenter.buyWithNoPwd(response.getActivityId(), currentCouponId);
        }

    }

    /**
     * 点击进入体验金卡券列表
     */
    @OnClick(R.id.rl_coupon)
    void clickCoupon() {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            Intent intent = new Intent(this, TiYanBaoCouponH5Activity.class);
            intent.putExtra(TiYanBaoCouponH5Activity.EXTRA_URL, response.getCouponUrl()+"?"+currentCouponId);
            startActivityForResult(intent, REQUEST_CODE_COUPON);
        }
    }


    /**
     * 进入购买结果页面
     *
     * @param object
     */
    public void goToPayResultActivity(BuyTiyanbaoResponse object) {
        Intent mIntent = new Intent(mContext, BuyTiYanBaoResultActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(BuyTiYanBaoResultActivity.KEY, object);
        mIntent.putExtras(mBundle);
        mContext.startActivity(mIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_COUPON:
                if (resultCode == RESULT_OK) {
                    boolean noData = data.getBooleanExtra("noData", false);
                    if (noData) {
                        getTiYanBaoData();
                    } else {
                        currentCouponId = data.getIntExtra("couponId", -1);
                        couponAmount = data.getDoubleExtra("couponAmount", -1);
                        int expiredDays = data.getIntExtra("expiredDays", -1);
                        response.setExpiredDays(expiredDays);
                        long time = response.getBackDate() + expiredDays * 24 * 60 * 60 * 1000;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
                        response.setDueDateStr(dateFormat.format(new Date(time)));
                        initHasCoupon("体验金" + MathUtil.subZeroAndDot(String.valueOf(couponAmount)) + "元");
                    }
                }
                break;
        }


    }

    /**
     * 刷新体验金数据
     */
    public void getTiYanBaoData() {
        TiyanbaoDetailRequest request = new TiyanbaoDetailRequest();
        request.setActiviId(response.getActivityId());
        ServiceSender.exec(this, request, new IwjwRespListener<BuyTiyanbaoInitResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadTranstView();
            }

            @Override
            public void onJsonSuccess(BuyTiyanbaoInitResponse jsonObject) {
                showContentView();
                response = jsonObject;
                initData();
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                showContentView();
                showMyToast(errorInfo);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.removeActivity();
    }
}
