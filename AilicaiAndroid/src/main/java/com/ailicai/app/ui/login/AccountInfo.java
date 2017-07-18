package com.ailicai.app.ui.login;

import android.text.TextUtils;

import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.model.response.account.AccountResponse;

/**
 * 存储开户信息.
 * Created by David on 16/1/7.
 */
public class AccountInfo {

    public static final String ACCOUNT_OPENED = "account_opened";
    public static final String ACCOUNT_HAS_PAY_PSD = "account_has_pay_psd";
    public static final String ACCOUNT_REAL_NAME_VERIFY = "account_real_name_verify";
    public static final String ACCOUNT_BIND_DEBIT_CARD = "account_bind_debit_card";
    public static final String ACCOUNT_HAS_SAFE_CARD = "account_has_safe_card";
    public static final String ACCOUNT_NAME = "account_name";
    public static final String ACCOUNT_ID_CARD_NO = "account_id_card_no";
    public static final String ACCOUNT_CARD_TAIL_NO = "account_card_tail_no";
    public static final String ACCOUNT_BANK_NAME = "account_bank_name";
    public static final String ACCOUNT_CARD_TYPE = "account_card_type";
    public static final String ACCOUNT_IS_ADULT = "account_is_adult";
    public static final String ACCOUNT_IS_SHOW_DEDUCT = "account_is_show_deduct";
    public static final String ACCOUNT_IS_USE_H5 = "account_is_use_h5";
    public static final String ACCOUNT_OPEN_RESULT_URL = "account_open_result_url";
    public static final String ACCOUNT_OPEN_RESULT_ACTIVITY_MEMO = "account_open_result_activity";
    public static final String ACCOUNT_OPEN_RESULT_SUCCESS_TITLE = "account_open_result_success_title";
    public static final String ACCOUNT_OPEN_LAST_PROCESS_HINT = "account_open_last_process_hint";

    static AccountInfo instance;

    public static synchronized AccountInfo getInstance() {
        if (instance == null) instance = new AccountInfo();
        return instance;
    }

    public void saveAccountInfo(AccountResponse account) {
        MyPreference.getInstance().write(ACCOUNT_OPENED, account.getIsOpenAccount());
        MyPreference.getInstance().write(ACCOUNT_HAS_PAY_PSD, account.getIsSetPayPwd());
        MyPreference.getInstance().write(ACCOUNT_REAL_NAME_VERIFY, account.getIsRealNameVerify());
        MyPreference.getInstance().write(ACCOUNT_BIND_DEBIT_CARD, account.getIsBinDebitCard());
        MyPreference.getInstance().write(ACCOUNT_HAS_SAFE_CARD, account.getHasSafeCard());
        MyPreference.getInstance().write(ACCOUNT_NAME, account.getName());
        MyPreference.getInstance().write(ACCOUNT_ID_CARD_NO, account.getIdCardNo());
        MyPreference.getInstance().write(ACCOUNT_CARD_TAIL_NO, account.getBankcardTailNo());
        MyPreference.getInstance().write(ACCOUNT_BANK_NAME, account.getBankName());
        MyPreference.getInstance().write(ACCOUNT_CARD_TYPE, account.getCardType());
        MyPreference.getInstance().write(ACCOUNT_IS_ADULT, account.getIsAdult());
        MyPreference.getInstance().write(ACCOUNT_IS_SHOW_DEDUCT, account.getShowDeductMoney());
        MyPreference.getInstance().write(ACCOUNT_IS_USE_H5,account.getUseH5());
        MyPreference.getInstance().write(ACCOUNT_OPEN_RESULT_URL,account.getOpenResultUrl());
        MyPreference.getInstance().write(ACCOUNT_OPEN_RESULT_ACTIVITY_MEMO,account.getActivityMemo());
        MyPreference.getInstance().write(ACCOUNT_OPEN_RESULT_SUCCESS_TITLE,account.getOpenAccountNotice());
        MyPreference.getInstance().write(ACCOUNT_OPEN_LAST_PROCESS_HINT,account.getOpenAccountTitle());
    }

