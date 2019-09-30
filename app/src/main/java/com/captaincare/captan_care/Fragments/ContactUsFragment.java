package com.captaincare.captan_care.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.captaincare.captan_care.Activities.MainActivity;
import com.captaincare.captan_care.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {

    private CardView phone_card;
    private TextView phone_txt;

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        phone_card = view.findViewById(R.id.phone_card);
        phone_txt = view.findViewById(R.id.phone_txt);

        phone_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneCall = new Intent(Intent.ACTION_DIAL);
                phoneCall.setData(Uri.parse("tel" + phone_txt.getText().toString()));
                startActivity(phoneCall);
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).setToolbarVisibility(View.VISIBLE);
    }
}