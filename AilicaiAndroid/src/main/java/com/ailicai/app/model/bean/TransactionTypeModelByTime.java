package com.ailicai.app.model.bean;

import com.ailicai.app.ui.view.transaction.TransactionEnumByTime;

/**
 * Created by wulianghuan on 2016/1/4.
 */
public class TransactionTypeModelByTime extends CommonTransactionTypeModel{

    private TransactionEnumByTime transactionEnumByTime;
    private boolean isCurrent;

    public TransactionTypeModelByTime() {
    }

    public TransactionTypeModelByTime(TransactionEnumByTime transactionEnumByTime, boolean isCurrent) {
        this.transactionEnumByTime = transactionEnumByTime;
        this.isCurrent = isCurrent;
    }

    public TransactionEnumByTime getTransactionEnumByTime() {
        return transactionEnumByTime;
    }

    public void setTransactionEnumByTime(TransactionEnumByTime transactionEnumByTime) {
        this.transactionEnumByTime = transactionEnumByTime;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }
}
