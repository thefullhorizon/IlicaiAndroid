package com.ailicai.app.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.eventbus.FinancePayEvent;
import com.ailicai.app.model.bean.BuyHuoqibaoResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.view.transaction.TransactionListActivity;
import com.ailicai.app.widget.IWTopTitleView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 钱包转入结果
 * Created by Gerry on 2015/12/29.
 */
public class CurrentRollInResultActivity extends BaseBindActivity {
    public static String KEY = "response";
    @Bind(R.id.success_layout)
    LinearLayout mSuccessLayout;
    @Bind(R.id.no_success_layout)
    LinearLayout mNoSuccessLayout;
    @Bind(R.id.image_icon)
    TextView mImageIcon;
    @Bind(R.id.top_title_view)
    IWTopTitleView mTopTitleView;
    @Bind(R.id.mesg_result_text)
    TextView mMesgResultText;
    @Bind(R.id.mesg_tips_1)
    TextView mMesgTips1;
    @Bind(R.id.mesg_tips_2)
    TextView mMesgTips2;
    @Bind(R.id.confirm_success)
    Button mConfirmSuccess;
    @Bind(R.id.confirm_repay)
    Button mConfirmRepay;
    @Bind(R.id.banner_layout)
    RelativeLayout mBannerLayout;
    @Bind(R.id.yield_value_text)
    TextView mYieldValueText;
    @Bind(R.id.amount_txt)
    TextView mAmountTxt;
    @Bind(R.id.calculation_date)
    TextView mCalculationDate;
    @Bind(R.id.give_date)
    TextView mGiveDate;
    @Bind(R.id.cash_voucher_layout)
    LinearLayout cashVoucherLayout;
    @Bind(R.id.cash_value)
    TextView cashValue;
    @Bind(R.id.xjq_tips)
    TextView xjqTips;
    @Bind(R.id.top_splid_line)
    LinearLayout topSplidLine;
    @Bind(R.id.bottom_card_layout)
    LinearLayout bottomCardLayout;
    private BuyHuoqibaoResponse response;

    @Override
    public int getLayout() {
        return R.layout.activity_roll_in_result;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        //response = MyIntent.getDetailResponse(getIntent());
        response = (BuyHuoqibaoResponse) getIntent().getExtras().getSerializable(KEY);
        //处理结果，S成功;P处理中;F失败
        String bizStatus = response.getBizStatus();
        //post 事件刷新相关页面
        postEventBus(bizStatus);
        switch (bizStatus) {
            case "S":
                //现金券逻辑
                cashVoucherLayout.setVisibility(View.GONE);
                String cash = response.getActivityMsg();
                if (!"".equals(cash)) {
                    cashVoucherLayout.setVisibility(View.VISIBLE);
                    cashValue.setText(cash);
                } else {
                    xjqTips.setVisibility(View.GONE);
                    cashVoucherLayout.setVisibility(View.GONE);
                }
                mSuccessLayout.setVisibility(View.VISIBLE);
                mNoSuccessLayout.setVisibility(View.GONE);
                mBannerLayout.setVisibility(View.GONE);
                mImageIcon.setTextColor(ContextCompat.getColor(this, R.color.color_succeed));
                mImageIcon.setText(R.string.succeed);
                mAmountTxt.setText(getResources().getString(R.string.roll_in_amount_text, CommonUtil.amountWithTwoAfterPoint(response.getAmount())));
                mCalculationDate.setText(response.getCalculationDate());
                mGiveDate.setText(response.getGiveDate());
                mConfirmRepay.setText("继续转入");
                bottomCardLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
                topSplidLine.setVisibility(View.VISIBLE);
                //mYieldValueText.setText(Html.fromHtml(getResources().getString(R.string.yield_value_txt, response.getAnnualizedYield())));
                EventLog.upEventLog("201610282", "show", "into_success");
                break;
            case "F":
                xjqTips.setVisibility(View.GONE);
                cashVoucherLayout.setVisibility(View.GONE);
                mSuccessLayout.setVisibility(View.GONE);
                mNoSuccessLayout.setVisibility(View.VISIBLE);
                mBannerLayout.setVisibility(View.GONE);
                mImageIcon.setTextColor(ContextCompat.getColor(this, R.color.color_failed));
                mImageIcon.setText(R.string.failured);
                mMesgResultText.setText("转入失败");
                mConfirmRepay.setText("继续转入");
                mMesgTips1.setText("" + response.getMessage());
                mMesgTips1.setVisibility(View.VISIBLE);
                mMesgTips2.setVisibility(View.VISIBLE);
                mMesgTips2.setText(Html.fromHtml(getResources().getString(R.string.rollin_fail_tips_txt, "1",
                        response.getBankName() + "(" + response.getCardNo() + ")")));
                bottomCardLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                topSplidLine.setVisibility(View.GONE);
                EventLog.upEventLog("201610282", "show", "into_fail");
                break;
            case "P":
                xjqTips.setVisibility(View.GONE);
                cashVoucherLayout.setVisibility(View.GONE);
                mSuccessLayout.setVisibility(View.GONE);
                mNoSuccessLayout.setVisibility(View.VISIBLE);
                mBannerLayout.setVisibility(View.GONE);
                mImageIcon.setTextColor(ContextCompat.getColor(this, R.color.color_waiting));
                mImageIcon.setText(R.string.waiting);
                mMesgResultText.setText("转入进行中");
                mConfirmRepay.setText("交易记录");
                mMesgTips1.setText("支付中，稍后请在交易记录中查询");
                mMesgTips1.setVisibility(View.VISIBLE);
                mMesgTips2.setVisibility(View.GONE);
                bottomCardLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                topSplidLine.setVisibility(View.GONE);
                EventLog.upEventLog("201610282", "show", "into_underway");
                break;
        }


    }

    /**
     * post 事件刷新相关页面
     *
     * @param bizStatus
     */
    public void postEventBus(String bizStatus) {
        FinancePayEvent payEvent = new FinancePayEvent();
        payEvent.setPayType(FinancePayEvent.PAY_IN);
        payEvent.setPayState(bizStatus);
        EventBus.getDefault().post(payEvent);
    }

    @Nullable
    @OnClick(R.id.confirm_success)
    public void confirmSuccess(Button success) {
        finish();
        String bizStatus = response.getBizStatus();
        switch (bizStatus) {
            case "S":
                //成功
                EventLog.upEventLog("201610282", "click_finish", "into_success");
                break;
            case "F":
                //失败
                EventLog.upEventLog("201610282", "click_finish", "into_fail");
                break;
            case "P":
                //进行中
                EventLog.upEventLog("201610282", "click_finish", "into_underway");
                break;
        }
    }

    @Nullable
    @OnClick(R.id.confirm_repay)
    public void confirmRepay(Button repay) {
        //处理结果，S成功;P处理中;F失败
        String bizStatus = response.getBizStatus();
        switch (bizStatus) {
            case "S":
                finish();
                MyIntent.startActivity(this, CurrentRollInActivity.class, "");
                EventLog.upEventLog("201610282", "click_goon", "into_success");
                break;
            case "F":
                finish();
                MyIntent.startActivity(this, CurrentRollInActivity.class, "");
                EventLog.upEventLog("201610282", "click_goon", "into_fail");
                break;
            case "P":
                finish();
                MyIntent.startActivity(this, TransactionListActivity.class, "");
                EventLog.upEventLog("201610282", "click_history", "into_underway");
                break;
        }

    }
}
