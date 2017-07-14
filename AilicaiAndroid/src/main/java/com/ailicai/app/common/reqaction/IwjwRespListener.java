package com.ailicai.app.common.reqaction;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.ailicai.app.ApplicationPresenter;
import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.reporthttp.IwErrorLogSender;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.version.VersionInterface;
import com.ailicai.app.common.version.VersionUtil;
import com.ailicai.app.setting.DeBugLogActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.huoqiu.framework.backstack.BackOpFragmentActivity;
import com.huoqiu.framework.commhttp.BaseRequest;
import com.huoqiu.framework.commhttp.JsonHttpResponseListener;
import com.huoqiu.framework.commhttp.JsonResponsePreProcessor;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.util.LogUtil;

import java.lang.ref.WeakReference;

/**
 * Created by Jer on 2015/7/24.
 */
public abstract class IwjwRespListener<T> extends JsonHttpResponseListener<T> implements JsonResponsePreProcessor<T>, VersionInterface {

    //private boolean hasCheckNewVersion = false;
    public static final String HASCHECKNEWVERSION = "HAS_CHECK_NEW_VERSION";
    //当前URL
    String mUrl;
    private WeakReference weakReferenceContext;
    private WeakReference weakReferenceFragment;
    private boolean isFragmentReq;

    public IwjwRespListener() {
        super();
        setResponsePreProcessor(this);
    }

    public IwjwRespListener(Fragment fragment) {
        super();
        setFragment(fragment);
        setResponsePreProcessor(this);
    }

    public IwjwRespListener(Context mContext) {
        super();
        setContext(mContext);
        setResponsePreProcessor(this);
    }

    /**
     * 唯一账号强制退出处理
     * 只执行一次弹出错误信息及推出登录界面
     */
    public static void doForceExit(Response response, Activity act) {
        if (act != null) {
            toReLogin(act, response);
        }
    }

    protected static void toReLogin(Activity activity, Response res) {
        //  RestException restException = new RestException(res.getMessage(), res.getErrorCode());
        if (activity != null && activity instanceof BackOpFragmentActivity) {
            ((BackOpFragmentActivity) activity).doLogout(res.getMessage());
        }
    }

    public void setContext(Context mContext) {
        weakReferenceContext = new WeakReference<>(mContext);
        isFragmentReq = false;
    }

    public void setFragment(Fragment fragment) {
        weakReferenceFragment = new WeakReference<>(fragment);
        isFragmentReq = true;
    }

    public <V> V getWRContext() {
        if (weakReferenceContext != null) {
            return (V) weakReferenceContext.get();
        } else {
            return null;
        }
    }

