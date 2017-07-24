package com.ailicai.app.ui.guide;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ailicai.app.R;
import com.ailicai.app.ui.base.BaseBindActivity;

import butterknife.Bind;

/**
 * 引导页
 * Created by jeme on 2017/7/21.
 */

public class GuideActivity extends BaseBindActivity implements ViewPager.OnPageChangeListener{

    @Bind(R.id.vp_guide)
    ViewPager mVpGuide;
    @Bind(R.id.ll_guide_point)
    LinearLayout mLlGuidePoint;

    public static final int PAGE_SIZE = 3;

    private ImageView[] mIvPointArray;

    @Override
    public int getLayout() {
        return R.layout.activity_guide;
    }
    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initViewPager();
        initPoint();
    }

   private void initViewPager(){

       mVpGuide.setAdapter(new GuideAdapter(this));
       mVpGuide.addOnPageChangeListener(this);
   }

    /***
     * 初始化底部引导点
     */
   private void initPoint(){
        mIvPointArray = new ImageView[PAGE_SIZE];
       int margin = getResources().getDimensionPixelOffset(R.dimen.guide_point_padding);
       int size = getResources().getDimensionPixelSize(R.dimen.guide_point_size);
       ImageView ivPoint;
       for (int i = 0;i<PAGE_SIZE;i++){
           ivPoint = new ImageView(this);
           LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size,size);
           params.setMargins(margin,0,margin,0);
           ivPoint.setLayoutParams(params);
           mIvPointArray[i] = ivPoint;

           //第一个页面需要设置为选中状态，这里采用两张不同的图片
           if (i == 0){
               ivPoint.setBackgroundResource(R.drawable.red_point_bg);
           }else{
               ivPoint.setBackgroundResource(R.drawable.gray_point_bg);
           }
           //将数组中的ImageView加入到ViewGroup
           mLlGuidePoint.addView(mIvPointArray[i]);

       }
   }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int i=0;i<PAGE_SIZE;i++){
            mIvPointArray[i].setBackgroundResource(position == i ? R.drawable.red_point_bg:
                    R.drawable.gray_point_bg);
        }
        mLlGuidePoint.setVisibility(position == PAGE_SIZE - 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
