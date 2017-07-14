package com.ailicai.app.common.utils;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;


import com.ailicai.app.MyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Ted on 14-8-4.
 */
public class FileRWManger {

    /**
     * 得到当前存储设备的目录
     */
    public static final String SDK_ROOT_PATH = Environment.getExternalStorageDirectory()
            + File.separator + "IWJW" + File.separator;

    public static final String SDK_DATA_PATH = MyApplication.getInstance().getCacheDir().getPath() + File.separator;
    /**
     * 获取扩展SD卡设备状态
     */
    private static String SDStateString = Environment.getExternalStorageState();


    public static void initBaseAppFolder() {
        if (isHaveStorage()) {
            File baseFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "IWJW");
            if (baseFolder != null && baseFolder.exists()) {
                deleteFile(baseFolder);
            }
            baseFolder.mkdir();
        }

    }

    public static void writeFile(String filePath, String fileNme, String mContent) {
        if (TextUtils.isEmpty(mContent)) {
            return;
        }
        File folder = new File(filePath);
        if (folder != null && !folder.exists()) {
            if (!folder.mkdir() && !folder.isDirectory()) {
                //LogUtil.e("创建文件失败");
                return;
            }
        }

        String targetPath = filePath + fileNme;
        File targetFile = new File(targetPath);
        if (targetFile != null) {
            FileWriter ss;
            try {
                ss = new FileWriter(targetFile, false);
                ss.append(mContent + "\r\n");
                ss.flush();
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readFile(String filePath, String fileName) {
        String result = "";
        try {
            File file = new File(filePath + fileName);
            if (!file.exists()) {
                return result;
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputReader = new InputStreamReader(fileInputStream);
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            while ((line = bufReader.readLine()) != null) {
                result += line;
            }
            bufReader.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isHaveStorage() {
        String sdStatus = Environment.getExternalStorageState();
        return sdStatus.equals(Environment.MEDIA_MOUNTED);
    }


    public static long getSDAvailableSize() {
        if (SDStateString.equals(Environment.MEDIA_MOUNTED)) {
            // 取得sdcard文件路径
            File pathFile = Environment
                    .getExternalStorageDirectory();
            StatFs statfs = new StatFs(pathFile.getPath());
            // 获取SDCard上每个block的SIZE
            long nBlocSize = 0;
            // 获取可供程序使用的Block的数量
            long nAvailaBlock = 0;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                nBlocSize = statfs.getBlockSizeLong();
                nAvailaBlock = statfs.getAvailableBlocksLong();
            } else {
                nBlocSize = statfs.getBlockSize();
                nAvailaBlock = statfs.getAvailableBlocks();
            }

            // 计算 SDCard 剩余大小Byte
            long nSDFreeSize = nAvailaBlock * nBlocSize;
            return nSDFreeSize;
        }
        return 0;
    }

    public static boolean isSDCardAvailaleSize() {

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File path = Environment.getExternalStorageDirectory();// 取得sdcard文件路径

            StatFs stat = new StatFs(path.getPath());

            long blockSize = 0;

            long availableBlocks = 0;

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                blockSize = stat.getBlockSizeLong();
                availableBlocks = stat.getAvailableBlocksLong();
            } else {
                blockSize = stat.getBlockSize();
                availableBlocks = stat.getAvailableBlocks();
            }

            return availableBlocks * blockSize > 30 * 1024 * 1024;
        } else {
            return false;
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
}
