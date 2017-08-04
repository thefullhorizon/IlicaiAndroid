package com.ailicai.app.common.version;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.ailicai.app.common.constants.CommonTag;
import com.ailicai.app.common.download.DownloadListener;
import com.ailicai.app.common.download.DownloadNotificationManager;
import com.ailicai.app.common.download.DownloadProgressDialogManger;
import com.ailicai.app.common.download.FileDownLoaderManger;
import com.ailicai.app.common.download.update.PatchUtil;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.ContextUtil;
import com.ailicai.app.common.utils.FileRWManger;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.common.utils.NetCheckUtil;
import com.ailicai.app.common.utils.StringUtil;
import com.ailicai.app.common.utils.SystemUtil;
import com.ailicai.app.common.utils.ToastUtil;
import com.ailicai.app.eventbus.ExitEvent;
import com.ailicai.app.model.request.UpdateInfoRequest;
import com.ailicai.app.model.response.UpdateInfoResponse;
import com.ailicai.app.setting.AppInfo;
import com.ailicai.app.widget.DialogBuilder;
import com.huoqiu.framework.app.AppConfig;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.rest.UpdateInfo;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.ref.WeakReference;

import static com.ailicai.app.common.reqaction.IwjwRespListener.HASCHECKNEWVERSION;


/**
 * Created by David on 15/8/10.
 */
public class Version implements DownloadListener {
    private static Version version;
    private WeakReference<VersionInterface> reference;
    private boolean isCurrentPatchUpdate = false;
    private WeakReference<Activity> activityWeakReference;

    public static synchronized Version instance() {
        if (version == null) version = new Version();
        return version;
    }

    public void setVersionInterface(VersionInterface _interface) {
        reference = new WeakReference<>(_interface);
    }

    public void checkUpdateStrong(Activity activity, UpdateInfo updateInfo) {
        activityWeakReference = new WeakReference<>(activity);
        VersionInterface version = reference.get();
        version.checkSuccess();
        AppInfo.getInstance().setAppInfo(updateInfo);
        executeUpdateResult();
    }

