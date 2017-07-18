package com.ailicai.app.common.logCollect;

import android.text.TextUtils;

import com.ailicai.app.MyApplication;
import com.ailicai.app.common.constants.CommonTag;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.MD5;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.ui.login.UserInfo;
import com.huoqiu.framework.app.AppConfig;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by shejian on 2015/4/8.
 * modify by duo.chen
 */
public class EventLog {

    private static CollectEventLogInfo collectEventLogInfo;

    public static CollectEventLogInfo getInstance() {
        if (null == collectEventLogInfo) {
            collectEventLogInfo = new CollectEventLogInfo();
        }
        return collectEventLogInfo;
    }

    public static class CollectEventLogInfo {

        String uuid;
        String imei;
        String place;
        String cityId;
        String usid;
        String bussiness;
        String houseCode;
        String subwayLine;
        String subwayStation;
        String districtRegion;
        String districtArea;
        String rental;
        String bedroom;
        String searchKeyword;
        String clickKeyword;
        String clickType;
        String actionType;
        String actionValue;
        String actionLocation;
        String recommandList;
        String platform;
        String cid;
        String est;
        String ver;
        String lct;
        String tips;
        String number;
        //  标签类型type
        // 0 默认（直接搜索）
        // 1租房标签
        // 2.二手房标签
        // 3.新房标签
        // 4.热门标签
        // 5.历史标签
        // 6.直接点击tips
        // 8.品牌公寓标签
        String type;
        //V6.8新增通用埋点字段
        String pv_id;
        String model;

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getUsid() {
            return usid;
        }

        public void setUsid(String usid) {
            this.usid = usid;
        }

       /* public String getBussiness() {
            return bussiness;
        }

        public void setBussiness(String bussiness) {
            this.bussiness = bussiness;
        }*/

        public String getHouseCode() {
            return houseCode;
        }

        public void setHouseCode(String houseCode) {
            this.houseCode = houseCode;
        }

        public String getSubwayLine() {
            return subwayLine;
        }

        public void setSubwayLine(String subwayLine) {
            this.subwayLine = subwayLine;
        }

        public String getSubwayStation() {
            return subwayStation;
        }

        public void setSubwayStation(String subwayStation) {
            this.subwayStation = subwayStation;
        }

        public String getDistrictRegion() {
            return districtRegion;
        }

        public void setDistrictRegion(String districtRegion) {
            this.districtRegion = districtRegion;
        }

        public String getDistrictArea() {
            return districtArea;
        }

        public void setDistrictArea(String districtArea) {
            this.districtArea = districtArea;
        }

        public String getRental() {
            return rental;
        }

        public void setRental(String rental) {
            this.rental = rental;
        }

        public String getBedroom() {
            return bedroom;
        }

        public void setBedroom(String bedroom) {
            this.bedroom = bedroom;
        }

        public String getSearchKeyword() {
            return searchKeyword;
        }

        public void setSearchKeyword(String searchKeyword) {
            this.searchKeyword = searchKeyword;
        }

        public String getClickKeyword() {
            return clickKeyword;
        }

        public void setClickKeyword(String clickKeyword) {
            this.clickKeyword = clickKeyword;
        }

        public String getClickType() {
            return clickType;
        }

        public void setClickType(String clickType) {
            this.clickType = clickType;
        }

        public String getActionType() {
            return actionType;
        }

        public void setActionType(String actionType) {
            this.actionType = actionType;
        }

        public String getActionValue() {
            return actionValue;
        }

        public void setActionValue(String actionValue) {
            this.actionValue = actionValue;
        }

        public String getActionLocation() {
            return actionLocation;
        }

        public void setActionLocation(String actionLocation) {
            this.actionLocation = actionLocation;
        }

        public String getRecommandList() {
            return recommandList;
        }

