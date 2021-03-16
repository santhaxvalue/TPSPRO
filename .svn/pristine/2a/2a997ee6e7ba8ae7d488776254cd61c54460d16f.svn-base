package com.panelManagement.socialsView;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.model.CountryModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchListAdaptor extends ArrayAdapter<CountryModel> implements Filterable {
    private final Object lock = new Object();
    Handler mHandler = new Handler();
    private ArrayList<CountryModel> items;
    private Context context;
    private ArrayList<CountryModel> originalValues;
    private ClientFilter clientFilter;

    public SearchListAdaptor(Context context, ArrayList<CountryModel> items) {
        super(context, R.layout.searchlist_item, items);
        this.items = items;
        this.context = context;
    }

    @Override
    public CountryModel getItem(int position) {
        return items.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        final ViewWrapper viewWrapper;
        final CountryModel profile = items.get(position);
        if (view == null) {
            view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.searchlist_item, null);
            viewWrapper = new ViewWrapper(view);
            view.setTag(viewWrapper);
        } else {
            viewWrapper = (ViewWrapper) view.getTag();
        }
        viewWrapper.getName().setText(profile.getCityDisplayname());
        // ImageView tickedImage = viewWrapper.getTick();
        // if(profile.isChecked())
        // tickedImage.setVisibility(View.VISIBLE);
        // else
        // tickedImage.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (clientFilter == null) {
            clientFilter = new ClientFilter();
        }
        return clientFilter;
    }

    @Override
    public void notifyDataSetChanged() {
        sortAssendingOrders();
        super.notifyDataSetChanged();
    }

    private void sortDecendingOrders() {
        Collections.sort(this.items, new Comparator<CountryModel>() {
            @Override
            public int compare(CountryModel lhs, CountryModel rhs) {
                return rhs.getCityDisplayname().compareToIgnoreCase(lhs.getCityDisplayname());
            }
        });
    }

    private void sortAssendingOrders() {
        Collections.sort(this.items, new Comparator<CountryModel>() {
            @Override
            public int compare(CountryModel lhs, CountryModel rhs) {
                return lhs.getCityDisplayname().compareToIgnoreCase(rhs.getCityDisplayname());
            }
        });
    }

    class ViewWrapper {
        TextView name;
        View base;

        ViewWrapper(View base) {
            this.base = base;
        }

        public TextView getName() {
            if (name == null)
                name = base.findViewById(R.id.ic_searchlist_name);
            return name;
        }
    }

    private class ClientFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (originalValues == null) {
                synchronized (lock) {
                    originalValues = new ArrayList<CountryModel>(items);
                }
            }
            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    ArrayList<CountryModel> list = new ArrayList<CountryModel>(originalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                String prefixString = prefix.toString().toLowerCase();
                final ArrayList<CountryModel> values = originalValues;
                final int count = values.size();
                final ArrayList<CountryModel> newValues = new ArrayList<CountryModel>(count);
                for (int i = 0; i < count; i++) {
                    final CountryModel value = values.get(i);
                    final String nameText = value.getCityDisplayname().toLowerCase();
                    if (nameText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = nameText.split(" ");
                        final int wordCount = words.length;
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items = (ArrayList<CountryModel>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
