package com.ailicai.app.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.ailicai.app.R;
import com.ailicai.app.common.hybrid.HybridEngine;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.index.IndexActivity;
import com.ailicai.app.widget.IWTopTitleView;
import com.huoqiu.framework.app.AppConfig;
import com.huoqiu.framework.app.SuperFragment;

import butterknife.Bind;
import butterknife.OnClick;

public class DebugActivity extends BaseBindActivity {
    @Bind(R.id.debug_top_title)
    IWTopTitleView debugTopTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.mine_debug_layout;
    }

    @OnClick(R.id.debug_channel)
    void onClickChannel() {
        ToastUtil.showInCenter("ChannelNO=" + AppConfig.channelNo + "\nVersionCode=" + AppConfig.versionCode + "\nVersionName=" + AppConfig.versionName);
    }

    @OnClick(R.id.change_ip)
    void onClickIpChange() {
        IpChangeFragment fra = new IpChangeFragment();
        fra.setManager(this.getSupportFragmentManager());
        fra.setContainerId(Window.ID_ANDROID_CONTENT);
        fra.setTag(IpChangeFragment.class.getSimpleName());
        fra.setDefaultAnimations();
        fra.show(SuperFragment.SHOW_ADD);
    }

    @OnClick(R.id.hybrid_debug_btn)
    void clickHybridButton() {
        ToastUtil.show(HybridEngine.getHybridEngine().getVersion());
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initView();
    }

    void initView() {
        debugTopTitle.setTitleOnClickListener(new IWTopTitleView.TopTitleOnClickListener() {
            @Override
            public boolean onBackClick() {
                DebugActivity.this.finish();
                Intent mIntent = new Intent(DebugActivity.this, IndexActivity.class);
                DebugActivity.this.startActivity(mIntent);
                return true;
            }
        });
//        String[] mItems = getResources().getStringArray(R.array.version_name);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
    }

}
