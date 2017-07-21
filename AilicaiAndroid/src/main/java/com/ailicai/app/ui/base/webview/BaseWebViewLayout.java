package com.ailicai.app.ui.base.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.net.http.SslError;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ailicai.app.BuildConfig;
import com.ailicai.app.R;
import com.ailicai.app.common.constants.AILICAIBuildConfig;
import com.ailicai.app.common.hybrid.HybridEngine;
import com.ailicai.app.common.hybrid.utils.VersionUpdate;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.jshttp.ServiceJsCallSender;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.common.utils.UrlDecoder;
import com.ailicai.app.common.version.VersionUtil;
import com.ailicai.app.eventbus.LoginEvent;
import com.ailicai.app.ui.account.OpenAccountWebViewActivity;
import com.ailicai.app.ui.base.BaseActivity;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.view.AssetInViewOfBirdActivity;
import com.ailicai.app.ui.view.MyWalletActivity;
import com.ailicai.app.ui.voucher.CouponWebViewActivity;
import com.ailicai.app.widget.IWTopTitleView;
import com.alibaba.fastjson.JSON;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.ConvertUtil;
import com.huoqiu.framework.util.TimeUtil;
import com.umeng.socialize.UMShareListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by duo.chen on 2016/7/5.17:13
 */

public class BaseWebViewLayout extends LinearLayout {

    public static final String TAG = "BaseWebViewActivity";
    private static final String UA = " Iwjw_Android_";

    private static final String ROUTER_SERVER = "_router=server";

