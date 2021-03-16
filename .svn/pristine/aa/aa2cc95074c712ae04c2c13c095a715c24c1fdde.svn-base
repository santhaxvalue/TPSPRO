package com.panelManagement.view;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.panelManagement.activity.R;

/**
 * Created by Centura Technology on 1/24/2018.
 */

public class FabAdapter extends BaseAdapter {

    private Context mContext;
    private final String[] web;
    private final int[] Imageid;

    public FabAdapter(Context mContext, int[] imageid) {
        this.mContext = mContext;
        this.web = mContext.getResources().getStringArray(R.array.home);
        Imageid = imageid;
    }

    @Override
    public int getCount() {
        return web.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = inflater.inflate(R.layout.fab_layout, null);
            TextView tv_menu_name = grid.findViewById(R.id.fab_grid_txt);
            ImageView iv_menu_image = grid.findViewById(R.id.fab_grid_img);
            tv_menu_name.setText(web[position]);
            iv_menu_image.setImageResource(Imageid[position]);
        } else {
            grid = convertView;
        }

        return grid;
    }
}
