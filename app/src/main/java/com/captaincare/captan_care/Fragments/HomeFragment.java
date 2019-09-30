package com.captaincare.captan_care.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.captaincare.captan_care.Activities.MainActivity;
import com.captaincare.captan_care.Adapters.CategoryAdapter;
import com.captaincare.captan_care.Models.CategoryClass;
import com.captaincare.captan_care.Activities.AddFreeOfferActivity;
import com.captaincare.captan_care.Activities.AddPaidOfferActivity;
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

public class HomeFragment extends Fragment {
    private RecyclerView recyclerSection;
    private ArrayList<CategoryClass> categoryArrayListData;
    private Button btnAddOffer, btnAddPaidOffer;
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Objects.requireNonNull(getActivity()).setTitle(R.string.home_page);

        recyclerSection = view.findViewById(R.id.recycleSection);
        categoryArrayListData = new ArrayList<>();

        progressBar = view.findViewById(R.id.progress);
        btnAddOffer = view.findViewById(R.id.button_addRequest);
        btnAddPaidOffer = view.findViewById(R.id.button_addPaidOffer);

        btnAddOffer.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddFreeOfferActivity.class));
        });
        btnAddPaidOffer.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddPaidOfferActivity.class));
        });
        RetrieveCategory();
        return view;

    }

    private void RetrieveCategory() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConnectionSrever.getService,
                response -> {
                    try {
                        progressBar.setVisibility(View.GONE);
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("message");
                            categoryArrayListData.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                CategoryClass categoryClass = new CategoryClass(
                                        object.getString("cat_id"),
                                        object.getString("cat_Name_ar"),
                                        object.getString("cat_Name"),
                                        object.getString("cat_icon"),
                                        object.getString("cat_color")
                                );
                                categoryArrayListData.add(categoryClass);
                            }
                            CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), categoryArrayListData);
                            recyclerSection.setAdapter(categoryAdapter);
                            recyclerSection.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            categoryAdapter.notifyDataSetChanged();

                        } else if (jsonObject.getString("error").equals("true")) {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(R.id.linear_layout), R.string.check_internet_connection, Snackbar.LENGTH_SHORT).show();

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("category", "category");
                return params;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).SetNavItemChecked(1);
        ((MainActivity) Objects.requireNonNull(getActivity())).setToolbarVisibility(View.VISIBLE);
    }


}
