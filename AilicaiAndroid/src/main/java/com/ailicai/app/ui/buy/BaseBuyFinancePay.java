package com.ailicai.app.ui.buy;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.model.request.GetOutTradeNoRequest;
import com.ailicai.app.model.response.GetOutTradeNoResponse;
import com.ailicai.app.ui.dialog.BaseBuyFinanceDialog;
import com.ailicai.app.ui.dialog.BuyDialogShowInfo;
import com.ailicai.app.ui.paypassword.PayPwdResetActivity;
import com.ailicai.app.widget.DialogBuilder;

/**
 * Created by Jer on 2016/1/7.
 */
public abstract class BaseBuyFinancePay implements IBuyFinancePay, BaseBuyFinanceDialog.IPwdDialogListener {
    FragmentActivity mActivity;
    IwPwdPayResultListener iwPayResultListener;
    BaseBuyFinanceDialog.DialogDismissListener dialogDismissListener;

    public BaseBuyFinancePay(FragmentActivity mActivity, IwPwdPayResultListener iwPayResultListener) {
        this.mActivity = mActivity;
        this.iwPayResultListener = iwPayResultListener;
    }


    BaseBuyFinanceDialog mFinanceDialog;

    public void showPwdDialog() {
        mFinanceDialog = new BaseBuyFinanceDialog(mActivity, this)
                .createDialog(iwPayResultListener).setBuyDialogShowInfo(getDispalyInfo())
                .create();
        if (dialogDismissListener != null) {
            mFinanceDialog.setDialogDismissListener(dialogDismissListener);
        }
        mFinanceDialog.show();
    }

    public void onDialogDismiss() {
        if (mFinanceDialog == null) {
            return;
        }
        mFinanceDialog.onDismiss();
    }

    abstract void toPayForInfo(String jsonObject);

    abstract BuyDialogShowInfo getDispalyInfo();

    @Override
    public void pay() {
        SystemUtil.HideSoftInput(mActivity);
        if (getBizType() == -1) {
            showPwdDialog();
        } else {
            getOutTradeNo();
        }
    }

    int bizType;
    double amount;

    abstract int getBizType();

    abstract double getAmount();

    @Override
    public void onInputFinish(String s) {
        toPayForInfo(s);
    }

    public void getOutTradeNo() {
        bizType = getBizType();
        amount = getAmount();
        GetOutTradeNoRequest getOutTradeNoRequest = new GetOutTradeNoRequest();
        getOutTradeNoRequest.setAmount(amount);
        getOutTradeNoRequest.setBizType(bizType);
        ServiceSender.exec(mActivity, getOutTradeNoRequest, new IwjwRespListener<GetOutTradeNoResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                PayPresenter.showProgressDialog(mActivity, "获取支付信息中...");

            }

            @Override
            public void onJsonSuccess(GetOutTradeNoResponse jsonObject) {
                PayPresenter.dismissProgressDialog();
                outTradeNoResponse = jsonObject;
                showPwdDialog();
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                PayPresenter.dismissProgressDialog();
                ToastUtil.showInCenter(errorInfo);

            }
        });
    }


    public void changeViewToMsgView(String mobilePhone, final View.OnClickListener doneOnClickListener, final View.OnClickListener msgCodeClickListener) {
        mFinanceDialog.changeViewToMsgView(mobilePhone, doneOnClickListener, msgCodeClickListener);
    }

    public void changeViewToMsgView(String mobilePhone, String title, String message, final View.OnClickListener doneOnClickListener, final View.OnClickListener msgCodeClickListener) {
        mFinanceDialog.changeViewToMsgView(mobilePhone, title, message, doneOnClickListener, msgCodeClickListener);
    }


    public void setViewSwitcherAnimToNull() {
        mFinanceDialog.setViewSwitcherAnimToNull();
    }


    public void show60msTimer() {
        mFinanceDialog.show60msTimer();
    }

    public Button getReSentBtn() {
        return mFinanceDialog.getReSentBtn();
    }


    public void clearPassword() {
        mFinanceDialog.clearPassword();
    }


    public void loadProgress() {
        mFinanceDialog.loadProgress();
    }


    public void disLoadProgress() {
        mFinanceDialog.disLoadProgress();
    }

    public void loadMsgCodeProgress() {
        mFinanceDialog.loadMsgProgress();
    }

    public void disMsgCodeProgress() {
        mFinanceDialog.disMsgCodeProgress();
    }


    private GetOutTradeNoResponse outTradeNoResponse;

    public GetOutTradeNoResponse getOutTradeNoResponse() {
        return outTradeNoResponse;
    }


    public void showPwdErrorResetDialog(final Activity mActivity, String message) {
        DialogBuilder.showSimpleDialog(mActivity, message, null, "重新输入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                iwPayResultListener.onPayPwdTryAgain();
            }
        }, "忘记密码", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mActivity.startActivity(new Intent(mActivity, PayPwdResetActivity.class));
            }
        });
    }

    public static void showPwdLockedErrorDialog(final Activity mActivity, String message) {
        DialogBuilder.showSimpleDialog(mActivity, null, message, null, null, "我知道了", null);
    }

    public void setDialogDismissListener(BaseBuyFinanceDialog.DialogDismissListener dialogDismissListener) {
        this.dialogDismissListener = dialogDismissListener;
    }
}
