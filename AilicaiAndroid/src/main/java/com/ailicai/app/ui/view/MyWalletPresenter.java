package com.ailicai.app.ui.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ailicai.app.R;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.model.bean.FundYeildRateModel;
import com.ailicai.app.model.request.WalletInfoRequest;
import com.ailicai.app.model.response.WalletInfoResponse;
import com.ailicai.app.widget.RateMarkView;
import com.ailicai.app.widget.mpchart.charts.LineChart;
import com.ailicai.app.widget.mpchart.components.XAxis;
import com.ailicai.app.widget.mpchart.components.YAxis;
import com.ailicai.app.widget.mpchart.data.Entry;
import com.ailicai.app.widget.mpchart.data.LineData;
import com.ailicai.app.widget.mpchart.data.LineDataSet;
import com.ailicai.app.widget.mpchart.formatter.YAxisValueFormatter;
import com.ailicai.app.widget.mpchart.interfaces.datasets.ILineDataSet;
import com.ailicai.app.widget.mpchart.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by duo.chen on 2016/7/4.11:17
 */

public class MyWalletPresenter {

    private WeakReference<MyWalletActivity> activityWeakReference;

    public MyWalletPresenter(MyWalletActivity activity) {
        activityWeakReference = new WeakReference<MyWalletActivity>(activity);
    }

    public MyWalletActivity getWeakReference() {
        return activityWeakReference.get();
    }

    public void requestMyWalletInfo() {

        if (null != getWeakReference()) {
            WalletInfoRequest walletInfoRequest = new WalletInfoRequest();
            ServiceSender.exec(getWeakReference(), walletInfoRequest, new
                    IwjwRespListener<WalletInfoResponse>() {

                @Override
                public void onStart() {
                    super.onStart();
                    if (null != getWeakReference()) {
                        getWeakReference().showLoadTranstView();
                    }
                }

                @Override
                public void onJsonSuccess(WalletInfoResponse jsonObject) {
                    if (null != getWeakReference()) {
                        getWeakReference().showContentView();
                        getWeakReference().setMywalletInfo(jsonObject);
                    }
                }

                @Override
                public void onFailInfo(String errorInfo) {
                    super.onFailInfo(errorInfo);
                    if (null != getWeakReference()) {
                        getWeakReference().showErrorView(errorInfo);
                    }
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (null != getWeakReference()) {
                        getWeakReference().onLoadFinish();
                    }
                }
            });
        }

    }


    public void initChart(LineChart mChart, final List<FundYeildRateModel> fundYeildRateModels) {
        if (null != getWeakReference()) {
            if (mChart.getData() == null) {
                mChart.setVisibility(View.VISIBLE);
                mChart.setTouchEnabled(true);
                mChart.setDragEnabled(false);
                mChart.setScaleEnabled(false);
                mChart.setPinchZoom(false);
                mChart.setMinOffset(0f);
                mChart.setExtraOffsets(6,18,6,0);
                mChart.setDescription(" ");

                YAxis leftAxis = mChart.getAxisLeft();
                leftAxis.removeAllLimitLines();
                leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
                leftAxis.setYOffset(-8f);
                leftAxis.setXOffset(2f);
                leftAxis.setDrawZeroLine(false);
                leftAxis.setDrawLimitLinesBehindData(true);
                leftAxis.setTextColor(Color.parseColor("#757575"));
                leftAxis.setTextSize(11f);
                leftAxis.setAxisLineColor(Color.parseColor("#e6e6e6"));
                leftAxis.setGridColor(Color.parseColor("#e6e6e6"));
                leftAxis.setAxisLineWidth(1f);
                leftAxis.setGridLineWidth(1.5f);
                leftAxis.setValueFormatter(new MyYAxisValueFormatter());

                XAxis xAxis = mChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextColor(Color.parseColor("#757575"));
                xAxis.setTextSize(11f);
                xAxis.setAxisLineColor(Color.parseColor("#e6e6e6"));
                xAxis.setGridColor(Color.parseColor("#e6e6e6"));
                xAxis.setAxisLineWidth(1f);
                xAxis.setGridLineWidth(1.5f);
                xAxis.setAvoidFirstLastClipping(true);

                List<String> XList = new ArrayList<>();
                float[] yList = new float[fundYeildRateModels.size()];
                float[] yListSort = new float[fundYeildRateModels.size()];
                List<Entry> entries = new ArrayList<>();
                for (int i = 0; i < fundYeildRateModels.size(); i++) {
                    XList.add(fundYeildRateModels.get(i).getDate());
                    yList[i] = Float.valueOf(fundYeildRateModels.get(i).getYeildRate());
                    LogUtil.i("LineChart","yList[i] " + yList[i]);
                    yListSort[i] = Float.valueOf(fundYeildRateModels.get(i).getYeildRate());
                    entries.add(new Entry(Float.valueOf(fundYeildRateModels.get(i).getYeildRate()
                    ), i));

                }
                Arrays.sort(yListSort);

                leftAxis.setLabelCount(6, true);

                leftAxis.setAxisMaxValue(yListSort[yListSort.length - 1] * 1.02f);
                leftAxis.setAxisMinValue(yListSort[0] * 0.980f);

                LogUtil.i("LineChart","yListSort[yListSort.length - 1] * 1.100f " + yListSort[yListSort.length - 1] * 1.100f);

                LineDataSet set = new LineDataSet(entries, "");

                set.setColor(Color.RED);
                set.setCircleColor(Color.RED);
                set.setCircleColorHole(Color.WHITE);
                set.setLineWidth(2f);
                set.setDrawCircleHole(false);
                set.setDrawVerticalHighlightIndicator(false);
                set.setDrawHorizontalHighlightIndicator(false);
                set.setCircleRadius(0f);
                set.setDrawValues(false);
                set.setDrawFilled(true);
                set.setHighlightEnabled(true);

                if (Utils.getSDKInt() >= 18) {
                    Drawable drawable = ContextCompat.getDrawable(getWeakReference(), R.drawable
                            .fade_red);

                    set.setFillDrawable(drawable);
                }
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set);
                LineData lineData = new LineData(XList, dataSets);
                mChart.setData(lineData);
                mChart.getAxisRight().setEnabled(false);

                RateMarkView rateMarkView = new RateMarkView(getWeakReference(), R.layout
                        .wallet_rate_mark_view_layout, yList);

                rateMarkView.setListener(new RateMarkView.updateContentListener() {
                    @Override
                    public void updateContent(int index) {
                        getWeakReference().updateContent(fundYeildRateModels.get(index).getUnitIncome());
//                        EventLog.upEventLog("354","7_day_ yield",4);
                    }
                });

                rateMarkView.setChartView(mChart);
                mChart.setMarkerView(rateMarkView);
                mChart.highlightValue(fundYeildRateModels.size() - 1, 0);
                mChart.animateY(1500);
            } else {
                //mChart.animateY(1500);
                mChart.highlightValue(fundYeildRateModels.size() - 1, 0);
            }
        }
    }

    public static class MyYAxisValueFormatter implements YAxisValueFormatter {

        public MyYAxisValueFormatter() {
        }

        @SuppressLint("DefaultLocale")
        @Override
        public String getFormattedValue(float value, YAxis yAxis) {
            return String.format("%.3f",value);
        }
    }


}
