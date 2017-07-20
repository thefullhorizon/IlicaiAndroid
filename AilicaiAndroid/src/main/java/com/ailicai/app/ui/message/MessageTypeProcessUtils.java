package com.ailicai.app.ui.message;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.push.constant.CommonTags;
import com.ailicai.app.common.push.model.PushMessage;
import com.ailicai.app.common.support.SupportFinance;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.version.VersionUtil;
import com.ailicai.app.message.Notice;
import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.html5.SupportUrl;
import com.ailicai.app.ui.index.IndexActivity;
import com.ailicai.app.ui.view.CapitalActivity;
import com.ailicai.app.ui.view.MyWalletActivity;
import com.ailicai.app.ui.view.reserveredrecord.ReserveRecordListActivity;
import com.ailicai.app.ui.voucher.CouponWebViewActivity;
import com.ailicai.app.widget.DialogBuilder;
import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeme on 2017/7/18.
 */

public class MessageTypeProcessUtils {

    /**
     * 主页各种跳转的逻辑处理
     *
     * @param mActivity
     * @param mViewPager
     */
    public static void parseIntent(final IndexActivity mActivity, ViewPager mViewPager) {
        Intent intent = mActivity.getIntent();
        if (null != intent) {
            String from = intent.getStringExtra(CommonTags.FROM);
            if (TextUtils.isEmpty(from)) {
                from = CommonTags.SMS;
            }
            switch (from) {
                case CommonTags.PUSH:
                    processPushMessage(mActivity,intent);
                    break;
                case CommonTags.SMS:
                    processSmsMessage(mActivity,intent);
                    break;

            }
        }
    }

    private static void processPushMessage(Activity activity,Intent intent){
        PushMessage pushMessage = (PushMessage)intent.getSerializableExtra(PushMessage.PUSHMESSAGE);
        Intent intentGo = new Intent();
        switch (pushMessage.getMsgType()) {
            case PushMessage.REMINDTYPE:
                switch (pushMessage.getOptional().getType()) {
                    case PushMessage.REMINDTYPECOUPONBANNER:
                        //  FinanceAdActivity.showAdFullDialog(mActivity);
                        break;
                    default:
                        intentGo.setClass(activity, BaseMessageListActivity.class);
                        intentGo.putExtra(BaseMessageListActivity.MESSAGELISTTYPE,
                                PushMessage.REMINDTYPE);
                        activity.startActivity(intentGo);
                        break;
                }
                break;
            case PushMessage.INFOTYPE:
            case PushMessage.ACTIVITYTYPE:
                switch (pushMessage.getOptional().getType()) {
                    case PushMessage.NOTICETYPETOFINANCE:
                        IndexActivity.startIndexActivityToTab(activity,1);
                        break;
                }
                break;
        }
    }

    private static void processSmsMessage(Activity activity,Intent intent){
        Uri data = intent.getParcelableExtra(CommonTags.URIDATA);
        if (null != data) {
            try {
                EventLog.upEventLog("684", URLEncoder.encode(data.toString()));
            } catch (Exception e) {
                LogUtil.i(e.toString());
            }

            String smsType = data.getPath();
            switch (smsType) {
                case CommonTags.COUPON:
                    if (!CouponWebViewActivity.goCoupon(activity)) {
                        activity.setIntent(intent);
                    } else {
                        activity.setIntent(new Intent());
                    }
                    break;
                case CommonTags.FINANCE:
                    IndexActivity.startIndexActivityToTab(activity,1);
                    break;
                case CommonTags.MYFCB:
                    String tab = data.getQueryParameter(CommonTags.TABINDEX);
                    switch (tab) {
                        case CommonTags.HOLDLIST:
                            //todo
                            /*Bundle fcbBundle = new Bundle();
                            fcbBundle.putString(CapitalActivity.TAB,CapitalActivity.HOLD);
                            if (!CapitalActivity.goCapital(activity,fcbBundle)) {
                                activity.setIntent(intent);
                            } else {
                                activity.setIntent(new Intent());
                            }*/
                            break;
                        default:
                            break;
                    }
                    break;
                case CommonTags.TIYANBAO:
                    //todo
                    /*Intent tiYanBaoIntent = new Intent();
                    tiYanBaoIntent.putExtra(RegularFinanceDetailH5Activity.EXTRA_URL,getTiYanBaoUrlByBrowser(data));
                    IndexChoiceBusinessView.toFinanceHome(mActivity,tiYanBaoIntent);*/
                    break;
                case CommonTags.FANGCHANBAO:
                    //todo
                    /*Intent fangchanbaoIntent = new Intent();
                    fangchanbaoIntent.putExtra(RegularFinanceDetailH5Activity.EXTRA_URL,getFangChanBaoUrlByBrowser(data));
                    IndexChoiceBusinessView.toFinanceHome(mActivity,fangchanbaoIntent);*/
                    break;

            }
        }
    }

