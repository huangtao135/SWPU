package com.ht.communi.model.impl;

/**
 * Created by Administrator on 2018/5/7.
 */

public interface EventModelImpl {
    interface BaseListener<T> {
        void getSuccess(T t);

        void getFailure();
    }
}
