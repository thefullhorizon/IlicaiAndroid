package com.ailicai.app.common.push.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import java.io.Serializable;

/**
 * Created by duo.chen on 2015/4/28.
 */
public class PushMessage implements Serializable {

    private static final long serialVersionUID = 5558698281525960822L;

    public static final String PUSHMESSAGE = "pushmessage";

    public static final int INITTYPE                     = 0;
    public static final int REMINDTYPE                   = INITTYPE + 1;//提醒
    public static final int INFOTYPE                     = INITTYPE + 2;//资讯，公告
    public static final int DYNAMICTYPE                  = INITTYPE + 3;//动态消息
    public static final int ACTIVITYTYPE                 = INITTYPE + 4;//活动消息
    public static final int EASETYPE                     = INITTYPE + 5;//环信消息

    public static final int REMINDTYPESCHEDULE           = INITTYPE + 1;
    public static final int REMINDTYPECOMMISSON          = INITTYPE + 2;
    public static final int REMINDTYPEIWJWORDER          = INITTYPE + 3;
    public static final int REMINDTYPEFANGGUANFANGORDER  = INITTYPE + 4;
    public static final int REMINDTYPEFANGGUANFANGBILL   = INITTYPE + 5;
    public static final int REMINDTYPECOMMISSONUPDATE    = INITTYPE + 9;
    public static final int REMINDTYPECOMPLAINSTATUS     = INITTYPE + 10;
    public static final int REMINDTYPENEWVOUCHER         = INITTYPE + 11;//现金券通知
    public static final int REMINDTYPEIWJWBILL           = INITTYPE + 12;//爱屋吉屋账单

    public static final int NOTICETYPETOWEBVIEW          = INITTYPE + 13; //资讯打开指定URL或资讯内容
    public static final int NOTICETYPETOFINANCE          = INITTYPE + 14; //14:资讯打开打开爱理财
    public static final int NOTICETYPETONOTICELIST       = INITTYPE + 15; //15:资讯打开资讯列表

    public static final int DYNAMICTYPEHOUSING           = INITTYPE + 16; //16 关注房源动态
    public static final int DYNAMICTYPEMOULDE            = INITTYPE + 17; //17 关注模块动态

