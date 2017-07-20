package com.ailicai.app.ui.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ailicai.app.R;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.common.utils.MapApi;
import com.ailicai.app.common.utils.MyIntent;
import com.ailicai.app.common.utils.ObjectUtil;
import com.ailicai.app.model.response.RegularProductDetailResponse;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.ui.base.webview.WebViewActivity;
import com.ailicai.app.widget.MoreTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 房产宝详情页Fragment
 * Created by liyanan on 16/4/6.
 */
public class RegularFinanceDetailFragment extends BaseBindFragment {

    @Bind(R.id.financing_detail_yearinterestrate)
    TextView mYearInterestRate;
    @Bind(R.id.financing_detail_biddablemount)
    TextView mBiddleAmount;//剩余额度
    @Bind(R.id.financing_detail_horizon)
    TextView mHorizon;
    @Bind(R.id.financing_detail_applyamount)
    TextView mApplyAmount;
    @Bind(R.id.financing_detail_progressbar)
    ProgressBar mProgressBar;//进度条
    @Bind(R.id.financing_detail_protext)
    TextView mProgressText;//申购进度值
    @Bind(R.id.financing_detail_endbuytimestr)
    TextView mEndBuyTimeStr;
    @Bind(R.id.financing_detail_productname)
    TextView mProductName;
    @Bind(R.id.financing_detail_product_demo)
    TextView mProductMemo;
    @Bind(R.id.mtv_buy_demo)
    MoreTextView mtvBuyDemo;
    @Bind(R.id.tv_special_introduction)
    TextView tvSpecialIntroduction;//特别说明
    /**
     * 风险准备金文案
     */
    @Bind(R.id.tv_prepare_money)
    TextView tvPrepareMoney;
    @Bind(R.id.ll_prepare_money)
    LinearLayout llPrepareMoney;
    @Bind(R.id.rl_map)
    RelativeLayout rlMap;
    @Bind(R.id.tv_house_address)
    TextView tvHouseAddress;
    @Bind(R.id.iv_map)
    ImageView ivMap;
    @Bind(R.id.tv_interest_voucher)
    TextView tvInterestVoucher;

    private String id;
    private String moreUrl;
    private String reserveFundMemoUrl;//风险准备金url

    @Override
    public int getLayout() {
        return R.layout.fragment_regular_finance_detail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        id = getArguments().getString(RegularFinancingDetailActivity.PROD_ID);
    }

    /**
     * 点击风险准备金
     */
    @OnClick(R.id.ll_prepare_money)
    void clickPrepareMoney() {
        if (!TextUtils.isEmpty(reserveFundMemoUrl)) {
            Map<String, String> dataMap = ObjectUtil.newHashMap();
            dataMap.put(WebViewActivity.NEED_REFRESH, "0");
            dataMap.put(WebViewActivity.URL, reserveFundMemoUrl);
            dataMap.put(WebViewActivity.TOPVIEWTHEME, String.valueOf(true));
            dataMap.put(WebViewActivity.USEWEBTITLE, String.valueOf(true));
            MyIntent.startActivity(getActivity(), WebViewActivity.class, dataMap);
        }
    }

