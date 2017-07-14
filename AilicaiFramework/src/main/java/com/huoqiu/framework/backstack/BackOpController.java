package com.huoqiu.framework.backstack;

public interface BackOpController {
    void push(Op op);

    Op pop();

    Op peek();

    Op pop(String tag);
}
