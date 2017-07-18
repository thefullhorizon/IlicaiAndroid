package com.ailicai.app.widget.pop;

/**
 * Created by Owen on 2016/5/26
 */
public class CustomPopWindowParams {

    private int whichOne;//显示哪一个三角形(1,2,3,4)
    private int selection;//listview选中条目
    private boolean isStyle;//是否是右边有勾的样式
    private int animLocation;//动画显示位置 0：中间  1左边  2右边

//    private boolean shouldWidthWrapContent = false; // popupwindow的宽度是否只是WrapContent
    private int customWidth = -1;

    public int getWhichOne() {
        return whichOne;
    }

    public void setWhichOne(int whichOne) {
        this.whichOne = whichOne;
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public boolean isStyle() {
        return isStyle;
    }

    public void setStyle(boolean style) {
        isStyle = style;
    }

    public int getAnimLocation() {
        return animLocation;
    }

    public void setAnimLocation(int animLocation) {
        this.animLocation = animLocation;
    }

//    public boolean isShouldWidthWrapContent() {
//        return shouldWidthWrapContent;
//    }
//
//    public void setShouldWidthWrapContent(boolean shouldWidthWrapContent) {
//        this.shouldWidthWrapContent = shouldWidthWrapContent;
//    }

    public int getCustomWidth() {
        return customWidth;
    }

    public void setCustomWidth(int customWidth) {
        this.customWidth = customWidth;
    }
}
