package com.captaincare.captan_care.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.API_Key;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;
import com.captaincare.captan_care.Manegers.ShareProfileData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextPhone, editTextPassword;
    private TextView txtErrorMessage;
    private Button btnSignUp, btnLogin, btnForgetPassword;
    private ConstraintLayout constraintLayout;
    private ProgressBar progressBarLogin;
    private AlertDialog.Builder dialogLogin;

    @SuppressLint("StaticFieldLeak")
    public static Activity activityCaptainLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activityCaptainLogin = this;
        defineElements();

    }

    private void defineElements() {

        constraintLayout = findViewById(R.id.constraint_layout);

        btnSignUp = findViewById(R.id.button_signUp);
        btnLogin = findViewById(R.id.button_login);
        btnForgetPassword = findViewById(R.id.button_forgetPass);

        editTextPhone = findViewById(R.id.edtPhone);
        editTextPassword = findViewById(R.id.edtPassword);

        txtErrorMessage = findViewById(R.id.txtErrorMessage);

        progressBarLogin = findViewById(R.id.progress);
        dialogLogin = new AlertDialog.Builder(LoginActivity.this);

        elementsEvents();
    }

    private void elementsEvents() {
        constraintLayout.setOnClickListener(null);
        editTextPhone.setSelection(editTextPhone.getText().length());
        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("+9627")) {
                    editTextPhone.setText("+9627");
                    editTextPhone.setSelection(editTextPhone.getText().length());
                }
            }
        });
        btnSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
        btnLogin.setOnClickListener(v -> {
            if (editTextPhone.getText().toString().equals("+9627")) {
                editTextPhone.setError("Required Field");
                editTextPhone.requestFocus();
            } else if (editTextPhone.getText().toString().length() < 13) {
                editTextPhone.setError("Phone must be 13 number");
                editTextPhone.requestFocus();
            } else if (editTextPassword.getText().toString().isEmpty()) {
                editTextPassword.setError("Required Field");
                editTextPassword.requestFocus();
            } else {
                progressBarLogin.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.GONE);

                String phone = editTextPhone.getText().toString();
                String password = editTextPassword.getText().toString();
                checkUserLogin(phone, password);
            }

        });
        btnForgetPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
        });
    }

    private void checkUserLogin(final String phone, final String password) {
        //TODO : Code that check phone and password if exist or not from database
        txtErrorMessage.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConnectionSrever.LogIn_Captain,
                response -> {
                    progressBarLogin.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        switch (jsonObject.getString("error")) {
                            case "false":
                                JSONObject object = jsonObject.getJSONObject("message");
                                //Retrieve Data From Database for current ic_profile_default and store it in Shared Reference
                                ShareProfileData.getInstance(LoginActivity.this).LogInCaptain(
                                        object.getString("cap_id"),
                                        object.getString("cap_mobile"),
                                        object.getString("cap_lic_url")

                                );
                                ShareProfileData.setSerialID(this, object.getString("cap_username"));
                                ShareProfileData.getInstance(LoginActivity.this).UpdateProfileCaptain(
                                        object.getString("cap_name"),
                                        object.getString("cap_mobile"),
                                        object.getString("cap_city"),
                                        object.getString("cap_photo_url")
                                );
                                ShareProfileData.getInstance(LoginActivity.this).SetReatAndPoint(
                                        object.getString("eval"),
                                        object.getString("cap_point")
                                );
                                ShareProfileData.getInstance(LoginActivity.this).LOGIN("1");
                                Intent I = new Intent(LoginActivity.this, MainActivity.class);
                                I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (ForgetPasswordActivity.activityCaptainForgetPass == null) {
                                    LoginActivity.activityCaptainLogin.finish();
                                } else {
                                    LoginActivity.activityCaptainLogin.finish();
                                    ForgetPasswordActivity.activityCaptainForgetPass.finish();
                                }
                                SendTokenCaptain();

                                startActivity(I);
                                finish();
                                break;
                            case "true":
                                switch (jsonObject.getString("message")) {
                                    case "Phone number already register .. Please wait to Active Account":
                                        dialogLogin.setIcon(R.drawable.ic_info_black);
                                        dialogLogin.setTitle("");
                                        dialogLogin.setMessage(R.string.messageNumbertemp);
                                        dialogLogin.setPositiveButton(R.string.ok, null);
                                        dialogLogin.show();
                                        break;
                                    default:
                                        txtErrorMessage.setVisibility(View.VISIBLE);
                                        txtErrorMessage.setText(R.string.check_Phone_or_pasword);
                                        break;
                                }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    txtErrorMessage.setVisibility(View.GONE);
                    progressBarLogin.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                    Snackbar.make(LoginActivity.this.findViewById(R.id.constraint_layout), R.string.check_internet_connection, Snackbar.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(API_Key.cap_mobile, phone);
                params.put(API_Key.cap_password, password);
                return params;
            }
        };
        RequestHand.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
    }
    private void SendTokenCaptain() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.UpdateTokenCaptain,
                response -> {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params=new HashMap<>();
                params.put("cap_id", ShareProfileData.getInstance(LoginActivity.this).getKEYID());
                params.put("token",ShareProfileData.getInstance(LoginActivity.this).getDeviceToken());
                return params;
            }
        };
        RequestHand.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
    }

}
