package com.panelManagement.fragment;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.tasks.Task;
import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.HomeFragmentActivity;
import com.panelManagement.activity.R;
import com.panelManagement.activity.RegistrationActivity;
import com.panelManagement.activity.SignUpActivity;
import com.panelManagement.listener.OnClickAction;
import com.panelManagement.listener.OnSocialAccountListener;
import com.panelManagement.model.CountryModel;
import com.panelManagement.model.DateOfBirthModel;
import com.panelManagement.model.UserInfoModel;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.AsyncHttpRequest;
import com.panelManagement.webservices.ParseJSonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;

public class SocialSignupFragment extends BaseFragment implements OnClickListener, OnSocialAccountListener, ConnectionCallbacks, OnConnectionFailedListener {
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    // Twitter variable declaration
    private static final String SHARED_PREF = "android_twitter";
    private static final String USER_SCREEN_NAME = "tw_user_id";
    private static final String USER_NAME = "tw_user_name";
    private static final String USER_PIC = "tw_user_pic";
    private static final int RC_SIGN_IN = 0;
    // Facebook variables
    private static final String TAG = "MainFragment";
    private static final int ELIGIBAL_COUNTRY = 12;
    public static boolean alredyOTP = false;
    protected UserInfoModel mCoreData;
    OnClickAction listener = null;
    AccountManager xmAccountManager;
    String token;
    int serverCode;
    boolean isFbClick = false;
    EditText emailText;
    RelativeLayout relativeLayout;
    int position;
    private Dialog otpDialog;
    private SharedPreferences mSharedPref;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog pd = null;
    private CallbackManager callbackManager;
    private LoginButton login;
    private String invalidUserRegistration;
    private boolean isClick;
    private com.panelManagement.widgets.OtpView edtOtp;
    private EditText emailid;
    private String emailUser;
    private GoogleSignInClient mGoogleSignInClient;

    public SocialSignupFragment() {
        super();
    }

    public static SocialSignupFragment newInstance(int i) {

        Bundle args = new Bundle();
        args.putInt("position", i);
        SocialSignupFragment fragment = new SocialSignupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        if (getArguments() != null) {
            position = getArguments().getInt("position");
        }
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        isFbClick = false;
        mSharedPref = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestId().requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).
                enableAutoManage(getActivity(), 0, this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        /*
         * Bundle bundle = getArguments(); if (bundle != null) { if
         * (bundle.containsKey(Constants.SIGNUP_KEY_USERINFO)) { mCoreData =
         * (UserInfoModel)
         * bundle.getSerializable(Constants.SIGNUP_KEY_USERINFO);
         * mCoreData.setLoginType(UserInfoModel.Type.MANUAL); } }
         */
        mCoreData = new UserInfoModel();
        mCoreData.setLoginType(UserInfoModel.Type.MANUAL);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, null);

