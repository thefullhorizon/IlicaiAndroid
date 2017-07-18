package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.AilicaiNotice;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by nanshan on 6/16/2017.
 */

public class AilicaiNoticeListOnRollReponse extends Response {

    private List<AilicaiNotice> bankNotices;

    public List<AilicaiNotice> getBankNotices() {
        return bankNotices;
    }

    public void setBankNotices(List<AilicaiNotice> bankNotices) {
        this.bankNotices = bankNotices;
    }
}
