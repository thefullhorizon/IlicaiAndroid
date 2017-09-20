package com.ailicai.app.ui.view.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ailicai.app.R;
import com.ailicai.app.ui.base.BaseBindActivity;
import com.ailicai.app.ui.view.IncomeDetailParentFragment;
import com.ailicai.app.ui.view.IncomeDetailWalletFragment;
import com.ailicai.app.widget.IWTopTitleView;
import com.ailicai.app.widget.NoScrollViewPager;
import com.ailicai.app.widget.slidingtab.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.Bind;

// 此页面用于取代了IncomeDetailActivity
public class IncomeRecordsActivity extends BaseBindActivity implements IWTopTitleView.TopTitleOnClickListener {

    @Bind(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTabLayout;
    @Bind(R.id.view_pager)
    NoScrollViewPager mViewPager;

    public static final String TYPE_INCOME_LEVEL_ONE = "type_income_level_one";
    public static final String TYPE_INCOME_LEVEL_TWO = "type_income_level_two";

    public static final int ALL = 0;//全部
    public static final int NETLOAN = 1;//网贷
    public static final int NETLOAN_REGULAR = 10;//网贷_房产宝
    public static final int NETLOAN_EXPERIENCE = 11;//网贷_体验宝
    public static final int WALLET = 2;//活期宝

    private String[] valueArray = {"全部","网贷","活期宝"};

    private IncomeAdapter mViewPagerAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_income_records;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        //initial ViewPager
        mViewPagerAdapter = new IncomeAdapter(this, getSupportFragmentManager());

        Bundle bundleALL = new Bundle();
        bundleALL.putInt(IncomeDetailWalletFragment.INCOME_TYPE, IncomeDetailWalletFragment.INCOME_ALL);
        mViewPagerAdapter.addNvgItem(valueArray[0], IncomeDetailWalletFragment.class, bundleALL);

        Bundle bundleNetLoan = new Bundle();
        bundleALL.putInt(IncomeDetailParentFragment.TAG_TYPE, getIntent().getIntExtra(TYPE_INCOME_LEVEL_TWO,10));
        mViewPagerAdapter.addNvgItem(valueArray[1], IncomeDetailParentFragment.class,bundleNetLoan);

        Bundle bundleCT = new Bundle();
        bundleCT.putInt(IncomeDetailWalletFragment.INCOME_TYPE, IncomeDetailWalletFragment.INCOME_CURRENT_TREASURE);
        mViewPagerAdapter.addNvgItem(valueArray[2], IncomeDetailWalletFragment.class, bundleCT);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCanScroll(true);
        mViewPager.setOffscreenPageLimit(3);

        // initial Tab
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator2, android.R.id.text1);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.main_red_color));
        mSlidingTabLayout.setDistributeEvenly(true);//均匀平铺选项卡

        // Attach ViewPager int ViewPager to tag
        mSlidingTabLayout.setViewPager(mViewPager);

        // provide the outer right of power
        mViewPager.setCurrentItem(getIntent().getIntExtra(TYPE_INCOME_LEVEL_ONE,0));

    }

    private class IncomeAdapter extends FragmentPagerAdapter {

        private final ArrayList<Fragment> baseFragmentArrayList = new ArrayList<>();
        private final ArrayList<String> labStrs = new ArrayList<>();
        Context mContext;

        public IncomeAdapter(Context mContext, FragmentManager fm) {
            super(fm);
            this.mContext = mContext;
        }

        public void addNvgItem(String labStr, Class<?> clss) {
            Fragment fragment = Fragment.instantiate(mContext, clss.getName());
            baseFragmentArrayList.add(fragment);
            labStrs.add(labStr);
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

    @Override
    public boolean onBackClick() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}

      