package com.ailicai.app.ui.message.presenter;

import com.ailicai.app.common.push.model.PushMessage;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.message.NoticeChangeRequest;
import com.ailicai.app.message.NoticeListRequest;
import com.ailicai.app.message.NoticeListResponse;
import com.ailicai.app.ui.base.BasePresenter;
import com.ailicai.app.ui.base.BaseView;
import com.ailicai.app.ui.login.UserInfo;
import com.huoqiu.framework.rest.Response;

/**
 * Created by jeme on 2017/7/20.
 */

public class BaseMessageActivityPresenter extends BasePresenter<BaseMessageActivityPresenter.BaseMessageView> {

    public interface BaseMessageView extends BaseView {

        void processSuccessView(NoticeListResponse response,boolean reload);
        void showNoData();
        void onFail(String msg);
        void deleteMsgSuccess(int pos);
    }

    private final static int MESSAGE_PAGE_SIZE = 10;

    private int mType;
    public void setMsgType(int msgType){
        this.mType = msgType;
    }

    public void loadData(int type, int offset, final boolean reload){
        NoticeListRequest noticeRequest = new NoticeListRequest();
        long userid = UserInfo.getInstance().getUserId();
        if (userid > 0) {
            noticeRequest.setUserId(userid);
            noticeRequest.setType(type);
            noticeRequest.setOffSet(offset);
            noticeRequest.setPageSize(MESSAGE_PAGE_SIZE);

            if (null != getContext()) {
                ServiceSender.exec(getContext(), noticeRequest, new IwjwRespListener<NoticeListResponse>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        getMvpView().showLoading();
                    }

                    @Override
                    public void onJsonSuccess(NoticeListResponse jsonObject) {
                        BaseMessageView view = getMvpView();
                        view.hideLoading();
                        if(jsonObject.getTotalNum() == 0 || jsonObject.getNoticeList() == null
                                || jsonObject.getNoticeList().size() == 0 ){
                            view.showNoData();
                        }else{
                            view.processSuccessView(jsonObject,reload);
                        }
                    }

                    @Override
                    public void onFailInfo(String errorInfo) {
                        getMvpView().hideLoading();
                        getMvpView().onFail(errorInfo);
                    }
                });
            } else {
                getMvpView().onFail("请登陆");
            }
        }
    }

    public void deleteMsgById(int type,final int position,long id){
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
                if (null != getContext()) {
                    ServiceSender.exec(getContext(), noticeSaveRequest, new IwjwRespListener<Response>() {
                        @Override
                        public void onJsonSuccess(Response jsonObject) {
                            getMvpView().deleteMsgSuccess(position);
                        }

                        @Override
                        public void onFailInfo(String errorInfo) {
                            getMvpView().onFail(errorInfo);
                        }
                    });
                } else {
                    getMvpView().onFail("请登陆");
                }
            }
    }

}
