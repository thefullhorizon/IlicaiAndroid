package com.ailicai.app.ui.view;

import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.bean.Protocol;
import com.ailicai.app.model.request.ProtocolRequest;
import com.ailicai.app.model.response.ProtocolResponse;
import com.ailicai.app.ui.base.BaseActivity;
import com.ailicai.app.ui.dialog.AgreementDialog;

import java.util.List;

/**
 * Created by liyanan on 16/10/8.
 */
public class ProtocolHelper {
    //填写银行卡
    public static final int TYPE_BANK_CARD_INFO = 1;
    //钱包转入
    public static final int TYPE_WALLET_ROLL_IN = 2;
    //普通房产宝收银台
    public static final int TYPE_NORMAL_PAY = 3;
    //转让房产宝收银台
    public static final int TYPE_TRANSFER_PAY = 4;
    //预约房产宝收银台
    public static final int TYPE_RESERVE_PAY = 5;
    //换卡时，添加新卡;已开户当前未设置安全卡，添加新卡;添加普通银行卡（小绑卡）时
    public static final int TYPE_ADD_NEW_BANK_CARD = 6;
    private BaseActivity activity;
    private int bizType;
    private List<Protocol> protocols;

    public ProtocolHelper(BaseActivity activity, int bizType) {
        this.activity = activity;
        this.bizType = bizType;
    }

    public void setProtocols(List<Protocol> protocols) {
        this.protocols = protocols;
    }

    /**
     * 获取协议
     *
     * @param showLoading
     */
    public void loadProtocol(final boolean showLoading) {
        if (protocols != null && protocols.size() > 0) {
            showProtocolDialog(activity);
            return;
        }
        ProtocolRequest request = new ProtocolRequest();
        request.setBizType(bizType);
        ServiceSender.exec(activity, request, new IwjwRespListener<ProtocolResponse>() {
            @Override
            public void onStart() {
                super.onStart();
                if (activity != null && !activity.isFinishing() && showLoading) {
                    activity.showLoadTranstView();
                }
            }

            @Override
            public void onJsonSuccess(ProtocolResponse jsonObject) {
                if (jsonObject != null) {
                    protocols = jsonObject.getList();
                    if (activity != null && !activity.isFinishing()) {
                        activity.showContentView();
                        activity.verifyProtocolListLogical(jsonObject.getList());
                        if(showLoading){
                            showProtocolDialog(activity);
                        }
                    }
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                if (activity != null && !activity.isFinishing() && showLoading) {
                    activity.showContentView();
                    ToastUtil.showInCenter(errorInfo);
                }
            }
        });
    }

    private void showProtocolDialog(BaseActivity activity) {
        AgreementDialog agreementDialog = new AgreementDialog();
        agreementDialog.setProtocols(protocols);
        activity.getmFragmentHelper().showDialog(null, agreementDialog);
    }
}
