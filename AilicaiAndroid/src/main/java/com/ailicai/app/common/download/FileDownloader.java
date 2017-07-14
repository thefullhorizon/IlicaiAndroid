package com.ailicai.app.common.download;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import com.ailicai.app.common.utils.LogUtil;
import com.ailicai.app.common.utils.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileDownloader {
    public static final String DOWNLOADING = "DOWNLOADING";
    public static final String STOP = "STOP";
    public static final String CANCEL = "CANCEL";
    public String version;
    private String state;// 下载状态
    /* 已下载文件长度 */
    private int downloadSize = 0;
    /* 原始文件长度 */
    private int fileSize = 0;
    /* 线程数 */
    private DownloadThread[] threads;
    /* 本地保存文件 */
    private File saveFile;
    /* 本地保存文件 */
    private File saveTempFile;
    /* 缓存各线程下载的长度 */
    //private Map<Integer, Integer> data = new ConcurrentHashMap<Integer, Integer>();
    /* 每条线程下载的长度 */
    private int block;
    /* 下载路径 */
    private String downloadUrl;
    /* 断点续传 */
    private boolean canRestart = false;
    /* 是否finish */
    private boolean notFinish;
    private String fileName;
    private String tempFileName;
    //    private RecordDownloadInfo recordDownloadInfo;
    private ArrayList<DownloadListener> downloadProgressListeners = new ArrayList<DownloadListener>();

    private int[] startPos;
    private int[] endPos;

    /**
     * 构建文件下载器
     *
     * @param downloadUrl 下载路径
     * @param fileSaveDir 文件保存目录
     * @param threadNum   下载线程数
     * @throws DownloadException
     */
    public FileDownloader(Context context, String downloadUrl, File fileSaveDir, int threadNum, String version, String versionControlFileName) throws DownloadException {
        try {
            this.downloadUrl = downloadUrl;
//            recordDownloadInfo = new RecordDownloadInfo(context, versionControlFileName);
            this.version = version;
            URL url = new URL(this.downloadUrl);
            if (!fileSaveDir.exists())
                fileSaveDir.mkdirs();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(20 * 1000);//超时20s
            //conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("GET");
            conn.connect();
            printResponseHeader(conn);
            if (conn.getResponseCode() == 200) {
                this.fileSize = conn.getContentLength();// 根据响应获取文件大小
                if (this.fileSize <= 0) {
                    // 仅能单线程，且不可断点续传
                    canRestart = false;
                    fileSize = 0;
                    throw new DownloadException();
                } else {
                    canRestart = true;
                }
                if (version.equals("0.0")) {
                    fileName = getMD5FileName(downloadUrl);// 获取文件名称
                } else {
                    fileName = getFileName(conn);// 获取文件名称
                }
                tempFileName = fileName + ".tmp";
                this.saveFile = new File(fileSaveDir, fileName);// 构建保存文件
                this.saveTempFile = new File(fileSaveDir, tempFileName);
                if (saveFile.exists()) {
                    saveFile.delete();
                }
                if (saveTempFile.exists()) {
                    saveTempFile.delete();
                }
                if (canRestart) {
                    threads = new DownloadThread[threadNum];

                    startPos = new int[threadNum];
                    endPos = new int[threadNum];
                    this.block = (this.fileSize % this.threads.length) == 0 ? this.fileSize / this.threads.length : this.fileSize / this.threads.length + 1;
                    for (int i = 0; i < threadNum; i++) {
                        int size = i * (fileSize / threadNum);
                        startPos[i] = size;

                        //设置最后一个结束点的位置
                        if (i == threadNum - 1) {
                            endPos[i] = fileSize;
                        } else {
                            size = (i + 1) * (fileSize / threadNum);
                            endPos[i] = size;
                        }
                    }
                }
            } else {
                throw new DownloadException();
            }
        } catch (Exception e) {
            throw new DownloadException();
        }

    }

    /**
     * 打印Http头字段
     *
     * @param http
     */

    public static void printResponseHeader(HttpURLConnection http) {
        Map<String, String> header = getHttpResponseHeader(http);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            String key = entry.getKey() != null ? entry.getKey() + ":" : "";
            LogUtil.d(key + entry.getValue());
        }
    }

    /**
     * 获取Http响应头字段
     *
     * @param http
     * @return
     */
    public static Map<String, String> getHttpResponseHeader(HttpURLConnection http) {
        Map<String, String> header = new LinkedHashMap<String, String>();
        for (int i = 0; ; i++) {
            String mine = http.getHeaderField(i);
            if (mine == null)
                break;
            header.put(http.getHeaderFieldKey(i), mine);
        }
        return header;
    }

    /**
     * 获取线程数
     */
    public int getThreadSize() {
        return threads.length;
    }

    /**
     * 获取文件大小
     *
     * @return
     */
    public int getFileSize() {
        return fileSize;
    }

//    /**
//     * 累计已下载大小
//     *
//     * @param size
//     */
//    protected synchronized void append(int size) {
//        downloadSize += size;
//    }
//
//    /**
//     * 更新指定线程最后下载的位置
//     *
//     * @param threadId 线程id
//     * @param pos      最后下载的位置
//     */
//    protected synchronized void update(int threadId, int pos, int size) {
//        this.data.put(threadId, pos);
//        this.recordDownloadInfo.updatePieces(threadId, pos);
//    }

