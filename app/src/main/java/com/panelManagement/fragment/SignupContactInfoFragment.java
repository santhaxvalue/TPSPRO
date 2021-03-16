package com.panelManagement.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kochava.base.Tracker;
import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.activity.SignUpActivity;
import com.panelManagement.listener.OnClickAction;
import com.panelManagement.listener.OnDateListener;
import com.panelManagement.listener.OnRegionsListener;
import com.panelManagement.model.CountryModel;
import com.panelManagement.model.DateOfBirthModel;
import com.panelManagement.model.GetLanguageDetailsModel;
import com.panelManagement.model.LanguageDetail;
import com.panelManagement.model.PanelistIdModel;
import com.panelManagement.model.UserInfoModel;
import com.panelManagement.utils.ApiInterface;
import com.panelManagement.utils.CircleTransform;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.ImageCompressor;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.PanelConstants;
import com.panelManagement.utils.ServiceGenerator;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.AsyncHttpRequest;
import com.panelManagement.webservices.AsyncHttpRequest.RequestListener;
import com.panelManagement.webservices.ParseJSonObject;
import com.panelManagement.widgets.TextViewPlus;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class SignupContactInfoFragment extends BaseFragment implements OnClickListener, OnRegionsListener, RequestListener, OnDateListener {

    public static final String KEY_CITY = "city";
    public static boolean IsOTP = false;
    // OTP contents
    private final long interval = 1000;
    private final int PERMISSION_REQUEST_CAMERA = 1;
    private final int PERMISSION_REQUEST_STORAGE = 2;
    public boolean IsApplied = false;
    OnClickAction listener = null;
    UserInfoModel mCoreData = null;
    // Button edtCountry;
    EditText edt_refer_code;
    TextView applyCode;
    EditText edtCity;
    EditText edtPhoneNumber;
    EditText ISDCode;
    EditText edtaddress;
    EditText edtzipCode;
    EditText lbl_country_language;
    CheckBox cb_terms_conditions;
    int maxPhoneLength = 10;
    int maxZipLength = 6;
    TextViewPlus lblGender;
    String emailid;
    TextViewPlus textviewResend = null;
    boolean alredyotp = false;
    Bundle bundle;
    private String str_selected_language;
    private ArrayList<LanguageDetail> languageDetailsModels;
    private Dialog otpDialog;
    private UserInfoModel mCoredata;
    private Button submitBtn;
    private CountryModel cityValue;
    private EditText edtFname;
    private EditText edtlastame;
    private EditText btnMonth;
    private TextView existingUser;
    private int minPhoneLength;
    private boolean Flag = false;
    private ProgressDialog mProgressDialog;
    private EditText editEmail;
    private com.panelManagement.widgets.OtpView edtOtp;
    private TextViewPlus txtTimer;
    private long startTime = 120000; // 1 min
    private MyCountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private Uri uri;
    private String finalReferalCode = "";
    private String base64_1 = "";
    private ImageView profilePic;

    private BroadcastReceiver SmsListener = new BroadcastReceiver() {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras(); // ---get the SMS message
                // passed in---
                SmsMessage[] msgs = null;
                // String msg_from;
                if (bundle != null) {
                    // ---retrieve the SMS message received---
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            // msg_from = msgs[i].getOriginatingAddress();
                            String msgBody = msgs[i].getMessageBody();
                            Pattern pattern = Pattern.compile("(\\d{4})");
                            Matcher matcher = pattern.matcher(msgBody);
                            String val = "";
                            if (matcher.find()) {
                                val = matcher.group(1);
                                edtOtp.setOTP(val);// 4 digit number
                                if (!TextUtils.isEmpty(edtOtp.getOTP()))
                                    verifyEmail();
                                else {
                                    showErrorAlert("", getString(R.string.error_enter_otp));
                                    Utility.showKeyboard(edtOtp, getActivity());
                                }
                            }
                            // do your stuff
                        }
                    } catch (Exception e) {
                        // Log.d("Exception caught",e.getMessage());
                    }
                }
            }
        }
    };

    // CountryParseModel countryList;

    public static SignupContactInfoFragment newInstance(Bundle bundle) {

        Bundle args = new Bundle();
        args.putBundle("bundle", bundle);
        if (bundle.containsKey(Constants.SIGNUP_KEY_USERINFO))
            args.putSerializable(Constants.SIGNUP_KEY_USERINFO, bundle.getSerializable(Constants.SIGNUP_KEY_USERINFO));
        if (bundle.containsKey("flag"))
            args.putBoolean("flag", bundle.getBoolean("flag"));
        SignupContactInfoFragment fragment = new SignupContactInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static String getTermsConditionText(Context mContext) {
        String terms = "";
        try {
            terms = mContext.getString(R.string.txt_iaggree) + " <a href='"
                    + InformatePreferences.getStringPrefrence(mContext, Constants.PREF_PRIVACYPOLICY) + "'>"
                    + mContext.getString(R.string.lbl_privacy_statement) + "</a>" + " "
                    + mContext.getString(R.string.txt_and_text) + " <a href='"
                    + InformatePreferences.getStringPrefrence(mContext, Constants.PREF_TNC) + "'>"
                    + mContext.getString(R.string.lbl_terms_cond) + ".</a>";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return terms;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(Constants.SIGNUP_KEY_USERINFO)) {
                mCoreData = (UserInfoModel) bundle.getSerializable(Constants.SIGNUP_KEY_USERINFO);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_contact_info, null);
        view.findViewById(R.id.ic_parentview).setOnClickListener(v -> Utility.hideKeyboard(v, getActivity()));

        //edtSelectLanguage = view.findViewById(R.id.select_language);

        if (IsOTP) {
            showCustomOTPDialog(bundle);
        } else {

            edtFname = view.findViewById(R.id.edt_personal_info_firstname);
            edtlastame = view.findViewById(R.id.edt_personal_info_lastname);
            edtPhoneNumber = view.findViewById(R.id.edt_phonenumber);
            ISDCode = view.findViewById(R.id.isdcode);
            edtaddress = view.findViewById(R.id.edt_address);
            edtzipCode = view.findViewById(R.id.edt_zipcode);
            lbl_country_language = view.findViewById(R.id.lbl_country_language);
            edt_refer_code = view.findViewById(R.id.edt_refer_code);
            applyCode = view.findViewById(R.id.apply_referal);
            applyCode.setOnClickListener(this);
            lbl_country_language.setClickable(true);
            lbl_country_language.setOnClickListener(this);

            profilePic = view.findViewById(R.id.register_profile_image);
            profilePic.setOnClickListener(this);

            existingUser = view.findViewById(R.id.existing_login);
            existingUser.setOnClickListener(this);

            lblGender = view.findViewById(R.id.lbl_gender);
            lblGender.setOnClickListener(this);

            btnMonth = view.findViewById(R.id.signupProfile_btn_month);
            btnMonth.setOnClickListener(this);

            submitBtn = view.findViewById(R.id.btn_reigster_submit);
            submitBtn.setOnClickListener(this);

            edtCity = view.findViewById(R.id.edtCity);
            edtCity.setOnClickListener(this);

            cb_terms_conditions = view.findViewById(R.id.check_contact_info_agreement);

            setPhoneMaxLength();
            setPhoneISDcode();

            initUserInfo(view);

            TextView txtTerms = view.findViewById(R.id.ic_signupContactTermscondition);
            txtTerms.setText(Html.fromHtml(getTermsConditionText(getActivity())));
            txtTerms.setClickable(true);
            txtTerms.setMovementMethod(LinkMovementMethod.getInstance());
            edt_refer_code.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (IsApplied) {
                        applyCode.setClickable(true);
                        applyCode.setTextColor(getResources().getColor(R.color.aqua_dark));
                        applyCode.setText(getString(R.string.apply_referral_code));
                        finalReferalCode = "";
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        return view;
    }

    private void setPhoneISDcode() {
        ISDCode.setText(String.format("+%s -", InformatePreferences.getStringPrefrence(getActivity(), Constants.PREF_ISDCODE)));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        languageDetailsModels = new ArrayList<>();


        _getLanguages();
        cb_terms_conditions.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                if (isValidated()) {
                    _showGDPRConsentForm(context, (consentOne, consentTwo, consentFinal, dialog) -> {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        InformatePreferences.setStringPrefrence(context, Constants.PREF_CONSENT_ONE, consentOne);
                        InformatePreferences.setStringPrefrence(context, Constants.PREF_CONSENT_TWO, consentTwo);
                        InformatePreferences.setStringPrefrence(context, Constants.PREF_CONSENT_FINAL, consentFinal);
                        Log.e("Consents", "ConsentOne : " + consentOne
                                + " ConsentTwo : " + consentTwo + " ConsentFinal : " + consentFinal);
                    });
                } else {
                    cb_terms_conditions.toggle();
                }
            } else {
                _clearConsentAnswersInPreferences();
            }
        });
    }

    private void _getLanguages() {
        showDialog(true, getString(R.string.dialog_login));
        String countryCode = InformatePreferences.getStringPrefrence(context, Constants.PREF_LOCALECODE);
        if (TextUtils.isEmpty(countryCode)) return;

        ApiInterface apiInterface = ServiceGenerator.createService().create(ApiInterface.class);
        Call<GetLanguageDetailsModel> call = apiInterface.getLanguageDetails(new PanelistIdModel(countryCode));

        call.enqueue(new Callback<GetLanguageDetailsModel>() {
            @Override
            public void onResponse(@NonNull Call<GetLanguageDetailsModel> call, @NonNull Response<GetLanguageDetailsModel> response) {
                dismissDialog();
                if (response.code() == 200 && response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus()) {

                            languageDetailsModels = response.body().getLanguageDetails();

                        } else {
                            showErrorAlert("", response.body().getMessage());
                        }
                    } else {
                        showErrorAlert("", getString(R.string.api_network_connection_unavailbale));
                    }
                } else {
                    showErrorAlert("", getString(R.string.api_network_connection_unavailbale));
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetLanguageDetailsModel> call, @NonNull Throwable t) {
                dismissDialog();
                showErrorAlert("", getString(R.string.api_network_connection_unavailbale));
            }
        });
    }


    void _clearConsentAnswersInPreferences() {
        SharedPreferences.Editor editor = context.getSharedPreferences("INFORMATE", Context.MODE_PRIVATE).edit();
        editor.remove(InformatePreferences.KEY_INFORMATE_VALUE + Constants.PREF_CONSENT_ONE);
        editor.remove(InformatePreferences.KEY_INFORMATE_VALUE + Constants.PREF_CONSENT_TWO);
        editor.remove(InformatePreferences.KEY_INFORMATE_VALUE + Constants.PREF_CONSENT_FINAL);
        editor.apply();
        editor.commit();

        Log.e("Consents Removed", "ConsentOne : " + InformatePreferences.getStringPrefrence(context, Constants.PREF_CONSENT_ONE)
                + " ConsentTwo : " + InformatePreferences.getStringPrefrence(context, Constants.PREF_CONSENT_TWO) + " ConsentFinal : "
                + InformatePreferences.getStringPrefrence(context, Constants.PREF_CONSENT_FINAL));
    }

    private void initUserInfo(View view) {
        edtFname.setText(TextUtils.isEmpty(mCoreData.getFirstName()) ? "" : mCoreData.getFirstName());
        edtlastame.setText(TextUtils.isEmpty(mCoreData.getLastName()) ? "" : mCoreData.getLastName());

        DateOfBirthModel dateofBirthObject = mCoreData.getDateofbirth();
        if (dateofBirthObject != null) {
            try {
                btnMonth.setText(dateofBirthObject.getMonth() + " " + dateofBirthObject.getDay() + "," + dateofBirthObject.getYear());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

        lblGender.setText(TextUtils.isEmpty(mCoreData.getGender()) ? "" : mCoreData.getGender());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnClickAction)
            listener = (OnClickAction) activity;
    }

    @Override
    public void onClick(View v) {
        Utility.hideKeyboard(v, getActivity());
        FragmentManager fm = null;
        DialogFragment fragment = null;
        switch (v.getId()) {

            case R.id.existing_login:
                startActivity(new Intent(context, SignUpActivity.class));
                getActivity().finish();
                break;

            case R.id.lbl_gender:
                showCustomAlertGenderDialog();
                break;

            case R.id.lbl_country_language:
                showCustomAlertLanguageDialog();
                break;

            case R.id.signupProfile_btn_month:
                showDatePicker();
                break;

            case R.id.edtCity:
                fm = getActivity().getSupportFragmentManager();
                String cityVal = edtCity.getText().toString().trim();

                if (!TextUtils.isEmpty(mCoreData.getDetectedCountry())) {

                    /**
                     *                     sorting of city names alphabatically
                     *                     niyaj 16-9-2017
                     */

                    Collections.sort(mCoreData.getCityList(), new Comparator<CountryModel>() {
                        @Override
                        public int compare(CountryModel o1, CountryModel o2) {
                            return o1.getCityDisplayname().compareTo(o2.getCityDisplayname());
                        }
                    });
                    fragment = new AlertCountryFragment(this, mCoreData);
                    Bundle args = new Bundle();
                    args.putString(KEY_CITY, mCoreData.getCountryShortCode());
                    fragment.setArguments(args);
                    fragment.show(fm, cityVal);
                    edtCity.setError(null);
                }
                break;

            case R.id.apply_referal:
                if (!TextUtils.isEmpty(edt_refer_code.getText().toString()))
                    verifyReferCode();
                else {
                    showErrorAlert(" ", getString(R.string.error_valid_referral_code));
                    finalReferalCode = "";
                }
                break;

            case R.id.btn_reigster_submit:
                if (isValidated()) {
//                    if (IsApplied) {
                    String firstName = edtFname.getText().toString();
                    String lastname = edtlastame.getText().toString();
                    mCoreData.setFirstName(firstName);
                    mCoreData.setLastName(lastname);
                    String dob = btnMonth.getText().toString();
                    StringTokenizer st = new StringTokenizer(dob.trim(), " ,");
                    int month = DatePickerFragment.getmonthfinal();
                    int day = DatePickerFragment.getDay();
                    int year = DatePickerFragment.getYear();
                    mCoreData.setDateofbirth(new DateOfBirthModel(String.valueOf(month), String.valueOf(day), String.valueOf(year)));
                    IsApplied = false;
                    postRegistrationData();
                }

                break;

            case R.id.register_profile_image:
                captureImage();
                break;
        }
    }

    private void verifyReferCode() {
        String refercode = edt_refer_code.getText().toString().trim();
        showDialog(true, getString(R.string.dialog_login));
        requestTypePost(Constants.API_REFERRAL_VALIDATION, new ParseJSonObject(context).referCodeValidation(refercode), Constants.REQUEST_REFER_VALIDATION);
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        String dob = btnMonth.getText().toString();
        if (!TextUtils.isEmpty(dob)) {
            StringTokenizer st = new StringTokenizer(dob.trim(), " ,");
            int month = DatePickerFragment.getmonthfinal();
            int day = DatePickerFragment.getDay();
            int year = DatePickerFragment.getYear();
            args.putInt("year", year);
            args.putInt("month", month - 1);
            args.putInt("day", day);
        } else {
            args.putInt("year", calender.get(Calendar.YEAR));
            args.putInt("month", calender.get(Calendar.MONTH));
            args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        }
        date.setArguments(args);
        date.setCallBack(this);
        date.show(getActivity().getSupportFragmentManager(), "Date Picker");
        btnMonth.setError(null);
    }

    private void postRegistrationData() {

        if (Utility.isConnectedToInternet(getActivity())) {
            showDialog(true, getString(R.string.dialog_login));
            IsOTP = true;
            AsyncHttpRequest mAppRequest = new AsyncHttpRequest(getActivity(), Constants.API_REGISTERPANELISTTHROUGHMOBILE, getRegistrationData(),
                    Constants.REQUESTCODE_SIGNUP_USER, AsyncHttpRequest.Type.POST);
            mAppRequest.setRequestListener(this);
            mAppRequest.execute();
        } else {
            showErrorAlert(getString(R.string.error), getString(R.string.msg_low_conn));
        }
    }

    private void setPhoneMaxLength() {

        if (!mCoreData.getCountryShortCode().equalsIgnoreCase("IN")) {
            maxZipLength = 15;
            edtzipCode.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            maxZipLength = 6;
            edtzipCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        maxPhoneLength = InformatePreferences.getMaxMobileLength(getActivity());
        minPhoneLength = InformatePreferences.getMinMobileLength(getActivity());

        edtPhoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxPhoneLength)});
        edtzipCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxZipLength)});
        edtzipCode.setText("");

    }

    public String getRegistrationData() {
        ParseJSonObject jsonObject = new ParseJSonObject(getActivity());
        jsonObject.putSignupObject(Constants.PANEL_FN, mCoreData.getFirstName());
        jsonObject.putSignupObject(Constants.PANEL_LN, mCoreData.getLastName());
        jsonObject.putSignupObject(Constants.PANEL_EMAIL_ID, mCoreData.getEmail());
        InformatePreferences.setStringPrefrence(getActivity(), Constants.EMAILID, mCoreData.getEmail());
        jsonObject.putSignupObject(Constants.PANEL_GENDERID, mCoreData.getGender());
        DateOfBirthModel dob = mCoreData.getDateofbirth();
        String dateofBirth = "";
        if (dob != null)
            dateofBirth = dob.getMonth() + "/" + dob.getDay() + "/" + dob.getYear();
        jsonObject.putSignupObject(Constants.PANEL_DOB, dateofBirth);
        jsonObject.putSignupObject(Constants.PANEL_PHONENUMBER, edtPhoneNumber.getText().toString());
        jsonObject.putSignupObject(Constants.PANEL_COUNTRY, mCoreData.getCountryShortCode());
        jsonObject.putSignupObject(Constants.PANEL_CITY, cityValue.getCityId());
        jsonObject.putSignupObject(Constants.PANEL_ZIP, edtzipCode.getText().toString().trim());
        JSONObject object = jsonObject.getSignupObject(mCoreData.getLoginType(), mCoreData.getUserID(), finalReferalCode.trim(), base64_1, str_selected_language, InformatePreferences.getStringPrefrence(context, Constants.PREF_REFFERELPANELLISTID));
        return object.toString();
    }

    public void showCustomAlertGenderDialog() {
        TextView male, female;
        ImageView closeDialog;
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.gender_dialog);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);
        dialog.show();
        male = dialog.findViewById(R.id.tv_male);
        female = dialog.findViewById(R.id.tv_female);

        if (mCoreData.getGender().equalsIgnoreCase(Constants.PANEL_GENDER_MALE)) {
            male.setBackgroundColor(getResources().getColor(R.color.orange));
            female.setBackgroundColor(getResources().getColor(R.color.white));
            Flag = true;

        } else if (mCoreData.getGender().equalsIgnoreCase(Constants.PANEL_GENDER_FEMALE)) {
            male.setBackgroundColor(getResources().getColor(R.color.white));
            female.setBackgroundColor(getResources().getColor(R.color.orange));
            Flag = true;

        } else {
            Flag = false;
        }

        male.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                male.setBackgroundColor(getResources().getColor(R.color.orange));
                female.setBackgroundColor(getResources().getColor(R.color.white));
                mCoreData.setGender(Constants.PANEL_GENDER_MALE);
                Flag = true;
                lblGender.setText("Male");
            }
        });

        female.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                male.setBackgroundColor(getResources().getColor(R.color.white));
                female.setBackgroundColor(getResources().getColor(R.color.orange));
                mCoreData.setGender(Constants.PANEL_GENDER_FEMALE);
                Flag = true;
                lblGender.setText("Female");
            }
        });
    }

    public void showCustomAlertLanguageDialog() {
        RecyclerView rv_languages;
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.language_dialog);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);

        rv_languages = dialog.findViewById(R.id.rv_languages);
        if (languageDetailsModels.size() > 0) {
            rv_languages.setAdapter(new LanguageAdapter(dialog));
            rv_languages.setLayoutManager(new LinearLayoutManager(context));
            dialog.show();
        } else {
            showErrorAlert("", "No Language");

        }


    }

    public void showCustomOTPDialog(Bundle bundle) {
        ImageView closeDialog;

        otpDialog = new Dialog(context, R.style.Theme_Dialog);
        otpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otpDialog.setCancelable(false);
        otpDialog.setContentView(R.layout.fragment_sign_up_otp);
        otpDialog.setCanceledOnTouchOutside(false);
        Window window = otpDialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        otpDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);
        otpDialog.show();

        IntentFilter i = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        getActivity().registerReceiver(SmsListener, i);

        edtOtp = otpDialog.findViewById(R.id.edtOtp);

        closeDialog = otpDialog.findViewById(R.id.close_dialog_otp);
        closeDialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getActivity().unregisterReceiver(SmsListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                otpDialog.dismiss();
            }
        });

        if (bundle != null) {
            if (bundle.containsKey(Constants.SIGNUP_KEY_OTP)) {
                startTime = 10;
            } else if (bundle.containsKey(Constants.SIGNUP_KEY_USERINFO)) {
                mCoredata = (UserInfoModel) bundle.getSerializable(Constants.SIGNUP_KEY_USERINFO);
                emailid = mCoredata != null ? mCoredata.getEmail()
                        : InformatePreferences.getStringPrefrence(getActivity(), Constants.EMAILID);
                InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_INREGISTRATIONPROCESS,
                        Constants.INREGISTRATIONPROCESS);
            }
        }

        otpDialog.findViewById(R.id.parentMessages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Utility.hideKeyboard(arg0, getActivity());
            }
        });

        otpDialog.findViewById(R.id.btn_signup_submit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!TextUtils.isEmpty(edtOtp.getOTP())) {

                    verifyEmail();
                } else {
                    showErrorAlert("", getString(R.string.error_enter_otp));
                    Utility.showKeyboard(edtOtp, getActivity());
                }
            }
        });
        TextViewPlus txtCheckMail = otpDialog.findViewById(R.id.ic_textview_checkmail);

        txtCheckMail.setText(Html.fromHtml(getString(R.string.ui_msg_checkemail)));

        txtTimer = otpDialog.findViewById(R.id.itimer);

        textviewResend = otpDialog.findViewById(R.id.txt_resend);
        textviewResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (Utility.isConnectedToInternet(getActivity())) {
                    IntentFilter i = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
                    getActivity().registerReceiver(SmsListener, i);
                    requestEmail();
                } else
                    showErrorAlert("", getString(R.string.async_task_error));
            }
        });
        TextViewPlus txtTermsCondition = otpDialog.findViewById(R.id.ic_signupContactTermscondition);
        txtTermsCondition.setText(Html.fromHtml(getResources().getString(R.string.ui_termsandcondition)));
        txtTermsCondition.setClickable(true);
        txtTermsCondition.setMovementMethod(LinkMovementMethod.getInstance());
        countDownTimer = new MyCountDownTimer(startTime, interval);
        // txtTimer.setText(txtTimer.getText() + String.valueOf(startTime /
        // 1000));
        startTimer(false);

    }

