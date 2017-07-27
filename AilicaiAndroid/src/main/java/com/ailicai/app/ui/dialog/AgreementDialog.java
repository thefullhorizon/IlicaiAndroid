package com.ailicai.app.ui.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.DisplayUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.model.bean.Protocol;
import com.ailicai.app.ui.base.webview.WebViewActivity;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by Tangxiaolong on 2015/1/5.
 */
public class AgreementDialog extends MyBaseDialog {

    private List<Protocol> protocols;

    @Override
    public int getLayout() {
        return R.layout.agreement_layout;
    }

    @Override
    public int getTheme() {
        return MYTHEME2;
    }

    @Override
    public int displayWindowLocation() {
        return Gravity.BOTTOM;
    }

    @Override
    public boolean cancelable() {
        return true;
    }

    @Override
    public void setupView(View rootView, Bundle bundle) {
        setDialogSize(rootView, DeviceUtil.getScreenSize()[0], LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout llProtocol = ButterKnife.findById(rootView, R.id.ll_protocol);
        if (protocols != null && protocols.size() > 0) {
            for (final Protocol protocol : protocols) {
                TextView tvProtocol = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_protocol_item, null);
                tvProtocol.setText(protocol.getName());
                tvProtocol.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        Map<String, String> dataMap = ObjectUtil.newHashMap();
                        dataMap.put(WebViewActivity.TITLE, "协议详情");
                        dataMap.put(WebViewActivity.URL, protocol.getUrl());
                        dataMap.put(WebViewActivity.NEED_REFRESH, "0");
                        dataMap.put(WebViewActivity.TOPVIEWTHEME, "false");
                        MyIntent.startActivity(getActivity(), WebViewActivity.class, dataMap);
                    }
                });
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(getActivity(), 48));
                params.setMargins(0, 1, 0, 0);
                tvProtocol.setLayoutParams(params);
                llProtocol.addView(tvProtocol);
            }
        }
        TextView tvCancel = ButterKnife.findById(rootView, R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setDialogSize(View rootView, int width, int height) {
        LinearLayout rootLayout = (LinearLayout) rootView.findViewById(R.id.dialog_root);
        rootLayout.getLayoutParams().width = width;
        rootLayout.getLayoutParams().height = height;
    }

    @Override
    public void setupData(Bundle bundle) {
    }


    public void setProtocols(List<Protocol> protocols) {
        this.protocols = protocols;
    }

}