package com.ailicai.app.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ailicai.app.BuildConfig;
import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.constants.AILICAIBuildConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * 系统功能调用工具类
 *
 * @author shengjunkai
 */
public final class SystemUtil {
    private final static String VIDEO_ERROR_TAG = "video_error";
    private final static String VIDEO_OUTPUT_TAG = "video_out";

    /**
     * 调用系统拨号功能，跳到拨号界面
     *
     * @param context
     * @param phoneNumber 电话号码
     */
    public static void callPhone(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }


    //隐藏虚拟键盘
    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    //显示虚拟键盘
    public static void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }


    /**
     * 显示或隐藏软键盘， 若软键盘处于显示状态，则执行隐藏； 若软键盘处于隐藏状态,则执行显示
     */
    public static void showOrHideSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示软键盘
     */
    public static void showSoftInput(View view) {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService
                (Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, 0);
    }

    /**
     * @param @param activity
     * @return void
     * @throws
     * @Title: HideSoftInput
     * @Description:关闭输入法
     */
    public static void HideSoftInput(Activity activity) {
        if (activity.getCurrentFocus() == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService
                (Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 获取软键盘的打开状态
     *
     * @param activity
     * @return true, 打开状态；false,关闭状态
     */
    public static boolean isSoftInputActive(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();// isOpen若返回true，则表示输入法打开
    }

    /**
     * 获取软键盘的打开状态 通过关闭软件盘 结果 判断的
     *
     * @param activity
     * @return true, 打开状态；false,关闭状态
     */
    public static boolean isSoftInputActive(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        return imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 手机是否开启了定位服务
     *
     * @param context
     * @return
     */
    public static final boolean isEnableLocation(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快） 
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位） 
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }

    /**
     * 检查APP 是否获取了相应的权限
     *
     * @param context
     * @param permisson
     * @return
     */
    public static final boolean isEnablePermission(Context context, String permisson) {
        PackageManager pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(permisson, "packageName"));
        return permission;
    }

    public static void exitApplication(Activity activity) {
        if (!activity.isFinishing()) activity.finish();
        ActivityManager amgr = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        amgr.killBackgroundProcesses(activity.getPackageName());
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 判断当前应用程序处于前台还是后台
     *
     * @return true 前台 false 后台
     */
    public static boolean isApplicationForeground(Context context) {
        return !TextUtils.isEmpty(getActivePackagesName(context)) && context.getPackageName().equals
                (getActivePackagesName(context));
    }

    private static String getActivePackagesName(Context context) {
        String packageName = null;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context
                .ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Vector<String> activePackages = new Vector<>();
            final List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    activePackages.addAll(Arrays.asList(processInfo.pkgList));
                }
            }
            if (activePackages.size() > 0) {
                packageName = activePackages.get(0);
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfoList = activityManager.getRunningTasks(1);
            ComponentName componentName = taskInfoList.get(0).topActivity;
            packageName = componentName.getPackageName();
        }
        return packageName;
    }

    public static boolean isNetWorkAvaliable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWIFIAvaliable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkApkExist(String packageName) {
        try {
            ApplicationInfo info = MyApplication.getInstance().getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public static AlertDialog AlertDialogBuilder(Activity mActivity) {
        return new AlertDialog.Builder(mActivity, R.style.AppCompatDialog).create();
    }


    /****************************************************************************
     * /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        String version = "";
        try {
            PackageManager manager = MyApplication.getInstance().getPackageManager();
            PackageInfo info = manager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
            version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }


    public static String getAppBuildVersion() {
        String buildConfig = BuildConfig.BUILDVERSION;
        if (TextUtils.isEmpty(buildConfig) || buildConfig.equalsIgnoreCase(AILICAIBuildConfig.BuildRELEASE)) {
            return "RELEASE";
        } else if (buildConfig.equalsIgnoreCase(AILICAIBuildConfig.BuildBeta)) {
            return "BETA";
        } else if (buildConfig.equalsIgnoreCase(AILICAIBuildConfig.BuildDev)) {
            return "DEV";
        } else if (buildConfig.equalsIgnoreCase(AILICAIBuildConfig.BuildDebug)) {
            return "DEBUG";
        } else {
            return "UNKNOW";
        }
    }

    /**
     * 获取当前进程的名称
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (null != mActivityManager.getRunningAppProcesses()) {
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {

                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        }
        return null;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}
