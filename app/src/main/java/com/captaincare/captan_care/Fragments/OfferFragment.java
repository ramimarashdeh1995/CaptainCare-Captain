package com.captaincare.captan_care.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.captaincare.captan_care.Activities.MainActivity;
import com.captaincare.captan_care.Adapters.OfferAdapter;
import com.captaincare.captan_care.Models.OfferClass;
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


public class OfferFragment extends Fragment {

    private RecyclerView recyclerOffer;
    private ArrayList<OfferClass> offerArrayList;
    private ProgressBar progressBar;
    private LinearLayout linearOfferEmpty;

    // to get sub_CategoryId
    private SharedPreferences subCategoryPreferences ;
    private String sub_CategoryId;

    public OfferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_captain_offer, container, false);

        android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.customToolbar);
        TextView txtTitle = toolbar.findViewById(R.id.txtTitle);
        ImageView imgBack = toolbar.findViewById(R.id.imgBack);
        txtTitle.setText(R.string.offer_details);
        imgBack.setOnClickListener(v -> {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        });

        subCategoryPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("shareSubCat_ID", Context.MODE_PRIVATE);
        sub_CategoryId = subCategoryPreferences.getString("key_subCat", null);


        offerArrayList = new ArrayList<>();
        recyclerOffer = view.findViewById(R.id.recycleOffer);
        progressBar = view.findViewById(R.id.progress);

        linearOfferEmpty =view.findViewById(R.id.linearOfferEmpty);

        retrieveOffers(sub_CategoryId);
        return view;
    }

    private void retrieveOffers(String sub_CategoryId) {
        progressBar.setVisibility(View.VISIBLE);
        linearOfferEmpty.setVisibility(View.GONE);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.MarketOffer,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")){
                            JSONArray jsonArray=jsonObject.getJSONArray("message");
                            offerArrayList.clear();
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject object=jsonArray.getJSONObject(i);
                                OfferClass offerClass=new OfferClass(
                                        object.getString("offer_id"),object.getString("ven_id"),
                                        object.getString("sub_id"),object.getString("city"),
                                        object.getString("offer_title"),object.getString("offer_disc"),
                                        object.getString("offer_pic1"),object.getString("offer_pic2"),
                                        object.getString("offer_pic3"),object.getString("offer_pic4"),
                                        object.getString("offer_pic5"),object.getString("offer_pic6"),
                                        object.getString("offer_start"),object.getString("offer_end"),
                                        object.getString("isEnd"),object.getString("ven_name"),
                                        object.getString("ven_mobile"),object.getString("ven_city"),
                                        object.getString("ven_photo_url"),object.getString("ven_token"),
                                        object.getString("ven_address"),object.getString("isfav"),
                                        object.getString("ven_lon"),object.getString("ven_lat"),
                                        object.getString("isPaid"),object.getString("cost"),
                                        object.getString("eval")
                                );
                                offerArrayList.add(offerClass);
                            }
                            OfferAdapter offerAdapter = new OfferAdapter(getActivity(),offerArrayList);
                            recyclerOffer.setAdapter(offerAdapter);
                            recyclerOffer.setLayoutManager(new GridLayoutManager(getActivity(),1));
                            offerAdapter.notifyDataSetChanged();
                        }else if (jsonObject.getString("error").equals("true")){
                            linearOfferEmpty.setVisibility(View.VISIBLE);
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
                params.put("cap_id", ShareProfileData.getInstance(getActivity()).getKEYID());
                params.put("sub_id",sub_CategoryId);
                return params;
            }

        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setToolbarVisibility(View.GONE);
    }
}
