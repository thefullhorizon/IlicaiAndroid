package com.ailicai.app.model.request;


import com.ailicai.app.common.reqaction.RequestPath;

/**
 * Created by Tangxiaolong on 2016/2/17.
 */
@RequestPath("/ailicai/bankcardList.rest")
public class BankcardListRequest extends Request {
    private int type; // 类型 0：我的银行卡列表  1：账单使用银行卡支付时的已绑定的银行卡列表
    private long billId; // 账单Id
    private int billType; // 业务类型1：出租佣金，2：房管房，3：二手房出售，11出租租金

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getBillId() {
        return billId;
    }

    public void setBillId(long billId) {
        this.billId = billId;
    }

    public int getBillType() {
        return billType;
    }

    public void setBillType(int billType) {
        this.billType = billType;
    }
}