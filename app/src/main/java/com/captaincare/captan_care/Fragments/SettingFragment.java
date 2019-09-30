package com.captaincare.captan_care.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.captaincare.captan_care.Language.LocaleHelpar;
import com.captaincare.captan_care.Activities.MainActivity;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;
import com.captaincare.captan_care.Manegers.ShareProfileData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.paperdb.Paper;

public class SettingFragment extends Fragment {

    CardView cardEditPass;
    Button btnArabic, btnEnglish;

    private TextView txtState;
    private SwitchCompat state;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle(R.string.setting1);

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        cardEditPass = view.findViewById(R.id.cardEditPass);

        state = view.findViewById(R.id.state);
        txtState = view.findViewById(R.id.txtState);

        btnArabic = view.findViewById(R.id.btnArabic);
        btnEnglish = view.findViewById(R.id.btnEnglish);


        Paper.init(getActivity());
        String Language = Paper.book().read("language");
        if (Language.equals("ar")) {
            btnArabic.setSelected(true);
            btnEnglish.setSelected(false);
        } else {
            btnArabic.setSelected(false);
            btnEnglish.setSelected(true);
        }


        if (ShareProfileData.getInstance(getActivity()).ISLOGIN()) {
            state.setChecked(true);
            txtState.setText(getString(R.string.online));
            txtState.setTextColor(getResources().getColor(R.color.baseColor));
        } else {
            state.setChecked(false);
            txtState.setText(getString(R.string.offline));
            txtState.setTextColor(getResources().getColor(R.color.red));
        }

        state.setOnClickListener(v -> {
            if (ShareProfileData.getInstance(getActivity()).ISLOGIN()) {
                ShareProfileData.getInstance(getActivity()).LogoutSTATE();
                state.setChecked(false);
                txtState.setText(getString(R.string.offline));
                txtState.setTextColor(getResources().getColor(R.color.red));
                updateUserState("0");
            } else {
                ShareProfileData.getInstance(getActivity()).LOGIN("1");
                state.setChecked(true);
                txtState.setText(getString(R.string.online));
                txtState.setTextColor(getResources().getColor(R.color.baseColor));
                updateUserState("1");
            }
        });

        cardEditPass.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ChangePasswordFragment()).addToBackStack(null).commit();
        });

        btnArabic.setOnClickListener(v -> {
            btnArabic.setSelected(true);
            btnEnglish.setSelected(false);
            Paper.book().write("language", "ar");
            updateLanguage(Paper.book().read("language"));
        });
        btnEnglish.setOnClickListener(v -> {
            btnEnglish.setSelected(true);
            btnArabic.setSelected(false);
            Paper.book().write("language", "en");
            updateLanguage(Paper.book().read("language"));
        });


        return view;
    }

    private void updateLanguage(String language) {
        Context context = LocaleHelpar.setLocal(getActivity(), language);
        Resources resources = context.getResources();

        Objects.requireNonNull(getActivity()).finish();
        startActivity(new Intent(getActivity(), MainActivity.class));

    }

    private void updateUserState(String s) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ConnectionSrever.State,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")) {

                        } else {
                            if (ShareProfileData.getInstance(getActivity()).ISLOGIN()) {
                                ShareProfileData.getInstance(getActivity()).LogoutSTATE();
                                state.setChecked(false);
                                txtState.setText(getString(R.string.offline));
                                txtState.setTextColor(getResources().getColor(R.color.red));
                            } else {
                                ShareProfileData.getInstance(getActivity()).LOGIN("1");
                                state.setChecked(true);
                                txtState.setText(getString(R.string.online));
                                txtState.setTextColor(getResources().getColor(R.color.blue));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
//                    if (ShareProfileData.getInstance(getActivity()).ISLOGIN()){
//                        ShareProfileData.getInstance(getActivity()).LogoutSTATE();
//                        state.setChecked(false);
//                        txtState.setText(getString(R.string.offline));
//                        txtState.setTextColor(getResources().getColor(R.color.red));
//                    }else {
//                        ShareProfileData.getInstance(getActivity()).LOGIN("1");
//                        state.setChecked(true);
//                        txtState.setText(getString(R.string.online));
//                        txtState.setTextColor(getResources().getColor(R.color.blue));
//                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("cap_id", ShareProfileData.getInstance(getActivity()).getKEYID());
                parms.put("state", s);
                return parms;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).setToolbarVisibility(View.VISIBLE);
    }
}
