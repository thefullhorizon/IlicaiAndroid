package com.ailicai.app.ui.mine;

import android.content.Context;

import com.ailicai.app.common.utils.MyIntent;
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
                //MyIntent.startActivity(context, BankCardListActivity.class, "");
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


}
