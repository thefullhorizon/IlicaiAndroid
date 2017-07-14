package com.ailicai.app.common.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Ted on 14-7-31.
 */
public class DownloadThread extends Thread {
    private static final int BUFFER_SIZE = 1024 * 1024;
    private File saveFile;
    private URL downUrl;//下载地址
    private String url;
    private int block;//长度
    private int threadId = -1; //下载线程
    private int downLength;//已下载长度
    private boolean finish = false;
    private FileDownloader downloader;
    private int startPos = 0, endPos = 0;

    public DownloadThread(FileDownloader downloader, URL downUrl, File saveFile, int block, int downLength, int threadId) {
        this.downUrl = downUrl;
        this.saveFile = saveFile;
        this.block = block;
        this.downloader = downloader;
        this.threadId = threadId;
        this.downLength = downLength;
    }

    public DownloadThread(FileDownloader downloader, String url, File saveFile, int startPos, int endPos) {
        this.url = url;
        this.saveFile = saveFile;
        this.startPos = startPos;
        this.downloader = downloader;
        this.endPos = endPos;
    }

    public static void setHeader(URLConnection conn) {
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 4.2.1; Nexus 7 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19");
        conn.setRequestProperty("Accept-Language", "en-us,en;q=0.7,zh-cn;q=0.3");
        conn.setRequestProperty("Accept-Encoding", "utf-8");
        conn.setRequestProperty("Keep-Alive", "300");
        conn.setRequestProperty("connnection", "keep-alive");
        conn.setRequestProperty("Cache-conntrol", "max-age=0");
    }

    @Override
    public void run() {
        try {
            URL url = new URL(this.url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //setHeader(conn);

            conn.setRequestProperty("Range", "bytes=" + startPos + "-" + (endPos - 1));//设置获取实体数据的范围
            InputStream inStream = conn.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inStream);
            byte[] buffer = new byte[BUFFER_SIZE];
            int offset = 0;
            RandomAccessFile threadfile = new RandomAccessFile(this.saveFile, "rwd");
            threadfile.seek(startPos);
            while ((offset = bufferedInputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
                threadfile.write(buffer, 0, offset);
                downLength += offset;
                if (!downloader.getState().equals(FileDownloader.DOWNLOADING)) {
                    break;
                }
            }
            threadfile.close();
            bufferedInputStream.close();
            inStream.close();
            if (downloader.getState().equals(FileDownloader.DOWNLOADING)) {
                this.finish = true;
            }
        } catch (Exception e) {
            this.downLength = -1;
        }
    }


//    @Override
//    public void run() {
//        if (block == 0 && downLength == 0) {
//            /**无法断点续传的文件*/
//            try {
//                HttpURLConnection connection = (HttpURLConnection) downUrl.openConnection();
//                InputStream in = connection.getInputStream();
//                BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
//                FileOutputStream fout = new FileOutputStream(saveFile);
//                byte[] buffer = new byte[BUFFER_SIZE];
//                int readed = 0;
//                while ((readed = bufferedInputStream.read(buffer)) != -1) {
//                    downLength += readed;
//                    fout.write(buffer, 0, readed);
//                    downloader.update(this.threadId, downLength);
//                }
//                fout.close();
//                bufferedInputStream.close();
//                in.close();
//                downloader.setNotFinish(false);
//            } catch (IOException e) {
//                e.printStackTrace();
//                this.downLength = -1;
//            }
//        } else {
//            if (downLength < block) {//未下载完成
//                try {
//                    HttpURLConnection http = (HttpURLConnection) downUrl.openConnection();
//                    http.setConnectTimeout(20 * 1000);
//                    //int startPos = block * (threadId - 1) + downLength;//开始位置
//                    //int endPos = block * threadId - 1;//结束位置
//                    http.setRequestProperty("Range", "bytes=" + startPos + "-" + (endPos - 1));//设置获取实体数据的范围
//                    http.setRequestProperty("Connection", "keep-alive");
//                    http.setRequestProperty("Cache-conntrol", "max-age=0");
//                    InputStream inStream = http.getInputStream();
//                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inStream);
//                    byte[] buffer = new byte[BUFFER_SIZE];
//                    int offset = 0;
//                    RandomAccessFile threadfile = new RandomAccessFile(this.saveFile, "rwd");
//                    threadfile.seek(startPos);
//                    while ((offset = bufferedInputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
//                        threadfile.write(buffer, 0, offset);
//                        downLength += offset;
//                        downloader.update(this.threadId, downLength);
//                        downloader.append(offset);
//                        if (!downloader.getState().equals(FileDownloader.DOWNLOADING)) {
//                            break;
//                        }
//                    }
//                    threadfile.close();
//                    bufferedInputStream.close();
//                    inStream.close();
//                    if (downloader.getState().equals(FileDownloader.DOWNLOADING)) {
//                        this.finish = true;
//                    }
//                } catch (Exception e) {
//                    this.downLength = -1;
//                }
//            }
//        }
//
//    }

    /**
     * 下载是否完成
     *
     * @return
     */
    public boolean isFinish() {
        return finish;
    }

    /**
     * 已经下载的内容大小
     *
     * @return 如果返回值为-1,代表下载失败
     */
    public long getDownLength() {
        return downLength;
    }
}
