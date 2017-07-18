package com.ailicai.app.ui.dialog;

/**
 * Created by Jer on 2016/1/7.
 */
public class BuyDialogShowInfo {

    private double amount = 0;
    private String moneyOutStr = "";
    private String payTypFrom = "";
    private String payTypFromIco = "";

    public String getPayTypFromIco() {
        return payTypFromIco;
    }


    public double getAmount() {
        return amount;
    }

    public String getMoneyOutStr() {
        return moneyOutStr;
    }

    public String getPayTypFrom() {
        return payTypFrom;
    }

    public static class BankPayBuild {

        double amount = 0;
        String moneyOutStr = "";
        String payTypFrom = "";
        String payTypFromIco = "";

        public BankPayBuild setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public BankPayBuild setMoneyOutStr(String moneyOutStr) {
            this.moneyOutStr = moneyOutStr;
            return this;
        }

        public BankPayBuild setPayTypFrom(String payTypFrom) {
            this.payTypFrom = payTypFrom;
            return this;
        }

        public BankPayBuild setPayTypFromIco(String payTypFromIco) {
            this.payTypFromIco = payTypFromIco;
            return this;
        }


        public void applyInfo(BuyDialogShowInfo buyDialogShowInfo) {
            buyDialogShowInfo.amount = this.amount;
            buyDialogShowInfo.moneyOutStr = this.moneyOutStr;
            buyDialogShowInfo.payTypFrom = this.payTypFrom;
            buyDialogShowInfo.payTypFromIco = this.payTypFromIco;
        }

        public BuyDialogShowInfo create() {
            BuyDialogShowInfo buyDialogShowInfo = new BuyDialogShowInfo();
            applyInfo(buyDialogShowInfo);
            return buyDialogShowInfo;
        }
    }
}
