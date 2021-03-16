package com.panelManagement.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.panelManagement.activity.R;
import com.panelManagement.listener.OnClickAction;
import com.panelManagement.listener.OnDateListener;
import com.panelManagement.listener.OnprofileCaptureTypeListener;
import com.panelManagement.model.DateOfBirthModel;
import com.panelManagement.model.UserInfoModel;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.StringTokenizer;

public class SignupProfileInfoFragment extends BaseFragment implements OnClickListener, OnDateListener, OnprofileCaptureTypeListener {
    OnClickAction listener = null;
    UserInfoModel coreData = null;
    private EditText edtFname;
    private EditText edtlastame;
    private CheckBox checkMale;
    private CheckBox checkFemale;
    private EditText btnMonth;
    // private Button btnDay;
    // private Button btnYear;
    private ImageView profilePic;
    private FragmentManager fm;
    private Uri imageUri;
    private Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getActivity().getSupportFragmentManager();
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(Constants.SIGNUP_KEY_USERINFO)) {
                coreData = (UserInfoModel) bundle.getSerializable(Constants.SIGNUP_KEY_USERINFO);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_profile_info, null);
        view.findViewById(R.id.ic_parentview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyboard(v, getActivity());
            }
        });
        view.findViewById(R.id.btn_profileinfo_next).setOnClickListener(this);
        profilePic = view.findViewById(R.id.ic_social_profile_image);
        profilePic.setOnClickListener(this);
        edtFname = view.findViewById(R.id.edt_personal_info_firstname);
        edtlastame = view.findViewById(R.id.edt_personal_info_lastname);
        checkMale = view.findViewById(R.id.signupProfile_checkbox_male);
        checkFemale = view.findViewById(R.id.signupProfile_checkbox_female);
        checkFemale.setOnClickListener(this);
        checkMale.setOnClickListener(this);
        btnMonth = view.findViewById(R.id.signupProfile_btn_month);
        btnMonth.setOnClickListener(this);
        if (coreData != null) {
            initUserInfo(view);
        }
        return view;
    }

    private void initUserInfo(View view) {
        if (!TextUtils.isEmpty(coreData.getProfilePic())) {
            new GetImageFromUrl().execute(coreData.getProfilePic());
        }
        edtFname.setText(TextUtils.isEmpty(coreData.getFirstName()) ? "" : coreData.getFirstName());
        edtlastame.setText(TextUtils.isEmpty(coreData.getLastName()) ? "" : coreData.getLastName());
        if (coreData.getGender().equalsIgnoreCase(Constants.PANEL_GENDER_MALE))
            checkMale.setChecked(true);
        else if (coreData.getGender().equalsIgnoreCase(Constants.PANEL_GENDER_FEMALE))
            checkFemale.setChecked(true);
        DateOfBirthModel dateofBirthObject = coreData.getDateofbirth();
        if (dateofBirthObject != null) {
            try {
                btnMonth.setText(dateofBirthObject.getMonth() + " " + dateofBirthObject.getDay() + "," + dateofBirthObject.getYear());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
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
        switch (v.getId()) {
            case R.id.btn_profileinfo_next:
                if (isValidated()) {
                    String firstName = edtFname.getText().toString();
                    String lastname = edtlastame.getText().toString();
                    coreData.setFirstName(firstName);
                    coreData.setLastName(lastname);
                    String dob = btnMonth.getText().toString();
                    StringTokenizer st = new StringTokenizer(dob.trim(), " ,");
                    int month = DatePickerFragment.getmonthfinal();
                    int day = DatePickerFragment.getDay();
                    int year = DatePickerFragment.getYear();
                    coreData.setDateofbirth(new DateOfBirthModel(String.valueOf(month), String.valueOf(day), String.valueOf(year)));
                    if (checkFemale.isChecked())
                        coreData.setGender(Constants.PANEL_GENDER_FEMALE);
                    else
                        coreData.setGender(Constants.PANEL_GENDER_MALE);
                    if (listener != null)
                        listener.onClickView(v);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    SignupContactInfoFragment mFragment = new SignupContactInfoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.SIGNUP_KEY_USERINFO, coreData);
                    mFragment.setArguments(bundle);
                    transaction.replace(R.id.ic_tabview_container, mFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;
            case R.id.signupProfile_checkbox_male:
                coreData.setGender(Constants.PANEL_GENDER_MALE);
                checkFemale.setChecked(false);
                break;
            case R.id.signupProfile_checkbox_female:
                coreData.setGender(Constants.PANEL_GENDER_FEMALE);
                checkMale.setChecked(false);
                break;
            case R.id.signupProfile_btn_month:
                showDatePicker();
                break;
        }
    }

    private boolean isValidated() {
        String firstName = edtFname.getText().toString();
        String lastname = edtlastame.getText().toString();
        String dob = btnMonth.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            //showErrorAlert(getString(R.string.error), getString(R.string.error_fn));
            Utility.showKeyboard(edtFname, getActivity());
            edtFname.requestFocus();
        } else if (TextUtils.isEmpty(lastname)) {
            //showErrorAlert(getString(R.string.error), getString(R.string.error_ln));
            Utility.showKeyboard(edtlastame, getActivity());
            edtlastame.requestFocus();
        } else if (!checkFemale.isChecked() && !checkMale.isChecked())
            showErrorAlert(getString(R.string.error), getString(R.string.error_gender));
        else if (TextUtils.isEmpty(dob))
            showErrorAlert(getString(R.string.error), getString(R.string.error_dob));
        else
            return true;
        return false;
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
        date.show(fm, "Date Picker");
    }

    @Override
    public void onDateSet(String value) {
        btnMonth.setText(value);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaColumns.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onPickerSelection(int value) {
        Intent intent;
        switch (value) {
            case AlertProfilePicFragment.PICK_CAMERA:
                ContentValues values = new ContentValues();
                values.put(MediaColumns.TITLE, "New Picture");
                values.put(ImageColumns.DESCRIPTION, "From your Camera");
                imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, AlertProfilePicFragment.PICK_CAMERA);
                break;
            case AlertProfilePicFragment.PICK_GALLERY:
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);
                intent.putExtra("outputX", 0);
                intent.putExtra("outputY", 0);
                try {
                    intent.putExtra("return-data", true);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), AlertProfilePicFragment.PICK_GALLERY);
                } catch (ActivityNotFoundException e) {
                }

                break;

        }
    }

    @Override
    public void vLayout(String res, int requestcode) {
    }

    @Override
    public void rError(int requestcode) {
    }

    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null)
                profilePic.setImageBitmap(Utility.getRoundedShape(result));
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString) throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }
}
