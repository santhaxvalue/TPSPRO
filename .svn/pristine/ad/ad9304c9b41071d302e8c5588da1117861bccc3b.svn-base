/*
 * Copyright (C) 2010 Cyril Mottier (http://www.cyrilmottier.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.panelManagement.asyncimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;

import com.panelManagement.activity.TPSApplication;
import com.panelManagement.activity.TPSApplication.OnLowMemoryListener;
import com.panelManagement.asyncimage.MemoryCache.ImageCacheParams;

import java.io.File;
import java.io.FileOutputStream;

/**
 * A very basing implementation of an Bitmap cache
 *
 * @author Cyril Mottier
 */
public class ImageCache implements OnLowMemoryListener {

    Context context;
    MemoryCache mMemoryCache = null;
    private File cacheDir;

    public ImageCache(Context context) {
        this.context = context;
        TPSApplication gApp = (TPSApplication) context.getApplicationContext();
        gApp.registerOnLowMemoryListener(this);
        initFileCache(context);
        initMemoryCache(context);
    }

    public static ImageCache from(Context context) {
        TPSApplication gApp = (TPSApplication) context.getApplicationContext();
        return gApp.getImageCache();
    }

    void initMemoryCache(Context context) {
        if (mMemoryCache == null) {
            MemoryCache.ImageCacheParams params = new ImageCacheParams();
            params.setMemCacheSizePercent(0.5f);
            mMemoryCache = MemoryCache.getInstance(params);
        }
    }

    public Bitmap get(String url) {
        return findImage(url, null);
    }

    public Bitmap get(String url, ImageOptions options) {
        return findImage(url, options);
    }

    @SuppressWarnings("deprecation")
    private Bitmap findImage(String url, ImageOptions options) {
        String filename = String.valueOf(url.hashCode());
        BitmapDrawable drawable = mMemoryCache.getBitmapFromMemCache(filename);
        Bitmap bitmap = null;
        if (drawable == null) {
            if (options != null)
                bitmap = decodeFile(getFile(url), options);
            else
                bitmap = decodeFile(getFile(url));
            if (bitmap != null)
                mMemoryCache.addBitmapToCache(filename, new BitmapDrawable(
                        bitmap));
        } else {
            bitmap = drawable.getBitmap();
        }
        return bitmap;
    }

    public void put(String url, Bitmap bitmap) {
        putfile(bitmap, String.valueOf(url.hashCode()));
    }

    public void flush() {
        flushMemoryCache();
        try {
            if (cacheDir.isDirectory()) {
                File[] flist = cacheDir.listFiles();
                for (File f : flist) {
                    if (!f.getName().endsWith("json"))
                        f.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void flushMemoryCache() {
        if (mMemoryCache != null)
            mMemoryCache.clearCache();
    }

    @Override
    public void onLowMemoryReceived() {
        flush();
    }

    public void initFileCache(Context context) {
        try {
            if (android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED))
                cacheDir = new File(android.os.Environment
                        .getExternalStorageDirectory().getAbsoluteFile()
                        + "/JASKindle");
            else
                cacheDir = context.getCacheDir();
            if (!cacheDir.exists())
                cacheDir.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;
    }

    public Bitmap decodeFile(File f) {
        Bitmap b = null;
        try {
            b = ShrinkBitmap(f.getAbsolutePath(), 500, 500);
        } catch (Exception e) {

        }
        return b;
    }

    public Bitmap decodeFile(File file, ImageOptions bmpFactoryOptions) {
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
                bmpFactoryOptions);
        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
                / (float) bmpFactoryOptions.requiredHeight);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
                / (float) bmpFactoryOptions.requiredWidth);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
                bmpFactoryOptions);
        return bitmap;
    }

    Bitmap ShrinkBitmap(String file, int width, int height) {

        BitmapFactory.Options sDefaultOptions = new BitmapFactory.Options();
        sDefaultOptions.inDither = true;
        sDefaultOptions.inScaled = true;
        sDefaultOptions.inDensity = DisplayMetrics.DENSITY_MEDIUM;
        sDefaultOptions.inTargetDensity = context.getResources()
                .getDisplayMetrics().densityDpi;

        sDefaultOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, sDefaultOptions);

        int heightRatio = (int) Math.ceil(sDefaultOptions.outHeight
                / (float) height);
        int widthRatio = (int) Math.ceil(sDefaultOptions.outWidth
                / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                sDefaultOptions.inSampleSize = heightRatio;
            } else {
                sDefaultOptions.inSampleSize = widthRatio;
            }
        }

        sDefaultOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, sDefaultOptions);
        return bitmap;
    }

    void putfile(Bitmap bmp, String filename) {
        try {
            File f = new File(cacheDir.getAbsolutePath() + "/" + filename);
            if (f.exists())
                f.delete();
            else
                f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
