package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.Product;
import com.huoqiu.framework.rest.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 16/3/15.
 */
public class HasBuyProductListResponse extends Response {
    private int errorCode = 0; //返回代码 0-正常 其他参考对应的errorCode定义
    private String message = "";//返回消息
    private int pageSize; //每页数据量
    private int total;// 数据总笔数
    private int reserveTotal;
    private List<Product> productList;
    private List<Product> reserveList; //预约申请列表

    private List<Product> tiyanbaoList = new ArrayList<>();//体验宝列表
    private int tiyanbaoTotal;//体验宝总数量, 到期   用于显示到期体验宝共XX笔

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

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

    public int getReserveTotal() {
        return reserveTotal;
    }

    public void setReserveTotal(int reserveTotal) {
        this.reserveTotal = reserveTotal;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getReserveList() {
        return reserveList;
    }

    public void setReserveList(List<Product> reserveList) {
        this.reserveList = reserveList;
    }

    public List<Product> getTiyanbaoList() {
        return tiyanbaoList;
    }

    public void setTiyanbaoList(List<Product> tiyanbaoList) {
        this.tiyanbaoList = tiyanbaoList;
    }

    public int getTiyanbaoTotal() {
        return tiyanbaoTotal;
    }

    public void setTiyanbaoTotal(int tiyanbaoTotal) {
        this.tiyanbaoTotal = tiyanbaoTotal;
    }
}
