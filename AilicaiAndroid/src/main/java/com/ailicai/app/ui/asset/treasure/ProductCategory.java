package com.ailicai.app.ui.asset.treasure;

/**
 * Created by David on 16/3/9.
 */
public enum ProductCategory {
    Apply(1, "暂无申购的项目", "去申购"), Holder(2, "暂无持有的项目", "去申购"), Expired(3, "暂无回款的项目", "去申购");
    int type;
    String content;
    String action;

    ProductCategory(int type, String content, String action) {
        this.content = content;
        this.action = action;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
