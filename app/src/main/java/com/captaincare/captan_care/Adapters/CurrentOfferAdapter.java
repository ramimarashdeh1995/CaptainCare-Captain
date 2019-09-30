package com.captaincare.captan_care.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.captaincare.captan_care.Models.CurrentOfferClass;
import com.captaincare.captan_care.Fragments.Profile.ProfileFragment2;
import com.captaincare.captan_care.Activities.OfferDetailsActivity;
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


public class CurrentOfferAdapter extends RecyclerView.Adapter<CurrentOfferAdapter.MyHolder> {

    private Context context;
    private ArrayList<CurrentOfferClass> currentOfferClassArrayList;
    private ArrayList<CurrentOfferClass>SearhArrayList;

    private AlertDialog.Builder  dialogShow;
    private SharedPreferences sharedPreferences;


    private Dialog dialogrank;
    private RatingBar ratingBar;
    private Button btnRate;
    private CircleImageView imgPersonalreat;
    private TextView namedialograte;
    private ProgressBar progressrate;
    private SharedPreferences offerPreferences;

    public CurrentOfferAdapter(Context context, ArrayList<CurrentOfferClass> ClassCurrentArrayList) {
        this.context = context;
        this.currentOfferClassArrayList = ClassCurrentArrayList;
        SearhArrayList =new ArrayList<>(ClassCurrentArrayList);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_vendor_current_offer, null);
        return new CurrentOfferAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {


        CurrentOfferClass currentOffer = currentOfferClassArrayList.get(i);


        if (currentOffer.getIsEnd().equals("0")) {
            myHolder.logincaptain.setVisibility(View.VISIBLE);
            myHolder.logincaptainrank.setVisibility(View.GONE);
        } else if (currentOffer.getIsEnd().equals("1")) {
            myHolder.logincaptain.setVisibility(View.GONE);
            myHolder.logincaptainrank.setVisibility(View.VISIBLE);
        }


        Glide.with(context).load(currentOffer.getUserImageProfile()).skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.ic_profile_default).into(myHolder.circleImageView);
        myHolder.txtCaptainName.setText(currentOffer.getUserName());
        if (currentOffer.getRank().equals("null")){
            myHolder.txtCaptainRate.setText(context.getString(R.string.rate)+" : 5");
        }else {
            myHolder.txtCaptainRate.setText(context.getString(R.string.rate)+" : "+currentOffer.getRank());
        }

