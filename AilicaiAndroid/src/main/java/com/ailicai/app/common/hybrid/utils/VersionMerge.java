package com.ailicai.app.common.hybrid.utils;

import android.content.Context;
import android.util.Log;

import com.ailicai.app.common.hybrid.HybridEngine;
import com.ailicai.app.common.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by duo.chen on 2016/5/17.
 */
public class VersionMerge {

    public enum MergeState{
        READY,
        MERGING,
        FINISH,
        FAILED
    }

    public VersionMerge(){
    }

    public void mergewithOption(final Context context, final int option, final File file, final IMergeListener iMergeListener) {
        if (file.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        unZipFiles(file, VersionUpdate.getHybridFileDir(context),option ,iMergeListener);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private static void unZipFiles(java.io.File zipFile, String descDir, int option,
                                   IMergeListener mergeListener) {
        mergeListener.mergeState(MergeState.MERGING);
        try {
            if (option == 1) {
                deleteFile(new File(descDir));
            }

            ZipFile zf = new ZipFile(zipFile);
            Enumeration entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String zipEntryName = entry.getName();

                if (entry.isDirectory()) {
                    File fmd = new File(descDir + File.separator + zipEntryName);
                    fmd.mkdirs();
                } else {

                    LogUtil.i(HybridEngine.TAG,"zipEntryName " + zipEntryName + " descDir " + descDir);
                    Log.i("zip","zipEntryName " + zipEntryName + " descDir " + descDir);
                    InputStream in = zf.getInputStream(entry);
                    File file = new File(descDir + File.separator + zipEntryName);
                    File par = new File(file.getParent());
                    if (!par.exists()) {
                        //mkdir -p
                        par.mkdirs();
                    }
                    OutputStream out = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                    out.flush();
                    in.close();
                    out.close();
                }
            }

            LogUtil.i(HybridEngine.TAG,"VersionMerge finish");
            mergeListener.mergeState(MergeState.FINISH);

        } catch (IOException e) {
            mergeListener.mergeState(MergeState.FAILED);
            e.printStackTrace();
        }

    }

    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }

    public interface IMergeListener {
        void mergeState(MergeState mergeState);
    }

}
