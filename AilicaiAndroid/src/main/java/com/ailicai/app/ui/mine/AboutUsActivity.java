package com.ailicai.app.ui.mine;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.ailicai.app.BuildConfig;
import com.ailicai.app.R;
import com.ailicai.app.common.constants.IWBuildConfig;
import com.ailicai.app.common.hybrid.HybridEngine;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.common.version.VersionInterface;
import com.ailicai.app.common.version.VersionUtil;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.ui.html5.SupportUrl;
import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.util.CheckDoubleClick;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Gerry on 2017/7/18.
 */

public class AboutUsActivity extends BaseBindActivity implements VersionInterface {
    private static final int MSG_CLICK_UPDATE_APP = 0x001;
    @Bind(R.id.app_version)
    TextView appVersion;

    @Override
    public int getLayout() {
        return R.layout.about_us_activity;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        String versionName;
        PackageManager pm = getPackageManager();
        PackageInfo pi;

        String mBuildNumber = "";
        if (!IWBuildConfig.isProduction()) {
            String buildNumber = BuildConfig.BUILDNUMBER;
            if (!TextUtils.isEmpty(buildNumber)) {
                mBuildNumber = "(" + buildNumber + ")";
            }
        }
        try {
            pi = pm.getPackageInfo(getPackageName(), 0);
            versionName = getResources().getString(R.string.mine_current_version, pi.versionName,
                    (TextUtils.isEmpty(HybridEngine.getHybridEngine().getVersion().replace(".", ""))
                            ? "" : "." + HybridEngine.getHybridEngine().getVersion().replace(".", "")), mBuildNumber);
        } catch (PackageManager.NameNotFoundException e) {
            versionName = getResources().getString(R.string.mine_current_version, "0.0", "", "");
        }

        appVersion.setText("版本号 " + versionName);
    }

    @OnClick(R.id.mine_love_app)
    void onClickLoveApp() {
        ManyiAnalysis.getInstance().onEvent("mine_mine_appraise");
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ToastUtil.showInCenter("你还未安装应用市场");
        }
    }

    @OnClick(R.id.app_version)
    void onClickUpdate() {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        //MyPreference.getInstance().write(HASCHECKNEWVERSION, false);
        VersionUtil.check(this, this);
    }

    @OnClick(R.id.contact_us)
    void onClickContactUs() {
        MyIntent.startActivity(mContext, ContactUsActivity.class, null);
    }

    @OnClick(R.id.understand_ailicai)
    void goUnderstandPage() {
        Map<String, String> dataMap = ObjectUtil.newHashMap();
        //dataMap.put(WebViewActivity.TITLE,  "");
        //dataMap.put(WebViewActivity.NEED_REFRESH, "0");
        dataMap.put(BaseWebViewActivity.URL, SupportUrl.getSupportUrlsResponse().getAboutAiLiCaiUrl());
        dataMap.put(BaseWebViewActivity.USEWEBTITLE, "true");
        dataMap.put(BaseWebViewActivity.TOPVIEWTHEME, "false");
        MyIntent.startActivity(mContext, WebViewActivity.class, dataMap);
    }

    @Override
    public void remindPoint() {
    }

    @Override
    public void checkStart() {
        showLoadTranstView();
    }

    @Override
    public void checkSuccess() {
        showContentView();
    }

    @Override
    public void checkFailed(String message) {
        showContentView();
        ToastUtil.showInCenter(message);
    }

    @Override
    public void checkLatest(String version) {
        ToastUtil.showInCenter("当前已是最新版本" + version + "！^0^");
    }

    @Override
    public boolean ignorePop() {
        return true;
    }
}
