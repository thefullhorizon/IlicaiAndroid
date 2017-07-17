package com.ailicai.app.setting;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.ailicai.app.R;
import com.ailicai.app.common.constants.CommonTag;
import com.ailicai.app.common.reqaction.IwjwHttp;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.huoqiu.framework.rest.Configuration;

import butterknife.Bind;
import butterknife.OnClick;

public class IpChangeFragment extends BaseBindFragment implements CompoundButton.OnCheckedChangeListener {
    @Bind(R.id.check_box1)
    RadioButton box1;
    @Bind(R.id.check_box2)
    RadioButton box2;
    @Bind(R.id.check_box3)
    RadioButton box3;
    @Bind(R.id.ip_edit)
    EditText ipEdit;
    @Bind(R.id.port_edit)
    EditText portEdit;

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
        saveConfig();
        IwjwHttp.updateRootUrlByConfig();
        showCurrentServer();
        remove();
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

        box1.setOnCheckedChangeListener(this);
        box2.setOnCheckedChangeListener(this);
        box3.setOnCheckedChangeListener(this);
        h5ipEdit.setText(MyPreference.getInstance().read(CommonTag.CUSTOME_H5HOST, ""));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.check_box1:
                    box2.setChecked(false);
                    box3.setChecked(false);
                    ipEdit.setText("192.168.1.44");
                    portEdit.setText("" + 1319);
                    break;
                case R.id.check_box2:
                    box1.setChecked(false);
                    box3.setChecked(false);
                    ipEdit.setText("poros.iwlicaibeta.com");
                    portEdit.setText("" + 8638);
                    break;
                case R.id.check_box3:
                    box1.setChecked(false);
                    box2.setChecked(false);
                    ipEdit.setText("poros.iwlicai.com");
                    portEdit.setText("" + 443);
                    break;
            }
        }
    }

    private void saveConfig() {
        Configuration.DEFAULT.hostname = ipEdit.getText().toString();
        Configuration.DEFAULT.port = Integer.parseInt(portEdit.getText().toString());
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
