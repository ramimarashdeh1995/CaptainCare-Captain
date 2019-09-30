package com.captaincare.captan_care.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.captaincare.captan_care.Activities.MainActivity;
import com.captaincare.captan_care.Adapters.CurrentOfferAdapter;
import com.captaincare.captan_care.Models.CurrentOfferClass;
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


public class CurrentOfferFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout linearOfferEmpty;

    private ArrayList<CurrentOfferClass>currentOfferArrayList;

    private CurrentOfferAdapter myOffersAdapter ;

    public CurrentOfferFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle(R.string.current_offers);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_offer, container, false);
        currentOfferArrayList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycleCurrentOffers);
        progressBar = view.findViewById(R.id.progress);
        linearOfferEmpty=view.findViewById(R.id.linearRequestEmpty);
        getCurrentOffer("cap_id");

        return view;
    }

    private void getCurrentOffer(String UserLOgIn) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.SelectCurrentNowOffer,
                response -> {
            progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")){
                            JSONArray jsonArray=jsonObject.getJSONArray("message");
                            ReadJsonArrayCaptain(jsonArray);
                        }else if (jsonObject.getString("error").equals("true")){
                            linearOfferEmpty.setVisibility(View.VISIBLE);
                            linearOfferEmpty.setGravity(Gravity.CENTER);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>parms=new HashMap<>();
                parms.put(UserLOgIn, ShareProfileData.getInstance(getActivity()).getKEYID());
                return parms;
            }

        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void ReadJsonArrayCaptain(JSONArray jsonArray) throws JSONException {
        currentOfferArrayList.clear();
        for (int i=0;i<jsonArray.length();i++){
            JSONObject object=jsonArray.getJSONObject(i);
            CurrentOfferClass currentOffer=new CurrentOfferClass(
                    object.getString("ven_id"),
                    object.getString("ven_name"),
                    object.getString("ven_photo_url"),
                    object.getString("ven_mobile"),
                    object.getString("save_id"),
                    object.getString("eval"),
                    object.getString("id_offer_cap"),
                    object.getString("id_offer_ven"),
                    object.getString("ven_lon"),
                    object.getString("ven_lat"),
                    object.getString("pic1"),
                    object.getString("pic2"),
                    object.getString("pic3"),
                    object.getString("pic4"),
                    object.getString("title"),
                    object.getString("disc"),
                    object.getString("dateend"),
                    object.getString("cap_pic1"),
                    object.getString("cap_pic2"),
                    object.getString("cap_pic3"),
                    object.getString("cap_pic4"),
                    object.getString("cap_title"),
                    object.getString("cap_disc"),
                    object.getString("cap_dateend"),
                    object.getString("end_process"),
                    object.getString("cost_ven"),
                    object.getString("cost_cap"),
                    object.getString("city_ven"),
                    object.getString("city_cap")
            );
            currentOfferArrayList.add(currentOffer);
        }
        myOffersAdapter = new CurrentOfferAdapter(getActivity(), currentOfferArrayList);
        recyclerView.setAdapter(myOffersAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        myOffersAdapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).SetNavItemChecked(5);
        ((MainActivity) Objects.requireNonNull(getActivity())).setToolbarVisibility(View.VISIBLE);
    }
}
