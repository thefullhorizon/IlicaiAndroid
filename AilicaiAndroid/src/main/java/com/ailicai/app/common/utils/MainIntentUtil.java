package com.ailicai.app.common.utils;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.ailicai.app.model.bean.Banner;
import com.ailicai.app.model.bean.OpenScreenPopModel;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.index.IndexActivity;
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

    /**
     * 点击工具栏或者开屏弹窗
     * @param activity 上下文
     * */
    public static void processFunctionClick(FragmentActivity activity, @NonNull OpenScreenPopModel popModel) {
        // 跳转内部：0-网页  4-吉爱财 31-无跳转
        //详见OpenScreenPopModel类
        switch (popModel.getAdPopupUrlType()) {
            case OpenScreenPopModel.POP_TYPE_H5:
                // 跳转webview
                Map<String, String> dataMap = ObjectUtil.newHashMap();
                dataMap.put(WebViewActivity.URL, popModel.getAdPopupUrl());
                dataMap.put(WebViewActivity.USEWEBTITLE, "true");
                MyIntent.startActivity(activity, WebViewActivity.class, dataMap);
                break;
            case OpenScreenPopModel.POP_TYPE_LICAI:
                IndexActivity.startIndexActivityToTab(activity,0);
                break;
            case OpenScreenPopModel.POP_TYPE_NONE://无跳转
                break;
            default:
                ToastUtil.showInCenter("未知类型");
                break;
        }
    }


}
