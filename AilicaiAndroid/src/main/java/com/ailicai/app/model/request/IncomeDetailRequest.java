package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by David on 16/1/11.
 */
@RequestPath("/ailicai/incomeDetail.rest")
public class IncomeDetailRequest extends Request {
    private long userId; //用户Id,通过请求Header里的uticket获取
    private int depositType = 1; // 0：全部(网贷+活期宝) 1：活期宝 2：定期宝 3体验宝
    private int pageSize = 20; // 每次加载20条数据
    private int offSet = 0; // 数据偏移量

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getDepositType() {
        return depositType;
    }

    public void setDepositType(int depositType) {
        this.depositType = depositType;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffSet() {
        return offSet;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }
}
