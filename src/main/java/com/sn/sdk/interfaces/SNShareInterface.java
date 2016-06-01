package com.sn.sdk.interfaces;

/**
 * Created by xuhui on 15/8/3.
 */
public interface SNShareInterface {
    void setShareListener(SNShareListener listener);

    void shareImageArray(String title, String content, String url, String[] imageArray, SNShareListener shareListener);

    void shareImagePath(String title, String content, String url, String imagePath, SNShareListener shareListener);

    void share(String title, String content, String url, String imageUrl, SNShareListener shareListener);
}
