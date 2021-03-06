package com.panelManagement.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.view.ExpandableListAdapter;
import com.panelManagement.webservices.ParseJSonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FAQFragment extends BaseFragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<String> listDataHeader, listID, listFaqCategoryId, child;
    HashMap<String, List<String>> listDataChild;
    RelativeLayout relativeLayout;
    Context context;

    public FAQFragment() {
        // Required empty public constructor
    }


    public static FAQFragment newInstance() {
        FAQFragment fragment = new FAQFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faq, container, false);
        context = this.getContext();
        getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);
        getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
        getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
        FrameLayout layout = getActivity().findViewById(R.id.main_container_fragment);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        layout.setLayoutParams(params);
        HomeActivity.setSurvayBackground();

        // get the listview
        expListView = view.findViewById(R.id.lvExp);
        relativeLayout = view.findViewById(R.id.layout_faq_main);

        listDataHeader = new ArrayList<>();
        listID = new ArrayList<>();
        listFaqCategoryId = new ArrayList<>();
        listDataChild = new HashMap<>();
        child = new ArrayList<>();

        showDialog(true, getString(R.string.dialog_login));
        requestTypePost(Constants.API_GETFAQINFO, new ParseJSonObject(context).getFaqObject(InformatePreferences.getStringPrefrence(context, Constants.PREF_COUNTRYCODE))
                , Constants.REQUEST_FAQ_LIST);

        // preparing list data
        //prepareListData();


        /*expListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));*/

        Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        hide.setDuration(1000);
        relativeLayout.setAnimation(hide);

        return view;
    }

    /*@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expListView.setIndicatorBoundsRelative(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
        }

    }*/

    @Override
    public void vLayout(String res, int requestcode) {
        switch (requestcode) {

            case Constants.REQUEST_FAQ_LIST:
                try {
                    JSONObject object = new JSONObject(res);

                    if (object.getBoolean("Status")) {

                        JSONArray ques = object.getJSONArray("lstFAQ");
                        if (ques != null && ques.length() != 0) {
                            JSONObject object1;
                            for (int i = 0; i < ques.length(); i++) {
                                object1 = ques.getJSONObject(i);
                                listDataHeader.add(object1.getString("Question"));
                                child.add(object1.getString("Answer"));
                                listDataChild.put(object1.getString("Question"), Collections.singletonList(object1.getString("Answer")));
                                listID.add(object1.getString("ID"));
                                listFaqCategoryId.add(object1.getString("FAQCategoryId"));
                            }
                            listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);

                            // setting list adapter
                            expListView.setAdapter(listAdapter);
                        }

                    } else {
                        showErrorAlert("", object.getString("Message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void rError(int requestcode) {
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getContext().getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
}
