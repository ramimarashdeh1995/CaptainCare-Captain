package com.captaincare.captan_care.Activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.captaincare.captan_care.Adapters.gridViewAdapter;
import com.captaincare.captan_care.Models.SubCategoryClass;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;
import com.captaincare.captan_care.Manegers.ShareProfileData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class AddPaidOfferActivity extends AppCompatActivity {

    private ImageButton imgSelectImage;
    private GridView gridGallery;
    private Button  btnAddOffer;
    private EditText editTextTitle, editTextDes, editTextPrice, editTextPhone, editTextAddress;
    private Spinner spinnerService, spinnerCity;
    private ConstraintLayout constraintLayout;
    private ProgressBar progressBar;

    private RadioGroup radioOfferTime;
    private String offerTime;
    private AlertDialog.Builder dialogOffer;

    private static final int imgRequestCode = 1001;
    private ArrayList<String> imagesList;
    private String imageEncoded;
    private com.captaincare.captan_care.Adapters.gridViewAdapter gridViewAdapter;

    private ArrayList<SubCategoryClass> categoryArrayList;
    private String[] CategoryName_AR;
    private String[] CategoryName_EN;

    private String subCatId;




    private Bitmap bitmap;
    private ArrayList<Bitmap> mArrayUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_paid_offer);
        getSupportActionBar().setTitle(R.string.request_offer);

        imgSelectImage = findViewById(R.id.imgSelectImage);

        constraintLayout = findViewById(R.id.constraint_layout);

        editTextTitle = findViewById(R.id.txtOfferTitle);
        editTextDes = findViewById(R.id.txtOfferDes);
        editTextPrice = findViewById(R.id.txtOfferPrice);
        editTextPhone = findViewById(R.id.edtPhone);
        editTextAddress = findViewById(R.id.txtAddress);


        btnAddOffer = findViewById(R.id.button_addRequest);
        spinnerService = findViewById(R.id.spinnerService);
        spinnerCity = findViewById(R.id.spinnerCity);

        radioOfferTime = findViewById(R.id.radioEndTime);


        gridGallery = findViewById(R.id.gridView);

        progressBar = findViewById(R.id.progress);

        categoryArrayList = new ArrayList<>();
        mArrayUri = new ArrayList<>();

        editTextPhone.setText(ShareProfileData.getInstance(this).getKeyPhone());
        String[] cities = getResources().getStringArray(R.array.Cities);
        String city = ShareProfileData.getInstance(this).getKeyCity();

        for (int i = 0; i < cities.length; i++) {
            if (city.equals(cities[i])) {
                spinnerCity.setSelection(i);
                break;
            }
        }

        elementsEvent();

        retrieveAllService();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void elementsEvent() {
        constraintLayout.setOnClickListener(null);
        imgSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), imgRequestCode);
        });

        radioOfferTime.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio2:
                    offerTime = "2";
                    break;
                case R.id.radio4:
                    offerTime = "4";
                    break;
                case R.id.radio12:
                    offerTime = "12";
                    break;
                case R.id.radio1:
                    offerTime = "24";
                    break;
            }
        });
        btnAddOffer.setOnClickListener(v -> {
            if (editTextTitle.getText().toString().isEmpty()) {
                editTextTitle.setError("Required Field");
                editTextTitle.requestFocus();
            } else if (editTextDes.getText().toString().isEmpty()) {
                editTextDes.setError("Required Field");
                editTextDes.requestFocus();
            } else if (editTextPrice.getText().toString().isEmpty()) {
                editTextPrice.setError("Required Field");
                editTextPrice.requestFocus();
            } else {
                disableElements();
                addCaptainOffer();
            }
        });
    }

    private void addCaptainOffer() {
        progressBar.setVisibility(View.VISIBLE);
        subCatId = String.valueOf(categoryArrayList.get(spinnerService.getSelectedItemPosition()).getSubCategoryID());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.AddPushOffer_Captain,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            dialogOffer = new AlertDialog.Builder(AddPaidOfferActivity.this);
                            dialogOffer.setTitle(getString(R.string.successful_offer));
                            dialogOffer.setIcon(R.drawable.ic_successfull);
                            dialogOffer.setPositiveButton(R.string.ok,  (dialog,which)->{
                                dialog.dismiss();
                                AddPaidOfferActivity.this.finish();
                            });
                            dialogOffer.show();
                        } else if (jsonObject.getString("error").equals("true")) {
                            if (jsonObject.getString("message").equals("not your have cc")){
                                dialogOffer = new AlertDialog.Builder(AddPaidOfferActivity.this);
                                dialogOffer.setTitle("Failed , Your not have CC");
                                dialogOffer.setIcon(R.drawable.ic_error2);
                                dialogOffer.setPositiveButton(R.string.ok, null);
                                dialogOffer.show();
                            }else {
                                dialogOffer = new AlertDialog.Builder(AddPaidOfferActivity.this);
                                dialogOffer.setTitle("Failed , Try Again after minutes");
                                dialogOffer.setIcon(R.drawable.ic_error2);
                                dialogOffer.setPositiveButton(R.string.ok, null);
                                dialogOffer.show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    enableElements();
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(findViewById(R.id.constraint_layout), R.string.check_internet_connection, Snackbar.LENGTH_SHORT).show();

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cap_id", ShareProfileData.getInstance(AddPaidOfferActivity.this).getKEYID());
                params.put("sub_id", subCatId);
                params.put("city", spinnerCity.getSelectedItem().toString());
                params.put("offer_title", editTextTitle.getText().toString());
                params.put("offer_disc", editTextDes.getText().toString());
                params.put("offer_pic1", mArrayUri.size() < 1 ? "" : image_to_string(mArrayUri.get(0)));
                params.put("offer_pic2", mArrayUri.size() < 2 ? "" : image_to_string(mArrayUri.get(1)));
                params.put("offer_pic3", mArrayUri.size() < 3 ? "" : image_to_string(mArrayUri.get(2)));
                params.put("offer_pic4", mArrayUri.size() < 4 ? "" : image_to_string(mArrayUri.get(3)));
                params.put("offer_pic5", mArrayUri.size() < 5 ? "" : image_to_string(mArrayUri.get(4)));
                params.put("offer_pic6", mArrayUri.size() < 6 ? "" : image_to_string(mArrayUri.get(5)));
                params.put("offer_end", offerTime);
                params.put("address",editTextAddress.getText().toString());
                params.put("cost", editTextPrice.getText().toString());
                return params;
            }
        };
        RequestHand.getInstance(AddPaidOfferActivity.this).addToRequestQueue(stringRequest);

    }

    private void disableElements() {
        editTextDes.setEnabled(false);
        editTextTitle.setEnabled(false);
        spinnerCity.setEnabled(false);
        spinnerService.setEnabled(false);
        imgSelectImage.setEnabled(false);
        btnAddOffer.setEnabled(false);
    }

    private void enableElements() {
        editTextDes.setEnabled(true);
        editTextTitle.setEnabled(true);
        spinnerCity.setEnabled(true);
        spinnerService.setEnabled(true);
        imgSelectImage.setEnabled(true);
        btnAddOffer.setEnabled(true);
    }

    private void retrieveAllService() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.SelectAllSubCategory,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("message");
                            categoryArrayList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                SubCategoryClass subCategoryClass = new SubCategoryClass(
                                        object.getString("s_id"),
                                        object.getString("c_id"),
                                        object.getString("sub_name_ar"),
                                        object.getString("sub_name"));

                                categoryArrayList.add(subCategoryClass);
                            }

                            CategoryName_AR = new String[categoryArrayList.size()];
                            CategoryName_EN = new String[categoryArrayList.size()];

                            for (int i = 0; i < categoryArrayList.size(); i++) {
                                CategoryName_EN[i] = categoryArrayList.get(i).getSubcategoryName_En();
                                CategoryName_AR[i] = categoryArrayList.get(i).getSubcategoryName_Ar();
                            }
                            Paper.init(this);
                            String language = Paper.book().read("language");
                            if (language.equals("en")) {
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(AddPaidOfferActivity.this, android.R.layout.simple_spinner_item, CategoryName_EN);
                                spinnerService.setAdapter(dataAdapter);
                                dataAdapter.notifyDataSetChanged();
                            } else if (language.equals("ar")) {
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(AddPaidOfferActivity.this, android.R.layout.simple_list_item_1, CategoryName_AR);
                                spinnerService.setAdapter(dataAdapter);
                                dataAdapter.notifyDataSetChanged();
                            }
                        } else if (jsonObject.getString("error").equals("true")) {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Snackbar.make(findViewById(R.id.constraint_layout), R.string.check_internet_connection, Snackbar.LENGTH_SHORT).show();

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sub_category", "sub_category");
                return params;
            }
        };
        RequestHand.getInstance(AddPaidOfferActivity.this).addToRequestQueue(stringRequest);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == imgRequestCode && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            imagesList = new ArrayList<String>();

            //TODO >>>> #1
            if (data.getData() != null) {

                Uri mImageUri = data.getData();

                // Get the cursor
                Cursor cursor = getContentResolver().query(mImageUri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imageEncoded = cursor.getString(columnIndex);
                cursor.close();
                // Move to first row
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                    mArrayUri = new ArrayList<>();
                    mArrayUri.add(bitmap);
                    gridViewAdapter = new gridViewAdapter(getApplicationContext(), mArrayUri);
                    gridGallery.setAdapter(gridViewAdapter);
                } catch (IOException e) {
                    e.printStackTrace();
                }



              /*  gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                        .getLayoutParams();
                mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);*/

                //TODO >>>> #2
            } else {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            mArrayUri.add(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // Get the cursor
                        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageEncoded = cursor.getString(columnIndex);
                        imagesList.add(imageEncoded);
                        cursor.close();

                        gridViewAdapter = new gridViewAdapter(getApplicationContext(), mArrayUri);
                        gridGallery.setAdapter(gridViewAdapter);
                        gridGallery.setVerticalSpacing(gridGallery.getHorizontalSpacing());
                      /*  ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                .getLayoutParams();
                        mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);**/

                    }
                    Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                }
            }
        } else {
            Toast.makeText(this, "You haven't picked Image",
                    Toast.LENGTH_LONG).show();
        }
    }

    private String image_to_string(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] imggByte = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(imggByte, android.util.Base64.DEFAULT);
    }

}
