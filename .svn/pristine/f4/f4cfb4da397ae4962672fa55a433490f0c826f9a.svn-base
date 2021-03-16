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
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.panelManagement.activity.TPSApplication;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * An ImageLoader asynchronously loads image from a given url. Client may be
 * notified from the current image loading state using the
 * {@link ImageLoaderCallback}.
 * <p>
 * <em><strong>Note: </strong>You normally don't need to use the {@link ImageLoader}
 * class directly in your application. You'll generally prefer using an
 * {@link ImageRequest} that takes care of the entire loading process.</em>
 * </p>
 *
 * @author Cyril Mottier
 */
public class ImageLoader {

    // private static final String LOG_TAG = ImageLoader.class.getSimpleName();

    private static final int ON_START = 0x100;
    private static final int ON_FAIL = 0x101;
    private static final int ON_END = 0x102;
    public static BitmapFactory.Options sDefaultOptions;
    private static ImageCache sImageCache;
    private static ExecutorService sExecutor;
    private static AssetManager sAssetManager;
    public ImageLoader(Context context) {
        TPSApplication newsApp = (TPSApplication) context
                .getApplicationContext();

        if (sImageCache == null) {
            sImageCache = newsApp.getImageCache();
        }
        if (sExecutor == null) {
            sExecutor = newsApp.getExecutor();
        }
        if (sDefaultOptions == null) {
            sDefaultOptions = new BitmapFactory.Options();
            sDefaultOptions.inDither = true;
            sDefaultOptions.inScaled = true;
            sDefaultOptions.inDensity = DisplayMetrics.DENSITY_MEDIUM;
            sDefaultOptions.inTargetDensity = context.getResources()
                    .getDisplayMetrics().densityDpi;
        }
        sAssetManager = context.getAssets();
    }

    public Future<?> loadImage(String url, ImageLoaderCallback callback,
                               ImageProcessor bitmapProcessor, ImageOptions options,
                               ImageCache cache) {
        return sExecutor.submit(new ImageFetcher(url, callback,
                bitmapProcessor, options, cache));
    }

    Bitmap ShrinkBitmap(String file, ImageOptions bmpFactoryOptions) {
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

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
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }

    /**
     * @author Cyril Mottier
     */
    public static interface ImageLoaderCallback {

        void onImageLoadingStarted(ImageLoader loader);

        void onImageLoadingEnded(ImageLoader loader, Bitmap bitmap);

        void onImageLoadingFailed(ImageLoader loader, Throwable exception);
    }

    private class ImageFetcher implements Runnable {

        private String mUrl;
        private ImageHandler mHandler;
        private ImageProcessor mBitmapProcessor;
        private ImageOptions mOptions;
        private ImageCache mCache = null;

        public ImageFetcher(String url, ImageLoaderCallback callback,
                            ImageProcessor bitmapProcessor, ImageOptions options,
                            ImageCache cache) {
            mUrl = url;
            mHandler = new ImageHandler(url, callback);
            mBitmapProcessor = bitmapProcessor;
            mOptions = options;
            mCache = cache;
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            final Handler h = mHandler;
            Bitmap bitmap = null;
            Throwable throwable = null;
            h.sendMessage(Message.obtain(h, ON_START));
            try {

                if (TextUtils.isEmpty(mUrl)) {
                    throw new Exception("The given URL cannot be null or empty");
                }

                InputStream inputStream = null;

                if (mUrl.startsWith("file:///android_asset/")) {
                    inputStream = sAssetManager.open(mUrl.replaceFirst(
                            "file:///android_asset/", ""));
                } else {
                    inputStream = new URL(mUrl).openStream();
                }

                File f = mCache.getFile(mUrl);
                if (!f.exists())
                    f.delete();
                InputStream input = new BufferedInputStream(inputStream);
                OutputStream output = new FileOutputStream(f);
                int total = 0;
                byte data[] = new byte[1024];
                int count = 0;
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                    total += count;
                }

                output.flush();
                output.close();
                input.close();

                bitmap = ShrinkBitmap(f.getAbsolutePath(), mOptions);
                if (mBitmapProcessor != null && bitmap != null) {
                    final Bitmap processedBitmap = mBitmapProcessor
                            .processImage(bitmap);
                    if (processedBitmap != null) {
                        bitmap = processedBitmap;
                    }
                }

            } catch (Exception e) {
                throwable = e;
            }

            if (bitmap == null) {
                if (throwable == null) {
                    // Skia returned a null bitmap ... that's usually because
                    // the given url wasn't pointing to a valid image
                    throwable = new Exception("Skia image decoding failed");
                }
                h.sendMessage(Message.obtain(h, ON_FAIL, throwable));
            } else {
                h.sendMessage(Message.obtain(h, ON_END, bitmap));
            }
        }
    }

    private class ImageHandler extends Handler {

        private ImageLoaderCallback mCallback;

        private ImageHandler(String url, ImageLoaderCallback callback) {
            mCallback = callback;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case ON_START:
                    if (mCallback != null) {
                        mCallback.onImageLoadingStarted(ImageLoader.this);
                    }
                    break;

                case ON_FAIL:
                    if (mCallback != null) {
                        mCallback.onImageLoadingFailed(ImageLoader.this,
                                (Throwable) msg.obj);
                    }
                    break;

                case ON_END:

                    final Bitmap bitmap = (Bitmap) msg.obj;
                    if (mCallback != null) {
                        mCallback.onImageLoadingEnded(ImageLoader.this, bitmap);
                    }
                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }
        }

        ;
    }

}