    public AccountResponse getAccountResponse() {
        AccountResponse accountResponse = new AccountResponse();
        MyPreference preference = MyPreference.getInstance();
        accountResponse.setIsOpenAccount(preference.read(ACCOUNT_OPENED, -1));
        accountResponse.setIsSetPayPwd(preference.read(ACCOUNT_HAS_PAY_PSD, -1));
        accountResponse.setIsRealNameVerify(preference.read(ACCOUNT_REAL_NAME_VERIFY, -1));
        accountResponse.setIsBinDebitCard(preference.read(ACCOUNT_BIND_DEBIT_CARD, -1));
        accountResponse.setIsBinDebitCard(preference.read(ACCOUNT_HAS_SAFE_CARD, -1));
        accountResponse.setName(preference.read(ACCOUNT_NAME, ""));
        accountResponse.setIdCardNo(preference.read(ACCOUNT_ID_CARD_NO, ""));
        accountResponse.setBankcardTailNo(preference.read(ACCOUNT_CARD_TAIL_NO, ""));
        accountResponse.setBankName(preference.read(ACCOUNT_BANK_NAME, ""));
        accountResponse.setCardType(preference.read(ACCOUNT_CARD_TYPE, -1));
        accountResponse.setIsAdult(preference.read(ACCOUNT_IS_ADULT, -1));
        accountResponse.setShowDeductMoney(preference.read(ACCOUNT_IS_SHOW_DEDUCT,-1));
        accountResponse.setUseH5(preference.read(ACCOUNT_IS_USE_H5,-1));
        accountResponse.setOpenResultUrl(preference.read(ACCOUNT_OPEN_RESULT_URL,""));
        accountResponse.setActivityMemo(preference.read(ACCOUNT_OPEN_RESULT_ACTIVITY_MEMO,""));
        accountResponse.setOpenAccountNotice(preference.read(ACCOUNT_OPEN_RESULT_SUCCESS_TITLE,""));
        accountResponse.setOpenAccountTitle(preference.read(ACCOUNT_OPEN_LAST_PROCESS_HINT,""));
        return accountResponse;
    }

    public void clearAccountInfo() {
        MyPreference.getInstance().write(ACCOUNT_OPENED, -1);
        MyPreference.getInstance().write(ACCOUNT_HAS_PAY_PSD, -1);
        MyPreference.getInstance().write(ACCOUNT_REAL_NAME_VERIFY, -1);
        MyPreference.getInstance().write(ACCOUNT_BIND_DEBIT_CARD, -1);
        MyPreference.getInstance().write(ACCOUNT_HAS_SAFE_CARD, -1);
        MyPreference.getInstance().write(ACCOUNT_NAME, "");
        MyPreference.getInstance().write(ACCOUNT_ID_CARD_NO, "");
        MyPreference.getInstance().write(ACCOUNT_CARD_TAIL_NO, "");
        MyPreference.getInstance().write(ACCOUNT_BANK_NAME, "");
        MyPreference.getInstance().write(ACCOUNT_CARD_TYPE, -1);
        MyPreference.getInstance().write(ACCOUNT_IS_ADULT, -1);
        MyPreference.getInstance().write(ACCOUNT_IS_SHOW_DEDUCT, -1);
        MyPreference.getInstance().write(ACCOUNT_IS_USE_H5,-1);
        MyPreference.getInstance().write(ACCOUNT_OPEN_RESULT_URL,"");
        MyPreference.getInstance().write(ACCOUNT_OPEN_RESULT_ACTIVITY_MEMO,"");
        MyPreference.getInstance().write(ACCOUNT_OPEN_RESULT_SUCCESS_TITLE,"");
        MyPreference.getInstance().write(ACCOUNT_OPEN_LAST_PROCESS_HINT,"");
    }

