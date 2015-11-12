package com.sn.models;

import android.widget.AdapterView;

import com.sn.main.SNElement;

public class SNAdapterViewInject extends SNViewInject {
    public int pos;
    public AdapterView<?> parent;
    public SNElement view;
    public SNElement viewGroup;
    public long id;
    public Object data;

    public SNAdapterViewInject(SNElement _v) {
        this.view = _v;
    }

    public <E> E getData(Class<E> _class) {
        if (data != null)
            return (E) data;
        else return null;
    }

}
