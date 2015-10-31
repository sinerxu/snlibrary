package com.sn.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xuhui on 15/8/23.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface SNBindId {
    int id();
}
