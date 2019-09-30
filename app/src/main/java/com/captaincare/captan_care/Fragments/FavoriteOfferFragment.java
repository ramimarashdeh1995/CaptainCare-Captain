package com.captaincare.captan_care.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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


public class FavoriteOfferFragment extends Fragment {


    private ArrayList<OfferClass> favoriteOfferArrayList;
    private RecyclerView recyclerFavoriteOffer;
    private ProgressBar progressBar;
    private LinearLayout linearOfferEmpty;

    public FavoriteOfferFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Objects.requireNonNull(getActivity()).setTitle(R.string.favorite_offer);
        View view = inflater.inflate(R.layout.fragment_favorite_offer, container, false);

        favoriteOfferArrayList = new ArrayList<>();
        recyclerFavoriteOffer = view.findViewById(R.id.recycleFavoriteOffer);

        progressBar = view.findViewById(R.id.progressFavorite);

        linearOfferEmpty=view.findViewById(R.id.linearRequestEmpty);

        retrieveFavoriteOffers();

        return view;
    }
    private void retrieveFavoriteOffers(){
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.MyFavoriteCaptin,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        ReadFromJSON(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }){
            @Override
            protected Map<String, String> getParams()  {
                Map<String,String> param=new HashMap<>();
                param.put("cap_id", ShareProfileData.getInstance(getActivity()).getKEYID());
                return param;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }




    private void ReadFromJSON(JSONObject jsonObject){
        try {
            if (jsonObject.getString("error").equals("false")){
                JSONArray jsonArray=jsonObject.getJSONArray("message");
                ReadFromJSONARRAY_LOGIN_CAPTAIN(jsonArray);

            }else if (jsonObject.getString("error").equals("true")){
                linearOfferEmpty.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ReadFromJSONARRAY_LOGIN_CAPTAIN(JSONArray jsonArray) throws JSONException {
        favoriteOfferArrayList.clear();
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
                    object.getString("ven_address"),"1",
                    object.getString("ven_lon"),object.getString("ven_lat"),
                    object.getString("isPaid"),object.getString("cost"),
                    object.getString("eval")
            );
            favoriteOfferArrayList.add(offerClass);
        }
        OfferAdapter offerAdapter = new OfferAdapter(getActivity(),favoriteOfferArrayList);
        recyclerFavoriteOffer.setAdapter(offerAdapter);
        recyclerFavoriteOffer.setLayoutManager(new GridLayoutManager(getActivity(),1));
        offerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).setToolbarVisibility(View.VISIBLE);
    }
}
