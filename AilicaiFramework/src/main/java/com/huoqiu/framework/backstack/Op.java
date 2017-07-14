package com.huoqiu.framework.backstack;


public interface Op {
    void perform(BackOpFragmentActivity activity);

    void setTag(String tag);

    String getTag();
}
