package com.panelManagement.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.panelManagement.fragment.LoginFragment;
import com.panelManagement.fragment.SocialSignupFragment;

/**
 * Created by Centura Technology on 2/7/2018.
 */

public class VerticalCarouselAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private int pos;
    private String email;
    private String message;

    public VerticalCarouselAdapter(Context context, FragmentManager fm, String email, String message) {
        super(fm);
        this.mContext = context;
        this.email = email;
        this.message = message;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 1:
                pos = position;
                fragment = LoginFragment.newInstance(position, email, message);
                break;
            case 0:
                pos = position;
                fragment = SocialSignupFragment.newInstance(position);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    public int getCurrentPosition() {
        return this.pos;
    }
}
