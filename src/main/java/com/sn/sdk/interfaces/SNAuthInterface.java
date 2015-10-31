package com.sn.sdk.interfaces;

/**
 * Created by xuhui on 15/8/7.
 */
public interface SNAuthInterface {
    public void auth(String authType, SNAuthListener onAuth);

    public void cancelAuth(String authType);

    public void getUserInfo(String authType, SNAuthListener onAuth);
}
