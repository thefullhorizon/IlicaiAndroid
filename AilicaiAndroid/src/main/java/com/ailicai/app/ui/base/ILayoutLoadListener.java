package com.ailicai.app.ui.base;

import android.view.View;

/**
 * Created by Jer on 2015/7/24.
 */
public interface ILayoutLoadListener {

    /**
     * 提示需要登录
     *
     * @param customeLoginView
     */
    void showLoginView(View customeLoginView);

    /**
     * 显示一个全屏白底的Load加载进度，一般用于首次加载耗时的操作
     */
    void showLoadView();

    /**
     * 在上层显示一个透明的Load加载进度，一般用于当前某个耗时操作
     */
    void showLoadTranstView();

    /**
     * 自定义错误文案
     *
     * @param errorInfo
     */
    void showErrorView(String errorInfo);

    /**
     * 自定义没有数据时显示的界面
     *
     * @param customeNoDataView
     */
    void showNoDataView(View customeNoDataView);

    /**
     * 显示正常有数据的界面，隐藏错误或Load
     */
    void showContentView();

    /**
     * 最上层的界面必须实现此函数，否则重载按钮无效
     */
    void reloadData();
}