        public void setRecommandList(String recommandList) {
            this.recommandList = recommandList;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getEst() {
            return est;
        }

        public void setEst(String est) {
            this.est = est;
        }

        public void setVer(String ver) {
            this.ver = ver;
        }

        public String getLct() {
            return lct;
        }

        public void setLct(String lct) {
            this.lct = lct;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPv_id() {
            return pv_id;
        }

        public void setPv_id(String pv_id) {
            this.pv_id = pv_id;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        /**
         * 上传事件数据
         */
        public void upSearchInfo() {

            AppendBaseInfo();
            appendLctAndLocationInfo();

            String infoStr =
                    (TextUtils.isEmpty(uuid) ? "uid=" + "ANONYMOUS_USER" + "&" : "uid=" + uuid + "&") +
                            (TextUtils.isEmpty(imei) ? "" : "imei=" + imei + "&") +
                            (TextUtils.isEmpty(place) ? "" : "pl=" + place + "&") +
                            (TextUtils.isEmpty(cityId) ? "" : "ct=" + cityId + "&") +
                            (TextUtils.isEmpty(usid) ? "" : "usid=" + usid + "&") +
                            (TextUtils.isEmpty(imei) ? "" : "adid=" + MD5.getMessageDigest(imei.getBytes()) + "&") +
                            (TextUtils.isEmpty(platform) ? "" : "pf=" + platform + "&") +
                            (TextUtils.isEmpty(bussiness) ? "" : "vtp=" + bussiness + "&") +
                            (TextUtils.isEmpty(houseCode) ? "" : "hos=" + houseCode + "&") +
                            (TextUtils.isEmpty(subwayLine) ? "" : "sw_le=" + subwayLine + "&") +
                            (TextUtils.isEmpty(subwayStation) ? "" : "sw_st=" + subwayStation + "&") +
                            (TextUtils.isEmpty(districtRegion) ? "" : "dt_rg=" + districtRegion + "&") +
                            (TextUtils.isEmpty(districtArea) ? "" : "dt_ae=" + districtArea + "&") +
                            (TextUtils.isEmpty(rental) ? "" : "ret=" + rental + "&") +
                            (TextUtils.isEmpty(bedroom) ? "" : "bdr=" + bedroom + "&") +
                            (TextUtils.isEmpty(searchKeyword) ? "" : "sk=" + searchKeyword + "&") +
                            (TextUtils.isEmpty(clickKeyword) ? "" : "ck=" + clickKeyword + "&") +
                            (TextUtils.isEmpty(clickType) ? "" : "clt=" + clickType + "&") +
                            (TextUtils.isEmpty(actionType) ? "" : "act_k=" + actionType + "&") +
                            (TextUtils.isEmpty(actionValue) ? "" : "act_v=" + actionValue + "&") +
                            (TextUtils.isEmpty(actionLocation) ? "" : "act_l=" + actionLocation + "&") +
                            (TextUtils.isEmpty(recommandList) ? "" : "rem=" + recommandList + "&") +
                            (TextUtils.isEmpty(cid) ? "" : "cid=" + cid + "&") +
                            (TextUtils.isEmpty(tips) ? "" : "tips=" + tips + "&") +
                            (TextUtils.isEmpty(number) ? "" : "num=" + number + "&") +
                            (TextUtils.isEmpty(est) ? "" : "est=" + est + "&") +
                            "ver=" + AppConfig.versionCode + "&" +
                            (TextUtils.isEmpty(lct) ? "" : "lct=" + lct+ "&")+
                            (TextUtils.isEmpty(type) ? "" : "type=" + type+ "&")+
                            (TextUtils.isEmpty(pv_id) ? "": "pv_id=" + pv_id + "&") +
                            (TextUtils.isEmpty(model) ? "" : "model=" + model);
            if (infoStr.endsWith("&")) {
                infoStr = infoStr.substring(0, infoStr.length() - 1);
            }
            android.util.Log.i("EventLog", infoStr);
            try {
                publishInfoToServices(URLEncoder.encode(infoStr, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        public void updateJsBridgeEventInfo(HashMap map) {

            EventLog.getInstance().init();

            if (map.containsKey("vtp")) {
//                setBussiness(String.valueOf(map.get("vtp")));
            }

            AppendBaseInfo();
            appendLctAndLocationInfo();

            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append(TextUtils.isEmpty(uuid) ? "uid=" + "ANONYMOUS_USER" + "&" : "uid=" +
                    uuid + "&")
                    .append((TextUtils.isEmpty(imei) ? "" : "imei=" + imei + "&"))
                    .append(TextUtils.isEmpty(place) ? "" : "pl=" + place + "&")
                    .append(TextUtils.isEmpty(cityId) ? "" : "ct=" + cityId + "&")
                    .append(TextUtils.isEmpty(usid) ? "" : "usid=" + usid + "&")
                    .append(TextUtils.isEmpty(platform) ? "" : "pf=" + platform + "&")
                    .append(TextUtils.isEmpty(bussiness) ? "" : "vtp=" + bussiness + "&");
            //LogUtil.d("debuglog", "之前" + stringBuffer.toString());
            for (Object key : map.keySet()) {
                if (!key.equals("pf") && !key.equals("uid") && !key.equals("vtp")) {
                    stringBuffer.append(key).append("=").append(String.valueOf(map.get(key)))
                            .append("&");
                }
            }

            String infoStr = stringBuffer.toString();
            if (infoStr.endsWith("&")) {
                infoStr = infoStr.substring(0, infoStr.length() - 1);
            }
            android.util.Log.i("EventLog", " js bridge" + infoStr);
        //    LogUtil.d("debuglog", "之后" + stringBuffer.toString());
            try {
                publishInfoToServices(URLEncoder.encode(infoStr, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        /** 这两个参数值其实一样的，这边做一个平衡，伟大的产品经理 **/
        private void appendLctAndLocationInfo() {
            if(TextUtils.isEmpty(lct) && TextUtils.isEmpty(actionLocation)) {
                lct = "other";
                actionLocation = "other";
            } else if(TextUtils.isEmpty(lct)  && !TextUtils.isEmpty(actionLocation)) {
                lct = actionLocation;
            } else if(!TextUtils.isEmpty(lct)  && TextUtils.isEmpty(actionLocation)) {
                actionLocation  = lct;
            }
        }

        /**
         * append base user info
         * such as uuid place city usid
         */
        private void AppendBaseInfo() {

            uuid = DeviceUtil.getIWJWUUID();
            imei = DeviceUtil.getIMEI();
            String latitude = MyPreference.getInstance().read(CommonTag.MAP_LATITUDE, "");
            String longitude = MyPreference.getInstance().read(CommonTag.MAP_LONGITUDE, "");
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                place = latitude + "," + longitude;
            }

            /*if (TextUtils.isEmpty(getBussiness())) {
                switch (MyApplication.getInstance().getCurrentBusinessType()) {
                    case MyApplication.BUSINESS_RENTAL:
                        setBussiness("1");
                        break;
                    case MyApplication.BUSINESS_SELL:
                        setBussiness("2");
                        break;
                    case MyApplication.BUSINESS_NEWHOUSE:
                        setBussiness("3");
                        break;
                }
            }*/
            cityId = AppConfig.cityId;
            usid = UserInfo.getInstance().getUserId() + "";
            platform = "android-app";
            pv_id = PVIDHandler.getPVID()+"";
            model = DeviceUtil.getBrandModel();

        }

        public void init() {
            uuid = null;
            imei = null;
            place = null;
            cityId = null;
            usid = null;
            platform = null;
            bussiness = null;
            houseCode = null;
            subwayLine = null;
            subwayStation = null;
            districtRegion = null;
            districtArea = null;
            rental = null;
            bedroom = null;
            searchKeyword = null;
            clickKeyword = null;
            clickType = null;
            actionType = null;
            actionValue = null;
            actionLocation = null;
            recommandList = null;
            cid = null;
            est = null;
            lct = null;
            tips = null;
            pv_id = null;
        }
    }

    /**
     * 发送统计数据
     *
     * @param str
     */
    private static void publishInfoToServices(String str) {
        LogCollectUtil.pushLog(str);
    }

    public static void upEventLog(String key, String value) {
        try {
            EventLog.getInstance().init();
            EventLog.getInstance().setActionType(key);
            EventLog.getInstance().setActionValue(value);
            EventLog.getInstance().upSearchInfo();
        } catch (Throwable e) {
            LogUtil.i(e.toString());
        }
    }


    /*public static void upEventLog(String key, String value, int business) {
        try {
            EventLog.getInstance().init();
            EventLog.getInstance().setActionType(key);
            EventLog.getInstance().setActionValue(value);
//            EventLog.getInstance().setBussiness(String.valueOf(business));
            EventLog.getInstance().upSearchInfo();
        } catch (Throwable e) {
            LogUtil.i(e.toString());
        }
    }*/

    public static void upEventLog(String key, String value, String location) {
        try {
            EventLog.getInstance().init();
            EventLog.getInstance().setActionType(key);
            EventLog.getInstance().setActionValue(value);
            EventLog.getInstance().setActionLocation(location);
            EventLog.getInstance().upSearchInfo();
        } catch (Throwable e) {
            LogUtil.i(e.toString());
        }
    }

   /* public static void upEventLog(String key, String value, String location, int business) {
        try {
            EventLog.getInstance().init();
            EventLog.getInstance().setActionType(key);
            EventLog.getInstance().setActionValue(value);
            EventLog.getInstance().setActionLocation(location);
            EventLog.getInstance().upSearchInfo();
        } catch (Throwable e) {
            LogUtil.i(e.toString());
        }
    }*/

    /**
     * 为了防止连接太慢，延迟一秒发送数据
     *
     * @param location
     */
    public static void upEventLctLog(final String location) {
        MyApplication.getInstance().getUiHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    EventLog.getInstance().init();
                    EventLog.getInstance().setLct(location);
                    EventLog.getInstance().upSearchInfo();
                } catch (Throwable e) {
                    LogUtil.i(e.toString());
                }
            }
        }, 1000);
    }

    /**
     * @param location
     */
    public static void upEventLctLog(final String location, final String pv_id, final String act_l) {
        MyApplication.getInstance().getUiHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    EventLog.getInstance().init();
                    EventLog.getInstance().setLct(location);
                    EventLog.getInstance().setPv_id(pv_id);
                    EventLog.getInstance().setActionLocation(act_l);
                    EventLog.getInstance().upSearchInfo();
                } catch (Throwable e) {
                    LogUtil.i(e.toString());
                }
            }
        }, 1000);
    }

}
