package com.sn.controlers.action;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.sn.controlers.SNLinearLayout;
import com.sn.lib.R;
import com.sn.main.SNElement;

import java.util.ArrayList;

/**
 * Created by xuhui on 15/11/22.
 */
public class SNActionBox extends SNLinearLayout {
    ArrayList<SNElement> actions;
    SNElement $main;
    SNElement $actionBox;

    public SNActionBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SNActionBox(Context context) {
        super(context);
    }

    void initView() {
        if (actions == null) {
            actions = new ArrayList<SNElement>();
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View item = getChildAt(i);
                if (item instanceof SNActionString)
                    actions.add($.create(item));
                else
                    throw new IllegalStateException("Child view must be ActionItem.");
            }
            //移除所有的view
            removeAllViews();
            $main = $.layoutInflateResId(R.layout.controler_action_box, $this.toViewGroup());
            $actionBox = $main.find(R.id.actionsBox);
            int index = 0;
            for (SNElement element : actions) {
                if (index == actions.size() - 1)
                    element.toView(SNActionString.class).hideSplit();
                $actionBox.add(element);
                index++;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        initView();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
