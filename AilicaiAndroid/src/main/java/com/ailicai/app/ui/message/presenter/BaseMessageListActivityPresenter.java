package com.ailicai.app.ui.message.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ailicai.app.common.push.model.PushMessage;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.message.NoticeChangeRequest;
import com.ailicai.app.message.NoticeListRequest;
import com.ailicai.app.message.NoticeListResponse;
import com.ailicai.app.ui.login.UserInfo;
import com.ailicai.app.ui.message.BaseMessageListActivity;
import com.huoqiu.framework.rest.Response;

import java.lang.ref.WeakReference;

/**
 * Created by duo.chen on 2015/8/11.
 */
public class BaseMessageListActivityPresenter {

    private final static int MESSAGE_PAGE_SIZE = 10;
    private Handler handler;
    private WeakReference<Context> context;
    private int type;
    public BaseMessageListActivityPresenter(Context context, Handler handler, int type) {
        this.context = new WeakReference<>(context);
        this.handler = handler;
        this.type = type;
    }

    public void loadData(int offset, final boolean reload) {
        NoticeListRequest noticeRequest = new NoticeListRequest();
        long userid = UserInfo.getInstance().getUserId();
        if (userid > 0) {
            noticeRequest.setUserId(userid);
            noticeRequest.setType(type);
            noticeRequest.setOffSet(offset);
            noticeRequest.setPageSize(MESSAGE_PAGE_SIZE);
            if (null != context.get()) {
                ServiceSender.exec(context.get(), noticeRequest, new IwjwRespListener<NoticeListResponse>() {

                    @Override
                    public void onJsonSuccess(NoticeListResponse jsonObject) {
                        Message message = handler.obtainMessage();
                        message.what = BaseMessageListActivity.MESSAGE_LOADDATA;
                        if (reload) {
                            message.arg1 = 1;
                        } else {
                            message.arg1 = 0;
                        }
                        message.obj = jsonObject;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onFailInfo(String errorInfo) {
                        onFail(errorInfo);
                    }
                });
            } else {
                onFail("请登陆");
            }
        }
    }


    public void onFail(String errorInfo){
        Message message = handler.obtainMessage();
        message.what = BaseMessageListActivity.REQUEST_ERRORORFAILED;
        message.obj = errorInfo;
        handler.sendMessage(message);
    }
    public void delMessage(long id) {
        NoticeChangeRequest noticeSaveRequest = new NoticeChangeRequest();
        long userid = UserInfo.getInstance().getUserId();
        if (userid > 0) {
            noticeSaveRequest.setUserId(userid);
            noticeSaveRequest.setId(id);
            noticeSaveRequest.setType(NoticeChangeRequest.DEL);
            switch (type) {
                case PushMessage.REMINDTYPE:
                    noticeSaveRequest.setNoticeType(PushMessage.REMINDTYPE);
                    break;
                case PushMessage.INFOTYPE:
                    noticeSaveRequest.setNoticeType(PushMessage.INFOTYPE);
                    break;
                case PushMessage.ACTIVITYTYPE:
                    noticeSaveRequest.setNoticeType(PushMessage.INFOTYPE);
                    break;
            }
            if (null != context.get()) {
                ServiceSender.exec(context.get(), noticeSaveRequest, new IwjwRespListener<Response>() {
                    @Override
                    public void onJsonSuccess(Response jsonObject) {
                        handler.sendEmptyMessage(BaseMessageListActivity.MESSAGE_DELSUCCESS);
                    }

                    @Override
                    public void onFailInfo(String errorInfo) {
                        onFail(errorInfo);
                    }
                });
            } else {
                onFail("请登陆");
            }
        }
    }
}
