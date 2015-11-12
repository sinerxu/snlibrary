/**
 * 
 */
/**
 * @author xuhui
 *
 */
package com.sn.interfaces;

import com.sn.main.SNElement;
import com.sn.models.SNAdapterViewInject;

public interface SNAdapterListener {
	 void onInject(SNAdapterViewInject inject);
	 SNAdapterViewInject onCreateInject(int pos);
}