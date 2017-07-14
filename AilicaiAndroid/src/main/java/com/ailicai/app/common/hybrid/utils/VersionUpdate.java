package com.ailicai.app.common.hybrid.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.ailicai.app.common.utils.LogUtil;
import com.parse.codec.binary.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by duo.chen on 2016/5/17.
 */
public class VersionUpdate {

    public final static String HYBRID_FOLODER = File.separator + "hybrid";
    public final static String HYBRID_FILE = "hybrid.zip";

    private DownloadManager downloadManager;
    private WeakReference<Context> contextWeakReference;

    public VersionUpdate(Context context) {
        if (null != context) {
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            contextWeakReference = new WeakReference(context);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public long download(String url) {
        long id = 0;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        if (null != getWeakContext()) {
            try {
                //noinspection ConstantConditions
                request.setDestinationInExternalFilesDir(getWeakContext(),null, HYBRID_FILE);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                id = downloadManager.enqueue(request);
            } catch (Exception ignored) {

            }
        }
        return id;
    }

    public Cursor getDownloadCursor(DownloadManager.Query query){
        return downloadManager.query(query);
    }

    public void remove(long id){
        if (id != -1) {
            try {
                downloadManager.remove(id);
            } catch (Exception e){
                LogUtil.i(this.getClass().getCanonicalName(),e.toString());
            }
        }
    }

    public String getFileMd5(long id) {
        if (id >= 0) {
            File file = new File(getDownloadFile(id));
            if (file.exists()) {
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    InputStream inputStream = new FileInputStream(file);
                    byte[] bytes = new byte[2048];
                    int numBytes;
                    while ((numBytes = inputStream.read(bytes)) != -1) {
                        md.update(bytes, 0, numBytes);
                    }
                    byte[] digest = md.digest();
                    return new String(Hex.encodeHex(digest));

                } catch (NoSuchAlgorithmException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String getDownloadFile(long id) {
        return downloadManager.getUriForDownloadedFile(id) == null ? null :
                downloadManager.getUriForDownloadedFile(id).getPath();
    }

    public Context getWeakContext() {
        return contextWeakReference.get();
    }

    public static File getHybridFileDownloadDir(Context context){
        return new File(context.getExternalFilesDir(null).getAbsolutePath() + File.separator + HYBRID_FILE);
    }

    public static String getHybridFileDir(Context context){
        return context.getFilesDir().getAbsolutePath() + HYBRID_FOLODER;
    }

}
