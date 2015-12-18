package com.sn.models;

import com.sn.main.SNManager;

/**
 * Created by xuhui on 15/11/26.
 */
public class SNIOCInject {
    public SNManager $;


    public SNIOCInject(SNManager _$) {
        this.$ = _$;
        $.injectIOC(this);
    }

}
