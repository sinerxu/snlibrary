package com.sn.controlers.slidingtab.blocktab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.sn.controlers.slidingtab.SNSlidingTabItem;
import com.sn.lib.R;
import com.sn.main.SNElement;

/**
 * Created by xuhui on 15/8/12.
 */
public class SNBlockTabItem extends SNSlidingTabItem {

    String text;

    int textColor;


    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }


    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        if (textColor != selectedColor)
            this.textColor = textColor;

        $this.id(R.id.text).textColor(textColor);
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        $this.id(R.id.text).text(text);
    }

    int selectedColor;
    SNElement $itemBox;

    public SNBlockTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        $itemBox = $.layoutInflateResId(R.layout.controler_block_slidetabitem, (ViewGroup) $this.toView(), false);
        $this.add($itemBox);
        TypedArray array = $.obtainStyledAttr(attrs, R.styleable.SNImageTextTabItem);
        text = array.getString(R.styleable.SNImageTextTabItem_android_text);
        textColor = array.getColor(R.styleable.SNImageTextTabItem_android_textColor, 0xFF000000);
        selectedColor = array.getColor(R.styleable.SNImageTextTabItem_selected_color, 0xFF555555);
        array.recycle();
        if (!$.util.strIsNullOrEmpty(text))
            setText(text);
        setTextColor(textColor);
    }

    public void setBorderStyle(int redId) {
        $itemBox.background(redId);
        //$this.id(R.id.rightHr).visible(isShow ? $.SN_UI_VISIBLE : $.SN_UI_NONE);
    }
}
