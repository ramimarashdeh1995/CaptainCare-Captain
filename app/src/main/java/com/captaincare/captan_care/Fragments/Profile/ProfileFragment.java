package com.captaincare.captan_care.Fragments.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.captaincare.captan_care.Fragments.Profile.FollowersAndFollowingAndBlock.BlockFragment;
import com.captaincare.captan_care.Fragments.Profile.FollowersAndFollowingAndBlock.FollowersFragment;
import com.captaincare.captan_care.Fragments.Profile.FollowersAndFollowingAndBlock.FollowingFragment;
import com.captaincare.captan_care.Activities.MainActivity;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;
import com.captaincare.captan_care.Manegers.ShareProfileData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private TextView txtFollowers, txtFollowing, txtRate, txtBlock, txtName, txtPhone, txtCity, txtLicense,
            txtAboutMe,
            txtpuch1, txtpuch2, txtpuch3, txtpuch4,
            txtfrre1, txtfrre2, txtfrre3, txtfrre4,
            txtcost,
            txtpireod;


    private ImageButton imgEditPayment, imgEditProfile, imgEditAboutMe, imgCancelAboutMe;
    private EditText editTextAboutMe;
    private TextInputLayout textInputAboutMe;
    private Button btnAboutMe;
    private CircleImageView imgProfile;

    private LinearLayout linearFollowers, linearFollowing, linearBlockers;

    private RelativeLayout relativeProfile, relativeprogerss;
    private ProgressBar progressBar, progressContract;
    private String imgUrl;

    private DiagonalLayout diagonalLayout, diagonalLayout2;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Objects.requireNonNull(getActivity()).setTitle(R.string.my_profile);


        relativeProfile = view.findViewById(R.id.relativeProfile);
        relativeprogerss = view.findViewById(R.id.relativeprogerss);
        progressContract = view.findViewById(R.id.progressContract);


        progressBar = view.findViewById(R.id.progress);

        linearFollowers = view.findViewById(R.id.linearFollowers);
        linearFollowing = view.findViewById(R.id.linearFollowing);
        linearBlockers = view.findViewById(R.id.linearBlockers);
        linearFollowers.setOnClickListener(this);
        linearFollowing.setOnClickListener(this);
        linearBlockers.setOnClickListener(this);

        imgEditProfile = view.findViewById(R.id.imgEditProfile);
        imgEditPayment = view.findViewById(R.id.imgEditPayment);
        imgEditAboutMe = view.findViewById(R.id.imgEditAboutMe);
        imgCancelAboutMe = view.findViewById(R.id.imgCancelAboutMe);


        imgProfile = view.findViewById(R.id.image_profile);

        txtName = view.findViewById(R.id.txtName);
        txtPhone = view.findViewById(R.id.edtPhone);
        txtCity = view.findViewById(R.id.txtCity);

        txtLicense = view.findViewById(R.id.txtLicense);
        txtAboutMe = view.findViewById(R.id.txtAboutMe);

        txtFollowers = view.findViewById(R.id.txtFollowers);
        txtFollowing = view.findViewById(R.id.txtFollowing);
        txtRate = view.findViewById(R.id.txtRate);
        txtBlock = view.findViewById(R.id.txtBlockers);

        editTextAboutMe = view.findViewById(R.id.editTextAboutMe);
        textInputAboutMe = view.findViewById(R.id.txtInputLayout);
        btnAboutMe = view.findViewById(R.id.button_update);


        txtpuch1 = view.findViewById(R.id.txtpuch1);
        txtpuch2 = view.findViewById(R.id.txtpuch2);
        txtpuch3 = view.findViewById(R.id.txtpuch3);
        txtpuch4 = view.findViewById(R.id.txtpuch4);

        txtfrre1 = view.findViewById(R.id.txtfree1);
        txtfrre2 = view.findViewById(R.id.txtfree2);
        txtfrre3 = view.findViewById(R.id.txtfree3);
        txtfrre4 = view.findViewById(R.id.txtfree4);

        txtcost = view.findViewById(R.id.txtcost);

        txtpireod = view.findViewById(R.id.txtpireod);

        btnAboutMe.setOnClickListener(this);
        imgEditProfile.setOnClickListener(this);
        imgEditPayment.setOnClickListener(this);
        imgEditAboutMe.setOnClickListener(this);
        imgCancelAboutMe.setOnClickListener(this);

        diagonalLayout = view.findViewById(R.id.diagonalLayout);
        diagonalLayout2 = view.findViewById(R.id.diagonalLayout2);

        progressContract.setVisibility(View.VISIBLE);

        Paper.init(getActivity());
        String Language = Paper.book().read("language");
        if (Language == null) {
            Paper.book().write("language", "en");
            diagonalLayout.setDiagonalDirection(DiagonalLayout.DIRECTION_RIGHT);
            diagonalLayout2.setDiagonalDirection(DiagonalLayout.DIRECTION_LEFT);
        } else if (Language.equals("ar")) {
            Paper.book().write("language", Language);
            diagonalLayout.setDiagonalDirection(DiagonalLayout.DIRECTION_LEFT);
            diagonalLayout2.setDiagonalDirection(DiagonalLayout.DIRECTION_RIGHT);
        }

        retrieveProfileData();

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgEditProfile:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, new EditProfileFragment()).addToBackStack(null).commit();
                break;
            case R.id.imgEditAboutMe:
                txtAboutMe.setVisibility(View.GONE);
                imgEditAboutMe.setVisibility(View.GONE);
                textInputAboutMe.setVisibility(View.VISIBLE);
                editTextAboutMe.setVisibility(View.VISIBLE);
                editTextAboutMe.setText(txtAboutMe.getText().toString());
                btnAboutMe.setVisibility(View.VISIBLE);
                imgCancelAboutMe.setVisibility(View.VISIBLE);
                btnAboutMe.setOnClickListener(v1 -> {
                    if (editTextAboutMe.getText().length() > 60) {
                        textInputAboutMe.setError("Just 60 character");
                        editTextAboutMe.requestFocus();
                    } else {
                        updateAboutMe(editTextAboutMe.getText().toString());
                    }
                });
                break;
            case R.id.imgCancelAboutMe:
                txtAboutMe.setVisibility(View.VISIBLE);
                imgEditAboutMe.setVisibility(View.VISIBLE);
                textInputAboutMe.setVisibility(View.GONE);
                imgCancelAboutMe.setVisibility(View.GONE);
                editTextAboutMe.setVisibility(View.GONE);
                editTextAboutMe.setText("");
                btnAboutMe.setVisibility(View.GONE);
                break;
            case R.id.linearFollowers:
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.container, new FollowersFragment()).addToBackStack(null).commit();
                break;
            case R.id.linearFollowing:
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.container, new FollowingFragment()).addToBackStack(null).commit();
                break;
            case R.id.linearBlockers:
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.container, new BlockFragment()).addToBackStack(null).commit();
                break;
        }
    }

    private void retrieveProfileData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.SelectInformaionCaptain,
                response -> {
                    ShowDataDesign();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject objectInformation = jsonObject.getJSONObject("Information");

                        imgUrl = objectInformation.getString("cap_photo_url");

                        Glide.with(Objects.requireNonNull(getActivity()))
                                .load(imgUrl).placeholder(R.drawable.ic_profile_default)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(imgProfile);

                        txtName.setText(objectInformation.getString("cap_name"));
                        txtPhone.setText(objectInformation.getString("cap_mobile"));
                        txtCity.setText(objectInformation.getString("cap_city"));
                        txtLicense.setText(objectInformation.getString("cap_username"));

                        if (objectInformation.getString("about_me").equals("null")) {
                            txtAboutMe.setText("Welcome in My Profile");
                        } else {
                            txtAboutMe.setText(objectInformation.getString("about_me"));
                        }

                        Glide.with(Objects.requireNonNull(getActivity()))
                                .load(objectInformation.getString("plan_logo"))
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(imgEditPayment);

                        txtpireod.setText(objectInformation.getString("plan_period") + " " + getString(R.string.day));
                        txtcost.setText(objectInformation.getString("cap_cc") + " CC Coins");

                        txtpuch1.setText(objectInformation.getString("PO2H"));
                        txtpuch2.setText(objectInformation.getString("PO4H"));
                        txtpuch3.setText(objectInformation.getString("PO12H"));
                        txtpuch4.setText(objectInformation.getString("PO24H"));

                        txtfrre1.setText(objectInformation.getString("AP4H"));
                        txtfrre2.setText(objectInformation.getString("AP8H"));
                        txtfrre3.setText(objectInformation.getString("AP24H"));
                        txtfrre4.setText(objectInformation.getString("AP72H"));

                        ShareProfileData.getInstance(getActivity()).SetReatAndPoint(objectInformation.getString("eval"), objectInformation.getString("cap_point"));

                        if (ShareProfileData.getInstance(getActivity()).getKeyRate().equals("null")) {
                            txtRate.setText("5");
                        } else {
                            txtRate.setText(ShareProfileData.getInstance(getActivity()).getKeyRate());
                        }

                        JSONObject objectFollowers = jsonObject.getJSONObject("followere");
                        txtFollowing.setText(objectFollowers.getString("COUNT(vendor_follow.cap_id)"));
                        JSONObject objectFollowing = jsonObject.getJSONObject("following");
                        txtFollowers.setText(objectFollowing.getString("COUNT(captain_follow.cap_id)"));
                        JSONObject objectBlock = jsonObject.getJSONObject("block");
                        txtBlock.setText(objectBlock.getString("COUNT(captain_block.cap_id)"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cap_id", ShareProfileData.getInstance(getActivity()).getKEYID());
                return params;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void ShowDataDesign() {
        progressContract.setVisibility(View.GONE);
        relativeprogerss.setVisibility(View.GONE);
        relativeProfile.setVisibility(View.VISIBLE);
    }


    private void updateAboutMe(String textAboutMe) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConnectionSrever.UpdateAboutMe,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            txtAboutMe.setText(textAboutMe);
                            txtAboutMe.setVisibility(View.VISIBLE);
                            imgEditAboutMe.setVisibility(View.VISIBLE);
                            textInputAboutMe.setVisibility(View.GONE);
                            imgCancelAboutMe.setVisibility(View.GONE);
                            editTextAboutMe.setVisibility(View.GONE);
                            editTextAboutMe.setText("");
                            btnAboutMe.setVisibility(View.GONE);
                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            progressBar.setVisibility(View.GONE);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cap_id", ShareProfileData.getInstance(getActivity()).getKEYID());
                params.put("about_me", textAboutMe);
                return params;
            }

        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).SetNavItemChecked(0);
        ((MainActivity) Objects.requireNonNull(getActivity())).setToolbarVisibility(View.VISIBLE);
        retrieveProfileData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
