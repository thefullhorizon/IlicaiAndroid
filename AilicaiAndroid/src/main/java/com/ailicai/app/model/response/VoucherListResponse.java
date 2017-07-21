package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.Voucher;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * 加息券列表Response
 * Created by liyanan on 16/3/4.
 */
public class VoucherListResponse extends Response {
    private int pageSize; //每页数据量
    private int total;// 数据总笔数
    private String instructionUrl = ""; //使用说明url
    private List<Voucher> voucherList;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getInstructionUrl() {
        return instructionUrl;
    }

    public void setInstructionUrl(String instructionUrl) {
        this.instructionUrl = instructionUrl;
    }

    public List<Voucher> getVoucherList() {
        return voucherList;
    }

    public void setVoucherList(List<Voucher> voucherList) {
        this.voucherList = voucherList;
    }
}
