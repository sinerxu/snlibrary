package com.sn.util;

import com.sn.models.SNBindInject;

import java.util.HashMap;

/**
 * Created by xuhui on 15/10/31.
 */
public class SNBindInjectManager {
     static HashMap<String, SNBindInject> bindInjects;
     static SNBindInjectManager manager;

    public static SNBindInjectManager instance() {
        if (manager == null)
            manager = new SNBindInjectManager();
        return manager;
    }

    /**
     * bind ioc
     *
     * @param bind
     * @param to
     */
    public void bind(Class bind, Class to) {
        if (SNUtility.instance().refClassAllowConvert(bind, to)) {
            if (bindInjects == null) {
                bindInjects = new HashMap<String, SNBindInject>();
            }
            SNBindInject bindInject = new SNBindInject();
            bindInject.bind = bind;
            bindInject.to = to;
            bindInjects.put(bind.getCanonicalName(), bindInject);
        } else
            throw new IllegalStateException("Cannot cast 'bind calss' cast to 'to class'.");
    }

    /**
     * get inject class by bind class
     *
     * @param bind
     * @return
     */
    public Class to(Class bind) {
        if (bindInjects != null) {
            String key=bind.getCanonicalName();
            if (bindInjects.containsKey(key))
            {
                SNBindInject item = bindInjects.get(key);
                return item.to;
            }
        }
        return null;
    }
}
