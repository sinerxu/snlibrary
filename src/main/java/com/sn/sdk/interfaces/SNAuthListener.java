package com.sn.sdk.interfaces;

import com.sn.sdk.models.SNAuthResult;

/**
 * 授权返回信息
 * Created by xuhui on 15/8/7.
 */
public interface SNAuthListener {
    public void onAuthResult(int state, SNAuthResult authResult);
}
