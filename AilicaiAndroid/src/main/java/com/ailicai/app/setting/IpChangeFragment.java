package com.ailicai.app.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.ailicai.app.R;
import com.ailicai.app.common.constants.CommonTag;
import com.ailicai.app.common.reqaction.IwjwHttp;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.HtmlUrlRequest;
import com.ailicai.app.model.response.Iwjwh5UrlResponse;
import com.ailicai.app.ui.asset.FinanceUpgradePresenter;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.ui.html5.SupportUrl;
import com.ailicai.app.ui.index.IndexActivity;
import com.ailicai.app.ui.login.LoginManager;
import com.ailicai.app.widget.DialogBuilder;
import com.huoqiu.framework.rest.Configuration;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

public class IpChangeFragment extends BaseBindFragment implements CompoundButton.OnCheckedChangeListener {
    @Bind(R.id.check_box1)
    RadioButton box1;
    @Bind(R.id.check_box2)
    RadioButton box2;
    @Bind(R.id.check_box3)
    RadioButton box3;
    @Bind(R.id.check_box4)
    RadioButton box4;
    @Bind(R.id.ip_edit)
    EditText ipEdit;
    @Bind(R.id.port_edit)
    EditText portEdit;
    @Bind(R.id.llSaveAndShow)
    LinearLayout llSaveAndShow;

    @Bind(R.id.h5ip_edit)
    EditText h5ipEdit;

    @Bind(R.id.h5ip_save_btn)
    Button h5ipSaveBtn;

    @Bind(R.id.h5ip_default_btn)
    Button h5ipDefaultBtn;


    @OnClick(R.id.show)
    public void showCurrentServer() {
        String builder = ("当前设置" + "\n") +
                "hostname:" + Configuration.DEFAULT.hostname + "\n" +
                "port:" + Configuration.DEFAULT.port;
        ToastUtil.show(getActivity(), builder);
    }

    @OnClick(R.id.save)
    public void saveCurrentServer() {
        changeConfigInMemory();
        HtmlUrlRequest request = new HtmlUrlRequest();
        ServiceSender.exec(this, request, new IwjwRespListener<Iwjwh5UrlResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoadTranstView();
            }

            @Override
            public void onJsonSuccess(Iwjwh5UrlResponse jsonObject) {

                showContentView();
                SupportUrl.saveUrls(jsonObject);
                changeConfigInPreference();
                showCurrentServer();
                DialogBuilder.showSimpleDialog(getWRActivity(), null, "切换成功，点击确定关闭应用", null, null, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remove();
                        //退出登录
                        LoginManager.loginOut(getActivity());
                        SystemUtil.exitApplication(getWRActivity());
                    }
                });
            }


            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                ToastUtil.showInCenterLong(getContext(),"保存的IP和端口不通");
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.ipchange_fragment;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Configuration conf = Configuration.DEFAULT;
        ipEdit.setText(conf.hostname);
        portEdit.setText("" + conf.port);
        if(conf.port == 0) {
            portEdit.setVisibility(View.GONE);
        }
        box1.setOnCheckedChangeListener(this);
        box2.setOnCheckedChangeListener(this);
        box3.setOnCheckedChangeListener(this);
        box4.setOnCheckedChangeListener(this);
        h5ipEdit.setText(MyPreference.getInstance().read(CommonTag.CUSTOME_H5HOST, ""));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.check_box1:
                    box2.setChecked(false);
                    box3.setChecked(false);
                    box4.setChecked(false);
                    portEdit.setVisibility(View.VISIBLE);
                    ipEdit.setText("192.168.1.44");
                    portEdit.setText("" + 1319);
                    ipEdit.setEnabled(false);
                    portEdit.setEnabled(false);
                    break;
                case R.id.check_box2:
                    box1.setChecked(false);
                    box3.setChecked(false);
                    box4.setChecked(false);
                    portEdit.setVisibility(View.VISIBLE);
                    ipEdit.setText("poros.iwlicaibeta.com");
                    portEdit.setText("" + 80);
                    ipEdit.setEnabled(false);
                    portEdit.setEnabled(false);
                    break;
                case R.id.check_box3:
                    box1.setChecked(false);
                    box2.setChecked(false);
                    box4.setChecked(false);
                    portEdit.setVisibility(View.GONE);
                    ipEdit.setText("fin.iwlicai.com");
                    portEdit.setText("");
                    ipEdit.setEnabled(false);
                    portEdit.setEnabled(false);
                    break;
                case R.id.check_box4:
                    box1.setChecked(false);
                    box2.setChecked(false);
                    box3.setChecked(false);
                    portEdit.setVisibility(View.VISIBLE);
                    ipEdit.setText("192.168.1.44");
                    portEdit.setText("" + 1319);
                    ipEdit.setEnabled(true);
                    portEdit.setEnabled(true);
                    break;
            }
        }
    }

    private void changeConfigInMemory() {
        Configuration.DEFAULT.hostname = ipEdit.getText().toString();
        if(TextUtils.isEmpty(portEdit.getText().toString().trim())) {
            Configuration.DEFAULT.port = 0;
        } else {
            Configuration.DEFAULT.port = Integer.parseInt(portEdit.getText().toString());
        }
    }

    private void changeConfigInPreference() {
        MyPreference.getInstance().write(CommonTag.SERVER_PORT, Configuration.DEFAULT.port);
        MyPreference.getInstance().write(CommonTag.SERVER_IP, Configuration.DEFAULT.hostname);
    }

    @OnClick(R.id.h5ip_save_btn)
    public void setH5ipSave() {
        MyPreference.getInstance().write(CommonTag.CUSTOME_H5HOST, h5ipEdit.getText().toString() + "");
        ToastUtil.showInCenter("设置完成");
    }

    @OnClick(R.id.h5ip_default_btn)
    public void setH5ipDefault() {
        MyPreference.getInstance().write(CommonTag.CUSTOME_H5HOST, "");
        h5ipEdit.setText("");
        ToastUtil.showInCenter("恢复完成");
    }
}
