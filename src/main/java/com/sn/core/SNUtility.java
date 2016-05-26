package com.sn.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.sn.interfaces.SNTaskListener;
import com.sn.interfaces.SNThreadDelayedListener;
import com.sn.interfaces.SNThreadListener;
import com.sn.main.SNConfig;
import com.sn.models.SNSize;

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
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class SNUtility {
    static class ThreadHandler extends Handler {
        SNThreadListener threadListener;

        public ThreadHandler(SNThreadListener _threadListener) {
            this.threadListener = _threadListener;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (threadListener != null)
                threadListener.onFinish(msg.obj);

        }
    }


    private static final String LCAP = "SNUtility Log";

    private static HashMap<String, SoftReference<Bitmap>> imageCatch;

    private static SNUtility utility;

    private SNUtility() {

    }

    public static SNUtility instance() {
        utility = new SNUtility();
        return utility;
    }


    //region url
    public String urlDecode(String url) {
        return urlDecode(url, SNConfig.DEFAULT_ENCODING);
    }

    public String urlDecode(String url, String encoding) {
        try {
            url = URLDecoder.decode(url, encoding);
        } catch (Exception ex) {
            Log.e(LCAP, "URLDecoder decode error");
        }
        return url;
    }


    public String urlEncode(String url) {
        return urlEncode(url, SNConfig.DEFAULT_ENCODING);
    }

    public String urlEncode(String url, String encoding) {
        try {
            url = URLEncoder.encode(url, encoding);
        } catch (Exception ex) {
            Log.e(LCAP, "URLEncoder encode error");
        }
        return url;
    }
    //endregion

    //region http request(http)
    private String httpEncodeParams(String params) {
        String[] ps = params.split("[&]");
        if (ps.length > 0) {
            for (String p : ps) {
                String[] kv = p.split("[=]");
                if (kv.length == 2) {
                    String value = kv[1];
                    try {
                        params = params.replace(value, URLEncoder.encode(value, SNConfig.DEFAULT_ENCODING));
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
            do {
                str = str.replace("{" + count + "}", pString);
            } while (str.indexOf("{" + count + "}") >= 0);
            count = count + 1;
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

    public String strCut(final String str, final int maxLength) {
        return strCut(str, maxLength, true);
    }

    public String strCut(final String str, final int maxLength, boolean ellipsis) {
        if (str == null) {
            return str;
        }
        String suffix = "";
        if (ellipsis) suffix = "...";
        int suffixLen = suffix.length();

        final StringBuffer sbuffer = new StringBuffer();
        final char[] chr = str.trim().toCharArray();
        int len = 0;
        for (int i = 0; i < chr.length; i++) {

            if (chr[i] >= 0xa1) {
                len += 2;
            } else {
                len++;
            }
        }
        if (len <= maxLength) {
            return str;
        }
        len = 0;
        for (int i = 0; i < chr.length; i++) {
            if (chr[i] >= 0xa1) {
                len += 2;
                if (len + suffixLen > maxLength) {
                    break;
                } else {
                    sbuffer.append(chr[i]);
                }
            } else {
                len++;
                if (len + suffixLen > maxLength) {
                    break;
                } else {
                    sbuffer.append(chr[i]);
                }
            }
        }
        sbuffer.append(suffix);
        return sbuffer.toString();
    }

    public String strJoin(String[] strs, String join) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            stringBuilder.append(strs[i]).append(join);
        }
        int last_str_index = stringBuilder.length();
        int last_join_index = last_str_index - join.length();
        stringBuilder.delete(last_join_index, last_str_index);
        return stringBuilder.toString();
    }
    //endregion

    //region decimal(decimal)
    public String decimalFormat(double val, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(val);
    }

    public String decimalFormat(float val, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(val);
    }

    public double decimalParse(String decimal) {
        try {
            BigDecimal bigDecimal = new BigDecimal(decimal);
            return bigDecimal.doubleValue();
        } catch (Exception ex) {
            return 0;
        }

    }

    //endregion

    //region Array(array)
    public <T> List<T> arrayToList(T[] t) {
        ArrayList<T> a = new ArrayList<T>();
        for (int i = 0; i < t.length; i++) {
            a.add(t[i]);
        }
        return a;
    }
    //endregion

    //region JSON(json)

    /**
     * 判断name是否在json中存在或者不是null
     *
     * @param object JSONObject
     * @param name   String
     * @return boolean
     */
    public boolean jsonIsNullOrNoHas(JSONObject object, String name) {
        if (object.isNull(name) || !object.has(name)) return true;
        else return false;
    }

    /**
     * 判断name是否有值
     *
     * @param object JSONObject
     * @param name   String
     * @return boolean
     */
    public boolean jsonNotIsNullOrNoHas(JSONObject object, String name) {
        return !jsonIsNullOrNoHas(object, name);
    }

    /**
     * json to Object
     *
     * @param json     json str
     * @param classOfT Class
     * @return
     */
    public <T> T jsonParse(Class<T> classOfT, String json) {
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
     *
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


    public SNSize imgScaleSize(Bitmap mapBitmap, int max, int min, double percent) {
        SNSize size = new SNSize();
        if (mapBitmap == null)
            return size;
        double p = percent * 0.01;
        double height = mapBitmap.getHeight();
        double width = mapBitmap.getWidth();
        if (height > min && width > min) {
            height = height * p;
            width = width * p;
            if (height > max) {
                double temp = height;
                height = max;
                width = width / temp * max;
            }
            if (width > max) {
                double temp = width;
                width = max;
                height = height / temp * width;
            }
        }
        size.setHeight((int) height);
        size.setWidth((int) width);
        return size;
    }

    public Bitmap imgZoom(Bitmap mapBitmap, int max, int min, double percent) {
        SNSize size = imgScaleSize(mapBitmap, max, min, percent);
        return imgZoom(mapBitmap, ((float) size.getWidth()) / ((float) mapBitmap.getWidth()), ((float) size.getHeight()) / ((float) mapBitmap.getHeight()));
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
        return imgZoom(mapBitmap, ((float) size.getWidth()) / ((float) mapBitmap.getWidth()), ((float) size.getHeight()) / ((float) mapBitmap.getHeight()));
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

    public Bitmap imgCircleBorder(Bitmap bitmap, int width, int color) {
        int targetWidth = bitmap.getWidth();
        int targetHeight = bitmap.getHeight();
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
        Paint paint = new Paint();
        /* 去锯齿 */
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(color);
        /* 设置paint的　style　为STROKE：空心 */
        paint.setStyle(Paint.Style.STROKE);
        /* 设置paint的外框宽度 */
        paint.setStrokeWidth(targetBitmap.getWidth());
        Bitmap sourceBitmap = bitmap;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        canvas.drawCircle(targetBitmap.getWidth() / 2, targetBitmap.getHeight() / 2, width, paint);
        return targetBitmap;
    }

    /**
     * 图片有圆角
     *
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

    public Bitmap imgCurve(Bitmap bitmap) {
        return imgCurve(bitmap, bitmap.getHeight() / 6);
    }

    public Bitmap imgCurve(Bitmap bitmap, float size) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(),
                        bitmap.getHeight()),
                new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), null);
        final Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        final Path path = new Path();
        final float x1 = 0;
        final float y1 = bitmap.getHeight();
        final float x2 = bitmap.getWidth();
        final float y2 = bitmap.getHeight();
        final float x3 = bitmap.getWidth() / 2;
        final float y3 = bitmap.getHeight() - size;
        float a = 2 * (x2 - x1);
        float b = 2 * (y2 - y1);
        float c = x2 * x2 + y2 * y2 - x1 * x1 - y1 * y1;
        float d = 2 * (x3 - x2);
        float e = 2 * (y3 - y2);
        float f = x3 * x3 + y3 * y3 - x2 * x2 - y2 * y2;
        float x = (b * f - e * c) / (b * d - e * a);
        float y = (d * c - a * f) / (b * d - e * a);
        float r = (float) Math.sqrt(((x - x1) * (x - x1) + (y - y1) * (y - y1)));
        canvas.drawCircle(x, y, r, paint);
        return output;
    }

    public Bitmap imgCreate(int w, int h) {
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        return output;
    }

    /**
     * Bitmap to InputStream
     *
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
     *
     * @param bm      Bitmap
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
     *
     * @param d
     * @return
     */
    public InputStream imgInputStream(Drawable d) {
        Bitmap bitmap = this.imgParse(d);
        return this.imgInputStream(bitmap);
    }

    /**
     * InputStream to Bitmap
     *
     * @param is InputStream
     * @return
     */
    public Bitmap imgParse(InputStream is) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            opts.inSampleSize = 1;
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * byte[] to Bitmap
     *
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
     *
     * @param is InputStream
     * @return Drawable
     */
    public Drawable imgParseDrawable(InputStream is) {
        Bitmap bitmap = this.imgParse(is);
        return this.imgParseDrawable(bitmap);
    }

    /**
     * Bitmap to Drawable
     *
     * @param bitmap
     * @return
     */
    public Drawable imgParseDrawable(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        Drawable d = (Drawable) bd;
        return d;
    }

    /**
     * Drawable to  Bitmap
     *
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
     *
     * @param b byte[]
     * @return InputStream
     */
    public InputStream byteInputStream(byte[] b) {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        return bais;
    }

    /**
     * InputStream to byte[]
     *
     * @param is InputStream
     * @return byte[]
     */
    public byte[] byteParse(InputStream is) {
        try {
            byte[] buffer = new byte[1024];
            int len = -1;
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            while ((len = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] data = outStream.toByteArray();
            outStream.close();
            is.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Bitmap to bytes
     *
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
     *
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
     * 读取文件
     *
     * @param path
     * @param name
     * @return
     */
    public String fileRead(String path, String name) {
        //生成文件夹之后，再生成文件，不然会出错
        String strFilePath = path + name;
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                return null;
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            byte[] read_bytes = new byte[(int) file.length()];
            raf.readFully(read_bytes);
            String content = new String(read_bytes);
            raf.close();
            return content;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 写入文件
     *
     * @param path
     * @param name
     * @param content
     * @return
     */
    public boolean fileWrite(String path, String name, String content) {
        //生成文件夹之后，再生成文件，不然会出错
        if (fileMake(path, name) != null) {
            String strFilePath = path + name;
            // 每次写入时，都换行写
            String strContent = content + "\r\n";
            try {
                File file = new File(strFilePath);
                if (file.exists()) {
                    file.delete();
                }
                file.getParentFile().mkdirs();
                file.createNewFile();
                RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                raf.seek(file.length());
                raf.write(strContent.getBytes());
                raf.close();
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * 创建文件
     *
     * @param path
     * @param name
     * @return
     */
    public File fileMake(String path, String name) {
        File file = null;
        if (fileMakeDirectory(path)) {
            try {
                file = new File(path + name);
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return file;
        } else {
            return null;
        }

    }

    /**
     * 创建目录
     *
     * @param path
     * @return
     */
    public boolean fileMakeDirectory(String path) {
        File file = null;
        try {
            file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

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
     * log 开关
     *
     * @param v
     */
    public void logSwitch(boolean v) {
        SNLogManager.setLogSwitch(v);
    }

    /**
     * 文件log开关
     *
     * @param v
     */
    public void logFileSwitch(boolean v) {
        SNLogManager.setFileLogSwitch(v);
    }

    /**
     * 调试日志
     *
     * @param c
     * @param msg
     */
    public void logDebug(Class c, String msg) {
        SNLogManager.d(logName(c), msg);
    }

    /**
     * 错误日志
     *
     * @param c
     * @param msg
     */
    public void logError(Class c, String msg) {
        SNLogManager.e(logName(c), msg);
    }

    /**
     * 信息日志
     *
     * @param c
     * @param msg
     */
    public void logInfo(Class c, String msg) {
        SNLogManager.i(logName(c), msg);
    }

    /**
     * 长日志
     *
     * @param c
     * @param msg
     */
    public void logVerbose(Class c, String msg) {
        SNLogManager.v(logName(c), msg);
    }

    /**
     * 警告日志
     *
     * @param c
     * @param msg
     */
    public void logWarn(Class c, String msg) {
        SNLogManager.w(logName(c), msg);
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

    //region encode and decode
    //region base64(base64)

    /**
     * byte[] base64 encode
     *
     * @param bytes
     * @return byte[]
     */
    public byte[] base64Encode(byte[] bytes) {
        try {
            return Base64.encode(bytes, Base64.NO_WRAP);
        } catch (Exception ex) {
            logError(SNUtility.class, ex.getMessage());
            return null;
        }
    }

    /**
     * bytes base64
     *
     * @param bytes byte[]
     * @return String
     */
    public String base64EncodeStr(byte[] bytes) {
        byte[] bytes_b64 = base64Encode(bytes);
        if (bytes_b64 != null)
            return new String(bytes_b64);
        else return null;
    }

    /**
     * strings base64
     *
     * @param s String
     * @return String
     */
    public String base64EncodeStr(String s) {
        return base64EncodeStr(s.getBytes());
    }

    /**
     * byte[] base64 decode to byte[]
     *
     * @param bytesB64 byte[]
     * @return byte[]
     */
    public byte[] base64Decode(byte[] bytesB64) {
        try {
            return Base64.decode(bytesB64, Base64.NO_WRAP);
        } catch (Exception ex) {
            logError(SNUtility.class, ex.getMessage());
            return null;
        }
    }

    /**
     * bytes base64 Decode to String
     *
     * @param bytesB64 byte[]
     * @return String
     */
    public String base64DecodeStr(byte[] bytesB64) {
        byte[] bytes = base64Decode(bytesB64);
        if (bytes != null)
            return new String(bytes);
        else return null;
    }

    /**
     * strBase64 base64 decode to byte[]
     *
     * @param strBase64 String
     * @return byte[]
     */
    public byte[] base64Decode(String strBase64) {
        return base64Decode(strBase64.getBytes());
    }

    /**
     * strBase64 base64 decode to String
     *
     * @param strBase64 String
     * @return String
     */
    public String base64DecodeStr(String strBase64) {
        return base64DecodeStr(strBase64.getBytes());
    }
    //endregion()

    //region md5(md5)
    public String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
    //endregion

    //region des(des)
    private final static String DES = "DES";

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public String desEncrypt(String data, String key) throws Exception {
        byte[] bt = desEncrypt(data.getBytes(), key.getBytes());
        String strs = base64EncodeStr(bt);
        return strs;
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws IOException
     * @throws Exception
     */
    public String desDecrypt(String data, String key) throws IOException,
            Exception {
        if (data == null)
            return null;

        byte[] buf = base64Decode(data);
        byte[] bt = desDecrypt(buf, key.getBytes());
        return new String(bt);
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private byte[] desEncrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private byte[] desDecrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }
    //endregion
    //endregion

    //region datetime(date)
    public Calendar dateUtc(Calendar _calendar) {
        Calendar calendar = (Calendar) _calendar.clone();
        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        int dstOffset = calendar.get(Calendar.DST_OFFSET);
        calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return calendar;
    }

    public Calendar dateInstance(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public Calendar dateNow() {
        return Calendar.getInstance();
    }

    public Calendar dateInstance(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar;
    }

    public String dateToString(Calendar date, String format) {
        SimpleDateFormat dataFormat = new SimpleDateFormat(format);
        return dataFormat.format(date.getTime());
    }

    public Calendar dateParse(String date, String format) {
        try {
            SimpleDateFormat dataFormat = new SimpleDateFormat(format);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataFormat.parse(date));
            return calendar;
        } catch (ParseException ex) {
            return null;
        }
    }

    /**
     * 获取当前日期中月份一共有多少天
     *
     * @param date Date
     * @return day
     */
    public int dateDayOfMonth(Calendar date) {
        return date.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取指定某年某月有多少天
     *
     * @param year  year
     * @param month month
     * @return day
     */
    public int dateDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 1);
        return dateDayOfMonth(c);
    }

    /**
     * 获取当前日期是星期几（0代表周日）
     *
     * @param calendar
     * @return week
     */
    public int dateWeek(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 获取指定年月日是星期几（0代表周日）
     *
     * @param year  year
     * @param month month
     * @param day   day
     * @return week
     */
    public int dateWeek(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        return dateWeek(c);
    }


    //endregion

    //region thread
    public SNTask taskRun(SNTaskListener taskListener) {
        SNTask task = new SNTask(taskListener);
        return task;
    }

    /**
     * 使用taskRun，可以解决多线程并发
     *
     * @param threadListener
     * @return
     */

    public Thread threadRun(final SNThreadListener threadListener) {
        final ThreadHandler handler = new ThreadHandler(threadListener);

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                if (threadListener != null) {
                    Message msg = new Message();
                    msg.obj = threadListener.run();
                    handler.sendMessage(msg);
                }
            }
        };
        thread.start();
        return thread;
    }

    public Thread threadDelayed(final long time, final SNThreadDelayedListener threadDelayedListener) {
        return threadRun(new SNThreadListener() {
            @Override
            public Object run() {
                try {
                    Thread.sleep(time);
                } catch (Exception e) {
                }
                return null;
            }

            @Override
            public void onFinish(Object object) {
                if (threadDelayedListener != null) threadDelayedListener.onFinish();
            }
        });
    }


    //endregion

    //region version(version)

    /**
     * 判断版本是否需要更新
     *
     * @param localVersion  本地版本
     * @param onlineVersion 远程版本
     * @return boolean
     */
    public static boolean versionIsNeedUpdate(String localVersion, String onlineVersion) {
        if (localVersion.equals(onlineVersion)) {
            return false;
        }
        String[] localArray = localVersion.split("\\.");
        String[] onlineArray = onlineVersion.split("\\.");

        int length = localArray.length < onlineArray.length ? localArray.length : onlineArray.length;

        for (int i = 0; i < length; i++) {
            if (Integer.parseInt(onlineArray[i]) > Integer.parseInt(localArray[i])) {
                return true;
            } else if (Integer.parseInt(onlineArray[i]) < Integer.parseInt(localArray[i])) {
                return false;
            }
            // 相等 比较下一组值
        }
        return true;
    }
    //endregion

    //region interval
    public SNInterval interval() {
        return new SNInterval();
    }
    //endregion

    //region print

    /**
     * 打印bundle
     *
     * @param bundle
     * @return
     */
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            sb.append("\nkey:" + key + ", value:" + bundle.get(key));
        }
        return sb.toString();
    }
    //endregion


    public class SNTask extends AsyncTask<Object, Void, Object> {
        SNTaskListener taskListener;

        SNTask(SNTaskListener _taskListener) {
            this.taskListener = _taskListener;
        }

        @Override
        protected Object doInBackground(Object... params) {
            if (params.length > 0)
                return taskListener.onTask(this, params[0]);
            else return taskListener.onTask(this, null);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            taskListener.onFinish(this, o);
        }


    }
}
