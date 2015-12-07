package com.sn.controlers.slidingtab.blocktab;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;

import com.sn.controlers.slidingtab.SNSlidingTab;
import com.sn.main.SNElement;

/**
 * Created by xuhui on 15/8/13.
 */
public class SNBlockTab extends SNSlidingTab {
    public SNBlockTab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void resetItem() {
        if (this.getItemBox().$itemList != null && this.getItemBox().$itemList.size() > 0) {
            for (SNElement item : this.getItemBox().$itemList) {
                SNBlockTabItem _item = item.toView(SNBlockTabItem.class);
                _item.setTextColor(_item.getTextColor());

            }
        }
    }

    @Override
    public void onPage(int _page, SNElement _item, Fragment _content) {
        super.onPage(_page, _item, _content);
        resetItem();
        SNBlockTabItem item = _item.toView(SNBlockTabItem.class);
        item.setTextColor(item.getSelectedColor());

    }
}
