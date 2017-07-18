package com.ailicai.app.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.eventbus.FinancePayEvent;
import com.ailicai.app.model.response.SaleHuoqibaoResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.view.transaction.TransactionListActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 钱包转出结果
 * Created by Gerry on 2015/12/29.
 */
public class CurrentRollOutResultActivity extends BaseBindActivity {

    @Bind(R.id.roll_out_tips_mesg)
    TextView mRollOutTipsMesg;
    @Bind(R.id.image_icon)
    TextView mImageIcon;
    @Bind(R.id.mesg_result_text)
    TextView mMesgResultText;
    @Bind(R.id.confirm_repay)
    Button mConfirmRepay;
    @Bind(R.id.confirm_success)
    Button mConfirmSuccess;
    @Bind(R.id.roll_out_tips)
    TextView rollOutTips;

    private SaleHuoqibaoResponse response;

    public static String KEY = "response";

    @Override
    public int getLayout() {
        return R.layout.activity_roll_out_result;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        response = (SaleHuoqibaoResponse) getIntent().getExtras().getSerializable(KEY);
        //处理结果，S成功;P处理中;F失败
        String bizStatus = response.getBizStatus();
        mConfirmRepay.setVisibility(View.VISIBLE);
        mConfirmSuccess.setVisibility(View.VISIBLE);
        //post 事件刷新相关页面
        postEventBus(bizStatus);
        switch (bizStatus) {
            case "S":
                mImageIcon.setTextColor(ContextCompat.getColor(this, R.color.color_succeed));
                mImageIcon.setText(R.string.succeed);
                mConfirmRepay.setVisibility(View.GONE);
                mConfirmSuccess.setText("完成");
                mMesgResultText.setText("转出成功");
                mRollOutTipsMesg.setText(Html.fromHtml(getResources().getString(R.string.roll_out_tips_text, response.getAmount(), response.getGiveDate())));
                if (!TextUtils.isEmpty(response.getTips())) {
                    rollOutTips.setVisibility(View.VISIBLE);
                    rollOutTips.setText(response.getTips());
                } else {
                    rollOutTips.setVisibility(View.GONE);
                }
                EventLog.upEventLog("201610282", "show", "out_success");
                break;
            case "F":
                mImageIcon.setTextColor(ContextCompat.getColor(this, R.color.color_failed));
                mImageIcon.setText(R.string.failured);
                mConfirmRepay.setText("继续转出");
                mConfirmSuccess.setText("交易记录");
                mMesgResultText.setText("转出失败");
                //mRollOutTipsMesg.setText("银行卡信息有误，请重试");
                mRollOutTipsMesg.setText(response.getMessage());
                EventLog.upEventLog("201610282", "show", "out_fail");
                break;
            case "P":
                mImageIcon.setTextColor(ContextCompat.getColor(this, R.color.color_waiting));
                mImageIcon.setText(R.string.failured);
                mConfirmRepay.setText("完成");
                mConfirmSuccess.setText("交易记录");
                mMesgResultText.setText("转出进行中");
                mRollOutTipsMesg.setText("支付中，稍后请在交易记录中查询");
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
        payEvent.setPayType(FinancePayEvent.PAY_OUT);
        payEvent.setPayState(bizStatus);
        EventBus.getDefault().post(payEvent);
    }

    @Nullable
    @OnClick(R.id.confirm_repay)
    public void confirmRepay(Button success) {
        String bizStatus = response.getBizStatus();
        switch (bizStatus) {
            case "S":
                break;
            case "F":
                finish();
                MyIntent.startActivity(this, CurrentRollOutActivity.class, "");
                EventLog.upEventLog("201610282", "click_goon", "out_fail");
                break;
            case "P":
                finish();
                break;
        }
    }

    @Nullable
    @OnClick(R.id.confirm_success)
    public void confirmSuccess(Button success) {
        String bizStatus = response.getBizStatus();
        switch (bizStatus) {
            case "S":
                finish();
                EventLog.upEventLog("201610282", "click_finish", "out_success");
                break;
            case "F":
                finish();
                MyIntent.startActivity(this, TransactionListActivity.class, "");
                EventLog.upEventLog("201610282", "click_history", "out_fail");
                break;
            case "P":
                finish();
                MyIntent.startActivity(this, TransactionListActivity.class, "");
                break;
        }
    }

}
