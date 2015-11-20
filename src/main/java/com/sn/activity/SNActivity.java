
/**
 * @author xuhui
 */
package com.sn.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


import com.sn.annotation.SNInjectActivity;
import com.sn.main.SNManager;
import com.sn.models.SNActivityInject;
import com.sn.models.SNInject;

import java.lang.annotation.Annotation;

public class SNActivity extends FragmentActivity {


    SNActivityInject inject;

    public <T> T getInject(Class<T> _class) {
        return (T) inject;
    }

    public SNActivityInject getInject() {
        return inject;
    }

    public SNManager $;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        $ = SNManager.instence(SNActivity.this);
    }

    public void injectActivity() {
        try {
            for (Annotation item : this.getClass().getDeclaredAnnotations()) {
                if (item instanceof SNInjectActivity) {
                    SNInjectActivity injectActivity = (SNInjectActivity) item;
                    int lid = injectActivity.injectView();
                    Class c = injectActivity.injectClass();
                    inject = (SNActivityInject) c.newInstance();
                    $.contentView(lid, inject);
                }
            }
        } catch (IllegalAccessException e) {
            inject = null;
        } catch (InstantiationException e) {
            inject = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}