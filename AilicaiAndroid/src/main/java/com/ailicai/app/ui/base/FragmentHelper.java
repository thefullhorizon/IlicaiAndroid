package com.ailicai.app.ui.base;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;

/**
 * FragmentManager包装类
 */
public class FragmentHelper {

    private FragmentManager mFragmentManager;

    public FragmentHelper(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
        FragmentManager.enableDebugLogging(false);
    }

    /**
     * BackStack中是否有Fragment存在
     *
     * @return true:历史栈中存在Fragment
     */
    public boolean hasBackStackEntry() {
        int count = mFragmentManager.getBackStackEntryCount();
        return count != 0;
    }

    public int getBackStackEntryCount() {
        return mFragmentManager.getBackStackEntryCount();
    }

    /**
     * 获取BackStack中对应位置的Fragment
     *
     * @param position
     * @return
     */
    public BackStackEntry getBackStackEntry(int position) {
        int count = mFragmentManager.getBackStackEntryCount();
        if (count == 0) {
            return null;
        } else {
            return mFragmentManager.getBackStackEntryAt(position);
        }
    }

    /**
     * 替换resId中存在的Fragment
     *
     * @param arguments
     * @param resId
     * @param fragment
     */
    public void replace(Bundle arguments, int resId, Fragment fragment) {
        String tag = fragment.getClass().getName();
        Fragment preFragment = mFragmentManager.findFragmentByTag(tag);
        removeFragment(preFragment);

        fragment.setArguments(arguments);
        FragmentTransaction fragmentTransaction = mFragmentManager
                .beginTransaction();
        fragmentTransaction.replace(resId, fragment, tag);
        // fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 在resId中添加Fragment
     *
     * @param arguments
     * @param resId
     * @param fragment
     */
    public void add(Bundle arguments, int resId, Fragment fragment) {
        String tag = fragment.getClass().getName();
        Fragment preFragment = mFragmentManager.findFragmentByTag(tag);
        removeFragment(preFragment);

        fragment.setArguments(arguments);
        FragmentTransaction fragmentTransaction = mFragmentManager
                .beginTransaction();
        fragmentTransaction.add(resId, fragment, tag);
        fragmentTransaction
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(tag);
        // fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 弹出对话框
     *
     * @param arguments
     * @param dialogFragment
     */
    public void showDialog(Bundle arguments, DialogFragment dialogFragment) {
        showDialogAllowStateLoss(arguments, dialogFragment);
 /*       String tag = dialogFragment.getClass().getName();
        Fragment preFragment = mFragmentManager.findFragmentByTag(tag);
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (preFragment != null) {
            ft.remove(preFragment);
        }
        // ft.addToBackStack(tag);
        dialogFragment.setArguments(arguments);
        dialogFragment.show(ft, tag);*/
    }

    /**
     * 弹出对话框, 允许页面状态丢失
     *
     * @param arguments
     * @param dialogFragment
     */
    public void showDialogAllowStateLoss(Bundle arguments, DialogFragment dialogFragment) {
        String tag = dialogFragment.getClass().getName();
        Fragment preFragment = mFragmentManager.findFragmentByTag(tag);
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (preFragment != null) {
            ft.remove(preFragment);
        }
        // ft.addToBackStack(tag);
        dialogFragment.setArguments(arguments);
        ft.add(dialogFragment, tag);
        if (!mFragmentManager.isDestroyed()) {
            try {
                ft.commitAllowingStateLoss();
                ft.show(dialogFragment);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除对话框Fragment
     *
     * @param dialogFragment
     */
    public void removeDialogFragment(DialogFragment dialogFragment) {
        // String backStateName = dialogFragment.getClass().getSimpleName();
        // Fragment mBackStackFragment = mFragmentManager
        // .findFragmentByTag(backStateName);
        // Fragment mBackStackFragment =
        // findFragment(dialogFragment.getClass());
        if (dialogFragment != null && dialogFragment.isVisible()) {
            removeFragment(dialogFragment);
        }
    }

    /**
     * 返回到第一个fragment
     */
    public void popBackStackImmediate(String backStateName) {
        mFragmentManager.popBackStackImmediate(backStateName, 0);
    }

    /**
     * 返回之前的fragment
     */
    public void popBackStack() {
        mFragmentManager.popBackStack();
    }

    /**
     * 删除一个fragment
     *
     * @param fragment
     */
    public void removeFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = mFragmentManager
                    .beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }

    /**
     * 通过findFragmentByTag获取Fragment，如果不存在则会根据默认构造创建出Fragment对象
     *
     * @param clazz
     * @return
     */
    public android.support.v4.app.Fragment findFragment(
            Class<? extends Fragment> clazz) {
        String tag = clazz.getName();
        android.support.v4.app.Fragment fragment = mFragmentManager
                .findFragmentByTag(tag);
        if (fragment == null) {
            // 创建
            // Fragment.instantiate(context, fname);
            try {
                Fragment f = clazz.newInstance();
                return f;
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
                // throw new java.lang.InstantiationException(
                // "Unable to instantiate fragment "
                // + tag
                // + ": make sure class name exists, is public, and has an"
                // + " empty constructor that is public");
                // throw new InstantiationException(
                // "Unable to instantiate fragment "
                // + tag
                // + ": make sure class name exists, is public, and has an"
                // + " empty constructor that is public", e);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                // throw new InstantiationException(
                // "Unable to instantiate fragment "
                // + tag
                // + ": make sure class name exists, is public, and has an"
                // + " empty constructor that is public", e);
            }
        }
        return fragment;
    }

    /**
     * 根据xml中设置的ID来查找Fragment
     *
     * @param resId
     * @return
     */
    public android.support.v4.app.Fragment findFragmentById(int resId) {
        android.support.v4.app.Fragment fragment = mFragmentManager
                .findFragmentById(resId);
        return fragment;
    }

    /**
     * 根据Tag查找Fragment
     *
     * @return
     */
    public android.support.v4.app.Fragment findFragmentByTag(
            Class<? extends Fragment> clazz) {
        String tag = clazz.getName();
        android.support.v4.app.Fragment fragment = mFragmentManager
                .findFragmentByTag(tag);
        return fragment;
    }

}
