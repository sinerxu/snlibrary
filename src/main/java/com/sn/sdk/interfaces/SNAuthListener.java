package com.sn.sdk.interfaces;

import com.sn.sdk.models.SNAuthResult;

/**
 * 授权返回信息
 * Created by xuhui on 15/8/7.
 */

public interface SNAuthListener {
    /**
     * state -1(网络错误) 0(用户取消授权) 1(授权成功)
     * @param state
     * @param authResult
     */
    public void onAuthResult(int state, SNAuthResult authResult);
}
