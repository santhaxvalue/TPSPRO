package com.panelManagement.activity;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Centura on 30/08/18.
 */

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static float MIN_SCALE = 1f;
    private static final float Right_ALPHA = 0.9f;
    private static final float Left_ALPHA = 0.5f;
    private static float small =  (float)0.7;

    public ZoomOutPageTransformer(boolean isZoomEnable) {
        if (isZoomEnable) {
            MIN_SCALE = 0.85f;
        } else {
            MIN_SCALE = 1f;
        }
    }

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        float vertMargin = pageHeight * (1 - MIN_SCALE) / 2;
        float horzMargin = pageWidth * (1 - MIN_SCALE) / 2;
        //view.setScaleX(MIN_SCALE);
        //view.setScaleY(MIN_SCALE);
        if (position <= -0.5) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(Right_ALPHA);
            //view.setTranslationX(horzMargin - vertMargin / 2);
//            view.setScaleX(small);
//            view.setScaleY(small);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                view.setTranslationZ(small);
//            }


        } else if (position > -0.5 && position < 0.5 ) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            vertMargin = pageHeight * (1 - scaleFactor) / 2;
            horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                //view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                //view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            view.bringToFront();
            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(1);
            view.setScaleY(1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setTranslationZ(6);
            }
            // Fade the page relative to its size.
            view.setAlpha(1);
            view.bringToFront();
            final ViewGroup parent = (ViewGroup)view.getParent();
            if (null != parent) {
                view.requestLayout();
                view.invalidate();
                parent.invalidate();
            }

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(Right_ALPHA);

            //view.setTranslationX(-horzMargin + vertMargin / 2);
//            view.setScaleX(small);
//            view.setScaleY(small);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                view.setTranslationZ(small);
//            }

        }
    }
}