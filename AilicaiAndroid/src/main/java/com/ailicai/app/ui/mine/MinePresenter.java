package com.ailicai.app.ui.mine;

import android.content.Context;
import android.content.Intent;

import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.ui.bankcard.BankCardListActivity;
import com.ailicai.app.ui.buy.AutomaticTenderActivity;
import com.ailicai.app.ui.buy.NoSetSafeCardHint;
import com.ailicai.app.ui.buy.ProcessActivity;
import com.ailicai.app.ui.view.CapitalActivity;
import com.ailicai.app.ui.view.MyWalletActivity;
import com.ailicai.app.ui.view.reserveredrecord.ReserveRecordListActivity;
import com.ailicai.app.ui.view.transaction.TransactionListActivity;
import com.ailicai.app.ui.voucher.CouponWebViewActivity;
import com.huoqiu.framework.util.CheckDoubleClick;

/**
 * Created by Administrator on 2015/6/18.
 */
public class MinePresenter {

    /**
     * 银行卡
     *
     * @param context
     */
    public void gotoMyBankCrad(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
                MyIntent.startActivity(context, BankCardListActivity.class, "");
            }
        }
    }

    /**
     * 交易记录
     *
     * @param context
     */
    public void gotoTransactionList(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
                MyIntent.startActivity(context, TransactionListActivity.class, "");
            }
        }
    }

    /**
     * 预约记录
     *
     * @param context
     */
    public void gotoReserveRecordList(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
                MyIntent.startActivity(context, ReserveRecordListActivity.class, "");
            }
        }
    }

    /**
     * 卡券
     */
    public void gotoCardCoupon(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
                MyIntent.startActivity(context, CouponWebViewActivity.class, null);
            }
        }
    }

    /**
     * 邀请奖励
     */
    public void goRewards(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
                MyIntent.startActivity(context, InviteRewardsActivity.class, null);
            }
        }
    }


    /**
     * 网贷类
     */
    public void goWangDai(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
                if (!NoSetSafeCardHint.isOpenAccount()) {
                    Intent intent = new Intent(context, ProcessActivity.class);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, CapitalActivity.class);
                    intent.putExtra(CapitalActivity.TAB, CapitalActivity.HOLD);
                    context.startActivity(intent);
                }
            }
        }
    }

    /**
     * 活期宝
     */
    public void goHQB(Context context) {
        if (!CheckDoubleClick.isFastDoubleClick()) {
            if (null != context) {
                /*if (!NoSetSafeCardHint.isOpenAccount()) {
                    Intent intent = new Intent(context, ProcessActivity.class);
                    context.startActivity(intent);
                } else {*/
                MyIntent.startActivity(context, MyWalletActivity.class, null);
                /*}*/
            }
        }
    }


}
