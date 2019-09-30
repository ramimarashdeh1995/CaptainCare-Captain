package com.captaincare.captan_care.Notification;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;
import com.captaincare.captan_care.Manegers.ShareProfileData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReciveNotification extends AppCompatActivity {

    private Button finishdialog,btnFavorite,btnAccept,btnRate,btnAccept1;
    private AlertDialog.Builder  dialogDelete;
    private AlertDialog.Builder  dialogShow;
    private LinearLayout logincaptain,loginvendor,
            typeOffer,typeRank;
    private RatingBar ratingBar;
    private CircleImageView circleImageView;
    private TextView namedialograte,pirce,addressdialog,timeEnd;
    private ProgressBar progressrate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recive_notification);

        logincaptain=findViewById(R.id.logincaptain1);
        finishdialog=findViewById(R.id.finishdialog);
        btnFavorite=findViewById(R.id.btnFavorite);
        btnAccept=findViewById(R.id.btnAccept);
        loginvendor=findViewById(R.id.loginvindor1);
        pirce=findViewById(R.id.pirce);
        addressdialog=findViewById(R.id.addressdialog);
        timeEnd=findViewById(R.id.timeEnd);
        finishdialog=findViewById(R.id.finishdialogcaptain);
        btnAccept1=findViewById(R.id.btnAcceptcaptain);

        typeRank=findViewById(R.id.typeRank);
        typeOffer=findViewById(R.id.typeOffer);


        circleImageView=findViewById(R.id.imgPersonalreat);
        ratingBar=findViewById(R.id.ratingBar);
        namedialograte=findViewById(R.id.namedialograte);
        btnRate=findViewById(R.id.btnRate);
        progressrate=findViewById(R.id.progressrate);



        String Type=getIntent().getStringExtra("type");
        int ID_Notification=getIntent().getIntExtra("id_notification",0);

        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(this);
        notificationCompat.cancel(ID_Notification++);

        if (Type.equals("offer")){
            String OfferID=getIntent().getStringExtra("OfferID");
            String UserID=getIntent().getStringExtra("userID");
            String Address=getIntent().getStringExtra("Address");
            String lon=getIntent().getStringExtra("lon");
            String lat=getIntent().getStringExtra("lat");

            String pirce=getIntent().getStringExtra("cost");
           String endTimehours=getIntent().getStringExtra("hours");
          /*  String endTimeminute=getIntent().getStringExtra("minute");
            String TimeEnd="";

            if (endTimehours.equals("null")){
                TimeEnd=endTimeminute+ " M";
            }else {
                TimeEnd=endTimehours+":"+endTimeminute+" H";
            }
*/
            typeOffer.setVisibility(View.VISIBLE);
            TypeOffer(OfferID,UserID,Address,lon,lat,endTimehours,pirce);


        } else if(Type.equals("acceptoffercaptain")) {
            String OfferID=getIntent().getStringExtra("OfferID");
            String UserID=getIntent().getStringExtra("userID");
            String Address=getIntent().getStringExtra("Address");
            String lon=getIntent().getStringExtra("lon");
            String lat=getIntent().getStringExtra("lat");

            String pirce=getIntent().getStringExtra("cost");
            String endTimehours=getIntent().getStringExtra("hours");
          /*  String endTimeminute=getIntent().getStringExtra("minute");
            String TimeEnd="";

            if (endTimehours.equals("null")){
                TimeEnd=endTimeminute+ " M";
            }else {
                TimeEnd=endTimehours+":"+endTimeminute+" H";
            }*/
            typeOffer.setVisibility(View.VISIBLE);
            loginvendor.setVisibility(View.GONE);
            TypeAcceptOfferCaptain(OfferID,UserID,Address,lon,lat,endTimehours,pirce);
        } else if (Type.equals("rank")){

            String img=getIntent().getStringExtra("imag");
            String Name=getIntent().getStringExtra("name");
            String SaveID=getIntent().getStringExtra("SaveID");

            typeRank.setVisibility(View.VISIBLE);
            TypeRank(img,Name,SaveID);
        }
    }

    /*
    *
    * This Function To Type == Offer
    *
    */

    private void TypeOffer(String offerID, String userID, String address, String lon, String lat, String TimeEnd, String pirce1) {
        pirce.setText(pirce1+" "+getString(R.string.jd));
        addressdialog.setText(address);
        timeEnd.setText(TimeEnd);
        logincaptain.setVisibility(View.GONE);
        finishdialog.setOnClickListener(v -> {
            System.exit(0);
        });
        btnFavorite.setOnClickListener(v -> {
            AddToFavotie(offerID);
        });
        btnAccept.setOnClickListener(v->{
            AcceptOffer(userID,offerID,address,lon,lat);
        });
    }

    private void AcceptOffer(String userID, String offerID, String address, String lon, String lat) {
        dialogDelete = new AlertDialog.Builder(this);
        dialogDelete.setTitle(getString(R.string.accept_offer_dialog)+" "+address);
        dialogDelete.setIcon(R.drawable.ic_successfull);
        dialogDelete.setPositiveButton(getString(R.string.go_now),(dialog,which)->{
            dialog.dismiss();
            acceptOfferVendorFromCaptainNow(userID,offerID,lon,lat);

        });
        dialogDelete.setNegativeButton(getString(R.string.go_latar),(dialog,which)->{
            dialog.dismiss();
            acceptOfferVendorFromCaptainLast(userID,offerID);

        });
        dialogDelete.setNeutralButton(getString(R.string.backe),(dialog,which)->{
            dialog.dismiss();
        });
        dialogDelete.show();
    }

    private void acceptOfferVendorFromCaptainLast(String userID, String offerID) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.SaveOfferLastGo,
                response -> {
                    try {
                        ReadJsonObject(response,"null","null");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("cap_id", ShareProfileData.getInstance(getApplicationContext()).getKEYID());
                parms.put("ven_id",userID);
                parms.put("offer_id", offerID);
                return parms;
            }
        };
        RequestHand.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void acceptOfferVendorFromCaptainNow(String userID, String offerID, String lon, String lat) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.SaveOfferMarket,
                response -> {
                    try {
                        ReadJsonObject(response,lon,lat);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("cap_id", ShareProfileData.getInstance(getApplicationContext()).getKEYID());
                parms.put("ven_id",userID);
                parms.put("offer_id_market", offerID);
                return parms;
            }
        };
        RequestHand.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void AddToFavotie(String offerID) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.AddFavoriteOffer,
                response ->{
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")){
                            System.exit(0);
                        }else if (jsonObject.getString("error").equals("true")){
                            System.exit(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ,error -> {
            ReadServerError(error);
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>parms =new HashMap<>();
                parms.put("cap_id", ShareProfileData.getInstance(getApplicationContext()).getKEYID());
                parms.put("offer_id",offerID);
                parms.put("ven_id","null");
                return parms;
            }
        };
        RequestHand.getInstance(this).addToRequestQueue(stringRequest);
    }



    /*
     *
     * This Function To Type == acceptoffercaptain
     *
     */
    private void TypeAcceptOfferCaptain(String OfferID,String UserID,
                                        String Address,String lon,String lat,String TimeEnd,String pirce1){


        pirce.setText(pirce1+" "+getString(R.string.jd));
        addressdialog.setText(Address);
        timeEnd.setText(TimeEnd);

        finishdialog.setOnClickListener(v -> {
            System.exit(0);
        });
        btnAccept1.setOnClickListener(v->{
            dialogSaveOfferCaptain(UserID,OfferID,Address,lon,lat);
        });

    }
    private void dialogSaveOfferCaptain(String VendorID,String OfferID,String venAddress,String lon,String lat){
        dialogDelete = new AlertDialog.Builder(this);
        dialogDelete.setTitle(getString(R.string.accept_offer_dialog)+" "+venAddress);
        dialogDelete.setIcon(R.drawable.ic_successfull);
        dialogDelete.setPositiveButton(getString(R.string.go_now),(dialog,which)->{
            dialog.dismiss();
            SaveOfferCaptain(VendorID,OfferID,lon,lat);

        });
        dialogDelete.setNeutralButton(getString(R.string.backe),(dialog,which)->{
            dialog.dismiss();

        });
        dialogDelete.show();
    }
    private void SaveOfferCaptain(String UserID,String OfferID,String lon,String lat){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.SaveOfferCaptain,
                response -> {
                    try {
                        ReadJsonObject(response,lon,lat);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    ReadServerError(error);
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("ven_id",UserID);
                params.put("cap_id",ShareProfileData.getInstance(getApplicationContext()).getKEYID());
                params.put("offer_id_market",OfferID);
                return params;
            }
        };
        RequestHand.getInstance(this).addToRequestQueue(stringRequest);
    }





    /*
     *
     * This Function To Type == rank
     *
     */
    private void TypeRank(String img,String Name,String SaveID){

        Glide.with(this).load(img).placeholder(R.drawable.ic_profile_default).into(circleImageView);
        namedialograte.setText(Name);


        btnRate.setOnClickListener(v -> {
            progressrate.setVisibility(View.VISIBLE);
            SetRateVendorFromCaptain(SaveID,ratingBar.getRating());
        });
    }

    private void SetRateVendorFromCaptain(String SaveID,float Rank){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.EndSaveProcessOffer,
                response -> {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if (jsonObject.getString("error").equals("false"))
                        {
                            JSONObject object=jsonObject.getJSONObject("message");
                            ShareProfileData.getInstance(ReciveNotification.this).SetReatAndPoint(
                                    object.getString("Rank"),object.getString("point")
                            );
                        }
                        System.exit(0);
                        this.finish();
                        //  ReadJsonObject(response,"null","null");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    ReadServerError(error);
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("save_id",SaveID);
                params.put("rank", String.valueOf(Rank));
                params.put("cap_id",ShareProfileData.getInstance(getApplicationContext()).getKEYID());
                return params;
            }
        };
        RequestHand.getInstance(this).addToRequestQueue(stringRequest);
    }







    private void ReadJsonObject(String response, String lon, String lat) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);
        if (jsonObject.getString("error").equals("false")){
            dialogDelete = new AlertDialog.Builder(this);
            dialogDelete.setTitle("Successful Deleting");
            dialogDelete.setIcon(R.drawable.ic_successfull);
            dialogDelete.setPositiveButton(this.getString(R.string.yes), (dialog, which) -> {
                dialog.dismiss();
                if (lon.equals("null")&&lat.equals("null")){
                    if (ShareProfileData.getInstance(this).IsLogInCaptain()){
                        Toast.makeText(this,"Current Offer",Toast.LENGTH_LONG).show();
                        System.exit(0);
                    }
                }else {
                    Uri navigationIntentUri = Uri.parse("google.navigation:q=" + lat +
                            "," + lon);//creating intent with latlng
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                    System.exit(0);
                }
            });
            dialogDelete.show();
        }else if (jsonObject.getString("error").equals("true")){
            if (jsonObject.getString("message").equals("your not have CC")){
                dialogDelete = new AlertDialog.Builder(this);
                dialogDelete.setTitle("your not have CC");
                dialogDelete.setIcon(R.drawable.ic_error2);
                dialogDelete.setPositiveButton(this.getString(R.string.yes), (dialog, which) -> {
                    dialog.dismiss();
                    this.finish();
                });
                dialogDelete.show();
            }else if (jsonObject.getString("message").equals("your accept offer")){
                dialogDelete = new AlertDialog.Builder(this);
                dialogDelete.setTitle("your Accept the offer");
                dialogDelete.setIcon(R.drawable.ic_error2);
                dialogDelete.setPositiveButton(this.getString(R.string.yes), (dialog, which) -> {
                    dialog.dismiss();
                    this.finish();
                });
                dialogDelete.show();
            }
            dialogDelete = new AlertDialog.Builder(this);
            dialogDelete.setTitle("Server Error");
            dialogDelete.setIcon(R.drawable.ic_error2);
            dialogDelete.setPositiveButton(this.getString(R.string.yes), (dialog, which) -> {
                dialog.dismiss();
                this.finish();
            });
            dialogDelete.show();
        }
    }

    private void ReadServerError(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            //This indicates that the reuest has either time out or there is no connection

        } else if (error instanceof AuthFailureError) {
            //Error indicating that there was an Authentication Failure while performing the request

        } else if (error instanceof ServerError) {
            //Indicates that the server responded with a error response

        } else if (error instanceof NetworkError) {
            //Indicates that there was network error while performing the request

        } else if (error instanceof ParseError) {
            // Indicates that the server response could not be parsed

        }
    }

}
