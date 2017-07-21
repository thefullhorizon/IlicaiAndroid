package com.ailicai.app.ui.view;

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
 * Created by David on 16/1/4
 * Modified by Owen on 16/8/10
 */
public class IncomeDetailParentFragment extends BaseBindFragment implements TabLayout.OnTabSelectedListener {

    public static final String TAG_TYPE = "tag_type";
    public static final String TAG_REGULAR = "tag_regular";
    public static final String TAG_TRY = "tag_try";

    @Bind(R.id.tab_layou)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager)
    NoScrollViewPager mViewPager;

    private OurViewPagerAdapter mViewPagerAdapter;
    private String[] pageTitles = new String[]{"定期资产", "体验宝"};

    @Override
    public int getLayout() {
        return R.layout.fragment_income_detail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        inittabView();
    }

    private void inittabView() {
        //设置TabLayout标签的显示方式
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (String tab : pageTitles) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tab));
        }
        mTabLayout.setOnTabSelectedListener(this);

        Bundle bundle = new Bundle();
        bundle.putInt(IncomeDetailChildFragment.INCOME_TYPE, IncomeDetailChildFragment.INCOME_REGULAR);
        mViewPagerAdapter.addNvgItem(pageTitles[0], IncomeDetailChildFragment.class, bundle);

        Bundle bundle2 = new Bundle();
        bundle2.putInt(IncomeDetailChildFragment.INCOME_TYPE, IncomeDetailChildFragment.INCOME_DEMAND);
        mViewPagerAdapter.addNvgItem(pageTitles[1], IncomeDetailChildFragment.class, bundle2);

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCanScroll(true);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(getArguments().getString(TAG_TYPE).equals(TAG_REGULAR) ? 0 : 1);
        mTabLayout.setupWithViewPager(mViewPager);
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
