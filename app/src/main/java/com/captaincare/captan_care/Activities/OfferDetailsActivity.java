package com.captaincare.captan_care.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.captaincare.captan_care.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class OfferDetailsActivity extends AppCompatActivity {
    private TextView txtOfferTitle, txtOfferDes, txtOfferCity, txtOfferEndTime,txtOfferPrice;
    private TextView endTime;
    private ImageButton img1, img2, img3, img4;
    private Button btnAcceptOffer;
    private Bitmap bitmap1, bitmap2, bitmap3, bitmap4;

    private View viewImage;
    private AlertDialog.Builder dialogImage, dialogAcceptOffet;

    private SharedPreferences offerPreferences;

    private String offerImg1, offerImg2, offerImg3, offerImg4, offerTitle,
            offerDescription, offerCity, offerService, offerEnd,offerPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        offerPreferences = getSharedPreferences("OfferData", Context.MODE_PRIVATE);
        offerImg1 = offerPreferences.getString("key_pic1", null);
        offerImg2 = offerPreferences.getString("key_pic2", null);
        offerImg3 = offerPreferences.getString("key_pic3", null);
        offerImg4 = offerPreferences.getString("key_pic4", null);
        offerTitle = offerPreferences.getString("key_title", null);
        offerDescription = offerPreferences.getString("key_des", null);
        offerCity = offerPreferences.getString("key_city", null);
        offerService = offerPreferences.getString("key_servicetype", null);
        offerEnd = offerPreferences.getString("key_offerend", null);
        offerPrice = offerPreferences.getString("key_price", null);

        txtOfferTitle = findViewById(R.id.txtOfferTitle);
        txtOfferDes = findViewById(R.id.txtOfferDes);
        txtOfferCity = findViewById(R.id.txtOfferCity);
        txtOfferEndTime = findViewById(R.id.txtOfferEndTime);
        txtOfferPrice=findViewById(R.id.txtOfferPrice);

        endTime = findViewById(R.id.endTime);
        btnAcceptOffer = findViewById(R.id.button_acceptOffer);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);

        endTime.setText(getString(R.string.offer_end_time));
        getSupportActionBar().setTitle(R.string.offer_details);
        showOfferData(offerTitle, offerCity, offerDescription, offerService, offerEnd,
                offerImg1, offerImg2, offerImg3, offerImg4,offerPrice);
    }
    private void showOfferData(String offerTitle, String offerCity, String offerDescription,
                               String offerService, String offerEnd, String offerImg1,
                               String offerImg2, String offerImg3, String offerImg4,String price) {

        txtOfferTitle.setText(offerTitle);
        txtOfferDes.setText(offerDescription);
        txtOfferCity.setText(offerCity);
        txtOfferEndTime.setText(offerEnd);
        txtOfferPrice.setText(price+" JD");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            if ((offerImg1 != null) && (!offerImg1.equals(""))) {
                img1.setVisibility(View.VISIBLE);
                URL url1 = new URL(offerImg1);
                HttpURLConnection connectionImage1 = (HttpURLConnection) url1.openConnection();
                connectionImage1.setDoInput(true);
                connectionImage1.connect();
                InputStream input1 = connectionImage1.getInputStream();
                bitmap1 = BitmapFactory.decodeStream(input1);
            } else {
                img1.setVisibility(View.GONE);
            }

            if ((offerImg2 != null) && (!offerImg2.equals(""))) {
                img2.setVisibility(View.VISIBLE);
                URL url2 = new URL(offerImg2);
                HttpURLConnection connectionImage2 = (HttpURLConnection) url2.openConnection();
                connectionImage2.setDoInput(true);
                connectionImage2.connect();
                InputStream input2 = connectionImage2.getInputStream();
                bitmap2 = BitmapFactory.decodeStream(input2);
            } else {
                img2.setVisibility(View.GONE);
            }
            if ((offerImg3 != null) && (!offerImg3.equals(""))) {
                img3.setVisibility(View.VISIBLE);
                URL url3 = new URL(offerImg3);
                HttpURLConnection connectionImage3 = (HttpURLConnection) url3.openConnection();
                connectionImage3.setDoInput(true);
                connectionImage3.connect();
                InputStream input3 = connectionImage3.getInputStream();
                bitmap3 = BitmapFactory.decodeStream(input3);
            } else {
                img3.setVisibility(View.GONE);
            }

            if ((offerImg4 != null) && (!offerImg4.equals(""))) {
                img4.setVisibility(View.VISIBLE);
                URL url4 = new URL(offerImg4);
                HttpURLConnection connectionImage4 = (HttpURLConnection) url4.openConnection();
                connectionImage4.setDoInput(true);
                connectionImage4.connect();
                InputStream input4 = connectionImage4.getInputStream();
                bitmap4 = BitmapFactory.decodeStream(input4);
            } else {
                img4.setVisibility(View.GONE);
            }

            img1.setImageBitmap(bitmap1);
            img1.setMaxHeight(200);
            img1.setScaleType(ImageView.ScaleType.FIT_XY);

            img2.setImageBitmap(bitmap2);
            img2.setMaxHeight(200);
            img2.setScaleType(ImageView.ScaleType.FIT_XY);

            img3.setImageBitmap(bitmap3);
            img3.setMaxHeight(200);
            img3.setScaleType(ImageView.ScaleType.FIT_XY);

            img4.setImageBitmap(bitmap4);
            img4.setMaxHeight(200);
            img4.setScaleType(ImageView.ScaleType.FIT_XY);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img1:
                showFullImage(offerImg1);
                break;
            case R.id.img2:
                showFullImage(offerImg2);
                break;
            case R.id.img3:
                showFullImage(offerImg3);
                break;
            case R.id.img4:
                showFullImage(offerImg4);
                break;

        }
    }
    private void showFullImage(String imgUrl) {
        viewImage = LayoutInflater.from(OfferDetailsActivity.this).inflate(R.layout.dialog_show_image, null);
        dialogImage = new AlertDialog.Builder(OfferDetailsActivity.this);
        dialogImage.setView(viewImage);
        ImageView imageView = viewImage.findViewById(R.id.showImage);
        Picasso.with(OfferDetailsActivity.this).load(imgUrl).into(imageView);
        dialogImage.show();

    }



}
