package com.sn.sdk.sharesdk;

import android.app.Activity;
import android.content.Context;

import com.sn.main.SNManager;
import com.sn.sdk.interfaces.SNShareListener;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by xuhui on 15/8/3.
 */
public class SNMobShare extends SNShare implements PlatformActionListener {
    final String LCAT = "SNMobShare Error";
    static boolean isInitShareSDK;
    static SNMobShare snMobShare;
    SNManager $;

    public static SNShare instance(Context _context, int _shareSDKType) {
        SNMobShare _result = null;
        if (snMobShare == null)
            snMobShare = new SNMobShare(_context);
        _result = snMobShare;
        return _result;
    }

    SNMobShare(Context _context) {
        super(_context);
        if (!isInitShareSDK) {
            isInitShareSDK = true;
            //初始化shareSDK
            ShareSDK.initSDK(_context);

            $ = new SNManager(_context);
        }
    }

    @Override
    public void share(String title, String content, String url, String imageUrl, SNShareListener shareListener) {
        super.share(title, content, url, imageUrl, shareListener);
        OnekeyShare oks = new OnekeyShare();
        oks.setTitle(title);
        oks.setText(content);
        oks.setUrl(url);
        oks.setCallback(this);
        if (imageUrl.indexOf("http") > -1) {
            oks.setImageUrl(imageUrl);
        } else {
            imageUrl = $.util.fileCopyFileIntoSDCard(this.context, imageUrl);
            if (!$.util.strIsNullOrEmpty(imageUrl)) {
                oks.setImagePath(imageUrl);
            }
        }
        oks.show(this.context);
    }


    @Override
    public void shareImagePath(String title, String content, String url, String imagePath, SNShareListener shareListener) {
        super.shareImagePath(title, content, url, imagePath, shareListener);
        OnekeyShare oks = new OnekeyShare();
        oks.setTitle(title);
        oks.setText(content);
        oks.setUrl(url);
        oks.setCallback(this);
        oks.setImagePath(imagePath);
        oks.show(this.context);
    }

    @Override
    public void shareImageArray(String title, String content, String url, String[] imageArray, SNShareListener shareListener) {
        super.shareImageArray(title, content, url, imageArray, shareListener);
        OnekeyShare oks = new OnekeyShare();
        oks.setTitle(title);
        oks.setText(content);
        oks.setUrl(url);
        oks.setCallback(this);
        oks.setImageArray(imageArray);
        oks.show(this.context);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (this._shareListener != null)
            this._shareListener.onCallback(SHARE_RESULT_SUCCESS);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        if (this._shareListener != null)
            this._shareListener.onCallback(SHARE_RESULT_ERROR);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        if (this._shareListener != null)
            this._shareListener.onCallback(SHARE_RESULT_CANCEL);
    }


}
