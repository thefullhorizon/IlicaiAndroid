package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.OpenScreenPopModel;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * 开屏弹窗
 * Created by jeme on 2017/5/24.
 */

public class OpenScreenPopResponse extends Response {
    private List<OpenScreenPopModel> openScreenPopList;

    public List<OpenScreenPopModel> getOpenScreenPopList() {
        return openScreenPopList;
    }

    public void setOpenScreenPopList(List<OpenScreenPopModel> openScreenPopList) {
        this.openScreenPopList = openScreenPopList;
    }
}
