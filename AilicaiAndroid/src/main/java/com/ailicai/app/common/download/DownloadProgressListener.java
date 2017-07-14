package com.ailicai.app.common.download;

/**
 * Created by Ted on 14-7-31.
 */
public interface DownloadProgressListener {
    void onDownloadSize(int size, int totalSize);

    void onDownloadFinish(String fileName);

    void onDownLoadFail();
}
