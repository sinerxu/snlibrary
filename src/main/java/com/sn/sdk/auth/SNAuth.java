package com.sn.sdk.auth;

import android.content.Context;

import com.sn.sdk.interfaces.SNAuthInterface;
import com.sn.sdk.interfaces.SNAuthListener;

/**
 * Created by xuhui on 15/8/7.
 */
public class SNAuth implements SNAuthInterface {
    SNAuthListener _onAuth;
    public static final String TYPE_AUTH_WEIBO = "SinaWeibo";
    public static final String TYPE_AUTH_QQ = "QQ";
    public static final String TYPE_AUTH_WECHAT = "Wechat";
    public String currentAuthType = TYPE_AUTH_WEIBO;
    Context context;
    /**
     * Mob
     */
    public static final int SDKAUTH_TYPE_MOB = 0;


    public static SNAuth instance(Context _context, int authSDKType) {
        SNAuth _result = null;
        switch (authSDKType) {
            case SDKAUTH_TYPE_MOB:
                _result = SNMobAuth.instance(_context);
                break;
            default:
                _result = SNMobAuth.instance(_context);
                break;
        }
        return _result;
    }


    SNAuth(Context _context) {
        context = _context;
    }

    @Override
    public void auth(String authType, SNAuthListener onAuth) {
        _onAuth = onAuth;
    }

    @Override
    public void cancelAuth(String authType) {

    }

    @Override
    public void getUserInfo(String authType, SNAuthListener onAuth) {
        _onAuth = onAuth;
    }
}
