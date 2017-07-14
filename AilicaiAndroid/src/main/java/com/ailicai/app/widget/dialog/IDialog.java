package com.ailicai.app.widget.dialog;

import android.os.Bundle;
import android.view.View;

public interface IDialog {
    /**
     * 初始化View
     *
     * @param rootView
     * @param savedInstanceState
     */
    void setupView(View rootView, Bundle savedInstanceState);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    void setupData(Bundle savedInstanceState);

    /**
     * 设置根view的id
     *
     * @return
     */
    int getLayout();

    /**
     * 设置对话框的位置
     */
    void setDialogLocation();

    /**
     * 设置theme
     */
    int initThemeStyle();
}
