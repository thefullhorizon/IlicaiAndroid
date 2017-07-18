package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.Protocol;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * 协议response
 * Created by liyanan on 16/10/8.
 */
public class ProtocolResponse extends Response {
    private List<Protocol> list;//协议list

    public List<Protocol> getList() {
        return list;
    }

    public void setList(List<Protocol> list) {
        this.list = list;
    }




}
