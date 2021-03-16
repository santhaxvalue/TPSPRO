package com.panelManagement.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.panelManagement.activity.HomeActivity;
import com.panelManagement.activity.R;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;
import com.panelManagement.webservices.ParseJSonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.panelManagement.utils.Constants.REQUEST_GET_UNSUBSCRIBE_REASONS;


public class AlertContactUsFragment extends BaseFragment implements OnClickListener {
    private final int PERMISSION_REQUEST_CAMERA = 1;
    private final int PERMISSION_REQUEST_STORAGE = 2;
    Spinner subjectSpinner;
    ScrollView scrollView;
    ArrayList<String> listdata;
    TextView imageName;
    private EditText edt_subject, edt_message;
    private Uri uri;
    private Bitmap bitmapImage = null;
    private String base64_1 = "";
    private String fileName = "";

    public static AlertContactUsFragment newInstance() {
        Bundle args = new Bundle();
        AlertContactUsFragment fragment = new AlertContactUsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        getActivity().findViewById(R.id.header_my_rewards).setVisibility(View.GONE);
        HomeActivity.setSurvayBackground();
        getActivity().findViewById(R.id.header_survey_and_redeem).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_redemption_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_reward_history).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_pointsinreview).setVisibility(View.GONE);
        getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
        getActivity().findViewById(R.id.iv_back_left_arrow).setClickable(true);
        FrameLayout layout = getActivity().findViewById(R.id.main_container_fragment);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        layout.setLayoutParams(params);

        listdata = new ArrayList<>();
        edt_subject = view.findViewById(R.id.edt_messagesubject);
        edt_message = view.findViewById(R.id.edt_tellusmore);
        subjectSpinner = view.findViewById(R.id.sp_subject);
        view.findViewById(R.id.btn_send).setOnClickListener(this);
        view.findViewById(R.id.attach).setOnClickListener(this);
        imageName = view.findViewById(R.id.attached_file_name);

        showDialog(true, getString(R.string.dialog_login));
        requestTypeGET(Constants.API_CONTACTUS_LIST,
                Constants.REQUEST_CONTACT_SUBJECT_LIST);
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(getContext(), subjectSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                if (subjectSpinner != null && subjectSpinner.getSelectedItem() != null) {
                    if (position == 10) {
                        edt_subject.setText("");
                        edt_subject.setVisibility(View.VISIBLE);
                    } else {
                        edt_subject.setText(subjectSpinner.getSelectedItem().toString());
                        edt_subject.setVisibility(View.GONE);
                    }
                } else {
                    edt_subject.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        scrollView = view.findViewById(R.id.contact_us_layout);
        Animation hide = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        hide.setDuration(1000);
        scrollView.setAnimation(hide);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_send:
                if (TextUtils.isEmpty(edt_subject.getText().toString())) {
                    showErrorAlert(getString(R.string.error), getString(R.string.msg_subject));
                } else if (TextUtils.isEmpty(edt_message.getText().toString())) {
                    showErrorAlert(getString(R.string.error), getString(R.string.msg_message));
                } else {

                    try {
                        if (Utility.isConnectedToInternet(getActivity())) {
                            showDialog(true, getString(R.string.dialog_login));
                            requestTypePost(Constants.API_CONTACTUS, new ParseJSonObject(getActivity()).getContactUSObject(edt_subject.getText().toString(),
                                    edt_message.getText().toString(), base64_1), Constants.REQUESTCODE_CONTACTUS);
                        } else {
                            showErrorAlert("", getString(R.string.msg_low_conn));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.attach:
                captureImage();
                break;

            default:
                break;
        }

    }

    @Override
    public void vLayout(String res, int requestcode) {
        dismissDialog();

        switch (requestcode) {
            case Constants.REQUESTCODE_CONTACTUS:
                try {
                    JSONObject object = new JSONObject(res);
                    if (object.getString("Status").equals("true")) {
                        showCustomAlertDialog(/*getResources().getString(R.string.thanks_dialog_txt)*/object.getString("Message"));
                    } else {
                        showErrorAlert("", object.getString("Message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.REQUEST_CONTACT_SUBJECT_LIST:
                try {

                    JSONObject object = new JSONObject(res);
                    if (object.getBoolean("Status")) {
                        JSONArray ques = object.getJSONArray("lstContactUS");
                        if (ques != null && ques.length() != 0) {
                            JSONObject object1;
                            for (int i = 0; i < ques.length(); i++) {
                                object1 = ques.getJSONObject(i);
                                listdata.add(object1.getString("Question"));
                            }
                            _setSpinnerData();
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

    private void _setSpinnerData() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listdata);
        /*new String[]{"PIN", "LINK", "EDIT", "OTHERS"});*/ //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(spinnerArrayAdapter);
    }

    public void showCustomAlertDialog(String s) {
        TextView rewards;
        ImageView closeDialog;
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.contact_us_dialog);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = .5f;
        window.setAttributes(wlp);
        dialog.show();
        rewards = dialog.findViewById(R.id.tv_thanks_msg);
        rewards.setText(s);
        closeDialog = dialog.findViewById(R.id.close_dialog_contact_us);

        closeDialog.setOnClickListener((View v) -> {
            dialog.dismiss();
            getActivity().findViewById(R.id.profile_image).setVisibility(View.VISIBLE);
            TextView user_name = getActivity().findViewById(R.id.user_name);
            user_name.setText(String.format("%s %s", InformatePreferences.getStringPrefrence(context, Constants.PREF_FIRSTNAME), InformatePreferences.getStringPrefrence(context, Constants.PREF_LASTNAME)));
            user_name.setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.email_id).setVisibility(View.GONE);
            getActivity().findViewById(R.id.phone_ll).setVisibility(View.GONE);
            getActivity().findViewById(R.id.tv_page_title).setVisibility(View.GONE);
            getActivity().findViewById(R.id.ll_profile_details).setVisibility(View.VISIBLE);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack("my_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.main_container_fragment, new AvailableSurveyFragment());
            fragmentTransaction.commit();
        });
    }

    @Override
    public void rError(int requestcode) {

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


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            //uri = intent.getArrayData();
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
                fileName = f.getName();
                imageName.setText(fileName);

                bitmapImage = Bitmap.createScaledBitmap(bitmap, 512, 432, true);

                if (uri != null) {
                    base64_1 = base64conversion(bitmapImage);
                } else {
                    base64_1 = "";
                }

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
            fileName = picturePath.substring(picturePath.lastIndexOf("/") + 1);
            imageName.setText(fileName);
            c.close();
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            Bitmap scaled = Bitmap.createScaledBitmap(thumbnail, 512, 432, true);

            uri = selectedImage;

            bitmapImage = scaled;

            if (uri != null) {
                base64_1 = base64conversion(bitmapImage);
            } else {
                base64_1 = "";
            }
        }
    }

    private String base64conversion(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

}