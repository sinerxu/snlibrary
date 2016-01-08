package com.sn.interfaces;

/**
 * Created by xuhui on 15/12/1.
 */
public interface SNTaskListener {

    Object onTask(Object param);

    void onFinish(Object object);
}
