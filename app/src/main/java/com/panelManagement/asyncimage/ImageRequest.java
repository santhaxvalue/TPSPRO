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

import com.panelManagement.asyncimage.ImageLoader.ImageLoaderCallback;

import java.util.concurrent.Future;

/**
 * An {@link ImageRequest} may be used to request an image from the network. The
 * process of requesting for an image is done in three steps:
 * <ul>
 * <li>Instantiate a new {@link ImageRequest}</li>
 * <li>Call {@link #load(Context)} to start loading the image</li>
 * <li>Listen to loading state changes using a {@link ImageRequestCallback}</li>
 * </ul>
 *
 * @author Cyril Mottier
 */
public class ImageRequest {

    private static ImageLoader sImageLoader;
    private Future<?> mFuture;
    private String mUrl;
    private ImageRequestCallback mCallback;
    private ImageProcessor mBitmapProcessor;
    private ImageOptions mOptions;
    private ImageCache mCache = null;
    public ImageRequest(String url, ImageRequestCallback callback) {
        this(url, callback, null);
    }

    public ImageRequest(String url, ImageRequestCallback callback,
                        ImageProcessor bitmapProcessor) {
        this(url, callback, bitmapProcessor, null, null);
    }

    public ImageRequest(String url, ImageRequestCallback callback,
                        ImageProcessor bitmapProcessor, ImageOptions options,
                        ImageCache cache) {
        mUrl = url;
        mCallback = callback;
        mBitmapProcessor = bitmapProcessor;
        mOptions = options;
        mCache = cache;
    }

    public void setImageRequestCallback(ImageRequestCallback callback) {
        mCallback = callback;
    }

    public String getUrl() {
        return mUrl;
    }

    public void load(Context context) {
        if (mFuture == null) {
            if (sImageLoader == null) {
                sImageLoader = new ImageLoader(context);
            }
            mFuture = sImageLoader.loadImage(mUrl, new InnerCallback(),
                    mBitmapProcessor, mOptions, mCache);
        }
    }

    public void cancel() {
        if (!isCancelled()) {
            // Here we do not want to force the task to be interrupted. Indeed,
            // it may be useful to keep the result in a cache for a further use
            mFuture.cancel(false);
            if (mCallback != null) {
                mCallback.onImageRequestCancelled(this);
            }
        }
    }

    public final boolean isCancelled() {
        return mFuture.isCancelled();
    }

    /**
     * @author Cyril Mottier
     */
    public static interface ImageRequestCallback {

        /**
         * Callback to be invoked when the request processing started.
         *
         * @param request The ImageRequest that started
         */
        void onImageRequestStarted(ImageRequest request);

        /**
         * Callback to be invoked when the request processing failed.
         *
         * @param request   ImageRequest that failed
         * @param throwable The Throwable that occurs
         */
        void onImageRequestFailed(ImageRequest request, Throwable throwable);

        /**
         * Callback to be invoked when the request processing ended.
         *
         * @param request ImageRequest that ended
         * @param image   The resulting Bitmap
         */
        void onImageRequestEnded(ImageRequest request, Bitmap image);

        /**
         * Callback to be invoked when the request processing has been
         * cancelled.
         *
         * @param request ImageRequest that has been cancelled
         */
        void onImageRequestCancelled(ImageRequest request);
    }

    private class InnerCallback implements ImageLoaderCallback {

        @Override
        public void onImageLoadingStarted(ImageLoader loader) {
            if (mCallback != null) {
                mCallback.onImageRequestStarted(ImageRequest.this);
            }
        }

        @Override
        public void onImageLoadingEnded(ImageLoader loader, Bitmap bitmap) {
            if (mCallback != null && !isCancelled()) {
                mCallback.onImageRequestEnded(ImageRequest.this, bitmap);
            }
            mFuture = null;
        }

        @Override
        public void onImageLoadingFailed(ImageLoader loader, Throwable exception) {
            if (mCallback != null && !isCancelled()) {
                mCallback.onImageRequestFailed(ImageRequest.this, exception);
            }
            mFuture = null;
        }
    }

}
