package com.ailicai.app.common.version;

/**
 * Created by zhouxuan on 16/4/28.
 */
public interface MergeProgressCallBack {

    void onMergeStart();
    void onMergeSuccess(String target);
    void onMergeFailed();
}