    private final static int TIMER_BEGIN = 0;
    private final static int TIMER_END = 1;
    protected CountDownTimer timer;
    @Bind(R.id.webview_title)
    IWTopTitleView topTitleView;
    @Bind(R.id.web_view_main_layout)
    View webViewMainLayout;
    @Bind(R.id.web_view)
    WebView webView;
    @Bind(R.id.webview_progress_line)
    ProgressBar loadingProgressBar;
    @Bind(R.id.webview_progress_wheel)
    View loadingWheel;
    @Bind(R.id.webview_networkerror)
    View networkErrorView;
    @Bind(R.id.webview_serviceerror)
    View serviceErrorView;
    @Bind(R.id.webview_retry)
    View webviewRetry;
    @Bind(R.id.web_view_swipelayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private LoadingStyle style = LoadingStyle.LINE;
    private boolean loadFinish;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIMER_BEGIN:
                    if (null != timer) {
                        timer.cancel();
                    }
                    initProgress();
                    initTimeCounter();
                    break;
                case TIMER_END:
                    timer.cancel();
                    break;
            }
        }
    };
    private String firstUrl;
    private boolean isDark = false;
    private WebViewShareBean webViewShareBean;
    private HashMap<String, String> globalCallBacks = new HashMap<>();
    private List<WebSchemeFilter> schemeFilters = new ArrayList<>();
    private List<WebJumpUiAction> jumpUiActions = new ArrayList<>();
    private List<WebPathAction> pathActions = new ArrayList<>();
    private List<WebMethodCallAction> webMethodCallActions = new ArrayList<>();
    private IWebListener iWebListener;
    private SoftReference<BaseActivity> webViewActivitySoftReference;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout
            .OnRefreshListener() {
        @Override
        public void onRefresh() {
            callJsRefresh();
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != swipeRefreshLayout) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }, 2000);
        }
    };

    public BaseWebViewLayout(BaseActivity context) {
        super(context);
        this.webViewActivitySoftReference = new SoftReference<>(context);
        initWebViews();
    }

    public BaseWebViewLayout(BaseActivity context, AttributeSet attrs) {
        super(context, attrs);
        this.webViewActivitySoftReference = new SoftReference<>(context);
        initWebViews();
    }

    public BaseWebViewLayout(BaseActivity context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.webViewActivitySoftReference = new SoftReference<>(context);
        initWebViews();
    }

    private BaseActivity getWRContext() {
        return webViewActivitySoftReference == null ? null : webViewActivitySoftReference.get();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViews() {
        if (null != getWRContext()) {
            View.inflate(getWRContext(), getLayout(), this);
            ButterKnife.bind(this);
            WebSettings settings = getWebSetting();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            settings.setJavaScriptEnabled(true);
            settings.setSupportZoom(true);
            settings.setDefaultTextEncodingName("UTF-8");
            settings.setBuiltInZoomControls(false);
            settings.setUseWideViewPort(true);
            settings.setTextZoom(100);
            String userAgent = settings.getUserAgentString();
            userAgent += UA + BuildConfig.VERSION_NAME;
            settings.setUserAgentString(userAgent);
            LogUtil.i(TAG, "userAgent " + userAgent);

            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            settings.setLoadWithOverviewMode(true);
            settings.setAppCacheEnabled(true);
            settings.setDomStorageEnabled(true);

            if (!AILICAIBuildConfig.isProduction() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                WebView.setWebContentsDebuggingEnabled(true);
            }

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    showLoadView();
                    showContentView();
                    handler.sendEmptyMessage(0);
                    LogUtil.i(TAG, "onPageStarted url " + url);
                    if (null != iWebListener) {
                        iWebListener.onWebLoadStart(BaseWebViewLayout.this);
                    }
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    setFirstBaseUrl(url);
                    loadFinish = true;
                    if (null != iWebListener) {
                        iWebListener.onReceivedTitle(view.getTitle(), BaseWebViewLayout.this);
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String
                        failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);

                    LogUtil.i(TAG, "errorCode " + errorCode + " description " + description + " failingUrl " + failingUrl);

                    if (errorCode == WebViewClient.ERROR_UNSUPPORTED_SCHEME) {
                        return;
                    }

                    view.setVisibility(View.GONE);
                    if (!SystemUtil.isNetWorkAvaliable(getWRContext())) {
                        showNetWorkError();
                    } else {
                        showServiceError();
                    }
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    LogUtil.i(TAG, "shouldOverrideUrlLoading url " + url);

             /*       if (!url.contains(ROUTER_SERVER)) {

                        try {
                            String realPath = findRealPath(url);
                            HashMap<String, String> map = HybridEngine.getHybridEngine().getRouter()
                                    .getRules();

                            if (map.containsKey(realPath)) {
                                String filePath = map.get(realPath);
                                File file = new File(VersionUpdate.getHybridFileDir
                                        (getWRContext()) + File.separator + filePath);
                                if (file.exists()) {
                                    loadFile(file.getAbsolutePath() + subUrl(url, realPath));
                                    return true;
                                }
                            }
                        } catch (Exception ignored) {

                        }
                    }*/

                    boolean should = super.shouldOverrideUrlLoading(view, url);

                    for (WebSchemeFilter webSchemeFilter : schemeFilters) {
                        if (webSchemeFilter.action(url)) {
                            should = true;
                        }
                    }

                    return should;
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    Log.e(TAG, "onReceivedSslError:" + error);
                    String mUAStr = view.getSettings().getUserAgentString();
                    Log.e(TAG, "mUAStr:" + mUAStr);
                    if (!TextUtils.isEmpty(mUAStr) && mUAStr.contains("Chrome/53") || mUAStr.contains("Chrome/54")) {
                        handler.proceed();
                    } else {
                        super.onReceivedSslError(view, handler, error);
                    }
                    //   handler.proceed();
                }

            });
            webView.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);

                    if (null != iWebListener) {
                        iWebListener.onProgressChanged(newProgress, BaseWebViewLayout.this);
                    }
                }

                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    if (null != iWebListener) {
                        iWebListener.onReceivedTitle(title, BaseWebViewLayout.this);
                    }
                }

            });

            addBasePathActions();
            addBaseSchemeFilters();
            addBaseJumpUiActions();
            addBaseCallMethodActions();

            webViewShareBean = new WebViewShareBean(getWRContext());

            shouldShowShare(false);

            setLoadingStyle(LoadingStyle.LINE);

            swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setColorSchemeResources(R.color.main_red_color);
            setCanRefresh(false);

            setTitleCanBack(null);

            EventBus.getDefault().register(this);
        }

    }

    private void addBaseSchemeFilters() {

        addSchemeFilter(new WebSchemeFilter("mailto:", "geo:",
                "tel:") {

            @Override
            public boolean action(String url) {
                if (filter(url)) {
                    if (null != getWRContext()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        getWRContext().startActivity(intent);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        addSchemeFilter(new WebSchemeFilter("iwjw://callnatvie") {
            @Override
            public boolean action(String url) {
                if (filter(url)) {
                    String path = Uri.parse(url).getPath();
                    for (WebPathAction webPathAction : pathActions) {
                        if (webPathAction.match(path)) {
                            webPathAction.action(url);
                            break;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    /**
     * 解析Api params
     *
     * @param url     需要处理的url
     * @param ignores 忽略的参数
     */
    private HashMap<String, String> parseUrlParams(String url, String... ignores) {

        HashMap<String, String> params = new HashMap<>();

        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer();
        sanitizer.setAllowUnregisteredParamaters(true);
        sanitizer.setUnregisteredParameterValueSanitizer(UrlQuerySanitizer.getAllButNulLegal());
        sanitizer.parseUrl(url);

        final Set<String> query = sanitizer.getParameterSet();
        if (!query.isEmpty()) {
            TreeSet<String> treeQuery = new TreeSet<>(query);
            for (String key : treeQuery) {
                boolean needPut = true;
                for (String ignore : ignores) {
                    if (ignore.contains(key)) {
                        needPut = false;
                    }
                }
                if (needPut) {
                    try {
                        params.put(key, UrlDecoder.getValue(sanitizer, key));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return params;
    }

    private void addBaseJumpUiActions() {

        addJumpUiActions(new WebJumpUiAction("0") {
            @Override
            public void jumpUi(HashMap<String, String> params) {

                if (params.containsKey("url")) {
                    String url = String.valueOf(params.get("url"));
                    Map<String, String> dataMap = ObjectUtil.newHashMap();
                    dataMap.put(BaseWebViewActivity.URL, url);
                    dataMap.put(BaseWebViewActivity.USEWEBTITLE, "true");
                    dataMap.put(BaseWebViewActivity.TOPVIEWTHEME, String.valueOf(isDark));
                    MyIntent.startActivity(getWRContext(), WebViewActivity.class, dataMap);
                }
            }
        });

        addJumpUiActions(new WebJumpUiAction("1") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
//                IndexChoiceBusinessView.toSecdHouseActivity(getWRContext(), new Intent());
            }
        });

        addJumpUiActions(new WebJumpUiAction("2") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
//                Intent intent = new Intent(getContext(), NewHouseListActivity.class);
//                getContext().startActivity(intent);
//                MyApplication.getInstance().setCurrentBusinessType(MyApplication.BUSINESS_NEWHOUSE);
            }
        });

        addJumpUiActions(new WebJumpUiAction("3") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
//                IndexChoiceBusinessView.toRentalActivity(getWRContext(), new Intent());
            }
        });

        addJumpUiActions(new WebJumpUiAction("4", "finance") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
//                IndexChoiceBusinessView.toFinanceHome(getWRContext(), new Intent());
            }
        });

        addJumpUiActions(new WebJumpUiAction("5") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
            }
        });

        addJumpUiActions(new WebJumpUiAction("6") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                if (params.containsKey("url")) {
                    String url = params.get("url");
//                    IndexChoiceBusinessView.toEncyclopedias(getWRContext(), url);
                }
            }
        });

        addJumpUiActions(new WebJumpUiAction("7") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                if (params.containsKey("url")) {
                    String url = params.get("url");
//                    IndexChoiceBusinessView.toPpTalk(getWRContext(), url);
                }
            }
        });

        addJumpUiActions(new WebJumpUiAction("8") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
//                IndexChoiceBusinessView.toCalcultorActivity(getWRContext());
            }
        });

        addJumpUiActions(new WebJumpUiAction("9") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
            }
        });

        addJumpUiActions(new WebJumpUiAction("10") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
            }
        });

        addJumpUiActions(new WebJumpUiAction("11") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                MyWalletActivity.goMywallet(getWRContext());
            }
        });

        addJumpUiActions(new WebJumpUiAction("12") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                if (null != getWRContext()) {
                    Intent intent = new Intent(getWRContext(), AssetInViewOfBirdActivity.class);
                    getWRContext().startActivity(intent);
                }
            }
        });

        addJumpUiActions(new WebJumpUiAction("openaccountprocess") {
            @Override
            public void jumpUi(HashMap<String, String> params) {

                if (params.containsKey("url")) {
                    OpenAccountWebViewActivity.goToOpenAccount(getWRContext());
                }
            }
        });


        addJumpUiActions(new WebJumpUiAction("coupon") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
                CouponWebViewActivity.goCoupon(getWRContext());
            }
        });


        addJumpUiActions(new WebJumpUiAction("useActivityName") {
            @Override
            public void jumpUi(HashMap<String, String> params) {
//                if (params.containsKey("className")) {
//                    String className = params.get("className");
//                    if (!TextUtils.isEmpty(className)) {
//                        try {
//                            if (null != getWRContext()) {
//                                Intent intent = new Intent(getWRContext(), Class.forName(className));
//                                try {
//                                    for (Map.Entry<String, String> entry : params.entrySet()) {
//                                        String value = entry.getValue();
//                                        String[] valueArray = value.split("-");
//                                        if (valueArray.length > 1) {
//                                            String valueType = valueArray[0];
//                                            String rValue = value.substring(value.indexOf("-") + 1);
//                                            LogUtil.i(TAG, "value " + value + " valueType " + valueType + " rValue " + rValue);
//                                            switch (valueType) {
//                                                case "float":
//                                                    intent.putExtra(entry.getKey(), Float.valueOf(rValue).floatValue());
//                                                    break;
//                                                case "int":
//                                                    intent.putExtra(entry.getKey(), Integer.valueOf(rValue).intValue());
//                                                    break;
//                                                case "String":
//                                                    intent.putExtra(entry.getKey(), rValue);
//                                                    break;
//                                                case "boolean":
//                                                    intent.putExtra(entry.getKey(), Boolean.valueOf(rValue).booleanValue());
//                                                    break;
//                                                case "long":
//                                                    intent.putExtra(entry.getKey(), Long.valueOf(rValue).longValue());
//                                                    break;
//                                                case "double":
//                                                    intent.putExtra(entry.getKey(), Double.valueOf(rValue).doubleValue());
//                                                    break;
//                                                default:
//                                                    break;
//                                            }
//                                        } else {
//                                            intent.putExtra(entry.getKey(), value);
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    LogUtil.e(e.toString());
//                                }
//                                getContext().startActivity(intent);
//                            }
//                        } catch (ClassNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
            }
        });

    }

    private void addBasePathActions() {

        addPathActions(new WebPathAction("/callapiservice") {
            @Override
            public void action(String url) {
                String callbackName = "callback";
                final String callBack = UrlDecoder.parseValue(url, callbackName);
                HashMap<String, String> params;

                String apiName = "apiname";
                String fullApiName = "fullapiname";
                String api = UrlDecoder.parseValue(url, apiName) + ".rest";

                if (!TextUtils.isEmpty(api)) {
                    if (!api.startsWith("/")) {
                        api = "/" + api;
                    }
                }
                String fakeApi = UrlDecoder.parseValue(url, fullApiName);
                boolean isFakeApi = !TextUtils.isEmpty(fakeApi);
                String callApi = isFakeApi ? fakeApi : api;
                params = parseUrlParams(url, callbackName, apiName, fullApiName);
                ServiceJsCallSender.exec(getWRContext(), isFakeApi,
                        callApi, params, new CallapiserviceListener(BaseWebViewLayout.this, callBack));
            }
        });

        addPathActions(new WebPathAction("/jumpui") {
            @Override
            public void action(String url) {

                String callbackName = "callback";
                final String callBack = UrlDecoder.parseValue(url, callbackName);
                HashMap<String, String> params;
                final StringBuilder js = new StringBuilder("javascript:");

                String pageKey = "page";
                String pageName = UrlDecoder.parseValue(url, pageKey);
                params = parseUrlParams(url, callbackName, pageKey);
                if (!TextUtils.isEmpty(pageName)) {
                    for (WebJumpUiAction action : jumpUiActions) {
                        if (action.match(pageName)) {
                            if (!CheckDoubleClick.isFastDoubleClick()) {
                                action.jumpUi(params);
                            }
                        }
                    }
                }
                loadJs(js.append(callBack).append("()").toString());
            }
        });

        addPathActions(new WebPathAction("/callmethod") {
            @Override
            public void action(String url) {
                String callbackName = "callback";
                final String callBack = UrlDecoder.parseValue(url, callbackName);
                HashMap<String, String> params;
                final StringBuilder js = new StringBuilder("javascript:");

                String methodKey = "method";
                String method = UrlDecoder.parseValue(url, methodKey);
                params = parseUrlParams(url, methodKey);
                Object call;

                if (!TextUtils.isEmpty(method)) {
                    for (WebMethodCallAction action : webMethodCallActions) {
                        if (action.match(method)) {
                            call = action.call(params);
                            if (null != call && !TextUtils.isEmpty(call.toString())) {
                                loadJs(js.append(callBack).append("" +
                                        "(").append(call).append(")").toString());
                            }
                            break;
                        }
                    }
                }
            }
        });

        addPathActions(new WebPathAction("/datacollection") {
            @Override
            public void action(String url) {

                String callbackName = "callback";
                final String callBack = UrlDecoder.parseValue(url, callbackName);
                final StringBuilder js = new StringBuilder("javascript:");
                try {
                    String data = UrlDecoder.parseValue(url, "data");
                    LogUtil.d("BaseWebViewActivity", "datacollection:" + data);
                    HashMap dataMap = JSON.parseObject(data, HashMap.class);
//                    EventLog.getInstance().updateJsBridgeEventInfo(dataMap);

                    loadJs(js.append(callBack).append("" +
                            "(").append(")").toString());

                } catch (Exception e) {
                    LogUtil.i(e.toString());
                }
            }
        });

        addPathActions(new WebPathAction("/back") {
            @Override
            public void action(String url) {
                if (null != getWRContext()) {
                    getWRContext().finish();
                }
            }
        });

        addPathActions(new WebPathAction("/toast") {
            @Override
            public void action(String url) {
                String msg = "msg";
                String message = UrlDecoder.parseValue(url, msg);
                if (!TextUtils.isEmpty(message)) {
                    ToastUtil.showInCenter(message);
                }
            }
        });

        addPathActions(new WebPathAction("/loginwithui") {
            @Override
            public void action(String url) {
                String callbackName = "callback";
                final String callBack = UrlDecoder.parseValue(url, callbackName);

                if (!TextUtils.isEmpty(callBack)) {
                    setLoginCallBack(callBack);
                    if (!UserInfo.isLogin()) {
                        if (null != getWRContext()) {
                            LoginManager.goLogin(getWRContext(), LoginManager.LOGIN_FROM_H5_WEB);
                        }
                    } else {
                        final StringBuilder js = new StringBuilder("javascript:");
                        loadJs(js + getLoginCallBack() + "(" + getUserInfo() + ")");
                    }
                }
            }
        });

    }

    private void addBaseCallMethodActions() {

        addMethodCallAction(new WebMethodCallAction("getcurrenttimeexact") {
            @Override
            public Object call(HashMap params) {
                return TimeUtil.getCurrentTimeExact().getTimeInMillis();
            }
        });

        addMethodCallAction(new WebMethodCallAction("getloginstate") {
            @Override
            public Object call(HashMap params) {
                if (UserInfo.isLogin()) {
                    return getUserInfo();
                } else {
                    return false;
                }
            }
        });

        addMethodCallAction(new WebMethodCallAction("updateapp") {
            @Override
            public Object call(HashMap params) {
                VersionUtil.check(getWRContext());
                return null;
            }
        });

        addMethodCallAction(new WebMethodCallAction("setrefreshhead") {
            @Override
            public Object call(HashMap params) {
                if (params.containsKey("status") && params.containsKey("jsRefresh")) {
                    int status = Integer.valueOf(String.valueOf(params.get("status")));
                    setRefreshMethodName(String.valueOf(params.get("jsRefresh")));
                    setCanRefresh(status == 1);
                }
                return "{}";
            }
        });

        addMethodCallAction(new WebMethodCallAction("sharepage") {
            @Override
            public Boolean call(HashMap params) {

                if (params.containsKey("close") && !TextUtils.isEmpty(String.valueOf(params.get
                        ("close"))) && String.valueOf(params.get("close")).equals("1")) {
                    shouldShowShare(false);
                } else {
                    addTitleRightText(R.string.detail_forward, isDark
                            ? R.style.text_18_ffffff :
                            R.style.text_18_00000000, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (CheckDoubleClick.isFastDoubleClick()) return;
                            showShareDialog();
                        }
                    });

                    shouldShowShare(true);
                    getWebViewShareBean().init();
                    if (params.containsKey("desc") && !TextUtils.isEmpty(String.valueOf(params.get
                            ("desc")))) {
                        getWebViewShareBean().setShareContent(String.valueOf(params.get("desc")));
                    }
                    if (params.containsKey("title") && !TextUtils.isEmpty(String.valueOf(params.get
                            ("title")))) {
                        getWebViewShareBean().setShareTitle(String.valueOf(params.get("title")));
                    }
                    if (params.containsKey("link") && !TextUtils.isEmpty(String.valueOf(params.get
                            ("link")))) {
                        getWebViewShareBean().setShareUrl(String.valueOf(params.get("link")));
                    }
                    if (params.containsKey("imgUrl") && !TextUtils.isEmpty(String.valueOf(params.get
                            ("imgUrl")))) {
                        getWebViewShareBean().setShareImageUrl(String.valueOf(params.get
                                ("imgUrl")));
                    }
                }

                return true;
            }
        });


        addMethodCallAction(new WebMethodCallAction("doshare") {
            @Override
            public Boolean call(HashMap params) {
                getWebViewShareBean().init();
                if (params.containsKey("desc") && !TextUtils.isEmpty(String.valueOf(params.get
                        ("desc")))) {
                    getWebViewShareBean().setShareContent(String.valueOf(params.get("desc")));
                }
                if (params.containsKey("title") && !TextUtils.isEmpty(String.valueOf(params.get
                        ("title")))) {
                    getWebViewShareBean().setShareTitle(String.valueOf(params.get("title")));
                }
                if (params.containsKey("link") && !TextUtils.isEmpty(String.valueOf(params.get
                        ("link")))) {
                    getWebViewShareBean().setShareUrl(String.valueOf(params.get("link")));
                }
                if (params.containsKey("imgUrl") && !TextUtils.isEmpty(String.valueOf(params.get
                        ("imgUrl")))) {
                    getWebViewShareBean().setShareImageUrl(String.valueOf(params.get
                            ("imgUrl")));
                }

                //分享callback
                if (params.containsKey("callback") && !TextUtils.isEmpty(String.valueOf(params.get
                        ("callback")))) {
                    setShareCallBack(String.valueOf(params.get
                            ("callback")));
                }
                showShareDialog();
                return true;
            }
        });

        addMethodCallAction(new WebMethodCallAction("systemupgradealert") {
            @Override
            public Object call(HashMap params) {
//                // 请求是否是否维护中
//                FinanceUpgradePresenter presenter = new FinanceUpgradePresenter();
//                presenter.httpForSystemIsFix(getWRContext());
                return false;
            }
        });
    }

    private void addGlobalCallBacks(String key, String callback) {
        globalCallBacks.put(key, callback);
    }

    private String getGlobalCallback(String key) {
        return globalCallBacks.get(key);
    }

    private String getRefreshMethodName() {
        return getGlobalCallback("refreshMethodName");
    }

    private void setRefreshMethodName(String callName) {
        addGlobalCallBacks("refreshMethodName", callName);
    }

    public String getLoginCallBack() {
        return getGlobalCallback("loginCallBack");
    }

    public void setLoginCallBack(String loginCallBack) {
        addGlobalCallBacks("loginCallBack", loginCallBack);
    }

    public String getShareCallBack() {
        return getGlobalCallback("shareCallBack");
    }

    public void setShareCallBack(String shareCallBack) {
        addGlobalCallBacks("shareCallBack", shareCallBack);
    }

    protected void callJsRefresh() {
        loadJs("javascript:callJs('" + getRefreshMethodName() + "',{})");
    }

    @OnClick(R.id.webview_retry)
    void clickRetry() {
        reloadData();
    }

    private void setCanRefresh(boolean canRefresh) {
        swipeRefreshLayout.setEnabled(canRefresh);
    }

    protected void addTitleRightText(int resId, int style, View.OnClickListener event) {
        topTitleView.addRightText(resId, style, event);
        if (isDark) {
            topTitleView.getRightText().setAlpha(1.0f);
        }
    }

    public void addTitleRightText(String text, int style, View.OnClickListener event) {
        topTitleView.addRightText(text, style, event);
        if (isDark) {
            topTitleView.getRightText().setAlpha(1.0f);
        }
    }

    protected void addTitleCustomView(View view, View.OnClickListener onClickListener) {
        topTitleView.addMiddleView(view, onClickListener);
    }

    protected void addLeftView(View view, View.OnClickListener onClickListener) {
        topTitleView.addLeftView(view, onClickListener);
    }

    protected void addRightView(View view, View.OnClickListener onClickListener) {
        topTitleView.addCustomeViewToRightLayout(view, onClickListener);
    }

    protected void shouldShowRight(boolean show) {
        topTitleView.shouldShowRight(show);
    }

    public void showMiddleView(boolean show) {
        topTitleView.showMiddleView(show);
    }

    protected void setiWebListener(IWebListener iWebListener) {
        this.iWebListener = iWebListener;
    }

    WebSettings getWebSetting() {
        return webView.getSettings();
    }

    public void setTitle(String title) {
        topTitleView.setTitleText(title);
    }

    public void setEmptyTitle() {
        topTitleView.setTitleText("");
    }

    public void showBack(boolean show) {
        topTitleView.showBackBtn(show);
    }

    public void showRightTextIcon(boolean show) {
        topTitleView.showRightTextIcon(show ? View.VISIBLE : View.GONE);
    }

    public void setNoTitle() {
        topTitleView.setVisibility(View.GONE);
        CommonUtil.uiSystemBarTintNoTitle(getWRContext(), webViewMainLayout);
    }

    protected void setTopTheme(boolean isDark) {
        topTitleView.setDark(isDark);
        this.isDark = isDark;
    }

    /**
     * 设置webview的title可以使用webview返回 一些有重定向的页面使用时会有问题，因为重定向页面无法后退
     * 默认back键直接finish
     */
    protected void setTitleCanBack(final ITitleClickListener listener) {

        topTitleView.setTitleOnClickListener(new IWTopTitleView.TopTitleOnClickListener() {
            @Override
            public boolean onBackClick() {
                if (null != listener) {
                    listener.onClick();
                }
                return false;
            }
        });
    }

    public void loadUrl(String url) {

        if (null != webView) {
            firstUrl = null;

            try {
                if (url.contains(ROUTER_SERVER)) {
                    webView.loadUrl(url);
                } else {

                    String realPath = findRealPath(url);
                    HashMap<String, String> map = HybridEngine.getHybridEngine().getRouter().getRules();

                    if (map.containsKey(realPath)) {

                        String filePath = map.get(realPath);

                        File file = new File(VersionUpdate.getHybridFileDir(getWRContext())
                                + File.separator + filePath);
                        if (file.exists()) {
                            loadFile(file.getAbsolutePath() + subUrl(url, realPath));
                        } else {
                            webView.loadUrl(url);
                        }
                    } else {
                        webView.loadUrl(url);
                    }
                }
            } catch (Exception e) {
                if (null != webView) {
                    webView.loadUrl(url);
                }
            }
        }
    }

    private void loadFile(String filePath) {
        if (null != webView) {
            try {
                webView.loadUrl("file:///" + filePath);
            } catch (Exception e) {
                LogUtil.i(e.toString());
            }

        }
    }

    public void loadJs(String js) {
        if (null != webView) {
            try {
                webView.loadUrl(js);
            } catch (Exception e) {
                LogUtil.i(e.toString());
            }

        }
    }

    public void reloadData() {
        showLoadView();
        webView.reload();
    }

    protected void goBack() {
        webView.goBack();
    }

    public void showLoadView() {
        loadFinish = false;
        shouldShowLoading(true);
        webView.setVisibility(View.GONE);
        webviewRetry.setVisibility(View.GONE);
        serviceErrorView.setVisibility(View.GONE);
        networkErrorView.setVisibility(View.GONE);
    }

    public void showContentView() {
        webView.setVisibility(View.VISIBLE);
        serviceErrorView.setVisibility(View.GONE);
        networkErrorView.setVisibility(View.GONE);
        webviewRetry.setVisibility(View.GONE);
    }

    private void showNetWorkError() {
        networkErrorView.setVisibility(View.VISIBLE);
        webviewRetry.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
        serviceErrorView.setVisibility(View.GONE);
        shouldShowLoading(false);
    }

    private void showServiceError() {
        webviewRetry.setVisibility(View.VISIBLE);
        serviceErrorView.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
        networkErrorView.setVisibility(View.GONE);
        shouldShowLoading(false);
    }

    public void setLoadingStyle(LoadingStyle loadingStyle) {
        this.style = loadingStyle;
        switch (loadingStyle) {
            case LINE:
                loadingProgressBar.setVisibility(View.VISIBLE);
                loadingWheel.setVisibility(View.GONE);
                break;
            case WHEEL:
                loadingProgressBar.setVisibility(View.GONE);
                loadingWheel.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void shouldShowLoading(boolean should) {
        if (null != getLoadingView()) {
            getLoadingView().setVisibility(should ? View.VISIBLE : View.GONE);
        }
    }

    private View getLoadingView() {
        return style.equals(LoadingStyle.LINE) ? loadingProgressBar : loadingWheel;
    }

    private void initTimeCounter() {
        timer = new CountDownTimer(1000 * 10, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (loadingProgressBar.getProgress() >= 70) {
                    if (loadingProgressBar.getProgress() <= 95) {
                        loadingProgressBar.setProgress(loadingProgressBar.getProgress() + 1);
                    }
                } else {
                    loadingProgressBar.setProgress(loadingProgressBar.getProgress() + 4);
                }
                if (loadingProgressBar.getProgress() >= 95 && loadFinish) {
                    loadingProgressBar.setVisibility(View.GONE);
                    handler.sendEmptyMessage(1);
                }
            }

            //设置10秒的计时器。基本保证onFinish时 loadFinish = true;
            @Override
            public void onFinish() {
                //if (loadFinish)
                loadingProgressBar.setVisibility(View.GONE);
            }
        };
        timer.start();
    }

    private void initProgress() {
        loadingProgressBar.setProgress(0);
    }

    //当前url是否为一级页面
    public boolean isFirstBaseUrl() {
        if (null != webView) {
            String loadingUrl = webView.getUrl();
            if (!TextUtils.isEmpty(loadingUrl) && !TextUtils.isEmpty(firstUrl)) {
                String compareUrlloading = loadingUrl;
                String compareUrlfirst = firstUrl;
                if (compareUrlloading.endsWith("/")) {
                    compareUrlloading = compareUrlloading.substring(0, compareUrlloading.length() - 1);

                }
                if (firstUrl.endsWith("/")) {
                    compareUrlfirst = compareUrlfirst.substring(0, compareUrlfirst.length() - 1);
                }
                LogUtil.i(TAG, "url " + compareUrlloading + " firstUrl " + compareUrlfirst);
                return compareUrlloading.equals(compareUrlfirst);
            } else {
                return true;
            }
        }
        return true;
    }

    //设置一级页面地址
    private void setFirstBaseUrl(String baseUrl) {
        if (TextUtils.isEmpty(firstUrl)) {
            this.firstUrl = baseUrl;
        }
    }

    public void destroy() {
        try {
            if (null != timer) {
                timer.cancel();
            }

            swipeRefreshLayout.removeView(webView);
            swipeRefreshLayout.setOnRefreshListener(null);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
    }

    public String getUserInfo() {
        HashMap map = new HashMap();
        map.put("userid", UserInfo.getInstance().getUserId());
        map.put("name", UserInfo.getInstance().getmName());
        map.put("phonenum", UserInfo.getInstance().getUserMobile());
        map.put("istestuser", UserInfo.getInstance().getIsTestuser());
        map.put("ucode", UserInfo.getInstance().getUticket());
        return JSON.toJSONString(map);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLoginEvent(LoginEvent event) {
        if (event.isLoginSuccess()) {
            if (!TextUtils.isEmpty(getLoginCallBack())) {
                loadJs("javascript:" + getLoginCallBack() + "(" + getUserInfo() + ")");
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    protected void addJumpUiActions(WebJumpUiAction webJumpUiAction) {
        jumpUiActions.add(webJumpUiAction);
    }

    protected void setShareListener(UMShareListener listener) {
        if (null != webViewShareBean && null != getWRContext()) {
            webViewShareBean.setShareListener(listener);
        }
    }

    protected void addMethodCallAction(WebMethodCallAction webMethodCallAction) {
        webMethodCallActions.add(webMethodCallAction);
    }

    protected void addPathActions(WebPathAction webPathAction) {
        pathActions.add(webPathAction);
    }

    protected void addSchemeFilter(WebSchemeFilter webSchemeFilter) {
        schemeFilters.add(webSchemeFilter);
    }

    protected boolean cangoBack() {
        return webView.canGoBack();
    }

    public int getLayout() {
        return R.layout.webview_layout;
    }

    public WebViewShareBean getWebViewShareBean() {
        return webViewShareBean;
    }

    private void showShareDialog() {
        if (null != webViewShareBean && null != getWRContext()) {
            webViewShareBean.showShareDialog(getWRContext().getSupportFragmentManager());
        }
    }

    public void showLeftCustomView(boolean should) {
        topTitleView.showLeftCustomView(should ? View.VISIBLE : View.GONE);
    }

    public void shouldShowShare(boolean should) {
        topTitleView.showRightTextIcon(should ? View.VISIBLE : View.GONE);
    }

    private String findRealPath(String url) {

        String keyPath = null;
        Uri uri = Uri.parse(url);
        Log.i(TAG, "findRealPath url path == " + uri.getPath());

        String path = uri.getPath();
        if (!TextUtils.isEmpty(path)) {
            String[] ps = path.split("/");
            keyPath = ps[ps.length - 1];
            Pattern p = Pattern.compile("^\\w*");
            Matcher matcher = p.matcher(keyPath);
            if (matcher.find()) {
                keyPath = matcher.group();
            }
        }
        return keyPath;
    }

    private String subUrl(String url, String realPath) {
        String sub = url.substring(url.indexOf(realPath) + realPath.length());

        LogUtil.i(TAG, "subUrl " + sub);

        if (sub.contains(".html")) {
            sub = sub.substring(5);
        } else if (sub.contains(".htm")) {
            sub = sub.substring(4);
        }

        return sub;
    }

    public enum LoadingStyle {
        LINE,
        WHEEL
    }

    public interface IWebListener {
        void onWebLoadStart(BaseWebViewLayout webViewLayout);

        void onProgressChanged(int newProgress, BaseWebViewLayout webViewLayout);

        void onReceivedTitle(String title, BaseWebViewLayout webViewLayout);
    }

    public interface ITitleClickListener {
        void onClick();
    }

    /**
     * 定义为static内部类，防止内存泄露
     */

    private static class CallapiserviceListener extends IwjwRespListener {

        final StringBuilder js = new StringBuilder("javascript:");
        private SoftReference<BaseWebViewLayout> webViewLayotSoftReference;
        private String callBack = "";

        CallapiserviceListener(BaseWebViewLayout webViewLayout, String callBack) {
            this.webViewLayotSoftReference = new SoftReference<>(webViewLayout);
            this.callBack = callBack;
        }

        @Override
        public void onJsonSuccess(Object jsonObject) {

            if (null != webViewLayotSoftReference.get()) {

                try {
                    Response response = ConvertUtil.json2Obj(jsonObject.toString(), Response.class);
                    switch (response.getErrorCode()) {
                        case RestException.NO_ERROR:
                        case RestException.ATTENTION_EXCEED_THE_UPPER_LIMIT://关注到达上限
                        case RestException.GET_VERIFY_CODE_ERROR://获取短信验证码错误：同一号码一分钟只能发一次
                        case RestException.SEND_VOICE_VERIFY_CODE_ERROR:// 电话已拨出，请在{30s}后获取
                        case RestException.GET_VERIFY_CODE_ERROR_TEN_TIMES://获取短信验证码错误：同一号码两小时只能发十次
                        case RestException.PAY_LIMIT_ERROR://支付验限额错误
                        case RestException.PAY_MSGCODE_ERROR://支付验证码错误
                        case RestException.PAY_PWD_ERROR://支付密码错误
                            //房源小区纠错提示错误码
                        case RestException.HOUSE_INFO_ERROR_CORRECTION1:
                        case RestException.HOUSE_INFO_ERROR_CORRECTION2:
                        case RestException.HOUSE_INFO_ERROR_CORRECTION3:
                        case RestException.PLAT_INFO_ERROR_CORRECTION4:
                        case RestException.PLAT_INFO_ERROR_CORRECTION5:
                        case RestException.PLAT_INFO_ERROR_CORRECTION6:
                            String parameter = JSON.toJSONString(jsonObject);
                            LogUtil.i(TAG, "onJsonSuccess" + parameter);
                            webViewLayotSoftReference.get().loadJs(js.append(callBack).append("" + "(").append
                                    (parameter).append(")").toString());
                            break;
                        case RestException.LOGOUT_ERROR:
                            if (null != webViewLayotSoftReference.get() && webViewLayotSoftReference.get().getWRContext() != null) {
                                doForceExit(response, webViewLayotSoftReference.get().getWRContext());
                            }
                            break;
                        default:
                            onFailInfo(response, response.getMessage());
                            break;
                    }
                } catch (Exception e) {
                    LogUtil.e(e.toString());
                }
            }
        }

        @Override
        public void onFailInfo(Response response, String
                errorInfo) {
            super.onFailInfo(response, errorInfo);
            if (null != webViewLayotSoftReference.get()) {
                ToastUtil.showInCenter(errorInfo);
                String parameter;
                if (null != response) {
                    parameter = JSON.toJSONString(response);
                } else {
                    parameter = "{}";
                }
                LogUtil.i(TAG, "onFailInfo " + parameter);
                webViewLayotSoftReference.get().loadJs(js.append(callBack).append("" +
                        "(").append
                        (parameter).append(")").toString());
            }
        }
    }

}
