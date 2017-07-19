package com.ailicai.app.common.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;


import com.ailicai.app.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpannableUtil {
    private Context context = null;

    public SpannableUtil(Context context) {
        this.context = context;
    }

    public SpannableStringBuilder getSpannableString(String src, int style) {
        if (TextUtils.isEmpty(src) || context == null) {
            return null;
        }
        SpannableStringBuilder spanStr = null;
        spanStr = new SpannableStringBuilder(src);
        spanStr.setSpan(new TextAppearanceSpan(context, style), 0, src.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public SpannableStringBuilder getSpannableString(String src1, String src2, int style1, int style2) {
        SpannableStringBuilder spanStr = null;
        String src = src1 + src2;
        int length1 = src1.length();
        int lengthAll = src.length();
        if (context != null) {
            spanStr = new SpannableStringBuilder(src);
            if (0 != length1) {
                spanStr.setSpan(new TextAppearanceSpan(context, style1), 0, length1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (lengthAll != length1) {
                spanStr.setSpan(new TextAppearanceSpan(context, style2), length1, lengthAll, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spanStr;
    }

    /**
     * 功能描述:(用于对三段string设置不同的style 并返回SpannableStringBuilder)
     * <p/>
     * <pre>
     *     w_xiong:   2013-6-7      新建
     * </pre>
     *
     * @param src1
     * @param src2
     * @param src3
     * @param style1
     * @param style2
     * @param sytle3
     * @return
     */
    public SpannableStringBuilder getSpannableString(String src1, String src2, String src3, int style1, int style2, int sytle3) {
        SpannableStringBuilder spanStr = null;
        String src = src1 + src2 + src3;
        int length1 = src1.length();
        int length12 = src1.length() + src2.length();
        int lengthAll = src.length();
        if (context != null) {
            spanStr = new SpannableStringBuilder(src);
            if (0 != length1) {
                spanStr.setSpan(new TextAppearanceSpan(context, style1), 0, length1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (length1 != length12) {
                spanStr.setSpan(new TextAppearanceSpan(context, style2), length1, length12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (length12 != lengthAll) {
                spanStr.setSpan(new TextAppearanceSpan(context, sytle3), length12, lengthAll, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spanStr;
    }

    public SpannableStringBuilder getSpannableString(String src1, String src2, String src3, String src4, int style1, int style2, int sytle3, int sytle4) {
        SpannableStringBuilder spanStr = null;
        String src = src1 + src2 + src3 + src4;
        int length1 = src1.length();
        int length12 = (src1 + src2).length();
        int length123 = (src1 + src2 + src3).length();
        int lengthAll = src.length();
        if (context != null) {
            spanStr = new SpannableStringBuilder(src);
            if (length1 != 0) {
                spanStr.setSpan(new TextAppearanceSpan(context, style1), 0, length1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (length1 != length12) {
                spanStr.setSpan(new TextAppearanceSpan(context, style2), length1, length12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (length12 != length123) {
                spanStr.setSpan(new TextAppearanceSpan(context, sytle3), length12, length123, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (length123 != lengthAll) {
                spanStr.setSpan(new TextAppearanceSpan(context, sytle4), length123, lengthAll, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spanStr;
    }

    public SpannableStringBuilder getSpannableStringCenter(String src1, String src2, int style1, int style2) {
        SpannableStringBuilder spanStr = null;
        String src = src1 + src2;
        int length1 = src1.length();
        int lengthAll = src.length();
        if (context != null) {
            spanStr = new SpannableStringBuilder(src);
            if (0 != length1) {
                spanStr.setSpan(new TextAppearanceSpan(context, style1), 0, length1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (lengthAll != length1) {
                spanStr.setSpan(new TextAppearanceSpan(context, style2), length1, lengthAll, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanStr.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),length1,lengthAll,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spanStr;
    }

    public SpannableStringBuilder getSpannableString(String src1, String src2, String src3, String src4, String src5, String src6, int style1, int style2, int sytle3, int style4, int style5, int style6) {
        SpannableStringBuilder spanStr = null;
        String src = src1 + src2 + src3 + src4 + src5 + src6;
        int length1 = src1.length();
        int length12 = (src1 + src2).length();
        int length123 = (src1 + src2 + src3).length();
        int length1234 = (src1 + src2 + src3 + src4).length();
        int length12345 = (src1 + src2 + src3 + src4 + src5).length();
        int lengthAll = src.length();
        if (context != null) {
            spanStr = new SpannableStringBuilder(src);
            if (length1 != 0) {
                spanStr.setSpan(new TextAppearanceSpan(context, style1), 0, length1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (length1 != length12) {
                spanStr.setSpan(new TextAppearanceSpan(context, style2), length1, length12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (length12 != length123) {
                spanStr.setSpan(new TextAppearanceSpan(context, sytle3), length12, length123, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (length123 != length1234) {
                spanStr.setSpan(new TextAppearanceSpan(context, style4), length123, length1234, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (length1234 != length12345) {
                spanStr.setSpan(new TextAppearanceSpan(context, style5), length1234, length12345, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (length12345 != lengthAll) {
                spanStr.setSpan(new TextAppearanceSpan(context, style6), length12345, lengthAll, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }
        return spanStr;
    }

    public CharSequence strQuote(CharSequence text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Pattern mPattern = Pattern.compile("quote");
        Matcher matcher = mPattern.matcher(text);
        int resId;
        int i =0;
        while (matcher.find()) {
            if(i == 0) {
                resId = R.drawable.quote_left;
            } else {
                resId = R.drawable.quote_right;
            }
            Drawable drawable = context.getResources().getDrawable(resId);
            if (i == 0) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());//这里设置图片的大小
            } else {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());//这里设置图片的大小
            }
            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
            builder.setSpan(imageSpan, matcher.start(),
                    matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            i++;
        }
        return builder;
    }

}
