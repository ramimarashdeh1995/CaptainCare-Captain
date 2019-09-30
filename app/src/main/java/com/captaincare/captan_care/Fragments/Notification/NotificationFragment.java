package com.captaincare.captan_care.Fragments.Notification;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.captaincare.captan_care.Activities.MainActivity;
import com.captaincare.captan_care.Adapters.NotificationAdapter;
import com.captaincare.captan_care.Models.NotificationClass;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    RecyclerView recyclerNotification;
    NotificationAdapter notificationAdapter;
    ArrayList<NotificationClass> notificationList;

    LinearLayout linearNotificationEmpty;
    ProgressBar progressNotification;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        Objects.requireNonNull(getActivity()).setTitle(R.string.notifications);


        recyclerNotification = view.findViewById(R.id.recycleNotification);
        notificationList = new ArrayList<>();

        linearNotificationEmpty = view.findViewById(R.id.linearNotificationEmpty);
        progressNotification = view.findViewById(R.id.progressNotification);

        getNotification();
        return view;
    }

    private void getNotification() {
        progressNotification.setVisibility(View.VISIBLE);
        linearNotificationEmpty.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.GetNotification,
                response -> {
                    progressNotification.setVisibility(View.GONE);
                    try {
                        ReadJson(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            progressNotification.setVisibility(View.GONE);
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("cap_id", ShareProfileData.getInstance(getActivity()).getKEYID());
                return parms;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void ReadJson(String response) throws JSONException {
        notificationList.clear();
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.getString("error").equals("false")) {
            JSONArray array = jsonObject.getJSONArray("notification");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                NotificationClass notificationClass = new NotificationClass(
                        object.getString("imag"), object.getString("name"), object.getString("title"),
                        object.getString("offerid"), object.getString("ven_id"), object.getString("sub_id"),
                        object.getString("city_offer"), object.getString("cost"), object.getString("type"),
                        object.getString("city"), object.getString("lat"), object.getString("lon"),
                        object.getString("disc"), object.getString("address"), object.getString("time"),
                        object.getString("dateend"),object.getString("isend")
                );
                notificationList.add(notificationClass);
            }

            notificationAdapter = new NotificationAdapter(getActivity(), notificationList);
            recyclerNotification.setAdapter(notificationAdapter);
            recyclerNotification.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            notificationAdapter.notifyDataSetChanged();

        } else if (jsonObject.getString("error").equals("true")) {
            linearNotificationEmpty.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setToolbarVisibility(View.VISIBLE);
    }

}
