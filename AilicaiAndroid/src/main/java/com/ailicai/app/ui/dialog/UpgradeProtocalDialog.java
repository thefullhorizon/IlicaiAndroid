package com.ailicai.app.ui.dialog;

import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.common.utils.UIUtils;
import com.ailicai.app.model.bean.Protocol;
import com.ailicai.app.model.response.ProtocalUpgradeResponse;
import com.ailicai.app.ui.asset.FinanceUpgradePresenter;
import com.ailicai.app.ui.base.webview.WebViewActivity;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * name: UpgradeProtocalDialog <BR>
 * description: 升级协议对话框 <BR>
 * create date: 2016/11/24
 *
 * @author: IWJW Zhou Xuan
 */
public class UpgradeProtocalDialog extends MyBaseDialog{

    @Bind(R.id.tvProtocalDesc)
    TextView tvProtocalDesc;
    @Bind(R.id.tvSubTitle)
    TextView tvSubTitle;
    @Bind(R.id.tvUpgradeDesc)
    TextView tvUpgradeDesc;

    List<Protocol> protocols;

    @Override
    public void setupView(View rootView, Bundle savedInstanceState) {
        int width = DeviceUtil.getScreenSize()[0] - UIUtils.dipToPx(getActivity(),32);
        int height = 272*width/576;
        setTopImageSize(rootView, width, height);
        setDialogSize(rootView, width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void setupData(Bundle savedInstanceState) {
        ProtocalUpgradeResponse jsonObject = (ProtocalUpgradeResponse) getArguments().getSerializable("data");

        tvSubTitle.setText(jsonObject.getSubTitle());
        tvUpgradeDesc.setText(Html.fromHtml(jsonObject.getContent()));

        protocols = jsonObject.getProtocols();
        String desc1 = "我已阅读并接受";
        for(Protocol protocol:protocols) {
            desc1 = desc1 + "《"+protocol.getName()+"》";
        }
        SpannableString spanableInfo = new SpannableString(desc1);

        String desc2 = "我已阅读并接受";
        for(int i = 0;i<protocols.size();i++) {
            int start = +desc2.length();
            desc2 = desc2 + "《"+protocols.get(i).getName()+"》";
            int end = desc2.length();
            spanableInfo.setSpan(new Clickable(i), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tvProtocalDesc.setText(spanableInfo);
        //setMovementMethod()该方法必须调用，否则点击事件不响应
        tvProtocalDesc.setMovementMethod(LinkMovementMethod.getInstance());

        avoidHintColor(tvProtocalDesc);
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_upgrade_protocol;
    }

    @Override
    public int getTheme() {
        return MYTHEME3;
    }

    @Override
    public int displayWindowLocation() {
        return Gravity.CENTER;
    }

    @Override
    public boolean cancelable() {
        return false;
    }

    private void setTopImageSize(View rootView, int width, int height) {
        ImageView iv_top_bg = (ImageView) rootView.findViewById(R.id.iv_top_bg);
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(width, height);
        iv_top_bg.setLayoutParams(lp1);
    }

    private void setDialogSize(View rootView, int width, int height) {
        LinearLayout rootLayout = (LinearLayout) rootView.findViewById(R.id.dialog_root);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(width, height);
        rootLayout.setLayoutParams(lp1);
    }

    @OnClick(R.id.tvIKown)
    void onIKnow() {
        FinanceUpgradePresenter presenter = new FinanceUpgradePresenter();
        presenter.httpForProtocalIKnow(getActivity());
        dismiss();
    }

    class Clickable extends ClickableSpan {

        private int type;

        public Clickable(int type) {
            super();
            this.type = type;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.bgColor = getResources().getColor(R.color.transparent);
            ds.setColor(getResources().getColor(R.color.color_265db6));
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View v) {
            avoidHintColor(v);

            Map<String, String> dataMap = ObjectUtil.newHashMap();
            dataMap.put(WebViewActivity.NEED_REFRESH, "0");
            dataMap.put(WebViewActivity.TOPVIEWTHEME, "false");
            for(int i = 0;i<protocols.size();i++) {
                if(i == type) {
                    dataMap.put(WebViewActivity.URL, protocols.get(i).getUrl());
                    dataMap.put(WebViewActivity.TITLE,  protocols.get(i).getName());
                }
            }
            MyIntent.startActivity(getActivity(), WebViewActivity.class, dataMap);
        }
    }

    private void avoidHintColor(View view){
        if(view instanceof TextView)
            ((TextView)view).setHighlightColor(getResources().getColor(android.R.color.transparent));
    }
}
