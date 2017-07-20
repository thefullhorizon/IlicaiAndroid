package com.ailicai.app.ui.reserve;


import com.ailicai.app.model.bean.ReserveListBean;
import com.ailicai.app.model.request.ReserveListRequest;

import java.util.List;

/**
 * Created by Owen on 2016/5/25
 */
public interface ReserveListInterface {

    int getPage();

    void setListData(List<ReserveListBean> listData);

    ReserveListRequest getReserveListRequest();
}
