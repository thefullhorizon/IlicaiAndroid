package com.ailicai.app.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.view.WindowManager;

import com.ailicai.app.R;


public abstract class MyBaseDialog extends BaseDialogFragment {

    protected static int MYTHEME1 = R.style.Theme_Base_Dialog_Fragment_1;
    protected static int MYTHEME2 = R.style.Theme_Base_Dialog_Fragment_2;
    protected static int MYTHEME3 = R.style.Theme_Base_Dialog_Fragment_3;
    protected static int MYTHEME4 = R.style.Theme_Base_Dialog_Fragment_4;
    protected static int MYTHEME5 = R.style.Theme_Base_Dialog_Fragment_5;
    protected static int MYTHEME6 = R.style.Theme_Base_Dialog_Fragment_6;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());
    }

    @Override
    public void setDialogLocation() {
        this.getDialog().setCancelable(cancelable());
        this.getDialog().setCanceledOnTouchOutside(cancelable());
        Window window = this.getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = displayWindowLocation();
        window.setAttributes(lp);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public abstract int getTheme();

    public abstract int displayWindowLocation();

    public abstract boolean cancelable();

}
