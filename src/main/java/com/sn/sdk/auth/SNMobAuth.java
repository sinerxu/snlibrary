package com.sn.sdk.auth;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.sn.sdk.interfaces.SNAuthListener;
import com.sn.sdk.models.SNAuthResult;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by xuhui on 15/8/7.
 */
public class SNMobAuth extends SNAuth implements PlatformActionListener {
    static final int HANDLE_ACTION_COMPLETE = 1;
    static final int HANDLE_ACTION_ERROR = -1;
    static final int HANDLE_ACTION_CANCEL = 0;
    static SNMobAuth mobAuth;

    static class MobAuthHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLE_ACTION_COMPLETE:
                    if (_onAuth != null) {
                        SNAuthResult _result = toAuthResult((Platform) msg.obj);
                        if (_result != null)
                            _onAuth.onAuthResult(AUTH_RESULT_SUCCESS, _result);
                        else
                            _onAuth.onAuthResult(AUTH_RESULT_ERROR, _result);
                        _onAuth = null;
                    }
                    break;
                case HANDLE_ACTION_CANCEL:
                    if (_onAuth != null) {
                        _onAuth.onAuthResult(AUTH_RESULT_CANCEL, null);
                        _onAuth = null;
                    }
                    break;
                default:
                    if (_onAuth != null) {
                        _onAuth.onAuthResult(AUTH_RESULT_ERROR, null);
                        _onAuth = null;
                    }
                    break;
            }
        }
    }

    MobAuthHandler resultHandler = new MobAuthHandler();

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
        plat.SSOSetting(false);
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
     */
    @Override
    public SNAuthResult getUserInfo(String authType) {
        super.getUserInfo(authType);
        Platform plat = ShareSDK.getPlatform(authType);
        return toAuthResult(plat);
    }

    static SNAuthResult toAuthResult(Platform platform) {
        if (platform != null && platform.isAuthValid()) {
            PlatformDb platDB = platform.getDb();
            SNAuthResult _result = new SNAuthResult();
            _result.setUid(platDB.getUserId());
            _result.setToken(platDB.getToken());
            _result.setUnionid(platDB.getUserId());
            _result.setIcon(platDB.getUserIcon());
            _result.setName(platDB.getUserName());
            _result.setAuthType(currentAuthType);
            return _result;
        } else {
            return null;
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Message message = new Message();
        message.what = HANDLE_ACTION_COMPLETE;
        message.obj = platform;
        resultHandler.sendMessage(message);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        resultHandler.sendEmptyMessage(HANDLE_ACTION_ERROR);

    }

    @Override
    public void onCancel(Platform platform, int i) {
        resultHandler.sendEmptyMessage(HANDLE_ACTION_CANCEL);

    }

    public boolean isWXAppInstalled() {
        Platform plat = ShareSDK.getPlatform(TYPE_AUTH_WECHAT);
        return plat.isClientValid();
    }
}
