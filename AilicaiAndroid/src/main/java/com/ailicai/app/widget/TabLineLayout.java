/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ailicai.app.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailicai.app.R;


/**
 * 房产宝详情页tab
 * created by liyanan on 16/4/7
 */
public class TabLineLayout extends LinearLayout {

    //默认tab均分
    private boolean mDistributeEvenly = true;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;


    public TabLineLayout(Context context) {
        this(context, null);
    }

    public TabLineLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLineLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setDistributeEvenly(boolean distributeEvenly) {
        mDistributeEvenly = distributeEvenly;
    }


    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }


    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(new InternalViewPagerListener());
            initTabs();
        }
    }

    /**
     * 初始化tabs
     */
    private void initTabs() {
        removeAllViews();
        final PagerAdapter adapter = mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = LayoutInflater.from(getContext()).inflate(R.layout.tab_regular_detail, null);
            TextView tabTitleView = (TextView) tabView.findViewById(R.id.tv_tab);
            View line = tabView.findViewById(R.id.line);
            if (mDistributeEvenly) {
                LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
                tabView.setLayoutParams(lp);
            }
            tabTitleView.setText(adapter.getPageTitle(i));
            tabView.setOnClickListener(tabClickListener);
            if (i == mViewPager.getCurrentItem()) {
                tabView.setSelected(true);
                line.setVisibility(View.VISIBLE);
            }
            addView(tabView);
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            onPagerSelected(position);
            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

    }

    private void onPagerSelected(int tabIndex) {
        mViewPager.setCurrentItem(tabIndex);
        int tabCount = getChildCount();
        for (int i = 0; i < tabCount; i++) {
            View view = getChildAt(i);
            View line = view.findViewById(R.id.line);
            if (tabIndex == i) {
                line.setVisibility(View.VISIBLE);
                view.setSelected(true);
            } else {
                line.setVisibility(View.INVISIBLE);
                view.setSelected(false);
            }
        }
    }

    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < getChildCount(); i++) {
                if (v == getChildAt(i)) {
                    onPagerSelected(i);
                    return;
                }
            }
        }
    }
}
