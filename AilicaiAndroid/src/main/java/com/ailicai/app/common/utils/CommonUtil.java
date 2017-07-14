package com.ailicai.app.common.utils;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.widget.SystemBarTintManager;
import com.huoqiu.framework.app.AppConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    @TargetApi(11)
    public static void addAnimForView(View rootView) {

        ViewGroup vg;
        if (DeviceUtil.getSDKVersionInt() >= 11 && rootView instanceof ViewGroup) {
            vg = (ViewGroup) rootView;
            LayoutTransition layoutTransition = new LayoutTransition();
            layoutTransition.setDuration(400);
            vg.setLayoutTransition(layoutTransition);
        }
    }

    /**
     * 设置沉浸式
     * 针对有titleview的情况
     *
     * @param mA
     * @param rootView
     */
    public static int uiSystemBarTint(Activity mA, View rootView) {
        RelativeLayout titleView = (RelativeLayout) rootView.findViewById(R.id.title_root_layout);
        titleView = titleView == null ? (RelativeLayout) rootView.findViewById(R.id.top_title_contentview) : titleView;
        if (titleView != null) {
            int titleHeight = MyApplication.getInstance().getResources().getDimensionPixelSize(R.dimen._48);
            return setTitleheight(titleView, titleHeight);
        }
        return 0;
    }

    public static int uiSystemBarTint(Activity mA, View rootView, int titleHeight) {
        RelativeLayout titleView = (RelativeLayout) rootView.findViewById(R.id.title_root_layout);
        titleView = titleView == null ? (RelativeLayout) rootView.findViewById(R.id.top_title_contentview) : titleView;
        if (titleView != null) {
            return setTitleheight(titleView, titleHeight);
        }
        return 0;
    }

    /**
     * 针对没有titleview的情况
     *
     * @param mA
     * @param rootView
     */
    public static int uiSystemBarTintNoTitle(Activity mA, View rootView) {
        if (rootView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                rootView.setPadding(0, DeviceUtil.getStatusbarHeight(), 0, 0);
                return DeviceUtil.getStatusbarHeight();
            }
        }
        return 0;
    }

    public static int setTitleheight(RelativeLayout titleView, int titleHeight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams layoutParams = titleView.getLayoutParams();
            layoutParams.height = titleHeight + DeviceUtil.getStatusbarHeight();
            titleView.setLayoutParams(layoutParams);
            RelativeLayout relativeLayout = (RelativeLayout) titleView.findViewById(R.id.top_title_contentview);
            if (relativeLayout != null) {
                relativeLayout.setPadding(0, DeviceUtil.getStatusbarHeight(), 0, 0);
            }
            return layoutParams.height;
        }
        return titleHeight;
    }

    public static int getTitleHeight(Activity mA) {
        RelativeLayout titleView = (RelativeLayout) mA.findViewById(R.id.title_root_layout);
        titleView = titleView == null ? (RelativeLayout) mA.findViewById(R.id.top_title_contentview) : titleView;
        if (titleView != null) {
            int titleHeight = mA.getResources().getDimensionPixelOffset(R.dimen._48);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return titleHeight + DeviceUtil.getStatusbarHeight();
            }
            return titleHeight;
        } else {
            return 0;
        }
    }

    /**
     * 黑字白底
     *
     * @param mActivity
     */
    public static void miDarkSystemBar(Activity mActivity) {
        if (AppConfig.CAN_SET_MIUIBAR) {
            setMiuiDarkSystemBar(mActivity.getWindow());// MIUI的沉浸式
        }
    }

    /**
     * 白字黑底
     *
     * @param mActivity
     */
    public static void miWhiteSystemBar(Activity mActivity) {
        if (AppConfig.CAN_SET_MIUIBAR) {
            setMiuiWhiteSystemBar(mActivity.getWindow());// MIUI的沉浸式
        }
    }

    public static boolean canSetMIUIbar() {
        if ("Xiaomi".equals(Build.MANUFACTURER)) {// 是小米设备
            switch (AppConfig.miuiV) {
                case "V5":
                    return false;
                case "V6":
                case "V7":
                case "V8":
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    public static boolean initSystemBar(Activity mActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = mActivity.getWindow().getDecorView();
            if (decorView.getTag() == null) {
                mActivity.getWindow().getDecorView().setTag("");
                SystemBarTintManager tintManager = new SystemBarTintManager(mActivity);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintResource(R.color.black_20_color);
            }

            return true;
        }
        return true;
    }

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            //Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    //     Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        if (null != context) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                    "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * +
     * 设置Miui的沉浸式
     * Method name: miuiDarkSystemBar <BR>
     * Description: miuiDarkSystemBar <BR>
     * Remark: <BR>
     *
     * @param
     */
    private static void setMiuiDarkSystemBar(Window window) {
        Class clazz = window.getClass();
        try {
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");

            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);

            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);

            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            //只需要状态栏透明
            //    extraFlagField.invoke(window, tranceFlag, tranceFlag);
            //或
            //状态栏透明且黑色字体
            extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);
            //清除黑色字体
            // extraFlagField.invoke(window, 0, darkModeFlag);
            //  MyApplication.isMIUIv6 = true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    private static void setMiuiWhiteSystemBar(Window window) {

        Class clazz = window.getClass();
        try {
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");

            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);

            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);

            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            //只需要状态栏透明
            extraFlagField.invoke(window, tranceFlag, tranceFlag);
            //或
            //状态栏透明且黑色字体
            //  extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);
            //清除黑色字体
            extraFlagField.invoke(window, 0, darkModeFlag);
            //     MyApplication.isMIUIv6 = true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取手机号
     *
     * @param mContext
     * @return
     */
    public static String getPhoneNumber(Context mContext) {
        String phoneNumber = "";
        if (null != mContext) {
            TelephonyManager mTelephonyMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            phoneNumber = mTelephonyMgr.getLine1Number();
        }
        return phoneNumber;
    }

    public static String getPhone11Num(Context mContext) {
        String numb = getPhoneNumber(mContext);
        if (!TextUtils.isEmpty(numb) && numb.length() > 11) {
            return numb.substring(numb.length() - 11, numb.length());
        } else {
            return numb;
        }
    }


    public static String getResourceStr(int strId) {
        return MyApplication.getAppResources().getString(strId);
    }

    /****
     * 保留一位小数，千分位“,”分隔
     **/
    public static final String formatMoney1(double money) {
        String resultStr;
        NumberFormat nf = new DecimalFormat(",###,##0.0");
        nf.setRoundingMode(RoundingMode.HALF_UP);
        resultStr = nf.format(money);
        if (resultStr != null && resultStr.endsWith(".0")) {
            //resultStr = resultStr.replace(".0", "");
        }
        return resultStr;
    }


    /****
     * 保留两位小数，>10w 千分位分隔.
     **/
    public static final String formatMoneyForFinance(double money) {
        String resultStr;
        if (money < 100000) {
            NumberFormat nf = new DecimalFormat("#0.00");
            nf.setRoundingMode(RoundingMode.HALF_UP);
            resultStr = nf.format(money);
            return resultStr;
        }

        NumberFormat nf = new DecimalFormat(",###,##0.00");
        nf.setRoundingMode(RoundingMode.HALF_UP);
        resultStr = nf.format(money);
        return resultStr;
    }

    /****
     * 保留两位小数，千分位“,”分隔
     **/
    public static final String formatMoney2(double money) {
        String resultStr;
        NumberFormat nf = new DecimalFormat(",###,##0.00");
        nf.setRoundingMode(RoundingMode.HALF_UP);
        resultStr = nf.format(money);
        return resultStr;
    }

    /**
     * 千分位“,”分隔
     *
     * @param money
     * @return
     */
    public static final String formatMoney(double money) {
        String resultStr;
        NumberFormat nf = new DecimalFormat(",###,##0.00");
        nf.setRoundingMode(RoundingMode.HALF_UP);
        resultStr = nf.format(money);
        if (resultStr != null && resultStr.endsWith(".00")) {
            resultStr = resultStr.replace(".00", "");
        }
        return resultStr;
    }

    /****
     * 保留两位小数
     **/
    public static final String formatMoneyDou(double money) {
        String resultStr;
        NumberFormat nf = new DecimalFormat("#0.00");
        nf.setRoundingMode(RoundingMode.HALF_UP);
        resultStr = nf.format(money);
        return resultStr;
    }


    //金额验证
    public static boolean isMoneyNumber(String str) {
        //判断小数点后2位的数字的正则表达式
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
        Matcher match = pattern.matcher(str);
        return match.matches();
    }

    /**
     * 格式化银行卡，4位1空格
     *
     * @param bankcardNo 需要格式化的银行卡
     * @return 格式化后的银行卡
     */
    public static String formatBankcardNo(String bankcardNo) {
        String regex = "(.{4})";
        bankcardNo = bankcardNo.replaceAll(regex, "$1 ");
        return bankcardNo;
    }

    /**
     * String =“42851577”用Double.parseDouble后 变4.2851577E7
     * 求解啊，怎么得到原来那数。。。
     *
     * @param value
     * @return
     */
    public static String numberFormat(double value) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return nf.format(value);
    }

    /**
     * 获取字符串中的所有数字
     *
     * @param msgStr
     * @return
     */
    public static String getNumCode(String msgStr) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(msgStr);
        return m.replaceAll("").trim();
    }

    // 判断一个字符串是否都为数字
    public boolean isDigit(String strNum) {
        Pattern pattern = Pattern.compile("[0-9]{1,}");
        Matcher matcher = pattern.matcher(strNum);
        return matcher.matches();
        //return strNum.matches("[0-9]{1,}");
    }

    //截取数字
    public String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    // 截取非数字
    public String splitNotNumber(String content) {
        Pattern pattern = Pattern.compile("\\D+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }


    public static boolean isAboveLOLLIPOP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

    }


    public interface OnDoubleClickListener {
        void OnSingleClick(View v);

        void OnDoubleClick(View v);
    }

    /**
     * 为控件增加双击事件
     */
    public static void registerDoubleClickListener(View view, final OnDoubleClickListener listener) {
        if (listener == null) return;
        view.setOnClickListener(new View.OnClickListener() {
            private static final int DOUBLE_CLICK_TIME = 200;        //双击间隔时间350毫秒
            private boolean waitDouble = true;

            private Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    listener.OnSingleClick((View) msg.obj);
                }

            };

            //等待双击
            public void onClick(final View v) {
                if (waitDouble) {
                    waitDouble = false;        //与执行双击事件
                    new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(DOUBLE_CLICK_TIME);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }    //等待双击时间，否则执行单击事件
                            if (!waitDouble) {
                                //如果过了等待事件还是预执行双击状态，则视为单击
                                waitDouble = true;
                                Message msg = handler.obtainMessage();
                                msg.obj = v;
                                handler.sendMessage(msg);
                            }
                        }

                    }.start();
                } else {
                    waitDouble = true;
                    listener.OnDoubleClick(v);    //执行双击
                }
            }
        });
    }

    /***
     * 获取properies的文件内容
     */
    public static Map<String,String> getProperties(Context context, String assetsName) {
        Map<String,String> propertiesMap = new HashMap<>();
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(context.getAssets().open(assetsName),"UTF-8"));
            for(String key : properties.stringPropertyNames()){
                propertiesMap.put(key,properties.getProperty(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return propertiesMap;
    }
}
