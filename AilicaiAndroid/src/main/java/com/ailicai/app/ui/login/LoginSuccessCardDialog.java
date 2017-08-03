package com.ailicai.app.ui.login;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ScreenUtils;
import com.ailicai.app.model.response.UserLoginResponse;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.voucher.CouponWebViewActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 注册完成卡券弹窗
 */
public class LoginSuccessCardDialog extends BaseBindActivity {

    public static final String CARD_DATA = "card_data";
    @Bind(R.id.card_desc)
    TextView cardDesc;

    @Override
    public void init(Bundle savedInstanceState) {
        setShowSystemBarTint(false);
        initWindow();

        UserLoginResponse jsonObject = (UserLoginResponse) getIntent().getSerializableExtra(CARD_DATA);
        if (jsonObject != null) {

        }

        //cardDesc.setText("恭喜您\n" + "获得¥688礼包");
        if (Build.VERSION.SDK_INT >= 24) {
            cardDesc.setText(Html.fromHtml(getString(R.string.login_card_dialog, "¥688礼包"), Html.FROM_HTML_MODE_COMPACT));
        } else {
            cardDesc.setText(Html.fromHtml(getString(R.string.login_card_dialog, "¥688礼包")));
        }
    }

    private void initWindow() {
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = ScreenUtils.getScreenWidth(this);
        p.height = ScreenUtils.getSreenHeight(this);
        p.gravity = Gravity.CENTER;
        p.dimAmount = 0.7f;
        getWindow().setAttributes(p);
    }

    @Override
    public int getLayout() {
        return R.layout.login_dialog_card;
    }


    @OnClick(R.id.tv_close)
    void onClickClose() {
        finish();
    }

    @OnClick(R.id.check_soon)
    void onClickLookSoon() {
        //立即查看跳卡券
        MyIntent.startActivity(this, CouponWebViewActivity.class, null);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.none, R.anim.activity_lollipop_close_exit);
    }

}
