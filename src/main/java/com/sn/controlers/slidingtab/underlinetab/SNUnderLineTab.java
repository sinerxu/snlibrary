package com.sn.controlers.slidingtab.underlinetab;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;

import com.sn.controlers.slidingtab.SNSlidingTab;
import com.sn.controlers.slidingtab.homebottomtab.SNHomeBottomTabItem;
import com.sn.main.SNElement;

/**
 * Created by xuhui on 15/8/13.
 */
public class SNUnderLineTab extends SNSlidingTab {
    public SNUnderLineTab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void resetItem() {
        if (this.getItemBox().$itemList != null && this.getItemBox().$itemList.size() > 0) {
            for (SNElement item : this.getItemBox().$itemList) {
                SNUnderLineTabItem _item = item.toView(SNUnderLineTabItem.class);
                _item.setTextColor(_item.getTextColor());
                _item.setSrc(_item.getSrc());
            }
        }
    }

    @Override
    public void onPage(int _page, SNElement _item, Fragment _content) {
        super.onPage(_page, _item, _content);
        resetItem();
        SNUnderLineTabItem item = _item.toView(SNUnderLineTabItem.class);
        item.setTextColor(item.getSelectedColor());
        item.setSrc(item.getSelectedSrc());
    }
}
