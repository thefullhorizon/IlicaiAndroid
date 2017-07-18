package com.ailicai.app.ui.view.banner;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ailicai.app.R;
import com.ailicai.app.common.imageloader.ImageLoaderClient;
import com.ailicai.app.common.utils.DeviceUtil;
import com.ailicai.app.model.bean.Banner;
import com.huoqiu.framework.imageloader.core.LoadParam;

import java.util.List;

/**
 * Created by nanshan on 5/22/2017.
 */

public class PayResultBannerPagerAdapter extends PagerAdapter {

    private FragmentActivity mActivity;
    List<Banner> bannerList = null;
    private String bannerLocation = "";

    public PayResultBannerPagerAdapter(FragmentActivity activity, List<Banner> bannerList, String bannerLocation) {
        super();
        this.mActivity = activity;
        this.bannerList = bannerList;
        this.bannerLocation = bannerLocation;
    }

    @Override
    public int getCount() {
        if (bannerList == null || bannerList.isEmpty()) {
            return 0;
        } else if (bannerList.size() == 1) {
            return 1;
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(android.view.View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((android.view.View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position %= bannerList.size();
        if (position < 0) {
            position = bannerList.size() + position;
        }
        final Banner banner = bannerList.get(position);
        final int bannerIndex = position;
        ImageView imageView = new ImageView(mActivity);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LoadParam param = new LoadParam();
        param.setEmptyPicId(R.drawable.index_banner_default);
        param.setFailPicId(R.drawable.index_banner_default);
        param.setLoadingPicId(R.drawable.index_banner_default);
        ImageLoaderClient.display(mActivity, imageView, banner.getImageUrl(), param);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO nanshan Banner埋点的上传
                /**
                MainIntentUtil.processBannerClickOnPayResult(mActivity, banner);
                // 上报点击量埋点
                Map logMap = new HashMap();
                logMap.put("banner_id", banner.getBannerId());
                logMap.put("banner_l", bannerLocation);
                EventLog.upEventLog("20170519002", JSON.toJSONString(logMap));
                */
            }
        });
        container.addView(imageView);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        int[] screenSize = DeviceUtil.getScreenSize();
        int screenWidth = screenSize[0];
        // 设置宽高比为4:1
        layoutParams.width = screenWidth;
        layoutParams.height = screenWidth / 4;
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }
}
