package com.ailicai.app.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

public abstract class BaseDialogFragment extends DialogFragment implements
        IDialog {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        int layoutResID = getLayout();
        View fragmentView;
        if (layoutResID > 0) {
            int themeStyle = initThemeStyle();
            LayoutInflater themeLayoutInflater = getThemeLayoutInflater(
                    inflater, themeStyle);
            fragmentView = themeLayoutInflater.inflate(layoutResID, container,
                    false);
        } else {
            fragmentView = new TextView(getActivity());//不传递Id，直接传递V
        }
        ButterKnife.bind(this, fragmentView);
        setupView(fragmentView, bundle);
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setupData(bundle);
        setDialogLocation();
    }

    @Override
    public int initThemeStyle() {
        return -1;
    }

    /**
     * 设置Fragment主题
     */
    private LayoutInflater getThemeLayoutInflater(LayoutInflater inflater,
                                                  int themeStyle) {
        if (themeStyle <= 0) {
            return inflater;
        }
        Context ctxWithTheme = new ContextThemeWrapper(getActivity(),
                themeStyle);
        LayoutInflater localLayoutInflater = inflater
                .cloneInContext(ctxWithTheme);
        return localLayoutInflater;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}