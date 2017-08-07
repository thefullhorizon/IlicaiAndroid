package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.Voucher;
import com.huoqiu.framework.rest.Response;

/**
 * Created by nanshan on 8/7/2017.
 */

public class GetAppropriateCouponResponse extends Response {

    private Voucher voucher;
    private int availableVoucherNumber;

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public int getAvailableVoucherNumber() {
        return availableVoucherNumber;
    }

    public void setAvailableVoucherNumber(int availableVoucherNumber) {
        this.availableVoucherNumber = availableVoucherNumber;
    }
}
