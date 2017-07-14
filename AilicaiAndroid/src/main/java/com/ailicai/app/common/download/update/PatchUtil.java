package com.ailicai.app.common.download.update;

import android.os.Handler;
import android.os.Looper;

import com.ailicai.app.common.version.MergeProgressCallBack;
import com.nothome.delta.Delta;
import com.nothome.delta.DiffWriter;
import com.nothome.delta.GDiffPatcher;
import com.nothome.delta.GDiffWriter;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 生成差异包
 * 合并差异包
 */
public class PatchUtil {

    /**
     * 生成差异包
     *
     * @param oldFile
     * @param newFile
     * @param patchFile
     * @throws Exception
     */
    public static void createPatch(String oldFile, String newFile, String patchFile) throws Exception {
        DiffWriter output = null;

        File sourceFile = null;
        File targetFile = null;
        File diffFile = new File(patchFile);

        sourceFile = new File(oldFile);
        if (!sourceFile.exists()) {
            throw new RuntimeException("原文件找不到");
        }

        targetFile = new File(newFile);
        if (!targetFile.exists()) {
            throw new RuntimeException("目标文件找不到");
        }

        if (!diffFile.exists()) {
            diffFile.createNewFile();
        }


        output = new GDiffWriter(new DataOutputStream(new BufferedOutputStream(new FileOutputStream(diffFile))));

        if (sourceFile.length() > Integer.MAX_VALUE
                || targetFile.length() > Integer.MAX_VALUE) {
            System.err
                    .println("source or target is too large, max length is "
                            + Integer.MAX_VALUE);
            System.err.println("aborting..");
        }
        Delta d = new Delta();
        d.compute(sourceFile, targetFile, output);
    }

    public static File mergeFile(final String source, final String patch, String target) throws Exception {
        GDiffPatcher patcher = new GDiffPatcher();
        File deffFile = new File(patch);
        File updatedFile = new File(target);
        patcher.patch(new File(source), deffFile, updatedFile);
        return updatedFile;
    }

    public static void mergeFile(final String source, final String patch, final String target, final MergeProgressCallBack callBack) {
        final GDiffPatcher patcher = new GDiffPatcher();
        final File deffFile = new File(patch);
        final File updatedFile = new File(target);

        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onMergeStart();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    patcher.patch(new File(source), deffFile, updatedFile);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onMergeSuccess(target);
                        }
                    });

                } catch (Exception e) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onMergeFailed();
                        }
                    });
                }
            }
        }).start();
    }
}
