package com.sn.util;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.sn.interfaces.SNOnImageLoadListener;
import com.sn.main.SNConfig;
import com.sn.models.SNSize;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.URLEncoder;
import java.util.HashMap;

public class SNUtility {
    private static final String LCAP = "SNUtility Log";
    private static HashMap<String, SoftReference<Bitmap>> imageCatch;
    private static SNUtility utility;

    private SNUtility() {

    }

    public static SNUtility instance() {
        utility = new SNUtility();
        return utility;
    }

    SNImageLoadHandler handler;

    class SNImageLoadResult{
        public SNOnImageLoadListener  onImageLoadListener;
        public Bitmap bitmap;
    }
    /**
     * Avoid leaks by using a non-anonymous handler class.
     */
    private static class SNImageLoadHandler extends Handler {
        SNUtility utility;

        public SNImageLoadHandler(SNUtility utility) {
            this.utility = utility;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj != null && msg.obj instanceof SNImageLoadResult) {
                SNImageLoadResult result=(SNImageLoadResult)msg.obj;
                if (result.onImageLoadListener!=null && result.bitmap!=null)
                    result.onImageLoadListener.onSuccess(result.bitmap);
                else if(result.onImageLoadListener!=null){
                    result.onImageLoadListener.onFailure();
                }
            }
        }
    }

    //region http request(http)

    private String httpEncodeParams(String params) {
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

    private String httpEncodeUrlOrParams(String urlOrParams) {
        String[] r = urlOrParams.split("[?]");
        if (r.length == 1) {
            urlOrParams = httpEncodeParams(r[0]);

        } else if (r.length == 2) {
            urlOrParams = urlOrParams.replace(r[1], httpEncodeParams(r[1]));
        }
        return urlOrParams;
    }

    /**
     * 生成AsyncHttpClient 对象
     *
     * @param requestHeader
     * @param contentType
     * @return
     */
    public AsyncHttpClient httpCreateAsyncHttpClient(HashMap<String, String> requestHeader, String contentType) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(SNConfig.SN_HTTP_REQUEST_RETRY, SNConfig.SN_HTTP_REQUEST_TIME_OUT);
        if (requestHeader != null)
            for (String key : requestHeader.keySet()) {
                client.addHeader(key, requestHeader.get(key));
            }
        if (strIsNotNullOrEmpty(contentType)) {
            client.addHeader(SNConfig.SN_HTTP_REQUEST_CONTENT_TYP_KEY, contentType);
        }
        return client;
    }

    //endregion

    //region String (str)

    /**
     * format("one:{0},two:{1}",1,2)
     *
     * @param str
     * @param objects
     * @return
     */
    public String strFormat(String str, Object... objects) {
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
     *
     * @param object
     * @return
     */
    public String strParse(Object object) {
        return object.toString();
    }

    /**
     * get string is null or empty?
     *
     * @param str
     * @return
     */
    public boolean strIsNullOrEmpty(String str) {
        if (str == null) {
            return true;
        }
        return str.equals("");
    }

    /**
     * get string is not null or empty?
     *
     * @param str
     * @return
     */
    public boolean strIsNotNullOrEmpty(String str) {
        return !strIsNullOrEmpty(str);
    }
    //endregion

    //region JSON(json)

    /**
     * jsong to Object
     *
     * @param json json str
     * @param classOfT Class
     * @return
     */
    public <T> T jsonParse(String json, Class<T> classOfT) {
        Gson gson = new Gson();
        return gson.fromJson(json, classOfT);
    }

    /**
     * Object to json
     *
     * @param object object
     * @return
     */
    public String jsonStringify(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    /**
     * json to JSONObject
     *
     * @param json json str
     * @return
     */
    public JSONObject jsonParse(String json) {
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    /**
     * json to JSONArray
     * @param json json str
     * @return JSONArray
     */
    public JSONArray jsonParseArray(String json) {
        try {
            return new JSONArray(json);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    //endregion

    //region image(img)

    /**
     * load image from url
     *
     * @param url
     * @param isCache
     * @return
     * @throws Exception
     */
    Bitmap imgLoadFromUrl(String url, boolean isCache) throws Exception {
        Bitmap rBitmap = imgCatch(url);
        if (rBitmap != null) {
            return rBitmap;
        }
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpGet getRequest = new HttpGet(url);
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            HttpResponse response = client.execute(getRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.e("PicShow", "Request URL failed, error code =" + statusCode);
            }

            HttpEntity entity = response.getEntity();
            if (entity == null) {
                Log.e("PicShow", "HttpEntity is null");
            }

            baos = new ByteArrayOutputStream();
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
        imgCatch(url, rBitmap);
        return rBitmap;
    }

    /**
     * load image support callback
     *
     * @param url                  url
     * @param _onImageLoadListener onImageLoadListener
     */
    public void imgLoad(final String url,final SNOnImageLoadListener _onImageLoadListener) {
        if (handler == null)
            handler = new SNImageLoadHandler(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                // TODO Auto-generated method stub
                try {
                    Bitmap bitmap = imgLoadFromUrl(url, true);
                    SNImageLoadResult result=new SNImageLoadResult();
                    result.onImageLoadListener=_onImageLoadListener;
                    result.bitmap=bitmap;
                    msg.obj = result;
                } catch (Exception e) {
                    msg.obj = null;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * get image catch by url
     *
     * @param url
     * @return
     */
    private Bitmap imgCatch(String url) {
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
     *
     * @param url
     * @param img
     */
    private void imgCatch(String url, Bitmap img) {
        if (imageCatch == null) {
            imageCatch = new HashMap<String, SoftReference<Bitmap>>();
        }
        if (!imageCatch.containsKey(url) && img != null) {
            imageCatch.put(url, new SoftReference<Bitmap>(img));
        }
    }

    /**
     * scale image
     *
     * @param mapBitmap bitmap
     * @param isWidth   is width
     * @param val       size val
     * @return SNSize
     */
    public SNSize imgScaleSize(Bitmap mapBitmap, boolean isWidth, int val) {
        SNSize size = new SNSize();
        if (mapBitmap == null) {
            return size;
        }
        if (isWidth) {
            size.setWidth(val);
            size.setHeight((int) (val * ((float) mapBitmap.getHeight() / (float) mapBitmap.getWidth())));
        } else {
            size.setHeight(val);
            size.setWidth((int) (val * ((float) mapBitmap.getWidth() / (float) mapBitmap.getHeight())));
        }
        return size;
    }

    /**
     * 等比例缩放图片（指定宽度或者高度）
     *
     * @param mapBitmap bitmap
     * @param isWidth   is width
     * @param val       size val
     * @return Bitmap
     */
    public Bitmap imgZoom(Bitmap mapBitmap, boolean isWidth, int val) {
        SNSize size = imgScaleSize(mapBitmap, isWidth, val);
        return imgZoom(mapBitmap, size.getWidth() / mapBitmap.getWidth(), size.getHeight() / mapBitmap.getHeight());
    }

    /**
     * 缩放图片(根据高度的缩放比例和宽度缩放比例)
     *
     * @param mapBitmap bitmap
     * @param sx        zoom width value
     * @param sy        zoom height value
     * @return Bitmap
     */
    public Bitmap imgZoom(Bitmap mapBitmap, float sx, float sy) {
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(mapBitmap, 0, 0, mapBitmap.getWidth(), mapBitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    /**
     * 等比例缩放图片
     *
     * @param mapBitmap bitmap
     * @param s         zoom width value
     * @return Bitmap
     */
    public Bitmap imgZoom(Bitmap mapBitmap, float s) {
        return imgZoom(mapBitmap, s, s);
    }

    /**
     * 将图片变成圆形
     *
     * @param scaleBitmapImage
     * @return Bitmap
     */
    public Bitmap imgCircle(Bitmap scaleBitmapImage) {

        int targetWidth = scaleBitmapImage.getWidth();
        int targetHeight = scaleBitmapImage.getHeight();
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();

        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);
        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;

    }

    /**
     * 图片有圆角
     * @param bitmap Bitmap
     * @param pixels px
     * @return
     */
    public Bitmap imgRadius(Bitmap bitmap, float pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final RectF rectF = new RectF(rect);

        final float roundPx = pixels;

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    /**
     * Bitmap to InputStream
     * @param bm Bitmap
     * @return InputStream
     */
    public InputStream imgInputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }


    /**
     * Bitmap to InputStream
     * @param bm Bitmap
     * @param quality quality
     * @return InputStream
     */
    public InputStream imgInputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    /**
     * Drawable to InputStream
     * @param d
     * @return
     */
    public InputStream imgInputStream(Drawable d) {
        Bitmap bitmap = this.imgParse(d);
        return this.imgInputStream(bitmap);
    }
    /**
     * InputStream to Bitmap
     * @param is InputStream
     * @return
     */
    public Bitmap imgParse(InputStream is) {
        return BitmapFactory.decodeStream(is);
    }

    /**
     * byte[] to Bitmap
     * @param b bytes
     * @return
     */
    public Bitmap imgParse(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }
    /**
     * InputStream to Drawable
     * @param is InputStream
     * @return Drawable
     */
    public Drawable imgParseDrawable(InputStream is) {
        Bitmap bitmap = this.imgParse(is);
        return this.imgParseDrawable(bitmap);
    }

    /**
     * Bitmap to Drawable
     * @param bitmap
     * @return
     */
    public Drawable imgParseDrawable(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        Drawable d = (Drawable) bd;
        return d;
    }
    /**
     *  Drawable to  Bitmap
     * @param drawable Drawable
     * @return
     */
    public Bitmap imgParse(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }
    //endregion

    //region byte opera(byte)

    /**
     * byte[] to InputStream
     * @param b byte[]
     * @return InputStream
     */
    public InputStream byteInputStream(byte[] b) {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        return bais;
    }

    /**
     * InputStream to byte[]
     * @param is InputStream
     * @return byte[]
     */
    public byte[] byteParse(InputStream is) {
        String str = "";
        byte[] readByte = new byte[1024];
        int readCount = -1;
        try {
            while ((readCount = is.read(readByte, 0, 1024)) != -1) {
                str += new String(readByte).trim();
            }
            return str.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  Bitmap to bytes
     * @param bm Bitmap
     * @return bytes
     */
    public byte[] byteParse(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Drawable to bytes
     * @param d Drawable
     * @return bytes
     */
    public byte[] byteParse(Drawable d) {
        Bitmap bitmap = this.imgParse(d);
        return this.byteParse(bitmap);
    }

    //endregion

    //region files(file)

    /**
     * asset files copy to sd card
     *
     * @param _context
     * @param fileName
     * @return
     */
    public String fileCopyFileIntoSDCard(Context _context, String fileName) {
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
                out.close();
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

    //region log manager(log)

    private String logName(Class c) {
        return c.getSimpleName() + " Log";
    }

    /**
     * 调试日志
     *
     * @param c
     * @param msg
     */
    public void logDebug(Class c, String msg) {
        SNLog.d(logName(c), msg);
    }

    /**
     * 错误日志
     *
     * @param c
     * @param msg
     */
    public void logError(Class c, String msg) {
        SNLog.e(logName(c), msg);
    }

    /**
     * 信息日志
     *
     * @param c
     * @param msg
     */
    public void logInfo(Class c, String msg) {
        SNLog.i(logName(c), msg);
    }

    /**
     * 长日志
     *
     * @param c
     * @param msg
     */
    public void logVerbose(Class c, String msg) {
        SNLog.v(logName(c), msg);
    }

    /**
     * 警告日志
     *
     * @param c
     * @param msg
     */
    public void logWarn(Class c, String msg) {
        SNLog.w(logName(c), msg);
    }


    //endregion

    //region reflection(ref)

    /**
     * 根据类名，动态实例化对象
     *
     * @param _c
     * @param name
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T refInstanceObject(Class<T> _c, String name) throws Exception {
        Class c = Class.forName(name);
        return (T) c.newInstance();
    }

    /**
     * 判断两个class是不是同一个类
     *
     * @param s
     * @param t
     * @return boolean
     */
    public boolean refClassIsEqual(Class s, Class t) {
        return s.getCanonicalName() == t.getCanonicalName();
    }

    /**
     * 判断两个类型能不能转换
     *
     * @param convert
     * @param to
     * @return boolean
     */
    public boolean refClassAllowConvert(Class convert, Class to) {
        Class[] class_array = to.getInterfaces();
        for (Class item : class_array) {
            if (refClassIsEqual(item, convert))
                return true;
        }
        return false;
    }
    //endregion

    //region
    //endregion
}
