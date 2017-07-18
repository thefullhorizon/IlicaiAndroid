package com.ailicai.app.model.response;

import com.ailicai.app.model.bean.Banner;
import com.huoqiu.framework.rest.Response;

import java.util.List;

/**
 * Created by nanshan on 5/22/2017.
 */

public class BannerListResponse extends Response {

    public List<Banner> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }

    private List<Banner> bannerList; //banner列表
}
