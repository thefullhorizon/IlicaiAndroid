package com.ailicai.app.ui.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.UIUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * name: SystemMaintenanceDialog <BR>
 * description: 系统维护对话框 <BR>
 * create date: 2016/11/24
 *
 * @author: IWJW Zhou Xuan
 */
public class SystemMaintenanceDialog extends MyBaseDialog{

    @Bind(R.id.tvBackTime)
    TextView tvBackTime;
    @Bind(R.id.tvFixDesc)
    TextView tvFixDesc;

    @Override
    public void setupView(View rootView, Bundle savedInstanceState) {
        int width = DeviceUtil.getScreenSize()[0] - UIUtils.dipToPx(getActivity(),32);
        int height = 220*width/576;
        setTopImageSize(rootView, width, height);
        setDialogSize(rootView, width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void setupData(Bundle savedInstanceState) {
        String time = (String) getArguments().get("data");
        tvBackTime.setText("预计"+time+"前恢复");
        tvFixDesc.setText(String.format(getResources().getString(R.string.finance_system_fixing),"预计将在"+time+"前恢复使用"));
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_system_fix;
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
        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(width, height);
        iv_top_bg.setLayoutParams(lp1);
    }

    private void setDialogSize(View rootView, int width, int height) {
        LinearLayout rootLayout = (LinearLayout) rootView.findViewById(R.id.dialog_root);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(width, height);
        rootLayout.setLayoutParams(lp1);
    }

    @OnClick(R.id.tvIKown)
    void onIKnow() {

        dismiss();
        //TODO nanshan 系统维护
//        if(getActivity() instanceof FinanceHomeActivity) {
//            IndexMainActivity.startIndexActivityToTab(getActivity(),0);
//        }
        getActivity().finish();
    }
}
