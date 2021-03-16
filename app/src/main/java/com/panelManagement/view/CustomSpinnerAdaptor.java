package com.panelManagement.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.model.AnswerChoiceModel;

import java.util.ArrayList;

public class CustomSpinnerAdaptor extends ArrayAdapter<AnswerChoiceModel> {
    ArrayList<AnswerChoiceModel> data = null;
    private Activity context;

    public CustomSpinnerAdaptor(Activity context, int resource, ArrayList<AnswerChoiceModel> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {   // Ordinary view in Spinner, we use android.R.layout.simple_spinner_item
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {   // This view starts when we click the spinner.
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_item, parent, false);
        }

        AnswerChoiceModel item = data.get(position);

        if (item != null) {   // Parse the data from each object and set it.
            TextView myCountry = (TextView) row.findViewById(R.id.countryName);
            if (myCountry != null)
                myCountry.setText(item.getAnswerChoiceText());

        }

        return row;
    }
}