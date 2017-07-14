package com.ailicai.app.common.download;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.huoqiu.framework.app.AppConfig;

import java.io.File;
import java.util.HashMap;

/**
 * 文件下载管理器
 * Created by Ted on 14-7-31.
 */
public class FileDownLoaderManger {

    private Context mContext;
    private HashMap<String, Boolean> downingMap = new HashMap<String, Boolean>();
    private String downloadPath;//下载目录
    private int threadNum = 1;// 下载线程数
    private HashMap<String, FileDownloader> hashMap = new HashMap<String, FileDownloader>();
    private static FileDownLoaderManger mFileDownLoaderManger = null;

    public FileDownLoaderManger(Context context) {
        this.mContext = context;
        downloadPath = Environment.getExternalStorageDirectory()
                + File.separator + "IWJW" + File.separator
                + AppConfig.versionName + File.separator
                + "download" + File.separator;
    }

    public static FileDownLoaderManger getInstance(Context context) {
        if (null == mFileDownLoaderManger) {
            mFileDownLoaderManger = new FileDownLoaderManger(context);
        }
        return mFileDownLoaderManger;
    }

    public void cancel(String url) {
        if (hashMap.get(url) != null) {
            hashMap.get(url).cleanListener();
            hashMap.get(url).stopDownload();
            hashMap.remove(url);
            downingMap.remove(url);
        }
    }

    public void removeListener(String url, DownloadListener downloadProgressListener) {
        if (hashMap.get(url) != null) {
            hashMap.get(url).removeListener(downloadProgressListener);
        }
    }

    public void removeFinishOrFailUrl(String url) {
        downingMap.remove(url);
    }

    private void download(final Context context, final String versionControlFileName, final String savePath, final String downloadUrl, final String version, final DownloadListener downloadListener) {
        if (downingMap.get(downloadUrl) != null && downingMap.get(downloadUrl)) {
            return;
        } else {
            downingMap.put(downloadUrl, true);
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    if (hashMap.get(downloadUrl) != null) {
                        hashMap.get(downloadUrl).download(downloadListener);
                    } else {
                        FileDownloader downloader = new FileDownloader(context, downloadUrl, new File(savePath), threadNum, version, versionControlFileName);
                        hashMap.put(downloadUrl, downloader);
                        downloader.download(downloadListener);
                    }
                } catch (DownloadException e) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            if (downloadListener != null) {
                                downloadListener.onDownLoadFail();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    downingMap.remove(downloadUrl);
                    hashMap.remove(downloadUrl);
                }
            }
        }).start();
    }

    public void download(final String downloadUrl, final String version, final DownloadListener il) {
        threadNum = 5;
        download(mContext, "task.xml", downloadPath, downloadUrl, version, il);
    }
}
