package com.sn.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xuhui on 15/8/23.
 * @SNBindId(id=R.id.listView)
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface SNInjectElement {
    int id();
}
