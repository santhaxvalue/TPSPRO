package com.panelManagement.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.panelManagement.activity.R;
import com.panelManagement.listener.OnRegionsListener;
import com.panelManagement.model.CountryModel;
import com.panelManagement.model.UserInfoModel;
import com.panelManagement.socialsView.SearchListAdaptor;
import com.panelManagement.webservices.AsyncHttpRequest;
import com.panelManagement.webservices.AsyncHttpRequest.RequestListener;

import java.util.ArrayList;

public class AlertCountryFragment extends DialogFragment implements OnClickListener, TextWatcher, OnItemClickListener, RequestListener {
    public static final int COUNTRY = 1;
    public static final int CITY = 2;
    OnRegionsListener listener;
    ArrayList<CountryModel> mSearchList;
    SearchListAdaptor mListAdapter;
    ListView listview;
    EditText editText;
    ProgressBar progressbar;
    AsyncHttpRequest mAppRequest;
    private UserInfoModel model;

    public AlertCountryFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public AlertCountryFragment(OnRegionsListener listener, UserInfoModel model) {
        this.listener = listener;
        this.model = model;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchList = new ArrayList<CountryModel>();
        mSearchList.addAll(model.getCityList());
        mListAdapter = new SearchListAdaptor(getActivity(), mSearchList);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.country_search, container, false);
        rootView.findViewById(R.id.country_search_cancel_button).setOnClickListener(this);
        rootView.findViewById(R.id.search_cross).setOnClickListener(this);
        progressbar = rootView.findViewById(R.id.ic_progressbar);
        progressbar.setVisibility(View.INVISIBLE);
        TextView titleText = rootView.findViewById(R.id.ic_coutry_title);
        listview = rootView.findViewById(R.id.country_list_view);
        listview.setAdapter(mListAdapter);
        listview.setOnItemClickListener(this);
        editText = rootView.findViewById(R.id.country_search_auto_complete_edittext);
        editText.addTextChangedListener(this);
        titleText.setText(getString(R.string.message_city));
        return rootView;
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.country_search_cancel_button:
                dismiss();
                break;
            case R.id.search_cross:
                editText.setText("");
                mListAdapter.getFilter().filter("");
                break;
        }
    }

    @Override
    public void afterTextChanged(Editable arg0) {
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        mListAdapter.getFilter().filter(arg0);
    }

    protected synchronized void requestTypeGET(String url, int requestCode) {
        mAppRequest = new AsyncHttpRequest(getActivity(), url, null, requestCode, AsyncHttpRequest.Type.GET);
        mAppRequest.setRequestListener(this);
        mAppRequest.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
        if (isVisible()) {
            dismiss();
        }
        CountryModel value = (CountryModel) adapter.getItemAtPosition(position);
        listener.onClickRegionItem(value);

    }

    @Override
    public void onRequestCompleted(String response, int requestCode) {
        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestError(Exception e, int requestCode) {
        if (progressbar != null)
            progressbar.setVisibility(View.INVISIBLE);
        // Utility.showToast(getActivity(), e.getMessage());
    }

    @Override
    public void onRequestStarted(int requestCode) {
        if (progressbar != null)
            progressbar.setVisibility(View.VISIBLE);
    }
}