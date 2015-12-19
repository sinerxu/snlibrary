package com.sn.controlers.slidingtab.homebottomtab;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;

import com.sn.controlers.slidingtab.SNSlidingTab;
import com.sn.main.SNElement;

/**
 * @style style="?attr/theme_home_bottomtab_style"    需要添加的的样式
 * <p/>
 * Created by xuhui on 15/8/13.
 */
public class SNHomeBottomTab extends SNSlidingTab {
    public SNHomeBottomTab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void resetItem() {
        if (this.getItemBox().$itemList != null && this.getItemBox().$itemList.size() > 0) {
            for (SNElement item : this.getItemBox().$itemList) {
                SNHomeBottomTabItem _item = item.toView(SNHomeBottomTabItem.class);
                _item.setTextColor(_item.getTextColor());
                _item.setSrc(_item.getSrc());
            }
        }
    }

    @Override
    public void onPage(int _page, SNElement _item, Fragment _content) {
        resetItem();
        SNHomeBottomTabItem item = _item.toView(SNHomeBottomTabItem.class);
        item.setTextColor(item.getSelectedColor());
        item.setSrc(item.getSelectedSrc());
        super.onPage(_page, _item, _content);
    }
}
