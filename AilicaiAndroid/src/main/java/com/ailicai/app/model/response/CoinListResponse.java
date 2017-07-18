package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.Coin;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by nanshan on 2017/5/8.
 */

public class CoinListResponse extends Response {
    private int errorCode = 0; //返回代码 0-正常 其他参考对应的errorCode定义
    private String message = "";//返回消息
    private int total;// 总数
    private int offset;// 当前页

    private List<Coin> productList;//产品列表

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<Coin> getProductList() {
        return productList;
    }

    public void setProductList(List<Coin> productList) {
        this.productList = productList;
    }
}
