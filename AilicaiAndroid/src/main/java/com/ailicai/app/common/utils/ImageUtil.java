package com.ailicai.app.common.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by xubin on 14-5-29.
 */
public class ImageUtil {
    public static void autoFitImage(Resources res, int id, ImageView view) {
        ViewGroup.LayoutParams pa = view.getLayoutParams();
        if (pa.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            Drawable able = res.getDrawable(id);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            int height = view.getMeasuredWidth() / able.getMinimumWidth() * able.getMinimumHeight();
            pa.height = height;
            view.setLayoutParams(pa);
            view.setImageResource(id);
        } else if (view.getLayoutParams().height == ViewGroup.LayoutParams.MATCH_PARENT) {
            Drawable able = res.getDrawable(id);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            int width = view.getHeight() / able.getMinimumHeight() * able.getMinimumWidth();
            pa.width = width;
            view.setLayoutParams(pa);
            view.setImageResource(id);
        }
    }
}
