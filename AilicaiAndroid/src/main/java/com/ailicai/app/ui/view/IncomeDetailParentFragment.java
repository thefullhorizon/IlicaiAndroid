package com.ailicai.app.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ailicai.app.R;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.widget.NoScrollViewPager;
import com.ailicai.app.widget.slidingtab.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.Bind;


/**
 * Created by David on 16/1/4
 * Modified by Owen on 16/8/10
 */
public class IncomeDetailParentFragment extends BaseBindFragment {

    public static final String TAG_TYPE = "tag_type";
    public static final String TAG_REGULAR = "tag_regular";
    public static final String TAG_TRY = "tag_try";

    @Bind(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTabLayout;
    @Bind(R.id.view_pager)
    NoScrollViewPager mViewPager;

    private OurViewPagerAdapter mViewPagerAdapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_income_detail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        inittabView();
        mViewPager.setCurrentItem(getArguments().getString(TAG_TYPE).equals(TAG_REGULAR) ? 0 : 1);
    }

    public void inittabView() {
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator2, android.R.id.text1);
        Resources res = getResources();
        mSlidingTabLayout.setSelectedIndicatorColors(res.getColor(R.color.main_red_color));// 设置下面的线颜色
        mSlidingTabLayout.setDistributeEvenly(true);
        iniTab();
    }

    private void iniTab() {
        mViewPagerAdapter = new OurViewPagerAdapter(getActivity(), getChildFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putInt(IncomeDetailChildFragment.INCOME_TYPE, IncomeDetailChildFragment.INCOME_REGULAR);
        mViewPagerAdapter.addNvgItem("网贷资产", IncomeDetailChildFragment.class, bundle);

        Bundle bundle2 = new Bundle();
        bundle2.putInt(IncomeDetailChildFragment.INCOME_TYPE, IncomeDetailChildFragment.INCOME_DEMAND);
        mViewPagerAdapter.addNvgItem("体验宝", IncomeDetailChildFragment.class, bundle2);

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCanScroll(true);
        mViewPager.setOffscreenPageLimit(2);
        mSlidingTabLayout.setViewPager(mViewPager);
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
