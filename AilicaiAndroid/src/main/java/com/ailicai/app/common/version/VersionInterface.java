package com.ailicai.app.common.version;

/**
 * Created by David on 15/8/10.
 */
public interface VersionInterface {

    /**
     * 更新红点提示.
     */
    void remindPoint();

    /**
     * 更新服务开始 用于添加loading.
     */
    void checkStart();

    /**
     * 更新成功.
     */
    void checkSuccess();

    /**
     * 更新失败.
     * @param message 失败信息
     */
    void checkFailed(String message);

    /**
     * 已经是最新版本.
     * @param version
     */
    void checkLatest(String version);

    /**
     * 设置界面忽略pop字段.
     * @return
     */
    boolean ignorePop();

    /***
     * 弹窗失败
     */
    void popFailed();

}
