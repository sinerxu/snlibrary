package com.sn.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.sn.main.SNConfig;
import com.sn.models.SNSize;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by xuhui on 15/8/4.
 */
public class SNUtility {
    private static final String LCAP = "SNUtility Log";
    private static HashMap<String, SoftReference<Bitmap>> imageCatch;

    //region http request


    private static String encodeParams(String params) {
        String[] ps = params.split("[&]");
        if (ps.length > 0) {
            for (String p : ps) {
                String[] kv = p.split("[=]");
                if (kv.length == 2) {
                    String value = kv[1];
                    try {
                        params = params.replace(value, URLEncoder.encode(value, "utf-8"));
                    } catch (Exception ex) {
                        Log.e(LCAP, "URLEncoder encode error");
                    }
                }
            }
        }
        return params;
    }

    private static String encodeUrlOrParams(String urlOrParams) {
        String[] r = urlOrParams.split("[?]");
        if (r.length == 1) {
            urlOrParams = encodeParams(r[0]);

        } else if (r.length == 2) {
            urlOrParams = urlOrParams.replace(r[1], encodeParams(r[1]));
        }
        return urlOrParams;
    }

    /**
     * 生成AsyncHttpClient 对象
     * @param requestHeader
     * @param contentType
     * @return
     */
    public static AsyncHttpClient makeAsyncHttpClient(HashMap<String, String> requestHeader,String contentType) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(SNConfig.SN_HTTP_REQUEST_RETRY, SNConfig.SN_HTTP_REQUEST_TIME_OUT);
        if (requestHeader != null)
            for (String key : requestHeader.keySet()) {
                client.addHeader(key, requestHeader.get(key));
            }
        if (isNotNullOrEmpty(contentType)){
            client.addHeader(SNConfig.SN_HTTP_REQUEST_CONTENT_TYP_KEY,contentType);
        }
        return client;
    }

    //endregion

    //region String

    /**
     * format("one:{0},two:{1}",1,2)
     * @param str
     * @param objects
     * @return
     */
    public static String format(String str, Object... objects) {
        str = str.toString();
        int count = 0;
        do {
            if (objects.length < count) {
                return str;
            }
            String pString = "";
            if (objects[count] != null) {
                pString = objects[count].toString();
            }
            str = str.replaceAll("[{]" + count + "[}]", pString);

            count++;
        } while (str.indexOf("{" + count + "}") >= 0);
        return str;
    }

    /**
     * obj to string
     * @param object
     * @return
     */
    public static String toString(Object object) {
        return object.toString();
    }

    /**
     * get string is null or empty?
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null) {
            return true;
        }
        return str.equals("");
    }

    /**
     * get string is not null or empty?
     * @param str
     * @return
     */
    public static boolean isNotNullOrEmpty(String str) {
       return !isNullOrEmpty(str);
    }
    //endregion

    //region JSON

    /**
     * jsong to Object
     *
     * @param json
     * @param classOfT
     * @return
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        Gson gson = new Gson();
        return gson.fromJson(json, classOfT);
    }

    /**
     * Object to json
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    /**
     * json to JSONObject
     *
     * @param json
     * @return
     */
    public static JSONObject fromJson(String json) {
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    //endregion

    //region image

    /**
     * load image from url
     * @param url
     * @param isCache
     * @return
     * @throws Exception
     */
    public static Bitmap loadImageFromUrl(String url, boolean isCache) throws Exception {
        Bitmap rBitmap = getImageCatch(url);
        if (rBitmap != null) {
            return rBitmap;
        }
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpGet getRequest = new HttpGet(url);
        HttpResponse response = client.execute(getRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            Log.e("PicShow", "Request URL failed, error code =" + statusCode);
        }

        HttpEntity entity = response.getEntity();
        if (entity == null) {
            Log.e("PicShow", "HttpEntity is null");
        }
        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            is = entity.getContent();
            byte[] buf = new byte[1024];
            int readBytes = -1;
            while ((readBytes = is.read(buf)) != -1) {
                baos.write(buf, 0, readBytes);
            }
        } finally {
            if (baos != null) {
                baos.close();
            }
            if (is != null) {
                is.close();
            }
        }
        byte[] imageArray = baos.toByteArray();
        rBitmap = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
        setImageCatch(url, rBitmap);
        return rBitmap;
    }

    /**
     * load image support callback
     * @param url
     * @param handler
     */
    public static void loadImage(final String url, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                // TODO Auto-generated method stub
                try {
                    Bitmap bitmap = loadImageFromUrl(url, true);
                    msg.obj = bitmap;
                } catch (Exception e) {
                    msg.obj = null;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * get image catch by url
     * @param url
     * @return
     */
    private static Bitmap getImageCatch(String url) {
        if (imageCatch == null || !imageCatch.containsKey(url)) {
            return null;
        } else {
            if (imageCatch.get(url).get() == null) {
                imageCatch.remove(url);
                return null;
            }
            return imageCatch.get(url).get();
        }
    }

    /**
     * set img catch by url
     * @param url
     * @param img
     */
    private static void setImageCatch(String url, Bitmap img) {
        if (imageCatch == null) {
            imageCatch = new HashMap<String, SoftReference<Bitmap>>();
        }
        if (!imageCatch.containsKey(url) && img != null) {
            imageCatch.put(url, new SoftReference<Bitmap>(img));
        }
    }

    /**
     * scale img size ,support auto size
     * @param mapBitmap
     * @param isWidth
     * @param val
     * @return
     */
    public static SNSize scale(Bitmap mapBitmap, boolean isWidth, int val) {
        SNSize size = new SNSize();
        if (mapBitmap == null) {
            return size;
        }
        if (isWidth) {
            size.width = val;
            size.height = (int) (val * ((float) mapBitmap.getHeight() / (float) mapBitmap.getWidth()));
        } else {
            size.height = val;
            size.width = (int) (val * ((float) mapBitmap.getWidth() / (float) mapBitmap.getHeight()));
        }
        return size;
    }
    //endregion

    //region files

    /**
     * asset files copy to sd card
     *
     * @param _context
     * @param fileName
     * @return
     */
    public static String copyFileIntoSDCard(Context _context, String fileName) {
        String lcat = "CopyFileIntoSDCard Log";
        Log.e(lcat, fileName);
        String path = null;
        try {
            //判断SD卡是否存在
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())
                    && Environment.getExternalStorageDirectory().exists()) {
                path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/" + fileName;
            } else {
                //放入/data/data//files目录
                path = _context.getFilesDir().getAbsolutePath() + "/" + fileName;
            }

            InputStream in = null;
            OutputStream out = null;

            try {
                Log.e(lcat, "open assets file : " + fileName);
                in = _context.getAssets().open(fileName);
                Log.e(lcat, "new file : " + path);
                File outFile = new File(path);
                if (!outFile.exists()) {
                    out = new FileOutputStream(outFile);
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                }
            } catch (IOException e) {
                Log.e(lcat, e.getMessage());
            } finally {
                in.close();
                in = null;
                out.close();
                out = null;
            }

            Log.d(lcat, "Copy file into sd card:" + path);
        } catch (Throwable t) {
            if (t != null) {
                Log.e(lcat, t.toString());
            } else {
                Log.e(lcat, "Copy file into sd card fail and no any message.");
            }

            Log.e(lcat, "Copy file into sd card fail");
        }
        return path;
    }
    //endregion

    //region log manager

    private static String getLogName(Class c) {
        return c.getSimpleName() + " Log";
    }

    /**
     * 调试日志
     *
     * @param c
     * @param msg
     */
    public static void logDebug(Class c, String msg) {
        SNLog.d(getLogName(c), msg);
    }

    /**
     * 错误日志
     *
     * @param c
     * @param msg
     */
    public static void logError(Class c, String msg) {
        SNLog.e(getLogName(c), msg);
    }

    /**
     * 信息日志
     *
     * @param c
     * @param msg
     */
    public static void logInfo(Class c, String msg) {
        SNLog.i(getLogName(c), msg);
    }

    /**
     * 长日志
     *
     * @param c
     * @param msg
     */
    public static void logVerbose(Class c, String msg) {
        SNLog.v(getLogName(c), msg);
    }

    /**
     * 警告日志
     *
     * @param c
     * @param msg
     */
    public static void logWarn(Class c, String msg) {
        SNLog.w(getLogName(c), msg);
    }


    //endregion

    //region activity

    /**
     * 可以跳转到微信的intent
     * @return Intent
     */
    public static Intent getWeChatIntent() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        return intent;
    }
    //endregion

    //region reflection
    /**
     * 根据类名，动态实例化对象
     * @param _c
     * @param name
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T instanceObject(Class<T> _c, String name) throws Exception {
        Class c = Class.forName(name);
        return (T) c.newInstance();
    }

    /**
     * 判断两个class是不是同一个类
     * @param s
     * @param t
     * @return boolean
     */
    public static boolean classIsEqual(Class s,Class t){
        return s.getCanonicalName()==t.getCanonicalName();
    }

    /**
     * 判断两个类型能不能转换
     * @param convert
     * @param to
     * @return boolean
     */
    public static boolean classAllowConvert(Class convert,Class to){
        Class[] class_array=to.getInterfaces();
        for (Class item:class_array) {
            if (classIsEqual(item,convert))
                return true;
        }
        return false;
    }
    //endregion
}
