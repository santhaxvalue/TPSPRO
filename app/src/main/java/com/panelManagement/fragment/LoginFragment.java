package com.panelManagement.fragment;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
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
import com.panelManagement.activity.SplashScreenActivity;
import com.panelManagement.listener.OnClickAction;
import com.panelManagement.location.DBhelper;
import com.panelManagement.model.DateOfBirthModel;
import com.panelManagement.model.UserInfoModel;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.PanelConstants;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.AsyncHttpRequest;
import com.panelManagement.webservices.ParseJSonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoginFragment extends BaseFragment implements OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {
    public static final int RC_SIGN_IN = 0;
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    // Twitter variable declaration
    private static final String SHARED_PREF = "android_twitter";
    private static final String USER_SCREEN_NAME = "tw_user_id";
    private static final String USER_NAME = "tw_user_name";
    private static final String USER_PIC = "tw_user_pic";
    // Facebook variables
    private static final String TAG = "MainFragment";
    protected UserInfoModel mCoreData;
    OnClickAction listener = null;
    AccountManager mAccountManager;
    String token;
    int serverCode;
    AutoCompleteTextView emailText;
    String[] languages = {"Android ", "java", "IOS", "SQL", "JDBC", "Web services"};
    boolean isFbClick = false;
    LinearLayout linearLayout;
    Bundle bundle;
    String alreadyEmail = "";
    String errorMessage = "";
    private ProgressDialog pd = null;
    private SharedPreferences mSharedPref;
    private boolean mIntentInProgress;
    private ConnectionResult mConnectionResult;
    private EditText emailPassword;
    private TextView forgotPassword;
    public static TextView signuplable;
    public static LinearLayout login_socialmedia_layout;
    private WebView myBrowser;
    private String mPassword;
    private TextView btnSubmit;
    private boolean isAccountLinkactivated;
    private GoogleApiClient mGoogleApiClient;
    private LoginButton login;
    private CallbackManager callbackManager;
    private List<String> accountsList;
    private DBhelper db;
    //int position;
    private GoogleSignInClient mGoogleSignInClient;
    AppEventsLogger logger;

    public static LoginFragment newInstance(int i, String email, String message) {

        Bundle args = new Bundle();
        args.putInt("position", i);
        args.putString(Constants.PANEL_EMAIL_ID, email);
        args.putString("message", message);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            alreadyEmail = getArguments().getString(Constants.PANEL_EMAIL_ID);
            errorMessage = getArguments().getString("message");
        }

        PanelConstants.isLogin = false;
        db = new DBhelper(getContext());

        bundle = new Bundle();
        bundle = getArguments().getBundle("bundle");

        //pingToIMI();

        //SourceEdge

        logger = AppEventsLogger.newLogger(getContext());

        accountsList = new ArrayList<String>();
        callbackManager = CallbackManager.Factory.create();

        accountsList = new ArrayList<>();
        callbackManager = CallbackManager.Factory.create();

        mSharedPref = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestId().requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).
                enableAutoManage(getActivity(), 2, this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        mCoreData = new UserInfoModel();
        mCoreData.setLoginType(UserInfoModel.Type.MANUAL);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, null);

       /* if (bundle.containsKey("message")) {
            showErrorAlert("", bundle.getString("message"));
        }*/

        view.findViewById(R.id.ic_parentview_login).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Utility.hideKeyboard(v, getActivity());
            }
        });
        btnSubmit = view.findViewById(R.id.txt_signin_submit);
        btnSubmit.setOnClickListener(this);

        //  view.findViewById(R.id.btn_signup_test).setOnClickListener(this);

        login = view.findViewById(R.id.btn_facebook);

        view.findViewById(R.id.fb).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                login.performClick();
                isFbClick = true;
            }
        });

        login.setReadPermissions(Collections.singletonList("public_profile, email, user_birthday, user_location"));
        login.setFragment(this);

        login.setOnClickListener((View view1) -> {
            if (AccessToken.getCurrentAccessToken() != null) {

            }
        });

        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if (AccessToken.getCurrentAccessToken() != null) {
                    facebookLogin();
                }

                isFbClick = false;
            }

            @Override
            public void onCancel() {
                dismissDialog();
                dismissIntermediate();
                isFbClick = false;
            }

            @Override
            public void onError(FacebookException exception) {
                dismissDialog();
                dismissIntermediate();
                showErrorAlert("", exception.getMessage());
                isFbClick = false;
            }
        });

        view.findViewById(R.id.btn_gmail).setOnClickListener(this);

        emailText = view.findViewById(R.id.edt_email);
        emailText.setText(alreadyEmail);

        if (!TextUtils.isEmpty(errorMessage)) {
            showErrorAlert("", errorMessage);
        }

        setupAutoComplete();
        emailPassword = view.findViewById(R.id.edt_passowrd);

     /*   emailPassword.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                Utility.hideKeyboard(textView, getActivity());
                if (isValidated()) {
                    if (Utility.isConnectedToInternet(getActivity())) {
                        loginTodashbaord();
                    } else {
                        showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                    }
                }
                return true;
            }
            return false;
        });*/
        emailPassword.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                Utility.hideKeyboard(emailPassword, getActivity());
                if (isValidated()) {
                    if (Utility.isConnectedToInternet(getActivity())) {
                        loginTodashbaord();
                    } else {
                        showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                    }
                }
                return true;
            }
            return false;
        });
        // emailPassword.setText("qwert");
        forgotPassword = view.findViewById(R.id.tv_forgotPassword);
        signuplable = view.findViewById(R.id.signuplable);
        login_socialmedia_layout = view.findViewById(R.id.login_socialmedia_layout);
        forgotPassword.setPaintFlags(forgotPassword.getPaintFlags());
        forgotPassword.setOnClickListener(this);
        myBrowser = view.findViewById(R.id.webview);
        myBrowser.addJavascriptInterface(new MyJavaScriptInterface(getActivity()), "AndroidFunction");
        WebSettings webSettings = myBrowser.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //new code
        webSettings.setAppCacheMaxSize(0);
        webSettings.setAllowFileAccess(false);
        webSettings.setAppCacheEnabled(false);

        myBrowser.getSettings().setAppCacheMaxSize(0);
        myBrowser.getSettings().setAppCacheEnabled(false);
        myBrowser.getSettings().setAllowFileAccess(false);

        myBrowser.loadUrl("file:///android_asset/encript.html");

        linearLayout = view.findViewById(R.id.ic_parentview_login);
        Animation hide = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        hide.setDuration(1500);
        linearLayout.setAnimation(hide);

        return view;
    }

    private void setupAutoComplete() {
        emailText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.email || id == EditorInfo.IME_NULL) {
                    Utility.hideKeyboard(textView, getActivity());
                    emailPassword.requestFocus();
                    return true;
                }
                return false;
            }
        });
        if (Build.VERSION.SDK_INT <= 21) {
            try {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //  ActivityCompat#requestPermissions
                    //  here to request the missing permissions, and then overriding
                    //  public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //  int[] grantResults)
                    //  to handle the case where the user grants the permission. See the documentation
                    //  for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Account[] accounts = AccountManager.get(getActivity()).getAccountsByType("com.google");
                for (Account account : accounts) {
                    accountsList.add(account.name);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, accountsList);
                emailText.setAdapter(adapter);
                emailText.setThreshold(1);
            } catch (Exception e) {
                Log.i("Exception", "Exception:" + e);
            }
        }
    }

    public void facebookLogin() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                (object, response) -> {

                    JSONObject json = response.getJSONObject();
                    try {
                        if (json != null) {
                            String[] name = json.getString("name").split(" ");
                            if (name.length > 0) {
                                mCoreData.setFirstName(name[0]);
                                mCoreData.setLastName(name.length > 1 ? name[1] : "");
                            }
                            String gender = json.optString("gender");
                            mCoreData.setGender(gender.equalsIgnoreCase("male") ? Constants.PANEL_GENDER_MALE
                                    : Constants.PANEL_GENDER_FEMALE);
                            mCoreData.setEmail(json.getString("email"));
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
                            dismissDialog();
                            dismissIntermediate();
                            mCoreData.setLoginType(UserInfoModel.Type.FACEBOOK);
                            verifyUser(Constants.AUTOSOCIALLOGIN);
                        }

                    } catch (JSONException e) {
                        dismissDialog();
                        dismissIntermediate();
                        LoginManager.getInstance().logOut();
                        Utility.alertDialogCustom(getActivity(), R.string.unsupport_fb_details, true);
                        e.printStackTrace();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,likes");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
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
            case R.id.txt_signin_submit:
                if (isValidated()) {
                    if (Utility.isConnectedToInternet(getActivity())) {
                        loginTodashbaord();
                    } else {
                        showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                    }
                }
                break;

            case R.id.btn_gmail:
                if (Utility.isConnectedToInternet(getActivity())) {
                    showIndermedaiteDailog();
                    signIn();
                } else {
                    showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                }
                break;
            case R.id.tv_forgotPassword:
                DialogFragment fragment = new AlertForgotPasswordFragment();
                fragment.show(getActivity().getSupportFragmentManager(), "");
                break;
        }
    }

    private void signIn() {
        @SuppressLint("RestrictedApi") Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
    }

    private void dismissIntermediate() {
        if (pd != null)
            pd.dismiss();
    }

    private boolean isValidated() {
        String email = emailText.getText().toString().trim();
        String password = emailPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            emailText.setError(getString(R.string.login_email));
            emailText.requestFocus();
            return false;
        } else if (!Utility.isEmailValid(email)) {
            emailText.setError(getString(R.string.msg_valid_email));
            emailText.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            emailPassword.setError(getString(R.string.msg_valid_password));
            emailPassword.requestFocus();
            return false;
        }
        return true;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            getActivity();
            if (resultCode == Activity.RESULT_OK) {
                mIntentInProgress = false;
                dismissDialog();
                showIndermedaiteDailog();
                try {
                    @SuppressLint("RestrictedApi") Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);
                } catch (Exception e) {
                    Utility.showToast(getActivity(), "" + e.getMessage());
                }

            } else {
                mIntentInProgress = false;
                dismissDialog();
                dismissIntermediate();
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
            if (!TextUtils.isEmpty(mCoreData.getEmail())) {
                mCoreData.setLoginType(UserInfoModel.Type.GMAIL);
                verifyUser(Constants.AUTOSOCIALLOGIN);
                signOutFromGplus();
                //   signOutFromGplus();
            } else {
                showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                //  signOutFromGplus();
                signOutFromGplus();
            }
            dismissIntermediate();
        } catch (Exception e) {
            dismissIntermediate();
            dismissDialog();
            signOutFromGplus();
            //   signOutFromGplus();
            e.printStackTrace();
        }
    }

    protected void verifyUser(int Id) {
        switch (Id) {
            case Constants.LOGIN:
                try {
                    if (Utility.isConnectedToInternet(getActivity())) {
                        if (isAccountLinkactivated) {
                            showDialog(true, getString(R.string.dialog_login));
                            requestTypePost(Constants.API_AUTOSOCIALLOGIN, new ParseJSonObject(getActivity())
                                    .getSocialLoginLinkObject(emailText.getText().toString().trim(),
                                            mPassword, mCoreData.getUserID(), mCoreData.getLoginType()), Constants.REQUESTCODE_SIGNIN);
                        } else {
                            showDialog(true, getString(R.string.dialog_login));
                            requestTypePost(Constants.BASEURL + "PanelistValidationMobileandDesktop", new ParseJSonObject(getActivity())
                                    .getLoginObject(emailText.getText().toString().trim(), mPassword, getCurrentDateTime()), Constants.REQUESTCODE_SIGNIN);
                        }
                    } else {
                        showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constants.AUTOSOCIALLOGIN:
                try {
                    if (Utility.isConnectedToInternet(getActivity())) {
                        showDialog(true, getString(R.string.dialog_login)); // fb, g+
                        requestTypePost(Constants.API_AUTOSOCIALLOGIN, new ParseJSonObject(getActivity()).getSocialLoginObject(mCoreData.getUserID(), mCoreData.getLoginType()),
                                Constants.REQUESTCODE_AUTOSOCIALLOGIN);
                    } else {
                        showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void vLayout(String res, int requestcode) {
        dismissDialog();
        dismissIntermediate();
        switch (requestcode) {
            case Constants.REQUESTCODE_SIGNIN:
                parseSignin(res);
                break;
            case Constants.REQUESTCODE_AUTOSOCIALLOGIN:
                parseSociallogin(res);
                break;
        }
    }

    private void parseSociallogin(String res) {
        try {
            isAccountLinkactivated = false;
            JSONObject object = new JSONObject(res);
            if (object.has("Status") && object.getBoolean("Status")) {

                logEVENT_NAME_COMPLETED_REGISTRATIONEvent();
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_ID, object.optString("ID"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_SESSIONID, object.optString("PanelistSessionID"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_ISDCODE, object.optString("ISDCode"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_COUNTRYCODE, object.optString("CountryCode"));
                InformatePreferences.putInt(getActivity(), Constants.PREF_MOBILENUMBERMINLENGHT, object.getInt("MobileNumberMinLength"));
                InformatePreferences.putInt(getActivity(), Constants.PREF_MOBILENUMBERMAXLENGHT, object.getInt("MobileNumberMaxLength"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_FIRSTNAME, object.optString("FirstName"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_LASTNAME, object.optString("LastName"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_PANELISTSESSIONID, object.optString("PanelistSessionID"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_PRIVACYPOLICY, object.optString("PPLink"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_TNC, object.optString("TandCLink"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_FBLINK, object.optString("FBLink"));

                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_FILENAME, object.optString("FileName"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_FILEPATH, object.optString("FilePath"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_REFFERELCODE, object.optString("ReferralCode"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_MOBILENUMBER, object.optString("MobileNo"));
                _startHomeActivity();
            } else {
                btnSubmit.setText(getString(R.string.txt_social_link));
                isAccountLinkactivated = true;
                LoginManager.getInstance().logOut();
                dismissDialog();
                dismissIntermediate();
                showErrorAlert("  ", object.optString("Message"));
            }
        } catch (JSONException e) {
            // Utility.showToast(getActivity(), e.getMessage());
            e.printStackTrace();
        }

    }

    public void logEVENT_NAME_COMPLETED_REGISTRATIONEvent() {


        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
        logger.logEvent("EVENT_NAME_COMPLETED_REGISTRATION");
    }

    // manual login response
    private void parseSignin(String res) {

        try {
            JSONObject object = new JSONObject(res);
            if (object.has("Status") && object.getBoolean("Status")) {

                logEVENT_NAME_COMPLETED_REGISTRATIONEvent();
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_ID, object.optString("ID"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_SESSIONID, object.optString("PanelistSessionID"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_ISDCODE, object.optString("ISDCode"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_COUNTRYCODE, object.optString("CountryCode"));
                InformatePreferences.putInt(getActivity(), Constants.PREF_MOBILENUMBERMAXLENGHT, object.getInt("MobileNumberMaxLength"));
                InformatePreferences.putInt(getActivity(), Constants.PREF_MOBILENUMBERMINLENGHT, object.getInt("MobileNumberMinLength"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_FIRSTNAME, object.optString("FirstName"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_LASTNAME, object.optString("LastName"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_PANELISTSESSIONID, object.optString("PanelistSessionID"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_PRIVACYPOLICY, object.optString("PPLink"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_TNC, object.optString("TandCLink"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_FBLINK, object.optString("FBLink"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_FILENAME, object.optString("FileName"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_FILEPATH, object.optString("FilePath"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_REFFERELCODE, object.optString("ReferralCode"));
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_MOBILENUMBER, object.optString("MobileNo"));
                InformatePreferences.putInt(getActivity(), Constants.PREF_MARKET_ID, object.getInt("MarketId"));

                _startHomeActivity();

            } else {
                showErrorAlert(getString(R.string.error), object.optString("Message"));
                emailText.setText("");
                emailText.requestFocus();
                emailPassword.setText("");
            }
        } catch (JSONException e) {
            // Utility.showToast(getActivity(), e.getMessage());
            e.printStackTrace();
        }
    }

    private void _startHomeActivity() {
        ODMmeterStatus();

    }

    private void ODMmeterStatus() {
        final JSONObject json = new JSONObject();
        try {
            json.put("PanelistID", InformatePreferences.getStringPrefrence(context, Constants.PREF_ID));
            //           json.put("LanguageCulture", InformatePreferences.getStringPrefrence(context,Constants.PREF_LOCALECODE));

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
                    if (mCoreData != null) {
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.EMAILID, emailText.getText().toString().trim());
                    }
                    Intent intent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
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
    public void rError(int requestcode) {
    }

    protected void loginTodashbaord() {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        if (today.month < 10) {
            mPassword = emailPassword.getText().toString() + "_" + today.monthDay + "0" + today.month + today.year + today.format("%k%M%S") + "_";
        } else {
            mPassword = emailPassword.getText().toString() + "_" + today.monthDay + today.month + today.year + today.format("%k%M%S") + "_";
        }
        myBrowser.loadUrl("javascript:myFunction_encrypted(\"" + mPassword + "\")");
    }

    @Override
    public void onConnected(Bundle arg0) {
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
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
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.clearDefaultAccountAndReconnect();
            mGoogleSignInClient.revokeAccess();
        }
    }

    private class MyJavaScriptInterface {
        Context mContext;

        MyJavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void getEncryptedText(String password) {
            mPassword = password;
            verifyUser(Constants.LOGIN);
        }
    }
}
