package com.captaincare.captan_care.Fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.captaincare.captan_care.Activities.MainActivity;
import com.captaincare.captan_care.Manegers.ShareProfileData;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {
    private EditText editNowPassword, editTextPassword, editTextConfirmPassword;
    private ProgressBar progressBarPass;
    private Button btnSave;

    AlertDialog.Builder edtPassDialog;


    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.customToolbar);
        TextView txtTitle = toolbar.findViewById(R.id.txtTitle);
        ImageView imgBack = toolbar.findViewById(R.id.imgBack);
        txtTitle.setText(R.string.change_password);
        imgBack.setOnClickListener(v -> {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingFragment()).commit();
        });
        edtPassDialog = new AlertDialog.Builder(getActivity());

        editNowPassword = view.findViewById(R.id.txtNowPassword);
        editTextPassword = view.findViewById(R.id.edtPassword);
        editTextConfirmPassword = view.findViewById(R.id.edtConfirmPass);
        btnSave = view.findViewById(R.id.button_save);
        progressBarPass = view.findViewById(R.id.progress);

        btnSave.setOnClickListener(v -> {

            if (editNowPassword.getText().toString().length() < 6) {
                editNowPassword.setError("Password must be 6 number");
                editNowPassword.requestFocus();
            } else if (editTextPassword.getText().toString().isEmpty()) {
                editTextPassword.setError("Required Field");
                editTextPassword.requestFocus();
            } else if (!editTextConfirmPassword.getText().toString().equals(editTextPassword.getText().toString())) {
                editTextConfirmPassword.setError("Repeat Password Not Correct");
                editTextConfirmPassword.requestFocus();
            } else {
                btnSave.setEnabled(false);
                progressBarPass.setVisibility(View.VISIBLE);
                UpdatePassword();
            }
        });

        return view;
    }

    private void UpdatePassword() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConnectionSrever.UbdateNewPassword,
                response -> {
                    progressBarPass.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            edtPassDialog.setTitle("Password changed successfully");
                            edtPassDialog.setCancelable(false);
                            edtPassDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    editTextConfirmPassword.setText("");
                                    editTextPassword.setText("");
                                    editNowPassword.setText("");
                                }
                            });
                            edtPassDialog.show();
                        } else if (jsonObject.getString("error").equals("true") && jsonObject.getString("message").equals("error pass")) {
                            editNowPassword.setError("Check Password");
                            editNowPassword.requestFocus();
                        } else {
                            Toast.makeText(
                                    getActivity(),
                                    "Error",
                                    Toast.LENGTH_LONG
                            ).show();
                            btnSave.setEnabled(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("cap_id", ShareProfileData.getInstance(getActivity()).getKEYID());
                parms.put("passwordNow", editNowPassword.getText().toString());
                parms.put("NewPassword", editTextPassword.getText().toString());
                return parms;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setToolbarVisibility(View.GONE);
    }
}
