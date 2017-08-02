package com.ailicai.app.common.utils;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.ailicai.app.MyApplication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.UUID;

public class DeviceUtil {
    public static int nScreentWidth;
    public static int nScreentHeight;
    private static int nStatusbarHeight;
    private static int nNavigatebarHeight;
    private static int nNavigationBarWidth;

    /**
     * 获取设备的型号
     */
    public static String getDeviceModel() {
        String model = Build.MODEL;

        if (model == null) {
            return "";
        } else {
            return model;
        }
    }

    /**
     * 获取设备 SDK版本名
     */
    @SuppressWarnings("deprecation")
    public static String getSDKVersion() {
        return Build.VERSION.SDK;
    }

    /**
     * 获取设备 SDK版本号
     */
    public static int getSDKVersionInt() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 判断是否存在SD卡
     *
     * @return
     */
    public static boolean isSdCardExist() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取设备的IMEI号
     */
    public static String getIMEI() {
        TelephonyManager teleMgr = (TelephonyManager) MyApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        return teleMgr.getDeviceId();
    }

    /**
     * 获取设备的IMSI号
     */
    public static String getIMSI(Context context) {
        TelephonyManager teleMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return teleMgr.getSubscriberId();
    }

    /**
     * 获取设备号 wifi mac + imei + cpu serial
     *
     * @return 设备号
     */
    public static String getMobileUUID(Context context) {
        String uuid = "";
        // 先获取mac
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        /* 获取mac地址 */
        if (wifiMgr != null) {
            WifiInfo info = wifiMgr.getConnectionInfo();
            if (info != null && info.getMacAddress() != null) {
                uuid = info.getMacAddress().replace(":", "");
            }
        }

        // 再加上imei
        TelephonyManager teleMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = teleMgr.getDeviceId();
        uuid += imei;

        // 最后再加上cpu
        String str = "", strCPU = "", cpuAddress = "";
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder cmd = new ProcessBuilder(args);
            Process pp = cmd.start();
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    if (str.indexOf("Serial") > -1) {
                        strCPU = str.substring(str.indexOf(":") + 1, str.length());
                        cpuAddress = strCPU.trim();
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        uuid += cpuAddress;

        // 如果三个加在一起超过64位的话就截取
        if (uuid != null && uuid.length() > 64) {
            uuid = uuid.substring(0, 64);
        }
        return uuid;
    }

    /** 获取动画设置 */
//	public static boolean getAnimationSetting(){
//		 ContentResolver cv = BusinessController.getApplication().getContentResolver();
//	     String animation = android.provider.Settings.System.getString(cv, android.provider.Settings.System.TRANSITION_ANIMATION_SCALE);
//	     return StringUtil.toDouble(animation)>0;
//	}

    /**
     * 获取屏幕的宽高
     *
     * @param dm 设备显示对象描述
     * @return int数组, int[0] - width, int[1] - height
     */
    public static int[] getScreenSize(DisplayMetrics dm) {
        int[] result = new int[2];
        result[0] = dm.widthPixels;
        result[1] = dm.heightPixels;
        return result;
    }

    /**
     * Dip转换为实际屏幕的像素值
     *
     * @param dm  设备显示对象描述
     * @param dip dip值
     * @return 匹配当前屏幕的像素值
     */
    public static int getPixelFromDip(DisplayMetrics dm, float dip) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, dm) + 0.5f);
    }

    /**
     * 判断当前设备的数据服务是否有效
     *
     * @return true - 有效，false - 无效
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectMgr == null) {
            return false;
        }

        NetworkInfo nwInfo = connectMgr.getActiveNetworkInfo();
        return !(nwInfo == null || !nwInfo.isAvailable());
    }

    public static void initScreenParams(Resources resources) {
        if (resources == null) {
            return;
        }
        int resourceIdA = resources.getIdentifier("status_bar_height", "dimen", "android");
        int resourceIdB = resources.getIdentifier("navigation_bar_width", "dimen", "android");
        int resourceIdC = resources.getIdentifier("navigation_bar_height_landscape", "dimen", "android");
        if (resourceIdA > 0) {
            nStatusbarHeight = resources.getDimensionPixelSize(resourceIdA);
        }
        if (resourceIdB > 0) {
            nNavigationBarWidth = resources.getDimensionPixelSize(resourceIdB);
        }
        if (resourceIdC > 0) {
            nNavigatebarHeight = resources.getDimensionPixelSize(resourceIdC);
        }

        nScreentWidth = resources.getDisplayMetrics().widthPixels;
        nScreentHeight = resources.getDisplayMetrics().heightPixels;
    }

    public static int getStatusbarHeight() {
        if (nStatusbarHeight > 0) {
            return nStatusbarHeight;
        } else {
            initScreenParams(MyApplication.getInstance().getResources());
            return nStatusbarHeight;
        }
    }

    public static int getNavigationbarHeight() {
        return nNavigatebarHeight;
    }

    public static int getNavigationBarWidth() {
        return nNavigationBarWidth;

    }

    public static int getAcailableScreenHeight() {
        return nScreentHeight - nStatusbarHeight;
    }

    public static int getPixelFromDip(Context context, float dip) {
        return getPixelFromDip(MyApplication.getInstance().getResources().getDisplayMetrics(), dip);
    }

    public static double calculateScreenSize(DisplayMetrics outMetrics) {
        double x = Math.pow(outMetrics.widthPixels / outMetrics.xdpi, 2);
        double y = Math.pow(outMetrics.heightPixels / outMetrics.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        return screenInches;
    }

    public static double calculateScreenSize(Context context) {
        DisplayMetrics outMetrics = context.getResources().getDisplayMetrics();
        return calculateScreenSize(outMetrics);
    }

    public static int[] getScreenSize() {
        int[] result = new int[2];
        // result[0] = dm.widthPixels;
        // result[1] = dm.heightPixels;
        result[0] = nScreentWidth;
        result[1] = nScreentHeight;
        return result;
    }

    public static boolean isAppInstalled(Context context, String pkgName) {
        if (context == null) {
            return false;
        }

        try {
            context.getPackageManager().getPackageInfo(pkgName, PackageManager.PERMISSION_GRANTED);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * (检查终端是否支持指定的action)
     */
    public static boolean isIntentAvailable(Context context, String action) {
        return isIntentAvailable(context, new Intent(action));
    }

    /**
     * (检查终端是否支持指定的intent)
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        if (context == null || intent == null) {
            return false;
        }

        PackageManager pkgManager = context.getPackageManager();
        if (pkgManager == null) {
            return false;
        }
        List<ResolveInfo> list = pkgManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 获取手机品牌 + 型号
     *
     * @return
     */
    public static String getBrandModel() {
        return Build.BRAND + "," + Build.MODEL;
    }

    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取SIM卡运营商名字
     *
     * @param context
     * @return
     */
    public static String getSimOperatorName(Context context) {
        TelephonyManager teleMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return teleMgr.getSimOperatorName();
    }

    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * 获取SIM卡运营商
     *
     * @param context
     * @return
     */
    public static String getOperators(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI = tm.getSubscriberId();
        if (IMSI == null || ("").equals(IMSI)) {
            return "";
        }
        String operator = "";
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.equals("46007")) {
            operator = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            operator = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            operator = "中国电信";
        }
        return operator;
    }


    /**
     * @return 获取App的UUID 随机生成
     */
    public static String getIWJWUUID() {
        String uuid = UUID.randomUUID().toString();
        // 这里无需判断mobile是否为""
        uuid = MyPreference.getInstance().read("IWJW_UUID", uuid);
        MyPreference.getInstance().write("IWJW_UUID", uuid);
        return uuid;
    }

    public static int getScreenWidth() {
        if (nScreentWidth == 0) {
            initScreenParams(MyApplication.getInstance().getResources());
        }
        return nScreentWidth;


    }

    public static boolean isCameraGranted(Context cxt) {
        boolean isGranted = false;
        PackageManager pm = cxt.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.CAMERA ", "com.manyi.lovehouse"));
        isGranted = permission;
        return isGranted;
    }

    /**
     * 测试当前摄像头能否被使用
     *
     * @return
     */
    public static boolean isCameraPositive() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open(0);
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
        }
        return canUse;
    }

    public static boolean isCameraOK(Context cxt) {
        boolean isPositive = isCameraPositive();
        boolean isGranted = isCameraGranted(cxt);
        return isPositive || isGranted;
    }
}