package com.ailicai.app.common.download.update;



import java.io.File;

/**
 * 增量更新
 * Created by yanxin on 2015/9/25.
 */

public class TestPatch {

    /**
     * 创建差异包
     * @throws Exception
     */
    public void diffPatch() throws Exception {
        String oldFile = "D:/diff"+ File.separator+"3.1.apk";
        String newFile = "D:/diff"+ File.separator+"3.1.2.apk";
        String patchFile = "D:/diff"+ File.separator+"3.1.1diff.patch";
        PatchUtil.createPatch(oldFile, newFile, patchFile);
        System.out.println("创建差异包成功");
    }

    /**
     * 合并差异包
     * @throws Exception
     */
    public void mixPatch() throws Exception {
        String oldFile = "D:/diff"+ File.separator+"3.1.1.apk";
        String newFile = "D:/diff"+ File.separator+"3.1.1upgrade.apk";
        String patchFile = "D:/diff"+ File.separator+"3.1diff.patch";
        PatchUtil.mergeFile(oldFile, patchFile, newFile);
        System.out.println("合并差异包成功");
    }

}
