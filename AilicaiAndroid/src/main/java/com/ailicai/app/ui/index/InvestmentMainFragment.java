package com.ailicai.app.ui.index;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.ailicai.app.R;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.widget.NoScrollViewPager;
import com.ailicai.app.widget.slidingtab.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 投资主页面
 */
public class InvestmentMainFragment extends BaseBindFragment implements ViewPager.OnPageChangeListener {
    @Bind(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTabLayout;
    @Bind(R.id.view_pager)
    NoScrollViewPager mViewPager;

    private OurViewPagerAdapter mViewPagerAdapter;

    @Override
    public int getLayout() {
        return R.layout.investment_fragment;
    }

    @Override
    public void init(Bundle savedInstanceState) {
//        CommonUtil.uiSystemBarTint(getWRActivity());
        inittabView();
    }

    public void inittabView() {
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator2, android.R.id.text1);
        mSlidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(getWRActivity(), R.color.main_red_color));// 设置下面的线颜色
        mSlidingTabLayout.setDistributeEvenly(true);
        iniTab();
    }

    private void iniTab() {
        mViewPagerAdapter = new OurViewPagerAdapter(getActivity(), getChildFragmentManager());
        Bundle bundleTJ = new Bundle();
        mViewPagerAdapter.addNvgItem("推荐", InvestmentChildFragment.class, bundleTJ);

        Bundle bundleWD = new Bundle();
        mViewPagerAdapter.addNvgItem("网贷", InvestmentChildFragment.class, bundleWD);

        Bundle bundleHJ = new Bundle();
        mViewPagerAdapter.addNvgItem("货基", InvestmentChildFragment.class, bundleHJ);

        Bundle bundleZR = new Bundle();
        mViewPagerAdapter.addNvgItem("转让", InvestmentChildFragment.class, bundleZR);

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCanScroll(true);
        mViewPager.setOffscreenPageLimit(4);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);
    }

    public void setCurrentItem(int index) {
        mViewPager.setCurrentItem(index);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class OurViewPagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<Fragment> baseFragmentArrayList = new ArrayList<>();
        private final ArrayList<String> labStrs = new ArrayList<>();
        Context mContext;

        public OurViewPagerAdapter(Context mContext, FragmentManager fm) {
            super(fm);
            this.mContext = mContext;
        }


        public void addNvgItem(String labStr, Class<?> clss, Bundle args) {
            Fragment fragment = Fragment.instantiate(mContext, clss.getName(), args);
            baseFragmentArrayList.add(fragment);
            labStrs.add(labStr);
        }

        @Override
        public Fragment getItem(int position) {
            return baseFragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return labStrs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return labStrs.get(position);
        }
    }

}
