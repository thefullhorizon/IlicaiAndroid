package com.ailicai.app.ui.base.webview;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.LinearLayout;

import com.ailicai.app.R;
import com.ailicai.app.common.constants.AILICAIBuildConfig;
import com.ailicai.app.common.constants.CommonTag;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.ui.base.BaseActivity;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.umeng.socialize.UMShareListener;

import butterknife.Bind;

/**
 * Created by duo.chen on 2015/10/29.
 * webview ui
 */
@SuppressWarnings("unchecked")
public abstract class BaseWebViewFragment extends BaseBindFragment {

    @Bind(R.id.webview_root)
    LinearLayout rootView;

    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String TOPVIEWTHEME = "isDarkTop";
    public static final String NEED_REFRESH = "needRefresh";
    public static final String USEWEBTITLE = "usewebtitle";
    private BaseWebViewLayout webViewLayout;

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        webViewLayout = new BaseWebViewLayout((BaseActivity) getActivity());
        rootView.addView(webViewLayout);
    }

    @Override
    public void onDestroy() {
        if (webViewLayout != null) {
            webViewLayout.destroy();
        }
        super.onDestroy();
    }

    @Override
    public int getLayout() {
        return R.layout.webview_layout_root;
    }

    @Override
    public void onBackPressed() {

        //重定向问题
        //由pc.iwjwtest.com 重定向到m.iwjwtest.com上 不能goback
        if (!isFirstBaseUrl()) {
            if (null != getWebViewLayout()
                    && getWebViewLayout().cangoBack()) {
                goBack();
                showLeftCustomView(true);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    protected WebViewShareBean getWebViewShareBean() {
        if (null != getWebViewLayout()) {
            return getWebViewLayout().getWebViewShareBean();
        } else
            return null;
    }

    protected WebSettings getWebSetting() {
        if (null != getWebViewLayout()) {
            return getWebViewLayout().getWebSetting();
        } else {
            return null;
        }
    }

    protected void goBack() {
        if (null != getWebViewLayout()) {
            getWebViewLayout().goBack();
        }
    }

    public boolean isFirstBaseUrl() {
        return getWebViewLayout().isFirstBaseUrl();
    }

    protected void setTitleCanBack(final BaseWebViewLayout.ITitleClickListener listener) {
        if (null != getWebViewLayout()) {
            getWebViewLayout().setTitleCanBack(listener);
        }
    }

    public void showLeftCustomView(boolean should) {
        if (null != getWebViewLayout()) {
            getWebViewLayout().showLeftCustomView(should);
        }
    }

    public void setEmptyTitle() {
        if (null != getWebViewLayout()) {
            getWebViewLayout().setEmptyTitle();
        }
    }

    public void showBack(boolean show) {
        if (null != getWebViewLayout()) {
            getWebViewLayout().showBack(show);
        }
    }

    public void showRightTextIcon(boolean show) {
        if (null != getWebViewLayout()) {
            getWebViewLayout().showRightTextIcon(show);
        }
    }

    public void setNoTitle() {
        if (null != getWebViewLayout()) {
            getWebViewLayout().setNoTitle();
        }
    }

    public void addTitleRightText(String text, int style, View.OnClickListener event) {
        if (null != getWebViewLayout()) {
            getWebViewLayout().addTitleRightText(text, style, event);
        }
    }

    protected void addTitleCustomView(View view, View.OnClickListener onClickListener) {
        if (null != getWebViewLayout()) {
            getWebViewLayout().addTitleCustomView(view, onClickListener);
        }
    }

    protected void addLeftView(View view, View.OnClickListener onClickListener) {
        if (null != getWebViewLayout()) {
            getWebViewLayout().addLeftView(view, onClickListener);
        }
    }

    public void showMiddleView(boolean show) {
        if (null != getWebViewLayout()) {
            getWebViewLayout().showMiddleView(show);
        }
    }


    protected void addTitleRightText(int resId, int style, View.OnClickListener event) {
        if (null != getWebViewLayout()) {
            getWebViewLayout().addTitleRightText(resId, style, event);
        }
    }

    protected void callJsRefresh() {
        if (null != getWebViewLayout()) {
            getWebViewLayout().callJsRefresh();
        }
    }

    protected void addMethodCallAction(WebMethodCallAction webMethodCallAction) {
        webViewLayout.addMethodCallAction(webMethodCallAction);
    }

    protected void addJumpUiActions(WebJumpUiAction webJumpUiAction) {
        webViewLayout.addJumpUiActions(webJumpUiAction);
    }

    protected void setShareListener(UMShareListener listener) {
        webViewLayout.setShareListener(listener);
    }

    public void loadJs(String js) {
        if (null != getWebViewLayout()) {
            getWebViewLayout().loadJs(js);
        }
    }


    protected void setIWebListener(BaseWebViewLayout.IWebListener iWebListener) {
        webViewLayout.setiWebListener(iWebListener);
    }

    public void shouldShowLoading(boolean should) {
        if (null != getWebViewLayout()) {
            getWebViewLayout().shouldShowLoading(should);
        }
    }

    public void setLoadingStyle(BaseWebViewLayout.LoadingStyle loadingStyle) {
        if (null != getWebViewLayout()) {
            getWebViewLayout().setLoadingStyle(loadingStyle);
        }
    }

    public void loadUrl(String url) {
        if (null != getWebViewLayout()) {
            if (!TextUtils.isEmpty(url)) {
                if (!AILICAIBuildConfig.isProduction()) {
                    String customeH5Host = MyPreference.getInstance().read(CommonTag.CUSTOME_H5HOST, "");
                    if (!TextUtils.isEmpty(customeH5Host)) {
                        String nohttpurl = url.replace("http://", "");
                        nohttpurl = nohttpurl.substring(nohttpurl.indexOf("/"));
                        url = "http://" + customeH5Host + nohttpurl;
                    }
                }
                getWebViewLayout().loadUrl(url);
            }
        }
    }

    protected void setTopTheme(boolean isDark) {
        if (null != getWebViewLayout()) {
            getWebViewLayout().setTopTheme(isDark);
        }
    }

    protected void addRightView(View view, View.OnClickListener onClickListener) {
        if (null != getWebViewLayout()) {
            getWebViewLayout().addRightView(view, onClickListener);
        }
    }

    protected void shouldShowRight(boolean show) {
        if (null != getWebViewLayout()) {
            getWebViewLayout().shouldShowRight(show);
        }
    }

    public BaseWebViewLayout getWebViewLayout() {
        return webViewLayout;
    }

    public void setTitle(String title) {
        if (null != getWebViewLayout()) {
            getWebViewLayout().setTitle(title);
        }
    }


}
