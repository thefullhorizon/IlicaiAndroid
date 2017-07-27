package com.ailicai.app.common.utils;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.ailicai.app.model.bean.Banner;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.view.CapitalActivity;
import com.ailicai.app.ui.view.MyWalletActivity;

import java.util.Map;

/**
 * 处理点击跳转分发
 * Created by wulianghuan on 2016/7/5.
 */
public class MainIntentUtil {

    /**
     * 点击焦点图作跳转
     * @param mActivity 上下文
     * @param banner 对象实体
     * */
    public static void processBannerClickOnPayResult(FragmentActivity mActivity, Banner banner) {
        int bannerType = banner.getUrlType();
        if (0 == bannerType) {
            // 跳转webview
            Map<String, String> dataMap = ObjectUtil.newHashMap();
            dataMap.put(WebViewActivity.URL, banner.getDetailUrl());
            dataMap.put(WebViewActivity.USEWEBTITLE, "true");
            MyIntent.startActivity(mActivity, WebViewActivity.class, dataMap);
        } else {
            //  11-钱包页 12-资产页
            switch (bannerType) {

                case 11:// 钱包页
                    Intent intent = new Intent(mActivity, MyWalletActivity.class);
                    mActivity.startActivity(intent);
                    break;
                case 12://资产页
                    MyIntent.startActivity(mActivity, CapitalActivity.class, null);
                    break;

                default:
                    ToastUtil.showInCenter("未知类型");
                    break;
            }
        }
    }


}
