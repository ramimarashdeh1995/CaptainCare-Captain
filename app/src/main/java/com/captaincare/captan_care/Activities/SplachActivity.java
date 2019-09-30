package com.captaincare.captan_care.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.captaincare.captan_care.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;
import com.captaincare.captan_care.Manegers.ShareProfileData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SplachActivity extends AppCompatActivity {

    int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);

        ImageView imageSplach = findViewById(R.id.img_splach);
        progress=0;
        final ProgressBar progressBar = findViewById(R.id.progress);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }
                    String token = task.getResult().getToken();
                    String msg =  token;
                    Log.d(TAG, msg);
                    ShareProfileData.getInstance(SplachActivity.this).saveDeviceToken(token);
                });

        final Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000);
                    if (ShareProfileData.getInstance(getApplicationContext()).IsLogInCaptain()) {
                        SendTokenCaptain();
                        startActivity(new Intent(SplachActivity.this, MainActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(SplachActivity.this, LoginActivity.class));
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

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
                    startActivity(new Intent(SplachActivity.this, MainActivity.class));
                    finish();
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params=new HashMap<>();
                params.put("cap_id", ShareProfileData.getInstance(SplachActivity.this).getKEYID());
                params.put("token",ShareProfileData.getInstance(SplachActivity.this).getDeviceToken());
                return params;
            }
        };
        RequestHand.getInstance(SplachActivity.this).addToRequestQueue(stringRequest);
    }

}
