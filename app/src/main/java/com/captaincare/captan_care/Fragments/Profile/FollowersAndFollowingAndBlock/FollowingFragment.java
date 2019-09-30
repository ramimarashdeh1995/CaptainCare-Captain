package com.captaincare.captan_care.Fragments.Profile.FollowersAndFollowingAndBlock;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.captaincare.captan_care.Adapters.FollowersAdapter;
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

public class FollowingFragment extends Fragment {
    private RecyclerView recyclerFollowing;
    private ArrayList<FollowAndBlockClass> followingArrayList;
    private ProgressBar progressBar;


    public FollowingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following, container, false);
        recyclerFollowing=view.findViewById(R.id.recycleFollowing);
        followingArrayList=new ArrayList<>();
        progressBar=view.findViewById(R.id.progress);
        retrieveCaptainFollowing();
        return view;
    }
    private void retrieveCaptainFollowing() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.MyFolowingCaptain,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("message");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    FollowAndBlockClass followAndBlockClass = new FollowAndBlockClass(
                                            object.getString("ven_id"), object.getString("ven_name"),
                                            object.getString("ven_mobile"), object.getString("ven_city"),
                                            object.getString("ven_photo_url"), object.getString("ven_address"),
                                            object.getString("ven_lon"), object.getString("ven_lat"),
                                            object.getString("ven_token"),"1"
                                    );
                                    followingArrayList.add(followAndBlockClass);
                                }
                                FollowersAdapter followersAdapter = new FollowersAdapter(getActivity(), followingArrayList);
                                recyclerFollowing.setAdapter(followersAdapter);
                                recyclerFollowing.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                                followersAdapter.notifyDataSetChanged();
                            } else if (jsonObject.getString("error").equals("true")) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(getActivity().findViewById(R.id.frame_layout),R.string.check_internet_connection,Snackbar.LENGTH_SHORT).show();

                    }
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

}
