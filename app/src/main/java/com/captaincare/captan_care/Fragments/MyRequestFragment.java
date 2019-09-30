package com.captaincare.captan_care.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.captaincare.captan_care.Activities.MainActivity;
import com.captaincare.captan_care.Activities.AddFreeOfferActivity;
import com.captaincare.captan_care.Activities.AddPaidOfferActivity;
import com.captaincare.captan_care.Adapters.MyRequestAdapter;
import com.captaincare.captan_care.Models.MyRequestClass;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;
import com.captaincare.captan_care.Manegers.ShareProfileData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MyRequestFragment extends Fragment {

    private Button btnAddRequest, btnPushRequest;
    private LinearLayout linearEmptyRequest;
    private ProgressBar progressMyRequest;

    private RecyclerView recyclerMyRequest;
    private ArrayList<MyRequestClass> myRequestList;

    private SharedPreferences userPreferences;
    private String userId;

    public MyRequestFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_request, container, false);
        Objects.requireNonNull(getActivity()).setTitle(R.string.requests_history);

        myRequestList = new ArrayList<>();
        recyclerMyRequest = view.findViewById(R.id.recycleMyRequests);
        progressMyRequest = view.findViewById(R.id.progress);

        linearEmptyRequest = view.findViewById(R.id.linearRequestEmpty);
        btnAddRequest = view.findViewById(R.id.button_addRequest);
        btnPushRequest = view.findViewById(R.id.button_pushٌRequest);

        retrieveRequests();
        return view;
    }

    private void retrieveRequests() {
        linearEmptyRequest.setVisibility(View.GONE);
        progressMyRequest.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.CaptainMyAds,
                response -> {
                    progressMyRequest.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("message");
                            myRequestList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                MyRequestClass myRequestClass = new MyRequestClass(
                                        object.getString("offer_id"), object.getString("cap_id"),
                                        object.getString("sub_id"), object.getString("c_id"),
                                        object.getString("sub_name"), object.getString("sub_name_ar"),
                                        object.getString("city"), object.getString("offer_title"),
                                        object.getString("offer_disc"), object.getString("offer_pic1"),
                                        object.getString("offer_pic2"), object.getString("offer_pic3"),
                                        object.getString("offer_pic4"), object.getString("offer_start"),
                                        object.getString("offer_end"), object.getString("isEnd"),
                                        object.getString("cost"),object.getString("address")
                                );
                                myRequestList.add(myRequestClass);
                            }
                            MyRequestAdapter myRequestAdapter = new MyRequestAdapter(getActivity(), myRequestList);
                            recyclerMyRequest.setAdapter(myRequestAdapter);
                            recyclerMyRequest.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                            myRequestAdapter.notifyDataSetChanged();

                        } else if (jsonObject.getString("error").equals("true")) {
                            //You Don`t have Offer
                            linearEmptyRequest.setVisibility(View.VISIBLE);
                            btnAddRequest.setOnClickListener(v -> {
                                startActivity(new Intent(getActivity(), AddFreeOfferActivity.class));
                            });
                            btnPushRequest.setOnClickListener(v->{
                                startActivity(new Intent(getActivity(), AddPaidOfferActivity.class));
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressMyRequest.setVisibility(View.GONE);
                    linearEmptyRequest.setVisibility(View.GONE);
                    Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(R.id.frame_layout), R.string.check_internet_connection, Snackbar.LENGTH_SHORT).show();

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


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).SetNavItemChecked(2);
        ((MainActivity) Objects.requireNonNull(getActivity())).setToolbarVisibility(View.VISIBLE);
    }
}
