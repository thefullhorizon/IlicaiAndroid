package com.ailicai.app.ui.message;

import android.os.Bundle;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.ui.base.BaseBindActivity;

/**
 * Created by duo.chen on 2015/8/6.
 * update by liyanan since 5.0
 */
public class MessageActivity extends BaseBindActivity {

    @Override
    public int getLayout() {
        return R.layout.activity_message_list;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        MessageFragment messageFragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("showBack", true);
        messageFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, messageFragment).commit();
        // 任意地方 进入消息页面 取消合并消息状态
//        SnackBarUtil.resetLastMergeMsgState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtil.miDarkSystemBar(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MsgLiteView.refreshNoticeNums(null);
        finish();
    }

}
