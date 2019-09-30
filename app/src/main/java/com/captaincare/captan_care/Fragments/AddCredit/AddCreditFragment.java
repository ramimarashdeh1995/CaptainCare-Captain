package com.captaincare.captan_care.Fragments.AddCredit;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class AddCreditFragment extends Fragment {

    private RecyclerView recyclerCC;
    private ArrayList<PymentCC> pymentCCArrayList;
    private ProgressBar progressBar;



    public AddCreditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cc, container, false);
        Objects.requireNonNull(getActivity()).setTitle(R.string.add_credit);

        // Inflate the layout for this fragment
        recyclerCC=view.findViewById(R.id.recycleCC);
        progressBar=view.findViewById(R.id.progress);

        pymentCCArrayList=new ArrayList<>();

        RetrivePackeg();


        return view;
    }

    private void RetrivePackeg() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.PackegCC,
                response -> {
            progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")){
                            JSONArray jsonArray=jsonObject.getJSONArray("message");
                            pymentCCArrayList.clear();
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject object=jsonArray.getJSONObject(i);
                                PymentCC pymentCC=new PymentCC(
                                        object.getString("pid"),
                                        object.getString("pcc"),
                                        object.getString("pprice")
                                );
                                pymentCCArrayList.add(pymentCC);
                            }
                            PymentCCAdaptar categoryAdapter = new PymentCCAdaptar(getActivity(),
                                    pymentCCArrayList );
                            recyclerCC.setAdapter(categoryAdapter);
                            recyclerCC.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            categoryAdapter.notifyDataSetChanged();
                        }else if (jsonObject.getString("error").equals("true")){
                            Toast.makeText(
                                    getActivity(),
                                    jsonObject.getString("message"),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
            progressBar.setVisibility(View.GONE);
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("ccpackage","ccpackage");
                return  params;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
    @Override
    public void onResume() {
        super.onResume();
//        mapView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
//        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        mapView.onLowMemory();
    }

}
