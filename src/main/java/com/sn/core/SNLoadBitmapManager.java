package com.sn.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.LruCache;

import com.sn.interfaces.SNOnImageLoadListener;
import com.sn.interfaces.SNOnSetImageListenter;
import com.sn.interfaces.SNTaskListener;
import com.sn.main.SNManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
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
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion($.getContext()), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            //先调用一级缓存
            Bitmap bitmap = getBitmapFromMemoryCache(key);
            if (bitmap == null) {
                SNUtility.SNTask task = $.util.taskRun(new SNTaskListener() {
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
                        if (_onImageLoadListener != null) {
                            try {
                                Bitmap bitmap = (Bitmap) object;
                                if (bitmap != null) _onImageLoadListener.onSuccess(bitmap);
                                else _onImageLoadListener.onFailure();
                            } catch (Exception e) {
                                _onImageLoadListener.onFailure();
                            }
                        }
                        taskCollection.remove(task);
                    }
                });
                taskCollection.add(task);
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


    private LruCache<String, Bitmap> getLruCatch() {
        if (mMemoryCache == null) {
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int cacheSize = maxMemory / 8;
            // 设置图片缓存大小为程序最大可用内存的1/8
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount();
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
        return getLruCatch().get(key);
    }

    private Bitmap getBitmapFromDisk(String key) {
        try {
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            if (snapShot != null) {
                Bitmap bitmap = $.util.imgParse(snapShot.getInputStream(0));
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
        String name = $.util.md5(url);
        return name;
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