    private static String getTiYanBaoUrlByBrowser(Uri data) {
        SupportFinance supportFinance = MyPreference.getInstance().read(SupportFinance.class);
        return supportFinance != null ? supportFinance.getTiyanbaoDetailUrl()+getParaDescForUri(data) : "";
    }

    private static String getFangChanBaoUrlByBrowser(Uri data) {
        SupportFinance supportFinance = MyPreference.getInstance().read(SupportFinance.class);
        return supportFinance != null ? supportFinance.getProductDetailUrl()+getParaDescForUri(data) : "";
    }

    private static String getParaDescForUri(Uri data) {
        return data.toString().substring(data.toString().indexOf("?"),data.toString().length());
    }

    public static void processMessageListActivityItemClick(Activity context, int msgType, Notice notice){
        if(notice == null){
            return;
        }
        if(msgType == PushMessage.REMINDTYPE){//提醒的消息
            processRemindMessageClick(context,notice);
        }else{//活动和资讯消息
            switch (notice.getRemindType()){
                case PushMessage.NOTICETYPETOFINANCE:
                    sendEventLog(notice);
                    IndexActivity.startIndexActivityToTab(context,1);
                    break;
                default:
                    showNewVersionDialog(context);
                    break;
            }
        }
    }

    /***
     * 处理消息详情列表页的item点击事件
     */
    private static void processRemindMessageClick(Activity activity,Notice notice){
        Intent intent;
        int remindType = notice.getRemindType();
        switch (remindType){
            case PushMessage.REMINDTYPENEWVOUCHER:
            case PushMessage.REMINDTYPETIYANJI:
                intent = new Intent(activity, CouponWebViewActivity.class);
                activity.startActivity(intent);
                break;
            case PushMessage.REMINDTYPECOUPONBANNER:
                intent = new Intent(activity, MessageDetailWebViewActivity.class);
                intent.putExtra(BaseWebViewActivity.URL, SupportUrl.getTradeEnsureCardUrl());
                activity.startActivity(intent);
                break;
            case PushMessage.REMINDTYPERESERVESUCCESS:
                sendEventLog(notice);
                intent = new Intent(activity, ReserveRecordListActivity.class);
                activity.startActivity(intent);
                break;
            case PushMessage.REMINDTYPERESERVEFAIL:
            case PushMessage.REMINDTYPELIUBIAO:
            case PushMessage.REMINDTYPETYJTOFIHOME:
                sendEventLog(notice);
                IndexActivity.startIndexActivityToTab(activity,1);
                break;
            case PushMessage.REMINDTYPEHUANKUAN:
            case PushMessage.REMINDTYPEZHUANRANG:
            case PushMessage.REMINDTYPEMUZIJIXI:
                sendEventLog(notice);
                intent = new Intent(activity, CapitalActivity.class);
                intent.putExtra(CapitalActivity.TAB, remindType == PushMessage.REMINDTYPEHUANKUAN
                        ? CapitalActivity.EXPIRED : CapitalActivity.HOLD);
                activity.startActivity(intent);
                break;
            case PushMessage.REMINDTYPEBANKRECEIPTFAIL:
                sendEventLog(notice);
                MyWalletActivity.goMywallet(activity);
                break;
            default:
                showNewVersionDialog(activity);
                break;
        }
    }
    private static void showNewVersionDialog(final Activity activity){
        DialogBuilder.showSimpleDialog(activity, "更新最新版app，可查看更多内容", null, "取消", null, "确定", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VersionUtil.check(activity);
                    }
                });
    }

    private static void sendEventLog(Notice notice){
        // todo 统计
        if(notice != null) {
            Map<String, String> map = new HashMap<>();
            map.put("noticeId", notice.getId() + "");
            map.put("title", notice.getTitle());
            EventLog.upEventLog("164", JSON.toJSONString(map));
        }
    }
}
