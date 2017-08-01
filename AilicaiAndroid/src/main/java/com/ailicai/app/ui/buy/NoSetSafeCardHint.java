package com.ailicai.app.ui.buy;

import android.content.DialogInterface;

import com.ailicai.app.MyApplication;
import com.ailicai.app.model.response.UserLoginResponse;
import com.ailicai.app.ui.account.OpenAccountWebViewActivity;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.login.AccountInfo;
import com.ailicai.app.ui.login.UserManager;
import com.ailicai.app.widget.DialogBuilder;


/**
 * Created by zhouxuan on 16/2/22.
 */
public class NoSetSafeCardHint {

    public static boolean isShowHintDialog(BaseBindActivity activity) {

        boolean isOpenCount = AccountInfo.isOpenAccount();

        if (!hasSafeCard() && isOpenCount) {
            showDeductionsCheckCardDialog(activity);
            return true;
        }
        return false;
    }

    public static boolean isShowHintDialogWithLoginInfo(BaseBindActivity activity, UserLoginResponse jsonObject) {

        boolean isOpenCount = AccountInfo.isOpenAccount();

        if (!(hasSafeCard()|| (jsonObject.getHasSafeCard() == 1)) && (isOpenCount || (jsonObject.getIsOpenAccount() == 1))) {
            showDeductionsCheckCardDialog(activity);
            return true;
        }
        return false;
    }

    /**
     * 是否已设置安全卡
     *
     * @return
     */
    public static boolean isHasSafeCard(BaseBindActivity activity) {
        if (!hasSafeCard()) {
            showDeductionsCheckCardDialog(activity);
            return false;
        }
        return true;
    }

    /**
     * 是否已开户
     *
     * @return
     */
    public static boolean isOpenAccount() {
        return AccountInfo.isOpenAccount();
    }

    private static boolean hasSafeCard() {

        // 因为userInfo这边不一定能请求成功
        boolean isUserInfoHasSafeCard = UserManager.getInstance(MyApplication.getInstance()).isHasSafeCard();
        boolean isAccountInfoHasSafeCard = AccountInfo.hasSafeCard();

        return isAccountInfoHasSafeCard || isUserInfoHasSafeCard;
    }

    /**
     * 转入转出适合未设置安全卡，提示
     */
    private static void showDeductionsCheckCardDialog(final BaseBindActivity activity) {
        DialogBuilder.showSimpleDialog(activity, "未设置安全卡", null, "取消", null, "设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showPayPwdCheckDialog(activity);
            }
        });
    }

    // 显示支付密码验证输入
    private static void showPayPwdCheckDialog(final BaseBindActivity activity) {
        SetSafeBankPwdCheckDialog checkDialog = new SetSafeBankPwdCheckDialog(activity, new IwPwdPayResultListener() {
            @Override
            public void onPayPwdTryAgain() {
                showPayPwdCheckDialog(activity);
            }

            @Override
            public void onPayComplete(Object object) {
                OpenAccountWebViewActivity.goToOpenAccount(activity);
            }

            @Override
            public void onPayStateDelay(String msgInfo, Object object) {

            }

            @Override
            public void onPayFailInfo(String msgInfo, String errorCode, Object object) {

            }
        });
        checkDialog.show();
    }
}
