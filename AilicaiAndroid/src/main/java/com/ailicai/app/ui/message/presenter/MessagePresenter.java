package com.ailicai.app.ui.message.presenter;


import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.message.NoticeRequest;
import com.ailicai.app.message.NoticeResponse;
import com.ailicai.app.ui.message.MessageFragment;

/**
 * 消息ListPresenter
 * Created by liyanan on 16/4/12.
 */
public class MessagePresenter {
    private static MessagePresenter presenter;
    private MessageFragment fragment;
    private boolean startLoad = true;

    private MessagePresenter() {

    }

    public static synchronized MessagePresenter getPresenter() {
        if (presenter == null) {
            presenter = new MessagePresenter();
        }
        return presenter;

    }


    public void setFragment(MessageFragment fragment) {
        this.fragment = fragment;
    }


    public void requestNotice() {
        NoticeRequest noticeRequest = new NoticeRequest();
        if (!MyPreference.getInstance().read("get_agent_disturb_data_status", false)) {
            noticeRequest.setDataStatus(1);
        }
        ServiceSender.exec(fragment, noticeRequest, new IwjwRespListener<NoticeResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                if (fragment != null) {
                    fragment.showLoadView();
                }
            }

            @Override
            public void onJsonSuccess(NoticeResponse jsonObject) {
       /*         if (jsonObject.getHasPopActive() == 1 && fragment != null) {
                    FinanceAdActivity.showAdFullDialog(fragment.getActivity());
                }*/
                if (fragment != null && !fragment.isDetached()) {
                    fragment.showContentView();
                    fragment.requestSuccess(jsonObject);
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                if (fragment != null) {
                    fragment.showContentView();
                    fragment.requestFail(errorInfo);
                }
            }
        });
    }

    public void removeFragment() {
        presenter = null;
        startLoad = true;
    }
}
