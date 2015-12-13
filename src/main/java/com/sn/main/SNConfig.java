package com.sn.main;

import android.view.View;
import android.view.ViewGroup;

import com.sn.postting.alert.SNAlert;

public class SNConfig {

    public static final String SHAREDPREFERENCES_KEY="SHAREDPREFERENCES_KEY";

    public static final String SN_VERSION = "1.0.0";


    //region activity start or finish animate type
    /**
     * 无动画
     */
    public static final int SN_ANIMATE_ACTIVITY_NO = 0;
    /**
     * 默认动画
     */
    public static final int SN_ANIMATE_ACTIVITY_YES = 1;
    public static final int SN_ANIMATE_ACTIVITY_SN = 2;
    public static final int SN_ANIMATE_ACTIVITY_FADE = 3;
    public static final int SN_ANIMATE_ACTIVITY_SLIDE = 4;
    public static final int SN_ANIMATE_ACTIVITY_SCALE = 5;
    public static final int SN_ANIMATE_ACTIVITY_SCALE_ROTATE = 6;
    public static final int SN_ANIMATE_ACTIVITY_SCALE_TRANSLATE = 7;
    public static final int SN_ANIMATE_ACTIVITY_PUSH_POP_HORIZONTAL = 8;
    public static final int SN_ANIMATE_ACTIVITY_PUSH_POP_VERTICAL = 9;
    public static final int SN_ANIMATE_ACTIVITY_ZOOM = 10;
    public static final int SN_ANIMATE_ACTIVITY_TEST = 1000;
    //endregion


    //region alert style
    public static final int SN_UI_ALERT_STYLE = SNAlert.ALERT_TYPE_IOS;
    //endregion

    //region visibility value
    public static final int SN_UI_NONE = View.GONE;
    public static final int SN_UI_VISIBLE = View.VISIBLE;
    public static final int SN_UI_INVISIBLE = View.INVISIBLE;
    //endregion

    //region size
    public static final int SN_UI_FILL = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int SN_UI_SIZE = ViewGroup.LayoutParams.MATCH_PARENT;
    //endregion

    //region http
    public static final int SN_HTTP_REQUEST_TIME_OUT = 60000;
    public static final int SN_HTTP_REQUEST_RETRY = 0;
    public static final int SN_HTTP_REQUEST_TYPE_GET = 0;
    public static final int SN_HTTP_REQUEST_TYPE_POST = 1;
    public static final int SN_HTTP_REQUEST_TYPE_PUT = 2;
    public static final int SN_HTTP_REQUEST_TYPE_DELETE = 3;

    public static final String SN_HTTP_REQUEST_CONTENT_TYP_KEY = "Content-Type";
    public static final String SN_HTTP_REQUEST_CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String SN_HTTP_REQUEST_CONTENT_TYPE_HTML = "text/html";
    public static final String SN_HTTP_REQUEST_CONTENT_TYPE_JSON = "application/json";
    public static final String SN_HTTP_REQUEST_CONTENT_TYPE_XML = "text/xml";
    //endregion
    // region cache

    // endregion

}
