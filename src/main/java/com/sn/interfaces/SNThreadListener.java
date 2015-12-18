package com.sn.interfaces;

/**
 * Created by xuhui on 15/12/1.
 */
public interface SNThreadListener {
    Object run();
    void onFinish(Object object);
}
