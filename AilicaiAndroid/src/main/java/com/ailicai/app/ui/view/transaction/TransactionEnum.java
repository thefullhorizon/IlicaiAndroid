package com.ailicai.app.ui.view.transaction;

/**
 * Created by wulianghuan on 2016/1/7.
 */

public enum TransactionEnum {
    // 交易类型 0：全部，1：转入，2：转出，3：购买，4：回款
    ALL(0, "全部", "全部交易"),
    TRANSFER_IN(1, "转入", "转入"),
    TTRANSFER_OUT(2, "转出", "转出"),
    BUY(3, "购买", "购买"),
    BACK_FUND(4, "回款", "回款"),
    PAY(5, "支付", "订单支付"),
    TRANSFER(6, "转让", "转让赎回"),
    BACK_ALL(7, "退款", "退款");

    private int code;
    private String item;
    private String title;

    TransactionEnum(int code, String item, String title) {
        this.code = code;
        this.item = item;
        this.title = title;
    }

    public int getCode() {
        return this.code;
    }

    public String getItem() {
        return item;
    }

    public String getTitle() {
        return title;
    }

    public static TransactionEnum find(int code) {
        if (ALL.getCode() == code) return ALL;
        else if (TRANSFER_IN.getCode() == code) return TRANSFER_IN;
        else if (TTRANSFER_OUT.getCode() == code) return TTRANSFER_OUT;
        else if (BUY.getCode() == code) return BUY;
        else if (BACK_FUND.getCode() == code) return BACK_FUND;
        return null;
    }

}