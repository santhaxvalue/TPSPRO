package com.panelManagement.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.panelManagement.activity.R;
import com.panelManagement.utils.CircleTransform;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Centura Technology on 2/22/2018.
 */

public class ImageSliderAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<String> images;
    Context context;
    ImageView imageView;

   public ImageSliderAdapter(Context mContext, ArrayList<String> ints) {
        this.context = mContext;
        this.images = ints;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_layout, container, false);
        imageView = view.findViewById(R.id.img_slide);
        Picasso.get().load(images.get(position)).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).placeholder(R.drawable.ic_profile)
                .networkPolicy(NetworkPolicy.NO_CACHE).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
