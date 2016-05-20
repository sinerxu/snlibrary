package com.sn.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.LruCache;
import android.widget.AbsListView;

import com.sn.interfaces.SNOnImageLoadListener;
import com.sn.interfaces.SNOnSetImageListenter;
import com.sn.interfaces.SNTaskListener;
import com.sn.interfaces.SNThreadListener;
import com.sn.main.SNManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import libcore.io.DiskLruCache;

/**
 * Created by xuhui on 16/1/8.
 */
public class SNLoadBitmapManager {
    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private static LruCache<String, Bitmap> mMemoryCache;
    /**
     * 记录所有正在下载或等待下载的任务。
     */
    private Set<SNUtility.SNTask> taskCollection;

    SNManager $;
    private static SNLoadBitmapManager loadBitmapManager;
    /**
     * 图片硬盘缓存核心类。
     */
    private DiskLruCache mDiskLruCache;

    public static SNLoadBitmapManager instance(SNManager manager) {
        if (loadBitmapManager == null)
            loadBitmapManager = new SNLoadBitmapManager(manager);
        return loadBitmapManager;
    }

    SNLoadBitmapManager(SNManager manager) {
        $ = manager;
        taskCollection = new HashSet<SNUtility.SNTask>();
        try {
            File cacheDir = getDiskCacheDir($.getContext(), "bitmap");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion($.getContext()), 1, 5 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static boolean allow = true;
    static Object lock = new Object();

    /**
     * 保存图片
     *
     * @return
     */
    public void loadImageFromUrl(final String imageUrl, final SNOnSetImageListenter onSetImageListenter, final SNOnImageLoadListener _onImageLoadListener) {

        if ($.util.strIsNullOrEmpty(imageUrl)) {
            _onImageLoadListener.onFailure();
        } else {
            final String key = getImageNameByUrl(imageUrl);
            if ($.util.strIsNullOrEmpty(key)) _onImageLoadListener.onFailure();
            //先调用一级缓存
            Bitmap bitmap = getBitmapFromMemoryCache(key);
            if (bitmap == null) {
                final SNUtility.SNTask task = $.util.taskRun(new SNTaskListener() {
                    @Override
                    public Object onTask(SNUtility.SNTask task, Object param) {
                        try {
                            String imageUrl = param.toString();
                            String key = getImageNameByUrl(imageUrl);
                            //二级缓存
                            Bitmap bitmap = getBitmapFromDisk(key);
                            if (bitmap != null) {
                                if (onSetImageListenter != null)
                                    bitmap = onSetImageListenter.onSetBitmap(bitmap);
                                addBitmapToMemoryCache(key, bitmap);
                                return bitmap;
                            }
                            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                            if (editor != null) {
                                OutputStream outputStream = editor.newOutputStream(0);
                                if (downloadUrlToStream(imageUrl, outputStream)) {
                                    editor.commit();
                                } else {
                                    editor.abort();
                                }
                            }
                            mDiskLruCache.flush();
                            //保存到缓存
                            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
                            if (snapShot != null) {
                                bitmap = getBitmapFromDisk(key);
                                if (bitmap != null) {
                                    if (onSetImageListenter != null)
                                        bitmap = onSetImageListenter.onSetBitmap(bitmap);
                                    addBitmapToMemoryCache(key, bitmap);
                                    return bitmap;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;

                    }

                    @Override
                    public void onFinish(SNUtility.SNTask task, Object object) {
                        if (_onImageLoadListener != null && object != null) {
                            try {
                                Bitmap bitmap = (Bitmap) object;
                                if (bitmap != null) _onImageLoadListener.onSuccess(bitmap);
                                else _onImageLoadListener.onFailure();
                            } catch (Exception e) {
                                e.printStackTrace();
                                _onImageLoadListener.onFailure();
                            }
                        } else {
                            _onImageLoadListener.onFailure();
                        }

                    }
                });

                task.execute(imageUrl);
            } else {
                if (_onImageLoadListener != null) _onImageLoadListener.onSuccess(bitmap);
            }
        }
    }


    /**
     * 取消所有正在下载或等待下载的任务。
     */
    public void cancelAllTasks() {
        if (taskCollection != null) {
            for (SNUtility.SNTask task : taskCollection) {
                task.cancel(false);
            }
        }
    }

    public void lock() {
        allow = false;
    }

    public void unlock() {
        allow = true;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    private LruCache<String, Bitmap> getLruCatch() {
        if (mMemoryCache == null) {
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            //int max = 8 * 1024 * 1024 * 8;
            int cacheSize = maxMemory / 8;//> max ? max : maxMemory / 10;
            $.util.logInfo(SNLoadBitmapManager.class, $.util.strFormat("初始化LruCache，maxMemory={0}byte,{1}kb", maxMemory, maxMemory / 1024));
            // 设置图片缓存大小为程序最大可用内存的1/8
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }
        return mMemoryCache;
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getLruCatch().get(key) == null) {
            getLruCatch().put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemoryCache(String key) {
        Bitmap bitmap = getLruCatch().get(key);
        loadImageLog("getBitmapFromMemoryCache", key, bitmap);
        return bitmap;
    }


    private void loadImageLog(String type, String key, Bitmap bitmap) {
        if (bitmap != null)
            $.util.logInfo(SNLoadBitmapManager.class, $.util.strFormat(type + " key：{0},size：{1}byte,{2}kb,当前缓存大小：{3}byte，当前缓存大小:{4}kb,最大缓存大小：{5}byte,{6}kb", key, bitmap.getRowBytes() * bitmap.getHeight(), bitmap.getRowBytes() * bitmap.getHeight() / 1024.0, getLruCatch().size(), getLruCatch().size() / 1024.0, getLruCatch().maxSize(), getLruCatch().maxSize() / 1024));
    }

    private Bitmap getBitmapFromDisk(String key) {
        try {
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                byte[] imageByte = $.util.byteParse(is);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length, opts);
                opts.inSampleSize = 1;
                opts.inJustDecodeBounds = false;
                $.util.logInfo(SNLoadBitmapManager.class, $.util.strFormat("outWidth={0} outHeight={1}", opts.outWidth, opts.outHeight));

                if (opts.outWidth == opts.outHeight) {
                    if (opts.outWidth >= 120) {
                        opts.inSampleSize = opts.outWidth / 120;
                    }
                } else {
                    if (opts.outWidth > 800 || opts.outHeight > 800) {
                        opts.inSampleSize = Math.max(opts.outWidth / 800, opts.outHeight / 800);
                    }
                }


                opts.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length, opts);
                if (bitmap == null)
                    $.util.logInfo(SNLoadBitmapManager.class, "inJustDecodeBounds = false bitmap is null");
                is.close();
                System.gc();
                loadImageLog("getBitmapFromDisk", key, bitmap);
                return bitmap;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String getImageNameByUrl(String url) {
        $.util.logInfo(SNLoadBitmapManager.class, "图片名称：" + url);
        try {
            String name = $.util.md5(url);
            $.util.logInfo(SNLoadBitmapManager.class, "转化成md5的名称：" + name);
            return name;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private File getDiskCacheDir(Context context, String uniqueName) {

        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

}
