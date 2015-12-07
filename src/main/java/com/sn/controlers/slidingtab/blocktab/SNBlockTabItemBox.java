package com.sn.controlers.slidingtab.blocktab;

import android.content.Context;
import android.util.AttributeSet;

import com.sn.controlers.slidingtab.SNSlidingTabItemBox;
import com.sn.controlers.slidingtab.homeslidingtab.SNHomeSlidingTabItem;
import com.sn.lib.R;
import com.sn.main.SNElement;

/**
 * @style style="?attr/theme_home_slidingtabitembox_style"  需要添加的的样式
 * Created by xuhui on 15/8/12.
 */
public class SNBlockTabItemBox extends SNSlidingTabItemBox {
    public SNBlockTabItemBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onItemLoadFinish() {
        super.onItemLoadFinish();
        if ($itemList != null && $itemList.size() > 0) {
            for (int i = 0; i < $itemList.size(); i++) {
                SNBlockTabItem item = $itemList.get(i).toView(SNBlockTabItem.class);
                if (i == 0)
                    item.setBorderStyle(R.drawable.block_tab_item_left);
                else if (i == $itemList.size() - 1)
                    item.setBorderStyle(R.drawable.block_tab_item_right);
                else item.setBorderStyle(R.drawable.block_tab_item_normal);
            }

        }
    }

}
