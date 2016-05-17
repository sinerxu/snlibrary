package com.sn.controlers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.sn.interfaces.SNOnClickListener;
import com.sn.lib.R;
import com.sn.main.SNConfig;
import com.sn.main.SNElement;
import com.sn.main.SNManager;

public class SNNavTitleBar extends ViewGroup {
    static final String LCAT = "SNNavTitleBar Log";
    SNManager $;
    SNElement $this;
    SNElement $navTitleBarBox;
    SNElement $leftButton;
    SNElement $leftButtonIcon;
    SNElement $rightButton;
    SNElement $rightButtonIcon;
    SNElement $rightButtonText;
    SNElement $title;
    SNElement $logo;
    SNElement $leftBox;
    SNElement $rightBox;
    String title;
    Drawable logo;


    public SNNavTitleBar(Context context) {
        this(context, null, 0);
    }

    public SNNavTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SNNavTitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        $ = new SNManager(context);
        $this = $.create(this);
        $navTitleBarBox = $.layoutInflateName("navtitlebar");
        $this.add($navTitleBarBox);
        $leftButton = $navTitleBarBox.find(R.id.leftButton);
        $leftButtonIcon = $navTitleBarBox.find(R.id.leftButtonIcon);
        $rightButton = $navTitleBarBox.find(R.id.rightButton);
        $rightButtonIcon = $navTitleBarBox.find(R.id.rightButtonIcon);
        $rightButtonText = $navTitleBarBox.find(R.id.rightButtonText);
        $title = $navTitleBarBox.find(R.id.title);
        $logo = $navTitleBarBox.find(R.id.logo);
        $leftBox = $navTitleBarBox.find(R.id.leftBox);
        $rightBox = $navTitleBarBox.find(R.id.rightBox);
        TypedArray a = $.obtainStyledAttr(attrs, R.styleable.SNNavTitleBar);
        title = a.getString(R.styleable.SNNavTitleBar_nav_title);
        updateTitle();
        logo = a.getDrawable(R.styleable.SNNavTitleBar_nav_logo);
        updateLogo();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View contentView = getChildAt(0);
        contentView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
        // TODO Auto-generated method stub
        $navTitleBarBox.layout(0, 0, $this.width(), $this.height());
    }


    //region 左侧按钮

    /**
     * 显示返回按钮
     *
     * @param onClickListener
     */
    public void showBack(SNOnClickListener onClickListener) {
        showLeftButton(R.drawable.nav_bar_back, onClickListener);
    }

    /**
     * 显示菜单按钮
     *
     * @param onClickListener
     */
    public void showMenu(SNOnClickListener onClickListener) {
        showLeftButton(R.drawable.nav_bar_menu, onClickListener);
    }

    /**
     * 显示左侧按钮
     *
     * @param imgResId        图片id
     * @param onClickListener 事件
     */
    public void showLeftButton(int imgResId, SNOnClickListener onClickListener) {
        showLeftButton();
        $leftButtonIcon.image(imgResId);
        $leftButton.click(onClickListener);
    }

    /**
     * 设置左侧按钮的background
     *
     * @param resId resId
     */
    public void setLeftButtonBakcground(int resId) {
        this.$leftButton.background(resId);
    }

    /**
     * 设置左侧按钮的background
     *
     * @param drawable drawable
     */
    public void setLeftButtonBakcground(Drawable drawable) {
        this.$leftButton.background(drawable);
    }


    //endregion

    //region 右侧按钮

    /**
     * 显示右侧文字按钮
     *
     * @param text
     * @param onClickListener
     */
    public void showRightText(String text, SNOnClickListener onClickListener) {
        showRightButton();
        $rightButtonIcon.visible(SNConfig.SN_UI_NONE);
        $rightButtonText.visible(SNConfig.SN_UI_VISIBLE);
        $rightButtonText.text(text);
        $rightButton.click(onClickListener);
    }

    /**
     * 设置右侧文字的颜色
     *
     * @param colorId
     */
    public void setRightTextColor(int colorId) {
        $rightButtonText.textColorResId(colorId);
    }

    /**
     * 显示右侧图片按钮
     *
     * @param imgResId        img res id
     * @param onClickListener call back
     */
    public void showRightImage(int imgResId, SNOnClickListener onClickListener) {
        showRightButton();
        $rightButtonIcon.visible(SNConfig.SN_UI_VISIBLE);
        $rightButtonText.visible(SNConfig.SN_UI_NONE);
        $rightButtonIcon.image(imgResId);
        $rightButton.click(onClickListener);
    }

    /**
     * 显示发布文章按钮
     *
     * @param onClickListener call back
     */
    public void showRightWriteImage(SNOnClickListener onClickListener) {
        showRightImage(R.drawable.nav_bar_write, onClickListener);
    }
    //endregion

    //region 中部内容

    /**
     * 设置标题
     *
     * @param _title title
     */
    public void setTitle(String _title) {
        this.title = _title;
        updateTitle();
    }

    /**
     * 设置标题的颜色
     *
     * @param resId resId
     */
    public void setTitleColor(int resId) {
        $title.textColorResId(resId);
    }

    /**
     * 设置中间图片
     *
     * @param resId
     */
    public void setLogo(int resId) {
        setLogo($.drawableResId(resId));
    }

    /**
     * 设置中间图片
     *
     * @param drawable drawable
     */
    public void setLogo(Drawable drawable) {
        this.logo = drawable;
        updateLogo();
    }
    //endregion

    //region private
    void showLeftButton() {
        $leftButton.visible(SNConfig.SN_UI_VISIBLE);
    }

    void showRightButton() {
        $rightButton.visible(SNConfig.SN_UI_VISIBLE);
    }

    void updateTitle() {
        if (!$.util.strIsNullOrEmpty(title)) {
            $title.text(title);
            $title.visible(SNManager.SN_UI_VISIBLE);
            $logo.visible(SNManager.SN_UI_NONE);
        } else {
            $title.visible(SNManager.SN_UI_NONE);
            $logo.visible(SNManager.SN_UI_NONE);
        }
    }

    void updateLogo() {
        if (logo != null) {
            $logo.image(logo);
            $title.visible(SNManager.SN_UI_NONE);
            $logo.visible(SNManager.SN_UI_VISIBLE);
        } else {
            $logo.visible(SNManager.SN_UI_NONE);
            $title.visible(SNManager.SN_UI_NONE);
        }
    }

    public void setLeftView(SNElement element) {
        $leftBox.removeAllChild();
        $leftBox.add(element);
    }

    public void setRightView(SNElement element) {
        $rightBox.removeAllChild();
        $rightBox.add(element);
    }
    //endregion
}
