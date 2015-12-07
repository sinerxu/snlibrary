package com.sn.models;

import com.sn.main.SNManager;

/**
 * Created by xuhui on 15/11/26.
 */
public class SNIOCInject {
    public SNManager $;


    boolean alreadyInject;
    public SNIOCInject(SNManager _$) {
        this.$ = _$;
        if (!alreadyInject)
        {
            alreadyInject=true;
            $.injectIOC(this);
        }
    }

}
