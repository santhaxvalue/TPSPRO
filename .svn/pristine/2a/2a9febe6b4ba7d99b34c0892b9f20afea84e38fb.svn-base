package com.panelManagement.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.panelManagement.activity.R;
import com.panelManagement.activity.SplashScreenActivity;
import com.panelManagement.model.IdReason;
import com.panelManagement.utils.Constants;
import com.panelManagement.utils.InformatePreferences;
import com.panelManagement.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.panelManagement.utils.Constants.REQUEST_UNSUBSCRIBE_DELETE_USER;
import static com.panelManagement.utils.Constants.REQUEST_UNSUBSCRIBE_USER;
import static com.panelManagement.utils.InformatePreferences.KEY_INFORMATE_VALUE;

public class UnsubscribeFragment extends BaseFragment {
    private RecyclerView rv_reasons;
    private ArrayList<IdReason> reasonArrayList;
    private Button btn_btn_unsubscribe_delete;
    private Button btn_unsubscribe;
    private EditText et_other_reason;
    private Context mContext;
    private boolean mIsChecked;

    public UnsubscribeFragment() {
    }

    public static UnsubscribeFragment newInstance(ArrayList<IdReason> reasonArrayList) {
        UnsubscribeFragment unsubscribeFragment = new UnsubscribeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("reasons", reasonArrayList);
        unsubscribeFragment.setArguments(bundle);
        return unsubscribeFragment;
    }

    @Override
    public void vLayout(String res, int requestcode) {
        try {
            JSONObject jsonObject = new JSONObject(res);
            boolean status = jsonObject.optBoolean("Status");
            String message = jsonObject.optString("Message");

            switch (requestcode) {
                case REQUEST_UNSUBSCRIBE_USER:

                    if (status) {
                        _showUnsubscribeSuccessfulDialog();
                    } else {
                        showErrorAlert("", message);
                    }
                    break;

                case REQUEST_UNSUBSCRIBE_DELETE_USER:

                    if (status) {
                        _showUnsubscribeDeleteSuccessfulDialog();
                    } else {
                        showErrorAlert("", message);
                    }
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rError(int requestcode) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        if (getArguments() != null) {
            reasonArrayList = (ArrayList<IdReason>) getArguments().getSerializable("reasons");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unsubscribe, container, false);
        rv_reasons = view.findViewById(R.id.rv_reasons);
        et_other_reason = view.findViewById(R.id.et_other_reason);
        btn_unsubscribe = view.findViewById(R.id.btn_unsubscribe);
        btn_btn_unsubscribe_delete = view.findViewById(R.id.btn_unsubscribe_delete);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (reasonArrayList != null) {
            rv_reasons.setLayoutManager(new LinearLayoutManager(mContext));
            rv_reasons.setAdapter(new ReasonAdapter(reasonArrayList));
            rv_reasons.setNestedScrollingEnabled(false);
        }

        btn_unsubscribe.setOnClickListener(v -> {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < reasonArrayList.size(); i++) {
                if (reasonArrayList.get(i).isTicked()) {
                    stringBuilder.append(reasonArrayList.get(i).getId()).append(",");
                }
            }
            if (mIsChecked && TextUtils.isEmpty((et_other_reason.getText().toString()))) {
                showErrorAlert("", getString(R.string.others_cannot_be_blank));
            } else {
                _callUnsubscribeApi(et_other_reason.getText().toString(), stringBuilder.toString().trim());
            }

        });

        btn_btn_unsubscribe_delete.setOnClickListener(v -> {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < reasonArrayList.size(); i++) {
                if (reasonArrayList.get(i).isTicked()) {
                    stringBuilder.append(reasonArrayList.get(i).getId()).append(",");
                }
            }
            if (mIsChecked && TextUtils.isEmpty((et_other_reason.getText().toString()))) {
                showErrorAlert("", getString(R.string.others_cannot_be_blank));
            } else {
                openDialog(et_other_reason.getText().toString(), stringBuilder.toString().trim());
            }

        });
    }

    private void openDialog(String otherReason, String ids) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View layoutView = getActivity().getLayoutInflater().inflate(R.layout.dialog_unsubscribe_statements, null);
        Button btn_ok =  layoutView.findViewById(R.id.btn_ok);
        Button btn_cancel =  layoutView.findViewById(R.id.btn_cancel);
        dialogBuilder.setView(layoutView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _callUnsubscribeDeleteApi(otherReason, ids);
                alertDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();
    }

    private class ReasonAdapter extends RecyclerView.Adapter<ReasonAdapter.ViewHolder> {
        private ArrayList<IdReason> reasonArrayList;

        ReasonAdapter(ArrayList<IdReason> reasonArrayList) {
            this.reasonArrayList = reasonArrayList;
        }

        @NonNull
        @Override
        public ReasonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ReasonAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reasons, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ReasonAdapter.ViewHolder holder, int position) {
            holder.cb_reason.setText(reasonArrayList.get(position).getReason());

            holder.cb_reason.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    reasonArrayList.get(position).setTicked(true);
                } else
                    reasonArrayList.get(position).setTicked(false);

                if (position == 7) {
                    mIsChecked = isChecked;
                    if (isChecked) {
                        et_other_reason.setVisibility(View.VISIBLE);
                    } else
                        et_other_reason.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return this.reasonArrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private CheckBox cb_reason;

            ViewHolder(View itemView) {
                super(itemView);
                cb_reason = itemView.findViewById(R.id.cb_reason);
            }
        }
    }

}
