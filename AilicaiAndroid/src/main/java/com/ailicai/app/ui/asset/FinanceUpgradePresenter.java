package com.ailicai.app.ui.asset;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.model.request.FinanceSystemIsFixRequest;
import com.ailicai.app.model.request.ProtocalUpgradeRequest;
import com.ailicai.app.model.request.ReportProtocalikonwRequest;
import com.ailicai.app.model.response.FinanceSystemIsFixResponse;
import com.ailicai.app.model.response.ProtocalUpgradeResponse;
import com.ailicai.app.ui.dialog.SystemMaintenanceDialog;
import com.ailicai.app.ui.dialog.UpgradeProtocalDialog;
import com.huoqiu.framework.rest.Response;

/**
 * name: FinanceUpgradePresenter <BR>
 * description: 协议升级（快钱换新浪） 系统维护中 请求状态<BR>
 * create date: 2016/11/24
 *
 * @author: IWJW Zhou Xuan
 */
public class FinanceUpgradePresenter {

    public void httpForProtocalUpgradeState(final FragmentActivity fragmentActivity) {
        ProtocalUpgradeRequest request = new ProtocalUpgradeRequest();
        ServiceSender.exec(fragmentActivity, request, new IwjwRespListener<ProtocalUpgradeResponse>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onJsonSuccess(ProtocalUpgradeResponse jsonObject) {
                if(jsonObject.getProtocols() != null && jsonObject.getProtocols().size() > 0){
                    if(jsonObject.isShowDialog()) {

                        Fragment fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag(UpgradeProtocalDialog.class.getSimpleName());

                        if(fragment == null) {
                            UpgradeProtocalDialog dialog = new UpgradeProtocalDialog();
                            Bundle data = new Bundle();
                            data.putSerializable("data",jsonObject);
                            dialog.setArguments(data);
                            dialog.show(fragmentActivity.getSupportFragmentManager(),UpgradeProtocalDialog.class.getSimpleName());
                        }
                    }
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
            }
        });
    }

    public void httpForProtocalIKnow(final FragmentActivity fragmentActivity) {
        ReportProtocalikonwRequest request = new ReportProtocalikonwRequest();
        ServiceSender.exec(fragmentActivity, request, new IwjwRespListener<Response>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onJsonSuccess(Response jsonObject) {
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
            }
        });
    }

    public void httpForSystemIsFix(final FragmentActivity fragmentActivity) {
        FinanceSystemIsFixRequest request = new FinanceSystemIsFixRequest();
        ServiceSender.exec(fragmentActivity, request, new IwjwRespListener<FinanceSystemIsFixResponse>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onJsonSuccess(FinanceSystemIsFixResponse response) {
                if(response.isShowDialog()) {

                    Fragment fragment = fragmentActivity.getSupportFragmentManager().findFragmentByTag(SystemMaintenanceDialog.class.getSimpleName());
                    if(fragment == null) {
                        SystemMaintenanceDialog dialog = new SystemMaintenanceDialog();
                        Bundle data = new Bundle();
                        data.putString("data",response.getTime());
                        dialog.setArguments(data);
                        dialog.show(fragmentActivity.getSupportFragmentManager(),SystemMaintenanceDialog.class.getSimpleName());
                    }
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
            }
        });
    }
}
