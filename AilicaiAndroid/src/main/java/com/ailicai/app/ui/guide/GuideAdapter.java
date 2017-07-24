package com.ailicai.app.ui.guide;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ailicai.app.R;
import com.ailicai.app.common.push.constant.CommonTags;
import com.ailicai.app.ui.index.IndexActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页的适配器
 * Created by jeme on 2017/7/21.
 */

public class GuideAdapter extends PagerAdapter implements View.OnClickListener{

    private List<View> mViews;
    private Activity mActivity;
    private int[] mVTextResIds = new int[]{R.mipmap.guide_text_1,R.mipmap.guide_text_2,R.mipmap.guide_text_3};
    private int[] mVImageResIds = new int[]{R.mipmap.guide_image_1,R.mipmap.guide_image_2,R.mipmap.guide_image_3};
    public GuideAdapter(Activity activity) {
        mActivity = activity;
        mViews = new ArrayList<>(GuideActivity.PAGE_SIZE);
        View vGoIndexBtn;
        LayoutInflater li = LayoutInflater.from(activity);
        for(int i=0;i<GuideActivity.PAGE_SIZE;i++){
            View view = li.inflate(R.layout.guide_item,null);
            ((ImageView)view.findViewById(R.id.iv_text)).setImageResource(mVTextResIds[i]);
            ((ImageView)view.findViewById(R.id.iv_image)).setImageResource(mVImageResIds[i]);
            mViews.add(view);

            if(i == GuideActivity.PAGE_SIZE - 1){
                vGoIndexBtn = view.findViewById(R.id.tv_go_index);
                vGoIndexBtn.setVisibility(View.VISIBLE);
                vGoIndexBtn.setOnClickListener(this);
            }
        }
    }

    @Override
    public int getCount() {
        return GuideActivity.PAGE_SIZE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mActivity, IndexActivity.class);
        Bundle bundle = mActivity.getIntent().getExtras();
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        Uri data = mActivity.getIntent().getData();
        if (null != data) {
            //短信会有此值
            intent.putExtra(CommonTags.URIDATA, data);
        }
        mActivity.startActivity(intent);
        mActivity.finish();
    }
}
