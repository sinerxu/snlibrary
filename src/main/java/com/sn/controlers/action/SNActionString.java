package com.sn.controlers.action;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.sn.controlers.SNLinearLayout;
import com.sn.lib.R;
import com.sn.main.SNElement;


/**
 * Created by xuhui on 15/11/22.
 */
public class SNActionString extends SNLinearLayout {
    SNElement ivActionImage;
    SNElement tvTitle;
    SNElement tvDescription;
    SNElement viewSplit;

    SNElement $main;
    int image;
    String title;
    String description;

    public SNActionString(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = $.obtainStyledAttr(attrs, R.styleable.SNActionString);
        image = ta.getResourceId(R.styleable.SNActionString_android_src, 0);
        title = ta.getString(R.styleable.SNActionString_android_text);
        description = ta.getString(R.styleable.SNActionString_description);
        ta.recycle();
        initView();
    }

    public SNActionString(Context context) {
        super(context);
        initView();
    }

    void initView() {
        if ($main == null) {
            $main = $.layoutInflateResId(R.layout.controler_action_string, $this.toView(ViewGroup.class));
            ivActionImage = $main.create(R.id.ivActionImage);
            tvTitle = $main.create(R.id.tvTitle);
            tvDescription = $main.create(R.id.tvDescription);
            viewSplit = $main.create(R.id.viewSplit);
            setImage(image);
            if ($.util.strIsNotNullOrEmpty(title))
                setTitle(title);
            if ($.util.strIsNotNullOrEmpty(description))
                setDescription(description);
            $this.clickable(true);
            $this.background(R.drawable.action_item_default_selected);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void hideSplit() {
        viewSplit.visible($.SN_UI_NONE);
    }

    public void setImage(int resid) {
        this.image = resid;
        ivActionImage.image(image);
    }

    public void setTitle(String title) {
        this.title = title;
        tvTitle.text(title);
    }

    public void setDescription(String description) {
        this.description = description;
        tvDescription.text(description);
    }

}
