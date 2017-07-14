package com.ailicai.app.common.share;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.ui.dialog.MyBaseDialog;


/**
 * Created by duo.chen on 2015/8/6.
 */
public class MessageDetailShareDialog extends MyBaseDialog {


    private static ShareToWhereListener shareToWhereListener = new ShareToWhereListener() {

        @Override
        public void shareToWechat() {

        }

        @Override
        public void shareToWechatCircle() {

        }

        @Override
        public void cancel() {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupView(View rootView, Bundle bundle) {
        setDialogSize(rootView, DeviceUtil.getScreenSize()[0], DeviceUtil.getScreenSize()[1] / 3);
        rootView.findViewById(R.id.message_web_share_wechat).setOnClickListener(mOnClickListener);
        rootView.findViewById(R.id.message_web_share_wechat_circle).setOnClickListener
                (mOnClickListener);
        rootView.findViewById(R.id.share_cancel_button).setOnClickListener(mOnClickListener);
    }

    private void setDialogSize(View rootView, int width, int height) {
        RelativeLayout rootLayout = (RelativeLayout) rootView
                .findViewById(R.id.dialog_root);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(width,
                height);
        rootLayout.setLayoutParams(lp1);
    }

    @Override
    public void setupData(Bundle bundle) {
    }

    @Override
    public int getLayout() {
        return R.layout.message_detail_share_dialog_layout;
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

    public void setOnClickListener(ShareToWhereListener listener) {
        shareToWhereListener = listener;
    }

    public View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.message_web_share_wechat:
                    shareToWhereListener.shareToWechat();
                    dismiss();
                    break;
                case R.id.message_web_share_wechat_circle:
                    shareToWhereListener.shareToWechatCircle();
                    dismiss();
                    break;
                case R.id.share_cancel_button:
                    dismiss();
                    shareToWhereListener.cancel();
                    break;
                default:
                    break;
            }

        }
    };

    public interface ShareToWhereListener {
        void shareToWechat();
        void shareToWechatCircle();
        void cancel();
    }
}
