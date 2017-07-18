package com.ailicai.app.ui.view.transaction;

/**
 * Created by wulianghuan on 2016/1/4.
 */
public class TransactionTypeModel {

    private TransactionEnum transactionEnum;
    private boolean isCurrent;

    public TransactionTypeModel() {
    }

    public TransactionTypeModel(TransactionEnum transactionEnum, boolean isCurrent) {
        this.transactionEnum = transactionEnum;
        this.isCurrent = isCurrent;
    }

    public TransactionEnum getTransactionEnum() {
        return transactionEnum;
    }

    public void setTransactionEnum(TransactionEnum transactionEnum) {
        this.transactionEnum = transactionEnum;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }
}
