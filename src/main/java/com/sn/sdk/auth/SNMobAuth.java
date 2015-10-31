package com.sn.sdk.auth;

import android.content.Context;

import com.sn.sdk.interfaces.SNAuthListener;
import com.sn.sdk.models.SNAuthResult;
import com.sn.sdk.sharesdk.SNShare;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by xuhui on 15/8/7.
 */
public class SNMobAuth extends SNAuth implements PlatformActionListener {

    static SNMobAuth mobAuth;


    public static SNMobAuth instance(Context _context) {
        SNMobAuth _result = null;
        if (mobAuth == null) {
            ShareSDK.initSDK(_context);
            mobAuth = new SNMobAuth(_context);
        }

        _result = mobAuth;
        return _result;
    }


    public SNMobAuth(Context _context) {
        super(_context);
    }

    @Override
    public void auth(String authType, SNAuthListener onAuth) {
        super.auth(authType, onAuth);
        currentAuthType = authType;
        Platform plat = ShareSDK.getPlatform(authType);
        plat.SSOSetting(true);
        plat.setPlatformActionListener(this);
        plat.authorize();
    }

    /**
     * 取消授权
     *
     * @param authType
     */
    @Override
    public void cancelAuth(String authType) {
        super.cancelAuth(authType);
        ShareSDK.getPlatform(TYPE_AUTH_QQ).removeAccount(true);
        ShareSDK.getPlatform(TYPE_AUTH_WECHAT).removeAccount(true);
        ShareSDK.getPlatform(TYPE_AUTH_WEIBO).removeAccount(true);
    }


    /**
     * 获取授权信息
     *
     * @param authType
     * @param onAuth
     */
    @Override
    public void getUserInfo(String authType, SNAuthListener onAuth) {
        super.getUserInfo(authType, onAuth);

    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (_onAuth != null) {
            PlatformDb platDB = platform.getDb();
            SNAuthResult _result = new SNAuthResult();
            _result.setUid(platDB.getUserId());
            _result.setToken(platDB.getToken());
            _result.setUnionid(platDB.getUserId());
            _result.setIcon(platDB.getUserIcon());
            _result.setName(platDB.getUserName());
            _result.setAuthType(currentAuthType);
            _onAuth.onAuthResult(1, _result);
            _onAuth = null;
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        if (_onAuth != null) {
            _onAuth.onAuthResult(-1, null);
            _onAuth = null;
        }
    }

    @Override
    public void onCancel(Platform platform, int i) {
        if (_onAuth != null) {
            _onAuth.onAuthResult(0, null);
            _onAuth = null;
        }
    }
}
