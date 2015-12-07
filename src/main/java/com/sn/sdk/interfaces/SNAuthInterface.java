package com.sn.sdk.interfaces;

import com.sn.sdk.models.SNAuthResult;

/**
 * Created by xuhui on 15/8/7.
 */
public interface SNAuthInterface {
     void auth(String authType, SNAuthListener onAuth);
     void cancelAuth(String authType);
     SNAuthResult getUserInfo(String authType);
     boolean isWXAppInstalled();
}
