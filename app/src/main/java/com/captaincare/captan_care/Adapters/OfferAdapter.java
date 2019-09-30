package com.captaincare.captan_care.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.captaincare.captan_care.Models.OfferClass;
import com.captaincare.captan_care.Dialog.ImageDialog;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;
import com.captaincare.captan_care.Manegers.ShareProfileData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyHolder> {

    private Context context;
    private ArrayList<OfferClass> offerArrayList;
    private AlertDialog.Builder  dialogDelete,sureDialog;

    public OfferAdapter(Context context, ArrayList<OfferClass> offerArrayList) {
        this.context = context;
        this.offerArrayList = offerArrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_offer_layout, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        OfferClass offerClass = offerArrayList.get(position);


       if (offerClass.getIspaid().equals("0")){
           holder.imgVip.setVisibility(View.GONE);
       }

        if (offerClass.getIs_Favorite().equals("null")){
            holder.btnFavorite.setText(context.getString(R.string.add_to_favorite));
            holder.btnFavorite.setSelected(false);
        }else {
            holder.btnFavorite.setText(context.getString(R.string.remove));
            holder.btnFavorite.setSelected(true);
        }

        holder.txtCaptainName.setText(offerClass.getUser_name());
        holder.txtOfferTitle.setText(offerClass.getOffer_title());
        holder.txtOfferDes.setText(offerClass.getOffer_disc());
        holder.txtOfferCity.setText(offerClass.getCity());
        holder.txtCaptainRate.setText(offerClass.getEval());
        holder.txtOfferEndTime.setText(offerClass.getOffer_end());
        holder.txtOfferAddress.setText(offerClass.getVendor_address());
        holder.txtOfferPrice.setText(offerClass.getCost());

        Glide.with(context).load(offerClass.getUser_photo_url()).placeholder(R.drawable.ic_profile_default).into(holder.imgPersonal);
        if (!offerClass.getOffer_pic1().isEmpty()) {
            holder.imgOffer.setVisibility(View.VISIBLE);
            Glide.with(context).load(offerClass.getOffer_pic1()).into(holder.imgOffer);
        } else {
            holder.imgOffer.setVisibility(View.GONE);
        }

        holder.imgOffer.setOnClickListener(v -> {
            ImageDialog imageDialog = new ImageDialog(context,offerClass.getOffer_pic1(),offerClass.getOffer_pic2(),
                    offerClass.getOffer_pic3(),offerClass.getOffer_pic4());
            imageDialog.show();

        });

        holder.btnFavorite.setOnClickListener(v -> {
            if (holder.btnFavorite.getText().toString().equals(context.getString(R.string.add_to_favorite))) {
                holder.btnFavorite.setText(context.getString(R.string.remove));
                holder.btnFavorite.setSelected(true);

                addOfferFavoriteCaptain(ShareProfileData.getInstance(context).getKEYID(),
                        offerClass.getOffer_id(), offerClass.getUser_id());
            } else {
                holder.btnFavorite.setText(context.getString(R.string.add_to_favorite));
                holder.btnFavorite.setSelected(false);

                removeOfferFavoriteCaptain(ShareProfileData.getInstance(context).getKEYID(), offerClass.getOffer_id());
            }
        });

        holder.btnAccept.setOnClickListener(v -> {
            holder.btnAccept.setEnabled(false);
            dialogAcceptOfferVendorFromCaptain(offerClass.getUser_id(), offerClass.getOffer_id(),
                    offerClass.getLon(), offerClass.getLat(), offerClass.getVendor_address(), position);
            holder.btnAccept.setEnabled(true);
        });
        holder.cardView.setOnClickListener(v -> {

        });
    }

    private void dialogAcceptOfferVendorFromCaptain(String VendorID,String OfferID,String lon,String lat,String venAddress,int i){

        sureDialog=new AlertDialog.Builder(context);
        sureDialog.setTitle("Are you sure to accept the offer and go to : ");
        sureDialog.setMessage(venAddress);
        sureDialog.setPositiveButton("Go Now", (dialog, which) -> {
            dialog.dismiss();
            acceptOfferVendorFromCaptainNow(VendorID,OfferID,lon,lat,i);
        });
        sureDialog.setNegativeButton("Go Last", (dialog, which) -> {
            dialog.dismiss();
            acceptOfferVendorFromCaptainLast(VendorID,OfferID,i);
        });
        sureDialog.setNeutralButton("Back", (dialog, which) -> dialog.dismiss());
        sureDialog.create().show();


      /*  dialogDelete = new AlertDialog.Builder(context);
        dialogDelete.setTitle("Are you sure to accept the offer and go to "+venAddress);
        dialogDelete.setIcon(R.drawable.ic_successfull);
        dialogDelete.setPositiveButton("Go Now",(dialog,which)->{
            dialog.dismiss();
            acceptOfferVendorFromCaptainNow(VendorID,OfferID,lon,lat,i);

        });
        dialogDelete.setPositiveButton("Go Later",(dialog,which)->{
            dialog.dismiss();
            acceptOfferVendorFromCaptainLast(VendorID,OfferID,i);

        });
        dialogDelete.setPositiveButton("Back",(dialog,which)->{
            dialog.dismiss();
        });
        dialogDelete.show();*/
    }

    private void acceptOfferVendorFromCaptainLast(String VendorID,String OfferID,int i){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.SaveOfferLastGo,
                response -> {
                    try {
                        ReadJsonObject(response,"null","null",i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("cap_id", ShareProfileData.getInstance(context).getKEYID());
                parms.put("ven_id",VendorID);
                parms.put("offer_id", OfferID);
                return parms;
            }
        };
        RequestHand.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void acceptOfferVendorFromCaptainNow(String VendorID,String OfferID,String lon,String lat,int i){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.SaveOfferMarket,
                response -> {
                    try {
                        ReadJsonObject(response,lon,lat,i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("cap_id", ShareProfileData.getInstance(context).getKEYID());
                parms.put("ven_id",VendorID);
                parms.put("offer_id_market", OfferID);
                return parms;
            }
        };
        RequestHand.getInstance(context).addToRequestQueue(stringRequest);
    }


    private void ReadJsonObject(String response,String lon,String lat,int i) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);
        if (jsonObject.getString("error").equals("false")){
            dialogDelete = new AlertDialog.Builder(context);
            dialogDelete.setTitle("Successful Deleting");
            dialogDelete.setIcon(R.drawable.ic_successfull);
            dialogDelete.setPositiveButton(context.getString(R.string.yes), (dialog, which) -> {
                dialog.dismiss();
                offerArrayList.remove(i);
                notifyItemRemoved(i);
                if (lon.equals("null")&&lat.equals("null")){
                        Toast.makeText(context,"Current Offer",Toast.LENGTH_LONG).show();

                }else {
                    Uri navigationIntentUri = Uri.parse("google.navigation:q=" + lat +
                            "," + lon);//creating intent with latlng
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
                }

            });
            dialogDelete.show();
        }else if (jsonObject.getString("error").equals("true")){
            if (jsonObject.getString("message").equals("your not have CC")){
                dialogDelete = new AlertDialog.Builder(context);
                dialogDelete.setTitle("your not have CC");
                dialogDelete.setIcon(R.drawable.ic_error2);
                dialogDelete.setPositiveButton(context.getString(R.string.yes), (dialog, which) -> {
                    dialog.dismiss();
                });
                dialogDelete.show();
            }else{
                Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                dialogDelete = new AlertDialog.Builder(context);
                dialogDelete.setTitle("Server Error");
                dialogDelete.setIcon(R.drawable.ic_error2);
                dialogDelete.setPositiveButton(context.getString(R.string.yes), (dialog, which) -> {
                    dialog.dismiss();
                });
                dialogDelete.show();
            }

        }
    }


    private void removeOfferFavoriteCaptain(String captainId, String offer_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.UnFavoriteOffer,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {

                        } else if (jsonObject.getString("error").equals("true")) {

                        }
                        Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                , error -> {
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("cap_id", captainId);
                parms.put("offer_id", offer_id);
                return parms;
            }
        };
        RequestHand.getInstance(context).addToRequestQueue(stringRequest);
    }

    // كابتن بدي اعمل مفضلة لصاحب محل
    private void addOfferFavoriteCaptain(String captainId, String offer_id,String venID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.AddFavoriteOffer,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {

                        } else if (jsonObject.getString("error").equals("true")) {

                        }
                        Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                , error -> {
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("cap_id", captainId);
                parms.put("offer_id", offer_id);
                parms.put("ven_id",venID);
                return parms;
            }
        };
        RequestHand.getInstance(context).addToRequestQueue(stringRequest);
    }

    @Override
    public int getItemCount() {
        return offerArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtOfferTitle, txtOfferDes, txtOfferCity, txtOfferAddress, txtCaptainName,
                txtCaptainRate, txtOfferEndTime,txtAddress,txtOfferPrice;
        ImageView imgOffer,imgVip;
        CircleImageView imgPersonal;
        CardView cardView;
        Button btnAccept , btnFavorite;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardMyAds);

            txtOfferTitle = itemView.findViewById(R.id.txtOfferTitle);
            txtOfferDes = itemView.findViewById(R.id.txtOfferDes);
            txtOfferCity = itemView.findViewById(R.id.txtOfferCity);
            txtOfferAddress = itemView.findViewById(R.id.txtOfferAddress);
            txtOfferEndTime = itemView.findViewById(R.id.txtOfferEndTime);
            txtAddress=itemView.findViewById(R.id.txtAddress);

            imgVip=itemView.findViewById(R.id.imgVip);

            txtCaptainName = itemView.findViewById(R.id.txtCaptainName);
            txtOfferPrice=itemView.findViewById(R.id.txtOfferPrice);
            txtCaptainRate = itemView.findViewById(R.id.txtCaptainRate);


            imgPersonal = itemView.findViewById(R.id.imgPersonal);
            imgOffer = itemView.findViewById(R.id.imgOffer);

            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);


        }
    }
}
   /* if (offerClass.getIs_Favorite().equals("null")) {
            holder.imgFavorite.setImageResource(R.drawable.ic_favorite2);
            holder.txtFavorite.setText(context.getString(R.string.add_to_favorite));
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_favorite_red);
            holder.txtFavorite.setText(context.getString(R.string.remove));
        }

        //Offer Details
        holder.details.setOnClickListener(v -> {
            offerPreferences = context.getSharedPreferences("OfferData", Context.MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = offerPreferences.edit();
            editor.putString("key_pic1", offerClass.getOffer_pic1());
            editor.putString("key_pic2", offerClass.getOffer_pic2());
            editor.putString("key_pic3", offerClass.getOffer_pic3());
            editor.putString("key_pic4", offerClass.getOffer_pic4());
            editor.putString("key_title", offerClass.getOffer_title());
            editor.putString("key_des", offerClass.getOffer_disc());
            editor.putString("key_city", offerClass.getCity());
            editor.putString("key_servicetype", null);
            editor.putString("key_offerend", offerClass.getOffer_end());
            editor.apply();
            context.startActivity(new Intent(context, OfferDetailsActivity.class));
        });*/
     /*  holder.linearFavorite.setOnClickListener(v -> {
            if (holder.txtFavorite.getText().toString().equals(context.getString(R.string.add_to_favorite))) {
                holder.imgFavorite.setImageResource(R.drawable.ic_favorite_red);
                holder.txtFavorite.setText(context.getString(R.string.remove));
                if (ShareProfileData.getInstance(context).IsLogInCaptain()) {
                    addOfferFavoriteCaptain(ShareProfileData.getInstance(context).getKEYID(),offerClass.getOffer_id());
                } else if (ShareProfileData.getInstance(context).IsLogInVendor()) {
                    addOfferFavoriteVendor(ShareProfileData.getInstance(context).getKEYID(),offerClass.getOffer_id());
                }
            } else {
                holder.imgFavorite.setImageResource(R.drawable.ic_favorite2);
                holder.txtFavorite.setText(context.getString(R.string.add_to_favorite));
                if (ShareProfileData.getInstance(context).IsLogInCaptain()) {
                    removeOfferFavoriteCaptain(ShareProfileData.getInstance(context).getKEYID(),offerClass.getOffer_id());
                } else if (ShareProfileData.getInstance(context).IsLogInVendor()) {
                    removeOfferFavoriteVendor(ShareProfileData.getInstance(context).getKEYID(),offerClass.getOffer_id());
                }

            }

        });
        holder.call.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel :"+"+962444"));
            context.startActivity(intent);
        });*/

     /* //Show Profile
        holder.imgPersonal.setOnClickListener(v -> {
          *//*  AppCompatActivity activity = (AppCompatActivity) v.getContext();
            sharedPreferences = context.getSharedPreferences("shareCaptainId", Context.MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("key1", offerClass.getUser_id());
            editor.apply();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment2()).addToBackStack(null).commit();*//*

        });*/