package com.sn.sdk.sharesdk;

import android.app.Activity;
import android.content.Context;

import com.sn.sdk.interfaces.SNShareInterface;
import com.sn.sdk.interfaces.SNShareListener;

/**
 * Created by xuhui on 15/8/3.
 */
public class SNShare implements SNShareInterface {


    Context context;

    SNShareListener _shareListener;
    public static final int SDKTYPE_MOB = 0;

    public static final int SHARE_RESULT_SUCCESS = 1;
    public static final int SHARE_RESULT_CANCEL = 0;
    public static final int SHARE_RESULT_ERROR = -1;

    /**
     * 创建分享对象
     *
     * @param _context
     * @param _shareSDKType
     * @return
     */
    public static SNShare instance(Context _context, int _shareSDKType) {
        SNShare _result = null;
        switch (_shareSDKType) {
            case SDKTYPE_MOB:
                _result = SNMobShare.instance(_context, _shareSDKType);
                break;
            default:
                _result = SNMobShare.instance(_context, _shareSDKType);
                break;
        }
        return _result;
    }


    SNShare(Context _context) {

        context = _context;
    }

    @Override
    public void shareImageArray(String title, String content, String url, String[] imageArray, SNShareListener shareListener) {
        this._shareListener = shareListener;
    }

    @Override
    public void share(String title, String content, String url, String imageUrl, SNShareListener shareListener) {
        this._shareListener = shareListener;
    }

    @Override
    public void shareImagePath(String title, String content, String url, String imagePath, SNShareListener shareListener) {
        this._shareListener = shareListener;
    }

    @Override
    public void setShareListener(SNShareListener listener) {
        this._shareListener = listener;
    }
}
