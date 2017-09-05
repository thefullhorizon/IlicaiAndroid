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

    public static final int REMINDTYPENEWVOUCHER         = INITTYPE + 11;//现金券通知

    public static final int NOTICETYPETOWEBVIEW          = INITTYPE + 13; //资讯打开指定URL或资讯内容
    public static final int NOTICETYPETOFINANCE          = INITTYPE + 14; //14:资讯打开打开吉爱财
    public static final int NOTICETYPETONOTICELIST       = INITTYPE + 15;//啥也不干

    public static final int REMINDTYPERESERVESUCCESS     = INITTYPE + 23; //23 吉爱财加息券-预约自动购买-成功
    public static final int REMINDTYPERESERVEFAIL        = INITTYPE + 24; //24 吉爱财加息券-预约自动购买-失败
    public static final int REMINDTYPELIUBIAO            = INITTYPE + 25; //25 吉爱财加息券-房产宝项目流标
    public static final int REMINDTYPEHUANKUAN           = INITTYPE + 26; //26 吉爱财加息券-房产宝还款
    public static final int REMINDTYPEZHUANRANG          = INITTYPE + 27; //27 转让状态从转让中变更为已结束
    public static final int REMINDTYPEMUZIJIXI           = INITTYPE + 28; //28 募资完成开始计息提醒
    public static final int REMINDTYPETIYANJI            = INITTYPE + 29; //29 代表是体验金
    public static final int REMINDTYPETYJTOFIHOME        = INITTYPE + 32; //32 体验金到吉爱财首页
    public static final int REMINDTYPEBANKRECEIPTFAIL    = INITTYPE + 36; //36 钱包出款失败
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
    }
}
