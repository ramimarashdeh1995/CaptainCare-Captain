package com.captaincare.captan_care.Fragments.Profile;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {
    private EditText editName, edtSerialNumber, edtPhoneNumber;
    private CircleImageView imageProfile;
    private Spinner spinnerCity;
    private Button btnUpdate;

    private static final int imgGallery = 1001;
    private static final int imgCamera = 1002;
    private Uri imageUri;
    private Bitmap bitmap;
    private ProgressBar progress;

    RelativeLayout changePhoto;

    private AlertDialog.Builder editDialog;


    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.customToolbar);
        TextView txtTitle = toolbar.findViewById(R.id.txtTitle);
        ImageView imgBack = toolbar.findViewById(R.id.imgBack);
        txtTitle.setText(R.string.edit_profile);
        imgBack.setOnClickListener(v -> {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
        });

        editName = view.findViewById(R.id.txtName);
        edtPhoneNumber = view.findViewById(R.id.edtPhone);
        edtSerialNumber = view.findViewById(R.id.txtSerialNumber);
        spinnerCity = view.findViewById(R.id.spinnerCity);

        imageProfile = view.findViewById(R.id.imageProfile);
        btnUpdate = view.findViewById(R.id.button_update);
        progress = view.findViewById(R.id.progress);

        changePhoto = view.findViewById(R.id.changePhoto);
        editDialog = new AlertDialog.Builder(getActivity());

        String[] cities = getResources().getStringArray(R.array.Cities);
        String city = ShareProfileData.getInstance(getActivity()).getKeyCity();

        for (int i = 0; i < cities.length; i++) {
            if (city.equals(cities[i])) {
                spinnerCity.setSelection(i);
                break;
            }
        }

        editName.setText(ShareProfileData.getInstance(getActivity()).getKeyName());
        edtPhoneNumber.setText(ShareProfileData.getInstance(getActivity()).getKeyPhone());
        edtSerialNumber.setText(ShareProfileData.getSerialID());

        if (!ShareProfileData.getInstance(getActivity()).getKeyprofile_URL().equals("null")) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url1 = null;
            try {
                url1 = new URL(ShareProfileData.getInstance(getActivity()).getKeyprofile_URL());
                HttpURLConnection connectionImage1 = (HttpURLConnection) url1.openConnection();
                connectionImage1.setDoInput(true);
                connectionImage1.connect();
                InputStream input1 = connectionImage1.getInputStream();
                bitmap = BitmapFactory.decodeStream(input1);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageProfile.setImageBitmap(bitmap);
        }


        changePhoto.setOnClickListener(v -> {
            String[] item = {"Camera", "Gallery"};
            AlertDialog.Builder builderPicture = new AlertDialog.Builder(getActivity());
            builderPicture.setTitle("Choose");
            builderPicture.setItems(item, (dialog, which) -> {
                switch (which) {
                    case 0:
                        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        this.startActivityForResult(intentCamera, imgCamera);
                        dialog.dismiss();
                        break;
                    case 1:
                        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        this.startActivityForResult(intentGallery, imgGallery);
                        dialog.dismiss();
                        break;
                }
            });
            builderPicture.show();
        });


        btnUpdate.setOnClickListener(v -> {
            btnUpdate.setEnabled(false);
            UpdateProfileData();
        });


        return view;
    }

    private void UpdateProfileData() {
        KProgressHUD kProgressHUD = KProgressHUD.create(getActivity());
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Updating data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.UpdateProfileCaptain,
                response -> {
                    kProgressHUD.dismiss();
                    btnUpdate.setEnabled(true);
                    try {
                        ReadJSONCaptain(response);
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
                parms.put("cap_name", editName.getText().toString());
                parms.put("image", image_to_string(bitmap));
                parms.put("city", spinnerCity.getSelectedItem().toString());
                return parms;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void ReadJSONCaptain(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.getString("error").equals("false")) {
            JSONArray jsonArray = jsonObject.getJSONArray("message");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                ShareProfileData.getInstance(getActivity()).UpdateProfileCaptain(
                        object.getString("cap_name"), object.getString("cap_mobile"),
                        object.getString("cap_city"), object.getString("cap_photo_url"));
                editDialog.setTitle("Update successfully");
                editDialog.setCancelable(false);
                editDialog.setPositiveButton(R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                });
                editDialog.show();
                btnUpdate.setEnabled(true);
            }
        } else if (jsonObject.getString("error").equals("true")) {
            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
        }
    }

    private String image_to_string(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        Bitmap.createScaledBitmap(bitmap, 200, 200, false);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        byte[] imggByte = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(imggByte, android.util.Base64.DEFAULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case imgGallery:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    imageUri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                        imageProfile.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case imgCamera:
                if (resultCode == RESULT_OK && data != null) {
                    bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    imageProfile.setImageBitmap(bitmap);
                }
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).setToolbarVisibility(View.GONE);
    }
}
