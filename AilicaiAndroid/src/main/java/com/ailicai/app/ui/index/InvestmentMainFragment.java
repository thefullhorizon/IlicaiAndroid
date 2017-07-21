package com.ailicai.app.ui.index;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ailicai.app.R;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.widget.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 投资主页面
 */
public class InvestmentMainFragment extends BaseBindFragment implements TabLayout.OnTabSelectedListener {
    @Bind(R.id.tab_layou)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    NoScrollViewPager mViewPager;

    private OurViewPagerAdapter mViewPagerAdapter;
    private String[] pageTitles = new String[]{"推荐", "网贷", "货基", "转让"};

    @Override
    public int getLayout() {
        return R.layout.investment_fragment;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        inittabView();
    }

    private void inittabView() {
        //设置TabLayout标签的显示方式
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //循环注入标签
        for (String tab : pageTitles) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tab));
        }
        //设置TabLayout点击事件
        mTabLayout.setOnTabSelectedListener(this);

        mViewPagerAdapter = new OurViewPagerAdapter(getActivity(), getChildFragmentManager());
        Bundle bundleTJ = new Bundle();
        mViewPagerAdapter.addNvgItem(pageTitles[0], InvestmentRecommendFragment.class, bundleTJ);

        Bundle bundleWD = new Bundle();
        mViewPagerAdapter.addNvgItem(pageTitles[1], InvestmentNetLoanFragment.class, bundleWD);

        Bundle bundleHJ = new Bundle();
        mViewPagerAdapter.addNvgItem(pageTitles[2], InvestmentMoneyFundFragment.class, bundleHJ);

        Bundle bundleZR = new Bundle();
        mViewPagerAdapter.addNvgItem(pageTitles[3], InvestmentTransferFragment.class, bundleZR);

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCanScroll(true);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setCurrentItem(0);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void setCurrentItem(int index) {
        mViewPager.setCurrentItem(index);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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
