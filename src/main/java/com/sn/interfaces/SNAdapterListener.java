/**
 * 
 */
/**
 * @author xuhui
 *
 */
package com.sn.interfaces;

import com.sn.main.SNElement;
import com.sn.models.SNViewHolder;

public interface SNAdapterListener {
	public SNElement onFillViewHolder(SNViewHolder viewHolder);
	public SNViewHolder onCreateViewHolder(int pos);
}