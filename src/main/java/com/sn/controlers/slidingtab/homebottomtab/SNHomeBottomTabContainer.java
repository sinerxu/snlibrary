package com.sn.controlers.slidingtab.homebottomtab;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;

import com.sn.controlers.slidingtab.SNSlidingTabContainer;

import java.util.ArrayList;

/**
 * @style   style="?attr/theme_home_bottomtabcontent_style"    需要添加的的样式
 * Created by xuhui on 15/8/13.
 */
public class SNHomeBottomTabContainer extends SNSlidingTabContainer {

    public SNHomeBottomTabContainer(Context _context, FragmentManager _manager, ArrayList<Fragment> _list, boolean _isScrollContainer) {
        super(_context, _manager, _list, _isScrollContainer);
    }

    public SNHomeBottomTabContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
