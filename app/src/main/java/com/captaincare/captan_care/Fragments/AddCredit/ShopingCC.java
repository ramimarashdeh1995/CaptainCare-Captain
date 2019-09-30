package com.captaincare.captan_care.Fragments.AddCredit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.RequestHand;
import com.captaincare.captan_care.Manegers.ShareProfileData;

import java.util.HashMap;
import java.util.Map;

public class ShopingCC extends AppCompatActivity {

    private final String API_GET_TOKEN="http://captain-care.org/payment/main.php";
    private final String API_CHECK_OUT="http://captain-care.org/payment/checkout.php";

    private static final int REQUEST_CODE=1234;

    private String token,amount;

    private HashMap<String,String> parmsHash;

    private LinearLayout pyment,wite;

    private TextView txtCc,txtPrice;
    private Button btnShop;
    private ProgressBar process;

    private String cc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoping_cc);

        String P_ID=getIntent().getStringExtra("p_id");
        String P_CC=getIntent().getStringExtra("p_cc");
        String P_PRICE=getIntent().getStringExtra("p_price");

        cc=P_CC;

        pyment=findViewById(R.id.payment1);
        wite=findViewById(R.id.waite1);

        process=findViewById(R.id.progress);

        txtCc=findViewById(R.id.txtCostCCShoping);
        txtPrice=findViewById(R.id.txtFreeCCShoping);
        btnShop=findViewById(R.id.btnShop);

        txtCc.setText(P_CC+" CC");
        txtPrice.setText(P_PRICE);

        process.setVisibility(View.VISIBLE);

        new  getToken().execute();

        btnShop.setOnClickListener(v->{
            submiutpayment();
        });

    }

    private void submiutpayment() {
        DropInRequest dropInRequest=new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(this),REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_CODE){
            if (resultCode==RESULT_OK){
                DropInResult result=data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce paymentMethodNonce=result.getPaymentMethodNonce();
                String strnoce=paymentMethodNonce.getNonce();


                if (! txtPrice.getText().toString().isEmpty()){
                    amount=txtPrice.getText().toString();
                    parmsHash=new HashMap<>();
                    parmsHash.put("amount",amount);
                    parmsHash.put("nonce",strnoce);

                    sendPayment(amount,strnoce);
                }
                else {
                    Toast.makeText(this,"error1",Toast.LENGTH_LONG).show();
                }
            }else if (resultCode==RESULT_CANCELED){
                Toast.makeText(this,"user cancel",Toast.LENGTH_LONG).show();
            }else {
                Exception error=(Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("EMDT_ERROR",error.toString());
            }
        }
    }

    private void sendPayment(String amount,String stnouce) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                API_CHECK_OUT,
                response -> {
                    if (response.contains("Successful")){
                        Toast.makeText(getApplicationContext(),
                                "Successful",
                                Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),
                                response,
                                Toast.LENGTH_LONG).show();
                    }
                    Log.d("EMDT_LOG",response);
                }, error -> Log.d("EMDT_ERROR",error.toString())){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parms = new HashMap<>();
                parms.put("amount", amount);
                parms.put("nonce", stnouce);
                parms.put("cc", cc);


                parms.put("type", "cap");
                parms.put("id", ShareProfileData.getInstance(ShopingCC.this).getKEYID());


                return parms;
              /*  if (parmsHash==null)
                    return null;
                Map<String,String>parms=new HashMap<>();
                for (String key : parms.keySet()){
                    parms.put(key,parmsHash.get(key));
                }
                return parms;*/
            }

           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("Content-type","application/x-www-form-urlencoded");
                return params;
            }*/

        };


        RequestHand.getInstance(this).addToRequestQueue(stringRequest);
    }

    private class getToken extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient client =new HttpClient();
            client.get(API_GET_TOKEN, new HttpResponseCallback() {
                @Override
                public void success(final String responseBody) {
                    runOnUiThread(() -> {
                        wite.setVisibility(View.GONE);
                        pyment.setVisibility(View.VISIBLE);
                        token=responseBody;
                    });
                }

                @Override
                public void failure(Exception exception) {
                    Log.d("EDMT_ERROR",exception.toString());
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            process.setVisibility(View.GONE);
        }
    }
}
