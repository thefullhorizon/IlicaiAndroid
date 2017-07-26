package com.ailicai.app.ui.index.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ailicai.app.common.utils.CommonUtil;
import com.ailicai.app.ui.base.BaseFragment;
import com.ailicai.app.widget.ahbottomnavigation.AHBottomNavigation;
import com.ailicai.app.widget.ahbottomnavigation.AHBottomNavigationItem;

import java.util.ArrayList;

/**
 * name: NavigationPagerAdapter <BR>
 * description: write class description <BR>
 * create date: 2017/7/12
 *
 * @author: IWJW Administrator
 */
public class NavigationPagerAdapter extends FragmentPagerAdapter implements ViewPager
        .OnPageChangeListener, AHBottomNavigation.OnTabSelectedListener {
    private final ArrayList<BaseFragment> baseFragmentArrayList = new ArrayList<>();
    private FragmentActivity activity;
    private AHBottomNavigation bottomNavigation;
    private ViewPager mViewPager;
    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();

    public NavigationPagerAdapter(FragmentActivity activity, AHBottomNavigation
            bottomNavigation, ViewPager pager, FragmentManager fm) {
        super(fm);
        this.activity = activity;
        this.bottomNavigation = bottomNavigation;
        mViewPager = pager;
        mViewPager.setAdapter(this);
        mViewPager.addOnPageChangeListener(this);
        bottomNavigation.setOnTabSelectedListener(this);
    }

    public ArrayList<AHBottomNavigationItem> getBottomNavigationItems() {
        return bottomNavigationItems;
    }

   public void addNvgItem(AHBottomNavigationItem item, Class<?> clss, Bundle args) {
        bottomNavigationItems.add(item);
        BaseFragment fragment = (BaseFragment) Fragment.instantiate(activity, clss.getName(), args);
        fragment.setBackOp(null);
        baseFragmentArrayList.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return baseFragmentArrayList.size();
    }

    @Override
    public BaseFragment getItem(int position) {
        return baseFragmentArrayList.get(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {


    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {

        if (!wasSelected) {//初始化打开
            mViewPager.setCurrentItem(position, false);
        }
//        BaseFragment currentFragment = getItem(position);
//        //去掉红点和通知
//        switch (position) {
//            case 0:
////                ((IndexFragment) currentFragment).alphaChangeMiSystemBarColor();
//                currentFragment.reloadData();
//                break;
//            case 1:
//                CommonUtil.miDarkSystemBar(activity);
//                break;
//            case 2:
//                CommonUtil.miDarkSystemBar(activity);
//                break;
//            case 3:
//                CommonUtil.miWhiteSystemBar(activity);
//                break;
//        }
        return true;
    }


    public void claenFragments() {
        baseFragmentArrayList.clear();
    }
}