//    @Override
//    protected void showDialog(boolean isShow, String message) {
//
//        if (isShow) {
//            mProgressDialog = new ProgressDialog(getActivity());
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.setCanceledOnTouchOutside(false);
//            mProgressDialog.setMessage(message);
//            mProgressDialog.show();
//        }
//    }
//
//    @Override
//    protected void dismissDialog() {
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//
//            mProgressDialog = null;
//        }
//    }

    private void startTimer(boolean timerd) {
        this.timerHasStarted = timerd;
        if (!timerHasStarted) {
            countDownTimer.start();
            timerHasStarted = true;
            textviewResend.setVisibility(View.INVISIBLE);
        } else {
            countDownTimer.cancel();
            timerHasStarted = false;
            textviewResend.setVisibility(View.VISIBLE);
        }

    }

    private void requestEmail() {

        showDialog(true, getString(R.string.dialog_login));
        //requestTypePost(Constants.API_SENDEMAILOPT, new ParseJSonObject(getActivity()).getEmailOtp(emailid), 1);
        requestTypePost(Constants.API_SENDEMAILOPT, new ParseJSonObject(getActivity()).getEmailOtp(mCoredata != null
                ? mCoredata.getEmail() : InformatePreferences.getStringPrefrence(getActivity(), Constants.EMAILID)), 1);

    }

    private boolean isValidated() {

        String phone = edtPhoneNumber.getText().toString();
        String city = edtCity.getText().toString();
        String zipcode = edtzipCode.getText().toString();
        String firstName = edtFname.getText().toString();
        String lastname = edtlastame.getText().toString();
        String dob = btnMonth.getText().toString();

        if (TextUtils.isEmpty(firstName.trim())) {
            //showErrorAlert(getString(R.string.error), getString(R.string.error_fn));
            edtFname.setError(getString(R.string.error_fn));
            Utility.showKeyboard(edtFname, getActivity());
            edtFname.requestFocus();
        } else if (TextUtils.isEmpty(lastname.trim())) {
            //showErrorAlert(getString(R.string.error), getString(R.string.error_ln));
            edtlastame.setError(getString(R.string.error_ln));
            Utility.showKeyboard(edtlastame, getActivity());
            edtlastame.requestFocus();
        } else if (!Flag)
            showErrorAlert(getString(R.string.error), getString(R.string.error_gender));
        else if (TextUtils.isEmpty(dob)) {
            //showErrorAlert(getString(R.string.error), getString(R.string.error_dob));
            btnMonth.setError(getString(R.string.error_dob));
            btnMonth.requestFocus();
        } else if (TextUtils.isEmpty(phone)) {
            //showErrorAlert(getString(R.string.error), getString(R.string.error_phone_empty));
            btnMonth.setError(null);
            edtPhoneNumber.setError(getString(R.string.error_phone_empty));
            edtPhoneNumber.requestFocus();
            Utility.showKeyboard(edtPhoneNumber, getActivity());
        } else if (phone.length() != maxPhoneLength && mCoreData.getCountryShortCode().equalsIgnoreCase("IN")) {
            //showErrorAlert(getString(R.string.error), getString(R.string.error_phone_length));
            btnMonth.setError(null);
            edtPhoneNumber.setError(getString(R.string.error_phone_length));
            edtPhoneNumber.requestFocus();
            Utility.showKeyboard(edtPhoneNumber, getActivity());
        } else if (phone.length() < minPhoneLength && phone.length() < maxPhoneLength) {
            //showErrorAlert(getString(R.string.error), getString(R.string.error_phone_length));
            btnMonth.setError(null);
            edtPhoneNumber.setError(getString(R.string.error_phone_length));
            edtPhoneNumber.requestFocus();
            Utility.showKeyboard(edtPhoneNumber, getActivity());
        } else if (TextUtils.isEmpty(city)) {
            //showErrorAlert(getString(R.string.error), getString(R.string.error_city));
            btnMonth.setError(null);
            edtCity.setError(getString(R.string.error_city));
            edtCity.requestFocus();
        } else if (TextUtils.isEmpty(str_selected_language)) {
            lbl_country_language.setError("Please select your language");
        } else if (TextUtils.isEmpty(zipcode)) {
            //showErrorAlert(getString(R.string.error), getString(R.string.error_zipcode));
            btnMonth.setError(null);
            edtCity.setError(null);
            edtzipCode.setError(getString(R.string.error_zipcode));
            edtzipCode.requestFocus();
            Utility.showKeyboard(edtzipCode, getActivity());
        } else if (zipcode.length() != maxZipLength && mCoreData.getCountryShortCode().equalsIgnoreCase("IN")) {
            //showErrorAlert(getString(R.string.error), getString(R.string.error_zipcode_length));
            btnMonth.setError(null);
            edtCity.setError(null);
            edtzipCode.setError(getString(R.string.error_zipcode_length));
            edtzipCode.requestFocus();
            Utility.showKeyboard(edtzipCode, getActivity());
        } else if (zipcode.length() != maxZipLength && mCoreData.getCountryShortCode().equalsIgnoreCase("IN")) {
            //showErrorAlert(getString(R.string.error), getString(R.string.error_zipcode_length));
            btnMonth.setError(null);
            edtCity.setError(null);
            edtzipCode.setError(getString(R.string.error_zipcode_length));
            edtzipCode.requestFocus();
            Utility.showKeyboard(edtzipCode, getActivity());
        } else if (zipcode.length() < 2) {
            //showErrorAlert(getString(R.string.error), getString(R.string.error_zipcode_length));
            btnMonth.setError(null);
            edtCity.setError(null);
            edtzipCode.setError(getString(R.string.error_zipcode_length));
            edtzipCode.requestFocus();
            Utility.showKeyboard(edtzipCode, getActivity());
        } else if (!cb_terms_conditions.isChecked()) {
            showErrorAlert(getString(R.string.error), getString(R.string.error_terms_conditions));
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onClickRegionItem(CountryModel values) {
        this.cityValue = values;
        edtCity.setText(values.getCityDisplayname().toString());
    }

    @Override
    public void onRequestCompleted(String response, int requestCode) {
        dismissDialog();

        if (requestCode == Constants.REQUEST_REFER_VALIDATION) {
            try {
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.getBoolean("Status")) {

                    IsApplied = true;
                    finalReferalCode = edt_refer_code.getText().toString();
                    applyCode.setText("Applied");
                    applyCode.setTextColor(getResources().getColor(R.color.green));
                    applyCode.setClickable(false);
                    applyCode.setTypeface(null, Typeface.ITALIC);

                    InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_REFFERELPANELLISTID, jsonObject.optString("ReferrerPanelistId"));


                } else {
                    IsApplied = false;
                    finalReferalCode = "";
                    showErrorAlert(" ", jsonObject.getString("Message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (IsOTP) {
            otpResponse(response, requestCode);
        } else {
            parseStatus(response, requestCode);

        }
    }


    @SuppressLint("StringFormatInvalid")
    private void otpResponse(String response, int requestCode) {
        try {
            JSONObject object = new JSONObject(response);
            switch (requestCode) {
                case Constants.REQUESTCODE_GET_LANGUAGE_DETAILS:

                    break;

                case 1: // OTP sent response
                    if (object.has("Status") && object.getBoolean("Status")) {

                        try {
                            getActivity().unregisterReceiver(SmsListener);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        startTimer(false);
                        try {
                            showErrorAlert("", String.format(getString(R.string.msgcheckemailforotp), mCoredata != null ? mCoredata.getEmail()
                                    : InformatePreferences.getStringPrefrence(getActivity(), Constants.EMAILID)));

                        } catch (Exception e) {
                            showErrorAlert("", String.format(getString(R.string.msgcheckemailforotp), mCoredata != null ? ""
                                    : InformatePreferences.getStringPrefrence(getActivity(), Constants.EMAILID)));
                            e.printStackTrace();
                        }

                    } else {
                        showErrorAlert("", object.getString("Message"));
                    }
                    break;

                case Constants.REQUESTCODE_SIGNUP_USER:
                    if (object.has("Status") && object.getBoolean("Status")) {


                        Tracker.sendEvent(new Tracker.Event(Tracker.EVENT_TYPE_REGISTRATION_COMPLETE));
                        IsOTP = true;

                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_ID, object.optString("ID"));  //niyaj changes
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
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_MOBILENUMBER, object.optString("MobileNo"));
                        InformatePreferences.setStringPrefrence(context, Constants.PREF_FILEPATH, object.optString("FilePath"));
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_REFFERELCODE, object.optString("ReferralCode"));
                        if (Integer.parseInt(mCoreData.getLoginType()) == 0) { // manual
                            showCustomOTPDialog(bundle);
                        } else { // social
                            IsOTP = false;
                            _saveConsentLog(object.optString("ID"));
                            Log.e("contact_info", "launching home for first time");
                            Log.e("contact_info", "account not linked yet");
                        }
                    } else {
                        IsOTP = false;
                        showErrorAlert("", object.getString("Message"));
                    }

                    break;

                case 3: // OTP verification successful
                    if (object.has("Status") && object.getBoolean("Status")) {
                        otpDialog.dismiss();
                        try {
                            getActivity().unregisterReceiver(SmsListener);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_REFFERELCODE, object.optString("ReferralCode"));
                        IsOTP = false;
                        if (object.has("FilePath"))
                            InformatePreferences.setStringPrefrence(context, Constants.PREF_FILEPATH, object.optString("FilePath"));
                        // OTP verified, call consent log api; then go to dashboard
                        _saveConsentLog(object.optString("PanelistId"));
                    } else {
                        showErrorAlert("", object.getString("Message"));
                    }
                    break;

                case Constants.REQUESTCODE_CONSENT_LOG_SAVE:
                    JSONObject responseOB;
                    responseOB = new JSONObject(response);
                    boolean statusFlag = responseOB.optBoolean("Status");
                    InformatePreferences.putBoolean(context, Constants.PREF_IS_CONSENT_GIVEN, statusFlag);
                    if (statusFlag) {
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.putExtra(HomeActivity.KEY_REGISTER, true);
                        startActivity(intent);
                        getActivity().finish();

                        //ODMmeterStatus();
                    } else {
                        showErrorAlert("", responseOB.optString("Message"));
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseStatus(String response, int requestCode) {
        if (requestCode == Constants.REQUESTCODE_CONSENT_LOG_SAVE) {
            JSONObject responseOB;
            try {
                responseOB = new JSONObject(response);
                if (responseOB.has("Status") && responseOB.getBoolean("Status")) {
                    boolean statusFlag = responseOB.optBoolean("Status");
                    InformatePreferences.putBoolean(context, Constants.PREF_IS_CONSENT_GIVEN, statusFlag);
                    if (statusFlag) {
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.putExtra(HomeActivity.KEY_REGISTER, true);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        showErrorAlert("", responseOB.optString("Message"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                JSONObject object = new JSONObject(response);
                if (object.has("Status") && object.getBoolean("Status")) {

                    InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_ID, object.optString("ID"));
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
                    InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_MOBILENUMBER, object.optString("MobileNo"));
                    InformatePreferences.setStringPrefrence(context, Constants.PREF_FILEPATH, object.optString("FilePath"));
                    InformatePreferences.setStringPrefrence(getActivity(), Constants.PREF_REFFERELCODE, object.optString("ReferralCode"));

                    if (Integer.parseInt(mCoreData.getLoginType()) == 0) { // manual
                        if (listener != null)
                            listener.onClickView(submitBtn);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.SIGNUP_KEY_USERINFO, mCoreData);

                        showCustomOTPDialog(bundle);
                    }
                } else {
                    showErrorAlert(getString(R.string.error), object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void _saveConsentLog(String id) {
        JSONObject object1 = new JSONObject();
        SharedPreferences sharedPreferences = context.getSharedPreferences("INFORMATE", Context.MODE_PRIVATE);
        String c1 = sharedPreferences.getString(InformatePreferences.KEY_INFORMATE_VALUE + Constants.PREF_CONSENT_ONE, "");
        String c2 = sharedPreferences.getString(InformatePreferences.KEY_INFORMATE_VALUE + Constants.PREF_CONSENT_TWO, "");
        String cf = sharedPreferences.getString(InformatePreferences.KEY_INFORMATE_VALUE + Constants.PREF_CONSENT_FINAL, "");
        try {
            object1.put("ConsentText1", c1);
            object1.put("ConsentText2", c2);
            object1.put("ConsentGiven", cf);
            object1.put("PanelistId", id);
            object1.put("ConsentVersion", "v1.0");
            object1.put("IPAddress", PanelConstants.DEVICE_ID);
            object1.put("BrowserVersion", PanelConstants.DEVICE_ID);
            object1.put("BrowserExtraInfo", PanelConstants.DEVICE_ID);
            // object1.put("LanguageCulture", InformatePreferences.getStringPrefrence(context,Constants.PREF_LOCALECODE));

            showDialog(true, getString(R.string.dialog_login));
            requestTypePost(Constants.API_CONSENT_LOG_SAVE, object1, Constants.REQUESTCODE_CONSENT_LOG_SAVE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestError(Exception e, int requestCode) {
        if (IsOTP) {
            dismissDialog();
        }
    }

    @Override
    public void onRequestStarted(int requestCode) {

    }

    @Override
    public void vLayout(String res, int requestcode) {
    }

    @Override
    public void rError(int requestcode) {

    }

    @Override
    public void onDateSet(String value) {
        btnMonth.setText(value);
    }

    private void verifyEmail() {
        if (Utility.isConnectedToInternet(getActivity())) {
            showDialog(true, getString(R.string.dialog_login));
            requestTypePost(Constants.API_VERIFYEMAILOTP, new ParseJSonObject(getActivity()).verifyEmailOtp(emailid, edtOtp.getOTP()), 3);
        } else {
            showErrorAlert("", getString(R.string.async_task_error));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            setPicture(requestCode, data);
        }
    }

    public void captureImage() {
        final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.choose_from_gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.add_photo));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            takePhoto();
                        } else {
                            requestCamera();
                        }
                    } else {
                        requestStorage();
                    }
                } else if (item == 1) {
                    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        openGalleryToChoosePhoto();
                    } else
                        requestStorage();
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void requestCamera() {

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.CAMERA)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                }

            }
        }
    }

    public void requestStorage() {

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
                }
            }
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        uri = Uri.fromFile(f);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            Uri photoUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f);
            if (photoUri != null)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            uri = photoUri;
        }

        startActivityForResult(intent, 1);
    }

    private void openGalleryToChoosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }

    private void setPicture(int requestCode, Intent data) {

        if (requestCode == 1) {
            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }

            try {
                Bitmap bitmap = null;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                if (uri != null) {
                    base64_1 = base64conversion(ImageCompressor.imageCompression(bitmap, getActivity()));
                } else {
                    base64_1 = "";
                }

                setPhoto(uri);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 2) {

            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = context.getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            uri = selectedImage;
            if (uri != null) {
                base64_1 = base64conversion(ImageCompressor.imageCompression(thumbnail, getActivity()));
            } else {
                base64_1 = "";
            }

            setPhoto(uri);
        }
    }

    private void setPhoto(Uri absoluteFile) {
        if (absoluteFile != null) {
            Picasso.get().load(absoluteFile).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).transform(new CircleTransform()).into(profilePic);
        } else {
            profilePic.setImageResource(R.drawable.ic_profile);
        }
    }

    private String base64conversion(Bitmap bm) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            txtTimer.setText("");
            textviewResend.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (isAdded()) {
                txtTimer.setText(getResources().getString(R.string.resendactivationcode) + " " + String.format("%d " + getResources().getString(R.string.min) + " %d " + getResources().getString(R.string.sec), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }
        }
    }

    private class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
        private Dialog dialog;

        LanguageAdapter(Dialog dialog) {
            this.dialog = dialog;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.searchlist_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (languageDetailsModels != null && !languageDetailsModels.isEmpty()) {
                holder.ic_searchlist_name.setText(languageDetailsModels.get(position).getLanguageName());

                holder.ic_searchlist_name.setOnClickListener((view -> {
                    lbl_country_language.setText(languageDetailsModels.get(holder.getAdapterPosition()).getLanguageName());
                    str_selected_language = String.valueOf(languageDetailsModels.get(holder.getAdapterPosition()).getLanguageID());
                    dialog.dismiss();
                }));
            }
        }

        @Override
        public int getItemCount() {
            return languageDetailsModels.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView ic_searchlist_name;

            public ViewHolder(View itemView) {
                super(itemView);
                ic_searchlist_name = itemView.findViewById(R.id.ic_searchlist_name);
            }
        }
    }


}