        myHolder.btnendservicecaptain.setOnClickListener(v -> {
                dialogShow = new AlertDialog.Builder(context);
                dialogShow.setTitle("Are you sure to End the offer ? " +
                        "30% of the value will be deducted");
                dialogShow.setIcon(R.drawable.ic_successfull);
                dialogShow.setPositiveButton(R.string.yes, (dialog, which) -> {
                    dialog.dismiss();
                    EndSaveFromCaptain(currentOffer.getOfferIDCaptain(), currentOffer.getSaveID(), i);

                });
                dialogShow.setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                });
                dialogShow.show();
        });

        myHolder.btnviewoffer.setOnClickListener(v -> {
            if (currentOffer.getOfferIDCaptain().equals("null")) {
                offerPreferences = context.getSharedPreferences("OfferData", Context.MODE_PRIVATE);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = offerPreferences.edit();
                editor.putString("key_pic1", currentOffer.getPic1());
                editor.putString("key_pic2", currentOffer.getPic2());
                editor.putString("key_pic3", currentOffer.getPic3());
                editor.putString("key_pic4", currentOffer.getPic4());
                editor.putString("key_title", currentOffer.getTitle());
                editor.putString("key_des", currentOffer.getDisc());
                editor.putString("key_city", currentOffer.getCity_ven());
                editor.putString("key_servicetype", null);
                editor.putString("key_offerend", currentOffer.getEnddate());
                editor.putString("key_price", currentOffer.getCostven());
                editor.apply();
                context.startActivity(new Intent(context, OfferDetailsActivity.class));
            } else {
                offerPreferences = context.getSharedPreferences("OfferData", Context.MODE_PRIVATE);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = offerPreferences.edit();
                editor.putString("key_pic1", currentOffer.getCappic1());
                editor.putString("key_pic2", currentOffer.getCappic2());
                editor.putString("key_pic3", currentOffer.getCappic3());
                editor.putString("key_pic4", currentOffer.getCappic4());
                editor.putString("key_title", currentOffer.getCaptitle());
                editor.putString("key_des", currentOffer.getCapdisc());
                editor.putString("key_city", currentOffer.getCity_cap());
                editor.putString("key_servicetype", null);
                editor.putString("key_offerend", currentOffer.getCapenddate());
                editor.putString("key_price", currentOffer.getCostcap());
                editor.apply();
                context.startActivity(new Intent(context, OfferDetailsActivity.class));
            }
        });

        myHolder.btngotomap.setOnClickListener(v -> {
            Uri navigationIntentUri = Uri.parse("google.navigation:q=" + currentOffer.getLat() +
                    "," + currentOffer.getLon());//creating intent with latlng
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        });

        myHolder.btnrate.setOnClickListener(v -> {
            ShowDialogRankAndFinishProcess(currentOffer.getUserImageProfile(),
                    currentOffer.getUserName(),
                    currentOffer.getSaveID(),
                    currentOffer.getUserID(),
                    i);
        });

        myHolder.circleImageView.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            sharedPreferences = context.getSharedPreferences("shareCaptainId", Context.MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("key1", currentOffer.getUserID());
            editor.putString("key_rate",currentOffer.getRank());
            editor.apply();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment2()).addToBackStack(null).commit();

        });
    }

    private void ShowDialogRankAndFinishProcess(String img,String name,String SaveID,String UserID,int i){
        dialogrank=new Dialog(context);
        dialogrank.setContentView(R.layout.dialog_rate);
        dialogrank.setTitle("This is Rate Box");
        dialogrank.setCancelable(true);
        namedialograte=dialogrank.findViewById(R.id.namedialograte);
        imgPersonalreat=dialogrank.findViewById(R.id.imgPersonalreat);
        ratingBar=dialogrank.findViewById(R.id.ratingBar);
        progressrate=dialogrank.findViewById(R.id.progressrate);

        Glide.with(context).load(img).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.ic_profile_default).into(imgPersonalreat);
        namedialograte.setText(name);


        btnRate=dialogrank.findViewById(R.id.btnRate);

        btnRate.setOnClickListener(v->{
            progressrate.setVisibility(View.VISIBLE);
            btnRate.setEnabled(false);

            SetRateVendorFromCaptain(SaveID,ratingBar.getRating(),i);

        });
        dialogrank.show();
    }

    private void EndSaveFromCaptain( String offer_id,String save_id,int i) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.EndSaveProcessFromCaptain,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            Toast.makeText(context,"end save ",Toast.LENGTH_LONG).show();
                            currentOfferClassArrayList.remove(i);
                            notifyItemRemoved(i);
                        } else if (jsonObject.getString("error").equals("true")) {

                        }
                        Toast.makeText(context,jsonObject.getInt("message"),Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                , error -> {
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("cap_id", ShareProfileData.getInstance(context).getKEYID());
                parms.put("offer_id", offer_id);
                parms.put("save_id",save_id);
                return parms;
            }
        };
        RequestHand.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void SetRateVendorFromCaptain(String SaveID,float Rank,int i){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.EndSaveProcessOffer,
                response -> {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")){
                            dialogrank.dismiss();
                            JSONObject object=jsonObject.getJSONObject("message");
                            ShareProfileData.getInstance(context).SetReatAndPoint(
                                    object.getString("Rank"),object.getString("point")
                            );
                            currentOfferClassArrayList.remove(i);
                            notifyItemRemoved(i);
                        }else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("save_id",SaveID);
                params.put("rank",String.valueOf(Rank));
                params.put("cap_id",ShareProfileData.getInstance(context).getKEYID());
                return params;
            }
        };
        RequestHand.getInstance(context).addToRequestQueue(stringRequest);
    }

    @Override
    public int getItemCount() {
        return currentOfferClassArrayList.size();
    }

  /*  @Override
    public Filter getFilter() {
        return Searsh;
    }
    private Filter Searsh=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CurrentOfferClass> filterList=new ArrayList<>();
            if (constraint==null || constraint.length()==0){
                filterList.addAll(SearhArrayList);
            }else {
                String search=constraint.toString().toLowerCase().trim();
                for (CurrentOfferClass i: SearhArrayList){
                    if (i.getUserName().toLowerCase().contains(search)){
                        filterList.add(i);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            currentOfferClassArrayList.clear();
            currentOfferClassArrayList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };*/


    public class MyHolder extends RecyclerView.ViewHolder{

        CardView finishcardview;
        CircleImageView circleImageView;
        TextView txtCaptainName,txtCaptainRate;
        Button btnendservicecaptain,btnviewoffer,btngotomap,btnrate;
        LinearLayout logincaptain,logincaptainrank;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            finishcardview=itemView.findViewById(R.id.finishcardview);
            circleImageView=itemView.findViewById(R.id.imgPersonal);
            txtCaptainName=itemView.findViewById(R.id.txtCaptainName);
            txtCaptainRate=itemView.findViewById(R.id.txtCaptainRate);
            btnendservicecaptain=itemView.findViewById(R.id.btnEndServicecaptain);
            btnviewoffer=itemView.findViewById(R.id.btnviewoffer);
            btngotomap=itemView.findViewById(R.id.btngotomap);
            btnrate=itemView.findViewById(R.id.btnrate);

            logincaptain=itemView.findViewById(R.id.logincaptain);

            logincaptainrank=itemView.findViewById(R.id.logincaptainrank);
        }
    }
}
