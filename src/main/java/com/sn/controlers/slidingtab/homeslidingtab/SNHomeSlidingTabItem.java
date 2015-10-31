package com.sn.controlers.slidingtab.homeslidingtab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.sn.controlers.slidingtab.SNSlidingTabItem;
import com.sn.lib.R;
import com.sn.util.SNUtility;

/**
 * Created by xuhui on 15/8/12.
 */
public class SNHomeSlidingTabItem extends SNSlidingTabItem {

    String text;
    Drawable icon;
    boolean rightSplit;

    public SNHomeSlidingTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        $this = $.layoutInflateResId(R.layout.controler_home_slidetabitem, (ViewGroup) $this.toView());
        TypedArray a = $.loadStyle(attrs, R.styleable.SNHomeSlidingTabItem);
        text = a.getString(R.styleable.SNHomeSlidingTabItem_android_text);
        icon = a.getDrawable(R.styleable.SNHomeSlidingTabItem_android_src);
        rightSplit = a.getBoolean(R.styleable.SNHomeSlidingTabItem_right_split, true);
        a.recycle();
        if (!SNUtility.isNullOrEmpty(text))
            $this.id(R.id.text).text(text);
        if (icon != null)
            $this.id(R.id.icon).image(icon);
        setRightSplitVisible(rightSplit);
    }

    public void setRightSplitVisible(boolean isShow) {
        rightSplit = isShow;
        $this.id(R.id.rightSlipt).visible(rightSplit ? $.SN_UI_VISIBLE : $.SN_UI_NONE);
    }
}
