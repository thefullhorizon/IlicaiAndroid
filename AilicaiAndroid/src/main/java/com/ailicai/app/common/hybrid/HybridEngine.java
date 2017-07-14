package com.ailicai.app.common.hybrid;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Log;

import com.ailicai.app.MyApplication;
import com.ailicai.app.common.hybrid.utils.DownloadVersion;
import com.ailicai.app.common.hybrid.utils.VersionMerge;
import com.ailicai.app.common.hybrid.utils.VersionUpdate;
import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.NetCheckUtil;
import com.ailicai.app.model.request.HybridVersionUpdateRequest;
import com.ailicai.app.model.response.HybridVersionResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by duo.chen on 2016/5/17.10:39
 */
public class HybridEngine {

    public final static String TAG = "HybridEngine";

    private VersionUpdate versionUpdate;
    private VersionMerge versionMerge;
    private WeakReference<Context> context;
    private Router router;

    private static HybridEngine hybridEngine;

    private HybridEngine() {
        this.context = new WeakReference<>(MyApplication.getInstance().getApplicationContext());
        init();
    }

    public static HybridEngine getHybridEngine() {
        if (null == hybridEngine) {
            hybridEngine = new HybridEngine();
        }
        return hybridEngine;
    }

    public void init() {
        try {
            initRouter();
            if (null == versionUpdate) {
                versionUpdate = new VersionUpdate(getContext());
            }
            if (null == versionMerge) {
                versionMerge = new VersionMerge();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateCheck() {
        LogUtil.i(TAG, "updateCheck");
        downloadIfNeed();
    }

    private void initRouter() throws IOException {
        if (null != getContext() && router == null) {
            File file = new File(VersionUpdate.getHybridFileDir(getContext()) +
                    File.separator + "router.json");
            if (file.exists()) {
                FileInputStream routerJson = new FileInputStream(file);
                byte[] butter = new byte[routerJson.available()];
                routerJson.read(butter);
                router = JSON.parseObject(butter, Router.class, Feature.IgnoreNotMatch, Feature
                        .AllowISO8601DateFormat);

                LogUtil.i(TAG, router.toString());
            }
        }
    }

    public void downloadIfNeed() {

        final DownloadVersion.DownInfo downInfo = DownloadVersion.getSaveDownloadInfo(getContext());

        checkVersion(new IwjwRespListener<HybridVersionResponse>() {

            @Override
            public void onJsonSuccess(HybridVersionResponse jsonObject) {

                LogUtil.i(TAG, "" + jsonObject.toString());

                if (!TextUtils.isEmpty(jsonObject.getDownloadUrl())) {

                    String md5 = jsonObject.getFileMd5();

                    if (null != downInfo && downInfo.getMd5() != null) {
                        if (downInfo.getMd5().equals(md5)) {
                            // if file is downloading
                            if (downInfo.getId() != -1) {
                                DownloadManager.Query query = new DownloadManager.Query();
                                query.setFilterById(downInfo.getId());
                                Cursor cursor = versionUpdate.getDownloadCursor(query);
                                if (cursor.moveToFirst()) {
                                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager
                                            .COLUMN_STATUS));

                                    LogUtil.i(TAG, "download status " + status);

                                    switch (status) {
                                        case DownloadManager.STATUS_FAILED:
                                        case DownloadManager.STATUS_PAUSED:
                                        case DownloadManager.STATUS_PENDING:
                                            versionUpdate.remove(downInfo.getId());
                                            downLoad(new DownloadVersion.DownInfo(jsonObject
                                                    .getDownloadUrl(),
                                                    jsonObject.getFileMd5(),
                                                    jsonObject.getVersion(),
                                                    jsonObject.getUpdateMethod()));
                                            break;
                                        case DownloadManager.STATUS_RUNNING:
                                            break;
                                        case DownloadManager.STATUS_SUCCESSFUL:
                                            merge(downInfo.getId());
                                        default:
                                            break;
                                    }
                                } else {
                                    DownloadVersion.deleteSavedDownloadInfo(HybridEngine.getHybridEngine().getContext());
                                }
                            }
                        } else {
                            //if md5 of downloading file != server md5,stop download and download
                            // the given file
                            versionUpdate.remove(downInfo.getId());
                            downLoad(new DownloadVersion.DownInfo(jsonObject.getDownloadUrl(),
                                    jsonObject.getFileMd5(),
                                    jsonObject.getVersion(),
                                    jsonObject.getUpdateMethod()));
                        }
                    } else {
                        downLoad(new DownloadVersion.DownInfo(jsonObject.getDownloadUrl(),
                                jsonObject.getFileMd5(),
                                jsonObject.getVersion(),
                                jsonObject.getUpdateMethod()));
                    }

                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                LogUtil.i(TAG, "hybridEngine checkVersion fail " + errorInfo);
            }
        });

    }

    public void downLoad(DownloadVersion.DownInfo downInfo) {
        if (null == versionUpdate) {
            versionUpdate = new VersionUpdate(getContext());
        }
        long id = versionUpdate.download(downInfo.getDownloadUrl());
        downInfo.setId(id);
        DownloadVersion.saveDownloadInfo(getContext(), downInfo);
        CompleteReceiver receiver = new CompleteReceiver(id);
        hybridEngine.getContext().registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public Router getRouter() throws IOException {
        initRouter();
        return router;
    }

    public void merge(long id) {
        if (null == versionMerge) {
            versionMerge = new VersionMerge();
        }

        final DownloadVersion.DownInfo info = DownloadVersion.getSaveDownloadInfo(getContext());
        int option = info.getMergeOption();
        String fileId = versionUpdate.getDownloadFile(id);
        if (!TextUtils.isEmpty(fileId)) {
            File file = new File(fileId);
            //md5 check
            Log.i(TAG, "server md5 " + info.getMd5() + "and caculate file md5 " + versionUpdate.getFileMd5(id));
            if (!TextUtils.isEmpty(info.getMd5()) && !TextUtils.isEmpty(versionUpdate.getFileMd5
                    (id))) {
                if (info.getMd5().equals(versionUpdate.getFileMd5(id))) {
                    versionMerge.mergewithOption(getContext(), option, file,
                            new VersionMerge.IMergeListener() {
                                @Override
                                public void mergeState(VersionMerge.MergeState mergeState) {
                                    if (mergeState == VersionMerge.MergeState.FINISH
                                            || mergeState == VersionMerge.MergeState.FAILED) {
                                        versionUpdate.remove(info.getId());
                                        DownloadVersion.deleteSavedDownloadInfo(getContext());
                                        if (mergeState == VersionMerge.MergeState.FINISH) {
                                            HashMap map = new HashMap<>();
                                            map.put("ver", info.getVersion());
//                                            EventLog.upEventLog("640", JSON.toJSONString(map));
                                            DownloadVersion.saveOrUpdateDownloadVersion(getContext(), info.getVersion());
                                        }
                                    }
                                }
                            });
                } else {
                    versionUpdate.remove(info.getId());
                }
            } else {
                versionUpdate.remove(info.getId());
            }
        } else {
            Log.e(TAG, "fileId is Empty");
        }
    }

    public String getVersion() {
        return DownloadVersion.getDownloadVersion(getContext());
    }

    public void checkVersion(IwjwRespListener iwjwRespListener) {
        HybridVersionUpdateRequest request = new HybridVersionUpdateRequest();
        if (ConnectivityManager.TYPE_WIFI == NetCheckUtil.getNetWorkTypeInt()) {
            request.setNetType(2);
        } else {
            request.setNetType(1);
        }
        request.setVersion(TextUtils.isEmpty(getVersion()) ? "0" : getVersion());
        ServiceSender.exec(getContext(), request, iwjwRespListener);
    }

    public Context getContext() {
        return context.get();
    }

    class CompleteReceiver extends BroadcastReceiver {

        long id;

        public CompleteReceiver(long id) {
            this.id = id;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (id == completeDownloadId) {
                //TODO
                // download finish
                merge(id);
            }
        }
    }
}
