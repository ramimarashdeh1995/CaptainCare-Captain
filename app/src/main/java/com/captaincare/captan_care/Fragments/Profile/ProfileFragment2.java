package com.captaincare.captan_care.Fragments.Profile;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.captaincare.captan_care.Activities.MainActivity;
import com.captaincare.captan_care.Fragments.HomeFragment;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment2 extends Fragment {

    private TextView txtFollowers, txtFollowing, txtRate, txtAboutMe, txtName,txtName2, txtPhone, txtCity, txtAddress;
    private Button btnFollow, btnBlcok;
    private Spinner spinnerFollow;
    private CircleImageView imgProfile;
    private LinearLayout  linearAddress;
    private AlertDialog.Builder blockDialog;

    private RecyclerView recyclerMyService;
    private RelativeLayout relativeProfile,relativeprogerss;
    private SharedPreferences idPreferences, retrievePreferences;
    private String vendor_id;
    private String id,rate; // اللي داخل ع التطبيق
    private String user_id; // اللي بدي اروح ع صفحته

    private ProgressBar progressContract;

    public ProfileFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle(R.string.profile);

        View view = inflater.inflate(R.layout.fragment_profile2, container, false);

        relativeProfile = view.findViewById(R.id.relative_layout);
        relativeprogerss = view.findViewById(R.id.relativeprogerss);
        progressContract = view.findViewById(R.id.progressContract);


        btnFollow = view.findViewById(R.id.button_follow);
        btnBlcok = view.findViewById(R.id.button_UnBlock);
//        spinnerFollow = view.findViewById(R.id.spinnerFollow);
        linearAddress = view.findViewById(R.id.linearAddress);
        imgProfile = view.findViewById(R.id.image_profile);
        txtFollowers = view.findViewById(R.id.txtFollowers);
        txtFollowing = view.findViewById(R.id.txtFollowing);
        txtRate = view.findViewById(R.id.txtRate);
        txtName = view.findViewById(R.id.txtName);
        txtName2 = view.findViewById(R.id.txtName2);
        txtAboutMe = view.findViewById(R.id.txtAboutMe);
        txtPhone = view.findViewById(R.id.edtPhone);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtCity = view.findViewById(R.id.txtCity);

        retrievePreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("shareCaptainId", Context.MODE_PRIVATE);
        user_id = retrievePreferences.getString("key1", null);

        progressContract.setVisibility(View.VISIBLE);


        idPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("SharingProfile", Context.MODE_PRIVATE);
        id = idPreferences.getString("key_id", null);
        rate = idPreferences.getString("key_rate", null);
        view.findViewById(R.id.linearAddress).setVisibility(View.VISIBLE);
        // بسترجع بيانات صاحب المحل عشان اعرضها عند الكابتن
        retrieveVendorData(id, user_id);


        btnFollow.setOnClickListener(v -> {

            if (btnFollow.getText().toString().equals("Follow") || btnFollow.getText().toString().equals("متابعة")) {
                followVendor(id, user_id);
                btnFollow.setText(R.string.unfollow);
            } else if (btnFollow.getText().toString().equals("Un-Follow") || btnFollow.getText().toString().equals("الغاء متابعة")) {
                unFollowVendor(id, user_id);
                btnFollow.setText(R.string.follow);
            }


        });

        btnBlcok.setOnClickListener(v -> {

            blockDialog = new AlertDialog.Builder(getActivity());
            blockDialog.setTitle("Are you want to Block " + txtName.getText().toString() + " ?");
            blockDialog.setNegativeButton(R.string.cancel, null);

            blockDialog.setPositiveButton(R.string.ok, (dialog, which) -> {
                blockVendor(id, user_id);
                dialog.dismiss();
            });

            blockDialog.show();

        });

        return view;
    }
    private void ShowDataDisgn(){
        progressContract.setVisibility(View.GONE);
        relativeprogerss.setVisibility(View.GONE);
        relativeProfile.setVisibility(View.VISIBLE);
    }


    // Captain Follow Vendor الكابتن يتابع صاحب المحل
    private void followVendor(String userId, String venId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.FollowToMarlet,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            retrieveVendorFollowers(venId);
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
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void unFollowVendor(String userId, String venId) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.UnFollowToMarket,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            retrieveVendorFollowers(venId);
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
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void retrieveVendorData(String userId, String venId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.SelectInformationFromVenToCap,
                response -> {
            ShowDataDisgn();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            if (jsonObject.getString("following").equals("1")) {
                                btnFollow.setText(R.string.unfollow);
                            } else if (jsonObject.getString("following").equals("0")) {
                                btnFollow.setText(R.string.follow);
                            }
                            JSONObject object = jsonObject.getJSONObject("message");
                            if (object.getString("ven_photo_url").equals("null") || object.getString("ven_photo_url").equals("")) {
                                imgProfile.setImageResource(R.drawable.ic_profile_default);
                            } else {
                                Glide.with(getActivity()).load(object.getString("ven_photo_url")).skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE).into(imgProfile);
                            }
                            txtName.setText(object.getString("ven_name"));
                            txtName2.setText(object.getString("ven_name"));
                            txtCity.setText(object.getString("ven_city"));
                            txtAddress.setText(object.getString("ven_address"));
                            if (object.getString("about_me").equals("null")) {
                                txtAboutMe.setText("Welcome in My Profile");
                            } else {
                                txtAboutMe.setText(object.getString("about_me"));
                            }
                            txtPhone.setText(object.getString("ven_mobile"));
                            txtRate.setText(object.getString("eval"));
                            retrieveVendorFollowers(venId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ven_id", venId);
                params.put("cap_id", userId);
                return params;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void retrieveVendorFollowers(String venId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.SelectCounterFollowMarket,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            JSONObject object1 = jsonObject.getJSONObject("followere");
                            txtFollowers.setText(object1.getString("COUNT(captain_follow.ven_id)"));
                            JSONObject object2 = jsonObject.getJSONObject("following");
                            txtFollowing.setText(object2.getString("COUNT(vendor_follow.ven_id)"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ven_id", venId);
                return params;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void blockVendor(String userId, String venId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.BlockToMarket,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            assert getFragmentManager() != null;
                            getFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cap_id", userId);
                params.put("ven_id", venId);
                return params;
            }
        };

        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).setToolbarVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
//        retrieveMyService(vendor_id);
    }



}
