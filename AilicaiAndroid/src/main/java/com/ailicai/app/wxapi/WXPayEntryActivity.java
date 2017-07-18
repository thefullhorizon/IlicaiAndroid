package com.ailicai.app.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.ui.buy.IwPayResultListener;
import com.ailicai.app.ui.buy.PayPresenter;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;


public class WXPayEntryActivity extends FragmentActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        //TODO nanshan 微信支付APP_ID
//        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

        //  Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case 0: //展示成功页面
                    wxPpayEndListener.onPayComplete(resp);
                    break;
                case -1://可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
                    //    ToastUtil.show(this, "支付失败"/*+ resp.errStr*/);
                //    LogUtil.d("debuglog", "微信支付失败：" + resp.errCode + "");
                    wxPpayEndListener.onPayFailInfo("支付失败", resp.errCode + "", resp);
                    break;
                case -2://无需处理。发生场景：用户不支付了，点击取消，返回APP。
                    wxPpayEndListener.onPayFailInfo("你取消了支付", resp.errCode + "", resp);
                    break;
                default:
                    break;
            }
        }
    }

    IwPayResultListener wxPpayEndListener = new IwPayResultListener() {

        @Override
        public void onPayComplete(Object object) {
            MyApplication.getInstance().setOpenWx(true);
            finish();
            //发送支付成功后相关数据给服务端
        }

        @Override
        public void onPayStateDelay(String msgInfo, Object object) {
            PayPresenter.showResultDialog(WXPayEntryActivity.this, "" + msgInfo);
        }

        @Override
        public void onPayFailInfo(String msgInfo, String errorCode, Object object) {
            PayPresenter.showResultDialog(WXPayEntryActivity.this, "" + msgInfo);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}