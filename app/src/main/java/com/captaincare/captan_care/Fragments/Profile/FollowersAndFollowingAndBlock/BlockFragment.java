package com.captaincare.captan_care.Fragments.Profile.FollowersAndFollowingAndBlock;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.captaincare.captan_care.Adapters.BlockAdapter;
import com.captaincare.captan_care.Models.FollowAndBlockClass;
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

public class BlockFragment extends Fragment {
    private RecyclerView recyclerBlock;
    private ArrayList<FollowAndBlockClass> blockArrayList;
    private ProgressBar progressBar;


    public BlockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_block, container, false);
        recyclerBlock = view.findViewById(R.id.recycleBlock);
        blockArrayList=new ArrayList<>();
        progressBar=view.findViewById(R.id.progress);

        retrieveCaptainBlock();

        return view;
    }
    private void retrieveCaptainBlock() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.MyBlockCaptain,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")){
                                JSONArray jsonArray=jsonObject.getJSONArray("message");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject object=jsonArray.getJSONObject(i);
                                    FollowAndBlockClass followAndBlockClass=new FollowAndBlockClass(
                                            object.getString("ven_id"),object.getString("ven_name"),
                                            object.getString("ven_photo_url"),object.getString("ven_token"),
                                            object.getString("b_id"));
                                    blockArrayList.add(followAndBlockClass);
                                }
                                BlockAdapter blockAdapter = new BlockAdapter(getActivity(),blockArrayList);
                                recyclerBlock.setAdapter(blockAdapter);
                                recyclerBlock.setLayoutManager(new GridLayoutManager(getActivity(),1));
                                blockAdapter.notifyDataSetChanged();
                            }else if (jsonObject.getString("error").equals("true")){

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cap_id", ShareProfileData.getInstance(getActivity()).getKEYID());
                return params;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
