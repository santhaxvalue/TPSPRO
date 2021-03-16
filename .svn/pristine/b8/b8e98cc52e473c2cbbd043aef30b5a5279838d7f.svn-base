package com.panelManagement.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.panelManagement.activity.R;
import com.panelManagement.listener.LocaleChangeListener;
import com.panelManagement.model.ChangeLanguageModel;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.AsyncHttpRequest;

import java.util.ArrayList;

public class ChangeLanguageListAdapter extends ArrayAdapter<ChangeLanguageModel> {

    public LocaleChangeListener callback;
    // private Context context;
    Activity mActivity;
    Handler mHandler = new Handler();
    private ArrayList<ChangeLanguageModel> items;
    private int filterCount = 0;

    public ChangeLanguageListAdapter(Activity mActivity, ArrayList<ChangeLanguageModel> value, LocaleChangeListener listener) {
        super(mActivity, R.layout.list_change_language, value);
        this.items = value;
        this.callback = listener;
        this.mActivity = mActivity;
    }

    @Override
    public ChangeLanguageModel getItem(int position) {
        return items.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        final ViewWrapper viewWrapper;
        final ChangeLanguageModel profile = items.get(position);
        if (view == null) {
            view = ((LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_change_language, null);
            viewWrapper = new ViewWrapper(view);
            view.setTag(viewWrapper);
        } else {
            viewWrapper = (ViewWrapper) view.getTag();
        }

        viewWrapper.getCountry().setText(profile.getName());
        if (profile.isChecked()) {
            viewWrapper.getCheckedBox().setBackgroundResource(R.drawable.ic_checkbox_blue);
        } else {
            viewWrapper.getCheckedBox().setBackgroundResource(R.drawable.ic_checkbox_gray);
        }

        viewWrapper.getCheckedBox().setOnClickListener((View v) -> {

            if (!AsyncHttpRequest.isConnected(mActivity)) {
                Dialog dialog = Utility.MandatoryalertDialog(getContext(), mActivity.getResources().getString(R.string.async_task_error));
                dialog.show();
                return;
            }
            profile.setChecked(true);
            notifyDataSetChanged();
            for (int i = 0; i < items.size(); i++) {
                ChangeLanguageModel reversedData = items.get(i);
                if (reversedData.isChecked() && position != i) {
                    reversedData.setChecked(false);
                }
            }
            notifyDataSetChanged();
            if (callback != null) {
                callback.setLocaleChange(true, profile.getLocaleCode());
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public void notifyDataSetChanged() {

        super.notifyDataSetChanged();
    }

    class ViewWrapper {
        TextView txtCountry;
        View base;
        CheckBox chbox;
        RelativeLayout rl_Backgound;

        ViewWrapper(View base) {
            this.base = base;
        }

        public TextView getCountry() {
            if (txtCountry == null)
                txtCountry = base.findViewById(R.id.tv_locale_name);
            return txtCountry;
        }

        CheckBox getCheckedBox() {
            if (chbox == null)
                chbox = base.findViewById(R.id.cb_locale);
            return chbox;
        }
    }
}
