package com.sn.sdk.interfaces;

/**
 * 分享监听
 * Created by xuhui on 15/8/5.
 */
public interface SNShareListener {
    /**
     * 分享回调
     * @param state 1成功 0取消 <0失败
     */
    public void onCallback(int state);
}
