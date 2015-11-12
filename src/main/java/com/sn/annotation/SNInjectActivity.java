
package com.sn.annotation;

import com.sn.models.SNViewInject;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xuhui on 15/8/23.
 *
 * @SNInjectActivity(name="")
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SNInjectActivity {

    int injectView();

    Class injectClass() default SNViewInject.class;
}
