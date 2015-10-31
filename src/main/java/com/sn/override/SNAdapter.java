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
import com.sn.main.SNManager;
import com.sn.models.SNViewHolder;

public class SNAdapter extends BaseAdapter {
	public SNManager $;
	public SNAdapterListener onLoadView;
	public Context context;
	public LayoutInflater layoutInflater;
	private ArrayList<Object> list;

	public SNAdapter(ArrayList<Object> list, Context context) {
		$ = SNManager.instence(context);
		this.context = context;
		this.list = list;
		this.layoutInflater = LayoutInflater.from(context);
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
		// TODO Auto-generated method stub
		SNViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = onLoadView.onViewBind(pos);
			view = viewHolder.view.toView();
			view.setTag(viewHolder);
		} else {
			viewHolder = (SNViewHolder) view.getTag();
		}
		if (onLoadView != null) {
			if (viewHolder == null) {
				viewHolder = new SNViewHolder($.create(view));
			}
			viewHolder.pos = pos;
			viewHolder.viewGroup = $.create(viewGroup);
			view = onLoadView.onDataBind(viewHolder).toView();
		}
		return view;
	}
}
