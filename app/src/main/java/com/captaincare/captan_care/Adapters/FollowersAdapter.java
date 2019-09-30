package com.captaincare.captan_care.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.captaincare.captan_care.Models.FollowAndBlockClass;
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

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.myHolder> {

    private Context context;
    private ArrayList<FollowAndBlockClass> followersArrayList;
    private SharedPreferences sharedPreferences;

    public FollowersAdapter(FragmentActivity activity, ArrayList<FollowAndBlockClass> followersArrayList) {
        this.context=activity;
        this.followersArrayList =followersArrayList;
    }


    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_followers_layout,null);

        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        holder.txtName.setText(followersArrayList.get(position).getUserName());

        if (followersArrayList.get(position).getIsFollow().equals("null")){
            holder.imgFollow.setVisibility(View.VISIBLE);
            holder.imgUnFollow.setVisibility(View.GONE);
        }else {
            holder.imgFollow.setVisibility(View.GONE);
            holder.imgUnFollow.setVisibility(View.VISIBLE);
        }

        Glide.with(context).load(followersArrayList.get(position).getUserPhoto()).placeholder(R.drawable.ic_profile_default).into(holder.imgPersonal);
        holder.imgFollow.setOnClickListener(v -> {
            holder.imgUnFollow.setVisibility(View.VISIBLE);
            holder.imgFollow.setVisibility(View.GONE);
            followVendor(ShareProfileData.getInstance(context).getKEYID(), followersArrayList.get(position).getUserId());

        });
        holder.imgUnFollow.setOnClickListener(v -> {
            holder.imgFollow.setVisibility(View.VISIBLE);
            holder.imgUnFollow.setVisibility(View.GONE);
            unFollowVendor(ShareProfileData.getInstance(context).getKEYID(), followersArrayList.get(position).getUserId());
        });

    }

    private void unFollowVendor(String userId, String venId) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.UnFollowToMarket,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
//                            retrieveVendorFollowers(venId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cap_id", userId);
                params.put("ven_id", venId);
                return params;
            }
        };
        RequestHand.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void followVendor(String userId, String venId) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    ConnectionSrever.FollowToMarlet,
                    response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")) {
//                                retrieveVendorFollowers(venId);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {

            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("ven_id", venId);
                    params.put("cap_id", userId);
                    return params;
                }
            };
            RequestHand.getInstance(context).addToRequestQueue(stringRequest);
        }



    @Override
    public int getItemCount() {
        return followersArrayList.size();
    }

    public class myHolder extends RecyclerView.ViewHolder{

        private TextView txtName;
        private ImageButton  imgFollow,imgUnFollow , imgDetails;
        private CircleImageView imgPersonal;

        public myHolder(@NonNull View itemView) {
            super(itemView);

            txtName=itemView.findViewById(R.id.txtName);
            imgFollow=itemView.findViewById(R.id.imgFollow);
            imgDetails=itemView.findViewById(R.id.imgDetails);
            imgPersonal=itemView.findViewById(R.id.imgPersonal);
            imgUnFollow=itemView.findViewById(R.id.imgUnfollow);
        }
    }
}
//        holder.imgDetails.setOnClickListener(v -> {
//                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                sharedPreferences = context.getSharedPreferences("shareCaptainId", Context.MODE_PRIVATE);
//@SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("key1", followersArrayList.get(position).getUserId());
//        editor.apply();
//        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment2()).addToBackStack(null).commit();
//        });
