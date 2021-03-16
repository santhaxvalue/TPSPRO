package com.panelManagement.asyncimage;

import android.content.Context;
import android.graphics.BitmapFactory;

public class ImageOptions extends BitmapFactory.Options {
    public int requiredWidth = 500;
    public int requiredHeight = 500;

    public static ImageOptions getProfilePicImageOptions(Context mContext) {
        ImageOptions options = new ImageOptions();
        options = new ImageOptions();
        options.inDither = true;
        options.inScaled = true;
        options.requiredHeight = AsyncImageView.dipToPix(
                mContext.getResources(), 100);
        options.requiredWidth = AsyncImageView.dipToPix(
                mContext.getResources(), 100);
        options.inTargetDensity = mContext.getResources().getDisplayMetrics().densityDpi;
        return options;
    }

    public static ImageOptions getProductPicImageOptions(Context mContext) {
        ImageOptions options = new ImageOptions();
        options = new ImageOptions();
        options.inDither = true;
        options.inScaled = true;
        options.requiredHeight = AsyncImageView.dipToPix(
                mContext.getResources(), 200);
        options.requiredWidth = AsyncImageView.dipToPix(
                mContext.getResources(), 200);
        options.inTargetDensity = mContext.getResources().getDisplayMetrics().densityDpi;
        return options;
    }

    public static ImageOptions getFullProductPicImageOptions(Context mContext) {
        ImageOptions options = new ImageOptions();
        options = new ImageOptions();
        options.inDither = true;
        options.inScaled = true;
        options.requiredHeight = 1000;
        options.requiredWidth = 1000;
        options.inTargetDensity = mContext.getResources().getDisplayMetrics().densityDpi;
        return options;
    }

}
