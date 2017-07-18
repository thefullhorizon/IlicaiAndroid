package com.ailicai.app.ui.view.reserveredrecord;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.model.bean.Product;
import com.ailicai.app.model.bean.ReserveRecordBuyListBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Owen on 2016/3/10
 */
public class ReserveRecordListAdapter extends BaseAdapter implements View.OnClickListener {

    private ReserveRecordListActivity activity;
    private List<Product> listData = new ArrayList<>();
    public List<String> positionList = new ArrayList<>();
    public Map<String, List<ReserveRecordBuyListBean>> mapList = new HashMap<>();

    public ReserveRecordListAdapter(ReserveRecordListActivity activity, List<Product> listData) {
        this.activity = activity;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Product getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHodle hodle;
        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.item_booking_record, null);
            hodle = new ViewHodle(view);
            view.setTag(hodle);
        } else {
            hodle = (ViewHodle) view.getTag();
        }

        hodle.position = position;

        final Product bean = getItem(position);
        hodle.tvBookingTime.setText(bean.getOrderTimeStr());
        hodle.tvBookingPrice.setText(bean.getBidAmountStr());
        hodle.tvTransactionPrice.setText(bean.getOrderDealAmount());
        hodle.tvBookingCode.setText(bean.getProductName());
        hodle.tvBookingDays.setText(bean.getHorizonStr());

        hodle.tvBookingPrice.setTextColor(Color.parseColor("#de000000"));
        hodle.tvPriceHint.setTextColor(Color.parseColor("#8a000000"));
        hodle.tvBookingCode.setTextColor(Color.parseColor("#de000000"));
        hodle.tvBookingDays.setTextColor(Color.parseColor("#de000000"));

        if (bean.isCanbecancel()) {
            hodle.tfMore.setVisibility(View.VISIBLE);
        } else {
            hodle.tfMore.setVisibility(View.GONE);
        }

        if (bean.getOrderStatus() == 2) {
            hodle.tvCancel.setText("等待成交");
            hodle.tvCancel.setTextColor(Color.parseColor("#e84a01"));
            hodle.tvCancel.setVisibility(View.VISIBLE);
            hodle.rlTransactionPrice.setVisibility(View.GONE);
            hodle.llShowBuy.setVisibility(View.GONE);
        } else if (bean.getOrderStatus() == 5) {
            hodle.tvCancel.setText("已取消");
            hodle.tvCancel.setTextColor(Color.parseColor("#757575"));
            hodle.tvCancel.setVisibility(View.VISIBLE);
            hodle.rlTransactionPrice.setVisibility(View.GONE);
            hodle.llShowBuy.setVisibility(View.GONE);
            hodle.tvBookingPrice.setTextColor(Color.parseColor("#8f8e94"));
            hodle.tvPriceHint.setTextColor(Color.parseColor("#8f8e94"));
            hodle.tvBookingCode.setTextColor(Color.parseColor("#8a000000"));
            hodle.tvBookingDays.setTextColor(Color.parseColor("#8a000000"));
        } else if (bean.getOrderStatus() == 6) {
            hodle.rlTransactionPrice.setVisibility(View.VISIBLE);
            hodle.tvCancel.setVisibility(View.GONE);
            hodle.llShowBuy.setVisibility(View.VISIBLE);
        } else {
            hodle.tvCancel.setVisibility(View.GONE);
            hodle.rlTransactionPrice.setVisibility(View.VISIBLE);
            hodle.llShowBuy.setVisibility(View.GONE);
        }

        if (bean.isShow()) {
            bean.setShow(true);
            getBuyListData(hodle, false);
            hodle.tfArrow.setText(activity.getResources().getString(R.string.chevous_up));
        } else {
            bean.setShow(false);
            hodle.llBuyList.setVisibility(View.GONE);
            hodle.tfArrow.setText(activity.getResources().getString(R.string.chevous_down));
        }

        hodle.tfMore.setTag(hodle);
        hodle.llShowBuy.setTag(hodle);
        hodle.tfMore.setOnClickListener(this);
        hodle.llShowBuy.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        ViewHodle hodle = (ViewHodle) v.getTag();
        switch (v.getId()) {
            case R.id.tfMore:
                activity.setProductBean(getItem(hodle.position));
                activity.showCancelDialog();
                break;
            case R.id.llShowBuy:
                if (positionList.contains(hodle.position + "")) {
                    getItem(hodle.position).setShow(false);
                    positionList.remove(hodle.position + "");
//                    startAnimation(hodle.llBuyList, hodle.position);
                    hodle.llBuyList.setVisibility(View.GONE);
                    hodle.tfArrow.setText(activity.getResources().getString(R.string.chevous_down));
                } else {
                    getItem(hodle.position).setShow(true);
                    getBuyListData(hodle, true);
                    positionList.add(hodle.position + "");
                    hodle.tfArrow.setText(activity.getResources().getString(R.string.chevous_up));
                }
                break;
            default:
                break;
        }
    }

    private void getBuyListData(ViewHodle hodle, boolean isClick) {
        if (mapList.keySet().contains(hodle.position + "")) {
            List<ReserveRecordBuyListBean> rowBean = mapList.get(hodle.position + "");
            setBuyList(rowBean, hodle, isClick);
        } else {
            activity.requestForGetBuyListData(getItem(hodle.position).getBidOrderNo(), hodle);
        }
    }

    public void setBuyList(List<ReserveRecordBuyListBean> rowBean, ViewHodle hodle) {
        setBuyList(rowBean, hodle, true);
    }

    public void setBuyList(List<ReserveRecordBuyListBean> rowBean, ViewHodle hodle, boolean isClick) {
        if (rowBean == null) return;
        if (rowBean.size() > 0) {
            hodle.llBuyList.removeAllViews();
            mapList.put(hodle.position + "", rowBean);
            for (int i = 0; i < rowBean.size(); i++) {
                View v = activity.getLayoutInflater().inflate(R.layout.item_booking_record_buy, null);
                TextView buyName = (TextView) v.findViewById(R.id.buyName);
                TextView buyDay = (TextView) v.findViewById(R.id.buyDay);
                TextView buyYield = (TextView) v.findViewById(R.id.buyYield);
                TextView buyPrice = (TextView) v.findViewById(R.id.buyPrice);
                TextView buyPriceHint = (TextView) v.findViewById(R.id.buyPriceHint);
                TextView buyTime = (TextView) v.findViewById(R.id.buyTime);
                TextView buyTimeHint = (TextView) v.findViewById(R.id.buyTimeHint);
                ImageView ivError = (ImageView) v.findViewById(R.id.ivError);
                TextView tfIcon = (TextView) v.findViewById(R.id.tfIcon);

                ReserveRecordBuyListBean bean = rowBean.get(i);
                buyName.setText(bean.getProductName());
                buyDay.setText(bean.getHorizonStr());
                buyYield.setText(bean.getYearInterestRateStr());
                buyPrice.setText(bean.getBidAmountStr());
                buyTime.setText(bean.getBidTimeStr());
                if (bean.getStatus() == 3) {
                    tfIcon.setTextColor(Color.parseColor("#757575"));
                    ivError.setVisibility(View.VISIBLE);
                    buyName.setTextColor(Color.parseColor("#757575"));
                    buyDay.setTextColor(Color.parseColor("#757575"));
                    buyYield.setTextColor(Color.parseColor("#757575"));
                    buyPrice.setTextColor(Color.parseColor("#757575"));
                    buyPriceHint.setTextColor(Color.parseColor("#757575"));
                    buyTime.setTextColor(Color.parseColor("#757575"));
                    buyTimeHint.setTextColor(Color.parseColor("#757575"));
                } else {
                    tfIcon.setTextColor(Color.parseColor("#e84a01"));
                    ivError.setVisibility(View.GONE);
                    buyName.setTextColor(Color.parseColor("#212121"));
                    buyDay.setTextColor(Color.parseColor("#212121"));
                    buyYield.setTextColor(Color.parseColor("#212121"));
                    buyPrice.setTextColor(Color.parseColor("#212121"));
                    buyPriceHint.setTextColor(Color.parseColor("#212121"));
                    buyTime.setTextColor(Color.parseColor("#212121"));
                    buyTimeHint.setTextColor(Color.parseColor("#212121"));
                }

                hodle.llBuyList.addView(v);
            }

//            if (isClick) {
//                startAnimation(hodle.llBuyList, hodle.position);
//            } else {
            hodle.llBuyList.setVisibility(View.VISIBLE);
//            }
        }
    }

    private void startAnimation(View expand, final int position) {
        ExpandAnimation expandAnimation = new ExpandAnimation(expand, 200);
        expand.startAnimation(expandAnimation);
        expandAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                activity.smoothScrollToPosition(position);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    static class ExpandAnimation extends Animation {
        private View mAnimationView = null;
        private LinearLayout.LayoutParams mViewLayoutParams = null;
        private int mStart = 0;
        private int mEnd = 0;

        public ExpandAnimation(View view, int duration) {
            initialize(view, duration);
        }

        private void initialize(View view, int duration) {
            setDuration(duration);
            mAnimationView = view;
            mViewLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            mStart = mViewLayoutParams.bottomMargin;
            mEnd = (mStart == 0 ? (0 - view.getHeight()) : 0);
            view.setVisibility(View.VISIBLE);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);

            if (interpolatedTime < 1.0f) {
                mViewLayoutParams.bottomMargin = mStart + (int) ((mEnd - mStart) * interpolatedTime);
                // invalidate
                mAnimationView.requestLayout();
            } else {
                mViewLayoutParams.bottomMargin = mEnd;
                mAnimationView.requestLayout();
                if (mEnd != 0) {
                    mAnimationView.setVisibility(View.GONE);
                }
            }
        }
    }

    class ViewHodle {
        int position = -1;

        TextView tvBookingTime;
        TextView tvBookingPrice;
        TextView tvPriceHint;
        RelativeLayout rlTransactionPrice;
        TextView tvTransactionPrice;
        TextView tvCancel;
        TextView tvBookingCode;
        TextView tvBookingDays;
        TextView tfMore;
        LinearLayout llShowBuy;
        TextView tfArrow;
        LinearLayout llBuyList;

        ViewHodle(View v) {
            tvBookingTime = (TextView) v.findViewById(R.id.tvBookingTime);
            tvBookingPrice = (TextView) v.findViewById(R.id.tvBookingPrice);
            tvPriceHint = (TextView) v.findViewById(R.id.tvPriceHint);
            rlTransactionPrice = (RelativeLayout) v.findViewById(R.id.rlTransactionPrice);
            tvTransactionPrice = (TextView) v.findViewById(R.id.tvTransactionPrice);
            tvCancel = (TextView) v.findViewById(R.id.tvCancel);
            tvBookingCode = (TextView) v.findViewById(R.id.tvBookingCode);
            tvBookingDays = (TextView) v.findViewById(R.id.tvBookingDays);
            tfMore = (TextView) v.findViewById(R.id.tfMore);
            llShowBuy = (LinearLayout) v.findViewById(R.id.llShowBuy);
            tfArrow = (TextView) v.findViewById(R.id.tfArrow);
            llBuyList = (LinearLayout) v.findViewById(R.id.llBuyList);
        }
    }
}
