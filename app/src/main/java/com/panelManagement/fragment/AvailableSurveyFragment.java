package com.panelManagement.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.activity.SurveyViewActivity;
import com.panelManagement.model.RewardPointsModels;
import com.panelManagement.model.SurveyModels;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.PanelConstants;
import com.panelManagement.utils.Utility;
import com.panelManagement.view.ImageSliderAdapter;
import com.panelManagement.view.SurveyListAdapter;
import com.panelManagement.webservices.ParseJSonObject;
import com.panelManagement.widgets.WrapContentViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PagerContainer;

import static com.panelManagement.activity.HomeActivity.MYFRAGMENTKEY;

public class AvailableSurveyFragment extends BaseFragment implements View.OnClickListener {

    public static final String IMAGEVIEW_TAG = "drag_drop";
    WrapContentViewPager lv_survey_list;
    View update_profile;
    SurveyListAdapter adaptorSurvey;
    ArrayList<SurveyModels> surveyarray;
    RelativeLayout available_survey_layout;
    LinearLayout background_layout;
    private TabLayout tl_dots;
    ViewPager bottom_slider, overlap_pager;
    ScrollView svSurvey;
    View cvDragDrop;
    PagerContainer pagerContainer;
    ImageButton next, previous;
    Context context;
    ImageView dropImg, targetImg;
    private TextView availableSurveyCount;
    private ImageSliderAdapter myViewPagerAdapter;
    private TextView txt_points_redeemed;
    public static TextView  txt_points_inreview_available;
    private ArrayList<String> imageUrls = new ArrayList<>();
    private Button mQuickPoll;
    private ShimmerFrameLayout mShimmerViewContainer;
    private ShimmerFrameLayout mShimmerPointsViewContainer;
    private CardView mSurveySlider;
    private boolean flag;

    public static AvailableSurveyFragment newInstance() {

        Bundle args = new Bundle();
        AvailableSurveyFragment fragment = new AvailableSurveyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        context = getContext();
        surveyarray = new ArrayList<>();
        adaptorSurvey = new SurveyListAdapter(getActivity(), surveyarray, new SurveyListAdapter.OnPuzzleCompleted() {
            @Override
            public void onPuzzleCompleted() {
                requestMatchThePuzzleAPI(); // new api integration
            }
        });
    }

    private void requestMatchThePuzzleAPI() {
        Intent intent = new Intent(context, SurveyViewActivity.class);
        String url = Utility.getUrlForPuzzle(Constants.API_MATCH_THE_PUZZLE_STAGE
                , InformatePreferences.getStringPrefrence(context, Constants.PREF_ID)
                , InformatePreferences.getStringPrefrence(context, Constants.PREF_LOCALECODE));

        Log.d("urlnew:","urlnew:"+url);
        Log.d("urlnew:","urlnew:"+InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
        Log.d("urlnew:","urlnew:"+InformatePreferences.getStringPrefrence(context, Constants.PREF_LOCALECODE));

        intent.putExtra(SurveyViewFragment.SURVEY_URL_KEY, url);
        ((Activity) context).startActivityForResult(intent, ProfilerFragment.RESULT_PROFILER);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_available_survey, container, false);
        getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.VISIBLE);
        //getActivity().findViewById(R.id.header_points_container).setVisibility(View.INVISIBLE);

        getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_pointsinrejected).setVisibility(View.GONE);
        getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
        FrameLayout layout = getActivity().findViewById(R.id.main_container_fragment);
        Utility.setMargins(context, layout, Utility.getDp(context, 36));
        HomeActivity.setSurvayBackground();

        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        /*mShimmerPointsViewContainer =  getActivity().findViewById(R.id.shimmer_redeem_view_container);
        mShimmerPointsViewContainer.setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.header_points_container).setVisibility(View.INVISIBLE);*/


        next = view.findViewById(R.id.next_img_btn);
        next.setOnClickListener(this);
        previous = view.findViewById(R.id.prev_img_btn);
        tl_dots = view.findViewById(R.id.tl_dots);
        overlap_pager = view.findViewById(R.id.overlap_pager);
        previous.setOnClickListener(this);

        bottom_slider = view.findViewById(R.id.bottom_slider);
        mSurveySlider = view.findViewById(R.id.survey_slider);

        bottom_slider.setOffscreenPageLimit(3);
        availableSurveyCount = view.findViewById(R.id.available_survey_count);
        txt_points_redeemed = getActivity().findViewById(R.id.txt_points_redeemed_available);
        txt_points_inreview_available = getActivity().findViewById(R.id.txt_points_inreview_available);
        txt_points_redeemed.setText("0");
        txt_points_inreview_available.setText("0");
        cvDragDrop = view.findViewById(R.id.cv_puzzle);
        lv_survey_list = view.findViewById(R.id.lv_survey_list);
        background_layout = view.findViewById(R.id.background_layout);
        update_profile = view.findViewById(R.id.update_profile);

        //mQuickPoll = view.findViewById(R.id.btn_quickpoll);
        //mQuickPoll.setOnClickListener(this);

        pagerContainer = view.findViewById(R.id.pager_container);

        tl_dots.setupWithViewPager(overlap_pager);
        if (isAdded())
            if (!InformatePreferences.getBoolean(context, Constants.PREF_IS_CONSENT_GIVEN, false)) {
                _checkConsentData();
            } else {
                request();
            }
        if (InformatePreferences.getInt(context, Constants.PREF_PROFILERCOMPLETE) != 100) {
            pagerContainer.setVisibility(View.GONE);
            tl_dots.setVisibility(View.GONE);
            background_layout.setVisibility(View.GONE);
            flag = true;
            update_profile.setVisibility(View.GONE);
        } else {
            pagerContainer.setVisibility(View.VISIBLE);
            tl_dots.setVisibility(View.VISIBLE);
            background_layout.setVisibility(View.VISIBLE);
            update_profile.setVisibility(View.GONE);
        }
        view.findViewById(R.id.update_profile_btn).setOnClickListener(
                v -> ((HomeActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container_fragment, ProfilerFragment.newInstance()).addToBackStack(MYFRAGMENTKEY).commit()
        );

        dropImg = view.findViewById(R.id.drop_img);
        targetImg = view.findViewById(R.id.target_img);

        dropImg.setTag(IMAGEVIEW_TAG);

        dropImg.setOnTouchListener(new MyTouchListener());

        view.findViewById(R.id.target_layout).setOnDragListener(new MyDragListener());


        available_survey_layout = view.findViewById(R.id.available_survey_layout);


        return view;
    }

    public void autoSwipeBanner() {
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            int currentPage = bottom_slider.getCurrentItem();
            if (currentPage == myViewPagerAdapter.getCount() - 1) {
                currentPage = -1;
            }
            bottom_slider.setCurrentItem(currentPage + 1, true);
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 0, 5000);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProfilerFragment.RESULT_PROFILER) {
            if (isAdded())
                request();
        }
    }

    private void request() {
        mShimmerViewContainer.startShimmer();
       /* mShimmerPointsViewContainer.startShimmer();
        mShimmerPointsViewContainer.setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.header_points_container).setVisibility(View.INVISIBLE);*/
        //showDialog(true, getString(R.string.dialog_login));
        requestTypePost(Constants.PANLISTAVAILABLESURVEYDATATABLE, sessionObject(), Constants.REQUESTCODE_SIGNIN);
    }


    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
       /* mShimmerPointsViewContainer.startShimmer();
        mShimmerPointsViewContainer.setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.header_points_container).setVisibility(View.INVISIBLE);*/
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        mShimmerViewContainer.setVisibility(View.INVISIBLE);
       /* mShimmerPointsViewContainer.stopShimmer();
        mShimmerPointsViewContainer.setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.header_points_container).setVisibility(View.VISIBLE);*/
        super.onPause();
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void vLayout(String res, int requestcode) {
        if (isAdded())
            switch (requestcode) {
                case Constants.REQUESTCODE_CONSENT_LOG_SAVE:
                    try {
                        request();
                        JSONObject responseOB = new JSONObject(res);
                        boolean statusFlag = responseOB.optBoolean("Status");
                        InformatePreferences.putBoolean(context, Constants.PREF_IS_CONSENT_GIVEN, statusFlag);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constants.REQUESTCODE_CONSENT_CHECK:
                    try {
                        JSONObject responseOB = new JSONObject(res);
                        if (responseOB.optBoolean("Status")) {
                            request();
                            Log.e("Consent", responseOB.optString("Message"));
                        } else {
                            _showGDPRConsentForm(context, ((consentOne, consentTwo, consentFinal, dialog) -> {
                                try {
                                    //showDialog(true, getString(R.string.dialog_login));
                                    /*if (dialog != null && dialog.isShowing()) {
                                        dialog.dismiss();
                                    }*/
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("ConsentText1", consentOne);
                                    jsonObject.put("ConsentText2", consentTwo);
                                    jsonObject.put("ConsentGiven", consentFinal);
                                    jsonObject.put("PanelistId", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
                                    jsonObject.put("ConsentVersion", "v1.0");
                                    jsonObject.put("IPAddress", PanelConstants.DEVICE_ID);
                                    jsonObject.put("BrowserVersion", PanelConstants.DEVICE_ID);
                                    jsonObject.put("BrowserExtraInfo", PanelConstants.DEVICE_ID);
                                    //           jsonObject.put("LanguageCulture", InformatePreferences.getStringPrefrence(context,Constants.PREF_LOCALECODE));


                                    requestTypePost(Constants.API_CONSENT_LOG_SAVE, jsonObject, Constants.REQUESTCODE_CONSENT_LOG_SAVE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }));
                            Log.e("Consent", responseOB.optString("Message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constants.REQUEST_UNSUBSCRIBE_USER:
                    try {
                        JSONObject jsonObject;
                        jsonObject = new JSONObject(res);
                        boolean status = jsonObject.optBoolean("Status");
                        String message = jsonObject.optString("Message");
                        if (status) {
                            _showUnsubscribeSuccessfulDialog();
                        } else {
                            showErrorAlert("", message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constants.REQUESTCODE_SIGNIN:
                    try {
                        String surveyCount = "0";
                        int survaycount_int = 0;
                        int percentageOfComplete = 0;
                        Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                        hide.setDuration(1000);
                        available_survey_layout.setVisibility(View.VISIBLE);
                        available_survey_layout.setAnimation(hide);
                        surveyarray.clear();
                        JSONObject jsonObject = new JSONObject(res);
                        JSONArray images = jsonObject.optJSONArray("promotionalImages");
                        if (images != null) {
                            for (int i = 0; i < images.length(); i++) {
                                imageUrls.add(String.valueOf(images.get(i)));
                            }
                            myViewPagerAdapter = new ImageSliderAdapter(context, imageUrls);
                            bottom_slider.setAdapter(myViewPagerAdapter);
                            myViewPagerAdapter.notifyDataSetChanged();
                            mSurveySlider.setVisibility(View.VISIBLE);
                            autoSwipeBanner();
                        }
                        survaycount_int = jsonObject.optInt("SurveyCount");
                        surveyCount = jsonObject.optString("SurveyCount");
                        percentageOfComplete = jsonObject.optInt("PercentageOfComplete");

                        InformatePreferences.putBoolean(getActivity(), Constants.IS_REDEEM_INSTANTLY, jsonObject.optBoolean("IsRedeemInstantly"));

                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_FIRSTNAME, jsonObject.optString("FirstName"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_LASTNAME, jsonObject.optString("LastName"));
                        InformatePreferences.putInt(getActivity(), Constants.PREF_PROFILERCOMPLETE, percentageOfComplete);

                        mSurveySlider.setVisibility(View.VISIBLE);

                        if (survaycount_int > 0) {
                            pagerContainer.setVisibility(View.VISIBLE);
                            tl_dots.setVisibility(View.VISIBLE);
                            background_layout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.available_survey));
                            background_layout.setVisibility(View.VISIBLE);
                            update_profile.setVisibility(View.GONE);
                            cvDragDrop.setVisibility(View.GONE);
                        } else {
                            if (percentageOfComplete != 100) {
                                cvDragDrop.setVisibility(View.GONE);
                                pagerContainer.setVisibility(View.GONE);
                                background_layout.setVisibility(View.GONE);
                                tl_dots.setVisibility(View.GONE);
                                update_profile.setVisibility(View.VISIBLE);
                            } else {
                                cvDragDrop.setVisibility(View.VISIBLE);
                                update_profile.setVisibility(View.GONE);
                                pagerContainer.setVisibility(View.GONE);
                                background_layout.setVisibility(View.GONE);
                                tl_dots.setVisibility(View.GONE);
                            }
                        }

                        availableSurveyCount.setText(getResources().getString(R.string.available_count) + " " + surveyCount);
                        TextView tv_survey_count = getActivity().findViewById(R.id.tv_survey_count);
                        //tv_survey_count.setText(surveyCount);
                        surveyarray.addAll(new ParseJSonObject(getActivity()).getAvailableSurvey(new JSONObject(res)));
                        InformatePreferences.setStringPrefrence(context, Constants.PREF_FB_LINK_SOCIAL_CONNECT, jsonObject.optString("FBLink"));
                        InformatePreferences.setStringPrefrence(context, Constants.PREF_GOOGLELINK_SOCIAL_CONNECT, jsonObject.optString("GoogleLink"));
                        InformatePreferences.setStringPrefrence(context, Constants.PREF_TWITTER_LINK_SOCIAL_CONNECT, jsonObject.optString("TwitterLink"));

                        if (surveyarray.size() > 0) {
                            lap();
                        }
                        adaptorSurvey.notifyDataSetChanged();

                        if(flag)
                            update_profile.setVisibility(View.VISIBLE);
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.INVISIBLE);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    requestTypePost(Constants.GETINCENTIVEDETAILSMOBILE, sessionObject(), Constants.REQUEST_AVAILABLE_POINTS);

                    break;

                case Constants.REQUEST_AVAILABLE_POINTS:
                    try {
                        Log.e("GetIncenAvailableSur",res.toString());
                        RewardPointsModels rewardsPointsData = new ParseJSonObject(getContext()).getRewardsPoints(new JSONObject(res));
                        InformatePreferences.setStringPrefrence(getContext(), Constants.PREF_AVAILABLEPOINTS_, rewardsPointsData.getAvailablePoints());
                        txt_points_redeemed.setText(rewardsPointsData.getAvailablePoints());
                        txt_points_inreview_available.setText(rewardsPointsData.getPointsReview());

                        /*mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.INVISIBLE);
                        mShimmerPointsViewContainer.stopShimmer();
                        mShimmerPointsViewContainer.setVisibility(View.INVISIBLE);
                        getActivity().findViewById(R.id.header_points_container).setVisibility(View.VISIBLE);*/

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case Constants.REQUEST_CODE_SURVEY_COUNT:
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        int surveyCount = jsonObject.optInt("SurveyCount");
                        if (surveyCount > 0) {
                            requestMatchThePuzzleAPI();
                        } else {
                            showErrorAlert("", getString(R.string.txt_no_survey));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
    }

    public JSONObject sessionObject() {
        if (new ParseJSonObject(context).getSessionObject() != null) {
            return new ParseJSonObject(context).getSessionObject();
        } else {
            return new JSONObject();
        }
    }

    @Override
    public void rError(int requestcode) {
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.btn_quickpoll:
//                mListener.onClickQuickPoll();
//                break;

            case R.id.next_img_btn:
                if (imageUrls != null && !imageUrls.isEmpty()) {
                    int currentPage = bottom_slider.getCurrentItem();
                    if (currentPage == myViewPagerAdapter.getCount() - 1) {
                        currentPage = -1;
                    }
                    bottom_slider.setCurrentItem(currentPage + 1, true);
                }
                break;

            case R.id.prev_img_btn:
                if (imageUrls != null && !imageUrls.isEmpty()) {
                    int currentPage1 = bottom_slider.getCurrentItem();
                    if (currentPage1 == 0) {
                        currentPage1 = myViewPagerAdapter.getCount() - 1;
                    }
                    bottom_slider.setCurrentItem(currentPage1 - 1, true);
                }
                break;
        }
    }

    public static final class MyTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            view.performClick();
            // create it from the object's tag
            ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            view.startDrag(data, //data to be dragged
                    shadowBuilder, //drag shadow
                    view,
                    0   //no needed flags
            );
            view.setVisibility(View.INVISIBLE);
            return false;
        }
    }

    class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            View dragView = (View) event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    if (v.getTag().equals("target_position")) {
                        View view = (View) event.getLocalState();
                        if (isAdded())
                            requestMatchThePuzzleAPI();
                        view.setVisibility(View.VISIBLE);
                    } else {
                        View view = (View) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        break;
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (dropEventNotHandled(event)) {
                        dragView.setVisibility(View.VISIBLE);
                    }
                    break;

                default:
                    break;
            }
            return true;
        }

        private boolean dropEventNotHandled(DragEvent dragEvent) {
            return !dragEvent.getResult();
        }
    }

    public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<SurveyModels> items;
        public Context context;
        public SurveyListAdapter.OnPuzzleCompleted mListener;

        public MyFragmentPagerAdapter(FragmentManager fm, Context context, ArrayList<SurveyModels> value, SurveyListAdapter.OnPuzzleCompleted mListener) {
            super(fm);
            this.items = value;
            Log.e("items", "=>" + new Gson().toJson(value));
            this.context = context;
            this.mListener = mListener;
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("itemsssss", "==>" + items.size());
            return OverlapFragment.newInstance(items.get(position));
        }

        @Override
        public int getCount() {
            return items.size();
        }

    }

    public void lap() {
        pagerContainer.setOverlapEnabled(true);
        ViewPager viewPager = pagerContainer.getViewPager();
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), context, surveyarray, new SurveyListAdapter.OnPuzzleCompleted() {
            @Override
            public void onPuzzleCompleted() {
                requestMatchThePuzzleAPI();
            }
        });
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
        new CoverFlow.Builder().with(viewPager)
                .scale(0.3f)
                .pagerMargin(getResources().getDimensionPixelSize(R.dimen.dp660))
                .build();
        pagerContainer.onPageSelected(0);
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                ViewCompat.setElevation(fragment.getView(), 8.0f);
            }
        });
    }
}