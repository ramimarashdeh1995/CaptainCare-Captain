package com.captaincare.captan_care.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.captaincare.captan_care.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.captaincare.captan_care.ServerClass.API_Key;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;
import com.captaincare.captan_care.Manegers.ShareProfileData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText editTextPhone, editTextPassword, editTextConfirmPassword;
    private TextView txtErrorMessage;
    private Button btnSave;
    private LinearLayout linearLayout;
    private ProgressBar progressBarForgetPass;

    private ProgressDialog progressDialogSendCode;
    private ProgressDialog progressDialogCheckCode;
    private ProgressDialog progressDialog;

    PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerification;
    private FirebaseAuth authPhoneNumber;

    private View view, viewProgress;
    private AlertDialog.Builder dialogForgetPass , dialogProgress;
    private AlertDialog alertDialog;

    @SuppressLint("StaticFieldLeak")
    public static Activity activityCaptainForgetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        activityCaptainForgetPass = this;

        editTextPhone = findViewById(R.id.edtPhone);
        editTextPassword = findViewById(R.id.edtPassword);
        editTextConfirmPassword = findViewById(R.id.edtConfirmPass);

        btnSave = findViewById(R.id.button_save);

        linearLayout = findViewById(R.id.linear_layout);

        txtErrorMessage = findViewById(R.id.txtErrorMessage);

        progressDialogSendCode = new ProgressDialog(ForgetPasswordActivity.this, R.style.ProgressDialogTheme);
        progressDialogCheckCode = new ProgressDialog(ForgetPasswordActivity.this, R.style.ProgressDialogTheme);
        progressDialogCheckCode.setCanceledOnTouchOutside(false);
        progressDialogSendCode.setCanceledOnTouchOutside(false);

        progressBarForgetPass = findViewById(R.id.progress);

        authPhoneNumber = FirebaseAuth.getInstance();

        linearLayout.setOnClickListener(null);
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
        btnSave.setOnClickListener(v -> {
            if (editTextPhone.getText().toString().equals("+9627")) {
                editTextPhone.setError("Required Field");
                editTextPhone.requestFocus();
            } else if (editTextPhone.getText().toString().length() < 13) {
                editTextPhone.setError("Phone must be 13 number");
                editTextPhone.requestFocus();
            } else if (editTextPassword.getText().toString().isEmpty()) {
                editTextPassword.setError("Required Field");
                editTextPassword.requestFocus();
            } else if (!editTextConfirmPassword.getText().toString().equals(editTextPassword.getText().toString())) {
                editTextConfirmPassword.setError("Repeat Password Not Correct");
                editTextConfirmPassword.requestFocus();
            } else {
                progressBarForgetPass.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.GONE);
                checkPhone(editTextPhone.getText().toString());
            }
        });

    }

    private void checkPhone(String phone) {
        txtErrorMessage.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.SignUp_Captain,
                response -> {
                    progressBarForgetPass.setVisibility(View.GONE);
                    btnSave.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            //Phone not Exist in database
                            txtErrorMessage.setVisibility(View.VISIBLE);
                            txtErrorMessage.setText(R.string.check_phone_number);
                        } else if (jsonObject.getString("error").equals("true")) {
                            switch (jsonObject.getString("message")) {
                                //Phone Number is Exist
                                case "Phone Number Already exist":
                                    sendCode(phone);
                                    break;
                                //Phone Number is Register and not Active
                                case "Phone number already register .. Please wait to Active Account ":
                                    dialogForgetPass = new AlertDialog.Builder(this);
                                    dialogForgetPass.setIcon(R.drawable.ic_info_black);
                                    dialogForgetPass.setTitle("");
                                    dialogForgetPass.setMessage(R.string.messageNumbertemp);
                                    dialogForgetPass.setPositiveButton(R.string.ok, null);
                                    dialogForgetPass.show();
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressBarForgetPass.setVisibility(View.GONE);
                    btnSave.setVisibility(View.VISIBLE);
                    txtErrorMessage.setVisibility(View.GONE);
                    Snackbar.make(findViewById(R.id.linear_layout), R.string.check_internet_connection, Snackbar.LENGTH_SHORT).show();

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(API_Key.cap_mobile, phone);
                return params;
            }
        };
        RequestHand.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void sendCode(String phone) {
        txtErrorMessage.setVisibility(View.GONE);
        progressDialogSendCode.setMessage("Sending Code ...");
        progressDialogSendCode.show();

        PhoneAuthProvider authProvider = PhoneAuthProvider.getInstance();
        authProvider.verifyPhoneNumber(phone, 30, TimeUnit.SECONDS, ForgetPasswordActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                progressDialogSendCode.dismiss();
                showVerificationDialog();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressDialogSendCode.dismiss();
                txtErrorMessage.setVisibility(View.VISIBLE);
                txtErrorMessage.setText(R.string.check_phone_number);

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                progressDialogSendCode.dismiss();
                mVerification = s;
                mResendToken = forceResendingToken;
                showVerificationDialog();
            }
        });
    }

    @SuppressLint("InflateParams")
    private void showVerificationDialog() {
        view = LayoutInflater.from(ForgetPasswordActivity.this).inflate(R.layout.dialog_verification, null);
        dialogForgetPass = new AlertDialog.Builder(ForgetPasswordActivity.this);
        dialogForgetPass.setCancelable(false);
        dialogForgetPass.setView(view);
        alertDialog = dialogForgetPass.create();
        alertDialog.show();
        checkCode();
    }

    private void checkCode() {
        EditText editTextCode = view.findViewById(R.id.txtCode);
        Button btnVerification = view.findViewById(R.id.button_verification);
        btnVerification.setOnClickListener(v -> {
            String code = editTextCode.getText().toString();
            if (code.isEmpty()) {
                editTextCode.setError("Required Field");
                editTextCode.requestFocus();
            } else {
                progressDialogCheckCode.setMessage("Checking Code ...");
                progressDialogCheckCode.show();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerification, code);
                signInWithPhoneAuthCredential(credential);
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressDialogCheckCode.dismiss();
        TextView txtError = view.findViewById(R.id.txtErrorMessage);
        txtError.setVisibility(View.GONE);
        authPhoneNumber.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                saveNewPassword();
            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                txtError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void saveNewPassword() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        progressBarForgetPass.setVisibility(View.VISIBLE);
        //TODO : save new password depends on mobile number
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.UpdatePassword,
                response -> {
                    progressBarForgetPass.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        switch (jsonObject.getString("error")) {
                            case "false":
//                                CaptainCheckLoginClass.getInstance(ForgetPasswordActivity.this, progressBarForgetPass).checkLogin(
//                                        editTextPhone.getText().toString(), editTextPassword.getText().toString()
//                                );
                                CaptainCheckLoginClass(editTextPhone.getText().toString(), editTextPassword.getText().toString());
                                break;
                            case "true":
                                Snackbar.make(findViewById(R.id.linear_layout), R.string.problemHappened, Snackbar.LENGTH_SHORT).show();
                                break;

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                   /* if (alertDialog != null) {
                        alertDialog.dismiss();
                    }*/
                    progressBarForgetPass.setVisibility(View.GONE);
                    Snackbar.make(findViewById(R.id.linear_layout), R.string.check_internet_connection, Snackbar.LENGTH_SHORT).show();

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(API_Key.cap_mobile, editTextPhone.getText().toString());
                params.put(API_Key.cap_password, editTextPassword.getText().toString());
                return params;
            }
        };
        RequestHand.getInstance(ForgetPasswordActivity.this).addToRequestQueue(stringRequest);
    }

    private void CaptainCheckLoginClass(String phone, String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.LogIn_Captain,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            switch (jsonObject.getString("error")) {
                                case "false":
                                    JSONObject object = jsonObject.getJSONObject("message");
                                    //Retrieve Data From Database for current ic_profile_default and store it in Shared Reference
                                    ShareProfileData.getInstance(ForgetPasswordActivity.this).LogInCaptain(
                                            object.getString("cap_id"),
                                            object.getString("cap_mobile"),
                                            object.getString("cap_lic_url")
                                    );
                                    ShareProfileData.getInstance(ForgetPasswordActivity.this).UpdateProfileCaptain(
                                            object.getString("cap_name"),
                                            object.getString("cap_mobile"),
                                            object.getString("cap_city"),
                                            object.getString("cap_photo_url"));
                                    ShareProfileData.getInstance(ForgetPasswordActivity.this).SetReatAndPoint(
                                            object.getString("eval"),
                                            object.getString("cap_point")
                                    );
                                    ShareProfileData.getInstance(ForgetPasswordActivity.this).LOGIN("1");
                                    //  SendTokenCaptain();
                                    Intent I = new Intent(ForgetPasswordActivity.this, MainActivity.class);
                                    I.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    if (ForgetPasswordActivity.activityCaptainForgetPass == null) {
                                        LoginActivity.activityCaptainLogin.finish();
                                    } else {
                                        LoginActivity.activityCaptainLogin.finish();
                                        ForgetPasswordActivity.activityCaptainForgetPass.finish();
                                    }
                                    startActivity(I);
                                    break;
                                case "true":
                                    switch (jsonObject.getString("message")) {
                                        case "Phone number already register .. Please wait to Active Account":
//
                                            break;
                                        default:
//
                                            break;

                                    }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(API_Key.cap_mobile, phone);
                params.put(API_Key.cap_password, password);
                return params;
            }
        };
        RequestHand.getInstance(ForgetPasswordActivity.this).addToRequestQueue(stringRequest);
    }

}
