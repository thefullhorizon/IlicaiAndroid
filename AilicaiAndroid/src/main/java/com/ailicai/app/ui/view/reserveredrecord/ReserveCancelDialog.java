package com.ailicai.app.ui.view.reserveredrecord;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.ui.dialog.MyBaseDialog;

import butterknife.ButterKnife;

/**
 * Created by Owen on 2015/3/11
 */
public class ReserveCancelDialog extends MyBaseDialog implements View.OnClickListener {

    private ReserveCancelEvent reserveCancelEvent;

    @Override
    public int getLayout() {
        return R.layout.dialog_booking_record;
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

    @Override
    public void setupView(View rootView, Bundle bundle) {
        setDialogSize(rootView, DeviceUtil.getScreenSize()[0], LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView tvCancelBooking = ButterKnife.findById(rootView, R.id.tvCancelBooking);
        TextView tvCancel = ButterKnife.findById(rootView, R.id.tvCancel);

        tvCancelBooking.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

    }

    private void setDialogSize(View rootView, int width, int height) {
        LinearLayout rootLayout = (LinearLayout) rootView.findViewById(R.id.dialog_root);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(width, height);
        rootLayout.setLayoutParams(lp1);
    }

    @Override
    public void setupData(Bundle bundle) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancelBooking:
                dismiss();
                reserveCancelEvent.cancelBooking();
                break;
            case R.id.tvCancel:
                dismiss();
                break;
            default:
                break;
        }
    }


    public interface ReserveCancelEvent {
        void cancelBooking();
    }

    public void setReserveCancleEvent(ReserveCancelEvent event) {
        this.reserveCancelEvent = event;
    }

}