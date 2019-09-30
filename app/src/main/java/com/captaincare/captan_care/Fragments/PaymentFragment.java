package com.captaincare.captan_care.Fragments;


import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.captaincare.captan_care.Activities.MainActivity;
import com.captaincare.captan_care.Models.ClassPlan;
import com.captaincare.captan_care.R;
import com.captaincare.captan_care.ServerClass.ConnectionSrever;
import com.captaincare.captan_care.ServerClass.RequestHand;
import com.captaincare.captan_care.Manegers.ShareProfileData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class PaymentFragment extends Fragment {

    private RecyclerView recyclerPackage;
    private ProgressBar progressBar,progressContract,progressContract1;
    private LinearLayout linearPackage,linearpush5,linearfree5,linearpush6,linearfree6,linearProgress;

    private AlertDialog.Builder  dialogShow,dialog;


    private CircleImageView imgplan;
    private TextView nameplan,
            push1,push2,push3,push4,
            free1,free2,free3,free4,
            txtpuch1,txtpuch2,txtpuch3,txtpuch4,
            txtfrre1,txtfrre2,txtfrre3,txtfrre4,
            cost,txtcost,
            pireod,txtpireod;

    private Button btnSubscribe;

    private Spinner spinnerPackage;

    private ArrayList<ClassPlan>classPlanArrayList;

    private String[] PlanName_AR;
    private String[] PlanName_EN;

    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        Objects.requireNonNull(getActivity()).setTitle(R.string.change_subsections);

        linearPackage=view.findViewById(R.id.linearPackage);
        linearProgress=view.findViewById(R.id.linearProgress);


        imgplan=view.findViewById(R.id.imgplan);

        nameplan = view.findViewById(R.id.nameplan);

        push1 = view.findViewById(R.id.push1);
        push2 = view.findViewById(R.id.push2);
        push3 = view.findViewById(R.id.push3);
        push4 = view.findViewById(R.id.push4);

        free1 = view.findViewById(R.id.free1);
        free2 = view.findViewById(R.id.free2);
        free3 = view.findViewById(R.id.free3);
        free4 = view.findViewById(R.id.free4);

        txtpuch1=view.findViewById(R.id.txtpuch1);
        txtpuch2=view.findViewById(R.id.txtpuch2);
        txtpuch3=view.findViewById(R.id.txtpuch3);
        txtpuch4=view.findViewById(R.id.txtpuch4);

        txtfrre1=view.findViewById(R.id.txtfree1);
        txtfrre2=view.findViewById(R.id.txtfree2);
        txtfrre3=view.findViewById(R.id.txtfree3);
        txtfrre4=view.findViewById(R.id.txtfree4);

        cost=view.findViewById(R.id.cost);
        txtcost=view.findViewById(R.id.txtcost);

        pireod=view.findViewById(R.id.pireod);
        txtpireod=view.findViewById(R.id.txtpireod);

        spinnerPackage=view.findViewById(R.id.spinnerPackage);

        progressContract=view.findViewById(R.id.progressContract);
        progressContract1=view.findViewById(R.id.progressContract1);


        btnSubscribe=view.findViewById(R.id.btnSubscribe);

        classPlanArrayList=new ArrayList<>();

        SetTextToLOGIN();



        return view;
    }


    private void SetTextToLOGIN() {
        progressContract1.setVisibility(View.VISIBLE);


            push1.setText(R.string.h2);
            push2.setText(R.string.h4);
            push3.setText(R.string.h12);
            push4.setText(R.string.h24);

            free1.setText(R.string.h2);
            free2.setText(R.string.h4);
            free3.setText(R.string.h24);
            free4.setText(R.string.h72);



            RetriveDataPlanCaptain();


    }



    private void ViewPlan(String apr, String rpr, String plan_id, String plan_logo, String plan_name_en, String plan_name_ar,
                                String free1, String free2, String free3, String free4,
                                String push1, String push2, String push3, String push4,
                                String plan_price, String plan_period) {


        txtpuch1.setText(push1);
        txtpuch2.setText(push2);
        txtpuch3.setText(push3);
        txtpuch4.setText(push4);

        txtfrre1.setText(free1);
        txtfrre2.setText(free2);
        txtfrre3.setText(free3);
        txtfrre4.setText(free4);


        nameplan.setText(plan_name_en);
        //imgplan.setImageBitmap(getBitmapFromURL(plan_logo));
        Glide.with(getActivity()).load(plan_logo).into(imgplan);

        txtcost.setText(plan_price+" CC Coins");
        txtpireod.setText(plan_period+" "+getString(R.string.month));

        btnSubscribe.setOnClickListener(v-> {
            dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("Are you sure to Subscribe the Plan "+plan_name_en);
            dialog.setIcon(R.drawable.ic_successfull);
            dialog.setPositiveButton("Yes",(dialog,which)->{
                progressContract.setVisibility(View.VISIBLE);
                dialog.dismiss();

                SubscribePlanCaptain(plan_id);

            });
            dialog.setNeutralButton("No",(dialog,which)->{
                dialog.dismiss();
            });
            dialog.show();

        });

    }


    private void SubscribePlanCaptain(String plan_id) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.SubsicrebPlanCaptain,
                response -> {
            progressContract.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if (jsonObject.getString("error").equals("false")){
                            dialogShow = new AlertDialog.Builder(getActivity());
                            dialogShow.setTitle("Successful");
                            dialogShow.setIcon(R.drawable.ic_successfull);
                            dialogShow.setPositiveButton(this.getString(R.string.yes), (dialog, which) -> {
                                dialog.dismiss();
                            });
                            dialogShow.show();
                        }else if (jsonObject.getString("error").equals("true")){
                            if (jsonObject.getString("message").equals("not your have cc")){
                                dialogShow = new AlertDialog.Builder(getActivity());
                                dialogShow.setTitle("your not have CC");
                                dialogShow.setIcon(R.drawable.ic_error2);
                                dialogShow.setPositiveButton(this.getString(R.string.yes), (dialog, which) -> {
                                    dialog.dismiss();
                                });
                                dialogShow.show();
                            }else {
                                dialogShow = new AlertDialog.Builder(getActivity());
                                dialogShow.setTitle("error in server");
                                dialogShow.setIcon(R.drawable.ic_error2);
                                dialogShow.setPositiveButton(this.getString(R.string.yes), (dialog, which) -> {
                                    dialog.dismiss();
                                });
                                dialogShow.show();
                            }
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
                parms.put("cap_id", ShareProfileData.getInstance(getActivity()).getKEYID());
                parms.put("plan_id",plan_id);
                return parms;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

//    private void ViewPlanVendor(String APR,String RPR,String )

    private void RetriveDataPlanCaptain() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ConnectionSrever.viewplancaptain,
                response -> {
                    progressContract1.setVisibility(View.GONE);
                    linearProgress.setVisibility(View.GONE);
                    linearPackage.setVisibility(View.VISIBLE);
                    try {
                        ReadJSON_Captain(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("plan","plan");
                return  params;
            }
        };
        RequestHand.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void ReadJSON_Captain(String response) throws JSONException {
        JSONObject jsonObject=new JSONObject(response);
        if (jsonObject.getString("error").equals("false")){
            classPlanArrayList.clear();
            JSONArray jsonArray=jsonObject.getJSONArray("message");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object=jsonArray.getJSONObject(i);
                ClassPlan classPlan=new ClassPlan(
                        object.getString("plan_id"),object.getString("plan_name_en"),
                        object.getString("plan_name_ar"),object.getString("plan_logo"),
                        object.getString("plan_period"),object.getString("plan_price"),
                        object.getString("RPR"),object.getString("APR"),
                        object.getString("PO2H"),object.getString("PO4H"),
                        object.getString("PO12H"),object.getString("PO24H"),
                        object.getString("AP4H"),object.getString("AP8H"),
                        object.getString("AP24H"),object.getString("AP72H")
                );
                classPlanArrayList.add(classPlan);
            }
            PlanName_AR = new String[classPlanArrayList.size()];
            PlanName_EN = new String[classPlanArrayList.size()];

            for (int i = 0; i < classPlanArrayList.size(); i++) {
                PlanName_AR[i] = classPlanArrayList.get(i).getPlan_name_ar();
                PlanName_EN[i] = classPlanArrayList.get(i).getPlan_name_en();
            }
            if (Locale.getDefault().getLanguage().equals("en")) {
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, PlanName_EN);
                spinnerPackage.setAdapter(dataAdapter);
                dataAdapter.notifyDataSetChanged();
            } else if (Locale.getDefault().getLanguage().equals("ar")) {
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, PlanName_AR);
                spinnerPackage.setAdapter(dataAdapter);
                dataAdapter.notifyDataSetChanged();
            }
            spinnerPackage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ViewPlan(
                            classPlanArrayList.get(position).getAPR(),classPlanArrayList.get(position).getRPR(),
                            classPlanArrayList.get(position).getPlan_id(),classPlanArrayList.get(position).getPlan_logo(),
                            classPlanArrayList.get(position).getPlan_name_en(),classPlanArrayList.get(position).getPlan_name_ar(),
                            classPlanArrayList.get(position).getFree1(),classPlanArrayList.get(position).getFree2(),
                            classPlanArrayList.get(position).getFree3(),classPlanArrayList.get(position).getFree4(),

                            classPlanArrayList.get(position).getPush1(),classPlanArrayList.get(position).getPush2(),
                            classPlanArrayList.get(position).getPush3(),classPlanArrayList.get(position).getPush4(),

                            classPlanArrayList.get(position).getPlan_price(),classPlanArrayList.get(position).getPlan_period()

                    );
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }



    private Bitmap getBitmapFromURL(String URL) {
        try {
            java.net.URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) Objects.requireNonNull(getActivity())).SetNavItemChecked(6);
        ((MainActivity) Objects.requireNonNull(getActivity())).setToolbarVisibility(View.VISIBLE);
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
