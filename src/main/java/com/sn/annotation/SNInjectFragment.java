
package com.sn.annotation;

import com.sn.models.SNInject;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xuhui on 15/8/23.
 *
 * @SNInjectFragment(injectClass=.class,injectView=R.layout.view)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SNInjectFragment {

    int injectView();

    Class injectClass() default SNInject.class;

    boolean animated() default false;
}
