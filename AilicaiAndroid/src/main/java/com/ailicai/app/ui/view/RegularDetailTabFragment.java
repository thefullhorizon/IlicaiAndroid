package com.ailicai.app.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.ailicai.app.R;
import com.ailicai.app.ui.base.BaseBindFragment;
import com.ailicai.app.ui.view.reserveredrecord.ProductInvestRecordFragment;
import com.ailicai.app.widget.NoScrollViewPager;
import com.ailicai.app.widget.TabLineLayout;

import butterknife.Bind;

/**
 * 房产宝详情页TabFragment
 * Created by liyanan on 16/4/7.
 */
public class RegularDetailTabFragment extends BaseBindFragment {

    @Bind(R.id.pager)
    NoScrollViewPager pager;
    @Bind(R.id.tab_layout)
    TabLineLayout tabLayout;
    private String id;
    private ProductInvestRecordFragment recordFragment;
    private ProjectDetailFragment projectDetailFragment;

    @Override
    public int getLayout() {
        return R.layout.fragment_regular_detail_tab;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        id = getArguments().getString(RegularFinancingDetailActivity.PROD_ID);
        initViewPager();
    }

    public void onRefresh() {
        if (projectDetailFragment != null) {
            projectDetailFragment.onRefresh();
        }
        if (recordFragment != null) {
            recordFragment.onRefresh();
        }
    }

    private void initViewPager() {
        pager.setAdapter(new ViewPagerAdapter());
        pager.setOffscreenPageLimit(3);
        tabLayout.setViewPager(pager);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private String[] titles = {"项目详情", "投资记录", "常见问题"};

        public ViewPagerAdapter() {
            super(getActivity().getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle bundle = new Bundle();
            bundle.putString(RegularFinancingDetailActivity.PROD_ID, id);
            switch (position) {
                case 0:
                    //项目详情
                    fragment = new ProjectDetailFragment();
                    fragment.setArguments(bundle);
                    projectDetailFragment = (ProjectDetailFragment) fragment;
                    break;
                case 1:
                    //投资记录
                    fragment = new ProductInvestRecordFragment();
                    fragment.setArguments(bundle);
                    recordFragment = (ProductInvestRecordFragment) fragment;
                    break;
                case 2:
                    //常见问题
                    fragment = new CommonQuestionFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }


}
