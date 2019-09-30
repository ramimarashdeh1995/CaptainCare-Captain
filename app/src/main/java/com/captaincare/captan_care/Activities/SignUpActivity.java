package com.captaincare.captan_care.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 0;
    private EditText editTextName, editTextPhone, editTextPassword, editTextConfirmPassword;
    private TextView txtErrorMessage, textContactTitle, textContractDisc1;
    private Spinner spinnerCity;
    private ImageView imageLicense;
    private Button btnLogin, btnSignUp, btnNext;
    private ConstraintLayout constraintLayout;
    private ProgressBar progressSigUp, progressContract;

    private LinearLayout linearContract, linearSignUp;
    private CheckBox checkContract;

    private static final int imgGallery = 1001;
    private static final int imgCamera = 1002;

    private Uri imageUri;


    PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerification;
    private FirebaseAuth authPhoneNumber;

    private ProgressDialog progressDialogCode;
    private ProgressDialog progressDialogSuccessfulRegister;

    private Bitmap bitmap;

    private View view;
    private AlertDialog.Builder dialogVerification, dialogSuccessful, dialogSignUp;
    private AlertDialog alertDialogVerification, alertDialogSucecessful;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        defineElements();

    }

    private void defineElements() {
        constraintLayout = findViewById(R.id.constraint_layout);

        linearContract = findViewById(R.id.linearContract);
        linearSignUp = findViewById(R.id.linearSignUp);

        progressContract = findViewById(R.id.progressContract);

        textContactTitle = findViewById(R.id.textContractTitle);
        textContractDisc1 = findViewById(R.id.tatContractDescription);

        checkContract = findViewById(R.id.checkContract);
        btnNext = findViewById(R.id.btnNext);

        editTextName = findViewById(R.id.txtName);
        editTextPhone = findViewById(R.id.edtPhone);
        editTextPassword = findViewById(R.id.txtPass);
        editTextConfirmPassword = findViewById(R.id.edtConfirmPass);

        txtErrorMessage = findViewById(R.id.txtErrorMessage);
        spinnerCity = findViewById(R.id.spinnerCity);

        imageLicense = findViewById(R.id.license_image);
        progressSigUp = findViewById(R.id.progress);
        dialogSignUp = new AlertDialog.Builder(SignUpActivity.this);

        btnLogin = findViewById(R.id.button_login);
        btnSignUp = findViewById(R.id.button_signUp);

        authPhoneNumber = FirebaseAuth.getInstance();

        progressDialogCode = new ProgressDialog(SignUpActivity.this, R.style.ProgressDialogTheme);
        progressDialogSuccessfulRegister = new ProgressDialog(SignUpActivity.this, R.style.ProgressDialogTheme);
        progressDialogSuccessfulRegister.setCanceledOnTouchOutside(false);
        progressDialogCode.setCanceledOnTouchOutside(false);

        RetrieveContract();

    }

    private void RetrieveContract() {
        progressContract.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.Contract,
                response -> {
                    progressContract.setVisibility(View.GONE);
                    linearContract.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            JSONObject object = jsonObject.getJSONObject("message");
                            textContactTitle.setText(object.getString("contract_title"));
                            textContractDisc1.setText(object.getString("contract_disc1") + object.getString("contract_disc2"));

                            NextToContract();

                        } else {
                            Snackbar.make(SignUpActivity.this.findViewById(R.id.constraint_layout),
                                    SignUpActivity.this.getString(R.string.problemHappened), Snackbar.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> {
                    Snackbar.make(findViewById(R.id.constraint_layout),
                            SignUpActivity.this.getString(R.string.problemHappened), Snackbar.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("contract", "contract");
                return parms;
            }
        };
        RequestHand.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void NextToContract() {
        btnNext.setVisibility(View.GONE);
        checkContract.setOnClickListener(v -> {
            if (checkContract.isChecked()) {
                btnNext.setVisibility(View.VISIBLE);
            } else if (!checkContract.isChecked()) {
                btnNext.setVisibility(View.GONE);
            }
        });
        btnNext.setOnClickListener(v -> {
            linearContract.setVisibility(View.GONE);
            linearSignUp.setVisibility(View.VISIBLE);
            elementsEvents();
        });

    }

    private void elementsEvents() {
        constraintLayout.setOnClickListener(null);
        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editTextPhone.setSelection(editTextPhone.getText().length());
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
            validationElements();
        });
        btnLogin.setOnClickListener(v -> {
            SignUpActivity.this.startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            SignUpActivity.this.finish();
        });
        findViewById(R.id.relative).setOnClickListener(v -> {
            String[] item = {getResources().getString(R.string.camera), getResources().getString(R.string.gallery)};
            AlertDialog.Builder builderPicture = new AlertDialog.Builder(SignUpActivity.this);
            builderPicture.setTitle(getString(R.string.choose));
            builderPicture.setItems(item, (dialog, which) -> {
                switch (which) {
                    case 0:
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                        break;
                    case 1:
                        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        SignUpActivity.this.startActivityForResult(intentGallery, imgGallery);
                        dialog.dismiss();
                        break;
                }
            });
            builderPicture.show();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera, imgCamera);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }
        }

    }

    private void validationElements() {
        if (editTextName.getText().toString().isEmpty()) {
            editTextName.setError(getResources().getString(R.string.requierd_field));
            editTextName.requestFocus();
        } else if (editTextPhone.getText().toString().equals("+9627")) {
            editTextPhone.setError(getResources().getString(R.string.requierd_field));
            editTextPhone.requestFocus();
        } else if (editTextPhone.getText().toString().length() < 13) {
            editTextPhone.setError(getResources().getString(R.string.enter_valid_phone));
            editTextPhone.requestFocus();
        } else if (editTextPassword.getText().toString().isEmpty()) {
            editTextPassword.setError(getResources().getString(R.string.requierd_field));
            editTextPassword.requestFocus();
        } else if (editTextPassword.getText().toString().length() < 6) {
            editTextPassword.setError(getResources().getString(R.string.enter_valid_pass));
            editTextPassword.requestFocus();
        } else if (!editTextConfirmPassword.getText().toString().equals(editTextPassword.getText().toString())) {
            editTextConfirmPassword.setError(getResources().getString(R.string.password_does_not_match));
            editTextConfirmPassword.requestFocus();
        } else if (bitmap == null) {
            Toast.makeText(this, "Select image license", Toast.LENGTH_SHORT).show();
            imageLicense.requestFocus();
        } else {
            disableElements();
            btnSignUp.setVisibility(View.GONE);
            progressSigUp.setVisibility(View.VISIBLE);
            checkMobilePhone(editTextPhone.getText().toString());
        }
    }

    private void disableElements() {
        editTextName.setEnabled(false);
        editTextPhone.setEnabled(false);
        editTextPassword.setEnabled(false);
        editTextConfirmPassword.setEnabled(false);
        spinnerCity.setEnabled(false);
        imageLicense.setEnabled(false);
        btnLogin.setEnabled(false);
        btnSignUp.setEnabled(false);
    }

    private void checkMobilePhone(final String phoneNumber) {
        txtErrorMessage.setVisibility(View.GONE);
        progressSigUp.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.SignUp_Captain,
                response -> {
                    progressSigUp.setVisibility(View.GONE);
                    btnSignUp.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            //Phone Number not Exist in Database
                            SignUpActivity.this.sendCode(phoneNumber);
                        } else if (jsonObject.getString("error").equals("true")) {
                            switch (jsonObject.getString("message")) {
                                case "Phone Number Already exist":
                                    txtErrorMessage.setVisibility(View.VISIBLE);
                                    txtErrorMessage.setText(R.string.phone_number_registered);
                                    SignUpActivity.this.enableElements();
                                    break;
                                case "Phone number already register .. Please wait to Active Account ":
                                    dialogSignUp.setIcon(R.drawable.ic_info_black);
                                    dialogSignUp.setTitle(R.string.alert_message);
                                    dialogSignUp.setMessage(R.string.messageNumbertemp);
                                    dialogSignUp.setPositiveButton(R.string.ok, null);
                                    dialogSignUp.show();
                                    SignUpActivity.this.enableElements();
                                    break;
                                default:
                                    Snackbar.make(SignUpActivity.this.findViewById(R.id.constraint_layout),
                                            SignUpActivity.this.getString(R.string.problemHappened), Snackbar.LENGTH_SHORT).show();
                                    SignUpActivity.this.enableElements();
                                    break;
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressSigUp.setVisibility(View.GONE);
                    txtErrorMessage.setVisibility(View.GONE);
                    btnSignUp.setVisibility(View.VISIBLE);
                    enableElements();
                    Snackbar.make(findViewById(R.id.constraint_layout), R.string.check_internet_connection, Snackbar.LENGTH_SHORT).show();

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(API_Key.cap_mobile, phoneNumber);
                return params;
            }
        };
        RequestHand.getInstance(this).addToRequestQueue(stringRequest);
    }

    //Send Verification Code From Firebase
    private void sendCode(String phoneNumber) {
        progressDialogCode.setMessage(getResources().getString(R.string.sending_code));
        progressDialogCode.show();
        txtErrorMessage.setVisibility(View.GONE);

        PhoneAuthProvider authProvider = PhoneAuthProvider.getInstance();
        authProvider.verifyPhoneNumber(phoneNumber, 30, TimeUnit.SECONDS, SignUpActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                progressDialogCode.dismiss();
                showVerificationDialog();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressDialogCode.dismiss();
                enableElements();
//                Snackbar.make(findViewById(R.id.constraint_layout), R.string.check_phone_number, Snackbar.LENGTH_SHORT).show();
                txtErrorMessage.setVisibility(View.VISIBLE);
                txtErrorMessage.setText(R.string.check_phone_number);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                progressDialogCode.dismiss();
                showVerificationDialog();
                mVerification = s;
                mResendToken = forceResendingToken;
            }
        });
    }

    //This Dialog is shown when Verification Code is sent
    @SuppressLint("InflateParams")
    private void showVerificationDialog() {
        view = LayoutInflater.from(SignUpActivity.this).inflate(R.layout.dialog_verification, null);
        dialogVerification = new AlertDialog.Builder(SignUpActivity.this);
        dialogVerification.setCancelable(false);
        dialogVerification.setView(view);
        alertDialogVerification = dialogVerification.create();
        alertDialogVerification.show();
        checkCode();
    }

    //Check Code that insert from ic_profile_defaulttt
    private void checkCode() {
        EditText editTextCode = view.findViewById(R.id.txtCode);
        Button btnVerification = view.findViewById(R.id.button_verification);

        btnVerification.setOnClickListener(v -> {
            String code = editTextCode.getText().toString();
            if (code.isEmpty()) {
                editTextCode.setError(getResources().getString(R.string.requierd_field));
                editTextCode.requestFocus();
            } else {
                progressDialogCode.setMessage(getResources().getString(R.string.checking_code));
                progressDialogCode.show();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerification, code);
                signInWithPhoneAuthCredential(credential);
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressDialogCode.dismiss();
        TextView txtError = view.findViewById(R.id.txtErrorMessage);
        txtError.setVisibility(View.GONE);
        authPhoneNumber.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                createUser();
            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                txtError.setVisibility(View.VISIBLE);
                txtError.setText(R.string.verification_code_error);
            }
        });
    }

    private void createUser() {
        txtErrorMessage.setVisibility(View.GONE);
        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String password = editTextPassword.getText().toString();
        String city = spinnerCity.getSelectedItem().toString();

        progressDialogCode.setMessage(getString(R.string.Send_ruquest));
        progressDialogCode.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.SignUp_Captain,
                response -> {
                    if (alertDialogVerification != null)
                        alertDialogVerification.dismiss();
                    enableElements();
                    progressDialogCode.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            showSuccessfulDialog();
                        } else if (jsonObject.getString("error").equals("true")) {
                            switch (jsonObject.getString("message")) {
                                case "A problem happened":
                                    Snackbar.make(findViewById(R.id.constraint_layout),
                                            getString(R.string.problemHappened), Snackbar.LENGTH_SHORT).show();
                                    break;
                                case "Phone Number Already exist":
                                    txtErrorMessage.setVisibility(View.VISIBLE);
                                    txtErrorMessage.setText(R.string.phone_number_registered);
                                    break;
                                case "Phone number already register .. Please wait to Active Account":
                                    dialogSignUp.setIcon(R.drawable.ic_info_black);
                                    dialogSignUp.setTitle("");
                                    dialogSignUp.setMessage(R.string.messageNumbertemp);
                                    dialogSignUp.setPositiveButton(R.string.ok, null);
                                    dialogSignUp.show();
                                    enableElements();
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    enableElements();
                    txtErrorMessage.setVisibility(View.GONE);
                    progressDialogCode.dismiss();
                    Snackbar.make(findViewById(R.id.constraint_layout), R.string.check_internet_connection, Snackbar.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(API_Key.cap_name, name);
                params.put(API_Key.cap_password, password);
                params.put(API_Key.cap_mobile, phone);
                params.put(API_Key.cap_city, city);
                params.put(API_Key.cap_lic_url, image_to_string(bitmap));
                params.put("cap_token", ""/*ShareProfileData.getInstance(SignUpActivity.this).getDeviceToken()*/);
                return params;
            }
        };
        RequestHand.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void showSuccessfulDialog() {
        dialogSuccessful = new AlertDialog.Builder(this);
        dialogSuccessful.setTitle(getString(R.string.showSuccessful));
        dialogSuccessful.setIcon(R.drawable.ic_successfull);
        dialogSuccessful.setMessage(getString(R.string.messageSuccessful));
        dialogSuccessful.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            dialog.dismiss();
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
        dialogSuccessful.setCancelable(false);
        dialogSuccessful.show();
        clearElementsData();
    }

    private void enableElements() {
        btnLogin.setEnabled(true);
        imageLicense.setEnabled(true);
        editTextConfirmPassword.setEnabled(true);
        editTextPassword.setEnabled(true);
        editTextPhone.setEnabled(true);
        spinnerCity.setEnabled(true);
        editTextName.setEnabled(true);
        btnSignUp.setEnabled(true);
    }

    private void clearElementsData() {
        editTextName.setText("");
        editTextPhone.setText("");
        editTextPassword.setText("");
        editTextConfirmPassword.setText("");
        imageLicense.setImageResource(R.drawable.ic_add_a_photo);
    }


    private String image_to_string(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        byte[] imggByte = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(imggByte, android.util.Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case imgGallery:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    imageUri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        imageLicense.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case imgCamera:
                if (resultCode == RESULT_OK && data != null) {
                    bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    imageLicense.setImageBitmap(bitmap);
                    imageLicense.setScaleType(ImageView.ScaleType.FIT_XY);
                }
                break;
        }

    }
}