    public static boolean hasCacheAccountInfo() {
        return MyPreference.getInstance().read(ACCOUNT_OPENED, -1) >= 0;
    }

    public static boolean isOpenAccount() {
        return MyPreference.getInstance().read(ACCOUNT_OPENED, -1) == 1;
    }
    public static int getIsOpenAccount() {
        return MyPreference.getInstance().read(ACCOUNT_OPENED, -1);
    }

    public static void setIsSetPayPwd(boolean isSet) {
        MyPreference.getInstance().write(ACCOUNT_HAS_PAY_PSD, isSet ? 1 : 0);
    }

    public static void setHasSafeCard(boolean hasSafeCard) {
        // 是否已绑定安全卡  0:否，1:是
        if (hasSafeCard) {
            MyPreference.getInstance().write(ACCOUNT_HAS_SAFE_CARD, 1);
        } else{
            MyPreference.getInstance().write(ACCOUNT_HAS_SAFE_CARD, 0);
        }
    }

    public static boolean isSetPayPwd() {
        return MyPreference.getInstance().read(ACCOUNT_HAS_PAY_PSD, -1) == 1;
    }

    public static boolean isRealNameVerify() {
        return MyPreference.getInstance().read(ACCOUNT_REAL_NAME_VERIFY, -1) == 1;
    }

    public static void setRealNameVerify(String name, String idCardNo) {
        // 是否实名  0:否，1:是
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(idCardNo)) {
            MyPreference.getInstance().write(ACCOUNT_REAL_NAME_VERIFY, 1);
            MyPreference.getInstance().write(ACCOUNT_NAME, name);
            MyPreference.getInstance().write(ACCOUNT_ID_CARD_NO, idCardNo);
        } else {
            MyPreference.getInstance().write(ACCOUNT_REAL_NAME_VERIFY, 0);
            MyPreference.getInstance().write(ACCOUNT_NAME, "");
            MyPreference.getInstance().write(ACCOUNT_ID_CARD_NO, "");
        }
    }

    public static boolean isBinDebitCard() {
        return MyPreference.getInstance().read(ACCOUNT_BIND_DEBIT_CARD, -1) == 1;
    }

    public static boolean hasSafeCard() {
        return MyPreference.getInstance().read(ACCOUNT_HAS_SAFE_CARD, -1) == 1;
    }

    public static boolean isAdult() {
        return MyPreference.getInstance().read(ACCOUNT_IS_ADULT, -1) == 1;
    }

    public static void setAdult(boolean adult) {
        // 判断是否成年 0  否  1是
        if (adult) {
            MyPreference.getInstance().write(ACCOUNT_IS_ADULT, 1);
        } else{
            MyPreference.getInstance().write(ACCOUNT_IS_ADULT, 0);
        }
    }

    public static boolean isShowDeductMoney() {
       return MyPreference.getInstance().read(ACCOUNT_IS_SHOW_DEDUCT,-1) == 1;
    }

    public static boolean isUseH5() {
        return MyPreference.getInstance().read(ACCOUNT_IS_USE_H5,-1) == 1;
    }

    public static String getOpenResultUrl() {
        return  MyPreference.getInstance().read(ACCOUNT_OPEN_RESULT_URL, "");
    }

    public static String getOpenAccountActivityMemo() {
        return  MyPreference.getInstance().read(ACCOUNT_OPEN_RESULT_ACTIVITY_MEMO, "");
    }

    public static String getOpenAccountSuccessTitle() {
        return  MyPreference.getInstance().read(ACCOUNT_OPEN_RESULT_SUCCESS_TITLE, "开户成功");
    }

    public static String getOpenAccountLastProcessName() {
        return  MyPreference.getInstance().read(ACCOUNT_OPEN_LAST_PROCESS_HINT, "开户成功");
    }
}