//    private boolean checkFileDownloaded(String version) {
//        boolean taskExist = recordDownloadInfo.isExists(downloadUrl, version, fileSize);
//        if (taskExist) {
//            if (saveFile.exists()) {
//                long saveFileSize = saveFile.length();
//                boolean fileLength = false;
//                if (fileSize > 0) {
//                    fileLength = (fileSize == saveFileSize);
//                } else {
//                    data = recordDownloadInfo.readPieces();
//                    long downTempSize = 0;
//                    for (int i = 0; i < this.data.size(); i++) {
//                        downTempSize += this.data.get(i + 1);
//                    }
//                    fileLength = (downTempSize == saveFileSize);
//                }
//                boolean tempDeleted = !saveTempFile.exists();
//                return fileLength && tempDeleted;
//            }
//        }
//        return false;
//
//    }

    private String getMD5FileName(String url) {
        return StringUtil.getMD5(url.getBytes());
    }

    /**
     * 获取文件名
     *
     * @param conn
     * @return
     */
    private String getFileName(HttpURLConnection conn) {
        String filename = "";
        filename = this.downloadUrl.substring(this.downloadUrl.lastIndexOf('/') + 1);
        if (filename == null || "".equals(filename.trim())) {// 如果获取不到文件名称
            for (int i = 0; ; i++) {
                String mine = conn.getHeaderField(i);
                if (mine == null)
                    break;
                if ("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase(Locale.ENGLISH))) {
                    Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase(Locale.ENGLISH));
                    if (m.find())
                        return m.group(1);
                }
            }
            filename = UUID.randomUUID() + "";// 默认取一个文件名
        }
        return filename;

    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    /**
     * 开始下载文件
     *
     * @param downloadProgressListener 监听下载数量的变化,如果不需要了解实时下载的数量,可以设置为null
     * @return 已下载文件大小
     * @throws Exception
     */
    public int download(DownloadListener downloadProgressListener) throws Exception {
        if (!downloadProgressListeners.contains(downloadProgressListener)) {
            downloadProgressListeners.add(downloadProgressListener);
        }

        try {
            state = DOWNLOADING;
            if (this.fileSize > 0) {
                RandomAccessFile randOut = new RandomAccessFile(this.saveTempFile, "rw");
                randOut.setLength(this.fileSize);
                randOut.close();

                for (int i = 0; i < this.threads.length; i++) {// 开启线程进行下载
                    //this.threads[i] = new DownloadThread(this, url, this.saveTempFile, this.block, this.data.get(i + 1), i + 1);
                    this.threads[i] = new DownloadThread(this, this.downloadUrl, this.saveTempFile, startPos[i], endPos[i]);
                    this.threads[i].start();
                }

                boolean notFinish = true;// 下载未完成
                Handler handler = new Handler(Looper.getMainLooper());
                while (notFinish) {// 循环判断所有线程是否完成下载
                    notFinish = false;// 假定全部线程下载完成
                    if (!state.equals(DOWNLOADING)) {
                        break;
                    }

                    downloadSize = 0;
                    for (int i = 0; i < this.threads.length; i++) {
                        downloadSize += this.threads[i].getDownLength();
                        if (this.threads[i] != null && !this.threads[i].isFinish()) {// 如果发现线程未完成下载
                            notFinish = true;// 设置标志为下载没有完成
                            if (this.threads[i].getDownLength() == -1) {// 如果下载失败,再重新下载
                                this.threads[i] = new DownloadThread(this, this.downloadUrl, this.saveTempFile, startPos[i], endPos[i]);
                                this.threads[i].start();
                            }
                        }
                    }

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            for (DownloadListener listener : downloadProgressListeners) {
                                if (listener != null) {
                                    listener.onDownloadSize(downloadSize, fileSize);// 通知目前已经下载完成的数据长度
                                }
                            }
                        }
                    });

                    Thread.sleep(500);
                }
                Log.e("downloadSize======>",downloadSize+"");
                Log.e("fileSize=====",fileSize+"");
                if (downloadSize == fileSize) {
                    moveTempFileToWholeFile(saveTempFile, saveFile);
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            for (DownloadListener listener : downloadProgressListeners) {
                                if (listener != null) {
                                    listener.onDownloadFinish(saveFile.getAbsolutePath());// 通知目前已经下载完成
                                }
                            }
                        }
                    });
                }
            }

        } catch (Exception e) {
            try {
                if (saveTempFile != null && saveTempFile.exists()) {
                    saveTempFile.delete();
                }
            } catch (Exception e2) {
            }
            throw new Exception("file download fail");
        }

        return this.downloadSize;
    }

    private boolean moveTempFileToWholeFile(File sourceFile, File destFile) {
        boolean copySuccess = false;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(sourceFile);
            out = new FileOutputStream(destFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            copySuccess = true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            if (!copySuccess) {
                if (destFile != null && destFile.exists()) {
                    destFile.delete();
                }
            } else {
                if (sourceFile != null && sourceFile.exists()) {
                    sourceFile.delete();
                }
            }
        } catch (Exception e) {
        }
        return copySuccess;
    }

    public void stopDownload() {
        state = STOP;
    }

    public void cancleDownload() {
        state = CANCEL;
    }

    public boolean isNotFinish() {
        return notFinish;
    }

    public void setNotFinish(boolean notFinish) {
        this.notFinish = notFinish;
    }

    public void removeListener(DownloadListener listener) {
        downloadProgressListeners.remove(listener);
    }

    public void addListener(DownloadListener listener) {
        downloadProgressListeners.add(listener);
    }

    public void cleanListener() {
        downloadProgressListeners.clear();
    }
}