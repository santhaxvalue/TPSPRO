package com.panelManagement.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.model.LocaleModel;
import com.panelManagement.utils.Utility;

import java.util.ArrayList;

public class SettingsListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<LocaleModel> mLocaleList = new ArrayList<LocaleModel>();
    private ViewHolder viewHolder;
    private ListView mListView;
    private Typeface mRegularTypeFace;

    public SettingsListAdapter(Context context, ListView listView) {
        mContext = context;

        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = listView;
        this.mLocaleList = new ArrayList<LocaleModel>();
        mRegularTypeFace = Utility.getTypeface(mContext, true);
    }

    public void updateList(ArrayList<LocaleModel> result) {
        this.mLocaleList = result;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return mLocaleList.size();
    }

    @Override
    public Object getItem(int position) {

        return mLocaleList.get(position);
    }

    @Override
    public long getItemId(int arg0) {

        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_change_language,
                    null);
            viewHolder = new ViewHolder();

            viewHolder.tv_localeName = (TextView) convertView
                    .findViewById(R.id.tv_locale_name);

            viewHolder.cb_isSelected = (CheckBox) convertView.findViewById(R.id.cb_locale);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        convertView.setTag(viewHolder);
        convertView.setTag(R.id.tv_locale_name, viewHolder.tv_localeName);
        convertView.setTag(R.id.cb_locale, viewHolder.cb_isSelected);
        viewHolder.tv_localeName.setTypeface(mRegularTypeFace);

        viewHolder.tv_localeName.setText(mLocaleList.get(position)
                .getLocalename());
        /*
		 * if (mLocaleList.get(position).getLocaleid() == selectedLocaleId) {
		 * viewHolder.cb_isSelected.setChecked(true); } else
		 */
        viewHolder.cb_isSelected.setChecked(mListView.isItemChecked(position));

        // mListView.smoothScrollToPosition(10);

        return convertView;
    }

    public static class ViewHolder {

        TextView tv_localeName;
        CheckBox cb_isSelected;

    }

}
