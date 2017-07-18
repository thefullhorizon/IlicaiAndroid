package com.ailicai.app.ui.asset.treasure;

/**
 * Created by David on 16/3/9.
 */
public class EmptyValue {

    private ProductCategory category = ProductCategory.Apply;

    private boolean showLoading = true;

    private int height;

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public boolean isShowLoading() {
        return showLoading;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
