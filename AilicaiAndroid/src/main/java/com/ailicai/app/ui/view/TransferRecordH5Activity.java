package com.ailicai.app.ui.view;

import android.os.Bundle;
import android.text.TextUtils;

import com.ailicai.app.model.response.CancelCreditAssignmentResponse;
import com.ailicai.app.ui.base.webview.BaseWebViewActivity;
import com.ailicai.app.ui.base.webview.BaseWebViewLayout;
import com.ailicai.app.ui.base.webview.WebMethodCallAction;
import com.ailicai.app.ui.buy.CancelTransferPay;
import com.ailicai.app.ui.buy.IwPwdPayResultListener;
import com.ailicai.app.ui.dialog.BaseBuyFinanceDialog;

import java.util.HashMap;

/**
 * 转让记录H5页面
 * Created by liyanan on 16/7/29.
 */
public class TransferRecordH5Activity extends BaseWebViewActivity {
    public static final String EXTRA_URL = "url";

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTopTheme(false);
        setLoadingStyle(BaseWebViewLayout.LoadingStyle.WHEEL);
        shouldShowLoading(true);
        setIWebListener(new BaseWebViewLayout.IWebListener() {
            @Override
            public void onWebLoadStart(BaseWebViewLayout webViewLayout) {

            }

            @Override
            public void onProgressChanged(int newProgress, BaseWebViewLayout webViewLayout) {
                if (newProgress > 50) {
                    webViewLayout.shouldShowLoading(false);
                }
            }

            @Override
            public void onReceivedTitle(String title, BaseWebViewLayout webViewLayout) {
                if (!TextUtils.isEmpty(title)) {
                    webViewLayout.setTitle(title);
                }
            }
        });
        addMethodCallAction();
        loadUrl(getIntent().getStringExtra(EXTRA_URL));
    }

    private boolean showCancel;

    private void addMethodCallAction() {
        //取消转让
        addMethodCallAction(new WebMethodCallAction("canceltransfer") {
            @Override
            public Object call(HashMap params) {
                if (showCancel) {
                    return null;
                }
                showCancel = true;
                String id = (String) params.get("id");
                String txt = (String) params.get("txt");
                String money = (String) params.get("money");
                if (!TextUtils.isEmpty(money) && money.contains("元")) {
                    money = money.replace("元", "");
                }
                cancelTransfer(id, txt, money);
                return null;
            }
        });
    }

    /**
     * 取消转让
     *
     * @param id
     * @param txt
     * @param money
     */
    private void cancelTransfer(final String id, final String txt, final String money) {
        final CancelTransferPay.CancelTransfer cancelTransfer = new CancelTransferPay.CancelTransfer();
        cancelTransfer.setId(id);
        cancelTransfer.setMoney(money);
        cancelTransfer.setTxt(txt);
        //弹出验证交易密码弹层
        CancelTransferPay cancelTransferPay = new CancelTransferPay(TransferRecordH5Activity.this, cancelTransfer, new IwPwdPayResultListener<CancelCreditAssignmentResponse>() {
            @Override
            public void onPayPwdTryAgain() {
                cancelTransfer(id, txt, money);
            }

            @Override
            public void onPayComplete(CancelCreditAssignmentResponse object) {
                callJsRefresh();
            }

            @Override
            public void onPayStateDelay(String msgInfo, CancelCreditAssignmentResponse object) {

            }

            @Override
            public void onPayFailInfo(String msgInfo, String errorCode, CancelCreditAssignmentResponse object) {
                callJsRefresh();
            }
        });
        cancelTransferPay.setDialogDismissListener(new BaseBuyFinanceDialog.DialogDismissListener() {
            @Override
            public void onDismiss() {
                showCancel = false;
            }
        });
        cancelTransferPay.pay();
    }

}
