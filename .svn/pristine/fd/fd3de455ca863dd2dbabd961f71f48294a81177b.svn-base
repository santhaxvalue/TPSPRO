package com.panelManagement.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;

import com.panelManagement.fragment.SignupContactInfoFragment;
import com.panelManagement.model.UserInfoModel;
import com.panelManagement.utils.Constants;

public class RegistrationActivity extends FragmentActivity {

    Context context;
    FrameLayout frameLayout;
    Bundle bundle;
    UserInfoModel userInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        context = RegistrationActivity.this;
        frameLayout = findViewById(R.id.ic_register_container);

        bundle = new Bundle();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Constants.SIGNUP_KEY_USERINFO)) {
                bundle.putSerializable(Constants.SIGNUP_KEY_USERINFO, (UserInfoModel) intent.getSerializableExtra(Constants.SIGNUP_KEY_USERINFO));
            }

            if (intent.hasExtra("flag")) {
                bundle.putBoolean("flag", intent.getBooleanExtra("flag", true));
            }
        }
        if (bundle != null) {
            getSupportFragmentManager().beginTransaction().add(R.id.ic_register_container, SignupContactInfoFragment.newInstance(bundle)).commit();
        }

    }
}
