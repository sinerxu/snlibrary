package com.sn.models;

import android.widget.AdapterView;

import com.sn.main.SNElement;
import com.sn.override.SNAdapter;

public class SNAdapterViewInject extends SNInject {


    SNAdapter adapter;

    int pos;
    AdapterView<?> parent;
    SNElement view;


    SNElement viewGroup;
    Object data;


    public SNAdapterViewInject(SNElement _v) {
        this.view = _v;
        this.view.inject(this);
    }


    public AdapterView<?> getParent() {
        return parent;
    }

    public void setParent(AdapterView<?> parent) {
        this.parent = parent;
    }

    public SNElement getView() {
        return view;
    }

    public void setView(SNElement view) {
        this.view = view;
    }

    public SNElement getViewGroup() {
        return viewGroup;
    }

    public void setViewGroup(SNElement viewGroup) {
        this.viewGroup = viewGroup;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public <E> E getData(Class<E> _class) {
        if (data != null)
            return (E) data;
        else return null;
    }

    public SNAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(SNAdapter adapter) {
        this.adapter = adapter;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void notifyUpdateUI() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

}
