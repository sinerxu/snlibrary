package com.sn.interfaces;

import com.sn.core.SNUtility;

/**
 * Created by xuhui on 15/12/1.
 */
public interface SNTaskListener {

    Object onTask(SNUtility.SNTask task, Object param);

    void onFinish(SNUtility.SNTask task, Object object);
}