        view.findViewById(R.id.already_otp).setOnClickListener(this);
        view.findViewById(R.id.ic_parentview_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyboard(v, getActivity());
            }
        });

        view.findViewById(R.id.txt_create_account).setOnClickListener(this);

        login = view.findViewById(R.id.btn_facebook);
        login.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_location"));
        login.setFragment(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() != null) {
                }
            }
        });
        login.setText(getString(R.string.signup_facebook));
        view.findViewById(R.id.fb).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                login.performClick();
                isFbClick = true;
            }
        });
        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if (AccessToken.getCurrentAccessToken() != null) {
                    facebookLogin();
                    isFbClick = false;
                }
            }

            @Override
            public void onCancel() {
                dismissDialog();
                isFbClick = false;
            }

            @Override
            public void onError(FacebookException exception) {
                showErrorAlert("", exception.getMessage());
                dismissDialog();
                dismissIntermediate();
                isFbClick = false;
            }
        });
        ImageButton gmailLogin = view.findViewById(R.id.btn_gmail);
        //gmailLogin.setTypeface(null, Typeface.NORMAL);
        gmailLogin.setOnClickListener(this);
        //view.findViewById(R.id.btn_gmail).setOnClickListener(this);
        emailText = view.findViewById(R.id.socialselection_edt_Email);

        relativeLayout = view.findViewById(R.id.ic_parentview_signup);
        emailText.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                Utility.hideKeyboard(emailText, getActivity());
                if (isValidated()) {
                    if (Utility.isConnectedToInternet(getActivity())) {
                        verifyUser();
                    } else {
                        showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                    }
                }
                return true;
            }
            return false;
        });
       /* if(position == 0){
            available_survey_layout.setClickable(true);
        }else {
            available_survey_layout.setClickable(false);
        }*/


        Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        hide.setDuration(1500);
        relativeLayout.setAnimation(hide);

        return view;
    }

    private void detectingCountry(boolean isClick) {

        this.isClick = isClick;
        if (Utility.isConnectedToInternet(getActivity())) {
            showDialog(true, getString(R.string.dialog_login));
            AsyncHttpRequest mAppRequest = new AsyncHttpRequest(getActivity(), Constants.GET_COUNTRY_URL, null,
                    ELIGIBAL_COUNTRY, AsyncHttpRequest.Type.GET);
            mAppRequest.setRequestListener(this);
            mAppRequest.execute();
        } else {
            showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
        }
    }

    public void facebookLogin() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        JSONObject json = response.getJSONObject();
                        try {
                            if (json != null) {
                                String[] name = json.getString("name").split(" ");
                                if (name.length > 0) {
                                    mCoreData.setFirstName(name[0]);
                                    mCoreData.setLastName(name.length > 1 ? name[1] : "");
                                }
                                //  String gender = json.getString("gender");
                                //  mCoreData.setGender(gender.equalsIgnoreCase("male") ? Constants.PANEL_GENDER_MALE
                                //          : Constants.PANEL_GENDER_FEMALE);

                                mCoreData.setEmail(json.getString("email"));
                                emailText.setText(mCoreData.getEmail());
                                mCoreData.setuserid(json.getString("id"));
                                if (json.has("birthday")) {
                                    try {
                                        String dob = json.getString("birthday");
                                        String dateformate = Utility.formateDateFromstring("MM/dd/yyyy", "yyyy-MM-dd", dob);
                                        String[] parts = dateformate.trim().split("/");
                                        String month = new DateFormatSymbols().getShortMonths()[Integer.parseInt(parts[0]) - 1];
                                        String day = parts[1];
                                        String year = parts[2];
                                        mCoreData.setDateofbirth(new DateOfBirthModel(month, day, year));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                mCoreData.setLoginType(UserInfoModel.Type.FACEBOOK);
                                dismissDialog();
                                dismissIntermediate();
                                verifyUser();
                            }

                        } catch (JSONException e) {
                            dismissDialog();
                            dismissIntermediate();
                            LoginManager.getInstance().logOut();
                            Utility.alertDialogCustom(getActivity(), R.string.unsupport_fb_details, true);
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onStart() {
        super.onStart();
        //detectingCountry(false);
    }

    private void initData() {
        emailText.setText(mCoreData.getEmail());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnClickAction)
            listener = (OnClickAction) context;
    }

    @Override
    public void onClick(View v) {
        Utility.hideKeyboard(v, getActivity());
        switch (v.getId()) {
            case R.id.txt_create_account:
                if (!TextUtils.isEmpty(invalidUserRegistration)) {
                    showErrorAlert("", invalidUserRegistration);
                    return;
                }
//                if (isValidated()) {
//                    verifyUser();
//                }
                if (Utility.isConnectedToInternet(getActivity())) {
                    _redirectToWebForSignUp();
                } else {
                    showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                }
                break;

            case R.id.btn_gmail:
                if (!TextUtils.isEmpty(invalidUserRegistration)) {
                    showErrorAlert("", invalidUserRegistration);
                    return;
                }
                if (Utility.isConnectedToInternet(getActivity())) {
                    showIndermedaiteDailog();
                    signIn();
                    // mGoogleApiClient.connect();
                } else {
                    showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                }
                break;

            case R.id.already_otp:
                showCustomOTPDialog();
                break;
        }
    }

    private void _redirectToWebForSignUp() {
        Intent viewIntent =
                new Intent("android.intent.action.VIEW",
                        Uri.parse(Constants.GET_WEB_URL_FOR_SIGNUP));
        startActivity(viewIntent);
    }

    private void signIn() {
        @SuppressLint("RestrictedApi") Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private boolean isValidated() {
        String email = emailText.getText().toString();
        if (TextUtils.isEmpty(email))
            //showErrorAlert(getString(R.string.error), getString(R.string.login_email));
            emailText.setError(getString(R.string.login_email));

        else if (!Utility.isEmailValid(email))
            //showErrorAlert(getString(R.string.error), getString(R.string.msg_valid_email));
            emailText.setError(getString(R.string.msg_valid_email));

        else {
            mCoreData.setEmail(email);
            return true;
        }
        return false;
    }

    public void saveCredential(String screenName, String name, String profilePic) {
        Editor editor = mSharedPref.edit();
        editor.putString(USER_SCREEN_NAME, screenName);
        editor.putString(USER_NAME, name);
        editor.putString(USER_PIC, profilePic);
        editor.commit();
    }

    @Override
    public void onGoogleInfo(String value) {
        dismissDialog();
        dismissIntermediate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (requestCode == RC_SIGN_IN) {
                if (resultCode == Activity.RESULT_OK) {
                    mSignInClicked = false;
                    mIntentInProgress = false;
                    dismissIntermediate();
                    showIndermedaiteDailog();

                    @SuppressLint("RestrictedApi") Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);
                } else {
                    mSignInClicked = false;
                    mIntentInProgress = false;
                    dismissDialog();
                    dismissIntermediate();
                }

            }

        } else if (resultCode == Activity.RESULT_OK) {
            showDialog(true, getString(R.string.dialog_login));
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else {
            dismissIntermediate();
            showIndermedaiteDailog();
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
            //getGoogleProfileInformation();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }


    }

    private void updateUI(GoogleSignInAccount account) {
        Log.wtf(TAG, "updateUI: accountInfo" + account.getDisplayName());
        try {
            String personName = account.getDisplayName();
            String email = account.getEmail();
            String personPhotoUrl = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "";

            mCoreData.setuserid(account.getId());
            mCoreData.setUserProfiImage(personPhotoUrl);
            mCoreData.setFirstName(account.getGivenName());
            mCoreData.setLastName(account.getFamilyName());

            mCoreData.setEmail(email);
            emailText.setText(email);
            mCoreData.setLoginType(UserInfoModel.Type.GMAIL);
            if (!TextUtils.isEmpty(mCoreData.getEmail()) && !mCoreData.getFirstName().equalsIgnoreCase("null")
                    && !mCoreData.getLastName().equalsIgnoreCase("null")) {
                mCoreData.setLoginType(UserInfoModel.Type.GMAIL);
                showIndermedaiteDailog();
                verifyUser();
                signOutFromGplus();
            } else {
                emailText.setText("");
                showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                signOutFromGplus();
            }
            dismissIntermediate();
        } catch (Exception e) {
            dismissIntermediate();
            dismissDialog();
            emailText.setText("");
            showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
            signOutFromGplus();
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void verifyUser() {
        //mCoreData.setCountryShortCode("VN");
        try {
            if (Utility.isConnectedToInternet(getActivity())) {
                if (!TextUtils.isEmpty(mCoreData.getCountryShortCode())) {
                    showDialog(true, getString(R.string.dialog_login));
                    requestTypePost(Constants.API_ISVALIDUSER, new ParseJSonObject(getActivity()).getIsValidUserObject(mCoreData.getEmail(),
                            Utility.getDeviceId(getActivity())/*"shdghasg"*/, mCoreData.getCountryShortCode(),
                            Constants.OS_TYPE, mCoreData.getUserID()), Constants.REQUESTCODE_ISVALIDUSER);
                } else {
                    detectingCountry(true);
                }
            } else {
                showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showIndermedaiteDailog() {
        if (pd != null) {
            pd.hide();
            pd.dismiss();
            pd = null;
        }
        pd = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setMessage(getString(R.string.dialog_login));
        pd.setCancelable(true);
        pd.setIndeterminate(true);
        pd.show();
    }

    private void dismissIntermediate() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    @Override
    public void onFacebookInfo(String value) {
    }

    @Override
    public void onTwiterInfo(String value) {
    }

    @Override
    public void onError(String message) {
        dismissDialog();
        dismissIntermediate();
        showErrorAlert(getString(R.string.error), getString(R.string.async_task_not_responding));
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
//        getGoogleProfileInformation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

//    /*private void getGoogleProfileInformation() {
//        try {
//            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
//                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
//                String personName = currentPerson.getDisplayName();
//                Name userName = currentPerson.getName();
//                int gender = currentPerson.getGender();
//                String personPhotoUrl = currentPerson.getImage().getUrl();
//                String personGooglePlusProfile = currentPerson.getUrl();
//                String birthday = currentPerson.getBirthday();
//                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
//
//                Log.e(TAG, "Name: " + personName + ", plusProfile: " + personGooglePlusProfile + ", email: " + email
//                        + ", Image: " + personPhotoUrl + " birthday " + birthday);
//
//                mCoreData.setuserid(currentPerson.getId());
//                mCoreData.setUserProfiImage(personPhotoUrl);
//                mCoreData.setFirstName(userName.getGivenName());
//                mCoreData.setLastName(userName.getFamilyName());
//                if (gender == 0) {
//                    mCoreData.setGender(Constants.PANEL_GENDER_MALE);
//                } else if (gender == 1) {
//                    mCoreData.setGender(Constants.PANEL_GENDER_FEMALE);
//                }
//
//                mCoreData.setEmail(email);
//                emailText.setText(email);
//                mCoreData.setLoginType(UserInfoModel.Type.GMAIL);
//                if (!TextUtils.isEmpty(birthday)) {
//                    String[] parts = birthday.trim().split("-");
//                    String year = parts[0].trim();
//                    String month = new DateFormatSymbols().getShortMonths()[Integer.parseInt(parts[1].trim()) - 1];
//                    String day = parts[2].trim();
//                    mCoreData.setDateofbirth(new DateOfBirthModel(month, day, year));
//                }
//                if (!TextUtils.isEmpty(mCoreData.getEmail())) {
//                    mCoreData.setLoginType(UserInfoModel.Type.GMAIL);
//                    verifyUser();
//                    signOutFromGplus();
//                } else {
//                    showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
//                    signOutFromGplus();
//                }
//            } else {
//                showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
//                signOutFromGplus();
//            }
//            dismissIntermediate();
//        } catch (Exception e) {
//            dismissIntermediate();
//            dismissDialog();
//            signOutFromGplus();
//            e.printStackTrace();
//        }
//    }*/

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), getActivity(), 0).show();
            dismissIntermediate();
            return;
        }
        if (!mIntentInProgress) {
            mConnectionResult = result;
            resolveSignInError();
        }
    }

    /**
     * Method to resolve any signin errors
     */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(getActivity(), RC_SIGN_IN);

            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void signOutFromGplus() {
        emailUser = "";
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.clearDefaultAccountAndReconnect();
            mGoogleSignInClient.revokeAccess();
        }
    }

    @Override
    public void vLayout(String res, int requestcode) {
        // Utility.showToast(getActivity(), "Response:\n" + res);
        dismissIntermediate();
        switch (requestcode) {

            case ELIGIBAL_COUNTRY:
                try {
                    JSONObject object = new JSONObject(res);
                    String status = object.getString("status");
                    if (status.equalsIgnoreCase("Success")) {
                        String countryName = object.getString("country");
                        String countryCode = object.getString("code");
                        String ip_address = object.getString("ip_address");
                        mCoreData.setStartTime(Utility.getCurrentTimeStamp());
                        mCoreData.setPublicIpAddress(ip_address);
                        mCoreData.setDetectedCountry(countryName);
                        mCoreData.setCountryShortCode(countryCode);
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_COUNTRYCODE, countryCode);
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_IPADDRESS, ip_address);
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_COUNTRY, countryName);
                        if (isClick)
                            verifyUser();
                    } else {
                        invalidUserRegistration = getString(R.string.isvalidbamarket) + " " + object.getString("country");
                    }
                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;

            case Constants.REQUESTCODE_ISVALIDUSER:
                try {
                    LoginManager.getInstance().logOut();
                    JSONObject object = new JSONObject(res);
                    if (object.has("Status") && object.getBoolean("Status")) {
                        InformatePreferences.setStringPrefrence(context, Constants.PREF_HELP_DESK_EMAIL, object.optString("HelpdeskEmailId"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_EMAIL_ID, mCoreData.getEmail());
                        int mobileNumberMaxLength = 15;
                        int mobileNumberMinLength = 15;
                        int ISDCode = 0;
                        if (object.has("MobileNumberMaxLength")) {
                            mobileNumberMaxLength = object.getInt("MobileNumberMaxLength");
                            if (mobileNumberMaxLength == 0)
                                mobileNumberMaxLength = 10;
                        }
                        if (object.has("MobileNumberMinLength")) {
                            mobileNumberMinLength = object.getInt("MobileNumberMinLength");
                            if (mobileNumberMinLength == 0)
                                mobileNumberMinLength = 10;
                        }
                        if (object.has("ISDCode")) {
                            ISDCode = object.getInt("ISDCode");
                        }
                        if (object.has("PPLink"))
                            InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_PRIVACYPOLICY, object.getString("PPLink"));
                        if (object.has("TandCLink"))
                            InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_TNC, object.getString("TandCLink"));
                        ArrayList<CountryModel> cityList = new ArrayList<CountryModel>();

                        if (object.has("CityList")) {
                            if (object.getString("CityList").equals("null")) {
                                showErrorAlert("", getString(R.string.msg_citynotfound));
                                return;
                            }
                            JSONArray cityArray = object.getJSONArray("CityList");
                            for (int i = 0; i < cityArray.length(); i++) {
                                object = cityArray.getJSONObject(i);
                                int mCityId = 0;
                                if (object.has("CityAnsID"))
                                    mCityId = object.getInt("CityAnsID");
                                String mCityName = "";
                                if (object.has("CityName"))
                                    mCityName = object.getString("CityName");
                                cityList.add(new CountryModel(mCityName, mCityId));
                            }
                            if (listener != null) {
                                mCoreData.setCityList(cityList);
                                mCoreData.setEmail(emailText.getText().toString().trim());
                                mCoreData.setMobileLength(mobileNumberMaxLength);
                                InformatePreferences.putInt(getActivity(), Constants.PREF_MOBILENUMBERMINLENGHT, mobileNumberMinLength);
                                InformatePreferences.putInt(getActivity(), Constants.PREF_MOBILENUMBERMAXLENGHT, mobileNumberMaxLength);
                                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_ISDCODE, String.valueOf(ISDCode));
                                //listener.SocialInfo(mCoreData);
                            }
                            Intent intent = new Intent(context, RegistrationActivity.class);
                            intent.putExtra(Constants.SIGNUP_KEY_USERINFO, mCoreData);

                            getActivity().startActivityForResult(intent, 0);
                            getActivity().finish();
                            //	transaction.addToBackStack(null);
                        } else {
                            showErrorAlert("", getString(R.string.msg_citynotfound));
                        }
                    } else {
                        Intent intent = new Intent(getContext(), SignUpActivity.class);
                        intent.putExtra(Constants.PANEL_EMAIL_ID, emailText.getText().toString().trim());
                        intent.putExtra("message", object.getString("Message"));
                        startActivityForResult(intent, 0);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dismissIntermediate();
                }
                break;

            case 3:
                dismissDialog();
                JSONObject object = null;
                try {
                    object = new JSONObject(res);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (object.has("Status") && object.getBoolean("Status")) {
                        if (otpDialog != null && otpDialog.isShowing()) {
                            otpDialog.dismiss();
                        }
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_ID, object.optString("PanelistId"));  //niyaj changes
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_SESSIONID, object.optString("PanelistSessionID"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_ISDCODE, object.optString("ISDCode"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_COUNTRYCODE, object.optString("CountryCode"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_MOBILENUMBERMAXLENGHT, object.optString("MobileNumberMaxLength"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_FIRSTNAME, object.optString("FirstName"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_LASTNAME, object.optString("LastName"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_PANELISTSESSIONID, object.optString("PanelistSessionID"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_PRIVACYPOLICY, object.optString("PPLink"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_TNC, object.optString("TandCLink"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_FBLINK, object.optString("FBLink"));
                        InformatePreferences.setStringPrefrence(context, Constants.PREF_FILENAME, object.optString("FileName"));
                        InformatePreferences.setStringPrefrence(context, Constants.PREF_FILEPATH, object.optString("FilePath"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_REFFERELCODE, object.optString("ReferralCode"));

                        SignupContactInfoFragment.IsOTP = false;
                        //ODMmeterStatus();
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.putExtra(HomeActivity.KEY_REGISTER, true);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        showErrorAlert("", object.getString("Message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void rError(int requestcode) {

    }

    private void ODMmeterStatus() {
        final JSONObject json = new JSONObject();
        try {
            json.put("PanelistID", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
//            json.put("LanguageCulture", InformatePreferences.getStringPrefrence(context,Constants.PREF_LOCALECODE));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncHttpRequest mAppRequest = new AsyncHttpRequest(context, Constants.API_ODMmeterSTATUS, json.toString(),
                Constants.REQUESTCODE_ODMMETERINGSTATUS, AsyncHttpRequest.Type.POST);

        mAppRequest.setRequestListener(new AsyncHttpRequest.RequestListener() {
            @Override
            public void onRequestCompleted(String response, int requestCode) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    InformatePreferences.putBoolean(context, Constants.PREF_ODMMETERINGSTATUS, jsonObject.getBoolean("Status"));

                    Constants.EnableMetering = jsonObject.getBoolean("Status");
                    Constants.FirstAttemp_Metering = true;
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra(HomeActivity.KEY_REGISTER, true);
                    startActivity(intent);
                    getActivity().finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestError(Exception e, int requestCode) {

            }

            @Override
            public void onRequestStarted(int requestCode) {

            }
        });
        mAppRequest.execute();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isFbClick) {
            isFbClick = false;
            dismissIntermediate();
            dismissDialog();
        }
    }

    public void showCustomOTPDialog() {
        ImageView closeDialog;

        otpDialog = new Dialog(context, R.style.Theme_Dialog);
        otpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otpDialog.setCancelable(false);
        otpDialog.setContentView(R.layout.already_have_otp);
        otpDialog.setCanceledOnTouchOutside(false);
        Window window = otpDialog.getWindow();
        assert window != null;
        otpDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);


        edtOtp = otpDialog.findViewById(R.id.edtOtp);
        emailid = otpDialog.findViewById(R.id.otp_email);

        TextView txtCheckMail = otpDialog.findViewById(R.id.ic_textview_checkmail_already_otp);

        txtCheckMail.setText(Html.fromHtml(getString(R.string.ui_msg_checkemail)));

        closeDialog = otpDialog.findViewById(R.id.close_dialog_already_otp);

        closeDialog.setOnClickListener(v -> {
            Utility.hideKeyboard(v, getActivity());
            otpDialog.dismiss();
        });

//        otpDialog.findViewById(R.id.parentMessages).setOnClickListener(arg0 -> Utility.hideKeyboard(arg0, getActivity()));

        otpDialog.findViewById(R.id.btn_signup_submit_alreadt_otp).setOnClickListener(arg0 -> {
            if (!TextUtils.isEmpty(emailid.getText())) {
                if (Utility.isEmailValid(emailid.getText().toString())) {
                    if (!TextUtils.isEmpty(edtOtp.getOTP()) && edtOtp.getOTP().length() == 6) {
                        verifyEmail();
                    } else {
                        Utility.hideKeyboard(edtOtp, getActivity());
                        showErrorAlert("", getString(R.string.unabletoverifypin));
                    }
                } else {
                    emailid.setError(getString(R.string.msg_valid_email));
                    emailid.requestFocus();
                    Utility.hideKeyboard(edtOtp, getActivity());
                }
            } else {
                showErrorAlert("", getString(R.string.msg_unregister_email));
                Utility.hideKeyboard(edtOtp, getActivity());
            }
        });
        otpDialog.show();
    }

    private void verifyEmail() {
        if (Utility.isConnectedToInternet(getActivity())) {
            showDialog(true, getString(R.string.dialog_login));
            requestTypePost(Constants.API_VERIFYEMAILOTP, new ParseJSonObject(getActivity()).verifyEmailOtp(emailid.getText().toString(), edtOtp.getOTP()), 3);
        } else {
            Utility.hideKeyboard(edtOtp, getActivity());
            showErrorAlert("", getString(R.string.async_task_error));
        }
    }
}
