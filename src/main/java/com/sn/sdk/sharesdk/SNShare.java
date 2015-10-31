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
    Activity activity;
    SNShareListener _shareListener;
    public static final int SDKTYPE_MOB = 0;


    /**
     * 创建分享对象
     *
     * @param _activity
     * @param _shareSDKType
     * @return
     */
    public static SNShare instance(Activity _activity, int _shareSDKType) {
        SNShare _result = null;
        switch (_shareSDKType) {
            case SDKTYPE_MOB:
                _result = SNMobShare.instance(_activity, _shareSDKType);
                break;
            default:
                _result = SNMobShare.instance(_activity, _shareSDKType);
                break;
        }
        return _result;
    }


    SNShare(Activity _activity) {
        activity = _activity;
        context = _activity.getBaseContext();
    }

    @Override
    public void share(String title, String content, String url, String imageUrl, SNShareListener shareListener) {

    }
}
