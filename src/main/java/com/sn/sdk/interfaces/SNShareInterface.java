package com.sn.sdk.interfaces;

/**
 * Created by xuhui on 15/8/3.
 */
public interface SNShareInterface {
    void setShareListener(SNShareListener listener);

    void share(String title, String content, String url, String imageUrl, SNShareListener shareListener);
}
