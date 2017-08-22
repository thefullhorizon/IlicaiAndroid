package com.ailicai.app.ui.buy;

import android.support.v4.app.FragmentActivity;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.ui.dialog.BaseBuyFinanceDialog;
import com.ailicai.app.ui.dialog.BuyDialogShowInfo;

/**
 * Created by jeme on 2017/8/21.
 */

public class AutomaticTenderPay extends BaseBuyFinancePay {
    private AutomaticTenderPresenter mPresenter;
    private AutomaticTenderInfo mInfo;

    public static class AutomaticTenderInfo {
        public boolean forOpen;
        public int strategyType;
        public Double reserveMoney;

    }
    public AutomaticTenderPay(FragmentActivity mActivity,AutomaticTenderInfo info, IwPwdPayResultListener iwPayResultListener) {
        super(mActivity, iwPayResultListener);
        this.mInfo = info;
        setDialogDismissListener(new BaseBuyFinanceDialog.DialogDismissListener() {
            @Override
            public void onDismiss() {
                if(mPresenter != null && mInfo != null){
                    mPresenter.pwdDialogClose(mInfo.forOpen);
                }
            }
        });
    }
    public void setAutomaticPresenter(AutomaticTenderPresenter presenter){
        mPresenter = presenter;
    }

    @Override
    void toPayForInfo(String jsonObject) {
        SystemUtil.HideSoftInput(mActivity);
        if(mPresenter != null && mInfo != null){
            mPresenter.submit(mInfo.forOpen,mInfo.strategyType,mInfo.reserveMoney,jsonObject);
        }
    }

    @Override
    BuyDialogShowInfo getDispalyInfo() {
        return new BuyDialogShowInfo.BankPayBuild()
                .setAmount(1)
                //.setMoneyOutStr("购买房产宝" + regularPayInfo.getProductId() + "号")
                .setMoneyOutStr("")
                .setPayTypFrom("使用账户余额支付")
                .setPayTypFromIco(mActivity.getString(R.string.current_pay))
                .create();
    }

    @Override
    int getBizType() {
        return -1;
    }

    @Override
    double getAmount() {
        return 0;
    }
}
