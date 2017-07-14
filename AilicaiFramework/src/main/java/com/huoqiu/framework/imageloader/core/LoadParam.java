package com.huoqiu.framework.imageloader.core;

/**
 * name: LoadParam <BR>
 * description:  <BR>
 * create date: 2015/7/13
 *
 * @author: IWJW Zhou Xuan
 */
public class LoadParam {

    public String imgUri;
    public int loadingPicId;
    public int emptyPicId;
    public int failPicId;
    public float cornerRadiusInDp;

    public boolean isRound() {
        return  cornerRadiusInDp > 0;
    }


    /************* the getter and setter**************/
    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public int getLoadingPicId() {
        return loadingPicId;
    }

    public void setLoadingPicId(int loadingPicId) {
        this.loadingPicId = loadingPicId;
    }

    // 如果为空值，默认为加载中的图片
    public int getEmptyPicId() {
        return emptyPicId == 0 ? loadingPicId : emptyPicId;
    }

    public void setEmptyPicId(int emptyPicId) {
        this.emptyPicId = emptyPicId;
    }

    // 如果为空值，默认为加载中的图片
    public int getFailPicId() {
        return failPicId == 0 ? loadingPicId : failPicId;
    }

    public void setFailPicId(int failPicId) {
        this.failPicId = failPicId;
    }

    public float getCornerRadiusInDp() {
        return cornerRadiusInDp;
    }

    public void setCornerRadiusInDp(float cornerRadiusInDp) {
        this.cornerRadiusInDp = cornerRadiusInDp;
    }
    /************* the getter and setter**************/
}
