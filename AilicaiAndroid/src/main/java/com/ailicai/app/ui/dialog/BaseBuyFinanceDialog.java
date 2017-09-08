package com.ailicai.app.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.rsa.RSAEncrypt;
import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.ui.buy.IwPayResultListener;
import com.ailicai.app.widget.TextViewTF;
import com.jungly.gridpasswordview.GridPasswordView;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Jer on 2016/1/5.
 */
public class BaseBuyFinanceDialog implements View.OnClickListener, GridPasswordView.OnPasswordChangedListener {

    TextView moneyoutView;
    TextView moneyTextView;
    TextView otherPayTypeView;
    TextView titleView;
    TextViewTF moneyPayIco;
    Dialog dialog;
    // AlertDialog dialog;
    FragmentActivity mActivity;
    GridPasswordView gpvView;
    IPwdDialogListener iPwdDialogListener;
    DialogDismissListener dialogDismissListener;
    IwPayResultListener iwPayResultListener;
    View dialogLayout;
    View changePayTypeView;
    Button reSentBtn;
    CountDownTimer countDownTimer;

/*
    public void update(Dialog dialog) {
        if (!dialog.isShowing() || mContentView == null) {
            return;
        }

        final WindowManager.LayoutParams p =
                (WindowManager.LayoutParams) mDecorView.getLayoutParams();

        boolean update = false;

        final int newAnim = computeAnimationResource();
        if (newAnim != p.windowAnimations) {
            p.windowAnimations = newAnim;
            update = true;
        }

        final int newFlags = computeFlags(p.flags);
        if (newFlags != p.flags) {
            p.flags = newFlags;
            update = true;
        }

        if (update) {
            setLayoutDirectionFromAnchor();
            mWindowManager.updateViewLayout(mDecorView, p);
        }
    }*/
    BuyDialogShowInfo buyDialogShowInfo;


    public BaseBuyFinanceDialog(final FragmentActivity mActivity, IPwdDialogListener iPwdDialogListener) {
        WeakReference<FragmentActivity> weakReference = new WeakReference<>(mActivity);
        this.mActivity = weakReference.get();
        this.iPwdDialogListener = iPwdDialogListener;
    }

    public BaseBuyFinanceDialog createDialog(final IwPayResultListener iwPayResultListener) {
        this.iwPayResultListener = iwPayResultListener;
        dialogLayout = View.inflate(mActivity, R.layout.dialog_bankpay_a_layout, null);
/*        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        dialogLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dialog= alertDialog.setView(dialogLayout).create();*/

        dialog = new Dialog(mActivity, R.style.CustomDialog);//com.android.internal.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        dialog.setContentView(dialogLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        gpvView = (GridPasswordView) dialogLayout.findViewById(R.id.gpv_view);
        gpvView.clearPassword();
        gpvView.setOnPasswordChangedListener(this);
        titleView = (TextView) dialogLayout.findViewById(R.id.titleview);
        moneyoutView = (TextView) dialogLayout.findViewById(R.id.moneyout_view);
        moneyPayIco = (TextViewTF) dialogLayout.findViewById(R.id.money_pay_ico);
        moneyTextView = (TextView) dialogLayout.findViewById(R.id.money_view);
        otherPayTypeView = (TextView) dialogLayout.findViewById(R.id.frompay_view);
        View changePayTypeView = dialogLayout.findViewById(R.id.change_pay_type_view);
        View closeView = dialogLayout.findViewById(R.id.close_button);
        changePayTypeView.setOnClickListener(this);
        closeView.setOnClickListener(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                SystemUtil.HideSoftInput(mActivity);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                if (dialogDismissListener != null) {
                    dialogDismissListener.onDismiss();
                }
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                SystemUtil.HideSoftInput(mActivity);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            }
        });
        return this;
    }

    public void loadProgress() {
        if (dialogLayout == null) {
            return;
        }
        dialog.setCancelable(false);
        dialogLayout.findViewById(R.id.close_button).setVisibility(View.GONE);
        View pwdLayout = dialogLayout.findViewById(R.id.pwd_layout);
        View pwdProgressLayout = dialogLayout.findViewById(R.id.pwd_progress_layout);
        pwdLayout.setVisibility(View.INVISIBLE);
        pwdProgressLayout.setVisibility(View.VISIBLE);
    }

    public void loadMsgProgress() {
        if (dialogLayout == null) {
            return;
        }
        View msgcodeLayout = dialogLayout.findViewById(R.id.msgcode_layout);
        View msgcodeProgressLayout = dialogLayout.findViewById(R.id.msgcode_progress_layout);
        msgcodeLayout.setVisibility(View.INVISIBLE);
        msgcodeProgressLayout.setVisibility(View.VISIBLE);
    }

    public void disMsgCodeProgress() {
        if (dialogLayout == null) {
            return;
        }
        View msgcodeLayout = dialogLayout.findViewById(R.id.msgcode_layout);
        View msgcodeProgressLayout = dialogLayout.findViewById(R.id.msgcode_progress_layout);
        msgcodeLayout.setVisibility(View.VISIBLE);
        msgcodeProgressLayout.setVisibility(View.GONE);
    }

    public void disLoadProgress() {
        if (dialogLayout == null) {
            return;
        }
        dialogLayout.findViewById(R.id.close_button).setVisibility(View.VISIBLE);
        View pwdLayout = dialogLayout.findViewById(R.id.pwd_layout);
        View pwdProgressLayout = dialogLayout.findViewById(R.id.pwd_progress_layout);
        pwdLayout.setVisibility(View.VISIBLE);
        pwdProgressLayout.setVisibility(View.INVISIBLE);
    }

    public void clearPassword() {
        if (gpvView == null) {
            return;
        }
        gpvView.clearPassword();
    }

    public void setViewSwitcherAnimToNull() {
        ViewSwitcher viewSwitcher = (ViewSwitcher) dialogLayout.findViewById(R.id.msg_viewswitch);
        viewSwitcher.setInAnimation(null);
        viewSwitcher.setOutAnimation(null);
    }