    public void checkUpdate(Activity activity) {

        activityWeakReference = new WeakReference<>(activity);

        final UpdateInfoRequest request = new UpdateInfoRequest();
        request.setType(1);
        request.setChannel(AppConfig.channelNo);

        ServiceSender.exec(activity, request, new IwjwRespListener<UpdateInfoResponse>() {
            @Override
            public void onStart() {
                Context context = getWRContext();
                if (context == null) return;

                VersionInterface version = reference.get();
                if (version == null) return;

                version.checkStart();
            }

            @Override
            public void onJsonSuccess(UpdateInfoResponse response) {
                Context context = getWRContext();
                if (context == null) return;

                VersionInterface version = reference.get();
                if (version == null) return;

                version.checkSuccess();

                int responseCode = response.getErrorCode();
                if (responseCode == 0 || responseCode == RestException.VERSION_UPDATE_STRONG) {
                    AppInfo.getInstance().setAppInfo(response);
                    executeUpdateResult();
                } else {
                    version.checkFailed(response.getMessage());
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                Context context = getWRContext();
                if (context == null) return;

                VersionInterface version = reference.get();
                if (version == null) return;

                version.checkFailed(errorInfo);
            }
        });

    }

    private void executeUpdateResult() {
        if (AppInfo.getInstance().isForceUpdate()) {
            if (MyPreference.getInstance().read(HASCHECKNEWVERSION, false)) {
                return;
            }
            MyPreference.getInstance().write(HASCHECKNEWVERSION, true);
        }

        int needPop = AppInfo.getInstance().getNeedPopup();

        String appVersion = AppInfo.getInstance().getAPP_VERSION();

        String appSize = "";
        if (!isPatchUpdateValid()) {
            appSize = AppInfo.getInstance().getFileSize();
        } else {
            appSize = AppInfo.getInstance().getPatchSize();
        }

        String versionInfo = AppInfo.getInstance().getVersionInfo();
        String serverVersion = AppInfo.getInstance().getmServerVersion();

        String newVersionUrl = AppInfo.getInstance().getmUpdateURL();

        boolean pop = needPop == 1;
        if (reference.get() == null) return;
        if (reference.get().ignorePop()) pop = true;

        if (!pop) return;
        if (TextUtils.isEmpty(newVersionUrl)) {
            // TODO
//            ToastUtil.showInCenter("下载路径为空");
            return;
        }

        if (StringUtil.isBigThan(serverVersion, appVersion)) {

            //有版本更新时我的Tab显示红点提示
            reference.get().remindPoint();

            if (getActivity() == null) return;
            Resources res = getActivity().getResources();
            StringBuilder sb = new StringBuilder();
            sb.append("版本：" + serverVersion);
            sb.append("\n大小：" + appSize);
            sb.append("\n功能：\n" + versionInfo);
            if (AppInfo.getInstance().isForceUpdate()) {
                MyApplication.getAppPresenter().setUpDialog(true);
                AlertDialog alertDialog = DialogBuilder.showSimpleDialog(getActivity(), "更新", sb.toString(), res.getString(R.string.update_version_exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //强更用、见IwjwRespListener
                        MyPreference.getInstance().write(HASCHECKNEWVERSION, false);
                        exitApp();
                    }
                }, res.getString(R.string.update_version_update), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyPreference.getInstance().write(HASCHECKNEWVERSION, false);
                        versionUpdate();
                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        MyPreference.getInstance().write(HASCHECKNEWVERSION, false);
                        MyApplication.getAppPresenter().setUpDialog(false);
                    }
                });
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        MyPreference.getInstance().write(HASCHECKNEWVERSION, false);
                        MyApplication.getAppPresenter().setUpDialog(false);
                    }
                });
            } else {
                // TODO Version 里面 如果是首页 并且不是wifi联网，不进行更新操作
//                // 如果是首页 并且不是wifi联网，不进行更新操作
//                if (getActivity() instanceof IndexMainActivity && !SystemUtil.isWIFIAvaliable(getActivity())) {
//                    return;
//                }

                MyApplication.getAppPresenter().setUpDialog(true);
                Dialog updialog = DialogBuilder.showSimpleDialog(getActivity(), "更新", sb.toString(), res.getString(R.string.update_version_cancel), null, res.getString(R.string.update_version_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyPreference.getInstance().write(HASCHECKNEWVERSION, false);
                        versionUpdate();
                    }
                });
                updialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        MyPreference.getInstance().write(HASCHECKNEWVERSION, false);
                        MyApplication.getAppPresenter().setUpDialog(false);
                    }
                });
                updialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        MyPreference.getInstance().write(HASCHECKNEWVERSION, false);
                        MyApplication.getAppPresenter().setUpDialog(false);
                    }
                });
            }
        } else {
            MyPreference.getInstance().write(HASCHECKNEWVERSION, false);
            String version = TextUtils.isEmpty(appVersion) ? "" : "V" + appVersion;
            reference.get().checkLatest(version);
        }
    }

    private Activity getActivity() {
        return activityWeakReference == null ? null : activityWeakReference.get();
    }

    public void exitApp() {
        if (getActivity() == null) return;
        getActivity().finish();
        ExitEvent exitEvent = new ExitEvent();
        exitEvent.setForceExit(true);
        EventBus.getDefault().post(exitEvent);
    }

    public void versionUpdate() {
        if (NetCheckUtil.hasActiveNetwork()) {
            chooseUrlToDownload();
        } else {
            ToastUtil.showInCenter("当前未连接网络，请检查后重试");
        }
        saveAppVersion();
    }

    private void chooseUrlToDownload() {
        if (isPatchUpdateValid()) {
            isCurrentPatchUpdate = true;
            downloadFromPatchUrl();
        } else {
            isCurrentPatchUpdate = false;
            downloadFromAllUpdateUrl();
        }
    }

    private boolean isPatchUpdateValid() {
        String patchDownLoadUrl = AppInfo.getInstance().getPatchDownloadURL();
        return !TextUtils.isEmpty(patchDownLoadUrl);
    }

    private void downloadFromPatchUrl() {
        DownloadProgressDialogManger.getInstance().setMessage("正在下载");
        String patchDownLoadUrl = AppInfo.getInstance().getPatchDownloadURL();
        download(patchDownLoadUrl, AppInfo.getInstance().isForceUpdate());
    }

    private void downloadFromAllUpdateUrl() {
        DownloadProgressDialogManger.getInstance().setMessage("正在下载");
        String newVersionUrl = AppInfo.getInstance().getmUpdateURL();
        if (!TextUtils.isEmpty(newVersionUrl)) {
            download(newVersionUrl, AppInfo.getInstance().isForceUpdate());
        }
    }

    private void download(String url, boolean isForceUpdate) {
        if (FileRWManger.isSDCardAvailaleSize()) {
            if (getActivity() == null) return;

            DownloadProgressDialogManger.getInstance().initDialog(getActivity(), isForceUpdate,
                    ContextUtil.getApplicationResources().getString(R.string.download_iwjw_title));

            if (isCurrentPatchUpdate) {
                // 增量更新，显示原包大小加中间划线
                DownloadProgressDialogManger.getInstance().setOriTotalSize(AppInfo.getInstance().getFileSize());
            } else {
                // 全量更新，不显示原包大小加中间划线
                DownloadProgressDialogManger.getInstance().setOriTotalSizeInvisible();
            }


            FileDownLoaderManger.getInstance(getActivity()).download(url, AppInfo.getInstance().getmServerVersion(), this);
        } else {
            ToastUtil.showInCenter(ContextUtil.getApplicationResources().getString(R.string.sd_broken));
        }
    }

    /**
     * 预先保存应用版本号、在我的页面显示引导页时使用
     */
    public void saveAppVersion() {
        try {
            PackageManager pm = MyApplication.getInstance().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
            MyPreference.getInstance().write(CommonTag.CUR_APP_VERSION, pi.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDownLoadFail() {
        if (getActivity() == null) return;

        DownloadProgressDialogManger.getInstance().removeDialog();
        ToastUtil.showInCenter("下载过程中出现错误");
    }

    @Override
    public void onDownloadSize(int size, int totalSize) {
        if (getActivity() == null) return;

        DownloadNotificationManager.getInstance(getActivity()).updateDownloadSize(size, totalSize);
        DownloadProgressDialogManger.getInstance().updateDialog(size, totalSize);
    }

    @Override
    public void onDownloadFinish(final String fileName) {
        if (getActivity() == null) return;

        if (isCurrentPatchUpdate) {
            PatchUtil.mergeFile(MyApplication.getInstance().getPackageCodePath(), fileName, getMergedFileName(), new MergeProgressCallBack() {
                @Override
                public void onMergeStart() {
                    DownloadProgressDialogManger.getInstance().setMessage("正在升级");

                    // 增量合成中，notification设置不可点击，并且设置notification提醒，正在升级
                    DownloadNotificationManager.getInstance(getActivity()).onMergingPatch();
                }

                @Override
                public void onMergeSuccess(String target) {

                    // 增量合成完毕，notification设置点击提醒
                    if (AppInfo.getInstance().isForceUpdate()) {
                        DownloadProgressDialogManger.getInstance().setMessage("本次更新为强制升级，请安装新版本应用程序！");
                        DownloadNotificationManager.getInstance(getActivity()).setNotificationClickToInstallApk(target);
                    } else {
                        DownloadProgressDialogManger.getInstance().removeDialog();
                        DownloadNotificationManager.getInstance(getActivity()).setNotificationClickToInstallApk(target);
                    }

                    installApkByFileName(target);
                }

                @Override
                public void onMergeFailed() {
                    DownloadProgressDialogManger.getInstance().destory();

                    isCurrentPatchUpdate = false;
                    // 合包出现异常，重新下载整包
                    downloadFromAllUpdateUrl();
                }
            });
        } else {

            if (AppInfo.getInstance().isForceUpdate()) {
                DownloadProgressDialogManger.getInstance().setMessage("本次更新为强制升级，请安装新版本应用程序！");
                DownloadNotificationManager.getInstance(getActivity()).setNotificationClickToInstallApk(fileName);
            } else {
                DownloadProgressDialogManger.getInstance().removeDialog();
                DownloadNotificationManager.getInstance(getActivity()).setNotificationClickToInstallApk(fileName);
            }

            installApkByFileName(fileName);
        }
    }

    private String getMergedFileName() {
        return Environment.getExternalStorageDirectory()
                + File.separator + "ILICAI" + File.separator
                + AppConfig.versionName + File.separator
                + "download" + File.separator + "merged-" + AppInfo.getInstance().getAPP_VERSION() + ".apk";
    }


    public void installApkByFileName(String fileName) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");

            if (getActivity() != null) {
                getActivity().startActivity(intent);

                SystemUtil.exitApplication(getActivity());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
