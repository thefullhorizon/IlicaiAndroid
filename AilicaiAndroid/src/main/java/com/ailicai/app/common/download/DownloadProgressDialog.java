package com.ailicai.app.common.download;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.ailicai.app.R;

import java.text.NumberFormat;

/**
 * Created by David on 15/6/15.
 */
public class DownloadProgressDialog extends AlertDialog {


    private ProgressBar mProgress;

    private TextView mProgressMessage;

    private TextView mProgressPercent;

    private TextView tvSizeDesc;

    private TextView tvLine;

    private TextView tvTotalSizeDesc;

    private TextView tvOriTotalSize;

    private NumberFormat mProgressPercentFormat;

    private CharSequence mMessage;

    private int mMax, mProgressVal;

    private boolean mStart;


    protected DownloadProgressDialog(Context context) {
        super(context);
    }

    protected DownloadProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_progress_dialog);
        mProgress = (ProgressBar) this.findViewById(R.id.download_progress_bar);
        mProgressMessage = (TextView) this.findViewById(R.id.download_progress_message);
        mProgressPercent = (TextView) this.findViewById(R.id.download_progress_precent);
        tvSizeDesc = (TextView) this.findViewById(R.id.tvSizeDesc);
        tvTotalSizeDesc = (TextView) this.findViewById(R.id.tvTotalSizeDesc);
        tvLine = (TextView) this.findViewById(R.id.tvLine);
        // 增量更新时候原大小
        tvOriTotalSize = (TextView) this.findViewById(R.id.tvOriTotalSize);
        tvOriTotalSize.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线

        initNumberFormat();

        onProgressChanged();
        if (mMessage != null) {
            setMessage(mMessage);
        }
        if (mMax > 0) {
            setMax(mMax);
        }
        if (mProgressVal > 0) {
            setProgress(mProgressVal);
        }
    }

    private void initNumberFormat() {
        mProgressPercentFormat = NumberFormat.getPercentInstance();
        mProgressPercentFormat.setMaximumFractionDigits(0);
    }

    @Override
    public void setMessage(CharSequence message) {
        if (mProgressMessage != null) {
            mProgressMessage.setText(message);
        } else {
            mMessage = message;
        }
    }

    public void setAlreadyDownloadAndTotalSize(String alreadySizeDesc, String totalSizeDesc) {
        tvSizeDesc.setText(alreadySizeDesc);
        tvTotalSizeDesc.setText(totalSizeDesc);

        tvSizeDesc.setVisibility(View.VISIBLE);
        tvTotalSizeDesc.setVisibility(View.VISIBLE);
        tvLine.setVisibility(View.VISIBLE);
    }


    public void setMax(int max) {
        if (mProgress != null) {
            mProgress.setMax(max);
            onProgressChanged();
        } else {
            mMax = max;
        }
    }

    private void onProgressChanged() {

        int progress = mProgress.getProgress();
        int max = mProgress.getMax();

        if (mProgressPercentFormat != null && max != 0) {
            double percent = (double) progress / (double) max;
            SpannableString tmp = new SpannableString(mProgressPercentFormat.format(percent));
            tmp.setSpan(new StyleSpan(Typeface.NORMAL),
                    0, tmp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mProgressPercent.setText(tmp);
        } else {
            mProgressPercent.setText("");
        }
    }

    public void setProgress(int value) {
        if (mStart) {
            mProgress.setProgress(value);
            onProgressChanged();
        } else {
            mProgressVal = value;
        }
    }

    public void setOriTotalSize(String oriTotalSize) {
        tvOriTotalSize.setVisibility(View.VISIBLE);
        tvOriTotalSize.setText(oriTotalSize);
    }

    public void setOriTotalSizeInvisible() {
        tvOriTotalSize.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mStart = true;
    }


    @Override
    protected void onStop() {
        super.onStop();
        mStart = false;
    }

    @Override
    public void dismiss() {
        int progress = mProgress.getProgress();
        int max = mProgress.getMax();
        if (progress >= max) {
            setMax(0);
            setProgress(0);
        }
        super.dismiss();
    }
}