    /**
     * 请求成功处理Response
     *
     * @param response
     */
    public void handleResponse(final RegularProductDetailResponse response) {
        mYearInterestRate.setText(response.getProduct().getYearInterestRateStr());
        mBiddleAmount.setText(String.format("%s", response.getProduct().getBiddableAmountStr()));
        mApplyAmount.setText(String.format("%s", response.getProduct().getApplyAmountStr()));
        mHorizon.setText(String.format("%s", response.getProduct().getHorizon()));
        mEndBuyTimeStr.setText(String.format("%s", response.getEndBuyTimeStr()));
        mProgressBar.setProgress((int) Math.round(100 * response.getProduct().getHasBuyPrecent
                ()));
        mProgressText.setText(String.format("%s%%", (int) Math.round(100 * response.getProduct()
                .getHasBuyPrecent())));

        List<String> buyDemoList = response.getBuyMemoList();
        StringBuilder sb = new StringBuilder();
        if (buyDemoList != null && buyDemoList.size() > 0) {
            for (int i = 0; i < buyDemoList.size(); i++) {
                sb.append("●  ").append(buyDemoList.get(i)).append("\n");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        mtvBuyDemo.setText(sb.toString());
        mProductName.setText(String.format("产品说明 -%s", response.getProduct().getProductName()));
        mProductMemo.setText(response.getProductMemo());
        moreUrl = response.getMoreDetailUrl();
        if (TextUtils.isEmpty(response.getReserveFund())) {
            //风险准备金文案为空
            llPrepareMoney.setVisibility(View.GONE);
        } else {
            //非空
            llPrepareMoney.setVisibility(View.VISIBLE);
            tvPrepareMoney.setText(response.getReserveFund());
        }
        reserveFundMemoUrl = response.getReserveFundMemoUrl();
        if (!TextUtils.isEmpty(response.getProductSpecMemo())) {
            tvSpecialIntroduction.setVisibility(View.VISIBLE);
            tvSpecialIntroduction.setText("特别说明：" + response.getProductSpecMemo());
        } else {
            tvSpecialIntroduction.setVisibility(View.GONE);
        }
        /***********测试代码**********/
//        response.setLon(121.4947);
//        response.setLat(31.235359);
//        response.setHouseAddress("豫园");
//        response.setInterestVoucher("用券+1.5%");
        /***********测试代码**********/
        if (response.getLat() > 0 && response.getLat() > 0 && !TextUtils.isEmpty(response.getHouseAddress())) {
            rlMap.setVisibility(View.VISIBLE);
            tvHouseAddress.setText(response.getHouseAddress());
            showMapImage(response.getLon(), response.getLat());
        } else {
            rlMap.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(response.getInterestVoucher()) && response.getProduct().getStatus() != 3) {
            tvInterestVoucher.setVisibility(View.VISIBLE);
            tvInterestVoucher.setText(response.getInterestVoucher());
        } else {
            tvInterestVoucher.setVisibility(View.GONE);
        }

    }

    public DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.img_no)// 设置图片在下载期间显示的图片
            .showImageForEmptyUri(R.drawable.img_failed)// 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.img_failed)// 设置图片加载/解码过程中错误时候显示的图片
            .cacheInMemory(true)// 是否缓存都內存中
            .cacheOnDisk(true)// 是否缓存到sd卡上
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.ARGB_8888)
            .build();

    private void showMapImage(double lon, double lat) {
        int height = DeviceUtil.getPixelFromDip(getContext(), 88);
        String url = MapApi.getMapImgUrl(1024, height, 17, lon, lat);
        ImageLoader.getInstance().displayImage(url, ivMap, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                ivMap.setImageResource(R.drawable.img_no);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                ivMap.setImageResource(R.drawable.img_failed);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage != null) {
                    ivMap.setImageBitmap(resizeBitmap(loadedImage));
                } else {
                    ivMap.setImageResource(R.drawable.img_failed);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                ivMap.setImageResource(R.drawable.img_failed);
            }
        });

    }

    private Bitmap resizeBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int offset = DeviceUtil.getPixelFromDip(getContext(), 16);
        return Bitmap.createBitmap(bitmap, 0, 0, width / 2 + offset, height);
    }

    /**
     * 刷新进度和额度值(用于自动刷新)
     */
    public void autoRefresh(double hasBuyPreCent, String biddableAmountStr, int productStatus) {
        mProgressBar.setProgress((int) Math.round(100 * hasBuyPreCent));
        mProgressText.setText(String.format("%s%%", (int) Math.round(100 * hasBuyPreCent)));
        mBiddleAmount.setText(String.format("%s", biddableAmountStr));
        if (productStatus == 3) {
            tvInterestVoucher.setVisibility(View.GONE);
        }
    }

}