    public <V> V getWRFragment() {
        if (weakReferenceFragment != null) {
            return (V) weakReferenceFragment.get();
        } else {
            return null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 请求成功的预处理代码块
     *
     * @param response
     * @return
     */
    @Override
    public boolean preProcess(T response) {
        if (isDetached()) {
            return false;
        }
        mUrl = getReqUrlLast();
        //服务器端统计请求情况
        //在parseNetworkResponse已设置过
//        getReportRequest().setResponseTime("" + (System.currentTimeMillis() - getStartTime()));
        IwErrorLogSender.sendReport2(getReportRequest());

        if (response instanceof Response) {
            Response responseTo = (Response) response;
            int errorCode = responseTo.getErrorCode();
            switch (errorCode) {
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
                case RestException.LOGIN_RECOMMEND_AGENT_YOURS:
                case RestException.LOGIN_RECOMMEND_AGENT_NOREGISTER:
                case RestException.LOGIN_RECOMMEND_AGENT_SINGLE:
                    LogUtil.d("IwjwRespListener", "请求接口" + mUrl);
                    return true;
                case RestException.VERSION_UPDATE_STRONG:
                    //当errorCode=999999的时候，代表当前版本小于强更的版本，需要客户端进行升级
                    if (isFragmentReq) {
                        Fragment fragment = getWRFragment();
                        if (null != fragment) {
                            Activity activity = fragment.getActivity();
                            if (null != activity) {
                                if (!MyPreference.getInstance().read(HASCHECKNEWVERSION, false)) {
                                    VersionUtil.check(this, activity, ((Response) response).getUpdateInfo());
                                }
                            }
                        }
                    } else {
                        Context context = getWRContext();
                        if (null != context && context instanceof Activity) {
                            if (!MyPreference.getInstance().read(HASCHECKNEWVERSION, false)) {
                                VersionUtil.check(this, (Activity) context, ((Response) response).getUpdateInfo());
                            }
                        }
                    }
                    return true;
                case RestException.LOGOUT_ERROR:
                    if (isFragmentReq) {
                        Fragment fragment = getWRFragment();
                        if (null != fragment) {
                            Activity activity = fragment.getActivity();
                            if (null != activity) {
                                doForceExit((Response) response, activity);
                            }
                        }
                    } else {
                        Context context = getWRContext();
                        if (null != context && context instanceof Activity) {
                            doForceExit((Response) response, (Activity) context);
                        }
                    }
                    return false;
                case RestException.SYNC_TIME_ERROR:
                    DeBugLogActivity.saveOtheLog("出现一次100001");
                    ApplicationPresenter.syncTime(null);
                case RestException.SYSTEM_ERROR:
                case RestException.INVALID_TOKEN:
                case 99:
                case -1:
                default:
                    onFailInfo(responseTo, responseTo.getMessage());
                    getReportRequest().setLogLevel("warn");
                    getReportRequest().setErrorCode("" + errorCode);
                    getReportRequest().setErrorMessage("请求正常，接口报错:" + responseTo.getMessage());
                    // IwErrorLogSender.sendReqErrorlogObj(getReportRequest());基本都是正常数据错误
                    return false;
            }
        }
        return true;
    }

    public boolean isDetached() {
        if (isFragmentReq) {
            Fragment fragment = getWRFragment();
            if (fragment == null) {
                return true;
            }
            if (fragment.isDetached()) {
                return true;
            }
            return fragment.getActivity() == null;
        } else {
            Context mContext = getWRContext();
            if (mContext == null) {
                return true;
            }
            if (mContext instanceof Activity) {
                Activity activity = (Activity) mContext;
                if (activity.isFinishing()) {
                    return true;
                }
            }
            return false;
        }
    }


    String getReqUrlLast() {
        return getRequrl().substring(getRequrl().lastIndexOf("/"));
    }

    /**
     * 请求失败的预处理代码块
     * <p>
     * //SocketTimeout ,ConnectTimeoutException:TimeoutError       var12     var13
     * //MalformedURLException:  MalformedURLException                        var14
     * //IOException: NoConnectionError,NetworkError,401,403ServerError ,AuthFailureError    var15
     *
     * @param volleyError
     */
    @Override
    public void errorPreprocess(VolleyError volleyError) {
        if (isDetached()) {
            return;
        }
        mUrl = getReqUrlLast();
        boolean isNeedReport = false;
        String errorInfo = "";
        String errorlogmsg = "";
        if (volleyError instanceof TimeoutError) {
            isNeedReport = true;
            getReportRequest().setResult("2");
            errorInfo = CommonUtil.getResourceStr(R.string.timeout_Error);
            errorlogmsg = "请求超时";
        } else if (volleyError instanceof NoConnectionError) {
            errorInfo = CommonUtil.getResourceStr(R.string.no_connection_error);
            errorlogmsg = "网络没有连接";
        } else if (volleyError instanceof NetworkError) {
            errorInfo = CommonUtil.getResourceStr(R.string.network_exception);
            errorlogmsg = "网络异常";
        } else if (volleyError instanceof ServerError) {
            isNeedReport = true;
            getReportRequest().setResult("3");
            errorInfo = CommonUtil.getResourceStr(R.string.service_exception);
            errorlogmsg = "服务器异常";
        } else if (volleyError instanceof AuthFailureError) {
            errorInfo = CommonUtil.getResourceStr(R.string.network_exception);
            errorlogmsg = "认证错误";
        }
        LogUtil.e("VolleyError", errorlogmsg);
        LogUtil.d("shejianTime", mUrl + ":TimeMs:" + (System.currentTimeMillis() - getStartTime()));
        if (isNeedReport) {
            //服务器端统计请求情况
            getReportRequest().setResponseTime("" + (System.currentTimeMillis() - getStartTime()));
            getReportRequest().setErrorMessage(volleyError.getMessage());
            if (volleyError.networkResponse != null) {
                getReportRequest().setErrorCode(volleyError.networkResponse.statusCode + "");
            }
            IwErrorLogSender.sendReport2(getReportRequest());
            if (!TextUtils.isEmpty(getRequrl())) {
                String errorUrl = getRequrl().substring(getRequrl().lastIndexOf("/"));
                getReportRequest().setErrorMessage(errorUrl + "  " + errorlogmsg);
                IwErrorLogSender.sendReqErrorlogObj(getReportRequest());//基本都是正常数据错误
            }
        }
        onFailInfo(null, errorInfo);
    }

    public void onFailInfo(String errorInfo) {
    }

    //如果想要获取Response做处理

    /**
     * 如果调用onFailInfo(String errorInfo)，千万不要写super.onFailInfo
     *
     * @param response
     * @param errorInfo
     */
    public void onFailInfo(Response response, String errorInfo) {
        onFailInfo(errorInfo);
    }

    @Override
    public void parseNetworkResponse(BaseRequest request) {
        if (request instanceof IwjwHttpReq) {
            IwjwHttpReq req = (IwjwHttpReq) request;
            //设置接口请求时间
            getReportRequest().setResponseTime(String.valueOf(System.currentTimeMillis() - req.getRequestStartTime()));
            LogUtil.d("IwErrorLogSender1", "统计地址是" + reportRequest.getRestUrl() + ";时间=" + (System.currentTimeMillis() - req.getRequestStartTime()));
        }
        super.parseNetworkResponse(request);
    }

    /*
     *====================================================
     * 版本强制更新回调方法
     *====================================================
     */
    @Override
    public void remindPoint() {

    }

    @Override
    public void checkStart() {

    }

    @Override
    public void checkSuccess() {
        //MyPreference.getInstance().write(HASCHECKNEWVERSION, true);
        //hasCheckNewVersion = true;
    }

    @Override
    public void checkFailed(String message) {

    }

    @Override
    public void checkLatest(String version) {
        //ToastUtil.show(MyApplication.getInstance(), "已是最新版本");
    }

    @Override
    public boolean ignorePop() {
        return false;
    }
}
