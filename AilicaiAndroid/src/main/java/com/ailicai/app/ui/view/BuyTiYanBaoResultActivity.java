package com.ailicai.app.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.utils.SpannableUtil;
import com.ailicai.app.eventbus.RegularPayEvent;
import com.ailicai.app.eventbus.RegularPayH5ActivityFinishEvent;
import com.ailicai.app.model.response.BuyTiyanbaoResponse;
import com.ailicai.app.ui.asset.CapitalListProductDetailActivity;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.buy.ProcessActivity;
import com.ailicai.app.ui.login.AccountInfo;
import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 体验宝购买结果页
 * Created by liyanan on 16/8/15.
 */
public class BuyTiYanBaoResultActivity extends BaseBindActivity {
    private final static int REQUEST_CODE_OPEN_ACCOUNT = 10001;

    public static String KEY = "response";
    private BuyTiyanbaoResponse response;
    @Bind(R.id.tv_profit_dao_time)
    TextView tvProfitDaoTime;
    @Bind(R.id.btn_complete)
    Button btnComplete;
    @Bind(R.id.btn_kai_hu)
    Button btnKaiHu;
    @Bind(R.id.tv_hint_text)
    TextView tvHintText;
    @Bind(R.id.ll_failure)
    LinearLayout llFailure;
    @Bind(R.id.ll_success)
    LinearLayout llSuccess;
    @Bind(R.id.tv_error_message)
    TextView tvErrorMessage;

    @Override
    public int getLayout() {
        return R.layout.activity_buy_tiyan_bao_success;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        response = (BuyTiyanbaoResponse) getIntent().getExtras().getSerializable(KEY);
        initData(true);
        EventBus.getDefault().post(new RegularPayEvent());
    }

    /**
     * @param upload 是否上报卖点
     */
    private void initData(boolean upload) {
        if (response.getBizCode() == -1) {
            //购买失败
            llFailure.setVisibility(View.VISIBLE);
            llSuccess.setVisibility(View.GONE);
            tvErrorMessage.setText(response.getMessage());
            btnKaiHu.setVisibility(View.GONE);
            btnComplete.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnComplete.setTextSize(16);
            btnComplete.setTypeface(Typeface.DEFAULT_BOLD);
            btnComplete.setBackgroundResource(R.drawable.round_corner_btn_bg);
        } else {
            //购买成功
            llFailure.setVisibility(View.GONE);
            llSuccess.setVisibility(View.VISIBLE);
            SpannableUtil spanUtil = new SpannableUtil(this);
            SpannableStringBuilder builder1 = spanUtil.getSpannableString("预计 ", response.getBackDateStr(), "回款至账户余额",
                    R.style.text_13_757575, R.style.text_13_e84a01, R.style.text_13_757575);
            tvProfitDaoTime.setText(builder1);
            if (response.getIsOpen() == 1) {
                //已开户
                btnComplete.setText("完成");
                btnKaiHu.setText("查看详情");
                tvHintText.setVisibility(View.GONE);
            } else {
                //未开户
                if (response.getHasValidLimit() > 0) {
                    tvHintText.setVisibility(View.VISIBLE);
                    tvHintText.setText("提示: 收益回款后请于" + response.getDueDateStr() + "前提现转出，逾期将清零。");
                }
                btnComplete.setText("查看详情");
                btnKaiHu.setText("马上开户");
                if (upload) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("name", "mskh");
                    params.put("action", "show");
                    EventLog.upEventLog("806", JSON.toJSONString(params));
                }
            }
        }
    }

    /**
     * 完成
     */
    @OnClick(R.id.btn_complete)
    void clickComplete() {
        if (response.getBizCode() == -1) {
            //购买失败--完成
            HashMap<String, String> params = new HashMap<>();
            params.put("name", "wc");
            params.put("action", "click");
            EventLog.upEventLog("806", JSON.toJSONString(params));
            finish();
        } else if (response.getIsOpen() == 1) {
            //购买成功已开户--完成
            HashMap<String, String> params = new HashMap<>();
            params.put("name", "wc");
            params.put("action", "click");
            EventLog.upEventLog("806", JSON.toJSONString(params));
            finish();
        } else {
            //购买成功未开户--查看详情
            Intent intent = new Intent(this, CapitalListProductDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong(CapitalListProductDetailActivity.TIYANBAO_ID, response.getCouponId());
            bundle.putBoolean(CapitalListProductDetailActivity.IS_TIYANBAO, true);
            bundle.putBoolean(CapitalListProductDetailActivity.START_CAPIYAL, true);
            intent.putExtras(bundle);
            startActivity(intent);
            EventBus.getDefault().post(new RegularPayH5ActivityFinishEvent());
            finish();
        }

    }

    /**
     * 开户
     */
    @OnClick(R.id.btn_kai_hu)
    void clickKaiHu() {
        if (response.getIsOpen() == 1) {
            //已开户--查看详情
            Intent intent = new Intent(this, CapitalListProductDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong(CapitalListProductDetailActivity.TIYANBAO_ID, response.getCouponId());
            bundle.putBoolean(CapitalListProductDetailActivity.IS_TIYANBAO, true);
            bundle.putBoolean(CapitalListProductDetailActivity.START_CAPIYAL, true);
            intent.putExtras(bundle);
            startActivity(intent);
            EventBus.getDefault().post(new RegularPayH5ActivityFinishEvent());
            finish();
        } else {
            //未开户--马上开户
            Intent intent = new Intent(this, ProcessActivity.class);
            startActivityForResult(intent, REQUEST_CODE_OPEN_ACCOUNT);
            //TODO nanshan 开户相关
//            OpenAccountFeature.isOpeningAccount = true;
            HashMap<String, String> params = new HashMap<>();
            params.put("name", "mskh");
            params.put("action", "click");
            EventLog.upEventLog("806", JSON.toJSONString(params));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        response.setIsOpen(AccountInfo.getIsOpenAccount());
        initData(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_OPEN_ACCOUNT:
                    //开户成功
                    //TODO nanshan 开户相关
//                    OpenAccountFeature.isOpeningAccount = false;
                    response.setIsOpen(AccountInfo.getIsOpenAccount());
                    initData(false);
                    break;
            }
        }
    }
}