/*
    public void update() {
        if (!isShowing() || mContentView == null) {
            return;
        }

        final WindowManager.LayoutParams p =
                (WindowManager.LayoutParams) mDecorView.getLayoutParams();

        boolean update = false;

        final int newAnim = computeAnimationResource();
        if (newAnim != p.windowAnimations) {
            p.windowAnimations = newAnim;
            update = true;
        }

        final int newFlags = computeFlags(p.flags);
        if (newFlags != p.flags) {
            p.flags = newFlags;
            update = true;
        }

        if (update) {
            setLayoutDirectionFromAnchor();
            mWindowManager.updateViewLayout(mDecorView, p);
        }
    }*/

    /**
     * 自定义title，msg
     *
     * @param mobilePhone
     * @param customTitle
     * @param msg
     * @param doneOnClickListener
     * @param sendMsgCodeListener
     */
    public void changeViewToMsgView(String mobilePhone, String customTitle, String msg, final View.OnClickListener doneOnClickListener, final View.OnClickListener sendMsgCodeListener) {
        changeViewToMsgView(mobilePhone, doneOnClickListener, sendMsgCodeListener);
        titleView.setText(customTitle);
        ((TextView) dialogLayout.findViewById(R.id.message_content_txt)).setText(msg);
        dialogLayout.findViewById(R.id.phone_txt).setVisibility(View.GONE);
    }

    public void changeViewToMsgView(String mobilePhone, final View.OnClickListener doneOnClickListener, final View.OnClickListener sendMsgCodeListener) {
        titleView.setText("付款验证");
        dialogLayout.findViewById(R.id.msgcode_layout).setVisibility(View.VISIBLE);
        dialogLayout.findViewById(R.id.close_button).setVisibility(View.GONE);
        ((TextView) dialogLayout.findViewById(R.id.phone_txt)).setText(mobilePhone);
        reSentBtn = (Button) dialogLayout.findViewById(R.id.msg_resend_btn);
        reSentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsgCodeListener.onClick(v);
            }
        });
        ViewSwitcher viewSwitcher = (ViewSwitcher) dialogLayout.findViewById(R.id.msg_viewswitch);
        viewSwitcher.setDisplayedChild(1);
        final EditText messageEdit = (EditText) dialogLayout.findViewById(R.id.message_edit);
        messageEdit.requestFocus();
        View messageDoneBtn = dialogLayout.findViewById(R.id.message_done_btn);
        dialogLayout.findViewById(R.id.message_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDismiss();
            }
        });
        messageDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtil.hideKeyboard(messageEdit);
                v.setTag(messageEdit.getText().toString());
                String verifyCode = messageEdit.getText().toString();
                if (!TextUtils.isEmpty(verifyCode)
                        && !TextUtils.isEmpty(verifyCode.replace(" ", ""))
                        && verifyCode.replace(" ", "").length() >= 4) {
                    doneOnClickListener.onClick(v);
                } else {
                    ToastUtil.showInCenter("请输入有效的短信验证码");
                }
            }
        });
        titleView.requestFocus();
        show60msTimer();
    }

    public Button getReSentBtn() {
        return reSentBtn;
    }

    public void show60msTimer() {
        countDownTimer = new CountDownTimer(1000 * 60, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                reSentBtn.setEnabled(false);
                reSentBtn.setText((millisUntilFinished / 1000) + "秒后重新获取");
            }

            @Override
            public void onFinish() {
                reSentBtn.setEnabled(true);
                reSentBtn.setText("重新获取");
            }
        }.start();
    }

    public void show() {
        if (mActivity != null && dialog != null) {
            if (!dialog.isShowing()) dialog.show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (mActivity != null) {
                        SystemUtil.showOrHideSoftInput(mActivity);
                    }
                }
            }, 300);
        }
    }

    public BaseBuyFinanceDialog setBuyDialogShowInfo(BuyDialogShowInfo buyDialogShowInfo) {
        this.buyDialogShowInfo = buyDialogShowInfo;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_button:
                dialog.dismiss();
                //      iwPayResultListener.onPayFailInfo("点击关闭取消支付", iwjw_pay_cancel_type_code + "");
                break;
            case R.id.change_pay_type_view:
                // dialog.dismiss();
                break;
        }
    }

    public void onDismiss() {
        if (dialog == null) {
            return;
        }
        dialog.dismiss();
    }

    @Override
    public void onTextChanged(String s) {

    }

    @Override
    public void onInputFinish(String s) {
        //  dialog.dismiss();
        String payPwd;
        try {
            payPwd = RSAEncrypt.encrypt(s);
            iPwdDialogListener.onInputFinish(payPwd);
        } catch (Exception e) {
            e.printStackTrace();
            iPwdDialogListener.onInputFinish(null);
        }
    }

    public BaseBuyFinanceDialog create() {
        if(!TextUtils.isEmpty(buyDialogShowInfo.title)){
            titleView.setText(buyDialogShowInfo.title);
        }
        if (buyDialogShowInfo.getAmount() == 0) {
            moneyTextView.setVisibility(View.GONE);
        } else {
            moneyTextView.setText(CommonUtil.amountWithTwoAfterPoint(buyDialogShowInfo.getAmount()) + "元");
        }
        if (TextUtils.isEmpty(buyDialogShowInfo.getPayTypFrom())) {
            dialogLayout.findViewById(R.id.change_pay_type_view).setVisibility(View.GONE);
            dialogLayout.findViewById(R.id.change_pay_type_line).setVisibility(View.GONE);
            dialogLayout.findViewById(R.id.msgcode_layout).setVisibility(View.GONE);
            View pwdViewFrame = dialogLayout.findViewById(R.id.pwdView_frame);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) pwdViewFrame.getLayoutParams();
            layoutParams.bottomMargin = MyApplication.getInstance().getResources().getDimensionPixelOffset(R.dimen._32);
        } else {
            otherPayTypeView.setText(String.valueOf(buyDialogShowInfo.getPayTypFrom()));
            /*
            if ("1".equals(buyDialogShowInfo.getPayMethod())) {
            } else if ("2".equals(buyDialogShowInfo.getPayMethod())) {
                //使用账户余额支付
            }
            */
            moneyPayIco.setText(buyDialogShowInfo.getPayTypFromIco());
        }
        String showInfo = String.valueOf(buyDialogShowInfo.getMoneyOutStr());
        if(TextUtils.isEmpty(showInfo)){
            moneyoutView.setVisibility(View.GONE);
        }else{
            moneyoutView.setText(showInfo);
            moneyoutView.setVisibility(View.VISIBLE);
        }
//        moneyoutView.setText(String.valueOf(buyDialogShowInfo.getMoneyOutStr()));
        return this;
    }

    public void setDialogDismissListener(DialogDismissListener dialogDismissListener) {
        this.dialogDismissListener = dialogDismissListener;
    }

    public interface IPwdDialogListener {
        void onInputFinish(String s);
    }

    public interface DialogDismissListener {
        void onDismiss();
    }
}
