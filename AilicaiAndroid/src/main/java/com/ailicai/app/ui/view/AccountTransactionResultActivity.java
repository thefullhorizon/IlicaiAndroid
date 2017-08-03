package com.ailicai.app.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.eventbus.AccountTransactionEvent;
import com.ailicai.app.model.bean.BuyHuoqibaoResponse;
import com.ailicai.app.model.response.SaleHuoqibaoResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.view.transaction.TransactionListActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 账户操作结果页面 定义充值
 * Created by nanshan on 2017/07/11.
 */
public class AccountTransactionResultActivity extends BaseBindActivity {

    @Bind(R.id.image_icon)
    TextView mImageIcon;
    @Bind(R.id.result_status)
    TextView mResultStatus;
    @Bind(R.id.result_status_detail)
    TextView mResultStatusDetail;

    @Bind(R.id.result_fail_layout)
    LinearLayout mResultFailLayout;
    @Bind(R.id.result_fail_detail)
    TextView mResultFailDetail;

    @Bind(R.id.confirm_left)
    Button mConfirmLeft;
    @Bind(R.id.confirm_right)
    Button mConfirmRight;

    public static String KEY = "response";
    private BuyHuoqibaoResponse topUpResponse;
    private SaleHuoqibaoResponse withdrawResponse;
    public static String TOPUP = "topup";
    public static String WITHDRAW = "withdraw";
    public static String TRANSACTIONTYPE = "transactionType";
    private String transactionType;
    private int transactionClass;
    private String bizStatus;//处理结果，S成功;P处理中;F失败

    @Override
    public int getLayout() {
        return R.layout.activity_account_transaction_result;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        transactionType = (String) getIntent().getExtras().getSerializable(TRANSACTIONTYPE);
        if (TOPUP.equals(transactionType)){
            topUpResponse = (BuyHuoqibaoResponse) getIntent().getExtras().getSerializable(KEY);
            bizStatus = topUpResponse.getBizStatus();
            transactionClass = AccountTransactionEvent.TOPUP;
        }else if (WITHDRAW.equals(transactionType)){
            withdrawResponse = (SaleHuoqibaoResponse) getIntent().getExtras().getSerializable(KEY);
            bizStatus = withdrawResponse.getBizStatus();
            transactionClass = AccountTransactionEvent.WITHDRAW;
        }
        postEventBus(bizStatus);
        switch (bizStatus) {
            case "S":
                mImageIcon.setTextColor(ContextCompat.getColor(this, R.color.color_succeed));
                mImageIcon.setText(R.string.succeed);

                if (TOPUP.equals(transactionType)){
                    mResultStatus.setText(getResources().getString(R.string.topup_amount_text, CommonUtil.numberFormatWithTwoDigital(topUpResponse.getAmount())+""));
                    mResultStatusDetail.setVisibility(View.GONE);
                    mConfirmLeft.setText("完成");
                    mConfirmRight.setText("继续充值");
                }else if (WITHDRAW.equals(transactionType)){
                    mResultStatus.setText("提现成功");
                    mResultStatusDetail.setVisibility(View.VISIBLE);
                    mResultStatusDetail.setText(Html.fromHtml(getResources().getString(R.string.withdraw_tips_text, CommonUtil.numberFormatWithTwoDigital(withdrawResponse.getAmount())+"", withdrawResponse.getGiveDate())));
                    mConfirmLeft.setVisibility(View.GONE);
                    mConfirmRight.setText("完成");
                }
                EventLog.upEventLog("201610282", "show", "out_success");
                break;
            case "F":
                mImageIcon.setTextColor(ContextCompat.getColor(this, R.color.color_failed));
                mImageIcon.setText(R.string.failured);
                if (TOPUP.equals(transactionType)){
                    mResultStatus.setText("充值失败");
                    mResultStatusDetail.setText(topUpResponse.getMessage());
                    mResultFailLayout.setVisibility(View.VISIBLE);
                    mResultFailDetail.setText(Html.fromHtml(getResources().getString(R.string.rollin_fail_tips_txt, "1",
                            topUpResponse.getBankName() + "(" + topUpResponse.getCardNo() + ")")));
                    mConfirmLeft.setText("完成");
                    mConfirmRight.setText("继续充值");
                }else if (WITHDRAW.equals(transactionType)){
                    mResultStatus.setText("提现失败");
                    mResultStatusDetail.setText(withdrawResponse.getMessage());
                    mConfirmLeft.setText("继续提现");
                    mConfirmRight.setText("交易记录");
                }
                EventLog.upEventLog("201610282", "show", "out_fail");
                break;
            case "P":
                mImageIcon.setTextColor(ContextCompat.getColor(this, R.color.color_waiting));
                mImageIcon.setText(R.string.waiting);

                if (TOPUP.equals(transactionType)){
                    mResultStatus.setText("充值进行中");
                    mResultStatusDetail.setText("充值中，稍后请在交易记录中查询");
                }else if (WITHDRAW.equals(transactionType)){
                    mResultStatus.setText("提现进行中");
                    mResultStatusDetail.setText("提现中，稍后请在交易记录中查询");
                }

                mConfirmLeft.setText("完成");
                mConfirmRight.setText("交易记录");

                break;
        }
    }

    /**
     * post 事件刷新相关页面
     *
     * @param bizStatus
     */
    public void postEventBus(String bizStatus) {
        AccountTransactionEvent payEvent = new AccountTransactionEvent();
        payEvent.setPayType(transactionClass);
        payEvent.setPayState(bizStatus);
        EventBus.getDefault().post(payEvent);
    }

    @Nullable
    @OnClick(R.id.confirm_left)
    public void confirmRepay(Button success) {
        if (WITHDRAW.equals(transactionType)){
            if ("F".equals(withdrawResponse.getBizStatus())){
                MyIntent.startActivity(this, AccountWithdrawActivity.class, "");
            }
        }
        finish();

    }

    @Nullable
    @OnClick(R.id.confirm_right)
    public void confirmSuccess(Button success) {
        switch (bizStatus) {
            case "S":
                finish();
                if (TOPUP.equals(transactionType)){
                    MyIntent.startActivity(this, AccountTopupActivity.class, "");
                    EventLog.upEventLog("201610282", "click_finish", "out_success");
                }
                break;
            case "F":
                finish();
                if (TOPUP.equals(transactionType)){
                    MyIntent.startActivity(this, AccountTopupActivity.class, "");
                }else if(WITHDRAW.equals(transactionType)){
                    MyIntent.startActivity(this, TransactionListActivity.class, "");
                }
                EventLog.upEventLog("201610282", "click_history", "out_fail");
                break;
            case "P":
                finish();
                MyIntent.startActivity(this, TransactionListActivity.class, "");
                break;
        }
    }


}
