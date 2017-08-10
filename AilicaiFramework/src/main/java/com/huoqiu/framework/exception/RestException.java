/**
 *
 */
package com.huoqiu.framework.exception;

/**
 * @author Administrator
 */
public class RestException extends RuntimeException {
    public static final int NO_ERROR = 0;
    public static final int SYSTEM_ERROR = 1;
    public static final int INVALID_TOKEN = 2;
    public static final int SYNC_TIME_ERROR = 100001;//时间同步错误
    /**
     * 部分接口需要登录才可以访问，否则返回该错误码
     * 为空时底层实现将不会弹出Toast信息，重新打开登录界面处理时弹出错误信息
     */
    public static final int LOGOUT_ERROR = 110001;
    public static final int PAY_LIMIT_ERROR = 800001;//您的实付金额已超过该银行卡的单笔支付限额
    public static final int PAY_PWD_ERROR = 800002;
    public static final int PAY_MSGCODE_ERROR = 800003;
    public static final int GET_VERIFY_CODE_ERROR = 200016;
    public static final int GET_VERIFY_CODE_ERROR_TEN_TIMES = 200018;
    public static final int SEND_VOICE_VERIFY_CODE_ERROR = 200039;//电话已拨出，请在{30s}后获取
    public static final int ATTENTION_EXCEED_THE_UPPER_LIMIT = 200036;
    //房源小区纠错错误码
    public static final int HOUSE_INFO_ERROR_CORRECTION1 = 900001;
    public static final int HOUSE_INFO_ERROR_CORRECTION2 = 900002;
    public static final int HOUSE_INFO_ERROR_CORRECTION3 = 900003;
    public static final int PLAT_INFO_ERROR_CORRECTION4 = 900004;
    public static final int PLAT_INFO_ERROR_CORRECTION5 = 900005;
    public static final int PLAT_INFO_ERROR_CORRECTION6 = 900006;
    //推荐经纪人提交
    public static final int LOGIN_RECOMMEND_AGENT_YOURS = 200050;
    public static final int LOGIN_RECOMMEND_AGENT_NOREGISTER = 200051;
    public static final int LOGIN_RECOMMEND_AGENT_SINGLE = 200052;
    //强制更新
    public static final int VERSION_UPDATE_STRONG = 999999;
    //系统维护
    public static final int SYSTEM_MAINTANCE = 999998;
    private static final long serialVersionUID = 1L;
    private int errorCode;

    /**
     * @param detailMessage
     */
    public RestException(String detailMessage, int errorCode) {
        super(detailMessage);
        setErrorCode(errorCode);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
