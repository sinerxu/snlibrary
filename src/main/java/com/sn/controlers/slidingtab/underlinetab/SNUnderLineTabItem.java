package com.sn.controlers.slidingtab.underlinetab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.sn.controlers.slidingtab.SNSlidingTabItem;
import com.sn.lib.R;

/**
 * Created by xuhui on 15/8/12.
 */
public class SNUnderLineTabItem extends SNSlidingTabItem {

    String text;
    Drawable src;
    int textColor;
    Drawable selectedSrc;


    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public Drawable getSelectedSrc() {
        return selectedSrc;
    }

    public void setSelectedSrc(Drawable selectedSrc) {
        this.selectedSrc = selectedSrc;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        if (textColor != selectedColor)
            this.textColor = textColor;

        $this.id(R.id.text).textColor(textColor);
    }

    public Drawable getSrc() {
        return src;
    }

    public void setSrc(Drawable src) {
        if (src != null) {
            if (src != selectedSrc)
                this.src = src;
            $this.id(R.id.image).image(src);
            $this.id(R.id.image).visible($.SN_UI_VISIBLE);
        } else {
            $this.id(R.id.image).visible($.SN_UI_NONE);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        $this.id(R.id.text).text(text);
    }

    int selectedColor;


    public SNUnderLineTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        $this = $.layoutInflateResId(R.layout.controler_underline_slidetabitem, (ViewGroup) $this.toView());
        TypedArray array = $.obtainStyledAttr(attrs, R.styleable.SNImageTextTabItem);
        text = array.getString(R.styleable.SNImageTextTabItem_android_text);
        src = array.getDrawable(R.styleable.SNImageTextTabItem_android_src);
        textColor = array.getColor(R.styleable.SNImageTextTabItem_android_textColor, 0xFF000000);
        selectedSrc = array.getDrawable(R.styleable.SNImageTextTabItem_selected_src);
        selectedColor = array.getColor(R.styleable.SNImageTextTabItem_selected_color, 0xFF555555);

        array.recycle();

        if (!$.util.strIsNullOrEmpty(text))
            setText(text);
        if (src != null)
            setSrc(src);
        setTextColor(textColor);
    }


}