    public static final int REMINDTYPECOUPONBANNER       = INITTYPE + 18; //18 卡券下发
    public static final int REMINDTYPEJUJIAN             = INITTYPE + 19; //19 二手房订单签居间
    public static final int REMINDTYPEWANGQIAN           = INITTYPE + 20; //20 二手房订单网签
    public static final int REMINDTYPEGUOHU              = INITTYPE + 21; //21 二手房订单过户
    public static final int REMINDTYPEPIDAI              = INITTYPE + 22; //22 二手房订单贷款批贷
    public static final int REMINDTYPERESERVESUCCESS     = INITTYPE + 23; //23 爱理财加息券-预约自动购买-成功
    public static final int REMINDTYPERESERVEFAIL        = INITTYPE + 24; //24 爱理财加息券-预约自动购买-失败
    public static final int REMINDTYPELIUBIAO            = INITTYPE + 25; //25 爱理财加息券-房产宝项目流标
    public static final int REMINDTYPEHUANKUAN           = INITTYPE + 26; //26 爱理财加息券-房产宝还款
    public static final int REMINDTYPEZHUANRANG          = INITTYPE + 27; //27 转让状态从转让中变更为已结束
    public static final int REMINDTYPEMUZIJIXI           = INITTYPE + 28; //28 募资完成开始计息提醒
    public static final int REMINDTYPETIYANJI            = INITTYPE + 29; //29 代表是体验金
    public static final int REMINDTYPEPUZUHETONG         = INITTYPE + 30; //30 普租合同
    public static final int NOTICETYPEHOUSEDETAIL        = INITTYPE + 31; //31 资讯打开房源详情页
    public static final int REMINDTYPETYJTOFIHOME        = INITTYPE + 32; //32 体验金到爱理财首页
    public static final int REMINDTYPEPUZHUQS            = INITTYPE + 33; //33 普租签署合同
    public static final int REMINDTYPEAGENTPINGJIA       = INITTYPE + 34; //34 普租签署合同
    public static final int REMINDTYPEIW400              = INITTYPE + 35; //35 iw400工单提醒
    public static final int REMINDTYPEBANKRECEIPTFAIL    = INITTYPE + 36; //36 钱包出款失败
    public static final int REMINDTYPEFANLISHENHE        = INITTYPE + 37; //37 返利券审核成功，审核失败，购买成功
    public static final int REMINDTYPEBRANDFLATYUEKAN    = INITTYPE + 38; //38 成功提交看房预约，店家跳转到品牌公寓房源详情
    public static final int REMINDTYPECUSTOMPUSH         = INITTYPE + 39; //39 专题页个性推送
    public static final int REMINDTYPEDOWNPRICELIST      = INITTYPE + 40; //40 降价榜单
    public static final int REMINDTYPEAGENDAAPPRAISE     = INITTYPE + 41; //41 经纪人评价
    /**
     * "title" : "消息标题"
     * "message" : "消息内容",
     * "msgType" : "消息类型",
     * "optional" : "附加内容"
     * "uri" : "callback uri"
     */
    private String title = "";
    private String message = "";
    private int msgType;
    private int noticeId;
    private String optional;
    private Optional optionalObj;
    private String payload;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setOptionalObj(Optional optionalObj){
        this.optionalObj = optionalObj;
    }
    public Optional getOptional() {
        if (optionalObj == null && !TextUtils.isEmpty(optional)) {
            this.optionalObj = JSON.parseObject(optional, Optional.class, Feature.IgnoreNotMatch,
                    Feature.AllowISO8601DateFormat);
        }
        return optionalObj;
    }

    @Override
    public String toString() {
        return "PushMessage{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", msgType=" + msgType +
                ", noticeId=" + noticeId +
                ", optional='" + optional + '\'' +
                ", optionalObj=" + optionalObj +
                '}';
    }

    public static class Optional implements Serializable {

        private static final long serialVersionUID = 5747201335425313061L;
        private int remindType = -1;
        private String url = "";
        private long houseId;
        private int rentOrSell;
        private String bizType; // 经纪人业务类型 1-租房经纪人 2-二手房经纪人
        private String appointmentId;
        private String agentId;//经纪人id
        private String appraiseTitle;//电话微聊的时间

        public int getType() {
            return remindType;
        }

        public void setRemindType(int remindType) {
            this.remindType = remindType;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Optional{" +
                    "remindType=" + remindType +
                    ", url='" + url + '\'' +
                    '}';
        }

        public long getHouseId() {
            return houseId;
        }

        public void setHouseId(long houseId) {
            this.houseId = houseId;
        }

        public int getRentOrSell() {
            return rentOrSell;
        }

        public void setRentOrSell(int rentOrSell) {
            this.rentOrSell = rentOrSell;
        }

        public String getBizType() {
            return bizType;
        }

        public void setBizType(String bizType) {
            this.bizType = bizType;
        }

        public String getAppointmentId() {
            return appointmentId;
        }

        public void setAppointmentId(String appointmentId) {
            this.appointmentId = appointmentId;
        }

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public String getAppraiseTitle() {
            return appraiseTitle;
        }

        public void setAppraiseTitle(String appraiseTitle) {
            this.appraiseTitle = appraiseTitle;
        }

        public int getAgentId2Int() {
            try {
               return Integer.parseInt(agentId);
            }catch (NumberFormatException e){
                return 0;
            }
        }

        public long getAppointmentId2Long() {
            try {
                return Long.parseLong(appointmentId);
            }catch (NumberFormatException e){
                return 0;
            }

        }
        public int getBizType2Int() {
            try {
                return Integer.parseInt(bizType);
            }catch (NumberFormatException e){
                return 0;
            }

        }

    }
}
