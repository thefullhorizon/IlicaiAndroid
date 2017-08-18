package com.ailicai.app.common.constants;

import android.os.Build;

import com.ailicai.app.BuildConfig;

/**
 * 全局tag
 */
public class CommonTag {
    public static final String IS_FIREST_START = "isFirstStart" + BuildConfig.VERSION_CODE;//是否第一次启动
    public static final String BUY_CARD_DATA = "buy_card_data";
    public static final String COOKIE = "Cookie";
    public static final String CUR_APP_VERSION = "cur_app_version";
    public static final String INTENT_PAGE = "intent_page";
    public static final String BIND_TYPE = "bindType";
    public static final String SESSION_ID = "sessionId";
    public static final String ACCOUNT_DATA = "accountData";
    public static final String GLOBAL_BUNDLE = "GLOBAL_BUNDLE";
    /*public static final String SUPPORT_SALE = "hasSale";
        public static final String SUPPORT_RENT = "hasRent";*/
    public final static String PERF_FIRST_INSTALL_LAUNCH = "perf_firsr_install_launch";
    public final static String SLIDE_FINISH_KEY = "slide_finish_key";
    public final static String SHOW_IWJW_FINANCE_KEY_2 = "show_iwjw_finance_key_2";
    public final static String SERVER_PORT = "server_port";
    public final static String SERVER_IP = "server_ip";
    public final static String SEX_KEY = "check_sex";
    public final static String NAME_KEY = "check_name";
    public final static String APPOINT_DATE_KEY = "check_date";
    public final static String APPOINT_TIME_KEY = "check_time";
    public final static String APPOINT_DATE_TIME_STR_KEY = "check_date_time_str";
    public static final String SEARCH_AREA_SAVE_KEY = "search_areas_save_key";
    public static final String FEED_BACK_STRING = "feedback";
    public static final String POP_SHOWED = "pop_showed";
    public static final String MAP_LATITUDE = "latitude";
    public static final String MAP_LONGITUDE = "longitude";
    public static final String TARGET_POINTLAT = "targetPointLat";
    public static final String TARGET_POINTLNG = "targetPointLng";
    public static final String TARGET_LEVEL = "targetLevel";
    public static final String IS_TARGET = "isTarget";
    public static final String TRUST_AGREEMENT_TITLE = "trust_agreement_title";
    public static final String TRUST_AGREEMENT_URL = "trust_agreement_url";
    public static final String MINE_TRUST_CLICK = "mine_trust_click";
    public static final String IMAGE_URL = "imageUrl";
    public static final String RENT_OR_SELL = "rent_or_sell";
    public static final String JUMP_ESTATE_PRICE = "jumpEstatePrice";
    public static final String FLATHOUSE_TYPE = "flatHouseType";
    public static final String RENT_TYPE = "rentType";
    public static final String HOUSE_BASE_INFO = "house_base_info";
    public static final String HOUSE_ID = "houseid";
    public static final String PAYMENT = "payment";//打开返利券卡券详情页的标识
    public static final String REMID = "remId";
    public static final String ESATE_ID = "esateId";
    public static final String PROPERTY_ID = "propertyId";
    public static final String PERSONAL_USER_ID = "userId";
    public static final String PERSONAL_USER_NAME = "userName";
    public static final String PERSONAL_BANK_NAME = "personal_bank_name";
    public static final String PERSONAL_BANKCARDTAILNO = "bankcardtailno";
    public static final String PERSONAL_USER_SEX = "userSex";
    public static final String PERSONAL_USER_PHONE = "userPhone";
    public static final String PERSONAL_USER_ISREALNAMEVERIFY = "isRealNameVerify";
    public static final String PERSONAL_USER_IDCARDNUMBER = "idCardNumber";

    public final static String VERSION_SHOW_KEY = "3_1_show_key";
    public final static String MAP_OR_LIST = "map_or_list";

    public final static String PREF_HOME_POP_VERSION = "prefHomePopVersion";

    public static final String PLAY_VIDEO_COUNT = "play_video_count";//未登录有9次限制

    public static final int PLAY_VIDEO_COUNT_MAX = 9;//未登录有9次限制

    public final static String PREF_PAY_RENT_HAS_SHOW = "prefPayRentHasShow"; // 租房页面交房租的弹窗是否显示过

    public final static String PREF_LAST_SHOW_MAP = "PREF_LAST_SHOW_MAP";

    public final static String PREF_BRAND_LAST_SHOW_MAP = "PREF_BRAND_LAST_SHOW_MAP";

    public final static String PREF_LAST_MAP_LOCATION = "PREF_LAST_MAP_LOCATION";

    public final static String PREF_LAST_MAP_POINT = "PREF_LAST_MAP_POINT";

    public final static String CUSTOME_H5HOST = "custome_h5host";//自定义的H5 域名

    public static final String MAP_LOCATION_CITY_NAME = "locationCityName"; // 定位的城市名

    public static final String IS_DRAW_MAP_FUNCTION_TIPED = "drawMapFunctionTip"; // 画圈找房文字提示显示过

    public static final String IS_DRAW_MAP_GUIDE_SHOWN = "drawMapGuideShown"; // 画圈找房引导显示过

    public static final String IS_HOUSE_LIST_NEARBY_FUNCTION_TIPED = "IS_HOUSE_LIST_NEARBY_FUNCTION_TIPED"; // 列表页附近功能文字提示显示过

    public final static String PREF_PAY_IS_READ_LIST = "PREF_PAY_IS_READ_LIST"; // 列表页已读房源

    public final static String PREF_IS_BRANDFLAT_AD_DISPLAYED = "PREF_IS_BRANDFLAT_AD_DISPLAYED"; // 大首页品牌公寓弹窗是否弹出过

    public static final String SEARCH_TYPE = "search_type";
}
