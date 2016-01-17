/**
 * @author Siner QQ348078707
 */
package com.sn.override;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sn.interfaces.SNAdapterListener;
import com.sn.main.SNElement;
import com.sn.main.SNManager;
import com.sn.models.SNAdapterViewInject;

public class SNAdapter extends BaseAdapter {
    public SNManager $;
    public SNAdapterListener onLoadView;
    public Context context;
    public LayoutInflater layoutInflater;
    private ArrayList<Object> list;
    private SNElement master;

    public SNElement getMaster() {
        return master;
    }

    public SNAdapter(SNElement master, ArrayList<Object> list, Context context) {
        $ = SNManager.instence(context);
        this.context = context;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.master = master;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return list.get(pos);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        $.util.logDebug(SNAdapter.class, "position=" + pos);
        // TODO Auto-generated method stub
        SNAdapterViewInject inject = null;
        if (view == null) {
            inject = onLoadView.onCreateInject(pos);
            view = inject.getView().toView();
            view.setTag(inject);
        } else {
            inject = (SNAdapterViewInject) view.getTag();
        }

        if (inject == null) {
            inject = new SNAdapterViewInject($.create(view));
        }
        inject.setPos(pos);
        inject.setViewGroup($.create(viewGroup));
        inject.setData(list.get(pos));
        //onLoadView.onInject(inject);
        view = inject.getView().toView();
        inject.setAdapter(this);
        inject.onInjectUI();
        inject.onInjectEvent();
        return view;
    }

    public ArrayList getDatas() {
        return list;
    }


    public void removeItem(int pos) {
        list.remove(pos);
        notifyDataSetChanged();
    }

    public void addItem(Object object) {
        list.add(object);
        notifyDataSetChanged();
    }
}
