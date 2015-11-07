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
	 SNElement onFillViewHolder(SNViewHolder viewHolder);
	 SNViewHolder onCreateViewHolder(int pos);
}